package com.tacs.backend.model;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("event_options")
public class EventOption {
    @Id
    private String id;

    @Field("date_time")
    private Date dateTime;

    @Field("vote_quantity")
    private long voteQuantity;

    @Field("update_time")
    @Indexed(direction = IndexDirection.DESCENDING)
    private Date updateDate;

    @Field("event_name")
    private String eventName;

}
