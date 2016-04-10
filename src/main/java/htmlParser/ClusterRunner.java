package htmlParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

public class ClusterRunner {

    private static final String PCA_COMMAND = "cluster -f %s -g 7 -pg";
    private static final String K_MEANS_COMMAND = "cluster -f %s -k %s -r 10 -g 7";
    public static final int EIGEN_THRESHOLD = 95;

    public static void extractClusters(String outputFilePath) {
        File outputFile = new File(outputFilePath);
        runPCA(outputFile);
        int k = identifyNumberOfMeans(outputFile);
        runKmeansClusering(outputFile, k);
    }

    private static int identifyNumberOfMeans(File outputFile) {
        int k = 3;
        String geneFile = outputFile.getAbsolutePath().replace(".txt", "_pca_gene.pc.txt");
        try {
            List<String> contentLines = Util.readContentFromFilePath(geneFile);
            Iterator<String> linesIterator = contentLines.iterator();
            List<Float> principleComponents = new ArrayList<Float>();
            float eigenValueSum = 0;
            for (int index = 0; linesIterator.hasNext(); index++) {
                String line = linesIterator.next();
                String[] lineSplitValues = line.split("\t");
                if (index >= 2) {
                    float eigenValue = Float.valueOf(lineSplitValues[0]);
                    principleComponents.add(eigenValue);
                    eigenValueSum += eigenValue;
                }
            }

            Iterator<Float> pcIterator = principleComponents.iterator();
            float currentEigenSum = 0;
            for (int index = 1; pcIterator.hasNext()
                    && currentEigenSumLesserThanThreshold(currentEigenSum, eigenValueSum); index++) {
                currentEigenSum += pcIterator.next();
                k = index;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return k;
    }

    private static boolean currentEigenSumLesserThanThreshold(float currentEigenSum, float eigenValueSum) {
        float eigenContribution = (currentEigenSum/eigenValueSum) * 100;
        return eigenContribution < EIGEN_THRESHOLD;
    }

    private static void runPCA(File outputFile) {
        String logMessage = String.format("Running PCA on file %s", outputFile.getAbsolutePath());
        Util.logMessage(Level.INFO, logMessage);
        String pcaCommand = String.format(PCA_COMMAND, outputFile.getAbsolutePath());
        try {
            pdf2htmlConvertor.Util.runSystemCommand(pcaCommand);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void runKmeansClusering(File outputFile, int k) {
        String logMessage = String.format(
                "Running K means with k=%s on file %s", k, outputFile.getAbsolutePath());
        Util.logMessage(Level.INFO, logMessage);
        String kMeansCommand = String.format(K_MEANS_COMMAND, outputFile.getAbsolutePath(), k);
        try {
            pdf2htmlConvertor.Util.runSystemCommand(kMeansCommand);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
