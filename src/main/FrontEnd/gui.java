package FrontEnd;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

import BackEnd.*;

public class gui extends Application {

    private static final String INDEX_DIR = "D:\\pitoura\\index"; // Update this path

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Lucene Search Application");

        TextField searchField = new TextField();
        searchField.setPromptText("Enter search query");

//        TextField searchByTitle = new TextField();
//        searchField.setPromptText("Enter search title");

        ComboBox<String> comboBox = new ComboBox<>();
        ObservableList<String> options = FXCollections.observableArrayList(
                "Keyword", "Author", "Title", "Year", "Abstract", "Full text"
        );
        comboBox.setItems(options);


        // Set a prompt text
        comboBox.setPromptText("Select Search By:");
        comboBox.setOnAction(event -> {
            String selectedOption = comboBox.getSelectionModel().getSelectedItem();
            System.out.println("Selected: " + selectedOption);
        });

        Button searchButton = new Button("Search");
        ListView<String> resultsList = new ListView<>();


        searchButton.setOnAction(e -> {
            String queryString;

            switch (comboBox.getSelectionModel().getSelectedItem()){
                case "Keyword":
                    queryString = searchField.getText();
                    break;
                case "Author":
                    queryString = "author:" + searchField.getText();
                    break;
                case "Title":
                    queryString = "title:" + searchField.getText();
                    break;
                case "Year":
                    queryString = "year:" + searchField.getText();
                    break;
                case "Abstract":
                    queryString = "abstract:" + searchField.getText();
                    break;
                default: // "Full text":
                    queryString = "fulltext:" + searchField.getText();
            }

            if (!queryString.isEmpty()) {
                try {
//                    searchIndex(queryString, resultsList);
                    resultsList.getItems().clear();

                    Manager manager = new Manager(INDEX_DIR);
                    manager.indexDocuments();

                    manager.search(queryString);
                    for (Document doc : manager.getSearcher().getMyList()) {

                        resultsList.getItems().add("Authors: "+doc.get("authors")+"\n"+"Year: "+doc.get("year")+"\n"+"Title: "+doc.get("title")+"\n"+"Abstract: "+doc.get("abstract"));
                    }

                    manager.deleteIndexes();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        VBox vboxDroplist = new VBox(10, comboBox);
        vboxDroplist.setPadding(new Insets(10));

        vbox.getChildren().addAll(vboxDroplist,searchField, searchButton, resultsList); //new Label("Lucene Search"),

        Scene scene = new Scene(vbox, 700, 400);

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
