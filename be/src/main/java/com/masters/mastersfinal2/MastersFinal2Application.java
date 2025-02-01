package com.masters.mastersfinal2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class MastersFinal2Application {

    public static void main(String[] args) {
        SpringApplication.run(MastersFinal2Application.class, args);
    }

}
