package clusterAnalysis;

import htmlParser.Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

public class ClusterAnalysisHelper {

    private static final String PCA_COMMAND = "cluster -f %s -g 7 -pg -u %s";
    private static final String K_MEANS_COMMAND = "cluster -f %s -k %s -r 10 -g 7 -u %s";

    public static int extractClusters(String pageFeatureFilePath, String outputPath) {
        File featureFile = new File(pageFeatureFilePath);
        runPCA(featureFile, outputPath);
        int k = identifyNumberOfMeansUsingPCAAnalysisResult(featureFile, outputPath);
        runKmeansClusering(featureFile, outputPath, k);
        return k;
    }

    public static int extractKfromPcaAnalysisContent(List<String> contentLines) {
        int k = 3;
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
        return k;
    }

    private static int identifyNumberOfMeansUsingPCAAnalysisResult(File featureFile, String outputPath) {
        String geneFile = Util.pathJoin(outputPath, featureFile.getName());
        geneFile = geneFile.replace(".txt", ClusterConstants.PCA_PC_TXT_EXTENSION);
        int k = 0;
        try {
            List<String> contentLines = Util.readContentFromFilePath(geneFile);
            k = extractKfromPcaAnalysisContent(contentLines);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return k;
    }

    private static boolean currentEigenSumLesserThanThreshold(float currentEigenSum, float eigenValueSum) {
        float eigenContribution = (currentEigenSum / eigenValueSum) * 100;
        return eigenContribution < ClusterConstants.EIGEN_THRESHOLD_PERCENT;
    }

    private static void runPCA(File featureFile, String outputPath) {
        String logMessage = String.format("Running PCA on file %s", featureFile.getAbsolutePath());
        Util.logMessage(Level.INFO, logMessage);
        String outputJobName = Util.pathJoin(outputPath, featureFile.getName());
        outputJobName = outputJobName.replace(".txt", "");
        String pcaCommand = String.format(PCA_COMMAND, featureFile.getAbsolutePath(), outputJobName);
        try {
            pdf2htmlConvertor.Util.runSystemCommand(pcaCommand);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void runKmeansClusering(File featureFile, String outputPath, int k) {
        String logMessage = String.format(
                "Running K means with k=%s on file %s", k, featureFile.getAbsolutePath());
        Util.logMessage(Level.INFO, logMessage);
        String outputJobName = Util.pathJoin(outputPath, featureFile.getName());
        outputJobName = outputJobName.replace(".txt", "");
        String kMeansCommand = String.format(K_MEANS_COMMAND,
                featureFile.getAbsolutePath(),
                k,
                outputJobName);
        try {
            pdf2htmlConvertor.Util.runSystemCommand(kMeansCommand);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
