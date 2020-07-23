package com.task.task.controler;

import com.task.task.domain.FileRecord;
import com.task.task.domain.FileRecordDto;
import com.task.task.exception.Response;
import com.task.task.mapper.FileMapper;
import com.task.task.service.DbService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import static com.task.task.codes.ResponseCodes.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TextRecordController.class)
class TextRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TextRecordController controller;

    @Mock
    private MultipartFile multipartFile;

    @MockBean
    private FileMapper fileMapper;

    @MockBean
    private DbService dbService;

    @Test
    void uploadFileInWrongFormat() {
        when(multipartFile.getContentType()).thenReturn("jpg");
        final ResponseEntity<Response> expectedResponse = ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body(new Response(WRONG_FORMAT.getValue()));

        final ResponseEntity<Response> actualResponse = controller.uploadFile(multipartFile);

        assertEquals(expectedResponse.toString(), actualResponse.toString());
    }

    @Test
    void uploadFile() {
        when(multipartFile.getContentType()).thenReturn("text/csv");
        when(multipartFile.getOriginalFilename()).thenReturn("test");
        doNothing().when(dbService).save(multipartFile);
        final ResponseEntity<Response> expectedResponse = ResponseEntity
                .status(HttpStatus.OK)
                .body(new Response(String.format(FILE_UPLOADED.getValue(), "test")));

        final ResponseEntity<Response> actualResponse = controller.uploadFile(multipartFile);

        verify(dbService,times(1)).save(multipartFile);
        assertEquals(expectedResponse.toString(), actualResponse.toString());
    }

    @Test
    void getRecord() throws Exception {
        final String primaryKey = "1";
        final FileRecord fileRecord = new FileRecord(primaryKey, "Adam", "architect", Timestamp.valueOf("2021-11-02 01:59:30.274"));
        final FileRecordDto fileRecordDto = new FileRecordDto(primaryKey, "Adam", "architect", Timestamp.valueOf("2021-11-02 01:59:30.274"));

        when(dbService.getRecordByPrimaryKey(primaryKey)).thenReturn(fileRecord);
        when(fileMapper.mapToTextRecordDto(fileRecord)).thenReturn(fileRecordDto);

        mockMvc.perform(get("/getRecord")
                .param("primaryKey", primaryKey)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.primaryKey",is(primaryKey)))
                .andExpect(jsonPath("$.name",is("Adam")))
                .andExpect(jsonPath("$.description",is("architect")))
                .andExpect(jsonPath("$.updatedTimestamp",is("2021-11-02T00:59:30.274+00:00")));
    }

    @Test
    void getRecords() throws Exception {
        final String primaryKey = "1";
        final FileRecord fileRecord = new FileRecord(
                primaryKey,
                "Adam",
                "architect",
                Timestamp.valueOf("2021-11-02 01:59:30.274"));
        final FileRecordDto fileRecordDto = new FileRecordDto(
                primaryKey,
                "Adam",
                "architect",
                Timestamp.valueOf("2021-11-02 01:59:30.274"));
        final List<FileRecord> fileRecords = Collections.singletonList(fileRecord);

        when(dbService.getAllRecords()).thenReturn(fileRecords);
        when(fileMapper.mapToTextRecordDto(fileRecord)).thenReturn(fileRecordDto);

        mockMvc.perform(get("/getRecords")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].primaryKey",is(primaryKey)))
                .andExpect(jsonPath("$[0].name",is("Adam")))
                .andExpect(jsonPath("$[0].description",is("architect")))
                .andExpect(jsonPath("$[0].updatedTimestamp",is("2021-11-02T00:59:30.274+00:00")));
    }

    @Test
    void deleteRecord() throws Exception {
        mockMvc.perform(post("/delete")
                .param("primaryKey", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}