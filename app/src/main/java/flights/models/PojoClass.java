package flights.models;

/**
 * Created by kuldeep on 31/01/18.
 */

public class PojoClass {

    private String text_airline,text_timeschedule,text_time;
    private String id;
    private String from;
    private String to;
    private boolean way;
    private String level;
    private String passangers;
    private String departure_date;
    private String return_date;
    private String stops;
    private int logo;

    public String getStops() {
        return stops;
    }

    public void setStops(String stops) {
        this.stops = stops;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public boolean getWay() {
        return way;
    }

    public void setWay(boolean way) {
        this.way = way;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPassangers() {
        return passangers;
    }

    public void setPassangers(String passangers) {
        this.passangers = passangers;
    }

    public String getDeparture_date() {
        return departure_date;
    }

    public void setDeparture_date(String departure_date) {
        this.departure_date = departure_date;
    }

    public String getReturn_date() {
        return return_date;
    }

    public void setReturn_date(String return_date) {
        this.return_date = return_date;
    }

    public String getText_airline() {
        return text_airline;
    }

    public void setText_airline(String text_airline) {
        this.text_airline = text_airline;
    }

    public String getText_timeschedule() {
        return text_timeschedule;
    }

    public void setText_timeschedule(String text_timeschedule) {
        this.text_timeschedule = text_timeschedule;
    }

    public String getText_time() {
        return text_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setText_time(String text_time) {
        this.text_time = text_time;
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }
}
