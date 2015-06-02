package warenautomat;

import java.util.ArrayList;
import java.util.List;

/**
 * Die Kasse verwaltet das eingenommene Geld sowie das Wechselgeld. <br>
 * Die Kasse hat fünf Münz-Säulen für: <br>
 * - 10 Rappen <br>
 * - 20 Rappen <br>
 * - 50 Rappen <br>
 * - 1 Franken <br>
 * - 2 Franken <br>
 */
public class Kasse {

    private static final int MAX_MUENZSAULE = 5;
    private Muenzsaeule[] muenzsaeulen = new Muenzsaeule[MAX_MUENZSAULE];
    private double muenzFuellBetrag;
    private int muenzFuellAnzahl;
    private List<VerkaufteWare> verkaufteWare = new ArrayList<>();
    private Saldo saldo = new Saldo();

    /**
     * Standard-Konstruktor. <br>
     * Führt die nötigen Initialisierungen durch.
     */
    public Kasse() {
        muenzsaeulen[0] = new Muenzsaeule(10);
        muenzsaeulen[1] = new Muenzsaeule(20);
        muenzsaeulen[2] = new Muenzsaeule(50);
        muenzsaeulen[3] = new Muenzsaeule(100);
        muenzsaeulen[4] = new Muenzsaeule(200);
    }

    /**
     * Diese Methode wird aufgerufen nachdem das Personal beim Geldauffüllen die
     * Münzart und die Anzahl der Münzen über die Tastatur eingegeben hat (siehe
     * Use-Case "Kasse auffüllen").
     *
     * @param pMuenzenBetrag
     *            Betrag der Münzart in Franken.
     * @param pAnzahl
     *            Anzahl der neu eingelegten Münzen.
     * @return Wenn es genügend Platz in der Münzsäule hat: die Anzahl Münzen
     *         welche eingelegt werden (d.h. pAnzahl). <br>
     *         Wenn es nicht genügend Platz hat: die Anzahl Münzen welche nicht
     *         Platz haben werden, als negative Zahl (z.B. -20). <br>
     *         Wenn ein nicht unterstützter Münzbetrag übergeben wurde: -200
     */
    public int fuelleKasse(double pMuenzenBetrag, int pAnzahl) {
        muenzFuellBetrag = pMuenzenBetrag;
        muenzFuellAnzahl = 0;
        int muenzBetragInRappen = gibRappen(pMuenzenBetrag);
        if (!istMuenzAuffuellBetragUnterstuetzt(muenzBetragInRappen)) {
            return -200;
        }
        Muenzsaeule muenzsaeule = gibMuenzsaeule(pMuenzenBetrag);
        int freierMuenzPlatz = muenzsaeule.getFreierMuenzPlatz();
        if (pAnzahl > freierMuenzPlatz) {
            muenzFuellAnzahl = freierMuenzPlatz;
            return freierMuenzPlatz - pAnzahl;
        } else {
            muenzFuellAnzahl = pAnzahl;
            return pAnzahl;
        }

    }

    private boolean istMuenzAuffuellBetragUnterstuetzt(int muenzBetragInRappen) {
        for (Muenzsaeule muenzsaeule : muenzsaeulen) {
            if (muenzsaeule.getMuenzartInRappen() == muenzBetragInRappen) {
                return true;
            }
        }
        return false;
    }

    /**
     * Diese Methode wird aufgerufen nachdem das Personal beim Geldauffüllen den
     * Knopf "Bestätigen" gedrückt hat. (siehe Use-Case "Kasse auffüllen"). <br>
     * Verbucht die Münzen gemäss dem vorangegangenen Aufruf der Methode
     * <code> fuelleKasse() </code>.
     */
    public void fuelleKasseBestaetigung() {
        Muenzsaeule muenzsaeule = gibMuenzsaeule(muenzFuellBetrag);
        muenzsaeule.muenzeHinzufuegen(muenzFuellAnzahl);
    }

    /**
     * Diese Methode wird aufgerufen wenn ein Kunde eine Münze eingeworfen hat. <br>
     * Führt den eingenommenen Betrag entsprechend nach. <br>
     * Stellt den nach dem Einwerfen vorhandenen Betrag im Kassen-Display dar. <br>
     * Eingenommenes Geld steht sofort als Wechselgeld zur Verfügung. <br>
     * Die Münzen werden von der Hardware-Kasse auf Falschgeld, Fremdwährung und
     * nicht unterstützte Münzarten geprüft, d.h. diese Methode wird nur
     * aufgerufen wenn ein Münzeinwurf soweit erfolgreich war. <br>
     * Ist die Münzsäule voll (d.h. 100 Münzen waren vor dem Einwurf bereits
     * darin enthalten), so wird mittels
     * <code> SystemSoftwareWrapper.auswerfenWechselGeld() </code> unmittelbar
     * ein entsprechender Münz-Auswurf ausgeführt. <br>
     * Hinweis: eine Hardware-Münzsäule hat jeweils effektiv Platz für 101
     * Münzen.
     *
     * @param pMuenzenBetragFranken
     *            Der Betrag der neu eingeworfenen Münze in Franken.
     * @return <code> true </code>, wenn er Einwurf erfolgreich war. <br>
     *         <code> false </code>, wenn Münzsäule bereits voll war.
     */
    public boolean einnehmen(double pMuenzenBetragFranken) {
        Muenzsaeule muenzsaeule = gibMuenzsaeule(pMuenzenBetragFranken);
        if (muenzsaeule.istVoll()) {
            return false;
        } else {
            muenzsaeule.muenzeHinzufuegen(1);
            saldo.setzteSaldo(saldo.inRappen() + gibRappen(pMuenzenBetragFranken));
            return true;
        }
    }

