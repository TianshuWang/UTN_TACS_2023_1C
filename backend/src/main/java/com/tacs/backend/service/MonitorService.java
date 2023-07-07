package com.tacs.backend.service;

import com.tacs.backend.dto.EventOptionReportDto;
import com.tacs.backend.dto.MarketingReportDto;
import com.tacs.backend.model.EventOption;
import com.tacs.backend.repository.EventOptionRepository;
import com.tacs.backend.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MonitorService {
    @Value("${monitor.time-range}")
    private int timeRange;
    private final EventRepository eventRepository;
    private final EventOptionRepository eventOptionRepository;


    public MarketingReportDto getMarketingReport() {
        MarketingReportDto report = new MarketingReportDto();
        report.setEventsCount(eventRepository.getLastCreatedEventsCount(timeRange));
        report.setOptionsCount(eventOptionRepository.getLastVotedEventOptionsCount(timeRange));

        return report;
    }

    public List<EventOptionReportDto> getLastVotedEventOptions() {
        List<EventOption> eventOptions = eventOptionRepository.getLastVotedEventOptions(timeRange);

        return eventOptions.stream()
                .map(this::convertToReportDto)
                .toList();
    }

    private EventOptionReportDto convertToReportDto(EventOption eventOption) {
        return EventOptionReportDto.builder()
                .dateTime(eventOption.getDateTime())
                .voteQuantity(eventOption.getVoteQuantity())
                .eventName(eventOption.getEventName())
                .build();
    }
}
