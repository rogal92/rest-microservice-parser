package com.task.task.repository;

import com.task.task.domain.FileRecord;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Transactional
@Repository
public interface CsvFileDao extends CrudRepository<FileRecord, String> {

    @Override
    List<FileRecord> findAll();

    @Query(value = "SELECT * FROM #{#entityName} WHERE UPDATED_TIMESTAMP BETWEEN :start AND :end", nativeQuery = true)
    List<FileRecord> findBySpecifiedTimestamps(@Param("start") final Timestamp start,
                                               @Param("end") final Timestamp end);
}
