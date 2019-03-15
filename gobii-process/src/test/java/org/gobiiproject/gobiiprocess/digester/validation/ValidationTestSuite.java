package org.gobiiproject.gobiiprocess.digester.validation;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        GermplasmValidationTest.class,
        GermplasmPropValidationTest.class,
        DnaSampleValidationTest.class,
        DnaSamplePropValidationTest.class,
        DnarunPropValidationTest.class,
        MarkerValidationTest.class,
        MarkerPropValidationTest.class,
        LinkageGroupValidationTest.class,
        MarkerLinkageGroupValidationTest.class,
        DatasetDnarunValidationTest.class,
        DatasetMarkerValidationTest.class})

public class ValidationTestSuite {
}
