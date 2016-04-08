package com.company.Weather;

import com.company.GuiCallback;
import com.google.gson.*;

import java.io.*;

/**
 * Created by Maksim on 01.04.2016.
 */
public class WeatherCheker {
    private Settings settings;
    private BufferedReader reader;
    private File settingsFile;
    private static WeatherCheker cheker;
    private ApiConnector api;

    public static WeatherCheker getInstance() {
        return cheker == null ? cheker = new WeatherCheker() : cheker;
    }

    public void printGreeting() {
        System.out.println("Hello from the OpenWeatherMap API sample app!");
    }

    public void printMenu(GuiCallback callback) {
        System.out.println("1 - Get weather forecast for today for " + settings.City);
        System.out.println("2 - Get weather forecast for tomorrow for " + settings.City);
        System.out.println("3 - Set target City");
        System.out.println("0 - Exit");

        int option;
        try {
            option = Integer.parseInt(reader.readLine());
        } catch (IOException e) {
            System.out.println("Введено некорректное число");
            return;
        }
        switch (option) {
            case 1 : printForecastForToday();
                break;
            case 2: printForecastForTomorrow();
                break;
            case 3: setCity();
                break;
            case 0:
                System.out.println("Работа окончена!");
                System.exit(0);
        }
        callback.reset();
    }

    public void printForecastForToday(){
        String jsonFotToday;
        try {
            jsonFotToday = api.getForecastForToday(settings.City);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }
        JsonParser parser = new JsonParser();
        JsonElement mainElement = parser.parse(jsonFotToday);

        JsonObject rootObject = mainElement.getAsJsonObject();
        JsonObject mainObject = rootObject.getAsJsonObject("main");


        System.out.println( String.format(
                "Temperature today is %s, pressure %s",
                mainObject.get("temp").getAsString(),
                mainObject.get("pressure").getAsString()
        ));
    }
    public void printForecastForTomorrow() {
        String forecastForTomorrow;
        try {
            forecastForTomorrow = api.getForecastForTomorrow(settings.City);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            return;
        }
        JsonParser parser = new JsonParser();
        JsonElement mainElement = parser.parse(forecastForTomorrow);

        JsonObject rootObject = mainElement.getAsJsonObject();
        JsonArray list = rootObject.getAsJsonArray("list");

        String temp = list.get(0).getAsJsonObject()
                .get("temp").getAsJsonObject()
                .get("day").getAsString();
        String pressure = list.get(0).getAsJsonObject()
                .get("pressure").getAsString();

        System.out.println(String.format(
                "Temperature tomorrow is %s, pressure %s",
                temp,
                pressure
        ));
    }

    public void setCity(){
        System.out.println("Enter new City: ");
        try{
            settings.City = new BufferedReader(new InputStreamReader(System.in)).readLine();
            System.out.println("New City is: " + settings.City);
            saveSettings();
        } catch (IOException ex){
            System.out.println("Ошибка ввода");
        }
    }

    private void saveSettings() throws IOException {
        FileWriter writer = new FileWriter(settingsFile);
        Gson gson = new Gson();
        writer.write(gson.toJson(settings));
        writer.close();
    }

    private WeatherCheker() {
        reader = new BufferedReader(new InputStreamReader(System.in));
        settingsFile = new File("default.json");
        api = new ApiConnector();
        loadSettings();
    }


    private void loadSettings() {
        try {
            if (!settingsFile.exists()) createDefaultSettings();
            String settingsString = (new BufferedReader(new FileReader(settingsFile.getAbsoluteFile()))).readLine();
            JsonObject root = new JsonParser().parse(settingsString).getAsJsonObject();
            settings = new Settings(root.get("City").getAsString());
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    private void createDefaultSettings() throws IOException {
        settingsFile.createNewFile();
        FileWriter writer = new FileWriter(settingsFile);
        Gson gson = new Gson();
        writer.write(gson.toJson(new Settings("Moscow")));
        writer.close();
    }

    public void start() {
        printGreeting();
        run();
    }


    public void run() {
        printMenu(new GuiCallback() {
            @Override
            public void reset() {
                run();
            }
        });
    }

    private class Settings {
        public String City;

        public Settings(String city) {
            this.City = city;
        }
    }
}
