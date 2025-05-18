package com.amin.ameenserver.settings;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingsRepository extends JpaRepository<Settings, Long> {
    Settings findByName(String name);
}