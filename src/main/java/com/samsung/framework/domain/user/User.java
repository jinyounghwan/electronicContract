package com.samsung.framework.domain.user;

import com.samsung.framework.domain.common.BaseEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString(callSuper = false)
@Getter
@EqualsAndHashCode(of = {"id", "name"}, callSuper = false)
public class User extends BaseEntity {

    private long seq;
    @NotNull(message = "아이디는 필수 값 입니다.")
    private String id;
    @NotNull(message = "비밀번호는 필수 값 입니다.")
    private String password;
    @NotNull(message = "이름은 필수 값 입니다.")
    private String name;
    private String email;
    private String tel;
    private String picture;
    private String userType;
//    private Set<AuthorityEnum> authority;
    private String deptCode;
    private String appointCode;
    private String positionCode;

    @Builder
    public User(String tableName, String logId1, String logId2, String logType, String logJson, String remark, String regId, String id, String password, String name, String email, String tel, String userType ,String picture,String deptCode, String appointCode, String positionCode) {
        super(tableName, logId1, logId2, logType, logJson, remark, regId);
        this.id = id;
        this.password = password;
        this.name = name;
        this.email = email;
        this.tel = tel;
        this.picture = picture;
        this.userType = userType;
//      this.authority = authority;
        this.deptCode = deptCode;
        this.appointCode = appointCode;
        this.positionCode = positionCode;
    }

    public User update(String name, String picture) {
        this.name = name;
        this.picture = picture;

        return this;
    }

//    public String getAuthorityKey() {
//        return String.valueOf(this.authority);
//    }
}
