package geekbrains;

import java.io.Serializable;

public class WeatherResponse implements Serializable {
    public Headline Headline;
    public DailyForecast[] DailyForecasts;
}