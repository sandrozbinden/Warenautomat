/* ============================================================================
 * Copyright (c) 2015 Imagic Bildverarbeitung AG, CH-8152 Glattbrugg.
 * All rights reserved.
 *
 * http://www.imagic.ch/
 * ============================================================================
 */
package warenautomat;

/**
 *
 * @version $Revision$
 * @version $Date$
 * @author $Author$
 * @owner Sandro Mario Zbinden
 */
public class Saldo {
    private int saldoInRappen;

    public double inFranken() {
        return Kasse.gibFranken(saldoInRappen);
    }

    public int inRappen() {
        return saldoInRappen;
    }

    public void setzteSaldo(int saldoInRappen) {
        this.saldoInRappen = saldoInRappen;
        SystemSoftware.zeigeBetragAn(inFranken());
    }
}
