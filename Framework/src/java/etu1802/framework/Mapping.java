/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu1802.framework;

/**
 *
 * @author safidy
 */
public class Mapping {
    private String className;
    private String method;

    public Mapping(String className, String method) {
        this.className = className;
        this.method = method;
    }

    public String getClassName() {
        return className;
    }

    public void setClassname(String classname) {
        this.className = classname;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
