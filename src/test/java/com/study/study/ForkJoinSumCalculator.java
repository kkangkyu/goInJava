package com.study.study;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

public class ForkJoinSumCalculator extends RecursiveTask<Long> {  // recusiveTask를 상속아서 구현

    public static final long THRESHOLD = 10_000;  //최소 서브태스크 갯수

    private final long[] numbers;  //더할 숫자 배열
    private final int start;   // 서브테스크에서 처리할 배열의 초기 위치와 최종위치
    private final int end;

    public ForkJoinSumCalculator(long[] numbers) {
        this(numbers, 0, numbers.length);
    }

    private ForkJoinSumCalculator(long[] numbers, int start, int end) {
        this.numbers = numbers;
        this.start = start;
        this.end = end;
    }

    /**
     * 1,2,3,4,5,6
     * 1,2,3 비동기, 4,5,6 동기처리
     */
    @Override  //RecursiveTask compute 구현
    protected Long compute() {
        int length = end - start;
        if (length <= THRESHOLD) {
            return computeSequentially();  // 서브태스트로 못 쪼개면 결과를 계산
        }
        ForkJoinSumCalculator leftTask = new ForkJoinSumCalculator(numbers, start, start + length/2);  // 왼쪽 절반 서브태스크 생성
        leftTask.fork();  // 새로 만든 서브탴스크를 비동기 실행 - 다른 쓰레드에게 시킴
        ForkJoinSumCalculator rightTask = new ForkJoinSumCalculator(numbers, start + length/2, end); //오른쪽 절반 서브태스크 생성
        Long rightResult = rightTask.compute();  // 동기 실행
        Long leftResult = leftTask.join(); // 왼쪽 비동기로 실행한 것이 끝나길 기다린다.
        return leftResult + rightResult;  // 두개 결과 합침
    }

    private long computeSequentially() {
        long sum = 0;
        for (int i = start; i < end; i++) {
            sum += numbers[i];
        }
        return sum;
    }

    public static long forkJoinSum(long n) {
        long[] numbers = LongStream.rangeClosed(1, n).toArray();
        ForkJoinTask<Long> task = new ForkJoinSumCalculator(numbers);
        return (new ForkJoinPool()).invoke(task);
    }
}
