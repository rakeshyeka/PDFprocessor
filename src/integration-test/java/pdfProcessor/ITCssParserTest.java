package pdfProcessor;

import htmlParser.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;

public class ITCssParserTest extends BaseTest{
    private File inputFile;
    @Before
    public void setup() {
        Config config = new Config(false);
        HtmlToTextProcessor.setConfig(config);
        inputFile = fetchResourceFileUsingClassLoader("input/test.html");
    }

    @Test
    public void domParserShouldParseTextAndReturnFeaturesForPages () throws IOException {
        DomParser domParser = new DomParser(inputFile.getAbsolutePath());
        Iterator<Page> pageIterator = domParser.getPagesIterator();

        while (pageIterator.hasNext()) {
            Page page = pageIterator.next();
            assertPageFeaturesAreAsExpected(page);
        }
    }

    private void assertPageFeaturesAreAsExpected(Page page) throws IOException {
        int pageNumber = page.getPageNumber();
        String featureFilePath = "output/features/testFeaturesPage" + pageNumber + ".txt";
        String expectedFeatures = fetchFileContentUsingClassLoader(featureFilePath);
        String responseFeatures = page.extractTextFeatures();
        assertThat(expectedFeatures).isEqualTo(responseFeatures);
    }

}
