package bookstore.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.util.Objects;

// Niche Entity Mapping (MoisturizerEntity)
// Concrete JPA entity for the Moisturizer niche product.
// Participates in Single Table Inheritance via ProductEntity.
// Contains niche-specific field: oilFree.

@Entity
public class MoisturizerEntity extends CosmeticEntity {

    @Column(name = "oil_free", nullable = false)
    private boolean oilFree;

    public MoisturizerEntity() {
    }

    public MoisturizerEntity(String skinType, double price, boolean oilFree) {
        super(skinType, price);
        this.oilFree = oilFree;
    }

    public boolean isOilFree() {
        return oilFree;
    }

    public void setOilFree(boolean oilFree) {
        this.oilFree = oilFree;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MoisturizerEntity that = (MoisturizerEntity) o;
        return Objects.equals(getProductId(), that.getProductId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProductId());
    }

    @Override
    public String toString() {
        return "MoisturizerEntity{" +
                "oilFree=" + oilFree +
                "} " + super.toString();
    }
}
