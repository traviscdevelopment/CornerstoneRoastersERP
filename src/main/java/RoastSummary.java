import java.util.Date;

public class RoastSummary {
    private int amountOfRoasts;
    private double gramsUsed;
    private double gramsProduced;
    private double poundsUsed;
    private double poundsProduced;
    private int bagsProduced;
    private double hry;
    private double lry;
    private String roastingDate;

    public RoastSummary(int amountOfRoasts, double gramsUsed, double gramsProduced, double poundsUsed, double poundsProduced, int bagsProduced, double hry, double lry, String roastingDate) {
        this.amountOfRoasts = amountOfRoasts;
        this.gramsUsed = gramsUsed;
        this.gramsProduced = gramsProduced;
        this.poundsUsed = poundsUsed;
        this.poundsProduced = poundsProduced;
        this.bagsProduced = bagsProduced;
        this.hry = hry;
        this.lry = lry;
        this.roastingDate = roastingDate;
    }

    public void saveRoastSummary(){
        new DatabaseConnection().insertRoastBatches(this);
    }

    public int getAmountOfRoasts() {
        return amountOfRoasts;
    }

    public double getGramsUsed() {
        return gramsUsed;
    }

    public double getGramsProduced() {
        return gramsProduced;
    }

    public double getPoundsUsed() {
        return poundsUsed;
    }

    public double getPoundsProduced() {
        return poundsProduced;
    }

    public int getBagsProduced() {
        return bagsProduced;
    }

    public double getHry() {
        return hry;
    }

    public double getLry() {
        return lry;
    }

    public String getRoastingDate() {
        return roastingDate;
    }
}
