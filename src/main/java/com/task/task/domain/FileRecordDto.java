package com.task.task.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FileRecordDto {

    private String primaryKey;
    private String name;
    private String description;
    private Timestamp updatedTimestamp;

    @Override
    public String toString() {
        return "PRIMARY_KEY: ".concat(primaryKey)
                .concat("\nNAME: ").concat(name)
                .concat("\nDESCRIPTTON: ").concat(description)
                .concat("\nUPDATTED_TIMESTAMP: ").concat(updatedTimestamp.toString());
    }
}
