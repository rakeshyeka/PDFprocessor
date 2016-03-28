package htmlParser;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

public class Config {
    private static Map<String, String> hindiFontClasses;

    static {
        hindiFontClasses = new HashMap<String, String>();
        hindiFontClasses.put("DV_Divyae", "DV_To_Unicode");
        hindiFontClasses.put("UntitledTTF", "DV_To_Unicode");
        hindiFontClasses.put("Walkman-Chanakya", "Walkman_chanakya");
    }

    private static ArrayList<String> colouredRGBValues;

    static {
        colouredRGBValues = new ArrayList<String>();
        colouredRGBValues.add("22, 113, 194");
        colouredRGBValues.add("0, 173, 239");
    }


    private static ArrayList<String> boldFonts;

    static {
        boldFonts = new ArrayList<String>();
        boldFonts.add("Bookman-Demi");
    }

    private boolean isMixedLanguage = false;
    private String inputFolder;
    private String outputFolder;
    private boolean paragraphForBold;
    private boolean paragraphForColoured;

    public Config() {
    }

    public Config(boolean isMixedLanguage) {
        this.isMixedLanguage = isMixedLanguage;
    }

    public boolean isMixedLanguage() {
        return this.isMixedLanguage;
    }

    public static String getHindiConvertorClass(String fontData) {
        for (String fontKey : hindiFontClasses.keySet()) {
            if (fontData.contains(fontKey)) {
                return getFontClass(fontKey);
            }
        }
        return null;
    }

    public static Boolean isColouredClass(String fontData) {
        return !isGreyishColour(fontData);
    }

    private static Boolean isGreyishColour(String fontData) {
        String[] rgb = fontData.split(", ");
        if (rgb.length == 3) {
            float r = Float.parseFloat(rgb[0]);
            float g = Float.parseFloat(rgb[1]);
            float b = Float.parseFloat(rgb[2]);

            float mean = (r + g + b) / 3;
            float meanDiff = (
                    Math.abs(mean - r)
                            + Math.abs(mean - g)
                            + Math.abs(mean - b)) / 3;
            return mean < 100 && meanDiff < 10;
        } else {
            return true;
        }
    }

    public static Boolean isBoldClass(String fontData) {
        for (String boldFont : boldFonts) {
            if (fontData.contains(boldFont)) {
                return true;
            }
        }
        return fontData.contains(CssParserConstants.BOLD);
    }

    public String getInputFolder() {
        return inputFolder;
    }

    public void setInputFolder(String inputFolder) {
        this.inputFolder = inputFolder;
    }

    public String getOutputFolder() {
        return outputFolder;
    }

    public void setOutputFolder(String outputFolder) {
        this.outputFolder = outputFolder;
    }

    private static String getFontClass(String font) {
        return hindiFontClasses.get(font);
    }

    public boolean isParagraphForBold() {
        return paragraphForBold;
    }

    public void setParagraphForBold(boolean paragraphForBold) {
        this.paragraphForBold = paragraphForBold;
    }

    public boolean isParagraphForColoured() {
        return paragraphForColoured;
    }

    public void setParagraphForColoured(boolean paragraphForColoured) {
        this.paragraphForColoured = paragraphForColoured;
    }
}
