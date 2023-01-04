package com.likelion.finalproject.service;

import com.likelion.finalproject.domain.dto.alarm.AlarmDto;
import com.likelion.finalproject.domain.entity.Alarm;
import com.likelion.finalproject.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final CheckException checkException;
    private final AlarmRepository alarmRepository;

    public Page<AlarmDto> alarmList(Pageable pageable, String userName) {
        checkException.checkUser(userName);

        Page<Alarm> alarms = alarmRepository.findAll(pageable);
        Page<AlarmDto> alarmDtos = alarms.map(alarm -> new AlarmDto(alarm.getId(),
                alarm.getAlarmType(), alarm.getFromUserId(), alarm.getTargetId(),
                alarm.getText(), alarm.getCreatedAt(), alarm.getLastModifiedAt()));

        return alarmDtos;
    }
}
