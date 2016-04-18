package pdfProcessor;

import clusterAnalysis.ClusterConstants;
import htmlParser.Util;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import clusterAnalysis.ClusterRunner;
import java.io.File;

public class ITClusterAnalysis extends BaseTest {
    private File inputFile;
    private static final String TEMP_PATH = "/tmp";

    @Before
    public void Setup(){
        inputFile = fetchResourceFileUsingClassLoader("input/test.txt");
    }

    @Test
    public void clusterAnalysisShouldRunPCAAndKMeans(){
        ClusterRunner.extractClusters(inputFile.getAbsolutePath(), TEMP_PATH);

        verifyPCAIsRun();
    }

    private void verifyPCAIsRun() {
        String pcaPCFile = inputFile.getName().replace(".txt", ClusterConstants.PCA_PC_TXT_EXTENSION);
        File expectedPcaPCFile = fetchResourceFileUsingClassLoader(
                Util.pathJoin("output/clusterAnalysis", pcaPCFile));
        String outputPcaPCFile = Util.pathJoin(TEMP_PATH, pcaPCFile);
        assertFileContentsAreSame(outputPcaPCFile, expectedPcaPCFile.getAbsolutePath());

        String pcaCoordsFile = inputFile.getName().replace(
                ".txt",
                ClusterConstants.PCA_COORDS_TXT_EXTENSION);
        File expectedPcaCoordsFile = fetchResourceFileUsingClassLoader(
                Util.pathJoin("output/clusterAnalysis", pcaCoordsFile));
        String outputPcaCoordsFile = Util.pathJoin(TEMP_PATH, pcaCoordsFile);
        assertFileContentsAreSame(outputPcaCoordsFile, expectedPcaCoordsFile.getAbsolutePath());
    }
}
