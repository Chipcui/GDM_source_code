package org.gobiiproject.gobidomain.services.impl.brapi;

import org.gobiiproject.gobidomain.services.impl.DnaRunServiceImpl;
import org.gobiiproject.gobiidtomapping.entity.noaudit.DtoMapDnaRun;
import org.gobiiproject.gobiimodel.dto.entity.noaudit.DnaRunDTO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.any;

/**
 * Created by VCalaminos on 7/18/2019.
 */
@WebAppConfiguration
public class DnaRunServiceImplTest {

    @InjectMocks
    private DnaRunServiceImpl dnaRunService;

    @Mock
    private DtoMapDnaRun dtoMapDnaRun;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    private DnaRunDTO createMockDnaRunDTO() {

        DnaRunDTO callSetDTO = new DnaRunDTO();

        callSetDTO.setCallSetDbId(34);
        callSetDTO.setCallSetName("test-callset");
        callSetDTO.setDnaRunCode("test-code");
        callSetDTO.setGermplasmDbId(1);

        return callSetDTO;
    }

    @Test
    public void getCallsets() throws Exception {

        List<DnaRunDTO> callsetsMock = new ArrayList<>();

        DnaRunDTO callset1 = createMockDnaRunDTO();
        callsetsMock.add(callset1);

        when (
                dtoMapDnaRun.getList(
                        any(Integer.TYPE), any(Integer.TYPE), any(DnaRunDTO.class)
                )
        ).thenReturn(callsetsMock);

        List<DnaRunDTO> callsetsList = dnaRunService.getDnaRuns(any(Integer.TYPE), any(Integer.TYPE), any(DnaRunDTO.class));

        assertEquals(callsetsMock.size(), callsetsList.size());
        verify(dtoMapDnaRun, times(1)).getList(any(Integer.TYPE), any(Integer.TYPE), any(DnaRunDTO.class));
    }

    @Test
    public void getCallsetById() throws Exception {

        DnaRunDTO callSetDTOMock = createMockDnaRunDTO();

        when (
                dtoMapDnaRun.get(callSetDTOMock.getCallSetDbId())
        ).thenReturn(callSetDTOMock);

        DnaRunDTO callSetResult = dnaRunService.getDnaRunById(callSetDTOMock.getCallSetDbId());

        assertEquals(callSetDTOMock.getCallSetDbId(), callSetResult.getCallSetDbId());
        assertEquals(callSetDTOMock.getCallSetName(), callSetResult.getCallSetName());
        verify(dtoMapDnaRun, times(1)).get(callSetDTOMock.getCallSetDbId());
    }

}
