/* ============================================================================
 * Copyright (c) 2015 Imagic Bildverarbeitung AG, CH-8152 Glattbrugg.
 * All rights reserved.
 *
 * http://www.imagic.ch/
 * ============================================================================
 */
package criterion;

import java.util.List;

/**
 *
 * @version $Revision$
 * @version $Date$
 * @author $Author$
 * @owner Sandro Mario Zbinden
 */
public class ANDCriterion<T> extends BaseCriterion<T> {

    private Criterion<T> first;
    private Criterion<T> second;

    public ANDCriterion(Criterion<T> first, Criterion<T> second) {
        this.irst = first;
        this.second = second;
    }

    @Override
    public List<T> matchCriterion(List<T> values) {
        return second.matchCriterion(first.matchCriterion(values));
    }
}
