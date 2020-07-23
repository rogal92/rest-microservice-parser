package com.task.task.helper;

import com.task.task.domain.FileRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class CsvHelperTest {

    @Autowired
    private CsvHelper csvHelper;

    @ParameterizedTest
    @MethodSource("provideInput")
    void convertFileToList(final String input) {
        final InputStream fakeStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        final FileRecord recordOne = new FileRecord("1", "Steve", "boss", Timestamp.valueOf("2020-11-02 01:59:30.274"));
        final FileRecord recordTwo = new FileRecord("2", "Adrew", "worker", Timestamp.valueOf("2021-11-02 01:59:30.274"));
        final List<FileRecord> expectedRecords = Arrays.asList(recordOne, recordTwo);

        final List<FileRecord> fileRecords = csvHelper.convertFileToList(fakeStream);

        assertEquals(expectedRecords.toString(), fileRecords.toString());
    }

    @Test
    void convertFileToListWrongTimestampFormat() {
        final InputStream fakeStream = new ByteArrayInputStream(getFileWithWrongTimestamp().getBytes(StandardCharsets.UTF_8));

        final IllegalArgumentException exc = assertThrows(IllegalArgumentException.class, () -> csvHelper.convertFileToList(fakeStream));
        assertEquals("Timestamp format must be yyyy-mm-dd hh:mm:ss[.fffffffff]", exc.getMessage());
    }

    private static Stream<Arguments> provideInput() {
        return Stream.of(
                Arguments.of(getFileForTesting()),
                Arguments.of(getFileWithEmptyLinesForTesting()),
                Arguments.arguments(getFileWithEmptyLinesAndAdditionalValueForTesting())
        );
    }

    private static String getFileForTesting() {
        return "PRIMARY_KEY,NAME,DESCRIPTION,UPDATED_TIMESTAMP\n" +
                "1,Steve,boss,2020-11-02 01:59:30.274\n" +
                "2,Adrew,worker,2021-11-02 01:59:30.274";
    }

    private static String getFileWithEmptyLinesForTesting() {
        return "PRIMARY_KEY,NAME,DESCRIPTION,UPDATED_TIMESTAMP\n" +
                "\n\n" +
                "1,Steve,boss,2020-11-02 01:59:30.274\n" +
                "2,Adrew,worker,2021-11-02 01:59:30.274";
    }

    private static String getFileWithEmptyLinesAndAdditionalValueForTesting() {
        return "PRIMARY_KEY,NAME,DESCRIPTION,UPDATED_TIMESTAMP\n" +
                "\n\n" +
                "1,Steve,boss,2020-11-02 01:59:30.274, this should not be added\n" +
                "2,Adrew,worker,2021-11-02 01:59:30.274";
    }

    private String getFileWithWrongTimestamp() {
        return "PRIMARY_KEY,NAME,DESCRIPTION,UPDATED_TIMESTAMP\n" +
                "1,Steve,boss,2020";
    }
}
