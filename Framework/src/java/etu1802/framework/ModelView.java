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
    private HashMap<String, Object> session = new HashMap<>();

    public void addSession(String key, Object value) {
        getSession().put(key, value);
    }
    
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
    
    public HashMap<String, Object> getSession() {
        return this.session;
    }

    public void setSession(HashMap<String, Object> session) {
        this.session = session;
    }
    
    
    
    
}
