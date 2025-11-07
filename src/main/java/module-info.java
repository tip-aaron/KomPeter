module kompeter {
    requires java.desktop;
    requires java.management;
    requires java.base;
    requires java.sql;
    requires com.formdev.flatlaf;
    requires com.formdev.flatlaf.extras;
    requires com.miglayout.swing;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires org.apache.commons.lang3;
    requires org.apache.commons.text;
    requires org.apache.pdfbox;
    requires org.jfree.jfreechart;
    requires org.commonmark;
    requires io.github.classgraph;
    requires lombok;
    requires modal.dialog;
    requires static org.jetbrains.annotations;

    exports kompeter;
    exports kompeter.ui.frames;
}
