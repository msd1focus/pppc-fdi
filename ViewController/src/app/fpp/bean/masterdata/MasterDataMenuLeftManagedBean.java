package app.fpp.bean.masterdata;

import oracle.adf.controller.TaskFlowId;

public class MasterDataMenuLeftManagedBean {
    private String lookupValueTaskFlowId = "/WEB-INF/md-lookup-value.xml#md-lookup-value";
    private String runNumberTaskFlowId = "/WEB-INF/md-run-number.xml#md-run-number";
    private String currentTF = "main";

    public MasterDataMenuLeftManagedBean() {
    }

    public TaskFlowId getDynamicTaskFlowId() {
        if (this.getCurrentTF().equalsIgnoreCase("main"))
            return TaskFlowId.parse(lookupValueTaskFlowId);
        else if (this.getCurrentTF().equalsIgnoreCase("runnum"))
            return TaskFlowId.parse(runNumberTaskFlowId);
        else 
            return TaskFlowId.parse(lookupValueTaskFlowId);
    } 

    public void setCurrentTF(String currentTF) {
        this.currentTF = currentTF;
    }

    public String getCurrentTF() {
        return currentTF;
    }
}
