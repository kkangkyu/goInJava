package com.study.study;

import org.junit.Test;

import java.util.Optional;

import static com.study.study.Refactoring.map;

public class OptionalStudy {

    //10.1 클래스 실행시에 null
    @Test
    public void getCarInsuranceName() {
        Person person = new Person();
        String a =  person.getCar().getInsurance().getName();
    }
    // getCarInsuranceName 메서드 실행 시 Person의 Car가 비어있다면 NullPointerException 발생

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 안전시도 1) 깊은 의심
    @Test
    public void getCarInsuranceName1() {
        Person person = new Person();
        String AA = "";
        if ( person != null ) {
            Car car = person.getCar();
            if ( car != null ) {
                Insurance insurance = car.getInsurance();
                if ( insurance != null ) {
                    AA = insurance.getName();
                }
            }
        }
        AA = "Unknown";
    }
    // null 확인 코드 때문에 나머지 호출 체인의 들여쓰기 수준이 증가함
    // 이와 같은 반복 패턴 코드를 '깊은 의심(deep doubt)' 이라고 부름

    // 안전시도 2) 너무 많은 출구
    @Test
    public void getCarInsuranceName2() {
        Person person = new Person();
        String BB = "";
        if( person == null ) {
            BB = "Unknown";
        }
        Car car = person.getCar();
        if ( car == null ) {
            BB = "Unknown";
        }
        Insurance insurance = car.getInsurance();
        if ( insurance == null ) {
            BB = "Unknown";
        }
    }
    // null 확인 코드마다 출구가 생김
    // 많은 출구 때문에 유지보수가 어려워짐

    //책 315 페이지 요약 한번 보기
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //OptionalStudy : 선택형값을 캡슐화하는 클래스
    // 예) 차를 소유하고 있지 않은 Person 클래스의 car 변수는 null을 가져야 함
    //   -> 새로운 Optional을 이용하면 null을 할당하는 것 대신 변수 형식을 Optional<Car>로 설정함.
    //      값이 있다면 Optioanl 클래스는 값을 감싸고, 값이 없으면 Optional.empty 메서드로 Optional 반환
    //- Optional.empty는 Optional의 특별한 싱글턴 인스턴스를 반환하는 정적 팩토리 메서드
    //- null 레퍼런스와 OptionalStudy.empty()
    // * null을 참조하려 하면 NullPointerException 발생
    // * Optional.empty()는 Optional 객체이므로 이를 다양한 방식으로 활용 가능
    // * null 대신 Optional을 사용하면서 Car 형식이 Optional<Car>로 바뀜
    //    -> 값이 없을 수 있음을 명시적으로 표현

    //10.2 Optional 클래스 소개
    public class Person1 {
        private Optional<Car1> car; // 사람이 차를 소유했을 수도 하지 않았을 수도 있으므로 Optional
        public Optional<Car1> getCar() { return car; }
    }
    public class Car1 {
        private Optional<Insurance1> insurance; // 자동차 보험에 가입 or 미가입일수 있으므로
        public Optional<Insurance1> getInsurance() { return insurance; }
    }
    public class Insurance1 {
        private String name; // 보험회사에는 반드시 이름이 존재함
        public String getName() { return name; }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //10.3 Optional 적용 패턴
    //10.3.1 Optional 객체 만들기
    @Test
    public void OptionalPattern() {
        //Optional.empty() : 빈 Optional 객체 생성한다
        //Optional.of(value) : value값이 null이 아닌 경우에 사용한다
        //Optional.ofNullable(value) : value값이 null인지 아닌지 확실하지 않은 경우에 사용한다

        Optional<String> optStr = Optional.empty(); //Optional.empty()는 empty Optional 객체를 생성

        String str = "test";
        Optional<String> optStr1 = Optional.of(str);    //Optional.of()는 null이 아닌 객체를 담고 있는 Optional 객체를 생성

        String nullStr = null;
        //Optional<String> optStr2 = Optional.of(nullStr); //null이 아닌 객체를 생성하기 때문에 null을 넘겨서 생성하면 NPE이 발생

        Optional<String> optStr3 = Optional.ofNullable(str);
        Optional<String> optStr4 = Optional.ofNullable(null); //empty Optional 객체를 반환함
    }

    //10.3.2 맵으로 Optional의 값을 추출하고 변환하기
    // 이름 정보에 접근하기 전에 insurance가 null인지 확인
    @Test
    public void getOptionalMap(){
        String name = null;
        Insurance insurance = new Insurance();
        if ( insurance != null ) {
            name = insurance.getName();
            System.out.println(name);
        }

        // Optional의 map 메서드 지원
        Optional<Insurance> optInsurance = Optional.ofNullable(insurance);
        Optional<String> name1 = optInsurance.map(Insurance::getName);

        System.out.println(name1);
    /*
     - Optional 객체를 최대 요소의 개수가 한 개 이하인 데이터 컬렉션으로 생각할 수 있다.
     - Optional이 값을 포함하면 map의 인수로 제공된 함수가 값을 바꾼다.
     - Optional이 비어있으면 아무 일도 일어나지 않는다. --> 중요~!
    */
    }

    //10.3.3 flatMap으로 Optional 객체 연결
    @Test
    public void getFlatMap(){
//        Person person = new Person();
//        Optional<Person> optPerson = Optional.of(person);
//        Optional<String> name = optPerson.map(Person::getCar)
//                                         .map(Car::getInsurance)  // 컴파일 에러!!
//                                         .map(Insurance::getName);
    /*
     - 에러 원인
      optPerson의 타입은 Optional<Person> -> map 메서드 호출 가능
      getCar는 Optional<Car> 반환
      -> map 메서드의 결과는 Optional<Optional<Car>> 형식의 중첩 Optional 객체 구조
      -> 해결하기 위해서 flatMap 메서드 사용! (스트림의 flatMap과 유사한 기능)
         함수를 적용해서 생성된 모든 스트림을 하나의 스트림으로 병합하여 평준화시킴
    */
    }

    //P.323
    //이거는 소스는 이해가는데 왜 에러가는지 이해가 안됨...=-=
//    public String getCarInsuranceName(Optional<Person> person) {
//        return person.flatMap(Person::getCar) // Optional<Person>를 Optional<Car>로 반환
//                     .flatMap(Car::getInsurance) // Optional<Car>를 Optional<Insurance>로 반환
//                     .map(Insurance::getName) // getName은 String을 반환하므로 flatMap 필요 X
//                     .orElse("Unknown"); // 결과 Optional이 비어있으면 기본값 사용
//    }
     //null을 확인하려고 조건 분기문을 추가해서 코드가 복잡해지는 현상 막을 수 있음

    //P.324 그림 보고 이해

    // 직렬화 모델이 필요할 때
    // Optional로 값을 반환받을 수 있는 메서드를 추가하는 방식 권장 아래와 같이 써주세요~! : )
    public class Person2 {
        private Car car;
        public Optional<Car> getCarAsOptional() {
            return Optional.ofNullable(car);
        }
    }

    //10.4 Optional 예제
    @Test
    public void exampleOptional() {
        // Map<String, Object> 형식의 맵에서 다음과 같이 key로 값에 접근할 때

        Object value = map.get("key");
        // key 에 해당하는 값이 없으면 null 반환

        // 방법1) 기존의 if-then-else 추가
        // 방법2) Optional.ofNullable 이용
        Optional<Object> value1 = Optional.ofNullable(map.get("key"));  //잠재적으로 null이 될 수 있는 대상을 Optional로 생성
    }

    // - 자바 API는 어떤 이유에서 값을 제공할 수 없을 때 null을 반환하는 대신 예외를 발생시킬 때 있음
    // - 이에 대한 예로 문자열을 정수로 변환하는 Integer.parseInt(String)등이 있음
    public static Optional<Integer> stringToInt(String s) {
        try {
            return Optional.of(Integer.parseInt(s)); // 변환 가능 시 변환된 값 포함하는 Optional 반환
        } catch (NumberFormatException e) {
            return Optional.empty(); // 그 외에는 빈 Optional 반환
        }
    }

}
