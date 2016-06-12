package pdfProcessor;

import clusterAnalysis.HierarchicalClusterAnalysisHelper;
import pdfProcessor.Utils.HierarchicalTreeNode;
import htmlParser.Text;
import org.junit.Before;
import org.junit.Test;
import pdfProcessor.Utils.TestData;
import pdfProcessor.Utils.TextTestHelper;

import java.util.*;

public class HierarchicalClusterAnalysisHelperTest extends BaseTest {

    public static final float GREATER_THAN_THRESHOLD = (float) 0.99;
    public static final float LESSER_THAN_THRESHOLD = (float) 0.5;

    @Before
    public void setup(){
    }

    @Test
    public void parseGTRShouldParseGTRContent(){
        Map<Integer, float[]> gtr = new HashMap<Integer, float[]>();
        HierarchicalClusterAnalysisHelper hierarchicalHelper =
                new HierarchicalClusterAnalysisHelper(gtr);
        int treeNodeSize = TestData.randomNaturalNumber(30);
        List<Text> content = generateTexts(treeNodeSize);
        List<List<Text>> clusterGroup = generateClusterGroups(content);
        gtr = generateValidGTR(clusterGroup);

        HierarchicalClusterAnalysisHelper hcah = new HierarchicalClusterAnalysisHelper(gtr);
        List<List<Text>> expectedClusterGroup = new ArrayList<List<Text>>();
        hcah.parseGTR(expectedClusterGroup, content);

        verifyGtrResponse(clusterGroup, expectedClusterGroup);
    }

    private void verifyGtrResponse(List<List<Text>> clusterGroup, List<List<Text>> expectedClusterGroup) {

    }

    private List<List<Text>> generateClusterGroups(List<Text> content) {
        List<List<Text>> clusterGroup = new ArrayList<List<Text>>();
        int contentSize = content.size();
        int numberOfClusters = TestData.randomNaturalNumber(contentSize);
        List<Text> cluster = new ArrayList<Text>();
        for (int i=0; i < content.size(); i++) {
            if (i % (contentSize/numberOfClusters) == 0) {
                clusterGroup.add(cluster);
                cluster = new ArrayList<Text>();
            }
            cluster.add(content.get(i));
        }
        clusterGroup.add(cluster);

        return clusterGroup;
    }

    private Map<Integer, float[]> generateValidGTR(List<List<Text>> clusterGroup) {
        int leafCount = 0, nodeCount = 1;
        PriorityQueue<HierarchicalTreeNode> remainingNodes = new PriorityQueue<HierarchicalTreeNode>();

        Map<Integer, float[]> gtr = new HashMap<Integer, float[]>();
        for (int i=0;i < clusterGroup.size(); i++) {
            HierarchicalTreeNode clusterTree = buildTreeForCluster(clusterGroup.get(i), gtr);
            remainingNodes.add(clusterTree);
        }

        while (remainingNodes.size()!=1 ) {
            HierarchicalTreeNode leftChild = remainingNodes.poll();
            HierarchicalTreeNode rightChild = remainingNodes.poll();
            HierarchicalTreeNode node = new HierarchicalTreeNode(
                    leftChild, rightChild);
            remainingNodes.add(node);
            float[] entry = new float[]{
                    leftChild.getNodeIndex(),
                    leftChild.isLeaf(),
                    rightChild.getNodeIndex(),
                    rightChild.isLeaf(),
                    LESSER_THAN_THRESHOLD
            };
            gtr.put(node.getNodeIndex(), entry);
        }

        return gtr;
    }

    private HierarchicalTreeNode buildTreeForCluster(List<Text> texts, Map<Integer, float[]> gtr) {
        PriorityQueue<HierarchicalTreeNode> remainingNodes = new PriorityQueue<HierarchicalTreeNode>();
        for (Text textNode : texts) {
            HierarchicalTreeNode node = new HierarchicalTreeNode(textNode);
            remainingNodes.add(node);
        }

        while (remainingNodes.size() != 1) {
            HierarchicalTreeNode leftChild = remainingNodes.poll();
            HierarchicalTreeNode rightChild = remainingNodes.poll();
            HierarchicalTreeNode node = new HierarchicalTreeNode(
                    leftChild, rightChild);
            remainingNodes.add(node);
            float[] entry = new float[]{
                    leftChild.getNodeIndex(),
                    leftChild.isLeaf(),
                    rightChild.getNodeIndex(),
                    rightChild.isLeaf(),
                    GREATER_THAN_THRESHOLD
            };
            gtr.put(node.getNodeIndex(), entry);
        }

        return remainingNodes.poll();
    }

    private List<Text> generateTexts(int treeNodeSize) {
        List<Text> content = new ArrayList<Text>();
        for (int i = 0; i < treeNodeSize; i++) {
            content.add(TextTestHelper.createTextRandomEntity());
        }

        return content;
    }
}
