/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu1802.model;

import etu1802.framework.annotation.url;

/**
 *
 * @author safidy
 */
public class User {
    private String nom;

    public User() {}

    public User(String nom) {
        this.nom = nom;
    }

    @url("/nom")
    public String getNom() {
        System.out.println("Safidy");
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    
}
