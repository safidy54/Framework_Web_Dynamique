/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etu1802.framework;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Safidy
 */
public class ModelView {
    private String view;
    private HashMap<String, Object> data = new HashMap<>();
    private HashMap<String, Object> session = new HashMap<>();
    private boolean JSON = false;
    private boolean invalidateSession = false;
    private ArrayList<String> removingSession = new ArrayList<>();

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

    public boolean isJSON() {
        return JSON;
    }

    public void setJSON(boolean JSON) {
        this.JSON = JSON;
    }
 
    public void activeJSON() {
        setJSON(true);
    }
    
    public boolean getInvalidateSession() {
        return this.invalidateSession;
    }
    
    public void invalidateSession(boolean value) {
        this.invalidateSession = value;
    }
    
    public ArrayList<String> getRemovingSession() {
        return this.removingSession;
    }
    
    public void setRemovingSession(ArrayList<String> value) {
        this.removingSession = value;
    }
    
    public void addRemovingSession(String session_name) {
        this.removingSession.add(session_name);
    }
}

