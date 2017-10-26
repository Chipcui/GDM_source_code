package org.gobiiproject.gobiidtomapping.core;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.gobiiproject.gobiimodel.dto.base.DTOBaseAuditable;
import org.hibernate.type.DateType;

import java.util.Date;

@Aspect
public class DtoMapAspect {


    /***
     * The intent of this class is to set the created/modified date and user values for all
     * DTOs that derive from DTOBaseAuditable. All DtoMap* classes for tables that have
     * the created_date, modified_date, created_by, and modified_by columns should
     * derive from DtoMap<> so that the DtoMap* class's method names will conform to
     * a naming (e.g., create() for insert). They must also live in the entity.auditable
     * namespace. The JoinPoint configurations in this class will then always and systematically
     * down-cast the dto to DTOBaseAuditable and set the appropriate methods.
     * In order for this class to be invoked, the application-config.xml file must have the
     *     <aop:aspectj-autoproxy proxy-target-class="true"/>
     * annotation.
     * It would be ideal if there were a way to configure the advice so that
     * it would be invoked only when the method argument was of type DTOBaseAuditable
     * The instanceof idiom is not great OO design. However, getting this piece to work
     * at all was a challenge. Configuring the join point to trigger in this way just
     * did not want to work. The next best thing is, as we have done here, to make
     * all DtoMap* classes for the appropriate tables derive from DTOBaseAuditable and live in a
     * namespace such that the JoinPoint configuration can target the names of the methods.
     */
    @Before(value = "execution(* org.gobiiproject.gobiidtomapping.entity.auditable.*.create(*))")
    public void beforeCreate(JoinPoint joinPoint) {

        Object dtoArg = joinPoint.getArgs()[0];
        if (dtoArg instanceof DTOBaseAuditable) {
            DTOBaseAuditable dto = (DTOBaseAuditable) dtoArg;
            dto.setCreatedDate(new Date());
        }
    }

    @Before(value = "execution(* org.gobiiproject.gobiidtomapping.entity.auditable.*.replace(*,*))")
    public void beforeReplace(JoinPoint joinPoint) {

        Object dtoArg = joinPoint.getArgs()[1];
        if (dtoArg instanceof DTOBaseAuditable) {
            DTOBaseAuditable dto = (DTOBaseAuditable) dtoArg;
            dto.setModifiedDate(new Date());
        }
    }
}