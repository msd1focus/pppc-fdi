package app.fpp.bean.masterdata;

import app.fpp.adfextensions.JSFUtils;

import oracle.adf.model.AttributeBinding;
import oracle.adf.model.BindingContext;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.event.DialogEvent;
import oracle.adf.view.rich.event.PopupCanceledEvent;
import oracle.adf.view.rich.event.PopupFetchEvent;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

public class MasterDataBean {
    private RichTable tblLookupValueList;
    private RichTable tblRegionList;

    public MasterDataBean() {
    }

    public BindingContainer getBindings() {
        return BindingContext.getCurrent().getCurrentBindingsEntry();
    }

    public void addSalesAreaDialogListener(DialogEvent dialogEvent) {
        BindingContainer bindings = getBindings();
        if (dialogEvent.getOutcome().name().equals("ok")) {
            OperationBinding operationBindingCommit =
                bindings.getOperationBinding("Commit");
            operationBindingCommit.execute();

            JSFUtils.addFacesInformationMessage("Sales area sudah ditambahkan");

        } else {
            OperationBinding operationBinding =
                bindings.getOperationBinding("Rollback");
            operationBinding.execute();
        }
    }

    public void addSalesAreaPopupFetchListener(PopupFetchEvent popupFetchEvent) {
        BindingContainer bindings = getBindings();
        OperationBinding operationBinding =
            bindings.getOperationBinding("CreateInsert");
        operationBinding.execute();
    }

    public void editSalesAreaDialogListener(DialogEvent dialogEvent) {
        BindingContainer bindings = getBindings();
        AttributeBinding regionCodeAttr =
            (AttributeBinding)bindings.getControlBinding("RegionCode");
        String regionCode = (String)regionCodeAttr.getInputValue();

        if (dialogEvent.getOutcome().name().equals("ok")) {
            OperationBinding operationBindingCommit =
                bindings.getOperationBinding("Commit");
            operationBindingCommit.execute();

            JSFUtils.addFacesInformationMessage("Sales area \"" + regionCode +
                                                "\" sudah diupdate");

        } else {
            OperationBinding operationBinding =
                bindings.getOperationBinding("Rollback");
            operationBinding.execute();
        }

    }

    public void addLookupValuePopupFetchListener(PopupFetchEvent popupFetchEvent) {
        BindingContainer bindings = getBindings();
        OperationBinding operationBinding =
            bindings.getOperationBinding("CreateInsert");
        operationBinding.execute();
    }

    public void addLookupValueDialogListener(DialogEvent dialogEvent) {        
        BindingContainer bindings = getBindings();
        if (dialogEvent.getOutcome().name().equals("ok")) {
            OperationBinding operationBindingCommit =
                bindings.getOperationBinding("Commit");
            operationBindingCommit.execute();

            JSFUtils.addFacesInformationMessage("Lookup value sudah ditambahkan");

        } else {
            OperationBinding operationBinding =
                bindings.getOperationBinding("Rollback");
            operationBinding.execute();
        }
    }

    public void editLookupValueDialogListener(DialogEvent dialogEvent) {
        BindingContainer bindings = getBindings();
        AttributeBinding labelAttr =
            (AttributeBinding)bindings.getControlBinding("Descr");
        String label = (String)labelAttr.getInputValue();

        if (dialogEvent.getOutcome().name().equals("ok")) {
            OperationBinding operationBindingCommit =
                bindings.getOperationBinding("Commit");
            operationBindingCommit.execute();

            JSFUtils.addFacesInformationMessage("Lookup value \"" + label +
                                                "\" sudah diupdate");

        } else {
            OperationBinding operationBinding =
                bindings.getOperationBinding("Rollback");
            operationBinding.execute();
        }

    }

    public void removeLookupValueDialogListener(DialogEvent dialogEvent) {
        BindingContainer bindings = getBindings();
        AttributeBinding labelAttr =
            (AttributeBinding)bindings.getControlBinding("Descr");
        String label = (String)labelAttr.getInputValue();

        if (dialogEvent.getOutcome().name().equals("ok")) {
            OperationBinding operationBindingDelete =
                bindings.getOperationBinding("Delete");
            operationBindingDelete.execute();
            
            OperationBinding operationBindingCommit =
                bindings.getOperationBinding("Commit");
            operationBindingCommit.execute();

            JSFUtils.addFacesInformationMessage("Lookup value \"" + label +
                                                "\" sudah dihapus");

        } else {
            OperationBinding operationBinding =
                bindings.getOperationBinding("Rollback");
            operationBinding.execute();
        }

    }

    public void addLookupValuePopupCanceledListener(PopupCanceledEvent popupCanceledEvent) {
        BindingContainer bindings = this.getBindings();
        OperationBinding operationBinding =
            bindings.getOperationBinding("Rollback");
        operationBinding.execute();
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblLookupValueList);
    }

    public void setTblLookupValueList(RichTable tblLookupValueList) {
        this.tblLookupValueList = tblLookupValueList;
    }

    public RichTable getTblLookupValueList() {
        return tblLookupValueList;
    }

    public void setTblRegionList(RichTable tblRegionList) {
        this.tblRegionList = tblRegionList;
    }

    public RichTable getTblRegionList() {
        return tblRegionList;
    }

    public void addSalesAreaPopupCanceledListener(PopupCanceledEvent popupCanceledEvent) {
        BindingContainer bindings = this.getBindings();
        OperationBinding operationBinding =
            bindings.getOperationBinding("Rollback");
        operationBinding.execute();
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblRegionList);
    }
}
