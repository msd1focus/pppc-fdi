package app.fpp.bean.historyapproval;

import app.fpp.adfextensions.ADFUtils;
import app.fpp.adfextensions.JSFUtils;
import app.fpp.bean.confirmation.ListAddendumAmount;
import app.fpp.bean.confirmation.ListAddendumBudget;
import app.fpp.bean.confirmation.ListBudgetRemainingValidasi;
import app.fpp.bean.confirmation.ListStatusOverBudgetId;
import app.fpp.bean.confirmation.RealisasiGrMfByLine;
import app.fpp.bean.confirmation.realisasiTempClass;
import app.fpp.bean.useraccessmenu.UserData;
import app.fpp.model.am.HistoryApprovalAMImpl;
import app.fpp.model.views.confirmation.ProposalConfirmationViewImpl;
import app.fpp.model.views.historyapproval.HistoryApprovalViewImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.sql.CallableStatement;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import oracle.adf.model.AttributeBinding;
import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;
import oracle.adf.view.faces.bi.component.pivotTable.CellFormat;
import oracle.adf.view.faces.bi.component.pivotTable.DataCellContext;
import oracle.adf.view.faces.bi.component.pivotTable.HeaderCellContext;
import oracle.adf.view.faces.bi.component.pivotTable.HeaderFormatManager;
import oracle.adf.view.faces.bi.component.pivotTable.SizingManager;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputListOfValues;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.layout.RichShowDetailItem;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.nav.RichCommandToolbarButton;
import oracle.adf.view.rich.component.rich.output.RichOutputFormatted;
import oracle.adf.view.rich.component.rich.output.RichOutputText;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.event.DialogEvent;
import oracle.adf.view.rich.event.LaunchPopupEvent;
import oracle.adf.view.rich.event.PopupFetchEvent;
import oracle.adf.view.rich.event.QueryEvent;
import oracle.adf.view.rich.event.ReturnPopupEvent;
import oracle.adf.view.rich.model.AttributeCriterion;
import oracle.adf.view.rich.model.AttributeDescriptor;
import oracle.adf.view.rich.model.ConjunctionCriterion;
import oracle.adf.view.rich.model.Criterion;
import oracle.adf.view.rich.model.FilterableQueryDescriptor;
import oracle.adf.view.rich.model.ListOfValuesModel;
import oracle.adf.view.rich.render.ClientEvent;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

import oracle.jbo.Key;
import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;
import oracle.jbo.ViewCriteria;
import oracle.jbo.ViewObject;
import oracle.jbo.domain.DBSequence;
import oracle.jbo.uicli.binding.JUCtrlHierBinding;

import org.apache.myfaces.trinidad.component.UIXSwitcher;
import org.apache.myfaces.trinidad.event.ReturnEvent;
import org.apache.myfaces.trinidad.model.CollectionModel;
import org.apache.myfaces.trinidad.model.RowKeySet;

public class HistoryApprovalBean {

    private RichTable tblPropHistApproval;
    private RichTable tblSrcPropConfirm;
    private RichTable tblSrcPropAdendum;
    private UIXSwitcher switcherCustPost;
    private RichTable tblBudgetCustomer;
    private RichTable tblBudgetPosting;
    private RichInputText itBudgetPostPercent;
    private RichInputText itBudgetCustPercent;
    private String discTypePotongan = "POTONGAN";
    private String discTypeBiaya = "BIAYA";
    private String discTypePromoBarang = "PROMOBARANG";
    private RichInputText itBudgetOnTop;
    private RichInputText itBudgetMF;
    private RichInputText amount;
    private RichCommandButton btnHitungPercent;
    private RichPopup potmessage;
    private RichOutputFormatted otpesan;
    private RichInputText itBudgetPostAmount;
    private RichPopup popupDetailProd;
    private RichTable tblListProduct;
    private RichInputListOfValues itLovbudgetPost;
    private RichInputText itConfirmNo;
    private RichSelectOneChoice budgetBySoc;
    private RichShowDetailItem tabBudgetBind;
    private RichOutputText pathBind;
    private RichPopup pattacment;
    private UIXSwitcher switchMain;
    private RichInputListOfValues itLovBudgetBy;
    private RichPopup pcustomerBudget;
    private RichPopup ppostingPopup;
    private RichInputText budgetCustIdCust;
    private RichInputText budgetCustIdPost;
    private RichInputText itKombBudgetProdCust;
    private RichInputText itKombBudgetProdPost;
    private RichPopup potmessageRemove;
    private RichOutputFormatted otpesanRemove;
    private RichInputText itStatusCust;
    private RichInputText itStatusPost;
    private RichPopup potmessageRemovePost;
    private RichOutputFormatted otpesanRemovePost;
    private RichPopup popupCreatePr;
    private List<ListBudgetRemainingValidasi> listBudgetRemainingValidasi =
        new ArrayList<ListBudgetRemainingValidasi>();
    private List<ListAddendumBudget> listAddendumBudget =
        new ArrayList<ListAddendumBudget>();
    private List<realisasiTempClass> realisasiTemp =
        new ArrayList<realisasiTempClass>();
    private List<ListAddendumAmount> listAddendumAmount =
        new ArrayList<ListAddendumAmount>();
    private List<RealisasiGrMfByLine> realisasiGrMfByLine =
        new ArrayList<RealisasiGrMfByLine>();
    private List<ListStatusOverBudgetId> StatusOverBudgetId
        =new ArrayList<ListStatusOverBudgetId>();
    
    private RichPopup pConfCategoryPc;
    private RichInputListOfValues itlovCategoryPc;
    private RichCommandButton btnCreatePr;
    private RichCommandToolbarButton btnAddPost;
    private RichCommandToolbarButton btnAddCust;
    private RichInputListOfValues lovItemExpense;
    private RichInputListOfValues kdPostingOntop;
    private RichInputListOfValues kdPostingMf;
    private RichPopup potmessageKdOntop;
    private RichOutputText otpesanKdOntop;
    private RichPopup potmessageKdMf;
    private RichOutputText otpesanKdMf;
    private RichPopup poverBudgetStatus;
    private String printCountYes = "Y";
    private RichCommandButton btnPrintPreview;
    private RichCommandButton btnOkPP;
    private UIXSwitcher switchButtonMain;

    public HistoryApprovalBean() {
    }

    public BindingContainer getBindings() {
        return BindingContext.getCurrent().getCurrentBindingsEntry();
    }

    public static java.util.Date convertOracleDateToJavaUtilDate(oracle.jbo.domain.Date oracleDate) {
        if (oracleDate == null)
            return null;

        java.sql.Date javaSqlDate = oracleDate.dateValue();
        long javaMilliSeconds = javaSqlDate.getTime();
        return new java.util.Date(javaMilliSeconds);
    }

    public oracle.jbo.domain.Date getCurrentSysDate() {
        java.sql.Timestamp datetime =
            new java.sql.Timestamp(System.currentTimeMillis());
        oracle.jbo.domain.Date daTime = new oracle.jbo.domain.Date(datetime);
        return daTime;
    }

    public void setTblSrcPropConfirm(RichTable tblSrcPropConfirm) {
        this.tblSrcPropConfirm = tblSrcPropConfirm;
    }

    public RichTable getTblSrcPropConfirm() {
        return tblSrcPropConfirm;
    }

    public void setTblSrcPropAdendum(RichTable tblSrcPropAdendum) {
        this.tblSrcPropAdendum = tblSrcPropAdendum;
    }

    public RichTable getTblSrcPropAdendum() {
        return tblSrcPropAdendum;
    }

    public void setSwitcherCustPost(UIXSwitcher switcherCustPost) {
        this.switcherCustPost = switcherCustPost;
    }

    public UIXSwitcher getSwitcherCustPost() {
        return switcherCustPost;
    }

    public void budgetByValueChangeListener(ValueChangeEvent valueChangeEvent) {
        //Remove all budget customer row
        DCIteratorBinding dciterCust =
            ADFUtils.findIterator("ProdBudgetByCustView1Iterator");
        RowSetIterator rsiBudCust = dciterCust.getRowSetIterator();
        for (Row budCustRow : dciterCust.getAllRowsInRange()) {
            budCustRow.remove();
        }
        rsiBudCust.closeRowSetIterator();
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblBudgetCustomer);

