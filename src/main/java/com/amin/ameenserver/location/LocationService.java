package com.amin.ameenserver.location;

import com.amin.ameenserver.user.User;
import com.amin.ameenserver.user.UserRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    @Autowired
    private UserService userService;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationAreaRepository locationAreaRepository;

    public void updateUserLocation(User user, Double latitude, Double longitude){

        Location location = new Location();
        location.setUser(user);
        location.setLongitude(longitude);
        location.setLatitude(latitude);
        locationRepository.save(location);
    }

    public Location getLastUserLocation(User user) {
        return locationRepository.findFirstByUserIdOrderByTimestampDesc(user.getId());
    }

    public void sendUpdateDriverLocation(Location location){

    }

    public List<User> getAllUserByLocation(Double latitude, Double longitude){
        return userRepository.findByLocationAndDistance(latitude, longitude, 3000);
    }

    public LocationArea getAreaFromLocation(Double latitude, Double longitude){

        List<LocationArea> list = locationAreaRepository.findAll();

        for (int i = 0; i < list.size(); i++) {
            LocationArea item = list.get(i);
            if (isBounded(item.getLatitude1(), item.getLongitude1(), item.getLatitude2(),item.getLongitude2(),latitude, longitude)){
                return item;
            }
        }
        return null;
    }

    public Boolean locationInsidePolygon(Double latitude, Double longitude, LocationArea locationArea){
        /*return locationInsideRectangle(latitude,longitude,
                locationArea.getTopLeft(),
                locationArea.getTopLeft(),
                locationArea.getTopLeft(),
                locationArea.getTopLeft());*/

        return false;
    }

    public Boolean locationInsideRectangle(Double latitude, Double longitude, Point pt1, Point pt2, Point pt3, Point pt4){

        GeometryFactory geometryFactory = new GeometryFactory();

//        org.locationtech.jts.geom.Point point1 = geometryFactory.createPoint(new Coordinate(pt1.getX(), pt1.getY()));
//        org.locationtech.jts.geom.Point point2 = geometryFactory.createPoint(new Coordinate(pt2.getX(), pt2.getY()));
//        org.locationtech.jts.geom.Point point3 = geometryFactory.createPoint(new Coordinate(pt3.getX(), pt3.getY()));
//        org.locationtech.jts.geom.Point point4 = geometryFactory.createPoint(new Coordinate(pt4.getX(), pt4.getY()));

        LinearRing linearRing = geometryFactory.createLinearRing(new Coordinate[]{
                new Coordinate(pt1.getX(), pt1.getY()),
                new Coordinate(pt2.getX(), pt2.getY()),
                new Coordinate(pt3.getX(), pt3.getY()),
                new Coordinate(pt4.getX(), pt4.getY())
        });

        Polygon polygon = geometryFactory.createPolygon(linearRing);

        org.locationtech.jts.geom.Point location = geometryFactory.createPoint(new Coordinate(latitude, longitude));

        return polygon.contains(location);

    }

    /*
     * top: north latitude of bounding box.
     * left: left longitude of bounding box (western bound).
     * bottom: south latitude of the bounding box.
     * right: right longitude of bounding box (eastern bound).
     * latitude: latitude of the point to check.
     * longitude: longitude of the point to check.
     */
    private boolean isBounded(double top, double left, double bottom, double right, double latitude, double longitude){
        /* Check latitude bounds first. */
        if(top >= latitude && latitude >= bottom){
                    /* If your bounding box doesn't wrap
                       the date line the value
                       must be between the bounds.
                       If your bounding box does wrap the
                       date line it only needs to be
                       higher than the left bound or
                       lower than the right bound. */
            if(left <= right && left <= longitude && longitude <= right){
                return true;
            } else if(left > right && (left <= longitude || longitude <= right)) {
                return true;
            }
        }
        return false;
    }
}
