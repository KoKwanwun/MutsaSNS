package com.likelion.finalproject.controller.mission;

import com.likelion.finalproject.service.AlgorithmService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/hello")
@RequiredArgsConstructor
public class KeywordController {

    private final AlgorithmService algorithmService;

    @ApiOperation(value = "문자 출력")
    @GetMapping()
    public String printHello(){
        return "고관운";
    }

    @ApiOperation(value = "자릿수 합 구하기")
    @GetMapping("/{num}")
    public Integer sumOfDigit(@PathVariable Integer num){
        return algorithmService.sumOfDigit(num);
    }
}
