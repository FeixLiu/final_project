import java.util.Calendar;

public class Date {
    private String year;
    private String month;
    private String day;

    public Date() {
        Calendar now = Calendar.getInstance();
        year = String.valueOf(now.get(Calendar.YEAR));
        month = String.valueOf(now.get(Calendar.MONTH) + 1);
        day = String.valueOf(now.get(Calendar.DAY_OF_MONTH));
    }

    public Date(String year, String month, String day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public String getDate() {
        return year + "-" + month + "-" + day;
    }
}
