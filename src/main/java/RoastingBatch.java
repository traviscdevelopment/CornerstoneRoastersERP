import javax.xml.crypto.Data;

public class RoastingBatch {
    private double gramsUsed;
    private double gramsProduced;
    private double poundsUsed;
    private double poundsProduced;
    private int roastNumber;
    private int bagsWanted;

    public RoastingBatch(int roastNumber, double gramsUsed, int bagsWanted) {
        this.roastNumber = roastNumber;
        this.gramsUsed = gramsUsed;
        this.bagsWanted = bagsWanted;
        poundsUsed = DataConversion.gramsToPounds(gramsUsed);
    }

    public void setgramsProduced(double gramsProduced) {
        this.gramsProduced = gramsProduced;
        poundsProduced = DataConversion.gramsToPounds(gramsProduced);
        updateCache();
    }

    private void updateCache(){
        new DatabaseConnection().setCacheSession(roastNumber,gramsUsed,
                gramsProduced,poundsUsed,poundsProduced,bagsWanted);
    }



}
