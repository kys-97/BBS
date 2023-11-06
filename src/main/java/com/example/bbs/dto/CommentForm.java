package com.example.bbs.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CommentForm {

    @NotEmpty(message = "Write Comment!")
    private String content;
}
