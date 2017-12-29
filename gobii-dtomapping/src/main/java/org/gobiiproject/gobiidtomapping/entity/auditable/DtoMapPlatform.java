package org.gobiiproject.gobiidtomapping.entity.auditable;

import org.gobiiproject.gobiidtomapping.core.GobiiDtoMappingException;
import org.gobiiproject.gobiimodel.dto.entity.auditable.PlatformDTO;

import java.util.List;

/**
 * Created by Phil on 4/27/2016.
 */
public interface DtoMapPlatform extends DtoMap<PlatformDTO> {

    PlatformDTO getPlatformDetailsByVendorProtocolId(Integer vendorProtocolId) throws GobiiDtoMappingException;

}
