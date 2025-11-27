package com.nihar.quoraapp.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "follows")
@CompoundIndexes({
        @CompoundIndex(name = "follower_followee_unique", def = "{'followerId': 1, 'followeeId': 1}", unique = true)
})
public class Follow {
    @Id
    private String id;

    @Indexed
    private String followerId;

    @Indexed
    private String followeeId;

    @CreatedDate
    private LocalDateTime createdAt;
}


