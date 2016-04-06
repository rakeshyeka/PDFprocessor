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

    private static void parseFileAndExtractTextFeatures(String fileName, String inputFile, String outputPath) {
        DomParser domParser = new DomParser(inputFile);
        Iterator<Page> pageIterator = domParser.getPagesIterator();
        while (pageIterator.hasNext()) {
            Page page = pageIterator.next();
            int pageNumber = page.getPageNumber();
            fileName = fileName.replace(".html", "");
            String outputFile = Util.pathJoin(outputPath, fileName);
            outputFile = Util.pathJoin(outputFile, Integer.toString(pageNumber));
            outputFile = outputFile + OUTPUT_FILE_EXTENSION;
            ContentFileWriter contentFileAppender = new ContentFileWriter(outputFile, true);
            //contentFileAppender.write(TextPropertyVault.getFeatureListFormat());
            FileSystem fs = null;
            SequenceFile.Writer writer = null;
            Path path = new Path(outputFile);
            Configuration conf = new Configuration();
            try {
                fs = FileSystem.get(conf);
                writer = new SequenceFile.Writer(fs, conf, path, LongWritable.class, VectorWritable.class);
            } catch(Exception e){
                e.printStackTrace();
            }
            Iterator<Text> textIterator = page.getTextIterator();
            while (textIterator.hasNext()) {
                Text text = textIterator.next();
                String textFeatures = text.getTextFeatures();
                //writeVectorsToFileForMahout(textFeatures, writer);
                contentFileAppender.write(textFeatures);
            }
            contentFileAppender.close();
        }
    }

    private static void writeVectorsToFileForMahout(String textFeatures, SequenceFile.Writer writer){
        String[] stringFeatures = textFeatures.split(", ");
        double[] doubleFeatures = new double[stringFeatures.length];
        for (int i=0; i < stringFeatures.length; i++) {
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
