package com.company;

import com.company.Weather.WeatherCheker;

public class Application {

    public static void main(String[] args) {
        WeatherCheker cheker = WeatherCheker.getInstance();
        cheker.start();
    }


}
