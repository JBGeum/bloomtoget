package com.bloomtoget.todo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class TodoRecordVO {
    private long tdtNo;
    private long userNo;
    private long groupNo;
    private long tdr_No;
    private Date tdr_recordDate;

}
