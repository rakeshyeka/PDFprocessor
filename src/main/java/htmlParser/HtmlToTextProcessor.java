package htmlParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class HtmlToTextProcessor extends FolderWalker {
	private static final String HINDI = "Hindi";
	private static final String ENGLISH = "English";

	@Override
	public void updateConfig() {
		Config config = new Config(false);
		config.setInputFolder("/home/rakesh/Copy/NCERT/02_HTML/Current/Psychology");
		config.setOutputFolder("/home/rakesh/Copy/NCERT/03_Text/Current/Psychology");
		setConfig(config);
	}

	@Override
	public void processHTMLFile(String inputFilePath, String currentDirectoryPath, File inputNode, File outputNode) {
		String fileName = inputFilePath.replace(currentDirectoryPath, "");
		fileName = Util.stripSlashes(fileName);
		if (getConfig().isMixedLanguage()) {
			String engFile = getEnglishOutputFilePath(currentDirectoryPath, fileName, inputNode, outputNode);
			String hinFile = getHindiOutputFilePath(currentDirectoryPath, fileName, inputNode, outputNode);
			parseInputFileIntoEnglishHindiOutputText(inputFilePath, engFile, hinFile);
		} else {
			String outputFile = getOutputFilePath(currentDirectoryPath, fileName, inputNode, outputNode, "");
			parseInputFileIntoOutputText(inputFilePath, outputFile);
		}
	}

	public static void main(String args[]) throws IOException {
		HtmlToTextProcessor htmlToTextProcessor = new HtmlToTextProcessor();
		htmlToTextProcessor.execute();
	}

	private static void parseInputFileIntoEnglishHindiOutputText(String inputFile, String engFile, String hinFile) {
		DomParser dom = new DomParser(inputFile);
		String englishPages = dom.getEnglishPages();
		Util.writeContentToFile(engFile, englishPages);

		String hindiPages = dom.getHindiPages();
		Util.writeContentToFile(hinFile, hindiPages);
	}

	private static void parseInputFileIntoOutputText(String inputFile, String outputFile) {
		DomParser dom = new DomParser(inputFile);
		String pages = dom.getPages();
		Util.writeContentToFile(outputFile, pages);
	}

	private static String getEnglishOutputFilePath(
			String currentDirectoryPath,
			String fileName,
			File inputDirectory,
			File outputDirectory) {
		return getOutputFilePath(currentDirectoryPath, fileName, inputDirectory, outputDirectory, ENGLISH);

	}

	private static String getHindiOutputFilePath(
			String currentDirectoryPath,
			String fileName,
			File inputDirectory,
			File outputDirectory) {
		return getOutputFilePath(currentDirectoryPath, fileName, inputDirectory, outputDirectory, HINDI);
	}
}
