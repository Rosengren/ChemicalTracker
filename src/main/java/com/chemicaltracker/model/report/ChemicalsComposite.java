package com.chemicaltracker.model.report;

import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;

import com.chemicaltracker.model.Storage;
import com.chemicaltracker.model.Chemical; // may be used in the future

public class ChemicalsComposite extends AbstractDocumentComposite implements DocumentComponent {

    public ChemicalsComposite(Storage storage) {
        this.storage = storage;
    }

    @Override
    public Paragraph getFormattedParagraph(int level) {
        Paragraph content = new Paragraph();

        addHeader(content, level);

        addBody(content, level);

        return content;
    }

    private void addBody(Paragraph content, int level) {

        List chemicalList = new List(NOT_NUMBERED, NOT_LETTERED, level * 2 * INDENT_WIDTH);
        chemicalList.setListSymbol("");

        if (storage.getStoredItemNames().isEmpty()) {
            chemicalList.add(new ListItem("There are no chemicals in this cabinet"));
        } else {
            for (String chemicalName : storage.getStoredItemNames()) {
                chemicalList.add(new ListItem(chemicalName, LIST_ITEM));
            }
        }

        content.add(chemicalList);
    }
}
