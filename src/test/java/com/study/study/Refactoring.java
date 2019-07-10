package com.study.study;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.internal.util.collections.ListUtil.filter;

public class Refactoring {

    /// 참고... 현재 테스트 버전이 낮아서 그런지...테스트 돌릴때 한글이 지원 안되요 참고하세요~~~
    /// junit 5 부터만 한글 버전 지원 가능


    ///P.262
    // 콘텍스트 오버로딩에 따른 모호함 초래 // 이해안됨....
    interface Task {
        public void execute();
    }
    public static void doSomething(Runnable r) { r.run(); }
    public static void doSomething(Task a) { a.execute(); }  ///책 오타~! 참고 r이 아니라 a 여야함. 받는 객체가 다름


    //-------------8.1.2 익명클래스를 람다 표현식으로 리팩토링
    @Test
    public void anonymous1(){
        // 클래스명 extends 상속받을 클래스 구조로 사용하는데 클래스명이 존재 하지 않고
        // 무명의 어떠한 클래스가 부모클래스로 부터 상속을 받은 인스턴스 인 것이다.
        // 즉 이름이 없으므로 생성자를 선언 할 수도 없는것이다. (https://mommoo.tistory.com/16)

        // 익명 클래스
        Runnable r1 = new Runnable(){
            @Override
            public void run() {
                System.out.println("Hello_1");
            }
        };

        // 람다 표현식
        Runnable r2 = () -> System.out.println("Hello_2");
        r1.run();
        r2.run();
    }

    @Test
    public void anonymous2() {
        // 익명 클래스는 감싸고 있는 클래스의 변수를 가릴 수 있음(섀도 변수)
        // 그러나 람다 표현식은 변수를 가질 수 없음

        // 람다 표현식 (컴파일 에러)
        int a = 10;
        Runnable r3 = () -> {
            //int a = 2; // 에러
            System.out.println(a);
        };

        // 익명 클래스 (정상)
        Runnable r4 = new Runnable() {
            int a = 2;

            @Override
            public void run() {
                System.out.println(a);
            }
        };

        r3.run();
        r4.run();


        doSomething(new Task() {
            public void execute() {
                System.out.println("Danger danger!!");
            }
        });

        // 람다 표현식
        // doSomething(() -> System.out.println("Danger danger!!")); // 컴파일 에러
        doSomething((Task) () -> System.out.println("Danger danger!!")); // 명시적 형변환으로 모호함 제거
        //메서드 안에 넣어주세요
        //책에서는 람다표현식을 오버로딩해서 쓰면 문제라하지만 명시적 형변환도 오류를 뱉어냄... 이게 맞나...?

    }
        //------------- 8.1.3 람다표현식을 메서드 레퍼런스로 리팩토링하기
        // 4장 DISH 예제
        List<Dish> menu = Arrays.asList(
            new Dish("pork", false, 800, Dish.Type.MEAT),
            new Dish("beef", false, 700, Dish.Type.MEAT),
            new Dish("chicken", false, 400, Dish.Type.MEAT),
            new Dish("french fries", true, 530, Dish.Type.OTHER),
            new Dish("rice", true, 350, Dish.Type.OTHER),
            new Dish("season fruit", true, 120, Dish.Type.OTHER),
            new Dish("pizza", true, 550, Dish.Type.FISH),
            new Dish("prawns", false, 300, Dish.Type.FISH),
            new Dish("salmon", false, 450, Dish.Type.FISH));

        // 람다 표현식을 메서드 레퍼런스로 리펙토링 하기
        // step1. 원래의 표현식
        Map<CaloricLevel, List<Dish>> dishesByCaloricLevel1 =
            menu.stream()
                .collect(
                    groupingBy(dish -> {
                        if (dish.getCalories() <= 400)
                            return CaloricLevel.DIET;
                        else if (dish.getCalories() <= 700)
                            return CaloricLevel.NORMAL;
                        else
                            return CaloricLevel.FAT;
                    }));

        // step2. Dish클래스에 getCaloricLevel을 추가.

        // step3. 아래처럼 메서드 레퍼런스로 대신 할 수 있음.
        Map<CaloricLevel, List<Dish>> dishesByCaloricLevel2 =
            menu.stream().collect(groupingBy(Dish::getCaloricLevel));

