package pdfProcessor.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class TestData {
	public static int getIntegerLessThan5() {
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
	
	public static String createAndWriteToTemporaryFile (String content) throws IOException {
	    File tempFile = File.createTempFile("PDFProcessor", "");
	    writeContentToFile(tempFile, content);
	    return tempFile.getAbsolutePath();
	}

	private static void writeContentToFile(File file,String content) throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		fileOutputStream.write(content.getBytes());
		fileOutputStream.close();
	}
}
