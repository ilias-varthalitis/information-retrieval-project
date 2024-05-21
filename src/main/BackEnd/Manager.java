package BackEnd;

import lombok.Getter;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import javafx.scene.control.ListView;

public class Manager {
    private Indexer indexer;
    @Getter
    private Searcher searcher;
    Path currentDir = Paths.get("").toAbsolutePath();


    public Manager(String indexPath) throws IOException {
        System.out.println(indexPath);
        this.indexer = new Indexer(indexPath);
    }

    public void indexDocuments() throws IOException {
        System.out.println(currentDir);
        currentDir = Paths.get("src/main/Data/corpus.csv").toAbsolutePath();
        System.out.println(currentDir);
        this.indexer.indexCSV(currentDir.toString());
        this.indexer.close();
    }

    public void search(String query) throws IOException, ParseException {
        currentDir = Paths.get("src/main/Data/index").toAbsolutePath();
        this.searcher = new Searcher(currentDir.toString());
        this.searcher.search(query);
    }

    public void deleteIndexes(){
        currentDir = Paths.get("src/main/Data/index").toAbsolutePath();
        File indexDir = new File(currentDir.toString());
        if (indexDir.exists()) {
            System.out.println("Number of Files = " + indexDir.listFiles().length);
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
