package org.gobiiproject.gobiidao.resultset.core;

import java.sql.SQLException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.jpa.HibernateEntityManagerFactory;

/**
 * Created by Phil on 4/8/2016.
 */
public class DbMetaData {

    @PersistenceContext
    protected EntityManager em;

    public String getCurrentDbUrl() throws SQLException {

        String returnVal = "";
//        Shared EntityManager proxy for target factory [org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean@48fde1c2]


        HibernateEntityManagerFactory entityManagerFactory = (HibernateEntityManagerFactory) em.getEntityManagerFactory();
        SessionImplementor sessionFactory = (SessionImplementor) entityManagerFactory.getSessionFactory();
        return sessionFactory.getJdbcConnectionAccess().obtainConnection().getMetaData().getURL();
//        DataSource dataSource = datasourceConnectionProviderImplj.getDataSource();



 //       returnVal = dataSource.getConnection().getMetaData().getURL();

   //     return returnVal;
    }
}

