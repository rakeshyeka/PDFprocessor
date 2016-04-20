package pdfProcessor;

import htmlParser.Text;
import htmlParser.TextPropertyVault;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import pdfProcessor.Utils.TestData;
import pdfProcessor.Utils.TextTestHelper;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class TextTest extends BaseTest {
    private static Text textEntity;

    static {
        setup();
    }

    public static void setup() {
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
    public void addToShouldAddFeaturesOfText() {
        Text text1 = TextTestHelper.createTextRandomEntity();
        Text text2 = TextTestHelper.createTextRandomEntity();
        String features1 = text1.getTextFeatures();
        String features2 = text2.getTextFeatures();
        String[] addedFeatures = addFeatureStrings(features1, features2);

        text1.addTo(text2);

        assertThat(text1.getTextFeatures()).isNotEqualTo(features1);
        assertThat(text1.getTextFeatures()).isEqualTo(
                StringUtils.join(addedFeatures, TextPropertyVault.getDelimiter()));
    }

    @Test
    public void divideShouldDivideFeaturesOfTextWithAFloat() {
        Text text1 = TextTestHelper.createTextRandomEntity();
        String features1 = text1.getTextFeatures();
        int randomInt = TestData.randomNaturalNumber();
        String[] dividedFeatures = divideFeatureStrings(features1, randomInt);

        text1.divide(randomInt);

        assertThat(text1.getTextFeatures()).isNotEqualTo(features1);
        assertThat(text1.getTextFeatures()).isEqualTo(
                StringUtils.join(dividedFeatures, TextPropertyVault.getDelimiter()));
    }

    private String[] divideFeatureStrings(String features1, int num) {
        String[] featureList1 = features1.split(TextPropertyVault.getDelimiter());
        String[] dividedFeatures = new String[featureList1.length];
        for (int index = 0; index < dividedFeatures.length; index++) {
            dividedFeatures[index] = Float.toString(
                    Float.parseFloat(featureList1[index]) / num);
        }

        return dividedFeatures;
    }

    private String[] addFeatureStrings(String features1, String features2) {
        String[] featureList1 = features1.split(TextPropertyVault.getDelimiter());
        String[] featureList2 = features2.split(TextPropertyVault.getDelimiter());
        String[] addedFeatures = new String[featureList1.length];
        for (int index = 0; index < addedFeatures.length; index++) {
            addedFeatures[index] = Float.toString(
                    Float.parseFloat(featureList1[index])
                            + Float.parseFloat(featureList2[index])
            );
        }

        return addedFeatures;
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

    private Object[] getFeatureListFormats() {
        String[] defaultFeatures = TextPropertyVault.getDefaultFeatureListFormat().split(" ");
        Object[] objectArray = new Object[]{
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
        return new Object[][]{
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
