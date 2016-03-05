package htmlParser;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleSheet;

import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.SACParserCSS3;

public class CssParser {
	private static final String FONT_COLOUR = "\\.fc";
	private static final String BOLD = "Bold";
	private static final String BASE64_PATTERN = "(?!base64)[^,]+(?=\\) format\\(\"truetype\"\\))";
	private static final String FONT_FAMILY_PATTERN = "(?!font-family: )ff[0-9a-fA-F]+(?=;)";
	private static final String FONT_FACE = "@font-face";
	private static final String FONT_COLOUR_FAMILY = "\\.(fc[0-9]+)";
	private static final String FONT_COLOUR_PATTERN = "(?!\\.fc[0-9]+ \\{ color: rgb\\()[0-9]+, *[0-9]+, *[0-9]+(?=\\) \\})";
	private static final String FONT_COLOUR_TRANSPARENT_PATTERN = "(?!\\.fc[0-9]+ \\{ color: )transparent(?= \\})";

	public CssParser() {
	}

	static void parseCssText(Document dom) {
		TextPropertyVault vault = TextPropertyVault.getVault();
		Map<String, String> hindiFontClasses = vault.getHindiFontClasses();
		List<String> boldFontClasses = vault.getBoldFontClasses();
		Map<String, Boolean> colouredClasses = vault.getColouredClasses();
		for (Element style : dom.getElementsByTag(Constants.STYLE_TAG)) {
			String data = style.data();
			if (data.contains(FONT_FACE) || data.contains(FONT_COLOUR)) {
				InputSource source = new InputSource(new StringReader(data));
				CSSOMParser parser = new CSSOMParser(new SACParserCSS3());
				CSSStyleSheet sheet;
				try {
					sheet = parser.parseStyleSheet(source, null, null);
					CSSRuleList cssRules = sheet.getCssRules();
					for (int i = 0; i < cssRules.getLength(); i++) {
						CSSRule cssRule = cssRules.item(i);
						String cssText = cssRule.getCssText();
						String fontFamily = Util.substringRegex(cssText, FONT_FAMILY_PATTERN);
						String fontData;
						if (fontFamily != null) {
							String fontDataEncoded = Util.substringRegex(cssText, BASE64_PATTERN);
							if (fontDataEncoded != null) {
								fontData = Util.decode(fontDataEncoded);
								String convertorClass = Config.getHindiConvertorClass(fontData);
								if (convertorClass != null
										&& !hindiFontClasses.containsKey(fontFamily)) {
									hindiFontClasses.put(fontFamily, convertorClass);
								}
								if (fontData.contains(BOLD) && !boldFontClasses.contains(fontFamily)) {
									boldFontClasses.add(fontFamily);
								}
							}
						}
						String fcFamily = Util.substringRegex(cssText, FONT_COLOUR_FAMILY, 1);
						if (fcFamily != null) {
							String fontColor = Util.substringRegex(cssText, FONT_COLOUR_PATTERN);
							if (fontColor == null) {
								fontColor = Util.substringRegex(cssText, FONT_COLOUR_TRANSPARENT_PATTERN);
							}
							if (fontColor == null) {
								System.out.println("ScrewedUp");
							}
							if (!colouredClasses.containsKey(fcFamily)) {
								colouredClasses.put(fcFamily, Config.isColouredClass(fontColor));
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	
		}
		vault.setHindiFontClasses(hindiFontClasses);
		vault.setBoldFontClasses(boldFontClasses);
		vault.setColouredClasses(colouredClasses);
	}
}