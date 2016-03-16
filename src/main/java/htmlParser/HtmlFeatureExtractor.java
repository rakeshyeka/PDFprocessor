package htmlParser;

import java.io.File;
import java.io.IOException;

/**
 * Created by rakesh on 16/3/16.
 */
public class HtmlFeatureExtractor extends FolderWalker {
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
        String outputDirectoryPath = getOutputDirectoryPath(
                currentDirectoryPath,
                inputNode,
                outputNode,
                "");
        parseFileAndExtractTextFeatures(fileName, inputFilePath, outputDirectoryPath);
    }

    public static void main(String args[]) throws IOException {
        HtmlToTextProcessor htmlToTextProcessor = new HtmlToTextProcessor();
        htmlToTextProcessor.execute();
    }

    private static void parseFileAndExtractTextFeatures(String fileName, String inputFile, String outputPath) {
        DomParser domParser = new DomParser(inputFile);
        while (domParser.getPagesIterator().hasNext()) {
            Page page = domParser.getPagesIterator().next();
            String pageTextFeatures = page.extractTextFeatures();
            int pageNumber = page.getPageNumber();
            String outputFile = Util.pathJoin(outputPath, fileName);
            outputFile = Util.pathJoin(outputFile, Integer.toString(pageNumber));
            Util.writeContentToFile(outputFile, pageTextFeatures);
        }
    }
}
