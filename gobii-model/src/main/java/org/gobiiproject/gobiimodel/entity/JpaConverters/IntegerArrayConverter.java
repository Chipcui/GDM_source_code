package org.gobiiproject.gobiimodel.entity.JpaConverters;

import org.gobiiproject.gobiimodel.config.GobiiException;
import org.postgresql.jdbc.PgArray;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Array;

/**
 * JPA convertor class to convert Postgresql Database Integer array to Ineteger array and vice versa.
 */
@Converter
public class IntegerArrayConverter implements AttributeConverter<Integer[], Object> {

    /**
     * Converts Java Integer array to Postgres Integer array.
     * @param intArray - Integer array which needs to be persisted
     * @return
     */
    @Override
    public Object convertToDatabaseColumn(Integer[] intArray) {
        return intArray;
    }

    /**
     * Converts postgres Integer arrayString to Java Integer array.
     * @param  - Postgres Ineteger array object.
     * @return
     */
    @Override
    public Integer[] convertToEntityAttribute(Object pgObject) {

        Integer[] intArray = null;

        if(pgObject instanceof Integer[])
            return (Integer[])pgObject;

        try {


            Array databaseArray = ((PgArray) pgObject);

            if(pgObject != null) {

                intArray = (Integer[]) databaseArray.getArray();

            }

            return intArray;
        }
        catch(Exception e) {
            throw new GobiiException("Conversion of text[] database column to string array in failed");
        }

    }

}
