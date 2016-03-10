package pdfProcessor.Utils;

import htmlParser.Text;
import htmlParser.TextPropertyVault;

import java.util.HashMap;
import java.util.Map;

public class TextTestHelper {


    public static String populateRandomFeaturesInTextEntity(Text textEntity) {
        textEntity.setData(TestData.randomString());
        Map<String, String> classesMap = new HashMap<String, String>();
        String randomXPosition = getRandomKeyFromMap(TextPropertyVault.getXPositions());
        classesMap.put("x", randomXPosition);
        String randomYPosition = getRandomKeyFromMap(TextPropertyVault.getYPositions());
        classesMap.put("y", randomYPosition);
        String randomFontSize = getRandomKeyFromMap(TextPropertyVault.getFontSizes());
        classesMap.put("fs", randomFontSize);
        textEntity.setClasses(classesMap);
        return String.format("%s %s %s",
                TextPropertyVault.getXPositions().get(randomXPosition),
                TextPropertyVault.getYPositions().get(randomYPosition),
                TextPropertyVault.getFontSizes().get(randomFontSize));
    }

    public static Map<String, Float> getRandomMap() {
        Map<String, Float> map = new HashMap<String, Float>();
        int sizeOfMap = TestData.randomNaturalNumber();
        for (int i=0; i < sizeOfMap; i++) {
            map.put(TestData.randomString(), TestData.randomFloat());
        }
        return map;
    }

    public static String getRandomKeyFromMap(Map map) {
        int mapSize = map.size();
        int randomKeyIndex = TestData.randomNaturalNumber(mapSize);
        Object key = null;
        for (int i=0; i< randomKeyIndex; i++) {
            key = map.keySet().iterator().next();
        }
        return (String) key;
    }
}
