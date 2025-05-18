package com.amin.ameenserver.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);
    //@Query("select (count(u) > 0) from User u where u.username = ?1")
    Boolean existsByUsername(String username);
    Optional<User> findByEmail(String email);
    @Query("select u from User u where u.email = ?1 and u.enabled = true")
    Optional<User> findByEmailAndEnabledIsTrue(String email);
    //User findById(long userId);
    Optional<User> findByIdAndEnabledIsTrue(long userId);
    User findByIdAndStatus(String name, int status);
    Optional<User> findByUsername(String username);
    @Query("select u from User u where u.username = ?1 and u.enabled = true")
    Optional<User> findByUsernameAndEnabledIsTrue(String username);
    Optional<User> findByUsernameOrEmail(String username, String email);

    @Query(value = """
            SELECT u.*, ST_Distance_Sphere(point(u.latitude, u.longitude), point(?1,?2)) AS st_distance
            FROM users AS u
            LEFT JOIN drivers AS drv ON drv.user_id = u.id
            WHERE drv.active = 1 and u.enabled = 1 and u.user_mode = 1
            HAVING st_distance >= 1 AND st_distance <= ?3""", nativeQuery = true)
    List<User> findByLocationAndDistance(Double latitude, Double longitude, int distance);

    @Query("SELECT u FROM User u WHERE (u.geohashLocation LIKE '?1%') AND u.driverMode = TRUE AND u.enabled = true")
    List<User> findByLocationApproximately(String geoHash);

    @Query(value = "SELECT MONTHNAME(registration_date) AS month_name, COUNT(id) AS count_user FROM `users` " +
            "WHERE DATE(registration_date) BETWEEN DATE_SUB(CURRENT_DATE(),INTERVAL 1 YEAR) AND CURRENT_DATE() " +
            "GROUP BY MONTH(registration_date)", nativeQuery = true)
    String getUserCountYear();
}
