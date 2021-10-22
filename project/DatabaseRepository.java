package ru.geekbrains.qa.java2.lesson8.project;

import ru.geekbrains.qa.java2.lesson8.project.entity.WeatherData;
import ru.geekbrains.qa.java2.lesson8.project.dto.WeatherResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

// Репозиторий для работы
public interface DatabaseRepository {

    boolean saveWeatherData(WeatherData weatherData) throws SQLException;

    List<WeatherData> getAllSavedData() throws IOException;
}
