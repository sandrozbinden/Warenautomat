/* ============================================================================
 * Copyright (c) 2015 Imagic Bildverarbeitung AG, CH-8152 Glattbrugg.
 * All rights reserved.
 *
 * http://www.imagic.ch/
 * ============================================================================
 */
package criterion;

/**
 *
 * @version $Revision$
 * @version $Date$
 * @author $Author$
 * @owner Sandro Mario Zbinden
 */
public abstract class BaseCriterion<T> implements Criterion<T> {

    @Override
    public Criterion<T> and(Criterion<T> second) {
        return new ANDCriterion(this, second);
    }
}
