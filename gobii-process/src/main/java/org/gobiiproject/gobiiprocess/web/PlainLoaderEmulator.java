package org.gobiiproject.gobiiprocess.web;

import org.gobiiproject.gobiimodel.config.ConfigSettings;
import org.gobiiproject.gobiimodel.config.GobiiCropConfig;
import org.gobiiproject.gobiimodel.types.ServerType;

/**
 * Simple process class for example of reading configuration files.
 * Created by Phil on 6/24/2016.
 */
public class PlainLoaderEmulator {

    public static void main(String[] args) {

        try {

            ConfigSettings configSettings = new ConfigSettings("C:\\gobii-config\\gobii-web.properties");
            for(GobiiCropConfig currentGobiiCropConfig : configSettings.getActiveCropConfigs() ) {
                System.out.println(currentGobiiCropConfig.getServer(ServerType.GOBII_WEB).getHost());
            }


        } catch (Exception e ) {
            e.printStackTrace();
        }
    } // main()
}
