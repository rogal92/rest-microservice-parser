package com.task.task.mapper;

import com.task.task.domain.FileRecord;
import com.task.task.domain.FileRecordDto;
import org.springframework.stereotype.Component;

@Component
public class FileMapper {

    public FileRecordDto mapToTextRecordDto(final FileRecord fileRecord) {
        return new FileRecordDto(
                fileRecord.getPrimaryKey(),
                fileRecord.getName(),
                fileRecord.getDescription(),
                fileRecord.getUpdatedTimestamp());
    }
}
