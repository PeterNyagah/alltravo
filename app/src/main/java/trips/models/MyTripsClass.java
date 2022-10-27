package trips.models;

/**
 * Created by kuldeep on 31/01/18.
 */

public class MyTripsClass {

    private String text_airline;

    private String name;
    private String city;
    private String country;
    private String roomtype;
    private String persons;
    private String status;
    private String from;
    private String to;
    private String duration;
    private String text_timeschedule;
    private String type;
    private double price;
    private int id;
    private String date2;

    public String getDate2() {
        return date2;
    }

    public void setDate2(String date2) {
        this.date2 = date2;
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRoomtype() {
        return roomtype;
    }

    public void setRoomtype(String roomtype) {
        this.roomtype = roomtype;
    }

    public String getPersons() {
        return persons;
    }

    public void setPersons(String persons) {
        this.persons = persons;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
