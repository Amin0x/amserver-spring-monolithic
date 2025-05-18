package com.amin.ameenserver.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class SettingsController {

    @Autowired
    SettingsRepository settingsRepository;

    @PostMapping("/admin/settings/")
    public ResponseEntity<Settings> createSetting(Settings setting){
        Settings savedSetting = settingsRepository.save(setting);
        return new ResponseEntity<>(savedSetting, HttpStatus.CREATED);
    }

    @PutMapping("/admin/settings/{id}")
    public ResponseEntity<Settings> updateSetting(@PathVariable long id, @RequestBody Settings newSetting){
        Optional<Settings> optionalSetting = settingsRepository.findById(id);
        if (optionalSetting.isPresent()) {
            Settings existingSetting = optionalSetting.get();
            existingSetting.setName(newSetting.getName()); // Assuming you have a setName method
            existingSetting.setValue(newSetting.getValue()); // Assuming you have a setValue method
            Settings updatedSetting = settingsRepository.save(existingSetting);
            return new ResponseEntity<>(updatedSetting, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/admin/settings/{id}")
    public ResponseEntity<Settings> getSetting(@PathVariable long id) {
        Optional<Settings> setting = settingsRepository.findById(id);
        if (setting.isPresent()) {
            return new ResponseEntity<>(setting.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/admin/settings")
    public ResponseEntity<List<Settings>> getAllSetting(){
        List<Settings> settings = settingsRepository.findAll();
        return new ResponseEntity<>(settings, HttpStatus.OK);
    }

    @DeleteMapping("/admin/settings/{id}")
    public ResponseEntity<Void> deleteSetting(@PathVariable long id) {
        settingsRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
