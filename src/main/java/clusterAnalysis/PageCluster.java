package clusterAnalysis;

import com.sun.org.apache.xpath.internal.operations.Bool;
import htmlParser.Page;
import htmlParser.Text;
import htmlParser.TextPropertyVault;
import htmlParser.Util;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PageCluster {

    private List<List<Text>> clusterGroups = new ArrayList<List<Text>>();
    private Map<Integer, Integer> kgg = new HashMap<Integer, Integer>();
    private List<Text> groupIndexNodes = new ArrayList<Text>();
    private Page page;
    private int k;

    public PageCluster(Page page) {
        this.page = page;
    }

    public void runCluster3ForFile(String pageFeatureFilePath, String outputPath) {
        this.k = ClusterAnalysisHelper.extractClusters(pageFeatureFilePath, outputPath);
    }

    public static void runHierarchicalClusterForFile(String pageFeatureFilePath, String outputPath) {
        File featureFile = new File(pageFeatureFilePath);

        HierarchicalClusterAnalysisHelper.runHierarchicalClustering(featureFile, outputPath);
    }

    public void serializePage(String featureFilePath) {
        String kggFilePath = featureFilePath.replace(".txt",
                String.format(ClusterConstants.K_MEANS_KGG_EXTENSION, Integer.toString(k)));
        this.readKGGFile(kggFilePath);
        this.segregateClusters();
        this.calculatePrimaryNodesAsIndexNodes();
        List<Text> serialContent = new ArrayList<Text>();
        Collections.sort(this.groupIndexNodes);
        for (Text centroid: this.groupIndexNodes) {
            int groupIndex = Integer.parseInt(centroid.toString());
            List<Text> group = this.clusterGroups.get(groupIndex);
            Collections.sort(group);
            serialContent.addAll(group);
        }
        this.page.setContent(serialContent);
    }

    public void serializePageUsingHierarchical(String featureFilePath) {
        String gtrFilePath = featureFilePath.replace(".txt",
                ClusterConstants.HIERARCHICAL_GTR_EXTENSION);
        HierarchicalClusterAnalysisHelper hierarchicalClusterer = new HierarchicalClusterAnalysisHelper();
        hierarchicalClusterer.readGTRFile(gtrFilePath);
        hierarchicalClusterer.parseGTR(this.clusterGroups, this.page.getContent());
        this.calculatePrimaryNodesAsIndexNodes();
        List<Text> serialContent = new ArrayList<Text>();
        Collections.sort(this.groupIndexNodes);
        for (Text centroid: this.groupIndexNodes) {
            int groupIndex = Integer.parseInt(centroid.toString());
            List<Text> group = this.clusterGroups.get(groupIndex);
            Collections.sort(group);
            serialContent.addAll(group);
        }
        this.page.setContent(serialContent);
    }

    public void readKGGFile(String inputKggFilePath) {
        List<String> kggFileContent = new ArrayList<String>();
        try {
            kggFileContent = Util.readContentFromFilePath(inputKggFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int index = 1; index < kggFileContent.size(); index++) {
            String line = kggFileContent.get(index);
            String[] splits = line.split("\t");
            int id = Integer.parseInt(splits[0]);
            int group = Integer.parseInt(splits[1]);
            this.kgg.put(id, group);
        }
    }

    public void segregateClusters() {
        Iterator<Text> textIterator = this.page.getTextIterator();
        for (int index = 0; textIterator.hasNext(); index++) {
            Text text = textIterator.next();
            int group = this.kgg.get(index);
            while (this.clusterGroups.size() <= group) {
                this.clusterGroups.add(new ArrayList<Text>());
            }

            this.clusterGroups.get(group).add(text);
        }
    }

    public void calculatePrimaryNodesAsIndexNodes() {
        int groupIndex = 0;
        for (List<Text> group: this.clusterGroups) {
            Text groupCentroid = getZeroFeatureTextElement();
            this.groupIndexNodes.add(groupCentroid);
            groupCentroid.setData(Integer.toString(groupIndex));
            groupCentroid.addTo(group.get(0));
            groupIndex++;
        }
    }

    public void calculateCentroidsAsIndexNodes() {
        int groupIndex = 0;
        for (List<Text> group: this.clusterGroups) {
            Text groupCentroid = getZeroFeatureTextElement();
            for (Text groupElement : group) {
                groupCentroid.addTo(groupElement);
            }

            groupCentroid.divide(group.size());
            groupCentroid.setData(Integer.toString(groupIndex));
            this.groupIndexNodes.add(groupCentroid);
            groupIndex++;
        }
    }

    public Page getPage(){
        return this.page;
    }

    public void sortTextContent(List<Text> content) {
        Collections.sort(content);
        removeDuplicates(content);
    }

    private void removeDuplicates(List<Text> content) {

        List<Text> toRemoveList = new ArrayList<Text>();
        for (int index = 0, nextIndex = index + 1;
             index < content.size() && nextIndex < content.size();){
            Text currentText = content.get(index);
            Text nextText = content.get(nextIndex);
            if (currentText.compareTo(nextText) == 0) {
                toRemoveList.add(nextText);
                nextIndex++;
            } else {
                index++;
                nextIndex = index+1;
            }
        }

        content.removeAll(toRemoveList);
    }

    private Text getZeroFeatureTextElement() {
        String[] emptyFeatures = TextPropertyVault.getFeatureListFormat().split(
                TextPropertyVault.getDelimiter());
        for (int index = 0; index < emptyFeatures.length; index++){
            emptyFeatures[index] = "0";
        }

        return new Text(emptyFeatures);
    }

    public int getK(){
        return this.k;
    }

    public void setK(int k) {
        this.k = k;
    }
}
