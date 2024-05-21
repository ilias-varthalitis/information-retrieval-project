package FrontEnd;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.apache.lucene.document.Document;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.regex.Pattern;

import BackEnd.*;
import org.apache.lucene.search.Query;

public class gui extends Application {

    private static final String INDEX_DIR = Paths.get("src/main/Data/index").toAbsolutePath().toString();//"D:\\pitoura\\index"; // Update this path
    private static final int RESULTS_PER_PAGE = 10;
    private List<Document> searchResults;
    private int currentPage = 0;
    private Query query;
    private ObservableList<String> originalOptions;
    String queryString;
    ComboBox<String> comboBox;

    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Lucene Search Application");

        TextField searchField = new TextField();
        searchField.setPromptText("Enter search query");

        comboBox = new ComboBox<>();
        originalOptions = FXCollections.observableArrayList(
                "Keyword", "Author", "Year", "Title", "Abstract", "Full text"
        );
        comboBox.setItems(originalOptions);
        comboBox.setPromptText("Select Search By:");
        comboBox.setOnAction(event -> {
            String selectedOption = comboBox.getSelectionModel().getSelectedItem();
            System.out.println("Selected: " + selectedOption);
        });

        Button searchButton = new Button("Search");
        Button sortAscButton = new Button("Sort by Year Ascending");
        Button sortDescButton = new Button("Sort by Year Descending");
        ListView<VBox> resultsList = new ListView<>();
        Button prevButton = new Button("Previous");
        Button nextButton = new Button("Next");
        Button viewHistoryButton = new Button("View Search History");

        prevButton.setDisable(true);
        nextButton.setDisable(true);

        searchButton.setOnAction(e -> {
            handleSearch(comboBox, searchField, resultsList, prevButton, nextButton);
        });

        sortAscButton.setOnAction(e -> {
            if (searchResults != null) {
                searchResults.sort(Comparator.comparing(doc -> doc.get("year")));
                currentPage = 0;
                updateResultsList(resultsList, prevButton, nextButton, queryString);
            }
        });

        sortDescButton.setOnAction(e -> {
            if (searchResults != null) {
                searchResults.sort(Comparator.comparing((Document doc) -> doc.get("year")).reversed());
                currentPage = 0;
                updateResultsList(resultsList, prevButton, nextButton, queryString);
            }
        });

        prevButton.setOnAction(e -> {
            if (currentPage > 0) {
                currentPage--;
                updateResultsList(resultsList, prevButton, nextButton, queryString);
            }
        });

        nextButton.setOnAction(e -> {
            if ((currentPage + 1) * RESULTS_PER_PAGE < searchResults.size()) {
                currentPage++;
                updateResultsList(resultsList, prevButton, nextButton, queryString);
            }
        });

