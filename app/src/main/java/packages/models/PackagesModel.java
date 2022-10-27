package packages.models;

public class PackagesModel {

    int id;
    String name;
    String price;
    String validity;
    String image1;
    String image2;
    String image3;
    String image4;
    String description;
    String pkginclusion;

    public PackagesModel(int id, String name, String price, String validity, String image1, String image2, String image3, String image4, String description, String pkginclusion) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.validity = validity;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
        this.description = description;
        this.pkginclusion = pkginclusion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPkginclusion() {
        return pkginclusion;
    }

    public void setPkginclusion(String pkginclusion) {
        this.pkginclusion = pkginclusion;
    }
}
