package htmlParser;

import clusterAnalysis.PageCluster;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class HtmlToTextProcessor extends FolderWalker {
    private static final String HINDI = "Hindi";
    private static final String ENGLISH = "English";

    @Override
    public void updateConfig() {
        TextPropertyVault.setFeatureListFormat("x\ty\tfs\tl\tfc\tfb");
        TextPropertyVault.setDelimiter("\t");
        Config config = new Config(false);
        config.setInputFolder("/home/rakesh/Dropbox/NCERT/02_HTML/Current/Psychology/Psychology-XI/temp");
        config.setIntermediateFolder("/home/rakesh/Dropbox/NCERT/02_5_Features/Current/Psychology/Psychology-XI/temp");
        config.setOutputFolder("/home/rakesh/Dropbox/NCERT/03_Text/Current/Psychology/Psychology-XI/temp");
        config.setParagraphForBold(true);
        config.setParagraphForColoured(true);
        config.setSortContent(true);
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
            File intermediateOutputNode = new File(getConfig().getIntermediateFolder());
            String intermediateOutputFile = getOutputFilePath(
                    currentDirectoryPath,
                    fileName,
                    inputNode,
                    intermediateOutputNode,
                    "");
            parseInputFileIntoOutputText(inputFilePath, outputFile, intermediateOutputFile);
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

    private static void parseInputFileIntoOutputText(
            String inputFilePath,
            String outputFile,
            String intermediateOutputFilePath) {
        DomParser dom = new DomParser(inputFilePath);
        Iterator<Page> pagesIterator = dom.getPagesIterator();
        ContentFileWriter fileWriter = new ContentFileWriter(outputFile, false);
        File inputFile = new File(inputFilePath);
        File intermediateOutputFile = new File(intermediateOutputFilePath);
        while (pagesIterator.hasNext()) {
            Page currentPage = pagesIterator.next();
            currentPage.sortTextAndRemoveDuplicateContent();
            String inputFileName = inputFile.getName();
            String pageOutputFile = HtmlFeatureExtractor.parsePageAndExtractTextFeatures(
                    inputFileName, intermediateOutputFile.getParent(), currentPage);
            inputFileName = inputFileName.replace(".html", "");
            String clusterOutputFilePath = Util.pathJoin(intermediateOutputFile.getParent(), inputFileName);

            PageCluster pageCluster = new PageCluster(currentPage);
            pageCluster.runHierarchicalClusterForFile(pageOutputFile, clusterOutputFilePath);
            pageCluster.serializePageUsingHierarchical(pageOutputFile);

            fileWriter.write(Util.newLineJoin(
                    Constants.PAGE_DECORATION_BOUNDARY, pageCluster.getPage().toString()));
            fileWriter.write("\n");
        }

        /*String pages = dom.getPages();
        fileWriter.write(pages);*/
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
