package sample.src;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.management.InvalidAttributeValueException;
import java.security.Key;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Folder {
    // Built using template from: https://www.geeksforgeeks.org/classes-objects-java/
    // instance variables

    String foldername = ""; // Updated when the folders themselves are generated as the names in the textField
                            // can change at any time.
    int level = -1;         // Set as -1 as this is an "illegal" value which must be later updated but must be
    int id    = -1;         // Same as level, set to an illegal value to be set later.
    int maxLevels = 9;
    TextField t = new TextField();
    HBox h = new HBox();
    Label l = new Label();
    Folder reference;

    // Static Class Variables (accessible to all instances of Folder)
    static ArrayList<Integer> ids = new ArrayList<>();

    public Folder(Integer id, VBox mainWindow, int _level){
        // Initialisation class that is called when a new instance of the class is created.
        setID(id);
        setLevel(_level);
        if (ids.contains(id)){
            System.out.println("Existing ids: " + ids);
            throw new IllegalArgumentException("This ID, " + id + " already exists with level " + level);
        }

        if (_level > maxLevels - 1){
            throw new IllegalArgumentException("Level exceeds maximum limit of 10");
        }
        else{
            ids.add(id);
            // Allow the creation of the Folder object.
            System.out.println("Creating a folder object with id: " + id + " and level: " + level);

            t.setId(id.toString());
            h.getChildren().addAll(t, l);
            l.setText(id.toString());
            mainWindow.getChildren().addAll(h);

            // Add an event listener to create a new textField;
            t.setOnAction(e->{
                int newLevel = (this.getLevel() + 1);
                int newID = Folder.getFreeID();

                Folder newFolder = new Folder(newID, mainWindow, newLevel);
                newFolder.getTextField().requestFocus();

            });

            KeyCode indent = KeyCode.SPACE;
            final KeyCombination dedent = new KeyCodeCombination(KeyCode.SPACE, KeyCombination.CONTROL_DOWN);


            t.setOnKeyPressed(e -> {
                if (dedent.match(e)){
                    System.out.println("Dedent triggered");
                    setLevel(level - 1);
                    this.getTextField().requestFocus();
                }

                else{
                    if (e.getCode() == indent){
                        System.out.println("Indent triggered");
                        setLevel(level + 1);
                        this.getTextField().requestFocus();
                    }
                }
            });
        }
    }

    public void setIndentation(int level){
        int leftPadding = 10;
        int rightPadding = 0;
        int topPadding = 3;
        int bottomPadding = 0;

        if (level == 0){
            // Overwrite for root
            topPadding = 10;
        }

        // Calculate indentation for left padding
        HBox h = this.getHBox();
        int paddingPerLevel = 20;
        leftPadding = leftPadding + paddingPerLevel* level;

        h.setStyle("-fx-padding: " + topPadding + " " + rightPadding + " " + bottomPadding + " " + leftPadding);

        System.out.println("Setting the indentation of #" + this.getID() + " to " + level);
    }

    public int getID(){
        return id;
    }

    public int getLevel(){
        return level;
    }

    public TextField getTextField(){
        return t;
    }

    public static Integer getFreeID(){
        Integer max = 0;
        for (Integer id : ids){
            if (id> max){
                max = id;
            }
        }
        return max + 1;
    }

    public HBox getHBox(){
        return h;
    }

    public void setID(int _id){
        id = _id;
    }

    public void setReference(Folder ref){
        reference = ref;
    }

    public void setLevel(int _level){
        if (this.getID() == 0){
            // i.e. root
            if (_level != 0){
                throw new IllegalArgumentException("Cannot set the indentation of root");
            }
        }
        else{
            if (_level == 0){
                System.out.println("Level: " + level);
                throw new IllegalArgumentException("Cannot set the indentation of folder to 0 for id: " + this.getID());
            }
        }

        level = _level;
        setIndentation(level);
    }


}
