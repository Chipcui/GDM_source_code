package org.gobiiproject.gobiiprocess.digester.utils.validation;

import org.gobiiproject.gobiidtomapping.entity.noaudit.impl.DtoMapNameIds.DtoMapNameIdFetchReferences;
import org.gobiiproject.gobiidtomapping.entity.noaudit.impl.DtoMapNameIds.DtoMapNameIdParams;
import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.dto.entity.children.NameIdDTO;
import org.gobiiproject.gobiimodel.types.GobiiEntityNameType;
import org.gobiiproject.gobiimodel.types.GobiiFilterType;
import org.gobiiproject.gobiimodel.utils.error.ErrorLogger;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Companion class to take metedata, specifically elements which have defined types, and to validate that their types conform to the types
 * in the database.
 */
public class MetadataValidator {


	/**
	 * Returns the list of msising elements from a list of elements, or an empty list if all elements were named.
	 * @param names
	 * @param CVTable
	 * @return
	 */
	public List<String> missingElements(List<String> names, String CVTable){
		DtoMapNameIdFetchReferences references = null;//TODO: How to I get one of these?
		//TODO: How do I use the CV table name?
		CvGroup group = CvGroup.valueOf(CVTable);
		return getCVListFromReference(names,references,GobiiEntityNameType.CV);
	}

	public List<String> getCVListFromReference(List<String> names, DtoMapNameIdFetchReferences references, GobiiEntityNameType type){
		List<NameIdDTO> nameDTOs=names.stream().map(MetadataValidator::NameIdDTOFromName).collect(Collectors.toList());
		DtoMapNameIdParams params = new DtoMapNameIdParams(type, GobiiFilterType.NAMES_BY_NAME_LIST,null,nameDTOs);
		List<NameIdDTO> invalidDTOs=new LinkedList<NameIdDTO>();
		try {
			invalidDTOs=references.getNameIds(params);
		}catch(Exception e){
			ErrorLogger.logError("Metadata Validator",e);
		}
		return invalidDTOs.stream().map(NameIdDTO::getName).collect(Collectors.toList());

	}

	private static NameIdDTO NameIdDTOFromName(String name){
		NameIdDTO ret = new NameIdDTO();
		ret.setName(name);
		return ret;
	}
}
