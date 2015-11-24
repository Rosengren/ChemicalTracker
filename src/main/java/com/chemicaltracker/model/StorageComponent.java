package com.chemicaltracker.model;

import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;

public interface StorageComponent {

    public Phrase getFormattedPDF(int level);
}
