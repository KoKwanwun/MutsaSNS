package com.likelion.finalproject.service;

import org.springframework.stereotype.Service;

@Service
public class AlgorithmService {
    public Integer sumOfDigit(Integer num) {
        int result = 0;

        while(num > 0){
            result += (num % 10);
            num /= 10;
        }

        return result;
    }
}
