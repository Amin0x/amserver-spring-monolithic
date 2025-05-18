package com.amin.ameenserver.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    Driver findByGovernmentIdNumber(String governmentIdNumber);
    Driver findBydrivingLicenceNumber(String drivingLicenceNumber);
}