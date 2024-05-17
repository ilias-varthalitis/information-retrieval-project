import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.util.List;

public class Manager {
    private Indexer indexer;
    private Searcher searcher;

    public Manager(String indexPath) throws IOException {
        this.indexer = new Indexer(indexPath);
        this.searcher = new Searcher(indexPath);
    }

    public void indexDocuments(List<Document> documents) throws IOException {
        for (Document doc : documents) {
            indexer.indexDocument(doc);
        }
        indexer.close();
    }

    public void search(String query) throws IOException, ParseException {
        searcher.search(query);
    }

    public static void main(String[] args) {
        try {
            Manager manager = new Manager("path/to/index");

            // Δημιουργία και ευρετηρίαση εγγράφων
            Document doc1 = new Document("Title1", "Abstract1", "Full text of document 1", "Author1");
            Document doc2 = new Document("Title2", "Abstract2", "Full text of document 2", "Author2, Author3");
            manager.indexDocuments(List.of(doc1, doc2));

            // Αναζήτηση εγγράφων
            manager.search("document");
            manager.search("title:Title1");
            manager.search("authors:Author1");

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
