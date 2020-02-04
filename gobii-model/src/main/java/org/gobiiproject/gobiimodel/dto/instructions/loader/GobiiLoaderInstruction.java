package org.gobiiproject.gobiimodel.dto.instructions.loader;


import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * A loader instruction containing all the details nessisary to create a digest file.
 * See {@link GobiiFile} and {@link GobiiFileColumn}
 * Created by Phil on 4/12/2016.
 */
@Data
@Accessors(chain = true)
public class GobiiLoaderInstruction {


    //Name of this table. Used as filename for loading, and to determine what database table it goes to.
    private String table = null;
    //List of GobiiFileColumn columns, left to right ordering
    private List<GobiiFileColumn> gobiiFileColumns = new ArrayList<>();

}