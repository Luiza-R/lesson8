package geekbrains;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.sql.*;

public class Main {

    private static final String DB_URL = "jdbc:sqlite:D:/weather.db";

    public static void main(String[] args) throws Exception {
        String result = getWeatherFromAPI();

        //

        createDB();
        parseAndPutWeatherToDB(result);
    }

    private static String getWeatherFromAPI() throws Exception {
        String result = "";
        HttpGet get = new HttpGet("http://dataservice.accuweather.com/forecasts/v1/daily/5day/2206565"
                + "?apikey=4kLEHve4i2NDl9nx977kd5fxiKwwWwlF");

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(get)){
            result = EntityUtils.toString(response.getEntity());
        }

        System.out.println(result);
        return result;
    }

    private static void parseAndPutWeatherToDB(String str) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        WeatherResponse wr = objectMapper.readValue(str, WeatherResponse.class);

        if (wr != null && wr.DailyForecasts != null) {
            try (Connection conn = DriverManager.getConnection(DB_URL)) {
                if (conn != null) {
                    Statement statement = conn.createStatement();
                    statement.setQueryTimeout(30);

                    for (DailyForecast df : wr.DailyForecasts) {
                        // Выводим значения дня в консоль
                        String toUser = "В городе Saint-Petersburg на дату "
                                + df.Date
                                + " ожидается "
                                + df.Day.IconPhrase
                                + ", температура - "
                                + df.Temperature.Maximum.Value
                                + df.Temperature.Maximum.Unit;
                        System.out.println(toUser);

                        // Засовываем значения дня в БД
                        statement.executeUpdate("insert into weather values("
                                + "'Saint-Petersburg',"
                                + " '" + df.Date + "',"
                                + " '" + df.Day.IconPhrase + "',"
                                + df.Temperature.Maximum.Value
                                + ")");
                    }

                    // Проверяем, что засунулось в БД
                    ResultSet rs = statement.executeQuery("select * from weather");
                    while (rs.next()) {
                        System.out.println("city = " + rs.getString("city"));
                        System.out.println("localdate = " + rs.getString("localdate"));
                        System.out.println("weathertext = " + rs.getString("weathertext"));
                        System.out.println("temperature = " + rs.getDouble("temperature"));
                    }
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Не удалось получить погоду");
        }
    }

    public static void createDB() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");

                Statement statement = conn.createStatement();
                statement.setQueryTimeout(30);

                statement.executeUpdate("drop table if exists weather");
                statement.executeUpdate("create table weather (city string, localdate string, weathertext string, temperature double)");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}