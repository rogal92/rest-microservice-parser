package com.task.task.service;

import com.task.task.domain.FileRecord;
import com.task.task.exception.RecordNotFoundException;
import com.task.task.helper.CsvHelper;
import com.task.task.repository.CsvFileDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class DbServiceTest {

    @Mock
    private MultipartFile multipartFile;
    @MockBean
    private CsvHelper csvHelper;
    @MockBean
    private CsvFileDao csvFileDao;
    @Autowired
    private DbService dbService;

    @Test
    void save() throws IOException {
        final ByteArrayInputStream sampleInput = new ByteArrayInputStream("sample".getBytes(StandardCharsets.UTF_8));
        final FileRecord recordOne = new FileRecord("1", "Steve", "boss", Timestamp.valueOf("2020-11-02 01:59:30.274"));
        final FileRecord recordTwo = new FileRecord("2", "Adrew", "worker", Timestamp.valueOf("2021-11-02 01:59:30.274"));
        when(multipartFile.getInputStream()).thenReturn(sampleInput);
        when(csvHelper.convertFileToList(sampleInput)).thenReturn(Arrays.asList(recordOne, recordTwo));

        dbService.save(multipartFile);

        verify(multipartFile, times(1)).getInputStream();
        verify(csvHelper, times(1)).convertFileToList(sampleInput);
        verify(csvFileDao,times(1)).saveAll(eq(Arrays.asList(recordOne, recordTwo)));
    }

    @Test
    void saveCatchesIOException() throws IOException {
        when(multipartFile.getInputStream()).thenThrow(IOException.class);

        assertThrows(RuntimeException.class, () -> dbService.save(multipartFile));
    }

    @Test
    void getAllRecords() {
        when(csvFileDao.findAll()).thenReturn(Collections.emptyList());

        dbService.getAllRecords();

        verify(csvFileDao, times(1)).findAll();
    }

    @Test
    void getRecordByPrimaryKey() {
        final FileRecord sampleRecord = new FileRecord("1", "John", "Doctor", Timestamp.valueOf("2020-11-02 01:59:30.274"));
        when(csvFileDao.findById("1")).thenReturn(Optional.of(sampleRecord));

        dbService.getRecordByPrimaryKey("1");

        verify(csvFileDao, times(1)).findById("1");
    }

    @Test
    void getRecordByPrimaryKeyThrowsCorrectException() {
        when(csvFileDao.findById("1")).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> dbService.getRecordByPrimaryKey("1"));
    }

    @Test
    void deleteRecordByPrimaryKey() {
        doNothing().when(csvFileDao).deleteById("1");

        dbService.deleteRecordByPrimaryKey("1");

        verify(csvFileDao, times(1)).deleteById("1");
    }
}