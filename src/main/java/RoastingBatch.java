public class RoastingBatch {
    private double gramsUsed;
    private double gramsProduced;
    private double poundsUsed;
    private double poundsProduced;
    private int roastNumber;
    private int bagsWanted;


    //creates roasting batch with the roast number grams used and bags wanted
    public RoastingBatch(int roastNumber, double gramsUsed, int bagsWanted) {
        this.roastNumber = roastNumber;
        this.gramsUsed = gramsUsed;
        this.bagsWanted = bagsWanted;
        poundsUsed = DataConversion.gramsToPounds(gramsUsed);
    }

    //updates grams produced and updates cache database on input change
    public void setgramsProduced(double gramsProduced) {
        this.gramsProduced = gramsProduced;
        poundsProduced = DataConversion.gramsToPounds(gramsProduced);
        updateCache();
    }

    //function for updating the cache on input change
    private void updateCache(){
        new DatabaseConnection().setCacheSession(roastNumber,gramsUsed,
                gramsProduced,poundsUsed,poundsProduced,bagsWanted);
    }



}
