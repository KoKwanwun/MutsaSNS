package com.likelion.finalproject.repository;

import com.likelion.finalproject.domain.entity.Alarm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    Page<Alarm> findAllByUser_UserName(String userName, Pageable pageable);
}
