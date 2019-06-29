/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group1project;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gerardo
 */
public class Individual {
    String firstname, surname, image;
    List<Prizes> prizeList = new ArrayList<>();
    Personal per;
    
    Individual(){
        firstname = "";
        surname = "";
        per = new Personal();
    }
    Individual(String fname, String sname){
        firstname = fname;
        surname = sname;
        per = new Personal();
    }
    Individual(Individual ind){
        this.firstname = ind.firstname;
        this.surname = ind.surname;
        this.per = ind.per;
        this.image = ind.image;
    }
    
    
    public String shortShow(){
        String s="";
        s=s+firstname+" "+surname+"\n"+per.gender;
        return s;
    }

    //builds the image url
    public void buildImage(){
        String last;
        last = surname.toLowerCase();
        last = stripAccents(last);
        // if there is a last name enter the loop
        if(last != ""){
            // if there are brackets surrounding the words remove the brackets
            if(last.charAt(0) == '('){
                last = last.substring(1, last.length()-1);
            }
            //if there is a space enter getEnd method 
            if(last.contains(" ")){
                last = getEnd(last);
            }
            //if we are getting picutre of an organization enter 
            if(per.gender.equals("org")){
                image = null;
            } else {
                StringBuilder sb = new StringBuilder("https://www.nobelprize.org/nobel_prizes/");
                if(per.prizeList.get(0).category.equals("economics")){
                    sb.append("economic-sciences");
                } else {
                    sb.append(per.prizeList.get(0).category);
                }
                sb.append("/laureates/");
                sb.append(per.prizeList.get(0).year);
                sb.append("/");
                sb.append(last);
                sb.append("_postcard.jpg");
                image = sb.toString();
            }
        }
    }    
    //Strip accents from words 
    //https://stackoverflow.com/questions/15190656/easy-way-to-remove-accents-from-a-unicode-string
    public String stripAccents(String s){
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }
    
    //Add a prize to the prizeList
    public void addPrize(String year, String category, String share, String motivation, Affil aff){
        Prizes p = new Prizes(year, category, share, motivation, aff);
    }
    
    //get the last word from a string. if the last word is ends with r. then skip that word
    // because we are assuming it is jr. 
    public String getEnd(String s){
        StringBuilder sb = new StringBuilder();
        for(int i = s.length()-1; i >= 0; i--){
            if(s.charAt(i) == '.'){
                if(s.charAt(i-1) == 'r'){
                    i = i-4;
                }
            }
            if(s.charAt(i) == ' '){
                sb.reverse();
                return sb.toString();
            } else {
                sb.append(s.charAt(i));
            }
        }
        sb.reverse();
        return sb.toString();
    }
    @Override
    public String toString(){
        String tmp = "ID: " +per.id+"\nName: "+firstname+" "+surname+"\n"+per.toString()+image+"\n";
        return tmp;
    } 
    public String toStr(){
        String tmp = "ID: " +per.id+"\nName: "+firstname+" "+surname+"\n"+per.toString();
        return tmp;
    }
    
}
