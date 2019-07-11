package org.gobiiproject.gobiimodel.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gobiiproject.gobiimodel.dto.instructions.loader.GobiiLoaderProcedure;

import java.io.IOException;

public class Marshal {


	public static GobiiLoaderProcedure unmarshalGobiiLoaderProcedure(String str) throws IOException {
		if (str == null) {
			return null;
		} else {
			return new ObjectMapper().readValue(str, GobiiLoaderProcedure.class);
		}
	}
}