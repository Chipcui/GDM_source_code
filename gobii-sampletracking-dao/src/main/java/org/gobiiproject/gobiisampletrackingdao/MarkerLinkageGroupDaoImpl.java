package org.gobiiproject.gobiisampletrackingdao;

import org.gobiiproject.gobiimodel.entity.MarkerLinkageGroup;
import org.gobiiproject.gobiimodel.types.GobiiStatusLevel;
import org.gobiiproject.gobiimodel.types.GobiiValidationStatusType;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.type.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

public class MarkerLinkageGroupDaoImpl implements MarkerLinkageGroupDao {

    Logger LOGGER = LoggerFactory.getLogger(DnaSampleDao.class);

    @PersistenceContext
    protected EntityManager em;



    @Override
    @Transactional
    public List<MarkerLinkageGroup> getMarkerLinkageGroups(Integer pageNum, Integer pageSize,
                                                           Integer markerId, Integer linkageGroupId,
                                                           Integer datasetId) {
        List<MarkerLinkageGroup> markerLinkageGroups;
        final int defaultPageSize = 1000;
        final int defaultPageNum = 0;

        try {


            Session session = em.unwrap(Session.class);

            Criteria markerLinkageCriteria = session.createCriteria(MarkerLinkageGroup.class);

            Criteria markerCriteria = markerLinkageCriteria.createCriteria("marker");

            // TODO: new linkagegroup criteria will be added when linkage group entity is created
            if(linkageGroupId != null) {
                markerLinkageCriteria.add(Restrictions.eq("linkageGroupId", linkageGroupId));
            }

            if(markerId != null) {
                markerCriteria.add(Restrictions.eq("markerId", markerId));
            }

            if(datasetId != null) {
                markerCriteria.add(Restrictions.sqlRestriction(
                        "{alias}.dataset_marker_idx ?? ?", datasetId.toString(),
                        StringType.INSTANCE));
            }



            if(pageSize != null && pageNum != null) {
                markerLinkageCriteria
                        .setMaxResults(pageSize)
                        .setFirstResult(pageSize * pageNum);
            }
            else {
                //If either page size or page number is not provided, use default maximum limit which is 1000
                //and fetch the first page
                markerLinkageCriteria
                        .setMaxResults(defaultPageSize)
                        .setFirstResult(defaultPageNum);
            }

            markerLinkageGroups = markerLinkageCriteria.list();

        }
        catch(Exception e) {

            LOGGER.error(e.getMessage(), e);

            throw new GobiiDaoException(GobiiStatusLevel.ERROR,
                    GobiiValidationStatusType.UNKNOWN,
                    e.getMessage() + " Cause Message: " + e.getCause().getMessage());
        }

        return markerLinkageGroups;


    }

}
