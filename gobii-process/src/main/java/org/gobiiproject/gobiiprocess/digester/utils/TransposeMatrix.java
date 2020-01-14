package org.gobiiproject.gobiiprocess.digester.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobiimodel.utils.error.Logger;
import static org.gobiiproject.gobiimodel.utils.FileSystemInterface.lineCount;
import static org.gobiiproject.gobiimodel.utils.HelperFunctions.checkFileExistence;
import static org.gobiiproject.gobiimodel.utils.HelperFunctions.tryExec;
import static org.gobiiproject.gobiiprocess.digester.utils.TransposeMatrix.transposeMatrix;

public class TransposeMatrix {
    private static long startTime, endTime, duration;
    public static void transposeMatrix (String sep, String iFile, String oFile, String dest){
        if (!checkFileExistence(iFile)) {
            Logger.logError("Transpose Matrix", "Input file "+iFile+" provided does not exists.\n");
            return;
        }
        startTime = System.currentTimeMillis();

        switch (sep){
            case "tab":
                sep = "\t";
                break;
            case "csv":
                sep = ",";
                break;
            default:
                Logger.logError("Transpose Matrix", "Unknown file separator" + sep);
                break;
        }
        File directory = new File(dest);
        //Create as a directory if it is not
        directory.mkdir();
        tryExec("split "+iFile+" "+dest+"/_transpose_");
        File files[] = directory.listFiles();
        List<Thread> threads=new LinkedList<>();
        List<String> outFiles=new LinkedList<>();
        for (File inFile: files){
            if(inFile.getName().matches("_transpose_.*")){
                String outFile = inFile.toString().replace("_transpose_","_transposed_");
                outFiles.add(outFile);
                Thread transposeThread=new Thread(new TransposeThread(sep,inFile,outFile));
                transposeThread.start();
                threads.add(transposeThread);
            }
        }

        //Wait for all threads to complete, lets just wait for all of them in order (obviously, many will be done
        // before hand, in which case 'join' does nothing. Brilliant.
        for(Thread t:threads){
            try {
                t.join();
            }
            catch(InterruptedException e){
                Logger.logError("TransposeMatrix","Interrupt",e);
            }
        }
        StringBuilder fileList=new StringBuilder();
        for(String s:outFiles){
            fileList.append(" ").append(s);
        }
        if(fileList.length() > 0) { //if there are no files this will hang
            tryExec("paste" + fileList.toString(), oFile, null);
        }
        //Delete all intermediates once 'paste' is complete
        for (File inFile: files){
            if(inFile.getName().matches("_transposed_.*")){
                inFile.delete();
            }
        }
        //Delete intermediate folder as well
        directory.delete();
    }

    /***
     * Transposes short matrix
     * @param sep = "\t" or ","
     * @param iFile = Input File
     * @param oFile = Output File
     * @return success
     */
    protected static boolean transposeMatrix(String sep, String iFile, String oFile){
        int estimatedNumberOfLines = lineCount(iFile);//Todo - line count doesn't equate to buffered read line list, wc -l could be more or less 'lines'
        int lineNo=0, colNo=0;
        String inLine;
        int columnNumber = getColumnNumber(iFile, sep);
        if(columnNumber < 0){
            Logger.logError("Transpose Matrix", "Input file does not contain any content.\n");
        }
        String[][] matrix = new String[columnNumber][estimatedNumberOfLines];
        try(BufferedWriter buffOut=new BufferedWriter(new FileWriter(oFile));
            BufferedReader buffIn=new BufferedReader(new FileReader(iFile)))
            { while ((inLine = buffIn.readLine()) != null){
                String [] iBase = inLine.split(sep);
                colNo = 0;
                for (String base:iBase){
                    matrix[colNo][lineNo] = base;
                    colNo++;
                }
                lineNo++;
                if(lineNo > estimatedNumberOfLines){
                    //Turns out our estimate was short. Not enough space in the matrix for the transposition
                    Logger.logError("TransposeMatrix", "Lines in file exceeded 'linecount' for file. Transposition failed.");
                    break;
                }
            }
            for (int i = 0; i < colNo; i++) {
                buffOut.write(StringUtils.join(matrix[i], sep));
                buffOut.newLine();
            }
            endTime = System.currentTimeMillis();
            duration = endTime-startTime;
            Logger.logTrace("Transpose Matrix","Time taken" + "(" + iFile + "):" + duration/1000 + " Seconds");
            return true;
        } catch (FileNotFoundException e){
            Logger.logError("Transpose Matrix","Missing file", e);
        } catch (IOException e){
            Logger.logError("Transpose Matrix","IOException transposing from "+iFile+" to "+oFile, e);
        }
        return false;
    }

    /***
     * To get the number of column of a file. Checking only with first line.
     * @param file = Input File
     * @param sep = "\t" or ","
     * @return
     */
    private static int getColumnNumber(String file, String sep) {
        try(BufferedReader buffIn = new BufferedReader(new FileReader(file))){
            String[] iLine = buffIn.readLine().split(sep);
            return iLine.length;
        }catch(Exception e){
            Logger.logError("Transpose Matrix","Unable to open the input file",e);
            return -1;
        }
    }

}

class TransposeThread implements Runnable{
    String sep,oFile;
    File iFile;
    protected TransposeThread(String sep, File iFile, String oFile){
        this.sep=sep;
        this.iFile=iFile;
        this.oFile=oFile;
    }
    @Override
    public void run() {
        try {
            transposeMatrix(sep,iFile.toString(),oFile);
            //Clean up input file when done
            iFile.delete();
        }
        catch(Exception e){
            Logger.logError("TransposeThread","Error processing short file transposition",e);
        }
    }
}