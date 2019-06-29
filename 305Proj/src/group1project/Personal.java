/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group1project;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gerardo
 */
public class Personal {
    String id, born, bornCountry, bornCountryCode, bornCity,
            died, diedCountry, diedCountryCode, diedCity, gender;
    List<Prizes> prizeList;
    
    //for initial declaration in elements object
    Personal(){
        id = null;
        born = null;
        bornCountry = null;
        bornCountryCode = null;
        bornCity = null;
        died = null;
        diedCountry = null;
        diedCountryCode = null;
        diedCity = null;
        gender = null;
        prizeList = new ArrayList<>();
    }
    
    Personal(Personal per){
        this.id = per.id;
        this.born = per.born;
        this.bornCountry = per.bornCountry;
        this.bornCountryCode = per.bornCountryCode;
        this.bornCity = per.bornCity;
        this.died = per.died;
        this.diedCountry = per.diedCountry;
        this.diedCountryCode = per.diedCountryCode;
        this.diedCity = per.diedCity;
        this.gender = per.gender;
        this.prizeList = new ArrayList<>();
        for(Prizes i:per.prizeList){
            this.prizeList.add(new Prizes(i));
        }
    }
    
    //for adding elements later
    Personal(String id, String born, String bornCountry, String bornCountryCode, 
            String bornCity, String died, String diedCountry, String diedCountryCode, 
            String diedCity, String gender,String image, Prizes p){
        this.id = id;
        this.born = born;
        this.bornCountry = bornCountry;
        this.bornCountryCode = bornCountryCode;
        this.bornCity = bornCity;
        this.died = died;
        this.diedCountry = diedCountry;
        this.diedCountryCode = diedCountryCode;
        this.diedCity = diedCity;
        this.gender = gender;
        prizeList = new ArrayList<>();
        addPrizeList(p);
    }

    public void addPrizeList(Prizes p){
        prizeList.add(p);
    }
    @Override
    public String toString(){
        String tmp;
        if(gender.equals("org")){
            tmp = prizeList.toString(); 
        } else if (died == null){
            tmp = "birth: "+born+" in "+bornCountry+" "+ bornCity
                    +"\nGender: " + gender+"\n"+prizeList.toString(); 
        } else {
            tmp = "birth: "+born+" in "+bornCountry+" "+ bornCity+"\ndeath: "
                    +died+" in "+diedCountry+" "+ diedCity+"\nGender: " 
                    + gender+"\n"+prizeList.toString();
        }
            
       return tmp;
    }
}
