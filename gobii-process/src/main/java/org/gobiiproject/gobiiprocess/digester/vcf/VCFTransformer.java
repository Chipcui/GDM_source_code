package org.gobiiproject.gobiiprocess.digester.vcf;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

/**
 * Created by AVB on 1/5/2017.
 * <p>
 * Bares all the matrix data by rejecting anything after the ":" character inclusive and
 * creates a file of bimatrix data in the "bimatrix" file by using
 * the following rules:
 * a) the (bare) matrix cell must be X/Y where X, Y can be 0 or 1 or . so there are 9 possibilities: 0/0, 0/1, 0/., 1/0, 1/1, 1/., ./0, ./1, and ./.
 * b) 0 from the matrix cell -> the cell from the same row in the .mref matrix but in the first column "ref"
 * c) 1 from the matrix cell -> the cell from the same row in the .mref matrix but in the second column "alt"
 * d) . from the matrix cell -> N
 **/
public class VCFTransformer {

    //Bi-allelic unknown character state
    private static final String BI_UNKNOWN = "N";

    //Maximum Transformer errors in one file, because you can get a lot of them
    private static final int MAX_ERRORS = 20;
    private static int errorsSoFar = 0;

    /**
     * @param lineNumber      line processed
     * @param mrefLineData    contains reference and alternates
     * @param matrixLineDatum single entry (eg: 0/0) of the bi-allelic datum
     * @param col             column processed
     * @return unseparated bi-allelic closest approximation of the VCF line data
     */
    //TODO - need to add some separation to appropriately deal with multi-allelic alternates downstream - potentially wrapper class
    @SuppressWarnings("StringConcatenationInLoop")
    private static String getBiallelicEntry(int lineNumber, String[] mrefLineData, String matrixLineDatum, int col, boolean unspecifiedAlt) {
        String[] terms = matrixLineDatum.split("/");
        if (terms.length != 2) {
            if (++errorsSoFar < MAX_ERRORS) {
                ErrorLogger.logError("VCFTransformer", "Incorrect number of alleles: " + matrixLineDatum
                        + "\n Data line " + lineNumber + " column " + col);
            }
            return null;
        }
        String bimatrixCell = "";
        for (int k = 0; k < 2; k++) {
            switch (terms[k]) {
                case "0":
                case "1":
                case "2":
                case "3":
                case "4":
                case "5":
                case "6":
                case "7":
                case "8":
                case "9":
                    int value = Integer.parseInt(terms[k]);
                    if (value >= mrefLineData.length) {
                        if (unspecifiedAlt) {
                            ErrorLogger.logWarning("VCFTransformer", "Alternate number " + terms[k] + " specified with '.' alts listed on data line " + lineNumber + " column " + col);
                        } else {
                            if (++errorsSoFar < MAX_ERRORS) {
                                ErrorLogger.logError("VCFTransformer", "Alternate number " + terms[k] + " exceeds the length of the alternates list on data line " + lineNumber + " column " + col);
                            }
                        }
                        bimatrixCell += BI_UNKNOWN;
                    } else {
                        String cellValue = mrefLineData[value];
                        if (cellValue.equals(".")) cellValue = BI_UNKNOWN;
                        bimatrixCell += cellValue;
                    }
                    break;
                case ".":
                    bimatrixCell += BI_UNKNOWN;
                    break;
                default:
                    if (++errorsSoFar < MAX_ERRORS) {
                        ErrorLogger.logError("VCFTransformer", "Unsupported alternate: " + terms[k] + " on data line " + lineNumber + " column " + col);
                    }
                    return null;
            }
        }
        return bimatrixCell;
    }

    public static boolean transformVCFLine(String mrefLine, int lineNumber, List<String> inputList, List<String> outputList) throws IOException {
        lineNumber++;
        boolean unspecifiedAlt = false;
        String[] mrefLineRawData = mrefLine.trim().toUpperCase().split("\\s+");
        String[] mrefAltData = new String[]{""};

        //If alternates are unspecified (.), need to handle as a special case. Will let AltData become an empty lsit, and all alts will be dealt with appropriately.
        //And since it looks like ""{value}"",""{Value}"", lets just use contains.
        if (mrefLineRawData[1].contains(".")) {
            unspecifiedAlt = true;
        }
        mrefAltData = mrefLineRawData[1].split("[^A-Za-z\\-.]+");//Split on non-alphabetic, may be jsonified already

        if (mrefAltData.length == 0)
            mrefAltData = new String[]{""};//if we have no entries, make it a blank first and all below will work fine

        int offset = 0;//offset for first alt being blank (happens with this regex and split (freaking split)
        if (mrefAltData[0].equals("")) offset = 1;

        String mrefLineData[] = new String[mrefAltData.length + 1 - offset];//List of 1 + alts length
        mrefLineData[0] = mrefLineRawData[0];
        System.arraycopy(mrefAltData, 0 + offset, mrefLineData, 1, mrefAltData.length - offset);

        //Fix - to N
        for (int i = 0; i < mrefLineData.length; i++) {
            if (mrefLineData[i].equals("-")) mrefLineData[i] = "N";// - is technically not valid
            if (mrefLineData[i].equals("*")) mrefLineData[i] = BI_UNKNOWN;// * is upstream deletion
            if (mrefLineData[i].equals("INS"))
                mrefLineData[i] = "+";// Technically, all these are supposed to be info fields
            if (mrefLineData[i].equals("DEL")) mrefLineData[i] = "-";
            if (mrefLineData[i].equals("DUP")) mrefLineData[i] = BI_UNKNOWN;
            if (mrefLineData[i].equals("INV")) mrefLineData[i] = BI_UNKNOWN;
        }

        outputList = new LinkedList<String>();
        for (int i = 0; i < inputList.size(); i++) {
            String input = inputList.get(i);
            String alleles = input.split(":")[0];
            String bimatrixCell = getBiallelicEntry(lineNumber, mrefLineData, alleles, i, unspecifiedAlt);
            if (bimatrixCell == null) {
                return false;
            }
            outputList.add(bimatrixCell);
        }
        return true;
    }
}
