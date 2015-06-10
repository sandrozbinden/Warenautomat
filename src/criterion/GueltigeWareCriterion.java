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
public class GueltigeWareCriterion<T extends Ware> extends BaseCriterion<T> {

    @Override
    public List<T> matchCriterion(List<T> values) {
        return values.stream().filter(w -> !w.istAbgelaufen()).collect(Collectors.toList());
    }

}
