package FrontEnd;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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

        Button searchButton = new Button("Search");
        ListView<String> resultsList = new ListView<>();


        Manager manager = new Manager("D:\\pitoura");


        searchButton.setOnAction(e -> {
            String queryString = searchField.getText();
            if (!queryString.isEmpty()) {
                try {
//                    searchIndex(queryString, resultsList);
                    manager.indexDocuments();

                    manager.search("title: recipe");

                    manager.deleteIndexes();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(new Label("Lucene Search"), searchField, searchButton, resultsList);

        Scene scene = new Scene(vbox, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void searchIndex(String queryString, ListView<String> resultsList) throws Exception {
        resultsList.getItems().clear();

        try (FSDirectory dir = FSDirectory.open(Paths.get(INDEX_DIR));
             IndexReader reader = DirectoryReader.open(dir)) {
            IndexSearcher searcher = new IndexSearcher(reader);
            StandardAnalyzer analyzer = new StandardAnalyzer();

            QueryParser parser = new QueryParser("content", analyzer);
            Query query = parser.parse(queryString);

            TopDocs results = searcher.search(query, 10);
            ScoreDoc[] hits = results.scoreDocs;

            for (ScoreDoc hit : hits) {
                Document doc = searcher.doc(hit.doc);
                String content = doc.get("content");
                resultsList.getItems().add(content);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
