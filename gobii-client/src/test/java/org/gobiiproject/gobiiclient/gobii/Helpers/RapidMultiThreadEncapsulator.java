package org.gobiiproject.gobiiclient.gobii.Helpers;

import org.junit.Assert;

import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by VCalaminos on 1/30/2019.
 */
public class RapidMultiThreadEncapsulator {

    public static void checkMessages(List<Future<Object>> futures) throws Exception {

        for (Future<Object> currenFuture : futures) {
            String currentMessage = null;
            if (currenFuture.get() != null) {
                currentMessage = currenFuture.get().toString();
            }

            Assert.assertNull(currentMessage,
                    currentMessage);
        }

    }

}
