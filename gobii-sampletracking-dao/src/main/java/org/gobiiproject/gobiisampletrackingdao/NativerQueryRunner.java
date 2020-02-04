package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.QueryField;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.IntegerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NativerQueryRunner {

     Logger LOGGER = LoggerFactory.getLogger(DatasetDao.class);

     public NativerQueryRunner() {}

     public NativerQueryRunner(EntityManager em) { this.em = em;}

    @PersistenceContext
    protected EntityManager em;

    public static NativerQueryRunner getInstance() {
        return new NativerQueryRunner();
    }

    @Transactional
    public List<Object[]> run(String queryString,
                              List<QueryField> queryParamters,
                              List<QueryField> entityFields,
                              List<QueryField> scalarFields) {

            try {

                Session session = em.unwrap(Session.class);

                List<Object[]> resultTuplesList = new ArrayList<>();

                NativeQuery nativeQuery = session
                        .createNativeQuery(queryString);

                for (QueryField queryParameter : queryParamters) {
                    nativeQuery.setParameter(queryParameter.getParameterName(),
                            queryParameter.getParameterValue(), queryParameter.getParamterType());
                }

                for (QueryField entityAlias : entityFields) {
                    nativeQuery.addEntity(entityAlias.getParameterName(), (Class) entityAlias.getParameterValue());
                }

                for (QueryField scalarAlias : scalarFields) {
                    nativeQuery.addScalar(scalarAlias.getParameterName(), scalarAlias.getParamterType());
                }


                resultTuplesList = nativeQuery.list();


                return resultTuplesList;
            }
            catch(Exception e) {
                LOGGER.error(e.getMessage(), e);

                throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.UNKNOWN,
                        e.getMessage() + " Cause Message: " + e.getCause().getMessage());

            }


    }
}
