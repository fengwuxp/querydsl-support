package com.wuxp.querydsl.processor.entities;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "t_member")
@Data
@Accessors(chain = true)
public class Member implements Serializable {


    private static final long serialVersionUID = 6329410533798293192L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    private String name;

    private String nickname;

    private String mobilePhone;

    @Temporal(TemporalType.TIMESTAMP)
    private Date birthday;
}
