package com.amin.ameenserver.settings;

import org.springframework.beans.factory.annotation.Autowired;

public class SettingsService {

    @Autowired
    private SettingsRepository settingsRepository;

    public Settings getValue(String name){
        Settings settings = settingsRepository.findByName(name);
        return settings;
    }
}
