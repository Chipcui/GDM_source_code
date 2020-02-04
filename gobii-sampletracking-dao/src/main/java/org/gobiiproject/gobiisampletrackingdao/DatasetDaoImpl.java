package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.gobiiproject.gobiimodel.entity.Analysis;
import org.gobiiproject.gobiimodel.entity.Dataset;
import org.gobiiproject.gobiimodel.entity.QueryField;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.hibernate.Session;
import org.hibernate.type.IntegerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Data access object Implementation for dataset Entity in the database
 *
 */
public class DatasetDaoImpl implements DatasetDao {

    Logger LOGGER = LoggerFactory.getLogger(DatasetDao.class);

    @PersistenceContext
    protected EntityManager em;

    final int defaultPageSize = 1000;


    /**
     * Gets list of dataset entities that match the given filter parameters.
     * @param pageCursor page cursor used to fetch data.
     * @param pageSize size of the page to be fetched.
     * @return List of dataset entity
     */
    @Override
    @Transactional
    public List<Dataset> listDatasetsByPageCursor(String pageCursor, Integer pageSize) throws GobiiException {

        List<Dataset> datasets;

        final int defaultPageSize = 1000;

        if (pageSize == null) pageSize = defaultPageSize;

        Integer datasetId;

        try {
            datasetId = Integer.parseInt(pageCursor);
        }
        catch(Exception e) {
            //Invalid page cursor return first page
            datasetId = 0;
        }

        try {

            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

            CriteriaQuery<Dataset> criteriaQuery = criteriaBuilder.createQuery(Dataset.class);

            Root<Dataset> datasetRoot = criteriaQuery.from(Dataset.class);

            criteriaQuery.select(datasetRoot);

            criteriaQuery.where(criteriaBuilder.gt(datasetRoot.get("datasetId"), datasetId));

            criteriaQuery.orderBy(criteriaBuilder.asc(datasetRoot.get("datasetId")));

            Query listQuery = em.createQuery(criteriaQuery);

            listQuery
                    .setMaxResults(pageSize);

            datasets = listQuery.getResultList();

        }
        catch(Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage() + " Cause Message: " + e.getCause().getMessage());

        }

        return datasets;
    }

    /**
     * In database, Analyses for a given dataset are saved as an array of reference ids to the analysis table.
     * Not able to map using hibernate ManytoOne relation as the array datatype is not supported in hibernate.
     * Using native query to left join analysis entities along with other required scalar fields
     * Returns list of Dataset entities joined with respective analysis entities.
     * The respective analysis entities are added to the mappedAnalyses hashset
     * @param pageSize - size of the page
     * @param rowOffset - Row offset after which the pages need to be fetched
     * @param datasetId - Id for dataset. Unique identifier.
     * @return List of DatsetEntity
     */
    @Override
    @Transactional
    public List<Dataset> listDatasets(Integer pageSize,
                                      Integer rowOffset,
                                      Integer datasetId) {

        List<Dataset> datasetsWithMarkersAndSamplesCount = new ArrayList<>();

        String queryString = "WITH ds AS (" +
                "SELECT * " +
                "FROM dataset " +
                "WHERE :datasetId IS NULL OR dataset_id = :datasetId " +
                "LIMIT :pageSize OFFSET :rowOffset) " +
                "SELECT ds.* , anas.*, " +
                "(SELECT gettotalmarkersindataset " +
                "FROM gettotalmarkersindataset(CAST(ds.dataset_id AS TEXT))) " +
                "AS marker_count, " +
                "(SELECT gettotaldnarunsindataset " +
                "FROM gettotaldnarunsindataset(CAST(ds.dataset_id AS TEXT))) " +
                "AS dnarun_count " +
                "FROM ds " +
                "LEFT JOIN analysis AS anas ON(anas.analysis_id = ANY(ds.analyses)) ";

        try {

            if (pageSize == null) {
                pageSize = defaultPageSize;
            }

            Session session = em.unwrap(Session.class);

            List<Object[]> resultTupleList = session.createNativeQuery(queryString)
                    .addEntity("ds", Dataset.class)
                    .addEntity("anas", Analysis.class)
                    .addScalar("marker_count", IntegerType.INSTANCE)
                    .addScalar("dnarun_count", IntegerType.INSTANCE)
                    .setParameter("pageSize", pageSize, IntegerType.INSTANCE)
                    .setParameter("rowOffset", rowOffset, IntegerType.INSTANCE)
                    .setParameter("datasetId", datasetId, IntegerType.INSTANCE)
                    .list();

            datasetsWithMarkersAndSamplesCount = mapAnalysesToDataset(resultTupleList);

            return datasetsWithMarkersAndSamplesCount;

        }
        catch(Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage() + " Cause Message: " + e.getCause().getMessage());

        }
    }

    /**
     * Map Analysis entities in the result tuple to their respective Dataset Entity.
     * @param resultTuplesList - Result tuple with dataset left joined with analysis and other scalar fields
     * @return tuple list with Dataset Entity and other scalar fields,
     */
    public List<Dataset> mapAnalysesToDataset(List<Object[]> resultTuplesList) throws GobiiException {

        List<Dataset> datasetsWithMarkersAndSamplesCount = new ArrayList<>();

        HashMap<Integer, Dataset> datasetsMapById = new HashMap<>();

        for(Object[] tuple : resultTuplesList) {

            Dataset dataset = (Dataset) tuple[0];

            if(dataset == null) {
                continue;
            }


            dataset.setMarkerCount((Integer) tuple[2]);
            dataset.setDnaRunCount((Integer) tuple[3]);

            if(tuple[1] == null) {

                dataset.getMappedAnalyses().add(dataset.getCallingAnalysis());

                datasetsWithMarkersAndSamplesCount.add(dataset);

            }
            else {

                if (datasetsMapById.containsKey(dataset.getDatasetId())) {

                    datasetsMapById.get(dataset.getDatasetId()).getMappedAnalyses().add((Analysis) tuple[1]);

                } else {

                    dataset.getMappedAnalyses().add((Analysis) tuple[1]);
                    dataset.getMappedAnalyses().add(dataset.getCallingAnalysis());

                    datasetsMapById.put(dataset.getDatasetId(), dataset);

                    datasetsWithMarkersAndSamplesCount.add(dataset);

                }
            }
        }

        return datasetsWithMarkersAndSamplesCount;
    }

    /**
     * Gets the dataset entity by dataset id.
     * @param datasetId - Dataset Id
     * @return
     */
    @Override
    @Transactional
    public Dataset getDatasetById(Integer datasetId) throws GobiiException {

        try {

            List<Dataset> datasetsById = this.listDatasets(null, null, datasetId);

            if (datasetsById.size() > 1) {
                LOGGER.error("More than one duplicate entries found.");

                throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.NONE,
                        "More than one dataset entity exists for the same Id");

            } else if (datasetsById.size() == 0) {
                throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                        GobiiValidationStatusType.ENTITY_DOES_NOT_EXIST,
                        "Dataset Entity for given id does not exist");
            }

            return datasetsById.get(0);
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



}
