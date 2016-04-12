package htmlParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextPropertyVault {
	private static TextPropertyVault vault = new TextPropertyVault();
	private static Map<String, String> hindiFontClasses;
	private static List<String> boldFontClasses;
	private static Map<String, Boolean> colouredClasses;
	private static Map<String, Float> xPositions;
	private static Map<String, Float> yPositions;
	private static Map<String, Float> fontSizes;
	private static final String DEFAULT_FEATURE_LIST_FORMAT = "x y fs l fc fb";
	private static final String DEFAULT_DELIMITER = " ";
	private static String featureListFormat;
	private static String delimiter;

	private TextPropertyVault() {
		clearVault(true);
	}

	public static void clearVault(Boolean clearFeatureFormat) {
		TextPropertyVault.setHindiFontClasses(new HashMap<String, String>());
		TextPropertyVault.setBoldFontClasses(new ArrayList<String>());
		TextPropertyVault.setColouredClasses(new HashMap<String, Boolean>());
		TextPropertyVault.setXPositions(new HashMap<String, Float>());
		TextPropertyVault.setYPositions(new HashMap<String, Float>());
		TextPropertyVault.setFontSizes(new HashMap<String, Float>());
		if (clearFeatureFormat) {
			TextPropertyVault.featureListFormat = null;
		}
	}

	public static TextPropertyVault getVault() {
		return vault;
	}

	public static void setVault(TextPropertyVault vault) {
		TextPropertyVault.vault = vault;
	}

	public static Map<String, Float> getXPositions() {
		return xPositions;
	}

	public static void setXPositions(Map<String, Float> xPositions) {
		TextPropertyVault.xPositions = xPositions;
	}

	public static Map<String, Float> getYPositions() {
		return yPositions;
	}

	public static void setYPositions(Map<String, Float> yPositions) {
		TextPropertyVault.yPositions = yPositions;
	}

	public static Map<String, Float> getFontSizes() {
		return fontSizes;
	}

	public static void setFontSizes(Map<String, Float> fontSizes) {
		TextPropertyVault.fontSizes = fontSizes;
	}

	public static String getFeatureListFormat() {
		String outputFeatureListFormat =
				featureListFormat == null ? DEFAULT_FEATURE_LIST_FORMAT : featureListFormat;

		return correctDelimiter(outputFeatureListFormat);
	}

	public static String getDefaultFeatureListFormat() {
		return DEFAULT_FEATURE_LIST_FORMAT;
	}

	public static void setFeatureListFormat(String featureListFormat) {
		TextPropertyVault.featureListFormat = featureListFormat;
	}

	public static String getDelimiter() {
		return delimiter == null ? DEFAULT_DELIMITER : delimiter;
	}

	public static void setDelimiter(String delimiter) {
		TextPropertyVault.delimiter = delimiter;
	}

	public Map<String, String> getHindiFontClasses() {
		return hindiFontClasses;
	}

	public static void setHindiFontClasses(Map<String, String> hindiFontClasses) {
		TextPropertyVault.hindiFontClasses = hindiFontClasses;
	}

	public List<String> getBoldFontClasses() {
		return boldFontClasses;
	}

	public static void setBoldFontClasses(List<String> boldFontClasses) {
		TextPropertyVault.boldFontClasses = boldFontClasses;
	}

	public Map<String, Boolean> getColouredClasses() {
		return colouredClasses;
	}

	public static void setColouredClasses(Map<String, Boolean> colouredClasses) {
		TextPropertyVault.colouredClasses = colouredClasses;
	}

	private static String correctDelimiter(String outputFeatureListFormat) {
		return outputFeatureListFormat.replace(DEFAULT_DELIMITER, getDelimiter());
	}
}
