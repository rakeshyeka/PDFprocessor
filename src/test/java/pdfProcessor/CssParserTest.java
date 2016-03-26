package pdfProcessor;

import java.io.IOException;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import htmlParser.Constants;
import htmlParser.CssParser;
import htmlParser.TextPropertyVault;
import pdfProcessor.Utils.CssTestHelper;
import pdfProcessor.Utils.TestData;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class CssParserTest extends BaseTest {
	@Mock
	private Document dom;
	
	@Mock
	private Element style;
	
	private Elements elements;
	
	@Before
	public void setup() throws IOException {
		elements = new Elements();
		elements.add(style);
		TextPropertyVault.clearVault();
		when(dom.getElementsByTag(Constants.STYLE_TAG)).thenReturn(elements);
	}
	
	@Test
	public void parseCssTextShouldParseFontFamilyContent() {
		int numberOfFamilyTags = TestData.randomIntegerLessThan5();
		when(style.data()).thenReturn(CssTestHelper.generateFontFamilyContent(numberOfFamilyTags));

		CssParser.parseCssText(dom);

		assertThat(TextPropertyVault.getVault().getHindiFontClasses().size()).isEqualTo(numberOfFamilyTags);
	}

	@Test
	public void parseCssTextShouldParseFontColorContent() {
		int numberOfFontColorTags = TestData.randomIntegerLessThan5();
		when(style.data()).thenReturn(CssTestHelper.generateFontColorContent(numberOfFontColorTags));

		CssParser.parseCssText(dom);

		assertThat(TextPropertyVault.getVault().getColouredClasses().size()).isEqualTo(numberOfFontColorTags);
	}

	@Test
	public void parseCSSTextShouldParseFontFamilyWithBoldContent() {
		int numberOfFontColorTags = TestData.randomIntegerLessThan5();
		when(style.data()).thenReturn(CssTestHelper.generateFontFamilyBoldContent(numberOfFontColorTags));

		CssParser.parseCssText(dom);

		assertThat(TextPropertyVault.getVault().getBoldFontClasses().size()).isEqualTo(numberOfFontColorTags);
	}

	@Test
	public void parseCSSTextShouldParseXPositionContent() {
		int numberOfXPositionTags = TestData.randomNaturalNumber();
		when(style.data()).thenReturn(CssTestHelper.generateXpositionContent(numberOfXPositionTags));

		CssParser.parseCssText(dom);

		assertThat(TextPropertyVault.getVault().getXPositions().size()).isEqualTo(numberOfXPositionTags);
	}

	@Test
	public void parseCSSTextShouldParseYPositionContent() {
		int numberOfYPositionTags = TestData.randomNaturalNumber();
		when(style.data()).thenReturn(CssTestHelper.generateYpositionContent(numberOfYPositionTags));

		CssParser.parseCssText(dom);

		assertThat(TextPropertyVault.getVault().getYPositions().size()).isEqualTo(numberOfYPositionTags);
	}

	@Test
	public void parseCSSTextShouldParseFontSize() {
		int numberOfFontSizeTags = TestData.randomNaturalNumber();
		when(style.data()).thenReturn(CssTestHelper.generateFontSizeContent(numberOfFontSizeTags));

		CssParser.parseCssText(dom);

		assertThat(TextPropertyVault.getVault().getFontSizes().size()).isEqualTo(numberOfFontSizeTags);
	}
}