        //Remove all budget posting row
        DCIteratorBinding dciterPost =
            ADFUtils.findIterator("ProdBudgetByPostView1Iterator");
        RowSetIterator rsiBudPost = dciterPost.getRowSetIterator();
        for (Row budPostRow : dciterPost.getAllRowsInRange()) {
            budPostRow.remove();
        }
        rsiBudPost.closeRowSetIterator();
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblBudgetPosting);

        AdfFacesContext.getCurrentInstance().addPartialTarget(switcherCustPost);
    }

    public void setTblBudgetCustomer(RichTable tblBudgetCustomer) {
        this.tblBudgetCustomer = tblBudgetCustomer;
    }

    public RichTable getTblBudgetCustomer() {
        return tblBudgetCustomer;
    }

    public void setTblBudgetPosting(RichTable tblBudgetPosting) {
        this.tblBudgetPosting = tblBudgetPosting;
    }

    public RichTable getTblBudgetPosting() {
        return tblBudgetPosting;
    }

    public void budgetAmountPostValueChangeListener(ValueChangeEvent valueChangeEvent) {
        try {
            String budcustId = "";
            DCBindingContainer bindings =
                (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
            DCIteratorBinding dcItteratorBindings =
                bindings.findIteratorBinding("ProdBudgetByPostView1Iterator");
            ViewObject voTableData = dcItteratorBindings.getViewObject();
            Row rowSelected = voTableData.getCurrentRow();
            if (rowSelected.getAttribute("BudgetById") != null) {
                budcustId = rowSelected.getAttribute("BudgetById").toString();
            }

            DCIteratorBinding dcItteratorProposal =
                bindings.findIteratorBinding("HistoryApprovalView1Iterator");
            Row rDiskon = dcItteratorProposal.getCurrentRow();
            String diskonType =
                rDiskon.getAttribute("DiscountType").toString();
            AttributeBinding RealisasiGrSelisihMfByLineMfAttr =
                (AttributeBinding)bindings.getControlBinding("RealisasiGrSelisihMfByLine");
            String RealisasiGrSelisihMfByLine =
                (String)RealisasiGrSelisihMfByLineMfAttr.getInputValue();

            AttributeBinding discountTypeAttr =
                (AttributeBinding)bindings.getControlBinding("DiscountType1");
            String discountType = (String)discountTypeAttr.getInputValue();

            AttributeBinding AddendumKeAttr =
                (AttributeBinding)bindings.getControlBinding("AddendumKe");
            String AddendumKe =
                (String)AddendumKeAttr.getInputValue() == null ? "0" :
                (String)AddendumKeAttr.getInputValue();

            AttributeBinding mekPenagihanAttr =
                (AttributeBinding)bindings.getControlBinding("MekanismePenagihan");
            Integer mekPenagihan = (Integer)mekPenagihanAttr.getInputValue();

            AttributeBinding DcvFlagAttr =
                (AttributeBinding)bindings.getControlBinding("DcvPcPrFlag");

            String DcvFlag = (String)DcvFlagAttr.getInputValue().toString();
            DCIteratorBinding dcItteratorDiscPromoProd =
                bindings.findIteratorBinding("PromoProdukView1Iterator");
            Row rPromoProd = dcItteratorDiscPromoProd.getCurrentRow();
            float IMF = 0;

            if (diskonType.equalsIgnoreCase(discTypePotongan)) {
                if (!AddendumKe.equalsIgnoreCase("0")) {
                    if (mekPenagihan.equals(0) &&
                        DcvFlag.equalsIgnoreCase("N")) {
                        if (!rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString().equalsIgnoreCase("0")) {
                                IMF =
                                 Float.valueOf(rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString() ==
                                     null ? "" :
                                     rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString());
                        } else {
                            IMF =
                                 Float.valueOf(rPromoProd.getAttribute("DiscMf").toString() == null ? "" :
                                rPromoProd.getAttribute("DiscMf").toString());
                        }

                    } else if (mekPenagihan.equals(0) &&
                               DcvFlag.equalsIgnoreCase("Y")) {
                    if (!rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString().equalsIgnoreCase("0")) {
                        IMF =
                         Float.valueOf(rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString() ==
                        null ? "" :
                        rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString());
                    } else {
                            IMF =
                             Float.valueOf(rPromoProd.getAttribute("DiscMf").toString() == null ? "" :
                            rPromoProd.getAttribute("DiscMf").toString());
                        }
                    } else if (mekPenagihan.equals(1)) {
                        IMF =
                         Float.valueOf(rPromoProd.getAttribute("DiscMf").toString() == null ? "" :
                        rPromoProd.getAttribute("DiscMf").toString());
                    }
                } else {
                    IMF =
                    Float.valueOf(rPromoProd.getAttribute("DiscMf").toString() == null ? "" :
                    rPromoProd.getAttribute("DiscMf").toString());
                }
                //               System.out.println("IMFD = "+IMF);
            } else if (diskonType.equalsIgnoreCase(discTypeBiaya)) {
                if (!AddendumKe.equalsIgnoreCase("0")) {
                    if (!rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString().equalsIgnoreCase("0")) {
                        IMF =
Float.valueOf(rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString() ==
                 null ? "" :
                 rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString());
                    } else {
                        IMF =
 Float.valueOf(rPromoProd.getAttribute("BiaMf").toString() == null ? "" :
                 rPromoProd.getAttribute("BiaMf").toString());
                    }
                } else {
                    IMF =
    Float.valueOf(rPromoProd.getAttribute("BiaMf").toString() == null ? "" :
                 rPromoProd.getAttribute("BiaMf").toString());
                }
                //               System.out.println("IMFB = "+IMF);
            } else if (diskonType.equalsIgnoreCase(discTypePromoBarang)) {
                if (!AddendumKe.equalsIgnoreCase("0")) {
                    if (mekPenagihan.equals(0) &&
                        DcvFlag.equalsIgnoreCase("N")) {
                        if (!rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString().equalsIgnoreCase("0")) {
                            IMF =
Float.valueOf(rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString() ==
                 null ? "" :
                 rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString());
                        } else {
                            IMF =
 Float.valueOf(rPromoProd.getAttribute("BrgBonusMf").toString() == null ?
                 "" : rPromoProd.getAttribute("BrgBonusMf").toString());
                        }
                    } else if (mekPenagihan.equals(0) &&
                               DcvFlag.equalsIgnoreCase("Y")) {
                        if (!rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString().equalsIgnoreCase("0")) {
                            IMF =
Float.valueOf(rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString() ==
                 null ? "" :
                 rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString());
                        } else {
                            IMF =
        Float.valueOf(rPromoProd.getAttribute("BrgBonusMf").toString() == null ?
                 "" : rPromoProd.getAttribute("BrgBonusMf").toString());
                        }
                    }
                } else {
                    IMF =
            Float.valueOf(rPromoProd.getAttribute("BrgBonusMf").toString() == null ?
                 "" : rPromoProd.getAttribute("BrgBonusMf").toString());
                    //               System.out.println("IMFPB = "+IMF);
                }
            }
            DCIteratorBinding dciterBudget =
                ADFUtils.findIterator("ProdBudgetByPostView1Iterator");
            
            int i = 1;
            float persenRow = 0;
            float budgetAmountRow = 0;
            for (Row r : dciterBudget.getAllRowsInRange()) {
                float amountFirst = 0;
                float persen = 0;
                String budId =r.getAttribute("BudgetById").toString() == null ? "" :r.getAttribute("BudgetById").toString();
                if(i != dciterBudget.getEstimatedRowCount()){
                    budgetAmountRow +=Float.valueOf(r.getAttribute("Amount").toString());
                    persenRow += Float.valueOf(r.getAttribute("Percentage").toString());
                }
                if (budcustId.equalsIgnoreCase(budId)) {
                    amountFirst = Float.valueOf(valueChangeEvent.getNewValue().toString().replaceAll(",",""));
                    if (amountFirst > 0) {
                       persen =(amountFirst / IMF ) * 100.00f;
                       BigDecimal overpersen = new BigDecimal(persen).add(new BigDecimal(persenRow).setScale(2, RoundingMode.HALF_UP));
                       BigDecimal percen=new BigDecimal(persen).setScale(2, RoundingMode.HALF_UP);
                       BigDecimal totalAmount =new BigDecimal(budgetAmountRow).add(new BigDecimal(amountFirst));
                       BigDecimal selisihpersen=overpersen.subtract(new BigDecimal(100));
                       if(selisihpersen.compareTo(BigDecimal.ZERO) > 0 && new BigDecimal(IMF).compareTo(totalAmount)==0){
                            r.setAttribute("Percentage", percen.subtract(selisihpersen).setScale(2, RoundingMode.HALF_UP));
                       }else{
                          r.setAttribute("Percentage", percen.setScale(2,RoundingMode.HALF_UP));
                       }
                        btnAddPost.setDisabled(false);
                       AdfFacesContext.getCurrentInstance().addPartialTarget(btnAddPost);
                   } else {
                       JSFUtils.addFacesWarningMessage("Amount harus bernilai lebih dari 0");
                       btnAddPost.setDisabled(true);
                       AdfFacesContext.getCurrentInstance().addPartialTarget(btnAddPost);
                   }
                }
                i=i+1;
            }
            AdfFacesContext.getCurrentInstance().addPartialTarget(tblBudgetPosting);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setItBudgetPostPercent(RichInputText itBudgetPostPercent) {
        this.itBudgetPostPercent = itBudgetPostPercent;
    }

    public RichInputText getItBudgetPostPercent() {
        return itBudgetPostPercent;
    }

    public void budgetAmountCustValueChangeListener(ValueChangeEvent valueChangeEvent) {
        try {
            String budcustId = "";
            DCBindingContainer bindings =
                (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
            DCIteratorBinding dcItteratorBindings =
                bindings.findIteratorBinding("ProdBudgetByCustView1Iterator");
            ViewObject voTableData = dcItteratorBindings.getViewObject();
            Row rowSelected = voTableData.getCurrentRow();
            if (rowSelected.getAttribute("BudgetById") != null) {
                budcustId = rowSelected.getAttribute("BudgetById").toString();
            }

            DCIteratorBinding dcItteratorProposal =
                bindings.findIteratorBinding("HistoryApprovalView1Iterator");
            Row rDiskon = dcItteratorProposal.getCurrentRow();
            String diskonType =
                rDiskon.getAttribute("DiscountType").toString();

            AttributeBinding RealisasiGrSelisihMfByLineMfAttr =
                (AttributeBinding)bindings.getControlBinding("RealisasiGrSelisihMfByLine");
            String RealisasiGrSelisihMfByLine =
                (String)RealisasiGrSelisihMfByLineMfAttr.getInputValue();

            AttributeBinding discountTypeAttr =
                (AttributeBinding)bindings.getControlBinding("DiscountType1");
            String discountType = (String)discountTypeAttr.getInputValue();

            AttributeBinding AddendumKeAttr =
                (AttributeBinding)bindings.getControlBinding("AddendumKe");
            String AddendumKe =
                (String)AddendumKeAttr.getInputValue() == null ? "0" :
                (String)AddendumKeAttr.getInputValue();

            AttributeBinding mekPenagihanAttr =
                (AttributeBinding)bindings.getControlBinding("MekanismePenagihan");
            Integer mekPenagihan = (Integer)mekPenagihanAttr.getInputValue();

            AttributeBinding DcvFlagAttr =
                (AttributeBinding)bindings.getControlBinding("DcvPcPrFlag");

            String DcvFlag = (String)DcvFlagAttr.getInputValue().toString();
            DCIteratorBinding dcItteratorDiscPromoProd =
                bindings.findIteratorBinding("PromoProdukView1Iterator");
            Row rPromoProd = dcItteratorDiscPromoProd.getCurrentRow();
            float IMF = 0;
                        if (diskonType.equalsIgnoreCase(discTypePotongan)) {
                            if (!AddendumKe.equalsIgnoreCase("0")) {
                                if (mekPenagihan.equals(0) &&
                                    DcvFlag.equalsIgnoreCase("N")) {
                                    if (!rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString().equalsIgnoreCase("0")) {
                                            IMF =
                                             Float.valueOf(rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString() ==
                                                 null ? "" :
                                                 rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString());
                                    } else {
                                        IMF =
                                             Float.valueOf(rPromoProd.getAttribute("DiscMf").toString() == null ? "" :
                                            rPromoProd.getAttribute("DiscMf").toString());
                                    }

                                } else if (mekPenagihan.equals(0) &&
                                           DcvFlag.equalsIgnoreCase("Y")) {
                                if (!rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString().equalsIgnoreCase("0")) {
                                    IMF =
                                     Float.valueOf(rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString() ==
                                    null ? "" :
                                    rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString());
                                } else {
                                        IMF =
                                         Float.valueOf(rPromoProd.getAttribute("DiscMf").toString() == null ? "" :
                                        rPromoProd.getAttribute("DiscMf").toString());
                                    }
                                } else if (mekPenagihan.equals(1)) {
                                    IMF =
                                     Float.valueOf(rPromoProd.getAttribute("DiscMf").toString() == null ? "" :
                                    rPromoProd.getAttribute("DiscMf").toString());
                                }
                                
                            } else {
                                IMF =
                                Float.valueOf(rPromoProd.getAttribute("DiscMf").toString() == null ? "" :
                                rPromoProd.getAttribute("DiscMf").toString());
                            }
                            //               System.out.println("IMFD = "+IMF);
                        } else if (diskonType.equalsIgnoreCase(discTypeBiaya)) {
                            if (!AddendumKe.equalsIgnoreCase("0")) {
                                if (!rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString().equalsIgnoreCase("0")) {
                                    IMF =
            Float.valueOf(rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString() ==
                             null ? "" :
                             rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString());
                                } else {
                                    IMF =
             Float.valueOf(rPromoProd.getAttribute("BiaMf").toString() == null ? "" :
                             rPromoProd.getAttribute("BiaMf").toString());
                                }
                            } else {
                                IMF =
                Float.valueOf(rPromoProd.getAttribute("BiaMf").toString() == null ? "" :
                             rPromoProd.getAttribute("BiaMf").toString());
                            }
                            //               System.out.println("IMFB = "+IMF);
                        } else if (diskonType.equalsIgnoreCase(discTypePromoBarang)) {
                            if (!AddendumKe.equalsIgnoreCase("0")) {
                                if (mekPenagihan.equals(0) &&
                                    DcvFlag.equalsIgnoreCase("N")) {
                                    if (!rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString().equalsIgnoreCase("0")) {
                                        IMF =
            Float.valueOf(rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString() ==
                             null ? "" :
                             rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString());
                                    } else {
                                        IMF =
             Float.valueOf(rPromoProd.getAttribute("BrgBonusMf").toString() == null ?
                             "" : rPromoProd.getAttribute("BrgBonusMf").toString());
                                    }
                                } else if (mekPenagihan.equals(0) &&
                                           DcvFlag.equalsIgnoreCase("Y")) {
                                    if (!rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString().equalsIgnoreCase("0")) {
                                        IMF =
            Float.valueOf(rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString() ==
                             null ? "" :
                             rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString());
                                    } else {
                                        IMF =
                    Float.valueOf(rPromoProd.getAttribute("BrgBonusMf").toString() == null ?
                             "" : rPromoProd.getAttribute("BrgBonusMf").toString());
                                    }
                                }
                            } else {
                                IMF =
                        Float.valueOf(rPromoProd.getAttribute("BrgBonusMf").toString() == null ?
                             "" : rPromoProd.getAttribute("BrgBonusMf").toString());
                                //               System.out.println("IMFPB = "+IMF);
                            }
                        }
            DCIteratorBinding dciterBudget =
                ADFUtils.findIterator("ProdBudgetByCustView1Iterator");
            BigDecimal grandTotalPersen=BigDecimal.ZERO;
            BigDecimal budgetAmount=BigDecimal.ZERO;
            for (Row ri : dciterBudget.getAllRowsInRange()) {
                oracle.jbo.domain.Number budgetAmountRow =
                    (oracle.jbo.domain.Number)ri.getAttribute("Amount");
                budgetAmount = budgetAmount.add(budgetAmountRow.getBigDecimalValue());
                BigDecimal persenRow=new BigDecimal(ri.getAttribute("Percentage").toString());
                grandTotalPersen=grandTotalPersen.add(persenRow);
            }
            
            int i = 1;
            float persenRow = 0;
            float budgetAmountRow = 0;
            for (Row r : dciterBudget.getAllRowsInRange()) {
                float amountFirst = 0;
                float persen = 0;
                String budId = r.getAttribute("BudgetById").toString() == null ? "" : r.getAttribute("BudgetById").toString();
                if(i != dciterBudget.getEstimatedRowCount()){
                    budgetAmountRow +=Float.valueOf(r.getAttribute("Amount").toString());
                    persenRow += Float.valueOf(r.getAttribute("Percentage").toString());
                }
                if (budcustId.equalsIgnoreCase(budId)) {
                    amountFirst = Float.valueOf(valueChangeEvent.getNewValue().toString().replaceAll(",",""));
                       if (amountFirst > 0) {
                           persen =(amountFirst / IMF ) * 100.00f;
                           BigDecimal overpersen = new BigDecimal(persen).add(new BigDecimal(persenRow).setScale(2, RoundingMode.HALF_UP));
                           BigDecimal percen=new BigDecimal(persen).setScale(2, RoundingMode.HALF_UP);
                           BigDecimal totalAmount =new BigDecimal(budgetAmountRow).add(new BigDecimal(amountFirst));
                           BigDecimal selisihpersen=overpersen.subtract(new BigDecimal(100));
                               if(selisihpersen.compareTo(BigDecimal.ZERO) > 0 && new BigDecimal(IMF).compareTo(totalAmount)==0){
                                    r.setAttribute("Percentage", percen.subtract(selisihpersen).setScale(2, RoundingMode.HALF_UP));
                               }else{
                                  r.setAttribute("Percentage", percen.setScale(2,RoundingMode.HALF_UP));
                               }
                           btnAddCust.setDisabled(false);
                           AdfFacesContext.getCurrentInstance().addPartialTarget(btnAddCust);
                       } else {
                           JSFUtils.addFacesWarningMessage("Amount harus bernilai lebih dari 0");
                           btnAddCust.setDisabled(true);
                           AdfFacesContext.getCurrentInstance().addPartialTarget(btnAddCust);
                       }
                }
                i=i+1;
            }
            AdfFacesContext.getCurrentInstance().addPartialTarget(tblBudgetCustomer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String toPercentage(float n) {
        return String.format("%.0f", n) + "%";
    }

    public static String toPercentage(float n, int digits) {
        return String.format("%." + digits + "f", n);
    }

    public void setItBudgetCustPercent(RichInputText itBudgetCustPercent) {
        this.itBudgetCustPercent = itBudgetCustPercent;
    }

    public RichInputText getItBudgetCustPercent() {
        return itBudgetCustPercent;
    }

    public void detailProdukDialogListener(DialogEvent dialogEvent) {
        BindingContainer bindings = getBindings();

        DCIteratorBinding dciterPropConfirm =
            ADFUtils.findIterator("HistoryApprovalView1Iterator");
        Key parentKey = dciterPropConfirm.getCurrentRow().getKey();

        DCIteratorBinding dciterPromoProduk =
            ADFUtils.findIterator("PromoProdukView1Iterator");
        Key promoProdKey = dciterPromoProduk.getCurrentRow().getKey();

        if (dialogEvent.getOutcome() == DialogEvent.Outcome.ok) {
            try {
                DCIteratorBinding dciterCust =
                    ADFUtils.findIterator("ProdBudgetByCustView1Iterator");
                float totalPercen = 0;
                if (dciterCust.getEstimatedRowCount() > 0) {
                    for (Row budCustRow : dciterCust.getAllRowsInRange()) {
                        totalPercen +=
                                Float.valueOf(budCustRow.getAttribute("Percentage").toString());
                    }
                }
                DCIteratorBinding dciterPost =
                    ADFUtils.findIterator("ProdBudgetByPostView1Iterator");
                float totalPercenPost = 0;
                if (dciterPost.getEstimatedRowCount() > 0) {
                    for (Row budPostRow : dciterPost.getAllRowsInRange()) {
                        totalPercenPost +=
                                Float.valueOf(budPostRow.getAttribute("Percentage").toString());
                    }
                }

                if (totalPercenPost > 100 || totalPercen > 100) {
                    showPopup("Persentase budget tidak boleh melebihi 100%.",
                              potmessage);
                }
            } catch (Exception e) {
                JSFUtils.addFacesErrorMessage(e.getLocalizedMessage());
            }
        }
        dciterPropConfirm.setCurrentRowWithKey(parentKey.toStringFormat(true));
        dciterPromoProduk.setCurrentRowWithKey(promoProdKey.toStringFormat(true));

        AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropHistApproval);
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
    }

    private void messageBox(String msg, FacesMessage.Severity severity) {
        String messageText = msg;
        FacesMessage fm = new FacesMessage(messageText);
        /**
                * set the type of the message.
                * Valid types: error, fatal,info,warning
                */
        fm.setSeverity(severity);
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, fm);
    }

    public void showPopup(String pesan, RichPopup p) {
        pesan = pesan.replaceAll("<nr>", "<br>");
        otpesan.setValue(pesan);
        AdfFacesContext adc = AdfFacesContext.getCurrentInstance();
        adc.addPartialTarget(otpesan);
        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        p.show(hints);
    }

    public void showPopupkdOntop(String pesan, RichPopup p) {
        otpesanKdOntop.setValue(pesan);
        AdfFacesContext adc = AdfFacesContext.getCurrentInstance();
        adc.addPartialTarget(otpesanKdOntop);
        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        p.show(hints);
    }

    public void showPopupkdMf(String pesan, RichPopup p) {
        otpesanKdMf.setValue(pesan);
        AdfFacesContext adc = AdfFacesContext.getCurrentInstance();
        adc.addPartialTarget(otpesanKdMf);
        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        p.show(hints);
    }

    public void detilProdukPopupFetchListener(PopupFetchEvent popupFetchEvent) {

        BindingContainer bindings = getBindings();

        OperationBinding executeBudgetPost =
            bindings.getOperationBinding("ExecuteBudgetPost");
        executeBudgetPost.execute();

        OperationBinding executeBudgetCust =
            bindings.getOperationBinding("ExecuteBudgetCust");
        executeBudgetCust.execute();
    }

    public void setItBudgetOnTop(RichInputText itBudgetOnTop) {
        this.itBudgetOnTop = itBudgetOnTop;
    }

    public RichInputText getItBudgetOnTop() {
        return itBudgetOnTop;
    }

    public void setItBudgetMF(RichInputText itBudgetMF) {
        this.itBudgetMF = itBudgetMF;
    }

    public RichInputText getItBudgetMF() {
        return itBudgetMF;
    }

    public void setAmount(RichInputText amount) {
        this.amount = amount;
    }

    public RichInputText getAmount() {
        return amount;
    }

    public void setBtnHitungPercent(RichCommandButton btnHitungPercent) {
        this.btnHitungPercent = btnHitungPercent;
    }

    public RichCommandButton getBtnHitungPercent() {
        return btnHitungPercent;
    }

    public void setPotmessage(RichPopup potmessage) {
        this.potmessage = potmessage;
    }

    public RichPopup getPotmessage() {
        return potmessage;
    }

    public void setOtpesan(RichOutputFormatted otpesan) {
        this.otpesan = otpesan;
    }

    public RichOutputFormatted getOtpesan() {
        return otpesan;
    }

    public void setItBudgetPostAmount(RichInputText itBudgetPostAmount) {
        this.itBudgetPostAmount = itBudgetPostAmount;
    }

    public RichInputText getItBudgetPostAmount() {
        return itBudgetPostAmount;
    }

    public void setPopupDetailProd(RichPopup popupDetailProd) {
        this.popupDetailProd = popupDetailProd;
    }

    public RichPopup getPopupDetailProd() {
        return popupDetailProd;
    }
       
    public void dialogEventCustomDetailProduct(ActionEvent actionEvent) {
        popupDetailProd.hide();
    }

    public void dialogCancelEventCustomDetailProduct(ActionEvent actionEvent) {
        popupDetailProd.hide();
    }

    public void showPDetailProduct(ActionEvent actionEvent) {
        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        popupDetailProd.show(hints);
    }

    public void bjpValueChangeListener(ValueChangeEvent valueChangeEvent) {
        DCIteratorBinding dciterPromoProduk =
            ADFUtils.findIterator("PromoProdukView1Iterator");
        for (Row promoProdRow : dciterPromoProduk.getAllRowsInRange()) {
            promoProdRow.setAttribute("ItemExpense", null);
        }

        AdfFacesContext.getCurrentInstance().addPartialTarget(tblListProduct);
    }

    public void setTblListProduct(RichTable tblListProduct) {
        this.tblListProduct = tblListProduct;
    }

    public RichTable getTblListProduct() {
        return tblListProduct;
    }

    public void addendumPopupFetchListener(PopupFetchEvent popupFetchEvent) {
        BindingContainer bindings =
            BindingContext.getCurrent().getCurrentBindingsEntry();
        AttributeBinding propNoAttr =
            (AttributeBinding)bindings.getControlBinding("ProposalNo");
        String propNo = (String)propNoAttr.getInputValue();

        DCIteratorBinding dciterPropAdendum =
            ADFUtils.findIterator("ProposalForAdendumView1Iterator");
        ViewObject propAdendumView = dciterPropAdendum.getViewObject();
        propAdendumView.setNamedWhereClauseParam("copySrc", propNo);
        propAdendumView.executeQuery();
    }

    public void propConfirmPopupFetchListener(PopupFetchEvent popupFetchEvent) {
        DCIteratorBinding dciterPropAdendum =
            ADFUtils.findIterator("ProposalForAdendumView1Iterator");
        ViewObject propAdendumView = dciterPropAdendum.getViewObject();
        propAdendumView.setNamedWhereClauseParam("copySrc", "");
        propAdendumView.executeQuery();
    }

    public void kombinasiBudgetPostLovEvent(ReturnPopupEvent returnPopupEvent) {
        RichInputListOfValues lovField =
            (RichInputListOfValues)returnPopupEvent.getSource();
        ListOfValuesModel lovModel = lovField.getModel();
        CollectionModel collectionModel =
            lovModel.getTableModel().getCollectionModel();
        JUCtrlHierBinding treeBinding =
            (JUCtrlHierBinding)collectionModel.getWrappedData();
        RowKeySet rks = (RowKeySet)returnPopupEvent.getReturnValue();
        List tableRowKey = (List)rks.iterator().next();
        DCIteratorBinding dciter = treeBinding.getDCIteratorBinding();
        Key key = (Key)tableRowKey.get(0);
        Row rw = dciter.findRowByKeyString(key.toStringFormat(true));

        DCIteratorBinding dciterPost =
            ADFUtils.findIterator("ProdBudgetByPostView1Iterator");
        if (dciterPost.getEstimatedRowCount() > 0) {
            Row budPostRow = dciterPost.getCurrentRow();
            budPostRow.setAttribute("BudgetCustId",
                                    rw.getAttribute("BudgetCustomerId").toString());
            //dciterPost.getDataControl().commitTransaction();
        }
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblBudgetPosting);
    }

    public void kombinasiBudgetCustLovEvent(ReturnPopupEvent returnPopupEvent) {
        RichInputListOfValues lovField =
            (RichInputListOfValues)returnPopupEvent.getSource();
        ListOfValuesModel lovModel = lovField.getModel();
        CollectionModel collectionModel =
            lovModel.getTableModel().getCollectionModel();
        JUCtrlHierBinding treeBinding =
            (JUCtrlHierBinding)collectionModel.getWrappedData();
        RowKeySet rks = (RowKeySet)returnPopupEvent.getReturnValue();
        List tableRowKey = (List)rks.iterator().next();
        DCIteratorBinding dciter = treeBinding.getDCIteratorBinding();
        Key key = (Key)tableRowKey.get(0);
        Row rw = dciter.findRowByKeyString(key.toStringFormat(true));

        DCIteratorBinding dciterPost =
            ADFUtils.findIterator("ProdBudgetByCustView1Iterator");
        if (dciterPost.getEstimatedRowCount() > 0) {
            Row budPostRow = dciterPost.getCurrentRow();
            budPostRow.setAttribute("BudgetCustId",
                                    rw.getAttribute("BudgetCustomerId").toString());
            //dciterPost.getDataControl().commitTransaction();
        }
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblBudgetCustomer);
    }

    public void setItLovbudgetPost(RichInputListOfValues itLovbudgetPost) {
        this.itLovbudgetPost = itLovbudgetPost;
    }

    public RichInputListOfValues getItLovbudgetPost() {
        return itLovbudgetPost;
    }

    public void setBudgetBySoc(RichSelectOneChoice budgetBySoc) {
        this.budgetBySoc = budgetBySoc;
    }

    public RichSelectOneChoice getBudgetBySoc() {
        return budgetBySoc;
    }

    public void setItConfirmNo(RichInputText itConfirmNo) {
        this.itConfirmNo = itConfirmNo;
    }

    public RichInputText getItConfirmNo() {
        return itConfirmNo;
    }

    public void setTabBudgetBind(RichShowDetailItem tabBudgetBind) {
        this.tabBudgetBind = tabBudgetBind;
    }

    public RichShowDetailItem getTabBudgetBind() {
        return tabBudgetBind;
    }

    public void setPathBind(RichOutputText pathBind) {
        this.pathBind = pathBind;
    }

    public RichOutputText getPathBind() {
        return pathBind;
    }

    public void downloadAction(FacesContext facesContext,
                               OutputStream outputStream) throws IOException {
        File filed = new File(pathBind.getValue().toString());
        FileInputStream fis;
        byte[] b;
        try {
            fis = new FileInputStream(filed);

            int n;
            while ((n = fis.available()) > 0) {
                b = new byte[n];
                int result = fis.read(b);
                outputStream.write(b, 0, b.length);
                if (result == -1)
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        outputStream.flush();
    }

    public void setPattacment(RichPopup pattacment) {
        this.pattacment = pattacment;
    }

    public RichPopup getPattacment() {
        return pattacment;
    }

    public void showPAttacment(ActionEvent actionEvent) {
        DCBindingContainer bindings =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dciter =
            ADFUtils.findIterator("HistoryApprovalView1Iterator");
        DBSequence propId =
            (DBSequence)dciter.getCurrentRow().getAttribute("ProposalId");
        DCIteratorBinding dcItteratorBindings =
            bindings.findIteratorBinding("UploadDownloadView1Iterator");
        ViewObject vo = dcItteratorBindings.getViewObject();
        vo.setWhereClause("UploadDownload.PROPOSAL_ID = " + "'" +
                          propId.getValue() + "'");
        vo.executeQuery();

        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        pattacment.show(hints);
    }
    
    public void saveConfirm(ActionEvent actionEvent) {
        AdfFacesContext context = AdfFacesContext.getCurrentInstance();
        Map vScope = context.getViewScope();
        vScope.put("Action", "save");
        Key keyRow = (Key)vScope.get("ProdRowKey");

        DCIteratorBinding dciterPromoProduk =
            ADFUtils.findIterator("PromoProdukView1Iterator");
        
        Key promoProdKey = null;
        if (dciterPromoProduk.getEstimatedRowCount() > 0) {
            if (keyRow != null) {
                promoProdKey = keyRow;
            } else {
                promoProdKey = dciterPromoProduk.getCurrentRow().getKey();
            }
        }
        
        BindingContainer bindings = getBindings();
        AttributeBinding confirmNoAttr =
            (AttributeBinding)bindings.getControlBinding("ConfirmNo");
        String confirmNo =
            (String)confirmNoAttr.getInputValue() == null ? "" : (String)confirmNoAttr.getInputValue();

        AttributeBinding categoryPcAttr =
            (AttributeBinding)bindings.getControlBinding("CategoryPc");
        String categoryPc = (String)categoryPcAttr.getInputValue();

        String existingCatPc =
            confirmNo.substring(0, Math.min(confirmNo.length(), 3));

        if (categoryPc == null) {
            StringBuilder message = new StringBuilder("<html><body>");
            message.append("<p>Anda belum mengisi \"Category PC\", data sudah disimpan</br>");
            message.append("tetapi belum dapat melakukan \"Create PR\" atau \"Create Modifier\"</p>");
            message.append("</body></html>");
            JSFUtils.addFacesWarningMessage(message.toString());
        } else {
            String currCatPc =
                (String)ADFContext.getCurrent().getSessionScope().get("currCatPc");
            String currNoConf =
                (String)ADFContext.getCurrent().getSessionScope().get("currNoConfirm");

            if (confirmNo.equalsIgnoreCase("Auto Generated") ||
                !existingCatPc.equalsIgnoreCase(categoryPc)) {
                RichPopup.PopupHints hints = new RichPopup.PopupHints();
                pConfCategoryPc.show(hints);
            } else {
                OperationBinding operationBindingCommit =
                    bindings.getOperationBinding("Commit");
                operationBindingCommit.execute();
            }

        }

        AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropHistApproval);

        if (promoProdKey != null) {
            dciterPromoProduk.setCurrentRowWithKey(promoProdKey.toStringFormat(true));
        }

        // Destroy view scope
        if (keyRow != null) {
            AdfFacesContext.getCurrentInstance().getViewScope().put("ProdRowKey",
                                                                    null);
        }
    }

    public void setSwitchMain(UIXSwitcher switchMain) {
        this.switchMain = switchMain;
    }

    public UIXSwitcher getSwitchMain() {
        return switchMain;
    }

    public void setEL(String el, Object val) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ELContext elContext = facesContext.getELContext();
        ExpressionFactory expressionFactory =
            facesContext.getApplication().getExpressionFactory();
        ValueExpression exp =
            expressionFactory.createValueExpression(elContext, el,
                                                    Object.class);
        exp.setValue(elContext, val);
    }

    public void setBudgetCustIdCust(RichInputText budgetCustIdCust) {
        this.budgetCustIdCust = budgetCustIdCust;
    }

    public RichInputText getBudgetCustIdCust() {
        return budgetCustIdCust;
    }

    public void setBudgetCustIdPost(RichInputText budgetCustIdPost) {
        this.budgetCustIdPost = budgetCustIdPost;
    }

    public RichInputText getBudgetCustIdPost() {
        return budgetCustIdPost;
    }

    public void setItKombBudgetProdCust(RichInputText itKombBudgetProdCust) {
        this.itKombBudgetProdCust = itKombBudgetProdCust;
    }

    public RichInputText getItKombBudgetProdCust() {
        return itKombBudgetProdCust;
    }

    public void setItKombBudgetProdPost(RichInputText itKombBudgetProdPost) {
        this.itKombBudgetProdPost = itKombBudgetProdPost;
    }

    public RichInputText getItKombBudgetProdPost() {
        return itKombBudgetProdPost;
    }

    public void setPotmessageRemove(RichPopup potmessageRemove) {
        this.potmessageRemove = potmessageRemove;
    }

    public RichPopup getPotmessageRemove() {
        return potmessageRemove;
    }

    public void setOtpesanRemove(RichOutputFormatted otpesanRemove) {
        this.otpesanRemove = otpesanRemove;
    }

    public RichOutputFormatted getOtpesanRemove() {
        return otpesanRemove;
    }

    public void setPcustomerBudget(RichPopup pcustomerBudget) {
        this.pcustomerBudget = pcustomerBudget;
    }

    public RichPopup getPcustomerBudget() {
        return pcustomerBudget;
    }

    public void lovbudgetCustomer(ActionEvent actionEvent) {
        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        pcustomerBudget.show(hints);

    }

    public void setPpostingPopup(RichPopup ppostingPopup) {
        this.ppostingPopup = ppostingPopup;
    }

    public RichPopup getPpostingPopup() {
        return ppostingPopup;
    }

    public void budgetByPopupReturnListener(ReturnPopupEvent returnPopupEvent) {
        //Remove all budget customer row
        DCIteratorBinding dciterCust =
            ADFUtils.findIterator("ProdBudgetByCustView1Iterator");
        RowSetIterator rsiBudCust = dciterCust.getRowSetIterator();
        for (Row budCustRow : dciterCust.getAllRowsInRange()) {
            budCustRow.remove();
        }
        rsiBudCust.closeRowSetIterator();
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblBudgetCustomer);

        //Remove all budget posting row
        DCIteratorBinding dciterPost =
            ADFUtils.findIterator("ProdBudgetByPostView1Iterator");
        RowSetIterator rsiBudPost = dciterPost.getRowSetIterator();
        for (Row budPostRow : dciterPost.getAllRowsInRange()) {
            budPostRow.remove();
        }
        rsiBudPost.closeRowSetIterator();
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblBudgetPosting);

        AdfFacesContext.getCurrentInstance().addPartialTarget(switcherCustPost);
    }

    public void setItLovBudgetBy(RichInputListOfValues itLovBudgetBy) {
        this.itLovBudgetBy = itLovBudgetBy;
    }

    public RichInputListOfValues getItLovBudgetBy() {
        return itLovBudgetBy;
    }

    public Row getTableRow(String iterator) {
        DCBindingContainer bindings =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcItteratorBindings =
            bindings.findIteratorBinding(iterator);
        ViewObject voTableData = dcItteratorBindings.getViewObject();
        Row rowSelected = voTableData.getCurrentRow();
        return rowSelected;
    }

    public String getRow(Row r, String columnName) {
        return r.getAttribute(columnName) == null ? "" :
               r.getAttribute(columnName).toString();
    }

    public void showpostingLov(ActionEvent actionEvent) {
        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        ppostingPopup.show(hints);
    }

    public void dialogPostPopupLov(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome() == DialogEvent.Outcome.ok) {
            Row rowSelected = getTableRow("KomBudgetPostLov1Iterator");
            DCIteratorBinding dciterProdBudgetByPostID =
                ADFUtils.findIterator("ProdBudgetByPostView1Iterator");
            DCIteratorBinding dciterProdBudgetByPost =
                ADFUtils.findIterator("ProdBudgetByPostView1Iterator");
            ViewObject voTableDataOT = dciterProdBudgetByPost.getViewObject();
            Row r = dciterProdBudgetByPost.getCurrentRow();
            Row row = dciterProdBudgetByPostID.getCurrentRow();
            String idbudget = row.getAttribute("BudgetById").toString();

            if (rowSelected != null) {
                String idUpdate = row.getAttribute("BudgetById").toString();
                if (idbudget.equalsIgnoreCase(idUpdate)) {
                    r.setAttribute("BudgetCustId",
                                   getRow(rowSelected, "BudgetCustomerId"));
                    r.setAttribute("KombinasiBudget",
                                   getRow(rowSelected, "CombinationDescription"));
                    itKombBudgetProdPost.setSubmittedValue(getRow(rowSelected,
                                                                  "CombinationDescription").toString());
                    budgetCustIdPost.setSubmittedValue(getRow(rowSelected,
                                                              "BudgetCustomerId"));
                    AdfFacesContext.getCurrentInstance().addPartialTarget(budgetCustIdPost);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(itKombBudgetProdPost);
                }
                AdfFacesContext.getCurrentInstance().addPartialTarget(tblBudgetPosting);
                RichPopup panggilPopup = this.getPpostingPopup();
                panggilPopup.hide();
            } else {
                System.out.println("Tidak ada data pada tabel");
            }
        } else {
            RichPopup panggilPopup = this.getPpostingPopup();
            panggilPopup.hide();
        }
    }

    public void dialogCustPopupLov(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome() == DialogEvent.Outcome.ok) {
            Row rowSelected = getTableRow("KomBudgetCustLov1Iterator");
            DCIteratorBinding dciterProdBudgetByCustID =
                ADFUtils.findIterator("ProdBudgetByCustView1Iterator");
            DCIteratorBinding dciterProdBudgetByCust =
                ADFUtils.findIterator("ProdBudgetByCustView1Iterator");
            ViewObject voTableDataOT = dciterProdBudgetByCust.getViewObject();
            Row r = dciterProdBudgetByCust.getCurrentRow();
            Row row = dciterProdBudgetByCustID.getCurrentRow();
            String idbudget = row.getAttribute("BudgetById").toString();
            if (rowSelected != null) {
                //            for (Row r : dciterProdBudgetByCust.getAllRowsInRange()){
                //             RowSetIterator iterCust= voTableDataOT.createRowSetIterator(null);
                //             while(iterCust.hasNext()){
                //                Row r =iterCust.next();
                String idUpdate = row.getAttribute("BudgetById").toString();
                if (idbudget.equalsIgnoreCase(idUpdate)) {
                    r.setAttribute("BudgetCustId",
                                   getRow(rowSelected, "BudgetCustomerId"));
                    r.setAttribute("KombinasiBudget",
                                   getRow(rowSelected, "CombinationDescription"));
                    //                    rowSelectedSet.setAttribute("BudgetCustId", getRow(rowSelected,"BudgetCustomerId"));
                    //                System.out.println("budgetCustID "+getRow(rowSelected,"BudgetCustomerId"));
                    //                setEL("#{row.bindings.KombinasiBudget.inputValue}",getRow(rowSelected,"CombinationDescription"));
                    //                System.out.println("KombinasiBudget "+getRow(rowSelected,"CombinationDescription"));
                    itKombBudgetProdCust.setSubmittedValue(getRow(rowSelected,
                                                                  "CombinationDescription").toString());
                    //                itKombBudgetProdCust.setSubmittedValue(getRow(rowSelected,"CombinationDescription"));
                    budgetCustIdCust.setSubmittedValue(getRow(rowSelected,
                                                              "BudgetCustomerId").toString());
                    AdfFacesContext.getCurrentInstance().addPartialTarget(itKombBudgetProdCust);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(budgetCustIdCust);
                    //                    dciterProdBudgetByCust.getDataControl().commitTransaction();
                    //                    dciterProdBudgetByCust.executeQuery();
                    //                }
                }
                //               iterCust.closeRowSetIterator();
                AdfFacesContext.getCurrentInstance().addPartialTarget(tblBudgetCustomer);

                RichPopup panggilPopup = this.getPcustomerBudget();
                panggilPopup.hide();
            } else {
                System.out.println("Tidak ada data pada tabel");
            }
        } else {
            RichPopup panggilPopup = this.getPcustomerBudget();
            panggilPopup.hide();
        }
    }

    public void showPopup1(String pesan, RichPopup p) {
        pesan = pesan.replaceAll("<nr>", "<br>");
        otpesanRemove.setValue(pesan);
        AdfFacesContext adc = AdfFacesContext.getCurrentInstance();
        adc.addPartialTarget(otpesanRemove);
        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        p.show(hints);
    }

    public void showPopup2(String pesan, RichPopup p) {
        pesan = pesan.replaceAll("<nr>", "<br>");
        otpesanRemovePost.setValue(pesan);
        AdfFacesContext adc = AdfFacesContext.getCurrentInstance();
        adc.addPartialTarget(otpesanRemovePost);
        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        p.show(hints);
    }

    public void removeBudgetevent(ActionEvent actionEvent) {
        BindingContainer bindings =
            BindingContext.getCurrent().getCurrentBindingsEntry();
        String budgetCustIdFdi = "";
        String budgetId = "";
        int Checkrowstatus = 0;
        BigDecimal amountSubs = BigDecimal.ZERO;
        DCIteratorBinding dciterCust =
            ADFUtils.findIterator("ProdBudgetByCustView1Iterator");
        ViewObject voTableData = dciterCust.getViewObject();
        Row rowSelected = voTableData.getCurrentRow();
        if (dciterCust.getEstimatedRowCount() > 0) {
            budgetId =
                    rowSelected.getAttribute("BudgetById") == null ? " " : rowSelected.getAttribute("BudgetById").toString();
            budgetCustIdFdi =
                    rowSelected.getAttribute("BudgetCustId") == null ? " " :
                    rowSelected.getAttribute("BudgetCustId").toString();
            amountSubs =
                    new BigDecimal(rowSelected.getAttribute("Amount").toString());
            Checkrowstatus =
                    Integer.parseInt(rowSelected.getAttribute("CheckRowStatus").toString());
            if (Checkrowstatus == 0) {
                rowSelected.remove();
                btnAddCust.setDisabled(false);
                AdfFacesContext.getCurrentInstance().addPartialTarget(btnAddCust);
            } else {
                showPopup1("Yakin akan menghapus budget ini?<nr> Jika ya, akan mengupdate \"Amount Remaining\" budget.",
                           potmessageRemove);
            }
        } else {
            rowSelected.remove();
            itLovBudgetBy.setDisabled(false);
            btnAddCust.setDisabled(false);
            AdfFacesContext.getCurrentInstance().addPartialTarget(itLovBudgetBy);
            AdfFacesContext.getCurrentInstance().addPartialTarget(btnAddCust);
        }
    }

    public void removePostBean(ActionEvent actionEvent) {
        BindingContainer bindings =
            BindingContext.getCurrent().getCurrentBindingsEntry();
        String budgetId = "";
        String budgetCustIdFdi = "";
        int Checkrowstatus = 0;
        BigDecimal amountSubs = BigDecimal.ZERO;
        DCIteratorBinding dciterCust =
            ADFUtils.findIterator("ProdBudgetByPostView1Iterator");
        ViewObject voTableData = dciterCust.getViewObject();
        Row rowSelected = voTableData.getCurrentRow();
        if (dciterCust.getEstimatedRowCount() > 0) {
            budgetId =
                    rowSelected.getAttribute("BudgetById") == null ? " " : rowSelected.getAttribute("BudgetById").toString();
            budgetCustIdFdi =
                    rowSelected.getAttribute("BudgetCustId") == null ? " " :
                    rowSelected.getAttribute("BudgetCustId").toString();
            amountSubs =
                    new BigDecimal(rowSelected.getAttribute("Amount").toString());
            Checkrowstatus =
                    Integer.parseInt(rowSelected.getAttribute("CheckRowStatus").toString());
            if (Checkrowstatus == 0) {
                rowSelected.remove();
                btnAddPost.setDisabled(false);
                AdfFacesContext.getCurrentInstance().addPartialTarget(btnAddPost);
            } else {
                showPopup2("Yakin akan menghapus budget ini?<nr> Jika ya, akan mengupdate \"Amount Remaining\" budget.",
                           potmessageRemovePost);
            }
        } else {
            rowSelected.remove();
            btnAddPost.setDisabled(false);
            itLovBudgetBy.setDisabled(false);
            AdfFacesContext.getCurrentInstance().addPartialTarget(itLovBudgetBy);
            AdfFacesContext.getCurrentInstance().addPartialTarget(btnAddPost);
        }
    }


    public void setItStatusCust(RichInputText itStatusCust) {
        this.itStatusCust = itStatusCust;
    }

    public RichInputText getItStatusCust() {
        return itStatusCust;
    }

    public RichInputText getItStatusPost() {
        return itStatusPost;
    }

    public void setItStatusPost(RichInputText itStatusPost) {
        this.itStatusPost = itStatusPost;
    }

    public void setPotmessageRemovePost(RichPopup potmessageRemovePost) {
        this.potmessageRemovePost = potmessageRemovePost;
    }

    public RichPopup getPotmessageRemovePost() {
        return potmessageRemovePost;
    }

    public void setOtpesanRemovePost(RichOutputFormatted otpesanRemovePost) {
        this.otpesanRemovePost = otpesanRemovePost;
    }

    public RichOutputFormatted getOtpesanRemovePost() {
        return otpesanRemovePost;
    }

    public void setPopupCreatePr(RichPopup popupCreatePr) {
        this.popupCreatePr = popupCreatePr;
    }

    public RichPopup getPopupCreatePr() {
        return popupCreatePr;
    }

    public void setListBudgetRemainingValidasi(List<ListBudgetRemainingValidasi> listBudgetRemainingValidasi) {
        this.listBudgetRemainingValidasi = listBudgetRemainingValidasi;
    }

    public List<ListBudgetRemainingValidasi> getListBudgetRemainingValidasi() {
        return listBudgetRemainingValidasi;
    }

    public void catPcRevLogPopupFetchListener(PopupFetchEvent popupFetchEvent) {
        BindingContainer bindings = getBindings();
        OperationBinding catPcRevLog =
            bindings.getOperationBinding("ExecuteCatPcRevLog");
        catPcRevLog.execute();
    }

    public void setPconfCategoryPc(RichPopup pConfCategoryPc) {
        this.pConfCategoryPc = pConfCategoryPc;
    }

    public RichPopup getPconfCategoryPc() {
        return pConfCategoryPc;
    }

    public void setItlovCategoryPc(RichInputListOfValues itlovCategoryPc) {
        this.itlovCategoryPc = itlovCategoryPc;
    }

    public RichInputListOfValues getItlovCategoryPc() {
        return itlovCategoryPc;
    }

    public void refreshConfirmList(ActionEvent actionEvent) {
        BindingContainer bindings = getBindings();
        DCIteratorBinding parentIter =
            (DCIteratorBinding)bindings.get("HistoryApprovalView1Iterator");
        ProposalConfirmationViewImpl propConfVo =
            (ProposalConfirmationViewImpl)parentIter.getViewObject();
        //View Criteria without bind variable
        ViewCriteria vc = propConfVo.getViewCriteria("PropConfirmAllStatusVC");
        propConfVo.applyViewCriteria(vc);
        propConfVo.executeQuery();
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropHistApproval);
        /*
        //Get current row key
        Key parentKey = null;
        if (parentIter.getEstimatedRowCount() > 0) {
            parentKey = parentIter.getCurrentRow().getKey();
        }

        OperationBinding executePropConfirm =
            bindings.getOperationBinding("ExecutePropConfirm");
        executePropConfirm.execute();
        if (parentIter.getEstimatedRowCount() > 0 && parentKey != null) {
            parentIter.setCurrentRowWithKey(parentKey.toStringFormat(true));
        }
        */
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropHistApproval);
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
    }

    public void setBtnCreatePr(RichCommandButton btnCreatePr) {
        this.btnCreatePr = btnCreatePr;
    }

    public RichCommandButton getBtnCreatePr() {
        return btnCreatePr;
    }

    public void setBtnAddPost(RichCommandToolbarButton btnAddPost) {
        this.btnAddPost = btnAddPost;
    }

    public RichCommandToolbarButton getBtnAddPost() {
        return btnAddPost;
    }

    public void setBtnAddCust(RichCommandToolbarButton btnAddCust) {
        this.btnAddCust = btnAddCust;
    }

    public RichCommandToolbarButton getBtnAddCust() {
        return btnAddCust;
    }

    public void popupClearItemExpense(LaunchPopupEvent launchPopupEvent) {

    }

    public void setLovItemExpense(RichInputListOfValues lovItemExpense) {
        this.lovItemExpense = lovItemExpense;
    }

    public RichInputListOfValues getLovItemExpense() {
        return lovItemExpense;
    }

    public void setKdPostingOntop(RichInputListOfValues kdPostingOntop) {
        this.kdPostingOntop = kdPostingOntop;
    }

    public RichInputListOfValues getKdPostingOntop() {
        return kdPostingOntop;
    }

    public void setKdPostingMf(RichInputListOfValues kdPostingMf) {
        this.kdPostingMf = kdPostingMf;
    }

    public RichInputListOfValues getKdPostingMf() {
        return kdPostingMf;
    }

    public void setPotmessageKdOntop(RichPopup potmessageKdOntop) {
        this.potmessageKdOntop = potmessageKdOntop;
    }

    public RichPopup getPotmessageKdOntop() {
        return potmessageKdOntop;
    }

    public void setOtpesanKdOntop(RichOutputText otpesanKdOntop) {
        this.otpesanKdOntop = otpesanKdOntop;
    }

    public RichOutputText getOtpesanKdOntop() {
        return otpesanKdOntop;
    }

    public void setPotmessageKdMf(RichPopup potmessageKdMf) {
        this.potmessageKdMf = potmessageKdMf;
    }

    public RichPopup getPotmessageKdMf() {
        return potmessageKdMf;
    }

    public void setOtpesanKdMf(RichOutputText otpesanKdMf) {
        this.otpesanKdMf = otpesanKdMf;
    }

    public RichOutputText getOtpesanKdMf() {
        return otpesanKdMf;
    }

    public void dialogListenerKdOntop(DialogEvent dialogEvent) {
        DCIteratorBinding dciterPromoProduk =
            ADFUtils.findIterator("PromoProdukView1Iterator");
        Key promoProdKey = dciterPromoProduk.getCurrentRow().getKey();

        if (dialogEvent.getOutcome() == DialogEvent.Outcome.ok) {
            Row promoProdRow = dciterPromoProduk.getCurrentRow();
            promoProdRow.setAttribute("KodePosting", null);
            kdPostingOntop.setSubmittedValue(null);
            AdfFacesContext.getCurrentInstance().addPartialTarget(kdPostingOntop);
            AdfFacesContext.getCurrentInstance().addPartialTarget(tblListProduct);
        }
        dciterPromoProduk.setCurrentRowWithKey(promoProdKey.toStringFormat(true));
    }

    public void dialogListenerKdMf(DialogEvent dialogEvent) {
        DCIteratorBinding dciterPromoProduk =
            ADFUtils.findIterator("PromoProdukView1Iterator");
        Key promoProdKey = dciterPromoProduk.getCurrentRow().getKey();
        if (dialogEvent.getOutcome() == DialogEvent.Outcome.ok) {
            Row promoProdRow = dciterPromoProduk.getCurrentRow();
            promoProdRow.setAttribute("KodePostingMf", null);
            kdPostingMf.setSubmittedValue(null);
            AdfFacesContext.getCurrentInstance().addPartialTarget(kdPostingMf);
            AdfFacesContext.getCurrentInstance().addPartialTarget(tblListProduct);
        }
        dciterPromoProduk.setCurrentRowWithKey(promoProdKey.toStringFormat(true));
    }

    public void categoryPcLaunchPopUpListener(LaunchPopupEvent launchPopupEvent) {
        BindingContainer bindings = getBindings();
        AttributeBinding confirmNoAttr =
            (AttributeBinding)bindings.getControlBinding("ConfirmNo");
        String confirmNo =
            (String)confirmNoAttr.getInputValue() == null ? "" : (String)confirmNoAttr.getInputValue();

        AttributeBinding categoryPcAttr =
            (AttributeBinding)bindings.getControlBinding("CategoryPc");
        String categoryPc = (String)categoryPcAttr.getInputValue();

        String existingCatPc =
            confirmNo.substring(0, Math.min(confirmNo.length(), 3));

        if (categoryPc == null) {
            StringBuilder message = new StringBuilder("<html><body>");
            message.append("<p>Anda belum mengisi \"Category PC\", data sudah disimpan</br>");
            message.append("tetapi belum dapat melakukan \"Create PR\" atau \"Create Modifier\"</p>");
            message.append("</body></html>");
            JSFUtils.addFacesWarningMessage(message.toString());
        } else {
            String currCatPc =
                (String)ADFContext.getCurrent().getSessionScope().get("currCatPc");
            String currNoConf =
                (String)ADFContext.getCurrent().getSessionScope().get("currNoConfirm");

            if (!confirmNo.equalsIgnoreCase("Auto Generated")) {
                RichPopup.PopupHints hints = new RichPopup.PopupHints();
                pConfCategoryPc.show(hints);
            } else {

                //                OperationBinding operationBindingCommit =
                //                    bindings.getOperationBinding("Commit");
                //                operationBindingCommit.execute();
            }
        }

        AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropHistApproval);
    }

    public void categoryPcReturnPopUpListener(ReturnPopupEvent returnPopupEvent) {
        BindingContainer bindings = getBindings();
        AttributeBinding confirmNoAttr =
            (AttributeBinding)bindings.getControlBinding("ConfirmNo");
        String confirmNo =
            (String)confirmNoAttr.getInputValue() == null ? "" : (String)confirmNoAttr.getInputValue();

        AttributeBinding categoryPcAttr =
            (AttributeBinding)bindings.getControlBinding("CategoryPc");
        String categoryPc = (String)categoryPcAttr.getInputValue();

        String existingCatPc =
            confirmNo.substring(0, Math.min(confirmNo.length(), 3));

        if (categoryPc == null) {
            StringBuilder message = new StringBuilder("<html><body>");
            message.append("<p>Anda belum mengisi \"Category PC\", data sudah disimpan</br>");
            message.append("tetapi belum dapat melakukan \"Create PR\" atau \"Create Modifier\"</p>");
            message.append("</body></html>");
            JSFUtils.addFacesWarningMessage(message.toString());
        } else {
            String currCatPc =
                (String)ADFContext.getCurrent().getSessionScope().get("currCatPc");
            String currNoConf =
                (String)ADFContext.getCurrent().getSessionScope().get("currNoConfirm");

            if (!confirmNo.equalsIgnoreCase("Auto Generated")) {
                //                || !existingCatPc.equalsIgnoreCase(categoryPc)) {
                RichPopup.PopupHints hints = new RichPopup.PopupHints();
                pConfCategoryPc.show(hints);
            } else {

                //                OperationBinding operationBindingCommit =
                //                    bindings.getOperationBinding("Commit");
                //                operationBindingCommit.execute();
            }
        }

        AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropHistApproval);
    }

    public void createModifierReturnCommit(ReturnEvent returnEvent) {
        BindingContainer bindings = getBindings();
        FacesContext ctx = FacesContext.getCurrentInstance();
        OperationBinding operationBinding =
            bindings.getOperationBinding("Commit");
        operationBinding.execute();
    }

    public void setListAddendumBudget(List<ListAddendumBudget> listAddendumBudget) {
        this.listAddendumBudget = listAddendumBudget;
    }

    public List<ListAddendumBudget> getListAddendumBudget() {
        return listAddendumBudget;
    }

    public void setRealisasiTemp(List<realisasiTempClass> realisasiTemp) {
        this.realisasiTemp = realisasiTemp;
    }

    public List<realisasiTempClass> getRealisasiTemp() {
        return realisasiTemp;
    }

    public void setListAddendumAmount(List<ListAddendumAmount> listAddendumAmount) {
        this.listAddendumAmount = listAddendumAmount;
    }

    public List<ListAddendumAmount> getListAddendumAmount() {
        return listAddendumAmount;
    }
    
    public void setRealisasiGrMfByLine(List<RealisasiGrMfByLine> realisasiGrMfByLine) {
        this.realisasiGrMfByLine = realisasiGrMfByLine;
    }

    public List<RealisasiGrMfByLine> getRealisasiGrMfByLine() {
        return realisasiGrMfByLine;
    }

    public void setPoverBudgetStatus(RichPopup poverBudgetStatus) {
        this.poverBudgetStatus = poverBudgetStatus;
    }

    public RichPopup getPoverBudgetStatus() {
        return poverBudgetStatus;
    }

    public void printPreviewPopupFetchListener(PopupFetchEvent popupFetchEvent) {
        BindingContainer bindings =
            BindingContext.getCurrent().getCurrentBindingsEntry();
        AttributeBinding propIdAttr =
            (AttributeBinding)bindings.getControlBinding("ProposalId");
        DBSequence propId = (DBSequence)propIdAttr.getInputValue();
        
        BindingContext bctx = BindingContext.getCurrent();
        DCBindingContainer binding =
            (DCBindingContainer)bctx.getCurrentBindingsEntry();
        DCIteratorBinding iterProposal =
            binding.findIteratorBinding("HistoryApprovalView1Iterator");
        ViewObject voProposal = iterProposal.getViewObject();
        Row rowSelected = voProposal .getCurrentRow();

        UserData userData =
            (UserData)JSFUtils.resolveExpression("#{UserData}");
        String userNameLogin =
            userData.getUserNameLogin() == null ? "" : userData.getUserNameLogin();
        String printCount =
            userData.getPrintCount() == null ? "N" : userData.getPrintCount();
        
        if (printCount.equalsIgnoreCase(printCountYes)) {        
            HistoryApprovalAMImpl historyApprovalAM =
                (HistoryApprovalAMImpl)ADFUtils.getApplicationModuleForDataControl("HistoryApprovalAMDataControl");
            CallableStatement cst = null;
            try {
                cst =
            historyApprovalAM.getDBTransaction().createCallableStatement("BEGIN FCS_PRINT_PREV_LOG(" + propId.getSequenceNumber().getBigDecimalValue() + ", '" + userNameLogin + "'); END;", 0);
                cst.executeUpdate();
            } catch (SQLException e) {
                JSFUtils.addFacesErrorMessage(e.getMessage());
            } finally {
                if (cst != null) {
                    try {
                        cst.close();
                    } catch (SQLException e) {
                        //e.printStackTrace();
                    }
                }
            }
        }

        iterProposal.executeQuery();
        iterProposal.setCurrentRowWithKey(rowSelected.getKey().toStringFormat(true));
        AdfFacesContext.getCurrentInstance().addPartialTarget(btnPrintPreview);
    }

    public void setBtnPrintPreview(RichCommandButton btnPrintPreview) {
        this.btnPrintPreview = btnPrintPreview;
    }

    public RichCommandButton getBtnPrintPreview() {
        return btnPrintPreview;
    }

    public void setStatusOverBudgetId(List<ListStatusOverBudgetId> StatusOverBudgetId) {
        this.StatusOverBudgetId = StatusOverBudgetId;
    }

    public List<ListStatusOverBudgetId> getStatusOverBudgetId() {
        return StatusOverBudgetId;
    }

    public void setBtnOkPP(RichCommandButton btnOkPP) {
        this.btnOkPP = btnOkPP;
    }

    public RichCommandButton getBtnOkPP() {
        return btnOkPP;
    }

    public void filterByInProcess(ActionEvent actionEvent) {
        BindingContainer bindings = getBindings();
        DCIteratorBinding parentIter =
            (DCIteratorBinding)bindings.get("HistoryApprovalView1Iterator");
        HistoryApprovalViewImpl propVo =
            (HistoryApprovalViewImpl)parentIter.getViewObject();
        //View Criteria without bind variable
        ViewCriteria vc =
            propVo.getViewCriteria("ProposalInProcessVC");
        propVo.applyViewCriteria(vc);
        propVo.executeQuery();

        switchMain.setFacetName("nodata");
        switchButtonMain.setFacetName("nodata");
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchButtonMain);
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropHistApproval);
    }

    public void filterByApproved(ActionEvent actionEvent) {
        BindingContainer bindings = getBindings();
        DCIteratorBinding parentIter =
            (DCIteratorBinding)bindings.get("HistoryApprovalView1Iterator");
        HistoryApprovalViewImpl propVo =
            (HistoryApprovalViewImpl)parentIter.getViewObject();
        //View Criteria without bind variable
        ViewCriteria vc =
            propVo.getViewCriteria("ProposalApprovedVC");
        propVo.applyViewCriteria(vc);
        propVo.executeQuery();

        switchMain.setFacetName("nodata");
        switchButtonMain.setFacetName("nodata");
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchButtonMain);
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropHistApproval);
    }

    public void filterByActive(ActionEvent actionEvent) {
        BindingContainer bindings = getBindings();
        DCIteratorBinding parentIter =
            (DCIteratorBinding)bindings.get("HistoryApprovalView1Iterator");
        HistoryApprovalViewImpl propVo =
            (HistoryApprovalViewImpl)parentIter.getViewObject();
        //View Criteria without bind variable
        ViewCriteria vc =
            propVo.getViewCriteria("ProposalActiveVC");
        propVo.applyViewCriteria(vc);
        propVo.executeQuery();

        switchMain.setFacetName("nodata");
        switchButtonMain.setFacetName("nodata");
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchButtonMain);
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropHistApproval);
    }

    public void filterByRejected(ActionEvent actionEvent) {
        BindingContainer bindings = getBindings();
        DCIteratorBinding parentIter =
            (DCIteratorBinding)bindings.get("HistoryApprovalView1Iterator");
        HistoryApprovalViewImpl propVo =
            (HistoryApprovalViewImpl)parentIter.getViewObject();
        //View Criteria without bind variable
        ViewCriteria vc =
            propVo.getViewCriteria("ProposalRejectedVC");
        propVo.applyViewCriteria(vc);
        propVo.executeQuery();

        switchMain.setFacetName("nodata");
        switchButtonMain.setFacetName("nodata");
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchButtonMain);
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropHistApproval);
    }

    public void refreshProposalList(ActionEvent actionEvent) {
        BindingContainer bindings = getBindings();
        DCIteratorBinding parentIter =
            (DCIteratorBinding)bindings.get("HistoryApprovalView1Iterator");
        HistoryApprovalViewImpl propVo =
            (HistoryApprovalViewImpl)parentIter.getViewObject();
        //View Criteria without bind variable
        ViewCriteria vc =
            propVo.getViewCriteria("ProposalAllStatusVC");
        propVo.applyViewCriteria(vc);
        propVo.executeQuery();

        switchMain.setFacetName("nodata");
        switchButtonMain.setFacetName("nodata");
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchButtonMain);
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropHistApproval);
    }
    
    public void filterByCanceled(ActionEvent actionEvent) {
        BindingContainer bindings = getBindings();
        DCIteratorBinding parentIter =
            (DCIteratorBinding)bindings.get("HistoryApprovalView1Iterator");
        HistoryApprovalViewImpl propVo =
            (HistoryApprovalViewImpl)parentIter.getViewObject();
        //View Criteria without bind variable
        ViewCriteria vc =
            propVo.getViewCriteria("ProposalCanceledVC");
        propVo.applyViewCriteria(vc);
        propVo.executeQuery();

        switchMain.setFacetName("nodata");
        switchButtonMain.setFacetName("nodata");
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchButtonMain);
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropHistApproval);
    }

    public void filterByClosed(ActionEvent actionEvent) {
        BindingContainer bindings = getBindings();
        DCIteratorBinding parentIter =
            (DCIteratorBinding)bindings.get("HistoryApprovalView1Iterator");
        HistoryApprovalViewImpl propVo =
            (HistoryApprovalViewImpl)parentIter.getViewObject();
        //View Criteria without bind variable
        ViewCriteria vc =
            propVo.getViewCriteria("ProposalClosedVC");
        propVo.applyViewCriteria(vc);
        propVo.executeQuery();

        switchMain.setFacetName("nodata");
        switchButtonMain.setFacetName("nodata");
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchButtonMain);
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropHistApproval);
    }

    public void setTblPropHistApproval(RichTable tblPropHistApproval) {
        this.tblPropHistApproval = tblPropHistApproval;
    }

    public RichTable getTblPropHistApproval() {
        return tblPropHistApproval;
    }

    public void approvalHistPopupFetchListener(PopupFetchEvent popupFetchEvent) {
        DCIteratorBinding dciter =
            ADFUtils.findIterator("DocApprovalView1Iterator");
        dciter.executeQuery();
    }

    public void tableDoubleClick(ClientEvent clientEvent) {
        switchMain.setFacetName("dataavailable");
        switchButtonMain.setFacetName("dataavailable");
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchButtonMain);
    }

    public void setSwitchButtonMain(UIXSwitcher switchButtonMain) {
        this.switchButtonMain = switchButtonMain;
    }

    public UIXSwitcher getSwitchButtonMain() {
        return switchButtonMain;
    }
    
    public CellFormat headerFormat(HeaderCellContext cxt) {
        CellFormat format = new CellFormat(null, "", "");
        //format.setStyle("min-width:172px;max-width:172px;");
        
        if (cxt.getValue() != null) {
            String header = cxt.getValue().toString();
            if (header.equals("Requester")) {
                format.setStyle("min-width:172px;max-width:172px;");
            } else {
                format.setStyle("min-width:172px;max-width:172px;");
            }
        }
        
        return format;
    }
    
    public CellFormat dataFormat(DataCellContext cxt) {
        CellFormat format = new CellFormat(null, "", "");
        //format.setStyle("min-width:172px;max-width:172px;");
        
        if (cxt.getValue() != null) {
            String data = cxt.getValue().toString();
            if (data.equals("ALL")) {
                format.setStyle("min-width:172px;max-width:172px;");
            } else {
                format.setStyle("min-width:172px;max-width:172px;");
            }
        }
        
        return format;
    }
}
