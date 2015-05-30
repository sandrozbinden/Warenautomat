package warenautomat;

import java.util.Date;

public class VerkaufteWare extends Ware {

    private Date verkaufsDatum;

    public VerkaufteWare(String warenName, double preis, Date verfallsDatum, Date verkaufsDatum) {
        super(warenName, preis, verfallsDatum);
        this.verkaufsDatum = verkaufsDatum;
    }

    public Date getVerkaufsDatum() {
        return verkaufsDatum;
    }

}
