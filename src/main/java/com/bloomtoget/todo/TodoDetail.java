package com.bloomtoget.todo;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class TodoDetail {
    private long tdt_No;
    private long tdt_writer;
    private int tdt_active;
    private int tdt_public;
    private String tdt_goal;
    private long tdt_groupNo;
    private Date tdt_startdate;
    private String writerName;
    private int currentMember;
    private int memberCount;
    private String tdr_No;
    private int today;
    private int total;


}
