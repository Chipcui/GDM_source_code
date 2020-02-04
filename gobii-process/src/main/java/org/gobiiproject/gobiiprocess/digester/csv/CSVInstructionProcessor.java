package org.gobiiproject.gobiiprocess.digester.csv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiFileColumn;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderProcedure;
import org.gobiiproject.gobiimodel.types.GobiiColumnType;
import org.gobiiproject.gobiimodel.types.GobiiFileType;
import org.gobiiproject.gobiimodel.utils.FileSystemInterface;
import org.gobiiproject.gobiimodel.utils.HelperFunctions;
import org.gobiiproject.gobiimodel.utils.error.Logger;
import org.gobiiproject.gobiiprocess.digester.Digester;
import org.gobiiproject.gobiiprocess.digester.DigesterInstructionProcessor;
import org.gobiiproject.gobiiprocess.digester.LoaderGlobalConfigs;
import org.gobiiproject.gobiiprocess.digester.MatrixProcessorResult;
import org.gobiiproject.gobiiprocess.digester.ProcessorResult;
import org.gobiiproject.gobiiprocess.digester.csv.matrixValidation.MatrixValidation;
import org.gobiiproject.gobiiprocess.digester.csv.matrixValidation.ValidationResult;

/**
 * CSV-Specific File Loader class, used by
 * {@link Digester} Contains
 * methods specific to reading of single-character separated text files, such as
 * .csv, tab-delimited, and pipe-separated values. This class is used, when none
 * of GobiiColumnType in GobiiLoaderInstruction
 * {@link org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction}
 * is CSV_COL.
 * Assumptions:
 * No more than one CSV_BOTH can exist in the instruction.
 * No other column in CSV_BOTH
 * Only one of CSV_ROW, CSV_COL, CSV_BOTH can exist in the file.
 *
 * @author CSarma
 * @date 3/23/2017
 */

public class CSVInstructionProcessor implements DigesterInstructionProcessor {

    private static final String NEWLINE = "\n";
    private static final String TAB = "\t";
    private final String loaderScriptPath;
    private GobiiProcessedInstruction processedInstruction;
    private int maxLines = 0;

    public CSVInstructionProcessor(String loaderScriptPath) {
        this.loaderScriptPath = loaderScriptPath;
    }

    /**
     * Reads the input file specified by the loader instruction and creates a
     * digest file based on the instruction.
     *
     * @param loaderInstruction Singular instruction, specifying input and output directories
     */
    public ProcessorResult process(GobiiLoaderProcedure procedure, GobiiLoaderInstruction loaderInstruction) {

        processedInstruction = new GobiiProcessedInstruction(loaderInstruction);
        processedInstruction.parseInstruction();

        String outputFileName = HelperFunctions.getDestinationFile(procedure, loaderInstruction);
        try (BufferedWriter tempFileBufferedWriter = new BufferedWriter(new FileWriter(outputFileName))) {

            File file = new File(procedure.getMetadata().getGobiiFile().getSource());
            File outputFile = new File(HelperFunctions.getDestinationFile(procedure, loaderInstruction));
            if (file.isDirectory()) {
                listFilesFromFolder(file, tempFileBufferedWriter, procedure, outputFile);
            } else {
                return writeToOutputFile(file, tempFileBufferedWriter, procedure, outputFile, true);
            }
        } catch (FileNotFoundException e) {
            Logger.logError("CSVReader", "Unexpected Missing File", e);
        } catch (IOException e) {
            Logger.logError("CSVReader", "Unexpected IO Error", e);
        }

        return null;
    }

