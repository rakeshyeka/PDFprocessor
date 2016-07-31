package clusterAnalysis;

import htmlParser.Text;
import htmlParser.Util;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

public class HierarchicalClusterAnalysisHelper {

    private List<List<Text>> clusterGroups;
    private List<Text> content;
    private static float GROUP_CLUSTERING_THRESHOLD = (float) 0.9;

    private static final String HIERARCHICAL_COMMAND = "cluster -f %s -m s -g 7 -u %s";
    private Map<Integer, float[]> gtr = new HashMap<Integer, float[]>();

    public HierarchicalClusterAnalysisHelper(Map<Integer, float[]> gtr){
        this.gtr = gtr;
    }

    public HierarchicalClusterAnalysisHelper(){
    }

    public void parseGTR(List<List<Text>> clusterGroups, List<Text> content) {
        this.clusterGroups = clusterGroups;
        this.content = content;
        int tree = this.gtr.size();
        recurseTree(tree);
    }

    public void readGTRFile(String inputGtrFilePath) {
        List<String> gtrFileContent = new ArrayList<String>();
        try {
            gtrFileContent = Util.readContentFromFilePath(inputGtrFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int index = 0; index < gtrFileContent.size(); index++) {
            String line = gtrFileContent.get(index);
            String[] splits = line.split("\t");
            int[] head = getIndexAndType(splits[0]);
            int[] child1 = getIndexAndType(splits[1]);
            int[] child2 = getIndexAndType(splits[2]);
            float costOfAlignment = Float.parseFloat(splits[3]);
            float[] entry = new float[] {child1[0], child1[1], child2[0], child2[1], costOfAlignment};
            gtr.put(head[0], entry);
        }
    }

    private List<Text> recurseTree(int head) {
        float[] entry = this.gtr.get(head);
        List<Text> leftChildPoints;
        List<Text> rightChildPoints;
        List<Text> totalChildPoints = new ArrayList<Text>();

        // left child
        leftChildPoints = processChildAndFetchChildNodes(entry[0], entry[1]);

        // right child
        rightChildPoints = processChildAndFetchChildNodes(entry[2], entry[3]);

        if (entry[4] < GROUP_CLUSTERING_THRESHOLD
                || leftChildPoints == null
                || rightChildPoints == null) {
            if (leftChildPoints != null) {
                this.clusterGroups.add(leftChildPoints);
            }
            if (rightChildPoints != null) {
                this.clusterGroups.add(rightChildPoints);
            }

            return null;
        } else {
            totalChildPoints.addAll(leftChildPoints);
            totalChildPoints.addAll(rightChildPoints);
        }

        return totalChildPoints;
    }

    private List<Text> processChildAndFetchChildNodes(float floatChildIndex, float isLeaf) {
        List<Text> childPoints;
        int childIndex = (int) floatChildIndex;
        if (isLeaf == 1) {
            // isLeaf
            childPoints = new ArrayList<Text>();
            childPoints.add(this.content.get(childIndex));
        } else {
            childPoints = recurseTree(childIndex);
        }
        return childPoints;
    }

    private int[] getIndexAndType(String Node) {
        String index = Util.substringRegex(Node, "(?<=GENE)[0-9]+(?=X)");
        Boolean isLeaf = index != null;
        if (!isLeaf) {
            index = Util.substringRegex(Node, "(?<=NODE)[0-9]+(?=X)");
        }

        return new int[]{
                Integer.parseInt(index),
                isLeaf ? 1:0};
    }

    public static void runHierarchicalClustering(File featureFile, String outputPath) {
        String logMessage = String.format(
                "Running Hierarchical clustering on file %s", featureFile.getAbsolutePath());
        Util.logMessage(Level.INFO, logMessage);
        String outputJobName = Util.pathJoin(outputPath, featureFile.getName());
        outputJobName = outputJobName.replace(".txt", "");
        String hierarchicalCommand = String.format(HIERARCHICAL_COMMAND,
                featureFile.getAbsolutePath(),
                outputJobName);
        try {
            pdf2htmlConvertor.Util.runSystemCommand(hierarchicalCommand);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
