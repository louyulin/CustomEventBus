package com.example.louyulin.customeventbusdemo;

import java.lang.reflect.Method;

public  class SubscribleMethod {

    //回调方法
    private Method mMethod;
    //线程模式
    private ThreadMode threadMode;
    //方法的参数
    private Class<?> type;

    public SubscribleMethod(Method mMethod, ThreadMode threadMode, Class<?> type) {
        this.mMethod = mMethod;
        this.threadMode = threadMode;
        this.type = type;
    }

    public Method getmMethod() {
        return mMethod;
    }

    public void setmMethod(Method mMethod) {
        this.mMethod = mMethod;
    }

    public ThreadMode getThreadMode() {
        return threadMode;
    }

    public void setThreadMode(ThreadMode threadMode) {
        this.threadMode = threadMode;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }
}
