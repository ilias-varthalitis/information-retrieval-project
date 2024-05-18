package BackEnd;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;


public class Searcher {
    private IndexSearcher searcher;
    private QueryParser parser;

    public Searcher(String indexPath) throws IOException {
        searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(Paths.get(indexPath))));
        String[] fields = {"title", "abstract", "full_text", "authors"};

        StandardAnalyzer analyzer = new StandardAnalyzer();
        parser = new MultiFieldQueryParser(fields, analyzer);
    }

    public void search(String queryString) throws IOException, ParseException {
        Query query = parser.parse(queryString);
        TopDocs results = searcher.search(query, 10);
        System.out.println("results " + results.totalHits);
        saveSearchToHistory(queryString, results.totalHits.value);
        for (ScoreDoc hit : results.scoreDocs) {
            Document doc = searcher.doc(hit.doc);
            System.out.println("Title: " + doc.get("title"));
            System.out.println("Abstract: " + doc.get("abstract"));
            //System.out.println("Full Text: " + doc.get("full_text"));
            System.out.println("Authors: " + doc.get("authors"));
            System.out.println("Doc num: " + hit.doc);
            System.out.println();
        }
    }

    private void saveSearchToHistory(String query, long totalHits) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("D:\\pitoura\\search_history.txt", true))) {
            writer.write(LocalDateTime.now() + " - Query: " + query + " - Hits: " + totalHits);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}