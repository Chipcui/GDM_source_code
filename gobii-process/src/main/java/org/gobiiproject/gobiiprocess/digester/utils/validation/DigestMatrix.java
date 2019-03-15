package org.gobiiproject.gobiiprocess.digester.utils.validation;

import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobiimodel.types.DataSetType;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class DigestMatrix {

    public static boolean validateDatasetList(int lineNumber, List<String> rowList, String type) {
        List<String> allowedCharacters;
        DataSetType dataSetType = DataSetType.valueOf(type);
        switch (dataSetType) {
            case NUCLEOTIDE_2_LETTER:
            case IUPAC:
                allowedCharacters = initNucleotide2letterList();
                break;
            case CO_DOMINANT_NON_NUCLEOTIDE:
                allowedCharacters = initCoDominantList();
                break;
            case DOMINANT_NON_NUCLEOTIDE:
                allowedCharacters = initDominantList();
                break;
            case SSR_ALLELE_SIZE:
                /*
                 * since ssr data has only digits (upto 8)
                 */
                for (String element : rowList) {
                    // Only accept numerical strings of exactly eight characters. No hetro SSR
                    if (!(StringUtils.isNumeric(element) && element.length() == 8)) {
                        ErrorLogger.logError("Validate Dataset Matrix", "Invalid data found in post-processed matrix line: " + lineNumber + " Data:" + element);
                        return false;
                    }
                }
                return true;
            case VCF:
                //TODO: SHould this be rtue or false. CHeck with JOSH for VCF Transform
                return true;
            default:
                ErrorLogger.logError("Validate Dataset Matrix", "Invalid dataset type " + dataSetType);
                return false;
        }
        for (String element : rowList)
            if (element == null || element.equals("") || !allowedCharacters.contains(element)) {
                ErrorLogger.logError("Validate Dataset Matrix", "Invalid data found in post-processed matrix line: " + lineNumber + " Data:" + element);
                return false;
            }
        return true;
    }

    /***
     * Assign data in respective data types.
     */
    private static List<String> initNucleotide2letterList() {
        List<String> elements = new ArrayList<>(Arrays.asList("AA", "TT", "CC", "GG", "AT", "TA", "AG", "GA", "AC", "CA", "TG", "GT", "TC", "CT", "GC", "CG", "NN",
                "++", "--", "+-", "-+", "AN", "NA", "CN", "NC", "GN", "NG", "TN", "NT"));
        for (char c : "ACGTN".toCharArray()) {
            elements.add(c + "+");
            elements.add(c + "-");
            elements.add("+" + c);
            elements.add("-" + c);
        }
        return elements;
    }

    private static List<String> initDominantList() {
        return Arrays.asList("0", "1", "N");
    }

    private static List<String> initCoDominantList() {
        return Arrays.asList("0", "1", "2", "N");
    }
}
