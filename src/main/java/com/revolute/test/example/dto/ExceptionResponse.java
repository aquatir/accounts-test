package com.revolute.test.example.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "ofText")
@Getter
public class ExceptionResponse {
    private final String text;
}
