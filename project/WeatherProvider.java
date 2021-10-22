package ru.geekbrains.qa.java2.lesson8.project;

import ru.geekbrains.qa.java2.lesson7_project.project.enums.Periods;
import ru.geekbrains.qa.java2.lesson8.project.entity.WeatherData;

import java.io.IOException;

public interface WeatherProvider {

    WeatherData getWeather(Periods periods) throws IOException;

    WeatherData getAllFromDb() throws IOException;
}
