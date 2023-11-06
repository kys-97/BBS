package com.example.bbs.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BoardForm {

    @NotEmpty(message="Write Title!")
    @Size(max=200)
    private String title;

    @NotEmpty(message="Write Content!")
    private String content;

}
