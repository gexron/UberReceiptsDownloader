package org.gexron.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.io.File;
import java.io.IOException;

public class PDFUtils {
    private static final Log logger = LogFactory.getLog(PDFUtils.class);

    private PDFUtils() {

    }

    public static String parseFile(File receiptFile) throws IOException {
        try {
            PDDocument document = PDDocument.load(receiptFile);

            PDFTextStripperByArea stripper = new PDFTextStripperByArea();
            stripper.setSortByPosition(true);

            stripper.addRegion("wholePage", new java.awt.Rectangle(0, 0, 600, 600));

            stripper.extractRegions(document.getPage(0));
            String page = stripper.getTextForRegion("wholePage");

            document.close();

            return page;
        }
        catch (IOException e) {
            logger.error("Error while reading file: " + receiptFile.getName());
            throw e;
        }
    }
}
