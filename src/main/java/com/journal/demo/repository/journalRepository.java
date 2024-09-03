package com.journal.demo.repository;

import com.journal.demo.entity.journalEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface journalRepository extends MongoRepository<journalEntity, ObjectId> {
}
