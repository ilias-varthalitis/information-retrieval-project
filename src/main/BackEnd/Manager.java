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

    public void indexDocuments() throws IOException {
        indexer.indexCSV("D:\\pitoura\\corpus.csv");
    }

    public void search(String query) throws IOException, ParseException {
        searcher.search(query);
    }

    public static void main(String[] args) {
        try {
            Manager manager = new Manager("D:\\pitoura");

            manager.indexDocuments();

            manager.search("title:Algorithm");
            //manager.search("authors:Author1");

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
