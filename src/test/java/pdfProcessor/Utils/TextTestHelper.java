package pdfProcessor.Utils;

import htmlParser.Text;
import htmlParser.TextPropertyVault;
import org.apache.commons.lang3.BooleanUtils;

import java.util.HashMap;
import java.util.Map;

public class TextTestHelper {


    public static String populateRandomFeaturesInTextEntity(Text textEntity) {
        String textData = TestData.randomString();
        textEntity.setData(textData);
        Boolean isColored = TestData.randomBoolean();
        textEntity.setColoured(isColored);
        Boolean isBold = TestData.randomBoolean();
        textEntity.setBold(isBold);
        Map<String, String> classesMap = new HashMap<String, String>();
        String randomXPosition = getRandomKeyFromMap(TextPropertyVault.getXPositions());
        classesMap.put("x", randomXPosition);
        String randomYPosition = getRandomKeyFromMap(TextPropertyVault.getYPositions());
        classesMap.put("y", randomYPosition);
        String randomFontSize = getRandomKeyFromMap(TextPropertyVault.getFontSizes());
        classesMap.put("fs", randomFontSize);
        textEntity.setClasses(classesMap);
        String expectedFeatures = TextPropertyVault.getFeatureListFormat();
        expectedFeatures = expectedFeatures.replace(
                "x", TextPropertyVault.getXPositions().get(randomXPosition).toString());
        expectedFeatures = expectedFeatures.replace(
                "y", TextPropertyVault.getYPositions().get(randomYPosition).toString());
        expectedFeatures = expectedFeatures.replace(
                "fs", TextPropertyVault.getFontSizes().get(randomFontSize).toString());
        expectedFeatures = expectedFeatures.replace(
                "l", Integer.toString(textData.length()));
        expectedFeatures = expectedFeatures.replace(
                "fc", Integer.toString(BooleanUtils.toInteger(isColored)));
        expectedFeatures = expectedFeatures.replace(
                "fb", Integer.toString(BooleanUtils.toInteger(isBold)));
        return expectedFeatures;
    }

    public static Map<String, Float> getRandomMap() {
        Map<String, Float> map = new HashMap<String, Float>();
        int sizeOfMap = TestData.randomNaturalNumber();
        for (int i = 0; i < sizeOfMap; i++) {
            map.put(TestData.randomString(), TestData.randomFloat());
        }
        return map;
    }

    public static String getRandomKeyFromMap(Map map) {
        int mapSize = map.size();
        int randomKeyIndex = TestData.randomNaturalNumber(mapSize);
        Object key = null;
        for (int i = 0; i < randomKeyIndex; i++) {
            key = map.keySet().iterator().next();
        }
        return (String) key;
    }

    public static Text createTextRandomEntity() {
        String randomFeatureList = generateRandomTextFeatures();
        return createTextRandomEntity(randomFeatureList.split(TextPropertyVault.getDelimiter()));
    }

    public static String generateRandomTextFeatures() {
        String featureListFormat = TextPropertyVault.getFeatureListFormat();
        featureListFormat = featureListFormat.replace("x", Float.toString(TestData.randomFloat()));
        featureListFormat = featureListFormat.replace("y", Float.toString(TestData.randomFloat()));
        featureListFormat = featureListFormat.replace("fs", Float.toString(TestData.randomFloat()));
        featureListFormat = featureListFormat.replace("l",
                Integer.toString(TestData.randomNaturalNumber()));
        featureListFormat = featureListFormat.replace("fc", Integer.toString(
                BooleanUtils.toInteger(TestData.randomBoolean())));
        featureListFormat = featureListFormat.replace("fb", Integer.toString(
                BooleanUtils.toInteger(TestData.randomBoolean())));
        return featureListFormat;
    }

    public static Text createTextRandomEntity(String[] features) {

        String xPosClass = TestData.randomString();
        float xPos = Float.parseFloat(features[0]);
        TextPropertyVault.getXPositions().put(xPosClass, xPos);

        String yPosClass = TestData.randomString();
        float yPos = Float.parseFloat(features[1]);
        TextPropertyVault.getYPositions().put(yPosClass, yPos);

        String fontSizeClass = TestData.randomString();
        float fontSize = Float.parseFloat(features[2]);
        TextPropertyVault.getFontSizes().put(fontSizeClass, fontSize);

        Text textEntity = new Text();
        textEntity.getClasses().put("x", xPosClass);
        textEntity.getClasses().put("y", yPosClass);
        textEntity.getClasses().put("fs", fontSizeClass);
        textEntity.setData(TestData.randomString(Integer.parseInt(features[3])));
        textEntity.setColoured(BooleanUtils.toBoolean(Integer.parseInt(features[4])));
        textEntity.setBold(BooleanUtils.toBoolean(Integer.parseInt(features[5])));
        return textEntity;
    }
}
