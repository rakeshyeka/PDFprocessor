package htmlParser;

import com.sun.org.apache.xerces.internal.impl.dv.xs.BooleanDV;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class FolderWalker {

	private static final String FOLDER_PARSE_FINISH = "Finished parsing Input Folder: %s";
	private static final String FOLDER_PARSE_START = "Started parsing Input Folder: %s";
	private static final String CURRENT_FILE_INFO_TEMPLATE = "Processing file %s";
	private static final String HTML_POSTFIX = ".html";
	private static final String TXT_POSTFIX = ".txt";
	private static Config config;

	public abstract void updateConfig();
	public abstract void processHTMLFile(String inputFilePath, String currentDirectoryPath, File inputNode, File outputNode);

	public static Config getConfig() {
		return FolderWalker.config;
	}
	
	public static void setConfig(Config config) {
		FolderWalker.config = config;
	}

	public void execute() throws IOException {
		// String inputFolder =
		// "/home/rakesh/Copy/Constitution/Consttn/bilingual-constitution/tempPDF";
		this.updateConfig();
		this.parseFileInFolder(config.getInputFolder(), config.getOutputFolder());
	}

	protected static String getOutputFilePath(
			String currentDirectoryPath,
			String fileName,
			File inputDirectory,
			File outputDirectory,
			String language) {
		String currentOutputDirectoryPath = getOutputDirectoryPath(
				currentDirectoryPath,
				inputDirectory,
				outputDirectory,
				language);

		String outputTxtFile = fileName.replace(HTML_POSTFIX, TXT_POSTFIX);

		File currentOutputDirectory = new File(currentOutputDirectoryPath);
		currentOutputDirectory.mkdirs();
		return Util.pathJoin(currentOutputDirectory.getAbsolutePath(), outputTxtFile);
	}

	protected static String getOutputDirectoryPath(
			String currentDirectoryPath,
			File inputDirectory,
			File outputDirectory,
			String language) {
		String inputDirectoryPath = inputDirectory.getAbsolutePath();
		String inputTrailingPath = currentDirectoryPath.replace(inputDirectoryPath, "");
		String outputFilePath = Util.pathJoin(outputDirectory.getAbsolutePath(), language);
		outputFilePath = Util.pathJoin(outputFilePath, inputTrailingPath);
		return outputFilePath;
	}

	private void parseFileInFolder(String inputFolder, String outputFolder) {
		File inputDirectory = new File(inputFolder);
		File outputDirectory = new File(outputFolder);
		if (inputDirectory.isDirectory()) {
			Util.logMessage(Level.INFO, String.format(FOLDER_PARSE_START, inputFolder));
			this.recurseDirectory(inputDirectory, inputDirectory, outputDirectory);
			Util.logMessage(Level.INFO, String.format(FOLDER_PARSE_FINISH, inputFolder));
		}
		Util.closeLogger();
	}

	private static void writeContentToFile(String fileName, String content) {
		PrintWriter out;
		try {
			out = new PrintWriter(fileName);
			out.println(content);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void extractTextFeatures(String inputFile, String outputPath) {
		DomParser domParser = new DomParser(inputFile);
		while (domParser.getPagesIterator().hasNext()) {
			Page page = domParser.getPagesIterator().next();
			String pageTextFeatures = page.extractTextFeatures();
			int pageNumber = page.getPageNumber();
			String outputFile = Util.pathJoin(outputPath, Integer.toString(pageNumber));
			writeContentToFile(outputFile, pageTextFeatures);
		}
	}

	private void recurseDirectory(File currentDirectory, File inputDirectory, File outputDirectory) {
		String currentDirectoryPath = currentDirectory.getAbsolutePath();
		List<String> childrenInCurrentDirectory = Arrays.asList(currentDirectory.list());
		Collections.sort(childrenInCurrentDirectory);
		for (String childName : childrenInCurrentDirectory) {
			File childEntity = new File(Util.pathJoin(currentDirectoryPath, childName));
			if (childEntity.isDirectory()) {
				recurseDirectory(childEntity, inputDirectory, outputDirectory);
			} else if (endsWithHTMLTag(childName)) {
				String inputFilePath = Util.pathJoin(currentDirectoryPath, childName);
				Util.logMessage(Level.INFO, String.format(CURRENT_FILE_INFO_TEMPLATE, inputFilePath));
				processHTMLFile(inputFilePath, currentDirectoryPath, inputDirectory, outputDirectory);
			}
		}
	}

	private Boolean endsWithHTMLTag(String fileName) {
		Pattern xmlRegex = Pattern.compile(".html$");
		Matcher m = xmlRegex.matcher(fileName);
		return m.find();
	}
}
