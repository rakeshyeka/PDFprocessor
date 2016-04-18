package pdfProcessor;

import clusterAnalysis.ClusterConstants;
import clusterAnalysis.ClusterRunner;
import org.junit.Before;
import org.junit.Test;
import pdfProcessor.Utils.TestData;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ClusterRunnerTest extends BaseTest{

    private static final float BIAS = (float) 0.1;

    @Before
    public void setup() {

    }

    @Test
    public void extractKfromPcaAnalysisContentShouldReturnK() {
        int expectedK = TestData.randomNaturalNumber();
        List<String> eigenPC = generateValidEigenPC(expectedK);
        int outputK = ClusterRunner.extractKfromPcaAnalysisContent(eigenPC);
        assertThat(outputK).isEqualTo(expectedK);
    }

    private List<String> generateValidEigenPC(int expectedK) {
        List<String> listOfEigenValues = new ArrayList<String>();
        float dummyFloat = TestData.randomFloat();
        listOfEigenValues.add(Float.toString(dummyFloat));
        listOfEigenValues.add(Float.toString(dummyFloat));

        float eigenSum = 0;
        for (int index = 0; index < expectedK; index++) {
            float randomFloat = TestData.randomFloat();
            eigenSum += randomFloat;
            listOfEigenValues.add(Float.toString(randomFloat));
        }

        float expectedPercentageOfSum = ClusterConstants.EIGEN_THRESHOLD_PERCENT + BIAS;

        float ignorableSum = ((100 - expectedPercentageOfSum) * eigenSum) / expectedPercentageOfSum;
        int remainderElements = TestData.randomNaturalNumber();
        for (int index = 0; index < remainderElements; index++) {
            float ignoreElement = ignorableSum/remainderElements;
            eigenSum += ignoreElement;
            listOfEigenValues.add(Float.toString(ignoreElement));
        }

        return listOfEigenValues;
    }
}
