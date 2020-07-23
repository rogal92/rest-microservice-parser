package com.task.task.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity(name = "RECORDS")
public class FileRecord {

    @Id
    @NotBlank
    @NotNull
    @Column(name = "PRIMARY_KEY")
    private String primaryKey;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "UPDATED_TIMESTAMP")
    private Timestamp updatedTimestamp;

    @Override
    public String toString() {
        return "Primary key: ".concat(primaryKey)
                .concat(" Name: ").concat(name)
                .concat(" Description: ").concat(description)
                .concat(" Updated Timestamp: ").concat(updatedTimestamp.toString());
    }
}
