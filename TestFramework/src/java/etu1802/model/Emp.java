/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etu1802.model;

import etu1802.framework.FileUpload;
import etu1802.framework.ModelView;
import etu1802.framework.annotation.auth;
import etu1802.framework.annotation.restAPI;
import etu1802.framework.annotation.session;
import etu1802.framework.annotation.url;
import java.util.HashMap;

/**
 *
 * @author Safidy
 */
public class Emp {
    HashMap<String, Object> session = new HashMap<>();
    String firstname;
    String lastname;
    String[] loisir;
    FileUpload myfiles;
    String username;
    String mdp;

    public Emp() {
        System.out.println("New Instance Emp");
    }
    
    @session
    @url("/removing-session.action")
    public ModelView removeSession(String key) {
        ModelView mv = new ModelView("session.jsp");
        mv.addRemovingSession("test-session");
        System.out.println("SESSION->"+getSession());
        return mv;
    }
    
    @auth
    @url("/find-all.action")
    public ModelView findAll() {
        ModelView mv = new ModelView("list.jsp");
        mv.addItem("first_name", "John");
        mv.addItem("last_name", "Doe");
        return mv;
    }
    
    @url("/save.action")
    public ModelView save() {
        ModelView mv = new ModelView("list.jsp");
        mv.addItem("first_name", getFirstname());
        mv.addItem("last_name", getLastname());
        mv.addItem("loisir", getLoisir());
        return mv;
    }
    
    @auth("admin")
    @url("/parent.action")
    public ModelView parent(String dadname, String momname) {
        ModelView mv = new ModelView("list.jsp");
        mv.addItem("first_name", dadname);
        mv.addItem("last_name", momname);
        mv.addItem("loisir", getLoisir());
        return mv;
    }
    
    @url("/log.action")
    public ModelView logInput() {
        ModelView mv = new ModelView("log.jsp");
        return mv;
    }
    
    @url("/log-out.action")
    public ModelView logOut() {
        ModelView mv = new ModelView("log-out.jsp");
        mv.invalidateSession(true);
        return mv;
    }
    
    @session
    @url("/session.action")
    public ModelView testSession(String key, Object value) {
        ModelView mv = new ModelView("session.jsp");
        mv.addSession(key, value);
        System.out.println("SESSION->"+getSession());
        return mv;
    }
    
    @url("/json.action")
    public ModelView testJSON() {
        ModelView mv = new ModelView();
        mv.activeJSON();
        mv.addItem("name", "Safidy");
        mv.addItem("last_name", "Rabezatovo");
        return mv;
    }
    
    @restAPI
    @url("/apirest.action")
    public Object apiRest() {
        return this;
    }
    
    @url("/login.action")
    public ModelView logIn() {
        String username = getUsername();
        String mdp = getMdp();
        ModelView mv = new ModelView("connected.jsp");
        if (username.equals("admin") && mdp.equals("admin")) {
            mv.addSession("isconnected", true);
            mv.addSession("profile", "admin");
            mv.addItem("user", "ADMIN");
            return mv;
        }
        if (username.equals("user") && mdp.equals("user")) {
            mv.addSession("isconnected", true);
            mv.addSession("profile", "user");
            mv.addItem("user", "USER");
            return mv;
        }
        return new ModelView("non_connected.jsp");
    }
    
    
    
    @url("/input.action")
    public ModelView input() {
        ModelView mv = new ModelView("input.jsp");
        return mv;
    }
    
    

    public String[] getLoisir() {
        return loisir;
    }

    public void setLoisir(String[] loisir) {
        this.loisir = loisir;
    }
    
    

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public FileUpload getMyfiles() {
        return myfiles;
    }

    public void setMyfiles(FileUpload myfiles) {
        this.myfiles = myfiles;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getMdp() {
        return mdp;
    }
    
    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public HashMap<String, Object> getSession() {
        return session;
    }

    public void setSession(HashMap<String, Object> session) {
        this.session = session;
    }
    
    
    
    
}
