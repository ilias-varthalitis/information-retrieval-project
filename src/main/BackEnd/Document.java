package BackEnd;

import lombok.Getter;

public class Document {
    @Getter
    private String authors;
    @Getter
    private String year;
    @Getter
    private String title;
    @Getter
    private String abstractText;
    @Getter
    private String fullText;

    public Document(String authors,String year, String title ,String abstractText, String fullText) {
        this.authors = authors;
        this.title = title;
        this.year = year;
        this.abstractText = abstractText;
        this.fullText = fullText;
    }
}
