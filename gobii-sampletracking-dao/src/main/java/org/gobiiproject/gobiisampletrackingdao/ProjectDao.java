package org.gobiiproject.gobiisampletrackingdao;


import org.gobiiproject.gobiimodel.entity.Project;

import java.util.List;
import java.util.Map;

public interface ProjectDao {

    Integer createProject(Project newProject);
    Project getProjectById(Integer projectId);
    Project getProjectByName(String projectName);
    List<Project> listProjects(Integer pageNum, Integer pageSize, Map<String, String> projectQuery);
    Integer updateProject(Project newProject);

}
