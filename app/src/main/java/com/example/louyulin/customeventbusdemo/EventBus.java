package com.example.louyulin.customeventbusdemo;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EventBus {

    private Map<Object, List<SubscribleMethod>> cacheMap;

    private Handler mHandler;

    private static volatile EventBus instance;

    private EventBus() {
        cacheMap = new HashMap<>();
        mHandler = new Handler();

    }

    public static EventBus getDefult() {
        if (instance == null) {
            synchronized (EventBus.class) {
                if (instance == null) {
                    instance = new EventBus();
                }
            }
        }

        return instance;
    }


    public void register(Object object) {
        List<SubscribleMethod> list =
                cacheMap.get(object);
        if (list == null) {
            list = findSubscribleMethods(object);
            cacheMap.put(object, list);
        }
    }

    private List<SubscribleMethod> findSubscribleMethods(Object object) {
        List<SubscribleMethod> list = new ArrayList<>();
        Class<?> clazz = object.getClass();
        //得到类里面所有的方法
        Method[] declaredMethods = clazz.getDeclaredMethods();

        while (clazz != null) {

            //找父类的时候需要判断是否是系统级别的父类
            String name = clazz.getName();
            if (name.startsWith("java.") || name.startsWith("javax.")
                    || name.startsWith("android.")) {
                break;
            }
            for (Method declaredMethod : declaredMethods) {
                //找到带有Subcribe注解的方法
                Subscrible subscrible = declaredMethod.getAnnotation(Subscrible.class);
                if (subscrible == null) {
                    continue;
                }
                //判断带有Subscribe的参数类型
                Class<?>[] types = declaredMethod.getParameterTypes();
                if (types.length != 1) {
                    Log.d("EventBus", "参数只能为1个");
                }
                ThreadMode threadMode = subscrible.threadMode();
                SubscribleMethod subscribleMethod = new SubscribleMethod(declaredMethod,
                        threadMode, types[0]);
                list.add(subscribleMethod);
            }

            clazz = clazz.getSuperclass();
        }


        return list;
    }

    public void post(final Object type) {

        Set<Object> set = cacheMap.keySet();
        Iterator<Object> iterator = set.iterator();
        while (iterator.hasNext()) {
            final Object object = iterator.next();
            List<SubscribleMethod> list = cacheMap.get(object);
            for (final SubscribleMethod subscribleMethod : list) {

                //if条件前面的对象是不是后面对象所对应的的类信息所对应的的父类或者接口
                if (subscribleMethod.getType()
                        .isAssignableFrom(type.getClass())) {
                    //当他们是同一个类型
                    switch (subscribleMethod.getThreadMode()) {
                        case MAIN:
                            if (Looper.myLooper()
                                    == Looper.getMainLooper()) {
                                //主到主
                                invoke(subscribleMethod, object, type);

                            } else {
//子到主
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        invoke(subscribleMethod, object, type);
                                    }
                                });
                            }
                            break;
                        case BACKGROUND:
                            if (Looper.myLooper()
                                    == Looper.getMainLooper()) {
                                //主到子
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        invoke(subscribleMethod, object, type);
                                    }
                                });

                            } else {
//子到子
                                invoke(subscribleMethod, object, type);
                            }
                            break;
                    }
                }
            }
        }

    }

    private void invoke(SubscribleMethod subscribleMethod, Object object, Object type) {
        Method method = subscribleMethod.getmMethod();
        try {
            method.invoke(object, type);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }




}
