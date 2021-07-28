package com.github.hlam;

import lombok.Data;

import java.util.Date;

@Data
public class ImportantData {
    private String firstName;
    private String lastName;
    private Date birthday;
    private SimpleData simpleData;
}
