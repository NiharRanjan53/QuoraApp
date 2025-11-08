package com.nihar.quoraapp.services;

import com.nihar.quoraapp.dto.LikeRequestDTO;
import com.nihar.quoraapp.dto.LikeResponseDTO;
import reactor.core.publisher.Mono;

public interface ILikeService {
    Mono<LikeResponseDTO> createLike(LikeRequestDTO likeRequestDTO);
    Mono<LikeResponseDTO> countLikesByTargetIdAndTargetType(String targetId, String targetType);
    Mono<LikeResponseDTO> countDisLikesByTargetIdAndTargetType(String targetId, String targetType);
    Mono<LikeResponseDTO> toggleLike(String targetId, String targetType, Boolean isLike);
}
