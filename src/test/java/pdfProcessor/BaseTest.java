package pdfProcessor;

import htmlParser.TextPropertyVault;
import org.mockito.MockitoAnnotations;
import pdfProcessor.Utils.TextTestHelper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;

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
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(filePath);
        return new File(resource.getFile());
    }

    public static void initializeTextPropertyVault () {
        TextPropertyVault.clearVault(true);
        TextPropertyVault.setXPositions(TextTestHelper.getRandomMap());
        TextPropertyVault.setYPositions(TextTestHelper.getRandomMap());
        TextPropertyVault.setFontSizes(TextTestHelper.getRandomMap());
    }

    public static void assertFileContentsAreSame(String file1Path, String file2Path) {
        File file1 = new File(file1Path);
        File file2 = new File(file2Path);
        try {
            String file1Content = new String(
                    Files.readAllBytes(file1.toPath()));
            String file2Content = new String(
                    Files.readAllBytes(file2.toPath()));
            assertThat(file1Content).isEqualTo(file2Content);
        } catch (IOException e) {
            e.printStackTrace();
            assertThat(1).isEqualTo(0);
        }
    }
}