        viewHistoryButton.setOnAction(e -> {
            try {
                showSearchHistory();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });




        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        VBox vboxDroplist = new VBox(10, comboBox);
        vboxDroplist.setPadding(new Insets(10));

        HBox navigationButtons = new HBox(10, prevButton, nextButton);
        HBox sortButtons = new HBox(10, sortAscButton, sortDescButton);
        HBox historyButtonBox = new HBox(10, viewHistoryButton);

        vbox.getChildren().addAll(vboxDroplist, searchField, searchButton, sortButtons, resultsList, navigationButtons, historyButtonBox);

        Scene scene = new Scene(vbox, 700, 400);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleSearch(ComboBox<String> comboBox, TextField searchField, ListView<VBox> resultsList, Button prevButton, Button nextButton) {
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
                Manager manager = new Manager(INDEX_DIR);
                manager.indexDocuments();
                manager.search(queryString);
                searchResults = manager.getSearcher().getMyList(); // Ensure this returns List<Document>
                manager.deleteIndexes();

                currentPage = 0;
                updateResultsList(resultsList, prevButton, nextButton, queryString);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    private void updateResultsList(ListView<VBox> resultsList, Button prevButton, Button nextButton, String queryString) {
        resultsList.getItems().clear();

        int start = currentPage * RESULTS_PER_PAGE;
        int end = Math.min(start + RESULTS_PER_PAGE, searchResults.size());

        for (int i = start; i < end; i++) {
            Document doc = searchResults.get(i);

            String authors = doc.get("authors");
            String year = doc.get("year");
            String title = doc.get("title");
            String abstractContent = doc.get("abstract");
            String fullTextContent = doc.get("full_text");

            TextFlow authorsTextFlow = stringToTextFlow(authors);
            TextFlow yearTextFlow = stringToTextFlow(year);
            TextFlow titleTextFlow = stringToTextFlow(title);
            TextFlow abstractTextTextFlow = stringToTextFlow(abstractContent);
            TextFlow fullTextTextFlow = stringToTextFlow(fullTextContent);

            //TextFlow authorsTextFlow = authors;//, yearTextFlow, titleTextFlow, abstractTextFlow, fulltextTextFlow;

            /*if (!Objects.equals(comboBox.getSelectionModel().getSelectedItem(), "Keyword")) {

                queryString = queryString.replaceFirst("^[^:]+:", "");
            }
            titleText = createTextFlowWithHighlightedWord(title, queryString);*/

            switch (comboBox.getSelectionModel().getSelectedItem()) {
                case "Keyword":
                    authorsTextFlow = createTextFlowWithHighlightedWord(authors, queryString);
                    yearTextFlow = createTextFlowWithHighlightedWord(year, queryString);
                    titleTextFlow = createTextFlowWithHighlightedWord(title, queryString);
                    abstractTextTextFlow = createTextFlowWithHighlightedWord(abstractContent, queryString);
                    fullTextTextFlow = createTextFlowWithHighlightedWord(fullTextContent, queryString);
                    break;
                case "Year":
                    queryString = queryString.replaceFirst("^[^:]+:", "");
                    yearTextFlow = createTextFlowWithHighlightedWordNew(queryString);
                    break;
                case "Author":
                    queryString = queryString.replaceFirst("^[^:]+:", "");
                    authorsTextFlow = createTextFlowWithHighlightedWord(authors, queryString);
                    break;
                case "Title":
                    queryString = queryString.replaceFirst("^[^:]+:", "");
                    titleTextFlow = createTextFlowWithHighlightedWord(title, queryString);
                    break;
                case "Abstract":
                    queryString = queryString.replaceFirst("^[^:]+:", "");
                    abstractTextTextFlow = createTextFlowWithHighlightedWord(abstractContent, queryString);
                    break;
                case "Full text":
                    queryString = queryString.replaceFirst("^[^:]+:", "");
                    fullTextTextFlow = createTextFlowWithHighlightedWord(fullTextContent, queryString);
                    break;
            }


            HBox hboxAuthors = new HBox(1, new Label("Authors:"), authorsTextFlow);
            HBox hboxYear = new HBox(1, new Label("Year:"), yearTextFlow);
            HBox hboxTitle = new HBox(1, new Label("Title:"), titleTextFlow);
            Hyperlink abstractLink = new Hyperlink("Abstract");
            Hyperlink fullTextLink = new Hyperlink("Full text");

            abstractLink.setOnAction(event -> openAbstractWindow(abstractContent));
            fullTextLink.setOnAction(event -> openFullTextWindow(fullTextContent));

            VBox vBox = new VBox(5, hboxAuthors, hboxYear, hboxTitle, abstractLink, fullTextLink);
            resultsList.getItems().add(vBox);
        }

        prevButton.setDisable(currentPage == 0);
        nextButton.setDisable((currentPage + 1) * RESULTS_PER_PAGE >= searchResults.size());
    }

    private TextFlow stringToTextFlow(String content) {
        TextFlow textFlow = new TextFlow();
        Text text = new Text(content);
        textFlow.getChildren().add(text);
        return textFlow;
    }

    private TextFlow createTextFlowWithHighlightedWord(String content, String wordToHighlight) {
        TextFlow textFlow = new TextFlow();

        // Split the content into parts using a case-insensitive approach
        String[] parts = content.split("(?i)(" + Pattern.quote(wordToHighlight) + ")");
        for (int i = 0; i < parts.length; i++) {
            // Add non-highlighted text part
            Text text = new Text(parts[i]);
            textFlow.getChildren().add(text);

            // Add highlighted text part, but not after the last part
            if (i < parts.length - 1) {
                Text highlightedText = new Text(wordToHighlight);
                highlightedText.setFill(Color.GREEN);
                textFlow.getChildren().add(highlightedText);
            }
        }

        return textFlow;
    }

    private TextFlow createTextFlowWithHighlightedWordNew(String wordToHighlight) {
        TextFlow textFlow = new TextFlow();
        Text highlightedText = new Text(wordToHighlight);
        highlightedText.setFill(Color.GREEN);
        textFlow.getChildren().add(highlightedText);
        return textFlow;
    }

    private void openAbstractWindow(String abstractTextFlow) {
        Stage abstractStage = new Stage();
        abstractStage.setTitle("Abstract");

        Label abstractLabel = new Label(abstractTextFlow);
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

    private void showSearchHistory() throws IOException {
        Stage historyStage = new Stage();
        historyStage.setTitle("Search History");

        TextArea historyTextArea = new TextArea();
        historyTextArea.setEditable(false);

        try (BufferedReader reader = new BufferedReader(new FileReader("D:\\pitoura\\search_history.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                historyTextArea.appendText(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ScrollPane scrollPane = new ScrollPane(historyTextArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefSize(600, 400);

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> historyStage.close());

        Button deleteButton = new Button("Delete History");
        deleteButton.setOnAction(e -> {
            try {
                clearSearchHistory();
                historyTextArea.clear();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        HBox buttonBox = new HBox(10, closeButton, deleteButton);

        VBox vbox = new VBox(10, scrollPane, buttonBox);
        vbox.setPadding(new Insets(10));

        Scene scene = new Scene(vbox, 700, 600);
        historyStage.setScene(scene);
        historyStage.setResizable(true);
        historyStage.show();
    }

    private void clearSearchHistory() throws IOException {
        try (FileWriter writer = new FileWriter("D:\\pitoura\\search_history.txt")) {
            writer.write("");
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
