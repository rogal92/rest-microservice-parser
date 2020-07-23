package com.task.task.helper;

import com.task.task.domain.FileRecord;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.task.task.codes.ResponseCodes.*;

@Component
public class CsvHelper {
    public static final Predicate<MultipartFile> isFileCsv =
            file -> !Objects.isNull(file.getContentType()) && file.getContentType().equals("text/csv");

    private static final Logger LOG = LogManager.getLogger();

    public List<FileRecord> convertFileToList(final InputStream inputStream) {
        LOG.info("Starting to parse the file into the list...");
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
             final CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader()
                                                                                .withIgnoreHeaderCase()
                                                                                .withTrim()
                                                                                .withIgnoreEmptyLines());) {
            return csvParser.getRecords().stream()
                                         .map(record -> new FileRecord(record.get("PRIMARY_KEY"),
                                                                     record.get("NAME"),
                                                                     record.get("DESCRIPTION"),
                                                                     Timestamp.valueOf(record.get("UPDATED_TIMESTAMP"))))
                                         .collect(Collectors.toList());
        } catch (IOException io) {
            LOG.debug(PARSING_CSV_WENT_WRONG + io.getMessage());
            throw new RuntimeException(PARSING_CSV_WENT_WRONG + io.getMessage());
        }
    }
}
