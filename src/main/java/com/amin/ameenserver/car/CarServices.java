package com.amin.ameenserver.car;

import com.amin.ameenserver.core.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarServices {

    @Autowired
    private CarBrandRepository carBrandRepository;

    @Autowired
    private CarColorRepository carColorRepository;

    @Autowired
    private CarManufacturerRepository carManufacturerRepository;

    public CarBrand createCarBrand(long manufacurerId, String brandName){
        CarManufacurer manufacurer = carManufacturerRepository.findById(manufacurerId)
                .orElseThrow(() -> new ResourceNotFoundException(""));

        CarBrand brand = new CarBrand(brandName, Math.toIntExact(manufacurer.getId()));
        return carBrandRepository.save(brand);
    }

    public void deleteCarBrand(String id){
        carBrandRepository.deleteById(id);
    }

    public CarBrand getCarBrand(String id){
        return carBrandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("car brand not found"));
    }

    public List<CarBrand> getAllCarBrand(){
        return carBrandRepository.findAll();
    }

    public CarBrand updateCarBrand(String name, CarBrand carBrand){
        CarBrand brand = carBrandRepository.findById(name).orElseThrow(() -> new ResourceNotFoundException(""));
        brand.setBrandAr(carBrand.getBrandAr());
        brand.setManufactureId(carBrand.getManufactureId());
        return carBrandRepository.save(brand);
    }

    public CarColor createColor(String colorName, String colorNameAr){
        CarColor carColor = new CarColor(colorName, colorNameAr);
        try {
            return carColorRepository.save(carColor);
        } catch (Exception e) {
            throw new IllegalArgumentException("cant create car color " + colorName);
        }
    }

    public CarColor updateColor(int id, String colorName, String colorNameAr){
        CarColor carColor = carColorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(""));
        carColor.setColor(colorName);
        carColor.setColorAr(colorNameAr);
        return carColorRepository.save(carColor);
    }

    public void deleteColor(int id){
        try {
            carColorRepository.deleteById(id);
        } catch (Exception e) {
            throw new ResourceNotFoundException("no color found with id " + id);
        }
    }

    public List<CarColor> getAllColors(){
        return carColorRepository.findAll();
    }

    public CarManufacurer createManufacurer(String manufacurer, String manufacurerAr){
        CarManufacurer carManufacurer = new CarManufacurer(manufacurer, manufacurerAr);
        return carManufacturerRepository.save(carManufacurer);
    }

    public CarManufacurer updateManufacurer(long id, String manufacurer, String manufacurerAr){
        CarManufacurer carManufacurer = carManufacturerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(""));
        carManufacurer.setManufacurer(manufacurer);
        carManufacurer.setManufacurerAr(manufacurerAr);
        return carManufacturerRepository.save(carManufacurer);
    }

    public CarManufacurer getManufacurer(long id){
        CarManufacurer carManufacurer = carManufacturerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(""));
    
        return carManufacurer;
    }

    public List<CarManufacurer> getAllManufacurers(){
        List<CarManufacurer> carManufacurers = carManufacturerRepository.findAll();
    
        return carManufacurers;
    }

    public void deleteManufacurer(long id){
        carManufacturerRepository.deleteById(id);
    }
}
