package hotels.models;

import android.widget.ImageView;
import android.widget.TextView;

public class HotelModel {
    String id,name,country,owner,image,description,city;

    public HotelModel(String id, String name, String country, String owner, String image, String description,String city) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.owner = owner;
        this.image = image;
        this.description = description;
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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
}