    /**
     * Finds all files in DIR{@code folder}, reading each file for the
     * data needed, and writing to {@code tmpFileBufferedWriter}. Sub
     * folders(Nested folders) are ignored.
     *
     * @param folder                 Folder in the file-system to start from (input folder)
     * @param tempFileBufferedWriter Output File writer
     */
    private void listFilesFromFolder(File folder, BufferedWriter tempFileBufferedWriter,
                                     GobiiLoaderProcedure procedure, File outputFile) {
        if (folder == null) {
            Logger.logWarning("CSV Instruction Processor", "Read from null folder");
            return;
        }
        boolean firstFile = true; //TODO - generation of metadata requires deduplication beyond 'dedup', placing here
        for (File file : folder.listFiles()) {
            // Sub folders are ignored
            if (file.isFile() & !file.getName().contains("digest")) {
                try {
                    writeToOutputFile(file, tempFileBufferedWriter, procedure, outputFile, firstFile);
                    firstFile=false;
                } catch (IOException e) {
                    Logger.logError("CSVReader", "Failure to write digest files", e);
                }
            }
        }
    }

    /**
     * Reads data from a single input file, and writes to digest file
     * (referenced by {@code }tmpFileBufferedsWriter}. This method is primarily called by
     * {@link CSVInstructionProcessor listFilesFromFolder(File, BufferedWriter, GobiiLoaderInstruction, boolean)}
     *
     * @param file                   File to read from
     * @param tempFileBufferedWriter output file writer
     * @throws IOException when the requisite file is missing or cannot be read
     */
    private ProcessorResult writeToOutputFile(File file, BufferedWriter tempFileBufferedWriter,
                                              GobiiLoaderProcedure procedure, File outputFile, boolean firstFile) throws IOException {

        if (processedInstruction.hasCSV_ROW()) {
            if(!firstFile){
                return null; //TODO - assumption that this is a duplicated 'normal' oriented file.
                //Multiple files are stacked 'vertically'. This "feature" is very jank, and this bit'll have to be ripped
                //out while replacing it.
            }
            processCsvRow(file, tempFileBufferedWriter, procedure);
        } else if (processedInstruction.hasCSV_COL()) {
            processCsvColumn(file, tempFileBufferedWriter, procedure);
        } else if (processedInstruction.hasCSV_BOTH()) {
            return processCsvBoth(file, tempFileBufferedWriter, procedure, outputFile);
        }

        return null;
    }

    /**
     * Creates digest table for CSV_ROW.
     *
     * @param file                   Input file to read from.
     * @param tempFileBufferedWriter Output file writer.
     * @throws IOException Exception in I/O operations
     */
    private void processCsvRow(File file, BufferedWriter tempFileBufferedWriter, GobiiLoaderProcedure procedure) throws IOException {
        readCsvRows(file, procedure);
        // Added for consistency in flow. For CSV_ROW this variable is not used. So empty list is passed
        ArrayList<String> rowList = new ArrayList<>();
        if (processedInstruction.isFirstLine()) {
            writeFirstLine(tempFileBufferedWriter);
            processedInstruction.setFirstLine(false);
        }
        for (int rowNo = 0; rowNo < maxLines; rowNo++) {
            writeOutputLine(tempFileBufferedWriter, rowList);
        }
    }

    /**
     * Creates digest table for CSV_COL.
     *
     * @param file                   Input file to read from.
     * @param tempFileBufferedWriter Output file writer.
     */

