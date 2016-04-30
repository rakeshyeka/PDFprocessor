package htmlParser;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.VectorWritable;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class HtmlFeatureExtractor extends FolderWalker {

    public static final String OUTPUT_FILE_EXTENSION = ".txt";

    @Override
    public void updateConfig() {
        Config config = new Config(false);
        config.setInputFolder("/home/rakesh/Copy/NCERT/02_HTML/Current/Psychology");
        config.setOutputFolder("/home/rakesh/Copy/NCERT/03_5_Features/Tabbed/Psychology");
        TextPropertyVault.setFeatureListFormat("x\ty\tfs\tl\tfc\tfb");
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

    public static String parsePageAndExtractTextFeatures(String fileName, String outputPath, Page page) {
        int pageNumber = page.getPageNumber();
        fileName = fileName.replace(".html", "");
        String outputFilePath = Util.pathJoin(outputPath, fileName);
        String outputFile = Util.pathJoin(outputFilePath, Integer.toString(pageNumber));
        outputFile = outputFile + OUTPUT_FILE_EXTENSION;
        ContentFileWriter contentFileAppender = new ContentFileWriter(outputFile, true);
        contentFileAppender.write("ID\t" + TextPropertyVault.getFeatureListFormat());
        SequenceFile.Writer writer = getVectorFileWriter(outputFile);
        Iterator<Text> textIterator = page.getTextIterator();
        int counter = 0;
        while (textIterator.hasNext()) {
            Text text = textIterator.next();
            String textFeatures = text.getTextFeatures();
            //writeVectorsToFileForMahout(textFeatures, writer);
            contentFileAppender.write(Integer.toString(counter) + "\t" + textFeatures);
            counter += 1;
        }
        contentFileAppender.close();
        return outputFile;
    }

    private static void parseFileAndExtractTextFeatures(String fileName, String inputFile, String outputPath) {
        DomParser domParser = new DomParser(inputFile);
        Iterator<Page> pageIterator = domParser.getPagesIterator();
        while (pageIterator.hasNext()) {
            Page page = pageIterator.next();
            parsePageAndExtractTextFeatures(fileName, outputPath, page);
        }
    }

    private static SequenceFile.Writer getVectorFileWriter(String outputFile) {
        FileSystem fs = null;
        SequenceFile.Writer writer = null;
        Path path = new Path(outputFile);
        Configuration conf = new Configuration();
        try {
            fs = FileSystem.get(conf);
            writer = new SequenceFile.Writer(fs, conf, path, LongWritable.class, VectorWritable.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return writer;
    }

    private static void writeVectorsToFileForMahout(String textFeatures, SequenceFile.Writer writer) {
        String[] stringFeatures = textFeatures.split(", ");
        double[] doubleFeatures = new double[stringFeatures.length];
        for (int i = 0; i < stringFeatures.length; i++) {
            doubleFeatures[i] = Double.valueOf(stringFeatures[i]);
        }
        DenseVector vector = new DenseVector(doubleFeatures);
        try {
            VectorWritable vec = new VectorWritable();
            vec.set(vector);
            writer.append(new LongWritable(), vec);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
