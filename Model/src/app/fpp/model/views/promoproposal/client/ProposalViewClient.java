package app.fpp.model.views.promoproposal.client;

import app.fpp.model.views.promoproposal.common.ProposalView;

import oracle.jbo.client.remote.ViewUsageImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Tue Jan 16 20:53:21 ICT 2018
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class ProposalViewClient extends ViewUsageImpl implements ProposalView {
    /**
     * This is the default constructor (do not remove).
     */
    public ProposalViewClient() {
    }

    public void rollbackPartial() {
        Object _ret =
            getApplicationModuleProxy().riInvokeExportedMethod(this,"rollbackPartial",null,null);
        return;
    }
}