package com.chemicaltracker.model.report;

import com.itextpdf.text.Paragraph;

public interface DocumentComponent {

    public Paragraph getFormattedParagraph(int level);
}
