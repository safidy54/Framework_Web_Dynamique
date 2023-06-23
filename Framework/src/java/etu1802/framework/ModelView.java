/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu1802.framework;

import java.util.HashMap;

/**
 *
 * @author safidy
 */
public class ModelView {
    private String view;
    private HashMap<String, Object> data = new HashMap<>();

    public void addItem(String key, Object value) {
        getData().put(key, value);
    }
    
    public ModelView(String view) {
        setView(view);
    }
    public ModelView() {
        
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }
    
    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }
    
    
}
