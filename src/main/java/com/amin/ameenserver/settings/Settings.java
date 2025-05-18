package com.amin.ameenserver.settings;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "Settings")
public class Settings implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String name;
    private String value;
    private boolean enabled;
}
