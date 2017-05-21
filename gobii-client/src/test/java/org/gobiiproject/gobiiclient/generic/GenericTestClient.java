package org.gobiiproject.gobiiclient.generic;

import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.http.HttpStatus;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.gobiiproject.gobiiapimodel.restresources.common.RestUri;
import org.gobiiproject.gobiiclient.core.common.GenericClientContext;
import org.gobiiproject.gobiiclient.core.common.HttpMethodResult;
import org.gobiiproject.gobiiclient.generic.model.GenericTestValues;
import org.gobiiproject.gobiimodel.config.ServerBase;
import org.gobiiproject.gobiimodel.types.GobiiHttpHeaderNames;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;

/**
 * Created by Phil on 5/19/2017.
 */
public class GenericTestClient {

    private static Logger LOGGER = LoggerFactory.getLogger(GenericTestClient.class);

    private static Server server = null;
    private static ServerBase serverBase = null;
    private static GenericClientContext genericClientContext = null;

    @BeforeClass
    public static void serverSetup() throws Exception {


        /*
        // This setup is if you are using jetty without jersey
        // Please keep this example around in case we need to revert to that
        // However, for now it seems that jersey makes it easier to set up the
        // test server resources
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8090);
        server.setConnectors(new Connector[] { connector });

        ContextHandler contextGet = new ContextHandler();
        contextGet.setHandler(new TestServerHandlerGet());
        contextGet.setContextPath("/generic/getresource");

        ContextHandler contextPost = new ContextHandler();
        contextPost.setHandler(new TestServerHandlerPost());
        contextPost.setContextPath("/generic/postresource");

        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[] { contextGet, contextPost });
        server.setHandler(contexts);

        server.start();
        server.join();


        System.out.print(server.dump());

        */

        serverBase = new ServerBase("localhost",
                GenericTestPaths.GENERIC_CONTEXT_ONE,
                8099,
                true);



        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.packages(GenericServerMethodsContextOne.class.getPackage().getName());
        resourceConfig.register(JacksonFeature.class);
        resourceConfig.register(SerializationFeature.INDENT_OUTPUT);
        ServletContainer servletContainer = new ServletContainer(resourceConfig);
        ServletHolder sh = new ServletHolder(servletContainer);
        server = new Server(serverBase.getPort());

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addServlet(sh, "/*");
        server.setHandler(context);


        genericClientContext = new GenericClientContext(serverBase);

        //System.out.print(server.dump());
        server.start();
//        server.join();

    }

    public static boolean didHttpMethodSucceed(HttpMethodResult httpMethodResult) {

        boolean returnVal = true;

        if (httpMethodResult.getResponseCode() != HttpStatus.SC_OK) {
            String message = "http method failed: "
                    + httpMethodResult.getUri().toString()
                    + "; failure mode: "
                    + Integer.toString(httpMethodResult.getResponseCode())
                    + " ("
                    + httpMethodResult.getReasonPhrase()
                    + ")";

            LOGGER.error(message);

            returnVal = false;
        }

        return returnVal;
    }

    @AfterClass
    public static void serverTearDown() throws Exception {

        server.stop();
    }

    @Test
    public void testGetJson() throws Exception {

        RestUri restUriGetPerson = new RestUri(GenericTestPaths.GENERIC_TEST_ROOT,
                GenericTestPaths.GENERIC_CONTEXT_ONE,
                GenericTestPaths.RESOURCE_PERSON);

        HttpMethodResult httpMethodResult = genericClientContext
                .get(restUriGetPerson);

        Assert.assertTrue(didHttpMethodSucceed(httpMethodResult));

        Assert.assertNotNull(httpMethodResult.getJsonPayload());

        Assert.assertEquals(httpMethodResult
                        .getJsonPayload()
                        .get("nameLast")
                        .getAsString(),
                GenericTestValues.NAME_LAST);

        Assert.assertEquals(httpMethodResult
                        .getJsonPayload()
                        .get("nameFirst")
                        .getAsString(),
                GenericTestValues.NAME_FIRST);

    }

    @Test
    public void testGetPlain() throws Exception {

        RestUri restUriGetPerson = new RestUri(GenericTestPaths.GENERIC_TEST_ROOT,
                GenericTestPaths.GENERIC_CONTEXT_TWO,
                GenericTestPaths.RESOURCE_PERSON)
                .withHttpHeader(GobiiHttpHeaderNames.HEADER_NAME_CONTENT_TYPE,
                        MediaType.TEXT_PLAIN)
                .withHttpHeader(GobiiHttpHeaderNames.HEADER_NAME_ACCEPT,
                        MediaType.TEXT_PLAIN);

        HttpMethodResult httpMethodResult = genericClientContext
                .get(restUriGetPerson);

        Assert.assertTrue(didHttpMethodSucceed(httpMethodResult));

        String plainTextResult = httpMethodResult.getPlainPayload().toString();
        Assert.assertNotNull(plainTextResult);
        Assert.assertTrue(plainTextResult.contains(GenericTestValues.NAME_FIRST));
        Assert.assertTrue(plainTextResult.contains(GenericTestValues.NAME_LAST));


    }
}
