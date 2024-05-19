package FrontEnd;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.lucene.document.Document;

import java.io.IOException;

import BackEnd.*;

public class gui extends Application {

    private static final String INDEX_DIR = "D:\\pitoura\\index"; // Update this path

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Lucene Search Application");

        TextField searchField = new TextField();
        searchField.setPromptText("Enter search query");

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
        ListView<VBox> resultsList = new ListView<>(); // Change the type here to ListView<VBox>

        searchButton.setOnAction(e -> {
            String queryString;

            switch (comboBox.getSelectionModel().getSelectedItem()) {
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
                    resultsList.getItems().clear();

                    Manager manager = new Manager(INDEX_DIR);
                    manager.indexDocuments();

                    manager.search(queryString);
                    for (Document doc : manager.getSearcher().getMyList()) {

                        String authors = doc.get("authors");
                        String year = doc.get("year");
                        String title = doc.get("title");
                        String abstractContent = doc.get("abstract");
                        String fullTextContent = doc.get("full_text");

                        Label authorsLabel = new Label("Authors: " + authors);
                        Label yearLabel = new Label("Year: " + year);
                        Label titleLabel = new Label("Title: " + title);
                        Hyperlink abstractLink = new Hyperlink("Abstract");
                        Hyperlink fullTextLink = new Hyperlink("Full text");

                        abstractLink.setOnAction(event -> openAbstractWindow(abstractContent));
                        fullTextLink.setOnAction(event -> openFullTextWindow(fullTextContent));

                        VBox vBox = new VBox(5, authorsLabel, yearLabel, titleLabel, abstractLink, fullTextLink);
                        resultsList.getItems().add(vBox);
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

        vbox.getChildren().addAll(vboxDroplist, searchField, searchButton, resultsList);

        Scene scene = new Scene(vbox, 700, 400);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openAbstractWindow(String abstractContent) {
        Stage abstractStage = new Stage();
        abstractStage.setTitle("Abstract");

        Label abstractLabel = new Label(abstractContent);
        abstractLabel.setWrapText(true);

        ScrollPane scrollPane = new ScrollPane(abstractLabel);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefSize(600, 400);

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> abstractStage.close());

        VBox vbox = new VBox(10, scrollPane, cancelButton);
        vbox.setPadding(new Insets(10));

        Scene scene = new Scene(vbox, 700, 600); // Increase the size of the window
        abstractStage.setScene(scene);
        abstractStage.setResizable(true);
        abstractStage.show();
    }

    private void openFullTextWindow(String fullTextContent) {
        Stage fullTextStage = new Stage();
        fullTextStage.setTitle("Full Text");

        Label fullTextLabel = new Label(fullTextContent);
        fullTextLabel.setWrapText(true);

        ScrollPane scrollPane = new ScrollPane(fullTextLabel);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefSize(600, 400);

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> fullTextStage.close());

        VBox vbox = new VBox(10, scrollPane, cancelButton);
        vbox.setPadding(new Insets(10));

        Scene scene = new Scene(vbox, 700, 600); // Increase the size of the window
        fullTextStage.setScene(scene);
        fullTextStage.setResizable(true);
        fullTextStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
