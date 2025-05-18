package com.amin.ameenserver.location;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Data Transfer Object for updating user location.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserLocationDto implements Serializable {

    /**
     * Latitude of the user's location.
     * Cannot be null.
     */
    @NotNull(message = "Latitude cannot be null")
    private double latitude;

    /**
     * Longitude of the user's location.
     * Cannot be null.
     */
    @NotNull(message = "Longitude cannot be null")
    private double longitude;

    /**
     * Geohash of the user's location.
     * Must be between 1 and 12 characters.
     */
    @Size(min = 1, max = 12, message = "GeoHash must be between 1 and 12 characters")
    private String geoHash;

    /**
     * ID of the user.
     * Must be a positive number.
     */
    @Positive(message = "User ID must be positive")
    private long userId;

    /**
     * Converts the DTO to a string representation.
     * 
     * @return String representation of the DTO.
     */
    @Override
    public String toString() {
        return "UpdateUserLocationDto{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", geoHash='" + geoHash + '\'' +
                ", userId=" + userId +
                '}';
    }
}
