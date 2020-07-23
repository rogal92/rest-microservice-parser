package com.task.task.service;

import com.task.task.codes.ResponseCodes;
import com.task.task.domain.FileRecord;
import com.task.task.exception.RecordNotFoundException;
import com.task.task.helper.CsvHelper;
import com.task.task.repository.CsvFileDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class DbService {
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    private CsvFileDao fileRepository;
    @Autowired
    private CsvHelper csvHelper;

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

    public FileRecord getRecordByPrimaryKey(final String primaryKey) {
        LOG.info("Searching for record by PRIMARY_KEY" + primaryKey);
        return fileRepository.findById(primaryKey).orElseThrow(RecordNotFoundException::new);
    }

    public void deleteRecordByPrimaryKey(final String primaryKey) {
        LOG.info("Deleting Record by PRIMARY_KEY " + primaryKey);
        fileRepository.deleteById(primaryKey);
    }
}
