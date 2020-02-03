package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.List;

public class Main extends Application {
    public List<String> getDirectory(String dirPath){
        System.out.println("Getting the directory for: " + dirPath);
        File dir = new File(dirPath);
        File[] firstLevelFiles = dir.listFiles();

        List<String> directories = new ArrayList<>();
        if (firstLevelFiles != null && firstLevelFiles.length > 0) {
            for (File aFile : firstLevelFiles) {
                if (aFile.isDirectory()) {
                    String directoryName = aFile.getName();
                    directories.add(directoryName);
                } else {
                    String filenames = aFile.getName();
                }
            }
        }
        return directories;
    }

    public void refreshSidebar(String currentDirectory, VBox _sidebar){
        System.out.println("---------- Refreshing the Sidebar for the Directory: --------------");
        System.out.println(currentDirectory);

        // Step 1: Get the contents of the directory located at 'currentDirectory'
        List<String> folders = getDirectory(currentDirectory);

        // Step 2: Clear the Sidebar
        _sidebar.getChildren().clear();

        // Step 3: Populate the sidebar
        for (String f: folders){
            Map <String, Button> buttonMap = new HashMap<>();
            Map <String, Label> labelMap = new HashMap<>();
            Map <String, HBox> boxMap = new HashMap<>();

            // Load in the image for the button
            buttonMap.put(f, new Button(">"));
            labelMap.put(f, new Label(f));
            boxMap.put(f, new HBox());

            Button b = buttonMap.get(f);
            Label l = labelMap.get(f);
            HBox h = boxMap.get(f);

            h.getChildren().addAll(b, l);
            _sidebar.getChildren().addAll(h);

            b.setOnAction(e -> {
                System.out.println(f + ": button clicked");
//                String newDirectory = currentDirectory + "//" + f + "//";
                String newDirectory = currentDirectory + f + "//";
                List<String> subdirs = getDirectory(newDirectory);
                System.out.println("Subfolders: " + subdirs.toString());

                _sidebar.getChildren().clear();
                refreshSidebar(newDirectory, _sidebar);
            });
        }
        addBackButton(_sidebar, currentDirectory);
    }

    public void addBackButton(VBox _sidebar, String currentDirectory){
        Button backButton = new Button("Back");

        backButton.setOnAction(e-> {
            String[] parts = currentDirectory.split("//");
            // Split the current directory into a folder-wise list
            List<String> listParts = new LinkedList<>(Arrays.asList(parts));

            // Obtain the index of the last element and delete the element
            int idxToRemove = listParts.size() - 1;

            System.out.println(idxToRemove);
            System.out.println(currentDirectory);

            listParts.remove(idxToRemove);
            String newDirectory = String.join("//", listParts);

            refreshSidebar(newDirectory, _sidebar);
        });

        _sidebar.getChildren().addAll(backButton);
    }

    public void createNewFolder(VBox _mainWindow, int level){
        
    }
    @Override
    public void start(Stage primaryStage){
        double width = 800;
        double height = 600;

        primaryStage.setTitle("Filr");

        AnchorPane root = new AnchorPane();
        VBox sidebar = new VBox(10);
        VBox mainWindow = new VBox();

        sidebar.setId("sidebar");
        sidebar.setMaxSize(width*0.2, height);
        sidebar.setMinSize(width*0.2, height);

        mainWindow.setId("mainWindow");
        mainWindow.setMaxSize(width*0.8, height);
        mainWindow.setMinSize(width*0.8, height);

        AnchorPane.setLeftAnchor(sidebar, 0.0);
        AnchorPane.setTopAnchor(sidebar, 0.0);
        root.getChildren().addAll(sidebar, mainWindow);

        AnchorPane.setRightAnchor(mainWindow, 0.0);
        AnchorPane.setTopAnchor(mainWindow, 0.0);

        String currentDirectory = "C://Users//MattC//Documents//Uni//";
        refreshSidebar(currentDirectory, sidebar);

        // Add listener for resizing.
        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            double currentWidth = primaryStage.getWidth();

            // Rescale the Window
            double newHeight = (currentWidth*height)/width;
            primaryStage.setHeight(newHeight);

            // Rescale the sidebar
            sidebar.setMinSize(currentWidth*0.2, newHeight);
            sidebar.setMaxSize(currentWidth*0.2, newHeight);
        });

        // Configure the main window
        TextField t = new TextField();
        HBox h = new HBox(t);
        mainWindow.getChildren().addAll(h);
        t.setId("rootFolderName");
        h.setId("folderContainer");

        // Logic for TextField Mechanics
        t.setOnAction(e -> {
            System.out.println("Enter pressed in the textfield;");

        });

        // Render
        Scene scene = new Scene(root, width, height);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
