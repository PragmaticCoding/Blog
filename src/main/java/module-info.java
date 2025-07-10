module ca.pragmaticcoding.blog {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires kotlin.stdlib;
    requires org.kordamp.ikonli.javafx;


    opens ca.pragmaticcoding.blog to javafx.fxml, javafx.base;
    exports ca.pragmaticcoding.blog;
    exports ca.pragmaticcoding.blog.actionproperty;
    exports ca.pragmaticcoding.blog.beginners;
    exports ca.pragmaticcoding.blog.taskprogress;
    exports ca.pragmaticcoding.blog.conditionalbinding;
    exports ca.pragmaticcoding.blog.mvciquick;
    exports ca.pragmaticcoding.blog.customproperty;
    exports ca.pragmaticcoding.blog.combobox;
    exports ca.pragmaticcoding.blog.quicksort;
    exports ca.pragmaticcoding.blog.propertyintro;
    exports ca.pragmaticcoding.blog.csstransition;
    exports ca.pragmaticcoding.blog.observablelist;
    exports ca.pragmaticcoding.blog.modena;
    exports ca.pragmaticcoding.blog.stylingtables;
    exports ca.pragmaticcoding.blog.tablecolumns;
    exports ca.pragmaticcoding.blog.styleable;
    exports ca.pragmaticcoding.blog.extractors;
    exports ca.pragmaticcoding.blog.transformlist;
    exports ca.pragmaticcoding.blog.dirtyfx;
}