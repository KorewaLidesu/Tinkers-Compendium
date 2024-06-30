package lance5057.tDefense.core.library.materialutilities;

import slimeknights.tconstruct.library.MaterialIntegration;

import java.io.PrintWriter;

public interface MaterialBase {

    void setupPre(MaterialHelper mat);

    void setupInit(MaterialHelper mat);

    void setupIntegration(MaterialIntegration mi);

    void setupPost(MaterialHelper mat);

    void setupClient(MaterialHelper mat);

    void setupModels(MaterialHelper mat);

    void setupWiki(MaterialHelper mat, PrintWriter out);
}
