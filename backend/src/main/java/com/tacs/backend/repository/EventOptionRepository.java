package com.tacs.backend.repository;

import com.tacs.backend.model.EventOption;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface EventOptionRepository extends MongoRepository<EventOption, String> ,EventOptionRepositoryCustom{
}
