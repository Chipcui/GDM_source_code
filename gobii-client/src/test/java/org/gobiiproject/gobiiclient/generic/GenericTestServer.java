
package org.gobiiproject.gobiiclient.generic;

import org.gobiiproject.gobiiclient.generic.model.Person;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path(GenericTestPaths.GENERIC_TEST_ROOT + "/" + GenericTestPaths.GENERIC_CONTEXT_ONE)
public class GenericTestServer {

    @GET
    @Path(GenericTestPaths.RESOURCE_PERSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Person helloWorld() {
        return new Person("John","Smith","Nice guy");
    }
}
