package warenautomat;

public class Drehteller {

    private int drehtellerNummer;
    private static final int MAX_FAECHER = 16;
    private Fach[] faecher = new Fach[MAX_FAECHER];
    private Fach aktuellesFach;

    public Drehteller(int drehtellerNummer) {
        this.drehtellerNummer = drehtellerNummer;
        for (int i = 0; i < MAX_FAECHER; i++) {
            faecher[i] = new Fach(i);
        }
        aktuellesFach = faecher[0];
    }

    public void drehen() {
        if (aktuellesFach.getFachNummer() == (MAX_FAECHER - 1)) {
            aktuellesFach = faecher[0];
        } else {
            aktuellesFach = faecher[aktuellesFach.getFachNummer() + 1];
        }
        aktualisiereDrehtellerAnzeige();
    }

    public Fach getAktuellesFach() {
        return aktuellesFach;
    }

    public void setzeWareInAktuellesFach(Ware ware) {
        aktuellesFach.setWare(ware);
    }

    public void aktualisiereDrehtellerAnzeige() {
        SystemSoftware.zeigeWarenPreisAn(drehtellerNummer, aktuellesFach.getWarenPreis());
        SystemSoftware.zeigeVerfallsDatum(drehtellerNummer, aktuellesFach.getVerfallsDatumsZustand());
        if (aktuellesFach.getWare() != null) {
            SystemSoftware.zeigeWareInGui(drehtellerNummer, aktuellesFach.getWare().getWarenName(), aktuellesFach.getWare().getVerfallsDatum());
        }
    }

    public Fach[] gibFaecher() {
        return faecher;
    }

    public void leereAktuelesFach() {
        aktuellesFach.setWare(null);
    }
}
