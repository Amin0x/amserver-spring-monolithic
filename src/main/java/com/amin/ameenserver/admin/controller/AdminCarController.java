package com.amin.ameenserver.admin;

import com.amin.ameenserver.car.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminCarController {

    @Autowired
    CarBrandRepository carBrandRepository;

    @Autowired
    CarManufacturerRepository manufacturerRepository;

    @Autowired
    CarColorRepository carColorRepository;

    @ResponseBody
    @PostMapping("/cars/addCarBrand")
    public CarBrand addCarBrand(@RequestBody CarBrand carBrand){
        return carBrandRepository.save(carBrand);
    }

    @ResponseBody
    @PostMapping("/cars/deleteCarBrand")
    public void deleteCarBrand(@RequestBody String id){
        carBrandRepository.deleteById(id);
    }
    
	@ResponseBody
    @PostMapping("/cars/updateCarBrand")
    public void updateCarBrand(@RequestBody CarBrand carBrand){
        carBrandRepository.save(carBrand);
    }

    @GetMapping("/cars/getAllManufacturer")
    public List<CarManufacurer> getAllManufacturer(){
        return manufacturerRepository.findAll();
    }

    @ResponseBody
    @PostMapping("/cars/addManufacturer")
    public CarManufacurer addManufacturer(@RequestBody CarManufacurer manufacturer){
        return manufacturerRepository.save(manufacturer);
    }

    @ResponseBody
    @PostMapping("/cars/deleteManufacturer")
    public void deleteManufacturer(@RequestBody Long id){
        manufacturerRepository.deleteById(id);
    }

    @ResponseBody
    @PostMapping("/cars/updateManufacturer")
    public void updateManufacturer(@RequestBody CarManufacurer manufacturer){
        manufacturerRepository.save(manufacturer);
    }

    @GetMapping("/cars/getAllCarColors")
    public List<CarColor> getAllCarColors(){
        return carColorRepository.findAll();
    }

    @GetMapping("/cars/getCarColor/{id}")
    public void getCarColor(@PathVariable Integer id){
        carColorRepository.findById(id);
    }

    @ResponseBody
    @PostMapping("/cars/addCarColor")
    public CarColor addCarColor(@RequestBody CarColor carColor){
        return carColorRepository.save(carColor);
    }

    @ResponseBody
    @PostMapping("/cars/deleteCarColor")
    public void deleteCarColor(){}
}
