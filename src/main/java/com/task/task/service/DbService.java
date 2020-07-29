package com.task.task.service;

import com.task.task.codes.ResponseCodes;
import com.task.task.domain.FileRecord;
import com.task.task.exception.NoFilteredDataException;
import com.task.task.exception.RecordNotFoundException;
import com.task.task.helper.CsvHelper;
import com.task.task.repository.CsvFileDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class DbService {
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    private CsvFileDao fileRepository;
    @Autowired
    private CsvHelper csvHelper;
    private List<FileRecord> fileRecordsByPeriod;

    public void save(final MultipartFile multipartFile) {
        try {
            final List<FileRecord> records = csvHelper.convertFileToList(multipartFile.getInputStream());
            LOG.info("Parsing has finished successfully");
            fileRepository.saveAll(records);
            LOG.info("Records have been saved");
        } catch (IOException io) {
            LOG.debug(ResponseCodes.STORING_RECORDS_PROBLEM.getValue());
            throw new RuntimeException(ResponseCodes.STORING_RECORDS_PROBLEM.getValue() + io.getMessage());
        }
    }

    public List<FileRecord> getAllRecords() {
        LOG.info("Searching for all the Records");
        return fileRepository.findAll();
    }

    public void filterRecordsByPeriod(final Timestamp start, final Timestamp end) {
        LOG.info("Searching for records within provided timestamps: start {} end: {}", start, end);
        fileRecordsByPeriod = fileRepository.findBySpecifiedTimestamps(start, end);
        LOG.info("Filtered records have been saved");
    }

    public Page<FileRecord> findPage(final Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<FileRecord> pageRecords;

        if (Objects.isNull(this.fileRecordsByPeriod)) throw new NoFilteredDataException();
        if (this.fileRecordsByPeriod.size() < startItem) {
            pageRecords = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, fileRecordsByPeriod.size());
            pageRecords = fileRecordsByPeriod.subList(startItem, toIndex);
        }

        return new PageImpl<>(pageRecords, PageRequest.of(currentPage, pageSize), fileRecordsByPeriod.size());
    }

    public FileRecord getRecordByPrimaryKey(final String primaryKey) {
        LOG.info("Searching for record by PRIMARY_KEY" + primaryKey);
        return fileRepository.findById(primaryKey).orElseThrow(RecordNotFoundException::new);
    }

    public void deleteRecordByPrimaryKey(final String primaryKey) {
        LOG.info("Deleting Record by PRIMARY_KEY " + primaryKey);
        fileRepository.deleteById(primaryKey);
    }
}
