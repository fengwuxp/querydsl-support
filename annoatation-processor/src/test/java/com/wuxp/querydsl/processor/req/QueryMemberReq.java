package com.wuxp.querydsl.processor.req;

import com.wuxp.querydsl.annoations.op.Eq;
import lombok.Data;

@Data
public class QueryMemberReq {

    @Eq
    private Long id;

    private String name;

    private String nickname;

    private String mobilePhone;
}
