package bookstore.pojos;

import  bookstore.entities.SerumEntity;
import java.util.Objects;
import java.util.Scanner;

// DTO for Serum (Serum)
// Console-facing DTO for the Serum niche product.
// Encapsulates presentation logic and maps to/from SerumEntity,
// keeping database concerns inside the JPA Entity classes.

public class Serum extends SkincareProduct {

    private String activeIngredient = "Hyaluronic Acid";
    private int copies;

    public Serum() {
        super();
        this.copies = 0;
    }

    public String getActiveIngredient() {
        return activeIngredient;
    }

    public void setActiveIngredient(String activeIngredient) {
        this.activeIngredient = activeIngredient;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    @Override
    public void initialize(Scanner input) {
        super.initialize(input);

        System.out.println("Enter Active Ingredient:");
        this.activeIngredient = getInput(input, this.activeIngredient);
    }

    @Override
    public void edit(Scanner input) {
        super.edit(input);

        System.out.println("Edit Active Ingredient [" + this.activeIngredient + "]:");
        this.activeIngredient = getInput(input, this.activeIngredient);
    }

    @Override
    public void sellItem() {
        if (copies > 0) {
            copies--;
        }
        System.out.println("Selling Serum with ingredient: " + activeIngredient);
    }

    @Override
    public String toString() {
        return "Serum{" +
                "activeIngredient='" + activeIngredient + '\'' +
                ", copies=" + copies +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Serum serum = (Serum) o;
        return Objects.equals(getActiveIngredient(), serum.getActiveIngredient());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getActiveIngredient());
    }

    public SerumEntity toEntity() {
        SerumEntity entity = new SerumEntity();

        // IDs inherited from Product
        entity.setId(this.getDbId());
        entity.setProductId(this.getProductId());

        // Attributes inherited from SkincareProduct
        entity.setSkinType(this.getSkinType());
        entity.setPrice(this.getPrice());

        // Serum-specific attribute
        entity.setActiveIngredient(this.getActiveIngredient());

        return entity;
    }

    public static Serum fromEntity(SerumEntity entity) {
        Serum dto = new Serum();

        // IDs inherited from Product
        dto.setDbId(entity.getId());
        dto.setProductId(entity.getProductId());

        // Attributes inherited from SkincareProduct
        dto.setSkinType(entity.getSkinType());
        dto.setPrice(entity.getPrice());

        // Serum-specific attribute
        dto.setActiveIngredient(entity.getActiveIngredient());

        return dto;
    }



}
