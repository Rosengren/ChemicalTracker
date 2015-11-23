package com.chemicaltracker.model.report;

import com.chemicaltracker.model.Storage;

import java.util.List;
import java.util.ArrayList;

import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;

public class StorageComposite implements DocumentComponent {

    private static final int DEFAULT_FONT_SIZE = 30;
    private static final Font BODY = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

    private Storage storage;
    private String storageType;
    private List<DocumentComponent> elements;

    public StorageComposite(Storage storage, String storageType) {
        elements = new ArrayList<DocumentComponent>();
        this.storage = storage;
        this.storageType = storageType;
    }

    public void addElement(DocumentComponent element) {
        elements.add(element);
    }

    @Override
    public Paragraph getFormattedParagraph(int level) { // level must start at 0
        Paragraph formatted = new Paragraph();

        // ADD HEADER
        Paragraph header = new Paragraph();

        int fontSize = DEFAULT_FONT_SIZE - level * 4;
        header.add(new Paragraph(storageType + ": " + storage.getName(), new Font(Font.FontFamily.HELVETICA, fontSize, Font.BOLD)));
        header.add(new Paragraph(storage.getDescription(), BODY));

        // Add BODY
        Paragraph body = new Paragraph();
        for (DocumentComponent element : elements) {
            body.add(element.getFormattedParagraph(level + 1));
            createEmptyLine(body, 1);
        }

        formatted.add(header);
        formatted.add(body);

        return formatted;
    }

    private static void createEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}
