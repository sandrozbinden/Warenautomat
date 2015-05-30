package warenautomat;

import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Locale;

import org.junit.Test;

public class FachTest {

    private DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.GERMAN);

    @Test
    public void testVerfallsdatumsStatusKeineWare() {
        Fach fach = new Fach(1);
        assertEquals(0, fach.getVerfallsDatumsZustand());
    }

    @Test
    public void testVerfallsdatumsStatusGueltig() throws ParseException {
        Fach fach = new Fach(1);
        fach.setWare(new Ware("", 1, df.parse("01.01.2100")));
        SystemSoftware.setzeAktuellesDatum(df.parse("01.01.2000"));
        assertEquals(1, fach.getVerfallsDatumsZustand());
    }

    @Test
    public void testVerfallsdatumsStatusAbgelaufen() throws ParseException {
        Fach fach = new Fach(1);
        fach.setWare(new Ware("", 1, df.parse("01.01.2000")));
        SystemSoftware.setzeAktuellesDatum(df.parse("01.01.2100"));
        assertEquals(2, fach.getVerfallsDatumsZustand());
    }
}
