package com.amin.ameenserver.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface ValidationCodeRepository extends JpaRepository<ValidationCode, Long> {
    @Query("select v from ValidationCode v where v.code = ?1 and v.phone = ?2 and v.date > ?3 order by v.date DESC")
    List<ValidationCode> findByPhoneAndCodeOrderByTime(String code, String phone, Date date);
}