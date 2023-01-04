package com.likelion.finalproject.controller;

import com.likelion.finalproject.configuration.annotation.Lock;
import com.likelion.finalproject.domain.dto.Response;
import com.likelion.finalproject.domain.dto.alarm.AlarmDto;
import com.likelion.finalproject.service.AlarmService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/v1/alarms")
@RequiredArgsConstructor
public class AlarmRestController {

    private final AlarmService alarmService;

    @Lock
    @ApiOperation(value = "알람 리스트 조회")
    @GetMapping()
    public Response<Page<AlarmDto>> alarmList(@ApiIgnore @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, @ApiIgnore Authentication authentication){
        Page<AlarmDto> alarmList = alarmService.alarmList(pageable, authentication.getName());
        return Response.success(alarmList);
    }
}
