package warenautomat;

public class WarenFilter {

    protected String warenName;
    protected boolean mitAbgelaufenerWare;
    protected boolean mitGueltigerWare;

    public WarenFilter() {
        this(null, false, false);
    }

    protected WarenFilter(String warenName, boolean mitGueltigerWare, boolean mitAbgelaufenerWare) {
        this.warenName = warenName;
        this.mitGueltigerWare = mitGueltigerWare;
        this.mitAbgelaufenerWare = mitAbgelaufenerWare;
    }

    public WarenFilter mitWarenName(String warenName) {
        return new WarenFilter(warenName, mitGueltigerWare, mitAbgelaufenerWare);
    }

    public WarenFilter mitAbgelaufenenWaren() {
        return new WarenFilter(warenName, mitGueltigerWare, true);
    }

    public WarenFilter mitGueltigenWaren() {
        return new WarenFilter(warenName, true, mitAbgelaufenerWare);
    }

    public WarenFilter mitGueltigenUndAbgelaufennenWaren() {
        return new WarenFilter(warenName, true, true);
    }

    public String getWarenName() {
        return warenName;
    }

    public boolean istMitGueltigenWaren() {
        return mitGueltigerWare;
    }

    public boolean istMitAbelaufenerWaren() {
        return mitAbgelaufenerWare;
    }

    public boolean istSpezifischeWare() {
        return warenName != null;
    }

    public boolean acept(Ware ware) {
        boolean akzeptiert = false;
        if (istMitAbelaufenerWaren() && ware.istAbgelaufen()) {
            akzeptiert = true;
        }
        if (istMitGueltigenWaren() && !ware.istAbgelaufen()) {
            akzeptiert = true;
        }
        if (istSpezifischeWare() && !ware.getWarenName().equals(warenName)) {
            akzeptiert = false;
        }
        return akzeptiert;
    }

}
