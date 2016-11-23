package kuangye.entity;

import java.util.Map;

/**
 * Created by YE on 2016/11/15.
 */
public class Product {

    private String type;
    private String id;
    private String name;
    private long price;
    private long stock;
    private long quantity;
    private String description;
    private Map<String, String> properties;

    public Product(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public long getStock() {
        return stock;
    }

    public void setStock(long stock) {
        this.stock = stock;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public synchronized void addQuantity(long quantity) {
        this.quantity += quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
        if (properties != null) {
            this.description = String.join(", ", properties.values());
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Product)) return false;
        if (this.id == null) {
            if (((Product) obj).id == null) return true;
            return false;
        }
        return this.id.equals(((Product) obj).getId());
    }

    @Override
    public int hashCode() {
        if (this.id == null) return 0;
        return this.id.hashCode();
    }


}
