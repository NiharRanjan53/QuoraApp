package com.nihar.quoraapp.services;

import com.nihar.quoraapp.dto.FollowRequestDTO;
import com.nihar.quoraapp.dto.FollowResponseDTO;
import reactor.core.publisher.Mono;

public interface IFollowService {

    Mono<FollowResponseDTO> follow(FollowRequestDTO requestDTO);

    Mono<Void> unfollow(FollowRequestDTO requestDTO);
}
