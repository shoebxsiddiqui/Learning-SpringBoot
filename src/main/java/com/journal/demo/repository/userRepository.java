package com.journal.demo.repository;

import com.journal.demo.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface userRepository extends MongoRepository<User, ObjectId> {
    User findByUserName(String username);
}
