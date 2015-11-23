package com.chemicaltracker.model.report;

import com.itextpdf.text.List;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.ListItem;

import com.chemicaltracker.model.Storage;
import com.chemicaltracker.model.Chemical; // may be used in the future

public class ChemicalsComposite implements DocumentComponent {

    private static final int DEFAULT_FONT_SIZE = 30;
    private static Font LIST_ITEM = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
    private static Font BODY = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

    private static final String storageType = "Cabinet: "; // May change this in the future
    private Storage storage;

    public ChemicalsComposite(Storage storage) {
        this.storage = storage;
    }

    @Override
    public Paragraph getFormattedParagraph(int level) {
        Paragraph formatted = new Paragraph();

        // ADD HEADER
        Paragraph header = new Paragraph();

        int fontSize = DEFAULT_FONT_SIZE - level * 4;
        header.add(new Paragraph(storageType + ": " + storage.getName(), new Font(Font.FontFamily.HELVETICA, fontSize, Font.BOLD)));
        header.add(new Paragraph(storage.getDescription(), BODY));

        // ADD BODY
        Paragraph body = new Paragraph();
        List chemicalList = new List(false, false, 60);
        chemicalList.setListSymbol(""); // remove bullets

        if (storage.getStoredItemNames().isEmpty()) {
            chemicalList.add(new ListItem("There are no chemicals in this " + storageType));
        } else {
            for (String chemicalName : storage.getStoredItemNames()) {
                chemicalList.add(new ListItem(chemicalName, LIST_ITEM));
            }
        }

        body.add(chemicalList);
        createEmptyLine(body, 1);

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
