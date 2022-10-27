package hotels.models;

public class HotelRoomModel {
    String id,name,price,image,h_id,h_name;

    public HotelRoomModel(String id, String name, String price, String image,String h_id,String h_name) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.h_id = h_id;
        this.h_name = h_name;
    }

    public String getH_name() {
        return h_name;
    }

    public void setH_name(String h_name) {
        this.h_name = h_name;
    }

    public String getH_id() {
        return h_id;
    }

    public void setH_id(String h_id) {
        this.h_id = h_id;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
