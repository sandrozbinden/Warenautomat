package warenautomat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Der Automat besteht aus 7 Drehtellern welche wiederum je aus 16 Fächer
 * bestehen. <br>
 * Der erste Drehteller und das jeweils erste Fach haben jeweils die Nummer 1
 * (nicht 0!). <br>
 * Im Weitern hat der Automat eine Kasse. Diese wird vom Automaten instanziert.
 */
public class Automat {

    private static final int NR_DREHTELLER = 7;
    private Drehteller[] drehtellern = new Drehteller[NR_DREHTELLER];
    private Kasse kassen;
    private List<Bestellung> bestellungen = new ArrayList<Bestellung>();

    /**
     * Der Standard-Konstruktor. <br>
     * Führt die nötigen Initialisierungen durch (u.a. wird darin die Kasse
     * instanziert).
     */
    public Automat() {
        for (int i = 0; i < NR_DREHTELLER; i++) {
            drehtellern[i] = new Drehteller(i + 1);
        }
        kassen = new Kasse();
    }

    /**
     * Füllt ein Fach mit Ware. <br>
     * Wenn das Service-Personal den Automaten füllt, wird mit einem
     * Bar-Code-Leser zuerst die Ware gescannt. <br>
     * Daraufhin wird die Schiebe-Tür geöffnet. <br>
     * Das Service-Personal legt die neue Ware ins Fach und schliesst das Fach. <br>
     * Die Hardware resp. System-Software ruft die Methode
     * <code> Automat.fuelleFach() </code> auf.
     *
     * @param pDrehtellerNr
     *            Der Drehteller bei welchem das Fach hinter der Schiebe-Türe
     *            gefüllt wird. <br>
     *            Nummerierung beginnt mit 1 (nicht 0)!
     * @param pWarenName
     *            Der Name der neuen Ware.
     * @param pPreis
     *            Der Preis der neuen Ware.
     * @param pVerfallsDatum
     *            Das Verfallsdatum der neuen Ware.
     */
    public void fuelleFach(int pDrehtellerNr, String pWarenName, double pPreis, Date pVerfallsDatum) {
        Drehteller drehteller = gibDrehteller(pDrehtellerNr);
        drehteller.setzeWareInAktuellesFach(new Ware(pWarenName, pPreis, pVerfallsDatum));
        drehteller.aktualisiereDrehtellerAnzeige();
    }

    private Drehteller gibDrehteller(int pDrehtellerNr) {
        return drehtellern[pDrehtellerNr - 1];
    }

    /**
     * Gibt die Objekt-Referenz auf die <em> Kasse </em> zurück.
     */
    public Kasse gibKasse() {
        return kassen;
    }

    /**
     * Wird von der System-Software jedesmal aufgerufen wenn der gelbe
     * Dreh-Knopf gedrückt wird. <br>
     * Die Applikations-Software führt die Drehteller-Anzeigen nach (Warenpreis,
     * Verfallsdatum). <br>
     * Das Ansteuern des Drehteller-Motors übernimmt hat die System-Software
     * (muss nicht von der Applikations-Software gesteuert werden.). <br>
     * Die System-Software stellt sicher, dass <em> drehen </em> nicht
     * durchgeführt wird wenn ein Fach offen ist.
     */
    public void drehen() {
        SystemSoftware.dreheWarenInGui();
        for (Drehteller drehteller : drehtellern) {
            drehteller.drehen();
        }
    }

    public Fach gibFach(int drehtellerPosition) {
        return gibDrehteller(drehtellerPosition).getAktuellesFach();
    }

    /**
     * Beim Versuch eine Schiebetüre zu öffnen ruft die System-Software die
     * Methode <code> oeffnen() </code> der Klasse <em> Automat </em> mit der
     * Drehteller-Nummer als Parameter auf. <br>
     * Es wird überprüft ob alles o.k. ist: <br>
     * - Fach nicht leer <br>
     * - Verfallsdatum noch nicht erreicht <br>
     * - genug Geld eingeworfen <br>
     * - genug Wechselgeld vorhanden <br>
     * Wenn nicht genug Geld eingeworfen wurde, wird dies mit
     * <code> SystemSoftware.zeigeZuWenigGeldAn() </code> signalisiert. <br>
     * Wenn nicht genug Wechselgeld vorhanden ist wird dies mit
     * <code> SystemSoftware.zeigeZuWenigWechselGeldAn() </code> signalisiert. <br>
     * Wenn o.k. wird entriegelt ( <code> SystemSoftware.entriegeln() </code>)
     * und positives Resultat zurückgegeben, sonst negatives Resultat. <br>
     * Es wird von der System-Software sichergestellt, dass zu einem bestimmten
     * Zeitpunkt nur eine Schiebetüre offen sein kann.
     *
     * @param pDrehtellerNr
     *            Der Drehteller bei welchem versucht wird die Schiebe-Türe zu
     *            öffnen. <br>
     *            Nummerierung beginnt mit 1 (nicht 0)!
     * @return Wenn alles o.k. <code> true </code>, sonst <code> false </code>.
     */
    public boolean oeffnen(int pDrehtellerNr) {
        Fach aktuellesFach = gibDrehteller(pDrehtellerNr).getAktuellesFach();
        if (aktuellesFach.istLeer()) {
            return false;
        } else {
            Ware ware = aktuellesFach.getWare();
            if (ware.istAbgelaufen()) {
                return false;
            } else if (!kassen.istSaldoVorhanden(ware.getPreis())) {
                SystemSoftware.zeigeZuWenigGeldAn();
                return false;
            } else if (!kassen.istWechselGeldVorhanden(ware.getPreis())) {
                SystemSoftware.zeigeZuWenigWechselGeldAn();
                return false;
            } else {
                SystemSoftware.entriegeln(pDrehtellerNr);
                aktuellesFach.entleeren();
                kassen.kaufen(ware);
                wareBestellen(ware.getWarenName());
                return true;
            }
        }
    }

