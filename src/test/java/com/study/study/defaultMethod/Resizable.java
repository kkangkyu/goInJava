package com.study.study.defaultMethod;


    // API 버전1
    public interface Resizable {
        int getWidth();
        int getHeight();
        void setWidth(int width);
        void setHeight(int height);
        void setAbsoluteSize(int width, int height);
        default void setRelativeSize(int wFactor, int hFactor) {
            setAbsoluteSize(getWidth() / wFactor, getHeight() / hFactor);
        }
        //api 버전2 추가
        //에러
        //void setAutoSize(int width, int height);

        //정상
        default void setAutoSize(int width, int height){
        }

    }
