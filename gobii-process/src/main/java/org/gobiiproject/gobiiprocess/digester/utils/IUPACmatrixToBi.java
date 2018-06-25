package org.gobiiproject.gobiiprocess.digester.utils;

import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobiimodel.types.NucIupacCodes;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;
import static org.gobiiproject.gobiimodel.types.NucIupacCodes.*;
import static org.gobiiproject.gobiimodel.utils.HelperFunctions.checkFileExistence;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by siva on 18-05-2017.
 * Converts IUPAC single code matrix to bi-allelic matrix. (WARNING: does not work with multi(2+)-allelic codes)
 */
public class IUPACmatrixToBi {

    private static long startTime, endTime, duration;
    private static String fSep;
    private static int line;
    private static int decodeErrors=0;
    private static final int MAX_ERRORS=20;

    public static boolean convertIUPACtoBi(String sep, String iFile, String oFile, String loaderScriptPath) throws FileNotFoundException {

        if (!checkFileExistence(iFile)) {
            ErrorLogger.logError("IUPAC to Bi","Input file provided does not exist.\n");
            return false;
        }

        Map<String, NucIupacCodes> hash = new HashMap<>();

        initNuclHash(hash, loaderScriptPath);
        switch (sep) {
            case "tab":
                fSep = "\t";
                break;
            case "csv":
                fSep = ",";
                break;
            default:
                ErrorLogger.logError("IUPAC to Bi","Given file format can not be processed.");
                break;
        }
        startTime = System.currentTimeMillis();
        BufferedReader buffIn = new BufferedReader(new FileReader(iFile));
        line=0;
        try (BufferedWriter buffOut=new BufferedWriter(new FileWriter(oFile)))
        {
            String iLine;
            while ((iLine = buffIn.readLine()) != null) {
                if(iLine.equals("matrix")){
                    buffOut.newLine();
                    continue;
                }
                line++;
                String[] iNucl = iLine.split(fSep,-1);//lim -1 causes blanks at the end and beginning to be registered
                String[] oNucl;
                oNucl = new String[(iNucl.length)];
                for (int i = 0; i < iNucl.length; i++) {
                    if(iNucl[i].length() > 1){
                        char first=iNucl[i].charAt(0);
                        char last = iNucl[i].charAt(iNucl[i].length()-1);
                        if((first=='+'||first=='-') &&(last=='+'||last=='-')) {// takes care of "+/+" or "+/-" or "-/-" cases
                            oNucl[i] = first + "" +last;
                        }
                    }
                    else{
                        NucIupacCodes code =hash.get(iNucl[i].toUpperCase());
                        if(code == null){
                            ErrorLogger.logError("IUPACMatrixToBi","Unknown IUPAC code " + iNucl[i].toUpperCase() + " on data line " +line + " column "+ i);
                            decodeErrors++;
                            if(decodeErrors>MAX_ERRORS) return false;
                        }
                        else {
                            oNucl[i] = code.getName();
                        }
                    }
                }
                buffOut.write(StringUtils.join(oNucl, fSep));
                buffOut.newLine();
            }
            buffOut.close();
            buffIn.close();
            endTime = System.currentTimeMillis();
            duration = endTime-startTime;
            ErrorLogger.logTrace("IUPAC to Bi","Time taken:"+ duration/1000 + " Secs");
        } catch (FileNotFoundException e){
            ErrorLogger.logError("IUPAC to Bi","Missing output File:", e);
        } catch (IOException e){
            ErrorLogger.logError("IUPAC to Bi","Unexpected error", e);
        }
        return true;
    }

    /***
     * Initializing IUPAC nucleotide Dictionary
     * @param hash passed in hashmap to be filled with valid values of IUPAC data (I/O)
     * @param loaderScriptPath Base script directory. Used to calculate where the missing indicators .txt file is to use that for extra
     *                         null values.
     */
    private static void initNuclHash(Map<String,NucIupacCodes> hash, String loaderScriptPath){
        hash.put("A",AA);
        hash.put("T",TT);
        hash.put("G",GG);
        hash.put("C",CC);

        //Two potential alleles are specified as one of each
        hash.put("W",AT);
        hash.put("R",AG);
        hash.put("M",AC);
        hash.put("K",TG);
        hash.put("Y",TC);
        hash.put("S",GC);
        // deal with 0 by making it +-
        hash.put("0",plusminus);

        //Plus and minus are duplicated
        hash.put("+",plus);
        hash.put("-",minus);

        //Three potential alleles become unknown, and are set to NN
        hash.put("B",NN);
        hash.put("D",NN);
        hash.put("H",NN);
        hash.put("V",NN);
        hash.put(".",minus);//As Per GSD-456

        //N (IUPAC for 'any base' is set to NN - unknown
        hash.put("N",NN);

        //NN for anything in 'missing indicators' text file.
        try {
            File unknownsFile = new File(loaderScriptPath, "etc/missingIndicators.txt");
            BufferedReader br = new BufferedReader(new FileReader(unknownsFile));
            String next;
            while((next=br.readLine())!=null){
                hash.put(next,NN);
            }
        }catch (Throwable e){
            ErrorLogger.logWarning("IUPACMatrixToBI",e.getMessage());
        }
    }
}
