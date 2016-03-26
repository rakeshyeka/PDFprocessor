package htmlParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleSheet;

import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.SACParserCSS3;

public class CssParser {

    private static TextPropertyVault vault;

    public CssParser() {
    }

    public static void parseCssText(Document dom) {
        vault = TextPropertyVault.getVault();
        for (Element style : dom.getElementsByTag(Constants.STYLE_TAG)) {
            extractCssFromStyleTag(style);
        }
    }

    private static void extractCssFromStyleTag(Element style) {
        String data = style.data();
        if (data.contains(CssParserConstants.FONT_FACE)
                || data.contains(CssParserConstants.FONT_COLOUR)
                || data.contains(CssParserConstants.X_POSITION_PLAIN)
                || data.contains(CssParserConstants.Y_POSITION_PLAIN)
                || data.contains(CssParserConstants.FONT_SIZE_PLAIN)) {
            InputSource source = new InputSource(new StringReader(data));
            CSSOMParser parser = new CSSOMParser(new SACParserCSS3());
            CSSStyleSheet sheet;
            try {
                sheet = parser.parseStyleSheet(source, null, null);
                CSSRuleList cssRules = sheet.getCssRules();
                for (int i = 0; i < cssRules.getLength(); i++) {
                    CSSRule cssRule = cssRules.item(i);
                    String cssText = cssRule.getCssText();
                    processCSStextForBoldAndHindiClasses(cssText);
                    processCSStextForColouredClasses(cssText);
                    processCSStextForXPositions(cssText);
                    processCSStextForYPositions(cssText);
                    processCSStextForFontSizes(cssText);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void processCSStextForXPositions(String cssText) {
        String xPosition = Util.substringRegex(cssText, CssParserConstants.X_POSITION, 1);
        if (xPosition != null) {
            String xPositionStringValue = Util.substringRegex(cssText, CssParserConstants.X_POSITION_PATTERN);
            if (xPositionStringValue != null) {
                Float xPositionValue = Float.parseFloat(xPositionStringValue);
                if (!vault.getXPositions().containsKey(xPosition)) {
                    vault.getXPositions().put(xPosition, xPositionValue);
                }
            }
        }
    }

    private static void processCSStextForYPositions(String cssText) {
        String yPosition = Util.substringRegex(cssText, CssParserConstants.Y_POSITION, 1);
        if (yPosition != null) {
            String yPositionStringValue = Util.substringRegex(cssText, CssParserConstants.Y_POSITION_PATTERN);
            if (yPositionStringValue != null) {
                Float yPositionValue = Float.parseFloat(yPositionStringValue);
                if (!vault.getYPositions().containsKey(yPosition)) {
                    vault.getYPositions().put(yPosition, yPositionValue);
                }
            }
        }
    }

    private static void processCSStextForFontSizes(String cssText) {
        String fontSize = Util.substringRegex(cssText, CssParserConstants.FONT_SIZE, 1);
        if (fontSize != null) {
            String fontSizeStringValue = Util.substringRegex(cssText, CssParserConstants.FONT_SIZE_PATTERN);
            if (fontSizeStringValue != null) {
                Float fontSizeValue = Float.parseFloat(fontSizeStringValue);
                if (!vault.getFontSizes().containsKey(fontSize)) {
                    vault.getFontSizes().put(fontSize, fontSizeValue);
                }
            }
        }
    }

    private static void processCSStextForColouredClasses(String cssText) {
        String fcFamily = Util.substringRegex(cssText, CssParserConstants.FONT_COLOUR_FAMILY, 1);
        if (fcFamily != null) {
            String fontColor = Util.substringRegex(cssText, CssParserConstants.FONT_COLOUR_PATTERN);
            if (fontColor == null) {
                fontColor = Util.substringRegex(cssText, CssParserConstants.FONT_COLOUR_TRANSPARENT_PATTERN);
            }
            if (fontColor == null) {
                System.out.println("ScrewedUp");
            }
            if (!vault.getColouredClasses().containsKey(fcFamily)) {
                vault.getColouredClasses().put(fcFamily, Config.isColouredClass(fontColor));
            }
        }
    }

    private static void processCSStextForBoldAndHindiClasses(String cssText) {
        String fontFamily = Util.substringRegex(cssText, CssParserConstants.FONT_FAMILY_PATTERN);
        String fontData;
        if (fontFamily != null) {
            String fontDataEncoded = Util.substringRegex(cssText, CssParserConstants.BASE64_PATTERN);
            if (fontDataEncoded != null) {
                fontData = Util.decode(fontDataEncoded);
                String convertorClass = Config.getHindiConvertorClass(fontData);
                if (convertorClass != null
                        && !vault.getHindiFontClasses().containsKey(fontFamily)) {
                    vault.getHindiFontClasses().put(fontFamily, convertorClass);
                }
                if (Config.isBoldClass(fontData) && !vault.getBoldFontClasses().contains(fontFamily)) {
                    vault.getBoldFontClasses().add(fontFamily);
                }
            }
        }
    }
}