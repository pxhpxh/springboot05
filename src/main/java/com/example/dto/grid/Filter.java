package com.example.dto.grid;

import lombok.Data;

import java.io.Serializable;

@Data
public class Filter implements Serializable {
    private static final long serialVersionUID = -3399893406820509242L;

    private String fieldName;
    private String fieldType;
    private String parValue1;
    private String parValue2;
}
