package com.bloomtoget.group;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GroupVO{
    private long groupNo;
    private String groupName;
    private long groupAdmin;
    private int groupExp;
    private int groupLv;
    private int groupPublic;
    private String groupDesc;
}
