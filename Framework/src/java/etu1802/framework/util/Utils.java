/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu1802.framework.util;

import etu1802.framework.Mapping;
import etu1802.framework.annotation.url;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author safidy
 */
public class Utils {
    
    public static List<Class<?>> getClassesFromPath(String path) throws Exception {
        List<Class<?>> classes = new ArrayList<>();
        File dir = new File(path);
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException(path + " is not a directory");
        }
        URLClassLoader classLoader = new URLClassLoader(new URL[]{dir.toURI().toURL()});
        for (File file : dir.listFiles()) {
            if (file.getName().endsWith(".class")) {
                String className = file.getName().substring(0, file.getName().length() - 6);
                Class<?> clazz = classLoader.loadClass(className);
                classes.add(clazz);
            }
        }
        return classes;
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
    
}