    private void processCsvColumn(File file, BufferedWriter tempFileBufferedWriter,
                                  GobiiLoaderProcedure procedure) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            int rowNo = 0;
            String fileRow;
            if (processedInstruction.isFirstLine()) {
                writeFirstLine(tempFileBufferedWriter);
                processedInstruction.setFirstLine(false);
            }
            // All the columns should start on the same row. Else there will be mismatch in the length of col's.
            int rowNoInGobiiColumn = 0;
            for (GobiiFileColumn gobiiFileColumn : processedInstruction.getColumnList()) {
                if (gobiiFileColumn.getGobiiColumnType().equals(GobiiColumnType.CSV_COLUMN)) {
                    rowNoInGobiiColumn = gobiiFileColumn.getrCoord();
                    break;
                }
            }
            List<Integer> reqCols = getRequiredColNo();
            while ((fileRow = bufferedReader.readLine()) != null) {

                String[] row = fileRow.split(procedure.getMetadata().getGobiiFile().getDelimiter(), -1);//Need to capture blank trailing values

                if (rowNo >= rowNoInGobiiColumn) {
                    ArrayList<String> rowList = new ArrayList<>();
                    for (Integer colNo : reqCols) {
                        rowList.add(row[colNo]);
                    }
                    getCol(rowList);
                    writeOutputLine(tempFileBufferedWriter, rowList);
                }
                rowNo++;
            }
        } catch (FileNotFoundException e) {
            Logger.logError("CSVReader", "Unexpected Missing File", e);
        } catch (IOException e) {
            Logger.logError("CSVReader", "Unexpected IO Error", e);
        }
    }

    /**
     * Creates digest table for CSV_BOTH.
     *
     * @param file                   Input file to read from.
     * @param tempFileBufferedWriter Output file writer.
     * #returns Pair of row, column counts as integers
     *
     * @throws IOException Exception in I/O operations
     */

    private MatrixProcessorResult processCsvBoth(File file, BufferedWriter tempFileBufferedWriter,
                                                 GobiiLoaderProcedure procedure, File outputFile) throws IOException {
        Integer totalCols=null;
        Integer totalRows=null;
        boolean skipValidation = !LoaderGlobalConfigs.isEnableValidation();

        GobiiFileColumn csvBothColumn = null;
        for (GobiiFileColumn gobiiFileColumn : processedInstruction.getColumnList()) {
            if (gobiiFileColumn.getGobiiColumnType().equals(GobiiColumnType.CSV_BOTH)) {
                csvBothColumn = gobiiFileColumn;
                break;
            }
        }
        // For matrix file there is no need of writing first line. Setting it to false for consistency
        processedInstruction.setFirstLine(false);
        String missingFile = loaderScriptPath + "/etc/missingIndicators.txt";
        String parentDirectory = outputFile.getParentFile().getAbsolutePath();
        String markerFile = parentDirectory + "/digest.marker";
        MatrixValidation matrixValidation = new MatrixValidation(procedure.getMetadata().getDatasetType().getName(), missingFile, markerFile);
        if (matrixValidation.setUp()) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
                int rowNo = 0;
                String fileRow;
                ArrayList<String> inputRowList, outputRowList;
                String delimiter = procedure.getMetadata().getGobiiFile().getDelimiter();
                boolean isVCF = procedure.getMetadata().getGobiiFile().getGobiiFileType().equals(GobiiFileType.VCF);
                while ((fileRow = bufferedReader.readLine()) != null) {
                    if (rowNo >= csvBothColumn.getrCoord()) {
                        inputRowList = new ArrayList<>(Arrays.asList(fileRow.split(delimiter)));
                        outputRowList = new ArrayList<>();
                        getRow(inputRowList, csvBothColumn);
                        ValidationResult validationResult=matrixValidation.validate(rowNo, csvBothColumn.getrCoord(), inputRowList, outputRowList, isVCF, skipValidation);
                        if (validationResult.success) {
                            writeOutputLine(tempFileBufferedWriter, outputRowList);
                            totalCols=validationResult.numRows;
                        }
                        else {
                            if (matrixValidation.stopProcessing()) {
                                tempFileBufferedWriter.flush();
                                tempFileBufferedWriter.close();
                                FileSystemInterface.rmIfExist(HelperFunctions.getDestinationFile(procedure, procedure.getInstructions().get(0)));
								MatrixProcessorResult result = new MatrixProcessorResult();
								result.setNumColumns(totalCols);
								result.setNumRows(rowNo);
                                return result;
                            }
                        }
                    }
                    rowNo++;
                }
                totalRows = rowNo - csvBothColumn.getrCoord();
            }
        }
        if (matrixValidation.getErrorCount() != 0) {
            tempFileBufferedWriter.flush();
            tempFileBufferedWriter.close();
            FileSystemInterface.rmIfExist(HelperFunctions.getDestinationFile(procedure, procedure.getInstructions().get(0)));
        }
		MatrixProcessorResult result = new MatrixProcessorResult();
		result.setNumColumns(totalCols);
		result.setNumRows(totalRows);
		return result;
    }

    /**
     * Writes the first line to the file. Contains column names.
     *
     * @param tempFileBufferedWriter Output file writer.
     * @throws IOException Exception in I/O operations.
     */
    private void writeFirstLine(BufferedWriter tempFileBufferedWriter) throws IOException {
        StringBuilder outputLine = new StringBuilder();
        for (GobiiFileColumn column : processedInstruction.getColumnList()) {
            appendTabToOutput(outputLine, column);
            appendColumnName(outputLine, column);
        }
        tempFileBufferedWriter.write(outputLine.toString());
        tempFileBufferedWriter.write(NEWLINE);
    }

    /**
     * Adds column name if it is not a sub-column
     *
     * @param outputLine Line that is be written to file.
     * @param column     Column whose name is appended.
     */
    private void appendColumnName(StringBuilder outputLine, GobiiFileColumn column) {
        if (!column.isSubcolumn())
            outputLine.append(column.getName());
    }

    /**
     * Adds tab to output if it is not at beginning of line or not a sub-column
     *
     * @param outputLine Line that is be written to file.
     * @param column     Column whose name is appended.
     */
    private void appendTabToOutput(StringBuilder outputLine, GobiiFileColumn column) {
        if (outputLine.length() > 0) {
            if (!column.isSubcolumn())
                outputLine.append(TAB);
        }
    }

    /**
     * Writes a line to output file.
     *
     * @param tempFileBufferedWriter Output file writer.
     * @param rowList                Contains the columns that needs to be appended.
     * @throws IOException I/O Exception
     * TODO: this method only works performantly in an array list, and see below TODO which does not even do that.
     */
    private void writeOutputLine(BufferedWriter tempFileBufferedWriter, ArrayList<String> rowList) throws IOException {
        StringBuilder outputLine = new StringBuilder();
        // Used in traversing requiredRows
        int rowNo = 0;
        int rowIndex = 0; //position of elements 'removed' from rowList.
        for (FileLineEntry entry : processedInstruction.getFileLine()) {
            GobiiFileColumn column = processedInstruction.getColumnList().get(entry.getColumnNo());
            switch (entry.getColumnType()) {
                case CONSTANT:
                    appendTabToOutput(outputLine, column);
                    outputLine.append(entry.getValue());
                    break;
                case AUTOINCREMENT:
                    appendTabToOutput(outputLine, column);
                    outputLine.append(entry.getValue());
                    String increment = (Integer.parseInt(entry.getValue()) + 1) + "";
                    entry.setValue(increment);
                    break;
                case CSV_ROW:
                    appendTabToOutput(outputLine, column);
                    //TODO- this is also super bad, using remove on a long array list
                    outputLine.append(processedInstruction.getRequiredRows().get(rowNo).remove(0));
                    rowNo++;
                    break;
                case CSV_COLUMN:
                    appendTabToOutput(outputLine, column);
                    outputLine.append(rowList.get(rowIndex++));
                    break;
                case CSV_BOTH:
                    while (rowList.size() > rowIndex) {
                        appendTabToOutput(outputLine, column);
                        outputLine.append(rowList.get(rowIndex++));
                    }
                    break;
                default:
                    break;
            }
        }

        tempFileBufferedWriter.write(outputLine.toString());
        tempFileBufferedWriter.write(NEWLINE);
    }

    /**
     * Reads all the required csv_rows and stores in requiredRows list. Stops
     * processing once all the required rows are read.
     *
     * @param file              Input file.
     */
    private void readCsvRows(File file, GobiiLoaderProcedure procedure) {
        int maxRequiredRowNo = maxRequiredRow();



        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            int rowNo = 0;
            String fileRow;
            while ((fileRow = bufferedReader.readLine()) != null) {
                if (rowNo > maxRequiredRowNo) {
                    // All required rows read.
                    break;
                }
                String[] row = fileRow.split(procedure.getMetadata().getGobiiFile().getDelimiter());
                // Can't initialize here as it creates issue when same row is
                // used multiple gobiiFileColumn's.
                ArrayList<String> rowList;
                // Check for which all columns this row is required.
                int currentCol = 0;
                for (GobiiFileColumn column : processedInstruction.getColumnList()) {
                    if (column.getGobiiColumnType().equals(GobiiColumnType.CSV_ROW)) {
                        if (column.getrCoord() == rowNo) {
                            rowList = new ArrayList<>(Arrays.asList(row));
                            getRow(rowList, column);
                            processedInstruction.addRow(currentCol, rowList);

                            if (maxLines < rowList.size())
                                maxLines = rowList.size();
                        }
                        currentCol++;
                    }
                }
                rowNo++;
            }
        } catch (FileNotFoundException e) {
            Logger.logError("CSVReader", "Unexpected Missing File", e);
        } catch (IOException e) {
            Logger.logError("CSVReader", "Unexpected IO Error", e);
        }

    }

    /**
     * Applying filter on the column data.
     * Parses all the columns to identify CSV_COLUMN to identify the required columns.
     *
     * @param rowList List of the required columns required.
     */
    private void getCol(List<String> rowList) {
        int colNo = 0;
        for (GobiiFileColumn column : processedInstruction.getColumnList()) {
            if (column.getGobiiColumnType().equals(GobiiColumnType.CSV_COLUMN)) {
                rowList.set(colNo, HelperFunctions.filter(rowList.get(colNo), column.getFilterFrom(), column.getFilterTo(),
                        column.getFindText(), column.getReplaceText()));
                colNo++;
            }
        }
    }

    /**
     * Returns a list of required column No's
     */
    private List<Integer> getRequiredColNo() {
        List<Integer> reqCols = new ArrayList<>();
        for (GobiiFileColumn column : processedInstruction.getColumnList()) {
            if (column.getGobiiColumnType().equals(GobiiColumnType.CSV_COLUMN)) {
                reqCols.add(column.getcCoord());
            }
        }
        return reqCols;
    }

    /**
     * Removing columns till cCoord and applying filter.
     *
     * @param rowList row from the file
     * @param column  gobii file column
     */
    private void getRow(List<String> rowList, GobiiFileColumn column) {
        int colNo = 0;
        for (String element : rowList) {
            rowList.set(colNo, HelperFunctions.filter(element, column.getFilterFrom(), column.getFilterTo(),
                    column.getFindText(), column.getReplaceText()));
            colNo++;
        }
        for (int cCoord = 0; cCoord < column.getcCoord(); cCoord++) {
            rowList.remove(0);
        }
    }

    /**
     * Max row number required to be read for the 'required' rows to all fit into what was read.
     * Synonomous with the highest numbered row of metadata, with exception of if no row exists, one row is returned
     * to read at least one row. (Required for the correct calculation of matrix sizes).
     * @return max required row no.
     */
    private int maxRequiredRow() {
        int maxRowNo = -1;
        for (GobiiFileColumn gobiiFileColumn : processedInstruction.getColumnList()) {
            if ((gobiiFileColumn.getGobiiColumnType().equals(GobiiColumnType.CSV_ROW))
                    && (maxRowNo < gobiiFileColumn.getrCoord()))
                maxRowNo = gobiiFileColumn.getrCoord();
        }
        if(maxRowNo==0){
            maxRowNo=1; //need to read at least one row to know maxLines.
        }
        return maxRowNo;
    }
}