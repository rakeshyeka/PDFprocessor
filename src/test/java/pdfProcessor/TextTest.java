package pdfProcessor;

import htmlParser.Text;
import htmlParser.TextPropertyVault;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import pdfProcessor.Utils.TextTestHelper;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class TextTest extends BaseTest{
    private Text textEntity;

    @Before
    public void setup(){
        initializeTextPropertyVault();
        textEntity = new Text();
    }

    @Test
    public void getTextFeaturesShouldReturnStringOfDefaultFeaturesWhenNoFeatureListIsSet() {
        String expectedFeatures = TextTestHelper.populateRandomFeaturesInTextEntity(textEntity);

        String outputFeatures = textEntity.getTextFeatures();

        int featureListSize = TextPropertyVault.getFeatureListFormat().split(" ").length;
        assertThat(outputFeatures.split(" ").length).isEqualTo(featureListSize);
        assertThat(outputFeatures).isEqualTo(expectedFeatures);
    }

    @Test
    @Parameters(method = "getFeatureListFormats")
    public void getTextWithSpecificFeaturesShouldReturnStringOfRequiredFeatures(
            String featureListFormat) {
        int featureListSize = featureListFormat.split(" ").length;
        TextPropertyVault.setFeatureListFormat(featureListFormat);
        String expectedFeatures = TextTestHelper.populateRandomFeaturesInTextEntity(textEntity);

        String outputFeatures = textEntity.getTextFeatures();

        assertThat(outputFeatures.split(" ").length).isEqualTo(featureListSize);
        assertThat(outputFeatures).isEqualTo(expectedFeatures);
    }

    private Object[] getFeatureListFormats (){
        return new Object[] {
                "x y",
                "x fs",
                "y x",
                "fs x y"
        };
    }
}
