package com.tacs.backend.repository;

import com.tacs.backend.model.EventOption;

import java.util.List;

public interface EventOptionRepositoryCustom {

    long getLastVotedEventOptionsCount(int timeRange);
    List<EventOption> getLastVotedEventOptions(int timeRange);
}
