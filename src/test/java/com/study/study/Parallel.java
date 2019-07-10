package com.study.study;

import org.junit.Test;

import java.util.Spliterator;
import java.util.function.Function;
import java.util.stream.LongStream;
import java.util.stream.Stream;
//
//public class Parallel {
//
//    /**
//     * 병렬 스트림
//     * 스레드에서 처리할수 있도록 스트림 요소를 여러 청크로 불할한 스트림이다.
//     *
//     * 병렬스트림으로 변환할떄 parallel()
//     * 순차스트림으로 변환할때 sequential()
//     *
//     * 병렬 스트림은 내부적으로 ForkJoinPool을 사용한다.
//     * ForkJoinPool 쓰레드 갯수는  프로세서 수
//     * 즉 runtime.getRunTime().availableProcessors 가 반환하는 값에 상은하는 스레드를 갖는다.
//     * 기본값을 그대로 사용하실 권장
//     *
//     * 성능 최적화 할때 3가지 황금규칙
//     * 첫쨰도 측정, 둘쨰도 측정 셋쨰도 측정...
//     */
//
//    public static <T, R> long measurePerf(Function<T, R> f, T input) {
//        long fastest = Long.MAX_VALUE;
//        for (int i = 0; i < 10; i++) {  // input을 10번 실행함.
//            long start = System.nanoTime();
//            R result = f.apply(input);
//            long duration = (System.nanoTime() - start) / 1_000_000;
//            System.out.println("Result: " + result);
//            if (duration < fastest) fastest = duration;
//        }
//        return fastest;
//    }
//
//    @Test
//    public void test(){
//
//
//        //for 사용
//        System.out.println("for Sum done in: " + measurePerf(this::iterativeSum, 10_000_000L) + " msecs");
//
//        //iterate 순차
//        System.out.println("Sequential Sum done in: " + measurePerf(this::sequentialSum, 10_000_000L) + " msecs");
//
//        //iterate 병렬
//        System.out.println("Parallel forkJoinSum done in: " + measurePerf(this::parallelSum, 10_000_000L) + " msecs" );
//
//        /**
//         * 실행 결과로 병렬이 가장 느림
//         *          이유
//         *          iterate가 방식된 객채를 생성하므로 다시 언박싱하는 과정이 필요
//         *          iterate는 이전 결과에 따라 다음 입력이 달라지기 때문에 청크 분할이 어려움
//         *          결과적으로 쓰레드만 할당하는 오버헤드만 증가
//         *
//         *          병렬이 만능은 아니다!!
//         */
//
//    }
//
//    public  long iterativeSum(long n) {
//        long result = 0;
//        for (long i = 0; i <= n; i++) {  //  1~ n 까지 더하기
//            result += i;
//        }
//        return result;
//    }
//
//    public  long sequentialSum(long n) {
//        return Stream.iterate(1L, i -> i + 1).limit(n).reduce(Long::sum).get();
//    }
//
//    public  long parallelSum(long n) {
//        return Stream.iterate(1L, i -> i + 1).limit(n).parallel().reduce(Long::sum).get();
//    }
//
//
//    @Test
//    public void 개선(){
//        // longStream르 사용해서 언박싱 오버헤드를 줄임
//        // rangeClosed를 사용해서 범위를 지정하여 청크 분할하기 쉽게 한다.
//
//        //LongStream.rangeClosed로 변경
//        System.out.println("Range forkJoinSum done in: " + measurePerf(this::rangedSum, 10_000_000L) + " msecs");
//
//        //LongStream.rangeClosed로 에 병렬 추가
//        System.out.println("Parallel range forkJoinSum done in: " + measurePerf(this::parallelRangedSum, 10_000_000L) + " msecs" );
//
//
//    }
//
//    public  long rangedSum(long n) {
//        return LongStream.rangeClosed(1, n).reduce(Long::sum).getAsLong();
//    }
//
//    public  long parallelRangedSum(long n) {
//        return LongStream.rangeClosed(1, n).parallel().reduce(Long::sum).getAsLong();
//    }
//
//    @Test
//    public void 올바른사용(){
//        /**
//         * 병렬화를 이용하려면 스트림을 재귀적으로 분할해야하고, 각 서브스트림을 서로 다른 스레드의 리듀싱 연산으로 할당하고, 결과를 하나로 합쳐야 함.
//         *          이 비용은 생각보다 비싸다.
//         *          병렬화를 올바르게 하는 지 항상 확인해야 한다.
//         *
//         *          병렬화를 사용할때 발생하는 실수
//         *          1.공유된 상태를 변경하려고 할때 ->공유가변 데이터를 피해야 함
//         */
//        System.out.println("SideEffect sum done in: " + measurePerf(this::sideEffectSum, 10_000_000L) + " msecs" );
//        System.out.println("SideEffect prallel sum done in: " + measurePerf(this::sideEffectParallelSum, 10_000_000L) + " msecs" );
//    }
//
//
//    public  long sideEffectSum(long n) {
//        Accumulator accumulator = new Accumulator();
//        LongStream.rangeClosed(1, n).forEach(accumulator::add);
//        return accumulator.total;
//    }
//
//    public  long sideEffectParallelSum(long n) {
//        Accumulator accumulator = new Accumulator();
//        LongStream.rangeClosed(1, n).parallel().forEach(accumulator::add);
//        return accumulator.total;
//    }
//
//    public  class Accumulator {
//        private long total = 0;
//
//        public void add(long value) {
//            total += value;  // total에 다수의 쓰레드가 접근하려고 함 -데이터 레이스 발생 -> 동기화 하다보면 순차실행과 다를 바가 없게된다.
//        }
//    }
//
//    /**
//     * 병렬 스트림 효과적으로 사용하기
//     * 1. 직접 속도 측정하기
//     * 2. 박싱을 주의하기 ( 기본형 특화 스트림 사용 하기 - IntStream, LongStream.. )
//     * 3. limit, findFirst 등 순서에 의존하는 연산은 병렬 처리 하지 말자
//     * 4. 스트림에서 수행하는 전체 파이프라인 연산 비용을 계산해보자 > 처리할 요소수가 N이면 하나처리하는 비용 Q , 전체스트림파이프라인처리비용 N*Q , Q가 클수록 병렬로 성능개선 가능성이 큼 -> IO 같이 오래 걸리는 작업은 병렬처리하기 적합?
//     * 5. 소량 데이터는 병렬 스트림이 도움 되지 않는다.
//     * 6. 스트림을 구성하는 자료구조거 적절한지 확인하라 - > arrayList가 LinkedList보다 병렬 적용하기 좋다. LinkedList는 탐색하는데 모든 요소를 탐색해야 한다.
//     * 7. 중간 연산의 특성에 따라 성능이 달라질수 있다. -> 크기가 명확하면 병렬이 쉽고, filter연산처럼 크기를 예측하기 힘든 중간 연산이 있으면 병렬처리가 어렵다.
//     * 8. 최종연산의 병합과정 비용이 비싸면 부분결과를 합치는 과정에서 병렬의 효과가 상쇄될수 있다.
//     * 9. arrayList , IntStream.range 적합. HashSet, TreeSet 좋음, LinkedList, Stream.Iterate 부적합
//     */
//
//    @Test
//    public void 포크조인프레임워크(){
//
//        /**
//         * 포크조인 프레임워크
//         * 병렬화 할수 있는 작업을 재귀적으로 작은 작업으로 분할 한 다음
//         * 서브태스크 각각의 결과를 합처 전체결과를 만들도록 설계되었다.
//         * 포크/조인 프레임워크에서는 서브태스크를 스레드풀의 작업자 스레드에 분산 할당 하는 ExcutorService 인터페이스를 구현한다.
//         *
//         *
//         */
//
//        System.out.println("ForkJoin sum done in: " + measurePerf(ForkJoinSumCalculator::forkJoinSum, 10_000_000L) + " msecs" );
//
//        /**
//         *
//         *
//         * 포크 조인 프레임워크를 제대로 사용하는 방법
//         * 1. join 메서드를 태스크에 호출하면 태스크가 생산하는 결과가 준비될 때 까지 호출자를 블록 시킨다. 따라서 두 서븤태스크가 모두 시작된 다음에 join을 호출해야 한다.
//         * 2. RecursiveTask내에서 ForkJoinPool의 invoke메소드를 사용하지 말아야 한다.
//         * 3. 서브태스크에 fork메서드를 호출해서 ForkJoinPool의 일정을 조절할수 있다. 왼쪽 장버과 오른쪽 작업 모두에 fork를 호출하는게 좋을것 같지만 한쪽은 compute를 호출하는것이 호율적이다. 그러면 한 테스크는 같은 쓰레드를 재사용할수 있다.
//         * 4. 포크 조인 프레임워크는 디버깅이 어렵다. fork가 다른 쓰레드를 만들어서 계산하므로 스텍 트레이스 문제가 도움이 도지 않는다.
//         * 5. 순차처리하는 것보다 무조건 빠를 거라는 생각을 버려라, 병렬은 포킹하는데 드는 시간 및 준비과정이 필요하다.
//         *
//         */
//
//        /**
//         * 작업 훔치기
//         * 실제로는 코어 개수와 관계없이 적절한 크기로 분할된 많은 태스크를 포킹하는 것이 바람직 하다.
//         * 포크조인 프레임 워크에서 작업 훔키기라는 기법은 모든 스레드를 거의 공정하게 분할한다.
//         * 각 스레디는 자신에게 할당 된 태스크를 포함하는 이중연결리스트를 참조하면서 작업이 끝날때마다 큐의 헤드에서 다른 태스크를 가져와 작업을 처리한다.
//         * 할일이 떨어지면 다른 쓰레드 큐으 ㅣ꼬리에저 ㅅ작업을 훔쳐온다.
//         * 모든 큐가 빌때까지 반복한다.
//         * 따라서 태스크 크기를 작게 나누어 작업자 간의 작업부하를 비슷한 수준으로 유지 할수 있다.
//         * 풀에 있는 작업자 스레드의 태스크를 재분배하고 균형을 맟출때 작업훔치키 알고리즘을 사용한다.
//         * 모든 태스크가 공정할떄까지 재귀적으로 반복한다.
//         *
//         */
//
//        /**
//         * 스플리터레이터
//         * 자동으로 스트림을 분할 해주는 기능
//         * 분할 할수 있는 반복자라는 의미
//         * 이터레이터처럼 소스의 요소탬색 기능을 제공한다는 점은 같지만 스플리터레이터는 병렬 작업에 특화되어 있다.
//         *
//         */
//        Spliterator s ;
//
//        /**
//         *  tryAdvance()
//         *  스플리터레이터의 요소를 하나씩 순차적으로 소비하면서 탐색해야할 요소가 남아있으면 참을 반환한다.
//         *
//         *  trySplit ()
//         *  Spliterator의 일부요소를 분할해서 두번째 Spliterator를 생성하는 메서드이다.
//         */
//
//        /**
//         * 분할 과정 247페이지
//         * Spliterator가 trySplit를 호출-> Spliterator 가 생김.
//         * 모든 Spliterator이 trySplit를 호출
//         * 1 -> 2->4 ->8 식으로 Spliterator가 생겨남.
//         * trySplit이 null이 될때 까지 반복함.
//         */
//
//    }
//
//}
