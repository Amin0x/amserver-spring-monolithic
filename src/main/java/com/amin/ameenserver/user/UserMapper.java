package com.amin.ameenserver.user;

public class UserMapper {
    public static UserDto userToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setPhone(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setStatus(user.getStatus());
        userDto.setEnabled(user.isEnabled());
        userDto.setRememberToken(user.getRememberToken());
        userDto.setVerified(user.isVerified());
        userDto.setRegistrationDate(user.getRegistrationDate());
        userDto.setProfileImage(user.getProfileImage());
        userDto.setDriverMode(user.getDriverMode());
        userDto.setRoles(userDto.getRoles());
        userDto.setUserCars(userDto.getUserCars());
        userDto.setUserDriver(userDriverToDto(user.getDriver()));
        userDto.setUserRider(userRiderToDto(user.getRider()));
        return userDto;
    }

    private static UserDriverDto userDriverToDto(Driver driver) {
        return null;
    }

    public static UserRiderDto userRiderToDto(Rider rider){
        return null;
    }
}
