package pdfProcessor;

import htmlParser.HtmlToTextProcessor;
import htmlParser.TextPropertyVault;
import org.mockito.MockitoAnnotations;
import pdfProcessor.Utils.TextTestHelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class BaseTest {

    public BaseTest() {
        MockitoAnnotations.initMocks(this);
    }

    public String fetchFileContentUsingClassLoader(String filePath)
            throws IOException {
        File file = fetchResourceFileUsingClassLoader(filePath);
        String fileContent = new String(
                Files.readAllBytes(file.toPath()));
        return fileContent;
    }

    public File fetchResourceFileUsingClassLoader(String filePath) {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource(filePath).getFile());
    }

    public static void initializeTextPropertyVault () {
        TextPropertyVault.clearVault(true);
        TextPropertyVault.setXPositions(TextTestHelper.getRandomMap());
        TextPropertyVault.setYPositions(TextTestHelper.getRandomMap());
        TextPropertyVault.setFontSizes(TextTestHelper.getRandomMap());
    }
}
