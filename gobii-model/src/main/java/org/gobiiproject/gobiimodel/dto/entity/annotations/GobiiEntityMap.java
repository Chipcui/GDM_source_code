package org.gobiiproject.gobiimodel.dto.entity.annotations;

import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.types.GobiiCvGroupType;

import java.lang.annotation.*;

/**
 * Annotation to Map DTO Object to Database Entity.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GobiiEntityMap {

    String paramName();

    Class entity() default void.class;

    boolean deep() default false;

    boolean base() default false;

}
