package com.chemicaltracker.persistence.model;

import com.itextpdf.text.*;
import com.itextpdf.text.List;

import java.util.ArrayList;

import static com.itextpdf.text.Font.FontFamily.HELVETICA;
import static com.itextpdf.text.Font.BOLD;

public abstract class AbstractStorageComponent {

    protected static final boolean NOT_NUMBERED = false;
    protected static final boolean NOT_LETTERED = false;

    protected static final int INDENT_WIDTH = 20;

    protected static final int DEFAULT_FONT_SIZE = 28;
    protected static final int SMALLER_FONT_SIZE = 6;
    protected static final int SMALLEST_FONT_SIZE = 12;

    protected static final Font BODY = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
    protected static final Font LIST_ITEM = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

    protected String name;
    protected java.util.List<StorageComponent> elements;

    public AbstractStorageComponent() {
        elements = new ArrayList<>();
    }

    abstract protected String getDescription();

    protected void createEmptyLine(final List list, final int number) {
        for (int i = 0; i < number; i++) {
            list.add(new ListItem("\n"));
        }
    }

    protected void addHeader(final Paragraph content, final int level) {

        final List header = new List(NOT_NUMBERED, NOT_LETTERED, level * INDENT_WIDTH);
        header.setListSymbol(""); // removes bullets

        int fontSize = Math.max(DEFAULT_FONT_SIZE - level * SMALLER_FONT_SIZE, SMALLEST_FONT_SIZE);
        header.add(new ListItem(name, new Font(HELVETICA, fontSize, BOLD)));
        header.add(new ListItem(getDescription(), BODY));

        content.add(header);
    }

    public Phrase getFormattedPDF(final int level) {
        Paragraph content = new Paragraph();

        addHeader(content, level);

        addBody(content, level);

        return content;
    }

    private void addBody(Paragraph content, int level) {

        com.itextpdf.text.List body = new com.itextpdf.text.List();
        body.setListSymbol("");

        if (elements.isEmpty()) {
            body.setIndentationLeft((level + 1) * INDENT_WIDTH);
            body.add(new ListItem("No further elements"));
        } else {
            for (StorageComponent element : elements) {
                body.add(new ListItem(element.getFormattedPDF(level + 1)));
                createEmptyLine(body, 1);
            }
        }

        content.add(body);
    }

    public void addElement(final StorageComponent element) {
        elements.add(element);
    }

    public java.util.List<StorageComponent> getElements() {
        return elements;
    }
}