    private void wareBestellen(String warenName) {
        Bestellung bestellung = gibBestellung(warenName);
        if (bestellung != null) {
            if (gibAnzahlGueltigeWare(warenName) <= bestellung.getGrenze()) {
                SystemSoftware.bestellen(warenName, bestellung.getBestellAnzahl());
            }
        }
    }

    private int gibAnzahlGueltigeWare(String warenName) {
        return gibWaren(new WarenFilter().mitWarenName(warenName).mitGueltigenWaren()).size();
    }

    private Bestellung gibBestellung(String warenName) {
        Bestellung aktuelleBestellung = null;
        for (Bestellung bestellung : bestellungen) {
            if (bestellung.getWarenName().equals(warenName)) {
                aktuelleBestellung = bestellung;
            }
        }
        return aktuelleBestellung;
    }

    /**
     * Konfiguration einer automatischen Bestellung. <br>
     * Der Automat setzt automatisch Bestellungen ab mittels
     * <code> SystemSoftware.bestellen() </code> wenn eine Ware ausgeht.
     * 
     * @param pWarenName
     *            Warenname derjenigen Ware, für welche eine automatische
     *            Bestellung konfiguriert wird.
     * @param pGrenze
     *            Grenze bei welcher Anzahl von verbleibenden, nicht
     *            abgelaufener Waren im Automat eine Bestellung abgesetzt werden
     *            soll (Bestellung wenn Waren-Anzahl nicht abgelaufener Waren <=
     *            pGrenze).
     * @param pBestellAnzahl
     *            Wieviele neue Waren jeweils bestellt werden sollen.
     */
    public void konfiguriereBestellung(String pWarenName, int pGrenze, int pBestellAnzahl) {
        bestellungen.add(new Bestellung(pWarenName, pGrenze, pBestellAnzahl));
    }

    /**
     * Gibt den aktuellen Wert aller im Automaten enthaltenen Waren in Franken
     * zurück. <br>
     * Analyse: <br>
     * Abgeleitetes Attribut. <br>
     *
     * @return Der totale Warenwert des Automaten.
     */
    public double gibTotalenWarenWert() {
        int totalerWarenWertRappen = 0;
        for (Ware gueltigeWare : gibGueltigeWaren()) {
            totalerWarenWertRappen = totalerWarenWertRappen + Kasse.gibRappen(gueltigeWare.getPreis());
        }
        for (Ware abgelaufeneWare : gibAbgelaufeneWaren()) {
            int zehnProzentInRappen = Kasse.gibRappen(abgelaufeneWare.getPreis() * 0.1);
            totalerWarenWertRappen = totalerWarenWertRappen + Kasse.auf5RappenRunden(zehnProzentInRappen);
        }
        return Kasse.gibFranken(totalerWarenWertRappen);
    }

    private List<Ware> gibGueltigeWaren() {
        return gibWaren(new WarenFilter().mitGueltigenWaren());
    }

    private List<Ware> gibAbgelaufeneWaren() {
        return gibWaren(new WarenFilter().mitAbgelaufenenWaren());
    }

    private List<Ware> gibWaren(WarenFilter filter) {
        List<Ware> waren = new ArrayList<Ware>();
        for (Ware ware : gibWaren()) {
            if (filter.acept(ware)) {
                waren.add(ware);
            }
        }
        return waren;
    }

    private List<Ware> gibWaren() {

        List<Ware> waren = new ArrayList<Ware>();
        for (Fach fach : gibFaecher()) {
            if (fach.istVoll()) {
                waren.add(fach.getWare());
            }
        }
        return waren;
    }

    public List<Fach> gibFaecher() {
        List<Fach> faecher = new ArrayList<Fach>();
        for (Drehteller drehteller : drehtellern) {
            for (Fach fach : drehteller.gibFaecher()) {
                faecher.add(fach);
            }
        }
        return faecher;
    }

    /**
     * Gibt die Anzahl der verkauften Ware <em> pName </em> seit (>=)
     * <em> pDatum </em> Zahl zurück.
     *
     * @param pName
     *            Der Name der Ware nach welcher gesucht werden soll.
     * @param pDatum
     *            Das Datum seit welchem gesucht werden soll.
     * @return Anzahl verkaufter Waren.
     */
    public int gibVerkaufsStatistik(String pName, Date pDatum) {
        return kassen.gibVerkaufteWare(new VerkaufteWarenFilter().mitWarenName(pName).mitWarenVerkauftNach(pDatum)).size();
    }

}