package warenautomat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import criterion.ANDCriterion;
import criterion.AbgelaufeneWareCriterion;
import criterion.GueltigeWareCriterion;
import criterion.VerkauftNachCriterion;
import criterion.WarennameEqualsCriterion;

/**
 * Der Automat besteht aus 7 Drehtellern welche wiederum je aus 16 Fächer
 * bestehen. <br>
 * Der erste Drehteller und das jeweils erste Fach haben jeweils die Nummer 1
 * (nicht 0!). <br>
 * Im Weitern hat der Automat eine Kasse. Diese wird vom Automaten instanziert.
 */
public class Automat {

    private static final int NR_DREHTELLER = 7;
    private final Drehteller[] drehtellern = new Drehteller[NR_DREHTELLER];
    private final Kasse kassen;
    private final List<Bestellung> bestellungen = new ArrayList<Bestellung>();

    /**
     * Der Standard-Konstruktor. <br>
     * Führt die nötigen Initialisierungen durch (u.a. wird darin die Kasse
     * instanziert).
     */
    public Automat() {
        IntStream.range(0, NR_DREHTELLER).forEach(i -> drehtellern[i] = new Drehteller(i + 1));
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
        gibDrehteller(pDrehtellerNr).setzeWareInAktuellesFach(new Ware(pWarenName, pPreis, pVerfallsDatum));
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
        Arrays.stream(drehtellern).forEach(d -> d.drehen());
        SystemSoftware.dreheWarenInGui();
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
            Ware ware = aktuellesFach.getWare().get();
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
        Optional<Bestellung> bestellung = gibBestellung(warenName);
        if (bestellung.isPresent()) {
            if (gibAnzahlGueltigeWare(warenName) <= bestellung.get().getGrenze()) {
                SystemSoftware.bestellen(warenName, bestellung.get().getBestellAnzahl());
            }
        }
    }

    private int gibAnzahlGueltigeWare(String warenName) {
        return new ANDCriterion(new WarennameEqualsCriterion(warenName), new GueltigeWareCriterion()).matchCriterion(gibWaren()).size();
    }

    private Optional<Bestellung> gibBestellung(String warenName) {
        return bestellungen.stream().filter(b -> b.getWarenName().equals(warenName)).findFirst();
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
        return Kasse.gibFranken(gibGueltigeWarenWertInRappen() + gibAbgelaufenerWarenWertInRappen());
    }

    private int gibGueltigeWarenWertInRappen() {
        return gibGueltigeWarenPreiseInRappen().stream().reduce(0, (a, b) -> a + b);
    }

    private List<Integer> gibGueltigeWarenPreiseInRappen() {
        return gibGueltigeWaren().stream().map(w -> Kasse.gibRappen(w.getPreis())).collect(Collectors.toList());
    }

    private int gibAbgelaufenerWarenWertInRappen() {
        return gibAbgelaufeneWarenPreiseInRappen().stream().reduce(0, (a, b) -> a + b);
    }

    private List<Integer> gibAbgelaufeneWarenPreiseInRappen() {
        return gibAbgelaufeneWaren().stream().map(w -> Kasse.auf5RappenRunden(Kasse.gibRappen(w.getPreis() * 0.1))).collect(Collectors.toList());
    }

    private List<Ware> gibGueltigeWaren() {
        return new GueltigeWareCriterion().matchCriterion(gibWaren());
    }

    private List<Ware> gibAbgelaufeneWaren() {
        return new AbgelaufeneWareCriterion().matchCriterion(gibWaren());
    }

    private List<Ware> gibWaren() {
        return gibVolleFaecher().stream().map(f -> f.getWare().get()).collect(Collectors.toList());
    }

    private List<Fach> gibVolleFaecher() {
        return gibFaecher().stream().filter(f -> f.istVoll()).collect(Collectors.toList());
    }

    public List<Fach> gibFaecher() {
        return Arrays.stream(drehtellern).map(d -> d.gibFaecher()).collect(Collectors.toList()).stream().flatMap(l -> l.stream()).collect(Collectors.toList());
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
        return new ANDCriterion(new WarennameEqualsCriterion(pName), new VerkauftNachCriterion(pDatum)).matchCriterion(kassen.gibVerkaufteWaren()).size();
    }

}
