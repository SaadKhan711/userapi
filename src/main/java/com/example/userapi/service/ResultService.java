package com.example.userapi.service;

import com.example.userapi.dto.ResultInputDto;
import com.example.userapi.entity.YearMark;

public interface ResultService {
    YearMark processResult(ResultInputDto dto);
}