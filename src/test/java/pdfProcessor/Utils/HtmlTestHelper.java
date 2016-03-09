package pdfProcessor.Utils;

import java.io.IOException;

public class HtmlTestHelper {
	private final static String HTML_TAG = "<html>\n%s\n</html>\n";
	private final static String HEAD_TAG = "<head>\n%s\n</head>\n";
	private final static String BODY_TAG = "<body>\n%s\n</body>\n";
	
	public static String createTempHtmlFileWithRandomContent() throws IOException {
		return TestData.createAndWriteToTemporaryFile(generateRandomHTMLContent());
	}

	private static String generateRandomHTMLContent() {
		String headContent = getHeadContent();
		String bodyContent = getBodyContent();
		return String.format(HTML_TAG, headContent + bodyContent);
	}

	private static String getHeadContent() {
		return String.format(HEAD_TAG, CssTestHelper.generateRandomCssContent());
	}

	private static String getBodyContent() {
		return String.format(BODY_TAG, ""); 
	}

}