        //이외에 우리가 배웠던 comparing과 maxBy 같은 정적 헬퍼 메서드도 사용 가능
        //comparing은 두객체 비교, maxBy 스트림의 최대값 추출 했던것...
        //sum, maximum 등 자주 사용하는 리듀싱 연산은 메서드 레퍼런스와 함께 사용할 수 있는 내장 헬퍼 메서드 제공

    @Test
    public void reduce() {
        // 저수준 리듀싱 연산을 조합
        int totalCalories1 = menu.stream().map(Dish::getCalories).reduce(0, (c1, c2) -> c1 + c2);
        // 내장 컬렉터를 이용하면 코드 자체로 문제를 더 명확하게 설명 가능!
        // 어떤 동작을 하는지 메서드 이름으로 설명
        int totalCalories2 = menu.stream().collect(summingInt(Dish::getCalories));

        System.out.println(totalCalories1);
        System.out.println(totalCalories2);
    }

    //------------- 8.1.4 명령형 데이터 처리를 스트림으로 리팩토링하기
    @Test
    public void streamApi() {
        // 기존 코드 : 전체 구현을 살펴봐야 의도 파악 가능, 병렬로 실행시키기 어려움
        List<String> dishNames1 = new ArrayList<>();
        for ( Dish dish : menu ) {
            if ( dish.getCalories() > 300 ) {
                dishNames1.add(dish.getName());
            }
        }

        // 스트림 API 이용 : 문제를 직접적으로 기술, 쉬운 병렬화 가능
        List<String> dishNames2 = menu.parallelStream().filter(d -> d.getCalories() > 300)
                                                       .map(Dish::getName)
                                                       .collect(Collectors.toList());

        System.out.println(dishNames1);
        System.out.println(dishNames2);
    }

    //------------- 8.1.5 코드 유연성 개선
    // 이론 적으로~! 함수형 인터페이스 적용 필요 하고, 람다 표현식을 사용하려면 함수형 인터페이스가 필요하다~! 참고!

    // 아래 코드들은 동작하지 않음;;; 메서드를 짜고해야하는데 소스만 넣음 에러남...개념으로 이해 부탁해요 :)
    // 내장 자바 Logger 크래스를 사용하는 예제
    // 1. if문을 사용한 예
//    if (logger.isLoggable(Level.FINER)) {
//        logger.finer("Problem : " + generateDiagnostic());
//    }
//    /*
//     * 문제점
//      - logger의 상태가 isLoggerable이라는 메서드에 의해 클라이언트 코드로 노출됨
//      - 메시지를 로깅할 때마다 logger 객체의 상태를 매번 확인해야 하는가? -> 코드를 어지럽힘
//    */
//
//    // 2. 불필요한 if문 제거, logger의 상태 노출 필요 없으므로 더 바람직해 짐
//    logger.log(Level.FINER, "Problem : " + generateDiagnostic());
//    /*
//     * 문제점
//      - 인수로 전달된 메시지 수준에서 logger가 활성화돼있지 않더라도 항상 로깅 메시지 평가하게 됨
//      -> 람다로 해결 가능(특정 조건에서만 메시지가 생성될 수 있도록 메시지 생성 과정을 연기 시킴)
//    */
//
//    // 3. 자바8 API에 새롭게 추가된 log 메서드 사용
//    public void log(Level level, Supplier<String> msgSupplier) {
//        if ( logger.isLoggable(level) ) {
//            log(level, msgSupplier.get()); // 람다 실행
//        }
//    }

    /*
     * 해결
      - 클라이언트 코드에서 객체 상태를 자주 확인하거나, 일부 메서드를 호출하는 상황이라면
        내부적으로 객체의 상태를 확인 후 메서드를 호출하도록 새 메서드 구현하는 것이 좋음
      - 가독성 향상 + 캡슐화 강화(객체 상태가 클라이언트 코드로 노출 X)
    */



    // 실행 어라운드 3장에서 했던 소스로~!
//    String oneLine = processFile((BufferedReader b) -> b.readLine()); //람다 전달
//    String twoLine = processFile((BufferedReader b) -> b.readLine() + b.readLine()); //다른 람다로 전달 가능
    //위의 구문이 주석 풀면 에러 나는 것은 exception 처리가 안되있어서....ㅠㅠ

