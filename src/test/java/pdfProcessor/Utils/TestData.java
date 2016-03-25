package pdfProcessor.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

public class TestData {
    public static int randomIntegerLessThan5() {
        Random randomGenerator = new Random();
        return randomGenerator.nextInt(6);
    }

    public static void createAndWriteToFile(String fileName, String fileContent) throws IOException {
        File file = new File(fileName);

        if (!file.exists()) {
            file.createNewFile();
        }

        writeContentToFile(file, fileContent);
    }

    public static String createAndWriteToTemporaryFile(String content) throws IOException {
        File tempFile = File.createTempFile("PDFProcessor", "");
        writeContentToFile(tempFile, content);
        return tempFile.getAbsolutePath();
    }

    public static int randomNaturalNumber() {
        return randomNaturalNumber(100);
    }

    public static int randomNaturalNumber(int upperLimit) {
        Random randomGenerator = new Random();
        int randomWholeNumber = Math.round(randomGenerator.nextFloat() * (upperLimit - 1));
        return randomWholeNumber + 1;
    }

    public static Boolean randomBoolean() {
        return randomNaturalNumber(2) > 1;
    }

    public static float randomFloat() {
        Random randomGenerator = new Random();
        return randomGenerator.nextFloat() * 100;
    }

    public static String randomUUID() {
        return UUID.randomUUID().toString();
    }
    public static String randomString() {
        return randomUUID();
    }

    public static int convertToHexValue(int n) {
        return Integer.valueOf(String.valueOf(n), 16);
    }

    private static void writeContentToFile(File file, String content) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(content.getBytes());
        fileOutputStream.close();
    }


}