/* ============================================================================
 * Copyright (c) 2015 Imagic Bildverarbeitung AG, CH-8152 Glattbrugg.
 * All rights reserved.
 *
 * http://www.imagic.ch/
 * ============================================================================
 */
package criterion;

import java.util.List;
import java.util.stream.Collectors;

import warenautomat.Ware;

/**
 *
 * @version $Revision$
 * @version $Date$
 * @author $Author$
 * @owner Sandro Mario Zbinden
 */
public class GueltigeWareCriterion implements Criterion<Ware> {

    @Override
    public List<Ware> matchCriterion(List<Ware> values) {
        return values.stream().filter(w -> !w.istAbgelaufen()).collect(Collectors.toList());
    }

}
