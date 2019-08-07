package com.study.study.defaultMethod;


interface Rotatable {
    void setRotationAngle(int angleInDegrees);
    int getRotationAngle();
    default void rotateBy(int angle) { // rotateBy 메서드의 기본 구현
        setRotationAngle((getRotationAngle() + angle) % 360);
    }

    /**
    - Rotatable을 구현하는 모든 클래스는 setRotationAngle, getRotationAngle을 구현해야 함
    - rotateBy는 기본 구현이 제공되므로 따로 구현하지 않아도 됨
    **/
}