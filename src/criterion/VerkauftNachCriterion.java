/* ============================================================================
 * Copyright (c) 2015 Imagic Bildverarbeitung AG, CH-8152 Glattbrugg.
 * All rights reserved.
 *
 * http://www.imagic.ch/
 * ============================================================================
 */
package criterion;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import warenautomat.VerkaufteWare;

/**
 *
 * @version $Revision$
 * @version $Date$
 * @author $Author$
 * @owner Sandro Mario Zbinden
 */
public class VerkauftNachCriterion implements Criterion<VerkaufteWare> {

    private Date verkauftNachDatum;

    public VerkauftNachCriterion(Date verkauftNachDatum) {
        this.verkauftNachDatum = verkauftNachDatum;
    }

    @Override
    public List<VerkaufteWare> matchCriterion(List<VerkaufteWare> values) {
        return values.stream().filter(w -> w.getVerkaufsDatum().getTime() >= verkauftNachDatum.getTime()).collect(Collectors.toList());
    }

}
