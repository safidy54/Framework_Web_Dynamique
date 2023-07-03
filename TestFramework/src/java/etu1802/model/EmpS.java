/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etu1802.model;

import etu1802.framework.FileUpload;
import etu1802.framework.ModelView;
import etu1802.framework.annotation.scope;
import etu1802.framework.annotation.url;

/**
 *
 * @author Safidy
 */
@scope("singleton")
public class EmpS {
    String firstname;
    String lastname;
    FileUpload myfiles;

    public EmpS() {
        System.out.println("New instance Emps");
    }

    @url("/find-allS.action")
    public ModelView findAll() {
        ModelView mv = new ModelView("list.jsp");
        mv.addItem("first_name", "John");
        mv.addItem("last_name", "Doe");
        return mv;
    }
    
    @url("/saveS.action")
    public ModelView save() {
        ModelView mv = new ModelView("list.jsp");
        mv.addItem("first_name", getFirstname());
        mv.addItem("last_name", getLastname());
        return mv;
    }
    
    @url("/parentS.action")
    public ModelView parent(String dadname, String momname) {
        ModelView mv = new ModelView("list.jsp");
        mv.addItem("first_name", dadname);
        mv.addItem("last_name", momname);
        return mv;
    }
    
    
    
    @url("/inputS.action")
    public ModelView input() {
        ModelView mv = new ModelView("input.jsp");
        return mv;
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
    
}
