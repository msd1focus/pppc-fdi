package app.fpp.bean.approvalsetting;

import app.fpp.adfextensions.JSFUtils;

import java.util.Map;

import oracle.adf.view.rich.component.rich.data.RichTable;

import oracle.adf.view.rich.event.PopupCanceledEvent;

import oracle.binding.BindingContainer;
import oracle.adf.model.BindingContext;
import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.event.DialogEvent;
import oracle.adf.view.rich.event.PopupFetchEvent;
import oracle.binding.OperationBinding;

import oracle.jbo.domain.DBSequence;

public class ApprovalSettingBean {
    private RichTable tblFlowList;
    private RichTable tblFlowStep;

    public ApprovalSettingBean() {
    }

    public BindingContainer getBindings() {
        return BindingContext.getCurrent().getCurrentBindingsEntry();
    }

    public void addFlowApprovalPopup(PopupFetchEvent popupFetchEvent) {
        BindingContainer bindings = getBindings();
        OperationBinding operationBinding =
            bindings.getOperationBinding("CreateInsert");
        operationBinding.execute();
    }

    public void addFlowApprovalDialog(DialogEvent dialogEvent) {
        BindingContainer bindings = getBindings();
        if (dialogEvent.getOutcome().name().equals("ok")) {
            OperationBinding operationBindingCommit =
                bindings.getOperationBinding("Commit");
            operationBindingCommit.execute();

            JSFUtils.addFacesInformationMessage("Data flow approval sudah ditambahkan");

        } else {
            OperationBinding operationBinding =
                bindings.getOperationBinding("Rollback");
            operationBinding.execute();
        }
        
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblFlowList);
    }
    

    public void editFlowApprovalDialog(DialogEvent dialogEvent) {
        BindingContainer bindings = getBindings();
        if (dialogEvent.getOutcome().name().equals("ok")) {
            OperationBinding operationCommit =
                bindings.getOperationBinding("Commit");
            operationCommit.execute();
            
            OperationBinding operationRefresh =
                bindings.getOperationBinding("Execute");
            operationRefresh.execute();
            
            JSFUtils.addFacesInformationMessage("Perubahan sudah disimpan.");

        } else {
            OperationBinding operationBinding =
                bindings.getOperationBinding("Rollback");
            operationBinding.execute();
        }            
        
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblFlowList);
    }

    public void setTblFlowList(RichTable tblFlowList) {
        this.tblFlowList = tblFlowList;
    }

    public RichTable getTblFlowList() {
        return tblFlowList;
    }

    public void setTblFlowStep(RichTable tblFlowStep) {
        this.tblFlowStep = tblFlowStep;
    }

    public RichTable getTblFlowStep() {
        return tblFlowStep;
    }

    public void addFlowStepDialog(DialogEvent dialogEvent) {
        BindingContainer bindings = getBindings();

        ADFContext adfCtx = ADFContext.getCurrent();
        Map pageFlowScope = adfCtx.getPageFlowScope();
        String flowStepStatus = (String)pageFlowScope.get("flowStepStatus");
        
        if (dialogEvent.getOutcome().name().equals("ok")) {
            OperationBinding operationBindingCommit =
                bindings.getOperationBinding("Commit");
            operationBindingCommit.execute();
            
            if (flowStepStatus.equalsIgnoreCase("add")) {
                JSFUtils.addFacesInformationMessage("Data flow step sudah ditambahkan");
            } else {
                JSFUtils.addFacesInformationMessage("Data flow step sudah diupdate");
            }

        } else {
            OperationBinding operationBinding =
                bindings.getOperationBinding("Rollback");
            operationBinding.execute();
        }        
        
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblFlowStep);
    }

    public void addFlowStepPopup(PopupFetchEvent popupFetchEvent) {
            ADFContext adfCtx = ADFContext.getCurrent();
            Map pageFlowScope = adfCtx.getPageFlowScope();
            
        if (popupFetchEvent.getLaunchSourceClientId().contains("ctbInsert")) {
            BindingContainer bindings = getBindings();
            
            OperationBinding operationBinding =
                bindings.getOperationBinding("CreateInsert1");
            operationBinding.execute();

            pageFlowScope.put("flowStepStatus", "add");
        } else {
            pageFlowScope.put("flowStepStatus", "edit");
        }
    }

    public void hapusFlowStepDialog(DialogEvent dialogEvent) {
        BindingContainer bindings = getBindings();
        if (dialogEvent.getOutcome().name().equals("ok")) {
            OperationBinding operationCommit =
                bindings.getOperationBinding("Delete1");
            operationCommit.execute();
            
            OperationBinding operationRefresh =
                bindings.getOperationBinding("Commit");
            operationRefresh.execute();
            
            JSFUtils.addFacesInformationMessage("Flow step sudah dihapus.");

        } else {
            OperationBinding operationBinding =
                bindings.getOperationBinding("Rollback");
            operationBinding.execute();
        }
        
        OperationBinding operationBinding =
            bindings.getOperationBinding("Execute1");
        operationBinding.execute();
        
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblFlowStep);
    }

    public void addFlowApprovalPopupCanceledListener(PopupCanceledEvent popupCanceledEvent) {
        BindingContainer bindings = this.getBindings();
        OperationBinding operationBinding =
            bindings.getOperationBinding("Rollback");
        operationBinding.execute();
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblFlowList);
    }

    public void addFlowStepPopupCanceledListener(PopupCanceledEvent popupCanceledEvent) {
        BindingContainer bindings = this.getBindings();
        OperationBinding operationBinding =
            bindings.getOperationBinding("Rollback");
        operationBinding.execute();
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblFlowStep);
    }
}
