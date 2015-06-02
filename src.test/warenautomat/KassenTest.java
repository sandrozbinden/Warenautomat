package warenautomat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

public class KassenTest {

    private Kasse kasse;
    private DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.GERMAN);

    @Before
    public void init() {
        kasse = new Kasse();
    }

    @Test
    public void testeMuenzsaeulen() {
        assertEquals(10, kasse.gibMuenzsaeule(0.1).getMuenzartInRappen());
        assertEquals(20, kasse.gibMuenzsaeule(0.2).getMuenzartInRappen());
        assertEquals(50, kasse.gibMuenzsaeule(0.5).getMuenzartInRappen());
        assertEquals(100, kasse.gibMuenzsaeule(1).getMuenzartInRappen());
        assertEquals(200, kasse.gibMuenzsaeule(2).getMuenzartInRappen());
    }

    @Test
    public void einnehmen() {
        assertTrue(kasse.einnehmen(0.5));
        assertEquals(50, kasse.gibSaldoInRappen(), 3);
    }

    @Test
    public void einnehmenNichtMoeglich() {
        for (int i = 0; i < 100; i++) {
            assertTrue(kasse.einnehmen(0.5));
        }
        assertFalse(kasse.einnehmen(0.5));
        assertEquals(5000, kasse.gibSaldoInRappen(), 3);
    }

    @Test
    public void fuelleKasse() {
        assertEquals(10, kasse.fuelleKasse(0.5, 10));
        kasse.fuelleKasseBestaetigung();
        assertEquals(80, kasse.fuelleKasse(0.5, 80));
        kasse.fuelleKasseBestaetigung();
        assertEquals(-20, kasse.fuelleKasse(0.5, 30));
        kasse.fuelleKasseBestaetigung();
        assertEquals(100, kasse.gibMuenzsaeule(0.5).getMuenzAnzahl());
    }

    @Test
    public void gibWechselGeld() {
        kasse.einnehmen(1.00);
        kasse.einnehmen(2.00);
        kasse.gibWechselGeld();
        assertEquals(0, kasse.gibSaldoInRappen());
    }

    @Test
    public void gibWechselGeld1() throws ParseException {
        assertEquals(10, kasse.fuelleKasse(0.5, 10));
        kasse.fuelleKasseBestaetigung();
        kasse.einnehmen(2.00);
        kasse.kaufen(new Ware("", 1.00, df.parse("01.01.2010")));
        kasse.gibWechselGeld();
        assertEquals(0, kasse.gibSaldoInRappen());
    }

    @Test
    public void ungueltigerEingang() throws ParseException {
        assertEquals(-200, kasse.fuelleKasse(3, 10));
    }

    @Test
    public void gibWechselGeld2() throws ParseException {
        kasse.fuelleKasse(0.5, 1);
        kasse.fuelleKasseBestaetigung();
        kasse.fuelleKasse(0.2, 2);
        kasse.fuelleKasseBestaetigung();
        kasse.fuelleKasse(0.1, 1);
        kasse.fuelleKasseBestaetigung();
        kasse.einnehmen(2.00);
        kasse.kaufen(new Ware("", 1.00, df.parse("01.01.2010")));
        kasse.gibWechselGeld();
        assertEquals(0, kasse.gibSaldoInRappen());
    }

    @Test
    public void gibWechselGeld3() throws ParseException {
        kasse.fuelleKasse(0.5, 1);
        kasse.fuelleKasseBestaetigung();
        kasse.fuelleKasse(0.2, 3);
        kasse.fuelleKasseBestaetigung();
        kasse.fuelleKasseBestaetigung();
        kasse.einnehmen(2.00);
        kasse.kaufen(new Ware("", 1.00, df.parse("01.01.2010")));
        kasse.gibWechselGeld();
        assertEquals(100, kasse.gibSaldoInRappen());
    }

    @Test
    public void verkaufteWareTest() throws ParseException {
        kasse.einnehmen(1.00);
        kasse.kaufen(new Ware("", 1.00, df.parse("01.01.2010")));
        kasse.einnehmen(2.00);
        kasse.kaufen(new Ware("", 2.00, df.parse("01.01.2010")));
        assertEquals(300, Kasse.gibRappen(kasse.gibBetragVerkaufteWaren()));
    }

    @Test
    public void rundenTest() {
        assertEquals(5, Kasse.auf5RappenRunden(1));
        assertEquals(5, Kasse.auf5RappenRunden(2));
        assertEquals(5, Kasse.auf5RappenRunden(3));
        assertEquals(5, Kasse.auf5RappenRunden(4));
        assertEquals(5, Kasse.auf5RappenRunden(5));
        assertEquals(10, Kasse.auf5RappenRunden(6));
        assertEquals(10, Kasse.auf5RappenRunden(7));
    }

    @Test
    public void kaufMaennischRundenTest() {
        assertEquals(1, Kasse.gibRappen(0.005));
        assertEquals(11, Kasse.gibRappen(0.105));
        assertEquals(101, Kasse.gibRappen(1.01));
    }
}
