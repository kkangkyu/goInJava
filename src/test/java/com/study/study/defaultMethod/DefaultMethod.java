package com.study.study.defaultMethod;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class DefaultMethod {

    /******* 맛보기 *********/
    @Test
    public void test_288(){
       //"디폴트 메소드 : 인터페이스에 신규 메소드를 모든 구현클래스 수정없이 추가하고자 할 때 사용한다."
        List<Integer> numbers = Arrays.asList(3, 5, 1, 2, 6);
        numbers.sort(Comparator.naturalOrder());
        //sort 는 List의 디폴트 메소드 이다.
    }



    public class List2 implements List{

        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @Override
        public Iterator iterator() {
            return null;
        }

        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @Override
        public boolean add(Object o) {
            return false;
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean addAll(Collection c) {
            return false;
        }

        @Override
        public boolean addAll(int index, Collection c) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public Object get(int index) {
            return null;
        }

        @Override
        public Object set(int index, Object element) {
            return null;
        }

        @Override
        public void add(int index, Object element) {

        }

        @Override
        public Object remove(int index) {
            return null;
        }


        @Override
        public int lastIndexOf(Object o) {
            return 0;
        }

        @Override
        public ListIterator listIterator() {
            return null;
        }

        @Override
        public ListIterator listIterator(int index) {
            return null;
        }

        @Override
        public List subList(int fromIndex, int toIndex) {
            return null;
        }

        @Override
        public boolean retainAll(Collection c) {
            return false;
        }

        @Override
        public boolean removeAll(Collection c) {
            return false;
        }

        @Override
        public boolean containsAll(Collection c) {
            return false;
        }

        @Override
        public Object[] toArray(Object[] a) {
            return new Object[0];
        }

        //사용자 구현에는 없는 상태에서 api에 추가 되었다고 가정하자!!
        @Override
        public int indexOf(Object o) {
            return 0;
        }
    }
    /******* 맛보기 예제 ******/
    @Test
    public void test_user(){

        List2 list = new List2();

        list.sort(Comparator.naturalOrder()); //디폴트 메소드로 구현을 고칠필요가 없다.

        list.indexOf(list); // indexOf가 만약 List에 신규 추가되었고, 구현 영역에 없으면 오류가 발생
    }

    /***************
     * 1. API가 바뀌면서 생기는 문제
     * 2. 디폴트 메서드란 무엇이며, API가 바뀌면서 발생한 문제를 어떻게 해결하는가
     * 3. 디폴트 메서드 활용패턴 (디폴트 메서드를 만들어 다중상속을 달성하는 방법)
     * 4. 시그너처를 갖는 여러 디폴트 메소를 상속받으면서 발생하는 모호성 문제를 자바 컴파일러가 어떻게 해결하는가
     ****************/


    @Test

    /** 1. API가 바뀌며서 생기는 문제 **/
    public void test_user_291(){

        Monitor m = new Monitor();
        m.setHeight(500);
        m.setWidth(100);

        m.setAutoSize(500, 100); //에러
        //m.setAutoSize2(500, 100); //정상
    }

    /** 2. 디폴트 메서드란 무엇이며, API가 바뀌면서 발생한 문제를 어떻게 해결하는가 **/

    /**
    2_1. 디폴트메서드 : 호환성을 유지하면서 API를 바꿀있도록함
    2_2. 누가 구현하나 ? 인터페이스를 구현하는 클래스에서 구현하지 않는 메서드는  인터페이스 자체에서 기본으로 제공한다
    2_3 API가 바뀌면서 발생한 문제를 어떻게해결하는가 ?
    Resizable 인터페이스를 구현하는 모든 클레스(Monitor)는 setAutoSize의 구현도 상속받는다.
    **/

    /** 3. 디폴트 메서드를 만들어 다중상속을 달성하는 방법 **/
    @Test
    public void test_297_9_3_1() {
        Iterator iterator = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        //remove 기능은 잘 사용하지 않으므로, remove기능을 무시했다.(빈구현을 제공)
        //Iterator 에서 default 메소드를 제공하여 필요없는 코드 줄임

    }



    /**
     * * 위의 ArrayList는 한 개의 클래스(AbstractList)를 상속받고,
     *     여섯 개의 인터페이스를 구현.
     *     -> List, RandomAccess, Cloneable, Serializable, Iterable, Collection의 서브형식이 된다
     ** 자바 8에서는 인터페이스가 구현을 포함할 수 있으므로 클래스는 여러 인터페이스에서 동작을
     *     상속받을 수 있다.
     */
    @Test
    public void test_298_9_3(){
        ArrayList arrayList = new ArrayList(){

        };
    }

    /**인터페이스 조합**/
    @Test
    public void test_300(){
        Monster m = new Monster();
        m.rotateBy(180);  // default 메서드이기 때문에 구현 없이 바로 사용 가능
        m.moveVertically(10);


        /** 옳지 못한 상속
         한 개의 메서드를 재사용하기위해 100개의 메서드가 정의된 클래스를 상속받는 것은 좋지 않음
         이러한 경우에는 델리게이션(delegation, 멤버 변수를 이용해 클래스에서 필요한 메서드를 직접
         호출하는 메서드를 작성하는 것)이 좋다
         **/
    }

    /**해석규칙

       1. 클래스가 항상 이긴다
       2. 1번외 서브인터페이스가 이긴다 (B가 A를 상속받는다면, B가 이긴다)
       3. 디폴트 메서드의 우선순위가 결정안되면, 여러 인터페이스를 상속받은 클래스가 명시적으로 디폴트 메서드를 오버라이드하고 호출해야함
    **/
    // 1. 다음 예제에서 클래스 C는 누구의 hello 메서드를 호출할까?
    interface A {
        default void hello() {
            System.out.println("Hello from A");
        }
    }

    interface B extends A {
        default void hello() {
            System.out.println("Hello from B");
        }
    }

    public class C implements B, A {

    }

    @Test
    public void test_303_9_4_2(){
       C c = new C();
       c.hello();
       // 실행 결과
       // Hello from B
       //2번 규칙에 따라 서브인터페이스가 이김
       //B가 A를 상속받았으므로 컴파일러는 B의 hello를 선택함
    }


    // 1. 다음 예제에서 클래스 C는 누구의 hello 메서드를 호출할까?
    interface AA {
        default void hello() {
            System.out.println("Hello from AA");
        }
    }

    interface BB extends AA {
        default void hello() {
            System.out.println("Hello from BB");
        }
    }

    class DD implements AA {
        /*
        public void hello() {
            System.out.println("Hello from DD");
        }
        */
    }

    public class CC extends DD implements BB, AA {

    }

    @Test
    public void test_304_9_2(){
        CC c = new CC();
        c.hello();
        /***
         - 1번 규칙 : 클래스의 메서드 구현이 이김
         * DD는 hello를 오버라이드하지 않았고 단순히 인터페이스 A를 구현
         * 따라서 DD는 인터페이스 AA의 디폴트 메서드 구현을 상속받음
         - 2번 규칙 : 클래스나 슈퍼클래스에 메서드 정의가 없을 때 디폴트 메서드를 정의하는 서브인터페이스 선택됨
         * 컴파일러는 인터페이스 AA나 BB의 hello 둘 중 하나를 선택해야 함
         * 위의 예제에서는 BB가 AA를 상속받는 관계이므로 BB의 hello 출력!
         *
         *
         * BUT DD가 hello를 오버라이드하여 구현하였다면 1번규칙에 의해 DD가 출력!!!!
         ****/
    }

    interface AAA {
        default void hello() {
            System.out.println("Hello from AAA");
        }
    }

    interface BBB {
        default void hello() {
            System.out.println("Hello from BBB");
        }
    }
    public class CCC implements BBB, AAA {
       public void hello(){
            BBB.super.hello();
        }

        //BBB가 AAA를 상속받지 않으면.. CCC에서 명시적으로 선택

    }

    @Test
    public void test_305_9_4_3(){
        CCC c = new CCC();
        c.hello();

    }

    /** 다이아몬드 문제***/

    public class D1 implements B1, C1 {

    }

    interface A1 {
        default void hello() {
            System.out.println("hello from A1");
        }
    }
    interface B1 extends A1{}
    interface C1 extends A1{}

    @Test
    public void test_306_9_4_4(){
        D1 d = new D1();
        d.hello();

        /**
         * 실제로 선택할 수 있는 메서드 선언은 하나뿐 -> 결국 출력은 A
         */
    }


    /** 다이아몬드 예외 문제***/

    public class D2 implements B2, C2 {

        //명시적으로 선택
        @Override
        public void hello() {
            System.out.println("hello from D2");
        }
    }

    interface A2 {
        default void hello() {
            System.out.println("hello from A1");
        }
    }
    interface B2 extends A2 {}
    interface C2 extends A2 {
        void hello();
    }

    @Test
    public void test_306_9_4_4_1(){
        D2 d = new D2();
        d.hello();

        /**
         *  C2는 A2를 상속받으므로 추상 메서드 hello가 A2의 디폴트 메서드 hello보다 우선권 가짐
         *  컴파일 에러 발생, 클래스 D가 어떤 hello를 사용할 지 명시적으로 선택해야 함
         */
    }

    /**
     * 총정리(요약)
     * 1. 자바 8의 인터페이스는 구현 코드를 포함하는 디폴트 메서드, 정적 메서드를 정의할 수 있다.
     * 2. 디폴트 메서드의 정의는 default 키워드로 시작하며, 바디를 갖는다.
     * 3. 공개된 인터페이스에 추상 메서드를 추가하면 소스 호환성이 깨진다.
     * 4. 디폴트 메서드를 이용하면 설계자가 API를 수정해도 기존 버전과 호환성을 유지할 수 있다.
     * 5. 선택형 메서드와 동작 다중 상속에도 디폴트 메서드를 사용할 수 있다.
     * 6. 클래스가 같은 시그니처를 갖는 여러 디폴트 메서드를 상속하면서 생기는 충돌 문제를 해결하는 규칙이 있다.
     * 7. 클래스나 슈퍼클래스에 정의된 메서드가 다른 디폴트 메서드 정의보다 우선한다.
     *    이 외의 상황에서는 서브인터페이스에서 제공하는 디폴트 메서드가 선택된다.
     * 8. 두 메서드의 시그니처가 같고, 상속관계로도 충돌 문제를 해결할 수 없을 때는 디폴트 메서드를 사용하는 클래스에서 메서드를 오버라이드해서 어떤 디폴트 메서드를 호출할지 명시적으로 결정해야 한다.
     */
}

