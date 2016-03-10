package pdfProcessor;

import htmlParser.Text;
import htmlParser.TextPropertyVault;
import org.junit.Before;
import org.junit.Test;
import pdfProcessor.Utils.TextTestHelper;

import static org.assertj.core.api.Assertions.assertThat;

public class TextTest extends BaseTest{
    private Text textEntity;

    @Before
    public void setup(){
        initializeTextPropertyVault();
        textEntity = new Text();
    }

    @Test
    public void getTextFeaturesShouldReturnStringOfFeatures() {
        String expectedFeatures = TextTestHelper.populateRandomFeaturesInTextEntity(textEntity);

        String outputFeatures = textEntity.getTextFeatures();

        assertThat(outputFeatures.split(" ").length).isEqualTo(3);
        assertThat(outputFeatures).isEqualTo(expectedFeatures);
    }
}
