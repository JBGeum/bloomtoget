package com.bloomtoget.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UserVO {
    private long userNo;
    private String userId;
    private String userPwd;
    private String userName;
    private String email;
    private int userPublic;
    private long groupNo;
    private MultipartFile imgFile;
    private String profImg;
    private String profImgThumb;

    @Override
    public String toString() {
        return "UserVO{" +
                "userNo=" + userNo +
                ", userId='" + userId + '\'' +
                ", userPwd='" + userPwd + '\'' +
                ", email='" + email + '\'' +
                ", userPublic=" + userPublic +
                ", groupNo=" + groupNo +
                '}';
    }
}

