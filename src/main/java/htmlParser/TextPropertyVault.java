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
	private static String featureListFormat;

	private TextPropertyVault() {
		clearVault();
	}

	public static void clearVault() {
		TextPropertyVault.setHindiFontClasses(new HashMap<String, String>());
		TextPropertyVault.setBoldFontClasses(new ArrayList<String>());
		TextPropertyVault.setColouredClasses(new HashMap<String, Boolean>());
		TextPropertyVault.setXPositions(new HashMap<String, Float>());
		TextPropertyVault.setYPositions(new HashMap<String, Float>());
		TextPropertyVault.setFontSizes(new HashMap<String, Float>());
		TextPropertyVault.featureListFormat = null;
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
		return featureListFormat == null ? DEFAULT_FEATURE_LIST_FORMAT : featureListFormat;
	}

	public static String getDefaultFeatureListFormat() {
		return DEFAULT_FEATURE_LIST_FORMAT;
	}

	public static void setFeatureListFormat(String featureListFormat) {
		TextPropertyVault.featureListFormat = featureListFormat;
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

}
