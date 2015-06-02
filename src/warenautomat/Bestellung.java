package warenautomat;

public class Bestellung {

    private String warenName;
    private int grenze;
    private int bestellAnzahl;

    public Bestellung(String warenName, int grenze, int bestellAnzahl) {
        this.warenName = warenName;
        this.grenze = grenze;
        this.bestellAnzahl = bestellAnzahl;
    }

    public String getWarenName() {
        return warenName;
    }

    public int getGrenze() {
        return grenze;
    }

    public int getBestellAnzahl() {
        return bestellAnzahl;
    }
}
