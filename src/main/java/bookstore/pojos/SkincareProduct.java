package bookstore.pojos;

import java.util.Objects;
import java.util.Scanner;

//  DTO Base for Niche Products (SkincareProduct)
// Abstract DTO parent for skincare products (Moisturizer, Serum).
// Holds shared presentation-layer fields and dbId mapping to the Entity primary key.
// Used only for console interaction and mapping, not persisted directly.

public abstract class SkincareProduct extends Product {

    private String skinType = "Normal";
    private double price = 0.0;

    public SkincareProduct() {
        super();
    }

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

    @Override
    public void initialize(Scanner input) {
        System.out.println("Enter Skin Type:");
        this.skinType = getInput(input, this.skinType);

        System.out.println("Enter Price:");
        this.price = getInput(input, this.price);
    }

    @Override
    public void edit(Scanner input) {
        System.out.println("Edit Skin Type [" + this.skinType + "]:");
        this.skinType = getInput(input, this.skinType);

        System.out.println("Edit Price [" + this.price + "]:");
        this.price = getInput(input, this.price);
    }

    @Override
    public String toString() {
        return "SkincareProduct{" +
                "skinType='" + skinType + '\'' +
                ", price=" + price +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SkincareProduct that = (SkincareProduct) o;
        return Double.compare(getPrice(), that.getPrice()) == 0 &&
                Objects.equals(getSkinType(), that.getSkinType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSkinType(), getPrice());
    }
}
