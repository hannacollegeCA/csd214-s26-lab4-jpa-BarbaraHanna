package bookstore.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.util.Objects;

// Niche Abstract Entity (CosmeticEntity)
// Abstract JPA entity parent for cosmetic niche products.
// Extends ProductEntity and participates in Single Table Inheritance.
// Holds shared database fields for MoisturizerEntity and SerumEntity.

@Entity
public abstract class CosmeticEntity extends ProductEntity {


    //Shared persistent fields for all cosmetic niche products
    @Column(name = "skin_type", nullable = false)
    private String skinType;

    @Column(name = "price", nullable = false)
    private double price;

    //Required no-arg constructor for Hibernate
    public CosmeticEntity() {
    }

    //Loaded constructor for subclass chaining
    public CosmeticEntity(String skinType, double price) {
        this.skinType = skinType;
        this.price = price;
    }

    // Standard getters/setters (presentation logic stays in DTOs)
    public String getSkinType() {
        return skinType;
    }

    public void setSkinType(String skinType) {
        this.skinType = skinType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // Identity Logic (equals, hashCode)
    // Entities must use productId (UUID) for logical equality.
    // This ensures stable identity across persistence operations
    // when Hibernate compares managed vs. detached entities
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CosmeticEntity that = (CosmeticEntity) o;
        return Objects.equals(getProductId(), that.getProductId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProductId());
    }

    //Debugging & Polymorphic Output (toString)
    //Helps verify inheritance mapping and ensures niche fields appear in JPQL listings
    @Override
    public String toString() {
        return "CosmeticEntity{" +
                "skinType='" + skinType + '\'' +
                ", price=" + price +
                "} " + super.toString();
    }
}
