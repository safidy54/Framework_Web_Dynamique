/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etu1802.framework.utils;

import etu1802.framework.Mapping;
import etu1802.framework.annotation.Url;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Safidy
 */
public class Util {
    public static List<Class> getClassFrom(String packages) throws Exception {
        String path = packages.replaceAll("[.]", "/");
        URL packagesUrl = Thread.currentThread().getContextClassLoader().getResource(path);
        File packDir = new File(packagesUrl.toURI());
        File[] inside = packDir.listFiles();
        List<Class> lists = new ArrayList<>();
        for (File f : inside) {
            String c = packages + "." + f.getName().substring(0, f.getName().lastIndexOf("."));
            lists.add(Class.forName(c));
        }
        return lists;
    }
    
    public static HashMap<String, Mapping> getMappingUrls(Class<?> c) {
        HashMap<String, Mapping> mappingUrls = new HashMap<>();
        Method[] methods = c.getDeclaredMethods();
        for (Method method : methods) {
            Url[] a = method.getAnnotationsByType(Url.class);
            if (a.length > 0) {
                mappingUrls.put(a[0].value(), new Mapping(c.getSimpleName(), method.getName()));
            }
        }
        return mappingUrls;
    }
    
     public static HashMap<String, Method> getSetters(Class<?> c) {
        HashMap<String, Method> setter = new HashMap<>();
        Field[] fields = c.getDeclaredFields();
        Method[] methods = c.getDeclaredMethods();
        for (Field field : fields) {
            for (Method method : methods) {
                if ( method.getName().equals(getSetterQuery(field.getName()))) {
                    setter.put(field.getName(), method);
                }
            }
        }
        return setter;
    }

    public static String getSetterQuery(String method) {
        String t1 = method.substring(0, 1);
        String t2 = method.substring(1);
        t1 = t1.toUpperCase();
        return "set" + t1 + t2;
    }

    public static <T extends Object> T CastTo(String init) throws ParseException {
        T result = null;
        if(result instanceof Date) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            result = (T) format.parse(init);
        } else if (result instanceof Integer) {
            result = (T) Integer.valueOf(init);
        } else if (result instanceof Float) {
            result = (T) Float.valueOf(init);
        } else if (result instanceof Double) {
            result = (T) Double.valueOf(init);
        } else {
            result = (T) init;
        }
        return result;
    }

    public static <T extends Object> T CastTo(String init, Class<?> type) throws ParseException {
        T result = null;
        if(type == Date.class) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            result = (T) format.parse(init);
        } else if (type == Integer.class) {
            result = (T) Integer.valueOf(init);
        } else if (type == Float.class) {
            result = (T) Float.valueOf(init);
        } else if (type == Double.class) {
            result = (T) Double.valueOf(init);
        } else {
            result = (T) init;
        }
        return result;
    }
}
