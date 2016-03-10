package pdfProcessor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import htmlParser.Config;
import htmlParser.DomParser;
import htmlParser.HtmlFileFilter;

import static org.assertj.core.api.Assertions.*;

public class ITHtmlDomParserTest extends BaseTest{
	private static final String PLAIN_REFERENCE_TXT = "testPlain.txt";
	private static final String PARA_MARKED_REFERENCE_TXT = "testParaMarked.txt";
	private File inputFile;
	private File referenceOutputFile;
	
	@Before
	public void setup() {
		Config config = new Config(false);
		HtmlFileFilter.setConfig(config);
		inputFile = fetchResourceFileUsingClassLoader("input/test.html");
	}
	
	@Test
	public void DomParserShouldSuccessfullyExtractTextFromInputWithParagraph()
			throws IOException {

		HtmlFileFilter.getConfig().setParagraphForBold(true);
		HtmlFileFilter.getConfig().setParagraphForColoured(true);
		DomParser domparser = new DomParser(inputFile.getAbsolutePath());
		
		String outputResponse = domparser.getPages();
		
		assertOutputResponseIsCorrect(outputResponse, PARA_MARKED_REFERENCE_TXT);
	}
	
	
	@Test
	public void DomParserShouldSuccessfullyExtractTextFromInputWithoutParagraph()
			throws IOException {

		HtmlFileFilter.getConfig().setParagraphForBold(false);
		HtmlFileFilter.getConfig().setParagraphForColoured(false);
		DomParser domparser = new DomParser(inputFile.getAbsolutePath());
		
		String outputResponse = domparser.getPages();
		
		assertOutputResponseIsCorrect(outputResponse, PLAIN_REFERENCE_TXT);
	}

	@Test
	public void IntegrationTestShouldRunSuccessfullyAndOutputToConsole() {
		System.out.println("Integration tests Running");
	}
	
	@After
	public void CleanUp() {
		HtmlFileFilter.setConfig(null);
	}

	private void assertOutputResponseIsCorrect(String outputResponse, String referenceFile)
			throws IOException {
		FileOutputStream outputStream = new FileOutputStream("/tmp/output.txt");
		outputStream.write(outputResponse.getBytes());
		outputStream.close();
		String referenceOutput = fetchFileContentUsingClassLoader("output/" + referenceFile);
		assertThat(outputResponse).isEqualTo(referenceOutput);
	}

}
