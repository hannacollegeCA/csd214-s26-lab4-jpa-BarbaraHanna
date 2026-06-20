package bookstore.pojos;

import java.util.Objects;
import java.util.Scanner;

public class Tire extends VehiclePart{
    private int diameter;

    public Tire() {
    }

    public Tire(int diameter) {
        this.diameter = diameter;
    }

    public Tire(String manufacturer, double price, int diameter) {
        super(manufacturer, price);
        this.diameter = diameter;
    }

    @Override
    public String toString() {
        return "Tire{" +
                "diameter=" + diameter +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Tire tire = (Tire) o;
        return diameter == tire.diameter;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), diameter);
    }

    public int getDiameter() {
        return diameter;
    }

    public void setDiameter(int diameter) {
        this.diameter = diameter;
    }

    @Override
    public void initialize(Scanner input) {
        // Pass scanner up to parent
        super.initialize(input);

        System.out.println("Enter Diameter:");
        this.diameter = getInput(input, 0);

    }

    @Override
    public void edit(Scanner input) {
        super.edit(input);
        System.out.println("Enter Diameter:");
        this.diameter = getInput(input, getDiameter());

    }


    @Override
    public void sellItem() {
        System.out.println("Selling a Tire");
    }
}
