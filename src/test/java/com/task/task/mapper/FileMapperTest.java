package com.task.task.mapper;

import com.task.task.domain.FileRecord;
import com.task.task.domain.FileRecordDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class FileMapperTest {

    @Autowired
    private FileMapper fileMapper;

    @Test
    void mapToTextFileDto() {
        final FileRecord fileRecord = new FileRecord("1", "Mark", "Layer", Timestamp.valueOf("2021-11-02 01:59:30.274"));
        final FileRecordDto expectedDto = new FileRecordDto("1", "Mark", "Layer", Timestamp.valueOf("2021-11-02 01:59:30.274"));

        final FileRecordDto fileRecordDto = fileMapper.mapToTextRecordDto(fileRecord);

        assertEquals(expectedDto.toString(), fileRecordDto.toString());
    }
}