    public static int gibRappen(double betragInFranken) {
        return (int) Math.round(betragInFranken * 100);
    }

    public static double gibFranken(int betragInRappen) {
        return betragInRappen / 100.0;
    }

    public Muenzsaeule gibMuenzsaeule(double muenzArtFranken) {
        int muenzArtRappen = gibRappen(muenzArtFranken);
        if (muenzArtRappen == 10) {
            return muenzsaeulen[0];
        } else if (muenzArtRappen == 20) {
            return muenzsaeulen[1];
        } else if (muenzArtRappen == 50) {
            return muenzsaeulen[2];
        } else if (muenzArtRappen == 100) {
            return muenzsaeulen[3];
        } else if (muenzArtRappen == 200) {
            return muenzsaeulen[4];
        }
        throw new RuntimeException("Finde keine Muenzsaeule fuer muenzArt: " + muenzArtFranken);
    }

    /**
     * Bewirkt den Auswurf des Restbetrages.
     */
    public void gibWechselGeld() {
        if (istWechselGeldVorhanden(saldo.inFranken())) {
            for (int i = muenzsaeulen.length - 1; i >= 0; i--) {
                Muenzsaeule muenzsaeule = muenzsaeulen[i];
                int ausgeworfeneMuenze = 0;
                int saldoInRappen = saldo.inRappen();
                while (saldoInRappen >= muenzsaeule.getMuenzartInRappen() && muenzsaeule.getMuenzAnzahl() > 0) {
                    saldoInRappen = saldoInRappen - muenzsaeule.getMuenzartInRappen();
                    muenzsaeule.muenzeAuswerfen();
                    ausgeworfeneMuenze = ausgeworfeneMuenze + 1;
                }
            }
            saldo.setzteSaldo(0);
        }

    }

    /**
     * Gibt den Gesamtbetrag der bisher verkauften Waren zurück. <br>
     * Analyse: Abgeleitetes Attribut.
     *
     * @return Gesamtbetrag der bisher verkauften Waren.
     */
    public double gibBetragVerkaufteWaren() {
        double betragVerkaufteWare = 0;
        for (VerkaufteWare verkaufteWaren : verkaufteWare) {
            betragVerkaufteWare = betragVerkaufteWare + verkaufteWaren.getPreis();
        }
        return betragVerkaufteWare;
    }

    public int gibSaldoInRappen() {
        return saldo.inRappen();
    }

    public boolean istSaldoVorhanden(double preis) {
        return saldo.inRappen() >= gibRappen(preis);
    }

    public boolean istWechselGeldVorhanden(double preis) {
        int preisInRappen = gibRappen(preis);
        for (int i = muenzsaeulen.length - 1; i >= 0; i--) {
            Muenzsaeule muenzsaeule = muenzsaeulen[i];
            int ausgeworfeneMuenze = 0;
            while (preisInRappen >= muenzsaeule.getMuenzartInRappen() && muenzsaeule.getMuenzAnzahl() > 0) {
                preisInRappen = preisInRappen - muenzsaeule.getMuenzartInRappen();
                muenzsaeule.muenzeAuswerfen();
                ausgeworfeneMuenze = ausgeworfeneMuenze + 1;
            }
            muenzsaeule.muenzeHinzufuegen(ausgeworfeneMuenze);
        }
        return preisInRappen == 0;
    }

    public void kaufen(Ware ware) {
        saldo.setzteSaldo(saldo.inRappen() - gibRappen(ware.getPreis()));
        verkaufteWare.add(new VerkaufteWare(ware.getWarenName(), ware.getPreis(), ware.getVerfallsDatum(), SystemSoftware.gibAktuellesDatum()));
    }

    public List<VerkaufteWare> gibVerkaufteWare() {
        return verkaufteWare;
    }

    public static int auf5RappenRunden(int rappen) {
        if (rappen % 5 == 0) {
            return rappen;
        } else {
            return rappen + (5 - rappen % 5);
        }
    }

}
