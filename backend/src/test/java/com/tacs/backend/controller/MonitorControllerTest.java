package com.tacs.backend.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tacs.backend.dto.*;
import com.tacs.backend.service.MonitorService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;


@ExtendWith(MockitoExtension.class)
public class MonitorControllerTest {

    private MockMvc mvc;
    @Mock
    private MonitorService monitorService;
    @InjectMocks
    private MonitorController monitorController;

    private List<EventOptionReportDto> eventOptionReportDtoList;

    @BeforeEach
    void setup() {
        EventOptionDto eventOptionDto = EventOptionDto.builder()
                .dateTime(new Date())
                .voteQuantity(0)
                .build();
        Set<EventOptionDto> eventOptionDtoSet = new HashSet<>();
        eventOptionDtoSet.add(eventOptionDto);
        eventOptionReportDtoList = new ArrayList<EventOptionReportDto>();

        mvc = MockMvcBuilders.standaloneSetup(monitorController)
                .setControllerAdvice(new ExceptionHandlerController())
                .build();
    }

    @Test
    @DisplayName("Should return 200 when get the marketing report")
    void itShouldReturnMarketingReportWith200StatusCodeWhenCalledGetCounterReport() throws Exception {
        MarketingReportDto marketingReportDto = new MarketingReportDto();
        marketingReportDto.setEventsCount(10);
        marketingReportDto.setOptionsCount(5);

        given(monitorService.getMarketingReport()).willReturn(marketingReportDto);
        HttpServletRequest request = mock(HttpServletRequest.class);

        MockHttpServletResponse response = mvc.perform(get("/v1/monitor/ratios")
                        //.header("Authorization", "Bearer saraza123")
                        .requestAttr("javax.servlet.http.HttpServletRequest", request)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(asJsonString(marketingReportDto));
    }

    @Test
    @DisplayName("Should return 200 when get last voted options")
    void itShouldReturnListOfLAstVotedOptionsWith200StatusCodeWhenCalledGetLastVotedEventOptions() throws Exception {
        EventOptionReportDto eventOptionReportDto = new EventOptionReportDto();
        eventOptionReportDto.setVoteQuantity(5);

        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, 02, 25, 15, 30);
        eventOptionReportDto.setDateTime(calendar.getTime());

        eventOptionReportDtoList.add(eventOptionReportDto);

        willReturn(eventOptionReportDtoList).
            given(monitorService).getLastVotedEventOptions();

        HttpServletRequest request = mock(HttpServletRequest.class);

        MockHttpServletResponse response = mvc.perform(get("/v1/monitor/options")
                        //.header("Authorization", "Bearer saraza123")
                        .requestAttr("javax.servlet.http.HttpServletRequest", request)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
