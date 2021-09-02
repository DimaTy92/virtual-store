package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class OrderReportDto {

    private final Map<String, List<String>> active;
    private final Map<String, List<String>> inActive;

}
