package pdfProcessor.Utils;

public class CssTestHelper {
	private final static String STYLE_TAG = "<style type=\"text/css\">\n%s\n</style>";
	private final static String FONT_FAMILY_TAG = ".ff%s{font-family:sans-serif;visibility:hidden;}\n";
	
	public static String generateRandomCssContent() {
		int numberOfFamilyTags = TestData.getIntegerLessThan5();
		String fontFamilyContent = "";
		for (int i=0; i < numberOfFamilyTags; i++){
			fontFamilyContent += String.format(FONT_FAMILY_TAG, i);
		}
		return String.format(STYLE_TAG, fontFamilyContent);
	}
}
