package test.com.wuxp.querydsl;

import com.wuxp.querydsl.annoations.op.Eq;

public class QueryMemberReq {

    @Eq
    private Long id;

    private String name;

    private String nickname;

    private String mobilePhone;
}
