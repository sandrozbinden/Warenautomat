package warenautomat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Drehteller {

    private int drehtellerNummer;
    private static final int MAX_FAECHER = 16;
    private Fach[] faecher = new Fach[MAX_FAECHER];
    private Fach aktuellesFach;

    public Drehteller(int drehtellerNummer) {
        this.drehtellerNummer = drehtellerNummer;
        IntStream.range(0, MAX_FAECHER).forEach(i -> faecher[i] = new Fach(i));
        aktuellesFach = faecher[0];
    }

    public void drehen() {
        aktuellesFach = faecher[gibNeueAktuelleFachNummer()];
        aktualisiereDrehtellerAnzeige();
    }

    private int gibNeueAktuelleFachNummer() {
        return aktuellesFach.getFachNummer() == (MAX_FAECHER - 1) ? 0 : aktuellesFach.getFachNummer() + 1;
    }

    public Fach getAktuellesFach() {
        return aktuellesFach;
    }

    public void setzeWareInAktuellesFach(Ware ware) {
        aktuellesFach.setWare(ware);
        aktualisiereDrehtellerAnzeige();
    }

    public void aktualisiereDrehtellerAnzeige() {
        SystemSoftware.zeigeWarenPreisAn(drehtellerNummer, aktuellesFach.getWarenPreis());
        SystemSoftware.zeigeVerfallsDatum(drehtellerNummer, aktuellesFach.getVerfallsDatumsZustand());
        if (aktuellesFach.getWare().isPresent()) {
            Ware ware = aktuellesFach.getWare().get();
            SystemSoftware.zeigeWareInGui(drehtellerNummer, ware.getWarenName(), ware.getVerfallsDatum());
        }
    }

    public List<Fach> gibFaecher() {
        return Arrays.asList(faecher);
    }

    public void leereAktuelesFach() {
        aktuellesFach.setWare(null);
    }
}
