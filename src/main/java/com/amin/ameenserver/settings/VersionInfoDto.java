package com.amin.ameenserver.settings;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VersionInfoDto implements Serializable {
    public int minVersion;
    public int version;
    public int heartbeat;
    public List<Settings> settings;
}
