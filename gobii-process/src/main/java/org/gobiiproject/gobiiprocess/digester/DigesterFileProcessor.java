package org.gobiiproject.gobiiprocess.digester;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderInstruction;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderProcedure;
import org.gobiiproject.gobiimodel.types.GobiiColumnType;
import org.gobiiproject.gobiimodel.utils.error.Logger;

public class DigesterFileProcessor {

	private DigesterInstructionProcessor processor;

	public DigesterFileProcessor(DigesterInstructionProcessor processor) {
		this.processor = processor;
	}

	public List<ProcessorResult> parseInstructionFile(GobiiLoaderProcedure procedure) {
		if (procedure.getInstructions() == null) {
			Logger.logError("Digester Instruction Processor", "No instructions passed in");
			return new ArrayList<>();
		}



		if (LoaderGlobalConfigs.isSingleThreadFileRead()) {
			return singleThreadProcess(procedure);
		} else {
			return mutliThreadProcess(procedure);
		}
	}

	private List<ProcessorResult> singleThreadProcess(GobiiLoaderProcedure procedure) {

		return process(procedure, () -> procedure.getInstructions().stream());
	}

	private List<ProcessorResult> mutliThreadProcess(GobiiLoaderProcedure procedure) {

		return process(procedure, () -> procedure.getInstructions().parallelStream());
	}

	private List<ProcessorResult> process(GobiiLoaderProcedure procedure, Supplier<Stream<GobiiLoaderInstruction>> instructionStream) {
		List<ProcessorResult> nonMatrixResults = instructionStream.get()
				.filter(i -> ! isMatrixInstruction(i))
				.map(instruction -> processInstruction(procedure, instruction))
				.collect(Collectors.toList());

		List<ProcessorResult> matrixInstructionResults = instructionStream.get()
				.filter(this::isMatrixInstruction)
				.map(instruction -> processInstruction(procedure, instruction))
				.collect(Collectors.toList());

		List<ProcessorResult> results = new LinkedList<>();
		results.addAll(nonMatrixResults);
		results.addAll(matrixInstructionResults);

		return results;
	}

	private ProcessorResult processInstruction(GobiiLoaderProcedure procedure, GobiiLoaderInstruction instruction) {
		try {
			return processor.process(procedure, instruction);
		} catch (Exception e) {
			Logger.logError("ReaderThread", "Error processing file read", e);
			return null;
		} catch(OutOfMemoryError e){
			Logger.logError("ReaderThread","Out of memory processing instruction " + instruction.getTable(),e);
			throw e;//Rethrow, as we can't deal with OOM
		}
	}

	//Gnarly logic - if the first column asked for is a both type, this likely is the matrix file
	private boolean isMatrixInstruction(GobiiLoaderInstruction inst){
		return inst.getGobiiFileColumns().get(0).getGobiiColumnType().equals(GobiiColumnType.CSV_BOTH);
	}
}
