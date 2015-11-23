package com.chemicaltracker.model.report;

import com.itextpdf.text.List;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.ListItem;

import com.chemicaltracker.model.Storage;
import com.chemicaltracker.model.Chemical; // may be used in the future

public class ChemicalsComposite implements DocumentComponent {

    private static final int DEFAULT_FONT_SIZE = 28;
    private static final int SMALLER_FONT_SIZE = 6;
    private static final int SMALLEST_FONT_SIZE = 10;
    private static Font LIST_ITEM = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
    private static Font BODY = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

    private Storage storage;

    public ChemicalsComposite(Storage storage) {
        this.storage = storage;
    }

    @Override
    public Paragraph getFormattedParagraph(int level) {
        Paragraph formatted = new Paragraph();
        formatted.setIndentationLeft(level);

        // ADD HEADER
        List header = new List(false, false, level * 20);
        header.setListSymbol("");

        int fontSize = Math.max(DEFAULT_FONT_SIZE - level * SMALLER_FONT_SIZE, SMALLEST_FONT_SIZE);
        header.add(new ListItem(storage.getName(), new Font(Font.FontFamily.HELVETICA, fontSize, Font.BOLD)));
        header.add(new ListItem(storage.getDescription(), BODY));

        // ADD BODY
        List chemicalList = new List(false, false, level * 40); // convert to CONSTANT
        chemicalList.setListSymbol(""); // remove bullets

        if (storage.getStoredItemNames().isEmpty()) {
            chemicalList.add(new ListItem("There are no chemicals in this cabinet"));
            createEmptyLine(chemicalList, 1);
        } else {
            for (String chemicalName : storage.getStoredItemNames()) {
                chemicalList.add(new ListItem(chemicalName, LIST_ITEM));
                createEmptyLine(chemicalList, 1);
            }
        }

        createEmptyLine(chemicalList, 2);

        formatted.add(header);
        formatted.add(chemicalList);
        return formatted;
    }

    private static void createEmptyLine(List list, int number) {
        for (int i = 0; i < number; i++) {
            list.add(new ListItem());
        }
    }
}