    public static String processFile(BufferedReaderProcessor p) throws IOException {
        try(BufferedReader br = new BufferedReader(new FileReader("java8inaction/chap8/data.txt"))){
            return p.process(br); // 인수로 전달된 BufferedReaderProcessor를 실행
        }
    }

    public interface BufferedReaderProcessor {  // IOException을 던질 수 있는 람다의 함수형 인터페이스
        String process(BufferedReader b) throws IOException;
    }

    ////////////람다로 객체지향 디자인 패턴 리펙토링
    //------------- 8.2.1 전략패턴 : 한 유형의 알고리즘을 보유한 상태에서 런타임에 적절한 알고리즘을 선택하는 기법

    // 1. String 문자열을 검증하는 인터페이스 구현
    public interface ValidationStrategy {
        boolean execute(String s);
    }

    // 2. 1에서 정의한 인터페이스를 구현하는 클래스 정의 (일반적 사용 시)
    public class IsAllLowerCase implements ValidationStrategy {
        @Override
        public boolean execute(String s) {
            return s.matches("[a-z]+");
        }
    }

    public class IsNumeric implements ValidationStrategy {
        @Override
        public boolean execute(String s) {
            return s.matches("\\d+");
        }
    }

    // 3. 사용
    public class Validator {
        private final ValidationStrategy strategy;

        public Validator(ValidationStrategy v) {
            this.strategy = v;
        }

        public boolean validate(String s) {
            return strategy.execute(s);
        }

        // 일반적인 사용
        Validator numericValidator = new Validator(new IsNumeric());
        boolean b1 = numericValidator.validate("aaa");

        Validator lowerCaseValidator = new Validator(new IsAllLowerCase());
        boolean b2 = lowerCaseValidator.validate("bbbb");

        // 람다 표현식 사용
        Validator numericValidator2
                    = new Validator((String s) -> s.matches("[a-z]+")); //람다를 통해 직접 전달~!!
        boolean b3 = numericValidator2.validate("aaa");

        Validator lowerCaseValidator2
                    = new Validator((String s) -> s.matches("\\d+"));   //람다를 통해 직접 전달~!!
        boolean b4 = lowerCaseValidator2.validate("bbb");

        //위에서 본것 처럼 람다표현식을 사용하게 되면, 자잘한 코드가 없어짐 위에서 메서드를 만든 것을 생략가능~!
    }

    //------------- 8.2.2 템플릿 메서드 : 알고리즘 개요를 제시 후 알고리즘의 일부를 고칠 수 있는 유연함을 제공해야 할 때 사용
    // 간단한 온라인 뱅킹 어플 구현. 사용자 고객ID를 입력하면 디비에서 고객 정보가져오고 서비스를 제공하는것.

//    // 일반적인 표현
//    abstract class OnlineBanking {
//        public void processCustomer(int id) { // 온라인 뱅킹 알고리즘이 해야할 일 보여줌
//            Customer c = Database.getCustomerWithId(id);
//            makeCustomerHappy(c);
//        }
//        abstract void makeCustomerHappy(Customer c);
//    }
//
//    // 람다 표현식 사용 (Consumer<Customer> makeCustomerHappy 시그니처를 사용하여...)
//    public void processCustomer(int id, Consumer<Customer> makeCustomerHappy) {
//        Customer c = Database.getCustomerWithId(id);
//        makeCustomerHappy.accept(c);
//    }
//
//    // 사용은 아래와 같이....(위의 표현이 간단하게 사용된다 클래스를 상속 받지 않고 사용)
//    new OnlineBankingLambda().processCustomer(1337, (Customer c) ->
//            System.out.println("Hello " + c.getName());


    //------------- 8.2.3 옵저버 : 어떤 이벤트가 발생했을 때 한 객체(주제_subject)가
    // 다른 객체 리스트(옵저버_observer)에 자동으로 알림을 보내야 하는 상황에 사용
    // 예를들어 옵저버 패턴이 머냥? 신문매체가 뉴스 트윗으로 구독한 사람한테 키워드가 포함된 것을 등록하면 알림을 받게 하는 것

