package cars.models;

public class CarModel {

    //    String id, name, country, owner, image, description, city, rate;
    String  name, plate, capacity, fee, city, country, image, description;
    Integer id;

    public CarModel(String name, String plate, String capacity, String fee, String city, String country, String image, String description, Integer id) {
        this.name = name;
        this.plate = plate;
        this.capacity = capacity;
        this.fee = fee;
        this.city = city;
        this.country = country;
        this.image = image;
        this.description = description;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CarModel{" +
                "name='" + name + '\'' +
                ", plate='" + plate + '\'' +
                ", capacity='" + capacity + '\'' +
                ", fee='" + fee + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                '}';
    }
}