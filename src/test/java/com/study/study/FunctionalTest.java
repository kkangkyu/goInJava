package com.study.study;

import org.junit.Test;

import java.util.stream.LongStream;

public class FunctionalTest {

/***

선언형 : 어떻게(how) 에 집중하는 프로그래밍 형식
       문제 자체가 코드로 명확하게 드러난다는 점이 선언형 프로그래밍의 장점이다.
스트림 API : 내부반복 사용자는 what(무엇)에 집중

함수형 프로그램이란 ? 함수는 0개 이상의 인수를 가지며 한개 이상의 결과를 반환하지만 부작용이 없어야한다.

함수형자바 : 자바에서는 완벽한 순수 함수형 프로그램을 구현하기 어렵다.
     실제 부작용이 있지만, 아무도 이를 보지 못하게 함으로써 함수형을 달성할수 있다.
     - 함수나 메서드는 지역 변수만을 변경해야 함수형이라 할 수 있음
     - 함수나 메서드에서 참조하는 객체가 있다면 그 객체는 불면 객체여야함
     - 즉, 객체의 모든 필드가 final이여야하고, 모든 참조 필드는 불변 객체를 직접 참조해야함
     - 예외적으로 메서드내에서 생성한 객체의 필드는 갱신할 수 있음
     - 함수나 메서드가 어떤 예외도 일으키지 않아야함

참조투명성 : 부작용을 감춰야한다 라는 제약은 참조 투명성 개념으로 귀결된다.
     즉 같은 인수로 함수를 호출했을때 항상 같은 결과를 반환한다. (=참조적으로 투명한 함수)

객체지향 프로그램과 함수형 프로그램 :
     자바8은 함수형 프로그램을 객체지향 프로그램의 일종으로간주한다. (p400)
*/

    @Test
    public void test_404_13_1(){
        //반복방식의팩토리얼 :매반복마다 변수 r과 i가 갱신된다 (반복)
        System.out.println("반복방식의팩토리얼 : " + factoriallIterative(5));
        //스트림 팩토리 : 재귀코드가 더 비싸다 factorialRecursive함수를 호출할때마다 호출 스택에 정보를 저장할 새로운 스택 프레임을 만든다. 즉 메모리 증가
        System.out.println("스트림팩토리얼 : " + factorialStreams(5));
        //꼬리재귀팩토리얼 : 재귀는 쓸모없는것이 아니다, 함수형 언어에서는 꼬리 호출 최적화라는 해결책을 제공한다.
        System.out.println("꼬리재귀팩토리얼 : " + factorialTailRecursive(5));
        //이해가안된다...같이 이해해봅시다

    }
    static int factoriallIterative(int n){
        int r=1;

        for (int i = 1; i <= n; i++){
            r *= i;
            System.out.println(r);
        }
        return r;
    }

    static long factorialStreams(long n){
        return LongStream.rangeClosed(1,n)
                .reduce(1,(long a, long b)->a*b);
    }

    int factorialTailRecursive(int n){
        return factorialHelper(1, n);
    }

    int factorialHelper(int acc, int n){
        return n == 1 ? acc : factorialHelper(acc * n, n - 1);
    }




}
