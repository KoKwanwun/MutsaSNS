package com.likelion.finalproject.service;

import com.likelion.finalproject.domain.dto.alarm.AlarmDto;
import com.likelion.finalproject.domain.entity.Alarm;
import com.likelion.finalproject.exception.ErrorCode;
import com.likelion.finalproject.exception.UserException;
import com.likelion.finalproject.repository.AlarmRepository;
import com.likelion.finalproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final UserRepository userRepository;
    private final AlarmRepository alarmRepository;

    public Page<AlarmDto> alarmList(Pageable pageable, String userName) {
        // 작성자(유저)가 DB에 존재하지 않을 경우
        userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));

        Page<Alarm> alarms = alarmRepository.findAllByUser_UserName(userName, pageable);
        Page<AlarmDto> alarmDtos = alarms.map(alarm -> new AlarmDto(alarm.getId(),
                alarm.getAlarmType(), alarm.getFromUserId(), alarm.getTargetId(),
                alarm.getText(), alarm.getCreatedAt(), alarm.getLastModifiedAt()));

        return alarmDtos;
    }
}
