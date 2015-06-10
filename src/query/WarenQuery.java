/* ============================================================================
 * Copyright (c) 2015 Imagic Bildverarbeitung AG, CH-8152 Glattbrugg.
 * All rights reserved.
 *
 * http://www.imagic.ch/
 * ============================================================================
 */
package query;

import java.util.List;

import warenautomat.Ware;
import criterion.ANDCriterion;
import criterion.Criterion;

/**
 *
 * @version $Revision$
 * @version $Date$
 * @author $Author$
 * @owner Sandro Mario Zbinden
 */
public class WarenQuery<T extends Ware> {

    public static WarennameCritionBuilder WARENNAME = new WarennameCritionBuilder();
    public static WarenCritionBuilder WARE = new WarenCritionBuilder();

    private Criterion<T> criterion;

    public WarenQuery(Criterion<T> criterion) {
        this.criterion = criterion;
    }

    public static <T extends Ware> WarenQuery<T> where(Criterion<T> criterion) {
        return new WarenQuery(criterion);
    }

    public WarenQuery<T> and(Criterion<T> andCriterion) {
        this.criterion = new ANDCriterion(criterion, andCriterion);
        return this;
    }

    public List<T> execute(List<T> values) {
        return criterion.matchCriterion(values);
    }

}
