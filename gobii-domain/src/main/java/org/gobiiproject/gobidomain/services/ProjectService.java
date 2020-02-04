// ************************************************************************
// (c) 2016 GOBii Project
// Initial Version: Phil Glaser
// Create Date:   2016-03-24
// ************************************************************************
package org.gobiiproject.gobidomain.services;

import java.util.List;
import org.gobiiproject.gobidomain.GobiiDomainException;
import org.gobiiproject.gobiimodel.dto.entity.auditable.ProjectDTO;

/**
 * Created by Phil on 3/24/2016.
 */
public interface ProjectService<T> {

    T createProject(T projectDTO) throws GobiiDomainException;
    T replaceProject(Integer projectId, T projectDTO) throws GobiiDomainException;
    List<T> getProjects() throws GobiiDomainException;
    T getProjectById(Integer projectId) throws GobiiDomainException;
    List<T> getProjectsForLoadedDatasets() throws GobiiDomainException;
    List<T> getProjects(Integer pageNum, Integer pageSize) throws GobiiDomainException;

}
