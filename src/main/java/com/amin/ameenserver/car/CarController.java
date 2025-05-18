package com.amin.ameenserver.car;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CarController {

    @Autowired
    private CarServices carServices;

    @GetMapping("/api/v1/cars/colors")
    public ResponseEntity<List<CarColor>> getAllColors(){
        List<CarColor> colors = carServices.getAllColors();
        return new ResponseEntity<>(colors, HttpStatus.OK);
    }

    @PostMapping(value = "/api/v1/cars/colors", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<CarColor> createColor(@RequestParam String colorName, @RequestParam String colorNameAr){
        if (!StringUtils.hasText(colorName) || !StringUtils.hasText(colorNameAr))
            throw new IllegalArgumentException("");

        CarColor color = carServices.createColor(colorName, colorNameAr);
        return new ResponseEntity<>(color, HttpStatus.CREATED);
    }

    @PostMapping("/api/v1/cars/colors/{id}/delete")
    public ResponseEntity<Void> deleteColor(@PathVariable int id){
        carServices.deleteColor(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/api/v1/cars/colors/{id}")
    public ResponseEntity<CarColor> updateColor(@PathVariable int id, @RequestParam String colorName, @RequestParam String colorNameAr){
        CarColor updatedColor = carServices.updateColor(id, colorName, colorNameAr);
        return new ResponseEntity<>(updatedColor, HttpStatus.OK);
    }

    @GetMapping("/api/v1/cars/brands")
    public ResponseEntity<List<CarBrand>> getAllCarBrand(){
        List<CarBrand> brands = carServices.getAllCarBrand();
        return new ResponseEntity<>(brands, HttpStatus.OK);
    }

    @PostMapping("/api/v1/cars/manufacturers/{manufacturerId}/brands")
    public ResponseEntity<CarBrand> createCarBrand(@PathVariable long manufacturerId, @RequestParam String brandName){
        CarBrand brand = carServices.createCarBrand(manufacturerId, brandName);
        return new ResponseEntity<>(brand, HttpStatus.CREATED);
    }

    @PostMapping("/api/v1/cars/brands/{brandId}")
    public ResponseEntity<CarBrand> updateCarBrand(@PathVariable String brandId, @RequestBody CarBrand carBrand){
        CarBrand updatedBrand = carServices.updateCarBrand(brandId, carBrand);
        return new ResponseEntity<>(updatedBrand, HttpStatus.OK);
    }

    @PostMapping("/api/v1/cars/brands/{Id}/delete")
    public ResponseEntity<Void> deleteCarBrand(@PathVariable String id){
        carServices.deleteCarBrand(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
