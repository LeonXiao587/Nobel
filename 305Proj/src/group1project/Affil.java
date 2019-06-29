/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group1project;

/**
 *
 * @author Gerardo
 */
public class Affil {
    String name, city, country;
    
    Affil(){
        name = null;
        city = null;
        country = null;
    }
    
    Affil(Affil iffy){
        this.name = iffy.name;
        this.city = iffy.city;
        this.country = iffy.country;
    }
    
    Affil(String name, String city, String country){
        this.name = name;
        this.city = city;
        this.country = country;

    }
    @Override
    public String toString(){
       String tmp = "Affiliation: " 
               +"\n\tname: "+name+"\n\tcity: "+city+"\n\tcountry: "+country;
       return tmp;
    }
}