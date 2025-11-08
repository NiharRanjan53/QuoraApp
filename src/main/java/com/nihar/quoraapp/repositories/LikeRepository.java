package com.nihar.quoraapp.repositories;

import com.nihar.quoraapp.models.Like;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends ReactiveMongoRepository<Like, String > {
}
