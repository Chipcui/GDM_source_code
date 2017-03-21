package org.gobiiproject.gobiiprocess.extractor.hapmap;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import org.apache.commons.lang.StringUtils;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import static org.gobiiproject.gobiimodel.utils.FileSystemInterface.rmIfExist;

public class HapmapTransformer {

	private static Integer dnaIndex = -1;

	public boolean generateFile(String markerFileIn, String sampleFileIn, String extendedMarkerFileIn, String genotypeFileIn, String outFile, String errorFile) throws IOException {
         ///////////////////////////
		// marker file (mandatory)
		////////////////////////////
		File markerFile = new File(markerFileIn);
		if (!(markerFile.exists())) {
			ErrorLogger.logError("Extractor","Marker file not found", errorFile);
			return false;
		}
		else {
			if (!(markerFile.isFile())) {
				ErrorLogger.logError("Extractor","Marker file not correct", errorFile);
				return false;
			}
		}

		////////////////////////////
		// sample file (mandatory)
		////////////////////////////
		File sampleFile = new File(sampleFileIn);
		if (!(sampleFile.exists())) {
			ErrorLogger.logError("Extractor","Sample file not found", errorFile);
			return false;
		}
		else {
			if (!(sampleFile.isFile())) {
				ErrorLogger.logError("Extractor","Sample file not correct", errorFile);
				return false;
			}
		}

		////////////////////////////////////////////////////////
		// extended marker file (optional, so existent or not)
		////////////////////////////////////////////////////////
		File extendedMarkerFile = new File(extendedMarkerFileIn);
		if (!(extendedMarkerFile.exists())) {
			ErrorLogger.logInfo("Extractor","Extended marker file not found");
			extendedMarkerFile = null;
		}
		else {
			if (!(extendedMarkerFile.isFile())) {
				ErrorLogger.logInfo("Extractor","Extended marker file not correct");
				extendedMarkerFile = null;
			}
		}

		//////////////////////////////
		// genotype file (mandatory)
		//////////////////////////////
		File genotypeFile = new File(genotypeFileIn);
		if (!(genotypeFile.exists())) {
			ErrorLogger.logError("Extractor","Genotype file not found", errorFile);
			return false;
		}
		else {
			if (!(genotypeFile.isFile())) {
				ErrorLogger.logError("Extractor", "Genotype file not correct", errorFile);
				return false;
			}
		}

		Scanner markerScanner = new Scanner(markerFile);
		Scanner sampleScanner = new Scanner(sampleFile);
		Scanner extendedMarkerScanner = new Scanner(extendedMarkerFile);
		Scanner genotypeScanner = new Scanner(genotypeFile);

		if (sampleScanner.hasNextLine()) {
			///////////////////
			// sample headers
			///////////////////
			List<String> sampleHeaders = new ArrayList<>();
			TreeMap<Integer, ArrayList<String>> sampleData = new TreeMap<>();
			String[] headers = sampleScanner.nextLine().split("\\t");
			ErrorLogger.logInfo("Extractor", headers.length + " sample header columns read");
			for (int index = 0; index < headers.length; index++) {
				sampleHeaders.add(headers[index].trim());
				sampleData.put(index, new ArrayList<String>());
			}
			int sampleRowsNumber = 0;
			while (sampleScanner.hasNextLine()) {
				String[] sampleRecords = sampleScanner.nextLine().split("\\t");
				for (int index = 0; index < sampleRecords.length; index++) {
					if (!(sampleData.containsKey(index))) {
						sampleData.put(index, new ArrayList<String>());
					}
					sampleData.get(index).add(sampleRecords[index].trim());
				}
				sampleRowsNumber = sampleRowsNumber + 1;
			}
			ErrorLogger.logInfo("Extractor", sampleRowsNumber + " sample data rows read");
			sampleScanner.close();
			if (sampleRowsNumber == 0) {
				ErrorLogger.logError("Extractor", "No sample data rows", errorFile);
				return false;
			}
			try {
				File out = new File(outFile);
				rmIfExist(outFile);
				FileWriter fileWriter = new FileWriter(out);

				// Transposing all the sample data with the exception of
				// the dnarun_name column into the output file
				for (Map.Entry<Integer, ArrayList<String>> entry : sampleData.entrySet()) {
					String header = sampleHeaders.get(entry.getKey());
					if (header.equals("dnarun_name")) {
						dnaIndex = entry.getKey();
						continue;
					}
					StringBuilder stringBuilderNewLine = new StringBuilder("#\t\t\t\t\t\t\t\t\t\t");
					stringBuilderNewLine.append(header);
					stringBuilderNewLine.append("\t");
					stringBuilderNewLine.append(StringUtils.join(entry.getValue(), "\t"));
					stringBuilderNewLine.append(System.lineSeparator());
					fileWriter.write(stringBuilderNewLine.toString());
				}
				fileWriter.flush();

				///////////////////
				// marker headers
				///////////////////
				List<String> markerHeaders = new ArrayList<>();
				if (markerScanner.hasNextLine()) {
					headers = markerScanner.nextLine().split("\\t");
					ErrorLogger.logInfo("Extractor", headers.length + " marker header columns read");
					for (int index = 0; index < headers.length; index++) {
						markerHeaders.add(headers[index].trim());
					}
				}
				else {
					ErrorLogger.logError("Extractor","Marker data file empty", errorFile);
					return false;
				}

				// Writing the new marker headers into the current line
				String[] newMarkerHeadersLine = new String[]{
						"marker_name",
						"alleles",
						"chrom",
						"pos",
						"strand",
						"assembly#",
						"center",
						"protLSID",
						"assayLSID",
						"panelLSID",
						"QCcode"};
				StringBuilder stringBuilderNewLine = new StringBuilder(StringUtils.join(newMarkerHeadersLine, "\t"));
				stringBuilderNewLine.append("\t");

				/////////////////////////////////////////////////
				// extended marker headers (if existent or not)
				/////////////////////////////////////////////////
				List<String> extendedMarkerHeaders = new ArrayList<>();
				if (extendedMarkerScanner != null) {
					if (extendedMarkerScanner.hasNextLine()) {
						headers = extendedMarkerScanner.nextLine().split("\\t");
						ErrorLogger.logInfo("Extractor", headers.length + " extended marker header columns read");
						for (int index = 0; index < headers.length; index++) {
							extendedMarkerHeaders.add(headers[index].trim());
						}
					}
					else {
						ErrorLogger.logInfo("Extractor","Extended marker data file empty");
					}
				}

				// Transposing and writing the dnarun_name column into the current line,
				// and then writing the current line into the output file
				stringBuilderNewLine.append(StringUtils.join(sampleData.get(dnaIndex), "\t"));
				stringBuilderNewLine.append(System.lineSeparator());
				fileWriter.write(stringBuilderNewLine.toString());

				if (!(markerScanner.hasNextLine())) {
					ErrorLogger.logError("Extractor","No marker data rows", errorFile);
					return false;
				}

				if (!(genotypeScanner.hasNextLine())) {
					ErrorLogger.logError("Extractor","No genotype data rows", errorFile);
					return false;
				}

				int processedBothRowsNumber = 0;
				int markerRefHeaderIndex = markerHeaders.indexOf("marker_ref");
				int markerAltsHeaderIndex = markerHeaders.indexOf("marker_alts");
				int extendedMarkerLinkageGroupNameHeaderIndex = extendedMarkerHeaders.indexOf("linkage_group_name");
				int extendedMarkerLinkageGroupStartHeaderIndex = extendedMarkerHeaders.indexOf("marker_linkage_group_start");
				int markerStrandHeaderIndex = markerHeaders.indexOf("marker_strand");

				while (markerScanner.hasNextLine() && genotypeScanner.hasNextLine()) {
					// Writing the marker (and extended marker if existent) data line(s)
					// in alignment to the new marker headers into the current line.
					// All the other new marker header columns not matched are left blank.
					String[] markerLineParts = markerScanner.nextLine().split("\\t");
					String[] extendedMarkerLineParts = null;
					if (extendedMarkerScanner != null) {
						if (extendedMarkerScanner.hasNextLine()) {
							extendedMarkerLineParts = extendedMarkerScanner.nextLine().split("\\t");
						}
					}
					stringBuilderNewLine = new StringBuilder();
					for (String newMarkerHeader : newMarkerHeadersLine) {
						// Old marker header to include data from marker line
						if (markerHeaders.contains(newMarkerHeader))
						{
							stringBuilderNewLine.append(markerLineParts[markerHeaders.indexOf(newMarkerHeader)]);
						}
						// New marker header to include data from marker line or extended marker line (if existent)
						else {
							switch (newMarkerHeader) {
								case "alleles":
									stringBuilderNewLine.append(markerLineParts[markerRefHeaderIndex]);
									stringBuilderNewLine.append("/");
									stringBuilderNewLine.append(markerLineParts[markerAltsHeaderIndex]);
									break;
								case "chrom":
									if (extendedMarkerLineParts != null) {
										stringBuilderNewLine.append(extendedMarkerLineParts[extendedMarkerLinkageGroupNameHeaderIndex]); }
									break;
								case "pos":
									if (extendedMarkerLineParts != null) {
										stringBuilderNewLine.append(extendedMarkerLineParts[extendedMarkerLinkageGroupStartHeaderIndex]); }
									break;
								case "strand":
									stringBuilderNewLine.append(markerLineParts[markerStrandHeaderIndex]);
									break;
								default:
							}
						}
						stringBuilderNewLine.append("\t");
					}

					// Writing the genotype line into the current line,
					// and then writing the current line into the output file
					stringBuilderNewLine.append(genotypeScanner.nextLine());
					stringBuilderNewLine.append(System.lineSeparator());
					fileWriter.write(stringBuilderNewLine.toString());
					processedBothRowsNumber = processedBothRowsNumber + 1;
				}
				ErrorLogger.logInfo("Extractor", processedBothRowsNumber + " processed rows read");

				fileWriter.close();
				markerScanner.close();
				extendedMarkerScanner.close();
				genotypeScanner.close();
			} catch (IOException e) {
				ErrorLogger.logError("Extractor","Error writing " + outFile + "." + System.lineSeparator() + System.lineSeparator() + "Reason: " + e.getMessage(), errorFile);
				return false;
			}
		}
		else {
			ErrorLogger.logError("Extractor","Sample data file empty", errorFile);
			return false;
		}

		return true;
	}
}
