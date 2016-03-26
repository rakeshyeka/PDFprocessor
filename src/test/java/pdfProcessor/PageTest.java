package pdfProcessor;

import htmlParser.Page;
import htmlParser.Text;
import org.junit.Before;
import org.junit.Test;
import pdfProcessor.Utils.TestData;
import pdfProcessor.Utils.TextTestHelper;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class PageTest extends BaseTest{

    private Page pageEntity;

    @Before
    public void setup(){
        initializeTextPropertyVault();
    }

    @Test
    public void extractTextFeaturesShouldReturnTextFeatures(){
        List<Text> textList = new ArrayList<Text>();
        String expectedFeatures = getValidTextEntityList(textList);
        pageEntity = new Page(textList);

        String outputFeatures = pageEntity.extractTextFeatures();

        assertThat(outputFeatures).isEqualTo(expectedFeatures);
    }

    public String getValidTextEntityList(List<Text> textEntityList) {
        int numberOfTextEntities = TestData.randomIntegerLessThan5();
        String textFeaturesString = "";
        for (int i=0; i < numberOfTextEntities; i++) {
            Text textEntity = new Text();
            textFeaturesString += TextTestHelper.populateRandomFeaturesInTextEntity(textEntity) + "\n";
            textEntityList.add(textEntity);
        }
        return textFeaturesString;
    }
}
