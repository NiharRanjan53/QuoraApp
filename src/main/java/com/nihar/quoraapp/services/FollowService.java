package com.nihar.quoraapp.services;

import com.nihar.quoraapp.dto.FollowRequestDTO;
import com.nihar.quoraapp.dto.FollowResponseDTO;
import com.nihar.quoraapp.models.Follow;
import com.nihar.quoraapp.models.User;
import com.nihar.quoraapp.repositories.FollowRepository;
import com.nihar.quoraapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FollowService implements IFollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Override
    public Mono<FollowResponseDTO> follow(FollowRequestDTO requestDTO) {
        if (requestDTO.getFollowerId().equals(requestDTO.getFolloweeId())) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot follow yourself"));
        }

        // Verify both users exist
        Mono<User> followerMono = userRepository.findById(requestDTO.getFollowerId())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Follower not found")));

        Mono<User> followeeMono = userRepository.findById(requestDTO.getFolloweeId())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Followee not found")));

        return Mono.zip(followerMono, followeeMono)
                .flatMap(tuple ->
                        followRepository.existsByFollowerIdAndFolloweeId(requestDTO.getFollowerId(), requestDTO.getFolloweeId())
                                .flatMap(exists -> {
                                    if (exists) {
                                        return Mono.error(new ResponseStatusException(HttpStatus.CONFLICT, "Already following"));
                                    }

                                    Follow follow = Follow.builder()
                                            .followerId(requestDTO.getFollowerId())
                                            .followeeId(requestDTO.getFolloweeId())
                                            .createdAt(LocalDateTime.now())
                                            .build();

                                    return followRepository.save(follow)
                                            .flatMap(saved -> updateCountersOnFollow(tuple.getT1(), tuple.getT2()))
                                            .thenReturn(FollowResponseDTO.builder()
                                                    .followerId(requestDTO.getFollowerId())
                                                    .followeeId(requestDTO.getFolloweeId())
                                                    .followedAt(LocalDateTime.now())
                                                    .build());
                                })
                );
    }

    @Override
    public Mono<Void> unfollow(FollowRequestDTO requestDTO) {
        if (requestDTO.getFollowerId().equals(requestDTO.getFolloweeId())) {
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot unfollow yourself"));
        }

        Mono<User> followerMono = userRepository.findById(requestDTO.getFollowerId())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Follower not found")));

        Mono<User> followeeMono = userRepository.findById(requestDTO.getFolloweeId())
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Followee not found")));

        return Mono.zip(followerMono, followeeMono)
                .flatMap(tuple ->
                        followRepository.existsByFollowerIdAndFolloweeId(requestDTO.getFollowerId(), requestDTO.getFolloweeId())
                                .flatMap(exists -> {
                                    if (!exists) {
                                        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Follow relationship not found"));
                                    }

                                    return followRepository.deleteByFollowerIdAndFolloweeId(requestDTO.getFollowerId(), requestDTO.getFolloweeId())
                                            .then(updateCountersOnUnfollow(tuple.getT1(), tuple.getT2()));
                                })
                );
    }

    private Mono<Void> updateCountersOnFollow(User follower, User followee) {
        follower.setFollowingCount(follower.getFollowingCount() + 1);
        followee.setFollowersCount(followee.getFollowersCount() + 1);

        return Mono.when(
                userRepository.save(follower),
                userRepository.save(followee)
        ).then();
    }

    private Mono<Void> updateCountersOnUnfollow(User follower, User followee) {
        follower.setFollowingCount(Math.max(0, follower.getFollowingCount() - 1));
        followee.setFollowersCount(Math.max(0, followee.getFollowersCount() - 1));

        return Mono.when(
                userRepository.save(follower),
                userRepository.save(followee)
        ).then();
    }
}


