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


    // Constructor
    public Document(String authors,String year, String title ,String abstractText, String fullText) {
        this.authors = authors;
        this.title = title;
        this.year = year;
        this.abstractText = abstractText;
        this.fullText = fullText;

    }

//    // Getters and setters
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getAbstractText() {
//        return abstractText;
//    }
//
//    public void setAbstractText(String abstractText) {
//        this.abstractText = abstractText;
//    }
//
//    public String getFullText() {
//        return fullText;
//    }
//
//    public void setFullText(String fullText) {
//        this.fullText = fullText;
//    }
//
//    public String getAuthors() {
//        return authors;
//    }
//
//    public void setAuthors(String authors) {
//        this.authors = authors;
//    }
}
