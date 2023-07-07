/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etu1802.model;

import etu1802.framework.annotation.Url;

/**
 *
 * @author Safidy
 */
public class Users {
    String nom;

    public Users() {}
    
    public Users(String nom) {
        setNom(nom);
    }
    
    @Url("/prenom")
    public void getPrenom() {
        System.out.println("Safidy");
    }

    @Url("/nom")
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }

}
