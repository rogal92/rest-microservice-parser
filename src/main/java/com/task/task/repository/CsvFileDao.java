package com.task.task.repository;

import com.task.task.domain.FileRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface CsvFileDao extends CrudRepository<FileRecord, String> {

    @Override
    List<FileRecord> findAll();
}
