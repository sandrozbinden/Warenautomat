package warenautomat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

public class AutomatTest {

    private DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.GERMAN);
    private Automat automat;

    @Before
    public void init() {
        automat = new Automat();
    }

    @Test
    public void fuelleFachTest() throws ParseException {
        automat.fuelleFach(1, "Eins", 1.00, df.parse("01.01.2001"));
        Fach fach = automat.gibFach(1);
        assertEquals("Eins", fach.getWare().getWarenName());
        assertEquals(1.00, fach.getWare().getPreis(), 3);
        assertEquals("01.01.2001", df.format(fach.getWare().getVerfallsDatum()));
    }

    @Test
    public void fuelleFachLastDrehteller() throws ParseException {
        automat.fuelleFach(7, "Eins", 1.00, df.parse("01.01.2001"));
        Fach fach = automat.gibFach(7);
        assertEquals("Eins", fach.getWare().getWarenName());
        assertEquals(1.00, fach.getWare().getPreis(), 3);
        assertEquals("01.01.2001", df.format(fach.getWare().getVerfallsDatum()));
    }

    @Test(expected = RuntimeException.class)
    public void fuelleFachTestToHighDrehteller() throws ParseException {
        automat.fuelleFach(8, "Eins", 1.00, df.parse("01.01.2001"));
    }

    @Test(expected = RuntimeException.class)
    public void fuelleFachTestInvalidDrehteller() throws ParseException {
        automat.fuelleFach(0, "Eins", 1.00, df.parse("01.01.2001"));
    }

    @Test
    public void simpleDrehen() throws ParseException {
        automat.fuelleFach(1, "Eins", 1.00, df.parse("01.01.2001"));
        for (int i = 0; i < 16; i++) {
            automat.drehen();
        }
        Fach fach = automat.gibFach(1);
        assertEquals("Eins", fach.getWare().getWarenName());
        assertEquals(1.00, fach.getWare().getPreis(), 3);
        assertEquals("01.01.2001", df.format(fach.getWare().getVerfallsDatum()));
    }

    @Test
    public void testDrehtellerPreis() throws ParseException {
        SystemSoftware.setzeAktuellesDatum(df.parse("01.01.2000"));
        automat.fuelleFach(1, "Eins", 1.00, df.parse("01.01.2100"));
    }

    @Test
    public void testDrehtellerAbgelaufen() throws ParseException {
        SystemSoftware.setzeAktuellesDatum(df.parse("01.01.2100"));
        automat.fuelleFach(1, "Eins", 1.00, df.parse("01.01.2000"));
    }

    @Test
    public void testOeffnenLeeresFach() {
        assertFalse(automat.oeffnen(1));
    }

    @Test
    public void testOeffnenWareAbgelaufen() throws ParseException {
        SystemSoftware.setzeAktuellesDatum(df.parse("01.01.2000"));
        automat.fuelleFach(1, "Eins", 1.00, df.parse("01.01.1999"));
        assertFalse(automat.oeffnen(1));
    }

    @Test
    public void testOeffnenKeinGeld() throws ParseException {
        SystemSoftware.setzeAktuellesDatum(df.parse("01.01.2000"));
        automat.fuelleFach(1, "Eins", 1.00, df.parse("01.01.2100"));
        automat.gibKasse().einnehmen(0.5);
        assertFalse(automat.oeffnen(1));
    }

    @Test
    public void testOeffnenKeinWechselGeldBenoetigt() throws ParseException {
        SystemSoftware.setzeAktuellesDatum(df.parse("01.01.2000"));
        automat.fuelleFach(1, "Eins", 1.00, df.parse("01.01.2100"));
        automat.gibKasse().einnehmen(0.5);
        automat.gibKasse().einnehmen(0.5);
        assertTrue(automat.oeffnen(1));
    }

    @Test
    public void testOeffnenKeinWechselGeld() throws ParseException {
        SystemSoftware.setzeAktuellesDatum(df.parse("01.01.2000"));
        automat.fuelleFach(1, "Eins", 1.00, df.parse("01.01.2100"));
        automat.gibKasse().einnehmen(0.5);
        automat.gibKasse().einnehmen(0.2);
        automat.gibKasse().einnehmen(0.2);
        automat.gibKasse().einnehmen(0.2);
        assertFalse(automat.oeffnen(1));
    }

    @Test
    public void testKaufeGleichesFach() throws ParseException {
        SystemSoftware.setzeAktuellesDatum(df.parse("01.01.2000"));
        automat.fuelleFach(1, "Eins", 1.00, df.parse("01.01.2010"));
        assertEquals(100, Kasse.gibRappen(automat.gibTotalenWarenWert()));
        automat.gibKasse().einnehmen(1.00);
        automat.gibKasse().einnehmen(1.00);
        assertTrue(automat.oeffnen(1));
        assertFalse(automat.oeffnen(1));
    }

    @Test
    public void testWarenwert() throws ParseException {
        automat.fuelleFach(1, "Eins", 1.00, df.parse("01.01.1999"));
        automat.fuelleFach(2, "Zwei", 2.00, df.parse("01.01.1999"));
        automat.fuelleFach(3, "DreiEinhalb", 3.50, df.parse("01.01.1999"));
        assertEquals(650, Kasse.gibRappen(automat.gibTotalenWarenWert()));
        automat.drehen();
        automat.fuelleFach(1, "Eins", 1.00, df.parse("01.01.1999"));
        automat.fuelleFach(4, "Vier", 4.00, df.parse("01.01.1999"));
        assertEquals(1150, Kasse.gibRappen(automat.gibTotalenWarenWert()));
    }

}
