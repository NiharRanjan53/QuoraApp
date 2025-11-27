package com.nihar.quoraapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FollowResponseDTO {
    private String followerId;
    private String followeeId;
    private LocalDateTime followedAt;
}


