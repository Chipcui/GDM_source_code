package org.gobiiproject.gobiiprocess.digester.csv.matrixValidation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.gobiiproject.gobiimodel.types.NucIupacCodes;
import static org.gobiiproject.gobiimodel.types.NucIupacCodes.AA;
import static org.gobiiproject.gobiimodel.types.NucIupacCodes.AC;
import static org.gobiiproject.gobiimodel.types.NucIupacCodes.AG;
import static org.gobiiproject.gobiimodel.types.NucIupacCodes.AT;
import static org.gobiiproject.gobiimodel.types.NucIupacCodes.CC;
import static org.gobiiproject.gobiimodel.types.NucIupacCodes.GC;
import static org.gobiiproject.gobiimodel.types.NucIupacCodes.GG;
import static org.gobiiproject.gobiimodel.types.NucIupacCodes.NN;
import static org.gobiiproject.gobiimodel.types.NucIupacCodes.TC;
import static org.gobiiproject.gobiimodel.types.NucIupacCodes.TG;
import static org.gobiiproject.gobiimodel.types.NucIupacCodes.TT;
import static org.gobiiproject.gobiimodel.types.NucIupacCodes.minus;
import static org.gobiiproject.gobiimodel.types.NucIupacCodes.plus;
import static org.gobiiproject.gobiimodel.types.NucIupacCodes.plusminus;

/**
 * Created by siva on 18-05-2017.
 *
 * @author csarma on 27-11-2018
 * Converts IUPAC single code matrix to bi-allelic matrix. (WARNING: does not work with multi(2+)-allelic codes)
 */
class IUPACmatrixToBi implements RowProcessor{

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

    public boolean process(int rowNo, List<String> inrow, List<String> outrow, MatrixErrorUtil matrixErrorUtil) {
        boolean returnStatus = true;
        for (String element : inrow) {
            if (element.length() > 1) {
                char first = element.charAt(0);
                char last = element.charAt(element.length() - 1);
                // if ((first == '+' || first == '-') && (last == '+' || last == '-')) {// takes care of "+/+" or "+/-" or "-/-" cases
                // Irrespective of the character, we need to add it to the outlist
                outrow.add(first + "" + last);
            } else {
                NucIupacCodes code = hash.get(element.toUpperCase());
                if (code == null) {
                    matrixErrorUtil.setError("IUPACMatrixToBi Unknown IUPAC code " + element.toUpperCase() + "in line " + rowNo);
                    returnStatus = false;
                } else
                    outrow.add(code.getName());
            }
        }
        return returnStatus;
    }
}
