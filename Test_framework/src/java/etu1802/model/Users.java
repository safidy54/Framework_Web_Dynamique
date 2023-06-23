/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etu1802.model;

import etu1802.framework.ModelView;
import etu1802.framework.annotation.Url;
import java.text.ParseException;
import java.util.Date;

/**
 *
 * @author Safidy
 */
public class Users {
    String nom;
    int age;
    String prenom;
    Date dtn;
    
    public Users() {}
    
    public Users(String nom) {
        setNom(nom);
    }
    
    @Url("/prenom")
    public ModelView getPrenom() {
        ModelView mv = new ModelView("index.jsp");
        mv.addItem("nom", "Rabe");
        mv.addItem("prenom", "Safidy");
        return mv;
    }
    @Url("/find-all")
    public ModelView findAll() throws ParseException {
        ModelView mv = new ModelView("index.jsp");
        mv.addItem("nom", "Rah");
        mv.addItem("prenom", "Safidy");
        mv.addItem("age", 18);
        return mv;
    }

    @Url("/input")
    public ModelView InputSave() {
        ModelView mv = new ModelView("input.jsp");
        return mv;
    }

     @Url("/save")
    public ModelView save() {
        ModelView mv = new ModelView("index.jsp");
        mv.addItem("nom", getNom());
        mv.addItem("prenom", getPrenom());
        mv.addItem("age", getAge());
        mv.addItem("dtn", getDtn());
        return mv;
    }
    
    @Url("/nom")
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }


    public void setAge(Integer age) {
        this.age = age;
    }
    
    public Date getDtn() {
        return dtn;
    }

    public void setDtn(Date dtn) {
        this.dtn = dtn;
    }

    public String str() {
        return "Personne{nom="+getNom()+"prenom"+getPrenom()+"}";
    }
}
