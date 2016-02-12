package com.chemicaltracker.persistence.model;

import com.itextpdf.text.Phrase;

public interface StorageComponent {
    Phrase getFormattedPDF(final int level);
}
