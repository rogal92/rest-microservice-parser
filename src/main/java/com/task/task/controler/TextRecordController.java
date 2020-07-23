package com.task.task.controler;

import com.task.task.domain.FileRecord;
import com.task.task.domain.FileRecordDto;
import com.task.task.exception.Response;
import com.task.task.helper.CsvHelper;
import com.task.task.mapper.FileMapper;
import com.task.task.service.DbService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.task.task.codes.ResponseCodes.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/")
public class TextRecordController {
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private DbService dbService;

    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<Response> uploadFile(@RequestParam("file") final MultipartFile file) {
        if (CsvHelper.isFileCsv.test(file)) {
            LOG.info("Saving the file: {}", file.getOriginalFilename());
            dbService.save(file);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new Response(String.format(FILE_UPLOADED.getValue(), file.getOriginalFilename())));
        }
        LOG.info(WRONG_FORMAT.getValue());
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body(new Response(WRONG_FORMAT.getValue()));
    }

    @GetMapping("/getRecord")
    public ResponseEntity<FileRecordDto> getRecord(@RequestParam("primaryKey") final String primaryKey) {
        final FileRecordDto recordDto = fileMapper.mapToTextRecordDto(dbService.getRecordByPrimaryKey(primaryKey));
        return ResponseEntity.ok().body(recordDto);
    }

    @GetMapping(value = "/getRecords")
    public ResponseEntity<List<FileRecordDto>> getRecords() {
        final List<FileRecord> records = dbService.getAllRecords();

        if (Objects.isNull(records))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(records.stream()
                .map(fileRecord -> fileMapper.mapToTextRecordDto(fileRecord))
                .collect(Collectors.toList()));
    }

    @Secured("OWNER")
    @PostMapping("/delete")
    public ResponseEntity<Response> deleteRecord(@RequestParam("primaryKey") final String primaryKey) {
        dbService.deleteRecordByPrimaryKey(primaryKey);
        return ResponseEntity
                .ok(new Response(String.format(RECORD_REMOVED.getValue(), primaryKey)));
    }
}
