package com.company.Weather;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.javafx.fxml.builder.URLBuilder;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.sql.Timestamp;
import java.util.Date;

/*
* Соединяется с OpenWeatherMap API и получает прогноз погоды
* Использует HTTP
* */

public class ApiConnector implements Runnable {

    private Thread forDots = new Thread(this);
    private boolean neddDots = false;
    private static final String APP_ID = "c7fd32100220c0295367f127a8d1848a";

    public String getForecastForToday(String city) throws IOException {
        forDots = new Thread(this);
        forDots.start();
        URL address = null;
        try {
            String endpointWithParams = String.format("/data/2.5/weather?q=%s&appid=%s&units=%s", city, APP_ID, "metric");
            address = new URL("http", "api.openweathermap.org", endpointWithParams);
        } catch (MalformedURLException e) {
            System.err.println(e.toString());
            System.exit(0);
        }

        URLConnection connection = address.openConnection();
        StringBuilder result = new StringBuilder();
        try (InputStream is = connection.getInputStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            System.err.println(e.toString());
        }
        neddDots = false;
        while (forDots.isAlive()){

        }
        return result.toString();
    }
    public String getForecastForTomorrow(String city) throws IOException {
        URL address = null;
        forDots = new Thread(this);
        forDots.start();
        try {//aapi.openweathermap.org
            String endpointWithParams = String.format("/data/2.5/forecast/daily?q=%s,ru&cnt=1&appid=%s&units=%s", city, APP_ID, "metric");
            address = new URL("http", "api.openweathermap.org", endpointWithParams);
        } catch (MalformedURLException e) {
            System.err.println(e.toString());
            System.exit(0);
        }

        StringBuilder result = new StringBuilder();
        try (InputStream is = address.openStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            System.err.println(e.toString());
        }

        neddDots = false;
        while (forDots.isAlive()){

        }
        return result.toString();

    }

    @Override
    public void run() {
        neddDots = true;
        while (neddDots) {
            System.out.print('.');
            try {
                Thread.sleep(100);
            }catch (Exception ex){
                ex.getMessage();
            }
        }
        System.out.println();
    }
}
