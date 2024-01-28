package com.example.dto.org;

import com.example.common.AbstractRequest;
import lombok.Data;

@Data
public class EditPasswordRequest extends AbstractRequest {
    private Long id;
    private String oldPassword;
    private String newPassword;
}
