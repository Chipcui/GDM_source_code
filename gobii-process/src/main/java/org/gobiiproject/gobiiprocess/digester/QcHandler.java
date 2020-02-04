package org.gobiiproject.gobiiprocess.digester;

import org.gobiiproject.gobiiapimodel.payload.Header;
import org.gobiiproject.gobiiapimodel.payload.HeaderStatusMessage;
import org.gobiiproject.gobiiapimodel.payload.PayloadEnvelope;
import org.gobiiproject.gobiiapimodel.restresources.gobii.GobiiUriFactory;
import org.gobiiproject.gobiiclient.core.gobii.GobiiClientContext;
import org.gobiiproject.gobiiclient.core.gobii.GobiiEnvelopeRestResource;
import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.RestResourceId;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.ExtractorInstructionFilesDTO;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiDataSetExtract;
import org.gobiiproject.gobiimodel.dto.instructions.extractor.GobiiExtractorInstruction;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderMetadata;
import org.gobiiproject.gobiimodel.types.GobiiAutoLoginType;
import org.gobiiproject.gobiimodel.types.GobiiExtractFilterType;
import org.gobiiproject.gobiimodel.types.GobiiFileType;
import org.gobiiproject.gobiimodel.types.GobiiProcessType;
import org.gobiiproject.gobiimodel.utils.DateUtils;
import org.gobiiproject.gobiimodel.utils.LineUtils;
import org.gobiiproject.gobiimodel.utils.error.Logger;

public class QcHandler {

	public void executeQc(ConfigSettings configuration, GobiiLoaderMetadata metadata) throws Exception {
		GobiiExtractorInstruction qcExtractInstruction = createQCExtractInstruction(metadata);
		setQCExtractPaths(qcExtractInstruction, metadata);

		sendQCExtract(qcExtractInstruction, configuration, metadata.getGobiiCropType());
	}

	private GobiiExtractorInstruction createQCExtractInstruction(GobiiLoaderMetadata metadata) {
		GobiiExtractorInstruction gobiiExtractorInstruction;
		Logger.logInfo("Digester", "qcCheck detected");
		Logger.logInfo("Digester", "Entering into the QC Subsection #1 of 3...");
		gobiiExtractorInstruction = new GobiiExtractorInstruction();
		gobiiExtractorInstruction.setContactEmail(metadata.getContactEmail());
		gobiiExtractorInstruction.setContactId(metadata.getContactId());
		gobiiExtractorInstruction.setGobiiCropType(metadata.getGobiiCropType());
		gobiiExtractorInstruction.getMapsetIds().add(metadata.getMapset().getId());
		gobiiExtractorInstruction.setQcCheck(true);
		Logger.logInfo("Digester", "Done with the QC Subsection #1 of 3!");
		return gobiiExtractorInstruction;
	}

	private void setQCExtractPaths(GobiiExtractorInstruction qcExtractInstruction, GobiiLoaderMetadata metadata) {
		Logger.logInfo("Digester", "Entering into the QC Subsection #2 of 3...");
		GobiiDataSetExtract gobiiDataSetExtract = new GobiiDataSetExtract();
		gobiiDataSetExtract.setAccolate(false);  // It is unused/unsupported at the moment
		gobiiDataSetExtract.setDataSet(metadata.getDataset());
		gobiiDataSetExtract.setGobiiDatasetType(metadata.getDatasetType());

		// According to Liz, the Gobii extract filter type is always "WHOLE_DATASET" for any QC job
		gobiiDataSetExtract.setGobiiExtractFilterType(GobiiExtractFilterType.WHOLE_DATASET);
		gobiiDataSetExtract.setGobiiFileType(GobiiFileType.HAPMAP);
		// It is going to be set by the Gobii web services
		gobiiDataSetExtract.setGobiiJobStatus(null);
		qcExtractInstruction.getDataSetExtracts().add(gobiiDataSetExtract);
		Logger.logInfo("Digester", "Done with the QC Subsection #2 of 3!");
	}

	private void sendQCExtract(GobiiExtractorInstruction qcExtractInstruction, ConfigSettings configuration, String crop) throws Exception {
		Logger.logInfo("Digester", "Entering into the QC Subsection #3 of 3...");
		ExtractorInstructionFilesDTO extractorInstructionFilesDTOToSend = new ExtractorInstructionFilesDTO();
		extractorInstructionFilesDTOToSend.getGobiiExtractorInstructions().add(qcExtractInstruction);
		extractorInstructionFilesDTOToSend.setInstructionFileName("extractor_" + DateUtils.makeDateIdString());
		GobiiClientContext gobiiClientContext = GobiiClientContext.getInstance(configuration, crop, GobiiAutoLoginType.USER_RUN_AS);
		if (LineUtils.isNullOrEmpty(gobiiClientContext.getUserToken())) {
			Logger.logError("Digester", "Unable to log in with user " + GobiiAutoLoginType.USER_RUN_AS.toString());
			return;
		}
		String currentCropContextRoot = GobiiClientContext.getInstance(null, false).getCurrentCropContextRoot();
		GobiiUriFactory gobiiUriFactory = new GobiiUriFactory(currentCropContextRoot);
		PayloadEnvelope<ExtractorInstructionFilesDTO> payloadEnvelope = new PayloadEnvelope<>(extractorInstructionFilesDTOToSend, GobiiProcessType.CREATE);
		GobiiEnvelopeRestResource<ExtractorInstructionFilesDTO, ExtractorInstructionFilesDTO> gobiiEnvelopeRestResourceForPost = new GobiiEnvelopeRestResource<>(gobiiUriFactory
				.resourceColl(RestResourceId.GOBII_FILE_EXTRACTOR_INSTRUCTIONS));
		PayloadEnvelope<ExtractorInstructionFilesDTO> extractorInstructionFileDTOResponseEnvelope = gobiiEnvelopeRestResourceForPost.post(ExtractorInstructionFilesDTO.class,
				payloadEnvelope);

		if (extractorInstructionFileDTOResponseEnvelope != null) {

			Header header = extractorInstructionFileDTOResponseEnvelope.getHeader();
			if (header.getStatus().isSucceeded()) {
				Logger.logInfo("Digester", "Extractor Request Sent");

			} else {

				String messages = extractorInstructionFileDTOResponseEnvelope.getHeader().getStatus().messages();

				for (HeaderStatusMessage currentStatusMesage : header.getStatus().getStatusMessages()) {
					messages += (currentStatusMesage.getMessage()) + "; ";
				}

				Logger.logError("Digester", "Error sending extract request: " + messages);

			}
		} else {
			Logger.logInfo("Digester", "Error Sending Extractor Request");
		}
		Logger.logInfo("Digester", "Done with the QC Subsection #3 of 3!");
	}

}
