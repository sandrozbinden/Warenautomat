/* ============================================================================
 * Copyright (c) 2015 Imagic Bildverarbeitung AG, CH-8152 Glattbrugg.
 * All rights reserved.
 *
 * http://www.imagic.ch/
 * ============================================================================
 */
package query;

import java.util.Date;

import warenautomat.Ware;
import criterion.AbgelaufeneWareCriterion;
import criterion.Criterion;
import criterion.GueltigeWareCriterion;
import criterion.VerkauftNachCriterion;

/**
 *
 * @version $Revision$
 * @version $Date$
 * @author $Author$
 * @owner Sandro Mario Zbinden
 */
public class WarenCritionBuilder {

    public Criterion istGueltig() {
        return new GueltigeWareCriterion<Ware>();
    }

    public Criterion istAbgelaufen() {
        return new AbgelaufeneWareCriterion();
    }

    public Criterion istVerkauftNach(Date verkauftNachDatum) {
        return new VerkauftNachCriterion(verkauftNachDatum);
    }

}
