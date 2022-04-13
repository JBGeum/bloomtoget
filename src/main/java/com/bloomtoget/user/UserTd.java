package com.bloomtoget.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class UserTd {
    private long userNo;
    private long groupNo;
    private long tdtNo;
    private Date utStartDate;
    private String userName;
    private String userId;
}
