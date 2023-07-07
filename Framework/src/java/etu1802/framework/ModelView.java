/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etu1802.framework;

import java.util.HashMap;

/**
 *
 * @author Safidy
 */
public class ModelView {
    String view;
    HashMap<String, Object> data;

    public ModelView(String view) {
        setView(view);
        setData(new HashMap<String, Object>());
    }
    
    
    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }
    
    public void addItem(String key, Object value) {
        getData().put(key, value);
    }
}
