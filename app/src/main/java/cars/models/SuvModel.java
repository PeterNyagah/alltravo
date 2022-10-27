package cars.models;

public class SuvModel {

    String id, name, country, owner, image, description, city, rate;


    public SuvModel(String id, String name, String country, String owner, String image, String description, String city, String rate) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.owner = owner;
        this.image = image;
        this.description = description;
        this.city = city;
        this.rate = rate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "CarModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", owner='" + owner + '\'' +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                ", city='" + city + '\'' +
                ", rate='" + rate + '\'' +
                '}';
    }
}
