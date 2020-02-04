package org.gobiiproject.gobidomain.services.impl.brapi;

import org.gobiiproject.gobidomain.services.impl.brapi.MarkerBrapiServiceImpl;
import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapMarkerBrapi;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.MarkerBrapiDTO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by VCalaminos on 7/18/2019.
 */
@WebAppConfiguration
public class MarkerBrapiServiceImplTest {

    @InjectMocks
    private MarkerBrapiServiceImpl markerBrapiService;

    @Mock
    private DtoMapMarkerBrapi dtoMapMarkerBrapi;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    private MarkerBrapiDTO createMockMarkerDTO() {

        MarkerBrapiDTO markerBrapiDTO = new MarkerBrapiDTO();
        List<Integer> variantSetDbId = new ArrayList<>();
        variantSetDbId.add(1);
        variantSetDbId.add(2);

        markerBrapiDTO.setVariantDbId(35);
        markerBrapiDTO.setVariantName("test-variant");
        markerBrapiDTO.setVariantSetDbId(variantSetDbId);
        markerBrapiDTO.setMapSetName("test-mapset");

        return markerBrapiDTO;
    }

    @Test
    public void getVariants() throws Exception {

        List<MarkerBrapiDTO> variantsMock = new ArrayList<>();

        MarkerBrapiDTO variant1 = createMockMarkerDTO();
        variantsMock.add(variant1);

        when (
                dtoMapMarkerBrapi.getList(
                        any(Integer.TYPE), any(Integer.TYPE) ,any(Integer.TYPE), any(MarkerBrapiDTO.class)
                )
        ).thenReturn(variantsMock);

        List<MarkerBrapiDTO> variantsList = markerBrapiService.getMarkers(
                any(Integer.TYPE), any(Integer.TYPE) ,any(Integer.TYPE), any(MarkerBrapiDTO.class));

        assertEquals(variantsMock.size(), variantsList.size());
        verify(dtoMapMarkerBrapi, times(1)).getList(any(Integer.TYPE),
                any(Integer.TYPE),any(Integer.TYPE), any(MarkerBrapiDTO.class));
    }

    @Test
    public void getVariantById() throws Exception {

        MarkerBrapiDTO variantDTOMock = createMockMarkerDTO();

        when (
                dtoMapMarkerBrapi.get(variantDTOMock.getVariantDbId())
        ).thenReturn(variantDTOMock);

        MarkerBrapiDTO variantResult = markerBrapiService.getMarkerById(variantDTOMock.getVariantDbId());

        assertEquals(variantDTOMock.getVariantDbId(), variantResult.getVariantDbId());
        assertEquals(variantDTOMock.getVariantName(), variantResult.getVariantName());
        verify(dtoMapMarkerBrapi, times(1)).get(variantDTOMock.getVariantDbId());

    }

}
