package com.chemicaltracker.model;

import com.itextpdf.text.Phrase;

public interface StorageComponent {

    public Phrase getFormattedPDF(final int level);
}
