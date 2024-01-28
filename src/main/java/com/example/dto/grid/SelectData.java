package com.example.dto.grid;

import lombok.Data;

import java.io.Serializable;

@Data
public class SelectData implements Serializable {
    private static final long serialVersionUID = -4012479247471761482L;
    private String id;
    private String text;
}
