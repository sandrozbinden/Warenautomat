package warenautomat;

import java.util.Date;

public class VerkaufteWarenFilter extends WarenFilter {

    private Date verkauftNachDatum;

    public VerkaufteWarenFilter() {
        super(null, false, false);
    }

    protected VerkaufteWarenFilter(String warenName, Date verkauftNachDatum) {
        super(warenName, true, true);
        this.verkauftNachDatum = verkauftNachDatum;
    }

    @Override
    public VerkaufteWarenFilter mitWarenName(String warenName) {
        return new VerkaufteWarenFilter(warenName, verkauftNachDatum);
    }

    public VerkaufteWarenFilter mitWarenVerkauftNach(Date verkauftNachDatum) {
        return new VerkaufteWarenFilter(warenName, verkauftNachDatum);
    }

    public boolean acept(VerkaufteWare ware) {
        if (!super.acept(ware)) {
            return false;
        }
        if (verkauftNachDatum != null) {
            return ware.getVerkaufsDatum().getTime() >= verkauftNachDatum.getTime();
        } else {
            return true;
        }
    }
}
