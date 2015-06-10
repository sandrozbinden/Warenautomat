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
public class WarennameEqualsCriterion<T extends Ware> extends BaseCriterion<T> {

    private final String warenname;

    public WarennameEqualsCriterion(String warenname) {
        this.warenname = warenname;
    }

    @Override
    public List<T> matchCriterion(List<T> values) {
        return values.stream().filter(w -> w.getWarenName().equalsIgnoreCase(warenname)).collect(Collectors.toList());
    }

}
