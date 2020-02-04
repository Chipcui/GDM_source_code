package org.gobiiproject.gobiiprocess.digester.csv.matrixValidation;

import java.util.List;

public interface RowProcessor {

	/**
	 * Processes a file row containing a series of values
	 * @param rowNo indicator of the row number, used primarily in error handling.
	 * @param inrow A delimited, unmodified row of entities, whitespace stripped (EG; {{A/C/G/T},{C/C/G/T}})
	 * @param outrow A row of the processed output (EG; {{ACGT},{CCGT}})
	 * @param matrixErrorUtil A utility callback class to register errors against
	 * @return true if the row was processed successfully, false otherwise
	 *
	 * Note: There are no contractual guarantees on the length of outrow if the return value is fales
	 */
	boolean process(int rowNo, List<String> inrow, List<String> outrow, MatrixErrorUtil matrixErrorUtil);
}
