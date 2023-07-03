/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etu1802.framework.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author Safidy
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface scope {
    String value() default "normal";
}
