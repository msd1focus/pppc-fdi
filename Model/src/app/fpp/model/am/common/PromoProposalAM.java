package app.fpp.model.am.common;

import oracle.jbo.ApplicationModule;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Mon Jun 12 10:29:50 ICT 2017
// ---------------------------------------------------------------------
public interface PromoProposalAM extends ApplicationModule {
    void setLoginToSession_PromoProposalAM(String key, String val);

    void addDocApproval(String propId, String docNo, String docStatus,
                        String promoDtFrom, String promoDtTo, String docRegion,
                        String usrRole, String userNm, String aprvlFlowNm,
                        String propDt);

    void cancelDocPp(String propId, String docNo, String docStatus,
                     String promoDtFrom, String promoDtTo, String docRegion,
                     String usrRole, String userNm, String aprvlCodeRun,
                     String idDocAprvl, String cancelReason, String usrAction,
                     String propDt);


    void updateTargetUom(String ppid, String uomTarget);
}
