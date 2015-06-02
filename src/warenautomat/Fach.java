package warenautomat;

import java.util.Optional;

public class Fach {

    private int fachNummer;
    private Ware ware;

    public Fach(int fachNummer) {
        this.fachNummer = fachNummer;
    }

    public int getFachNummer() {
        return fachNummer;
    }

    public void setWare(Ware ware) {
        this.ware = ware;
    }

    public Optional<Ware> getWare() {
        return Optional.ofNullable(ware);
    }

    public int getVerfallsDatumsZustand() {
        if (istLeer()) {
            return 0;
        }
        if (ware.istAbgelaufen()) {
            return 2;
        } else {
            return 1;
        }
    }

    public double getWarenPreis() {
        if (istLeer()) {
            return 0;
        } else {
            return ware.getPreis();
        }
    }

    public boolean istLeer() {
        return !getWare().isPresent();
    }

    public boolean istVoll() {
        return !istLeer();
    }

    public void entleeren() {
        ware = null;
    }
}
