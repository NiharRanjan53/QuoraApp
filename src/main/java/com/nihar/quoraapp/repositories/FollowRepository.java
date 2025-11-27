package com.nihar.quoraapp.repositories;

import com.nihar.quoraapp.models.Follow;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface FollowRepository extends ReactiveMongoRepository<Follow, String> {

    Mono<Boolean> existsByFollowerIdAndFolloweeId(String followerId, String followeeId);

    Mono<Void> deleteByFollowerIdAndFolloweeId(String followerId, String followeeId);
}


