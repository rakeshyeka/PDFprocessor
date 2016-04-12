package pdfProcessor;

import htmlParser.Text;
import htmlParser.TextPropertyVault;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import pdfProcessor.Utils.TextTestHelper;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class TextTest extends BaseTest{
    private static Text textEntity;

    static {
        setup();
    }

    public static void setup(){
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
    public void getTextFeaturesShouldReturnStringOfRequiredFeatures(
            String featureListFormat) {
        int featureListSize = featureListFormat.split(" ").length;
        TextPropertyVault.setFeatureListFormat(featureListFormat);
        String expectedFeatures = TextTestHelper.populateRandomFeaturesInTextEntity(textEntity);

        String outputFeatures = textEntity.getTextFeatures();

        assertThat(outputFeatures.split(" ").length).isEqualTo(featureListSize);
        assertThat(outputFeatures).isEqualTo(expectedFeatures);
    }

    @Test
    @Parameters(method = "getValidTextObjectsAndComparisions")
    public void textComparatorShouldReturnValidComparisionResponses(
            Text text1,
            Text text2,
            int expectedComparisionResult
    ) {
        TextPropertyVault.setFeatureListFormat(TextPropertyVault.getDefaultFeatureListFormat());
        int outputResult = text1.compareTo(text2);

        assertThat(outputResult).isEqualTo(expectedComparisionResult);
    }

    private Object[] getFeatureListFormats (){
        String[] defaultFeatures = TextPropertyVault.getDefaultFeatureListFormat().split(" ");
        Object[] objectArray = new Object[] {
                "x y",
                "x fs",
                "y x",
                "fs x y",
                "fc fb",
        };
        return ArrayUtils.addAll(defaultFeatures, objectArray);
    }

    private Object[][] getValidTextObjectsAndComparisions() {
        Text text1 = TextTestHelper.createTextRandomEntity();
        Text text2 = TextTestHelper.createTextRandomEntity();
        text2.setClasses(text1.getClasses());
        String[] features = text1.getTextFeatures().split(TextPropertyVault.getDelimiter());
        features[0] = addToNumericalStringValue(features[0], -3);
        Text text3 = TextTestHelper.createTextRandomEntity(features);
        features[0] = addToNumericalStringValue(features[0], 6);
        Text text4 = TextTestHelper.createTextRandomEntity(features);
        features[0] = addToNumericalStringValue(features[0], -3);
        features[1] = addToNumericalStringValue(features[1], -3);
        Text text5 = TextTestHelper.createTextRandomEntity(features);
        return new Object[][] {
                {text1, text2, 0},
                {text1, text3, -1},
                {text1, text4, 1},
                {text1, text5, -1}
        };
    }

    private String addToNumericalStringValue(String feature, double i) {
        double numericalValue = Double.parseDouble(feature);
        return Double.toString(numericalValue + i);
    }
}
