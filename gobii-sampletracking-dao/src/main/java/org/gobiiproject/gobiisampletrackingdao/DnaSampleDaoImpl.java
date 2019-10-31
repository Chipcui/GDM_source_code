package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.DnaSample;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

public class DnaSampleDaoImpl implements DnaSampleDao {

    Logger LOGGER = LoggerFactory.getLogger(DnaSampleDao.class);

    @PersistenceContext
    protected EntityManager em;

    @Override
    @Transactional
    public List<DnaSample> getDnaSamples(Integer pageNum, Integer pageSize, Integer dnaSampleId) {

        List<DnaSample> dnaSamples = new ArrayList<>();

        try {

            Session session = em.unwrap(Session.class);

            Query dnaSamplesQuery = session
                    .createQuery("SELECT dnasample FROM DnaSample dnasample" +
                            " WHERE dnasample.dnaSampleId = :dnaSampleId");

            if(dnaSampleId != null) {
                dnaSamplesQuery
                    .setInteger("dnaSampleId", dnaSampleId);
            }

            dnaSamples = dnaSamplesQuery
                    .setFirstResult(pageNum*pageSize)
                    .setMaxResults(pageSize)
                    .list();

        }
        catch(Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage());
        }



        return dnaSamples;

    }

}
