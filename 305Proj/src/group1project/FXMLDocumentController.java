/**
 * Group 1 Assignment
 * Winter 2018 CMPT 305
 * FXML Controller Class
 * Written by Leon Xiao and Gerardo Cea
 */


// MAKE SURE TO REPLACE THE PREV CONTROLLER SO YOU DON'T MESS YOUR CODE UP OR
// SOMETHING
package group1project;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author Leon and Gerardo
 */
public class FXMLDocumentController implements Initializable {
// The variable family
    Model nobleprize;
    @FXML
    private Button scButton;
    @FXML
    private TextField searchText;
    @FXML
    private ComboBox<String> Gender;
    ObservableList<String> Genderlist = FXCollections.observableArrayList("Male", "Female","org", "All");
    @FXML
    private ComboBox<String> Category;
    ObservableList<String> Categorylist = FXCollections.observableArrayList("Chemistry", "Economics","Medicine","Physics", "Peace","Literature", "All");
    @FXML
    private ComboBox<String> Country;
    ObservableList<String> Countrylist;
    @FXML
    private TextField yearFrom;
    @FXML
    private TextField yearTo;
    @FXML
    private ListView viewList;

    //list of individuals to feed ListView
    List<Individual> inds;
    // Our search results
    List<Individual> searchResults = new ArrayList<>();
    
/**
 * Initializes the controller class.
 */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
         // TODO
            nobleprize = new Model();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.print(nobleprize.returnPeopleList());
        Set<String> countryNames = new HashSet<>();
        
        countryGet(nobleprize.returnPeopleList(), countryNames);
        
        Countrylist = FXCollections.observableArrayList(countryNames);
        Gender.setItems(Genderlist);
        Category.setItems(Categorylist);
        Country.setItems(Countrylist);
        inds=nobleprize.returnPeopleList();
        ObservableList<Individual> lList = FXCollections.observableList(inds);
        System.out.println(lList.toString());
        viewList.setItems(FXCollections.observableList(nobleprize.returnPeopleList()));
    }
    private void updateListView(List<Individual> ray) {
        viewList.setItems(null);
        ObservableList<Individual> lList = FXCollections.observableList(ray);
        viewList.setItems(lList);
    }

    @FXML
    public void newWindow(MouseEvent event) throws MalformedURLException, FileNotFoundException, IOException{
        Group root = new Group();  
        Scene scene = new Scene(root, 530,400);
        Stage stage = new Stage();       
        Individual current = (Individual) viewList.getSelectionModel().getSelectedItem();
        
        if (current.image != null){
            Image pic = new Image(current.image);
            ImageView viewer = new ImageView();
            viewer.setImage(pic);
            root.getChildren().add(viewer);
        }
        stage.setTitle("Full Information");
        Label detail = new Label("\n"+current.toStr());
        detail.setMaxWidth(200);
        detail.setWrapText(true);  
        detail.setLayoutX(300);
        root.getChildren().add(detail);
        stage.setScene(scene);
        stage.show();
    }  

/**
 * handleSearchAction is the action function used by the search button
 * It calls on all of the drop-down boxes and the From-To year tabs
 * ATM the function just reads the user input and drop-down box choices
 * @param event
 */
    @FXML
    private void handleSearchAction2(ActionEvent event) {
        // Deep copy
        deepCopy();

        if (searchText.getText() == null || searchText.getText().equals("")){
            handleSearch();
        } else {
            searchBar();
        }
        updateListView(searchResults);
    }
/**
 * countryGet reads all the countries from all individuals and throws them into
 * the countries 
 * @param people
 * @param countries 
 */
    public void countryGet(List<Individual> people, Set<String> countries){
        for (Individual x : people){
            countries.add(x.per.bornCountry);
        }
    }
