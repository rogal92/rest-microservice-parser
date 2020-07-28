package com.task.task.codes;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseCodes {
    WRONG_FORMAT("Only csv files are allowed."),
    PROBLEM_WHILE_SAVING("Something went wrong while saving the file. Please check if it's properly" +
            " formatted or is smaller than 2MB and try again."),
    RECORD_REMOVED("Record with PRIMARY_KEY '%s' has been removed"),
    FILE_UPLOADED("File: '%s' has been uploaded."),
    DO_NOT_EXIST("Record with PRIMARY_KEY '%s' do not exist"),
    RECORD_NOT_FOUND("Record with PRIMARY_KEY '%s' has not been found"),
    FILE_IS_TOO_BIG("File should be smaller than 2 MB"),
    PARSING_CSV_WENT_WRONG("Something went wrong while parsing CSV! "),
    WRONG_RECORDS_FORMAT("Check if your file is properly formatted. First 3 arguments are regular Strings, " +
            "4th should look like this: yyyy-mm-dd hh:mm:ss[.fffffffff]"),
    STORING_RECORDS_PROBLEM("Something went wrong with storing the records: "),
    NO_FILTERED_DATA("You need to filter some data first"),
    NO_PERIOD_PROVIDED("You need to provide valid existing periods in this format: yyyy-mm-dd hh:mm:ss[.fffffffff]");

    private String value;
}
