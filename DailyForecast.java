package geekbrains;

import java.io.Serializable;

public class DailyForecast implements Serializable {
    public String Date;
    public Long EpochDate;
    public Temperature Temperature;
    public DN Day;
    public DN Night;
    public String[] Sources;
    public String MobileLink;
    public String Link;
}