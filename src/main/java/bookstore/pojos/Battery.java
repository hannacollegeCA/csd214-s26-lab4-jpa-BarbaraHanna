package bookstore.pojos;

import java.util.Objects;
import java.util.Scanner;

public class Battery extends VehiclePart{
    private int coldCrankingAmps;

    public Battery() {
    }

    public Battery(int coldCrankingAmps) {
        this.coldCrankingAmps = coldCrankingAmps;
    }

    public Battery(String manufacturer, double price, int coldCrankingAmps) {
        super(manufacturer, price);
        this.coldCrankingAmps = coldCrankingAmps;
    }

    @Override
    public String toString() {
        return "Battery{" +
                "coldCrankingAmps=" + coldCrankingAmps +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Battery battery = (Battery) o;
        return coldCrankingAmps == battery.coldCrankingAmps;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), coldCrankingAmps);
    }

    public int getColdCrankingAmps() {
        return coldCrankingAmps;
    }

    public void setColdCrankingAmps(int coldCrankingAmps) {
        this.coldCrankingAmps = coldCrankingAmps;
    }

    @Override
    public void initialize(Scanner input) {
        // Pass scanner up to parent
        super.initialize(input);
        System.out.println("Enter Cold Cranking Amps:");
        this.coldCrankingAmps = getInput(input, 0);
    }

    @Override
    public void edit(Scanner input) {
        super.edit(input);
        System.out.println("Enter Cold Cranking Amps:");
        this.coldCrankingAmps = getInput(input, getColdCrankingAmps());
    }

    @Override
    public void sellItem() {
        System.out.println("Selling a Battery");
    }
}
