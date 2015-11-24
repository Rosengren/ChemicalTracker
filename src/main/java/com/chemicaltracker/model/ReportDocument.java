package com.chemicaltracker.model;

//import com.chemicaltracker.model.report.*;

import java.util.ArrayList;

import java.util.Map;
import java.util.HashMap;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;

public class ReportDocument {

    public static final String DEFAULT_TITLE = "Chemical Report";
    public static final String DEFAULT_SUBTITLE = "Summary of all identified chemicals";

    private static Font TITLE = new Font(Font.FontFamily.HELVETICA, 30, Font.BOLD);
    private static Font SUBTITLE = new Font(Font.FontFamily.HELVETICA, 24, Font.NORMAL);
    private static Font HEADER = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD);
    private static Font SUBHEADER = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
    private static Font LIST_TITLE = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
    private static Font LIST_ITEM = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
    private static Font BODY = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
    private static Font FOOTER = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
    private static Font TIMESTAMP = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);

    //public static Document createPDF(final String file, final String title, final Storage location, final Map<Storage, java.util.List<Storage>> roomCabinetMap) {
    public static Document createPDF(final String file, final String title, final StorageComponent component) {

        Document document = null;

        try {
            document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            addMetaData(document);

            addHeader(document, title);

            addBody(document, component);

            document.close();

        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return document;

    }

    private static void addBody(Document document, StorageComponent component) throws DocumentException {
        document.add(component.getFormattedPDF(0));
    }

    private static void addMetaData(Document document) {
        document.addTitle("Chemical Report");
        document.addSubject("Chemical Report");
        document.addAuthor("Chemical Tracker");
        document.addCreator("Chemical Tracker");
    }

    private static void addHeader(Document document, String title)
            throws DocumentException {

        Paragraph header = new Paragraph();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Paragraph timestamp = new Paragraph("Report created on "
                + simpleDateFormat.format(new Date()), TIMESTAMP);
        timestamp.setAlignment(Element.ALIGN_RIGHT);

        header.add(timestamp);
        header.add(new Paragraph(title, TITLE));
        header.add(new Paragraph(DEFAULT_SUBTITLE, SUBTITLE));


        createEmptyLine(header, 1);

        document.add(header);
    }

    private static void createEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}
