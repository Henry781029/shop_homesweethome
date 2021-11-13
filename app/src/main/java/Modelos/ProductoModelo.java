package Modelos;

public class ProductoModelo {

    private String country;
    private String city;
    private String adress;
    private String bedrooms;
    private String priceNight;
    private String description;
    private String owner;
    private String state;

    public ProductoModelo() {
    }

    public ProductoModelo(String country, String city, String adress, String bedrooms, String priceNight,
                          String description, String owner, String state) {
        this.country = country;
        this.city = city;
        this.adress = adress;
        this.bedrooms = bedrooms;
        this.priceNight = priceNight;
        this.description = description;
        this.owner = owner;
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(String bedrooms) {
        this.bedrooms = bedrooms;
    }

    public String getPriceNight() {
        return priceNight;
    }

    public void setPriceNight(String priceNight) {
        this.priceNight = priceNight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
