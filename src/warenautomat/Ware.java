package warenautomat;

import java.util.Date;

public class Ware {

    private String warenName;
    private double preis;
    private Date verfallsDatum;

    public Ware(String warenName, double preis, Date verfallsDatum) {
        this.warenName = warenName;
        this.preis = preis;
        this.verfallsDatum = verfallsDatum;
    }

    public String getWarenName() {
        return warenName;
    }

    public double getPreis() {
        return preis;
    }

    public Date getVerfallsDatum() {
        return verfallsDatum;
    }

    public boolean istAbgelaufen() {
        return verfallsDatum.getTime() <= SystemSoftware.gibAktuellesDatum().getTime();
    }
}
