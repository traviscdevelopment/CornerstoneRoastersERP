public class DataConversion {
    private static final double gramsPerPound = 453.592;

    public static double gramsToPounds(double grams){
        return grams/gramsPerPound;
    }

    public static double poundsToGrams(double pounds){
        return pounds * gramsPerPound;
    }
}
