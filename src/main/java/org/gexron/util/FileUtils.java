package org.gexron.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.logging.Logger;

public class FileUtils {
    private static final Logger logger = Logger.getAnonymousLogger();

    private FileUtils() {

    }

    public static LocalDateTime getFileCreationDate(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
        Instant creationInstant = attrs.creationTime().toInstant();

        // Convert Instant to LocalDateTime with system default timezone
        return LocalDateTime.ofInstant(creationInstant, ZoneId.systemDefault());
    }

    public static void renameFile(File file, String newFileName) {
        String oldFilePath = file.getAbsolutePath();
        int delimiterIndex = oldFilePath.lastIndexOf(File.separator);
        String fileDirectory = oldFilePath.substring(0, delimiterIndex);
        String newFilePath = fileDirectory.concat(File.separator).concat(newFileName);
        File oldFileTrip = new File(oldFilePath);
        File newFileTrip = new File(newFilePath);
        if(!oldFileTrip.renameTo(newFileTrip))
            logger.warning("Failed to rename the file");
    }
}
