package com.amin.ameenserver.car;

import com.amin.ameenserver.user.UserCar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<UserCar, Long> {
}