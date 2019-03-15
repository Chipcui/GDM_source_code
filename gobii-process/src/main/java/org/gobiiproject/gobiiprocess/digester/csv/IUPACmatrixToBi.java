package org.gobiiproject.gobiiprocess.digester.csv;

import org.gobiiproject.gobiimodel.types.NucIupacCodes;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import static org.gobiiproject.gobiimodel.types.NucIupacCodes.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by siva on 18-05-2017.
 *
 * @author csarma on 27-11-2018
 * Converts IUPAC single code matrix to bi-allelic matrix. (WARNING: does not work with multi(2+)-allelic codes)
 */
class IUPACmatrixToBi {

    private Map<String, NucIupacCodes> hash;

    IUPACmatrixToBi() {
        hash = new HashMap<>();
        hash.put("A", AA);
        hash.put("T", TT);
        hash.put("G", GG);
        hash.put("C", CC);

        //Two potential alleles are specified as one of each
        hash.put("W", AT);
        hash.put("R", AG);
        hash.put("M", AC);
        hash.put("K", TG);
        hash.put("Y", TC);
        hash.put("S", GC);
        // deal with 0 by making it +-
        hash.put("0", plusminus);

        //Plus and minus are duplicated
        hash.put("+", plus);
        hash.put("-", minus);

        //Three potential alleles become unknown, and are set to NN
        hash.put("B", NN);
        hash.put("D", NN);
        hash.put("H", NN);
        hash.put("V", NN);
        hash.put(".", minus);//As Per GSD-456

        //N (IUPAC for 'any base' is set to NN - unknown
        hash.put("N", NN);
    }

    boolean process(int rowNo, List<String> inrow, List<String> outrow) {
        for (String element : inrow) {
            if (element.length() > 1) {
                char first = element.charAt(0);
                char last = element.charAt(element.length() - 1);
                if ((first == '+' || first == '-') && (last == '+' || last == '-')) {// takes care of "+/+" or "+/-" or "-/-" cases
                    outrow.add(first + "" + last);
                }
            } else {
                NucIupacCodes code = hash.get(element.toUpperCase());
                if (code == null) {
                    ErrorLogger.logError("IUPACMatrixToBi", "Unknown IUPAC code " + element.toUpperCase() + "in line " + rowNo);
                    return false;
                } else {
                    outrow.add(code.getName());
                }
            }
        }
        return true;
    }
}
