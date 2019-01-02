package app.fpp.bean.budgetsetting;

import app.fpp.adfextensions.ADFUtils;
import app.fpp.adfextensions.JSFUtils;
import java.math.BigDecimal;
import java.sql.SQLException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import oracle.adf.model.AttributeBinding;
import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.nav.RichCommandToolbarButton;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.event.DialogEvent;
import oracle.adf.view.rich.event.PopupFetchEvent;
import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;
import oracle.jbo.Row;

public class BudgetPostingBean {
    private RichInputText budgetPostingAmount;
    private RichInputText yearBudAsToDate;
    private RichInputText itMainYearlyBudgetAmount;
    private RichInputText itMainYearlyBudgetAmountSum;
    private RichCommandToolbarButton btnAddPostBudTrans;
    private RichPopup p1popup;

    public BudgetPostingBean() {
    }

    public BindingContainer getBindings() {
        return BindingContext.getCurrent().getCurrentBindingsEntry();
    }

    public void addBudgetPostTranPopupFetchListener(PopupFetchEvent popupFetchEvent) {
        BindingContainer bindings = getBindings();
        OperationBinding operationBinding =
            bindings.getOperationBinding("CreateInsertBudPostTran");
        operationBinding.execute();
    }

    public void addBudgetPostTranDialogListener(DialogEvent dialogEvent) {
        BindingContainer bindings = getBindings();
        if (dialogEvent.getOutcome().name().equals("ok")) {
            DCIteratorBinding dciterPosting=
                ADFUtils.findIterator("BudgetPostingView1Iterator");
            DCIteratorBinding dciterPostingTran=
                ADFUtils.findIterator("BudgetPostTranView1Iterator");    
           Row yearlyBudgetAmount= dciterPosting.getCurrentRow();
           String yearBudgetAmount=(String)yearlyBudgetAmount.getAttribute("YearlyBudgetAmountSum").toString();
            OperationBinding operationBindingCommit =
                bindings.getOperationBinding("Commit");
            operationBindingCommit.execute();

            OperationBinding refreshBudgetPosting =
                bindings.getOperationBinding("Execute");
            refreshBudgetPosting.execute();
        } else {
            OperationBinding operationBinding =
                bindings.getOperationBinding("Rollback");
            operationBinding.execute();
        }
    }

    public void setBudgetPostingAmount(RichInputText budgetPostingAmount) {
        this.budgetPostingAmount = budgetPostingAmount;
    }

    public RichInputText getBudgetPostingAmount() {
        return budgetPostingAmount;
    }

    public void setYearBudAsToDate(RichInputText yearBudAsToDate) {
        this.yearBudAsToDate = yearBudAsToDate;
    }

    public RichInputText getYearBudAsToDate() {
        return yearBudAsToDate;
    }

    public void amountValueChangeListener(ValueChangeEvent valueChangeEvent) {

        BindingContainer bindings = getBindings();
        AttributeBinding yearlyBudgetUsedAttr =
            (AttributeBinding)bindings.getControlBinding("YearlyBudgetUsed");
        
        String yearlyBudgetUsedStr = "0";
        try {
            yearlyBudgetUsedStr =
                yearlyBudgetUsedAttr.getInputValue().toString();
        } catch (Exception e) {
            yearlyBudgetUsedStr = "0";
        }
        
        
        BigDecimal budAmount = BigDecimal.ZERO;
        if (budgetPostingAmount.getValue() != null) {
            budAmount = new BigDecimal(budgetPostingAmount.getValue().toString().replaceAll(",", ""));
        }
        
        BigDecimal yearlyBudgetUsed = BigDecimal.ZERO;
        if (!yearlyBudgetUsedStr.equalsIgnoreCase("0")) {
            yearlyBudgetUsed = new BigDecimal(yearlyBudgetUsedStr.replaceAll(",", ""));
        } 
        
        BigDecimal budMainAmount = BigDecimal.ZERO;
        if(itMainYearlyBudgetAmount.getValue() !=null){
            budMainAmount = new BigDecimal(itMainYearlyBudgetAmount.getValue().toString().replaceAll(",", ""));
        }
                
        BigDecimal budAsToDate = yearlyBudgetUsed.add(budAmount).add(budMainAmount);
        oracle.jbo.domain.Number number = null;
        try {
            number = new oracle.jbo.domain.Number(budAsToDate.toString());
        } catch (SQLException e) {
            JSFUtils.addFacesErrorMessage("Error", e.getLocalizedMessage());
        }
        yearBudAsToDate.setValue(number);
    }

    public void setItMainYearlyBudgetAmount(RichInputText itMainYearlyBudgetAmount) {
        this.itMainYearlyBudgetAmount = itMainYearlyBudgetAmount;
    }

    public RichInputText getItMainYearlyBudgetAmount() {
        return itMainYearlyBudgetAmount;
    }

    public void setItMainYearlyBudgetAmountSum(RichInputText itMainYearlyBudgetAmountSum) {
        this.itMainYearlyBudgetAmountSum = itMainYearlyBudgetAmountSum;
    }

    public RichInputText getItMainYearlyBudgetAmountSum() {
        return itMainYearlyBudgetAmountSum;
    }

    public void showPopupPostBudget(ActionEvent actionEvent) {
        BindingContainer bindings =
            BindingContext.getCurrent().getCurrentBindingsEntry();
        oracle.jbo.domain.Number numberAsTodate = null;
        DCIteratorBinding dciterPosting=
            ADFUtils.findIterator("BudgetPostingView1Iterator");
        Row rBA=dciterPosting.getCurrentRow();
        String YearlyBudgetAmount=rBA.getAttribute("YearlyBudgetRemaining").toString();
        try {
        numberAsTodate = new oracle.jbo.domain.Number(YearlyBudgetAmount.toString());
        } catch (SQLException e) {
            JSFUtils.addFacesErrorMessage("Error", e.getLocalizedMessage());
        }
        if(dciterPosting.getEstimatedRowCount() > 0){
            yearBudAsToDate.setValue(numberAsTodate.toString());
        }
       
        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        p1popup.show(hints);
    }

    public void savePostBudgetEvent(ActionEvent actionEvent) {
        BindingContainer bindings = getBindings();
        OperationBinding operationBindingCommit =
            bindings.getOperationBinding("Commit");
        operationBindingCommit.execute();
        btnAddPostBudTrans.setDisabled(false);
        AdfFacesContext.getCurrentInstance().addPartialTarget(btnAddPostBudTrans);
    }

    public void setBtnAddPostBudTrans(RichCommandToolbarButton btnAddPostBudTrans) {
        this.btnAddPostBudTrans = btnAddPostBudTrans;
    }

    public RichCommandToolbarButton getBtnAddPostBudTrans() {
        return btnAddPostBudTrans;
    }

    public void setP1popup(RichPopup p1popup) {
        this.p1popup = p1popup;
    }

    public RichPopup getP1popup() {
        return p1popup;
    }
}
