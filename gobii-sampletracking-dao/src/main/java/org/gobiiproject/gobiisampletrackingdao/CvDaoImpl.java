package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.cvnames.CvGroup;
import org.gobiiproject.gobiimodel.entity.Cv;
import org.gobiiproject.gobiimodel.types.GobiiCvGroupType;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.gobiiproject.gobiisampletrackingdao.spworkers.SpWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

public class CvDaoImpl implements CvDao {

    Logger LOGGER = LoggerFactory.getLogger(SpWorker.class);

    @PersistenceContext
    protected EntityManager em;

    @Override
    public Cv getCvByCvId(Integer cvId) throws GobiiException {


        Objects.requireNonNull(cvId, "Cv Id should not be null");

        try {
            List<Cv> cvList = em
                    .createNativeQuery("SELECT * FROM cv WHERE cv_id = ?", Cv.class)
                    .setParameter(1, cvId)
                    .getResultList();

            if(cvList.size() == 1) {
                return cvList.get(0);
            }
            else {
                LOGGER.error("More than one entity for same Id");

                throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.UNIQUE_KEY_VIOLATION,
                        "More than one entity with same id found. ");

            }

        }
        catch(Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage());

        }

    }

    @Override
    public List<Cv> getCvListByCvGroup(String cvGroupName, GobiiCvGroupType cvType) throws GobiiException {

        List<Cv> cvList = new ArrayList<>();

        Objects.requireNonNull(cvGroupName, "CV group name should not be null");

        String queryString = "SELECT DISTINCT cv.* FROM cv INNER JOIN cvgroup " +
                "ON (cv.cvgroup_id = cvgroup.cvgroup_id AND cvgroup.name = ?";

        if(cvType != null) {
            queryString += " AND cvgroup.type = ?)";
        }
        else {
            queryString += " )";
        }

        try {

            Query q = em
                    .createNativeQuery(queryString, Cv.class)
                    .setParameter(1, cvGroupName);


            if(cvType != null) {
                q.setParameter(2, cvType.getGroupTypeId());
            }

            cvList = q.getResultList();

        }
        catch(Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage());

        }

        return cvList;
    }

    @Override
    public List<Cv> getCvsByCvTermAndCvGroup(String cvTerm,
                                             String cvGroupName,
                                             GobiiCvGroupType cvType) throws GobiiException {

        List<Cv> cvList = new ArrayList<>();

        Objects.requireNonNull(cvTerm, "CV term cannot be null");
        Objects.requireNonNull(cvGroupName, "CV Group Name cannot be null");

        String queryString = "SELECT DISTINCT cv.* FROM cv " + "INNER JOIN cvgroup " +
                "ON (cv.cvgroup_id = cvgroup.cvgroup_id AND cvgroup.name = ? AND cv.term = ?";

        if(cvType != null) {
            queryString += " AND cvgroup.type = ?)";
        }
        else {
            queryString += " )";
        }

        try {

            Query q = em.createNativeQuery(queryString, Cv.class)
                    .setParameter(1, cvGroupName)
                    .setParameter(2, cvTerm);

            if(cvType != null) {
                q.setParameter(3, cvType.getGroupTypeId());
            }

            cvList = q.getResultList();

        }
        catch(Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage());

        }

        return cvList;
    }
}
