package bookstore.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.util.Objects;

// Niche Entity Mapping (SerumEntity)
// Concrete JPA entity for the Serum niche product.
// Inherits from CosmeticEntity / ProductEntity (Single Table Inheritance).
// Contains niche-specific field: activeIngredient.

@Entity
public class SerumEntity extends CosmeticEntity {

    @Column(name = "active_ingredient", nullable = false)
    private String activeIngredient;

    public SerumEntity() {
    }

    public SerumEntity(String skinType, double price, String activeIngredient) {
        super(skinType, price);
        this.activeIngredient = activeIngredient;
    }

    public String getActiveIngredient() {
        return activeIngredient;
    }

    public void setActiveIngredient(String activeIngredient) {
        this.activeIngredient = activeIngredient;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SerumEntity that = (SerumEntity) o;
        return Objects.equals(getProductId(), that.getProductId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProductId());
    }
    @Override
    public String toString() {
        return "SerumEntity{" +
                "activeIngredient='" + activeIngredient + '\'' +
                "} " + super.toString();
    }
}