    // 일반적인 예
    @Test
    public void observerEx() {
        Feed f = new Feed();
        f.registerObserver(new NYTimes());
        f.registerObserver(new Guardian());
        f.registerObserver(new LeMonde());
        f.notifyObservers("The queen said her favourite book is Java 8 in Action");
        f.notifyObservers("queen wine");


        // 람다 표현식 사용 예시. 아래처럼 클래스를 만들고 호출 하는 것은 안해도 됨~!
        f.registerObserver((String tweet) -> {
            if ( tweet != null && tweet.contains("money") ) {
                System.out.println("----------Breaking news in NY! " + tweet);
            }
        });

        f.registerObserver((String tweet) -> {
            if ( tweet != null && tweet.contains("queen") ) {
                System.out.println(">>>>>>>>>>Yet another news in London... " + tweet);
            }
        });
    }

    interface Observer {
        void notify(String tweet);
    }

    class NYTimes implements Observer {
        @Override
        public void notify(String tweet) {
            if ( tweet != null && tweet.contains("money") ) {
                System.out.println("Breaking news in NY! " + tweet);
            }
        }
    }

    class Guardian implements Observer {
        @Override
        public void notify(String tweet) {
            if ( tweet != null && tweet.contains("queen") ) {
                System.out.println("Yet another news in London... " + tweet);
            }
        }
    }

    class LeMonde implements Observer {
        @Override
        public void notify(String tweet) {
            if ( tweet != null && tweet.contains("wine") ) {
                System.out.println("Today cheese, wine and news! " + tweet);
            }
        }
    }

    interface Subject {
        void registerObserver(Observer o);
        void notifyObservers(String tweet);
    }

    class Feed implements Subject {
        private final List<Observer> observers = new ArrayList<>();

        @Override
        public void registerObserver(Observer o) {
            this.observers.add(o);
        }

        @Override
        public void notifyObservers(String tweet) {
            observers.forEach(o -> o.notify(tweet));
        }
    }

    /*
      - 항상 람다 표현식을 사용해야 된다? -> X
      - 위 예제는 실행 동작이 비교적 간단하므로 람다 표현식 바람직
      - 옵저버가 상태를 가지고 여러 메서드를 정의한다면 기존의 클래스 구현 방식이 유리할 수 도 있음
    */


    //------------- 8.2.4 의무체인 : 한 객체가 어떤 작업을 처리한 다음 다른 객체로 결과 전달,
    //   다른 객체도 작업 처리 후 또 다른 객체로 전달하는 방식

    // 일반적인 사용 예제
    @Test
    public void chain() {
        ProcessingObject<String> p1 = new HeaderTextProcessing();
        ProcessingObject<String> p2 = new SpellcheckerProcessing();

        p1.setSuccessor(p2);

        String result1 = p1.handle("Aren't labdas really sexy?");
        System.out.println(result1);


        // 람다 표현식 사용
        UnaryOperator<String> headerProcessing = // 첫 번째 작업처리 객체
            (String text) -> ">>>>From Raoul, Mario and Alan : " + text;

        UnaryOperator<String> spellCheckProcessing = // 두 번째 작업처리 객체
            (String text) -> text.replaceAll("labda", "lambda");

        Function<String, String> pipeline = // 동작 체인으로 두 함수 조합
            headerProcessing.andThen(spellCheckProcessing);

        String result2 = pipeline.apply("Aren't labdas really sexy?");
        System.out.println(result2);
    }

    public abstract class ProcessingObject<T> {
        protected ProcessingObject<T> successor;

        public void setSuccessor(ProcessingObject<T> successor) {
            this.successor = successor;
        }

        public T handle(T input) {
            T r = handleWork(input);
            if ( successor != null )
                return successor.handle(r);
            return r;
        }

        abstract protected T handleWork(T input);
    }

    public class HeaderTextProcessing extends ProcessingObject<String> {
        @Override
        public String handleWork(String text) {
            return "From Raoul, Mario and Alan : " + text;
        }
    }

    public class SpellcheckerProcessing extends ProcessingObject<String> {
        @Override
        protected String handleWork(String text) {
            return text.replaceAll("labda", "lambda");
        }
    }

    //------------- 8.2.5 팩토리 : 인스턴스화 로직을 클라이언트에 노출하지 않고 객체를 만들 때 사용   ??????????????????????

    // 일반적인 팩토리 패턴
    Product p = ProductFactory.createProduct("loan");

