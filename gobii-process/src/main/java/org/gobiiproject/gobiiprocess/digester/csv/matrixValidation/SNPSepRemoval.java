package org.gobiiproject.gobiiprocess.digester.csv.matrixValidation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author csarma
 * Removes separator from SNP
 */
class SNPSepRemoval implements RowProcessor {

    private List<String> alleles, missingAlts, missingFromFile;

    SNPSepRemoval(List<String> missingFileElements) {
        alleles = new ArrayList<>();
        alleles.add("A");
        alleles.add("C");
        alleles.add("G");
        alleles.add("T");
        alleles.add("N");
        alleles.add("+");
        alleles.add("-");
        alleles.add("?");
        alleles.add("0");
        alleles.add("");

        missingAlts = new ArrayList<>();
        missingAlts.add("?");
        missingAlts.add("0");

        missingFromFile = new ArrayList<>();
        missingFromFile.addAll(missingFileElements);
        missingFromFile.remove("");//Just in case
    }

    public boolean process(int rowNo, List<String> inrow, List<String> outrow, MatrixErrorUtil matrixErrorUtil) {
        boolean returnStatus = true;
        for (String element : inrow) {
            if (element.length() < 1) {
                String errMsg = "SNPSepRemoval Unsupported empty Allele Call in row " + rowNo;
                matrixErrorUtil.setError(errMsg);
                returnStatus = false;
            } else {
                if (missingFromFile.contains(element)) {
                    outrow.add("NN");
                } else {
                    final String s = "SNPSepRemoval Unsupported Allele Call " + element.charAt(0) + " " + element.charAt(element.length() - 1) + " in row " + rowNo;

                    String allele1, allele2;
                    if (alleles.contains(String.valueOf(element.charAt(0))) && alleles.contains(String.valueOf(element.charAt(element.length() - 1)))) {
                        allele1 = String.valueOf(element.charAt(0));
                        allele2 = String.valueOf(element.charAt(element.length() - 1));
                        if (missingAlts.contains(allele1)) allele1 = "N";
                        if (missingAlts.contains(allele2)) allele2 = "N";
                        outrow.add(allele1 + allele2);
                    } else {
                        matrixErrorUtil.setError(s);
                        returnStatus = false;
                    }
                }
            }
        }
        return returnStatus;
    }
}