/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu2025.framework.util;

import etu2025.framework.Mapping;
import etu2025.framework.annotation.scope;
import etu2025.framework.annotation.url;
import jakarta.servlet.http.Part;
import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author tiavi
 */
public class Utils {

    
    
    public static boolean isSingleton(Class<?> c) {
        scope sc = c.getAnnotation(scope.class);
        return sc != null && "singleton".equals(sc.value());
    }
    
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
            url[] a = method.getAnnotationsByType(url.class);
            if (a.length > 0) {
                mappingUrls.put(a[0].value(), new Mapping(c.getSimpleName(), method.getName()));
            }
        }
        return mappingUrls;
    }
    
    public static HashMap<String, Method> getAllSetters(Class<?> c) throws NoSuchMethodException {
        HashMap<String, Method> setters = new HashMap<>();
        for (Field f : c.getDeclaredFields()) {
            setters.put(f.getName(), getSetter(c, f));
        }
        return setters;
    }
    
    public static Method getSetter(Class<?> c, Field f) throws NoSuchMethodException {
        String setter_query = getSetterQuery(f);
        Method setter = c.getDeclaredMethod(setter_query, f.getType());
        return setter;
    }
    
    public static String getSetterQuery(Field f) {
        String field_name = f.getName();
        String t1 = field_name.substring(0, 1);
        String t2 = field_name.substring(1);
        t1 = t1.toUpperCase();
        return "set" + t1 + t2;
    }
    
    public static <T> T cast(String init, Class<?> type) throws Exception {
        if (type.isArray()) {
            throw new Exception("String can't be cast to be an array");
        }
        T result = null;
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public static <T> T cast(String[] init, Class<?> type) throws Exception {
        T result = null;
        if (type.isArray()) {
            Class<?> type_array = type.getComponentType();
            if (init==null) {
                result = (T) Array.newInstance(type_array, 0);
            } else {
                Object[] result_array = (Object[]) Array.newInstance(type_array, init.length);
                for (int i=0; i<init.length; i++) {
                    result_array[i] = cast(init[i], type_array);
                }
                result = (T) result_array;
            }
        } else {
            if (init==null) {
                result = null;
            } else {
                result = (T) cast(init[0], type);
            }
        }
        return result;
    }
    
    
}