/**
 * handleSearch is the function called when there is no user input in the text
 * field.
 * This calls upon the drop-down boxes and year text-fields.
 */
    public void handleSearch(){
        String country_option = Country.getSelectionModel().getSelectedItem();
        String gender_option = Gender.getSelectionModel().getSelectedItem();
        String category_option = Category.getSelectionModel().getSelectedItem();
        String fromY = yearFrom.getText();
        String toY = yearTo.getText();
        // System.out.println("Country: " + country_option + "Gender: " + gender_option + "Cat: " + category_option + "Years: " + fromY + toY);
    // Stand in values for the From and To ints
        int fromY_int = 0;
        int toY_int = 9999;
    // Check that the user even wrote numbers in the From-To text fields
        if (fromY.matches("[0-9]+")) {
            fromY_int = Integer.parseInt(fromY);
        }
        if (toY.matches("[0-9]+")) {
            toY_int = Integer.parseInt(toY);
        }
    // If not, we'll use the 0 and 9999 for From and To respectively

    // Category search
    // If no Category has been chosen, we add them all in
        if(category_option != null && !category_option.equals("All")){
            System.out.println("Hello!");
            categorySearch(category_option);
        }
    
    // Country search
        if(country_option != null && !country_option.equals("All")){
            countrySearch(country_option);
        }

    // Gender search
        if(gender_option != null && !gender_option.equals("All")){
            genderSearch(gender_option);
        }
    
    // Year search
        if (fromY_int != 0 || toY_int != 9999){
            yearSearch(fromY_int, toY_int);
        }
    }

/**
 * categorySearch looks in searchResults and removes all individuals who do not
 * have any awards of the given category
 * @param option 
 */
    private void categorySearch(String option){
        List<Individual> temp = new ArrayList<>();
        for (Individual x : searchResults){
            int check = 0;
            x.prizeList.toString();
            for (Prizes y : x.per.prizeList){
                
                if (x.per.bornCountry == null){
                    temp.add(x);
                }
                if(y.category != null && y.category.toLowerCase().equals(option.toLowerCase())){
                    check++;
                }
            }
            if (check == 0){
                temp.add(x);
            }
        }
        for (Individual bad : temp){
            searchResults.remove(bad);
        }
    }
/**
 * countrySearch looks in searchResults and removes all individuals who were not
 * born in the specified country
 * @param option 
 */
    private void countrySearch(String option){
        List<Individual> temp = new ArrayList<>();
        for (Individual x : searchResults){
            if (x.per.bornCountry != null && !x.per.bornCountry.toLowerCase().equals(option.toLowerCase())){
                temp.add(x);
            }

        }
        for (Individual bad : temp){
            searchResults.remove(bad);
        }
    }
/**
 * genderSearch looks in searchResults and removes all individuals who are not
 * of the specified gender
 * @param option 
 */
    private void genderSearch(String option){
        List<Individual> temp = new ArrayList<>();
        for (Individual x : searchResults){
            if (x.per.gender == null){
                temp.add(x);
            }
            if (!x.per.gender.toLowerCase().equals(option.toLowerCase())){
                temp.add(x);
            }
        }
        for (Individual bad : temp){
            searchResults.remove(bad);
        }
    }
/**
 * yearSearch function looks in searchResults and removes all individuals who
 * have no awards within the specified from-to time frame
 * @param from
 * @param to 
 */
    private void yearSearch(int from, int to){
        List<Individual> temp = new ArrayList<>();

        for (Individual x : searchResults){
            for (Prizes y : x.per.prizeList){
                if (y.year == null){
                    temp.add(x);
                } else {
                    int prize_year = Integer.parseInt(y.year);
                    if (prize_year < from || prize_year > to){
                        temp.add(x);
                    }
                }
            }
        }
        for (Individual bad : temp){
            searchResults.remove(bad);
        }
    }
/**
 * the searchbar search thing
 * An idea for this is to just use a Includes sort of function
 */
    public void searchBar(){
        List<Individual> temp = new ArrayList<>();        
        String userInput = searchText.getText();
    // Turn userInput to lower case to avoid any problems with the case
        userInput = userInput.toLowerCase();
    // Split userInput into an array of string
    // This is in case the user wants to look for someone by their full name
    // firstname + surname
    
        for (Individual x : searchResults){
            String i_name = x.firstname+x.surname;
            if(!i_name.toLowerCase().contains(userInput)){
                temp.add(x);
            }
        }
        for (Individual bad : temp){
            searchResults.remove(bad);
        }
    }
/**
 * deepCopy copies deep
 * Used to deep copy the original list of laurietes and the searchResult list
 */
    private void deepCopy(){
        searchResults.clear();
        for (Individual x : nobleprize.returnPeopleList()){
            searchResults.add(new Individual(x));
        }
    }
}
