package pdfProcessor;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class ITHtmlDomParserTest {
	private File inputFile;
	private File referenceOutputFile;
	private File outputFile;
	@Before
	public void setup() {
		ClassLoader classLoader = getClass().getClassLoader();
		inputFile = new File(classLoader.getResource("input/test.html").getFile());
		referenceOutputFile = new File(classLoader.getResource("output/test.txt").getFile());
	}
	
	@Test
	public void DomParserShouldSuccessfullyExtractTextFromInput(){
	}

	@Test
	public void IntegrationTestShouldRunSuccessfullyAndOutputToConsole(){
		System.out.println("Integration tests Running");
	}

}
