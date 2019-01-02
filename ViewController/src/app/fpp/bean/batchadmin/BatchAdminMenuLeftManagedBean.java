package app.fpp.bean.batchadmin;

import javax.faces.event.ActionEvent;

import oracle.adf.controller.TaskFlowId;

public class BatchAdminMenuLeftManagedBean {
    private String baModifierTaskFlowId = "/WEB-INF/ba-modifier.xml#ba-modifier";
    private String baPromoBarangTaskFlowId = "/WEB-INF/ba-promo-barang.xml#ba-promo-barang";
    private String currentTF = "main";

    public BatchAdminMenuLeftManagedBean() {
    }

    public TaskFlowId getDynamicTaskFlowId() {
        if (this.getCurrentTF().equalsIgnoreCase("main"))
            return TaskFlowId.parse(baModifierTaskFlowId);
        else if (this.getCurrentTF().equalsIgnoreCase("batchpb"))
            return TaskFlowId.parse(baPromoBarangTaskFlowId);
        else 
            return TaskFlowId.parse(baModifierTaskFlowId);
    } 

    public void setCurrentTF(String currentTF) {
        this.currentTF = currentTF;
    }

    public String getCurrentTF() {
        return currentTF;
    }
}
