package app.fpp.bean.confirmation;

import app.fpp.adfextensions.ADFUtils;
import app.fpp.adfextensions.JSFUtils;
import app.fpp.bean.useraccessmenu.UserData;
import app.fpp.model.am.ConfirmationAMImpl;
import app.fpp.model.views.confirmation.CancelConfirmationClearBudgetViewImpl;
import app.fpp.model.views.confirmation.CategoryPcLogViewImpl;
import app.fpp.model.views.confirmation.ProdRealSummaryViewImpl;
import app.fpp.model.views.confirmation.PromoProdukLineClosedViewImpl;
import app.fpp.model.views.confirmation.ProposalConfirmationViewImpl;
import app.fpp.model.views.confirmation.ProposalUpdateConfirmAdendumViewImpl;
import app.fpp.model.views.confirmation.ProposalUpdatePrCreatedViewImpl;
import app.fpp.model.views.confirmation.checkAddendumBudgetClearViewImpl;
import app.fpp.model.views.confirmation.checkOverBudgetClearViewImpl;
import app.fpp.model.views.confirmation.checkProdukApprovalModifierOnInImpl;
import app.fpp.model.views.confirmation.dcv.CheckCmRealisasiDcvImpl;
import app.fpp.model.views.confirmation.dcv.CheckRealisasiSalesOrderImpl;
import app.fpp.model.views.confirmation.dcv.CloseValidationDcvAmountImpl;
import app.fpp.model.views.confirmation.getEmailUserApprovalHistoryImpl;
import app.fpp.model.views.confirmation.getPPIDRefViewImpl;
import app.fpp.model.views.confirmation.modifier.FcsModifierAreaExclViewImpl;
import app.fpp.model.views.confirmation.modifier.FcsModifierAreaViewImpl;
import app.fpp.model.views.confirmation.modifier.FcsModifierCeckBucketItemCategoryImpl;
import app.fpp.model.views.confirmation.modifier.FcsModifierHoExclViewImpl;
import app.fpp.model.views.confirmation.modifier.FcsModifierHoViewImpl;
import app.fpp.model.views.confirmation.modifier.FcsQpModifierTempExclViewImpl;
import app.fpp.model.views.confirmation.modifier.FcsQpModifierTempViewImpl;
import app.fpp.model.views.confirmation.modifier.FcsViewNoModifierViewImpl;
import app.fpp.model.views.confirmation.modifier.FcsViewNoModifierViewRowImpl;
import app.fpp.model.views.confirmation.modifier.HeaderIdSeqViewImpl;
import app.fpp.model.views.confirmation.targetpr.FcsApprovalPathViewImpl;
import app.fpp.model.views.confirmation.targetpr.FcsPoRequisitionTempViewImpl;
import app.fpp.model.views.confirmation.targetpr.FcsViewNoPrClosedViewImpl;
import app.fpp.model.views.confirmation.targetpr.FcsViewNoPrViewImpl;
import app.fpp.model.views.confirmation.targetpr.FcsViewNoPrViewRowImpl;
import app.fpp.model.views.masterdata.runnumber.RunNumConfViewImpl;
import app.fpp.model.views.masterdata.ebs.CompanyOUViewImpl;
import app.fpp.model.views.masterdata.ebs.KodePostingBJPViewImpl;
import app.fpp.model.views.masterdata.ebs.KodePostingViewImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import javax.naming.Context;

import javax.naming.InitialContext;

import javax.sql.DataSource;

import oracle.adf.controller.ControllerContext;
import oracle.adf.model.AttributeBinding;
import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;
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
import oracle.jbo.domain.Date;
import oracle.jbo.domain.Number;
import oracle.jbo.server.ViewObjectImpl;
import oracle.jbo.uicli.binding.JUCtrlHierBinding;

import oracle.jbo.uicli.binding.JUCtrlHierNodeBinding;

import org.apache.myfaces.trinidad.component.UIXSwitcher;
import org.apache.myfaces.trinidad.event.ReturnEvent;
import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.CollectionModel;
import org.apache.myfaces.trinidad.model.RowKeySet;

public class ConfirmationPromoBean {

    private Row runNumConf;
    private RichTable tblPropConfirmation;
    private RichTable tblSrcPropConfirm;
    private RichTable tblSrcPropAdendum;
    private String ebsOuIdFDI = "FDI OU";
    private String ebsOuIdFDN = "FDN OU";
    private UIXSwitcher switcherCustPost;
    private RichTable tblBudgetCustomer;
    private RichTable tblBudgetPosting;
    private String userCreatorHo = "HO";
    private String userCreatorArea = "AREA";
    private RichInputText itBudgetPostPercent;
    private RichInputText itBudgetCustPercent;
    private Integer isBJPTrue = 1;
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
    private static String offInvoice = "OFFINVOICE";
    private static String onInvoice = "ONINVOICE";
    private Number zeroNum = new Number(0);
    private String msgValKodePostOnTop1st = "Kode Posting On Top";
    private String msgValKodePostOnTop2nd = ", Kode Posting On Top";
    private String msgValKodePostMf1st = "Kode Posting MF";
    private String msgValKodePostMf2nd = ", Kode Posting MF";
    private String msgValItemExp1st = "Item Expense";
    private String msgValItemExp2nd = ", Item Expense";
    private RichTable tblListProduct;
    private String prStatusCancelled = "CANCELLED";
    private String prStatusFinallyClosed = "FINALLY CLOSED";
    private String modStatusInactive = "INACTIVE";
    private RichInputListOfValues itLovbudgetPost;
    private RichInputText itConfirmNo;
    private RichSelectOneChoice budgetBySoc;
    private RichShowDetailItem tabBudgetBind;
    private BigDecimal pajakDiv = new BigDecimal("1.1");
    private RichOutputText pathBind;
    private RichPopup pattacment;
    private UIXSwitcher switchMain;
    private RichInputListOfValues itLovBudgetBy;
    private String budgetByCust = "CUSTOMER";
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
    private List<ListStatusOverBudgetId> StatusOverBudgetId =
        new ArrayList<ListStatusOverBudgetId>();

    private RichPopup pConfCategoryPc;
    private RichInputListOfValues itlovCategoryPc;
    private String onDcv = "DCV";
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
    private DecimalFormat numFmt = new DecimalFormat("#,###,###,###,##0.00");
    private RichPopup poverBudgetStatus;
    private RichPopup popupCreateModifier;
    private String printCountYes = "Y";
    private RichCommandButton btnPrintPreview;
    private RichCommandButton btnOkPP;
    private String ppnExclude = "EXCLUDE";
    private RichInputText itBiaPrice;
    private DecimalFormat df2dgt = new DecimalFormat("###.##");
    private RichInputText itQtyBiaya;
    private RichInputListOfValues itLovBiayaUom;
    private RichTable tblBiaya;
    private RichInputText readOnlyBiaPrice;
    private UIXSwitcher switchButtonMain;
    private RichPopup popupSaveChanges;
    private String dsFdiConn = "jdbc/focusppDS";
    private String responFailed = "FAILED";
    private String responSuccess = "SUCCESS";
    private String proposalClosed = "CLOSED";
    private RichCommandButton btnClosePrmBrg;
    private RichCommandButton btnCloseMod;
    private RichCommandButton btnClosePr;

    public ConfirmationPromoBean() {
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

    public void confirmationPopupDialogListener(DialogEvent dialogEvent) {
        BindingContainer bindings = getBindings();
        if (dialogEvent.getOutcome().name().equals("ok")) {
            //Set to RED filter mode
            DCIteratorBinding parentIter =
                (DCIteratorBinding)bindings.get("ProposalConfirmationView1Iterator");
            ProposalConfirmationViewImpl propConfVo =
                (ProposalConfirmationViewImpl)parentIter.getViewObject();

            //View Criteria without bind variable
            ViewCriteria vc =
                propConfVo.getViewCriteria("PropConfirmNeedCreateVC");
            propConfVo.applyViewCriteria(vc);
            propConfVo.executeQuery();

            ConfirmationAMImpl confirmationAM =
                (ConfirmationAMImpl)ADFUtils.getApplicationModuleForDataControl("ConfirmationAMDataControl");

            DCIteratorBinding dciter =
                ADFUtils.findIterator("ProposalReadyConfirmAdendumView1Iterator");
            DBSequence propId =
                (DBSequence)dciter.getCurrentRow().getAttribute("ProposalId");

            ProposalUpdateConfirmAdendumViewImpl voProposal =
                (ProposalUpdateConfirmAdendumViewImpl)confirmationAM.getProposalUpdateConfirmAdendumView1();
            voProposal.setWhereClause("Proposal.PROPOSAL_ID = " +
                                      propId.getValue());
            voProposal.executeQuery();

            if (voProposal.getEstimatedRowCount() > 0) {
                Row propRow = voProposal.first();
                //propRow.setAttribute("ConfirmNo", confNumber);
                propRow.setAttribute("ConfirmNo", "Auto Generated");
                propRow.setAttribute("ConfirmDate", this.getCurrentSysDate());
            } 
            
            voProposal.closeRowSet();
            OperationBinding operationBindingCommit =
                bindings.getOperationBinding("Commit");
            operationBindingCommit.execute();
            
            OperationBinding operationExecutePropConfirm =
                bindings.getOperationBinding("ExecutePropConfirm");
            operationExecutePropConfirm.execute();

            OperationBinding operRefreshProposalList =
                bindings.getOperationBinding("refreshReadyProposal");
            operRefreshProposalList.execute();

            getEmailUserApprovalHistoryImpl emails = confirmationAM.getgetEmailUserApprovalHistory1();
            emails.setNamedWhereClauseParam("noProp", propId.getValue());
            emails.executeQuery();
            DCIteratorBinding dciterEmailNotifInsertView = ADFUtils.findIterator("EmailNotifReceiverView1Iterator"); 
            dciterEmailNotifInsertView.executeQuery();
            
            if(emails.getEstimatedRowCount()>0){
                while(emails.hasNext()){    
                    Row r=emails.next();
                    String ProposalId=r.getAttribute("ProposalId")==null ? "":r.getAttribute("ProposalId").toString();
                    String email=r.getAttribute("Descr")==null ? "":r.getAttribute("Descr").toString();
                    email= email.replace(";", ",");
                    String userName=r.getAttribute("ActionBy")==null ? "":r.getAttribute("ActionBy").toString();
                    String fullName=r.getAttribute("FullName")==null ? "":r.getAttribute("FullName").toString();
                    String Title=r.getAttribute("Title")==null ? "":r.getAttribute("Title").toString();
                    String DocApprovalId=r.getAttribute("DocApprovalId")==null ? "":r.getAttribute("DocApprovalId").toString();
                    String Action=r.getAttribute("Action")==null ? "":r.getAttribute("Action").toString();
                    Row dupEmailNotifRow =dciterEmailNotifInsertView.getRowSetIterator().createRow();
                    dupEmailNotifRow.setAttribute("ProposalId", ProposalId);
                    dupEmailNotifRow.setAttribute("EmailAddress", email);
                    dupEmailNotifRow.setAttribute("UserName", userName);
                    dupEmailNotifRow.setAttribute("FullName", fullName);
                    dupEmailNotifRow.setAttribute("Title", Title.replace("-", ""));
                    dupEmailNotifRow.setAttribute("DocApprovalId", DocApprovalId);
                    dupEmailNotifRow.setAttribute("Action", Action);
                    dciterEmailNotifInsertView.getRowSetIterator().insertRow(dupEmailNotifRow);
                    }
                    OperationBinding operationBindingCommit1 =
                        bindings.getOperationBinding("Commit");
                    operationBindingCommit1.execute();
                }

            //Set color mode
            ADFContext adfCtx = ADFContext.getCurrent();
            Map sessionScope = adfCtx.getSessionScope();
            sessionScope.put("colorMode", "RED");

            switchMain.setFacetName("dataavailable");
            switchButtonMain.setFacetName("dataavailable");
            AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
            AdfFacesContext.getCurrentInstance().addPartialTarget(switchButtonMain);
            AdfFacesContext.getCurrentInstance().addPartialTarget(tblSrcPropConfirm);
            AdfFacesContext.getCurrentInstance().addPartialTarget(tblSrcPropAdendum);
            AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropConfirmation);
            //ADFUtils.refreshCurrentPage();
        }
    }

    public oracle.jbo.domain.Date getCurrentSysDate() {
        java.sql.Timestamp datetime =
            new java.sql.Timestamp(System.currentTimeMillis());
        oracle.jbo.domain.Date daTime = new oracle.jbo.domain.Date(datetime);
        return daTime;
    }

    public void setTblPropConfirmation(RichTable tblPropConfirmation) {
        this.tblPropConfirmation = tblPropConfirmation;
    }

    public RichTable getTblPropConfirmation() {
        return tblPropConfirmation;
    }

    public void adendumPopupDialogListener(DialogEvent dialogEvent) {
        BindingContainer bindings = getBindings();
        FacesContext fctx = FacesContext.getCurrentInstance();
        DCIteratorBinding dciter =
            ADFUtils.findIterator("ProposalForAdendumView1Iterator");

        DBSequence propId = null;
        try {
            propId =
                    (DBSequence)dciter.getCurrentRow().getAttribute("ProposalId");
        } catch (Exception e) {
            StringBuilder message = new StringBuilder("<html><body>");
            message.append("<p>Proposal referensi untuk addendum harus dipilih.</p>");
            message.append("</body></html>");
            FacesMessage msg = new FacesMessage(message.toString());
            msg.setSeverity(FacesMessage.SEVERITY_WARN);
            fctx.addMessage(null, msg);
        }
        AttributeBinding ProposalNoAttr =
            (AttributeBinding)bindings.getControlBinding("ProposalNo");
        String ProposalNo = (String)ProposalNoAttr.getInputValue();

        AttributeBinding confirmNoAttr =
            (AttributeBinding)bindings.getControlBinding("ConfirmNo");
        String confirmNo = (String)confirmNoAttr.getInputValue();

        AttributeBinding categoryPcAttr =
            (AttributeBinding)bindings.getControlBinding("CategoryPc");
        String categoryPc = (String)categoryPcAttr.getInputValue();

        AttributeBinding DiscountTypeAttr =
            (AttributeBinding)bindings.getControlBinding("DiscountType1");
        String DiscountType =
            DiscountTypeAttr.getInputValue().toString();

        AttributeBinding mekPenagihanAttr =
            (AttributeBinding)bindings.getControlBinding("MekanismePenagihan1");
        String mekPenagihan = (String)mekPenagihanAttr.getInputValue();

        AttributeBinding DcvFlagAttr =
            (AttributeBinding)bindings.getControlBinding("DcvPcPrFlag");
        String DcvFlag = DcvFlagAttr.getInputValue().toString();

        DCIteratorBinding dciterPromoProduk =
            ADFUtils.findIterator("PromoProdukView1Iterator");
        realisasiTemp = new ArrayList<realisasiTempClass>();
        if (DiscountType.equalsIgnoreCase(discTypeBiaya)) {
            //FCS_VIEW_REALISASI_GR
            for (Row iterRealisasiProd :
                 dciterPromoProduk.getAllRowsInRange()) {
                realisasiTempClass realTemp = new realisasiTempClass();
                realTemp.setCONFIRM_NO(confirmNo);
                realTemp.setPROMO_PRODUK_ID(new BigDecimal(iterRealisasiProd.getAttribute("PromoProdukId").toString()));
                realTemp.setAMOUNTMF(new BigDecimal(iterRealisasiProd.getAttribute("RealisasiGrMf").toString()));
                realTemp.setAMOUNTOT(new BigDecimal(iterRealisasiProd.getAttribute("RealisasiGrOnTop").toString()));
                realisasiTemp.add(realTemp);
            }

        } else if (DiscountType.equalsIgnoreCase(discTypePotongan) &&
                   mekPenagihan.equalsIgnoreCase(offInvoice) && DcvFlag.equalsIgnoreCase("N")) {
            //FCS_VIEW_REALISASI_GR
            for (Row iterRealisasiProd :
                 dciterPromoProduk.getAllRowsInRange()) {
                realisasiTempClass realTemp = new realisasiTempClass();
                realTemp.setCONFIRM_NO(confirmNo);
                realTemp.setPROMO_PRODUK_ID(new BigDecimal(iterRealisasiProd.getAttribute("PromoProdukId").toString()));
                realTemp.setAMOUNTMF(new BigDecimal(iterRealisasiProd.getAttribute("RealisasiGrMf").toString()));
                realTemp.setAMOUNTOT(new BigDecimal(iterRealisasiProd.getAttribute("RealisasiGrOnTop").toString()));
                realisasiTemp.add(realTemp);
            }

        } else if (DiscountType.equalsIgnoreCase(discTypePromoBarang) &&
                   mekPenagihan.equalsIgnoreCase(offInvoice) && DcvFlag.equalsIgnoreCase("N")) {
            //FCS_VIEW_REALISASI_GR
            for (Row iterRealisasiProd :
                 dciterPromoProduk.getAllRowsInRange()) {
                realisasiTempClass realTemp = new realisasiTempClass();
                realTemp.setCONFIRM_NO(confirmNo);
                realTemp.setPROMO_PRODUK_ID(new BigDecimal(iterRealisasiProd.getAttribute("PromoProdukId").toString()));
                realTemp.setAMOUNTMF(new BigDecimal(iterRealisasiProd.getAttribute("RealisasiGrMf").toString()));
                realTemp.setAMOUNTOT(new BigDecimal(iterRealisasiProd.getAttribute("RealisasiGrOnTop").toString()));
                realisasiTemp.add(realTemp);
            }
        } else if (DiscountType.equalsIgnoreCase(discTypePotongan) &&
                   mekPenagihan.equalsIgnoreCase(offInvoice) && DcvFlag.equalsIgnoreCase("Y")) {
            for (Row iterRealisasiProd :
                 dciterPromoProduk.getAllRowsInRange()) {
                realisasiTempClass realTemp = new realisasiTempClass();
                realTemp.setCONFIRM_NO(confirmNo);
                realTemp.setPROMO_PRODUK_ID(new BigDecimal(iterRealisasiProd.getAttribute("PromoProdukId").toString()));
                realTemp.setAMOUNTMF(new BigDecimal(iterRealisasiProd.getAttribute("RealisasiDcvMf").toString()));
                realTemp.setAMOUNTOT(new BigDecimal(iterRealisasiProd.getAttribute("RealisasiDcvOnTop").toString()));
                realisasiTemp.add(realTemp);
            }
        } else if (DiscountType.equalsIgnoreCase(discTypePromoBarang) &&
                   mekPenagihan.equalsIgnoreCase(offInvoice) && DcvFlag.equalsIgnoreCase("Y")) {
            for (Row iterRealisasiProd :
                 dciterPromoProduk.getAllRowsInRange()) {
                realisasiTempClass realTemp = new realisasiTempClass();
                realTemp.setCONFIRM_NO(confirmNo);
                realTemp.setPROMO_PRODUK_ID(new BigDecimal(iterRealisasiProd.getAttribute("PromoProdukId").toString()));
                realTemp.setAMOUNTMF(new BigDecimal(iterRealisasiProd.getAttribute("RealisasiDcvMf").toString()));
                realTemp.setAMOUNTOT(new BigDecimal(iterRealisasiProd.getAttribute("RealisasiDcvOnTop").toString()));
                realisasiTemp.add(realTemp);
            }
        }

        if (dialogEvent.getOutcome().name().equals("ok") &&
            dciter.getEstimatedRowCount() > 0) {
            ConfirmationAMImpl confirmationAM =
                (ConfirmationAMImpl)ADFUtils.getApplicationModuleForDataControl("ConfirmationAMDataControl");
            
            checkAddendumBudgetClearViewImpl budgetsubs =
                confirmationAM.getcheckAddendumBudgetClearView1();
            budgetsubs.setNamedWhereClauseParam("confirmNO", confirmNo);
            budgetsubs.executeQuery();

            ProposalUpdateConfirmAdendumViewImpl voProposalConfirm =
                (ProposalUpdateConfirmAdendumViewImpl)confirmationAM.getProposalUpdateConfirmAdendumView1();
            voProposalConfirm.setWhereClause("Proposal.CONFIRM_NO = '" +
                                             confirmNo + "'");
            voProposalConfirm.executeQuery();
            Row propConfirmRow = voProposalConfirm.first();
            Number nextAddNumber =
                (Number)propConfirmRow.getAttribute("NextAddendum");
            String addendumNumber = String.valueOf(nextAddNumber);
            
            ProposalUpdateConfirmAdendumViewImpl voProposal =
                (ProposalUpdateConfirmAdendumViewImpl)confirmationAM.getProposalUpdateConfirmAdendumView1();
            voProposal.setWhereClause("Proposal.PROPOSAL_ID = " +
                                      propId.getValue());
            voProposal.executeQuery();
            
            listAddendumBudget = new ArrayList<ListAddendumBudget>();
            listAddendumAmount = new ArrayList<ListAddendumAmount>();
            HashMap<String, BigDecimal> mapOfSum =
                new HashMap<String, BigDecimal>();
            BigDecimal budgetAsToDateUsed = BigDecimal.ZERO;
            Connection conn = null;
            if (DiscountType.equalsIgnoreCase(discTypePotongan) &&
                mekPenagihan.equalsIgnoreCase(onInvoice)) {
                checkProdukApprovalModifierOnInImpl budgetcekProdukApproval =
                    confirmationAM.getcheckProdukApprovalModifierOnIn1();
                budgetcekProdukApproval.setNamedWhereClauseParam("pNo",
                                                                 ProposalNo);
                budgetcekProdukApproval.executeQuery();

                while (budgetcekProdukApproval.hasNext()) {
                    Row CekProdApp = budgetcekProdukApproval.next();
                    String ppidref =
                        CekProdApp.getAttribute("PpidRef").toString();
                    String prodAppStatus =
                        CekProdApp.getAttribute("ProductApproval").toString();
                    while (budgetsubs.hasNext()) {
                        Row erCekData = budgetsubs.next();
                        ListAddendumBudget ar = new ListAddendumBudget();
                        String pid =
                            erCekData.getAttribute("PromoProdukId").toString();
                        
                        if (pid.equalsIgnoreCase(ppidref) &&
                            prodAppStatus.equalsIgnoreCase("N")) {
                            //no action
                        } else {
                            String custId =
                                erCekData.getAttribute("BudgetCustId").toString();
                            String budgetid =
                                erCekData.getAttribute("BudgetById").toString();
                            ar.setBudgetCustId(custId);
                            ar.setPromoProdukId(pid);
                            ar.setBudgetById(budgetid);
                            
                            ListAddendumAmount tempUsed =
                                new ListAddendumAmount();
                            tempUsed.setPromoProdukId(pid);
                            tempUsed.setBudgetCustId(custId);
                            tempUsed.setBudgetById(budgetid);
                            BigDecimal total =
                                new BigDecimal(erCekData.getAttribute("Amount").toString());
                            tempUsed.setAmount(total);
                            BigDecimal totalOverBudget = BigDecimal.ZERO;
                            
                            tempUsed.setOverBudgetAmt(totalOverBudget);
                            listAddendumAmount.add(tempUsed);
                            listAddendumBudget.add(ar);
                        }
                    }
                }
            } else if (DiscountType.equalsIgnoreCase(discTypePromoBarang) &&
                       mekPenagihan.equalsIgnoreCase(onInvoice)) {
                checkProdukApprovalModifierOnInImpl budgetcekProdukApproval =
                    confirmationAM.getcheckProdukApprovalModifierOnIn1();
                budgetcekProdukApproval.setNamedWhereClauseParam("pNo",
                                                                 ProposalNo);
                budgetcekProdukApproval.executeQuery();

                while (budgetcekProdukApproval.hasNext()) {
                    Row CekProdApp = budgetcekProdukApproval.next();
                    String ppidref =
                        CekProdApp.getAttribute("PpidRef").toString();
                    String prodAppStatus =
                        CekProdApp.getAttribute("ProductApproval").toString();
                    while (budgetsubs.hasNext()) {
                        Row erCekData = budgetsubs.next();
                        ListAddendumBudget ar = new ListAddendumBudget();
                        String pid =
                            erCekData.getAttribute("PromoProdukId").toString();
                        if (pid.equalsIgnoreCase(ppidref) &&
                            prodAppStatus.equalsIgnoreCase("N")) {
                            //no action
                        } else {
                            String custId =
                                erCekData.getAttribute("BudgetCustId").toString();
                            String budgetid =
                                erCekData.getAttribute("BudgetById").toString();
                            ar.setBudgetCustId(custId);
                            ar.setPromoProdukId(pid);
                            ar.setBudgetById(budgetid);
                            ListAddendumAmount tempUsed =
                                new ListAddendumAmount();
                            tempUsed.setPromoProdukId(pid);
                            tempUsed.setBudgetCustId(custId);
                            tempUsed.setBudgetById(budgetid);
                            BigDecimal total =
                                new BigDecimal(erCekData.getAttribute("Amount").toString());
                            tempUsed.setAmount(total);
                            BigDecimal totalOverBudget = BigDecimal.ZERO;
                            tempUsed.setOverBudgetAmt(totalOverBudget);
                            listAddendumAmount.add(tempUsed);
                            listAddendumBudget.add(ar);
                        }
                    }
                }
            } else {

                while (budgetsubs.hasNext()) {
                    Row erCekData = budgetsubs.next();
                    ListAddendumBudget ar = new ListAddendumBudget();
                    String custId =
                        erCekData.getAttribute("BudgetCustId").toString();
                    String pid =
                        erCekData.getAttribute("PromoProdukId").toString();
                    String budgetid =
                        erCekData.getAttribute("BudgetById").toString();
                    ar.setBudgetCustId(custId);
                    ar.setPromoProdukId(pid);
                    ar.setBudgetById(budgetid);
                    List<realisasiTempClass> realTemp = getRealisasiTemp();
                    for (realisasiTempClass realBud : realTemp) {
                        if (pid.equalsIgnoreCase(realBud.getPROMO_PRODUK_ID().toString())) {
                            ListAddendumAmount tempUsed =
                                new ListAddendumAmount();
                            tempUsed.setPromoProdukId(pid);
                            tempUsed.setBudgetCustId(custId);
                            tempUsed.setBudgetById(budgetid);
                            BigDecimal total =
                                new BigDecimal(erCekData.getAttribute("Amount").toString());
                            BigDecimal totalOverBudget =
                                new BigDecimal(erCekData.getAttribute("OverBudgetAmt") ==
                                               null ? "0" :
                                               erCekData.getAttribute("OverBudgetAmt").toString());
                            tempUsed.setOverBudgetAmt(totalOverBudget);
                            BigDecimal realamountmf = realBud.getAMOUNTMF();
                            if (realamountmf.compareTo(total) < 0 ||
                                realamountmf.compareTo(total) == 0) {
                                BigDecimal selisihUsedBudget =
                                    total.subtract(realamountmf);
                                tempUsed.setAmount(selisihUsedBudget);
                            } else {
                                BigDecimal selisihUsedBudget =
                                    realamountmf.subtract(total);
                                BigDecimal endUsedBudget =
                                    selisihUsedBudget.subtract(total);
                                realBud.setAMOUNTMF(selisihUsedBudget);
                                if (endUsedBudget.compareTo(new BigDecimal(0)) <
                                    0) {
                                    tempUsed.setAmount(new BigDecimal(0));
                                } else {
                                    tempUsed.setAmount(endUsedBudget);
                                }
                            }
                            listAddendumAmount.add(tempUsed);
                        }
                    }
                    mapOfSum.put(custId, budgetAsToDateUsed);
                    listAddendumBudget.add(ar);
                }
            }
            
            List<ListAddendumAmount> realTempend = getListAddendumAmount();
            for (ListAddendumAmount cek : realTempend) {
                String budgetCustIdFdi = "";
                BigDecimal amountSubs = BigDecimal.ZERO;
                BigDecimal amountOverSubs = BigDecimal.ZERO;
                budgetCustIdFdi = cek.getBudgetCustId();
                amountSubs = cek.getAmount();
                amountOverSubs =
                        new BigDecimal(cek.getOverBudgetAmt().toString() ==
                                       null ? "0" :
                                       cek.getOverBudgetAmt().toString());
                if (amountSubs.compareTo(BigDecimal.ZERO) > 0) {
                    try {
                        Context ctx = new InitialContext();
                        DataSource ds = (DataSource)ctx.lookup(dsFdiConn);
                        conn = ds.getConnection();
                        conn.setAutoCommit(false);
                        PreparedStatement statement =
                            conn.prepareStatement("select BUDGETCUSTOMER.BUDGET_CUSTOMER_ID ,BUDGETCUSTOMER.BUDGET_AS_TO_DATE, BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED,BUDGETCUSTOMER.YEARLY_BUDGET_AMOUNT,BUDGETCUSTOMER.OVER_BUDGET from BUDGET_CUSTOMER BudgetCustomer where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID=? for update");
                        statement.setString(1, budgetCustIdFdi);
                        ResultSet rs = statement.executeQuery();
                        while (rs.next()) {
                            String BudgetCustId =
                                rs.getString("BUDGET_CUSTOMER_ID").toString();
                            BigDecimal budgetAsTodateCur = BigDecimal.ZERO;
                            BigDecimal budgetAsTodate = BigDecimal.ZERO;
                            BigDecimal overBudgetAmountCur = BigDecimal.ZERO;
                            budgetAsTodateCur =
                                    new BigDecimal(rs.getString("BUDGET_AS_TO_DATE_USED").toString());
                            budgetAsTodate =
                                    new BigDecimal(rs.getString("BUDGET_AS_TO_DATE").toString());
                            overBudgetAmountCur =
                                    new BigDecimal(rs.getString("OVER_BUDGET") ==
                                                   null ? "0" :
                                                   rs.getString("OVER_BUDGET").toString());
                            if (budgetCustIdFdi.equalsIgnoreCase(BudgetCustId)) {
                                BigDecimal value =
                                    budgetAsTodateCur.subtract(amountSubs);
                                BigDecimal overValue =
                                    overBudgetAmountCur.subtract(amountOverSubs);
                                try {
                                    PreparedStatement updateTtfNumSeq =
                                        conn.prepareStatement("UPDATE BUDGET_CUSTOMER BudgetCustomer SET BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED =? ,BUDGETCUSTOMER.OVER_BUDGET=? where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID = ?");
                                    updateTtfNumSeq.setBigDecimal(1, value);
                                    updateTtfNumSeq.setBigDecimal(2,
                                                                  overValue);
                                    updateTtfNumSeq.setString(3,
                                                              budgetCustIdFdi);
                                    updateTtfNumSeq.executeUpdate();
                                    updateTtfNumSeq.close();
                                } catch (SQLException sqle) {
                                    System.out.println("------------------------------------------------");
                                    System.out.println("ERROR: Cannot run update query");
                                    System.out.println("STACK: " +
                                                       sqle.toString().trim());
                                    System.out.println("------------------------------------------------");
                                }
                            }
                        }
                        conn.commit();
                        statement.close();
                        conn.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //                    }
                }
            }

            List<ListAddendumBudget> ab = getListAddendumBudget();
            for (ListAddendumBudget x : ab) {
                String budgetByid =
                    x.getBudgetById() == null ? "" : x.getBudgetById().toString();
                DCIteratorBinding dciterCust =
                    ADFUtils.findIterator("ProdBudgetByView1Iterator");
                RowSetIterator BudCust = dciterCust.getRowSetIterator();
                dciterCust.executeQuery();
                ViewObject voTableData = dciterCust.getViewObject();
                //                if(dciterCust.getEstimatedRowCount() > 0){
                //                    for(Row rowSelected : dciterCust.getAllRowsInRange()){
                while (voTableData.hasNext()) {
                    Row rowSelected = voTableData.next();
                    String budgetId =
                        rowSelected.getAttribute("BudgetById") == null ? "" :
                        rowSelected.getAttribute("BudgetById").toString();
                    if (budgetId.equalsIgnoreCase(budgetByid)) {
                        rowSelected.setAttribute("Status", "Y");
                    }
                }
                dciterCust.getDataControl().commitTransaction();
                BudCust.closeRowSetIterator();
                //            }
            }

            if (voProposal.getEstimatedRowCount() > 0) {
                Row propRow = voProposal.first();
                propRow.setAttribute("ConfirmNo", confirmNo);
                propRow.setAttribute("ConfirmDate", this.getCurrentSysDate());
                propRow.setAttribute("AddendumKe", addendumNumber);
                propRow.setAttribute("CategoryPc", categoryPc);
            }
            voProposal.closeRowSet();
            OperationBinding operationBindingCommit =
                bindings.getOperationBinding("Commit");
            operationBindingCommit.execute();

            OperationBinding operationExecutePropConfirm =
                bindings.getOperationBinding("ExecutePropConfirm");
            operationExecutePropConfirm.execute();

            OperationBinding operRefreshProposalList =
                bindings.getOperationBinding("refreshReadyProposal");
            operRefreshProposalList.execute();

            AdfFacesContext.getCurrentInstance().addPartialTarget(tblSrcPropConfirm);
            AdfFacesContext.getCurrentInstance().addPartialTarget(tblSrcPropAdendum);
        }
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

    public void createPrDialogListener(DialogEvent dialogEvent) {
        BindingContainer bindings = getBindings();

        FacesContext ctx = FacesContext.getCurrentInstance();

        Format datePrAttr2Fmt = new SimpleDateFormat("dd-MMM-yyyy");
        java.util.Date prDate =
            convertOracleDateToJavaUtilDate(getCurrentSysDate());
        String datePrAttr2Str =
            datePrAttr2Fmt.format(prDate).toString().toUpperCase();

        ConfirmationAMImpl confirmationAM =
            (ConfirmationAMImpl)ADFUtils.getApplicationModuleForDataControl("ConfirmationAMDataControl");

        // Remove PR failed rows
        clearFailPrCreation(confirmationAM);

        DCIteratorBinding dciter =
            ADFUtils.findIterator("ProposalConfirmationView1Iterator");

        //Get current row key
        Key parentKey = dciter.getCurrentRow().getKey();

        DBSequence propId =
            (DBSequence)dciter.getCurrentRow().getAttribute("ProposalId");

        AttributeBinding confirmNoAttr =
            (AttributeBinding)bindings.getControlBinding("ConfirmNo");
        String confNo = (String)confirmNoAttr.getInputValue();
        AttributeBinding adendumKeAttr =
            (AttributeBinding)bindings.getControlBinding("AddendumKe");
        String addendumKe = (String)adendumKeAttr.getInputValue();
        AttributeBinding discTypeAttr =
            (AttributeBinding)bindings.getControlBinding("DiscountType1");
        String discType = (String)discTypeAttr.getInputValue();
        AttributeBinding ppnFlagAttr =
            (AttributeBinding)bindings.getControlBinding("PpnFlag");
        String ppnFlag = (String)ppnFlagAttr.getInputValue();

        AttributeBinding bjpFlag =
            (AttributeBinding)bindings.getControlBinding("BjpFlag");
        Integer isBJP = (Integer)bjpFlag.getInputValue();

        String confirmNo = null;
        if (addendumKe != null) {
            confirmNo = confNo + "-" + addendumKe;
        } else {
            confirmNo = confNo;
        }

        UserData userData =
            (UserData)JSFUtils.resolveExpression("#{UserData}");
        String userName = userData.getFullName();
        String userTitle = userData.getTitle();

        String userFullName = null;
        if (userTitle == null || userTitle.isEmpty() || userTitle == "") {
            userFullName = userName + ",";
        } else {
            userFullName = userName + ", " + userTitle;
        }
        String compId = userData.getCompanyId();

        if (dialogEvent.getOutcome().name().equals("ok")) {
            // Flag run ebs procedure
            String flagRunEbs = "N";
            // Get batch id
            String currBatchId = batchNumber();
            // Validasi kode posting sudah terisi semua
            boolean kodePostingItemExpenseFilled = true;

            HashMap<Integer, String> valKodePostItemExp =
                new HashMap<Integer, String>();

            DCIteratorBinding dciterPromoProduk =
                ADFUtils.findIterator("PromoProdukView1Iterator");
            int prodLine = 0;
            for (Row promoProdRow : dciterPromoProduk.getAllRowsInRange()) {
                String productApproval =
                    (String)promoProdRow.getAttribute("ProductApproval");

                if (productApproval.equalsIgnoreCase("Y")) {
                    String itemExpense =
                        (String)promoProdRow.getAttribute("ItemExpense");
                    String kodePostingOnTop =
                        (String)promoProdRow.getAttribute("KodePosting");
                    String kodePostingMf =
                        (String)promoProdRow.getAttribute("KodePostingMf");

                    if (discType.equalsIgnoreCase(discTypePromoBarang)) {
                        Number barangBonusOnTop =
                            (Number)promoProdRow.getAttribute("BrgBonusOnTop");
                        Number barangBonusMf =
                            (Number)promoProdRow.getAttribute("BrgBonusMf");

                        String msgValPostItemExpBonus = "";
                        if (kodePostingOnTop == null &&
                            barangBonusOnTop.compareTo(zeroNum) > 0) {
                            if (msgValPostItemExpBonus.trim().length() == 0) {
                                msgValPostItemExpBonus =
                                        msgValKodePostOnTop1st;
                            } else {
                                msgValPostItemExpBonus =
                                        msgValPostItemExpBonus +
                                        msgValKodePostOnTop2nd;
                            }
                        }

                        if (kodePostingMf == null &&
                            barangBonusMf.compareTo(zeroNum) > 0) {
                            if (msgValPostItemExpBonus.trim().length() == 0) {
                                msgValPostItemExpBonus = msgValKodePostMf1st;
                            } else {
                                msgValPostItemExpBonus =
                                        msgValPostItemExpBonus +
                                        msgValKodePostMf2nd;
                            }
                        }

                        if (itemExpense == null && !isBJP.equals(isBJPTrue)) {
                            if (msgValPostItemExpBonus.trim().length() == 0) {
                                msgValPostItemExpBonus = msgValItemExp1st;
                            } else {
                                msgValPostItemExpBonus =
                                        msgValPostItemExpBonus +
                                        msgValItemExp2nd;
                            }
                        }

                        valKodePostItemExp.put(prodLine,
                                               msgValPostItemExpBonus);
                        prodLine = prodLine + 1;

                    } else if (discType.equalsIgnoreCase(discTypeBiaya)) {
                        Number biayaOnTop =
                            (Number)promoProdRow.getAttribute("BiaOntop");
                        Number biayaMf =
                            (Number)promoProdRow.getAttribute("BiaMf");

                        String msgValPostItemExpBiaya = "";
                        if (kodePostingOnTop == null &&
                            biayaOnTop.compareTo(zeroNum) > 0) {
                            if (msgValPostItemExpBiaya.trim().length() == 0) {
                                msgValPostItemExpBiaya =
                                        msgValKodePostOnTop1st;
                            } else {
                                msgValPostItemExpBiaya =
                                        msgValPostItemExpBiaya +
                                        msgValKodePostOnTop2nd;
                            }
                        }

                        if (kodePostingMf == null &&
                            biayaMf.compareTo(zeroNum) > 0) {
                            if (msgValPostItemExpBiaya.trim().length() == 0) {
                                msgValPostItemExpBiaya = msgValKodePostMf1st;
                            } else {
                                msgValPostItemExpBiaya =
                                        msgValPostItemExpBiaya +
                                        msgValKodePostMf2nd;
                            }
                        }

                        if (itemExpense == null && !isBJP.equals(isBJPTrue)) {
                            if (msgValPostItemExpBiaya.trim().length() == 0) {
                                msgValPostItemExpBiaya = msgValItemExp1st;
                            } else {
                                msgValPostItemExpBiaya =
                                        msgValPostItemExpBiaya +
                                        msgValItemExp2nd;
                            }
                        }

                        valKodePostItemExp.put(prodLine,
                                               msgValPostItemExpBiaya);
                        prodLine = prodLine + 1;

                    } else if (discType.equalsIgnoreCase(discTypePotongan)) {
                        Number potonganOnTop =
                            (Number)promoProdRow.getAttribute("DiscOnTop");
                        Number potonganMf =
                            (Number)promoProdRow.getAttribute("DiscMf");

                        String msgValPostItemExpPotongan = "";
                        if (kodePostingOnTop == null &&
                            potonganOnTop.compareTo(zeroNum) > 0) {
                            if (msgValPostItemExpPotongan.trim().length() ==
                                0) {
                                msgValPostItemExpPotongan =
                                        msgValKodePostOnTop1st;
                            } else {
                                msgValPostItemExpPotongan =
                                        msgValPostItemExpPotongan +
                                        msgValKodePostOnTop2nd;
                            }
                        }

                        if (kodePostingMf == null &&
                            potonganMf.compareTo(zeroNum) > 0) {
                            if (msgValPostItemExpPotongan.trim().length() ==
                                0) {
                                msgValPostItemExpPotongan =
                                        msgValKodePostMf1st;
                            } else {
                                msgValPostItemExpPotongan =
                                        msgValPostItemExpPotongan +
                                        msgValKodePostMf2nd;
                            }
                        }

                        if (itemExpense == null && !isBJP.equals(isBJPTrue)) {
                            if (msgValPostItemExpPotongan.trim().length() ==
                                0) {
                                msgValPostItemExpPotongan = msgValItemExp1st;
                            } else {
                                msgValPostItemExpPotongan =
                                        msgValPostItemExpPotongan +
                                        msgValItemExp2nd;
                            }
                        }

                        valKodePostItemExp.put(prodLine,
                                               msgValPostItemExpPotongan);
                        prodLine = prodLine + 1;
                    }
                }
            }

            // Validate error message
            for (int k = 0; k < valKodePostItemExp.size(); k++) {
                String errMsg =
                    valKodePostItemExp.get(k) == null ? "" : valKodePostItemExp.get(k);
                if (!errMsg.equalsIgnoreCase("")) {
                    kodePostingItemExpenseFilled = false;
                }
            }

            if (kodePostingItemExpenseFilled) {
                // Validasi jika sudah pernah Create PR
                FcsViewNoPrViewImpl noPrView =
                    (FcsViewNoPrViewImpl)confirmationAM.getFcsViewNoPrView1();
                noPrView.setNamedWhereClauseParam("noPc", confirmNo);
                noPrView.executeQuery();

                long prExists = noPrView.getEstimatedRowCount();
                String prStatus = "";
                Number prRevNum = new Number(0);
                if (prExists > 0) {
                    FcsViewNoPrViewRowImpl noPrRow =
                        (FcsViewNoPrViewRowImpl)noPrView.first();
                    prStatus = noPrRow.getStatus();
                    prRevNum = noPrRow.getRevision();
                }

                if (prExists == 0 ||
                    prStatus.equalsIgnoreCase(prStatusCancelled) ||
                    prStatus.equalsIgnoreCase(prStatusFinallyClosed)) {
                    int i = 0;
                    //Perubahan by request sodiq, add promo produk id kolom
                    for (Row r : dciterPromoProduk.getAllRowsInRange()) {
                        String productApproval =
                            (String)r.getAttribute("ProductApproval");
                        if (productApproval.equalsIgnoreCase("Y")) {
                            i = i + 1;
                            DBSequence promoProdukId =
                                (DBSequence)r.getAttribute("PromoProdukId");
                            Number nonYearlyBudget = zeroNum;
                            Number yearlyBudget = zeroNum;
                            Number realisasiGr =
                                r.getAttribute("RealisasiGrTotalByLine") ==
                                null ? new Number(0) :
                                (Number)r.getAttribute("RealisasiGrTotalByLine");
                            String itemExpense =
                                (String)r.getAttribute("ItemExpense");
                            String biayaUom = "";
                            Number biayaQty = zeroNum;
                            Number biayaMfPrice = zeroNum;
                            Number biayaOntopPrice = zeroNum;
                            Number biayaPricea = zeroNum;
                            BigDecimal biayaPrice = zeroNum.getBigDecimalValue();

                            if (discType.equalsIgnoreCase(discTypePotongan)) {
                                nonYearlyBudget =
                                        (Number)r.getAttribute("DiscOnTop");
                                yearlyBudget =
                                        (Number)r.getAttribute("DiscMf");
                            } else if (discType.equalsIgnoreCase(discTypePromoBarang)) {
                                nonYearlyBudget =
                                        (Number)r.getAttribute("BrgBonusOnTop");
                                yearlyBudget =
                                        (Number)r.getAttribute("BrgBonusMf");
                            } else if (discType.equalsIgnoreCase(discTypeBiaya)) {
                                nonYearlyBudget =
                                        (Number)r.getAttribute("BiaOntop");
                                yearlyBudget = (Number)r.getAttribute("BiaMf");
                                biayaUom =
                                        (String)r.getAttribute("BiayaUom") ==
                                        null ? "" :
                                        (String)r.getAttribute("BiayaUom");
                                biayaQty =
                                        (Number)r.getAttribute("BiayaQty") ==
                                        null ? new Number(1) :
                                        (Number)r.getAttribute("BiayaQty");
                              /*   biayaPrice =
                                        (Number)r.getAttribute("BiayaPrice") ==
                                        null ? zeroNum :
                                        (Number)r.getAttribute("BiayaPrice"); */
                                biayaMfPrice =
                                        (Number)r.getAttribute("BiaMf") ==
                                        null ? zeroNum :
                                        (Number)r.getAttribute("BiaMf");
                                biayaOntopPrice=(Number)r.getAttribute("BiaOntop") ==
                                        null ? zeroNum :
                                        (Number)r.getAttribute("BiaOntop");
                                biayaPricea = biayaMfPrice.add(biayaOntopPrice);
                                if (biayaQty.compareTo(zeroNum) == 0) {
                                    // Jika biaya qty nol atau null tidak usah di bagi.
                                    biayaPrice = biayaPricea.getBigDecimalValue();
                                } else {
                                    biayaPrice = biayaPricea.getBigDecimalValue().divide(biayaQty.getBigDecimalValue(),12,RoundingMode.HALF_UP);
                                }
                            } else {
                                nonYearlyBudget = zeroNum;
                                yearlyBudget = zeroNum;
                            }

                            Number allBudget =
                                nonYearlyBudget.add(yearlyBudget);

                            Number allBudgetMinGr =
                                (Number)allBudget.minus(realisasiGr);
                            BigDecimal prQuantity =
                                allBudgetMinGr.getBigDecimalValue();
                            BigDecimal biayaPriceCalc = biayaPrice;
                            if (ppnFlag.equalsIgnoreCase(ppnExclude)) {
                                prQuantity =
                                        allBudgetMinGr.getBigDecimalValue().divide(pajakDiv,
                                                                                   12,
                                                                                   RoundingMode.HALF_UP);
                                if (biayaPrice.compareTo(zeroNum.getBigDecimalValue()) > 0) {
                                    biayaPriceCalc =
                                            biayaPrice.divide(pajakDiv,
                                                               2,
                                                               RoundingMode.HALF_UP);
                                }
                            }
                            /*
                            System.out.println("=======LINE (" + i+ ") ========");
                            System.out.println("=== MF + OT : " + allBudget.getBigDecimalValue());
                            System.out.println("=== REAL GR : " + realisasiGr.getBigDecimalValue());
                            System.out.println("------------------------------");
                            System.out.println("=== OTMF-GR : " + allBudgetMinGr.getBigDecimalValue());
                            System.out.println("=== PR QTY  : " + prQuantity);
                            System.out.println("==============================");
                            */
                            if (allBudgetMinGr.compareTo(zeroNum) > 0) {
                                FcsPoRequisitionTempViewImpl prTableTemp =
                                    (FcsPoRequisitionTempViewImpl)confirmationAM.getFcsPoRequisitionTempView1();

                                Row prTableTempRow = prTableTemp.createRow();
                                prTableTempRow.setAttribute("CreationDate",
                                                            getCurrentSysDate());
                                prTableTempRow.setAttribute("InterfaceSourceCode",
                                                            "PC");
                                prTableTempRow.setAttribute("SourceTypeCode",
                                                            "VENDOR");
                                prTableTempRow.setAttribute("RequisitionType",
                                                            "PURCHASE");
                                prTableTempRow.setAttribute("DestinationTypeCode",
                                                            "EXPENSE");

                                if (!biayaUom.equalsIgnoreCase("") &&
                                    biayaQty.compareTo(zeroNum) != 0) {
                                    prTableTempRow.setAttribute("UnitPrice",
                                                                biayaPriceCalc);
                                } else {
                                    prTableTempRow.setAttribute("UnitPrice",
                                                                1);
                                }

                                prTableTempRow.setAttribute("AuthorizationStatus",
                                                            "INCOMPLETE");
                                prTableTempRow.setAttribute("BatchId",
                                                            currBatchId);
                                prTableTempRow.setAttribute("GroupCode",
                                                            confirmNo);
                                prTableTempRow.setAttribute("ApprovalPathId",
                                                            getApprovalPathId());
                                prTableTempRow.setAttribute("PreparerName",
                                                            "GENERATE,");

                                if ((prStatus.equalsIgnoreCase(prStatusCancelled) &&
                                     prExists > 0) ||
                                    (prStatus.equalsIgnoreCase(prStatusFinallyClosed) &&
                                     prExists > 0)) {
                                    Number newRevNum = prRevNum.add(1);
                                    prTableTempRow.setAttribute("ReqNumberSegment1",
                                                                confirmNo +
                                                                "." +
                                                                newRevNum);
                                    prTableTempRow.setAttribute("Revision",
                                                                newRevNum);
                                } else {
                                    prTableTempRow.setAttribute("ReqNumberSegment1",
                                                                confirmNo);
                                    prTableTempRow.setAttribute("Revision", 0);
                                }

                                prTableTempRow.setAttribute("HeaderAttribute1",
                                                            "PROMO");
                                prTableTempRow.setAttribute("HeaderAttribute2",
                                                            datePrAttr2Str);
                                prTableTempRow.setAttribute("HeaderAttribute3",
                                                            "Promo");
                                prTableTempRow.setAttribute("HeaderAttribute4",
                                                            confNo);
                                prTableTempRow.setAttribute("ItemSegment1",
                                                            itemExpense);

                                if (!biayaUom.equalsIgnoreCase("") &&
                                    biayaQty.compareTo(zeroNum) != 0) {
                                    prTableTempRow.setAttribute("UnitOfMeasure",
                                                                null);
                                } else {
                                    prTableTempRow.setAttribute("UnitOfMeasure",
                                                                getUomDesc(confirmationAM,
                                                                           itemExpense,
                                                                           "N"));
                                }

                                if (!biayaUom.equalsIgnoreCase("") &&
                                    biayaQty.compareTo(zeroNum) != 0) {
                                    prTableTempRow.setAttribute("UomCode",
                                                                biayaUom);
                                } else {
                                    prTableTempRow.setAttribute("UomCode",
                                                                getUomCode(confirmationAM,
                                                                           itemExpense,
                                                                           "N"));
                                }

                                if (!biayaUom.equalsIgnoreCase("") &&
                                    biayaQty.compareTo(zeroNum) != 0) {
                                    prTableTempRow.setAttribute("Quantity",
                                                                biayaQty);
                                } else {
                                    prTableTempRow.setAttribute("Quantity",
                                                                prQuantity);
                                }

                                prTableTempRow.setAttribute("LineType",
                                                            "EXPENSE");
                                prTableTempRow.setAttribute("NoteToReceiver",
                                                            confirmNo + " :" +
                                                            i);
                                prTableTempRow.setAttribute("DestinationOrganizationCode",
                                                            "IOT");
                                prTableTempRow.setAttribute("DestinationSubinventory",
                                                            null);
                                prTableTempRow.setAttribute("DeliverToLocationCode",
                                                            "FDI Location");
                                prTableTempRow.setAttribute("DeliverToRequestorName",
                                                            "PROMO,");
                                prTableTempRow.setAttribute("DistributionAttribute1",
                                                            confirmNo);
                                prTableTempRow.setAttribute("OrgId",
                                                            getOrgId(confirmationAM,
                                                                     compId));
                                prTableTempRow.setAttribute("LineNum", i);
                                r.setAttribute("ProdukRowNum", i);
                                prTableTempRow.setAttribute("Status", "NEW");
                                prTableTempRow.setAttribute("LineAttribute2",
                                                            datePrAttr2Str);
                                prTableTempRow.setAttribute("PromoProdukId",
                                                            promoProdukId.getSequenceNumber());
                                prTableTempRow.setAttribute("ProposalId",
                                                            propId.getSequenceNumber());

                                prTableTemp.insertRow(prTableTempRow);
                            } else {
                                // NILAI QUANTITY NOL, PR UNTUK ROW INI NGGA DILEMPAR
                                System.out.println("INFO: Line produk (" + i +
                                                   "): " +
                                                   propId.getSequenceNumber() +
                                                   "-" +
                                                   promoProdukId.getSequenceNumber() +
                                                   ", Nilai \"Quantity\" NOL, PR tidak disubmit ke EBS.");
                            }

                        } else {
                            // PRODUK APPROVAL UNCHECK
                        }
                    }

                    OperationBinding operationBinding =
                        bindings.getOperationBinding("Commit");
                    operationBinding.execute();

                    if (!operationBinding.getErrors().isEmpty()) {
                        throw new RuntimeException(operationBinding.getErrors().toString());
                    } else {
                        ProposalUpdatePrCreatedViewImpl voProposal =
                            (ProposalUpdatePrCreatedViewImpl)confirmationAM.getProposalUpdatePrCreatedView1();
                        voProposal.setWhereClause("Proposal.PROPOSAL_ID = " +
                                                  propId.getValue());
                        voProposal.executeQuery();

                        if (voProposal.getEstimatedRowCount() > 0) {
                            Row propRow = voProposal.first();
                            propRow.setAttribute("PrCreated", "Y");

                            OperationBinding operationBindingCommit =
                                bindings.getOperationBinding("Commit");
                            operationBindingCommit.execute();

                            OperationBinding operationExecutePropConfirm =
                                bindings.getOperationBinding("ExecutePropConfirm");
                            operationExecutePropConfirm.execute();

                            flagRunEbs = "Y";

                            AdfFacesContext.getCurrentInstance().addPartialTarget(tblSrcPropConfirm);

                        }
                    }

                    // Execute procedure EBS push PR
                    if (flagRunEbs.equalsIgnoreCase("Y")) {
                        Integer prRequestId =
                            runConcurentPrPppc(confirmationAM, currBatchId);
                        if (prRequestId != null) {
                            Row currRow = dciter.getCurrentRow();
                            currRow.setAttribute("PrRequestId", prRequestId);

                            // Update calculate columm, temporary supaya ada status EBS duluan aja INPROCESS
                            AttributeBinding statusPrEbsAttr =
                                (AttributeBinding)bindings.getControlBinding("StatusPrEbs");
                            statusPrEbsAttr.setInputValue("IN PROCESS");
                        } else {
                            // Update calculate columm, temporary supaya ada status EBS duluan aja ERROR
                            AttributeBinding statusPrEbsAttr =
                                (AttributeBinding)bindings.getControlBinding("StatusPrEbs");
                            statusPrEbsAttr.setInputValue("ERROR");
                        }
                    }

                    OperationBinding operationBindingCommit =
                        bindings.getOperationBinding("Commit");
                    operationBindingCommit.execute();

                } else {
                    JSFUtils.addFacesErrorMessage("Error",
                                                  "Dokumen ini \"" + confirmNo +
                                                  "\", sudah pernah diajukan PR sebelumnya.");
                }

                dciter.setCurrentRowWithKey(parentKey.toStringFormat(true));
                AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropConfirmation);
                AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
            } else {
                /*
                JSFUtils.addFacesErrorMessage("Error",
                                              "Kode posting dan item expense pada daftar produk belum lengkap terisi.");
                */

                StringBuilder message = new StringBuilder("<html><body>");
                for (int j = 0; j < valKodePostItemExp.size(); j++) {
                    String errMsg =
                        valKodePostItemExp.get(j) == null ? "" : valKodePostItemExp.get(j);
                    int prodLineNum = j + 1;
                    if (!errMsg.equalsIgnoreCase("")) {
                        message.append("<p>Produk line (" + prodLineNum +
                                       "): " + errMsg + " belum terisi.</p>");
                    }
                }
                message.append("</body></html>");
                FacesMessage msg = new FacesMessage(message.toString());
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                ctx.addMessage(null, msg);
            }
        }
    }

    private String getUomCode(ConfirmationAMImpl confirmationAM,
                              String kodePosting, String isBJP) {
        String uomCode = null;
        if (isBJP.equalsIgnoreCase("Y")) {
            KodePostingBJPViewImpl kodePostingVo =
                confirmationAM.getKodePostingBJPView1();
            kodePostingVo.setNamedWhereClauseParam("kodePosting", kodePosting);
            kodePostingVo.executeQuery();
            if (kodePostingVo.getEstimatedRowCount() > 0) {
                Row allKodePostingRow = kodePostingVo.first();
                uomCode = (String)allKodePostingRow.getAttribute("PrimaryUom");
            }
        } else {
            KodePostingViewImpl kodePostingVo =
                confirmationAM.getKodePostingView1();
            kodePostingVo.setNamedWhereClauseParam("kodePosting", kodePosting);
            kodePostingVo.executeQuery();
            if (kodePostingVo.getEstimatedRowCount() > 0) {
                Row allKodePostingRow = kodePostingVo.first();
                uomCode = (String)allKodePostingRow.getAttribute("PrimaryUom");
            }
        }
        return uomCode;
    }

    private String getUomDesc(ConfirmationAMImpl confirmationAM,
                              String kodePosting, String isBJP) {
        String uomDesc = null;
        if (isBJP.equalsIgnoreCase("Y")) {
            KodePostingBJPViewImpl kodePostingVo =
                confirmationAM.getKodePostingBJPView1();
            kodePostingVo.setNamedWhereClauseParam("kodePosting", kodePosting);
            kodePostingVo.executeQuery();
            if (kodePostingVo.getEstimatedRowCount() > 0) {
                Row allKodePostingRow = kodePostingVo.first();
                uomDesc =
                        (String)allKodePostingRow.getAttribute("PrimaryUnitOfMeasure");
            }
        } else {
            KodePostingViewImpl kodePostingVo =
                confirmationAM.getKodePostingView1();
            kodePostingVo.setNamedWhereClauseParam("kodePosting", kodePosting);
            kodePostingVo.executeQuery();
            if (kodePostingVo.getEstimatedRowCount() > 0) {
                Row allKodePostingRow = kodePostingVo.first();
                uomDesc =
                        (String)allKodePostingRow.getAttribute("PrimaryUnitOfMeasure");
            }
        }
        return uomDesc;
    }

    private oracle.jbo.domain.Number getOrgId(ConfirmationAMImpl confirmationAM,
                                              String compId) {
        oracle.jbo.domain.Number orgId = null;
        String compOu = null;
        if (compId.equalsIgnoreCase("N")) {
            compOu = ebsOuIdFDN;
        } else {
            compOu = ebsOuIdFDI;
        }

        CompanyOUViewImpl companyOUVo =
            (CompanyOUViewImpl)confirmationAM.getCompanyOUView1();
        companyOUVo.setNamedWhereClauseParam("ouName", compOu);
        companyOUVo.executeQuery();
        if (companyOUVo.getEstimatedRowCount() > 0) {
            Row companyOURow = companyOUVo.first();
            orgId = (oracle.jbo.domain.Number)companyOURow.getAttribute("Id");
        }
        return orgId;
    }

    private String batchNumber() {
        String batchNum = null;

        // Get Date on ddMMyy Format
        java.util.Date date = new java.util.Date(System.currentTimeMillis());
        DateFormat dfddMMyy = new SimpleDateFormat("ddMMyyHHmmss");
        String currDay = dfddMMyy.format(date);

        batchNum = "99" + currDay;

        return batchNum;
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

    public void createModifierDialogListener(DialogEvent dialogEvent) {
        BindingContainer bindings = getBindings();

        FacesContext ctx = FacesContext.getCurrentInstance();

        AttributeBinding propIdAttr =
            (AttributeBinding)bindings.getControlBinding("ProposalId");
        DBSequence propId = (DBSequence)propIdAttr.getInputValue();
        AttributeBinding confirmNoAttr =
            (AttributeBinding)bindings.getControlBinding("ConfirmNo");
        String confNo = (String)confirmNoAttr.getInputValue();
        AttributeBinding adendumKeAttr =
            (AttributeBinding)bindings.getControlBinding("AddendumKe");
        String addendumKe = (String)adendumKeAttr.getInputValue();
        AttributeBinding userTypeCreatorAttr =
            (AttributeBinding)bindings.getControlBinding("UserTypeCreator");
        String userTypeCreator = (String)userTypeCreatorAttr.getInputValue();
        AttributeBinding discountTypeAttr =
            (AttributeBinding)bindings.getControlBinding("DiscountType1");
        String discountType = (String)discountTypeAttr.getInputValue();
        AttributeBinding ppnFlagAttr =
            (AttributeBinding)bindings.getControlBinding("PpnFlag");
        String ppnFlag = (String)ppnFlagAttr.getInputValue();

        String confirmNo = null;
        String confirmNoAdd = null;
        if (addendumKe != null) {
            confirmNoAdd = confNo + "-" + addendumKe;
            confirmNo = confNo;
        } else {
            confirmNo = confNo;
        }

        ConfirmationAMImpl confirmationAM =
            (ConfirmationAMImpl)ADFUtils.getApplicationModuleForDataControl("ConfirmationAMDataControl");

        DCIteratorBinding dciterPropConfirm =
            ADFUtils.findIterator("ProposalConfirmationView1Iterator");

        //Get current row key
        Key parentKey = dciterPropConfirm.getCurrentRow().getKey();

        // Validasi jika sudah pernah Create Modifier
        FcsViewNoModifierViewImpl voModifierFind =
            confirmationAM.getFcsViewNoModifierView1();
        if (addendumKe != null) {
            voModifierFind.setNamedWhereClauseParam("noConf", confirmNoAdd);
        } else {
            voModifierFind.setNamedWhereClauseParam("noConf", confirmNo);
        }
        voModifierFind.executeQuery();

        if (voModifierFind.getEstimatedRowCount() == 0) {
            // Get user initial
            UserData userData =
                (UserData)JSFUtils.resolveExpression("#{UserData}");
            String userName = userData.getUserNameLogin();

            if (dialogEvent.getOutcome().name().equals("ok")) {
                // Flag run ebs procedure
                String flagRunEbs = "N";
                // Get Header Id Sequence
                HeaderIdSeqViewImpl voHeaderId =
                    (HeaderIdSeqViewImpl)confirmationAM.getHeaderIdSeqView1();
                voHeaderId.executeQuery();

                Row headerIdRow = voHeaderId.first();
                Number headerIdNum =
                    (Number)headerIdRow.getAttribute("HeaderIdSeq");

                HashMap<Integer, String> valKodePosting =
                    new HashMap<Integer, String>();
                DCIteratorBinding dciterPromoProduk =
                    ADFUtils.findIterator("PromoProdukView1Iterator");
                int prodLine = 0;
                for (Row promoProdRow :
                     dciterPromoProduk.getAllRowsInRange()) {
                    String productApproval =
                        (String)promoProdRow.getAttribute("ProductApproval");

                    if (productApproval.equalsIgnoreCase("Y")) {

                        String kodePostingOnTop =
                            (String)promoProdRow.getAttribute("KodePosting");
                        String kodePostingMf =
                            (String)promoProdRow.getAttribute("KodePostingMf");

                        if (discountType.equalsIgnoreCase(discTypePromoBarang)) {
                            Number barangBonusOnTop =
                                (Number)promoProdRow.getAttribute("BrgBonusOnTop");
                            Number barangBonusMf =
                                (Number)promoProdRow.getAttribute("BrgBonusMf");

                            String msgValPostBonus = "";
                            if (kodePostingOnTop == null &&
                                barangBonusOnTop.compareTo(zeroNum) > 0) {
                                if (msgValPostBonus.trim().length() == 0) {
                                    msgValPostBonus = msgValKodePostOnTop1st;
                                } else {
                                    msgValPostBonus =
                                            msgValPostBonus + msgValKodePostOnTop2nd;
                                }
                            }

                            if (kodePostingMf == null &&
                                barangBonusMf.compareTo(zeroNum) > 0) {
                                if (msgValPostBonus.trim().length() == 0) {
                                    msgValPostBonus = msgValKodePostMf1st;
                                } else {
                                    msgValPostBonus =
                                            msgValPostBonus + msgValKodePostMf2nd;
                                }
                            }

                            valKodePosting.put(prodLine, msgValPostBonus);
                            prodLine = prodLine + 1;

                        } else if (discountType.equalsIgnoreCase(discTypeBiaya)) {
                            Number biayaOnTop =
                                (Number)promoProdRow.getAttribute("BiaMf");
                            Number biayaMf =
                                (Number)promoProdRow.getAttribute("BiaOntop");

                            String msgValPostBiaya = "";
                            if (kodePostingOnTop == null &&
                                biayaOnTop.compareTo(zeroNum) > 0) {
                                if (msgValPostBiaya.trim().length() == 0) {
                                    msgValPostBiaya = msgValKodePostOnTop1st;
                                } else {
                                    msgValPostBiaya =
                                            msgValPostBiaya + msgValKodePostOnTop2nd;
                                }
                            }

                            if (kodePostingMf == null &&
                                biayaMf.compareTo(zeroNum) > 0) {
                                if (msgValPostBiaya.trim().length() == 0) {
                                    msgValPostBiaya = msgValKodePostMf1st;
                                } else {
                                    msgValPostBiaya =
                                            msgValPostBiaya + msgValKodePostMf2nd;
                                }
                            }

                            valKodePosting.put(prodLine, msgValPostBiaya);
                            prodLine = prodLine + 1;

                        } else if (discountType.equalsIgnoreCase(discTypePotongan)) {
                            Number potonganOnTop =
                                (Number)promoProdRow.getAttribute("DiscOnTop");
                            Number potonganMf =
                                (Number)promoProdRow.getAttribute("DiscMf");

                            String msgValPostPotongan = "";
                            if (kodePostingOnTop == null &&
                                potonganOnTop.compareTo(zeroNum) > 0) {
                                if (msgValPostPotongan.trim().length() == 0) {
                                    msgValPostPotongan =
                                            msgValKodePostOnTop1st;
                                } else {
                                    msgValPostPotongan =
                                            msgValPostPotongan + msgValKodePostOnTop2nd;
                                }
                            }

                            if (kodePostingMf == null &&
                                potonganMf.compareTo(zeroNum) > 0) {
                                if (msgValPostPotongan.trim().length() == 0) {
                                    msgValPostPotongan = msgValKodePostMf1st;
                                } else {
                                    msgValPostPotongan =
                                            msgValPostPotongan + msgValKodePostMf2nd;
                                }
                            }

                            valKodePosting.put(prodLine, msgValPostPotongan);
                            prodLine = prodLine + 1;

                        }
                        promoProdRow.setAttribute("ProdukRowNum", prodLine);
                    }
                }

                // Validate error message
                boolean postingValid = true;
                for (int k = 0; k < valKodePosting.size(); k++) {
                    String errMsg =
                        valKodePosting.get(k) == null ? "" : valKodePosting.get(k);
                    if (!errMsg.equalsIgnoreCase("")) {
                        postingValid = false;
                    }
                }

                if (postingValid) {
                    // Update modifier type
                    RowSetIterator rsiPropConfirm =
                        dciterPropConfirm.getRowSetIterator();
                    Row[] propConfirmRow =
                        rsiPropConfirm.findByKey(new Key(new Object[] { propId.getValue() }),
                                                 1);

                    if (userTypeCreator.equalsIgnoreCase(userCreatorHo)) {
                        // Fetch Modifier HO
                        FcsModifierHoViewImpl voModifierHo =
                            (FcsModifierHoViewImpl)confirmationAM.getFcsModifierHoView1();
                        voModifierHo.setNamedWhereClauseParam("noConfirm",
                                                              confirmNo);
                        voModifierHo.executeQuery();
                        
                        if (voModifierHo.getEstimatedRowCount() > 0) {
                            int i = 0;
                            Number ppidTemp = null;
                            while (voModifierHo.hasNext()) {
                                FcsQpModifierTempViewImpl voModifierTemp =
                                    confirmationAM.getFcsQpModifierTempView1();

                                ViewObjectImpl voCekBucket =
                                    confirmationAM.getBucketView1();
                                voCekBucket.executeQuery();

                                Row modifierHoRow = voModifierHo.next();
                                String noConfirm =
                                    (String)modifierHoRow.getAttribute("NoConfirm");
                                String currency =
                                    (String)modifierHoRow.getAttribute("Currency");
                                Date startDate =
                                    (Date)modifierHoRow.getAttribute("StartDate");
                                Date endDate =
                                    (Date)modifierHoRow.getAttribute("EndDate");
                                String active =
                                    (String)modifierHoRow.getAttribute("Active");
                                String automatic =
                                    (String)modifierHoRow.getAttribute("Automatic");
                                String lineLevel =
                                    (String)modifierHoRow.getAttribute("LineLevel");
                                String lineType =
                                    (String)modifierHoRow.getAttribute("LineType");
                                Date startDateLine =
                                    (Date)modifierHoRow.getAttribute("StartDateLine");
                                Date endDateLine =
                                    (Date)modifierHoRow.getAttribute("EndDateLine");
                                String automaticLine =
                                    (String)modifierHoRow.getAttribute("AutomaticLine");
                                String pricingPhase =
                                    (String)modifierHoRow.getAttribute("PricingPhase");
                                String custType =
                                    (String)modifierHoRow.getAttribute("CustomerType");
                                String custTypeKet =
                                    (String)modifierHoRow.getAttribute("CustomerTypeKet");
                                String custGroup =
                                    (String)modifierHoRow.getAttribute("CustomerGroup");
                                String custGroupKet =
                                    (String)modifierHoRow.getAttribute("CustomerGroupKet");
                                oracle.jbo.domain.Number custNumber = (oracle.jbo.domain.Number)modifierHoRow.getAttribute("Customer");
                                String cust = null;
                                if (custNumber != null) {
                                    cust = ((oracle.jbo.domain.Number)modifierHoRow.getAttribute("Customer")).getBigDecimalValue().toPlainString();
                                } else {
                                    cust = null;
                                }
                                String custKet =
                                    (String)modifierHoRow.getAttribute("CustomerKet");
                                String bucket = "";
                                String bucketTemp =
                                    (String)modifierHoRow.getAttribute("Bucket");

                                while (voCekBucket.hasNext()) {
                                    Row rBucket = voCekBucket.next();
                                    String bucketCek =
                                        rBucket.getAttribute("Item").toString();

                                    if (bucketTemp.equalsIgnoreCase(bucketCek)) {
                                        bucket =
                                                rBucket.getAttribute("Bucket").toString();
                                    }
                                }

                                String productAttribute =
                                    (String)modifierHoRow.getAttribute("ProductAttribute");
                                String productValue =
                                    (String)modifierHoRow.getAttribute("ProductValue");
                                String volumeType =
                                    (String)modifierHoRow.getAttribute("VolumeType");
                                String breakType =
                                    (String)modifierHoRow.getAttribute("BreakType");
                                String uom =
                                    (String)modifierHoRow.getAttribute("Uom");
                                Number valueFrom =
                                    (Number)modifierHoRow.getAttribute("ValueFrom");
                                Number valueTo =
                                    (Number)modifierHoRow.getAttribute("ValueTo");
                                String applicationMethod =
                                    (String)modifierHoRow.getAttribute("ApplicationMethod");
                                Number valueMod =
                                    (Number)modifierHoRow.getAttribute("Value");

                                BigDecimal value =
                                    valueMod.getBigDecimalValue();
                                if (ppnFlag.equalsIgnoreCase(ppnExclude)) {
                                    value =
                                            valueMod.getBigDecimalValue().divide(pajakDiv,
                                                                                 12,
                                                                                 RoundingMode.HALF_UP);
                                }

                                Number lineNum =
                                    (Number)modifierHoRow.getAttribute("LineNum");
                                Number groupingNo =
                                    (Number)modifierHoRow.getAttribute("GroupingNo");
                                String qualifierContext =
                                    (String)modifierHoRow.getAttribute("QualifierContext");
                                String qualifierAttr =
                                    (String)modifierHoRow.getAttribute("QualifierAttr");
                                String operatorSign =
                                    (String)modifierHoRow.getAttribute("OperatorSign");
                                String valueQualifier =
                                    (String)modifierHoRow.getAttribute("ValueQualifier");
                                String valueConfirmNoDef =
                                    (String)modifierHoRow.getAttribute("ConfirmNoDef");
                                Number promoProdukId =
                                    (Number)modifierHoRow.getAttribute("PromoProdukId");
                                String ketOntopMf =
                                    (String)modifierHoRow.getAttribute("Ket");
                                Number kelipatan =
                                    (Number)modifierHoRow.getAttribute("Kelipatan");
                                Number productId =
                                    (Number)modifierHoRow.getAttribute("ProductId");
                                String paketFlag =
                                    (String)modifierHoRow.getAttribute("PaketFlag");
                                String prodFlg =
                                    (String)modifierHoRow.getAttribute("ProdFlg");
                                String mulFlg =
                                    (String)modifierHoRow.getAttribute("MulFlg");

                                // Define line produk number
                                if (i == 0 && ppidTemp == null) {
                                    i = i + 1;
                                    ppidTemp = promoProdukId;
                                } else {
                                    if (ppidTemp.getValue() !=
                                        promoProdukId.getValue()) {
                                        i = i + 1;
                                        ppidTemp = promoProdukId;
                                    } else {
                                        // Keep i = i;
                                    }
                                }
                                
                                Row modifierTempRow =
                                    voModifierTemp.createRow();
                                modifierTempRow.setAttribute("ModifierType",
                                                             "Discount List");
                                modifierTempRow.setAttribute("Name",
                                                             noConfirm);
                                modifierTempRow.setAttribute("Description",
                                                             noConfirm);
                                modifierTempRow.setAttribute("Currency",
                                                             currency);
                                modifierTempRow.setAttribute("StartDate",
                                                             startDate);
                                modifierTempRow.setAttribute("EndDate",
                                                             endDate);
                                modifierTempRow.setAttribute("Active", active);
                                modifierTempRow.setAttribute("Automatic",
                                                             automatic);
                                modifierTempRow.setAttribute("Version", null);
                                modifierTempRow.setAttribute("LineLevel",
                                                             lineLevel);
                                modifierTempRow.setAttribute("LineType",
                                                             lineType);
                                modifierTempRow.setAttribute("StartDateLine",
                                                             startDateLine);
                                modifierTempRow.setAttribute("EndDateLine",
                                                             endDateLine);
                                modifierTempRow.setAttribute("AutomaticLine",
                                                             automaticLine);
                                modifierTempRow.setAttribute("PricingPhase",
                                                             pricingPhase);
                                modifierTempRow.setAttribute("ProductAttribute",
                                                             productAttribute);
                                modifierTempRow.setAttribute("ProductAttributeValue",
                                                             productValue);
                                modifierTempRow.setAttribute("VolumeType",
                                                             volumeType);
                                modifierTempRow.setAttribute("BreakType",
                                                             breakType);
                                modifierTempRow.setAttribute("Uom", uom);
                                modifierTempRow.setAttribute("ValueFrom",
                                                             valueFrom);
                                modifierTempRow.setAttribute("ValueTo",
                                                             valueTo);
                                modifierTempRow.setAttribute("ApplicationMethod",
                                                             applicationMethod);
                                modifierTempRow.setAttribute("Value", value);
                                modifierTempRow.setAttribute("GroupingNo",
                                                             groupingNo);
                                modifierTempRow.setAttribute("QualifierContext",
                                                             qualifierContext);
                                modifierTempRow.setAttribute("QualifierAttr",
                                                             qualifierAttr);
                                modifierTempRow.setAttribute("Attribute2",
                                                             valueConfirmNoDef);
                                modifierTempRow.setAttribute("Operator1",
                                                             operatorSign);
                                modifierTempRow.setAttribute("QValueFrom",
                                                             valueQualifier);
                                modifierTempRow.setAttribute("Bucket", bucket);
                                modifierTempRow.setAttribute("Flag", "N");
                                modifierTempRow.setAttribute("ListHeaderId",
                                                             headerIdNum);
                                modifierTempRow.setAttribute("LineNo",
                                                             lineNum);
                                modifierTempRow.setAttribute("CreatedBy",
                                                             userName);
                                modifierTempRow.setAttribute("Kelipatan",
                                                             kelipatan);
                                modifierTempRow.setAttribute("PromoProductId",
                                                             promoProdukId);
                                modifierTempRow.setAttribute("KetOtMf",
                                                             ketOntopMf);
                                modifierTempRow.setAttribute("LineNoPppc",
                                                             productId);
                                modifierTempRow.setAttribute("ItemType",
                                                             paketFlag);
                                modifierTempRow.setAttribute("ProdFlg",
                                                             prodFlg);
                                modifierTempRow.setAttribute("MultipleFlag",
                                                             mulFlg);
                                modifierTempRow.setAttribute("CustomerType",
                                                             custType);
                                modifierTempRow.setAttribute("CustomerTypeKet",
                                                             custTypeKet);
                                modifierTempRow.setAttribute("CustomerGroup",
                                                             custGroup);
                                modifierTempRow.setAttribute("CustomerGroupKet",
                                                             custGroupKet);
                                modifierTempRow.setAttribute("Customer",
                                                             cust);
                                modifierTempRow.setAttribute("CustomerKet",
                                                             custKet);
                            }
                            
                            // Execute HO Modifier Exclude
                            createModifierHoExcl(confirmationAM,
                                                 headerIdNum, userName,
                                                 confirmNo);
                        }

                        propConfirmRow[0].setAttribute("ModifierType", "D");

                        flagRunEbs = "Y";

                    } else if (userTypeCreator.equalsIgnoreCase(userCreatorArea)) {
                        // Fetch Modifier Area
                        FcsModifierAreaViewImpl voModifierArea =
                            confirmationAM.getFcsModifierAreaView1();
                        voModifierArea.setNamedWhereClauseParam("noConfirm",
                                                                confirmNo);
                        voModifierArea.executeQuery();

                        if (voModifierArea.getEstimatedRowCount() > 0) {
                            int i = 0;
                            Number ppidTemp = null;
                            while (voModifierArea.hasNext()) {
                                FcsQpModifierTempViewImpl voModifierTemp =
                                    confirmationAM.getFcsQpModifierTempView1();

                                ViewObjectImpl voCekBucket =
                                    confirmationAM.getBucketView1();
                                voCekBucket.executeQuery();

                                Row modifierAreaRow = voModifierArea.next();
                                String noConfirm =
                                    (String)modifierAreaRow.getAttribute("NoConfirm");
                                String currency =
                                    (String)modifierAreaRow.getAttribute("Currency");
                                Date startDate =
                                    (Date)modifierAreaRow.getAttribute("StartDate");
                                Date endDate =
                                    (Date)modifierAreaRow.getAttribute("EndDate");
                                String active =
                                    (String)modifierAreaRow.getAttribute("Active");
                                String automatic =
                                    (String)modifierAreaRow.getAttribute("Automatic");
                                String lineLevel =
                                    (String)modifierAreaRow.getAttribute("LineLevel");
                                String lineType =
                                    (String)modifierAreaRow.getAttribute("LineType");
                                Date startDateLine =
                                    (Date)modifierAreaRow.getAttribute("StartDateLine");
                                Date endDateLine =
                                    (Date)modifierAreaRow.getAttribute("EndDateLine");
                                String automaticLine =
                                    (String)modifierAreaRow.getAttribute("AutomaticLine");
                                String pricingPhase =
                                    (String)modifierAreaRow.getAttribute("PricingPhase");
                                String custType =
                                    (String)modifierAreaRow.getAttribute("CustomerType");
                                String custTypeKet =
                                    (String)modifierAreaRow.getAttribute("CustomerTypeKet");
                                String custGroup =
                                    (String)modifierAreaRow.getAttribute("CustomerGroup");
                                String custGroupKet =
                                    (String)modifierAreaRow.getAttribute("CustomerGroupKet");
                                oracle.jbo.domain.Number custNumber = (oracle.jbo.domain.Number)modifierAreaRow.getAttribute("Customer");
                                String cust = null;
                                if (custNumber != null) {
                                    cust = ((oracle.jbo.domain.Number)modifierAreaRow.getAttribute("Customer")).getBigDecimalValue().toPlainString();
                                } else {
                                    cust = null;
                                }
                                String custKet =
                                    (String)modifierAreaRow.getAttribute("CustomerKet");
                                String bucket = "";
                                String bucketTemp =
                                    (String)modifierAreaRow.getAttribute("Bucket");

                                while (voCekBucket.hasNext()) {
                                    Row rBucket = voCekBucket.next();
                                    String bucketCek =
                                        rBucket.getAttribute("Item").toString();
                                    if (bucketTemp.equalsIgnoreCase(bucketCek)) {
                                        bucket =
                                                rBucket.getAttribute("Bucket").toString();
                                    }
                                }

                                String productAttribute =
                                    (String)modifierAreaRow.getAttribute("ProductAttribute");
                                String productValue =
                                    (String)modifierAreaRow.getAttribute("ProductValue");
                                String volumeType =
                                    (String)modifierAreaRow.getAttribute("VolumeType");
                                String breakType =
                                    (String)modifierAreaRow.getAttribute("BreakType");
                                String uom =
                                    (String)modifierAreaRow.getAttribute("Uom");
                                Number valueFrom =
                                    (Number)modifierAreaRow.getAttribute("ValueFrom");
                                Number valueTo =
                                    (Number)modifierAreaRow.getAttribute("ValueTo");
                                String applicationMethod =
                                    (String)modifierAreaRow.getAttribute("ApplicationMethod");
                                Number value =
                                    (Number)modifierAreaRow.getAttribute("Value");
                                Number lineNum =
                                    (Number)modifierAreaRow.getAttribute("LineNum");
                                Number groupingNo =
                                    (Number)modifierAreaRow.getAttribute("GroupingNo");
                                String qualifierContext =
                                    (String)modifierAreaRow.getAttribute("QualifierContext");
                                String qualifierAttr =
                                    (String)modifierAreaRow.getAttribute("QualifierAttr");
                                String operatorSign =
                                    (String)modifierAreaRow.getAttribute("OperatorSign");
                                String valueQualifier =
                                    (String)modifierAreaRow.getAttribute("ValueQualifier");
                                String valueConfirmNoDef =
                                    (String)modifierAreaRow.getAttribute("ConfirmNoDef");
                                Number promoProdukId =
                                    (Number)modifierAreaRow.getAttribute("PromoProdukId");
                                String ketOntopMf =
                                    (String)modifierAreaRow.getAttribute("Ket");
                                Number kelipatan =
                                    (Number)modifierAreaRow.getAttribute("Kelipatan");
                                Number productId =
                                    (Number)modifierAreaRow.getAttribute("ProductId");
                                String paketFlag =
                                    (String)modifierAreaRow.getAttribute("PaketFlag");
                                String prodFlg =
                                    (String)modifierAreaRow.getAttribute("ProdFlg");
                                String mulFlg =
                                    (String)modifierAreaRow.getAttribute("MulFlg");

                                // Define line produk number
                                if (i == 0 && ppidTemp == null) {
                                    i = i + 1;
                                    ppidTemp = promoProdukId;
                                } else {
                                    if (ppidTemp.getValue() !=
                                        promoProdukId.getValue()) {
                                        i = i + 1;
                                        ppidTemp = promoProdukId;
                                    } else {
                                        // Keep i = i;
                                    }
                                }

                                Row modifierTempRow =
                                    voModifierTemp.createRow();
                                modifierTempRow.setAttribute("ModifierType",
                                                             "Discount List");
                                modifierTempRow.setAttribute("Name",
                                                             noConfirm);
                                modifierTempRow.setAttribute("Description",
                                                             noConfirm);
                                modifierTempRow.setAttribute("Currency",
                                                             currency);
                                modifierTempRow.setAttribute("StartDate",
                                                             startDate);
                                modifierTempRow.setAttribute("EndDate",
                                                             endDate);
                                modifierTempRow.setAttribute("Active", active);
                                modifierTempRow.setAttribute("Automatic",
                                                             automatic);
                                modifierTempRow.setAttribute("Version", null);
                                modifierTempRow.setAttribute("LineLevel",
                                                             lineLevel);
                                modifierTempRow.setAttribute("LineType",
                                                             lineType);
                                modifierTempRow.setAttribute("StartDateLine",
                                                             startDateLine);
                                modifierTempRow.setAttribute("EndDateLine",
                                                             endDateLine);
                                modifierTempRow.setAttribute("AutomaticLine",
                                                             automaticLine);
                                modifierTempRow.setAttribute("PricingPhase",
                                                             pricingPhase);
                                modifierTempRow.setAttribute("ProductAttribute",
                                                             productAttribute);
                                modifierTempRow.setAttribute("ProductAttributeValue",
                                                             productValue);
                                modifierTempRow.setAttribute("VolumeType",
                                                             volumeType);
                                modifierTempRow.setAttribute("BreakType",
                                                             breakType);
                                modifierTempRow.setAttribute("Uom", uom);
                                modifierTempRow.setAttribute("ValueFrom",
                                                             valueFrom);
                                modifierTempRow.setAttribute("ValueTo",
                                                             valueTo);
                                modifierTempRow.setAttribute("ApplicationMethod",
                                                             applicationMethod);
                                modifierTempRow.setAttribute("Value", value);
                                modifierTempRow.setAttribute("GroupingNo",
                                                             groupingNo);
                                modifierTempRow.setAttribute("QualifierContext",
                                                             qualifierContext);
                                modifierTempRow.setAttribute("QualifierAttr",
                                                             qualifierAttr);
                                modifierTempRow.setAttribute("Attribute2",
                                                             valueConfirmNoDef);
                                modifierTempRow.setAttribute("Operator1",
                                                             operatorSign);
                                modifierTempRow.setAttribute("QValueFrom",
                                                             valueQualifier);
                                modifierTempRow.setAttribute("Bucket", bucket);
                                modifierTempRow.setAttribute("Flag", "N");
                                modifierTempRow.setAttribute("ListHeaderId",
                                                             headerIdNum);
                                modifierTempRow.setAttribute("LineNo",
                                                             lineNum);
                                modifierTempRow.setAttribute("CreatedBy",
                                                             userName);
                                modifierTempRow.setAttribute("Kelipatan",
                                                             kelipatan);
                                modifierTempRow.setAttribute("PromoProductId",
                                                             promoProdukId);
                                modifierTempRow.setAttribute("KetOtMf",
                                                             ketOntopMf);
                                modifierTempRow.setAttribute("LineNoPppc",
                                                             productId);
                                modifierTempRow.setAttribute("ItemType",
                                                             paketFlag);
                                modifierTempRow.setAttribute("ProdFlg",
                                                             prodFlg);
                                modifierTempRow.setAttribute("MultipleFlag",
                                                             mulFlg);
                                modifierTempRow.setAttribute("CustomerType",
                                                             custType);
                                modifierTempRow.setAttribute("CustomerTypeKet",
                                                             custTypeKet);
                                modifierTempRow.setAttribute("CustomerGroup",
                                                             custGroup);
                                modifierTempRow.setAttribute("CustomerGroupKet",
                                                             custGroupKet);
                                modifierTempRow.setAttribute("Customer",
                                                             cust);
                                modifierTempRow.setAttribute("CustomerKet",
                                                             custKet);
                            }
                            
                            // Execute Area Modifier Exclude
                            createModifierAreaExcl(confirmationAM,
                                                   headerIdNum, userName,
                                                   confirmNo);
                        }

                        propConfirmRow[0].setAttribute("ModifierType", "D");

                        flagRunEbs = "Y";

                    } else {
                        JSFUtils.addFacesErrorMessage("Error",
                                                      "Tipe proposal creator tidak dikenali, valid input \"HO\" dan \"AREA\"");
                    }

                    // Commiting data on QP temp
                    try {
                        /*
                        OperationBinding operationBinding =
                            bindings.getOperationBinding("Commit");
                        operationBinding.execute();
                        */
                        confirmationAM.getDBTransaction().commit();
                    } catch (Exception e) {
                        // DO NOT SHOW ERROR
                        System.out.println("PCPP ERR PREPARE DATA MOD: " +
                                           e.getLocalizedMessage());
                    }

                    // Execute procedure EBS push Modifier
                    if (flagRunEbs.equalsIgnoreCase("Y")) {
                        Integer modifRequestId =
                            runConcurentModifPppc(confirmationAM, headerIdNum);
                        if (modifRequestId != null) {
                            Row currRow = dciterPropConfirm.getCurrentRow();
                            currRow.setAttribute("ModRequestId",
                                                 modifRequestId);

                            // Update calculate columm, temporary supaya ada status EBS duluan aja TRANSFERING
                            AttributeBinding statusModifierEbsAttr =
                                (AttributeBinding)bindings.getControlBinding("EbsModifierStatus");
                            statusModifierEbsAttr.setInputValue("TRANSFERING");
                        } else {
                            // Update calculate columm, temporary supaya ada status EBS duluan aja ERROR
                            AttributeBinding statusModifierEbsAttr =
                                (AttributeBinding)bindings.getControlBinding("EbsModifierStatus");
                            statusModifierEbsAttr.setInputValue("ERROR");
                        }
                    }

                    try {
                        /*
                        OperationBinding operationBinding =
                            bindings.getOperationBinding("Commit");
                        operationBinding.execute();
                        */
                        confirmationAM.getDBTransaction().commit();
                    } catch (Exception e) {
                        // DO NOT SHOW ERROR
                        System.out.println("PCPP ERR UPDATE STATUS MOD: " +
                                           e.getLocalizedMessage());
                    }

                } else {
                    /*
                    JSFUtils.addFacesErrorMessage("Error",
                                                  "Kode posting pada daftar produk belum lengkap terisi.");
                    */

                    StringBuilder message = new StringBuilder("<html><body>");
                    for (int j = 0; j < valKodePosting.size(); j++) {
                        String errMsg =
                            valKodePosting.get(j) == null ? "" : valKodePosting.get(j);
                        int prodLineNum = j + 1;
                        if (!errMsg.equalsIgnoreCase("")) {
                            message.append("<p>Produk line (" + prodLineNum +
                                           "): " + errMsg +
                                           " belum terisi.</p>");
                        }
                    }
                    message.append("</body></html>");
                    FacesMessage msg = new FacesMessage(message.toString());
                    msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                    ctx.addMessage(null, msg);
                }

                dciterPropConfirm.setCurrentRowWithKey(parentKey.toStringFormat(true));
                AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropConfirmation);
                AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
            }
        } else {
            JSFUtils.addFacesErrorMessage("Error",
                                          "Create Modifier sudah pernah dilakukan untuk dokumen no  \"" +
                                          confirmNo + "\" ini.");
        }
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
                bindings.findIteratorBinding("ProposalConfirmationView1Iterator");
            Row rDiskon = dcItteratorProposal.getCurrentRow();
            String diskonType =
                rDiskon.getAttribute("DiscountType").toString();
            AttributeBinding AddendumKeAttr =
                (AttributeBinding)bindings.getControlBinding("AddendumKe");
            String AddendumKe =
                (String)AddendumKeAttr.getInputValue() == null ? "0" :
                (String)AddendumKeAttr.getInputValue();

            AttributeBinding mekPenagihanAttr =
                (AttributeBinding)bindings.getControlBinding("MekanismePenagihan1");
            String mekPenagihan = (String)mekPenagihanAttr.getInputValue();

            AttributeBinding DcvFlagAttr =
                (AttributeBinding)bindings.getControlBinding("DcvPcPrFlag");

            String DcvFlag = DcvFlagAttr.getInputValue().toString();
            DCIteratorBinding dcItteratorDiscPromoProd =
                bindings.findIteratorBinding("PromoProdukView1Iterator");
            Row rPromoProd = dcItteratorDiscPromoProd.getCurrentRow();
            float IMF = 0;

            if (diskonType.equalsIgnoreCase(discTypePotongan)) {
                if (!AddendumKe.equalsIgnoreCase("0")) {
                    if (mekPenagihan.equalsIgnoreCase(offInvoice) &&
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

                    } else if (mekPenagihan.equalsIgnoreCase(offInvoice) &&
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
                    } else if (mekPenagihan.equalsIgnoreCase(onInvoice)) {
                        IMF =
Float.valueOf(rPromoProd.getAttribute("DiscMf").toString() == null ? "" :
              rPromoProd.getAttribute("DiscMf").toString());
                    }
                } else {
                    IMF =
Float.valueOf(rPromoProd.getAttribute("DiscMf").toString() == null ? "" :
              rPromoProd.getAttribute("DiscMf").toString());
                }
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
            } else if (diskonType.equalsIgnoreCase(discTypePromoBarang)) {
                if (!AddendumKe.equalsIgnoreCase("0")) {
                    if (mekPenagihan.equalsIgnoreCase(offInvoice) &&
                        DcvFlag.equalsIgnoreCase("N")) {
                        if (!rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString().equalsIgnoreCase("0")) {
                            IMF =
Float.valueOf(rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString() ==
              null ? "" :
              rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString());
                        } else {
                            IMF =
Float.valueOf(rPromoProd.getAttribute("BrgBonusMf").toString() == null ? "" :
              rPromoProd.getAttribute("BrgBonusMf").toString());
                        }
                    } else if (mekPenagihan.equalsIgnoreCase(offInvoice) &&
                               DcvFlag.equalsIgnoreCase("Y")) {
                        if (!rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString().equalsIgnoreCase("0")) {
                            IMF =
Float.valueOf(rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString() ==
              null ? "" :
              rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString());
                        } else {
                            IMF =
Float.valueOf(rPromoProd.getAttribute("BrgBonusMf").toString() == null ? "" :
              rPromoProd.getAttribute("BrgBonusMf").toString());
                        }
                    }
                } else {
                    IMF =
Float.valueOf(rPromoProd.getAttribute("BrgBonusMf").toString() == null ? "" :
              rPromoProd.getAttribute("BrgBonusMf").toString());
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
                String budId =
                    r.getAttribute("BudgetById").toString() == null ? "" :
                    r.getAttribute("BudgetById").toString();
                if (i != dciterBudget.getEstimatedRowCount()) {
                    budgetAmountRow +=
                            Float.valueOf(r.getAttribute("Amount").toString());
                    persenRow +=
                            Float.valueOf(r.getAttribute("Percentage").toString());
                }
                if (budcustId.equalsIgnoreCase(budId)) {
                    amountFirst =
                            Float.valueOf(valueChangeEvent.getNewValue().toString().replaceAll(",",
                                                                                               ""));
                    if (amountFirst > 0) {
                        persen = (amountFirst / IMF) * 100.00f;
                        BigDecimal overpersen =
                            new BigDecimal(persen).add(new BigDecimal(persenRow).setScale(2,
                                                                                          RoundingMode.HALF_UP));
                        BigDecimal percen =
                            new BigDecimal(persen).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal totalAmount =
                            new BigDecimal(budgetAmountRow).add(new BigDecimal(amountFirst));
                        BigDecimal selisihpersen =
                            overpersen.subtract(new BigDecimal(100));
                        if (selisihpersen.compareTo(BigDecimal.ZERO) > 0 &&
                            new BigDecimal(IMF).compareTo(totalAmount) == 0) {
                            r.setAttribute("Percentage",
                                           percen.subtract(selisihpersen).setScale(2,
                                                                                   RoundingMode.HALF_UP));
                        } else {
                            r.setAttribute("Percentage",
                                           percen.setScale(2, RoundingMode.HALF_UP));
                        }
                        btnAddPost.setDisabled(false);
                        AdfFacesContext.getCurrentInstance().addPartialTarget(btnAddPost);
                    } else {
                        JSFUtils.addFacesWarningMessage("Amount harus bernilai lebih dari 0");
                        btnAddPost.setDisabled(true);
                        AdfFacesContext.getCurrentInstance().addPartialTarget(btnAddPost);
                    }
                }
                i = i + 1;
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
                bindings.findIteratorBinding("ProposalConfirmationView1Iterator");
            Row rDiskon = dcItteratorProposal.getCurrentRow();
            String diskonType =
                rDiskon.getAttribute("DiscountType").toString();
            AttributeBinding AddendumKeAttr =
                (AttributeBinding)bindings.getControlBinding("AddendumKe");
            String AddendumKe =
                (String)AddendumKeAttr.getInputValue() == null ? "0" :
                (String)AddendumKeAttr.getInputValue();

            AttributeBinding mekPenagihanAttr =
                (AttributeBinding)bindings.getControlBinding("MekanismePenagihan1");
            String mekPenagihan = (String)mekPenagihanAttr.getInputValue();

            AttributeBinding DcvFlagAttr =
                (AttributeBinding)bindings.getControlBinding("DcvPcPrFlag");

            String DcvFlag = DcvFlagAttr.getInputValue().toString();
            DCIteratorBinding dcItteratorDiscPromoProd =
                bindings.findIteratorBinding("PromoProdukView1Iterator");
            Row rPromoProd = dcItteratorDiscPromoProd.getCurrentRow();
            float IMF = 0;
            if (diskonType.equalsIgnoreCase(discTypePotongan)) {
                if (!AddendumKe.equalsIgnoreCase("0")) {
                    if (mekPenagihan.equalsIgnoreCase(offInvoice) &&
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

                    } else if (mekPenagihan.equalsIgnoreCase(offInvoice) &&
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
                    } else if (mekPenagihan.equalsIgnoreCase(onInvoice)) {
                        IMF =
Float.valueOf(rPromoProd.getAttribute("DiscMf").toString() == null ? "" :
              rPromoProd.getAttribute("DiscMf").toString());
                    }

                } else {
                    IMF =
Float.valueOf(rPromoProd.getAttribute("DiscMf").toString() == null ? "" :
              rPromoProd.getAttribute("DiscMf").toString());
                }
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
            } else if (diskonType.equalsIgnoreCase(discTypePromoBarang)) {
                if (!AddendumKe.equalsIgnoreCase("0")) {
                    if (mekPenagihan.equalsIgnoreCase(offInvoice) &&
                        DcvFlag.equalsIgnoreCase("N")) {
                        if (!rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString().equalsIgnoreCase("0")) {
                            IMF =
Float.valueOf(rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString() ==
              null ? "" :
              rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString());
                        } else {
                            IMF =
Float.valueOf(rPromoProd.getAttribute("BrgBonusMf").toString() == null ? "" :
              rPromoProd.getAttribute("BrgBonusMf").toString());
                        }
                    } else if (mekPenagihan.equalsIgnoreCase(offInvoice) &&
                               DcvFlag.equalsIgnoreCase("Y")) {
                        if (!rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString().equalsIgnoreCase("0")) {
                            IMF =
Float.valueOf(rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString() ==
              null ? "" :
              rPromoProd.getAttribute("RealisasiGrSelisihMfByLine").toString());
                        } else {
                            IMF =
Float.valueOf(rPromoProd.getAttribute("BrgBonusMf").toString() == null ? "" :
              rPromoProd.getAttribute("BrgBonusMf").toString());
                        }
                    }
                } else {
                    IMF =
Float.valueOf(rPromoProd.getAttribute("BrgBonusMf").toString() == null ? "" :
              rPromoProd.getAttribute("BrgBonusMf").toString());
                }
            }
            DCIteratorBinding dciterBudget =
                ADFUtils.findIterator("ProdBudgetByCustView1Iterator");
            BigDecimal grandTotalPersen = BigDecimal.ZERO;
            BigDecimal budgetAmount = BigDecimal.ZERO;
            for (Row ri : dciterBudget.getAllRowsInRange()) {
                oracle.jbo.domain.Number budgetAmountRow =
                    (oracle.jbo.domain.Number)ri.getAttribute("Amount");
                budgetAmount =
                        budgetAmount.add(budgetAmountRow.getBigDecimalValue());
                BigDecimal persenRow =
                    new BigDecimal(ri.getAttribute("Percentage").toString());
                grandTotalPersen = grandTotalPersen.add(persenRow);
            }

            int i = 1;
            float persenRow = 0;
            float budgetAmountRow = 0;
            for (Row r : dciterBudget.getAllRowsInRange()) {
                float amountFirst = 0;
                float persen = 0;
                String budId =
                    r.getAttribute("BudgetById").toString() == null ? "" :
                    r.getAttribute("BudgetById").toString();
                if (i != dciterBudget.getEstimatedRowCount()) {
                    budgetAmountRow +=
                            Float.valueOf(r.getAttribute("Amount").toString());
                    persenRow +=
                            Float.valueOf(r.getAttribute("Percentage").toString());
                }
                if (budcustId.equalsIgnoreCase(budId)) {
                    amountFirst =
                            Float.valueOf(valueChangeEvent.getNewValue().toString().replaceAll(",",
                                                                                               ""));
                    if (amountFirst > 0) {
                        persen = (amountFirst / IMF) * 100.00f;
                        BigDecimal overpersen =
                            new BigDecimal(persen).add(new BigDecimal(persenRow).setScale(2,
                                                                                          RoundingMode.HALF_UP));
                        BigDecimal percen =
                            new BigDecimal(persen).setScale(2, RoundingMode.HALF_UP);
                        BigDecimal totalAmount =
                            new BigDecimal(budgetAmountRow).add(new BigDecimal(amountFirst));
                        BigDecimal selisihpersen =
                            overpersen.subtract(new BigDecimal(100));
                        if (selisihpersen.compareTo(BigDecimal.ZERO) > 0 &&
                            new BigDecimal(IMF).compareTo(totalAmount) == 0) {
                            r.setAttribute("Percentage",
                                           percen.subtract(selisihpersen).setScale(2,
                                                                                   RoundingMode.HALF_UP));
                        } else {
                            r.setAttribute("Percentage",
                                           percen.setScale(2, RoundingMode.HALF_UP));
                        }
                        btnAddCust.setDisabled(false);
                        AdfFacesContext.getCurrentInstance().addPartialTarget(btnAddCust);
                    } else {
                        JSFUtils.addFacesWarningMessage("Amount harus bernilai lebih dari 0");
                        btnAddCust.setDisabled(true);
                        AdfFacesContext.getCurrentInstance().addPartialTarget(btnAddCust);
                    }
                }
                i = i + 1;
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
        DCIteratorBinding dciterPropConfirm =
            ADFUtils.findIterator("ProposalConfirmationView1Iterator");
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

        AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropConfirmation);
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
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

    public void clearFailPrCreation(ConfirmationAMImpl confirmationAM) {
        FcsPoRequisitionTempViewImpl prTableTemp =
            (FcsPoRequisitionTempViewImpl)confirmationAM.getFcsPoRequisitionTempView1();
        prTableTemp.setWhereClause("EXISTS (SELECT 1 FROM APPS.FCS_PO_REQUISITION_TEMP FPR WHERE FPR.REQ_NUMBER_SEGMENT1 = FcsPoRequisitionTemp.REQ_NUMBER_SEGMENT1 AND (FPR.STATUS LIKE 'ERROR%' OR STATUS = 'FAIL'))");
        prTableTemp.executeQuery();

        if (prTableTemp.getEstimatedRowCount() > 0) {
            while (prTableTemp.hasNext()) {
                Row prTableTempRow = prTableTemp.next();
                prTableTempRow.remove();
            }
            confirmationAM.getDBTransaction().commit();
        }
    }

    public Integer runConcurentPrPppc(ConfirmationAMImpl confirmationAM,
                                      String batchIdGroup) {
        CallableStatement cst = null;
        Integer responMsg = null;
        try {
            cst =
confirmationAM.getDBTransaction().createCallableStatement("BEGIN APPS.FCS_RUN_CONCURRENT_PR_PPPC('" +
                                                          batchIdGroup +
                                                          "', ?); END;", 0);
            cst.registerOutParameter(1, Types.NUMERIC);
            cst.executeUpdate();
            responMsg = cst.getInt(1);
            //System.out.println("RESPON MSG: " + responMsg.toString());
            JSFUtils.addFacesInformationMessage("PR sudah di submit dengan no request: " +
                                                responMsg.toString());
        } catch (SQLException e) {
            JSFUtils.addFacesErrorMessage(e.getMessage());
        } finally {
            if (cst != null) {
                try {
                    cst.close();
                } catch (SQLException e) {
                    JSFUtils.addFacesErrorMessage(e.getMessage());
                }
            }
        }
        return responMsg;
    }

    public Integer runConcurentModifPppc(ConfirmationAMImpl confirmationAM,
                                         Number listHeaderId) {
        CallableStatement cst = null;
        Integer responMsg = null;
        try {
            cst =
confirmationAM.getDBTransaction().createCallableStatement("BEGIN APPS.FCS_RUN_CONCURRENT_MODIF_PPPC('" +
                                                          listHeaderId +
                                                          "', ?); END;", 0);
            cst.registerOutParameter(1, Types.NUMERIC);
            cst.executeUpdate();
            responMsg = cst.getInt(1);
            JSFUtils.addFacesInformationMessage("Modifier sudah di submit dengan no request: " +
                                                responMsg.toString());
        } catch (SQLException e) {
            JSFUtils.addFacesErrorMessage(e.getMessage());
        } finally {
            if (cst != null) {
                try {
                    cst.close();
                } catch (SQLException e) {
                    JSFUtils.addFacesErrorMessage(e.getMessage());
                }
            }
        }
        return responMsg;
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

    public void addRowEvent(ActionEvent actionEvent) {
        BindingContainer bindings =
            BindingContext.getCurrent().getCurrentBindingsEntry();

        oracle.jbo.domain.Number budgetAmount = new Number(0);
        oracle.jbo.domain.Number sisaAmountBudget = new Number(0);
        oracle.jbo.domain.Number amountMf = new Number(0);
        BigDecimal grandTotalPersen = BigDecimal.ZERO;

        AttributeBinding biaMfAttr =
            (AttributeBinding)bindings.getControlBinding("BiaMf");
        String biaMf = (String)biaMfAttr.getInputValue();
        AttributeBinding discMfAttr =
            (AttributeBinding)bindings.getControlBinding("DiscMf");
        String discMf = (String)discMfAttr.getInputValue();
        AttributeBinding brgBonusMfAttr =
            (AttributeBinding)bindings.getControlBinding("BrgBonusMf");
        String brgBonusMf = (String)brgBonusMfAttr.getInputValue();
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
            (AttributeBinding)bindings.getControlBinding("MekanismePenagihan1");
        String mekPenagihan = (String)mekPenagihanAttr.getInputValue();

        AttributeBinding DcvFlagAttr =
            (AttributeBinding)bindings.getControlBinding("DcvPcPrFlag");
        String DcvFlag = DcvFlagAttr.getInputValue().toString();

        DCIteratorBinding dciter =
            (DCIteratorBinding)bindings.get("ProdBudgetByCustView1Iterator");

        for (Row r : dciter.getAllRowsInRange()) {
            oracle.jbo.domain.Number budgetAmountRow =
                (oracle.jbo.domain.Number)r.getAttribute("Amount");
            budgetAmount = budgetAmount.add(budgetAmountRow);
            BigDecimal persenRow =
                new BigDecimal(r.getAttribute("Percentage").toString());
            grandTotalPersen = grandTotalPersen.add(persenRow);
        }

        if (discountType.equalsIgnoreCase(discTypePromoBarang)) {
            try {
                if (!AddendumKe.equalsIgnoreCase("0")) {
                    if (mekPenagihan.equalsIgnoreCase(offInvoice) &&
                        DcvFlag.equalsIgnoreCase("N")) {
                        if (!RealisasiGrSelisihMfByLine.equalsIgnoreCase("0")) {
                            amountMf =
                                    new oracle.jbo.domain.Number(RealisasiGrSelisihMfByLine.replaceAll(",",
                                                                                                       ""));
                        } else {
                            amountMf =
                                    new oracle.jbo.domain.Number(brgBonusMf.replaceAll(",",
                                                                                       ""));
                        }
                    } else if (mekPenagihan.equalsIgnoreCase(offInvoice) &&
                               DcvFlag.equalsIgnoreCase("Y")) {
                        if (!RealisasiGrSelisihMfByLine.equalsIgnoreCase("0")) {
                            amountMf =
                                    new oracle.jbo.domain.Number(RealisasiGrSelisihMfByLine.replaceAll(",",
                                                                                                       ""));
                        } else {
                            amountMf =
                                    new oracle.jbo.domain.Number(brgBonusMf.replaceAll(",",
                                                                                       ""));
                        }
                    } else if (mekPenagihan.equalsIgnoreCase(onInvoice)) {
                        amountMf =
                                new oracle.jbo.domain.Number(brgBonusMf.replaceAll(",",
                                                                                   ""));
                    }
                } else {
                    amountMf =
                            new oracle.jbo.domain.Number(brgBonusMf.replaceAll(",",
                                                                               ""));
                }
            } catch (SQLException e) {
                amountMf = new Number(0);
            }
        } else if (discountType.equalsIgnoreCase(discTypeBiaya)) {
            try {
                if (!AddendumKe.equalsIgnoreCase("0")) {
                    if (!RealisasiGrSelisihMfByLine.equalsIgnoreCase("0")) {
                        amountMf =
                                new oracle.jbo.domain.Number(RealisasiGrSelisihMfByLine.replaceAll(",",
                                                                                                   ""));
                    } else {
                        amountMf =
                                new oracle.jbo.domain.Number(biaMf.replaceAll(",",
                                                                              ""));
                    }
                } else {
                    amountMf =
                            new oracle.jbo.domain.Number(biaMf.replaceAll(",",
                                                                          ""));
                }
            } catch (SQLException e) {
                amountMf = new Number(0);
            }
        } else if (discountType.equalsIgnoreCase(discTypePotongan)) {
            try {
                if (!AddendumKe.equalsIgnoreCase("0")) {
                    if (mekPenagihan.equalsIgnoreCase(offInvoice) &&
                        DcvFlag.equalsIgnoreCase("N")) {
                        if (!RealisasiGrSelisihMfByLine.equalsIgnoreCase("0")) {
                            amountMf =
                                    new oracle.jbo.domain.Number(RealisasiGrSelisihMfByLine.replaceAll(",",
                                                                                                       ""));
                        } else {
                            amountMf =
                                    new oracle.jbo.domain.Number(discMf.replaceAll(",",
                                                                                   ""));
                        }

                    } else if (mekPenagihan.equalsIgnoreCase(offInvoice) &&
                               DcvFlag.equalsIgnoreCase("Y")) {
                        if (!RealisasiGrSelisihMfByLine.equalsIgnoreCase("0")) {
                            amountMf =
                                    new oracle.jbo.domain.Number(RealisasiGrSelisihMfByLine.replaceAll(",",
                                                                                                       ""));
                        } else {
                            amountMf =
                                    new oracle.jbo.domain.Number(discMf.replaceAll(",",
                                                                                   ""));
                        }
                    } else if (mekPenagihan.equalsIgnoreCase(onInvoice)) {
                        amountMf =
                                new oracle.jbo.domain.Number(discMf.replaceAll(",",
                                                                               ""));
                    }

                } else {
                    amountMf =
                            new oracle.jbo.domain.Number(discMf.replaceAll(",",
                                                                           ""));
                }

            } catch (SQLException e) {
                amountMf = new Number(0);
            }
        }

        sisaAmountBudget = (Number)amountMf.minus(budgetAmount);
        if (sisaAmountBudget.compareTo(new Number(0)) > 0) {
            //Percent calculation
            float sisaAmtBudgetFloat =
                sisaAmountBudget.getBigDecimalValue().floatValue();
            float amountMfFloat = amountMf.getBigDecimalValue().floatValue();
            float sisaAmtPercentFloat =
                (sisaAmtBudgetFloat / amountMfFloat) * 100.00f;
            RowSetIterator rsi = dciter.getRowSetIterator();
            Row lastRow = rsi.last();
            int lastRowIndex = rsi.getRangeIndexOf(lastRow);
            Row newRow = rsi.createRow();
            newRow.setNewRowState(Row.STATUS_INITIALIZED);
            newRow.setAttribute("Amount", sisaAmountBudget);
            //            System.out.println(grandTotalPersen.add(new BigDecimal(toPercentage(sisaAmtPercentFloat, 2))));
            BigDecimal totalAmount =
                budgetAmount.getBigDecimalValue().add(sisaAmountBudget.getBigDecimalValue());
            BigDecimal totalPersenPerRow =
                grandTotalPersen.add(new BigDecimal(toPercentage(sisaAmtPercentFloat,
                                                                 2)));
            BigDecimal selisihpersen =
                totalPersenPerRow.subtract(new BigDecimal(100));
            if (selisihpersen.compareTo(BigDecimal.ZERO) > 0 &&
                amountMf.getBigDecimalValue().compareTo(totalAmount) == 0) {
                newRow.setAttribute("Percentage",
                                    new BigDecimal(toPercentage(sisaAmtPercentFloat,
                                                                2)).subtract(selisihpersen));
            } else {
                newRow.setAttribute("Percentage",
                                    toPercentage(sisaAmtPercentFloat, 2));
            }
            newRow.setAttribute("Status", "N");
            //add row to last index + 1 so it becomes last in the range set
            rsi.insertRowAtRangeIndex(lastRowIndex + 1, newRow);
            //make row the current row so it is displayed correctly
            rsi.setCurrentRow(newRow);

            AdfFacesContext.getCurrentInstance().addPartialTarget(tblBudgetCustomer);

        } else {
            JSFUtils.addFacesWarningMessage("Total alokasi budget sudah mencapai 100%");
        }
    }

    public void addRowPostEvent(ActionEvent actionEvent) {
        BindingContainer bindings =
            BindingContext.getCurrent().getCurrentBindingsEntry();

        oracle.jbo.domain.Number budgetAmount = new Number(0);
        oracle.jbo.domain.Number sisaAmountBudget = new Number(0);
        oracle.jbo.domain.Number amountMf = new Number(0);
        BigDecimal grandTotalPersen = BigDecimal.ZERO;

        AttributeBinding biaMfAttr =
            (AttributeBinding)bindings.getControlBinding("BiaMf");
        String biaMf = (String)biaMfAttr.getInputValue();
        AttributeBinding discMfAttr =
            (AttributeBinding)bindings.getControlBinding("DiscMf");
        String discMf = (String)discMfAttr.getInputValue();
        AttributeBinding brgBonusMfAttr =
            (AttributeBinding)bindings.getControlBinding("BrgBonusMf");
        String brgBonusMf = (String)brgBonusMfAttr.getInputValue();

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
            (AttributeBinding)bindings.getControlBinding("MekanismePenagihan1");
        String mekPenagihan = (String)mekPenagihanAttr.getInputValue();

        AttributeBinding DcvFlagAttr =
            (AttributeBinding)bindings.getControlBinding("DcvPcPrFlag");
        String DcvFlag = DcvFlagAttr.getInputValue().toString();

        DCIteratorBinding dciter =
            (DCIteratorBinding)bindings.get("ProdBudgetByPostView1Iterator");
        for (Row r : dciter.getAllRowsInRange()) {
            oracle.jbo.domain.Number budgetAmountRow =
                (oracle.jbo.domain.Number)r.getAttribute("Amount");
            budgetAmount = budgetAmount.add(budgetAmountRow);
            BigDecimal persenRow =
                new BigDecimal(r.getAttribute("Percentage").toString());
            grandTotalPersen = grandTotalPersen.add(persenRow);
        }

        if (discountType.equalsIgnoreCase(discTypePromoBarang)) {
            try {
                if (!AddendumKe.equalsIgnoreCase("0")) {
                    if (mekPenagihan.equalsIgnoreCase(offInvoice) &&
                        DcvFlag.equalsIgnoreCase("N")) {
                        if (!RealisasiGrSelisihMfByLine.equalsIgnoreCase("0")) {
                            amountMf =
                                    new oracle.jbo.domain.Number(RealisasiGrSelisihMfByLine.replaceAll(",",
                                                                                                       ""));
                        } else {
                            amountMf =
                                    new oracle.jbo.domain.Number(brgBonusMf.replaceAll(",",
                                                                                       ""));
                        }
                    } else if (mekPenagihan.equalsIgnoreCase(offInvoice) &&
                               DcvFlag.equalsIgnoreCase("Y")) {
                        if (!RealisasiGrSelisihMfByLine.equalsIgnoreCase("0")) {
                            amountMf =
                                    new oracle.jbo.domain.Number(RealisasiGrSelisihMfByLine.replaceAll(",",
                                                                                                       ""));
                        } else {
                            amountMf =
                                    new oracle.jbo.domain.Number(brgBonusMf.replaceAll(",",
                                                                                       ""));
                        }
                    }
                    //                    else if(mekPenagihan.equals(1)){
                    //
                    //                    }
                } else {
                    amountMf =
                            new oracle.jbo.domain.Number(brgBonusMf.replaceAll(",",
                                                                               ""));
                }
            } catch (SQLException e) {
                amountMf = new Number(0);
            }
        } else if (discountType.equalsIgnoreCase(discTypeBiaya)) {
            try {
                if (!AddendumKe.equalsIgnoreCase("0")) {
                    if (!RealisasiGrSelisihMfByLine.equalsIgnoreCase("0")) {
                        amountMf =
                                new oracle.jbo.domain.Number(RealisasiGrSelisihMfByLine.replaceAll(",",
                                                                                                   ""));
                    } else {
                        amountMf =
                                new oracle.jbo.domain.Number(biaMf.replaceAll(",",
                                                                              ""));
                    }
                } else {
                    amountMf =
                            new oracle.jbo.domain.Number(biaMf.replaceAll(",",
                                                                          ""));
                }
            } catch (SQLException e) {
                amountMf = new Number(0);
            }
        } else if (discountType.equalsIgnoreCase(discTypePotongan)) {
            try {
                if (!AddendumKe.equalsIgnoreCase("0")) {
                    if (mekPenagihan.equalsIgnoreCase(offInvoice) &&
                        DcvFlag.equalsIgnoreCase("N")) {
                        if (!RealisasiGrSelisihMfByLine.equalsIgnoreCase("0")) {
                            amountMf =
                                    new oracle.jbo.domain.Number(RealisasiGrSelisihMfByLine.replaceAll(",",
                                                                                                       ""));
                        } else {
                            amountMf =
                                    new oracle.jbo.domain.Number(discMf.replaceAll(",",
                                                                                   ""));
                        }

                    } else if (mekPenagihan.equalsIgnoreCase(offInvoice) &&
                               DcvFlag.equalsIgnoreCase("Y")) {
                        if (!RealisasiGrSelisihMfByLine.equalsIgnoreCase("0")) {
                            amountMf =
                                    new oracle.jbo.domain.Number(RealisasiGrSelisihMfByLine.replaceAll(",",
                                                                                                       ""));
                        } else {
                            amountMf =
                                    new oracle.jbo.domain.Number(discMf.replaceAll(",",
                                                                                   ""));
                        }
                    } else if (mekPenagihan.equalsIgnoreCase(onInvoice)) {
                        amountMf =
                                new oracle.jbo.domain.Number(discMf.replaceAll(",",
                                                                               ""));
                    }
                } else {
                    amountMf =
                            new oracle.jbo.domain.Number(discMf.replaceAll(",",
                                                                           ""));
                }

            } catch (SQLException e) {
                amountMf = new Number(0);
            }
        }

        sisaAmountBudget = (Number)amountMf.minus(budgetAmount);

        if (sisaAmountBudget.compareTo(new Number(0)) > 0) {
            //Percent calculation
            float sisaAmtBudgetFloat =
                sisaAmountBudget.getBigDecimalValue().floatValue();
            float amountMfFloat = amountMf.getBigDecimalValue().floatValue();
            float sisaAmtPercentFloat =
                (sisaAmtBudgetFloat / amountMfFloat) * 100.00f;

            RowSetIterator rsi = dciter.getRowSetIterator();
            Row lastRow = rsi.last();
            int lastRowIndex = rsi.getRangeIndexOf(lastRow);
            Row newRow = rsi.createRow();
            newRow.setNewRowState(Row.STATUS_INITIALIZED);
            newRow.setAttribute("Amount", sisaAmountBudget);
            BigDecimal totalAmount =
                budgetAmount.getBigDecimalValue().add(sisaAmountBudget.getBigDecimalValue());
            BigDecimal totalPersenPerRow =
                grandTotalPersen.add(new BigDecimal(toPercentage(sisaAmtPercentFloat,
                                                                 2)));
            BigDecimal selisihpersen =
                totalPersenPerRow.subtract(new BigDecimal(100));
            if (selisihpersen.compareTo(BigDecimal.ZERO) > 0 &&
                amountMf.getBigDecimalValue().compareTo(totalAmount) == 0) {
                newRow.setAttribute("Percentage",
                                    new BigDecimal(toPercentage(sisaAmtPercentFloat,
                                                                2)).subtract(selisihpersen));
            } else {
                newRow.setAttribute("Percentage",
                                    toPercentage(sisaAmtPercentFloat, 2));
            }
            newRow.setAttribute("Status", "N");
            //add row to last index + 1 so it becomes last in the range set
            rsi.insertRowAtRangeIndex(lastRowIndex + 1, newRow);
            //make row the current row so it is displayed correctly
            rsi.setCurrentRow(newRow);

            AdfFacesContext.getCurrentInstance().addPartialTarget(tblBudgetPosting);
            /*
            OperationBinding operationBinding =
                bindings.getOperationBinding("CreateInsert1");
            operationBinding.execute();
            */
        } else {
            JSFUtils.addFacesWarningMessage("Total alokasi budget sudah mencapai 100%");
        }
    }

    public void addRowRealItemPaket(ActionEvent actionEvent) {
        BindingContainer bindings =
            BindingContext.getCurrent().getCurrentBindingsEntry();
        OperationBinding operationBinding =
            bindings.getOperationBinding("CreateInsertItemPaket");
        operationBinding.execute();
    }

    public void deleteRowRealItemPaket(ActionEvent actionEvent) {
        BindingContainer bindings =
            BindingContext.getCurrent().getCurrentBindingsEntry();
        OperationBinding operationBinding =
            bindings.getOperationBinding("DeleteItemPaket");
        operationBinding.execute();
    }

    public void setAmount(RichInputText amount) {
        this.amount = amount;
    }

    public RichInputText getAmount() {
        return amount;
    }

    public void hitung(ActionEvent actionEvent) {
        int onTop =
            Integer.parseInt(itBudgetOnTop.getValue() == null ? "0" : itBudgetOnTop.getValue().toString().replaceAll(",",
                                                                                                                     ""));
        int IMF =
            Integer.parseInt(itBudgetMF.getValue() == null ? "0" : itBudgetMF.getValue().toString().replaceAll(",",
                                                                                                               ""));
        int total = onTop + IMF;
        int amounts =
            Integer.parseInt(amount.getValue() == null ? "0" : amount.getValue().toString().replaceAll(",",
                                                                                                       ""));
        float persen = (amounts * 100.0f) / total;
        itBudgetCustPercent.setValue(toPercentage(persen, 2));
        AdfFacesContext.getCurrentInstance().addPartialTarget(itBudgetCustPercent);
    }

    public void hitungPost(ActionEvent actionEvent) {
        int onTop =
            Integer.parseInt(itBudgetOnTop.getValue() == null ? "0" : itBudgetOnTop.getValue().toString().replaceAll(",",
                                                                                                                     ""));
        int IMF =
            Integer.parseInt(itBudgetMF.getValue() == null ? "0" : itBudgetMF.getValue().toString().replaceAll(",",
                                                                                                               ""));
        int total = onTop + IMF;
        int amountspost =
            Integer.parseInt(itBudgetPostAmount.getValue() == null ? "0" :
                             itBudgetPostAmount.getValue().toString().replaceAll(",",
                                                                                 ""));
        float persen = (amountspost * 100.0f) / total;
        itBudgetPostPercent.setValue(toPercentage(persen, 2));
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblBudgetPosting);
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

    public void selectForOverBudget(ActionEvent actionEvent) {
        DCBindingContainer bindingsSelRow =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dciterCustBudgerOver =
            bindingsSelRow.findIteratorBinding("ProdBudgetByCustView1Iterator");
        Key parentKey = dciterCustBudgerOver.getCurrentRow().getKey();
        Row r = dciterCustBudgerOver.getCurrentRow();
        String Budgetid = r.getAttribute("BudgetById").toString();
        String BudgerOver = r.getAttribute("FlagStatusOver").toString();
        String BudgerOverNew = "N";
        if (BudgerOver.equals("N")) {
            BudgerOverNew = "Y";
        }
        r.setAttribute("FlagStatusOver", BudgerOverNew);
        for (Row row : dciterCustBudgerOver.getAllRowsInRange()) {
            if (!Budgetid.equalsIgnoreCase(row.getAttribute("BudgetById").toString()) &&
                row.getAttribute("FlagStatusOver").toString().equalsIgnoreCase("Y")) {
                row.setAttribute("FlagStatusOver", "N");
            }
        }
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblBudgetCustomer);
        dciterCustBudgerOver.setCurrentRowWithKey(parentKey.toStringFormat(true));
    }

    public void selectForOverBudgetPost(ActionEvent actionEvent) {
        DCBindingContainer bindingsSelRow =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dciterCustBudgerOver =
            bindingsSelRow.findIteratorBinding("ProdBudgetByPostView1Iterator");
        Row r = dciterCustBudgerOver.getCurrentRow();
        String Budgetid = r.getAttribute("BudgetById").toString();

        String BudgerOver = r.getAttribute("FlagStatusOver").toString();
        String BudgerOverNew = "N";
        if (BudgerOver.equals("N")) {
            BudgerOverNew = "Y";
        }
        r.setAttribute("FlagStatusOver", BudgerOverNew);
        for (Row row : dciterCustBudgerOver.getAllRowsInRange()) {
            if (!Budgetid.equalsIgnoreCase(row.getAttribute("BudgetById").toString()) &&
                row.getAttribute("FlagStatusOver").toString().equalsIgnoreCase("Y")) {
                row.setAttribute("FlagStatusOver", "N");
            }
        }
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblBudgetPosting);
    }


    public void dialogEventCustomDetailProduct(ActionEvent actionEvent) {
        BindingContainer bindings =
            BindingContext.getCurrent().getCurrentBindingsEntry();

        ConfirmationAMImpl confirmationAM =
            (ConfirmationAMImpl)ADFUtils.getApplicationModuleForDataControl("ConfirmationAMDataControl");

        DCIteratorBinding dciterPropConfirm =
            ADFUtils.findIterator("ProposalConfirmationView1Iterator");
        Key parentKey = dciterPropConfirm.getCurrentRow().getKey();

        AttributeBinding confirmNoAttr =
            (AttributeBinding)bindings.getControlBinding("ConfirmNo");
        String confirmNo = (String)confirmNoAttr.getInputValue();

        AttributeBinding DiscountTypeAttr =
            (AttributeBinding)bindings.getControlBinding("DiscountType1");
        String DiscountType =
            DiscountTypeAttr.getInputValue().toString();
        AttributeBinding mekPenagihanAttr1 =
            (AttributeBinding)bindings.getControlBinding("MekanismePenagihan1");
        String mekPenagihan1 = (String)mekPenagihanAttr1.getInputValue();
        DCIteratorBinding dciterPromoProduk =
            ADFUtils.findIterator("PromoProdukView1Iterator");
        Key promoProdKey = dciterPromoProduk.getCurrentRow().getKey();
        Row rSel = dciterPromoProduk.getCurrentRow();
        String promoProdukId = rSel.getAttribute("PromoProdukId").toString();
        
        if (DiscountType.equalsIgnoreCase(discTypeBiaya)) {
            if (itQtyBiaya.getValue() != null) {
                itLovBiayaUom.setRequired(true);
                AdfFacesContext.getCurrentInstance().addPartialTarget(itLovBiayaUom);
            } else {
                itLovBiayaUom.setRequired(false);
                AdfFacesContext.getCurrentInstance().addPartialTarget(itLovBiayaUom);
            }
        }
        
        try {
            String budgetByChoice = itLovBudgetBy.getValue().toString();
            if (budgetByChoice.equalsIgnoreCase(budgetByCust)) {
                DCIteratorBinding dciterCust =
                    ADFUtils.findIterator("ProdBudgetByCustView1Iterator");
                float totalPercen = 0;
                Connection conn = null;
                if (dciterCust.getEstimatedRowCount() > 0) {
                    if (DiscountType.equalsIgnoreCase(discTypePotongan) &&
                        mekPenagihan1.equalsIgnoreCase(onInvoice)) {
                        List<String> list = new ArrayList<String>();
                        List<String> count = new ArrayList<String>();
                        for (Row budgetCusr : dciterCust.getAllRowsInRange()) {
                            String BudgetById =
                                budgetCusr.getAttribute("BudgetById") == null ?
                                "" :
                                budgetCusr.getAttribute("BudgetById").toString();
                            String StatusOver =
                                budgetCusr.getAttribute("FlagStatusOver") ==
                                null ? "" :
                                budgetCusr.getAttribute("FlagStatusOver").toString();
                            if (StatusOver.equalsIgnoreCase("Y")) {
                                list.add(StatusOver);
                            }
                            count.add(BudgetById);
                        }

                        if (list.size() == 0) {
                            showPopup("Pilih \"Over Budget\" disalah satu budget.",
                                      potmessage);
                            //
                        } else {
                            RowSetIterator BudMainCust =
                                dciterCust.getRowSetIterator();
                            HashMap<String, BigDecimal> mapOfSum =
                                new HashMap<String, BigDecimal>();
                            BigDecimal budgetAsToDateUsed = BigDecimal.ZERO;
                            StatusOverBudgetId =
                                    new ArrayList<ListStatusOverBudgetId>();
                            for (Row budgetCusr :
                                 dciterCust.getAllRowsInRange()) {
                                String CheckRowStatus =
                                    budgetCusr.getAttribute("CheckRowStatus").toString();
                                String StatusOver =
                                    budgetCusr.getAttribute("FlagStatusOver") ==
                                    null ? "" :
                                    budgetCusr.getAttribute("FlagStatusOver").toString();
                                String BudgetById =
                                    budgetCusr.getAttribute("BudgetById") ==
                                    null ? "" :
                                    budgetCusr.getAttribute("BudgetById").toString();
                                totalPercen +=
                                        Float.valueOf(budgetCusr.getAttribute("Percentage").toString());
                                if (CheckRowStatus.equalsIgnoreCase("0")) {
                                    ListStatusOverBudgetId ov =
                                        new ListStatusOverBudgetId();
                                    ov.setBudgetId(BudgetById);
                                    ov.setStatusOver(StatusOver);
                                    String custId =
                                        budgetCusr.getAttribute("BudgetCustId").toString() +
                                        ";" +
                                        budgetCusr.getAttribute("KombinasiBudget").toString();
                                    if (mapOfSum.containsKey(custId)) {
                                        BigDecimal total =
                                            new BigDecimal(budgetCusr.getAttribute("Amount").toString());
                                        budgetAsToDateUsed =
                                                budgetAsToDateUsed.add(total);
                                    } else {
                                        BigDecimal total =
                                            new BigDecimal(budgetCusr.getAttribute("Amount").toString());
                                        budgetAsToDateUsed = total;
                                    }
                                    mapOfSum.put(custId, budgetAsToDateUsed);
                                    StatusOverBudgetId.add(ov);
                                }
                            }
                            BudMainCust.closeRowSetIterator();
                            listBudgetRemainingValidasi =
                                    new ArrayList<ListBudgetRemainingValidasi>();
                            PreparedStatement statement = null;
                            try {
                                Context ctx = new InitialContext();
                                DataSource ds =
                                    (DataSource)ctx.lookup(dsFdiConn);
                                conn = ds.getConnection();
                                conn.setAutoCommit(false);

                                checkOverBudgetClearViewImpl budgetOversub =
                                    confirmationAM.getcheckOverBudgetClearView1();
                                budgetOversub.setNamedWhereClauseParam("confirmNO",
                                                                       confirmNo);
                                budgetOversub.executeQuery();
                                //begin loop

                                BigDecimal budgetAsTodateCur = BigDecimal.ZERO;
                                BigDecimal budgetAsTodate = BigDecimal.ZERO;
                                BigDecimal overBudgetAmountCur =
                                    BigDecimal.ZERO;
                                BigDecimal budgetAsTodateRemaining =
                                    BigDecimal.ZERO;
                                BigDecimal budgetAsTodateRemainingInfo =
                                    BigDecimal.ZERO;
                                for (Map.Entry<String, BigDecimal> entry :
                                     mapOfSum.entrySet()) {
                                    ListBudgetRemainingValidasi ar =
                                        new ListBudgetRemainingValidasi();
                                    String cusId = "";
                                    String budgetComb = "";
                                    String statusOver = "";
                                    String budId = "";
                                    String key = entry.getKey();
                                    String[] keys = key.split(";");
                                    cusId = keys[0].trim();
                                    budgetComb = keys[1].trim();
                                    statement =
                                            conn.prepareStatement("select BUDGETCUSTOMER.BUDGET_CUSTOMER_ID ,BUDGETCUSTOMER.BUDGET_AS_TO_DATE, BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED,BUDGETCUSTOMER.YEARLY_BUDGET_AMOUNT,BUDGETCUSTOMER.OVER_BUDGET from BUDGET_CUSTOMER BudgetCustomer where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID=? for update");
                                    statement.setString(1, cusId);
                                    ResultSet rs = statement.executeQuery();
                                    while (rs.next()) {
                                        String BudgetCustId =
                                            rs.getString("BUDGET_CUSTOMER_ID").toString();
                                        budgetAsTodateCur =
                                                new BigDecimal(rs.getString("BUDGET_AS_TO_DATE_USED").toString());
                                        budgetAsTodate =
                                                new BigDecimal(rs.getString("BUDGET_AS_TO_DATE").toString());
                                        overBudgetAmountCur =
                                                new BigDecimal(rs.getString("OVER_BUDGET") ==
                                                               null ? "0" :
                                                               rs.getString("OVER_BUDGET").toString());
                                        BigDecimal value =
                                            entry.getValue().add(budgetAsTodateCur);
                                        budgetAsTodateRemainingInfo =
                                                budgetAsTodate.subtract(budgetAsTodateCur);
                                        budgetAsTodateRemaining =
                                                budgetAsTodate.subtract(value);
                                        if (budgetOversub.getEstimatedRowCount() >
                                            0) {
                                            List<ListStatusOverBudgetId> ovget =
                                                getStatusOverBudgetId();
                                            for (ListStatusOverBudgetId o :
                                                 ovget) {
                                                statusOver = o.getStatusOver();
                                                budId = o.getBudgetId();
                                                for (Row BudOver :
                                                     budgetOversub.getAllRowsInRange()) {
                                                    String CustIdOver = "";
                                                    CustIdOver =
                                                            BudOver.getAttribute("BudgetCustId").toString();
                                                    if (cusId.equalsIgnoreCase(BudgetCustId) &&
                                                        budgetAsTodateRemaining.compareTo(BigDecimal.ZERO) <
                                                        0 ||
                                                        totalPercen > 100) {
                                                        ar.setBudgetCustId(BudgetCustId);
                                                        ar.setBudgetAsTodateRemaining(budgetAsTodateRemainingInfo.toString());
                                                        ar.setBudgetComb(budgetComb);
                                                        listBudgetRemainingValidasi.add(ar);
                                                    } else {
                                                        BigDecimal amountOverSubs =
                                                            BigDecimal.ZERO;
                                                        amountOverSubs =
                                                                new BigDecimal(BudOver.getAttribute("OverBudgetAmt") ==
                                                                               null ?
                                                                               "0" :
                                                                               BudOver.getAttribute("OverBudgetAmt").toString());
                                                        DCIteratorBinding dciterCustClear =
                                                            ADFUtils.findIterator("ProdBudgetByCustView1Iterator");
                                                        dciterCustClear.executeQuery();
                                                        if (dciterCustClear.getEstimatedRowCount() >
                                                            0) {
                                                            for (Row r :
                                                                 dciterCustClear.getAllRowsInRange()) {
                                                                String BudgetById =
                                                                    r.getAttribute("BudgetById").toString();
                                                                if (budId.equalsIgnoreCase(BudgetById) &&
                                                                    statusOver.equalsIgnoreCase("Y")) {
                                                                    r.setAttribute("OverBudgetAmt",
                                                                                   amountOverSubs);
                                                                }
                                                            }
                                                            dciterCustClear.getDataControl().commitTransaction();
                                                        }
                                                        if (cusId.equalsIgnoreCase(BudgetCustId)) {
                                                            if (!cusId.equalsIgnoreCase(CustIdOver) &&
                                                                statusOver.equalsIgnoreCase("Y")) {
                                                                BigDecimal overValueNew =
                                                                    overBudgetAmountCur.add(amountOverSubs);
                                                                try {
                                                                    PreparedStatement updateTtfNumSeq =
                                                                        conn.prepareStatement("UPDATE BUDGET_CUSTOMER BudgetCustomer SET BUDGETCUSTOMER.OVER_BUDGET=? where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID = ?");
                                                                    updateTtfNumSeq.setBigDecimal(1,
                                                                                                  overValueNew);
                                                                    updateTtfNumSeq.setString(2,
                                                                                              cusId);
                                                                    updateTtfNumSeq.executeUpdate();
                                                                    updateTtfNumSeq.close();
                                                                } catch (SQLException sqle) {
                                                                    System.out.println("------------------------------------------------");
                                                                    System.out.println("ERROR: Cannot run update query");
                                                                    System.out.println("STACK: " +
                                                                                       sqle.toString().trim());
                                                                    System.out.println("------------------------------------------------");
                                                                }
                                                            }
                                                            try {
                                                                PreparedStatement updateTtfNumSeq =
                                                                    conn.prepareStatement("UPDATE BUDGET_CUSTOMER BudgetCustomer SET BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED =? where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID = ?");
                                                                updateTtfNumSeq.setBigDecimal(1,
                                                                                              value);
                                                                updateTtfNumSeq.setString(2,
                                                                                          cusId);
                                                                updateTtfNumSeq.executeUpdate();
                                                                updateTtfNumSeq.close();
                                                            } catch (SQLException sqle) {
                                                                System.out.println("------------------------------------------------");
                                                                System.out.println("ERROR: Cannot run update query");
                                                                System.out.println("STACK: " +
                                                                                   sqle.toString().trim());
                                                                System.out.println("------------------------------------------------");
                                                            }
                                                        } else if (CustIdOver.equalsIgnoreCase(BudgetCustId)) {
                                                            if (!cusId.equalsIgnoreCase(CustIdOver) &&
                                                                statusOver.equalsIgnoreCase("Y")) {
                                                                BigDecimal overValue =
                                                                    overBudgetAmountCur.subtract(amountOverSubs);
                                                                try {
                                                                    PreparedStatement updateTtfNumSeq =
                                                                        conn.prepareStatement("UPDATE BUDGET_CUSTOMER BudgetCustomer SET BUDGETCUSTOMER.OVER_BUDGET=? where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID = ?");
                                                                    updateTtfNumSeq.setBigDecimal(1,
                                                                                                  overValue);
                                                                    updateTtfNumSeq.setString(2,
                                                                                              CustIdOver);
                                                                    updateTtfNumSeq.executeUpdate();
                                                                    updateTtfNumSeq.close();
                                                                } catch (SQLException sqle) {
                                                                    System.out.println("------------------------------------------------");
                                                                    System.out.println("ERROR: Cannot run update query");
                                                                    System.out.println("STACK: " +
                                                                                       sqle.toString().trim());
                                                                    System.out.println("------------------------------------------------");
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                        } else {
                                            BigDecimal valPersen =
                                                new BigDecimal(totalPercen).setScale(2,
                                                                                     RoundingMode.HALF_UP);
                                            if (cusId.equalsIgnoreCase(BudgetCustId) &&
                                                budgetAsTodateRemaining.compareTo(BigDecimal.ZERO) <
                                                0 ||
                                                valPersen.compareTo(new BigDecimal("100.00")) >
                                                0) {
                                                ar.setBudgetCustId(BudgetCustId);
                                                ar.setBudgetAsTodateRemaining(budgetAsTodateRemainingInfo.toString());
                                                ar.setBudgetComb(budgetComb);
                                                listBudgetRemainingValidasi.add(ar);
                                            } else {
                                                if (cusId.equalsIgnoreCase(BudgetCustId)) {
                                                    try {
                                                        PreparedStatement updateTtfNumSeq =
                                                            conn.prepareStatement("UPDATE BUDGET_CUSTOMER BudgetCustomer SET BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED =? where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID = ?");
                                                        updateTtfNumSeq.setBigDecimal(1,
                                                                                      value);
                                                        updateTtfNumSeq.setString(2,
                                                                                  cusId);
                                                        updateTtfNumSeq.executeUpdate();
                                                        updateTtfNumSeq.close();
                                                    } catch (SQLException sqle) {
                                                        System.out.println("------------------------------------------------");
                                                        System.out.println("ERROR: Cannot run update query");
                                                        System.out.println("STACK: " +
                                                                           sqle.toString().trim());
                                                        System.out.println("------------------------------------------------");
                                                    }
                                                }
                                            }
                                        }
                                        //end loop
                                    }
                                    List<ListBudgetRemainingValidasi> ab =
                                        getListBudgetRemainingValidasi();
                                    if (ab.size() == 0) {
                                        conn.commit();
                                    }
                                    statement.close();

                                }
                                conn.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            
                            List<ListBudgetRemainingValidasi> ab =
                                getListBudgetRemainingValidasi();
                            BigDecimal custPersen =
                                new BigDecimal(totalPercen).setScale(2,
                                                                     RoundingMode.HALF_UP);
                            if (custPersen.compareTo(new BigDecimal("100.00")) >
                                0) {
                                showPopup("Persentase budget tidak boleh melebihi 100%.",
                                          potmessage);
                            } else if (ab.size() > 0) {
                                StringBuilder availBudgetMsg =
                                    new StringBuilder();
                                for (ListBudgetRemainingValidasi b : ab) {
                                    double budAsRem =
                                        (new BigDecimal(b.getBudgetAsTodateRemaining())).doubleValue();
                                    String budAsRemFmt =
                                        numFmt.format(budAsRem);
                                    availBudgetMsg.append(b.getBudgetComb() +
                                                          " [Available Budget: " +
                                                          budAsRemFmt +
                                                          "] <nr>");
                                }
                                availBudgetMsg.append("<nr>Amount yang dimasukan melebihi nilai dari \"Budget As To Date Remaining\"\"");
                                showPopup((availBudgetMsg.length() > 0 ?
                                           availBudgetMsg.substring(0,
                                                                    availBudgetMsg.length() -
                                                                    1) : ""),
                                          potmessage);

                                DCIteratorBinding dciterCustClear =
                                    ADFUtils.findIterator("ProdBudgetByCustView1Iterator");
                                dciterCustClear.executeQuery();
                                if (dciterCustClear.getEstimatedRowCount() >
                                    0) {
                                    for (Row r :
                                         dciterCustClear.getAllRowsInRange()) {
                                        String produkId =
                                            r.getAttribute("PromoProdukId").toString();
                                        String CheckRowStatus =
                                            r.getAttribute("CheckRowStatus").toString();
                                        if (produkId.equalsIgnoreCase(promoProdukId) &&
                                            CheckRowStatus.equalsIgnoreCase("0")) {
                                            r.remove();
                                        }
                                    }
                                }
                            } else {
                                OperationBinding operationBinding =
                                    bindings.getOperationBinding("Commit");
                                operationBinding.execute();

                                OperationBinding executeBudgetPost =
                                    bindings.getOperationBinding("ExecuteBudgetPost");
                                executeBudgetPost.execute();

                                OperationBinding executeBudgetCust =
                                    bindings.getOperationBinding("ExecuteBudgetCust");
                                executeBudgetCust.execute();

                                OperationBinding executeListConf =
                                    bindings.getOperationBinding("ExecuteProposalConf");
                                executeListConf.execute();

                                dciterPropConfirm.setCurrentRowWithKey(parentKey.toStringFormat(true));
                                dciterPromoProduk.setCurrentRowWithKey(promoProdKey.toStringFormat(true));

                                AdfFacesContext.getCurrentInstance().addPartialTarget(tblBudgetPosting);
                                AdfFacesContext.getCurrentInstance().addPartialTarget(tblBudgetCustomer);
                                AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropConfirmation);
                                AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
                                popupDetailProd.hide();
                            }
                        }

                        // ==================== untuk promo barang onInvoice ==================== //

                    } else if (DiscountType.equalsIgnoreCase(discTypePromoBarang) &&
                               mekPenagihan1.equalsIgnoreCase(onInvoice)) {
                        List<String> list = new ArrayList<String>();
                        List<String> count = new ArrayList<String>();
                        for (Row budgetCusr : dciterCust.getAllRowsInRange()) {
                            String StatusOver =
                                budgetCusr.getAttribute("FlagStatusOver") ==
                                null ? "" :
                                budgetCusr.getAttribute("FlagStatusOver").toString();
                            String BudgetById =
                                budgetCusr.getAttribute("BudgetById") == null ?
                                "" :
                                budgetCusr.getAttribute("BudgetById").toString();

                            if (StatusOver.equalsIgnoreCase("Y")) {
                                list.add(StatusOver);
                            }
                            count.add(BudgetById);
                        }
                        if (list.size() == 0) {
                            showPopup("Pilih \"Over Budget\" disalah satu budget.",
                                      potmessage);
                        } else {
                            RowSetIterator BudMainCust =
                                dciterCust.getRowSetIterator();
                            HashMap<String, BigDecimal> mapOfSum =
                                new HashMap<String, BigDecimal>();
                            BigDecimal budgetAsToDateUsed = BigDecimal.ZERO;
                            StatusOverBudgetId =
                                    new ArrayList<ListStatusOverBudgetId>();
                            for (Row budgetCusr :
                                 dciterCust.getAllRowsInRange()) {
                                String CheckRowStatus =
                                    budgetCusr.getAttribute("CheckRowStatus").toString();
                                String StatusOver =
                                    budgetCusr.getAttribute("FlagStatusOver") ==
                                    null ? "" :
                                    budgetCusr.getAttribute("FlagStatusOver").toString();
                                String BudgetById =
                                    budgetCusr.getAttribute("BudgetById") ==
                                    null ? "" :
                                    budgetCusr.getAttribute("BudgetById").toString();
                                totalPercen +=
                                        Float.valueOf(budgetCusr.getAttribute("Percentage").toString());

                                if (CheckRowStatus.equalsIgnoreCase("0")) {
                                    ListStatusOverBudgetId ov =
                                        new ListStatusOverBudgetId();
                                    ov.setBudgetId(BudgetById);
                                    ov.setStatusOver(StatusOver);
                                    String custId =
                                        budgetCusr.getAttribute("BudgetCustId").toString() +
                                        ";" +
                                        budgetCusr.getAttribute("KombinasiBudget").toString();

                                    if (mapOfSum.containsKey(custId)) {
                                        BigDecimal total =
                                            new BigDecimal(budgetCusr.getAttribute("Amount").toString());
                                        budgetAsToDateUsed =
                                                budgetAsToDateUsed.add(total);
                                    } else {
                                        BigDecimal total =
                                            new BigDecimal(budgetCusr.getAttribute("Amount").toString());
                                        budgetAsToDateUsed = total;
                                    }
                                    mapOfSum.put(custId, budgetAsToDateUsed);
                                    StatusOverBudgetId.add(ov);
                                }
                            }
                            BudMainCust.closeRowSetIterator();
                            //                        System.out.println("mapOfSum = "+mapOfSum);
                            listBudgetRemainingValidasi =
                                    new ArrayList<ListBudgetRemainingValidasi>();
                            PreparedStatement statement = null;
                            try {
                                Context ctx = new InitialContext();
                                DataSource ds =
                                    (DataSource)ctx.lookup(dsFdiConn);
                                conn = ds.getConnection();
                                conn.setAutoCommit(false);


                                checkOverBudgetClearViewImpl budgetOversub =
                                    confirmationAM.getcheckOverBudgetClearView1();
                                budgetOversub.setNamedWhereClauseParam("confirmNO",
                                                                       confirmNo);
                                budgetOversub.executeQuery();

                                BigDecimal budgetAsTodateCur = BigDecimal.ZERO;
                                BigDecimal budgetAsTodate = BigDecimal.ZERO;
                                BigDecimal overBudgetAmountCur =
                                    BigDecimal.ZERO;
                                BigDecimal budgetAsTodateRemaining =
                                    BigDecimal.ZERO;
                                BigDecimal budgetAsTodateRemainingInfo =
                                    BigDecimal.ZERO;


                                for (Map.Entry<String, BigDecimal> entry :
                                     mapOfSum.entrySet()) {
                                    ListBudgetRemainingValidasi ar =
                                        new ListBudgetRemainingValidasi();
                                    String cusId = "";
                                    String budgetComb = "";
                                    String statusOver = "";
                                    String budId = "";
                                    String key = entry.getKey();
                                    String[] keys = key.split(";");
                                    cusId = keys[0].trim();
                                    budgetComb = keys[1].trim();
                                    statement =
                                            conn.prepareStatement("select BUDGETCUSTOMER.BUDGET_CUSTOMER_ID ,BUDGETCUSTOMER.BUDGET_AS_TO_DATE, BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED,BUDGETCUSTOMER.YEARLY_BUDGET_AMOUNT,BUDGETCUSTOMER.OVER_BUDGET from BUDGET_CUSTOMER BudgetCustomer  where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID=? for update");
                                    statement.setString(1, cusId);
                                    ResultSet rs = statement.executeQuery();

                                    while (rs.next()) {
                                        String BudgetCustId =
                                            rs.getString("BUDGET_CUSTOMER_ID").toString();
                                        budgetAsTodateCur =
                                                new BigDecimal(rs.getString("BUDGET_AS_TO_DATE_USED").toString());
                                        budgetAsTodate =
                                                new BigDecimal(rs.getString("BUDGET_AS_TO_DATE").toString());
                                        overBudgetAmountCur =
                                                new BigDecimal(rs.getString("OVER_BUDGET") ==
                                                               null ? "0" :
                                                               rs.getString("OVER_BUDGET").toString());

                                        BigDecimal value =
                                            entry.getValue().add(budgetAsTodateCur);
                                        budgetAsTodateRemainingInfo =
                                                budgetAsTodate.subtract(budgetAsTodateCur);
                                        budgetAsTodateRemaining =
                                                budgetAsTodate.subtract(value);

                                        if (budgetOversub.getEstimatedRowCount() >
                                            0) {
                                            List<ListStatusOverBudgetId> ovget =
                                                getStatusOverBudgetId();
                                            for (ListStatusOverBudgetId o :
                                                 ovget) {
                                                statusOver = o.getStatusOver();
                                                budId = o.getBudgetId();
                                                for (Row BudOver :
                                                     budgetOversub.getAllRowsInRange()) {
                                                    String CustIdOver = "";
                                                    CustIdOver =
                                                            BudOver.getAttribute("BudgetCustId").toString();
                                                    BigDecimal valPersen =
                                                        new BigDecimal(totalPercen).setScale(2,
                                                                                             RoundingMode.HALF_UP);
                                                    if (cusId.equalsIgnoreCase(BudgetCustId) &&
                                                        budgetAsTodateRemaining.compareTo(BigDecimal.ZERO) <
                                                        0 ||
                                                        valPersen.compareTo(new BigDecimal("100.00")) >
                                                        0) {
                                                        ar.setBudgetCustId(BudgetCustId);
                                                        ar.setBudgetAsTodateRemaining(budgetAsTodateRemainingInfo.toString());
                                                        ar.setBudgetComb(budgetComb);
                                                        listBudgetRemainingValidasi.add(ar);
                                                    } else {
                                                        BigDecimal amountOverSubs =
                                                            BigDecimal.ZERO;
                                                        String prodProId =
                                                            BudOver.getAttribute("PromoProdukIdTo").toString();
                                                        amountOverSubs =
                                                                new BigDecimal(BudOver.getAttribute("OverBudgetAmt") ==
                                                                               null ?
                                                                               "0" :
                                                                               BudOver.getAttribute("OverBudgetAmt").toString());
                                                        DCIteratorBinding dciterCustClear =
                                                            ADFUtils.findIterator("ProdBudgetByCustView1Iterator");
                                                        dciterCustClear.executeQuery();
                                                        if (dciterCustClear.getEstimatedRowCount() >
                                                            0) {
                                                            for (Row r :
                                                                 dciterCustClear.getAllRowsInRange()) {
                                                                String produkId =
                                                                    r.getAttribute("PromoProdukId").toString();
                                                                String BudgetById =
                                                                    r.getAttribute("BudgetById").toString();
                                                                if (budId.equalsIgnoreCase(BudgetById) &&
                                                                    statusOver.equalsIgnoreCase("Y") &&
                                                                    produkId.equalsIgnoreCase(prodProId)) {
                                                                    r.setAttribute("OverBudgetAmt",
                                                                                   amountOverSubs);
                                                                }
                                                            }
                                                            dciterCustClear.getDataControl().commitTransaction();
                                                        }
                                                        if (cusId.equalsIgnoreCase(BudgetCustId)) {
                                                            if (!cusId.equalsIgnoreCase(CustIdOver) &&
                                                                statusOver.equalsIgnoreCase("Y")) {

                                                                BigDecimal amountOverSubsTemp =
                                                                    BigDecimal.ZERO;
                                                                for (Row r :
                                                                     dciterCustClear.getAllRowsInRange()) {
                                                                    String produkId =
                                                                        r.getAttribute("PromoProdukId").toString();
                                                                    if (produkId.equalsIgnoreCase(prodProId)) {
                                                                        amountOverSubsTemp =
                                                                                amountOverSubs;
                                                                    } else {
                                                                        amountOverSubs =
                                                                                BigDecimal.ZERO;
                                                                        ;
                                                                    }
                                                                }

                                                                BigDecimal overValueNew =
                                                                    overBudgetAmountCur.add(amountOverSubsTemp);
                                                                //
                                                                try {
                                                                    PreparedStatement updateTtfNumSeq =
                                                                        conn.prepareStatement("UPDATE BUDGET_CUSTOMER BudgetCustomer SET BUDGETCUSTOMER.OVER_BUDGET=? where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID = ?");
                                                                    //                            updateTtfNumSeq.setInt(1, nextSeq);
                                                                    updateTtfNumSeq.setBigDecimal(1,
                                                                                                  overValueNew);
                                                                    updateTtfNumSeq.setString(2,
                                                                                              cusId);
                                                                    updateTtfNumSeq.executeUpdate();
                                                                    updateTtfNumSeq.close();
                                                                } catch (SQLException sqle) {
                                                                    System.out.println("------------------------------------------------");
                                                                    System.out.println("ERROR: Cannot run update query");
                                                                    System.out.println("STACK: " +
                                                                                       sqle.toString().trim());
                                                                    System.out.println("------------------------------------------------");
                                                                }
                                                            }
                                                            try {
                                                                PreparedStatement updateTtfNumSeq =
                                                                    conn.prepareStatement("UPDATE BUDGET_CUSTOMER BudgetCustomer SET BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED =? where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID = ?");
                                                                //                            updateTtfNumSeq.setInt(1, nextSeq);
                                                                updateTtfNumSeq.setBigDecimal(1,
                                                                                              value);
                                                                updateTtfNumSeq.setString(2,
                                                                                          cusId);
                                                                updateTtfNumSeq.executeUpdate();
                                                                updateTtfNumSeq.close();
                                                            } catch (SQLException sqle) {
                                                                System.out.println("------------------------------------------------");
                                                                System.out.println("ERROR: Cannot run update query");
                                                                System.out.println("STACK: " +
                                                                                   sqle.toString().trim());
                                                                System.out.println("------------------------------------------------");
                                                            }
                                                        } else if (CustIdOver.equalsIgnoreCase(BudgetCustId)) {
                                                            if (!cusId.equalsIgnoreCase(CustIdOver) &&
                                                                statusOver.equalsIgnoreCase("Y")) {
                                                                BigDecimal overValue =
                                                                    overBudgetAmountCur.subtract(amountOverSubs);

                                                                if (overValue.compareTo(BigDecimal.ZERO) <=
                                                                    0) {
                                                                    amountOverSubs =
                                                                            BigDecimal.ZERO;
                                                                    overValue =
                                                                            BigDecimal.ZERO;
                                                                }
                                                                try {
                                                                    PreparedStatement updateTtfNumSeq =
                                                                        conn.prepareStatement("UPDATE BUDGET_CUSTOMER BudgetCustomer SET BUDGETCUSTOMER.OVER_BUDGET=? where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID = ?");
                                                                    //                            updateTtfNumSeq.setInt(1, nextSeq);
                                                                    updateTtfNumSeq.setBigDecimal(1,
                                                                                                  overValue);
                                                                    updateTtfNumSeq.setString(2,
                                                                                              CustIdOver);
                                                                    updateTtfNumSeq.executeUpdate();
                                                                    updateTtfNumSeq.close();
                                                                } catch (SQLException sqle) {
                                                                    System.out.println("------------------------------------------------");
                                                                    System.out.println("ERROR: Cannot run update query");
                                                                    System.out.println("STACK: " +
                                                                                       sqle.toString().trim());
                                                                    System.out.println("------------------------------------------------");
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                        } else {
                                            BigDecimal valPersen =
                                                new BigDecimal(totalPercen).setScale(2,
                                                                                     RoundingMode.HALF_UP);
                                            if (BudgetCustId.equalsIgnoreCase(cusId) &&
                                                budgetAsTodateRemaining.compareTo(BigDecimal.ZERO) <
                                                0 ||
                                                valPersen.compareTo(new BigDecimal("100.00")) >
                                                0) {
                                                ar.setBudgetCustId(BudgetCustId);
                                                ar.setBudgetAsTodateRemaining(budgetAsTodateRemainingInfo.toString());
                                                ar.setBudgetComb(budgetComb);
                                                listBudgetRemainingValidasi.add(ar);
                                            } else {
                                                if (cusId.equalsIgnoreCase(BudgetCustId)) {
                                                    try {
                                                        PreparedStatement updateTtfNumSeq =
                                                            conn.prepareStatement("UPDATE BUDGET_CUSTOMER BudgetCustomer SET BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED =? where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID = ?");
                                                        //                            updateTtfNumSeq.setInt(1, nextSeq);
                                                        updateTtfNumSeq.setBigDecimal(1,
                                                                                      value);
                                                        updateTtfNumSeq.setString(2,
                                                                                  cusId);
                                                        updateTtfNumSeq.executeUpdate();
                                                        updateTtfNumSeq.close();
                                                    } catch (SQLException sqle) {
                                                        System.out.println("------------------------------------------------");
                                                        System.out.println("ERROR: Cannot run update query");
                                                        System.out.println("STACK: " +
                                                                           sqle.toString().trim());
                                                        System.out.println("------------------------------------------------");
                                                    }
                                                }
                                            }
                                        }
                                        //end loop
                                    }
                                    List<ListBudgetRemainingValidasi> ab =
                                        getListBudgetRemainingValidasi();
                                    if (ab.size() == 0) {
                                        conn.commit();
                                    }
                                    statement.close();

                                }
                                conn.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            /*        finally {
                                List<ListBudgetRemainingValidasi> ab =
                                    getListBudgetRemainingValidasi();
                                if(ab.size() == 0){
                                conn.commit();
                                }
                                conn.close();
                                //                                if (statement != null) {
                                statement.close();
                                //                                }
                            } */
                            List<ListBudgetRemainingValidasi> ab =
                                getListBudgetRemainingValidasi();
                            BigDecimal custPersen =
                                new BigDecimal(totalPercen).setScale(2,
                                                                     RoundingMode.HALF_UP);
                            if (custPersen.compareTo(new BigDecimal("100.00")) >
                                0) {
                                //                        if (totalPercenPost > 100) {
                                showPopup("Persentase budget tidak boleh melebihi 100%.",
                                          potmessage);
                            } else if (ab.size() > 0) {
                                StringBuilder availBudgetMsg =
                                    new StringBuilder();
                                for (ListBudgetRemainingValidasi b : ab) {
                                    double budAsRem =
                                        (new BigDecimal(b.getBudgetAsTodateRemaining())).doubleValue();
                                    String budAsRemFmt =
                                        numFmt.format(budAsRem);
                                    availBudgetMsg.append(b.getBudgetComb() +
                                                          " [Available Budget: " +
                                                          budAsRemFmt +
                                                          "] <nr>");
                                }
                                availBudgetMsg.append("<nr>Amount yang dimasukan melebihi nilai dari \"Budget As To Date Remaining\"\"");
                                showPopup((availBudgetMsg.length() > 0 ?
                                           availBudgetMsg.substring(0,
                                                                    availBudgetMsg.length() -
                                                                    1) : ""),
                                          potmessage);
                                DCIteratorBinding dciterCustClear =
                                    ADFUtils.findIterator("ProdBudgetByPostView1Iterator");
                                dciterCustClear.executeQuery();
                                if (dciterCustClear.getEstimatedRowCount() >
                                    0) {
                                    //                                        for(int i = 0 ; i < dciterCustClear.getEstimatedRowCount(); i++){
                                    for (Row r :
                                         dciterCustClear.getAllRowsInRange()) {
                                        //                                            Row r = dciterCustClear.getRowAtRangeIndex(i);
                                        String produkId =
                                            r.getAttribute("PromoProdukId").toString();
                                        String CheckRowStatus =
                                            r.getAttribute("CheckRowStatus").toString();
                                        if (produkId.equalsIgnoreCase(promoProdukId) &&
                                            CheckRowStatus.equalsIgnoreCase("0")) {
                                            r.remove();
                                        }
                                    }
                                }
                            } else {
                                OperationBinding operationBinding =
                                    bindings.getOperationBinding("Commit");
                                operationBinding.execute();

                                OperationBinding executeBudgetPost =
                                    bindings.getOperationBinding("ExecuteBudgetPost");
                                executeBudgetPost.execute();

                                OperationBinding executeBudgetCust =
                                    bindings.getOperationBinding("ExecuteBudgetCust");
                                executeBudgetCust.execute();

                                OperationBinding executeListConf =
                                    bindings.getOperationBinding("ExecuteProposalConf");
                                executeListConf.execute();

                                dciterPropConfirm.setCurrentRowWithKey(parentKey.toStringFormat(true));
                                dciterPromoProduk.setCurrentRowWithKey(promoProdKey.toStringFormat(true));

                                AdfFacesContext.getCurrentInstance().addPartialTarget(tblBudgetPosting);
                                AdfFacesContext.getCurrentInstance().addPartialTarget(tblBudgetCustomer);
                                AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropConfirmation);
                                AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
                                popupDetailProd.hide();
                            }
                        }
                        // ====================   selain on invoice ==================== ////
                    } else {
                        RowSetIterator BudMainCust =
                            dciterCust.getRowSetIterator();
                        HashMap<String, BigDecimal> mapOfSum =
                            new HashMap<String, BigDecimal>();
                        BigDecimal budgetAsToDateUsed = BigDecimal.ZERO;
                        for (Row budgetCusr : dciterCust.getAllRowsInRange()) {
                            String CheckRowStatus =
                                budgetCusr.getAttribute("CheckRowStatus").toString();
                            totalPercen +=
                                    Float.valueOf(budgetCusr.getAttribute("Percentage").toString());
                            if (CheckRowStatus.equalsIgnoreCase("0")) {
                                String custId =
                                    budgetCusr.getAttribute("BudgetCustId").toString() +
                                    ";" +
                                    budgetCusr.getAttribute("KombinasiBudget").toString();
                                if (mapOfSum.containsKey(custId)) {
                                    BigDecimal total =
                                        new BigDecimal(budgetCusr.getAttribute("Amount").toString());
                                    budgetAsToDateUsed =
                                            budgetAsToDateUsed.add(total);
                                } else {
                                    BigDecimal total =
                                        new BigDecimal(budgetCusr.getAttribute("Amount").toString());
                                    budgetAsToDateUsed = total;
                                }
                                mapOfSum.put(custId, budgetAsToDateUsed);
                            }
                        }

                        BudMainCust.closeRowSetIterator();
                        //                System.out.println("mapOfSum = "+mapOfSum);
                        listBudgetRemainingValidasi =
                                new ArrayList<ListBudgetRemainingValidasi>();
                        PreparedStatement statement = null;
                        try {
                            Context ctx = new InitialContext();
                            DataSource ds = (DataSource)ctx.lookup(dsFdiConn);
                            conn = ds.getConnection();
                            conn.setAutoCommit(false);

                            BigDecimal budgetAsTodateCur = BigDecimal.ZERO;
                            BigDecimal budgetAsTodate = BigDecimal.ZERO;
                            BigDecimal budgetAsTodateRemaining =
                                BigDecimal.ZERO;
                            BigDecimal budgetAsTodateRemainingInfo =
                                BigDecimal.ZERO;

                            for (Map.Entry<String, BigDecimal> entry :
                                 mapOfSum.entrySet()) {
                                ListBudgetRemainingValidasi ar =
                                    new ListBudgetRemainingValidasi();
                                String cusId = "";
                                String budgetComb = "";
                                String key = entry.getKey();
                                String[] keys = key.split(";");
                                cusId = keys[0].trim();
                                budgetComb = keys[1].trim();
                                statement =
                                        conn.prepareStatement("select BUDGETCUSTOMER.BUDGET_CUSTOMER_ID ,BUDGETCUSTOMER.BUDGET_AS_TO_DATE, BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED,BUDGETCUSTOMER.YEARLY_BUDGET_AMOUNT from BUDGET_CUSTOMER BudgetCustomer where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID=? for update");
                                statement.setString(1, cusId);
                                ResultSet rs = statement.executeQuery();
                                while (rs.next()) {
                                    String BudgetCustId =
                                        rs.getString("BUDGET_CUSTOMER_ID").toString();
                                    budgetAsTodateCur =
                                            new BigDecimal(rs.getString("BUDGET_AS_TO_DATE_USED").toString());
                                    budgetAsTodate =
                                            new BigDecimal(rs.getString("BUDGET_AS_TO_DATE").toString());
                                    //                            budgetAsTodateRemaining = budgetAsTodate.subtract(budgetAsTodateCur);

                                    BigDecimal value =
                                        entry.getValue().add(budgetAsTodateCur);
                                    budgetAsTodateRemaining =
                                            budgetAsTodate.subtract(value);
                                    budgetAsTodateRemainingInfo =
                                            budgetAsTodate.subtract(budgetAsTodateCur);
                                    if (cusId.equalsIgnoreCase(BudgetCustId) &&
                                        budgetAsTodateRemaining.compareTo(BigDecimal.ZERO) <
                                        0 || totalPercen > 100) {
                                        ar.setBudgetCustId(BudgetCustId);
                                        ar.setBudgetAsTodateRemaining(budgetAsTodateRemainingInfo.toString());
                                        ar.setBudgetComb(budgetComb);
                                        listBudgetRemainingValidasi.add(ar);
                                    } else {
                                        if (cusId.equalsIgnoreCase(BudgetCustId)) {
                                            //                                                budCustRow.setAttribute("BudgetAsToDateUsed",
                                            //                                                                        value);
                                            try {
                                                PreparedStatement updateTtfNumSeq =
                                                    conn.prepareStatement("UPDATE BUDGET_CUSTOMER BudgetCustomer SET BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED =? where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID = ?");
                                                //                            updateTtfNumSeq.setInt(1, nextSeq);
                                                updateTtfNumSeq.setBigDecimal(1,
                                                                              value);
                                                updateTtfNumSeq.setString(2,
                                                                          cusId);
                                                updateTtfNumSeq.executeUpdate();
                                                updateTtfNumSeq.close();
                                            } catch (SQLException sqle) {
                                                System.out.println("------------------------------------------------");
                                                System.out.println("ERROR: Cannot run update query");
                                                System.out.println("STACK: " +
                                                                   sqle.toString().trim());
                                                System.out.println("------------------------------------------------");
                                            }
                                        }
                                    }
                                }
                                List<ListBudgetRemainingValidasi> ab =
                                    getListBudgetRemainingValidasi();
                                if (ab.size() == 0) {
                                    conn.commit();
                                }
                                statement.close();
                                //                                conn.close();
                            }
                            //                        conn.commit();
                            conn.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        /*    finally {
                            List<ListBudgetRemainingValidasi> ab =
                                getListBudgetRemainingValidasi();
                            if(ab.size() == 0){
                            conn.commit();
                            }
                            conn.close();
                            //                                if (statement != null) {
                            statement.close();
                            //                                }
                        } */
                        List<ListBudgetRemainingValidasi> ab =
                            getListBudgetRemainingValidasi();

                        if (totalPercen > 100) {
                            showPopup("Persentase budget tidak boleh melebihi 100%.",
                                      potmessage);
                        } else if (ab.size() > 0) {
                            StringBuilder availBudgetMsg = new StringBuilder();
                            for (ListBudgetRemainingValidasi b : ab) {
                                double budAsRem =
                                    (new BigDecimal(b.getBudgetAsTodateRemaining())).doubleValue();
                                String budAsRemFmt = numFmt.format(budAsRem);
                                availBudgetMsg.append(b.getBudgetComb() +
                                                      " [Available Budget: " +
                                                      budAsRemFmt + "] <nr>");
                            }
                            availBudgetMsg.append("<nr>Amount yang dimasukan melebihi nilai dari \"Budget As To Date Remaining\"\"");
                            showPopup((availBudgetMsg.length() > 0 ?
                                       availBudgetMsg.substring(0,
                                                                availBudgetMsg.length() -
                                                                1) : ""),
                                      potmessage);

                            DCIteratorBinding dciterCustClear =
                                ADFUtils.findIterator("ProdBudgetByCustView1Iterator");
                            dciterCustClear.executeQuery();
                            if (dciterCustClear.getEstimatedRowCount() > 0) {
                                for (Row r :
                                     dciterCustClear.getAllRowsInRange()) {
                                    String produkId =
                                        r.getAttribute("PromoProdukId").toString();
                                    String CheckRowStatus =
                                        r.getAttribute("CheckRowStatus").toString();
                                    if (produkId.equalsIgnoreCase(promoProdukId) &&
                                        CheckRowStatus.equalsIgnoreCase("0")) {
                                        r.remove();
                                    }
                                }
                            }
                        } else {
                            OperationBinding operationBinding =
                                bindings.getOperationBinding("Commit");
                            operationBinding.execute();

                            OperationBinding executeBudgetPost =
                                bindings.getOperationBinding("ExecuteBudgetPost");
                            executeBudgetPost.execute();

                            OperationBinding executeBudgetCust =
                                bindings.getOperationBinding("ExecuteBudgetCust");
                            executeBudgetCust.execute();

                            OperationBinding executeListConf =
                                bindings.getOperationBinding("ExecuteProposalConf");
                            executeListConf.execute();

                            dciterPropConfirm.setCurrentRowWithKey(parentKey.toStringFormat(true));
                            dciterPromoProduk.setCurrentRowWithKey(promoProdKey.toStringFormat(true));

                            AdfFacesContext.getCurrentInstance().addPartialTarget(tblBudgetPosting);
                            AdfFacesContext.getCurrentInstance().addPartialTarget(tblBudgetCustomer);
                            AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropConfirmation);
                            AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
                            popupDetailProd.hide();
                        }
                    }
                } else {
                    popupDetailProd.hide();
                }

                // posting

            } else {
                DCIteratorBinding dciterPost =
                    ADFUtils.findIterator("ProdBudgetByPostView1Iterator");
                float totalPercenPost = 0;
                Connection conn = null;

                if (dciterPost.getEstimatedRowCount() > 0) {
                    if (DiscountType.equalsIgnoreCase(discTypePotongan) &&
                        mekPenagihan1.equalsIgnoreCase(onInvoice)) {
                        List<String> list = new ArrayList<String>();
                        List<String> count = new ArrayList<String>();
                        for (Row budgetCusr : dciterPost.getAllRowsInRange()) {
                            String StatusOver =
                                budgetCusr.getAttribute("FlagStatusOver") ==
                                null ? "" :
                                budgetCusr.getAttribute("FlagStatusOver").toString();
                            String BudgetById =
                                budgetCusr.getAttribute("BudgetById") == null ?
                                "" :
                                budgetCusr.getAttribute("BudgetById").toString();

                            if (StatusOver.equalsIgnoreCase("Y")) {
                                list.add(StatusOver);
                            }
                            count.add(BudgetById);
                        }
                        if (list.size() == 0) {
                            showPopup("Pilih \"Over Budget\" disalah satu budget.",
                                      potmessage);
                        } else {
                            RowSetIterator BudMainCust =
                                dciterPost.getRowSetIterator();
                            HashMap<String, BigDecimal> mapOfSum =
                                new HashMap<String, BigDecimal>();
                            BigDecimal budgetAsToDateUsed = BigDecimal.ZERO;
                            StatusOverBudgetId =
                                    new ArrayList<ListStatusOverBudgetId>();
                            for (Row budgetCusr :
                                 dciterPost.getAllRowsInRange()) {
                                String CheckRowStatus =
                                    budgetCusr.getAttribute("CheckRowStatus").toString();
                                String StatusOver =
                                    budgetCusr.getAttribute("FlagStatusOver") ==
                                    null ? "" :
                                    budgetCusr.getAttribute("FlagStatusOver").toString();
                                String BudgetById =
                                    budgetCusr.getAttribute("BudgetById") ==
                                    null ? "" :
                                    budgetCusr.getAttribute("BudgetById").toString();
                                totalPercenPost +=
                                        Float.valueOf(budgetCusr.getAttribute("Percentage").toString());
                                if (CheckRowStatus.equalsIgnoreCase("0")) {
                                    ListStatusOverBudgetId ov =
                                        new ListStatusOverBudgetId();
                                    ov.setBudgetId(BudgetById);
                                    ov.setStatusOver(StatusOver);
                                    String custId =
                                        budgetCusr.getAttribute("BudgetCustId").toString() +
                                        ";" +
                                        budgetCusr.getAttribute("KombinasiBudget").toString();

                                    if (mapOfSum.containsKey(custId)) {
                                        BigDecimal total =
                                            new BigDecimal(budgetCusr.getAttribute("Amount").toString());
                                        budgetAsToDateUsed =
                                                budgetAsToDateUsed.add(total);
                                    } else {
                                        BigDecimal total =
                                            new BigDecimal(budgetCusr.getAttribute("Amount").toString());
                                        budgetAsToDateUsed = total;
                                    }
                                    mapOfSum.put(custId, budgetAsToDateUsed);
                                    StatusOverBudgetId.add(ov);
                                }
                            }
                            BudMainCust.closeRowSetIterator();
                            listBudgetRemainingValidasi =
                                    new ArrayList<ListBudgetRemainingValidasi>();
                            PreparedStatement statement = null;
                            try {
                                Context ctx = new InitialContext();
                                DataSource ds =
                                    (DataSource)ctx.lookup(dsFdiConn);
                                conn = ds.getConnection();
                                conn.setAutoCommit(false);

                                checkOverBudgetClearViewImpl budgetOversub =
                                    confirmationAM.getcheckOverBudgetClearView1();
                                budgetOversub.setNamedWhereClauseParam("confirmNO",
                                                                       confirmNo);
                                budgetOversub.executeQuery();

                                BigDecimal budgetAsTodateCur = BigDecimal.ZERO;
                                BigDecimal budgetAsTodate = BigDecimal.ZERO;
                                BigDecimal overBudgetAmountCur =
                                    BigDecimal.ZERO;
                                BigDecimal budgetAsTodateRemaining =
                                    BigDecimal.ZERO;
                                BigDecimal budgetAsTodateRemainingInfo =
                                    BigDecimal.ZERO;

                                for (Map.Entry<String, BigDecimal> entry :
                                     mapOfSum.entrySet()) {
                                    ListBudgetRemainingValidasi ar =
                                        new ListBudgetRemainingValidasi();
                                    String cusId = "";
                                    String budgetComb = "";
                                    String statusOver = "";
                                    String budId = "";
                                    String key = entry.getKey();
                                    String[] keys = key.split(";");
                                    cusId = keys[0].trim();
                                    budgetComb = keys[1].trim();
                                    statement =
                                            conn.prepareStatement("select BUDGETCUSTOMER.BUDGET_CUSTOMER_ID ,BUDGETCUSTOMER.BUDGET_AS_TO_DATE, BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED,BUDGETCUSTOMER.YEARLY_BUDGET_AMOUNT,BUDGETCUSTOMER.OVER_BUDGET from BUDGET_CUSTOMER BudgetCustomer where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID=? for update");
                                    statement.setString(1, cusId);
                                    ResultSet rs = statement.executeQuery();

                                    while (rs.next()) {
                                        //begin loop
                                        String BudgetCustId =
                                            rs.getString("BUDGET_CUSTOMER_ID").toString();
                                        budgetAsTodateCur =
                                                new BigDecimal(rs.getString("BUDGET_AS_TO_DATE_USED").toString());
                                        budgetAsTodate =
                                                new BigDecimal(rs.getString("BUDGET_AS_TO_DATE").toString());
                                        overBudgetAmountCur =
                                                new BigDecimal(rs.getString("OVER_BUDGET") ==
                                                               null ? "0" :
                                                               rs.getString("OVER_BUDGET").toString());

                                        BigDecimal value =
                                            entry.getValue().add(budgetAsTodateCur);
                                        budgetAsTodateRemainingInfo =
                                                budgetAsTodate.subtract(budgetAsTodateCur);
                                        budgetAsTodateRemaining =
                                                budgetAsTodate.subtract(value);
                                        if (budgetOversub.getEstimatedRowCount() >
                                            0) {
                                            List<ListStatusOverBudgetId> ovget =
                                                getStatusOverBudgetId();
                                            for (ListStatusOverBudgetId o :
                                                 ovget) {
                                                statusOver = o.getStatusOver();
                                                budId = o.getBudgetId();
                                                for (Row BudOver :
                                                     budgetOversub.getAllRowsInRange()) {
                                                    String CustIdOver = "";
                                                    CustIdOver =
                                                            BudOver.getAttribute("BudgetCustId").toString();
                                                    if (cusId.equalsIgnoreCase(BudgetCustId) &&
                                                        budgetAsTodateRemaining.compareTo(BigDecimal.ZERO) <
                                                        0 ||
                                                        totalPercenPost > 100) {
                                                        ar.setBudgetCustId(BudgetCustId);
                                                        ar.setBudgetAsTodateRemaining(budgetAsTodateRemainingInfo.toString());
                                                        ar.setBudgetComb(budgetComb);
                                                        listBudgetRemainingValidasi.add(ar);
                                                    } else {
                                                        BigDecimal amountOverSubs =
                                                            BigDecimal.ZERO;
                                                        amountOverSubs =
                                                                new BigDecimal(BudOver.getAttribute("OverBudgetAmt") ==
                                                                               null ?
                                                                               "0" :
                                                                               BudOver.getAttribute("OverBudgetAmt").toString());
                                                        DCIteratorBinding dciterCustClear =
                                                            ADFUtils.findIterator("ProdBudgetByCustView1Iterator");
                                                        dciterCustClear.executeQuery();
                                                        if (dciterCustClear.getEstimatedRowCount() >
                                                            0) {
                                                            for (Row r :
                                                                 dciterCustClear.getAllRowsInRange()) {
                                                                String BudgetById =
                                                                    r.getAttribute("BudgetById").toString();
                                                                if (budId.equalsIgnoreCase(BudgetById) &&
                                                                    statusOver.equalsIgnoreCase("Y")) {
                                                                    r.setAttribute("OverBudgetAmt",
                                                                                   amountOverSubs);
                                                                }

                                                            }
                                                            dciterCustClear.getDataControl().commitTransaction();
                                                        }
                                                        if (cusId.equalsIgnoreCase(BudgetCustId)) {
                                                            if (!cusId.equalsIgnoreCase(CustIdOver) &&
                                                                statusOver.equalsIgnoreCase("Y")) {

                                                                BigDecimal overValueNew =
                                                                    overBudgetAmountCur.add(amountOverSubs);
                                                                try {
                                                                    PreparedStatement updateTtfNumSeq =
                                                                        conn.prepareStatement("UPDATE BUDGET_CUSTOMER BudgetCustomer SET BUDGETCUSTOMER.OVER_BUDGET=? where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID = ?");
                                                                    updateTtfNumSeq.setBigDecimal(1,
                                                                                                  overValueNew);
                                                                    updateTtfNumSeq.setString(2,
                                                                                              cusId);
                                                                    updateTtfNumSeq.executeUpdate();
                                                                    updateTtfNumSeq.close();
                                                                } catch (SQLException sqle) {
                                                                    System.out.println("------------------------------------------------");
                                                                    System.out.println("ERROR: Cannot run update query");
                                                                    System.out.println("STACK: " +
                                                                                       sqle.toString().trim());
                                                                    System.out.println("------------------------------------------------");
                                                                }
                                                            }
                                                            try {
                                                                PreparedStatement updateTtfNumSeq =
                                                                    conn.prepareStatement("UPDATE BUDGET_CUSTOMER BudgetCustomer SET BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED =? where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID = ?");
                                                                updateTtfNumSeq.setBigDecimal(1,
                                                                                              value);
                                                                updateTtfNumSeq.setString(2,
                                                                                          cusId);
                                                                updateTtfNumSeq.executeUpdate();
                                                                updateTtfNumSeq.close();
                                                            } catch (SQLException sqle) {
                                                                System.out.println("------------------------------------------------");
                                                                System.out.println("ERROR: Cannot run update query");
                                                                System.out.println("STACK: " +
                                                                                   sqle.toString().trim());
                                                                System.out.println("------------------------------------------------");
                                                            }
                                                        } else if (CustIdOver.equalsIgnoreCase(BudgetCustId)) {
                                                            if (!cusId.equalsIgnoreCase(CustIdOver) &&
                                                                statusOver.equalsIgnoreCase("Y")) {
                                                                BigDecimal overValue =
                                                                    overBudgetAmountCur.subtract(amountOverSubs);
                                                                try {
                                                                    PreparedStatement updateTtfNumSeq =
                                                                        conn.prepareStatement("UPDATE BUDGET_CUSTOMER BudgetCustomer SET BUDGETCUSTOMER.OVER_BUDGET=? where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID = ?");
                                                                    updateTtfNumSeq.setBigDecimal(1,
                                                                                                  overValue);
                                                                    updateTtfNumSeq.setString(2,
                                                                                              CustIdOver);
                                                                    updateTtfNumSeq.executeUpdate();
                                                                    updateTtfNumSeq.close();
                                                                } catch (SQLException sqle) {
                                                                    System.out.println("------------------------------------------------");
                                                                    System.out.println("ERROR: Cannot run update query");
                                                                    System.out.println("STACK: " +
                                                                                       sqle.toString().trim());
                                                                    System.out.println("------------------------------------------------");
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                        } else {
                                            BigDecimal postPersenVal =
                                                new BigDecimal(totalPercenPost).setScale(2,
                                                                                         RoundingMode.HALF_UP);
                                            if (cusId.equalsIgnoreCase(BudgetCustId) &&
                                                budgetAsTodateRemaining.compareTo(BigDecimal.ZERO) <
                                                0 ||
                                                postPersenVal.compareTo(new BigDecimal("100.00")) >
                                                0) {
                                                ar.setBudgetCustId(BudgetCustId);
                                                ar.setBudgetAsTodateRemaining(budgetAsTodateRemainingInfo.toString());
                                                ar.setBudgetComb(budgetComb);
                                                listBudgetRemainingValidasi.add(ar);
                                            } else {
                                                if (cusId.equalsIgnoreCase(BudgetCustId)) {
                                                    try {
                                                        PreparedStatement updateTtfNumSeq =
                                                            conn.prepareStatement("UPDATE BUDGET_CUSTOMER BudgetCustomer SET BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED =? where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID = ?");
                                                        updateTtfNumSeq.setBigDecimal(1,
                                                                                      value);
                                                        updateTtfNumSeq.setString(2,
                                                                                  cusId);
                                                        updateTtfNumSeq.executeUpdate();
                                                        updateTtfNumSeq.close();
                                                    } catch (SQLException sqle) {
                                                        System.out.println("------------------------------------------------");
                                                        System.out.println("ERROR: Cannot run update query");
                                                        System.out.println("STACK: " +
                                                                           sqle.toString().trim());
                                                        System.out.println("------------------------------------------------");
                                                    }
                                                }
                                            }
                                        }
                                        //end loop
                                    }
                                    List<ListBudgetRemainingValidasi> ab =
                                        getListBudgetRemainingValidasi();
                                    if (ab.size() == 0) {
                                        conn.commit();
                                    }
                                    statement.close();
                                }
                                conn.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            
                            List<ListBudgetRemainingValidasi> ab =
                                getListBudgetRemainingValidasi();
                            BigDecimal postPersen =
                                new BigDecimal(totalPercenPost).setScale(2,
                                                                         RoundingMode.HALF_UP);
                            if (postPersen.compareTo(new BigDecimal("100.00")) >
                                0) {
                                showPopup("Persentase budget tidak boleh melebihi 100%.",
                                          potmessage);
                            } else if (ab.size() > 0) {
                                StringBuilder availBudgetMsg =
                                    new StringBuilder();
                                for (ListBudgetRemainingValidasi b : ab) {
                                    double budAsRem =
                                        (new BigDecimal(b.getBudgetAsTodateRemaining())).doubleValue();
                                    String budAsRemFmt =
                                        numFmt.format(budAsRem);
                                    availBudgetMsg.append(b.getBudgetComb() +
                                                          " [Available Budget: " +
                                                          budAsRemFmt +
                                                          "] <nr>");
                                }
                                availBudgetMsg.append("<nr>Amount yang dimasukan melebihi nilai dari \"Budget As To Date Remaining\"\"");
                                showPopup((availBudgetMsg.length() > 0 ?
                                           availBudgetMsg.substring(0,
                                                                    availBudgetMsg.length() -
                                                                    1) : ""),
                                          potmessage);
                                DCIteratorBinding dciterCustClear =
                                    ADFUtils.findIterator("ProdBudgetByPostView1Iterator");
                                dciterCustClear.executeQuery();
                                if (dciterCustClear.getEstimatedRowCount() >
                                    0) {
                                    for (Row r :
                                         dciterCustClear.getAllRowsInRange()) {
                                        String produkId =
                                            r.getAttribute("PromoProdukId").toString();
                                        String CheckRowStatus =
                                            r.getAttribute("CheckRowStatus").toString();
                                        if (produkId.equalsIgnoreCase(promoProdukId) &&
                                            CheckRowStatus.equalsIgnoreCase("0")) {
                                            r.remove();
                                        }
                                    }
                                }
                            } else {
                                OperationBinding operationBinding =
                                    bindings.getOperationBinding("Commit");
                                operationBinding.execute();

                                OperationBinding executeBudgetPost =
                                    bindings.getOperationBinding("ExecuteBudgetPost");
                                executeBudgetPost.execute();

                                OperationBinding executeBudgetCust =
                                    bindings.getOperationBinding("ExecuteBudgetCust");
                                executeBudgetCust.execute();

                                OperationBinding executeListConf =
                                    bindings.getOperationBinding("ExecuteProposalConf");
                                executeListConf.execute();

                                dciterPropConfirm.setCurrentRowWithKey(parentKey.toStringFormat(true));
                                dciterPromoProduk.setCurrentRowWithKey(promoProdKey.toStringFormat(true));

                                AdfFacesContext.getCurrentInstance().addPartialTarget(tblBudgetPosting);
                                AdfFacesContext.getCurrentInstance().addPartialTarget(tblBudgetCustomer);
                                AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropConfirmation);
                                AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
                                popupDetailProd.hide();
                            }
                        }
                        // untuk promo barang onInvoice

                    } else if (DiscountType.equalsIgnoreCase(discTypePromoBarang) &&
                               mekPenagihan1.equalsIgnoreCase(onInvoice)) {
                        List<String> list = new ArrayList<String>();
                        List<String> count = new ArrayList<String>();
                        for (Row budgetCusr : dciterPost.getAllRowsInRange()) {
                            String StatusOver =
                                budgetCusr.getAttribute("FlagStatusOver") ==
                                null ? "" :
                                budgetCusr.getAttribute("FlagStatusOver").toString();
                            String BudgetById =
                                budgetCusr.getAttribute("BudgetById") == null ?
                                "" :
                                budgetCusr.getAttribute("BudgetById").toString();

                            if (StatusOver.equalsIgnoreCase("Y")) {
                                list.add(StatusOver);
                            }
                            count.add(BudgetById);
                        }
                        if (list.size() == 0) {
                            showPopup("Pilih \"Over Budget\" disalah satu budget.",
                                      potmessage);
                        } else {
                            RowSetIterator BudMainCust =
                                dciterPost.getRowSetIterator();
                            HashMap<String, BigDecimal> mapOfSum =
                                new HashMap<String, BigDecimal>();
                            BigDecimal budgetAsToDateUsed = BigDecimal.ZERO;
                            StatusOverBudgetId =
                                    new ArrayList<ListStatusOverBudgetId>();
                            for (Row budgetCusr :
                                 dciterPost.getAllRowsInRange()) {
                                String CheckRowStatus =
                                    budgetCusr.getAttribute("CheckRowStatus").toString();
                                String StatusOver =
                                    budgetCusr.getAttribute("FlagStatusOver") ==
                                    null ? "" :
                                    budgetCusr.getAttribute("FlagStatusOver").toString();
                                String BudgetById =
                                    budgetCusr.getAttribute("BudgetById") ==
                                    null ? "" :
                                    budgetCusr.getAttribute("BudgetById").toString();
                                totalPercenPost +=
                                        Float.valueOf(budgetCusr.getAttribute("Percentage").toString());

                                if (CheckRowStatus.equalsIgnoreCase("0")) {
                                    ListStatusOverBudgetId ov =
                                        new ListStatusOverBudgetId();
                                    ov.setBudgetId(BudgetById);
                                    ov.setStatusOver(StatusOver);
                                    String custId =
                                        budgetCusr.getAttribute("BudgetCustId").toString() +
                                        ";" +
                                        budgetCusr.getAttribute("KombinasiBudget").toString();

                                    if (mapOfSum.containsKey(custId)) {
                                        BigDecimal total =
                                            new BigDecimal(budgetCusr.getAttribute("Amount").toString());
                                        budgetAsToDateUsed =
                                                budgetAsToDateUsed.add(total);
                                    } else {
                                        BigDecimal total =
                                            new BigDecimal(budgetCusr.getAttribute("Amount").toString());
                                        budgetAsToDateUsed = total;
                                    }
                                    mapOfSum.put(custId, budgetAsToDateUsed);
                                    StatusOverBudgetId.add(ov);
                                }
                            }
                            BudMainCust.closeRowSetIterator();
                            listBudgetRemainingValidasi =
                                    new ArrayList<ListBudgetRemainingValidasi>();
                            PreparedStatement statement = null;
                            try {
                                Context ctx = new InitialContext();
                                DataSource ds =
                                    (DataSource)ctx.lookup(dsFdiConn);
                                conn = ds.getConnection();
                                conn.setAutoCommit(false);


                                checkOverBudgetClearViewImpl budgetOversub =
                                    confirmationAM.getcheckOverBudgetClearView1();
                                budgetOversub.setNamedWhereClauseParam("confirmNO",
                                                                       confirmNo);
                                budgetOversub.executeQuery();

                                BigDecimal budgetAsTodateCur = BigDecimal.ZERO;
                                BigDecimal budgetAsTodate = BigDecimal.ZERO;
                                BigDecimal overBudgetAmountCur =
                                    BigDecimal.ZERO;
                                BigDecimal budgetAsTodateRemaining =
                                    BigDecimal.ZERO;
                                BigDecimal budgetAsTodateRemainingInfo =
                                    BigDecimal.ZERO;

                                for (Map.Entry<String, BigDecimal> entry :
                                     mapOfSum.entrySet()) {
                                    ListBudgetRemainingValidasi ar =
                                        new ListBudgetRemainingValidasi();
                                    String cusId = "";
                                    String budgetComb = "";
                                    String statusOver = "";
                                    String budId = "";
                                    String key = entry.getKey();
                                    String[] keys = key.split(";");
                                    cusId = keys[0].trim();
                                    budgetComb = keys[1].trim();
                                    statement =
                                            conn.prepareStatement("select BUDGETCUSTOMER.BUDGET_CUSTOMER_ID ,BUDGETCUSTOMER.BUDGET_AS_TO_DATE, BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED" +
                                                                  ",BUDGETCUSTOMER.YEARLY_BUDGET_AMOUNT,BUDGETCUSTOMER.OVER_BUDGET from BUDGET_CUSTOMER BudgetCustomer where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID=? for update");
                                    statement.setString(1, cusId);
                                    ResultSet rs = statement.executeQuery();
                                    while (rs.next()) {
                                        //begin loop
                                        String BudgetCustId =
                                            rs.getString("BUDGET_CUSTOMER_ID").toString();
                                        budgetAsTodateCur =
                                                new BigDecimal(rs.getString("BUDGET_AS_TO_DATE_USED").toString());
                                        budgetAsTodate =
                                                new BigDecimal(rs.getString("BUDGET_AS_TO_DATE").toString());
                                        overBudgetAmountCur =
                                                new BigDecimal(rs.getString("OVER_BUDGET") ==
                                                               null ? "0" :
                                                               rs.getString("OVER_BUDGET").toString());
                                        BigDecimal value =
                                            entry.getValue().add(budgetAsTodateCur);
                                        budgetAsTodateRemainingInfo =
                                                budgetAsTodate.subtract(budgetAsTodateCur);
                                        budgetAsTodateRemaining =
                                                budgetAsTodate.subtract(value);
                                        if (budgetOversub.getEstimatedRowCount() >
                                            0) {
                                            List<ListStatusOverBudgetId> ovget =
                                                getStatusOverBudgetId();
                                            for (ListStatusOverBudgetId o :
                                                 ovget) {
                                                statusOver = o.getStatusOver();
                                                budId = o.getBudgetId();
                                                for (Row BudOver :
                                                     budgetOversub.getAllRowsInRange()) {
                                                    String CustIdOver = "";
                                                    CustIdOver =
                                                            BudOver.getAttribute("BudgetCustId").toString();
                                                    BigDecimal postPersenVal =
                                                        new BigDecimal(totalPercenPost).setScale(2,
                                                                                                 RoundingMode.HALF_UP);
                                                    if (cusId.equalsIgnoreCase(BudgetCustId) &&
                                                        budgetAsTodateRemaining.compareTo(BigDecimal.ZERO) <
                                                        0 ||
                                                        postPersenVal.compareTo(new BigDecimal("100.00")) >
                                                        0) {
                                                        ar.setBudgetCustId(BudgetCustId);
                                                        ar.setBudgetAsTodateRemaining(budgetAsTodateRemainingInfo.toString());
                                                        ar.setBudgetComb(budgetComb);
                                                        listBudgetRemainingValidasi.add(ar);
                                                    } else {
                                                        BigDecimal amountOverSubs =
                                                            BigDecimal.ZERO;
                                                        amountOverSubs =
                                                                new BigDecimal(BudOver.getAttribute("OverBudgetAmt") ==
                                                                               null ?
                                                                               "0" :
                                                                               BudOver.getAttribute("OverBudgetAmt").toString());
                                                        DCIteratorBinding dciterCustClear =
                                                            ADFUtils.findIterator("ProdBudgetByCustView1Iterator");
                                                        dciterCustClear.executeQuery();
                                                        if (dciterCustClear.getEstimatedRowCount() >
                                                            0) {
                                                            for (Row r :
                                                                 dciterCustClear.getAllRowsInRange()) {
                                                                String BudgetById =
                                                                    r.getAttribute("BudgetById").toString();
                                                                if (budId.equalsIgnoreCase(BudgetById) &&
                                                                    statusOver.equalsIgnoreCase("Y")) {
                                                                    r.setAttribute("OverBudgetAmt",
                                                                                   amountOverSubs);
                                                                }

                                                            }
                                                            dciterCustClear.getDataControl().commitTransaction();
                                                        }
                                                        if (cusId.equalsIgnoreCase(BudgetCustId)) {
                                                            if (!cusId.equalsIgnoreCase(CustIdOver) &&
                                                                statusOver.equalsIgnoreCase("Y")) {
                                                                BigDecimal overValueNew =
                                                                    overBudgetAmountCur.add(amountOverSubs);
                                                                try {
                                                                    PreparedStatement updateTtfNumSeq =
                                                                        conn.prepareStatement("UPDATE BUDGET_CUSTOMER BudgetCustomer SET BUDGETCUSTOMER.OVER_BUDGET=? where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID = ?");
                                                                    updateTtfNumSeq.setBigDecimal(1,
                                                                                                  overValueNew);
                                                                    updateTtfNumSeq.setString(2,
                                                                                              cusId);
                                                                    updateTtfNumSeq.executeUpdate();
                                                                    updateTtfNumSeq.close();
                                                                } catch (SQLException sqle) {
                                                                    System.out.println("------------------------------------------------");
                                                                    System.out.println("ERROR: Cannot run update query");
                                                                    System.out.println("STACK: " +
                                                                                       sqle.toString().trim());
                                                                    System.out.println("------------------------------------------------");
                                                                }
                                                            }
                                                            try {
                                                                PreparedStatement updateTtfNumSeq =
                                                                    conn.prepareStatement("UPDATE BUDGET_CUSTOMER BudgetCustomer SET BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED =? where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID = ?");
                                                                updateTtfNumSeq.setBigDecimal(1,
                                                                                              value);
                                                                updateTtfNumSeq.setString(2,
                                                                                          cusId);
                                                                updateTtfNumSeq.executeUpdate();
                                                                updateTtfNumSeq.close();
                                                            } catch (SQLException sqle) {
                                                                System.out.println("------------------------------------------------");
                                                                System.out.println("ERROR: Cannot run update query");
                                                                System.out.println("STACK: " +
                                                                                   sqle.toString().trim());
                                                                System.out.println("------------------------------------------------");
                                                            }
                                                        } else if (CustIdOver.equalsIgnoreCase(BudgetCustId)) {
                                                            if (!cusId.equalsIgnoreCase(CustIdOver) &&
                                                                statusOver.equalsIgnoreCase("Y")) {
                                                                BigDecimal overValue =
                                                                    overBudgetAmountCur.subtract(amountOverSubs);
                                                                try {
                                                                    PreparedStatement updateTtfNumSeq =
                                                                        conn.prepareStatement("UPDATE BUDGET_CUSTOMER BudgetCustomer SET BUDGETCUSTOMER.OVER_BUDGET=? where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID = ?");
                                                                    updateTtfNumSeq.setBigDecimal(1,
                                                                                                  overValue);
                                                                    updateTtfNumSeq.setString(2,
                                                                                              CustIdOver);
                                                                    updateTtfNumSeq.executeUpdate();
                                                                    updateTtfNumSeq.close();
                                                                } catch (SQLException sqle) {
                                                                    System.out.println("------------------------------------------------");
                                                                    System.out.println("ERROR: Cannot run update query");
                                                                    System.out.println("STACK: " +
                                                                                       sqle.toString().trim());
                                                                    System.out.println("------------------------------------------------");
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }

                                        } else {
                                            BigDecimal valPersen =
                                                new BigDecimal(totalPercenPost).setScale(2,
                                                                                         RoundingMode.HALF_UP);
                                            if (BudgetCustId.equalsIgnoreCase(cusId) &&
                                                budgetAsTodateRemaining.compareTo(BigDecimal.ZERO) <
                                                0 ||
                                                valPersen.compareTo(new BigDecimal("100.00")) >
                                                0) {
                                                ar.setBudgetCustId(BudgetCustId);
                                                ar.setBudgetAsTodateRemaining(budgetAsTodateRemainingInfo.toString());
                                                ar.setBudgetComb(budgetComb);
                                                listBudgetRemainingValidasi.add(ar);
                                            } else {
                                                if (cusId.equalsIgnoreCase(BudgetCustId)) {
                                                    try {
                                                        PreparedStatement updateTtfNumSeq =
                                                            conn.prepareStatement("UPDATE BUDGET_CUSTOMER BudgetCustomer SET BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED =? where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID = ?");
                                                        updateTtfNumSeq.setBigDecimal(1,
                                                                                      value);
                                                        updateTtfNumSeq.setString(2,
                                                                                  cusId);
                                                        updateTtfNumSeq.executeUpdate();
                                                        updateTtfNumSeq.close();
                                                    } catch (SQLException sqle) {
                                                        System.out.println("------------------------------------------------");
                                                        System.out.println("ERROR: Cannot run update query");
                                                        System.out.println("STACK: " +
                                                                           sqle.toString().trim());
                                                        System.out.println("------------------------------------------------");
                                                    }
                                                }
                                            }
                                        }
                                        //end loop
                                    }
                                    List<ListBudgetRemainingValidasi> ab =
                                        getListBudgetRemainingValidasi();
                                    if (ab.size() == 0) {
                                        conn.commit();
                                    }
                                    statement.close();
                                }
                                conn.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            
                            List<ListBudgetRemainingValidasi> ab =
                                getListBudgetRemainingValidasi();
                            BigDecimal postPersen =
                                new BigDecimal(totalPercenPost).setScale(2,
                                                                         RoundingMode.HALF_UP);
                            if (postPersen.compareTo(new BigDecimal("100.00")) >
                                0) {
                                showPopup("Persentase budget tidak boleh melebihi 100%.",
                                          potmessage);
                            } else if (ab.size() > 0) {
                                StringBuilder availBudgetMsg =
                                    new StringBuilder();
                                for (ListBudgetRemainingValidasi b : ab) {
                                    double budAsRem =
                                        (new BigDecimal(b.getBudgetAsTodateRemaining())).doubleValue();
                                    String budAsRemFmt =
                                        numFmt.format(budAsRem);
                                    availBudgetMsg.append(b.getBudgetComb() +
                                                          " [Available Budget: " +
                                                          budAsRemFmt +
                                                          "] <nr>");
                                }
                                availBudgetMsg.append("<nr>Amount yang dimasukan melebihi nilai dari \"Budget As To Date Remaining\"\"");
                                showPopup((availBudgetMsg.length() > 0 ?
                                           availBudgetMsg.substring(0,
                                                                    availBudgetMsg.length() -
                                                                    1) : ""),
                                          potmessage);
                                DCIteratorBinding dciterCustClear =
                                    ADFUtils.findIterator("ProdBudgetByPostView1Iterator");
                                dciterCustClear.executeQuery();
                                if (dciterCustClear.getEstimatedRowCount() >
                                    0) {
                                    for (Row r :
                                         dciterCustClear.getAllRowsInRange()) {
                                        String produkId =
                                            r.getAttribute("PromoProdukId").toString();
                                        String CheckRowStatus =
                                            r.getAttribute("CheckRowStatus").toString();
                                        if (produkId.equalsIgnoreCase(promoProdukId) &&
                                            CheckRowStatus.equalsIgnoreCase("0")) {
                                            r.remove();
                                        }
                                    }
                                }
                            } else {
                                OperationBinding operationBinding =
                                    bindings.getOperationBinding("Commit");
                                operationBinding.execute();

                                OperationBinding executeBudgetPost =
                                    bindings.getOperationBinding("ExecuteBudgetPost");
                                executeBudgetPost.execute();

                                OperationBinding executeBudgetCust =
                                    bindings.getOperationBinding("ExecuteBudgetCust");
                                executeBudgetCust.execute();

                                OperationBinding executeListConf =
                                    bindings.getOperationBinding("ExecuteProposalConf");
                                executeListConf.execute();

                                dciterPropConfirm.setCurrentRowWithKey(parentKey.toStringFormat(true));
                                dciterPromoProduk.setCurrentRowWithKey(promoProdKey.toStringFormat(true));

                                AdfFacesContext.getCurrentInstance().addPartialTarget(tblBudgetPosting);
                                AdfFacesContext.getCurrentInstance().addPartialTarget(tblBudgetCustomer);
                                AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropConfirmation);
                                AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
                                popupDetailProd.hide();
                            }
                        }
                    } else {
                        // selain potongan OnInvoice  dan promo barang on invoice
                        RowSetIterator BudMainCust =
                            dciterPost.getRowSetIterator();
                        HashMap<String, BigDecimal> mapOfSum =
                            new HashMap<String, BigDecimal>();
                        BigDecimal budgetAsToDateUsed = BigDecimal.ZERO;

                        for (Row budgetCusr : dciterPost.getAllRowsInRange()) {
                            String CheckRowStatus =
                                budgetCusr.getAttribute("CheckRowStatus").toString();
                            totalPercenPost +=
                                    Float.valueOf(budgetCusr.getAttribute("Percentage").toString());
                            if (CheckRowStatus.equalsIgnoreCase("0")) {
                                String custId =
                                    budgetCusr.getAttribute("BudgetCustId").toString() +
                                    ";" +
                                    budgetCusr.getAttribute("KombinasiBudget").toString();

                                if (mapOfSum.containsKey(custId)) {
                                    BigDecimal total =
                                        new BigDecimal(budgetCusr.getAttribute("Amount").toString());
                                    budgetAsToDateUsed =
                                            budgetAsToDateUsed.add(total);
                                } else {
                                    BigDecimal total =
                                        new BigDecimal(budgetCusr.getAttribute("Amount").toString());
                                    budgetAsToDateUsed = total;
                                }
                                mapOfSum.put(custId, budgetAsToDateUsed);
                            }
                        }
                        BudMainCust.closeRowSetIterator();
                        listBudgetRemainingValidasi =
                                new ArrayList<ListBudgetRemainingValidasi>();
                        PreparedStatement statement = null;
                        try {
                            Context ctx = new InitialContext();
                            DataSource ds = (DataSource)ctx.lookup(dsFdiConn);
                            conn = ds.getConnection();
                            conn.setAutoCommit(false);

                            BigDecimal budgetAsTodateCur = BigDecimal.ZERO;
                            BigDecimal budgetAsTodate = BigDecimal.ZERO;
                            BigDecimal budgetAsTodateRemaining =
                                BigDecimal.ZERO;
                            BigDecimal budgetAsTodateRemainingInfo =
                                BigDecimal.ZERO;

                            for (Map.Entry<String, BigDecimal> entry :
                                 mapOfSum.entrySet()) {
                                ListBudgetRemainingValidasi ar =
                                    new ListBudgetRemainingValidasi();
                                String cusId = "";
                                String budgetComb = "";
                                String key = entry.getKey();
                                String[] keys = key.split(";");
                                cusId = keys[0].trim();
                                budgetComb = keys[1].trim();
                                statement =
                                        conn.prepareStatement("select BUDGETCUSTOMER.BUDGET_CUSTOMER_ID ,BUDGETCUSTOMER.BUDGET_AS_TO_DATE, BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED,BUDGETCUSTOMER.YEARLY_BUDGET_AMOUNT from BUDGET_CUSTOMER BudgetCustomer where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID=? for update");
                                statement.setString(1, cusId);
                                ResultSet rs = statement.executeQuery();
                                while (rs.next()) {
                                    String BudgetCustId =
                                        rs.getString("BUDGET_CUSTOMER_ID").toString();
                                    budgetAsTodateCur =
                                            new BigDecimal(rs.getString("BUDGET_AS_TO_DATE_USED").toString());
                                    budgetAsTodate =
                                            new BigDecimal(rs.getString("BUDGET_AS_TO_DATE").toString());
                                    
                                    BigDecimal value =
                                        entry.getValue().add(budgetAsTodateCur);
                                    budgetAsTodateRemaining =
                                            budgetAsTodate.subtract(value);
                                    budgetAsTodateRemainingInfo =
                                            budgetAsTodate.subtract(budgetAsTodateCur);
                                    BigDecimal postPersen =
                                        new BigDecimal(totalPercenPost).setScale(2,
                                                                                 RoundingMode.HALF_UP);
                                    if (cusId.equalsIgnoreCase(BudgetCustId) &&
                                        budgetAsTodateRemaining.compareTo(BigDecimal.ZERO) <
                                        0 ||
                                        postPersen.compareTo(new BigDecimal("100.00")) >
                                        0) {
                                        ar.setBudgetCustId(BudgetCustId);
                                        ar.setBudgetAsTodateRemaining(budgetAsTodateRemainingInfo.toString());
                                        ar.setBudgetComb(budgetComb);
                                        listBudgetRemainingValidasi.add(ar);
                                    } else {
                                        if (cusId.equalsIgnoreCase(BudgetCustId)) {
                                            try {
                                                PreparedStatement updateTtfNumSeq =
                                                    conn.prepareStatement("UPDATE BUDGET_CUSTOMER BudgetCustomer SET BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED =? where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID = ?");
                                                updateTtfNumSeq.setBigDecimal(1,
                                                                              value);
                                                updateTtfNumSeq.setString(2,
                                                                          cusId);
                                                updateTtfNumSeq.executeUpdate();
                                                updateTtfNumSeq.close();
                                            } catch (SQLException sqle) {
                                                System.out.println("------------------------------------------------");
                                                System.out.println("ERROR: Cannot run update query");
                                                System.out.println("STACK: " +
                                                                   sqle.toString().trim());
                                                System.out.println("------------------------------------------------");
                                            }
                                        }

                                    }
                                }
                                List<ListBudgetRemainingValidasi> ab =
                                    getListBudgetRemainingValidasi();
                                if (ab.size() == 0) {
                                    conn.commit();
                                }
                                statement.close();
                            }
                            conn.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        
                        List<ListBudgetRemainingValidasi> ab =
                            getListBudgetRemainingValidasi();
                        BigDecimal postPersenVal =
                            new BigDecimal(totalPercenPost).setScale(2,
                                                                     RoundingMode.HALF_UP);
                        if (postPersenVal.compareTo(new BigDecimal("100.00")) >
                            0) {
                            showPopup("Persentase budget tidak boleh melebihi 100%.",
                                      potmessage);
                        } else if (ab.size() > 0) {
                            StringBuilder availBudgetMsg = new StringBuilder();
                            for (ListBudgetRemainingValidasi b : ab) {
                                double budAsRem =
                                    (new BigDecimal(b.getBudgetAsTodateRemaining())).doubleValue();
                                String budAsRemFmt = numFmt.format(budAsRem);
                                availBudgetMsg.append(b.getBudgetComb() +
                                                      " [Available Budget: " +
                                                      budAsRemFmt + "] <nr>");
                            }
                            availBudgetMsg.append("<nr>Amount yang dimasukan melebihi nilai dari \"Budget As To Date Remaining\"\"");
                            showPopup((availBudgetMsg.length() > 0 ?
                                       availBudgetMsg.substring(0,
                                                                availBudgetMsg.length() -
                                                                1) : ""),
                                      potmessage);

                            DCIteratorBinding dciterCustClear =
                                ADFUtils.findIterator("ProdBudgetByPostView1Iterator");
                            dciterCustClear.executeQuery();
                            if (dciterCustClear.getEstimatedRowCount() > 0) {
                                for (Row r :
                                     dciterCustClear.getAllRowsInRange()) {
                                    String produkId =
                                        r.getAttribute("PromoProdukId").toString();
                                    String CheckRowStatus =
                                        r.getAttribute("CheckRowStatus").toString();
                                    if (produkId.equalsIgnoreCase(promoProdukId) &&
                                        CheckRowStatus.equalsIgnoreCase("0")) {
                                        r.remove();
                                    }
                                }
                            }
                        } else {
                            OperationBinding operationBinding =
                                bindings.getOperationBinding("Commit");
                            operationBinding.execute();

                            OperationBinding executeBudgetPost =
                                bindings.getOperationBinding("ExecuteBudgetPost");
                            executeBudgetPost.execute();

                            OperationBinding executeBudgetCust =
                                bindings.getOperationBinding("ExecuteBudgetCust");
                            executeBudgetCust.execute();

                            OperationBinding executeListConf =
                                bindings.getOperationBinding("ExecuteProposalConf");
                            executeListConf.execute();

                            dciterPropConfirm.setCurrentRowWithKey(parentKey.toStringFormat(true));
                            dciterPromoProduk.setCurrentRowWithKey(promoProdKey.toStringFormat(true));

                            AdfFacesContext.getCurrentInstance().addPartialTarget(tblBudgetPosting);
                            AdfFacesContext.getCurrentInstance().addPartialTarget(tblBudgetCustomer);
                            AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropConfirmation);
                            AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
                            popupDetailProd.hide();
                        }
                    }
                } else {
                    popupDetailProd.hide();
                }
            }
        } catch (Exception e) {
            DCIteratorBinding dciterPostClear =
                ADFUtils.findIterator("ProdBudgetByPostView1Iterator");
            dciterPostClear.executeQuery();
            if (dciterPostClear.getEstimatedRowCount() > 0) {
                for (Row r : dciterPostClear.getAllRowsInRange()) {
                    String KombinasiBudget =
                        r.getAttribute("KombinasiBudget") == null ? "" :
                        r.getAttribute("KombinasiBudget").toString();
                    String CheckRowStatus =
                        r.getAttribute("CheckRowStatus").toString();
                    if (CheckRowStatus.equalsIgnoreCase("0") &&
                        KombinasiBudget.equalsIgnoreCase("")) {
                        showPopup("Data masih ada yang belum lengkap.<nr>Mohon dilengkapi terlebih dahulu.",
                                  potmessage);

                    } else {
                        popupDetailProd.hide();
                    }
                }
            }

            DCIteratorBinding dciterCustClear =
                ADFUtils.findIterator("ProdBudgetByCustView1Iterator");
            dciterCustClear.executeQuery();
            if (dciterCustClear.getEstimatedRowCount() > 0) {
                for (Row r : dciterCustClear.getAllRowsInRange()) {
                    String KombinasiBudget =
                        r.getAttribute("KombinasiBudget") == null ? "" :
                        r.getAttribute("KombinasiBudget").toString();
                    String CheckRowStatus =
                        r.getAttribute("CheckRowStatus").toString();
                    if (CheckRowStatus.equalsIgnoreCase("0") &&
                        KombinasiBudget.equalsIgnoreCase("")) {
                        showPopup("Data masih ada yang belum lengkap.<nr>Mohon dilengkapi terlebih dahulu.",
                                  potmessage);

                    } else {
                        popupDetailProd.hide();
                    }
                }
            }
        }
    }

    public void dialogCancelEventCustomDetailProduct(ActionEvent actionEvent) {
        BindingContainer bindings =
            BindingContext.getCurrent().getCurrentBindingsEntry();

        DCIteratorBinding parentIter =
            (DCIteratorBinding)bindings.get("ProposalConfirmationView1Iterator");
        //Get current row key
        Key parentKey = parentIter.getCurrentRow().getKey();

        try {
            OperationBinding executeRollback =
                bindings.getOperationBinding("Rollback");
            executeRollback.execute();
            popupDetailProd.hide();
        } catch (Exception e) {
            showPopup(e.getLocalizedMessage(), potmessage);
        }

        //Set again row key as current row
        parentIter.setCurrentRowWithKey(parentKey.toStringFormat(true));
    }


    public void closePcDialogListener(DialogEvent dialogEvent) {
        BindingContainer bindings = getBindings();
        FacesContext ctx = FacesContext.getCurrentInstance();
        ConfirmationAMImpl confirmationAM =
            (ConfirmationAMImpl)ADFUtils.getApplicationModuleForDataControl("ConfirmationAMDataControl");
        AttributeBinding mekPenagihanAttr =
            (AttributeBinding)bindings.getControlBinding("MekanismePenagihan1");
        String mekPenagihan = (String)mekPenagihanAttr.getInputValue();
        AttributeBinding ProposalNoAttr =
            (AttributeBinding)bindings.getControlBinding("ProposalNo");
        String ProposalNo = (String)ProposalNoAttr.getInputValue();
        AttributeBinding confirmNoAttr =
            (AttributeBinding)bindings.getControlBinding("ConfirmNo");
        String confNo = (String)confirmNoAttr.getInputValue();
        AttributeBinding adendumKeAttr =
            (AttributeBinding)bindings.getControlBinding("AddendumKe");
        String addendumKe = (String)adendumKeAttr.getInputValue();
        AttributeBinding DiscountTypeAttr =
            (AttributeBinding)bindings.getControlBinding("DiscountType1");
        String DiscountType =
            DiscountTypeAttr.getInputValue().toString();

        String confirmNo = null;
        if (addendumKe != null) {
            confirmNo = confNo + "-" + addendumKe;
        } else {
            confirmNo = confNo;
        }
        AttributeBinding DcvFlagAttr =
            (AttributeBinding)bindings.getControlBinding("DcvPcPrFlag");
        String DcvFlag = DcvFlagAttr.getInputValue().toString();

        DCIteratorBinding dciterPromoProduk =
            ADFUtils.findIterator("PromoProdukView1Iterator");
        realisasiTemp = new ArrayList<realisasiTempClass>();
        // BIAYA
        // --> Biaya + Off Invoice = PR
        if (DiscountType.equalsIgnoreCase(discTypeBiaya)) {
            //FCS_VIEW_REALISASI_GR
            for (Row iterRealisasiProd :
                 dciterPromoProduk.getAllRowsInRange()) {
                realisasiTempClass realTemp = new realisasiTempClass();
                realTemp.setCONFIRM_NO(confirmNo);
                realTemp.setPROMO_PRODUK_ID(new BigDecimal(iterRealisasiProd.getAttribute("PromoProdukId").toString()));
                realTemp.setAMOUNTMF(new BigDecimal(iterRealisasiProd.getAttribute("RealisasiGrMf").toString()));
                realTemp.setAMOUNTOT(new BigDecimal(iterRealisasiProd.getAttribute("RealisasiGrOnTop").toString()));
                realTemp.setREALISASI_TYPE("GR");
                realisasiTemp.add(realTemp);
            }
        } 
        // POTONGAN
        // --> Potongan + Off Invoice + No DCV = PR
        else if (DiscountType.equalsIgnoreCase(discTypePotongan) &&
                   mekPenagihan.equalsIgnoreCase(offInvoice) && DcvFlag.equalsIgnoreCase("N")) {
            //FCS_VIEW_REALISASI_GR
            for (Row iterRealisasiProd :
                 dciterPromoProduk.getAllRowsInRange()) {
                realisasiTempClass realTemp = new realisasiTempClass();
                realTemp.setCONFIRM_NO(confirmNo);
                realTemp.setPROMO_PRODUK_ID(new BigDecimal(iterRealisasiProd.getAttribute("PromoProdukId").toString()));
                realTemp.setAMOUNTMF(new BigDecimal(iterRealisasiProd.getAttribute("RealisasiGrMf").toString()));
                realTemp.setAMOUNTOT(new BigDecimal(iterRealisasiProd.getAttribute("RealisasiGrOnTop").toString()));
                realTemp.setREALISASI_TYPE("GR");
                realisasiTemp.add(realTemp);
            }
        } 
        // --> Potongan + Off Invoice + DCV
        else if (DiscountType.equalsIgnoreCase(discTypePotongan) &&
                   mekPenagihan.equalsIgnoreCase(offInvoice) && DcvFlag.equalsIgnoreCase("Y")) {
            //FCS_VIEW_REALISASI_DCV
            for (Row iterRealisasiProd :
                 dciterPromoProduk.getAllRowsInRange()) {
                realisasiTempClass realTemp = new realisasiTempClass();
                realTemp.setCONFIRM_NO(confirmNo);
                realTemp.setPROMO_PRODUK_ID(new BigDecimal(iterRealisasiProd.getAttribute("PromoProdukId").toString()));
                realTemp.setAMOUNTMF(new BigDecimal(iterRealisasiProd.getAttribute("RealisasiDcvMf").toString()));
                realTemp.setAMOUNTOT(new BigDecimal(iterRealisasiProd.getAttribute("RealisasiDcvOnTop").toString()));
                realTemp.setREALISASI_TYPE("DCV");
                realisasiTemp.add(realTemp);
            }
        }
        // --> Potongan + On Invoice = MODIFIER
        else if(DiscountType.equalsIgnoreCase(discTypePotongan) && mekPenagihan.equalsIgnoreCase(onInvoice)){
            //FCS_VIEW_REALISASI_MODIFIER
            for (Row iterRealisasiProd :
                 dciterPromoProduk.getAllRowsInRange()) {
                realisasiTempClass realTemp = new realisasiTempClass();
                realTemp.setCONFIRM_NO(confirmNo);
                realTemp.setPROMO_PRODUK_ID(new BigDecimal(iterRealisasiProd.getAttribute("PromoProdukId").toString()));
                realTemp.setAMOUNTMF(new BigDecimal(iterRealisasiProd.getAttribute("RealisasiModMf").toString()));
                realTemp.setAMOUNTOT(new BigDecimal(iterRealisasiProd.getAttribute("RealisasiModOnTop").toString()));
                realTemp.setREALISASI_TYPE("MOD");
                realisasiTemp.add(realTemp);
            }
        }
        
        // PROMO BARANG
        // --> Promo Barang + Off Invoice + No DCV = PR
        else if (DiscountType.equalsIgnoreCase(discTypePromoBarang) &&
                   mekPenagihan.equalsIgnoreCase(offInvoice) && DcvFlag.equalsIgnoreCase("N")) {
            //FCS_VIEW_REALISASI_GR
            for (Row iterRealisasiProd :
                 dciterPromoProduk.getAllRowsInRange()) {
                realisasiTempClass realTemp = new realisasiTempClass();
                realTemp.setCONFIRM_NO(confirmNo);
                realTemp.setPROMO_PRODUK_ID(new BigDecimal(iterRealisasiProd.getAttribute("PromoProdukId").toString()));
                realTemp.setAMOUNTMF(new BigDecimal(iterRealisasiProd.getAttribute("RealisasiGrMf").toString()));
                realTemp.setAMOUNTOT(new BigDecimal(iterRealisasiProd.getAttribute("RealisasiGrOnTop").toString()));
                realTemp.setREALISASI_TYPE("GR");
                realisasiTemp.add(realTemp);
            }
        } 
        // --> Potongan + Off Invoice + DCV
        else if (DiscountType.equalsIgnoreCase(discTypePromoBarang) &&
                   mekPenagihan.equalsIgnoreCase(offInvoice) && DcvFlag.equalsIgnoreCase("Y")) {
            //FCS_VIEW_REALISASI_DCV
            for (Row iterRealisasiProd :
                 dciterPromoProduk.getAllRowsInRange()) {
                realisasiTempClass realTemp = new realisasiTempClass();
                realTemp.setCONFIRM_NO(confirmNo);
                realTemp.setPROMO_PRODUK_ID(new BigDecimal(iterRealisasiProd.getAttribute("PromoProdukId").toString()));
                realTemp.setAMOUNTMF(new BigDecimal(iterRealisasiProd.getAttribute("RealisasiDcvMf").toString()));
                realTemp.setAMOUNTOT(new BigDecimal(iterRealisasiProd.getAttribute("RealisasiDcvOnTop").toString()));
                realTemp.setREALISASI_TYPE("DCV");
                realisasiTemp.add(realTemp);
            }
        } 
        // --> Promo Barang + On Invoice
        else if(DiscountType.equalsIgnoreCase(discTypePromoBarang) && mekPenagihan.equalsIgnoreCase(onInvoice)){
            //FCS_VIEW_REALISASI_PROMOBARANG
            for (Row iterRealisasiProd :
                 dciterPromoProduk.getAllRowsInRange()) {
                realisasiTempClass realTemp = new realisasiTempClass();
                realTemp.setCONFIRM_NO(confirmNo);
                realTemp.setPROMO_PRODUK_ID(new BigDecimal(iterRealisasiProd.getAttribute("PromoProdukId").toString()));
                realTemp.setAMOUNTMF(new BigDecimal(iterRealisasiProd.getAttribute("RealisasiPrmBrgMfByLine").toString()));
                realTemp.setAMOUNTOT(new BigDecimal(iterRealisasiProd.getAttribute("RealisasiPrmBrgOnTopByLine").toString()));
                realTemp.setREALISASI_TYPE("PRB");
                realisasiTemp.add(realTemp);
            }
        }
        
        if (dialogEvent.getOutcome().name().equals("ok")) {
            if (mekPenagihan.equalsIgnoreCase(offInvoice)) {
                // Cek status PR, if CANCELLED or FINALLY CLOSED
                FcsViewNoPrClosedViewImpl noPrClosedView =
                    confirmationAM.getFcsViewNoPrClosedView1();
                noPrClosedView.setNamedWhereClauseParam("noPc", confirmNo);
                noPrClosedView.executeQuery();

                long prExists = noPrClosedView.getEstimatedRowCount();
                if (prExists > 0 || DcvFlag.equalsIgnoreCase("Y")) {
                    // Cek status DCV: AMOUNT DUE REMAINING = 0
                    CloseValidationDcvAmountImpl checkDcvView =
                        confirmationAM.getCloseValidationDcvAmount1();
                    checkDcvView.setNamedWhereClauseParam("noConfirm",
                                                          confirmNo);
                    checkDcvView.executeQuery();
                    if (DcvFlag.equalsIgnoreCase("Y")) {
                        long dcvExists = checkDcvView.getEstimatedRowCount();
                        if (dcvExists > 0) {
                            Row checkDcvRow = checkDcvView.first();
                            Number amtDueRem =
                                (Number)checkDcvRow.getAttribute("Remaining");
                            Number zeroVal = new Number(0);
                            if (amtDueRem.compareTo(zeroVal) == 0) {
                                //hapusBudgetAlokasi
                                checkAddendumBudgetClearViewImpl budgetsubs =
                                    confirmationAM.getcheckAddendumBudgetClearView1();
                                budgetsubs.setNamedWhereClauseParam("confirmNO",
                                                                    confirmNo);
                                budgetsubs.executeQuery();

                                ProposalUpdateConfirmAdendumViewImpl voProposalConfirm =
                                    (ProposalUpdateConfirmAdendumViewImpl)confirmationAM.getProposalUpdateConfirmAdendumView1();
                                voProposalConfirm.setWhereClause("Proposal.CONFIRM_NO = '" +
                                                                 confirmNo +
                                                                 "'");
                                voProposalConfirm.executeQuery();

                                listAddendumBudget =
                                        new ArrayList<ListAddendumBudget>();
                                listAddendumAmount =
                                        new ArrayList<ListAddendumAmount>();
                                HashMap<String, BigDecimal> mapOfSum =
                                    new HashMap<String, BigDecimal>();
                                BigDecimal budgetAsToDateUsed =
                                    BigDecimal.ZERO;
                                Connection conn = null;
                                checkProdukApprovalModifierOnInImpl budgetcekProdukApproval =
                                    confirmationAM.getcheckProdukApprovalModifierOnIn1();
                                budgetcekProdukApproval.setNamedWhereClauseParam("pNo",
                                                                                 ProposalNo);
                                budgetcekProdukApproval.executeQuery();

                                while (budgetsubs.hasNext()) {
                                    Row erCekData = budgetsubs.next();
                                    ListAddendumBudget ar =
                                        new ListAddendumBudget();
                                    String custId =
                                        erCekData.getAttribute("BudgetCustId").toString();
                                    String pid =
                                        erCekData.getAttribute("PromoProdukId").toString();
                                    String budgetid =
                                        erCekData.getAttribute("BudgetById").toString();
                                    ar.setBudgetCustId(custId);
                                    ar.setPromoProdukId(pid);
                                    ar.setBudgetById(budgetid);
                                    List<realisasiTempClass> realTemp =
                                        getRealisasiTemp();
                                    if (realTemp.size() > 0) {
                                        for (realisasiTempClass realBud :
                                             realTemp) {
                                            if (pid.equalsIgnoreCase(realBud.getPROMO_PRODUK_ID().toString())) {
                                                ListAddendumAmount tempUsed =
                                                    new ListAddendumAmount();
                                                tempUsed.setPromoProdukId(pid);
                                                tempUsed.setBudgetCustId(custId);
                                                tempUsed.setBudgetById(budgetid);
                                                BigDecimal total =
                                                    new BigDecimal(erCekData.getAttribute("Amount").toString());
                                                BigDecimal totalOverBudget =
                                                    new BigDecimal(erCekData.getAttribute("OverBudgetAmt") ==
                                                                   null ? "0" :
                                                                   erCekData.getAttribute("OverBudgetAmt").toString());
                                                tempUsed.setOverBudgetAmt(totalOverBudget);
                                                BigDecimal realamountmf =
                                                    realBud.getAMOUNTMF();
                                                if (realamountmf.compareTo(total) <
                                                    0 ||
                                                    realamountmf.compareTo(total) ==
                                                    0) {
                                                    BigDecimal selisihUsedBudget =
                                                        total.subtract(realamountmf);
                                                    tempUsed.setAmount(selisihUsedBudget);
                                                } else {
                                                    BigDecimal selisihUsedBudget =
                                                        realamountmf.subtract(total);
                                                    BigDecimal endUsedBudget =
                                                        selisihUsedBudget.subtract(total);
                                                    realBud.setAMOUNTMF(selisihUsedBudget);
                                                    if (endUsedBudget.compareTo(new BigDecimal(0)) <
                                                        0) {
                                                        tempUsed.setAmount(new BigDecimal(0));
                                                    } else {
                                                        tempUsed.setAmount(endUsedBudget);
                                                    }
                                                }
                                                listAddendumAmount.add(tempUsed);
                                            }
                                        }
                                    } else {
                                        ListAddendumAmount tempUsed =
                                            new ListAddendumAmount();
                                        tempUsed.setPromoProdukId(pid);
                                        tempUsed.setBudgetCustId(custId);
                                        tempUsed.setBudgetById(budgetid);
                                        BigDecimal total =
                                            new BigDecimal(erCekData.getAttribute("Amount").toString());
                                        BigDecimal totalOverBudget =
                                            new BigDecimal(erCekData.getAttribute("OverBudgetAmt") ==
                                                           null ? "0" :
                                                           erCekData.getAttribute("OverBudgetAmt").toString());
                                        tempUsed.setOverBudgetAmt(totalOverBudget);
                                        tempUsed.setAmount(total);
                                        listAddendumAmount.add(tempUsed);
                                    }
                                    mapOfSum.put(custId, budgetAsToDateUsed);
                                    listAddendumBudget.add(ar);
                                }

                                ///// insert table for consumtion report ////////
                                ProdRealSummaryViewImpl vo =
                                    confirmationAM.getProdRealSummaryView1();
                                List<ListAddendumAmount> realTempend =
                                    getListAddendumAmount();
                                for (ListAddendumAmount setRealsum :
                                     realTempend) {
                                    Row cvrt = vo.createRow();
                                    String promProdIdAmnt =
                                        setRealsum.getPromoProdukId().toString();
                                    String BudgetCustId =
                                        setRealsum.getBudgetCustId().toString();
                                    BigDecimal Amount =
                                        setRealsum.getAmount() == null ?
                                        BigDecimal.ZERO :
                                        setRealsum.getAmount();
                                    cvrt.setAttribute("ProposalNo",
                                                      ProposalNo);
                                    cvrt.setAttribute("PromoProdukId",
                                                      new Number(Float.parseFloat(promProdIdAmnt)));
                                    cvrt.setAttribute("BudgetCustId",
                                                      BudgetCustId);
                                    cvrt.setAttribute("Amount", Amount);
                                    cvrt.setAttribute("Status", "Closed");
                                    List<realisasiTempClass> realTemp =
                                        getRealisasiTemp();
                                    for (realisasiTempClass realSet :
                                         realTemp) {
                                        String ppidReal =
                                            realSet.getPROMO_PRODUK_ID().toString();
                                        System.out.println(realSet.getPROMO_PRODUK_ID().toString());
                                        System.out.println(realSet.getAMOUNTMF().toString());
                                        if (promProdIdAmnt.equalsIgnoreCase(ppidReal)) {
                                            oracle.jbo.domain.Number ot =
                                                new Number(Float.parseFloat(realSet.getAMOUNTOT().toString()));
                                            oracle.jbo.domain.Number mf =
                                                new Number(Float.parseFloat(realSet.getAMOUNTMF().toString()));
                                            cvrt.setAttribute("RealisasiOt",
                                                              ot);
                                            cvrt.setAttribute("RealisasiMf",
                                                              mf);
                                            cvrt.setAttribute("RealisasiType",
                                                              realSet.getREALISASI_TYPE());
                                        }
                                    }
                                }
                                confirmationAM.getDBTransaction().commit();
                                vo.executeQuery();
                                for (ListAddendumAmount cek : realTempend) {
                                    String budgetCustIdFdi = "";
                                    BigDecimal amountSubs = BigDecimal.ZERO;
                                    BigDecimal amountOverSubs =
                                        BigDecimal.ZERO;
                                    budgetCustIdFdi = cek.getBudgetCustId();
                                    amountSubs = cek.getAmount();
                                    amountOverSubs =
                                            new BigDecimal(cek.getOverBudgetAmt().toString() ==
                                                           null ? "0" :
                                                           cek.getOverBudgetAmt().toString());
                                    if (amountSubs.compareTo(BigDecimal.ZERO) >
                                        0) {
                                        try {
                                            Context ctx1 =
                                                new InitialContext();
                                            DataSource ds =
                                                (DataSource)ctx1.lookup("jdbc/focusppDS");
                                            conn = ds.getConnection();
                                            conn.setAutoCommit(false);
                                            PreparedStatement statement =
                                                conn.prepareStatement("select BUDGETCUSTOMER.BUDGET_CUSTOMER_ID ,BUDGETCUSTOMER.BUDGET_AS_TO_DATE, BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED,BUDGETCUSTOMER.YEARLY_BUDGET_AMOUNT,BUDGETCUSTOMER.OVER_BUDGET from BUDGET_CUSTOMER BudgetCustomer where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID=? for update");
                                            statement.setString(1,
                                                                budgetCustIdFdi);
                                            ResultSet rs =
                                                statement.executeQuery();
                                            while (rs.next()) {
                                                String BudgetCustId =
                                                    rs.getString("BUDGET_CUSTOMER_ID").toString();
                                                BigDecimal budgetAsTodateCur =
                                                    BigDecimal.ZERO;
                                                BigDecimal budgetAsTodate =
                                                    BigDecimal.ZERO;
                                                BigDecimal overBudgetAmountCur =
                                                    BigDecimal.ZERO;
                                                budgetAsTodateCur =
                                                        new BigDecimal(rs.getString("BUDGET_AS_TO_DATE_USED").toString());
                                                budgetAsTodate =
                                                        new BigDecimal(rs.getString("BUDGET_AS_TO_DATE").toString());
                                                overBudgetAmountCur =
                                                        new BigDecimal(rs.getString("OVER_BUDGET") ==
                                                                       null ?
                                                                       "0" :
                                                                       rs.getString("OVER_BUDGET").toString());
                                                if (budgetCustIdFdi.equalsIgnoreCase(BudgetCustId)) {
                                                    BigDecimal value =
                                                        budgetAsTodateCur.subtract(amountSubs);
                                                    BigDecimal overValue =
                                                        overBudgetAmountCur.subtract(amountOverSubs);
                                                    try {
                                                        PreparedStatement updateTtfNumSeq =
                                                            conn.prepareStatement("UPDATE BUDGET_CUSTOMER BudgetCustomer SET BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED =? ,BUDGETCUSTOMER.OVER_BUDGET=? where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID = ?");
                                                        updateTtfNumSeq.setBigDecimal(1,
                                                                                      value);
                                                        updateTtfNumSeq.setBigDecimal(2,
                                                                                      overValue);
                                                        updateTtfNumSeq.setString(3,
                                                                                  budgetCustIdFdi);
                                                        updateTtfNumSeq.executeUpdate();
                                                        updateTtfNumSeq.close();
                                                    } catch (SQLException sqle) {
                                                        System.out.println("------------------------------------------------");
                                                        System.out.println("ERROR: Cannot run update query");
                                                        System.out.println("STACK: " +
                                                                           sqle.toString().trim());
                                                        System.out.println("------------------------------------------------");
                                                    }
                                                }
                                            }
                                            conn.commit();
                                            statement.close();
                                            conn.close();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                List<ListAddendumBudget> ab =
                                    getListAddendumBudget();

                                for (ListAddendumBudget x : ab) {
                                    String budgetByid =
                                        x.getBudgetById() == null ? "" :
                                        x.getBudgetById().toString();
                                    DCIteratorBinding dciterCust =
                                        ADFUtils.findIterator("ProdBudgetByView1Iterator");
                                    RowSetIterator BudCust =
                                        dciterCust.getRowSetIterator();
                                    dciterCust.executeQuery();
                                    ViewObject voTableData =
                                        dciterCust.getViewObject();
                                    while (voTableData.hasNext()) {
                                        Row rowSelected = voTableData.next();
                                        String budgetId =
                                            rowSelected.getAttribute("BudgetById") ==
                                            null ? "" :
                                            rowSelected.getAttribute("BudgetById").toString();
                                        if (budgetId.equalsIgnoreCase(budgetByid)) {
                                            rowSelected.setAttribute("Status",
                                                                     "Y");
                                        }
                                    }
                                    dciterCust.getDataControl().commitTransaction();
                                    BudCust.closeRowSetIterator();
                                }

                                // Set CLOSE PC
                                AttributeBinding statusPropAttr =
                                    (AttributeBinding)bindings.getControlBinding("Status1");
                                statusPropAttr.setInputValue("CLOSED");
                            } else {
                                double amount = amtDueRem.getValue();
                                DecimalFormat formatter =
                                    new DecimalFormat("#,###.##");

                                StringBuilder message =
                                    new StringBuilder("<html><body>");
                                message.append("<p>Invoice DCV belum di apply full. Amount masih tersisa " +
                                               formatter.format(amount) +
                                               "</p>");
                                message.append("<p>Mohon untuk menyelesaikannya terlebih dahulu agar PR ini dapat di-close.</p>");
                                message.append("</body></html>");
                                FacesMessage msg =
                                    new FacesMessage(message.toString());
                                msg.setSeverity(FacesMessage.SEVERITY_WARN);
                                ctx.addMessage(null, msg);
                            }
                        } else {
                            StringBuilder message =
                                new StringBuilder("<html><body>");
                            message.append("<p>\"Amount Due Remaining\" tidak ditemukan untuk no confirm \"" +
                                           confirmNo + "\".</p>");
                            message.append("<p>Dokumen PR ini belum dapat di-close.</p>");
                            message.append("</body></html>");
                            FacesMessage msg =
                                new FacesMessage(message.toString());
                            msg.setSeverity(FacesMessage.SEVERITY_WARN);
                            ctx.addMessage(null, msg);
                        }
                    } else {
                        //hapusBudgetAlokasi
                        checkAddendumBudgetClearViewImpl budgetsubs =
                            confirmationAM.getcheckAddendumBudgetClearView1();
                        budgetsubs.setNamedWhereClauseParam("confirmNO",
                                                            confirmNo);
                        budgetsubs.executeQuery();

                        ProposalUpdateConfirmAdendumViewImpl voProposalConfirm =
                            (ProposalUpdateConfirmAdendumViewImpl)confirmationAM.getProposalUpdateConfirmAdendumView1();
                        voProposalConfirm.setWhereClause("Proposal.CONFIRM_NO = '" +
                                                         confirmNo + "'");
                        voProposalConfirm.executeQuery();

                        listAddendumBudget =
                                new ArrayList<ListAddendumBudget>();
                        listAddendumAmount =
                                new ArrayList<ListAddendumAmount>();
                        HashMap<String, BigDecimal> mapOfSum =
                            new HashMap<String, BigDecimal>();
                        BigDecimal budgetAsToDateUsed = BigDecimal.ZERO;
                        Connection conn = null;
                        checkProdukApprovalModifierOnInImpl budgetcekProdukApproval =
                            confirmationAM.getcheckProdukApprovalModifierOnIn1();
                        budgetcekProdukApproval.setNamedWhereClauseParam("pNo",
                                                                         ProposalNo);
                        budgetcekProdukApproval.executeQuery();
                        while (budgetsubs.hasNext()) {
                            Row erCekData = budgetsubs.next();
                            ListAddendumBudget ar = new ListAddendumBudget();
                            String custId =
                                erCekData.getAttribute("BudgetCustId").toString();
                            String pid =
                                erCekData.getAttribute("PromoProdukId").toString();
                            String budgetid =
                                erCekData.getAttribute("BudgetById").toString();
                            ar.setBudgetCustId(custId);
                            ar.setPromoProdukId(pid);
                            ar.setBudgetById(budgetid);
                            List<realisasiTempClass> realTemp =
                                getRealisasiTemp();
                            if (realTemp.size() > 0) {
                                for (realisasiTempClass realBud : realTemp) {
                                    if (pid.equalsIgnoreCase(realBud.getPROMO_PRODUK_ID().toString())) {
                                        ListAddendumAmount tempUsed =
                                            new ListAddendumAmount();
                                        tempUsed.setPromoProdukId(pid);
                                        tempUsed.setBudgetCustId(custId);
                                        tempUsed.setBudgetById(budgetid);
                                        BigDecimal total =
                                            new BigDecimal(erCekData.getAttribute("Amount").toString());
                                        BigDecimal totalOverBudget =
                                            new BigDecimal(erCekData.getAttribute("OverBudgetAmt") ==
                                                           null ? "0" :
                                                           erCekData.getAttribute("OverBudgetAmt").toString());
                                        tempUsed.setOverBudgetAmt(totalOverBudget);
                                        BigDecimal realamountmf =
                                            realBud.getAMOUNTMF();
                                        if (realamountmf.compareTo(total) <
                                            0 ||
                                            realamountmf.compareTo(total) ==
                                            0) {
                                            BigDecimal selisihUsedBudget =
                                                total.subtract(realamountmf);
                                            tempUsed.setAmount(selisihUsedBudget);
                                        } else {
                                            BigDecimal selisihUsedBudget =
                                                realamountmf.subtract(total);
                                            BigDecimal endUsedBudget =
                                                selisihUsedBudget.subtract(total);
                                            realBud.setAMOUNTMF(selisihUsedBudget);
                                            if (endUsedBudget.compareTo(new BigDecimal(0)) <
                                                0) {
                                                tempUsed.setAmount(new BigDecimal(0));
                                            } else {
                                                tempUsed.setAmount(endUsedBudget);
                                            }
                                        }
                                        listAddendumAmount.add(tempUsed);
                                    }
                                }
                            } else {
                                ListAddendumAmount tempUsed =
                                    new ListAddendumAmount();
                                tempUsed.setPromoProdukId(pid);
                                tempUsed.setBudgetCustId(custId);
                                tempUsed.setBudgetById(budgetid);
                                BigDecimal total =
                                    new BigDecimal(erCekData.getAttribute("Amount").toString());
                                BigDecimal totalOverBudget =
                                    new BigDecimal(erCekData.getAttribute("OverBudgetAmt") ==
                                                   null ? "0" :
                                                   erCekData.getAttribute("OverBudgetAmt").toString());
                                tempUsed.setOverBudgetAmt(totalOverBudget);
                                tempUsed.setAmount(total);
                                listAddendumAmount.add(tempUsed);
                            }
                            mapOfSum.put(custId, budgetAsToDateUsed);
                            listAddendumBudget.add(ar);
                        }

                        ///// insert table for consumtion report ////////
                        ProdRealSummaryViewImpl vo =
                            confirmationAM.getProdRealSummaryView1();
                        List<ListAddendumAmount> realTempend =
                            getListAddendumAmount();
                        for (ListAddendumAmount setRealsum : realTempend) {
                            Row cvrt = vo.createRow();
                            String promProdIdAmnt =
                                setRealsum.getPromoProdukId().toString();
                            String BudgetCustId =
                                setRealsum.getBudgetCustId().toString();
                            BigDecimal Amount =
                                setRealsum.getAmount() == null ?
                                BigDecimal.ZERO : setRealsum.getAmount();
                            cvrt.setAttribute("ProposalNo", ProposalNo);
                            cvrt.setAttribute("PromoProdukId",
                                              new Number(Float.parseFloat(promProdIdAmnt)));
                            cvrt.setAttribute("BudgetCustId", BudgetCustId);
                            cvrt.setAttribute("Amount", Amount);
                            cvrt.setAttribute("Status", "Closed");
                            List<realisasiTempClass> realTemp =
                                getRealisasiTemp();
                            for (realisasiTempClass realSet : realTemp) {
                                String ppidReal =
                                    realSet.getPROMO_PRODUK_ID().toString();
                                if (promProdIdAmnt.equalsIgnoreCase(ppidReal)) {
                                    oracle.jbo.domain.Number ot =
                                        new Number(Float.parseFloat(realSet.getAMOUNTOT().toString()));
                                    oracle.jbo.domain.Number mf =
                                        new Number(Float.parseFloat(realSet.getAMOUNTMF().toString()));
                                    cvrt.setAttribute("RealisasiOt", ot);
                                    cvrt.setAttribute("RealisasiMf", mf);
                                    cvrt.setAttribute("RealisasiType",
                                                      realSet.getREALISASI_TYPE());
                                }
                            }
                        }
                        confirmationAM.getDBTransaction().commit();
                        vo.executeQuery();
                        for (ListAddendumAmount cek : realTempend) {
                            String budgetCustIdFdi = "";
                            BigDecimal amountSubs = BigDecimal.ZERO;
                            BigDecimal amountOverSubs = BigDecimal.ZERO;
                            budgetCustIdFdi = cek.getBudgetCustId();
                            amountSubs = cek.getAmount();
                            amountOverSubs =
                                    new BigDecimal(cek.getOverBudgetAmt().toString() ==
                                                   null ? "0" :
                                                   cek.getOverBudgetAmt().toString());
                            if (amountSubs.compareTo(BigDecimal.ZERO) > 0) {
                                try {
                                    Context ctx1 = new InitialContext();
                                    DataSource ds =
                                        (DataSource)ctx1.lookup("jdbc/focusppDS");
                                    conn = ds.getConnection();
                                    conn.setAutoCommit(false);
                                    PreparedStatement statement =
                                        conn.prepareStatement("select BUDGETCUSTOMER.BUDGET_CUSTOMER_ID ,BUDGETCUSTOMER.BUDGET_AS_TO_DATE, BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED,BUDGETCUSTOMER.YEARLY_BUDGET_AMOUNT,BUDGETCUSTOMER.OVER_BUDGET from BUDGET_CUSTOMER BudgetCustomer where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID=? for update");
                                    statement.setString(1, budgetCustIdFdi);
                                    ResultSet rs = statement.executeQuery();
                                    while (rs.next()) {
                                        String BudgetCustId =
                                            rs.getString("BUDGET_CUSTOMER_ID").toString();
                                        BigDecimal budgetAsTodateCur =
                                            BigDecimal.ZERO;
                                        BigDecimal budgetAsTodate =
                                            BigDecimal.ZERO;
                                        BigDecimal overBudgetAmountCur =
                                            BigDecimal.ZERO;
                                        budgetAsTodateCur =
                                                new BigDecimal(rs.getString("BUDGET_AS_TO_DATE_USED").toString());
                                        budgetAsTodate =
                                                new BigDecimal(rs.getString("BUDGET_AS_TO_DATE").toString());
                                        overBudgetAmountCur =
                                                new BigDecimal(rs.getString("OVER_BUDGET") ==
                                                               null ? "0" :
                                                               rs.getString("OVER_BUDGET").toString());
                                        if (budgetCustIdFdi.equalsIgnoreCase(BudgetCustId)) {
                                            BigDecimal value =
                                                budgetAsTodateCur.subtract(amountSubs);
                                            BigDecimal overValue =
                                                overBudgetAmountCur.subtract(amountOverSubs);
                                            try {
                                                PreparedStatement updateTtfNumSeq =
                                                    conn.prepareStatement("UPDATE BUDGET_CUSTOMER BudgetCustomer SET BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED =? ,BUDGETCUSTOMER.OVER_BUDGET=? where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID = ?");
                                                updateTtfNumSeq.setBigDecimal(1,
                                                                              value);
                                                updateTtfNumSeq.setBigDecimal(2,
                                                                              overValue);
                                                updateTtfNumSeq.setString(3,
                                                                          budgetCustIdFdi);
                                                updateTtfNumSeq.executeUpdate();
                                                updateTtfNumSeq.close();
                                            } catch (SQLException sqle) {
                                                System.out.println("------------------------------------------------");
                                                System.out.println("ERROR: Cannot run update query");
                                                System.out.println("STACK: " +
                                                                   sqle.toString().trim());
                                                System.out.println("------------------------------------------------");
                                            }
                                        }
                                    }
                                    conn.commit();
                                    statement.close();
                                    conn.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        List<ListAddendumBudget> ab = getListAddendumBudget();

                        for (ListAddendumBudget x : ab) {
                            String budgetByid =
                                x.getBudgetById() == null ? "" :
                                x.getBudgetById().toString();
                            DCIteratorBinding dciterCust =
                                ADFUtils.findIterator("ProdBudgetByView1Iterator");
                            RowSetIterator BudCust =
                                dciterCust.getRowSetIterator();
                            dciterCust.executeQuery();
                            ViewObject voTableData =
                                dciterCust.getViewObject();
                            while (voTableData.hasNext()) {
                                Row rowSelected = voTableData.next();
                                String budgetId =
                                    rowSelected.getAttribute("BudgetById") ==
                                    null ? "" :
                                    rowSelected.getAttribute("BudgetById").toString();
                                if (budgetId.equalsIgnoreCase(budgetByid)) {
                                    rowSelected.setAttribute("Status", "Y");
                                }
                            }
                            dciterCust.getDataControl().commitTransaction();
                            BudCust.closeRowSetIterator();
                        }
                        // Set CLOSE PC
                        AttributeBinding statusPropAttr =
                            (AttributeBinding)bindings.getControlBinding("Status1");
                        statusPropAttr.setInputValue("CLOSED");
                    }
                } else {
                    // Cek tanggal akhir periode promo
                    AttributeBinding periodeToAttr =
                        (AttributeBinding)bindings.getControlBinding("PeriodeProgTo");
                    String endDateString =
                        (String)periodeToAttr.getInputValue();

                    DateFormat df =
                        new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
                    java.util.Date endDate = null;
                    try {
                        endDate = df.parse(endDateString + " 23:59:59");
                    } catch (ParseException e) {
                        JSFUtils.addFacesErrorMessage(e.getMessage());
                    }

                    java.util.Date todayDate = new java.util.Date();
                    if (endDate.before(todayDate) ||
                        todayDate.equals(endDate)) {
                        //hapusBudgetAlokasi
                        checkAddendumBudgetClearViewImpl budgetsubs =
                            confirmationAM.getcheckAddendumBudgetClearView1();
                        budgetsubs.setNamedWhereClauseParam("confirmNO",
                                                            confirmNo);
                        budgetsubs.executeQuery();

                        ProposalUpdateConfirmAdendumViewImpl voProposalConfirm =
                            (ProposalUpdateConfirmAdendumViewImpl)confirmationAM.getProposalUpdateConfirmAdendumView1();
                        voProposalConfirm.setWhereClause("Proposal.CONFIRM_NO = '" +
                                                         confirmNo + "'");
                        voProposalConfirm.executeQuery();

                        listAddendumBudget =
                                new ArrayList<ListAddendumBudget>();
                        listAddendumAmount =
                                new ArrayList<ListAddendumAmount>();
                        HashMap<String, BigDecimal> mapOfSum =
                            new HashMap<String, BigDecimal>();
                        BigDecimal budgetAsToDateUsed = BigDecimal.ZERO;
                        Connection conn = null;
                        checkProdukApprovalModifierOnInImpl budgetcekProdukApproval =
                            confirmationAM.getcheckProdukApprovalModifierOnIn1();
                        budgetcekProdukApproval.setNamedWhereClauseParam("pNo",
                                                                         ProposalNo);
                        budgetcekProdukApproval.executeQuery();
                        
                        while (budgetsubs.hasNext()) {
                            Row erCekData = budgetsubs.next();
                            ListAddendumBudget ar = new ListAddendumBudget();
                            String custId =
                                erCekData.getAttribute("BudgetCustId").toString();
                            String pid =
                                erCekData.getAttribute("PromoProdukId").toString();
                            String budgetid =
                                erCekData.getAttribute("BudgetById").toString();
                            ar.setBudgetCustId(custId);
                            ar.setPromoProdukId(pid);
                            ar.setBudgetById(budgetid);
                            List<realisasiTempClass> realTemp =
                                getRealisasiTemp();
                            if (realTemp.size() > 0) {
                                for (realisasiTempClass realBud : realTemp) {
                                    if (pid.equalsIgnoreCase(realBud.getPROMO_PRODUK_ID().toString())) {
                                        ListAddendumAmount tempUsed =
                                            new ListAddendumAmount();
                                        tempUsed.setPromoProdukId(pid);
                                        tempUsed.setBudgetCustId(custId);
                                        tempUsed.setBudgetById(budgetid);
                                        BigDecimal total =
                                            new BigDecimal(erCekData.getAttribute("Amount").toString());
                                        BigDecimal totalOverBudget =
                                            new BigDecimal(erCekData.getAttribute("OverBudgetAmt") ==
                                                           null ? "0" :
                                                           erCekData.getAttribute("OverBudgetAmt").toString());
                                        tempUsed.setOverBudgetAmt(totalOverBudget);
                                        BigDecimal realamountmf =
                                            realBud.getAMOUNTMF();
                                        if (realamountmf.compareTo(total) <
                                            0 ||
                                            realamountmf.compareTo(total) ==
                                            0) {
                                            BigDecimal selisihUsedBudget =
                                                total.subtract(realamountmf);
                                            tempUsed.setAmount(selisihUsedBudget);
                                        } else {
                                            BigDecimal selisihUsedBudget =
                                                realamountmf.subtract(total);
                                            BigDecimal endUsedBudget =
                                                selisihUsedBudget.subtract(total);
                                            realBud.setAMOUNTMF(selisihUsedBudget);
                                            if (endUsedBudget.compareTo(new BigDecimal(0)) <
                                                0) {
                                                tempUsed.setAmount(new BigDecimal(0));
                                            } else {
                                                tempUsed.setAmount(endUsedBudget);
                                            }
                                        }
                                        listAddendumAmount.add(tempUsed);
                                    }
                                }
                            } else {
                                ListAddendumAmount tempUsed =
                                    new ListAddendumAmount();
                                tempUsed.setPromoProdukId(pid);
                                tempUsed.setBudgetCustId(custId);
                                tempUsed.setBudgetById(budgetid);
                                BigDecimal total =
                                    new BigDecimal(erCekData.getAttribute("Amount").toString());
                                BigDecimal totalOverBudget =
                                    new BigDecimal(erCekData.getAttribute("OverBudgetAmt") ==
                                                   null ? "0" :
                                                   erCekData.getAttribute("OverBudgetAmt").toString());
                                tempUsed.setOverBudgetAmt(totalOverBudget);
                                tempUsed.setAmount(total);
                                listAddendumAmount.add(tempUsed);
                            }
                            mapOfSum.put(custId, budgetAsToDateUsed);
                            listAddendumBudget.add(ar);
                        }

                        ///// insert table for consumtion report ////////
                        ProdRealSummaryViewImpl vo =
                            confirmationAM.getProdRealSummaryView1();
                        List<ListAddendumAmount> realTempend =
                            getListAddendumAmount();
                        for (ListAddendumAmount setRealsum : realTempend) {
                            Row cvrt = vo.createRow();
                            String promProdIdAmnt =
                                setRealsum.getPromoProdukId().toString();
                            String BudgetCustId =
                                setRealsum.getBudgetCustId().toString();
                            BigDecimal Amount =
                                setRealsum.getAmount() == null ?
                                BigDecimal.ZERO : setRealsum.getAmount();
                            cvrt.setAttribute("ProposalNo", ProposalNo);
                            cvrt.setAttribute("PromoProdukId",
                                              new Number(Float.parseFloat(promProdIdAmnt)));
                            cvrt.setAttribute("BudgetCustId", BudgetCustId);
                            cvrt.setAttribute("Amount", Amount);
                            cvrt.setAttribute("Status", "Closed");
                            List<realisasiTempClass> realTemp =
                                getRealisasiTemp();
                            for (realisasiTempClass realSet : realTemp) {
                                String ppidReal =
                                    realSet.getPROMO_PRODUK_ID().toString();
                                System.out.println(realSet.getPROMO_PRODUK_ID().toString());
                                System.out.println(realSet.getAMOUNTMF().toString());
                                if (promProdIdAmnt.equalsIgnoreCase(ppidReal)) {
                                    oracle.jbo.domain.Number ot =
                                        new Number(Float.parseFloat(realSet.getAMOUNTOT().toString()));
                                    oracle.jbo.domain.Number mf =
                                        new Number(Float.parseFloat(realSet.getAMOUNTMF().toString()));
                                    cvrt.setAttribute("RealisasiOt", ot);
                                    cvrt.setAttribute("RealisasiMf", mf);
                                    cvrt.setAttribute("RealisasiType",
                                                      realSet.getREALISASI_TYPE());
                                }
                            }
                        }
                        confirmationAM.getDBTransaction().commit();
                        vo.executeQuery();
                        for (ListAddendumAmount cek : realTempend) {
                            String budgetCustIdFdi = "";
                            BigDecimal amountSubs = BigDecimal.ZERO;
                            BigDecimal amountOverSubs = BigDecimal.ZERO;
                            budgetCustIdFdi = cek.getBudgetCustId();
                            amountSubs = cek.getAmount();
                            amountOverSubs =
                                    new BigDecimal(cek.getOverBudgetAmt().toString() ==
                                                   null ? "0" :
                                                   cek.getOverBudgetAmt().toString());
                            if (amountSubs.compareTo(BigDecimal.ZERO) > 0) {
                                try {
                                    Context ctx1 = new InitialContext();
                                    DataSource ds1 =
                                        (DataSource)ctx1.lookup("jdbc/focusppDS");
                                    conn = ds1.getConnection();
                                    conn.setAutoCommit(false);
                                    PreparedStatement statement =
                                        conn.prepareStatement("select BUDGETCUSTOMER.BUDGET_CUSTOMER_ID ,BUDGETCUSTOMER.BUDGET_AS_TO_DATE, BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED,BUDGETCUSTOMER.YEARLY_BUDGET_AMOUNT,BUDGETCUSTOMER.OVER_BUDGET from BUDGET_CUSTOMER BudgetCustomer where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID=? for update");
                                    statement.setString(1, budgetCustIdFdi);
                                    ResultSet rs = statement.executeQuery();
                                    while (rs.next()) {
                                        String BudgetCustId =
                                            rs.getString("BUDGET_CUSTOMER_ID").toString();
                                        BigDecimal budgetAsTodateCur =
                                            BigDecimal.ZERO;
                                        BigDecimal budgetAsTodate =
                                            BigDecimal.ZERO;
                                        BigDecimal overBudgetAmountCur =
                                            BigDecimal.ZERO;
                                        budgetAsTodateCur =
                                                new BigDecimal(rs.getString("BUDGET_AS_TO_DATE_USED").toString());
                                        budgetAsTodate =
                                                new BigDecimal(rs.getString("BUDGET_AS_TO_DATE").toString());
                                        overBudgetAmountCur =
                                                new BigDecimal(rs.getString("OVER_BUDGET") ==
                                                               null ? "0" :
                                                               rs.getString("OVER_BUDGET").toString());
                                        if (budgetCustIdFdi.equalsIgnoreCase(BudgetCustId)) {
                                            BigDecimal value =
                                                budgetAsTodateCur.subtract(amountSubs);
                                            BigDecimal overValue =
                                                overBudgetAmountCur.subtract(amountOverSubs);
                                            try {
                                                PreparedStatement updateTtfNumSeq =
                                                    conn.prepareStatement("UPDATE BUDGET_CUSTOMER BudgetCustomer SET BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED =? ,BUDGETCUSTOMER.OVER_BUDGET=? where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID = ?");
                                                updateTtfNumSeq.setBigDecimal(1,
                                                                              value);
                                                updateTtfNumSeq.setBigDecimal(2,
                                                                              overValue);
                                                updateTtfNumSeq.setString(3,
                                                                          budgetCustIdFdi);
                                                updateTtfNumSeq.executeUpdate();
                                                updateTtfNumSeq.close();
                                            } catch (SQLException sqle) {
                                                System.out.println("------------------------------------------------");
                                                System.out.println("ERROR: Cannot run update query");
                                                System.out.println("STACK: " +
                                                                   sqle.toString().trim());
                                                System.out.println("------------------------------------------------");
                                            }
                                        }
                                    }
                                    conn.commit();
                                    statement.close();
                                    conn.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        List<ListAddendumBudget> ab = getListAddendumBudget();
                        for (ListAddendumBudget x : ab) {
                            String budgetByid =
                                x.getBudgetById() == null ? "" :
                                x.getBudgetById().toString();
                            DCIteratorBinding dciterCust =
                                ADFUtils.findIterator("ProdBudgetByView1Iterator");
                            RowSetIterator BudCust =
                                dciterCust.getRowSetIterator();
                            dciterCust.executeQuery();
                            ViewObject voTableData =
                                dciterCust.getViewObject();
                            while (voTableData.hasNext()) {
                                Row rowSelected = voTableData.next();
                                String budgetId =
                                    rowSelected.getAttribute("BudgetById") ==
                                    null ? "" :
                                    rowSelected.getAttribute("BudgetById").toString();
                                if (budgetId.equalsIgnoreCase(budgetByid)) {
                                    rowSelected.setAttribute("Status", "Y");
                                }
                            }
                            dciterCust.getDataControl().commitTransaction();
                            BudCust.closeRowSetIterator();
                        }

                        // Set CLOSE PC
                        AttributeBinding statusPropAttr =
                            (AttributeBinding)bindings.getControlBinding("Status1");
                        statusPropAttr.setInputValue("CLOSED");
                    } else {
                        JSFUtils.addFacesErrorMessage("Dokumen PC masih dalam periode promosi tidak dapat di-close.");
                    }
                }
            } else {
                // Set CLOSE PC Potongan + On Invoice
                if (mekPenagihan.equalsIgnoreCase(onInvoice) &&
                    DiscountType.equalsIgnoreCase(discTypePotongan)) {

                    String updateStatusMod = modifierUpdateCloseToEbs(confirmNo,addendumKe,"0");
                    
                    if (updateStatusMod.equalsIgnoreCase(responSuccess)) {
                        Connection conn = null;
                        HashMap<String, BigDecimal> mapOfSum =
                            new HashMap<String, BigDecimal>();
                        BigDecimal budgetAsToDateUsed = BigDecimal.ZERO;
                        checkAddendumBudgetClearViewImpl budgetsubs =
                            confirmationAM.getcheckAddendumBudgetClearView1();
                        budgetsubs.setNamedWhereClauseParam("confirmNO",
                                                            confirmNo);
                        budgetsubs.executeQuery();
    
                        checkProdukApprovalModifierOnInImpl budgetcekProdukApproval =
                            confirmationAM.getcheckProdukApprovalModifierOnIn1();
                        budgetcekProdukApproval.setNamedWhereClauseParam("pNo",
                                                                         ProposalNo);
                        budgetcekProdukApproval.executeQuery();
                        if(budgetcekProdukApproval.getEstimatedRowCount()>0){
                        
                        while (budgetcekProdukApproval.hasNext()) {
                            Row CekProdApp = budgetcekProdukApproval.next();
                            String ppidref =
                                CekProdApp.getAttribute("PpidRef").toString();
                            String prodAppStatus =
                                CekProdApp.getAttribute("ProductApproval").toString();
                            while (budgetsubs.hasNext()) {
                                Row erCekData = budgetsubs.next();
                                ListAddendumBudget ar = new ListAddendumBudget();
                                String pid =
                                    erCekData.getAttribute("PromoProdukId").toString();
                                if (pid.equalsIgnoreCase(ppidref) &&
                                    prodAppStatus.equalsIgnoreCase("N")) {
                                    //no action
                                } else {
                                    String custId =
                                        erCekData.getAttribute("BudgetCustId").toString();
                                    String budgetid =
                                        erCekData.getAttribute("BudgetById").toString();
                                    ar.setBudgetCustId(custId);
                                    ar.setPromoProdukId(pid);
                                    ar.setBudgetById(budgetid);
                                    List<realisasiTempClass> realTemp =
                                        getRealisasiTemp();
                                    if (realTemp.size() > 0) {
                                        for (realisasiTempClass realBud : realTemp) {
                                            if (pid.equalsIgnoreCase(realBud.getPROMO_PRODUK_ID().toString())) {
                                                ListAddendumAmount tempUsed =
                                                    new ListAddendumAmount();
                                                tempUsed.setPromoProdukId(pid);
                                                tempUsed.setBudgetCustId(custId);
                                                tempUsed.setBudgetById(budgetid);
                                                BigDecimal total =
                                                    new BigDecimal(erCekData.getAttribute("Amount").toString());
                                                BigDecimal totalOverBudget =
                                                    new BigDecimal(erCekData.getAttribute("OverBudgetAmt") ==
                                                                   null ? "0" :
                                                                   erCekData.getAttribute("OverBudgetAmt").toString());
                                                tempUsed.setOverBudgetAmt(totalOverBudget);
                                                BigDecimal realamountmf =
                                                    realBud.getAMOUNTMF();
                                                if (realamountmf.compareTo(total) <
                                                    0 ||
                                                    realamountmf.compareTo(total) ==
                                                    0) {
                                                    BigDecimal selisihUsedBudget =
                                                        total.subtract(realamountmf);
                                                    tempUsed.setAmount(selisihUsedBudget);
                                                } else {
                                                    BigDecimal selisihUsedBudget =
                                                        realamountmf.subtract(total);
                                                    BigDecimal endUsedBudget =
                                                        selisihUsedBudget.subtract(total);
                                                    realBud.setAMOUNTMF(selisihUsedBudget);
                                                    if (endUsedBudget.compareTo(new BigDecimal(0)) <
                                                        0) {
                                                        tempUsed.setAmount(new BigDecimal(0));
                                                    } else {
                                                        tempUsed.setAmount(endUsedBudget);
                                                    }
                                                }
                                                listAddendumAmount.add(tempUsed);
                                            }
                                        }
                                    } else {
                                        ListAddendumAmount tempUsed =
                                            new ListAddendumAmount();
                                        tempUsed.setPromoProdukId(pid);
                                        tempUsed.setBudgetCustId(custId);
                                        tempUsed.setBudgetById(budgetid);
                                        BigDecimal total =
                                            new BigDecimal(erCekData.getAttribute("Amount").toString());
                                        BigDecimal totalOverBudget =
                                            new BigDecimal(erCekData.getAttribute("OverBudgetAmt") ==
                                                           null ? "0" :
                                                           erCekData.getAttribute("OverBudgetAmt").toString());
                                        tempUsed.setOverBudgetAmt(totalOverBudget);
                                        tempUsed.setAmount(total);
                                        listAddendumAmount.add(tempUsed);
                                    }
                                    mapOfSum.put(custId, budgetAsToDateUsed);
                                    listAddendumBudget.add(ar);
                                }
                            }
                        }
                        }else{
                            while (budgetsubs.hasNext()) {
                                Row erCekData = budgetsubs.next();
                                ListAddendumBudget ar = new ListAddendumBudget();
                                String pid =
                                    erCekData.getAttribute("PromoProdukId").toString();
                                    String custId =
                                        erCekData.getAttribute("BudgetCustId").toString();
                                    String budgetid =
                                        erCekData.getAttribute("BudgetById").toString();
                                    ar.setBudgetCustId(custId);
                                    ar.setPromoProdukId(pid);
                                    ar.setBudgetById(budgetid);
                                List<realisasiTempClass> realTemp =
                                    getRealisasiTemp();
                                if (realTemp.size() > 0) {
                                    for (realisasiTempClass realBud : realTemp) {
                                        if (pid.equalsIgnoreCase(realBud.getPROMO_PRODUK_ID().toString())) {
                                            ListAddendumAmount tempUsed =
                                                new ListAddendumAmount();
                                            tempUsed.setPromoProdukId(pid);
                                            tempUsed.setBudgetCustId(custId);
                                            tempUsed.setBudgetById(budgetid);
                                            BigDecimal total =
                                                new BigDecimal(erCekData.getAttribute("Amount").toString());
                                            BigDecimal totalOverBudget =
                                                new BigDecimal(erCekData.getAttribute("OverBudgetAmt") ==
                                                               null ? "0" :
                                                               erCekData.getAttribute("OverBudgetAmt").toString());
                                            tempUsed.setOverBudgetAmt(totalOverBudget);
                                            BigDecimal realamountmf =
                                                realBud.getAMOUNTMF();
                                            if (realamountmf.compareTo(total) <
                                                0 ||
                                                realamountmf.compareTo(total) ==
                                                0) {
                                                BigDecimal selisihUsedBudget =
                                                    total.subtract(realamountmf);
                                                tempUsed.setAmount(selisihUsedBudget);
                                            } else {
                                                BigDecimal selisihUsedBudget =
                                                    realamountmf.subtract(total);
                                                BigDecimal endUsedBudget =
                                                    selisihUsedBudget.subtract(total);
                                                realBud.setAMOUNTMF(selisihUsedBudget);
                                                if (endUsedBudget.compareTo(new BigDecimal(0)) <
                                                    0) {
                                                    tempUsed.setAmount(new BigDecimal(0));
                                                } else {
                                                    tempUsed.setAmount(endUsedBudget);
                                                }
                                            }
                                            listAddendumAmount.add(tempUsed);
                                        }
                                    }
                                } else {
                                    ListAddendumAmount tempUsed =
                                        new ListAddendumAmount();
                                    tempUsed.setPromoProdukId(pid);
                                    tempUsed.setBudgetCustId(custId);
                                    tempUsed.setBudgetById(budgetid);
                                    BigDecimal total =
                                        new BigDecimal(erCekData.getAttribute("Amount").toString());
                                    BigDecimal totalOverBudget =
                                        new BigDecimal(erCekData.getAttribute("OverBudgetAmt") ==
                                                       null ? "0" :
                                                       erCekData.getAttribute("OverBudgetAmt").toString());
                                    tempUsed.setOverBudgetAmt(totalOverBudget);
                                    tempUsed.setAmount(total);
                                    listAddendumAmount.add(tempUsed);
                                }
                                mapOfSum.put(custId, budgetAsToDateUsed);
                                listAddendumBudget.add(ar);
                            }
                        }
                        ///// insert table for consumtion report ////////
                        ProdRealSummaryViewImpl vo =
                            confirmationAM.getProdRealSummaryView1();
                        List<ListAddendumAmount> realTempend =
                            getListAddendumAmount();
                        for (ListAddendumAmount setRealsum : realTempend) {
                            Row cvrt = vo.createRow();
                            String promProdIdAmnt =
                                setRealsum.getPromoProdukId().toString();
                            String BudgetCustId =
                                setRealsum.getBudgetCustId().toString();
                            BigDecimal Amount =
                                setRealsum.getAmount() == null ? BigDecimal.ZERO :
                                setRealsum.getAmount();
                            cvrt.setAttribute("ProposalNo", ProposalNo);
                            cvrt.setAttribute("PromoProdukId",
                                              new Number(Float.parseFloat(promProdIdAmnt)));
                            cvrt.setAttribute("BudgetCustId", BudgetCustId);
                            cvrt.setAttribute("Amount", Amount);
                            cvrt.setAttribute("Status", "Closed");
                        }
                        confirmationAM.getDBTransaction().commit();
                        vo.executeQuery();
    
                        for (ListAddendumAmount cek : realTempend) {
                            String budgetCustIdFdi = "";
                            BigDecimal amountSubs = BigDecimal.ZERO;
                            BigDecimal amountOverSubs = BigDecimal.ZERO;
                            budgetCustIdFdi = cek.getBudgetCustId();
                            amountSubs = cek.getAmount();
                            amountOverSubs =
                                    new BigDecimal(cek.getOverBudgetAmt().toString() ==
                                                   null ? "0" :
                                                   cek.getOverBudgetAmt().toString());
                            if (amountSubs.compareTo(BigDecimal.ZERO) > 0) {
                                try {
                                    Context ctx1 = new InitialContext();
                                    DataSource ds1 =
                                        (DataSource)ctx1.lookup("jdbc/focusppDS");
                                    conn = ds1.getConnection();
                                    conn.setAutoCommit(false);
                                    PreparedStatement statement =
                                        conn.prepareStatement("select BUDGETCUSTOMER.BUDGET_CUSTOMER_ID ,BUDGETCUSTOMER.BUDGET_AS_TO_DATE, BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED,BUDGETCUSTOMER.YEARLY_BUDGET_AMOUNT,BUDGETCUSTOMER.OVER_BUDGET from BUDGET_CUSTOMER BudgetCustomer where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID=? for update");
                                    statement.setString(1, budgetCustIdFdi);
                                    ResultSet rs = statement.executeQuery();
                                    while (rs.next()) {
                                        String BudgetCustId =
                                            rs.getString("BUDGET_CUSTOMER_ID").toString();
                                        BigDecimal budgetAsTodateCur =
                                            BigDecimal.ZERO;
                                        BigDecimal budgetAsTodate =
                                            BigDecimal.ZERO;
                                        BigDecimal overBudgetAmountCur =
                                            BigDecimal.ZERO;
                                        budgetAsTodateCur =
                                                new BigDecimal(rs.getString("BUDGET_AS_TO_DATE_USED").toString());
                                        budgetAsTodate =
                                                new BigDecimal(rs.getString("BUDGET_AS_TO_DATE").toString());
                                        overBudgetAmountCur =
                                                new BigDecimal(rs.getString("OVER_BUDGET") ==
                                                               null ? "0" :
                                                               rs.getString("OVER_BUDGET").toString());
                                        if (budgetCustIdFdi.equalsIgnoreCase(BudgetCustId)) {
                                            BigDecimal value =
                                                budgetAsTodateCur.subtract(amountSubs);
                                            BigDecimal overValue =
                                                overBudgetAmountCur.subtract(amountOverSubs);
                                            try {
                                                PreparedStatement updateTtfNumSeq =
                                                    conn.prepareStatement("UPDATE BUDGET_CUSTOMER BudgetCustomer SET BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED =? ,BUDGETCUSTOMER.OVER_BUDGET=? where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID = ?");
                                                updateTtfNumSeq.setBigDecimal(1,
                                                                              value);
                                                updateTtfNumSeq.setBigDecimal(2,
                                                                              overValue);
                                                updateTtfNumSeq.setString(3,
                                                                          budgetCustIdFdi);
                                                updateTtfNumSeq.executeUpdate();
                                                updateTtfNumSeq.close();
                                            } catch (SQLException sqle) {
                                                System.out.println("------------------------------------------------");
                                                System.out.println("ERROR: Cannot run update query");
                                                System.out.println("STACK: " +
                                                                   sqle.toString().trim());
                                                System.out.println("------------------------------------------------");
                                            }
                                        }
                                    }
                                    conn.commit();
                                    statement.close();
                                    conn.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
    
                        List<ListAddendumBudget> ab = getListAddendumBudget();
                        for (ListAddendumBudget x : ab) {
                            String budgetByid =
                                x.getBudgetById() == null ? "" : x.getBudgetById().toString();
                            DCIteratorBinding dciterCust =
                                ADFUtils.findIterator("ProdBudgetByView1Iterator");
                            RowSetIterator BudCust =
                                dciterCust.getRowSetIterator();
                            dciterCust.executeQuery();
                            ViewObject voTableData = dciterCust.getViewObject();
                            while (voTableData.hasNext()) {
                                Row rowSelected = voTableData.next();
                                String budgetId =
                                    rowSelected.getAttribute("BudgetById") ==
                                    null ? "" :
                                    rowSelected.getAttribute("BudgetById").toString();
                                if (budgetId.equalsIgnoreCase(budgetByid)) {
                                    rowSelected.setAttribute("Status", "Y");
                                }
                            }
                            dciterCust.getDataControl().commitTransaction();
                            BudCust.closeRowSetIterator();
                        }
    
                        AttributeBinding statusPropAttr =
                            (AttributeBinding)bindings.getControlBinding("Status1");
                        statusPropAttr.setInputValue("CLOSED");
                    } else {
                        System.out.println("FAILED UPDATE EBS, CONFIRM NO: " + confirmNo);
                        JSFUtils.addFacesWarningMessage("Update status ke EBS gagal, close PC dibatalkan.");
                    }
                } 
                
                // Set CLOSE PC Promo Barang + On Invoice
                else if (mekPenagihan.equalsIgnoreCase(onInvoice) &&
                           DiscountType.equalsIgnoreCase(discTypePromoBarang)) {
                    Connection conn = null;
                    checkAddendumBudgetClearViewImpl budgetsubs =
                        confirmationAM.getcheckAddendumBudgetClearView1();
                    budgetsubs.setNamedWhereClauseParam("confirmNO",
                                                        confirmNo);
                    budgetsubs.executeQuery();

                    checkProdukApprovalModifierOnInImpl budgetcekProdukApproval =
                        confirmationAM.getcheckProdukApprovalModifierOnIn1();
                    budgetcekProdukApproval.setNamedWhereClauseParam("pNo",
                                                                     ProposalNo);
                    budgetcekProdukApproval.executeQuery();
                        if(budgetcekProdukApproval.getEstimatedRowCount()>0){
                        
                        while (budgetcekProdukApproval.hasNext()) {
                            Row CekProdApp = budgetcekProdukApproval.next();
                            String ppidref =
                                CekProdApp.getAttribute("PpidRef").toString();
                            String prodAppStatus =
                                CekProdApp.getAttribute("ProductApproval").toString();
                            while (budgetsubs.hasNext()) {
                                Row erCekData = budgetsubs.next();
                                ListAddendumBudget ar = new ListAddendumBudget();
                                String pid =
                                    erCekData.getAttribute("PromoProdukId").toString();
                                if (pid.equalsIgnoreCase(ppidref) &&
                                    prodAppStatus.equalsIgnoreCase("N")) {
                                    //no action
                                } else {
                                    String custId =
                                        erCekData.getAttribute("BudgetCustId").toString();
                                    String budgetid =
                                        erCekData.getAttribute("BudgetById").toString();
                                    ar.setBudgetCustId(custId);
                                    ar.setPromoProdukId(pid);
                                    ar.setBudgetById(budgetid);
                                    
                                    ListAddendumAmount tempUsed =
                                        new ListAddendumAmount();
                                    tempUsed.setPromoProdukId(pid);
                                    tempUsed.setBudgetCustId(custId);
                                    tempUsed.setBudgetById(budgetid);
                                    BigDecimal total =
                                        new BigDecimal(erCekData.getAttribute("Amount").toString());
                                    tempUsed.setAmount(total);
                                    BigDecimal totalOverBudget =
                                        new BigDecimal(erCekData.getAttribute("OverBudgetAmt") ==
                                                       null ? "0" :
                                                       erCekData.getAttribute("OverBudgetAmt").toString());
                                    tempUsed.setOverBudgetAmt(totalOverBudget);
                                    listAddendumAmount.add(tempUsed);
                                    listAddendumBudget.add(ar);
                                }
                            }
                        }
                        }else{
                            while (budgetsubs.hasNext()) {
                                Row erCekData = budgetsubs.next();
                                ListAddendumBudget ar = new ListAddendumBudget();
                                String pid =
                                    erCekData.getAttribute("PromoProdukId").toString();
                                    String custId =
                                        erCekData.getAttribute("BudgetCustId").toString();
                                    String budgetid =
                                        erCekData.getAttribute("BudgetById").toString();
                                    ar.setBudgetCustId(custId);
                                    ar.setPromoProdukId(pid);
                                    ar.setBudgetById(budgetid);
                                
                                    ListAddendumAmount tempUsed =
                                        new ListAddendumAmount();
                                    tempUsed.setPromoProdukId(pid);
                                    tempUsed.setBudgetCustId(custId);
                                    tempUsed.setBudgetById(budgetid);
                                    BigDecimal total =
                                        new BigDecimal(erCekData.getAttribute("Amount").toString());
                                    tempUsed.setAmount(total);
                                    BigDecimal totalOverBudget =
                                        new BigDecimal(erCekData.getAttribute("OverBudgetAmt") ==
                                                       null ? "0" :
                                                       erCekData.getAttribute("OverBudgetAmt").toString());
                                    tempUsed.setOverBudgetAmt(totalOverBudget);
                                    listAddendumAmount.add(tempUsed);
                                    listAddendumBudget.add(ar);
                            }
                        }
                    ///// insert table for consumtion report ////////
                    ProdRealSummaryViewImpl vo =
                        confirmationAM.getProdRealSummaryView1();
                    List<ListAddendumAmount> realTempend =
                        getListAddendumAmount();
                    for (ListAddendumAmount setRealsum : realTempend) {
                        Row cvrt = vo.createRow();
                        String promProdIdAmnt =
                            setRealsum.getPromoProdukId().toString();
                        String BudgetCustId =
                            setRealsum.getBudgetCustId().toString();
                        BigDecimal Amount =
                            setRealsum.getAmount() == null ? BigDecimal.ZERO :
                            setRealsum.getAmount();
                        cvrt.setAttribute("ProposalNo", ProposalNo);
                        cvrt.setAttribute("PromoProdukId",
                                          new Number(Float.parseFloat(promProdIdAmnt)));
                        cvrt.setAttribute("BudgetCustId", BudgetCustId);
                        cvrt.setAttribute("Amount", Amount);
                        cvrt.setAttribute("Status", "Closed");
                    }
                    confirmationAM.getDBTransaction().commit();
                    vo.executeQuery();

                    for (ListAddendumAmount cek : realTempend) {
                        String budgetCustIdFdi = "";
                        BigDecimal amountSubs = BigDecimal.ZERO;
                        BigDecimal amountOverSubs = BigDecimal.ZERO;
                        budgetCustIdFdi = cek.getBudgetCustId();
                        amountSubs = cek.getAmount();
                        amountOverSubs =
                                new BigDecimal(cek.getOverBudgetAmt().toString() ==
                                               null ? "0" :
                                               cek.getOverBudgetAmt().toString());
                        if (amountSubs.compareTo(BigDecimal.ZERO) > 0) {
                            try {
                                Context ctx1 = new InitialContext();
                                DataSource ds1 =
                                    (DataSource)ctx1.lookup("jdbc/focusppDS");
                                conn = ds1.getConnection();
                                conn.setAutoCommit(false);
                                PreparedStatement statement =
                                    conn.prepareStatement("select BUDGETCUSTOMER.BUDGET_CUSTOMER_ID ,BUDGETCUSTOMER.BUDGET_AS_TO_DATE, BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED,BUDGETCUSTOMER.YEARLY_BUDGET_AMOUNT,BUDGETCUSTOMER.OVER_BUDGET from BUDGET_CUSTOMER BudgetCustomer where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID=? for update");
                                statement.setString(1, budgetCustIdFdi);
                                ResultSet rs = statement.executeQuery();
                                while (rs.next()) {
                                    String BudgetCustId =
                                        rs.getString("BUDGET_CUSTOMER_ID").toString();
                                    BigDecimal budgetAsTodateCur =
                                        BigDecimal.ZERO;
                                    BigDecimal budgetAsTodate =
                                        BigDecimal.ZERO;
                                    BigDecimal overBudgetAmountCur =
                                        BigDecimal.ZERO;
                                    budgetAsTodateCur =
                                            new BigDecimal(rs.getString("BUDGET_AS_TO_DATE_USED").toString());
                                    budgetAsTodate =
                                            new BigDecimal(rs.getString("BUDGET_AS_TO_DATE").toString());
                                    overBudgetAmountCur =
                                            new BigDecimal(rs.getString("OVER_BUDGET") ==
                                                           null ? "0" :
                                                           rs.getString("OVER_BUDGET").toString());
                                    if (budgetCustIdFdi.equalsIgnoreCase(BudgetCustId)) {
                                        BigDecimal value =
                                            budgetAsTodateCur.subtract(amountSubs);
                                        BigDecimal overValue =
                                            overBudgetAmountCur.subtract(amountOverSubs);
                                        try {
                                            PreparedStatement updateTtfNumSeq =
                                                conn.prepareStatement("UPDATE BUDGET_CUSTOMER BudgetCustomer SET BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED =? ,BUDGETCUSTOMER.OVER_BUDGET=? where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID = ?");
                                            updateTtfNumSeq.setBigDecimal(1,
                                                                          value);
                                            updateTtfNumSeq.setBigDecimal(2,
                                                                          overValue);
                                            updateTtfNumSeq.setString(3,
                                                                      budgetCustIdFdi);
                                            updateTtfNumSeq.executeUpdate();
                                            updateTtfNumSeq.close();
                                        } catch (SQLException sqle) {
                                            System.out.println("------------------------------------------------");
                                            System.out.println("ERROR: Cannot run update query");
                                            System.out.println("STACK: " +
                                                               sqle.toString().trim());
                                            System.out.println("------------------------------------------------");
                                        }
                                    }
                                }
                                conn.commit();
                                statement.close();
                                conn.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    List<ListAddendumBudget> ab = getListAddendumBudget();
                    for (ListAddendumBudget x : ab) {
                        String budgetByid =
                            x.getBudgetById() == null ? "" : x.getBudgetById().toString();
                        DCIteratorBinding dciterCust =
                            ADFUtils.findIterator("ProdBudgetByView1Iterator");
                        RowSetIterator BudCust =
                            dciterCust.getRowSetIterator();
                        dciterCust.executeQuery();
                        ViewObject voTableData = dciterCust.getViewObject();
                        while (voTableData.hasNext()) {
                            Row rowSelected = voTableData.next();
                            String budgetId =
                                rowSelected.getAttribute("BudgetById") ==
                                null ? "" :
                                rowSelected.getAttribute("BudgetById").toString();
                            if (budgetId.equalsIgnoreCase(budgetByid)) {
                                rowSelected.setAttribute("Status", "Y");
                            }
                        }
                        dciterCust.getDataControl().commitTransaction();
                        BudCust.closeRowSetIterator();
                    }

                    AttributeBinding statusPropAttr =
                        (AttributeBinding)bindings.getControlBinding("Status1");
                    statusPropAttr.setInputValue("CLOSED");
                } 
                
                // Set CLOSE PC else than Promo Barang + Potongan
                else {
                    checkAddendumBudgetClearViewImpl budgetsubs =
                        confirmationAM.getcheckAddendumBudgetClearView1();
                    budgetsubs.setNamedWhereClauseParam("confirmNO",
                                                        confirmNo);
                    budgetsubs.executeQuery();

                    ProposalUpdateConfirmAdendumViewImpl voProposalConfirm =
                        (ProposalUpdateConfirmAdendumViewImpl)confirmationAM.getProposalUpdateConfirmAdendumView1();
                    voProposalConfirm.setWhereClause("Proposal.CONFIRM_NO = '" +
                                                     confirmNo + "'");
                    voProposalConfirm.executeQuery();

                    listAddendumBudget = new ArrayList<ListAddendumBudget>();
                    listAddendumAmount = new ArrayList<ListAddendumAmount>();
                    HashMap<String, BigDecimal> mapOfSum =
                        new HashMap<String, BigDecimal>();
                    BigDecimal budgetAsToDateUsed = BigDecimal.ZERO;
                    Connection conn = null;
                    checkProdukApprovalModifierOnInImpl budgetcekProdukApproval =
                        confirmationAM.getcheckProdukApprovalModifierOnIn1();
                    budgetcekProdukApproval.setNamedWhereClauseParam("pNo",
                                                                     ProposalNo);
                    budgetcekProdukApproval.executeQuery();
                    ///
                    while (budgetsubs.hasNext()) {
                        Row erCekData = budgetsubs.next();
                        ListAddendumBudget ar = new ListAddendumBudget();
                        String custId =
                            erCekData.getAttribute("BudgetCustId").toString();
                        String pid =
                            erCekData.getAttribute("PromoProdukId").toString();
                        String budgetid =
                            erCekData.getAttribute("BudgetById").toString();
                        ar.setBudgetCustId(custId);
                        ar.setPromoProdukId(pid);
                        ar.setBudgetById(budgetid);
                        List<realisasiTempClass> realTemp = getRealisasiTemp();
                        if (realTemp.size() > 0) {
                            for (realisasiTempClass realBud : realTemp) {
                                if (pid.equalsIgnoreCase(realBud.getPROMO_PRODUK_ID().toString())) {
                                    ListAddendumAmount tempUsed =
                                        new ListAddendumAmount();
                                    tempUsed.setPromoProdukId(pid);
                                    tempUsed.setBudgetCustId(custId);
                                    tempUsed.setBudgetById(budgetid);
                                    BigDecimal total =
                                        new BigDecimal(erCekData.getAttribute("Amount").toString());
                                    BigDecimal totalOverBudget =
                                        new BigDecimal(erCekData.getAttribute("OverBudgetAmt") ==
                                                       null ? "0" :
                                                       erCekData.getAttribute("OverBudgetAmt").toString());
                                    tempUsed.setOverBudgetAmt(totalOverBudget);
                                    BigDecimal realamountmf =
                                        realBud.getAMOUNTMF();
                                    if (realamountmf.compareTo(total) < 0 ||
                                        realamountmf.compareTo(total) == 0) {
                                        BigDecimal selisihUsedBudget =
                                            total.subtract(realamountmf);
                                        tempUsed.setAmount(selisihUsedBudget);
                                    } else {
                                        BigDecimal selisihUsedBudget =
                                            realamountmf.subtract(total);
                                        BigDecimal endUsedBudget =
                                            selisihUsedBudget.subtract(total);
                                        realBud.setAMOUNTMF(selisihUsedBudget);
                                        if (endUsedBudget.compareTo(new BigDecimal(0)) <
                                            0) {
                                            tempUsed.setAmount(new BigDecimal(0));
                                        } else {
                                            tempUsed.setAmount(endUsedBudget);
                                        }
                                    }
                                    listAddendumAmount.add(tempUsed);
                                }
                            }
                        } else {
                            ListAddendumAmount tempUsed =
                                new ListAddendumAmount();
                            tempUsed.setPromoProdukId(pid);
                            tempUsed.setBudgetCustId(custId);
                            tempUsed.setBudgetById(budgetid);
                            BigDecimal total =
                                new BigDecimal(erCekData.getAttribute("Amount").toString());
                            BigDecimal totalOverBudget =
                                new BigDecimal(erCekData.getAttribute("OverBudgetAmt") ==
                                               null ? "0" :
                                               erCekData.getAttribute("OverBudgetAmt").toString());
                            tempUsed.setOverBudgetAmt(totalOverBudget);
                            tempUsed.setAmount(total);
                            listAddendumAmount.add(tempUsed);
                        }
                        mapOfSum.put(custId, budgetAsToDateUsed);
                        listAddendumBudget.add(ar);
                    }
                    ///// insert table for consumtion report ////////
                    ProdRealSummaryViewImpl vo =
                        confirmationAM.getProdRealSummaryView1();
                    List<ListAddendumAmount> realTempend =
                        getListAddendumAmount();
                    for (ListAddendumAmount setRealsum : realTempend) {
                        Row cvrt = vo.createRow();
                        String promProdIdAmnt =
                            setRealsum.getPromoProdukId().toString();
                        String BudgetCustId =
                            setRealsum.getBudgetCustId().toString();
                        BigDecimal Amount =
                            setRealsum.getAmount() == null ? BigDecimal.ZERO :
                            setRealsum.getAmount();
                        cvrt.setAttribute("ProposalNo", ProposalNo);
                        cvrt.setAttribute("PromoProdukId",
                                          new Number(Float.parseFloat(promProdIdAmnt)));
                        cvrt.setAttribute("BudgetCustId", BudgetCustId);
                        cvrt.setAttribute("Amount", Amount);
                        cvrt.setAttribute("Status", "Closed");
                        List<realisasiTempClass> realTemp = getRealisasiTemp();
                        for (realisasiTempClass realSet : realTemp) {
                            String ppidReal =
                                realSet.getPROMO_PRODUK_ID().toString();
                            System.out.println(realSet.getPROMO_PRODUK_ID().toString());
                            System.out.println(realSet.getAMOUNTMF().toString());
                            if (promProdIdAmnt.equalsIgnoreCase(ppidReal)) {
                                oracle.jbo.domain.Number ot =
                                    new Number(Float.parseFloat(realSet.getAMOUNTOT().toString()));
                                oracle.jbo.domain.Number mf =
                                    new Number(Float.parseFloat(realSet.getAMOUNTMF().toString()));
                                cvrt.setAttribute("RealisasiOt", ot);
                                cvrt.setAttribute("RealisasiMf", mf);
                                cvrt.setAttribute("RealisasiType",
                                                  realSet.getREALISASI_TYPE());
                            }
                        }
                    }
                    confirmationAM.getDBTransaction().commit();
                    vo.executeQuery();

                    for (ListAddendumAmount cek : realTempend) {
                        String budgetCustIdFdi = "";
                        BigDecimal amountSubs = BigDecimal.ZERO;
                        BigDecimal amountOverSubs = BigDecimal.ZERO;
                        budgetCustIdFdi = cek.getBudgetCustId();
                        amountSubs = cek.getAmount();
                        amountOverSubs =
                                new BigDecimal(cek.getOverBudgetAmt().toString() ==
                                               null ? "0" :
                                               cek.getOverBudgetAmt().toString());
                        if (amountSubs.compareTo(BigDecimal.ZERO) > 0) {
                            try {
                                Context ctx1 = new InitialContext();
                                DataSource ds1 =
                                    (DataSource)ctx1.lookup("jdbc/focusppDS");
                                conn = ds1.getConnection();
                                conn.setAutoCommit(false);
                                PreparedStatement statement =
                                    conn.prepareStatement("select BUDGETCUSTOMER.BUDGET_CUSTOMER_ID ,BUDGETCUSTOMER.BUDGET_AS_TO_DATE, BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED,BUDGETCUSTOMER.YEARLY_BUDGET_AMOUNT,BUDGETCUSTOMER.OVER_BUDGET from BUDGET_CUSTOMER BudgetCustomer where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID=? for update");
                                statement.setString(1, budgetCustIdFdi);
                                ResultSet rs = statement.executeQuery();
                                while (rs.next()) {
                                    String BudgetCustId =
                                        rs.getString("BUDGET_CUSTOMER_ID").toString();
                                    BigDecimal budgetAsTodateCur =
                                        BigDecimal.ZERO;
                                    BigDecimal budgetAsTodate =
                                        BigDecimal.ZERO;
                                    BigDecimal overBudgetAmountCur =
                                        BigDecimal.ZERO;
                                    budgetAsTodateCur =
                                            new BigDecimal(rs.getString("BUDGET_AS_TO_DATE_USED").toString());
                                    budgetAsTodate =
                                            new BigDecimal(rs.getString("BUDGET_AS_TO_DATE").toString());
                                    overBudgetAmountCur =
                                            new BigDecimal(rs.getString("OVER_BUDGET") ==
                                                           null ? "0" :
                                                           rs.getString("OVER_BUDGET").toString());
                                    if (budgetCustIdFdi.equalsIgnoreCase(BudgetCustId)) {
                                        BigDecimal value =
                                            budgetAsTodateCur.subtract(amountSubs);
                                        BigDecimal overValue =
                                            overBudgetAmountCur.subtract(amountOverSubs);
                                        try {
                                            PreparedStatement updateTtfNumSeq =
                                                conn.prepareStatement("UPDATE BUDGET_CUSTOMER BudgetCustomer SET BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED =? ,BUDGETCUSTOMER.OVER_BUDGET=? where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID = ?");
                                            updateTtfNumSeq.setBigDecimal(1,
                                                                          value);
                                            updateTtfNumSeq.setBigDecimal(2,
                                                                          overValue);
                                            updateTtfNumSeq.setString(3,
                                                                      budgetCustIdFdi);
                                            updateTtfNumSeq.executeUpdate();
                                            updateTtfNumSeq.close();
                                        } catch (SQLException sqle) {
                                            System.out.println("------------------------------------------------");
                                            System.out.println("ERROR: Cannot run update query");
                                            System.out.println("STACK: " +
                                                               sqle.toString().trim());
                                            System.out.println("------------------------------------------------");
                                        }
                                    }
                                }
                                conn.commit();
                                statement.close();
                                conn.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    List<ListAddendumBudget> ab = getListAddendumBudget();
                    for (ListAddendumBudget x : ab) {
                        String budgetByid =
                            x.getBudgetById() == null ? "" : x.getBudgetById().toString();
                        DCIteratorBinding dciterCust =
                            ADFUtils.findIterator("ProdBudgetByView1Iterator");
                        RowSetIterator BudCust =
                            dciterCust.getRowSetIterator();
                        dciterCust.executeQuery();
                        ViewObject voTableData = dciterCust.getViewObject();
                        while (voTableData.hasNext()) {
                            Row rowSelected = voTableData.next();
                            String budgetId =
                                rowSelected.getAttribute("BudgetById") ==
                                null ? "" :
                                rowSelected.getAttribute("BudgetById").toString();
                            if (budgetId.equalsIgnoreCase(budgetByid)) {
                                rowSelected.setAttribute("Status", "Y");
                            }
                        }
                        dciterCust.getDataControl().commitTransaction();
                        BudCust.closeRowSetIterator();
                    }
                    AttributeBinding statusPropAttr =
                        (AttributeBinding)bindings.getControlBinding("Status1");
                    statusPropAttr.setInputValue("CLOSED");
                }
            }

            OperationBinding operationBinding =
                bindings.getOperationBinding("Commit");
            operationBinding.execute();

            OperationBinding executeListConf =
                bindings.getOperationBinding("ExecuteProposalConf");
            executeListConf.execute();

            AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropConfirmation);
            AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
        }
    }

    private Number getApprovalPathId() {
        Number apvPathId = null;
        ConfirmationAMImpl confirmationAM =
            (ConfirmationAMImpl)ADFUtils.getApplicationModuleForDataControl("ConfirmationAMDataControl");

        FcsApprovalPathViewImpl approvalPathView =
            confirmationAM.getFcsApprovalPathView1();
        approvalPathView.executeQuery();

        long pathExists = approvalPathView.getEstimatedRowCount();

        if (pathExists > 0) {
            Row approvalPathRow = approvalPathView.first();
            apvPathId =
                    (Number)approvalPathRow.getAttribute("PositionStructureId");
        } else {
            JSFUtils.addFacesWarningMessage("\"Position Structure Id\" tidak ditemukan, \"Approval Path Id\" set null.");
        }

        return apvPathId;
    }

    public void showPDetailProduct(ActionEvent actionEvent) {
        BindingContainer bindings = getBindings();

        AttributeBinding DiscountTypeAttr =
            (AttributeBinding)bindings.getControlBinding("DiscountType1");
        String DiscountType =
            DiscountTypeAttr.getInputValue().toString();

        if (DiscountType.equalsIgnoreCase(discTypeBiaya)) {
            DCIteratorBinding dciterBiaya =
                ADFUtils.findIterator("BiayaView1Iterator");
            dciterBiaya.executeQuery();
        }
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
            ADFUtils.findIterator("ProposalConfirmationView1Iterator");
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

    public void createModifierHoExcl(ConfirmationAMImpl confirmationAM,
                                     Number headerIdNum, String userName,
                                     String noConfirmId) {

        // Fetch Modifier Exclude HO
        FcsModifierHoExclViewImpl voModifierHoExcl =
            confirmationAM.getFcsModifierHoExclView1();
        voModifierHoExcl.setNamedWhereClauseParam("noConfirm", noConfirmId);
        voModifierHoExcl.executeQuery();
        
        if (voModifierHoExcl.getEstimatedRowCount() > 0) {
            FcsQpModifierTempExclViewImpl voModifierTempExcl =
                (FcsQpModifierTempExclViewImpl)confirmationAM.getFcsQpModifierTempExclView1();

            while (voModifierHoExcl.hasNext()) {
                Row modifierHoRow = voModifierHoExcl.next();
                String noConfirm =
                    (String)modifierHoRow.getAttribute("NoConfirm");
                String currency =
                    (String)modifierHoRow.getAttribute("Currency");
                Date startDate = (Date)modifierHoRow.getAttribute("StartDate");
                Date endDate = (Date)modifierHoRow.getAttribute("EndDate");
                String active = (String)modifierHoRow.getAttribute("Active");
                String automatic =
                    (String)modifierHoRow.getAttribute("Automatic");
                String lineLevel =
                    (String)modifierHoRow.getAttribute("LineLevel");
                String lineType =
                    (String)modifierHoRow.getAttribute("LineType");
                Date startDateLine =
                    (Date)modifierHoRow.getAttribute("StartDateLine");
                Date endDateLine =
                    (Date)modifierHoRow.getAttribute("EndDateLine");
                String automaticLine =
                    (String)modifierHoRow.getAttribute("AutomaticLine");
                String pricingPhase =
                    (String)modifierHoRow.getAttribute("PricingPhase");
                String productAttribute =
                    (String)modifierHoRow.getAttribute("ProductAttribute");
                String productValue =
                    (String)modifierHoRow.getAttribute("ProductValue");
                String volumeType =
                    (String)modifierHoRow.getAttribute("VolumeType");
                String breakType =
                    (String)modifierHoRow.getAttribute("BreakType");
                String uom = (String)modifierHoRow.getAttribute("Uom");
                Number valueFrom =
                    (Number)modifierHoRow.getAttribute("ValueFrom");
                Number valueTo = (Number)modifierHoRow.getAttribute("ValueTo");
                String applicationMethod =
                    (String)modifierHoRow.getAttribute("ApplicationMethod");
                Number value = (Number)modifierHoRow.getAttribute("Value");
                Number groupingNo =
                    (Number)modifierHoRow.getAttribute("GroupingNo");
                String qualifierContext =
                    (String)modifierHoRow.getAttribute("QualifierContext");
                String qualifierAttr =
                    (String)modifierHoRow.getAttribute("QualifierAttr");
                String operatorSign =
                    (String)modifierHoRow.getAttribute("OperatorSign");
                String valueQualifier =
                    (String)modifierHoRow.getAttribute("ValueQualifier");
                String valueConfirmNoDef =
                    (String)modifierHoRow.getAttribute("ConfirmNoDef");
                Number LineNum = (Number)modifierHoRow.getAttribute("LineNum");

                Row modifierTempExclRow = voModifierTempExcl.createRow();
                modifierTempExclRow.setAttribute("ModifierType",
                                                 "Discount List");
                modifierTempExclRow.setAttribute("Name", noConfirm);
                modifierTempExclRow.setAttribute("Description", noConfirm);
                modifierTempExclRow.setAttribute("Currency", currency);
                modifierTempExclRow.setAttribute("StartDate", startDate);
                modifierTempExclRow.setAttribute("EndDate", endDate);
                modifierTempExclRow.setAttribute("Active", active);
                modifierTempExclRow.setAttribute("Automatic", automatic);
                modifierTempExclRow.setAttribute("Version", null);
                modifierTempExclRow.setAttribute("LineLevel", lineLevel);
                modifierTempExclRow.setAttribute("LineType", lineType);
                modifierTempExclRow.setAttribute("StartDateLine",
                                                 startDateLine);
                modifierTempExclRow.setAttribute("EndDateLine", endDateLine);
                modifierTempExclRow.setAttribute("AutomaticLine",
                                                 automaticLine);
                modifierTempExclRow.setAttribute("PricingPhase", pricingPhase);
                modifierTempExclRow.setAttribute("ProductAttribute",
                                                 productAttribute);
                modifierTempExclRow.setAttribute("ProductAttributeValue",
                                                 productValue);
                modifierTempExclRow.setAttribute("VolumeType", volumeType);
                modifierTempExclRow.setAttribute("BreakType", breakType);
                modifierTempExclRow.setAttribute("Uom", uom);
                modifierTempExclRow.setAttribute("ValueFrom", valueFrom);
                modifierTempExclRow.setAttribute("ValueTo", valueTo);
                modifierTempExclRow.setAttribute("ApplicationMethod",
                                                 applicationMethod);
                modifierTempExclRow.setAttribute("Value", value);
                modifierTempExclRow.setAttribute("GroupingNo", groupingNo);
                modifierTempExclRow.setAttribute("QualifierContext",
                                                 qualifierContext);
                modifierTempExclRow.setAttribute("QualifierAttr",
                                                 qualifierAttr);
                modifierTempExclRow.setAttribute("Attribute2",
                                                 valueConfirmNoDef);
                modifierTempExclRow.setAttribute("Operator1", operatorSign);
                modifierTempExclRow.setAttribute("QValueFrom", valueQualifier);
                modifierTempExclRow.setAttribute("Flag", "N");
                modifierTempExclRow.setAttribute("ListHeaderId", headerIdNum);
                modifierTempExclRow.setAttribute("LineNo", LineNum);
                modifierTempExclRow.setAttribute("CreatedBy", userName);
            }
        }
    }

    public void createModifierAreaExcl(ConfirmationAMImpl confirmationAM,
                                       Number headerIdNum, String userName,
                                       String noConfirmId) {
        // Fetch Modifier Exclude Area
        FcsModifierAreaExclViewImpl voModifierAreaExcl =
            confirmationAM.getFcsModifierAreaExclView1();
        voModifierAreaExcl.setNamedWhereClauseParam("noConfirm", noConfirmId);
        voModifierAreaExcl.executeQuery();

        if (voModifierAreaExcl.getEstimatedRowCount() > 0) {
            FcsQpModifierTempExclViewImpl voModifierTempExcl =
                (FcsQpModifierTempExclViewImpl)confirmationAM.getFcsQpModifierTempExclView1();

            while (voModifierAreaExcl.hasNext()) {
                Row modifierAreaExclRow = voModifierAreaExcl.next();
                String noConfirm =
                    (String)modifierAreaExclRow.getAttribute("NoConfirm");
                String currency =
                    (String)modifierAreaExclRow.getAttribute("Currency");
                Date startDate =
                    (Date)modifierAreaExclRow.getAttribute("StartDate");
                Date endDate =
                    (Date)modifierAreaExclRow.getAttribute("EndDate");
                String active =
                    (String)modifierAreaExclRow.getAttribute("Active");
                String automatic =
                    (String)modifierAreaExclRow.getAttribute("Automatic");
                String lineLevel =
                    (String)modifierAreaExclRow.getAttribute("LineLevel");
                String lineType =
                    (String)modifierAreaExclRow.getAttribute("LineType");
                Date startDateLine =
                    (Date)modifierAreaExclRow.getAttribute("StartDateLine");
                Date endDateLine =
                    (Date)modifierAreaExclRow.getAttribute("EndDateLine");
                String automaticLine =
                    (String)modifierAreaExclRow.getAttribute("AutomaticLine");
                String pricingPhase =
                    (String)modifierAreaExclRow.getAttribute("PricingPhase");
                String productAttribute =
                    (String)modifierAreaExclRow.getAttribute("ProductAttribute");
                String productValue =
                    (String)modifierAreaExclRow.getAttribute("ProductValue");
                String volumeType =
                    (String)modifierAreaExclRow.getAttribute("VolumeType");
                String breakType =
                    (String)modifierAreaExclRow.getAttribute("BreakType");
                String uom = (String)modifierAreaExclRow.getAttribute("Uom");
                Number valueFrom =
                    (Number)modifierAreaExclRow.getAttribute("ValueFrom");
                Number valueTo =
                    (Number)modifierAreaExclRow.getAttribute("ValueTo");
                String applicationMethod =
                    (String)modifierAreaExclRow.getAttribute("ApplicationMethod");
                Number value =
                    (Number)modifierAreaExclRow.getAttribute("Value");
                Number groupingNo =
                    (Number)modifierAreaExclRow.getAttribute("GroupingNo");
                String qualifierContext =
                    (String)modifierAreaExclRow.getAttribute("QualifierContext");
                String qualifierAttr =
                    (String)modifierAreaExclRow.getAttribute("QualifierAttr");
                String operatorSign =
                    (String)modifierAreaExclRow.getAttribute("OperatorSign");
                String valueQualifier =
                    (String)modifierAreaExclRow.getAttribute("ValueQualifier");
                String valueConfirmNoDef =
                    (String)modifierAreaExclRow.getAttribute("ConfirmNoDef");
                Number LineNum =
                    (Number)modifierAreaExclRow.getAttribute("LineNum");

                Row modifierTempExclRow = voModifierTempExcl.createRow();
                modifierTempExclRow.setAttribute("ModifierType",
                                                 "Discount List");
                modifierTempExclRow.setAttribute("Name", noConfirm);
                modifierTempExclRow.setAttribute("Description", noConfirm);
                modifierTempExclRow.setAttribute("Currency", currency);
                modifierTempExclRow.setAttribute("StartDate", startDate);
                modifierTempExclRow.setAttribute("EndDate", endDate);
                modifierTempExclRow.setAttribute("Active", active);
                modifierTempExclRow.setAttribute("Automatic", automatic);
                modifierTempExclRow.setAttribute("Version", null);
                modifierTempExclRow.setAttribute("LineLevel", lineLevel);
                modifierTempExclRow.setAttribute("LineType", lineType);
                modifierTempExclRow.setAttribute("StartDateLine",
                                                 startDateLine);
                modifierTempExclRow.setAttribute("EndDateLine", endDateLine);
                modifierTempExclRow.setAttribute("AutomaticLine",
                                                 automaticLine);
                modifierTempExclRow.setAttribute("PricingPhase", pricingPhase);
                modifierTempExclRow.setAttribute("ProductAttribute",
                                                 productAttribute);
                modifierTempExclRow.setAttribute("ProductAttributeValue",
                                                 productValue);
                modifierTempExclRow.setAttribute("VolumeType", volumeType);
                modifierTempExclRow.setAttribute("BreakType", breakType);
                modifierTempExclRow.setAttribute("Uom", uom);
                modifierTempExclRow.setAttribute("ValueFrom", valueFrom);
                modifierTempExclRow.setAttribute("ValueTo", valueTo);
                modifierTempExclRow.setAttribute("ApplicationMethod",
                                                 applicationMethod);
                modifierTempExclRow.setAttribute("Value", value);
                modifierTempExclRow.setAttribute("GroupingNo", groupingNo);
                modifierTempExclRow.setAttribute("QualifierContext",
                                                 qualifierContext);
                modifierTempExclRow.setAttribute("QualifierAttr",
                                                 qualifierAttr);
                modifierTempExclRow.setAttribute("Attribute2",
                                                 valueConfirmNoDef);
                modifierTempExclRow.setAttribute("Operator1", operatorSign);
                modifierTempExclRow.setAttribute("QValueFrom", valueQualifier);
                modifierTempExclRow.setAttribute("Flag", "N");
                modifierTempExclRow.setAttribute("ListHeaderId", headerIdNum);
                modifierTempExclRow.setAttribute("LineNo", LineNum);
                modifierTempExclRow.setAttribute("CreatedBy", userName);
            }
        }
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

        AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropConfirmation);

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

    public void tableFilterProcessQuery(QueryEvent queryEvent) {
        /*
        FilterableQueryDescriptor fqd =
            (FilterableQueryDescriptor)queryEvent.getDescriptor();
        ConjunctionCriterion conjunctionCriterion =
            fqd.getConjunctionCriterion();
        List<Criterion> criterionList =
            conjunctionCriterion.getCriterionList();
        for (Criterion criterion : criterionList) {
            AttributeDescriptor attrDescriptor =
                ((AttributeCriterion)criterion).getAttribute();
            Object value = ((AttributeCriterion)criterion).getValues();
            if (attrDescriptor.getType().equals(String.class)) {
                if (value != null) {
                    ((AttributeCriterion) criterion).setValue("%" + value + "%");
                }
            }
        }
        */

        //Execute query
        ADFUtils.invokeEL("#{bindings.ProposalConfirmationView11Query.processQuery}",
                          new Class[] { QueryEvent.class },
                          new Object[] { queryEvent });

        AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
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

    public void doubleClickCust(ClientEvent clientEvent) {
        Row rowSelected = getTableRow("KomBudgetCustLov1Iterator");
        DCIteratorBinding dciterProdBudgetByCustID =
            ADFUtils.findIterator("ProdBudgetByCustView1Iterator");
        DCIteratorBinding dciterProdBudgetByCust =
            ADFUtils.findIterator("ProdBudgetByCustView1Iterator");
        Row r = dciterProdBudgetByCust.getCurrentRow();
        Row row = dciterProdBudgetByCustID.getCurrentRow();
        String idbudget = row.getAttribute("BudgetById").toString();
        if (rowSelected != null) {
            String idUpdate = row.getAttribute("BudgetById").toString();
            if (idbudget.equalsIgnoreCase(idUpdate)) {
                r.setAttribute("BudgetCustId",
                               getRow(rowSelected, "BudgetCustomerId"));
                r.setAttribute("KombinasiBudget",
                               getRow(rowSelected, "CombinationDescription"));
                itKombBudgetProdCust.setSubmittedValue(getRow(rowSelected,
                                                              "CombinationDescription"));
                budgetCustIdCust.setSubmittedValue(getRow(rowSelected,
                                                          "BudgetCustomerId").toString());
                AdfFacesContext.getCurrentInstance().addPartialTarget(itKombBudgetProdCust);
                AdfFacesContext.getCurrentInstance().addPartialTarget(budgetCustIdCust);
            }
            AdfFacesContext.getCurrentInstance().addPartialTarget(tblBudgetCustomer);
            RichPopup panggilPopup = this.getPcustomerBudget();
            panggilPopup.hide();
        } else {
            System.out.println("Tidak ada data pada tabel");
        }
    }

    public void doubleClickPost(ClientEvent clientEvent) {
        Row rowSelected = getTableRow("KomBudgetPostLov1Iterator");
        DCIteratorBinding dciterProdBudgetByPostID =
            ADFUtils.findIterator("ProdBudgetByPostView1Iterator");
        DCIteratorBinding dciterProdBudgetByPost =
            ADFUtils.findIterator("ProdBudgetByPostView1Iterator");
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
                                                              "CombinationDescription"));
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
    }

    public void dialogPostPopupLov(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome() == DialogEvent.Outcome.ok) {
            Row rowSelected = getTableRow("KomBudgetPostLov1Iterator");
            DCIteratorBinding dciterProdBudgetByPostID =
                ADFUtils.findIterator("ProdBudgetByPostView1Iterator");
            DCIteratorBinding dciterProdBudgetByPost =
                ADFUtils.findIterator("ProdBudgetByPostView1Iterator");
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
            Row r = dciterProdBudgetByCust.getCurrentRow();
            Row row = dciterProdBudgetByCustID.getCurrentRow();
            String idbudget = row.getAttribute("BudgetById").toString();
            if (rowSelected != null) {
                String idUpdate = row.getAttribute("BudgetById").toString();
                if (idbudget.equalsIgnoreCase(idUpdate)) {
                    r.setAttribute("BudgetCustId",
                                   getRow(rowSelected, "BudgetCustomerId"));
                    r.setAttribute("KombinasiBudget",
                                   getRow(rowSelected, "CombinationDescription"));
                    itKombBudgetProdCust.setSubmittedValue(getRow(rowSelected,
                                                                  "CombinationDescription").toString());
                    budgetCustIdCust.setSubmittedValue(getRow(rowSelected,
                                                              "BudgetCustomerId").toString());
                    AdfFacesContext.getCurrentInstance().addPartialTarget(itKombBudgetProdCust);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(budgetCustIdCust);
                }
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

    public void removedialoglistener(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome() == DialogEvent.Outcome.ok) {
            String budgetByChoice = itLovBudgetBy.getValue().toString();
            BindingContainer bindings =
                BindingContext.getCurrent().getCurrentBindingsEntry();

            ConfirmationAMImpl confirmationAM =
                (ConfirmationAMImpl)ADFUtils.getApplicationModuleForDataControl("ConfirmationAMDataControl");

            AttributeBinding confirmNoAttr =
                (AttributeBinding)bindings.getControlBinding("ConfirmNo");
            String confirmNo = (String)confirmNoAttr.getInputValue();
            Connection conn = null;
            if (budgetByChoice.equalsIgnoreCase("CUSTOMER")) {
                String budgetCustIdFdi = "";
                int Checkrowstatus = 0;
                BigDecimal amountSubs = BigDecimal.ZERO;
                BigDecimal amountOverSubs = BigDecimal.ZERO;
                DCIteratorBinding dciterCust =
                    ADFUtils.findIterator("ProdBudgetByCustView1Iterator");
                ViewObject voTableData = dciterCust.getViewObject();
                Row rowSelected = voTableData.getCurrentRow();
                budgetCustIdFdi =
                        rowSelected.getAttribute("BudgetCustId").toString();
                String promoProdId =
                    rowSelected.getAttribute("PromoProdukId") == null ? "" :
                    rowSelected.getAttribute("PromoProdukId").toString();
                amountSubs =
                        new BigDecimal(rowSelected.getAttribute("Amount").toString());
                Checkrowstatus =
                        Integer.parseInt(rowSelected.getAttribute("CheckRowStatus").toString());
                String StatusOver =
                    rowSelected.getAttribute("FlagStatusOver") == null ? "" :
                    rowSelected.getAttribute("FlagStatusOver").toString();
                amountOverSubs =
                        new BigDecimal(rowSelected.getAttribute("OverBudgetAmt") ==
                                       null ? "0" :
                                       rowSelected.getAttribute("OverBudgetAmt").toString());

                checkOverBudgetClearViewImpl budgetOversub =
                    confirmationAM.getcheckOverBudgetClearView1();
                budgetOversub.setNamedWhereClauseParam("confirmNO", confirmNo);
                budgetOversub.executeQuery();

                getPPIDRefViewImpl ppIdRef =
                    confirmationAM.getgetPPIDRefView1();
                ppIdRef.setNamedWhereClauseParam("promoProduk", promoProdId);
                ppIdRef.executeQuery();

                String okeppid = "";
                for (Row re : ppIdRef.getAllRowsInRange()) {
                    okeppid = re.getAttribute("PpidRef").toString();
                }

                if (rowSelected.getAttribute("BudgetCustId") != null) {
                    if (Checkrowstatus == 1 || Checkrowstatus == 2) {
                        try {
                            Context ctx = new InitialContext();
                            DataSource ds = (DataSource)ctx.lookup(dsFdiConn);
                            conn = ds.getConnection();
                            conn.setAutoCommit(false);
                            PreparedStatement statement =
                                conn.prepareStatement("select BUDGETCUSTOMER.BUDGET_CUSTOMER_ID ,BUDGETCUSTOMER.BUDGET_AS_TO_DATE, BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED, BUDGETCUSTOMER.YEARLY_BUDGET_AMOUNT, BUDGETCUSTOMER.OVER_BUDGET from BUDGET_CUSTOMER BudgetCustomer where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID=? for update");
                            statement.setString(1, budgetCustIdFdi);
                            ResultSet rs = statement.executeQuery();
                            while (rs.next()) {
                                String BudgetCustId =
                                    rs.getString("BUDGET_CUSTOMER_ID").toString();
                                BigDecimal budgetAsTodateCur = BigDecimal.ZERO;
                                BigDecimal budgetAsTodate = BigDecimal.ZERO;
                                BigDecimal overBudgetAmountCur =
                                    BigDecimal.ZERO;
                                overBudgetAmountCur =
                                        new BigDecimal(rs.getString("OVER_BUDGET") ==
                                                       null ? "0" :
                                                       rs.getString("OVER_BUDGET").toString());
                                budgetAsTodateCur =
                                        new BigDecimal(rs.getString("BUDGET_AS_TO_DATE_USED").toString());
                                budgetAsTodate =
                                        new BigDecimal(rs.getString("BUDGET_AS_TO_DATE").toString());
                                if (budgetAsTodateCur.compareTo(BigDecimal.ZERO) ==
                                    0) {
                                    //DO NOTHING
                                    //JSFUtils.addFacesWarningMessage("BudgetAsToDateUsed Belum Terisi");
                                } else {
                                    BigDecimal value =
                                        budgetAsTodateCur.subtract(amountSubs);
                                    if (budgetCustIdFdi.equalsIgnoreCase(BudgetCustId)) {
                                        try {
                                            PreparedStatement updateTtfNumSeq =
                                                conn.prepareStatement("UPDATE BUDGET_CUSTOMER BudgetCustomer SET BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED =? where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID = ?");
                                            updateTtfNumSeq.setBigDecimal(1,
                                                                          value);
                                            updateTtfNumSeq.setString(2,
                                                                      budgetCustIdFdi);
                                            updateTtfNumSeq.executeUpdate();
                                            updateTtfNumSeq.close();
                                        } catch (SQLException sqle) {
                                            System.out.println("------------------------------------------------");
                                            System.out.println("ERROR: Cannot run update query");
                                            System.out.println("STACK: " +
                                                               sqle.toString().trim());
                                            System.out.println("------------------------------------------------");
                                        }

                                        if (StatusOver.equalsIgnoreCase("Y")) {
                                            BigDecimal overValueNew =
                                                overBudgetAmountCur.subtract(amountOverSubs);
                                            try {
                                                PreparedStatement updateTtfNumSeq =
                                                    conn.prepareStatement("UPDATE BUDGET_CUSTOMER BudgetCustomer SET BUDGETCUSTOMER.OVER_BUDGET=? where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID = ?");
                                                updateTtfNumSeq.setBigDecimal(1,
                                                                              overValueNew);
                                                updateTtfNumSeq.setString(2,
                                                                          budgetCustIdFdi);
                                                updateTtfNumSeq.executeUpdate();
                                                updateTtfNumSeq.close();
                                            } catch (SQLException sqle) {
                                                System.out.println("------------------------------------------------");
                                                System.out.println("ERROR: Cannot run update query");
                                                System.out.println("STACK: " +
                                                                   sqle.toString().trim());
                                                System.out.println("------------------------------------------------");
                                            }
                                        }
                                    }
                                }
                            }
                            PreparedStatement statement2 = null;
                            if (budgetOversub.getEstimatedRowCount() > 0) {
                                for (Row er :
                                     budgetOversub.getAllRowsInRange()) {
                                    String ppId =
                                        er.getAttribute("PromoProdukId").toString();
                                    String BCId =
                                        er.getAttribute("BudgetCustId").toString();
                                    statement2 =
                                            conn.prepareStatement("select BUDGETCUSTOMER.BUDGET_CUSTOMER_ID, BUDGETCUSTOMER.OVER_BUDGET from BUDGET_CUSTOMER BudgetCustomer where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID=? for update");
                                    statement2.setString(1, BCId);
                                    ResultSet rs2 = statement2.executeQuery();
                                    while (rs2.next()) {
                                        String BudgetCustId =
                                            rs2.getString("BUDGET_CUSTOMER_ID").toString();
                                        if (BudgetCustId.equalsIgnoreCase(BCId)) {
                                            BigDecimal overBudgetAmountCursub =
                                                BigDecimal.ZERO;
                                            overBudgetAmountCursub =
                                                    new BigDecimal(rs2.getString("OVER_BUDGET") ==
                                                                   null ? "0" :
                                                                   rs2.getString("OVER_BUDGET").toString());
                                            if (okeppid.equalsIgnoreCase(ppId)) {
                                                BigDecimal overValueBack =
                                                    overBudgetAmountCursub.add(amountOverSubs);
                                                try {
                                                    PreparedStatement updateTtfNumSeq =
                                                        conn.prepareStatement("UPDATE BUDGET_CUSTOMER BudgetCustomer SET BUDGETCUSTOMER.OVER_BUDGET=? where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID = ?");
                                                    updateTtfNumSeq.setBigDecimal(1,
                                                                                  overValueBack);
                                                    updateTtfNumSeq.setString(2,
                                                                              BCId);
                                                    updateTtfNumSeq.executeUpdate();
                                                    updateTtfNumSeq.close();
                                                } catch (SQLException sqle) {
                                                    System.out.println("------------------------------------------------");
                                                    System.out.println("ERROR: Cannot run update query");
                                                    System.out.println("STACK: " +
                                                                       sqle.toString().trim());
                                                    System.out.println("------------------------------------------------");
                                                }
                                            }
                                        }

                                    }
                                    conn.commit();
                                    statement2.close();
                                    conn.close();
                                }
                            }
                            conn.commit();
                            statement.close();
                            conn.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        rowSelected.remove();
                        dciterCust.getDataControl().commitTransaction();
                        dciterCust.executeQuery();
                        voTableData.clearCache();
                        AdfFacesContext.getCurrentInstance().addPartialTarget(itLovBudgetBy);

                    } else {
                        rowSelected.remove();
                        voTableData.clearCache();
                    }
                }
                AdfFacesContext.getCurrentInstance().addPartialTarget(tblBudgetCustomer);
                potmessageRemove.hide();
            }
        } else {
            potmessageRemove.hide();
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

    public void removedialoglistenerPost(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome() == DialogEvent.Outcome.ok) {
            String budgetByChoice = itLovBudgetBy.getValue().toString();
            BindingContainer bindings =
                BindingContext.getCurrent().getCurrentBindingsEntry();

            ConfirmationAMImpl confirmationAM =
                (ConfirmationAMImpl)ADFUtils.getApplicationModuleForDataControl("ConfirmationAMDataControl");

            AttributeBinding confirmNoAttr =
                (AttributeBinding)bindings.getControlBinding("ConfirmNo");
            String confirmNo = (String)confirmNoAttr.getInputValue();
            Connection conn = null;
            if (budgetByChoice.equalsIgnoreCase("POSTING")) {
                String budgetCustIdFdi = "";
                int Checkrowstatus = 0;
                BigDecimal amountSubs = BigDecimal.ZERO;
                BigDecimal amountOverSubs = BigDecimal.ZERO;
                DCIteratorBinding dciterCust =
                    ADFUtils.findIterator("ProdBudgetByPostView1Iterator");
                ViewObject voTableData = dciterCust.getViewObject();
                Row rowSelected = voTableData.getCurrentRow();
                String promoProdId =
                    rowSelected.getAttribute("PromoProdukId") == null ? "" :
                    rowSelected.getAttribute("PromoProdukId").toString();
                budgetCustIdFdi =
                        rowSelected.getAttribute("BudgetCustId") == null ? "" :
                        rowSelected.getAttribute("BudgetCustId").toString();
                amountSubs =
                        new BigDecimal(rowSelected.getAttribute("Amount") ==
                                       null ? "0" :
                                       rowSelected.getAttribute("Amount").toString());
                Checkrowstatus =
                        Integer.parseInt(rowSelected.getAttribute("CheckRowStatus").toString());
                String StatusOver =
                    rowSelected.getAttribute("FlagStatusOver") == null ? "" :
                    rowSelected.getAttribute("FlagStatusOver").toString();
                amountOverSubs =
                        new BigDecimal(rowSelected.getAttribute("OverBudgetAmt") ==
                                       null ? "0" :
                                       rowSelected.getAttribute("OverBudgetAmt").toString());

                checkOverBudgetClearViewImpl budgetOversub =
                    confirmationAM.getcheckOverBudgetClearView1();
                budgetOversub.setNamedWhereClauseParam("confirmNO", confirmNo);
                budgetOversub.executeQuery();

                getPPIDRefViewImpl ppIdRef =
                    confirmationAM.getgetPPIDRefView1();
                ppIdRef.setNamedWhereClauseParam("promoProduk", promoProdId);
                ppIdRef.executeQuery();

                String okeppid = "";
                for (Row re : ppIdRef.getAllRowsInRange()) {
                    okeppid = re.getAttribute("PpidRef").toString();
                }
                if (rowSelected.getAttribute("BudgetCustId") != null) {
                    if (Checkrowstatus == 1 || Checkrowstatus == 2) {
                        try {
                            Context ctx = new InitialContext();
                            DataSource ds = (DataSource)ctx.lookup(dsFdiConn);
                            conn = ds.getConnection();
                            conn.setAutoCommit(false);
                            PreparedStatement statement =
                                conn.prepareStatement("select BUDGETCUSTOMER.BUDGET_CUSTOMER_ID ,BUDGETCUSTOMER.BUDGET_AS_TO_DATE, BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED,BUDGETCUSTOMER.YEARLY_BUDGET_AMOUNT, BUDGETCUSTOMER.OVER_BUDGET from BUDGET_CUSTOMER BudgetCustomer where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID=? for update");
                            statement.setString(1, budgetCustIdFdi);
                            ResultSet rs = statement.executeQuery();
                            while (rs.next()) {
                                String BudgetCustId =
                                    rs.getString("BUDGET_CUSTOMER_ID").toString();
                                BigDecimal budgetAsTodateCur = BigDecimal.ZERO;
                                BigDecimal budgetAsTodate = BigDecimal.ZERO;
                                BigDecimal overBudgetAmountCur =
                                    BigDecimal.ZERO;
                                overBudgetAmountCur =
                                        new BigDecimal(rs.getString("OVER_BUDGET") ==
                                                       null ? "0" :
                                                       rs.getString("OVER_BUDGET").toString());
                                budgetAsTodateCur =
                                        new BigDecimal(rs.getString("BUDGET_AS_TO_DATE_USED").toString());
                                budgetAsTodate =
                                        new BigDecimal(rs.getString("BUDGET_AS_TO_DATE").toString());
                                if (budgetAsTodateCur.compareTo(BigDecimal.ZERO) ==
                                    0) {
                                    //DO NOTHING
                                    //JSFUtils.addFacesWarningMessage("BudgetAsToDateUsed Belum Terisi");
                                } else {
                                    BigDecimal value =
                                        budgetAsTodateCur.subtract(amountSubs);
                                    if (budgetCustIdFdi.equalsIgnoreCase(BudgetCustId)) {
                                        try {
                                            PreparedStatement updateTtfNumSeq =
                                                conn.prepareStatement("UPDATE BUDGET_CUSTOMER BudgetCustomer SET BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED =? where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID = ?");
                                            updateTtfNumSeq.setBigDecimal(1,
                                                                          value);
                                            updateTtfNumSeq.setString(2,
                                                                      budgetCustIdFdi);
                                            updateTtfNumSeq.executeUpdate();
                                            updateTtfNumSeq.close();
                                        } catch (SQLException sqle) {
                                            System.out.println("------------------------------------------------");
                                            System.out.println("ERROR: Cannot run update query");
                                            System.out.println("STACK: " +
                                                               sqle.toString().trim());
                                            System.out.println("------------------------------------------------");
                                        }
                                        
                                        if (StatusOver.equalsIgnoreCase("Y")) {
                                            BigDecimal overValueNew =
                                                overBudgetAmountCur.subtract(amountOverSubs);
                                            try {
                                                PreparedStatement updateTtfNumSeq =
                                                    conn.prepareStatement("UPDATE BUDGET_CUSTOMER BudgetCustomer SET BUDGETCUSTOMER.OVER_BUDGET=? where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID = ?");
                                                updateTtfNumSeq.setBigDecimal(1,
                                                                              overValueNew);
                                                updateTtfNumSeq.setString(2,
                                                                          budgetCustIdFdi);
                                                updateTtfNumSeq.executeUpdate();
                                                updateTtfNumSeq.close();
                                            } catch (SQLException sqle) {
                                                System.out.println("------------------------------------------------");
                                                System.out.println("ERROR: Cannot run update query");
                                                System.out.println("STACK: " +
                                                                   sqle.toString().trim());
                                                System.out.println("------------------------------------------------");
                                            }

                                        }
                                    }
                                }
                            }
                            PreparedStatement statement2 = null;
                            for (Row er : budgetOversub.getAllRowsInRange()) {
                                String ppId =
                                    er.getAttribute("PromoProdukId").toString();
                                String BCId =
                                    er.getAttribute("BudgetCustId").toString();
                                statement2 =
                                        conn.prepareStatement("select BUDGETCUSTOMER.BUDGET_CUSTOMER_ID, BUDGETCUSTOMER.OVER_BUDGET from BUDGET_CUSTOMER BudgetCustomer where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID=? for update");
                                statement2.setString(1, BCId);
                                ResultSet rs2 = statement2.executeQuery();
                                while (rs2.next()) {
                                    String BudgetCustId =
                                        rs2.getString("BUDGET_CUSTOMER_ID").toString();
                                    if (BudgetCustId.equalsIgnoreCase(BCId)) {
                                        BigDecimal overBudgetAmountCursub =
                                            BigDecimal.ZERO;
                                        overBudgetAmountCursub =
                                                new BigDecimal(rs2.getString("OVER_BUDGET") ==
                                                               null ? "0" :
                                                               rs2.getString("OVER_BUDGET").toString());
                                        if (okeppid.equalsIgnoreCase(ppId)) {
                                            BigDecimal overValueBack =
                                                overBudgetAmountCursub.add(amountOverSubs);
                                            try {
                                                PreparedStatement updateTtfNumSeq =
                                                    conn.prepareStatement("UPDATE BUDGET_CUSTOMER BudgetCustomer SET BUDGETCUSTOMER.OVER_BUDGET=? where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID = ?");
                                                updateTtfNumSeq.setBigDecimal(1,
                                                                              overValueBack);
                                                updateTtfNumSeq.setString(2,
                                                                          BCId);
                                                updateTtfNumSeq.executeUpdate();
                                                updateTtfNumSeq.close();
                                            } catch (SQLException sqle) {
                                                System.out.println("------------------------------------------------");
                                                System.out.println("ERROR: Cannot run update query");
                                                System.out.println("STACK: " +
                                                                   sqle.toString().trim());
                                                System.out.println("------------------------------------------------");
                                            }
                                        }
                                    }
                                }
                                conn.commit();
                                statement2.close();
                                conn.close();
                            }
                            conn.commit();
                            statement.close();
                            conn.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        rowSelected.remove();
                        dciterCust.getDataControl().commitTransaction();
                        dciterCust.executeQuery();
                        voTableData.clearCache();
                        AdfFacesContext.getCurrentInstance().addPartialTarget(itLovBudgetBy);
                    } else {
                        rowSelected.remove();
                        itLovBudgetBy.setDisabled(false);
                        AdfFacesContext.getCurrentInstance().addPartialTarget(itLovBudgetBy);
                        voTableData.clearCache();
                    }
                }
                AdfFacesContext.getCurrentInstance().addPartialTarget(tblBudgetPosting);
                potmessageRemovePost.hide();
            }
        } else {
            potmessageRemovePost.hide();
        }
    }

    public void setOtpesanRemovePost(RichOutputFormatted otpesanRemovePost) {
        this.otpesanRemovePost = otpesanRemovePost;
    }

    public RichOutputFormatted getOtpesanRemovePost() {
        return otpesanRemovePost;
    }

    public void createPrActionListener(ActionEvent actionEvent) {
        DCBindingContainer bindings =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();

        ConfirmationAMImpl confirmationAM =
            (ConfirmationAMImpl)ADFUtils.getApplicationModuleForDataControl("ConfirmationAMDataControl");

        AttributeBinding noPropAttr =
            (AttributeBinding)bindings.getControlBinding("ProposalNo");
        String noProp = (String)noPropAttr.getInputValue();

        AttributeBinding noConfAttr =
            (AttributeBinding)bindings.getControlBinding("ConfirmNo");
        String noConf = (String)noConfAttr.getInputValue();

        //Cek validasi DCV
        CheckCmRealisasiDcvImpl cekDcv =
            confirmationAM.getCheckCmRealisasiDcv1();
        cekDcv.setNamedWhereClauseParam("noPp", noProp);
        cekDcv.executeQuery();

        if (cekDcv.getEstimatedRowCount() == 0) {
            // Cek setiap row produk ada yg punya MF ga?,
            // kalau 1 aja ada yg punya harus cek budget.
            AttributeBinding discTypeAttr =
                (AttributeBinding)bindings.getControlBinding("DiscountType1");
            String discType = (String)discTypeAttr.getInputValue();

            boolean mfExist = false;
            Number totalMf = zeroNum;
            Number totalOnTop = zeroNum;

            DCIteratorBinding dcIterPromoProdForCheck =
                bindings.findIteratorBinding("PromoProdukView1Iterator");

            long rowCount = dcIterPromoProdForCheck.getEstimatedRowCount();
            String prodBudgetWarnMsg = "";

            if (rowCount > 0) {
                int i = 1;
                for (Row r : dcIterPromoProdForCheck.getAllRowsInRange()) {
                    String productApproval =
                        (String)r.getAttribute("ProductApproval");
                    if (productApproval.equalsIgnoreCase("Y")) {
                        Integer checkBudgetData =
                            r.getAttribute("CheckBudgetData") == null ? 0 :
                            (Integer)r.getAttribute("CheckBudgetData");

                        Number budgetPercentTotal =
                            r.getAttribute("BudgetPercentTotal") == null ?
                            zeroNum :
                            (Number)r.getAttribute("BudgetPercentTotal");

                        // Validasi budget sudah terisi atau belum.
                        if (discType.equalsIgnoreCase(discTypePotongan)) {
                            totalMf =
                                    r.getAttribute("DiscMf") == null ? zeroNum :
                                    (Number)r.getAttribute("DiscMf");
                            totalOnTop =
                                    r.getAttribute("DiscOnTop") == null ? zeroNum :
                                    (Number)r.getAttribute("DiscOnTop");
                            mfExist =
                                    r.getAttribute("DiscMf").toString() == "0" ?
                                    false : true;
                            if (mfExist != false) {
                                if (checkBudgetData == 0) {
                                    prodBudgetWarnMsg =
                                            prodBudgetWarnMsg + "Budget belum diisi pada produk baris ke-" +
                                            i + ".<nr>";
                                } else {
                                    if (budgetPercentTotal.compareTo(100) >
                                        0) {
                                        //showPopup("Persentase budget tidak boleh melebihi 100%.", potmessage);
                                        prodBudgetWarnMsg =
                                                prodBudgetWarnMsg + "Persentase budget produk baris ke-" +
                                                i +
                                                " tidak boleh melebihi 100%.<nr>";
                                    } else if (budgetPercentTotal.compareTo(100) <
                                               0) {
                                        //showPopup("Penggunaan budget baru mencapai " + totalPercen + "%, \"Create PR\" tidak dapat dilanjutkan.",  potmessage);
                                        prodBudgetWarnMsg =
                                                prodBudgetWarnMsg + "Penggunaan budget produk baris ke-" +
                                                i + " baru mencapai " +
                                                budgetPercentTotal +
                                                "%, \"Create PR\" tidak dapat dilanjutkan.<nr>";
                                    }
                                }
                            }
                        } else if (discType.equalsIgnoreCase(discTypeBiaya)) {
                            totalMf =
                                    r.getAttribute("BiaMf") == null ? zeroNum :
                                    (Number)r.getAttribute("BiaMf");
                            totalOnTop =
                                    r.getAttribute("BiaOntop") == null ? zeroNum :
                                    (Number)r.getAttribute("BiaOntop");
                            mfExist =
                                    r.getAttribute("BiaMf").toString() == "0" ?
                                    false : true;
                            if (mfExist != false) {
                                if (checkBudgetData == 0) {
                                    prodBudgetWarnMsg =
                                            prodBudgetWarnMsg + "Budget belum diisi pada produk baris ke-" +
                                            i + ".<nr>";
                                } else {
                                    if (budgetPercentTotal.compareTo(100) >
                                        0) {
                                        //showPopup("Persentase budget tidak boleh melebihi 100%.", potmessage);
                                        prodBudgetWarnMsg =
                                                prodBudgetWarnMsg + "Persentase budget produk baris ke-" +
                                                i +
                                                " tidak boleh melebihi 100%.<nr>";
                                    } else if (budgetPercentTotal.compareTo(100) <
                                               0) {
                                        //showPopup("Penggunaan budget baru mencapai " + totalPercen + "%, \"Create PR\" tidak dapat dilanjutkan.",  potmessage);
                                        prodBudgetWarnMsg =
                                                prodBudgetWarnMsg + "Penggunaan budget produk baris ke-" +
                                                i + " baru mencapai " +
                                                budgetPercentTotal +
                                                "%, \"Create PR\" tidak dapat dilanjutkan.<nr>";
                                    }
                                }
                            }
                        } else if (discType.equalsIgnoreCase(discTypePromoBarang)) {
                            totalMf =
                                    r.getAttribute("BrgBonusMf") == null ? zeroNum :
                                    (Number)r.getAttribute("BrgBonusMf");
                            totalOnTop =
                                    r.getAttribute("BrgBonusOnTop") == null ?
                                    zeroNum :
                                    (Number)r.getAttribute("BrgBonusOnTop");
                            mfExist =
                                    r.getAttribute("BrgBonusMf").toString() ==
                                    "0" ? false : true;
                            if (mfExist != false) {
                                if (checkBudgetData == 0) {
                                    prodBudgetWarnMsg =
                                            prodBudgetWarnMsg + "Budget belum diisi pada produk baris ke-" +
                                            i + ".<nr>";
                                } else {
                                    if (budgetPercentTotal.compareTo(100) >
                                        0) {
                                        //showPopup("Persentase budget tidak boleh melebihi 100%.", potmessage);
                                        prodBudgetWarnMsg =
                                                prodBudgetWarnMsg + "Persentase budget produk baris ke-" +
                                                i +
                                                " tidak boleh melebihi 100%.<nr>";
                                    } else if (budgetPercentTotal.compareTo(100) <
                                               0) {
                                        //showPopup("Penggunaan budget baru mencapai " + totalPercen + "%, \"Create PR\" tidak dapat dilanjutkan.",  potmessage);
                                        prodBudgetWarnMsg =
                                                prodBudgetWarnMsg + "Penggunaan budget produk baris ke-" +
                                                i + " baru mencapai " +
                                                budgetPercentTotal +
                                                "%, \"Create PR\" tidak dapat dilanjutkan.<nr>";
                                    }
                                }
                            }
                        }
                    }
                    i = i + 1;
                }
            }

            if (prodBudgetWarnMsg.length() > 0) {
                showPopup(prodBudgetWarnMsg, potmessage);
            } else {
                RichPopup.PopupHints hints = new RichPopup.PopupHints();
                popupCreatePr.show(hints);
            }
        } else {
            AttributeBinding dcvFlagAttr =
                (AttributeBinding)bindings.getControlBinding("DcvFlag");
            dcvFlagAttr.setInputValue(onDcv);
            AdfFacesContext.getCurrentInstance().addPartialTarget(btnCreatePr);
            JSFUtils.addFacesInformationMessage("PC dengan no " + noConf +
                                                " sudah pernah ter-generate DCV");
        }
    }

    public void setPopupCreatePr(RichPopup popupCreatePr) {
        this.popupCreatePr = popupCreatePr;
    }

    public RichPopup getPopupCreatePr() {
        return popupCreatePr;
    }

    public void cancelPcDialogListener(DialogEvent dialogEvent) {
        BindingContainer bindings = getBindings();
        DCIteratorBinding parentIter =
            (DCIteratorBinding)bindings.get("ProposalConfirmationView1Iterator");

        if (dialogEvent.getOutcome().name().equals("ok")) {

            ConfirmationAMImpl confirmationAM =
                (ConfirmationAMImpl)ADFUtils.getApplicationModuleForDataControl("ConfirmationAMDataControl");

            AttributeBinding noPropAttr =
                (AttributeBinding)bindings.getControlBinding("ProposalNo");
            String noProp = (String)noPropAttr.getInputValue();

            AttributeBinding mekPenagihanAttr =
                (AttributeBinding)bindings.getControlBinding("MekanismePenagihan1");
            String mekPenagihan = (String)mekPenagihanAttr.getInputValue();

            AttributeBinding discTypeAttr =
                (AttributeBinding)bindings.getControlBinding("DiscountType1");
            String discType = (String)discTypeAttr.getInputValue();

            AttributeBinding confirmNoAttr =
                (AttributeBinding)bindings.getControlBinding("ConfirmNo");
            String confNo = (String)confirmNoAttr.getInputValue();

            AttributeBinding adendumKeAttr =
                (AttributeBinding)bindings.getControlBinding("AddendumKe");
            String addendumKe = (String)adendumKeAttr.getInputValue();

            AttributeBinding prCreatedAttr =
                (AttributeBinding)bindings.getControlBinding("PrCreated");
            String prCreated = (String)prCreatedAttr.getInputValue();

            AttributeBinding prRequestIdAttr =
                (AttributeBinding)bindings.getControlBinding("PrRequestId");
            Number prRequestId = (Number)prRequestIdAttr.getInputValue();

            AttributeBinding modifierTypeAttr =
                (AttributeBinding)bindings.getControlBinding("ModifierType");
            String modifierType = (String)modifierTypeAttr.getInputValue();

            AttributeBinding modRequestIdAttr =
                (AttributeBinding)bindings.getControlBinding("ModRequestId");
            Number modRequestId = (Number)modRequestIdAttr.getInputValue();

            AttributeBinding statusModEbsPrAttr =
                (AttributeBinding)bindings.getControlBinding("StatusModEbs");
            String statusModEbs =
                statusModEbsPrAttr.getInputValue() == null ? "" :
                (String)statusModEbsPrAttr.getInputValue();

            AttributeBinding statusPrEbsPrAttr =
                (AttributeBinding)bindings.getControlBinding("StatusPrEbs");
            String statusPrEbs =
                statusPrEbsPrAttr.getInputValue() == null ? "" :
                (String)statusPrEbsPrAttr.getInputValue();

            String confirmNo = null;
            if (addendumKe != null) {
                confirmNo = confNo + "-" + addendumKe;
            } else {
                confirmNo = confNo;
            }

            //Cek validasi DCV
            CheckCmRealisasiDcvImpl cekDcv =
                confirmationAM.getCheckCmRealisasiDcv1();
            cekDcv.setNamedWhereClauseParam("noPp", noProp);
            cekDcv.executeQuery();

            // Jika sudah DCV, PC tidak bisa dicancel
            if (cekDcv.getEstimatedRowCount() == 0) {
                DateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
                java.util.Date startDate = null;
                java.util.Date endDate = null;

                // Cek tanggal mulai periode promo
                AttributeBinding periodeFromAttr =
                    (AttributeBinding)bindings.getControlBinding("PeriodeProgFrom");
                String startDateString =
                    (String)periodeFromAttr.getInputValue();

                try {
                    startDate = df.parse(startDateString + " 00:00:00");
                } catch (ParseException e) {
                    JSFUtils.addFacesErrorMessage(e.getMessage());
                }

                java.util.Date todayDate = new java.util.Date();

                // Cek tanggal akhir periode promo
                AttributeBinding periodeToAttr =
                    (AttributeBinding)bindings.getControlBinding("PeriodeProgTo");
                String endDateString = (String)periodeToAttr.getInputValue();

                try {
                    endDate = df.parse(endDateString + " 23:59:59");
                } catch (ParseException e) {
                    JSFUtils.addFacesErrorMessage(e.getMessage());
                }

                boolean validasi = false;
                boolean realisasi = false;
                if (mekPenagihan.equalsIgnoreCase(onInvoice) &&
                    discType.equalsIgnoreCase(discTypePotongan) &&
                    modifierType != null && modRequestId != null) {
                    // Create Modifier
                    FcsViewNoModifierViewImpl noModifierView =
                        confirmationAM.getFcsViewNoModifierView1();
                    noModifierView.setNamedWhereClauseParam("noConf",
                                                            confirmNo);
                    noModifierView.executeQuery();

                    long modifierExists =
                        noModifierView.getEstimatedRowCount();
                    String modifierStatus = "";
                    if (modifierExists > 0) {
                        FcsViewNoModifierViewRowImpl noModifierRow =
                            (FcsViewNoModifierViewRowImpl)noModifierView.first();
                        modifierStatus = noModifierRow.getActive();
                    }

                    if (modifierStatus.equalsIgnoreCase("N")) {
                        if ((todayDate.after(startDate) &&
                             todayDate.before(endDate)) ||
                            todayDate.equals(startDate) ||
                            todayDate.equals(endDate)) {
                            if (statusPrEbs.equalsIgnoreCase(prStatusCancelled) ||
                                statusModEbs.equalsIgnoreCase(modStatusInactive)) {
                                validasi = true;
                            } else {
                                validasi = false;
                                JSFUtils.addFacesErrorMessage("Dokumen PC masih dalam periode promosi, tidak dapat di-cancel.");
                            }
                        } /*else if (todayDate.after(endDate)) {
                            validasi = false;
                            JSFUtils.addFacesErrorMessage("Dokumen PC sudah lewat periode masa promosi, tidak dapat di-cancel.");
                        }*/ else {
                            validasi = true;
                        }
                    } else {
                        validasi = false;
                        JSFUtils.addFacesErrorMessage("Dokumen ini tidak dalam status INACTIVE, Modifier ini belum dapat di-cancel.");
                    }

                } else if (mekPenagihan.equalsIgnoreCase(offInvoice) &&
                           prRequestId != null && prCreated != null) {
                    // Validasi jika sudah pernah Create PR
                    FcsViewNoPrViewImpl noPrView =
                        (FcsViewNoPrViewImpl)confirmationAM.getFcsViewNoPrView1();
                    noPrView.setNamedWhereClauseParam("noPc", confirmNo);
                    noPrView.executeQuery();

                    long prExists = noPrView.getEstimatedRowCount();
                    String prStatus = "";
                    Number prRevNum = new Number(0);
                    if (prExists > 0) {
                        FcsViewNoPrViewRowImpl noPrRow =
                            (FcsViewNoPrViewRowImpl)noPrView.first();
                        prStatus = noPrRow.getStatus();
                        prRevNum = noPrRow.getRevision();
                    }

                    if (prExists == 0 ||
                        (prStatus.equalsIgnoreCase(prStatusCancelled))) {
                        if ((todayDate.after(startDate) &&
                             todayDate.before(endDate)) ||
                            todayDate.equals(startDate) ||
                            todayDate.equals(endDate)) {
                            validasi = false;
                            JSFUtils.addFacesErrorMessage("Dokumen PC masih dalam periode promosi, tidak dapat di-cancel.");
                        } /*else if (todayDate.after(endDate)) {
                            validasi = false;
                            JSFUtils.addFacesErrorMessage("Dokumen PC sudah lewat periode masa promosi, tidak dapat di-cancel.");
                        }*/ else {
                            validasi = true;
                        }
                    } else {
                        validasi = false;
                        JSFUtils.addFacesErrorMessage("Dokumen ini tidak dalam status CANCEL, PR ini belum dapat di-close.");
                    }

                } else if (mekPenagihan.equalsIgnoreCase(onInvoice) &&
                           discType.equalsIgnoreCase(discTypePromoBarang)) {
                    validasi = true;
                    // Validasi realisasi
                    CheckRealisasiSalesOrderImpl realisasiSO =
                        confirmationAM.getCheckRealisasiSalesOrder1();
                    realisasiSO.setNamedWhereClauseParam("propNo", noProp);
                    realisasiSO.executeQuery();
                    if (realisasiSO.getEstimatedRowCount() > 0) {
                        String status = "";
                        while (realisasiSO.hasNext()) {
                            Row checkSo = realisasiSO.next();
                            status =
                                    checkSo.getAttribute("OrderKet") == null ? "" :
                                    checkSo.getAttribute("OrderKet").toString();
                        }

                        if (status.equalsIgnoreCase("Yes")) {
                            realisasi = true;
                        } else {
                            realisasi = false;
                        }
                    }
                } else {
                    validasi = true;
                }

                if (validasi) {
                    if (!realisasi) {
                        String budgetByChoice =
                            itLovBudgetBy.getValue().toString();
                        if (budgetByChoice.equalsIgnoreCase("CUSTOMER")) {
                            CancelConfirmationClearBudgetViewImpl dciterCust =
                                confirmationAM.getCancelConfirmationClearBudgetView1();
                            dciterCust.setNamedWhereClauseParam("propNo",
                                                                noProp);
                            dciterCust.executeQuery();
                            if (dciterCust.getEstimatedRowCount() > 0) {
                                while (dciterCust.hasNext()) {
                                    Row erCekData = dciterCust.next();
                                    ListAddendumBudget ar =
                                        new ListAddendumBudget();
                                    String custId =
                                        erCekData.getAttribute("BudgetCustId").toString();
                                    String pid =
                                        erCekData.getAttribute("PromoProdukId").toString();
                                    String budgetid =
                                        erCekData.getAttribute("BudgetById").toString();
                                    ar.setBudgetCustId(custId);
                                    ar.setPromoProdukId(pid);
                                    ar.setBudgetById(budgetid);
                                    ListAddendumAmount tempUsed =
                                        new ListAddendumAmount();
                                    tempUsed.setPromoProdukId(pid);
                                    tempUsed.setBudgetCustId(custId);
                                    tempUsed.setBudgetById(budgetid);
                                    BigDecimal total =
                                        new BigDecimal(erCekData.getAttribute("Amount").toString());

                                    tempUsed.setAmount(total);
                                    listAddendumAmount.add(tempUsed);
                                    listAddendumBudget.add(ar);
                                }
                                Connection conn = null;
                                List<ListAddendumAmount> realTempend =
                                    getListAddendumAmount();
                                for (ListAddendumAmount cek : realTempend) {
                                    String budgetCustIdFdi = "";
                                    BigDecimal amountSubs = BigDecimal.ZERO;
                                    budgetCustIdFdi = cek.getBudgetCustId();
                                    amountSubs = cek.getAmount();
                                    if (amountSubs.compareTo(BigDecimal.ZERO) >
                                        0) {
                                        try {
                                            Context ctx = new InitialContext();
                                            DataSource ds =
                                                (DataSource)ctx.lookup("jdbc/focusppDS");
                                            conn = ds.getConnection();
                                            conn.setAutoCommit(false);
                                            PreparedStatement statement =
                                                conn.prepareStatement("select BUDGETCUSTOMER.BUDGET_CUSTOMER_ID ,BUDGETCUSTOMER.BUDGET_AS_TO_DATE, BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED,BUDGETCUSTOMER.YEARLY_BUDGET_AMOUNT from BUDGET_CUSTOMER BudgetCustomer where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID=? for update");
                                            statement.setString(1,
                                                                budgetCustIdFdi);
                                            ResultSet rs =
                                                statement.executeQuery();
                                            while (rs.next()) {
                                                String BudgetCustId =
                                                    rs.getString("BUDGET_CUSTOMER_ID").toString();
                                                BigDecimal budgetAsTodateCur =
                                                    BigDecimal.ZERO;
                                                BigDecimal budgetAsTodate =
                                                    BigDecimal.ZERO;
                                                budgetAsTodateCur =
                                                        new BigDecimal(rs.getString("BUDGET_AS_TO_DATE_USED").toString());
                                                budgetAsTodate =
                                                        new BigDecimal(rs.getString("BUDGET_AS_TO_DATE").toString());
                                                if (budgetCustIdFdi.equalsIgnoreCase(BudgetCustId)) {
                                                    BigDecimal value =
                                                        budgetAsTodateCur.subtract(amountSubs);
                                                    try {
                                                        PreparedStatement updateTtfNumSeq =
                                                            conn.prepareStatement("UPDATE BUDGET_CUSTOMER BudgetCustomer SET BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED =? where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID = ?");
                                                        updateTtfNumSeq.setBigDecimal(1,
                                                                                      value);
                                                        updateTtfNumSeq.setString(2,
                                                                                  budgetCustIdFdi);
                                                        updateTtfNumSeq.executeUpdate();
                                                        updateTtfNumSeq.close();
                                                    } catch (SQLException sqle) {
                                                        System.out.println("------------------------------------------------");
                                                        System.out.println("ERROR: Cannot run update query");
                                                        System.out.println("STACK: " +
                                                                           sqle.toString().trim());
                                                        System.out.println("------------------------------------------------");
                                                    }
                                                }
                                            }
                                            conn.commit();
                                            statement.close();
                                            conn.close();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                List<ListAddendumBudget> ab =
                                    getListAddendumBudget();
                                for (ListAddendumBudget x : ab) {
                                    String budgetByid =
                                        x.getBudgetById() == null ? "" :
                                        x.getBudgetById().toString();
                                    DCIteratorBinding dciterCust1 =
                                        ADFUtils.findIterator("ProdBudgetByView1Iterator");
                                    RowSetIterator BudCust =
                                        dciterCust1.getRowSetIterator();
                                    dciterCust1.executeQuery();
                                    ViewObject voTableData =
                                        dciterCust1.getViewObject();
                                    while (voTableData.hasNext()) {
                                        Row rowSelected = voTableData.next();
                                        String budgetId =
                                            rowSelected.getAttribute("BudgetById") ==
                                            null ? "" :
                                            rowSelected.getAttribute("BudgetById").toString();
                                        if (budgetId.equalsIgnoreCase(budgetByid)) {
                                            rowSelected.setAttribute("Status",
                                                                     "Y");
                                        }
                                    }
                                    dciterCust1.getDataControl().commitTransaction();
                                    BudCust.closeRowSetIterator();
                                }
                            }

                        } else {
                            CancelConfirmationClearBudgetViewImpl dciterCust =
                                confirmationAM.getCancelConfirmationClearBudgetView1();
                            dciterCust.setNamedWhereClauseParam("propNo",
                                                                noProp);
                            dciterCust.executeQuery();
                            if (dciterCust.getEstimatedRowCount() > 0) {
                                while (dciterCust.hasNext()) {
                                    Row erCekData = dciterCust.next();
                                    ListAddendumBudget ar =
                                        new ListAddendumBudget();
                                    String custId =
                                        erCekData.getAttribute("BudgetCustId").toString();
                                    String pid =
                                        erCekData.getAttribute("PromoProdukId").toString();
                                    String budgetid =
                                        erCekData.getAttribute("BudgetById").toString();
                                    ar.setBudgetCustId(custId);
                                    ar.setPromoProdukId(pid);
                                    ar.setBudgetById(budgetid);
                                    ListAddendumAmount tempUsed =
                                        new ListAddendumAmount();
                                    tempUsed.setPromoProdukId(pid);
                                    tempUsed.setBudgetCustId(custId);
                                    tempUsed.setBudgetById(budgetid);
                                    BigDecimal total =
                                        new BigDecimal(erCekData.getAttribute("Amount").toString());

                                    tempUsed.setAmount(total);
                                    listAddendumAmount.add(tempUsed);
                                    listAddendumBudget.add(ar);
                                    Connection conn = null;
                                    List<ListAddendumAmount> realTempend =
                                        getListAddendumAmount();
                                    for (ListAddendumAmount cek :
                                         realTempend) {
                                        String budgetCustIdFdi = "";
                                        BigDecimal amountSubs =
                                            BigDecimal.ZERO;
                                        budgetCustIdFdi =
                                                cek.getBudgetCustId();
                                        amountSubs = cek.getAmount();
                                        if (amountSubs.compareTo(BigDecimal.ZERO) >
                                            0) {
                                            try {
                                                Context ctx =
                                                    new InitialContext();
                                                DataSource ds =
                                                    (DataSource)ctx.lookup("jdbc/focusppDS");
                                                conn = ds.getConnection();
                                                conn.setAutoCommit(false);
                                                PreparedStatement statement =
                                                    conn.prepareStatement("select BUDGETCUSTOMER.BUDGET_CUSTOMER_ID ,BUDGETCUSTOMER.BUDGET_AS_TO_DATE, BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED,BUDGETCUSTOMER.YEARLY_BUDGET_AMOUNT from BUDGET_CUSTOMER BudgetCustomer where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID=? for update");
                                                statement.setString(1,
                                                                    budgetCustIdFdi);
                                                ResultSet rs =
                                                    statement.executeQuery();
                                                while (rs.next()) {
                                                    String BudgetCustId =
                                                        rs.getString("BUDGET_CUSTOMER_ID").toString();
                                                    BigDecimal budgetAsTodateCur =
                                                        BigDecimal.ZERO;
                                                    BigDecimal budgetAsTodate =
                                                        BigDecimal.ZERO;
                                                    budgetAsTodateCur =
                                                            new BigDecimal(rs.getString("BUDGET_AS_TO_DATE_USED").toString());
                                                    budgetAsTodate =
                                                            new BigDecimal(rs.getString("BUDGET_AS_TO_DATE").toString());
                                                    if (budgetCustIdFdi.equalsIgnoreCase(BudgetCustId)) {
                                                        BigDecimal value =
                                                            budgetAsTodateCur.subtract(amountSubs);
                                                        try {
                                                            PreparedStatement updateTtfNumSeq =
                                                                conn.prepareStatement("UPDATE BUDGET_CUSTOMER BudgetCustomer SET BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED =? where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID = ?");
                                                            updateTtfNumSeq.setBigDecimal(1,
                                                                                          value);
                                                            updateTtfNumSeq.setString(2,
                                                                                      budgetCustIdFdi);
                                                            updateTtfNumSeq.executeUpdate();
                                                            updateTtfNumSeq.close();
                                                        } catch (SQLException sqle) {
                                                            System.out.println("------------------------------------------------");
                                                            System.out.println("ERROR: Cannot run update query");
                                                            System.out.println("STACK: " +
                                                                               sqle.toString().trim());
                                                            System.out.println("------------------------------------------------");
                                                        }
                                                    }
                                                }
                                                conn.commit();
                                                statement.close();
                                                conn.close();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                    List<ListAddendumBudget> ab =
                                        getListAddendumBudget();
                                    for (ListAddendumBudget x : ab) {
                                        String budgetByid =
                                            x.getBudgetById() == null ? "" :
                                            x.getBudgetById().toString();
                                        DCIteratorBinding dciterCust1 =
                                            ADFUtils.findIterator("ProdBudgetByView1Iterator");
                                        RowSetIterator BudCust =
                                            dciterCust1.getRowSetIterator();
                                        dciterCust1.executeQuery();
                                        ViewObject voTableData =
                                            dciterCust1.getViewObject();
                                        while (voTableData.hasNext()) {
                                            Row rowSelected =
                                                voTableData.next();
                                            String budgetId =
                                                rowSelected.getAttribute("BudgetById") ==
                                                null ? "" :
                                                rowSelected.getAttribute("BudgetById").toString();
                                            if (budgetId.equalsIgnoreCase(budgetByid)) {
                                                rowSelected.setAttribute("Status",
                                                                         "Y");
                                            }
                                        }
                                        dciterCust1.getDataControl().commitTransaction();
                                        BudCust.closeRowSetIterator();
                                    }

                                    AdfFacesContext.getCurrentInstance().addPartialTarget(itLovBudgetBy);
                                }
                            }
                        }
                        AttributeBinding statusPropAttr =
                            (AttributeBinding)bindings.getControlBinding("Status1");
                        statusPropAttr.setInputValue("CANCELED");

                        OperationBinding operationBinding =
                            bindings.getOperationBinding("Commit");
                        operationBinding.execute();

                        OperationBinding executeListConf =
                            bindings.getOperationBinding("ExecuteProposalConf");
                        executeListConf.execute();
                    } else {
                        JSFUtils.addFacesWarningMessage("PC tidak dapat di cancel, sudah ada realisasi.");
                    }
                }
            } else {
                JSFUtils.addFacesWarningMessage("PC ini tidak bisa di-cancel karena sudah dibuat DCV.");

                OperationBinding executeListConf =
                    bindings.getOperationBinding("ExecuteProposalConf");
                executeListConf.execute();
            }
        }
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropConfirmation);
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
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

    public void confirmCategoryPcOkDialog(ActionEvent actionEvent) {
        BindingContainer bindings = getBindings();
        ConfirmationAMImpl confirmationAM =
            (ConfirmationAMImpl)ADFUtils.getApplicationModuleForDataControl("ConfirmationAMDataControl");

        DCIteratorBinding parentIter =
            (DCIteratorBinding)bindings.get("ProposalConfirmationView1Iterator");
        //Get current row key
        Row parentRow = parentIter.getCurrentRow();

        AttributeBinding confirmNoAttr =
            (AttributeBinding)bindings.getControlBinding("ConfirmNo");
        String confirmNo =
            (String)confirmNoAttr.getInputValue() == null ? "" : (String)confirmNoAttr.getInputValue();
        
        AttributeBinding categoryPcAttr =
            (AttributeBinding)bindings.getControlBinding("CategoryPc");
        String categoryPc =
            (String)categoryPcAttr.getInputValue() == null ? "---" :
            (String)categoryPcAttr.getInputValue();

        String existingCatPc =
            confirmNo.substring(0, Math.min(confirmNo.length(), 3));

        if (!categoryPc.equalsIgnoreCase("---")) {
            if (!existingCatPc.equalsIgnoreCase(categoryPc)) {
                // Get tahun 2 digit (dari tahun Start Program Promo Date)
                AttributeBinding periodeProgFromAttr =
                    (AttributeBinding)bindings.getControlBinding("PeriodeProgFrom");
                String periodeProgFrom =
                    (String)periodeProgFromAttr.getInputValue();

                DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
                java.util.Date date = null;
                try {
                    date = formatter.parse(periodeProgFrom);
                } catch (ParseException e) {
                    JSFUtils.addFacesErrorMessage("Error",
                                                  e.getLocalizedMessage());
                }

                DateFormat dfYY = new SimpleDateFormat("yy");
                String currYear = dfYY.format(date);
                Number numYear = new Number(Integer.parseInt(currYear));

                // Get Running Number
                Number runNumber;
                RunNumConfViewImpl runNumVo =
                    confirmationAM.getRunNumConfView1();
                runNumVo.setNamedWhereClauseParam("userName", categoryPc);
                runNumVo.setNamedWhereClauseParam("runYear", numYear);
                runNumVo.setNamedWhereClauseParam("runMonth", 0);
                runNumVo.executeQuery();

                if (runNumVo.getAllRowsInRange().length > 0) {
                    runNumConf = runNumVo.first();
                    runNumber = (Number)runNumConf.getAttribute("Value");
                    runNumConf.setAttribute("Value", runNumber.add(1));
                } else {
                    Row newRow = runNumVo.createRow();
                    newRow.setAttribute("RunNumType", "CONF");
                    newRow.setAttribute("UserName", categoryPc);
                    newRow.setAttribute("RunYear", numYear);
                    newRow.setAttribute("RunMonth", 0);
                    newRow.setAttribute("Value", 2);
                    runNumVo.insertRow(newRow);
                    runNumber = new Number(1);
                }

                // Generate Confirmation Number
                String runNumberFormatted =
                    String.format("%06d", runNumber.getBigDecimalValue().intValue());
                String confNumber = categoryPc + numYear + runNumberFormatted;
                itConfirmNo.setValue(confNumber);
                itConfirmNo.setSubmittedValue(confNumber);
                itlovCategoryPc.setValue(categoryPc);
                itlovCategoryPc.setSubmittedValue(categoryPc);
                parentRow.setAttribute("ConfirmNo", confNumber);

                //Set log category pc
                CategoryPcLogViewImpl catPcLogView =
                    confirmationAM.getCategoryPcLogView1();
                Row newCatPcLogRow = catPcLogView.createRow();
                newCatPcLogRow.setAttribute("NoPc", confNumber);
            }
        }
        //confirmationAM.getDBTransaction().commit();

        OperationBinding operationBindingCommit =
            bindings.getOperationBinding("Commit");
        operationBindingCommit.execute();

        OperationBinding operationExecutePropConfirm =
            bindings.getOperationBinding("ExecutePropConfirm");
        operationExecutePropConfirm.execute();

        Row confirmListRow = parentRow;
        String statusGreen =
            (String)confirmListRow.getAttribute("StatusGreen");
        String statusYellow =
            (String)confirmListRow.getAttribute("StatusYellow");
        String statusRed = (String)confirmListRow.getAttribute("StatusRed");
        String statusBlock =
            (String)confirmListRow.getAttribute("StatusBlock");
        String statusClosed = (String)confirmListRow.getAttribute("Status");

        //Set color mode
        ADFContext adfCtx = ADFContext.getCurrent();
        Map sessionScope = adfCtx.getSessionScope();

        ProposalConfirmationViewImpl propConfVo =
            (ProposalConfirmationViewImpl)parentIter.getViewObject();
        if (statusGreen.equalsIgnoreCase("Y")) {
            sessionScope.put("colorMode", "GREEN");
            ViewCriteria vc = propConfVo.getViewCriteria("PropConfirmNewPcVC");
            propConfVo.applyViewCriteria(vc);
            propConfVo.executeQuery();
        } else if (statusYellow.equalsIgnoreCase("Y")) {
            sessionScope.put("colorMode", "YELLOW");
            ViewCriteria vc =
                propConfVo.getViewCriteria("PropConfirmCanceledVC");
            propConfVo.applyViewCriteria(vc);
            propConfVo.executeQuery();
        } else if (statusRed.equalsIgnoreCase("Y")) {
            sessionScope.put("colorMode", "RED");
            ViewCriteria vc =
                propConfVo.getViewCriteria("PropConfirmNeedCreateVC");
            propConfVo.applyViewCriteria(vc);
            propConfVo.executeQuery();
        } else if (statusBlock.equalsIgnoreCase("Y")) {
            sessionScope.put("colorMode", "BLOCK");
            ViewCriteria vc =
                propConfVo.getViewCriteria("PropConfirmStatusCanceledVC");
            propConfVo.applyViewCriteria(vc);
            propConfVo.executeQuery();
        } else if (statusClosed.equalsIgnoreCase("CLOSED")) {
            sessionScope.put("colorMode", "CLOSED");
            ViewCriteria vc =
                propConfVo.getViewCriteria("PropConfirmClosedVC");
            propConfVo.applyViewCriteria(vc);
            propConfVo.executeQuery();
        } else {
            // DO NOTHING;
        }

        itConfirmNo.resetValue();
        itlovCategoryPc.resetValue();

        AdfFacesContext.getCurrentInstance().addPartialTarget(itConfirmNo);
        AdfFacesContext.getCurrentInstance().addPartialTarget(itlovCategoryPc);
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropConfirmation);
    }

    public void confirmCategoryPcCancelDialog(ActionEvent actionEvent) {
        BindingContainer bindings = getBindings();

        DCIteratorBinding parentIter =
            (DCIteratorBinding)bindings.get("ProposalConfirmationView1Iterator");
        //Get current row key
        Key parentKey = parentIter.getCurrentRow().getKey();

        itlovCategoryPc.setValue(null);
        itlovCategoryPc.setSubmittedValue(null);
        AdfFacesContext.getCurrentInstance().addPartialTarget(itlovCategoryPc);

        OperationBinding operationBindingCommit =
            bindings.getOperationBinding("Rollback");
        operationBindingCommit.execute();
        parentIter.setCurrentRowWithKey(parentKey.toStringFormat(true));
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropConfirmation);
    }

    public void refreshConfirmList(ActionEvent actionEvent) {
        BindingContainer bindings = getBindings();
        DCIteratorBinding parentIter =
            (DCIteratorBinding)bindings.get("ProposalConfirmationView1Iterator");
        ProposalConfirmationViewImpl propConfVo =
            (ProposalConfirmationViewImpl)parentIter.getViewObject();
        //View Criteria without bind variable
        ViewCriteria vc = propConfVo.getViewCriteria("PropConfirmAllStatusVC");
        propConfVo.applyViewCriteria(vc);
        propConfVo.executeQuery();
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropConfirmation);

        switchMain.setFacetName("nodata");
        switchButtonMain.setFacetName("nodata");
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchButtonMain);
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropConfirmation);
    }

    public void filterByNeedCreate(ActionEvent actionEvent) {
        BindingContainer bindings = getBindings();
        DCIteratorBinding parentIter =
            (DCIteratorBinding)bindings.get("ProposalConfirmationView1Iterator");
        ProposalConfirmationViewImpl propConfVo =
            (ProposalConfirmationViewImpl)parentIter.getViewObject();
        //View Criteria without bind variable
        ViewCriteria vc =
            propConfVo.getViewCriteria("PropConfirmNeedCreateVC");
        propConfVo.applyViewCriteria(vc);
        propConfVo.executeQuery();

        switchMain.setFacetName("nodata");
        switchButtonMain.setFacetName("nodata");
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchButtonMain);
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropConfirmation);
    }

    public void filterByCanceled(ActionEvent actionEvent) {
        BindingContainer bindings = getBindings();
        DCIteratorBinding parentIter =
            (DCIteratorBinding)bindings.get("ProposalConfirmationView1Iterator");
        ProposalConfirmationViewImpl propConfVo =
            (ProposalConfirmationViewImpl)parentIter.getViewObject();
        //View Criteria without bind variable
        ViewCriteria vc = propConfVo.getViewCriteria("PropConfirmCanceledVC");
        propConfVo.applyViewCriteria(vc);
        propConfVo.executeQuery();

        switchMain.setFacetName("nodata");
        switchButtonMain.setFacetName("nodata");
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchButtonMain);
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropConfirmation);
    }

    public void filterByNewPc(ActionEvent actionEvent) {
        BindingContainer bindings = getBindings();
        DCIteratorBinding parentIter =
            (DCIteratorBinding)bindings.get("ProposalConfirmationView1Iterator");
        ProposalConfirmationViewImpl propConfVo =
            (ProposalConfirmationViewImpl)parentIter.getViewObject();
        //View Criteria without bind variable
        ViewCriteria vc = propConfVo.getViewCriteria("PropConfirmNewPcVC");
        propConfVo.applyViewCriteria(vc);
        propConfVo.executeQuery();

        switchMain.setFacetName("nodata");
        switchButtonMain.setFacetName("nodata");
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchButtonMain);
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropConfirmation);
    }

    public void filterByAutoGenPc(ActionEvent actionEvent) {
        BindingContainer bindings = getBindings();
        DCIteratorBinding parentIter =
            (DCIteratorBinding)bindings.get("ProposalConfirmationView1Iterator");
        ProposalConfirmationViewImpl propConfVo =
            (ProposalConfirmationViewImpl)parentIter.getViewObject();
        //View Criteria without bind variable
        ViewCriteria vc = propConfVo.getViewCriteria("PropConfirmAutoGenVC");
        propConfVo.applyViewCriteria(vc);
        propConfVo.executeQuery();

        switchMain.setFacetName("nodata");
        switchButtonMain.setFacetName("nodata");
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchButtonMain);
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropConfirmation);
    }

    public void filterByClosedPc(ActionEvent actionEvent) {
        BindingContainer bindings = getBindings();
        DCIteratorBinding parentIter =
            (DCIteratorBinding)bindings.get("ProposalConfirmationView1Iterator");
        ProposalConfirmationViewImpl propConfVo =
            (ProposalConfirmationViewImpl)parentIter.getViewObject();
        //View Criteria without bind variable
        ViewCriteria vc = propConfVo.getViewCriteria("PropConfirmClosedVC");
        propConfVo.applyViewCriteria(vc);
        propConfVo.executeQuery();

        switchMain.setFacetName("nodata");
        switchButtonMain.setFacetName("nodata");
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchButtonMain);
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropConfirmation);
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

    public void filterByStatusCanceled(ActionEvent actionEvent) {
        BindingContainer bindings = getBindings();
        DCIteratorBinding parentIter =
            (DCIteratorBinding)bindings.get("ProposalConfirmationView1Iterator");
        ProposalConfirmationViewImpl propConfVo =
            (ProposalConfirmationViewImpl)parentIter.getViewObject();
        //View Criteria without bind variable
        ViewCriteria vc =
            propConfVo.getViewCriteria("PropConfirmStatusCanceledVC");
        propConfVo.applyViewCriteria(vc);
        propConfVo.executeQuery();

        switchMain.setFacetName("nodata");
        switchButtonMain.setFacetName("nodata");
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchButtonMain);
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropConfirmation);
    }

    public void resetStatusPr(DialogEvent dialogEvent) {
        BindingContainer bindings = getBindings();

        DCIteratorBinding parentIter =
            (DCIteratorBinding)bindings.get("ProposalConfirmationView1Iterator");
        //Get current row key
        Key parentKey = parentIter.getCurrentRow().getKey();
        Row currRow = parentIter.getCurrentRow();
        currRow.setAttribute("PrRequestId", null);
        currRow.setAttribute("PrCreated", null);
        //currRow.setAttribute("StatusPrEbs", null);
        currRow.setAttribute("EbsPrStatus", null); //Direct status pr from EBS

        OperationBinding operationBindingCommit =
            bindings.getOperationBinding("Commit");
        operationBindingCommit.execute();

        parentIter.setCurrentRowWithKey(parentKey.toStringFormat(true));
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropConfirmation);
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
    }

    public void popupClearItemExpense(LaunchPopupEvent launchPopupEvent) {

    }

    public void setLovItemExpense(RichInputListOfValues lovItemExpense) {
        this.lovItemExpense = lovItemExpense;
    }

    public RichInputListOfValues getLovItemExpense() {
        return lovItemExpense;
    }

    public void checkBucketKodePostingOntop(ReturnPopupEvent returnPopupEvent) {

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
        BindingContainer bindings = getBindings();
        DCIteratorBinding parentIter =
            (DCIteratorBinding)bindings.get("ProposalConfirmationView1Iterator");

        //Get current row key
        Key parentKey = parentIter.getCurrentRow().getKey();
        ConfirmationAMImpl confirmationAM =
            (ConfirmationAMImpl)ADFUtils.getApplicationModuleForDataControl("ConfirmationAMDataControl");
        AttributeBinding mekPenagihanAttr =
            (AttributeBinding)bindings.getControlBinding("MekanismePenagihan1");
        String mekPenagihan = (String)mekPenagihanAttr.getInputValue();
        AttributeBinding discTypeAttr =
            (AttributeBinding)bindings.getControlBinding("DiscountType1");
        String discType = (String)discTypeAttr.getInputValue();

        if (mekPenagihan.equalsIgnoreCase(onInvoice) &&
            discType.equalsIgnoreCase(discTypePotongan)) {
            String KodePostinglov =
                rw.getAttribute("Item") == null ? "" : rw.getAttribute("Item").toString();
            FcsModifierCeckBucketItemCategoryImpl vobucketCheckView =
                confirmationAM.getFcsModifierCeckBucketItemCategory1();
            vobucketCheckView.setNamedWhereClauseParam("kodePosting",
                                                       KodePostinglov);
            vobucketCheckView.executeQuery();
            if (vobucketCheckView.getEstimatedRowCount() > 0) {
                while (vobucketCheckView.hasNext()) {
                    Row ceckRow = vobucketCheckView.next();
                    String kodePostingCek =
                        ceckRow.getAttribute("Item").toString();
                    String bucket =
                        ceckRow.getAttribute("Bucket") == null ? "" :
                        ceckRow.getAttribute("Bucket").toString();

                    if (KodePostinglov.equalsIgnoreCase(kodePostingCek) &&
                        bucket.equalsIgnoreCase("")) {
                        showPopupkdOntop("Sequence bucket belum terisi untuk kode posting yang dipilih",
                                         potmessageKdOntop);
                        //                       JSFUtils.addFacesWarningMessage("\n" +
                        //                       "Sequence bucket belum terisi untuk kode posting yang dipilih");

                    }
                }
            }
        }
        parentIter.setCurrentRowWithKey(parentKey.toStringFormat(true));
    }

    public void checkBucketKodePostingMf(ReturnPopupEvent returnPopupEvent) {
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

        BindingContainer bindings = getBindings();
        DCIteratorBinding parentIter =
            (DCIteratorBinding)bindings.get("ProposalConfirmationView1Iterator");
        //Get current row key
        Key parentKey = parentIter.getCurrentRow().getKey();
        ConfirmationAMImpl confirmationAM =
            (ConfirmationAMImpl)ADFUtils.getApplicationModuleForDataControl("ConfirmationAMDataControl");
        AttributeBinding mekPenagihanAttr =
            (AttributeBinding)bindings.getControlBinding("MekanismePenagihan1");
        String mekPenagihan = (String)mekPenagihanAttr.getInputValue();
        AttributeBinding discTypeAttr =
            (AttributeBinding)bindings.getControlBinding("DiscountType1");
        String discType = (String)discTypeAttr.getInputValue();

        if (mekPenagihan.equalsIgnoreCase(onInvoice) &&
            discType.equalsIgnoreCase(discTypePotongan)) {
            String KodePostinglov =
                rw.getAttribute("Item") == null ? "" : rw.getAttribute("Item").toString();
            FcsModifierCeckBucketItemCategoryImpl vobucketCheckView =
                confirmationAM.getFcsModifierCeckBucketItemCategory1();
            vobucketCheckView.setNamedWhereClauseParam("kodePosting",
                                                       KodePostinglov);
            vobucketCheckView.executeQuery();
            if (vobucketCheckView.getEstimatedRowCount() > 0) {
                while (vobucketCheckView.hasNext()) {
                    Row ceckRow = vobucketCheckView.next();
                    String kodePostingCek =
                        ceckRow.getAttribute("Item").toString();
                    String bucket =
                        ceckRow.getAttribute("Bucket") == null ? "" :
                        ceckRow.getAttribute("Bucket").toString();

                    if (KodePostinglov.equalsIgnoreCase(kodePostingCek) &&
                        bucket.equalsIgnoreCase("")) {
                        showPopupkdMf("Sequence bucket belum terisi untuk kode posting yang dipilih",
                                      potmessageKdMf);
                        //                       JSFUtils.addFacesWarningMessage("\n" +
                        //                       "Sequence bucket belum terisi untuk kode posting yang dipilih");
                    }
                }
            }
        }
        parentIter.setCurrentRowWithKey(parentKey.toStringFormat(true));
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

    public void categoryPcReturnPopUpListener(ReturnPopupEvent returnPopupEvent) {
        BindingContainer bindings = getBindings();
        AttributeBinding confirmNoAttr =
            (AttributeBinding)bindings.getControlBinding("ConfirmNo");
        String confirmNo =
            (String)confirmNoAttr.getInputValue() == null ? "" : (String)confirmNoAttr.getInputValue();
        AttributeBinding categoryPcAttr =
            (AttributeBinding)bindings.getControlBinding("CategoryPc");
        String categoryPc = (String)categoryPcAttr.getInputValue();

        if (categoryPc == null) {
            StringBuilder message = new StringBuilder("<html><body>");
            message.append("<p>Anda belum mengisi \"Category PC\", data sudah disimpan</br>");
            message.append("tetapi belum dapat melakukan \"Create PR\" atau \"Create Modifier\"</p>");
            message.append("</body></html>");
            JSFUtils.addFacesWarningMessage(message.toString());
        } else {
            if (!confirmNo.equalsIgnoreCase("Auto Generated")) {
                RichPopup.PopupHints hints = new RichPopup.PopupHints();
                pConfCategoryPc.show(hints);
            } else {
                // DO NOTHING
                //                OperationBinding operationBindingCommit =
                //                    bindings.getOperationBinding("Commit");
                //                operationBindingCommit.execute();
            }
    	}
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropConfirmation);
    }

    public void createModifierReturnCommit(ReturnEvent returnEvent) {
        BindingContainer bindings = getBindings();
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

    public void closeGRRealisasiAmountEvent(ActionEvent actionEvent) {
        BindingContainer bindings = getBindings();
        ConfirmationAMImpl confirmationAM =
            (ConfirmationAMImpl)ADFUtils.getApplicationModuleForDataControl("ConfirmationAMDataControl");
        AttributeBinding propIdAttr =
            (AttributeBinding)bindings.getControlBinding("ProposalId");
        oracle.jbo.domain.DBSequence propId = (oracle.jbo.domain.DBSequence)propIdAttr.getInputValue();
        AttributeBinding discTypeAttr =
            (AttributeBinding)bindings.getControlBinding("DiscountType1");
        String discType = (String)discTypeAttr.getInputValue();
        AttributeBinding mekPenagihanAttr =
            (AttributeBinding)bindings.getControlBinding("MekanismePenagihan1");
        String mekPenagihan = (String)mekPenagihanAttr.getInputValue();
        AttributeBinding DcvFlagAttr =
            (AttributeBinding)bindings.getControlBinding("DcvPcPrFlag");
        String DcvFlag = DcvFlagAttr.getInputValue().toString();

        DCIteratorBinding dciterPromoProduk =
            ADFUtils.findIterator("PromoProdukView1Iterator");
        Key promoProdKey = dciterPromoProduk.getCurrentRow().getKey();
        Row rSel = dciterPromoProduk.getCurrentRow();
        String promoProdukId = rSel.getAttribute("PromoProdukId").toString();
        BigDecimal RealisasiGrMfByLine1 =
            new BigDecimal(rSel.getAttribute("RealisasiGrMfByLine").toString());
        BigDecimal RealisasiModMf =
            new BigDecimal(rSel.getAttribute("RealisasiModMf").toString());
        BigDecimal RealisasiDcvMf =
            new BigDecimal(rSel.getAttribute("RealisasiDcvMf").toString());
        BigDecimal DiscMf =
            new BigDecimal(rSel.getAttribute("DiscMf").toString());
        BigDecimal BrgBonusMf =
            new BigDecimal(rSel.getAttribute("BrgBonusMf").toString());
        BigDecimal BiaMf =
            new BigDecimal(rSel.getAttribute("BiaMf").toString());
        String CloseReason = "";
        try {
            CloseReason = rSel.getAttribute("CloseReason").toString();  
        } catch (NullPointerException e) {
            CloseReason = "";
        } catch (Exception e) {
            //DO NOTHING
        }
        
        if (CloseReason.length() > 0) {        
            realisasiGrMfByLine = new ArrayList<RealisasiGrMfByLine>();
            DCIteratorBinding dciterCust =
                ADFUtils.findIterator("ProdBudgetByView1Iterator");
            RowSetIterator BudCust = dciterCust.getRowSetIterator();
            dciterCust.executeQuery();
            ViewObject voTableData = dciterCust.getViewObject();
            BigDecimal realamountmf = new BigDecimal(0);
            if (voTableData.getEstimatedRowCount() > 0) {
                if (discType.equalsIgnoreCase(discTypePotongan)) {
                    if (mekPenagihan.equalsIgnoreCase(offInvoice) &&
                        DcvFlag.equalsIgnoreCase("Y")) {
                        realamountmf = DiscMf.subtract(RealisasiGrMfByLine1);
                    } else if (mekPenagihan.equalsIgnoreCase(onInvoice)) {
                        realamountmf = DiscMf.subtract(RealisasiModMf);
                    } else if (mekPenagihan.equalsIgnoreCase(offInvoice) &&
                               DcvFlag.equalsIgnoreCase("N")) {
                        realamountmf = DiscMf.subtract(RealisasiDcvMf);
                    }
                } else if (discType.equalsIgnoreCase(discTypeBiaya)) {
                    realamountmf = BiaMf.subtract(RealisasiGrMfByLine1);
                } else if (discType.equalsIgnoreCase(discTypePromoBarang)) {
                    if (mekPenagihan.equalsIgnoreCase(offInvoice) &&
                        DcvFlag.equalsIgnoreCase("Y")) {
                        realamountmf = BrgBonusMf.subtract(RealisasiDcvMf);
                    } else if (mekPenagihan.equalsIgnoreCase(offInvoice) &&
                               DcvFlag.equalsIgnoreCase("N")) {
                        realamountmf = BrgBonusMf.subtract(RealisasiGrMfByLine1);
                    }
                }
    
                while (voTableData.hasNext()) {
                    RealisasiGrMfByLine tempUsed = new RealisasiGrMfByLine();
                    Row rowSelected = voTableData.next();
                    String budgetId =
                        rowSelected.getAttribute("BudgetById") == null ? "" :
                        rowSelected.getAttribute("BudgetById").toString();
                    String promoProdukIdBud =
                        rowSelected.getAttribute("PromoProdukId") == null ? "" :
                        rowSelected.getAttribute("PromoProdukId").toString();
                    String Status =
                        rowSelected.getAttribute("Status") == null ? "" :
                        rowSelected.getAttribute("Status").toString();
                    String BudgetCustId =
                        rowSelected.getAttribute("BudgetCustId") == null ? "" :
                        rowSelected.getAttribute("BudgetCustId").toString();
                    String KombinasiBudget =
                        rowSelected.getAttribute("KombinasiBudget") == null ? "" :
                        rowSelected.getAttribute("KombinasiBudget").toString();
                    String beforeFirstDot = KombinasiBudget.split("\\.")[0];
                    if (promoProdukId.equalsIgnoreCase(promoProdukIdBud) &&
                        Status.equalsIgnoreCase("N")) {
                        tempUsed.setBudgetById(budgetId);
                        tempUsed.setBudgetCustId(BudgetCustId);
                        tempUsed.setKodebudget(beforeFirstDot);
                        realisasiGrMfByLine.add(tempUsed);
                    }
                }
            }
            Connection conn = null;
            String budgetCustIdFdi = "";
            BigDecimal amountSubs = BigDecimal.ZERO;
            if (realamountmf.compareTo(BigDecimal.ZERO) < 0 ||
                realamountmf.compareTo(BigDecimal.ZERO) == 0) {
                amountSubs = BigDecimal.ZERO;
            } else {
                amountSubs = realamountmf;
            }
    
            List<RealisasiGrMfByLine> realTempend = getRealisasiGrMfByLine();
            for (RealisasiGrMfByLine cek : realTempend) {
                String budgetIdFdi = "";
                budgetIdFdi = cek.getBudgetById();
                budgetCustIdFdi = cek.getBudgetCustId();
                try {
                    Context ctx = new InitialContext();
                    DataSource ds = (DataSource)ctx.lookup("jdbc/focusppDS");
                    conn = ds.getConnection();
                    conn.setAutoCommit(false);
                    PreparedStatement statement =
                        conn.prepareStatement("select BUDGETCUSTOMER.BUDGET_CUSTOMER_ID ,BUDGETCUSTOMER.BUDGET_AS_TO_DATE, BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED,BUDGETCUSTOMER.YEARLY_BUDGET_AMOUNT,BUDGETCUSTOMER.OVER_BUDGET from BUDGET_CUSTOMER BudgetCustomer where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID=? for update");
                    statement.setString(1, budgetCustIdFdi);
                    ResultSet rs = statement.executeQuery();
                    while (rs.next()) {
                        String BudgetCustId =
                            rs.getString("BUDGET_CUSTOMER_ID").toString();
                        BigDecimal budgetAsTodateCur = BigDecimal.ZERO;
                        BigDecimal budgetAsTodate = BigDecimal.ZERO;
                        budgetAsTodateCur =
                                new BigDecimal(rs.getString("BUDGET_AS_TO_DATE_USED").toString());
                        budgetAsTodate =
                                new BigDecimal(rs.getString("BUDGET_AS_TO_DATE").toString());
                        if (budgetCustIdFdi.equalsIgnoreCase(BudgetCustId)) {
                            BigDecimal value =
                                budgetAsTodateCur.subtract(amountSubs);
                            try {
                                PreparedStatement updateTtfNumSeq =
                                    conn.prepareStatement("UPDATE BUDGET_CUSTOMER BudgetCustomer SET BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED =? where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID = ?");
                                updateTtfNumSeq.setBigDecimal(1, value);
                                updateTtfNumSeq.setString(2, budgetCustIdFdi);
                                updateTtfNumSeq.executeUpdate();
                                updateTtfNumSeq.close();
                            } catch (SQLException sqle) {
                                System.out.println("------------------------------------------------");
                                System.out.println("ERROR: Cannot run update query");
                                System.out.println("STACK: " +
                                                   sqle.toString().trim());
                                System.out.println("------------------------------------------------");
                            }
                        }
                    }
                    conn.commit();
                    statement.close();
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                amountSubs = cek.getAmount();
                DCIteratorBinding dciterCustset =
                    ADFUtils.findIterator("ProdBudgetByView1Iterator");
                dciterCustset.executeQuery();
                ViewObject voTableDataset = dciterCustset.getViewObject();
                while (voTableDataset.hasNext()) {
                    Row rowSelectedset = voTableDataset.next();
                    String budgetIdset =
                        rowSelectedset.getAttribute("BudgetById") == null ? "" :
                        rowSelectedset.getAttribute("BudgetById").toString();
                    if (budgetIdFdi.equalsIgnoreCase(budgetIdset)) {
                        rowSelectedset.setAttribute("Status", "Y");
                    }
                }
            }
            
            rSel.setAttribute("CloseFlag", "Y");
            
            // Check if last closed promo produk line
            PromoProdukLineClosedViewImpl prodLineClosed = confirmationAM.getPromoProdukLineClosedView1();
            prodLineClosed.setNamedWhereClauseParam("propId", propId.getSequenceNumber());
            prodLineClosed.executeQuery();
            
            oracle.jbo.domain.Number numProdLineClosed = (oracle.jbo.domain.Number)prodLineClosed.first().getAttribute("NumProdClose");
            
            if (numProdLineClosed.compareTo(1) == 0) {
                DCIteratorBinding dciterProposal =
                    ADFUtils.findIterator("ProposalConfirmationView1Iterator");
                Row propSel = dciterProposal.getCurrentRow();
                propSel.setAttribute("Status", proposalClosed);
            }
       
            dciterCust.getDataControl().commitTransaction();
            dciterPromoProduk.getDataControl().commitTransaction();
            BudCust.closeRowSetIterator();
            dciterPromoProduk.setCurrentRowWithKey(promoProdKey.toStringFormat(true));

            AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropConfirmation);
            AdfFacesContext.getCurrentInstance().addPartialTarget(btnClosePr);
            AdfFacesContext.getCurrentInstance().addPartialTarget(btnCloseMod);
            AdfFacesContext.getCurrentInstance().addPartialTarget(btnClosePrmBrg);
        } else {
            JSFUtils.addFacesWarningMessage("Kolom \"Close Reason\" harus diisi untuk melakukan close per-line.");
        }
    }

    public void closeModRealisasiAmountEvent(ActionEvent actionEvent) {
        BindingContainer bindings = getBindings();
        ConfirmationAMImpl confirmationAM =
            (ConfirmationAMImpl)ADFUtils.getApplicationModuleForDataControl("ConfirmationAMDataControl");
        AttributeBinding propIdAttr =
            (AttributeBinding)bindings.getControlBinding("ProposalId");
        oracle.jbo.domain.DBSequence propId = (oracle.jbo.domain.DBSequence)propIdAttr.getInputValue();
        AttributeBinding discTypeAttr =
            (AttributeBinding)bindings.getControlBinding("DiscountType1");
        String discType = (String)discTypeAttr.getInputValue();
        AttributeBinding mekPenagihanAttr =
            (AttributeBinding)bindings.getControlBinding("MekanismePenagihan1");
        String mekPenagihan = (String)mekPenagihanAttr.getInputValue();
        AttributeBinding DcvFlagAttr =
            (AttributeBinding)bindings.getControlBinding("DcvPcPrFlag");
        String DcvFlag = DcvFlagAttr.getInputValue().toString();
        AttributeBinding ConfirmNoAttr =
            (AttributeBinding)bindings.getControlBinding("ConfirmNo");
        String confirmNo = ConfirmNoAttr.getInputValue().toString();
        AttributeBinding AddendumKeAttr =
            (AttributeBinding)bindings.getControlBinding("Addendumke");
        String addendumKe = "0";
        try {
            addendumKe = AddendumKeAttr.getInputValue().toString();
        } catch (NullPointerException e) {
            addendumKe = "0";
        } catch (Exception e) {
            //DO NOTHING
        }

        DCIteratorBinding dciterPromoProduk =
            ADFUtils.findIterator("PromoProdukView1Iterator");
        Key promoProdKey = dciterPromoProduk.getCurrentRow().getKey();
        Row rSel = dciterPromoProduk.getCurrentRow();
        String promoProdukId = rSel.getAttribute("PromoProdukId").toString();
        BigDecimal RealisasiGrMfByLine1 =
            new BigDecimal(rSel.getAttribute("RealisasiGrMfByLine").toString());
        BigDecimal RealisasiModMf =
            new BigDecimal(rSel.getAttribute("RealisasiModMf").toString());
        BigDecimal RealisasiDcvMf =
            new BigDecimal(rSel.getAttribute("RealisasiDcvMf").toString());
        BigDecimal DiscMf =
            new BigDecimal(rSel.getAttribute("DiscMf").toString());
        BigDecimal BrgBonusMf =
            new BigDecimal(rSel.getAttribute("BrgBonusMf").toString());
        BigDecimal BiaMf =
            new BigDecimal(rSel.getAttribute("BiaMf").toString());
        String CloseReason = "";
        try {
            CloseReason = rSel.getAttribute("CloseReason").toString();  
        } catch (NullPointerException e) {
            CloseReason = "";
        } catch (Exception e) {
            //DO NOTHING
        }
        
        if (CloseReason.length() > 0) {            
            String updateStatusMod = modifierUpdateCloseToEbs(confirmNo,addendumKe,promoProdukId);
    
            if (updateStatusMod.equalsIgnoreCase(responSuccess)) {
                realisasiGrMfByLine = new ArrayList<RealisasiGrMfByLine>();
                DCIteratorBinding dciterCust =
                    ADFUtils.findIterator("ProdBudgetByView1Iterator");
                RowSetIterator BudCust = dciterCust.getRowSetIterator();
                dciterCust.executeQuery();
                ViewObject voTableData = dciterCust.getViewObject();
                BigDecimal realamountmf = new BigDecimal(0);
                if (voTableData.getEstimatedRowCount() > 0) {
                    if (discType.equalsIgnoreCase(discTypePotongan)) {
                        if (mekPenagihan.equalsIgnoreCase(offInvoice) &&
                            DcvFlag.equalsIgnoreCase("Y")) {
                            realamountmf = DiscMf.subtract(RealisasiGrMfByLine1);
                        } else if (mekPenagihan.equalsIgnoreCase(onInvoice)) {
                            realamountmf = DiscMf.subtract(RealisasiModMf);
                        } else if (mekPenagihan.equalsIgnoreCase(offInvoice) &&
                                   DcvFlag.equalsIgnoreCase("N")) {
                            realamountmf = DiscMf.subtract(RealisasiDcvMf);
                        }
                    } else if (discType.equalsIgnoreCase(discTypeBiaya)) {
                        realamountmf = BiaMf.subtract(RealisasiGrMfByLine1);
                    } else if (discType.equalsIgnoreCase(discTypePromoBarang)) {
                        if (mekPenagihan.equalsIgnoreCase(offInvoice) &&
                            DcvFlag.equalsIgnoreCase("Y")) {
                            realamountmf = BrgBonusMf.subtract(RealisasiDcvMf);
                        } else if (mekPenagihan.equalsIgnoreCase(offInvoice) &&
                                   DcvFlag.equalsIgnoreCase("N")) {
                            realamountmf = BrgBonusMf.subtract(RealisasiGrMfByLine1);
                        }
                    }
        
                    while (voTableData.hasNext()) {
                        RealisasiGrMfByLine tempUsed = new RealisasiGrMfByLine();
                        Row rowSelected = voTableData.next();
                        String budgetId =
                            rowSelected.getAttribute("BudgetById") == null ? "" :
                            rowSelected.getAttribute("BudgetById").toString();
                        String promoProdukIdBud =
                            rowSelected.getAttribute("PromoProdukId") == null ? "" :
                            rowSelected.getAttribute("PromoProdukId").toString();
                        String Status =
                            rowSelected.getAttribute("Status") == null ? "" :
                            rowSelected.getAttribute("Status").toString();
                        String BudgetCustId =
                            rowSelected.getAttribute("BudgetCustId") == null ? "" :
                            rowSelected.getAttribute("BudgetCustId").toString();
                        String KombinasiBudget =
                            rowSelected.getAttribute("KombinasiBudget") == null ? "" :
                            rowSelected.getAttribute("KombinasiBudget").toString();
                        String beforeFirstDot = KombinasiBudget.split("\\.")[0];
                        if (promoProdukId.equalsIgnoreCase(promoProdukIdBud) &&
                            Status.equalsIgnoreCase("N")) {
                            tempUsed.setBudgetById(budgetId);
                            tempUsed.setBudgetCustId(BudgetCustId);
                            tempUsed.setKodebudget(beforeFirstDot);
                            realisasiGrMfByLine.add(tempUsed);
                        }
                    }
                }
                
                Connection conn = null;
                String budgetCustIdFdi = "";
                BigDecimal amountSubs = BigDecimal.ZERO;
                if (realamountmf.compareTo(BigDecimal.ZERO) < 0 ||
                    realamountmf.compareTo(BigDecimal.ZERO) == 0) {
                    amountSubs = BigDecimal.ZERO;
                } else {
                    amountSubs = realamountmf;
                }
                
                List<RealisasiGrMfByLine> realTempend = getRealisasiGrMfByLine();
                for (RealisasiGrMfByLine cek : realTempend) {
                    String budgetIdFdi = "";
                    budgetIdFdi = cek.getBudgetById();
                    budgetCustIdFdi = cek.getBudgetCustId();
                    try {
                        Context ctx = new InitialContext();
                        DataSource ds = (DataSource)ctx.lookup(dsFdiConn);
                        conn = ds.getConnection();
                        conn.setAutoCommit(false);
                        PreparedStatement statement =
                            conn.prepareStatement("SELECT BUDGETCUSTOMER.BUDGET_CUSTOMER_ID ,BUDGETCUSTOMER.BUDGET_AS_TO_DATE, BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED,BUDGETCUSTOMER.YEARLY_BUDGET_AMOUNT,BUDGETCUSTOMER.OVER_BUDGET from BUDGET_CUSTOMER BudgetCustomer where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID=? for update");
                        statement.setString(1, budgetCustIdFdi);
                        ResultSet rs = statement.executeQuery();
                        while (rs.next()) {
                            String BudgetCustId =
                                rs.getString("BUDGET_CUSTOMER_ID").toString();
                            BigDecimal budgetAsTodateCur = BigDecimal.ZERO;
                            BigDecimal budgetAsTodate = BigDecimal.ZERO;
                            budgetAsTodateCur =
                                    new BigDecimal(rs.getString("BUDGET_AS_TO_DATE_USED").toString());
                            budgetAsTodate =
                                    new BigDecimal(rs.getString("BUDGET_AS_TO_DATE").toString());
                            if (budgetCustIdFdi.equalsIgnoreCase(BudgetCustId)) {
                                BigDecimal value =
                                    budgetAsTodateCur.subtract(amountSubs);
                                try {
                                    PreparedStatement updateTtfNumSeq =
                                        conn.prepareStatement("UPDATE BUDGET_CUSTOMER BudgetCustomer SET BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED =? where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID = ?");
                                    updateTtfNumSeq.setBigDecimal(1, value);
                                    updateTtfNumSeq.setString(2, budgetCustIdFdi);
                                    updateTtfNumSeq.executeUpdate();
                                    updateTtfNumSeq.close();
                                } catch (SQLException sqle) {
                                    System.out.println("------------------------------------------------");
                                    System.out.println("ERROR: Cannot run update query");
                                    System.out.println("STACK: " +
                                                       sqle.toString().trim());
                                    System.out.println("------------------------------------------------");
                                }
                            }
                        }
                        conn.commit();
                        statement.close();
                        conn.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    amountSubs = cek.getAmount();
                    
                    DCIteratorBinding dciterCustset =
                        ADFUtils.findIterator("ProdBudgetByView1Iterator");
                    dciterCustset.executeQuery();
                    ViewObject voTableDataset = dciterCustset.getViewObject();
                    while (voTableDataset.hasNext()) {
                        Row rowSelectedset = voTableDataset.next();
                        String budgetIdset =
                            rowSelectedset.getAttribute("BudgetById") == null ? "" :
                            rowSelectedset.getAttribute("BudgetById").toString();
                        if (budgetIdFdi.equalsIgnoreCase(budgetIdset)) {
                            rowSelectedset.setAttribute("Status", "Y");
                        }
                    }
                }
            
                rSel.setAttribute("CloseFlag", "Y");
            
                // Check if last closed promo produk line
                PromoProdukLineClosedViewImpl prodLineClosed = confirmationAM.getPromoProdukLineClosedView1();
                prodLineClosed.setNamedWhereClauseParam("propId", propId.getSequenceNumber());
                prodLineClosed.executeQuery();
                
                oracle.jbo.domain.Number numProdLineClosed = (oracle.jbo.domain.Number)prodLineClosed.first().getAttribute("NumProdClose");
                
                if (numProdLineClosed.compareTo(1) == 0) {
                    DCIteratorBinding dciterProposal =
                        ADFUtils.findIterator("ProposalConfirmationView1Iterator");
                    Row propSel = dciterProposal.getCurrentRow();
                    propSel.setAttribute("Status", proposalClosed);
                }
        
                dciterCust.getDataControl().commitTransaction();
                dciterPromoProduk.getDataControl().commitTransaction();
                BudCust.closeRowSetIterator();
                dciterPromoProduk.setCurrentRowWithKey(promoProdKey.toStringFormat(true));

                AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropConfirmation);
                AdfFacesContext.getCurrentInstance().addPartialTarget(btnClosePr);
                AdfFacesContext.getCurrentInstance().addPartialTarget(btnCloseMod);
                AdfFacesContext.getCurrentInstance().addPartialTarget(btnClosePrmBrg);
            } else {
                System.out.println("FAILED UPDATE EBS, PPID: " + promoProdukId);
                JSFUtils.addFacesWarningMessage("Update status ke EBS gagal, close per-line dibatalkan.");
            }
        } else {
            JSFUtils.addFacesWarningMessage("Kolom \"Close Reason\" harus diisi untuk melakukan close per-line.");
        }
    }

    public void closeDcvAmountRealisasi(ActionEvent actionEvent) {
        BindingContainer bindings = getBindings();
        ConfirmationAMImpl confirmationAM =
            (ConfirmationAMImpl)ADFUtils.getApplicationModuleForDataControl("ConfirmationAMDataControl");
        AttributeBinding propIdAttr =
            (AttributeBinding)bindings.getControlBinding("ProposalId");
        oracle.jbo.domain.DBSequence propId = (oracle.jbo.domain.DBSequence)propIdAttr.getInputValue();
        AttributeBinding discTypeAttr =
            (AttributeBinding)bindings.getControlBinding("DiscountType1");
        String discType = (String)discTypeAttr.getInputValue();
        AttributeBinding mekPenagihanAttr =
            (AttributeBinding)bindings.getControlBinding("MekanismePenagihan1");
        String mekPenagihan = (String)mekPenagihanAttr.getInputValue();
        AttributeBinding DcvFlagAttr =
            (AttributeBinding)bindings.getControlBinding("DcvPcPrFlag");
        String DcvFlag = DcvFlagAttr.getInputValue().toString();

        DCIteratorBinding dciterPromoProduk =
            ADFUtils.findIterator("PromoProdukView1Iterator");
        Key promoProdKey = dciterPromoProduk.getCurrentRow().getKey();
        Row rSel = dciterPromoProduk.getCurrentRow();
        String promoProdukId = rSel.getAttribute("PromoProdukId").toString();
        BigDecimal RealisasiGrMfByLine1 =
            new BigDecimal(rSel.getAttribute("RealisasiGrMfByLine").toString());
        BigDecimal RealisasiModMf =
            new BigDecimal(rSel.getAttribute("RealisasiModMf").toString());
        BigDecimal RealisasiDcvMf =
            new BigDecimal(rSel.getAttribute("RealisasiDcvMf").toString());
        BigDecimal DiscMf =
            new BigDecimal(rSel.getAttribute("DiscMf").toString());
        BigDecimal BrgBonusMf =
            new BigDecimal(rSel.getAttribute("BrgBonusMf").toString());
        BigDecimal BiaMf =
            new BigDecimal(rSel.getAttribute("BiaMf").toString());
        String CloseReason = "";
        try {
            CloseReason = rSel.getAttribute("CloseReason").toString();  
        } catch (NullPointerException e) {
            CloseReason = "";
        } catch (Exception e) {
            //DO NOTHING
        }
        
        if (CloseReason.length() > 0) {
            realisasiGrMfByLine = new ArrayList<RealisasiGrMfByLine>();
            DCIteratorBinding dciterCust =
                ADFUtils.findIterator("ProdBudgetByView1Iterator");
            RowSetIterator BudCust = dciterCust.getRowSetIterator();
            dciterCust.executeQuery();
            ViewObject voTableData = dciterCust.getViewObject();
            BigDecimal realamountmf = new BigDecimal(0);
            if (voTableData.getEstimatedRowCount() > 0) {
                if (discType.equalsIgnoreCase(discTypePotongan)) {
                    if (mekPenagihan.equalsIgnoreCase(offInvoice) &&
                        DcvFlag.equalsIgnoreCase("Y")) {
                        realamountmf = DiscMf.subtract(RealisasiGrMfByLine1);
                    } else if (mekPenagihan.equalsIgnoreCase(onInvoice)) {
                        realamountmf = DiscMf.subtract(RealisasiModMf);
                    } else if (mekPenagihan.equalsIgnoreCase(offInvoice) &&
                               DcvFlag.equalsIgnoreCase("N")) {
                        realamountmf = DiscMf.subtract(RealisasiDcvMf);
                    }
                } else if (discType.equalsIgnoreCase(discTypeBiaya)) {
                    realamountmf = BiaMf.subtract(RealisasiGrMfByLine1);
                } else if (discType.equalsIgnoreCase(discTypePromoBarang)) {
                    if (mekPenagihan.equalsIgnoreCase(offInvoice) &&
                        DcvFlag.equalsIgnoreCase("Y")) {
                        realamountmf = BrgBonusMf.subtract(RealisasiDcvMf);
                    } else if (mekPenagihan.equalsIgnoreCase(offInvoice) &&
                               DcvFlag.equalsIgnoreCase("N")) {
                        realamountmf = BrgBonusMf.subtract(RealisasiGrMfByLine1);
                    }
                }

                while (voTableData.hasNext()) {
                    RealisasiGrMfByLine tempUsed = new RealisasiGrMfByLine();
                    Row rowSelected = voTableData.next();
                    String budgetId =
                        rowSelected.getAttribute("BudgetById") == null ? "" :
                        rowSelected.getAttribute("BudgetById").toString();
                    String promoProdukIdBud =
                        rowSelected.getAttribute("PromoProdukId") == null ? "" :
                        rowSelected.getAttribute("PromoProdukId").toString();
                    String Status =
                        rowSelected.getAttribute("Status") == null ? "" :
                        rowSelected.getAttribute("Status").toString();
                    String BudgetCustId =
                        rowSelected.getAttribute("BudgetCustId") == null ? "" :
                        rowSelected.getAttribute("BudgetCustId").toString();
                    String KombinasiBudget =
                        rowSelected.getAttribute("KombinasiBudget") == null ? "" :
                        rowSelected.getAttribute("KombinasiBudget").toString();
                    String beforeFirstDot = KombinasiBudget.split("\\.")[0];
                    if (promoProdukId.equalsIgnoreCase(promoProdukIdBud) &&
                        Status.equalsIgnoreCase("N")) {
                        tempUsed.setBudgetById(budgetId);
                        tempUsed.setBudgetCustId(BudgetCustId);
                        tempUsed.setKodebudget(beforeFirstDot);
                        realisasiGrMfByLine.add(tempUsed);
                    }
                }
            }
            
            Connection conn = null;
            String budgetCustIdFdi = "";
            BigDecimal amountSubs = BigDecimal.ZERO;
            if (realamountmf.compareTo(BigDecimal.ZERO) < 0 ||
                realamountmf.compareTo(BigDecimal.ZERO) == 0) {
                amountSubs = BigDecimal.ZERO;
            } else {
                amountSubs = realamountmf;
            }
            
            List<RealisasiGrMfByLine> realTempend = getRealisasiGrMfByLine();
            for (RealisasiGrMfByLine cek : realTempend) {
                String budgetIdFdi = "";
                budgetIdFdi = cek.getBudgetById();
                budgetCustIdFdi = cek.getBudgetCustId();
                try {
                    Context ctx = new InitialContext();
                    DataSource ds = (DataSource)ctx.lookup(dsFdiConn);
                    conn = ds.getConnection();
                    conn.setAutoCommit(false);
                    PreparedStatement statement =
                        conn.prepareStatement("select BUDGETCUSTOMER.BUDGET_CUSTOMER_ID ,BUDGETCUSTOMER.BUDGET_AS_TO_DATE, BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED,BUDGETCUSTOMER.YEARLY_BUDGET_AMOUNT,BUDGETCUSTOMER.OVER_BUDGET from BUDGET_CUSTOMER BudgetCustomer where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID=? for update");
                    statement.setString(1, budgetCustIdFdi);
                    ResultSet rs = statement.executeQuery();
                    while (rs.next()) {
                        String BudgetCustId =
                            rs.getString("BUDGET_CUSTOMER_ID").toString();
                        BigDecimal budgetAsTodateCur = BigDecimal.ZERO;
                        BigDecimal budgetAsTodate = BigDecimal.ZERO;
                        budgetAsTodateCur =
                                new BigDecimal(rs.getString("BUDGET_AS_TO_DATE_USED").toString());
                        budgetAsTodate =
                                new BigDecimal(rs.getString("BUDGET_AS_TO_DATE").toString());
                        if (budgetCustIdFdi.equalsIgnoreCase(BudgetCustId)) {
                            BigDecimal value =
                                budgetAsTodateCur.subtract(amountSubs);
                            try {
                                PreparedStatement updateTtfNumSeq =
                                    conn.prepareStatement("UPDATE BUDGET_CUSTOMER BudgetCustomer SET BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED =? where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID = ?");
                                updateTtfNumSeq.setBigDecimal(1, value);
                                updateTtfNumSeq.setString(2, budgetCustIdFdi);
                                updateTtfNumSeq.executeUpdate();
                                updateTtfNumSeq.close();
                            } catch (SQLException sqle) {
                                System.out.println("------------------------------------------------");
                                System.out.println("ERROR: Cannot run update query");
                                System.out.println("STACK: " +
                                                   sqle.toString().trim());
                                System.out.println("------------------------------------------------");
                            }
                        }
                    }
                    conn.commit();
                    statement.close();
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                amountSubs = cek.getAmount();
                DCIteratorBinding dciterCustset =
                    ADFUtils.findIterator("ProdBudgetByView1Iterator");
                dciterCustset.executeQuery();
                ViewObject voTableDataset = dciterCustset.getViewObject();
                while (voTableDataset.hasNext()) {
                    Row rowSelectedset = voTableDataset.next();
                    String budgetIdset =
                        rowSelectedset.getAttribute("BudgetById") == null ? "" :
                        rowSelectedset.getAttribute("BudgetById").toString();
                    if (budgetIdFdi.equalsIgnoreCase(budgetIdset)) {
                        rowSelectedset.setAttribute("Status", "Y");
                    }
                }
            }
            
            rSel.setAttribute("CloseFlag", "Y");
            
            // Check if last closed promo produk line
            PromoProdukLineClosedViewImpl prodLineClosed = confirmationAM.getPromoProdukLineClosedView1();
            prodLineClosed.setNamedWhereClauseParam("propId", propId.getSequenceNumber());
            prodLineClosed.executeQuery();
            
            oracle.jbo.domain.Number numProdLineClosed = (oracle.jbo.domain.Number)prodLineClosed.first().getAttribute("NumProdClose");
            
            if (numProdLineClosed.compareTo(1) == 0) {
                DCIteratorBinding dciterProposal =
                    ADFUtils.findIterator("ProposalConfirmationView1Iterator");
                Row propSel = dciterProposal.getCurrentRow();
                propSel.setAttribute("Status", proposalClosed);
            }

            dciterCust.getDataControl().commitTransaction();
            dciterPromoProduk.getDataControl().commitTransaction();
            BudCust.closeRowSetIterator();
            dciterPromoProduk.setCurrentRowWithKey(promoProdKey.toStringFormat(true));

            AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropConfirmation);
            AdfFacesContext.getCurrentInstance().addPartialTarget(btnClosePr);
            AdfFacesContext.getCurrentInstance().addPartialTarget(btnCloseMod);
            AdfFacesContext.getCurrentInstance().addPartialTarget(btnClosePrmBrg);
        } else {
            JSFUtils.addFacesWarningMessage("Kolom \"Close Reason\" harus diisi untuk melakukan close per-line.");
        }
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


    public void createPrPopupFetchListener(PopupFetchEvent popupFetchEvent) {
        // Save kode posting and kode item first, before create PR
        DCIteratorBinding dciterPromoProduk =
            ADFUtils.findIterator("PromoProdukView1Iterator");
        dciterPromoProduk.getDataControl().commitTransaction();
    }

    public void createModifierPopupFetchListener(PopupFetchEvent popupFetchEvent) {
        // Save kode posting and kode item first, before create PR
        DCIteratorBinding dciterPromoProduk =
            ADFUtils.findIterator("PromoProdukView1Iterator");
        dciterPromoProduk.getDataControl().commitTransaction();
    }

    public void createModifierActionListener(ActionEvent actionEvent) {
        DCBindingContainer bindings =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();

        ConfirmationAMImpl confirmationAM =
            (ConfirmationAMImpl)ADFUtils.getApplicationModuleForDataControl("ConfirmationAMDataControl");

        AttributeBinding noPropAttr =
            (AttributeBinding)bindings.getControlBinding("ProposalNo");
        String noProp = (String)noPropAttr.getInputValue();

        AttributeBinding noConfAttr =
            (AttributeBinding)bindings.getControlBinding("ConfirmNo");
        String noConf = (String)noConfAttr.getInputValue();

        // Cek tanggal mulai periode promo
        AttributeBinding periodeFromAttr =
            (AttributeBinding)bindings.getControlBinding("PeriodeProgFrom");
        String startDateString = (String)periodeFromAttr.getInputValue();
        DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        java.util.Date date = null;
        try {
            date = formatter.parse(startDateString);
        } catch (ParseException e) {
            JSFUtils.addFacesErrorMessage("Error", e.getLocalizedMessage());
        }
        java.util.Date dateCurr = new java.util.Date();

        //Cek validasi DCV
        CheckCmRealisasiDcvImpl cekDcv =
            confirmationAM.getCheckCmRealisasiDcv1();
        cekDcv.setNamedWhereClauseParam("noPp", noProp);
        cekDcv.executeQuery();

        if (cekDcv.getEstimatedRowCount() == 0) {

            // Cek setiap row produk ada yg punya MF ga?,
            // kalau 1 aja ada yg punya harus cek budget.
            AttributeBinding discTypeAttr =
                (AttributeBinding)bindings.getControlBinding("DiscountType1");
            String discType = (String)discTypeAttr.getInputValue();

            boolean mfExist = false;
            Number totalMf = zeroNum;
            Number totalOnTop = zeroNum;

            DCIteratorBinding dcIterPromoProdForCheck =
                bindings.findIteratorBinding("PromoProdukView1Iterator");

            long rowCount = dcIterPromoProdForCheck.getEstimatedRowCount();
            String prodBudgetWarnMsg = "";
            if (date.compareTo(dateCurr) > 0) {
                if (rowCount > 0) {
                    int i = 1;
                    for (Row r : dcIterPromoProdForCheck.getAllRowsInRange()) {
                        String productApproval =
                            (String)r.getAttribute("ProductApproval");
                        if (productApproval.equalsIgnoreCase("Y")) {
                            Integer checkBudgetData =
                                r.getAttribute("CheckBudgetData") == null ? 0 :
                                (Integer)r.getAttribute("CheckBudgetData");

                            Number budgetPercentTotal =
                                r.getAttribute("BudgetPercentTotal") == null ?
                                zeroNum :
                                (Number)r.getAttribute("BudgetPercentTotal");

                            // Validasi budget sudah terisi atau belum.
                            if (discType.equalsIgnoreCase(discTypePotongan)) {
                                totalMf =
                                        r.getAttribute("DiscMf") == null ? zeroNum :
                                        (Number)r.getAttribute("DiscMf");
                                totalOnTop =
                                        r.getAttribute("DiscOnTop") == null ?
                                        zeroNum :
                                        (Number)r.getAttribute("DiscOnTop");
                                mfExist =
                                        r.getAttribute("DiscMf").toString() ==
                                        "0" ? false : true;
                                if (mfExist != false) {
                                    if (checkBudgetData == 0) {
                                        prodBudgetWarnMsg =
                                                prodBudgetWarnMsg + "Budget belum diisi pada produk baris ke-" +
                                                i + ".<nr>";
                                    } else {
                                        if (budgetPercentTotal.compareTo(100) >
                                            0) {
                                            //showPopup("Persentase budget tidak boleh melebihi 100%.", potmessage);
                                            prodBudgetWarnMsg =
                                                    prodBudgetWarnMsg +
                                                    "Persentase budget produk baris ke-" +
                                                    i +
                                                    " tidak boleh melebihi 100%.<nr>";
                                        } else if (budgetPercentTotal.compareTo(100) <
                                                   0) {
                                            //showPopup("Penggunaan budget baru mencapai " + totalPercen + "%, \"Create PR\" tidak dapat dilanjutkan.",  potmessage);
                                            prodBudgetWarnMsg =
                                                    prodBudgetWarnMsg +
                                                    "Penggunaan budget produk baris ke-" +
                                                    i + " baru mencapai " +
                                                    budgetPercentTotal +
                                                    "%, \"Create Modifier\" tidak dapat dilanjutkan.<nr>";
                                        }
                                    }
                                }
                            } else if (discType.equalsIgnoreCase(discTypeBiaya)) {
                                totalMf =
                                        r.getAttribute("BiaMf") == null ? zeroNum :
                                        (Number)r.getAttribute("BiaMf");
                                totalOnTop =
                                        r.getAttribute("BiaOntop") == null ?
                                        zeroNum :
                                        (Number)r.getAttribute("BiaOntop");
                                mfExist =
                                        r.getAttribute("BiaMf").toString() == "0" ?
                                        false : true;
                                if (mfExist != false) {
                                    if (checkBudgetData == 0) {
                                        prodBudgetWarnMsg =
                                                prodBudgetWarnMsg + "Budget belum diisi pada produk baris ke-" +
                                                i + ".<nr>";
                                    } else {
                                        if (budgetPercentTotal.compareTo(100) >
                                            0) {
                                            //showPopup("Persentase budget tidak boleh melebihi 100%.", potmessage);
                                            prodBudgetWarnMsg =
                                                    prodBudgetWarnMsg +
                                                    "Persentase budget produk baris ke-" +
                                                    i +
                                                    " tidak boleh melebihi 100%.<nr>";
                                        } else if (budgetPercentTotal.compareTo(100) <
                                                   0) {
                                            //showPopup("Penggunaan budget baru mencapai " + totalPercen + "%, \"Create PR\" tidak dapat dilanjutkan.",  potmessage);
                                            prodBudgetWarnMsg =
                                                    prodBudgetWarnMsg +
                                                    "Penggunaan budget produk baris ke-" +
                                                    i + " baru mencapai " +
                                                    budgetPercentTotal +
                                                    "%, \"Create Modifier\" tidak dapat dilanjutkan.<nr>";
                                        }
                                    }
                                }
                            } else if (discType.equalsIgnoreCase(discTypePromoBarang)) {
                                totalMf =
                                        r.getAttribute("BrgBonusMf") == null ?
                                        zeroNum :
                                        (Number)r.getAttribute("BrgBonusMf");
                                totalOnTop =
                                        r.getAttribute("BrgBonusOnTop") == null ?
                                        zeroNum :
                                        (Number)r.getAttribute("BrgBonusOnTop");
                                mfExist =
                                        r.getAttribute("BrgBonusMf").toString() ==
                                        "0" ? false : true;
                                if (mfExist != false) {
                                    if (checkBudgetData == 0) {
                                        prodBudgetWarnMsg =
                                                prodBudgetWarnMsg + "Budget belum diisi pada produk baris ke-" +
                                                i + ".<nr>";
                                    } else {
                                        if (budgetPercentTotal.compareTo(100) >
                                            0) {
                                            //showPopup("Persentase budget tidak boleh melebihi 100%.", potmessage);
                                            prodBudgetWarnMsg =
                                                    prodBudgetWarnMsg +
                                                    "Persentase budget produk baris ke-" +
                                                    i +
                                                    " tidak boleh melebihi 100%.<nr>";
                                        } else if (budgetPercentTotal.compareTo(100) <
                                                   0) {
                                            //showPopup("Penggunaan budget baru mencapai " + totalPercen + "%, \"Create Modifier\" tidak dapat dilanjutkan.",  potmessage);
                                            prodBudgetWarnMsg =
                                                    prodBudgetWarnMsg +
                                                    "Penggunaan budget produk baris ke-" +
                                                    i + " baru mencapai " +
                                                    budgetPercentTotal +
                                                    "%, \"Create Modifier\" tidak dapat dilanjutkan.<nr>";
                                        }
                                    }
                                }
                            }
                        }
                        i = i + 1;
                    }
                }

                if (prodBudgetWarnMsg.length() > 0) {
                    showPopup(prodBudgetWarnMsg, potmessage);
                } else {
                    RichPopup.PopupHints hints = new RichPopup.PopupHints();
                    popupCreateModifier.show(hints);
                }
            } else {
                showPopup("Tanggal mulai periode promo harus lebih besar dari tanggal hari ini.<nr>\"Create Modifier\" tidak dapat dilanjutkan.<nr>",
                          potmessage);
            }
            //    }
        } else {
            AttributeBinding dcvFlagAttr =
                (AttributeBinding)bindings.getControlBinding("DcvFlag");
            dcvFlagAttr.setInputValue(onDcv);
            AdfFacesContext.getCurrentInstance().addPartialTarget(btnCreatePr);
            JSFUtils.addFacesInformationMessage("PC dengan no " + noConf +
                                                " sudah pernah ter-generate DCV");
        }
    }


    public void setPopupCreateModifier(RichPopup popupCreateModifier) {
        this.popupCreateModifier = popupCreateModifier;
    }

    public RichPopup getPopupCreateModifier() {
        return popupCreateModifier;
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
            binding.findIteratorBinding("ProposalConfirmationView1Iterator");
        ViewObject voProposal = iterProposal.getViewObject();
        Row rowSelected = voProposal.getCurrentRow();

        UserData userData =
            (UserData)JSFUtils.resolveExpression("#{UserData}");
        String userNameLogin =
            userData.getUserNameLogin() == null ? "" : userData.getUserNameLogin();
        String printCount =
            userData.getPrintCount() == null ? "N" : userData.getPrintCount();

        if (printCount.equalsIgnoreCase(printCountYes)) {
            ConfirmationAMImpl confirmationAM =
                (ConfirmationAMImpl)ADFUtils.getApplicationModuleForDataControl("ConfirmationAMDataControl");
            CallableStatement cst = null;
            try {
                cst =
confirmationAM.getDBTransaction().createCallableStatement("BEGIN FCS_PRINT_PREV_LOG(" +
                                                          propId.getSequenceNumber().getBigDecimalValue() +
                                                          ", '" +
                                                          userNameLogin +
                                                          "'); END;", 0);
                cst.executeUpdate();
            } catch (SQLException e) {
                JSFUtils.addFacesErrorMessage(e.getMessage());
            } finally {
                if (cst != null) {
                    try {
                        cst.close();
                    } catch (SQLException e) {
                        //JSFUtils.addFacesErrorMessage(e.getMessage());
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

    public void biayaQtyValueChangeListener(ValueChangeEvent valueChangeEvent) {
        String errText = "";
        BigDecimal newBiayaQtyBonusValue =
            new BigDecimal(valueChangeEvent.getNewValue() == "" ? "0" :
                           valueChangeEvent.getNewValue() == null ? "0" :
                           valueChangeEvent.getNewValue().toString());
        Number totAmount = new Number(0);
        String BiayaIdSel = "";

        DCBindingContainer bindingsSelRow =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcItteratorBindings =
            bindingsSelRow.findIteratorBinding("BiayaView1Iterator");
        Key parentKey = dcItteratorBindings.getCurrentRow().getKey();
        ViewObject voTableData = dcItteratorBindings.getViewObject();
        Row rowSelected = voTableData.getCurrentRow();

        if (rowSelected.getAttribute("BiayaId") != null) {
            BiayaIdSel = rowSelected.getAttribute("BiayaId").toString();
            totAmount =
                    (Number)rowSelected.getAttribute("BiayaTotAmt") == null ?
                    new Number(0) :
                    (Number)rowSelected.getAttribute("BiayaTotAmt");

            BigDecimal totAmtDec = new BigDecimal(totAmount.getValue());

            BigDecimal newPrice = zeroNum.getBigDecimalValue();
            
            if (valueChangeEvent.getNewValue() == null) {
                Row rowFirst = voTableData.first();
                totAmount = (Number)rowFirst.getAttribute("BiayaTotAmt");
                rowFirst.setAttribute("BiayaQty", null);
                rowFirst.setAttribute("BiayaUom", null);
                rowFirst.setAttribute("BiayaPrice", totAmount);
                itBiaPrice.setSubmittedValue(totAmount);
                readOnlyBiaPrice.setSubmittedValue(totAmount);

                while (voTableData.hasNext()) {
                    Row nextRow = voTableData.next();
                    totAmount = (Number)nextRow.getAttribute("BiayaTotAmt");
                    nextRow.setAttribute("BiayaQty", null);
                    nextRow.setAttribute("BiayaUom", null);
                    nextRow.setAttribute("BiayaPrice", totAmount);
                    itBiaPrice.setSubmittedValue(totAmount);
                    readOnlyBiaPrice.setSubmittedValue(totAmount);
                }

                itLovBiayaUom.setRequired(false);
                AdfFacesContext.getCurrentInstance().addPartialTarget(itLovBiayaUom);
                AdfFacesContext.getCurrentInstance().addPartialTarget(itBiaPrice);
                AdfFacesContext.getCurrentInstance().addPartialTarget(readOnlyBiaPrice);
            } else {            
                if (newBiayaQtyBonusValue.compareTo(zeroNum.getBigDecimalValue()) ==
                    0) {
                    errText =
                            "Nilai dikolom \"Qty\" tidak boleh 0 (nol).";
                    rowSelected.setAttribute("BiayaQty", null);
                    Row rowFirst = voTableData.first();
                    totAmount = (Number)rowFirst.getAttribute("BiayaTotAmt");
                    rowFirst.setAttribute("BiayaQty", null);
                    rowFirst.setAttribute("BiayaUom", null);
                    rowFirst.setAttribute("BiayaPrice", totAmount);
                    itBiaPrice.setSubmittedValue(totAmount);
                    readOnlyBiaPrice.setSubmittedValue(totAmount);
                    itQtyBiaya.setSubmittedValue(null);
    
                    while (voTableData.hasNext()) {
                        Row nextRow = voTableData.next();
                        totAmount = (Number)nextRow.getAttribute("BiayaTotAmt");
                        nextRow.setAttribute("BiayaQty", null);
                        nextRow.setAttribute("BiayaUom", null);
                        nextRow.setAttribute("BiayaPrice", totAmount);
                        itBiaPrice.setSubmittedValue(totAmount);
                        readOnlyBiaPrice.setSubmittedValue(totAmount);
                        itQtyBiaya.setSubmittedValue(null);
                    }
    
                    itLovBiayaUom.setRequired(true);                    
                    AdfFacesContext.getCurrentInstance().addPartialTarget(itLovBiayaUom);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(itBiaPrice);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(itQtyBiaya);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(readOnlyBiaPrice);
                } else {
                    newPrice =
                            totAmtDec.divide(newBiayaQtyBonusValue, 2, RoundingMode.HALF_UP);
    
                    oracle.jbo.domain.Number newPriceFormatted = null;
                    try {
                        newPriceFormatted =
                                new oracle.jbo.domain.Number(df2dgt.format(newPrice).toString());
                    } catch (SQLException e) {
                        JSFUtils.addFacesErrorMessage("Error",
                                                      e.getLocalizedMessage());
                    }
    
                    rowSelected.setAttribute("BiayaPrice", newPriceFormatted);
                    itBiaPrice.setSubmittedValue(newPriceFormatted);
                    readOnlyBiaPrice.setSubmittedValue(newPriceFormatted);
                    itLovBiayaUom.setRequired(true);
    
                    if (newPriceFormatted != null) {
                        OperationBinding operationBinding =
                            bindingsSelRow.getOperationBinding("Commit");
                        operationBinding.execute();
                    }
    
                    AdfFacesContext.getCurrentInstance().addPartialTarget(itLovBiayaUom);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(itBiaPrice);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(readOnlyBiaPrice);
                }
            }
            AdfFacesContext.getCurrentInstance().addPartialTarget(tblBiaya);
            dcItteratorBindings.setCurrentRowWithKey(parentKey.toStringFormat(true));
            if (errText.length() > 0) {
                JSFUtils.addFacesWarningMessage(errText);
            }
        }
    }

    public void setItBiaPrice(RichInputText itBiaPrice) {
        this.itBiaPrice = itBiaPrice;
    }

    public RichInputText getItBiaPrice() {
        return itBiaPrice;
    }

    public void setItQtyBiaya(RichInputText itQtyBiaya) {
        this.itQtyBiaya = itQtyBiaya;
    }

    public RichInputText getItQtyBiaya() {
        return itQtyBiaya;
    }

    public void biayaUomValueChangeListener(ValueChangeEvent valueChangeEvent) {

        String newBiayaUomValue =
            valueChangeEvent.getNewValue() == null ? "" : valueChangeEvent.getNewValue().toString();
        DCBindingContainer bindingsSelRow =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcItteratorBindings =
            bindingsSelRow.findIteratorBinding("BiayaView1Iterator");
        ViewObject voTableData = dcItteratorBindings.getViewObject();

        while (voTableData.hasNext()) {
            Row rowCurr = voTableData.next();
            rowCurr.setAttribute("BiayaUom", newBiayaUomValue);
            itLovBiayaUom.setSubmittedValue(newBiayaUomValue);
        }

        AdfFacesContext.getCurrentInstance().addPartialTarget(tblBiaya);
    }

    public void setItLovBiayaUom(RichInputListOfValues itLovBiayaUom) {
        this.itLovBiayaUom = itLovBiayaUom;
    }

    public RichInputListOfValues getItLovBiayaUom() {
        return itLovBiayaUom;
    }

    public void setTblBiaya(RichTable tblBiaya) {
        this.tblBiaya = tblBiaya;
    }

    public RichTable getTblBiaya() {
        return tblBiaya;
    }

    public void biayaUomReturnPopupListener(ReturnPopupEvent returnPopupEvent) {
        Number totAmount = new Number(0);
        Number oneNum = new Number(1);
        Number biayaQty = new Number(0);
        DCBindingContainer bindingsSelRow =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcItteratorBindings =
            bindingsSelRow.findIteratorBinding("BiayaView1Iterator");
        ViewObject voTableData = dcItteratorBindings.getViewObject();
        Row rowFirst = voTableData.first();
        totAmount = (Number)rowFirst.getAttribute("BiayaTotAmt");
        biayaQty =
                (Number)rowFirst.getAttribute("BiayaQty") == null ? new Number(0) :
                (Number)rowFirst.getAttribute("BiayaQty");
        if (biayaQty.compareTo(zeroNum) == 0) {
            rowFirst.setAttribute("BiayaQty", oneNum);
            rowFirst.setAttribute("BiayaPrice", totAmount);
        }

        while (voTableData.hasNext()) {
            Row nextRow = voTableData.next();
            totAmount = (Number)nextRow.getAttribute("BiayaTotAmt");
            biayaQty =
                    (Number)nextRow.getAttribute("BiayaQty") == null ? new Number(0) :
                    (Number)nextRow.getAttribute("BiayaQty");
            if (biayaQty.compareTo(zeroNum) == 0) {
                nextRow.setAttribute("BiayaQty", oneNum);
            }
            nextRow.setAttribute("BiayaPrice", totAmount);
        }

        AdfFacesContext.getCurrentInstance().addPartialTarget(tblBiaya);
    }

    public void actResetBiaya(ActionEvent actionEvent) {
        Number totAmount = new Number(0);
        DCBindingContainer bindingsSelRow =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcItteratorBindings =
            bindingsSelRow.findIteratorBinding("BiayaView1Iterator");
        ViewObject voTableData = dcItteratorBindings.getViewObject();
        Row rowFirst = voTableData.first();
        totAmount = (Number)rowFirst.getAttribute("BiayaTotAmt");
        rowFirst.setAttribute("BiayaQty", null);
        rowFirst.setAttribute("BiayaUom", null);
        rowFirst.setAttribute("BiayaPrice", totAmount);

        while (voTableData.hasNext()) {
            Row nextRow = voTableData.next();
            totAmount = (Number)nextRow.getAttribute("BiayaTotAmt");
            nextRow.setAttribute("BiayaQty", null);
            nextRow.setAttribute("BiayaUom", null);
            nextRow.setAttribute("BiayaPrice", totAmount);
        }

        itLovBiayaUom.setRequired(false);
        AdfFacesContext.getCurrentInstance().addPartialTarget(itLovBiayaUom);
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblBiaya);
    }

    public void setReadOnlyBiaPrice(RichInputText readOnlyBiaPrice) {
        this.readOnlyBiaPrice = readOnlyBiaPrice;
    }

    public RichInputText getReadOnlyBiaPrice() {
        return readOnlyBiaPrice;
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

    public void setPopupSaveChanges(RichPopup popupSaveChanges) {
        this.popupSaveChanges = popupSaveChanges;
    }

    public RichPopup getPopupSaveChanges() {
        return popupSaveChanges;
    }

    public void saveChangesDialogListener(DialogEvent dialogEvent) {
        BindingContainer bindings = getBindings();

        if (dialogEvent.getOutcome().name().equals("yes")) {
            OperationBinding operationBinding =
                bindings.getOperationBinding("Commit");
            operationBinding.execute();
        } else {
            OperationBinding operationBinding =
                bindings.getOperationBinding("Rollback");
            operationBinding.execute();
        }
    }

    public void lovItemExpenseReturnPopupListener(ReturnPopupEvent returnPopupEvent) {
        BindingContainer bindings = getBindings();

        AttributeBinding DiscountTypeAttr =
            (AttributeBinding)bindings.getControlBinding("DiscountType1");
        String DiscountType =
            (String)DiscountTypeAttr.getInputValue().toString();

        if (DiscountType.equalsIgnoreCase(discTypeBiaya)) {
            ADFContext adfCtx = ADFContext.getCurrent();
            Map pfScope = adfCtx.getPageFlowScope();
            
            Number totAmount = new Number(0);
            DCBindingContainer bindingsSelRow =
                (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
            DCIteratorBinding dcItteratorBindings =
                bindingsSelRow.findIteratorBinding("BiayaView1Iterator");
            ViewObject voTableData = dcItteratorBindings.getViewObject();
            Row rowFirst = voTableData.first();
            totAmount = (Number)rowFirst.getAttribute("BiayaTotAmt");
            rowFirst.setAttribute("BiayaQty", null);
            rowFirst.setAttribute("BiayaUom", null);
            rowFirst.setAttribute("BiayaPrice", totAmount);

            while (voTableData.hasNext()) {
                Row nextRow = voTableData.next();
                totAmount = (Number)nextRow.getAttribute("BiayaTotAmt");
                nextRow.setAttribute("BiayaQty", null);
                nextRow.setAttribute("BiayaUom", null);
                nextRow.setAttribute("BiayaPrice", totAmount);
            }
            OperationBinding operationBindingCommit =
                bindings.getOperationBinding("Commit");
            operationBindingCommit.execute();
            
            String iexVal = pfScope.get("iexVal") == null ? "" : pfScope.get("iexVal").toString(); 
            if (!iexVal.equalsIgnoreCase("")) {
                JSFUtils.addFacesInformationMessage("Detail produk biaya kolom \"Qty.\" dan \"Satuan\" telah di-reset ke nilai default.");
            }
        } else {
            OperationBinding operationBindingCommit =
                bindings.getOperationBinding("Commit");
            operationBindingCommit.execute();
        }

    }

    public void onTableSelect(SelectionEvent selectionEvent) {
        RowKeySet oldKeySet = selectionEvent.getRemovedSet();
        RichTable table = (RichTable)selectionEvent.getSource();
        CollectionModel tableModel = (CollectionModel)table.getValue();
        JUCtrlHierBinding adfTableBinding =
            (JUCtrlHierBinding)tableModel.getWrappedData();
        ControllerContext cctx = ControllerContext.getInstance();
            DCBindingContainer dcBindingContainer=(DCBindingContainer)
        BindingContext.getCurrent().getCurrentBindingsEntry();
        if (dcBindingContainer.getDataControl().isTransactionModified()) {
            RichPopup.PopupHints hints = new RichPopup.PopupHints();
            popupSaveChanges.show(hints); 
            FacesContext fctx = FacesContext.getCurrentInstance();
            table.setSelectedRowKeys(oldKeySet);
            AdfFacesContext adfFacesCtx = AdfFacesContext.getCurrentInstance();
            adfFacesCtx.addPartialTarget(table);
            fctx.renderResponse();
        } else {
            try {
                JUCtrlHierNodeBinding tableRowBinding =
                    (JUCtrlHierNodeBinding)table.getSelectedRowData();
                Row row = tableRowBinding.getRow();
                //access the iterator used by the table binding
                DCIteratorBinding iter =
                    adfTableBinding.getDCIteratorBinding();
                iter.getRowSetIterator().setCurrentRow(row);
            } catch (Exception e) {
                // DO NOTHING
                System.out.println("ERR CONF GET ROW: " + e.getLocalizedMessage());
            }
        }
    }
    
    public String modifierUpdateCloseToEbs(String confirmNo, 
                                     String addendumKe, String ppid) {
        //Close untuk header isi param PPID dengan angka 0 (nol)
        //Close untuk per line isi param PPID dengan promo produk id nya
        
        addendumKe = addendumKe == null ? "0" : addendumKe;
        
        ConfirmationAMImpl confirmationAM =
            (ConfirmationAMImpl)ADFUtils.getApplicationModuleForDataControl("ConfirmationAMDataControl");
        CallableStatement cst = null;
        String responMsg = null;
        
        try {
            cst =
    confirmationAM.getDBTransaction().createCallableStatement("BEGIN APPS.FCS_PPPC_UPDATE_CLOSE.MODIFIER_UPDATE_STATUS('" +
                                                          confirmNo + "', '"+addendumKe+"', "+ppid+", ?); END;", 0);
            cst.registerOutParameter(1, Types.VARCHAR);
            cst.executeUpdate();
            responMsg = cst.getString(1);
            
            if (responMsg.toString().equalsIgnoreCase(responFailed)) {
                JSFUtils.addFacesWarningMessage("Gagal mengupdate status close ke EBS, close per-line tidak dapat dilakukan.");
            } else {
                if (!ppid.equalsIgnoreCase("0")) {
                    JSFUtils.addFacesInformationMessage("Proses close per-line berhasil dilakukan.");
                } else {
                    JSFUtils.addFacesInformationMessage("Proses close PC berhasil dilakukan.");
                }
            }
        } catch (SQLException e) {
            JSFUtils.addFacesErrorMessage(e.getMessage());
        } finally {
            if (cst != null) {
                try {
                    cst.close();
                } catch (SQLException e) {
                    JSFUtils.addFacesErrorMessage(e.getMessage());
                }
            }
        }
        
        return responMsg;
    }

    public void closePromoBarangLine(ActionEvent actionEvent) {
        BindingContainer bindings = getBindings();
        ConfirmationAMImpl confirmationAM =
            (ConfirmationAMImpl)ADFUtils.getApplicationModuleForDataControl("ConfirmationAMDataControl");
        AttributeBinding propIdAttr =
            (AttributeBinding)bindings.getControlBinding("ProposalId");
        oracle.jbo.domain.DBSequence propId = (oracle.jbo.domain.DBSequence)propIdAttr.getInputValue();
        AttributeBinding discTypeAttr =
            (AttributeBinding)bindings.getControlBinding("DiscountType1");
        String discType = (String)discTypeAttr.getInputValue();
        AttributeBinding mekPenagihanAttr =
            (AttributeBinding)bindings.getControlBinding("MekanismePenagihan1");
        String mekPenagihan = (String)mekPenagihanAttr.getInputValue();
        AttributeBinding DcvFlagAttr =
            (AttributeBinding)bindings.getControlBinding("DcvPcPrFlag");
        String DcvFlag = DcvFlagAttr.getInputValue().toString();
        AttributeBinding AddendumKeAttr =
            (AttributeBinding)bindings.getControlBinding("Addendumke");
        String addendumKe = "0";
        try {
            addendumKe = AddendumKeAttr.getInputValue().toString();
        } catch (NullPointerException e) {
            addendumKe = "0";
        } catch (Exception e) {
            //DO NOTHING
        }

        DCIteratorBinding dciterPromoProduk =
            ADFUtils.findIterator("PromoProdukView1Iterator");
        Key promoProdKey = dciterPromoProduk.getCurrentRow().getKey();
        Row rSel = dciterPromoProduk.getCurrentRow();
        String promoProdukId = rSel.getAttribute("PromoProdukId").toString();
        BigDecimal RealisasiGrMfByLine1 =
            new BigDecimal(rSel.getAttribute("RealisasiGrMfByLine").toString());
        BigDecimal RealisasiModMf =
            new BigDecimal(rSel.getAttribute("RealisasiModMf").toString());
        BigDecimal RealisasiPrmBrgByLine =
            new BigDecimal(rSel.getAttribute("RealisasiPrmBrgByLine").toString());
        BigDecimal RealisasiDcvMf =
            new BigDecimal(rSel.getAttribute("RealisasiDcvMf").toString());
        BigDecimal DiscMf =
            new BigDecimal(rSel.getAttribute("DiscMf").toString());
        BigDecimal BrgBonusMf =
            new BigDecimal(rSel.getAttribute("BrgBonusMf").toString());
        BigDecimal BiaMf =
            new BigDecimal(rSel.getAttribute("BiaMf").toString());
        String CloseReason = "";
        try {
            CloseReason = rSel.getAttribute("CloseReason").toString();  
        } catch (NullPointerException e) {
            CloseReason = "";
        } catch (Exception e) {
            //DO NOTHING
        }
        
        if (CloseReason.length() > 0) {                  
            rSel.setAttribute("CloseFlag", "Y");
            
            // Check if last closed promo produk line
            PromoProdukLineClosedViewImpl prodLineClosed = confirmationAM.getPromoProdukLineClosedView1();
            prodLineClosed.setNamedWhereClauseParam("propId", propId.getSequenceNumber());
            prodLineClosed.executeQuery();
            
            oracle.jbo.domain.Number numProdLineClosed = (oracle.jbo.domain.Number)prodLineClosed.first().getAttribute("NumProdClose");
            
            if (numProdLineClosed.compareTo(1) == 0) {
                DCIteratorBinding dciterProposal =
                    ADFUtils.findIterator("ProposalConfirmationView1Iterator");
                Row propSel = dciterProposal.getCurrentRow();
                propSel.setAttribute("Status", proposalClosed);
            }
            
            dciterPromoProduk.getDataControl().commitTransaction();
            dciterPromoProduk.setCurrentRowWithKey(promoProdKey.toStringFormat(true));
                        realisasiGrMfByLine = new ArrayList<RealisasiGrMfByLine>();
            DCIteratorBinding dciterCust =
                ADFUtils.findIterator("ProdBudgetByView1Iterator");
            RowSetIterator BudCust = dciterCust.getRowSetIterator();
            dciterCust.executeQuery();
            ViewObject voTableData = dciterCust.getViewObject();
            BigDecimal realamountmf = new BigDecimal(0);
            if (voTableData.getEstimatedRowCount() > 0) {
                if (discType.equalsIgnoreCase(discTypePotongan)) {
                    if (mekPenagihan.equalsIgnoreCase(offInvoice) &&
                        DcvFlag.equalsIgnoreCase("Y")) {
                        realamountmf = DiscMf.subtract(RealisasiGrMfByLine1);
                    } else if (mekPenagihan.equalsIgnoreCase(onInvoice)) {
                        realamountmf = DiscMf.subtract(RealisasiModMf);
                    } else if (mekPenagihan.equalsIgnoreCase(offInvoice) &&
                               DcvFlag.equalsIgnoreCase("N")) {
                        realamountmf = DiscMf.subtract(RealisasiDcvMf);
                    }
                } else if (discType.equalsIgnoreCase(discTypeBiaya)) {
                    realamountmf = BiaMf.subtract(RealisasiGrMfByLine1);
                } else if (discType.equalsIgnoreCase(discTypePromoBarang)) {
                    if (mekPenagihan.equalsIgnoreCase(offInvoice) &&
                        DcvFlag.equalsIgnoreCase("Y")) {
                        realamountmf = BrgBonusMf.subtract(RealisasiDcvMf);
                    } else if (mekPenagihan.equalsIgnoreCase(offInvoice) &&
                               DcvFlag.equalsIgnoreCase("N")) {
                        realamountmf = BrgBonusMf.subtract(RealisasiGrMfByLine1);
                    } else if (mekPenagihan.equalsIgnoreCase(onInvoice) &&
                               DcvFlag.equalsIgnoreCase("N")) {
                        realamountmf = BrgBonusMf.subtract(RealisasiPrmBrgByLine);
                    }
                }
    
                while (voTableData.hasNext()) {
                    RealisasiGrMfByLine tempUsed = new RealisasiGrMfByLine();
                    Row rowSelected = voTableData.next();
                    String budgetId =
                        rowSelected.getAttribute("BudgetById") == null ? "" :
                        rowSelected.getAttribute("BudgetById").toString();
                    String promoProdukIdBud =
                        rowSelected.getAttribute("PromoProdukId") == null ? "" :
                        rowSelected.getAttribute("PromoProdukId").toString();
                    String Status =
                        rowSelected.getAttribute("Status") == null ? "" :
                        rowSelected.getAttribute("Status").toString();
                    String BudgetCustId =
                        rowSelected.getAttribute("BudgetCustId") == null ? "" :
                        rowSelected.getAttribute("BudgetCustId").toString();
                    String KombinasiBudget =
                        rowSelected.getAttribute("KombinasiBudget") == null ? "" :
                        rowSelected.getAttribute("KombinasiBudget").toString();
                    String beforeFirstDot = KombinasiBudget.split("\\.")[0];
                    if (promoProdukId.equalsIgnoreCase(promoProdukIdBud) &&
                        Status.equalsIgnoreCase("N")) {
                        tempUsed.setBudgetById(budgetId);
                        tempUsed.setBudgetCustId(BudgetCustId);
                        tempUsed.setKodebudget(beforeFirstDot);
                        realisasiGrMfByLine.add(tempUsed);
                    }
                }
            }
            
            Connection conn = null;
            String budgetCustIdFdi = "";
            BigDecimal amountSubs = BigDecimal.ZERO;
            if (realamountmf.compareTo(BigDecimal.ZERO) < 0 ||
                realamountmf.compareTo(BigDecimal.ZERO) == 0) {
                amountSubs = BigDecimal.ZERO;
            } else {
                amountSubs = realamountmf;
            }
            
            List<RealisasiGrMfByLine> realTempend = getRealisasiGrMfByLine();
            for (RealisasiGrMfByLine cek : realTempend) {
                String budgetIdFdi = "";
                budgetIdFdi = cek.getBudgetById();
                budgetCustIdFdi = cek.getBudgetCustId();
                try {
                    Context ctx = new InitialContext();
                    DataSource ds = (DataSource)ctx.lookup(dsFdiConn);
                    conn = ds.getConnection();
                    conn.setAutoCommit(false);
                    PreparedStatement statement =
                        conn.prepareStatement("SELECT BUDGETCUSTOMER.BUDGET_CUSTOMER_ID ,BUDGETCUSTOMER.BUDGET_AS_TO_DATE, BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED,BUDGETCUSTOMER.YEARLY_BUDGET_AMOUNT,BUDGETCUSTOMER.OVER_BUDGET from BUDGET_CUSTOMER BudgetCustomer where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID=? for update");
                    statement.setString(1, budgetCustIdFdi);
                    ResultSet rs = statement.executeQuery();
                    while (rs.next()) {
                        String BudgetCustId =
                            rs.getString("BUDGET_CUSTOMER_ID").toString();
                        BigDecimal budgetAsTodateCur = BigDecimal.ZERO;
                        BigDecimal budgetAsTodate = BigDecimal.ZERO;
                        budgetAsTodateCur =
                                new BigDecimal(rs.getString("BUDGET_AS_TO_DATE_USED").toString());
                        budgetAsTodate =
                                new BigDecimal(rs.getString("BUDGET_AS_TO_DATE").toString());
                        if (budgetCustIdFdi.equalsIgnoreCase(BudgetCustId)) {
                            BigDecimal value =
                                budgetAsTodateCur.subtract(amountSubs);
                            try {
                                PreparedStatement updateTtfNumSeq =
                                    conn.prepareStatement("UPDATE BUDGET_CUSTOMER BudgetCustomer SET BUDGETCUSTOMER.BUDGET_AS_TO_DATE_USED =? where BUDGETCUSTOMER.BUDGET_CUSTOMER_ID = ?");
                                updateTtfNumSeq.setBigDecimal(1, value);
                                updateTtfNumSeq.setString(2, budgetCustIdFdi);
                                updateTtfNumSeq.executeUpdate();
                                updateTtfNumSeq.close();
                            } catch (SQLException sqle) {
                                System.out.println("------------------------------------------------");
                                System.out.println("ERROR: Cannot run update query");
                                System.out.println("STACK: " +
                                                   sqle.toString().trim());
                                System.out.println("------------------------------------------------");
                            }
                        }
                    }
                    conn.commit();
                    statement.close();
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                amountSubs = cek.getAmount();
                
                DCIteratorBinding dciterCustset =
                    ADFUtils.findIterator("ProdBudgetByView1Iterator");
                RowSetIterator BudCustset = dciterCustset.getRowSetIterator();
                dciterCustset.executeQuery();
                ViewObject voTableDataset = dciterCustset.getViewObject();
                while (voTableDataset.hasNext()) {
                    Row rowSelectedset = voTableDataset.next();
                    String budgetIdset =
                        rowSelectedset.getAttribute("BudgetById") == null ? "" :
                        rowSelectedset.getAttribute("BudgetById").toString();
                    if (budgetIdFdi.equalsIgnoreCase(budgetIdset)) {
                        rowSelectedset.setAttribute("Status", "Y");
                    }
                }
            }
            rSel.setAttribute("CloseFlag", "Y");
    
            dciterCust.getDataControl().commitTransaction();
            dciterPromoProduk.getDataControl().commitTransaction();
            BudCust.closeRowSetIterator();
            dciterPromoProduk.setCurrentRowWithKey(promoProdKey.toStringFormat(true));

            AdfFacesContext.getCurrentInstance().addPartialTarget(tblPropConfirmation);
            AdfFacesContext.getCurrentInstance().addPartialTarget(btnClosePr);
            AdfFacesContext.getCurrentInstance().addPartialTarget(btnCloseMod);
            AdfFacesContext.getCurrentInstance().addPartialTarget(btnClosePrmBrg);
        } else {
            JSFUtils.addFacesWarningMessage("Kolom \"Close Reason\" harus diisi untuk melakukan close per-line.");
        }
    }

    public void setBtnClosePrmBrg(RichCommandButton btnClosePrmBrg) {
        this.btnClosePrmBrg = btnClosePrmBrg;
    }

    public RichCommandButton getBtnClosePrmBrg() {
        return btnClosePrmBrg;
    }

    public void setBtnCloseMod(RichCommandButton btnCloseMod) {
        this.btnCloseMod = btnCloseMod;
    }

    public RichCommandButton getBtnCloseMod() {
        return btnCloseMod;
    }

    public void setBtnClosePr(RichCommandButton btnClosePr) {
        this.btnClosePr = btnClosePr;
    }

    public RichCommandButton getBtnClosePr() {
        return btnClosePr;
    }
}