    // Loan, Stock, Bond 는 Product의 서브형식
    // createProduct 메서드는 생산된 상품을 설정하는 로직 포함 가능
    // 생성자와 설정을 외부로 노출하지 않음으로써 클라이언트가 단순하게 상품을 생산할 수 있다!!!

    // 람다 표현식 사용
    Supplier<Product> loanSupplier = ProductFactory.Loan::new;
    Product loan = loanSupplier.get();

    final static Map<String, Supplier<Product>> map = new HashMap<>();
    static {
        map.put("loan", ProductFactory.Loan::new);
        map.put("stock", ProductFactory.Stock::new);
        map.put("bond", ProductFactory.Bond::new);
    }

    public static Product createProduct(String name) {
        Supplier<Product> p = map.get(name);
        if ( p != null )
            return p.get();
        throw new IllegalArgumentException("No such product " + name);
    }

    ////////////람다 테스팅
    //------------- 8.3
    public static class Point {
        private final int x;
        private final int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public Point moveRightBy(int x) {
            return new Point(this.x + x, this.y);
        }
    }

    // 단위 테스트 예제
    @Test
    public void testMoveRightBy() throws Exception {
        Point p1 = new Point(5, 5);
        Point p2 = p1.moveRightBy(10);

        assertEquals(15, p2.getX());
        assertEquals(5, p2.getY());
    }

    //------------- 8.3.1 보이는 람다 표현식의 동작 테스팅
    // 람다는 익명이므로 테스트 코드 이름을 호출할 수 없다.
    // 따라서 필요하다면 람다를 필드에 저장해서 재사용할 수 있으며 람다의 로직을 테스트할 수 있다.
    // -> 메서드를 호출하는 것처럼 람다를 사용할 수 있음

    // Point class 에 추가
    public final static Comparator<Point> compareByXAndThenY =
        Comparator.comparing(Point::getX).thenComparing(Point::getY);

    // 람다 표현식은 함수형 인터페이스의 인스턴스 생성 -> 생성된 인스턴스의 동작으로 람다 표현식 테스트 가능
    @Test
    public void testComparingTwoPoints() throws Exception {
        Point p1 = new Point(10, 15);
        Point p2 = new Point(10, 20);
        int result = compareByXAndThenY.compare(p1, p2);
        assertEquals(-1, result);
    }

    //------------- 8.3.2 람다를 사용하는 메서드의 동작에 집중하라
    // Point class 추가
    public static List<Point> moveAllPointRightBy(List<Point> points, int x) {
        return points.stream()
            .map(p -> new Point(p.getX(), p.getY()))
            .collect(Collectors.toList());
    }

    // 테스트
    @Test
    public void testMoveAllPointRightBy() throws Exception {
        List<Point> points = Arrays.asList(new Point(5, 5), new Point(10, 5));
        List<Point> expectedPoints = Arrays.asList(new Point(15, 5), new Point(20, 5));
        List<Point> newPoints = moveAllPointRightBy(points, 10);
        assertNotEquals(expectedPoints, newPoints);
    }

    //------------- 8.3.3 복잡한 람다를 개별 메서드로 분할 책으로~!!!
    //------------- 8.3.4 고차원 함수 테스팅
    @Test
    public void testFilter() throws Exception {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4);
        List<Integer> even = filter(numbers, i -> i % 2 == 0);
        List<Integer> smallerThanThree = filter(numbers, i -> i < 3);

        assertEquals(Arrays.asList(2, 4), even);
        assertEquals(Arrays.asList(1, 2), smallerThanThree);
    }

    //------------- 8.4. 디버깅
    @Test
    public void test1(){
        List<Point> points = Arrays.asList(new Point(12, 2), null);
        // 1) 알아보기 힘든 트레이스 1
        points.stream().map(p -> p.getX()).forEach(System.out::println);
    }

    @Test
    public void test2(){
        List<Point> points = Arrays.asList(new Point(12, 2), null);
        // 2) 알아보기 힘든 트레이스 2
        points.stream().map(Point::getX).forEach(System.out::println);

    }

    // 제일 알아보기 쉬운 람다로 디버깅하는 방법, 하지만 여기서는 Debuggin이 선언 안되어 책으로 봐주세요~!P283
    public void test3(){
//        List<Integer> numbers = Arrays.asList(1, 2, 3);
//        numbers.stream().map(Debugging::divideByZero).forEach(System.out::println);
    }
}
