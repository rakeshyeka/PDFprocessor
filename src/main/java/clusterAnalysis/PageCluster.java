package clusterAnalysis;

import htmlParser.Page;
import htmlParser.Text;
import htmlParser.TextPropertyVault;
import htmlParser.Util;

import java.io.IOException;
import java.util.*;

public class PageCluster {

    private List<List<Text>> clusterGroups = new ArrayList<List<Text>>();
    private Map<Integer, Integer> kgg = new HashMap<Integer, Integer>();
    private List<Text> centroids = new ArrayList<Text>();
    private Page page;

    public PageCluster(Page page) {
        this.page = page;
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

    public void calculateCentroids() {
        int groupIndex = 0;
        for (List<Text> group: this.clusterGroups) {
            Text groupCentroid = getZeroFeatureTextElement();
            for (Text groupElement : group) {
                groupCentroid.addTo(groupElement);
            }

            groupCentroid.setData(Integer.toString(groupIndex));
            this.centroids.add(groupCentroid);
            groupIndex++;
        }
    }

    private Text getZeroFeatureTextElement() {
        String[] emptyFeatures = TextPropertyVault.getFeatureListFormat().split(
                TextPropertyVault.getDelimiter());
        for (int index = 0; index < emptyFeatures.length; index++){
            emptyFeatures[index] = "0";
        }

        return new Text(emptyFeatures);
    }
}
