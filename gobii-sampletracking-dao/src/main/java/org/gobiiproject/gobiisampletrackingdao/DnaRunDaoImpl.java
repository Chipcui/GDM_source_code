package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.entity.DnaRun;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.hibernate.Session;
import org.hibernate.type.IntegerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

public class DnaRunDaoImpl implements DnaRunDao {

    Logger LOGGER = LoggerFactory.getLogger(MarkerDao.class);

    @PersistenceContext
    protected EntityManager em;

    final int defaultPageSize = 1000;

    /**
     * Lists the DnaRun Entities which matches the given criterias.
     * @param pageSize - Number of rows to be fetched
     * @param rowOffset - Number of rows to be offseted before fetching tuples
     * @param dnaRunId - Dna Run Id which uniquely identifies dna run
     * @param datasetId - Dataset Id to which dnaruns belong to.
     * @return
     */
    @Override
    @Transactional
    public List<DnaRun> getDnaRuns(Integer pageSize, Integer rowOffset,
                                   Integer dnaRunId, Integer datasetId) {

        List<DnaRun> dnaRuns;

        String queryString = "SELECT dnarun.* " +
                " FROM dnarun AS dnarun " +
                " WHERE (:datasetId IS NULL OR dnarun.dataset_dnarun_idx->CAST(:datasetId AS TEXT) IS NOT NULL) " +
                " AND (:dnaRunId IS NULL OR dnarun.dnarun_id = :dnaRunId) " +
                " LIMIT :pageSize OFFSET :rowOffset ";

        try {

            Session session = em.unwrap(Session.class);
            session.enableFetchProfile("dnarun-experiment-dnasample");

            if(pageSize == null) {
                pageSize = defaultPageSize;
            }

            dnaRuns = session.createNativeQuery(queryString)
                    .addEntity("dnarun", DnaRun.class)
                    .setParameter("pageSize", pageSize, IntegerType.INSTANCE)
                    .setParameter("rowOffset", rowOffset, IntegerType.INSTANCE)
                    .setParameter("dnaRunId", dnaRunId, IntegerType.INSTANCE)
                    .setParameter("datasetId", datasetId, IntegerType.INSTANCE)
                    .list();

            return dnaRuns;

        }
        catch(Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage() + " Cause Message: " + e.getCause().getMessage());
        }

    }

    @Override
    @Transactional
    public List<DnaRun> getDnaRunsByDnaRunIdCursor(
            Integer dnaRunIdCursor,
            Integer datasetId,
            Integer pageSize) {

        List<DnaRun> dnaRuns;

        String queryString = "SELECT {dnarun.*} " +
                " FROM dnarun AS dnarun " +
                " WHERE (dnarun.dataset_dnarun_idx->CAST(:datasetId AS TEXT) IS NOT NULL OR :datasetId IS NULL) " +
                " AND (:dnaRunIdCursor IS NULL OR dnarun.dnarun_id > :dnaRunIdCursor) " +
                " ORDER BY dnarun.dnarun_id " +
                " LIMIT :pageSize ";

        try {

            if(pageSize == null) {
                pageSize = defaultPageSize;
            }

            Session session = em.unwrap(Session.class);

            dnaRuns = session.createNativeQuery(queryString)
                    .addEntity("dnarun", DnaRun.class)
                    .setParameter("pageSize", pageSize, IntegerType.INSTANCE)
                    .setParameter("datasetId", datasetId, IntegerType.INSTANCE)
                    .setParameter("dnaRunIdCursor",dnaRunIdCursor, IntegerType.INSTANCE)
                    .list();

            return dnaRuns;

        }
        catch(Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage() + " Cause Message: " + e.getCause().getMessage());
        }
    }

    /**
     * Get DnaRun Entity by dnaRun Id.
     * dnaRunId is unique id for dnaRunEntity.
     * @param dnaRunId - dna run id for which DnaRun Entity needs to be fetched.
     * @return - DnaRun Entity with the given id
     */
    @Override
    @Transactional
    public DnaRun getDnaRunById(Integer dnaRunId) {

        try {

            List<DnaRun> dnaRunsById = this.getDnaRuns(null, null,
                    dnaRunId, null);

            if (dnaRunsById.size() > 1) {

                LOGGER.error("More than one duplicate entries found.");

                throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.NONE,
                        "More than one dnarun entity exists for the same Id");

            } else if (dnaRunsById.size() == 0) {
                throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                        "Dna run Entity for given id does not exist");
            }

            return dnaRunsById.get(0);

        }
        catch(GobiiException ge) {
            throw ge;
        }
        catch (Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage() + " Cause Message: " + e.getCause().getMessage());

        }


    }

    /**
     * Lists DnaRun entities for given dataset Id
     * @param datasetId - ID of dataset to which dnaruns belong.
     * @param pageSize - Number of rows to be fetched
     * @param rowOffset - Number of rows to offset before fetching
     * @return - List of result DnaRun Entities.
     */
    @Override
    @Transactional
    public List<DnaRun> getDnaRunsByDatasetId(Integer datasetId, Integer pageSize,
                                              Integer rowOffset) {

        return getDnaRuns(pageSize, rowOffset, null, datasetId);

    }


    /**
     * Lists DnaRun Entitis with given DnaRun Id
     * @param dnaRunIds - List of IDs of dnaRun.
     * @return - List of DnaRun Entity result
     */
    @Override
    @Transactional
    public List<DnaRun> getDnaRunsByDanRunIds(List<Integer> dnaRunIds) {

        List<DnaRun> dnaruns = new ArrayList<>();

        try {

            CriteriaBuilder cb  = em.getCriteriaBuilder();

            // Initialize criteria with Marker Entity as Result
            CriteriaQuery<DnaRun> criteria = cb.createQuery(DnaRun.class);

            //Set Root entity and selected entities
            Root<DnaRun> root = criteria.from(DnaRun.class);
            criteria.select(root);


            criteria.where(root.get("dnaRunId").in(dnaRunIds));

            criteria.orderBy(cb.asc(root.get("dnaRunId")));

            dnaruns =  em.createQuery(criteria).getResultList();

            return dnaruns;

        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage() + " Cause Message: " + e.getCause().getMessage());

        }

    }


    /**
     * Lists DnaRun Entities with given DnaRun Names
     * @param dnaRunNames - List of Names for which dnaRuns need to be fecthed.
     * @return - List of DnaRun Entity result.
     */
    @Override
    @Transactional
    public List<DnaRun> getDnaRunsByDanRunNames(List<String> dnaRunNames) {

        List<DnaRun> dnaruns = new ArrayList<>();

        try {

            CriteriaBuilder cb  = em.getCriteriaBuilder();

            // Initialize criteria with Marker Entity as Result
            CriteriaQuery<DnaRun> criteria = cb.createQuery(DnaRun.class);

            //Set Root entity and selected entities
            Root<DnaRun> root = criteria.from(DnaRun.class);
            criteria.select(root);


            criteria.where(root.get("dnaRunName").in(dnaRunNames));

            criteria.orderBy(cb.asc(root.get("dnaRunName")));

            dnaruns = em.createQuery(criteria).getResultList();

            return dnaruns;

        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage() + " Cause Message: " + e.getCause().getMessage());

        }

    }



}
