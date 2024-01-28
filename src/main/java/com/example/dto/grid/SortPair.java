package com.example.dto.grid;

import com.example.config.NameToEnumDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.io.Serializable;

@Data
public class SortPair implements Serializable {
    public static enum Order {
        ASC,
        DESC;
    }
    private static final long serialVersionUID = 8943761459803948959L;
    private String sortField;
    @JsonDeserialize(using = NameToEnumDeserializer.class)
    private Order order;
}
