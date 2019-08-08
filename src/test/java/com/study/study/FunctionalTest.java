package com.study.study;

import org.junit.Test;

import java.util.stream.LongStream;

public class FunctionalTest {

/***

������ : ���(how) �� �����ϴ� ���α׷��� ����
       ���� ��ü�� �ڵ�� ��Ȯ�ϰ� �巯���ٴ� ���� ������ ���α׷����� �����̴�.
��Ʈ�� API : ���ιݺ� ����ڴ� what(����)�� ����

�Լ��� ���α׷��̶� ? �Լ��� 0�� �̻��� �μ��� ������ �Ѱ� �̻��� ����� ��ȯ������ ���ۿ��� ������Ѵ�.

�Լ����ڹ� : �ڹٿ����� �Ϻ��� ���� �Լ��� ���α׷��� �����ϱ� ��ƴ�.
     ���� ���ۿ��� ������, �ƹ��� �̸� ���� ���ϰ� �����ν� �Լ����� �޼��Ҽ� �ִ�.
     - �Լ��� �޼���� ���� �������� �����ؾ� �Լ����̶� �� �� ����
     - �Լ��� �޼��忡�� �����ϴ� ��ü�� �ִٸ� �� ��ü�� �Ҹ� ��ü������
     - ��, ��ü�� ��� �ʵ尡 final�̿����ϰ�, ��� ���� �ʵ�� �Һ� ��ü�� ���� �����ؾ���
     - ���������� �޼��峻���� ������ ��ü�� �ʵ�� ������ �� ����
     - �Լ��� �޼��尡 � ���ܵ� ����Ű�� �ʾƾ���

�������� : ���ۿ��� ������Ѵ� ��� ������ ���� ���� �������� �Ͱ�ȴ�.
     �� ���� �μ��� �Լ��� ȣ�������� �׻� ���� ����� ��ȯ�Ѵ�. (=���������� ������ �Լ�)

��ü���� ���α׷��� �Լ��� ���α׷� :
     �ڹ�8�� �Լ��� ���α׷��� ��ü���� ���α׷��� �������ΰ����Ѵ�. (p400)
*/

    @Test
    public void test_404_13_1(){
        //�ݺ���������丮�� :�Źݺ����� ���� r�� i�� ���ŵȴ� (�ݺ�)
        System.out.println("�ݺ���������丮�� : " + factoriallIterative(5));
        //��Ʈ�� ���丮 : ����ڵ尡 �� ��δ� factorialRecursive�Լ��� ȣ���Ҷ����� ȣ�� ���ÿ� ������ ������ ���ο� ���� �������� �����. �� �޸� ����
        System.out.println("��Ʈ�����丮�� : " + factorialStreams(5));
        //����������丮�� : ��ʹ� ������°��� �ƴϴ�, �Լ��� ������ ���� ȣ�� ����ȭ��� �ذ�å�� �����Ѵ�.
        System.out.println("����������丮�� : " + factorialTailRecursive(5));
        //���ذ��ȵȴ�...���� �����غ��ô�

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
