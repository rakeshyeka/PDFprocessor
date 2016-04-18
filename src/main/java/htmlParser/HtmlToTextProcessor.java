package htmlParser;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class HtmlToTextProcessor extends FolderWalker {
	private static final String HINDI = "Hindi";
	private static final String ENGLISH = "English";

	@Override
	public void updateConfig() {
		TextPropertyVault.setFeatureListFormat("x\ty");
		TextPropertyVault.setDelimiter("\t");
		Config config = new Config(false);
		config.setInputFolder("/home/rakesh/Copy/NCERT/02_HTML/Current/Psychology");
		config.setOutputFolder("/home/rakesh/Copy/NCERT/03_Text/Current/Psychology");
		config.setParagraphForBold(true);
		config.setParagraphForColoured(true);
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
		ContentFileWriter englishFileWriter = new ContentFileWriter(engFile, false);
		englishFileWriter.write(englishPages);
		englishFileWriter.close();

		String hindiPages = dom.getHindiPages();
		ContentFileWriter hindiFileWriter = new ContentFileWriter(hinFile, false);
		hindiFileWriter.write(hindiPages);
		hindiFileWriter.close();
	}

	private static void parseInputFileIntoOutputText(String inputFile, String outputFile) {
		DomParser dom = new DomParser(inputFile);
		Iterator<Page> pagesIterator = dom.getPagesIterator();
		while (pagesIterator.hasNext()) {
			Page currentPage = pagesIterator.next();
			HtmlFeatureExtractor.parsePageAndExtractTextFeatures(inputFile, outputFile, currentPage);
		}
		String pages = dom.getPages();
		ContentFileWriter fileWriter = new ContentFileWriter(outputFile, false);
		fileWriter.write(pages);
		fileWriter.close();
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
