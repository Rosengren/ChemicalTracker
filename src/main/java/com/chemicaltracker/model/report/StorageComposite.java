package com.chemicaltracker.model.report;

import com.chemicaltracker.model.Storage;

import java.util.ArrayList;

import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;

public class StorageComposite extends AbstractDocumentComposite implements DocumentComponent {

    private java.util.List<DocumentComponent> elements;

    public StorageComposite(Storage storage) {
        elements = new ArrayList<DocumentComponent>();
        this.storage = storage;
    }

    public void addElement(DocumentComponent element) {
        elements.add(element);
    }

    @Override
    public Paragraph getFormattedParagraph(int level) {
        Paragraph content = new Paragraph();

        addHeader(content, level);
        addBody(content, level);

        return content;
    }

    private void addBody(Paragraph content, int level) {

        List body = new List();
        body.setListSymbol("");

        if (elements.isEmpty()) {
            body.setIndentationLeft((level + 1) * INDENT_WIDTH);
            body.add(new ListItem("No further storages"));
            createEmptyLine(body, 1);
        } else {
            for (DocumentComponent element : elements) {
                body.add(new ListItem(element.getFormattedParagraph(level + 1)));
                createEmptyLine(body, 1);
            }
        }

        content.add(body);
    }
}
