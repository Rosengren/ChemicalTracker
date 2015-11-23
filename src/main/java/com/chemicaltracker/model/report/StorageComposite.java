package com.chemicaltracker.model.report;

import com.chemicaltracker.model.Storage;

//import java.util.List;
import java.util.ArrayList;

import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.html.HtmlTags;

public class StorageComposite implements DocumentComponent {

    private static final int DEFAULT_FONT_SIZE = 28;
    private static final int SMALLER_FONT_SIZE = 6;
    private static final int SMALLEST_FONT_SIZE = 10;
    private static final Font BODY = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

    private Storage storage;
    private java.util.List<DocumentComponent> elements;

    public StorageComposite(Storage storage) {
        elements = new ArrayList<DocumentComponent>();
        this.storage = storage;
    }

    public void addElement(DocumentComponent element) {
        elements.add(element);
    }

    @Override
    public Paragraph getFormattedParagraph(int level) {
        Paragraph formatted = new Paragraph();

        // ADD HEADER
        List header = new List(false, false, level * 20);
        header.setListSymbol("");

        int fontSize = Math.max(DEFAULT_FONT_SIZE - level * SMALLER_FONT_SIZE, SMALLEST_FONT_SIZE);
        header.add(new ListItem(storage.getName(), new Font(Font.FontFamily.HELVETICA, fontSize, Font.BOLD)));
        header.add(new ListItem(storage.getDescription(), BODY));
        createEmptyLine(header, 2);

        // Add BODY
        List body = new List();
        body.setListSymbol("");
        for (DocumentComponent element : elements) {
            body.add(new ListItem(element.getFormattedParagraph(level + 1)));
            createEmptyLine(body, 1);
        }

        createEmptyLine(body, 2);

        formatted.add(header);
        formatted.add(body);

        return formatted;
    }

    private void createEmptyLine(List list, int number) {
        for (int i = 0; i < number; i++) {
            list.add(new ListItem());
        }
    }
}
