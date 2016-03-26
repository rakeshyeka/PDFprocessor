package pdfProcessor.Utils;

public class CssTestHelper {
    private final static String STYLE_TAG = "<style type=\"text/css\">\n%s\n</style>";
    private final static String ENCODED_DV_DIVYAE = "RFZfRGl2eWFl";
    public static final String ENCODED_ARIEL_BOLD = "QXJpZWwtQm9sZA==";
    private final static String FONT_FACE_FAMILY_TAG = "\n@font-face{font-family:ff%s;" +
            "src:url('data:application/x-font-ttf;base64,%s')format(\"truetype\");}" +
            ".ff%s{font-family:ff%s;line-height:0.984000;font-style:normal;font-weight:normal;visibility:visible;}\n";
    private final static String FONT_COLOR_TAG = ".fc%s{color:rgb(171,171,171);}\n";
    private final static String X_POSITION_TAG = ".x%s{left:%spx;}\n";
    private final static String Y_POSITION_TAG = ".y%s{bottom:%spx;}\n";
    private final static String FONT_SIZE_TAG = ".fs%s{font-size:%spx;}\n";

    public static String generateRandomCssContentWithStyleTags() {
        return String.format(STYLE_TAG, generateRandomCssContent());
    }

    public static String generateRandomCssContent() {
        int numberOfFamilyTags = TestData.randomIntegerLessThan5();
        String fontFamilyContent = generateFontFamilyContent(numberOfFamilyTags);
        int numberOfFontColorTags = TestData.randomIntegerLessThan5();
        String fontColorContent = generateFontColorContent(numberOfFontColorTags);
        return fontFamilyContent + fontColorContent;
    }

    public static String generateFontColorContent(int numberOfFontColorTags) {
        String fontColorContent = "";
        for (int i = 0; i < numberOfFontColorTags; i++) {
            fontColorContent += String.format(FONT_COLOR_TAG, i);
        }
        return fontColorContent;
    }

    public static String generateFontFamilyContent(int numberOfFamilyTags) {
        String fontFamilyContent = "";
        for (int i = 0; i < numberOfFamilyTags; i++) {
            fontFamilyContent += String.format(FONT_FACE_FAMILY_TAG, i, ENCODED_DV_DIVYAE, i, i);
        }
        return fontFamilyContent;
    }

    public static String generateFontFamilyBoldContent(int numberOfFamilyTags) {
        String fontFamilyContent = "";
        for (int i = 0; i < numberOfFamilyTags; i++) {
            fontFamilyContent += String.format(FONT_FACE_FAMILY_TAG, i, ENCODED_ARIEL_BOLD, i, i);
        }
        return fontFamilyContent;
    }

    public static String generateXpositionContent(int numberOfXPositionTags) {
        String xPositionContent = "";
        for (int i = 0; i < numberOfXPositionTags; i++) {
            xPositionContent += String.format(
                    X_POSITION_TAG,
                    TestData.convertToHexValue(i), TestData.randomFloat());
        }
        return xPositionContent;
    }

    public static String generateYpositionContent(int numberOfYPositionTags) {
        String yPositionContent = "";
        for (int i = 0; i < numberOfYPositionTags; i++) {
            yPositionContent += String.format(
                    Y_POSITION_TAG,
                    TestData.convertToHexValue(i), TestData.randomFloat());
        }
        return yPositionContent;
    }

    public static String generateFontSizeContent(int numberOfFontSizeTags) {
        String fontSizeContent = "";
        for (int i = 0; i < numberOfFontSizeTags; i++) {
            fontSizeContent += String.format(
                    FONT_SIZE_TAG,
                    TestData.convertToHexValue(i), TestData.randomFloat());
        }
        return fontSizeContent;
    }
}
