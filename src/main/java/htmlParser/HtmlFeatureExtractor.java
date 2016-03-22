package htmlParser;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class HtmlFeatureExtractor extends FolderWalker {
    @Override
    public void updateConfig() {
        Config config = new Config(false);
        config.setInputFolder("/home/rakesh/Copy/NCERT/02_HTML/Current/Psychology");
        config.setOutputFolder("/home/rakesh/Copy/NCERT/03_5_Features/Current/Psychology");
        TextPropertyVault.setFeatureListFormat("x y fs l");
        this.setConfig(config);
    }

    @Override
    public void processHTMLFile(String inputFilePath, String currentDirectoryPath, File inputNode, File outputNode) {
        String fileName = inputFilePath.replace(currentDirectoryPath, "");
        fileName = Util.stripSlashes(fileName);
        String outputDirectoryPath = getOutputDirectoryPath(
                currentDirectoryPath,
                inputNode,
                outputNode,
                "");
        parseFileAndExtractTextFeatures(fileName, inputFilePath, outputDirectoryPath);
    }

    public static void main(String args[]) throws IOException {
        HtmlFeatureExtractor htmlFeatureExtractor = new HtmlFeatureExtractor();
        htmlFeatureExtractor.execute();
    }

    private static void parseFileAndExtractTextFeatures(String fileName, String inputFile, String outputPath) {
        DomParser domParser = new DomParser(inputFile);
        Iterator<Page> pageIterator = domParser.getPagesIterator();
        while (pageIterator.hasNext()) {
            Page page = pageIterator.next();
            String pageTextFeatures = page.extractTextFeatures();
            int pageNumber = page.getPageNumber();
            fileName = fileName.replace(".html", "");
            String outputFile = Util.pathJoin(outputPath, fileName);
            outputFile = Util.pathJoin(outputFile, Integer.toString(pageNumber));
            outputFile = outputFile + ".txt";
            Util.writeContentToFile(outputFile, pageTextFeatures);
        }
    }
}
