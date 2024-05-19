module information.retrieval.project {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.apache.lucene.core; // Use the inferred module name
    requires org.apache.lucene.analysis.common;
    requires org.apache.lucene.queryparser;
    requires org.apache.lucene.highlighter;
    requires org.apache.lucene.grouping;
    requires com.opencsv;
    requires org.apache.commons.csv;
    requires static lombok;

    opens FrontEnd to javafx.fxml;
    exports FrontEnd;
    exports BackEnd;
    opens BackEnd to javafx.fxml;
}
