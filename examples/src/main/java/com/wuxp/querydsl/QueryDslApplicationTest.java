package com.wuxp.querydsl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author wuxp
 */
@SpringBootApplication(scanBasePackages = {"com.wuxp.querydsl"})
public class QueryDslApplicationTest {


    public static void main(String[] args) {
        SpringApplication.run(QueryDslApplicationTest.class, args);
    }
}
