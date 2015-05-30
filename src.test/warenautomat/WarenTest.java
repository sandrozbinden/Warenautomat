package warenautomat;

import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Locale;

import org.junit.Test;

public class WarenTest {

    private DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.GERMAN);

    @Test
    public void wareAbgelaufenSelberTag() throws ParseException {
        SystemSoftware.setzeAktuellesDatum(df.parse("01.01.2001"));
        Ware ware = new Ware("", 1, df.parse("01.01.2001"));
        assertTrue(ware.istAbgelaufen());
    }
}
