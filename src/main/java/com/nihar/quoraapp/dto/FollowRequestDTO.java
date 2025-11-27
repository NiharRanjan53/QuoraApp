package com.nihar.quoraapp.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FollowRequestDTO {

    @NotBlank(message = "Follower ID is required")
    private String followerId;

    @NotBlank(message = "Followee ID is required")
    private String followeeId;
}


