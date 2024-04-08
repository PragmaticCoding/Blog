module ca.pragmaticcoding.blog {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires org.kordamp.ikonli.javafx;


    opens ca.pragmaticcoding.blog to javafx.fxml;
    exports ca.pragmaticcoding.blog;
    exports ca.pragmaticcoding.blog.actionproperty;
}