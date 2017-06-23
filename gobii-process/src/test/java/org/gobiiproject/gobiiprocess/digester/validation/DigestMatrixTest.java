package org.gobiiproject.gobiiprocess.digester.validation;

import org.gobiiproject.gobiiprocess.digester.utils.validation.DigestMatrix;
import org.junit.Test;
import java.io.File;
import static org.junit.Assert.*;

/**
 * Unit test for validation of digest.matrix
 */
public class DigestMatrixTest {
    @Test
    public void TestValidateDigestMatrix (){
        File matrixFile = new File("src/test/resources/digest.matrix");
        String dataSetType = "IUPAC";
        boolean success;
        try {
            success = DigestMatrix.validatematrix(matrixFile, dataSetType);
        }
        catch (Exception e){
            success = false;
        }
        assertTrue(success);
    }
}
