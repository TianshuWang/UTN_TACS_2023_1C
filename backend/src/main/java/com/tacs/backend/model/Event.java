package com.tacs.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("events")
public class Event {
    @Id
    private String id;

    private String name;

    private String description;

    @Field("owner_user")
    private User ownerUser;

    private Status status;

    @Field("event_options")
    @DBRef
    private Set<EventOption> eventOptions;

    @Field("registered_users")
    @DBRef
    private Set<User> registeredUsers;

    @Field("create_date")
    @Indexed(direction = IndexDirection.DESCENDING)
    private Date createDate;


    public enum Status {
        /**
         * vote pending
         */
        VOTE_PENDING,
        /**
         * vote closed
         */
        VOTE_CLOSED,
        /**
         * pending
         */
        PENDING,
        /**
         * started
         */
        STARTED,
        /**
         * finished
         */
        FINISHED
    }
}
