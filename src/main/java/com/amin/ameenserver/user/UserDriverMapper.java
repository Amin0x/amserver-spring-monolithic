package com.amin.ameenserver.user;

public class UserDriverMapper {

    public static Driver userDriverDtoToUserDriver(UserDriverDto userDriverDto){
        Driver driver = new Driver();
        driver.setUser(null);
        driver.setCarId(userDriverDto.getCarId());
        driver.setDrivingLicenceImage(userDriverDto.getDrivingLicImage());
        driver.setDrivingLicenceExpireDate(userDriverDto.getDrivingLicExpireDate());
        driver.setDrivingLicenceIssueDate(userDriverDto.getDrivingLicIssueDate());
        driver.setGovernmentIdImage(userDriverDto.getGovernmentIdImage());
        driver.setGovernmentIdIssueDate(userDriverDto.getGovernmentIdIssueDate());
        driver.setGovernmentIdNumber(userDriverDto.getGovernmentIdNumber());
        driver.setGovernmentIdExpireDate(userDriverDto.getGovernmentIdExpireDate());
        driver.setDrivingLicenceNumber(userDriverDto.getDrivingLicNumber());
        driver.setLikes(0);
        driver.setPoints(0);
        driver.setAc(false);
        driver.setCharger(false);
        driver.setWater(false);
        driver.setRidesDone(0);
        driver.setRideAcceptedCount(0);
        driver.setRideCount(0);
        driver.setWifi(false);

        return driver;
    }
}
