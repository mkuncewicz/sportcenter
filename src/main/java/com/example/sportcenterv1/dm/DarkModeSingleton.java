package com.example.sportcenterv1.dm;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DarkModeSingleton {

    private static DarkModeSingleton instance;

    private boolean isDarkMode = false;

    private DarkModeSingleton(){}

    public static DarkModeSingleton getInstance(){
        if (instance == null){
            instance = new DarkModeSingleton();
        }
        return instance;
    }
}
