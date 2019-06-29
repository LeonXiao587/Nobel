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
public class Prizes {
    String year, category, share, motivation;
    List <Affil> affiliation;

    Prizes(){
        year = null;
        category = null;
        share = null;
        motivation = null;
        affiliation = new ArrayList<>();
    } 
    
    Prizes(Prizes prz){
        this.year = prz.year;
        this.category = prz.category;
        this.share = prz.share;
        this.motivation = prz.motivation;
        this.affiliation = prz.affiliation;
    }
    
    Prizes(String year, String category, String share, String motivation, Affil aff){
        this.year = year;
        this.category = category;
        this.share = share;
        this.motivation = motivation;
        affiliation = new ArrayList<>();
        affiliation.add(aff);
    }
    @Override
    public String toString(){
        String tmp = "Year: "+year+"\ncategory: "+category+"\nshare: "+share
                +"\nmotivation: "+motivation+"\n"+affiliation.toString()+"\n";
        
        return tmp;
    }
}