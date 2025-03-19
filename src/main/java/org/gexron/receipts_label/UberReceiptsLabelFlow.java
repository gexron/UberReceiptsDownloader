package org.gexron.receipts_label;

import org.gexron.util.LocationUtils;
import org.gexron.util.FileUtils;
import org.gexron.util.PDFUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class UberReceiptsLabelFlow implements ReceiptsLabelFlow {

    private static final Logger logger = Logger.getAnonymousLogger();

    private static final String COMPANY_NAME = "EBC";

    private final LocalDateTime programStartTime = LocalDateTime.now();

    private int receiptCounter;
    @Override
    public void process() {
        logger.info("Start determining downloaded receipts");
        List<File> downloadedReceipts = getDownloadedReceipts();

        logger.info("Start labeling receipts");
        labelCompanyReceipts(downloadedReceipts);
        logger.info("Finished labeling receipts");
    }

    private List<File> getDownloadedReceipts() {
        String downloadsDirPath = System.getProperty("user.home") + File.separator + "Downloads";
        File downloadsDir = new File(downloadsDirPath);

        return Arrays.stream(downloadsDir.listFiles())
                .filter(file -> file.getName().endsWith(".pdf"))
                .filter(file -> {
                    try {
                        return FileUtils.getFileCreationDate(file.getPath()).isAfter(programStartTime);
                    } catch (IOException e) {
                        logger.warning("Couldn't read attributes of the file named " + file.getName());
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

    private void labelCompanyReceipts(List<File> downloadedReceipts) {
        downloadedReceipts.stream()
                .filter(file -> {
                    try {
                        return isCompanyRelatedReceipt(file);
                    } catch (IOException e) {
                        logger.warning("Failed to label file named + " + file.getName());
                    }
                    return false;
                })
                .forEach(this::labelReceiptFile);
    }

    private boolean isCompanyRelatedReceipt(File receiptFile) throws IOException {
        String receiptContent = PDFUtils.parseFile(receiptFile);
        String[] pdfLines = receiptContent.split(System.lineSeparator());
        List<String> tripEndpoints = getTripEndpoints(pdfLines);
        return LocationUtils.isTripCompanyRelated(tripEndpoints);
    }

    private List<String> getTripEndpoints(String[] pdfLines) {
        return Arrays.stream(pdfLines)
                .filter(line -> line.contains("|") && line.contains(":"))
                .flatMap(line -> Arrays.stream(line.split(" \\|")))
                .filter(line -> !line.contains(":"))
                .collect(Collectors.toList());
    }

    public void labelReceiptFile(File receiptFile) {
        String newFileName = COMPANY_NAME
                .concat("_")
                .concat(String.valueOf(receiptCounter++))
                .concat(".pdf");

        FileUtils.renameFile(receiptFile, newFileName);
    }
}
