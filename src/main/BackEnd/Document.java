package BackEnd;

public class Document {
    private String title;
    private String abstractText;
    private String fullText;
    private String authors;

    // Constructor
    public Document(String title, String abstractText, String fullText, String authors) {
        this.title = title;
        this.abstractText = abstractText;
        this.fullText = fullText;
        this.authors = authors;
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstractText() {
        return abstractText;
    }

    public void setAbstractText(String abstractText) {
        this.abstractText = abstractText;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }
}
