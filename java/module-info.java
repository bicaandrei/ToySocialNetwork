module ubb_221.toysocialnetworkgui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens ubb_221.toysocialnetworkgui to javafx.fxml;
    opens ubb_221.toysocialnetworkgui.domain to javafx.base;
    opens ubb_221.toysocialnetworkgui.controller to javafx.fxml;
    exports ubb_221.toysocialnetworkgui;
    exports ubb_221.toysocialnetworkgui.controller;
    exports ubb_221.toysocialnetworkgui.domain;
}