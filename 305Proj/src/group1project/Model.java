/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group1project;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Joel
 */
public class Model {
    private List<Individual> listofpeople = new ArrayList<>();

    public Model() throws IOException{
        readJSON();
    }
    
    private void readJSON() throws IOException {
        URL oracle = new URL("http://api.nobelprize.org/v1/laureate.json"); //database
        URLConnection yc = oracle.openConnection();
        InputStream is = yc.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        String jsonString = null;
        jsonString = in.readLine();
        
        decodeJSON(jsonString, listofpeople); //get data from the jsonString
        
        in.close(); //close the reader
        //System.out.print(listofpeople);
        //download all the pictures 
        if(folderIsEmpty()){
            for(int i = 0; i < listofpeople.size(); i++){
                Individual tmpIndividual = new Individual();
                String url;
                try{
                    tmpIndividual = listofpeople.get(i);
                    if (tmpIndividual.per.gender.equals("org")){
                        url = "https://cdn2.iconfinder.com/data/icons/basics-vol-1/100/question_answer_inquiry_query_invastigation_poll-256.png";
                        
                    } else{
                        url = tmpIndividual.image;
                    }
                    getImage(url, tmpIndividual);
                } catch(FileNotFoundException e ) {
                    tmpIndividual.image = null;
                    url  = "http://cdn.onlinewebfonts.com/svg/img_346711.png";
                    getImage(url, tmpIndividual);
                    System.out.println("URL WRONG FOR: "+ tmpIndividual.firstname + " "+tmpIndividual.surname);
                } catch(java.io.IOException e){
                    tmpIndividual.image = null;
                    url  = "http://cdn.onlinewebfonts.com/svg/img_346711.png";
                    getImage(url, tmpIndividual);
                    System.out.println("URL WRONG FOR: "+ tmpIndividual.firstname + " "+tmpIndividual.surname);
                } 
                
                
            }
        }
    }
    
    public static void decodeJSON(String jsonString, List<Individual> list){
        JSONObject jsonObject = new JSONObject(jsonString);
        //GET ARRAY of prizes
        JSONArray newJSON = jsonObject.getJSONArray("laureates");
       
        // Debugging prints
//        System.out.println(newJSON.toString()); //print it as a string
//        System.out.println(newJSON.get(0)); //print the first element of the array
        
        // for the length of the json string 
        for(int i = 0; i < newJSON.length();i++){
            jsonObject = new JSONObject(newJSON.get(i).toString()); // take the first elememnt make an object 
            Individual person = new Individual();
            //Get the year and category 
            int exists = 0;
            person.per.id = jsonObject.getString("id");
            for(int k = 0; k < list.size(); k++){
                Individual tmpPerson = new Individual();
                tmpPerson = list.get(k);
                if(tmpPerson.per.id.equals(person.per.id)){
                    exists = 1;
                    person = tmpPerson;
                    break;
                }
            }
            if(exists == 0){
                if(jsonObject.has("firstname")){
                    person.firstname = jsonObject.getString("firstname");
                }
                if(jsonObject.has("surname")){
                   person.surname = jsonObject.getString("surname");
                }
                person.per.born = jsonObject.getString("born");
                if(jsonObject.has("died") && jsonObject.has("diedCountry") && jsonObject.has("diedCountryCode")&& jsonObject.has("diedCity")){
                    person.per.died = jsonObject.getString("died");
                    person.per.diedCountry = jsonObject.getString("diedCountry");
                    person.per.diedCountryCode = jsonObject.getString("diedCountryCode");
                    person.per.diedCity = jsonObject.getString("diedCity");
                    
                }
                if(jsonObject.has("born") && jsonObject.has("bornCountry") && jsonObject.has("bornCountryCode")&& jsonObject.has("bornCity")){
                    person.per.bornCountry = jsonObject.getString("bornCountry");
                    person.per.bornCountryCode = jsonObject.getString("bornCountryCode");
                    person.per.bornCity = jsonObject.getString("bornCity");  
                }
                person.per.gender = jsonObject.getString("gender");
            } 
            // add them to the object prize
            Prizes p = new Prizes();
            
            // make another array that will contain the elements in 'laureates'
            JSONArray newJSON_2 = jsonObject.getJSONArray("prizes");
            for(int j = 0; j < newJSON_2.length(); j++){
                jsonObject = new JSONObject(newJSON_2.get(j).toString());
                
                try{
                    if(jsonObject.has("year")){
                        p.year = jsonObject.getString("year");
                    }
                    if(jsonObject.has("category")){
                        p.category = jsonObject.getString("category");
                    }
                    if(jsonObject.has("share")){
                        p.share = jsonObject.getString("share");
                    }
                    if(jsonObject.has("motivation")){
                        p.motivation = jsonObject.getString("motivation");
                    }
                    
                    JSONArray newJSON_3 = jsonObject.getJSONArray("affiliations");
                    List<Affil> affilList = new ArrayList<>();
                    
                        for(int f = 0; f < newJSON_3.length(); f++){
                            Affil affil = new Affil();
//                            try{
                            String tmpJsonString = newJSON_3.get(f).toString();
                            if(tmpJsonString.substring(0, 1).equals("{")){

                                jsonObject = new JSONObject(tmpJsonString);
                                if(jsonObject.has("city")){
                                    affil.city = jsonObject.getString("city");
                                }
                                if(jsonObject.has("country")){
                                    affil.country = jsonObject.getString("country");
                                }
                                if(jsonObject.has("name")){
                                    affil.name = jsonObject.getString("name");
                                }
                                affilList.add(affil);
                                
                            }
//                            
                        }
                    p.affiliation = affilList;
                   
                    person.per.addPrizeList(p);
                    
                } catch(JSONException e){
                    //INSTEAD of this you could add an if statement above saying
                    // if(category == peace) do the below
                    System.out.println("ERROR HERE?"+ e + person.firstname + person.surname);
                }
                person.buildImage();
//                System.out.println(person.toString()); //dispaly the string
            } 
            list.add(person);
        }    
    }
    //get image from a website
    public static void getImage(String s, Individual ind) throws MalformedURLException, FileNotFoundException, IOException{
            URL url = new URL(s);
            String fileName = url.getFile();
            String destName;
            if(ind.per.gender.equals("org") || ind.image == null){
                destName = "./images/"+ind.firstname+"_postcard.jpg";
            } else {
                destName = "./images" + fileName.substring(fileName.lastIndexOf("/"));
            }
            System.out.println(destName);
 
            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream(destName);
 
            byte[] b = new byte[2048];
            int length;
 
            while ((length = is.read(b)) != -1) {
        os.write(b, 0, length);
            }
 
            is.close();
            os.close();
    }
    public static boolean folderIsEmpty(){
        File file = new File("./images");
            
        if(file.isDirectory()){
            if(file.list().length>0){  
                return false;
                    
            }else{
                    
                return true;
                    
            }
                
        }
        return false;
    }
    
    public List<Individual> returnPeopleList(){
        return listofpeople;
    }
}
