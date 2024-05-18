package BackEnd;

import org.apache.lucene.queryparser.classic.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javafx.scene.control.ListView;

public class Manager {
    private Indexer indexer;
    private Searcher searcher;

    public Manager(String indexPath) throws IOException {
        System.out.println(indexPath);
        this.indexer = new Indexer(indexPath);
        //this.searcher = new Searcher(indexPath);
    }

    public void indexDocuments() throws IOException {
        this.indexer.indexCSV("D:\\pitoura\\corpus.csv");
        this.indexer.close();
    }

    public void search(String query) throws IOException, ParseException {
        this.searcher = new Searcher("D:\\pitoura\\index");
        this.searcher.search(query);
    }

    public void deleteIndexes(){
        File indexDir = new File("D:\\pitoura\\index");
        if (indexDir.exists()) {
            for (File file : indexDir.listFiles()) {
                file.delete();
            }
            indexDir.delete();
        }
    }

    /*public static void main(String[] args) {
        try {
            Manager manager = new Manager("D:\\pitoura\\index");

            manager.indexDocuments();

            manager.search("title: recipe");

            manager.deleteIndexes();

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }*/
}
