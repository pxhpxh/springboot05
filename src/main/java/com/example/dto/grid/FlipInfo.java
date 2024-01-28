package com.example.dto.grid;

import lombok.Data;

import java.io.Serializable;

@Data
public class FlipInfo implements Serializable {
    private static final long serialVersionUID = -5397821499912596670L;
    private int page = 1;
    private int size = 20;
    private int total = 0;

    public FlipInfo(){

    }
    public FlipInfo(int page,int size){
        this.page=page;
        this.size=size;
    }
}
