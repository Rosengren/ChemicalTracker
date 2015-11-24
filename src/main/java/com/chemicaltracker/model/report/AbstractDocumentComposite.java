package com.chemicaltracker.model.report;

import com.chemicaltracker.model.Storage;

import com.itextpdf.text.Paragraph;

import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Font;

import static com.itextpdf.text.Font.FontFamily.HELVETICA;
import static com.itextpdf.text.Font.BOLD;
import static com.itextpdf.text.Chunk.NEWLINE;

public abstract class AbstractDocumentComposite {

    protected static final boolean NOT_NUMBERED = false;
    protected static final boolean NOT_LETTERED = false;

    protected static final int INDENT_WIDTH = 20;

    protected static final int DEFAULT_FONT_SIZE = 28;
    protected static final int SMALLER_FONT_SIZE = 6;
    protected static final int SMALLEST_FONT_SIZE = 10;

    protected static final Font BODY = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
    protected static final Font LIST_ITEM = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

    protected Storage storage;

    protected void createEmptyLine(List list, int number) {
        for (int i = 0; i < number; i++) {
            list.add(new ListItem("\n"));
        }
    }

    protected void addHeader(Paragraph content, int level) {

        List header = new List(NOT_NUMBERED, NOT_LETTERED, level * INDENT_WIDTH);
        header.setListSymbol(""); // removes bullets

        int fontSize = Math.max(DEFAULT_FONT_SIZE - level * SMALLER_FONT_SIZE, SMALLEST_FONT_SIZE);
        header.add(new ListItem(storage.getName(), new Font(HELVETICA, fontSize, BOLD)));
        header.add(new ListItem(storage.getDescription(), BODY));

        content.add(header);
    }
}
