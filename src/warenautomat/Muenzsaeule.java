package warenautomat;

public class Muenzsaeule {

    private static final int MAX_MUENZEN = 100;

    private int muenzArtRappen;
    private int muenzAnzahl;

    public Muenzsaeule(int muenzartRappen) {
        this.muenzArtRappen = muenzartRappen;
    }

    public int getMuenzartInRappen() {
        return muenzArtRappen;
    }

    public int getMuenzAnzahl() {
        return muenzAnzahl;
    }

    public boolean istVoll() {
        if (muenzAnzahl == MAX_MUENZEN) {
            return true;
        } else {
            return false;
        }
    }

    public void muenzeHinzufuegen(int zusaetzlicheMuenzAnzahl) {
        muenzAnzahl = muenzAnzahl + zusaetzlicheMuenzAnzahl;
    }

    public int getFreierMuenzPlatz() {
        return MAX_MUENZEN - muenzAnzahl;
    }

    public void muenzeAuswerfen() {
        muenzAnzahl = muenzAnzahl - 1;
    }

}
