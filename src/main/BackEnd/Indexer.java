package BackEnd;

import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.store.*;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVFormat;

public class Indexer {
    private IndexWriter writer;

    public Indexer(String indexPath) throws IOException {
        Directory dir = FSDirectory.open(Paths.get(indexPath));

        StandardAnalyzer analyzer = new StandardAnalyzer();

        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        writer = new IndexWriter(dir, config);
    }

    public void indexDocument(Document doc) throws IOException {
        org.apache.lucene.document.Document luceneDoc = new org.apache.lucene.document.Document();
        luceneDoc.add(new TextField("authors", doc.getAuthors(), Field.Store.YES));
        luceneDoc.add(new TextField("year", doc.getYear(), Field.Store.YES));
        luceneDoc.add(new TextField("title", doc.getTitle(), Field.Store.YES));
        luceneDoc.add(new TextField("abstract", doc.getAbstractText(), Field.Store.YES));
        luceneDoc.add(new TextField("full_text", doc.getFullText(), Field.Store.YES));

        writer.addDocument(luceneDoc);
    }

    public void indexCSV(String csvFilePath) throws IOException {
        FileReader reader = new FileReader(csvFilePath);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader().parse(reader);
        for (CSVRecord record : records) {
            Document doc = new Document(
                    record.get("authors"),
                    record.get("year"),
                    record.get("title"),
                    record.get("abstract"),
                    record.get("full_text")
            );
            indexDocument(doc);
        }
    }

    public void close() throws IOException {
        writer.close();
    }
}



