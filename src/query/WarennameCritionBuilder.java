/* ============================================================================
 * Copyright (c) 2015 Imagic Bildverarbeitung AG, CH-8152 Glattbrugg.
 * All rights reserved.
 *
 * http://www.imagic.ch/
 * ============================================================================
 */
package query;

import warenautomat.Ware;
import criterion.Criterion;
import criterion.WarennameEqualsCriterion;

/**
 *
 * @version $Revision$
 * @version $Date$
 * @author $Author$
 * @owner Sandro Mario Zbinden
 */
public class WarennameCritionBuilder<T extends Ware> {

    public Criterion<T> equal(String name) {
        return new WarennameEqualsCriterion(name);
    }
}
