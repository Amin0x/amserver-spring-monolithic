package com.amin.ameenserver.order;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByRiderId(long userId, Pageable of);
    List<Order> findByDriverId(long userId, Pageable of);
    //Order findById(long id);
    Optional<Order> findByIdAndDriverIdOrRiderId(long id, long driverId, long riderId);
    //Order findByIdAndStatus(long userId, String status);

    //@Query(value = "SELECT * FROM orders WHERE driver_id = ?1 AND status = ?2", nativeQuery = true)
    //Order findByDriverIdAndStatus(long id, String status);

    //@Query(value = "SELECT * FROM orders WHERE (driver_id = ?1 OR rider_id = ?1) AND status in ?3", nativeQuery = true)
    //Order findFirstByDriverIdOrRiderIdAndStatusInOrderByCreatedAsc(long id, long uid, List<String> status);

    Long countByDriverIdAndRiderRate(Long id, Float i);
    Long countByRiderIdAndDriverRate(Long id, Float i);

    Optional<Order> findFirstByDriverIdOrderByCreated(Long id);
    Optional<Order> findFirstByRiderIdOrderByCreated(Long id);

    //@Query("select sum(u.price) FROM orders u where u.user_id = :userId and u.created >= :start and u.created <= :end")
    //long countById(@Param("userId") long userId, @Param("start") Date start, @Param("end") Date end);
}
