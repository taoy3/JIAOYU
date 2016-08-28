package com.taoy3.freight.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by taoy2 on 15-11-2.
 */
public class Messaging{
    private static final String METHOD = "onEvent";
    private static ArrayList<Object> objects = new ArrayList<>();

    public static void register(Object object) {
        objects.add(object);
    }

    public static void unregister(Object object) {
        if (objects.contains(object))
        objects.remove(object);
    }

    public static void post(String msg,int who) {
        synchronized (Messaging.class) {
            for (int i = 0; i < objects.size(); i++) {
                try {
                    Method method = objects.get(i).getClass().getMethod(METHOD, String.class,Integer.class);
                    if (method != null) {
                        try {
                            method.invoke(objects.get(i), msg,who);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            System.out.println("此处接收被调用方法内部未被捕获的异常");
                            Throwable t = e.getTargetException();// 获取目标异常
                            t.printStackTrace();
                        }
                    }
                } catch (NoSuchMethodException e) {
//                    e.printStackTrace();
                }
            }
        }
    }
    public static final int PORTEVENT = 1;
    public static final int SHIPCOMPANY = 2;
    public static final int OK = 3;
    public static void post(int event,int status) {
        synchronized (Messaging.class) {
            for (int i = 0; i < objects.size(); i++) {
                try {
                    Method method = objects.get(i).getClass().getMethod(METHOD,Integer.class,Integer.class);
                    if (method != null) {
                        try {
                            method.invoke(objects.get(i),event,status);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            System.out.println("此处接收被调用方法内部未被捕获的异常");
                            Throwable t = e.getTargetException();// 获取目标异常
                            t.printStackTrace();
                        }
                    }
                } catch (NoSuchMethodException e) {
//                    e.printStackTrace();
                }
            }
        }
    }
}
