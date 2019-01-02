package app.fpp.bean.approval;

import app.fpp.adfextensions.ADFUtils;
import app.fpp.adfextensions.JSFUtils;
import app.fpp.bean.useraccessmenu.UserData;
import app.fpp.model.am.ApprovalAMImpl;
import app.fpp.model.am.PromoProposalAMImpl;
import app.fpp.model.views.approval.ApprovalReceiverApproveProposalViewImpl;
import app.fpp.model.views.approval.ProposalApprovalViewImpl;
import app.fpp.model.views.masterdata.ebs.FcsViewCategCombinationViewImpl;
import app.fpp.model.views.promoproposal.ProdukItemViewImpl;
import app.fpp.model.views.promoproposal.ProdukVariantViewImpl;
import app.fpp.model.views.promoproposal.PromoProdukViewImpl;
import app.fpp.model.views.promoproposal.validation.ProdVariantValidationViewImpl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import java.sql.CallableStatement;
import java.sql.SQLException;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import oracle.adf.controller.ControllerContext;
import oracle.adf.model.AttributeBinding;
import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;
import oracle.adf.view.faces.bi.component.pivotTable.CellFormat;
import oracle.adf.view.faces.bi.component.pivotTable.DataCellContext;
import oracle.adf.view.faces.bi.component.pivotTable.HeaderCellContext;
import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.RichQuery;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.data.RichTreeTable;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputListOfValues;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.input.RichSelectOneRadio;
import oracle.adf.view.rich.component.rich.layout.RichShowDetailItem;
import oracle.adf.view.rich.component.rich.nav.RichCommandButton;
import oracle.adf.view.rich.component.rich.nav.RichCommandImageLink;
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
import oracle.adf.view.rich.model.QueryDescriptor;
import oracle.adf.view.rich.model.QueryModel;

import oracle.adf.view.rich.render.ClientEvent;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

import oracle.jbo.JboException;
import oracle.jbo.Key;
import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;
import oracle.jbo.ViewObject;
import oracle.jbo.domain.DBSequence;
import oracle.jbo.domain.Number;

import oracle.jbo.uicli.binding.JUCtrlHierBinding;
import oracle.jbo.uicli.binding.JUCtrlHierNodeBinding;

import org.apache.myfaces.trinidad.component.UIXSwitcher;
import org.apache.myfaces.trinidad.event.DisclosureEvent;
import org.apache.myfaces.trinidad.event.ReturnEvent;
import org.apache.myfaces.trinidad.event.RowDisclosureEvent;
import org.apache.myfaces.trinidad.event.SelectionEvent;
import org.apache.myfaces.trinidad.model.CollectionModel;
import org.apache.myfaces.trinidad.model.RowKeySet;

public class ApprovalPromoBean {
    private RichTable tblApproval;
    private RichTable tblDocHistory;
    private final Integer APPROVED = 0;
    private final Integer REJECTED = 1;
    private final Integer FORWARD = 2;
    private final String docStatusInprocess = "INPROCESS";
    private final String docStatusActive = "ACTIVE";
    private final String docStatusReject = "REJECTED";
    private RichTable tblListProduct;
    private RichInputText itTargetPercentage;
    private RichInputText itTargetValue;
    private RichInputText itTargetQty;
    private RichInputText itTargetHarga;
    private RichInputText itValueTotal;
    private RichTable tblListProductAddBuy;
    private RichInputListOfValues itlovProdClassAddBuy;
    private RichInputListOfValues itlovProdBrandAddBuy;
    private RichInputListOfValues itlovProdExtAddBuy;
    private RichInputListOfValues itlovProdPackAddBuy;
    private RichTable tblListProductBonus;
    private RichInputListOfValues itlovProdClassBonus;
    private RichInputListOfValues itlovProdBrandBonus;
    private RichInputListOfValues itlovProdExtBonus;
    private RichInputListOfValues itlovProdPackBonus;
    private RichInputListOfValues itlovProdClass;
    private RichInputListOfValues itlovProdBrand;
    private RichInputListOfValues itlovProdExtention;
    private RichInputListOfValues itlovProdPackaging;
    private UIXSwitcher switchRegCust;
    private static String propArea = "AREA";
    private static String propCustomer = "CUSTOMER";
    private static String propRegion = "REGION";
    private static String propLocation = "LOCATION";
    private static String propCustType = "CUSTTYPE";
    private static String propCustGroup = "CUSTGROUP";
    private static String prodArea = "AREA";
    private static String prodCustomer = "CUSTOMER";
    private static String prodRegion = "REGION";
    private static String prodLocation = "LOCATION";
    private static String prodCustType = "CUSTTYPE";
    private static String prodCustGroup = "CUSTGROUP";
    private static String onInvoice = "ONINVOICE";
    private static String userHo = "HO";
    private static String userArea = "AREA";
    private RichSelectOneRadio rbApprovalAction;
    private String checkCanApprove;
    private RichInputText itReasonApproval;
    private RichPopup potmessage;
    private RichOutputFormatted otpesan;
    private RichPopup pdetailProduct;
    private RichShowDetailItem tabTargerCustomer;
    private RichShowDetailItem tabBiaya;
    private RichShowDetailItem tabPromoBarang;
    private RichShowDetailItem tabPotongan;
    private RichShowDetailItem tabTargetAndBudget;
    private RichPopup papprove;
    private DecimalFormat df = new DecimalFormat("###");
    private DecimalFormat df2dgt = new DecimalFormat("###.##");
    private UIXSwitcher switchExclCust;
    private RichInputText itBonusVariant;
    private RichInputText itVariant;
    private RichTable tableListPotongan;
    private RichInputListOfValues socTypePotongan;
    BigDecimal valueMf = BigDecimal.ZERO;
    private BigDecimal bdHundred = new BigDecimal("100");
    private RichInputText otOnTop;
    private RichInputText otMF;
    private RichInputText rowOntop;
    private RichInputText rowMf;
    private RichInputText otRasioOntop;
    private RichInputText otRasioMf;
    private RichInputText otRasioTotal;
    private static String userCustInvalid = "INVALID";
    private RichInputText itPromoBonusOntop;
    private RichInputText itPromoBonusMf;
    private RichInputText otBrgOnTop;
    private RichInputText otBrgMf;
    private RichInputText otBrgRasioOnTop;
    private RichInputText otBrgRasioMf;
    private RichInputText otBrgRasioTotal;
    private RichOutputText itCategory;
    private RichInputListOfValues itLovProdCategory;
    private String prodCatCodeFood = "CT001";
    private String prodCatCodeNonFood = "CT002";
    private String prodDescCodeFood = "FOOD";
    private String prodDescCodeNonFood = "NON FOOD";
    private RichInputText itProductItem;
    private RichCommandImageLink linkProduct;
    private RichCommandImageLink linkVariant;
    private static String backDateBlockRegion = "R1-0";
    private RichInputText otBiaOntop;
    private RichInputText otBiaMf;
    private RichInputText otBiaRasioOntop;
    private RichInputText otBiaRasioMf;
    private RichInputText otBiaRasioTotal;
    private RichInputText rowBiaOntop;
    private RichInputText rowBiaMf;
    private RichInputDate idPeriodeTo;
    private RichInputDate idPeriodProgFrom;
    private RichOutputText otCountDays;
    private RichInputDate idHistFrom;
    private RichInputDate idHistTo;
    private RichInputText itCountDays;
    private UIXSwitcher switchMain;
    private RichSelectOneChoice socForwardTo;
    private RichInputText itUserForward;
    private RichOutputText otHistFrom;
    private RichOutputText otHistTo;
    private Number zeroNum = new Number(0);
    private Number maxNumber = new Number(999999);
    private RichSelectOneChoice propTypeVal;
    private RichCommandButton btnOkpromoDetail;
    private RichInputListOfValues lovTipePerhitungan;
    private RichInputText itqtyFromDisc;
    private RichInputText itKelipatanDisc;
    private static String discTypePotongan = "POTONGAN";
    private static String discTypeBiaya = "BIAYA";
    private static String discTypePromoBarang = "PROMOBARANG";
    private RichCommandToolbarButton btnAttachment;
    private RichInputText itReasonCanceled;
    private String initApprovalList;
    private String tipeHitungKelipatan = "KELIPATAN";
    private String tipeHitungTdkKelipatan = "TDKKELIPATAN";
    private String printCountYes = "Y";
    private RichCommandButton btnPrintPreview;
    private String tipePotonganPercent = "PERCENT";
    private String tipePotonganAmount = "AMOUNT";
    private RichInputText itPricePromoBonus;
    private RichInputText itQtyMulPrice;
    private RichInputListOfValues itlovPriceBasedPromoBarang;
    private RichCommandButton btnBonusPriceList;
    private RichPopup ppriceListpromoBarang;
    private RichQuery qryPriceList;
    private RichInputText itqtyToDisc;
    private RichInputText itValidComb;
    private RichInputText itTotAmt;
    private RichInputText itBiaPrice;
    private UIXSwitcher switchButtonMain;
    private RichPopup popupSaveChanges;
    private RichShowDetailItem tabCustRegion;
    private RichShowDetailItem tabCustArea;
    private RichShowDetailItem tabCustLocation;
    private RichShowDetailItem tabCustType;
    private RichShowDetailItem tabCustGroup;
    private RichShowDetailItem tabCustName;
    private static String varAll = "ALL";

    public ApprovalPromoBean() {
    }

    public BindingContainer getBindings() {
        return BindingContext.getCurrent().getCurrentBindingsEntry();
    }

    public void proposalApproveRejectDialog(DialogEvent dialogEvent) {
        BindingContainer bindings = getBindings();
        boolean mustRefreshPage = true;
        UserData userData =
            (UserData)JSFUtils.resolveExpression("#{UserData}");
        String usrName =
            userData.getUserNameLogin() == null ? "" : userData.getUserNameLogin();

        AttributeBinding aprvlActionAttr =
            (AttributeBinding)bindings.getControlBinding("Action");
        Integer aprvlAction = (Integer)aprvlActionAttr.getInputValue();

        AttributeBinding aprvlCodeAttr =
            (AttributeBinding)bindings.getControlBinding("AprvlCode");
        String aprvlCode = (String)aprvlCodeAttr.getInputValue();

        DCIteratorBinding dciterApproval =
            ADFUtils.findIterator("ProposalApprovalView1Iterator");
        Key approvalKey = dciterApproval.getCurrentRow().getKey();

        if (dialogEvent.getOutcome().name().equals("ok")) {
            try {
                if (aprvlAction.equals(APPROVED)) {
                    // Validasi produk harus ada
                    DCIteratorBinding dciterProduk =
                        ADFUtils.findIterator("PromoProdukView1Iterator");
                    Integer cekProduk = (int)dciterProduk.getEstimatedRowCount();
                    if (cekProduk > 0) {
                        AttributeBinding discTypeAttr =
                            (AttributeBinding)bindings.getControlBinding("DiscountType1");
                        String discType = (String)discTypeAttr.getInputValue();
                        AttributeBinding userTypeAttr =
                            (AttributeBinding)bindings.getControlBinding("UserTypeCreator");
                        String userTypeCreator = (String)userTypeAttr.getInputValue();
                        DCIteratorBinding dciterCustHo =
                            ADFUtils.findIterator("PromoCustomerHoView1Iterator");
                
                        Integer cekCustHo = (int)dciterCustHo.getEstimatedRowCount();
                        
                        if (userTypeCreator.equalsIgnoreCase(userHo)) {
                            Boolean isSavedValid = true;
                            String sSavedMsg = "";
                            if (!(cekCustHo > 0)) {
                                if (!isSavedValid) {
                                    sSavedMsg += ", ";
                                }
                                sSavedMsg +=
                                        "Tab \"Customer\" pada detail produk masih kosong, harus diisi.";
                                isSavedValid = false;
                            }
                            if (isSavedValid) {
                                // DO APPROVE PROCESS FOR HO PROPOSAL
                                DCIteratorBinding dciter =
                                    ADFUtils.findIterator("ApprovalReceiverApproveProposalView1Iterator");
                                ApprovalReceiverApproveProposalViewImpl apvrlReceiverVO =
                                    (ApprovalReceiverApproveProposalViewImpl)dciter.getViewObject();
                                apvrlReceiverVO.setNamedWhereClauseParam("aprvlCode",
                                                                         aprvlCode);
                                apvrlReceiverVO.setNamedWhereClauseParam("usrName",
                                                                         usrName);
                                apvrlReceiverVO.executeQuery();
            
                                if (apvrlReceiverVO.getEstimatedRowCount() > 0) {
                                    AttributeBinding propStatus =
                                        (AttributeBinding)bindings.getControlBinding("Status1");
                                    propStatus.setInputValue(docStatusInprocess);
                                } else {
                                    AttributeBinding propStatus =
                                        (AttributeBinding)bindings.getControlBinding("Status1");
                                    propStatus.setInputValue(docStatusActive);
                                }
            
                                OperationBinding operationApproveApproval =
                                    bindings.getOperationBinding("approveDocApproval");
                                operationApproveApproval.execute();

                                switchMain.setFacetName("nodata");
                                switchButtonMain.setFacetName("nodata");
                                AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
                                AdfFacesContext.getCurrentInstance().addPartialTarget(switchButtonMain);  
                                mustRefreshPage = true;
                            } else {
                                showPopup(sSavedMsg, potmessage);
                                mustRefreshPage = false;
                            }
                        } else {
                            Boolean isIterValid = true;
                            String sIterMsg = "";
                            if (discType.equalsIgnoreCase("BIAYA")) {
                                DCIteratorBinding dciterBiaya =
                                    ADFUtils.findIterator("BiayaView1Iterator");
                                if (dciterBiaya.getEstimatedRowCount() < 1) {
                                    if (!isIterValid) {
                                        sIterMsg += ", ";
                                    }
                                    sIterMsg += "Biaya Pada Produk Detail";
                                    isIterValid = false;
                                }
                            } else if (discType.equalsIgnoreCase("PROMOBARANG")) {
                                DCIteratorBinding dciterPromoBonus =
                                    ADFUtils.findIterator("PromoBonusView1Iterator");
                                if (dciterPromoBonus.getEstimatedRowCount() < 1) {
                                    if (!isIterValid) {
                                        sIterMsg += ", ";
                                    }
                                    sIterMsg += "Promo Pada Produk Detail";
                                    isIterValid = false;
                                }
                            } else if (discType.equalsIgnoreCase("POTONGAN")) {
                                DCIteratorBinding dciterDiscount =
                                    ADFUtils.findIterator("DiscountView1Iterator");
                                if (dciterDiscount.getEstimatedRowCount() < 1) {
                                    if (!isIterValid) {
                                        sIterMsg += ", ";
                                    }
                                    sIterMsg += "Potongan Pada Produk Detail";
                                    isIterValid = false;
                                }
                            }
                            if (userTypeCreator.equalsIgnoreCase(userArea)) {
                                DCIteratorBinding dciterCustArea =
                                    ADFUtils.findIterator("PromoCustomerAreaView1Iterator");
                
                                Integer cekUserArea = (int)dciterCustArea.getEstimatedRowCount();
                                if (!(cekUserArea > 0)) {
                                    if (!isIterValid) {
                                        sIterMsg += ", ";
                                    }
                                    sIterMsg += "Customer";
                                    isIterValid = false;
                                }
                            }
                            if (isIterValid) {
                                // DO APPROVE PROCESS FOR AREA PROPOSAL
                                DCIteratorBinding dciter =
                                    ADFUtils.findIterator("ApprovalReceiverApproveProposalView1Iterator");
                                ApprovalReceiverApproveProposalViewImpl apvrlReceiverVO =
                                    (ApprovalReceiverApproveProposalViewImpl)dciter.getViewObject();
                                apvrlReceiverVO.setNamedWhereClauseParam("aprvlCode",
                                                                         aprvlCode);
                                apvrlReceiverVO.setNamedWhereClauseParam("usrName",
                                                                         usrName);
                                apvrlReceiverVO.executeQuery();
            
                                if (apvrlReceiverVO.getEstimatedRowCount() > 0) {
                                    AttributeBinding propStatus =
                                        (AttributeBinding)bindings.getControlBinding("Status1");
                                    propStatus.setInputValue(docStatusInprocess);
                                } else {
                                    AttributeBinding propStatus =
                                        (AttributeBinding)bindings.getControlBinding("Status1");
                                    propStatus.setInputValue(docStatusActive);
                                }
            
                                OperationBinding operationApproveApproval =
                                    bindings.getOperationBinding("approveDocApproval");
                                operationApproveApproval.execute();

                                switchMain.setFacetName("nodata");
                                switchButtonMain.setFacetName("nodata");
                                AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
                                AdfFacesContext.getCurrentInstance().addPartialTarget(switchButtonMain);
                                mustRefreshPage = true;
                            } else {
                                showPopup("\"" + sIterMsg + "\" masih kosong, harus diisi.", potmessage);
                                mustRefreshPage = false;
                            }
                        }
                    } else {
                        showPopup("Proposal tidak memiliki list produk, mohon dilengkapi atau di \"Reject\".", potmessage);
                        mustRefreshPage = false;
                    }
                } else if (aprvlAction.equals(REJECTED)) {
                    AttributeBinding rejectReason =
                        (AttributeBinding)bindings.getControlBinding("Reason");
                    String reasonStr =
                        rejectReason.getInputValue() == null ? "" :
                        (String)rejectReason.getInputValue();
                    
					//if (!reasonStr.trim().equalsIgnoreCase("")) {
                    if (reasonStr.length() >= 10) {
                        AttributeBinding forwardTo =
                            (AttributeBinding)bindings.getControlBinding("ForwardTo1");
                        String isForward =
                            forwardTo.getInputValue() == null ? "N" : "Y";
                        if (isForward.equalsIgnoreCase("Y")) {
                            AttributeBinding propStatus =
                                (AttributeBinding)bindings.getControlBinding("Status1");
                            propStatus.setInputValue(docStatusReject);

                            OperationBinding operationApproveApproval =
                                bindings.getOperationBinding("rejectFwdDocApproval");
                            operationApproveApproval.execute();
                        } else {
                            AttributeBinding propStatus =
                                (AttributeBinding)bindings.getControlBinding("Status1");
                            propStatus.setInputValue(docStatusReject);

                            OperationBinding operationApproveApproval =
                                bindings.getOperationBinding("rejectDocApproval");
                            operationApproveApproval.execute();
                        }
                        mustRefreshPage = true;
                    } else {
                        showPopup("\"Reason\" harus diisi apabila melakukan reject approval. Minimal 10 karakter.", potmessage);
                        mustRefreshPage = false;
                    }
                } else if (aprvlAction.equals(FORWARD)) {
                    // Validasi produk harus ada
                    DCIteratorBinding dciterProduk =
                        ADFUtils.findIterator("PromoProdukView1Iterator");
                    Integer cekProduk = (int)dciterProduk.getEstimatedRowCount();
                    if (cekProduk > 0) {
                        AttributeBinding discTypeAttr =
                            (AttributeBinding)bindings.getControlBinding("DiscountType1");
                        String discType = (String)discTypeAttr.getInputValue();
                        AttributeBinding userTypeAttr =
                            (AttributeBinding)bindings.getControlBinding("UserTypeCreator");
                        String userTypeCreator = (String)userTypeAttr.getInputValue();
                        DCIteratorBinding dciterArea =
                            ADFUtils.findIterator("ProdRegionAreaView1Iterator");
                        DCIteratorBinding dciterCustomer =
                            ADFUtils.findIterator("ProdRegionCustomerView1Iterator");
                        DCIteratorBinding dciterRegion =
                            ADFUtils.findIterator("ProdRegionView1Iterator");
                        DCIteratorBinding dciterLoc =
                            ADFUtils.findIterator("ProdRegionLocView1Iterator");
                        DCIteratorBinding dciterCustGroup =
                            ADFUtils.findIterator("ProdRegionCustGroupView1Iterator");
                        DCIteratorBinding dciterCustType =
                            ADFUtils.findIterator("ProdRegionCustTypeView1Iterator");
                
                        Integer cekArea = (int)dciterArea.getEstimatedRowCount();
                        Integer cekCustomer = (int)dciterCustomer.getEstimatedRowCount();
                        Integer cekRegion = (int)dciterRegion.getEstimatedRowCount();
                        Integer cekLoc = (int)dciterLoc.getEstimatedRowCount();
                        Integer cekCustGroup = (int)dciterCustGroup.getEstimatedRowCount();
                        Integer cekCustType = (int)dciterCustType.getEstimatedRowCount();
                        
                        if (userTypeCreator.equalsIgnoreCase(userHo)) {
                            Boolean isSavedValid = true;
                            String sSavedMsg = "";
                            if (!(cekArea > 0 || cekCustomer > 0 || cekRegion > 0 ||
                                  cekLoc > 0 || cekCustGroup > 0 || cekCustType > 0)) {
                                if (!isSavedValid) {
                                    sSavedMsg += ", ";
                                }
                                sSavedMsg +=
                                        "Tab \"Customer\" pada detail produk masih kosong, harus diisi.";
                                isSavedValid = false;
                            }
                            if (isSavedValid) {
                                // DO FORWARD PROCESS FOR HO PROPOSAL                            
                                AttributeBinding propStatus =
                                    (AttributeBinding)bindings.getControlBinding("Status1");
                                propStatus.setInputValue(docStatusInprocess);
            
                                OperationBinding operationApproveApproval =
                                    bindings.getOperationBinding("forwardDocApproval");
                                operationApproveApproval.execute();

                                switchMain.setFacetName("nodata");
                                switchButtonMain.setFacetName("nodata");
                                AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
                                AdfFacesContext.getCurrentInstance().addPartialTarget(switchButtonMain);  
                                mustRefreshPage = true;
                            } else {
                                showPopup(sSavedMsg, potmessage);
                                mustRefreshPage = false;
                            }
                        } else {
                            Boolean isIterValid = true;
                            String sIterMsg = "";
                            if (discType.equalsIgnoreCase("BIAYA")) {
                                DCIteratorBinding dciterBiaya =
                                    ADFUtils.findIterator("BiayaView1Iterator");
                                if (dciterBiaya.getEstimatedRowCount() < 1) {
                                    if (!isIterValid) {
                                        sIterMsg += ", ";
                                    }
                                    sIterMsg += "Biaya Pada Produk Detail";
                                    isIterValid = false;
                                }
                            } else if (discType.equalsIgnoreCase("PROMOBARANG")) {
                                DCIteratorBinding dciterPromoBonus =
                                    ADFUtils.findIterator("PromoBonusView1Iterator");
                                if (dciterPromoBonus.getEstimatedRowCount() < 1) {
                                    if (!isIterValid) {
                                        sIterMsg += ", ";
                                    }
                                    sIterMsg += "Promo Pada Produk Detail";
                                    isIterValid = false;
                                }
                            } else if (discType.equalsIgnoreCase("POTONGAN")) {
                                DCIteratorBinding dciterDiscount =
                                    ADFUtils.findIterator("DiscountView1Iterator");
                                if (dciterDiscount.getEstimatedRowCount() < 1) {
                                    if (!isIterValid) {
                                        sIterMsg += ", ";
                                    }
                                    sIterMsg += "Potongan Pada Produk Detail";
                                    isIterValid = false;
                                }
                            }
                            if (userTypeCreator.equalsIgnoreCase(userArea)) {
                                DCIteratorBinding dciterArea1 =
                                    ADFUtils.findIterator("PropRegionAreaView1Iterator");
                                DCIteratorBinding dciterCustomer1 =
                                    ADFUtils.findIterator("PropRegionCustomerView1Iterator");
                                DCIteratorBinding dciterRegion1 =
                                    ADFUtils.findIterator("PropRegionView1Iterator");
                                DCIteratorBinding dciterLoc1 =
                                    ADFUtils.findIterator("PropRegionLocView1Iterator");
                                DCIteratorBinding dciterCustGroup1 =
                                    ADFUtils.findIterator("PropRegionCustGroupView1Iterator");
                                DCIteratorBinding dciterCustType1 =
                                    ADFUtils.findIterator("PropRegionCustTypeView1Iterator");
                
                                Integer cekArea1 = (int)dciterArea1.getEstimatedRowCount();
                                Integer cekCustomer1 =
                                    (int)dciterCustomer1.getEstimatedRowCount();
                                Integer cekRegion1 = (int)dciterRegion1.getEstimatedRowCount();
                                Integer cekLoc1 = (int)dciterLoc1.getEstimatedRowCount();
                                Integer cekCustGroup1 =
                                    (int)dciterCustGroup1.getEstimatedRowCount();
                                Integer cekCustType1 =
                                    (int)dciterCustType1.getEstimatedRowCount();
                                if (!(cekArea1 > 0 || cekCustomer1 > 0 || cekRegion1 > 0 ||
                                      cekLoc1 > 0 || cekCustGroup1 > 0 || cekCustType1 > 0)) {
                                    if (!isIterValid) {
                                        sIterMsg += ", ";
                                    }
                                    sIterMsg += "Customer";
                                    isIterValid = false;
                                }
                            }
                            if (isIterValid) {
                                // DO FORWARD PROCESS FOR AREA PROPOSAL                            
                                AttributeBinding propStatus =
                                    (AttributeBinding)bindings.getControlBinding("Status1");
                                propStatus.setInputValue(docStatusInprocess);
            
                                OperationBinding operationApproveApproval =
                                    bindings.getOperationBinding("forwardDocApproval");
                                operationApproveApproval.execute();

                                switchMain.setFacetName("nodata");
                                switchButtonMain.setFacetName("nodata");
                                AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
                                AdfFacesContext.getCurrentInstance().addPartialTarget(switchButtonMain);
                                mustRefreshPage = true;
                            } else {
                                showPopup("\"" + sIterMsg + "\" masih kosong, harus diisi.", potmessage);
                                mustRefreshPage = false;
                            }
                        }
                    } else {
                        showPopup("Proposal tidak memiliki list produk, mohon dilengkapi atau di \"Reject\".", potmessage);
                        mustRefreshPage = false;
                    }
                } else {
                    JSFUtils.addFacesErrorMessage("Error",
                                                  "Action approval tidak dikenal.");
                }

                OperationBinding operationRefresh =
                    bindings.getOperationBinding("Execute");
                operationRefresh.execute();

                OperationBinding operationRefreshDoc =
                    bindings.getOperationBinding("Execute1");
                operationRefreshDoc.execute();

                switchMain.setFacetName("nodata");
                switchButtonMain.setFacetName("nodata");
                AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
                AdfFacesContext.getCurrentInstance().addPartialTarget(switchButtonMain);
            } catch (JboException e) {
                JSFUtils.addFacesErrorMessage("Error", e.getBaseMessage());
                mustRefreshPage = false;
            }

            AdfFacesContext.getCurrentInstance().addPartialTarget(tblApproval);
            AdfFacesContext.getCurrentInstance().addPartialTarget(tblDocHistory);
            
            if (mustRefreshPage) {
                refreshPage();
            } else {
                if (approvalKey != null) {
                    dciterApproval.setCurrentRowWithKey(approvalKey.toStringFormat(true));
                }
            }
        } else {
            AdfFacesContext.getCurrentInstance().addPartialTarget(tblApproval);
            AdfFacesContext.getCurrentInstance().addPartialTarget(tblDocHistory);
        }
    }

    public void setTblApproval(RichTable tblApproval) {
        this.tblApproval = tblApproval;
    }

    public RichTable getTblApproval() {
        return tblApproval;
    }

    public void setTblDocHistory(RichTable tblDocHistory) {
        this.tblDocHistory = tblDocHistory;
    }

    public RichTable getTblDocHistory() {
        return tblDocHistory;
    }

    public void removeProducts(ActionEvent actionEvent) {
        DCIteratorBinding dciter =
            ADFUtils.findIterator("PromoProdukView1Iterator");
        ViewObject vo1 = dciter.getViewObject();
        RowSetIterator rsProductIter = vo1.createRowSetIterator(null);
        Row currRow = null;
        while (rsProductIter.hasNext()) {
            currRow = rsProductIter.next();
            if (true == currRow.getAttribute("SelectedRow")) {
                currRow.remove();
            }
        }
        rsProductIter.closeRowSetIterator();
        BindingContainer bindings = getBindings();
        OperationBinding operationCommit =
            bindings.getOperationBinding("Commit");
        operationCommit.execute();
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblListProduct);
    }

    public void setTblListProduct(RichTable tblListProduct) {
        this.tblListProduct = tblListProduct;
    }

    public RichTable getTblListProduct() {
        return tblListProduct;
    }

    public void detailProdPopupFetchListener(PopupFetchEvent popupFetchEvent) {
        BindingContainer bindings = getBindings();
        DCIteratorBinding dciterTarget =
            ADFUtils.findIterator("TargetView1Iterator");

        if (dciterTarget.getEstimatedRowCount() == 0) {
            OperationBinding operationCreateInsertDiscount =
                bindings.getOperationBinding("CreateInsertTarget");
            operationCreateInsertDiscount.execute();
        }
    }

    public void detailProdDialogListener(DialogEvent dialogEvent) {
        BindingContainer bindings = getBindings();
        if (dialogEvent.getOutcome().name().equals("ok")) {
            OperationBinding operationBindingCommit =
                bindings.getOperationBinding("Commit");
            operationBindingCommit.execute();
        } else {
            // Just do nothing !!!, kalau di rollback suka banyak yang ke reset jadi null soc nya.
        }

        AdfFacesContext.getCurrentInstance().addPartialTarget(tblListProduct);
    }

    public void addPromoProduk(ActionEvent actionEvent) {
        boolean canCreateNew = true;
        BindingContainer bindings =
            BindingContext.getCurrent().getCurrentBindingsEntry();

        UserData userData =
            (UserData)JSFUtils.resolveExpression("#{UserData}");
        String usrCustomer =
            userData.getUserCustomer() == null ? "" : userData.getUserCustomer();

        DCIteratorBinding dciter =
            (DCIteratorBinding)bindings.get("PromoProdukView1Iterator");
        long produkRowCount = dciter.getEstimatedRowCount();

        for (Row produkRow : dciter.getAllRowsInRange()) {
            Integer rowStatus =
                (Integer)produkRow.getAttribute("CheckRowStatus");
            String validComb = (String)produkRow.getAttribute("ValidComb");
            if ((rowStatus == 0 && produkRowCount > 0) ||
                (!("Y").equalsIgnoreCase(validComb) && produkRowCount > 0)) {
                canCreateNew = false;
            }
        }

        if (canCreateNew) {
            RowSetIterator rsi = dciter.getRowSetIterator();
            Row lastRow = rsi.last();
            int lastRowIndex = rsi.getRangeIndexOf(lastRow);
            Row newRow = rsi.createRow();
            newRow.setNewRowState(Row.STATUS_INITIALIZED);

            if (usrCustomer.equalsIgnoreCase(userCustInvalid)) {
                JSFUtils.addFacesWarningMessage("Anda tidak memiliki hak akses memilih customer.");
            }

            //add row to last index + 1 so it becomes last in the range set
            rsi.insertRowAtRangeIndex(lastRowIndex + 1, newRow);
            //make row the current row so it is displayed correctly
            rsi.setCurrentRow(newRow);

            BindingContext bctx = BindingContext.getCurrent();
            DCBindingContainer binding =
                (DCBindingContainer)bctx.getCurrentBindingsEntry();
            DCIteratorBinding iter =
                (DCIteratorBinding)binding.get("ProposalApprovalView1Iterator");
            Row r = iter.getCurrentRow();
            String FoodCode = r.getAttribute("ProposalType").toString();

            if (prodDescCodeFood.equalsIgnoreCase(FoodCode)) {
                itLovProdCategory.setValue(prodCatCodeFood);
                itCategory.setValue(prodDescCodeFood);
                AdfFacesContext adc = AdfFacesContext.getCurrentInstance();
                adc.addPartialTarget(itLovProdCategory);
                adc.addPartialTarget(itCategory);
                adc.addPartialTarget(tblListProduct);
            } else {
                itLovProdCategory.setValue(prodCatCodeNonFood);
                itCategory.setValue(prodDescCodeNonFood);
                AdfFacesContext adc = AdfFacesContext.getCurrentInstance();
                adc.addPartialTarget(itLovProdCategory);
                adc.addPartialTarget(itCategory);
                adc.addPartialTarget(tblListProduct);
            }
        } else {
            StringBuilder message = new StringBuilder("<html><body>");
            message.append("<p>Data produk ada yang belum selesai dilengkapi dan kombinasi belum valid.</p>");
            message.append("<p>Proses penambahan produk baru tidak dapat dilanjutkan.</p>");
            message.append("</body></html>");
            JSFUtils.addFacesWarningMessage(message.toString());
        }
    }

    public void growthByValueChangeListener(ValueChangeEvent valueChangeEvent) {
        itTargetPercentage.setValue(0);
        itTargetValue.setValue(0);

        AdfFacesContext.getCurrentInstance().addPartialTarget(itTargetPercentage);
        AdfFacesContext.getCurrentInstance().addPartialTarget(itTargetValue);
    }

    public void setItTargetPercentage(RichInputText itTargetPercentage) {
        this.itTargetPercentage = itTargetPercentage;
    }

    public RichInputText getItTargetPercentage() {
        return itTargetPercentage;
    }

    public void setItTargetValue(RichInputText itTargetValue) {
        this.itTargetValue = itTargetValue;
    }

    public RichInputText getItTargetValue() {
        return itTargetValue;
    }

    public void setItTargetQty(RichInputText itTargetQty) {
        this.itTargetQty = itTargetQty;
    }

    public RichInputText getItTargetQty() {
        return itTargetQty;
    }

    public void targetQtyValueChangeListener(ValueChangeEvent valueChangeEvent) {
        BigDecimal tgtQty =
            new BigDecimal(itTargetQty.getValue() == "" ? "0" : itTargetQty.getValue() ==
                                                                null ? "0" :
                                                                itTargetQty.getValue().toString().replaceAll(",",
                                                                                                             ""));
        BigDecimal tgtHarga =
            new BigDecimal(itTargetHarga.getValue() == "" ? "0" :
                           itTargetHarga.getValue() == null ? "0" :
                           itTargetHarga.getValue().toString().replaceAll(",",
                                                                          ""));
        BigDecimal totalValue = tgtQty.multiply(tgtHarga);
        oracle.jbo.domain.Number number = null;
        try {
            number =
                    new oracle.jbo.domain.Number(df2dgt.format(totalValue).toString());
        } catch (SQLException e) {
            JSFUtils.addFacesErrorMessage("Error", e.getLocalizedMessage());
        }
        itValueTotal.setValue(number);
    }

    public void setItTargetHarga(RichInputText itTargetHarga) {
        this.itTargetHarga = itTargetHarga;
    }

    public RichInputText getItTargetHarga() {
        return itTargetHarga;
    }

    public void targetHargaValueChangeListener(ValueChangeEvent valueChangeEvent) {
        DCBindingContainer bindings =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        BigDecimal tgtQty =
            new BigDecimal(itTargetQty.getValue() == "" ? "0" : itTargetQty.getValue() ==
                                                                null ? "0" :
                                                                itTargetQty.getValue().toString().replaceAll(",",
                                                                                                             ""));
        BigDecimal tgtHarga =
            new BigDecimal(valueChangeEvent.getNewValue() == "" ? "0" :
                           valueChangeEvent.getNewValue() == null ? "0" :
                           valueChangeEvent.getNewValue().toString().replaceAll(",",
                                                                                ""));

        BigDecimal totalValue = tgtQty.multiply(tgtHarga);
        oracle.jbo.domain.Number number = null;
        try {
            number =
                    new oracle.jbo.domain.Number(df2dgt.format(totalValue).toString());
        } catch (SQLException e) {
            JSFUtils.addFacesErrorMessage("Error", e.getLocalizedMessage());
        }
        itValueTotal.setValue(number);

        // Tipe proposal
        AttributeBinding discountType1Attr =
            (AttributeBinding)bindings.getControlBinding("DiscountType1");
        String discountType1 = (String)discountType1Attr.getInputValue();
        if (discountType1.equalsIgnoreCase(discTypePromoBarang)) {
            // DO NOTHING
            /*
            DCIteratorBinding dcItteratorBindings = bindings.findIteratorBinding("PromoBonusView1Iterator");
            if (dcItteratorBindings.getEstimatedRowCount() > 0) {
                BigDecimal onTopTotal = BigDecimal.ZERO;
                BigDecimal mfTotal = BigDecimal.ZERO;
                BigDecimal onTopQtyTotal = BigDecimal.ZERO;
                BigDecimal mfQtyTotal = BigDecimal.ZERO;
                BigDecimal rasioOnTop = BigDecimal.ZERO;
                BigDecimal rasioMf = BigDecimal.ZERO;
                BigDecimal rasioTotal = BigDecimal.ZERO;

                for (Row r : dcItteratorBindings.getAllRowsInRange()) {
                    String rowOnTopVal = r.getAttribute("DiscNonYearly") == null ? "0" : r.getAttribute("DiscNonYearly").toString().replaceAll(",", "");
                    String rowMfVal = r.getAttribute("DiscYearly") == null ? "0" : r.getAttribute("DiscYearly").toString().replaceAll(",", "");

                    BigDecimal valueOnTop = new BigDecimal(rowOnTopVal);
                    BigDecimal valueMf = new BigDecimal(rowMfVal);

                    onTopTotal = onTopTotal.add(valueOnTop);
                    mfTotal = mfTotal.add(valueMf);
                }

                onTopQtyTotal = onTopTotal.multiply(tgtQty);
                mfQtyTotal = mfTotal.multiply(tgtQty);
                rasioOnTop = (onTopQtyTotal.divide(totalValue, 3, RoundingMode.HALF_UP)).multiply(bdHundred).setScale(2,RoundingMode.HALF_UP);
                rasioMf = (mfQtyTotal.divide(totalValue, 3, RoundingMode.HALF_UP)).multiply(bdHundred).setScale(2,RoundingMode.HALF_UP);
                rasioTotal = rasioOnTop.add(rasioMf);

                otBrgOnTop.setSubmittedValue(onTopQtyTotal);
                otBrgMf.setSubmittedValue(mfQtyTotal);
                otBrgRasioOnTop.setSubmittedValue(rasioOnTop);
                otBrgRasioMf.setSubmittedValue(rasioMf);
                otBrgRasioTotal.setSubmittedValue(rasioTotal);

                AdfFacesContext ctx = AdfFacesContext.getCurrentInstance();
                ctx.addPartialTarget(otBrgOnTop);
                ctx.addPartialTarget(otBrgMf);
                ctx.addPartialTarget(otBrgRasioOnTop);
                ctx.addPartialTarget(otBrgRasioMf);
                ctx.addPartialTarget(otBrgRasioTotal);

                DCIteratorBinding dciterPromoProduk =
                    ADFUtils.findIterator("PromoProdukView1Iterator");
                Row r = dciterPromoProduk.getCurrentRow();
                r.setAttribute("BrgBonusOnTop", onTopQtyTotal);
                r.setAttribute("BrgBonusMf", mfQtyTotal);
                r.setAttribute("BrgBonusRasioMf", rasioMf);
                r.setAttribute("BrgBonusRasioOnTop", rasioOnTop);
                r.setAttribute("BrgBonusRasioTotal", rasioTotal);

                try {
                    dciterPromoProduk.getDataControl().commitTransaction();
                } catch (Exception e) {
                    JSFUtils.addFacesErrorMessage(e.getLocalizedMessage());
                }
            } else {
                // JIKA BELUM ADA ROW PROMO BARANG; DO NOTHING;
            }
            */
        } else if (discountType1.equalsIgnoreCase(discTypePotongan)) {
            DCIteratorBinding dcItteratorBindings =
                bindings.findIteratorBinding("DiscountView1Iterator");

            if (dcItteratorBindings.getEstimatedRowCount() > 0) {
                BigDecimal onTopMax = BigDecimal.ZERO;
                BigDecimal mfMax = BigDecimal.ZERO;
                BigDecimal onTopQtyTotal = BigDecimal.ZERO;
                BigDecimal mfQtyTotal = BigDecimal.ZERO;
                BigDecimal rasioOnTop = BigDecimal.ZERO;
                BigDecimal rasioMf = BigDecimal.ZERO;
                BigDecimal rasioTotal = BigDecimal.ZERO;
                String tipePotongan = "";
                String tipePerhitungan = "";
                Number kelipatanVal = new Number(0);
                
                for (Row r : dcItteratorBindings.getAllRowsInRange()) {
                        
                    tipePerhitungan =
                        r.getAttribute("TipePerhitungan") == null ? "0" :
                        r.getAttribute("TipePerhitungan").toString();
                    
                    tipePotongan =
                        r.getAttribute("TipePotongan") == null ? "0" :
                        r.getAttribute("TipePotongan").toString();     
                    
                    kelipatanVal =
                        r.getAttribute("Kelipatan") == null ? new Number(0) :
                        (Number)r.getAttribute("Kelipatan");
                        
                    String rowOnTopVal =
                        r.getAttribute("DiscNonYearly") == null ? "0" :
                        r.getAttribute("DiscNonYearly").toString().replaceAll(",",
                                                                              "");
                    String rowMfVal =
                        r.getAttribute("DiscYearly") == null ? "0" :
                        r.getAttribute("DiscYearly").toString().replaceAll(",",
                                                                           "");

                    BigDecimal valueOnTop = new BigDecimal(rowOnTopVal);
                    BigDecimal valueMf = new BigDecimal(rowMfVal);
                    
                    if (onTopMax.compareTo(valueOnTop) < 0) {
                        onTopMax = valueOnTop;
                    }

                    if (mfMax.compareTo(valueMf) < 0) {
                        mfMax = valueMf;
                    }
                }
                
                if (tipePerhitungan.equalsIgnoreCase(tipeHitungKelipatan)) {
                    if (tipePotongan.equalsIgnoreCase(tipePotonganPercent)) {
                        // PERCENT ON TOP CALC
                        BigDecimal roundTgtDivKelipatanOT = tgtQty.divide(kelipatanVal.getBigDecimalValue(),0,RoundingMode.DOWN);
                        BigDecimal priceMulPercentDiscOT = tgtHarga.multiply(onTopMax.divide(new BigDecimal(100)));
                        onTopQtyTotal = (roundTgtDivKelipatanOT.multiply(kelipatanVal.getBigDecimalValue())).multiply(priceMulPercentDiscOT);
                        
                        // PERCENT MF CALC
                        BigDecimal roundTgtDivKelipatanMF = tgtQty.divide(kelipatanVal.getBigDecimalValue(),0,RoundingMode.DOWN);
                        BigDecimal priceMulPercentDiscMF = tgtHarga.multiply(mfMax.divide(new BigDecimal(100)));
                        mfQtyTotal = (roundTgtDivKelipatanMF.multiply(kelipatanVal.getBigDecimalValue())).multiply(priceMulPercentDiscMF);
                    } else if (tipePotongan.equalsIgnoreCase(tipePotonganAmount)) {
                        // AMOUNT ON TOP CALC
                        BigDecimal roundTgtDivKelipatanOT = tgtQty.divide(kelipatanVal.getBigDecimalValue(),0,RoundingMode.DOWN);
                        onTopQtyTotal = (roundTgtDivKelipatanOT.multiply(kelipatanVal.getBigDecimalValue())).multiply(onTopMax);
                        
                        // AMOUNT MF CALC
                        BigDecimal roundTgtDivKelipatanMF = tgtQty.divide(kelipatanVal.getBigDecimalValue(),0,RoundingMode.DOWN);
                        mfQtyTotal = (roundTgtDivKelipatanMF.multiply(kelipatanVal.getBigDecimalValue())).multiply(mfMax);
                    }
                } else if (tipePerhitungan.equalsIgnoreCase(tipeHitungTdkKelipatan)) {
                    onTopQtyTotal = onTopMax.multiply(tgtQty);
                    mfQtyTotal = mfMax.multiply(tgtQty);
                }
                
                try {
                    rasioOnTop =
                            onTopQtyTotal.divide(totalValue, MathContext.DECIMAL128).multiply(bdHundred).setScale(2,
                                                                                                                  RoundingMode.HALF_UP);
                    //                            (onTopQtyTotal.divide(totalValue, 3, RoundingMode.HALF_UP)).multiply(bdHundred).setScale(2,RoundingMode.HALF_UP);
                    rasioMf =
                            mfQtyTotal.divide(totalValue, MathContext.DECIMAL128).multiply(bdHundred).setScale(2,
                                                                                                               RoundingMode.HALF_UP);
                    //                            (mfQtyTotal.divide(totalValue, 3, RoundingMode.HALF_UP)).multiply(bdHundred).setScale(2,RoundingMode.HALF_UP);
                    rasioTotal = rasioOnTop.add(rasioMf);
                } catch (Exception e) {
                    // ERROR: divide by zero; do nothing
                    rasioTotal = BigDecimal.ZERO;
                }

                otOnTop.setSubmittedValue(onTopQtyTotal);
                otMF.setSubmittedValue(mfQtyTotal);
                otRasioOntop.setSubmittedValue(rasioOnTop);
                otRasioMf.setSubmittedValue(rasioMf);
                otRasioTotal.setSubmittedValue(rasioTotal);

                AdfFacesContext ctx = AdfFacesContext.getCurrentInstance();
                ctx.addPartialTarget(otOnTop);
                ctx.addPartialTarget(otMF);
                ctx.addPartialTarget(otRasioOntop);
                ctx.addPartialTarget(otRasioMf);
                ctx.addPartialTarget(otRasioTotal);

                DCIteratorBinding dciterPromoProduk =
                    ADFUtils.findIterator("PromoProdukView1Iterator");
                Row r = dciterPromoProduk.getCurrentRow();
                r.setAttribute("DiscOnTop", onTopQtyTotal);
                r.setAttribute("DiscMf", mfQtyTotal);
                r.setAttribute("DiscRasioMf", rasioMf);
                r.setAttribute("DiscRasioOnTop", rasioOnTop);
                r.setAttribute("DiscRasioTotal1", rasioTotal);
                /*
                try {
                    dciterPromoProduk.getDataControl().commitTransaction();
                } catch (Exception e) {
                    JSFUtils.addFacesErrorMessage(e.getLocalizedMessage());
                }
                */
            } else {
                // JIKA BELUM ADA ROW POTONGAN; DO NOTHING;
            }
        } else if (discountType1.equalsIgnoreCase(discTypeBiaya)) {
            // DO NOTHING
            /*
            DCIteratorBinding dcItteratorBindings = bindings.findIteratorBinding("BiayaView1Iterator");
            if (dcItteratorBindings.getEstimatedRowCount() > 0) {
                BigDecimal onTopTotal = BigDecimal.ZERO;
                BigDecimal mfTotal = BigDecimal.ZERO;
                BigDecimal onTopQtyTotal = BigDecimal.ZERO;
                BigDecimal mfQtyTotal = BigDecimal.ZERO;
                BigDecimal rasioOnTop = BigDecimal.ZERO;
                BigDecimal rasioMf = BigDecimal.ZERO;
                BigDecimal rasioTotal = BigDecimal.ZERO;

                for (Row r : dcItteratorBindings.getAllRowsInRange()) {
                    String rowOnTopVal = r.getAttribute("BiayaNonYearly") == null ? "0" : r.getAttribute("BiayaNonYearly").toString().replaceAll(",", "");
                    String rowMfVal = r.getAttribute("BiayaYearly") == null ? "0" : r.getAttribute("BiayaYearly").toString().replaceAll(",", "");

                    BigDecimal valueOnTop = new BigDecimal(rowOnTopVal);
                    BigDecimal valueMf = new BigDecimal(rowMfVal);

                    onTopTotal = onTopTotal.add(valueOnTop);
                    mfTotal = mfTotal.add(valueMf);
                }
                onTopQtyTotal = onTopTotal.multiply(tgtQty);
                mfQtyTotal = mfTotal.multiply(tgtQty);
                rasioOnTop = (onTopQtyTotal.divide(totalValue, 3, RoundingMode.HALF_UP)).multiply(bdHundred).setScale(2,RoundingMode.HALF_UP);
                rasioMf = (mfQtyTotal.divide(totalValue, 3, RoundingMode.HALF_UP)).multiply(bdHundred).setScale(2,RoundingMode.HALF_UP);
                rasioTotal = rasioOnTop.add(rasioMf);

                otBiaOntop.setSubmittedValue(onTopQtyTotal);
                otBiaMf.setSubmittedValue(mfQtyTotal);
                otBiaRasioOntop.setSubmittedValue(rasioOnTop);
                otBiaRasioMf.setSubmittedValue(rasioMf);
                otBiaRasioTotal.setSubmittedValue(rasioTotal);

                AdfFacesContext ctx = AdfFacesContext.getCurrentInstance();
                ctx.addPartialTarget(otBiaOntop);
                ctx.addPartialTarget(otBiaMf);
                ctx.addPartialTarget(otBiaRasioOntop);
                ctx.addPartialTarget(otBiaRasioMf);
                ctx.addPartialTarget(otBiaRasioTotal);

                DCIteratorBinding dciterPromoProduk =
                    ADFUtils.findIterator("PromoProdukView1Iterator");
                Row r = dciterPromoProduk.getCurrentRow();
                r.setAttribute("BiaOntop", onTopQtyTotal);
                r.setAttribute("BiaMf", mfQtyTotal);
                r.setAttribute("BiaRasioMf", rasioMf);
                r.setAttribute("BiaRasionOntop", rasioOnTop);
                r.setAttribute("BiaRasioTotal", rasioTotal);

                try {
                    dciterPromoProduk.getDataControl().commitTransaction();
                } catch (Exception e) {
                    JSFUtils.addFacesErrorMessage(e.getLocalizedMessage());
                }
            } else {
                // JIKA BELUM ADA ROW BIAYA; DO NOTHING;
            }
            */
        } else {
            JSFUtils.addFacesErrorMessage("\"Tipe Proposal\" tidak dikenali.");
        }
    }

    public void setItValueTotal(RichInputText itValueTotal) {
        this.itValueTotal = itValueTotal;
    }

    public RichInputText getItValueTotal() {
        return itValueTotal;
    }

    public void setTblListProductAddBuy(RichTable tblListProductAddBuy) {
        this.tblListProductAddBuy = tblListProductAddBuy;
    }

    public RichTable getTblListProductAddBuy() {
        return tblListProductAddBuy;
    }

    public void productCategoryAddBuyChanged(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            this.getItlovProdClassAddBuy().setValue(null);
            this.getItlovProdBrandAddBuy().setValue(null);
            this.getItlovProdExtAddBuy().setValue(null);
            this.getItlovProdPackAddBuy().setValue(null);
        }
    }

    public void productClassAddBuyChanged(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            this.getItlovProdBrandAddBuy().setValue(null);
            this.getItlovProdExtAddBuy().setValue(null);
            this.getItlovProdPackAddBuy().setValue(null);
        }
    }

    public void productBrandAddBuyChanged(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            this.getItlovProdExtAddBuy().setValue(null);
            this.getItlovProdPackAddBuy().setValue(null);
        }
    }

    public void productExtentionAddBuyChanged(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            this.getItlovProdPackAddBuy().setValue(null);
        }
    }

    public void setItlovProdClassAddBuy(RichInputListOfValues itlovProdClassAddBuy) {
        this.itlovProdClassAddBuy = itlovProdClassAddBuy;
    }

    public RichInputListOfValues getItlovProdClassAddBuy() {
        return itlovProdClassAddBuy;
    }

    public void setItlovProdBrandAddBuy(RichInputListOfValues itlovProdBrandAddBuy) {
        this.itlovProdBrandAddBuy = itlovProdBrandAddBuy;
    }

    public RichInputListOfValues getItlovProdBrandAddBuy() {
        return itlovProdBrandAddBuy;
    }

    public void setItlovProdExtAddBuy(RichInputListOfValues itlovProdExtAddBuy) {
        this.itlovProdExtAddBuy = itlovProdExtAddBuy;
    }

    public RichInputListOfValues getItlovProdExtAddBuy() {
        return itlovProdExtAddBuy;
    }

    public void setItlovProdPackAddBuy(RichInputListOfValues itlovProdPackAddBuy) {
        this.itlovProdPackAddBuy = itlovProdPackAddBuy;
    }

    public RichInputListOfValues getItlovProdPackAddBuy() {
        return itlovProdPackAddBuy;
    }

    public void windowVariantAddBuyReturnListener(ReturnEvent returnEvent) {

        DCIteratorBinding dciterProdVariant =
            ADFUtils.findIterator("AddBuyVariantView1Iterator");
        if (dciterProdVariant.getEstimatedRowCount() == 1) {
            RowSetIterator rsiProdVariant =
                dciterProdVariant.getRowSetIterator();
            Row prodVariantRow = rsiProdVariant.first();
            if (!prodVariantRow.getAttribute("ProdVariant").toString().equalsIgnoreCase(varAll)) {
                DCIteratorBinding dciterProdItem =
                    ADFUtils.findIterator("AddBuyProdItemView1Iterator");
                RowSetIterator rsiProdItem =
                    dciterProdItem.getRowSetIterator();
                for (Row prodItemRow : dciterProdItem.getAllRowsInRange()) {
                    prodItemRow.remove();
                }
                rsiProdItem.closeRowSetIterator();
            }
        } else {
            DCIteratorBinding dciterProdItem =
                ADFUtils.findIterator("AddBuyProdItemView1Iterator");
            RowSetIterator rsiProdItem = dciterProdItem.getRowSetIterator();
            for (Row prodItemRow : dciterProdItem.getAllRowsInRange()) {
                prodItemRow.remove();
            }
            rsiProdItem.closeRowSetIterator();
        }

        BindingContainer bindings = this.getBindings();
        OperationBinding operationBinding =
            bindings.getOperationBinding("ExecutePromoAddBuy");
        operationBinding.execute();
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblListProductAddBuy);
    }

    public void windowItemAddBuyReturnListener(ReturnEvent returnEvent) {
        BindingContainer bindings = this.getBindings();
        OperationBinding operationBinding =
            bindings.getOperationBinding("ExecutePromoAddBuy");
        operationBinding.execute();
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblListProductAddBuy);
    }

    public void setTblListProductBonus(RichTable tblListProductBonus) {
        this.tblListProductBonus = tblListProductBonus;
    }

    public RichTable getTblListProductBonus() {
        return tblListProductBonus;
    }

    public void clearBonusVariantItem() {
        DCIteratorBinding dciterVariant =
            ADFUtils.findIterator("PromoBonusVariantView1Iterator");
        RowSetIterator rsiVariant = dciterVariant.getRowSetIterator();
        for (Row variantRow : dciterVariant.getAllRowsInRange()) {
            variantRow.remove();
        }
        rsiVariant.closeRowSetIterator();

        DCIteratorBinding dciterItem =
            ADFUtils.findIterator("PromoBonusProdItemView1Iterator");
        RowSetIterator rsiItem = dciterItem.getRowSetIterator();
        for (Row itemRow : dciterItem.getAllRowsInRange()) {
            itemRow.remove();
        }
        rsiItem.closeRowSetIterator();
    }

    public void productCategoryBonusChanged(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            this.getItlovProdClassBonus().setValue(null);
            this.getItlovProdBrandBonus().setValue(null);
            this.getItlovProdExtBonus().setValue(null);
            this.getItlovProdPackBonus().setValue(null);
            
           BindingContext bctx = BindingContext.getCurrent();
           DCBindingContainer binding =
               (DCBindingContainer)bctx.getCurrentBindingsEntry();
           DCIteratorBinding iter =
               (DCIteratorBinding)binding.get("PromoBonusVariantView1Iterator");
           DCIteratorBinding dciter =
               ADFUtils.findIterator("PromoBonusView1Iterator");
           for (Row r : iter.getAllRowsInRange()) {
               r.remove();
           }
           BindingContainer bindings = getBindings();
           OperationBinding operationBinding =
               bindings.getOperationBinding("Commit");
           operationBinding.execute();
           dciter.executeQuery();

           DCIteratorBinding dciterItem =
               ADFUtils.findIterator("PromoBonusProdItemView1Iterator");
           RowSetIterator rsiItem = dciterItem.getRowSetIterator();
           for (Row itemRow : dciterItem.getAllRowsInRange()) {
               itemRow.remove();
           }
           rsiItem.closeRowSetIterator();

           AdfFacesContext ctx = AdfFacesContext.getCurrentInstance();
           ctx.addPartialTarget(itBonusVariant);
           ctx.addPartialTarget(tblListProductBonus);
        }
    }

    public void setItlovProdClassBonus(RichInputListOfValues itlovProdClassBonus) {
        this.itlovProdClassBonus = itlovProdClassBonus;
    }

    public RichInputListOfValues getItlovProdClassBonus() {
        return itlovProdClassBonus;
    }

    public void productClassBonusChanged(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            this.getItlovProdBrandBonus().setValue(null);
            this.getItlovProdExtBonus().setValue(null);
            this.getItlovProdPackBonus().setValue(null);
        }
        if (itlovProdClassBonus.getValue().toString().equalsIgnoreCase(varAll)) {
            BindingContext bctx = BindingContext.getCurrent();
            DCBindingContainer binding =
                (DCBindingContainer)bctx.getCurrentBindingsEntry();
            DCIteratorBinding iter =
                (DCIteratorBinding)binding.get("PromoBonusVariantView1Iterator");

            for (Row r : iter.getAllRowsInRange()) {
                r.remove();
            }

            Row row = iter.getNavigatableRowIterator().createRow();

            DCIteratorBinding dciter =
                ADFUtils.findIterator("PromoBonusView1Iterator");
            ViewObject voTableData = dciter.getViewObject();
            Row promoBonusRow = voTableData.getCurrentRow();
            String PromoBonusId =
                ((DBSequence)promoBonusRow.getAttribute("PromoBonusId")).toString();

            row.setNewRowState(Row.STATUS_INITIALIZED);
            row.setAttribute("PromoBonusId", PromoBonusId);
            row.setAttribute("ProdVariant", varAll);
            row.setAttribute("VariantDesc", varAll);
            iter.getRowSetIterator().insertRow(row);
            iter.setCurrentRowWithKey(row.getKey().toStringFormat(true));
            BindingContainer bindings = getBindings();
            OperationBinding operationBinding =
                bindings.getOperationBinding("Commit");
            operationBinding.execute();
            dciter.executeQuery();

            itlovProdBrandBonus.setValue(varAll);
            itlovProdExtBonus.setValue(varAll);
            itlovProdPackBonus.setValue(varAll);

            AdfFacesContext ctx = AdfFacesContext.getCurrentInstance();
            ctx.addPartialTarget(itlovProdBrandBonus);
            ctx.addPartialTarget(itlovProdExtBonus);
            ctx.addPartialTarget(itlovProdPackBonus);
            ctx.addPartialTarget(itBonusVariant);
            ctx.addPartialTarget(tblListProductBonus);
        } else {
            BindingContext bctx = BindingContext.getCurrent();
            DCBindingContainer binding =
                (DCBindingContainer)bctx.getCurrentBindingsEntry();
            DCIteratorBinding iter =
                (DCIteratorBinding)binding.get("PromoBonusVariantView1Iterator");
            DCIteratorBinding dciter =
                ADFUtils.findIterator("PromoBonusView1Iterator");
            for (Row r : iter.getAllRowsInRange()) {
                r.remove();
            }
            BindingContainer bindings = getBindings();
            OperationBinding operationBinding =
                bindings.getOperationBinding("Commit");
            operationBinding.execute();
            dciter.executeQuery();

            DCIteratorBinding dciterItem =
                ADFUtils.findIterator("PromoBonusProdItemView1Iterator");
            RowSetIterator rsiItem = dciterItem.getRowSetIterator();
            for (Row itemRow : dciterItem.getAllRowsInRange()) {
                itemRow.remove();
            }
            rsiItem.closeRowSetIterator();

            AdfFacesContext ctx = AdfFacesContext.getCurrentInstance();
            ctx.addPartialTarget(itBonusVariant);
            ctx.addPartialTarget(tblListProductBonus);
        }
    }

    public void setItlovProdBrandBonus(RichInputListOfValues itlovProdBrandBonus) {
        this.itlovProdBrandBonus = itlovProdBrandBonus;
    }

    public RichInputListOfValues getItlovProdBrandBonus() {
        return itlovProdBrandBonus;
    }

    public void productBrandBonusChanged(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            this.getItlovProdExtBonus().setValue(null);
            this.getItlovProdPackBonus().setValue(null);
        }
        if (itlovProdBrandBonus.getValue().toString().equalsIgnoreCase(varAll)) {
            BindingContext bctx = BindingContext.getCurrent();
            DCBindingContainer binding =
                (DCBindingContainer)bctx.getCurrentBindingsEntry();
            DCIteratorBinding iter =
                (DCIteratorBinding)binding.get("PromoBonusVariantView1Iterator");

            for (Row r : iter.getAllRowsInRange()) {
                r.remove();
            }

            Row row = iter.getNavigatableRowIterator().createRow();

            DCIteratorBinding dciter =
                ADFUtils.findIterator("PromoBonusView1Iterator");
            ViewObject voTableData = dciter.getViewObject();
            Row promoBonusRow = voTableData.getCurrentRow();
            String PromoBonusId =
                ((DBSequence)promoBonusRow.getAttribute("PromoBonusId")).toString();

            row.setNewRowState(Row.STATUS_INITIALIZED);
            row.setAttribute("PromoBonusId", PromoBonusId);
            row.setAttribute("ProdVariant", varAll);
            row.setAttribute("VariantDesc", varAll);
            iter.getRowSetIterator().insertRow(row);
            iter.setCurrentRowWithKey(row.getKey().toStringFormat(true));
            BindingContainer bindings = getBindings();
            OperationBinding operationBinding =
                bindings.getOperationBinding("Commit");
            operationBinding.execute();
            dciter.executeQuery();

            itlovProdExtBonus.setValue(varAll);
            itlovProdPackBonus.setValue(varAll);

            AdfFacesContext ctx = AdfFacesContext.getCurrentInstance();

            ctx.addPartialTarget(itlovProdExtBonus);
            ctx.addPartialTarget(itlovProdPackBonus);
            ctx.addPartialTarget(itBonusVariant);
            ctx.addPartialTarget(tblListProductBonus);
        } else {
            BindingContext bctx = BindingContext.getCurrent();
            DCBindingContainer binding =
                (DCBindingContainer)bctx.getCurrentBindingsEntry();
            DCIteratorBinding iter =
                (DCIteratorBinding)binding.get("PromoBonusVariantView1Iterator");
            DCIteratorBinding dciter =
                ADFUtils.findIterator("PromoBonusView1Iterator");
            for (Row r : iter.getAllRowsInRange()) {
                r.remove();
            }
            BindingContainer bindings = getBindings();
            OperationBinding operationBinding =
                bindings.getOperationBinding("Commit");
            operationBinding.execute();
            dciter.executeQuery();

            DCIteratorBinding dciterItem =
                ADFUtils.findIterator("PromoBonusProdItemView1Iterator");
            RowSetIterator rsiItem = dciterItem.getRowSetIterator();
            for (Row itemRow : dciterItem.getAllRowsInRange()) {
                itemRow.remove();
            }
            rsiItem.closeRowSetIterator();

            AdfFacesContext ctx = AdfFacesContext.getCurrentInstance();
            ctx.addPartialTarget(itBonusVariant);
            ctx.addPartialTarget(tblListProductBonus);
        }

    }

    public void setItlovProdExtBonus(RichInputListOfValues itlovProdExtBonus) {
        this.itlovProdExtBonus = itlovProdExtBonus;
    }

    public RichInputListOfValues getItlovProdExtBonus() {
        return itlovProdExtBonus;
    }

    public void productExtentionBonusChanged(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            this.getItlovProdPackBonus().setValue(null);
        }
        if (itlovProdExtBonus.getValue().toString().equalsIgnoreCase(varAll)) {
            BindingContext bctx = BindingContext.getCurrent();
            DCBindingContainer binding =
                (DCBindingContainer)bctx.getCurrentBindingsEntry();
            DCIteratorBinding iter =
                (DCIteratorBinding)binding.get("PromoBonusVariantView1Iterator");

            for (Row r : iter.getAllRowsInRange()) {
                r.remove();
            }

            Row row = iter.getNavigatableRowIterator().createRow();

            DCIteratorBinding dciter =
                ADFUtils.findIterator("PromoBonusView1Iterator");
            ViewObject voTableData = dciter.getViewObject();
            Row promoBonusRow = voTableData.getCurrentRow();
            String PromoBonusId =
                ((DBSequence)promoBonusRow.getAttribute("PromoBonusId")).toString();

            row.setNewRowState(Row.STATUS_INITIALIZED);
            row.setAttribute("PromoBonusId", PromoBonusId);
            row.setAttribute("ProdVariant", varAll);
            row.setAttribute("VariantDesc", varAll);
            iter.getRowSetIterator().insertRow(row);
            iter.setCurrentRowWithKey(row.getKey().toStringFormat(true));
            BindingContainer bindings = getBindings();
            OperationBinding operationBinding =
                bindings.getOperationBinding("Commit");
            operationBinding.execute();
            dciter.executeQuery();

            itlovProdPackBonus.setValue(varAll);

            AdfFacesContext ctx = AdfFacesContext.getCurrentInstance();
            ctx.addPartialTarget(itlovProdPackBonus);
            ctx.addPartialTarget(itBonusVariant);
            ctx.addPartialTarget(tblListProductBonus);
        } else {
            BindingContext bctx = BindingContext.getCurrent();
            DCBindingContainer binding =
                (DCBindingContainer)bctx.getCurrentBindingsEntry();
            DCIteratorBinding iter =
                (DCIteratorBinding)binding.get("PromoBonusVariantView1Iterator");
            DCIteratorBinding dciter =
                ADFUtils.findIterator("PromoBonusView1Iterator");
            for (Row r : iter.getAllRowsInRange()) {
                r.remove();
            }
            BindingContainer bindings = getBindings();
            OperationBinding operationBinding =
                bindings.getOperationBinding("Commit");
            operationBinding.execute();
            dciter.executeQuery();

            DCIteratorBinding dciterItem =
                ADFUtils.findIterator("PromoBonusProdItemView1Iterator");
            RowSetIterator rsiItem = dciterItem.getRowSetIterator();
            for (Row itemRow : dciterItem.getAllRowsInRange()) {
                itemRow.remove();
            }
            rsiItem.closeRowSetIterator();

            AdfFacesContext ctx = AdfFacesContext.getCurrentInstance();
            ctx.addPartialTarget(itBonusVariant);
            ctx.addPartialTarget(tblListProductBonus);
        }
    }

    public void productPackBonusChanged(ValueChangeEvent valueChangeEvent) {
        if (itlovProdPackBonus.getValue().toString().equalsIgnoreCase(varAll)) {
            BindingContext bctx = BindingContext.getCurrent();
            DCBindingContainer binding =
                (DCBindingContainer)bctx.getCurrentBindingsEntry();
            DCIteratorBinding iter =
                (DCIteratorBinding)binding.get("PromoBonusVariantView1Iterator");

            for (Row r : iter.getAllRowsInRange()) {
                r.remove();
            }
            Row row = iter.getNavigatableRowIterator().createRow();

            DCIteratorBinding dciter =
                ADFUtils.findIterator("PromoBonusView1Iterator");
            ViewObject voTableData = dciter.getViewObject();
            Row promoBonusRow = voTableData.getCurrentRow();
            String PromoBonusId =
                ((DBSequence)promoBonusRow.getAttribute("PromoBonusId")).toString();

            row.setNewRowState(Row.STATUS_INITIALIZED);
            row.setAttribute("PromoBonusId", PromoBonusId);
            row.setAttribute("ProdVariant", varAll);
            row.setAttribute("VariantDesc", varAll);
            iter.getRowSetIterator().insertRow(row);
            iter.setCurrentRowWithKey(row.getKey().toStringFormat(true));
            BindingContainer bindings = getBindings();
            OperationBinding operationBinding =
                bindings.getOperationBinding("Commit");
            operationBinding.execute();
            dciter.executeQuery();
            AdfFacesContext ctx = AdfFacesContext.getCurrentInstance();
            ctx.addPartialTarget(itBonusVariant);
            ctx.addPartialTarget(tblListProductBonus);
        } else {
            BindingContext bctx = BindingContext.getCurrent();
            DCBindingContainer binding =
                (DCBindingContainer)bctx.getCurrentBindingsEntry();
            DCIteratorBinding iter =
                (DCIteratorBinding)binding.get("PromoBonusVariantView1Iterator");
            DCIteratorBinding dciter =
                ADFUtils.findIterator("PromoBonusView1Iterator");
            for (Row r : iter.getAllRowsInRange()) {
                r.remove();
            }
            BindingContainer bindings = getBindings();
            OperationBinding operationBinding =
                bindings.getOperationBinding("Commit");
            operationBinding.execute();
            dciter.executeQuery();

            DCIteratorBinding dciterItem =
                ADFUtils.findIterator("PromoBonusProdItemView1Iterator");
            RowSetIterator rsiItem = dciterItem.getRowSetIterator();
            for (Row itemRow : dciterItem.getAllRowsInRange()) {
                itemRow.remove();
            }
            rsiItem.closeRowSetIterator();

            AdfFacesContext ctx = AdfFacesContext.getCurrentInstance();
            ctx.addPartialTarget(itBonusVariant);
            ctx.addPartialTarget(tblListProductBonus);
        }
    }

    public void setItlovProdPackBonus(RichInputListOfValues itlovProdPackBonus) {
        this.itlovProdPackBonus = itlovProdPackBonus;
    }

    public RichInputListOfValues getItlovProdPackBonus() {
        return itlovProdPackBonus;
    }

    public void windowVariantBonusReturnListener(ReturnEvent returnEvent) {

        DCIteratorBinding dciterProdVariant =
            ADFUtils.findIterator("PromoBonusVariantView1Iterator");
        if (dciterProdVariant.getEstimatedRowCount() == 1) {
            RowSetIterator rsiProdVariant =
                dciterProdVariant.getRowSetIterator();
            Row prodVariantRow = rsiProdVariant.first();
            if (!prodVariantRow.getAttribute("ProdVariant").toString().equalsIgnoreCase(varAll)) {
                DCIteratorBinding dciterProdItem =
                    ADFUtils.findIterator("PromoBonusProdItemView1Iterator");
                RowSetIterator rsiProdItem =
                    dciterProdItem.getRowSetIterator();
                for (Row prodItemRow : dciterProdItem.getAllRowsInRange()) {
                    prodItemRow.remove();
                }
                rsiProdItem.closeRowSetIterator();
            }
        } else {
            DCIteratorBinding dciterProdItem =
                ADFUtils.findIterator("PromoBonusProdItemView1Iterator");
            RowSetIterator rsiProdItem = dciterProdItem.getRowSetIterator();
            for (Row prodItemRow : dciterProdItem.getAllRowsInRange()) {
                prodItemRow.remove();
            }
            rsiProdItem.closeRowSetIterator();
        }

        BindingContainer bindings = this.getBindings();
        OperationBinding operationBinding =
            bindings.getOperationBinding("ExecutePromoBonus");
        operationBinding.execute();
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblListProductBonus);
    }

    public void windowItemBonusReturnListener(ReturnEvent returnEvent) {
        BindingContainer bindings = this.getBindings();
        OperationBinding operationBinding =
            bindings.getOperationBinding("ExecutePromoBonus");
        operationBinding.execute();
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblListProductBonus);
    }

    public void productCategoryChanged(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            this.getItlovProdClass().setValue(null);
            this.getItlovProdBrand().setValue(null);
            this.getItlovProdExtention().setValue(null);
            this.getItlovProdPackaging().setValue(null);
        }
        clearVariantItem();
    }

    public void productClassChanged(ValueChangeEvent valueChangeEvent) {
        BindingContext bctx = BindingContext.getCurrent();
        DCBindingContainer binding =
            (DCBindingContainer)bctx.getCurrentBindingsEntry();
        String PromoProdukIdSel = "";
        DCIteratorBinding iterProduk =
            binding.findIteratorBinding("PromoProdukView1Iterator");
        ViewObject voPromoProduk = iterProduk.getViewObject();
        Row rowSelected = voPromoProduk.getCurrentRow();
        if (rowSelected.getAttribute("PromoProdukId") != null) {
            PromoProdukIdSel =
                    rowSelected.getAttribute("PromoProdukId").toString();
        }
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            this.getItlovProdBrand().setValue(null);
            this.getItlovProdExtention().setValue(null);
            this.getItlovProdPackaging().setValue(null);

        }

        clearVariantItem(PromoProdukIdSel);

        DCIteratorBinding iterVariant =
            (DCIteratorBinding)binding.get("ProdukVariantView1Iterator");
        
        if (itlovProdClass.getValue().toString().equalsIgnoreCase(varAll)) {
            // Update produk brand, extension, packaging to ALL
            Row rowProduk = iterProduk.getCurrentRow();
            rowProduk.setAttribute("ProductBrand", varAll);
            rowProduk.setAttribute("ProductExt", varAll);
            rowProduk.setAttribute("ProductPack", varAll);
            
            //Insert variant to ALL
            Row rowVariant = iterVariant.getNavigatableRowIterator().createRow();
            rowVariant.setNewRowState(Row.STATUS_INITIALIZED);
            rowVariant.setAttribute("PromoProdukId", PromoProdukIdSel);
            rowVariant.setAttribute("ProdVariant", varAll);
            rowVariant.setAttribute("VariantDesc", varAll);
            iterVariant.getRowSetIterator().insertRow(rowVariant);

            itlovProdBrand.setValue(varAll);
            itlovProdExtention.setValue(varAll);
            itlovProdPackaging.setValue(varAll);
        } else {
            Row rowProduk = iterProduk.getCurrentRow();
            rowProduk.setAttribute("ValidComb", "N");
            itValidComb.setValue("N");
        }
            
        BindingContainer bindings = getBindings();
        OperationBinding operationBinding =
            bindings.getOperationBinding("Commit");
        operationBinding.execute();
        
        iterProduk.executeQuery();
        iterProduk.setCurrentRowWithKey(rowSelected.getKey().toStringFormat(true));
        
        AdfFacesContext ctx = AdfFacesContext.getCurrentInstance();
        ctx.addPartialTarget(itlovProdBrand);
        ctx.addPartialTarget(itlovProdExtention);
        ctx.addPartialTarget(itlovProdPackaging);
        ctx.addPartialTarget(itVariant);
        ctx.addPartialTarget(itProductItem);
    }

    public void setItlovProdClass(RichInputListOfValues itlovProdClass) {
        this.itlovProdClass = itlovProdClass;
    }

    public RichInputListOfValues getItlovProdClass() {
        return itlovProdClass;
    }

    public void setItlovProdBrand(RichInputListOfValues itlovProdBrand) {
        this.itlovProdBrand = itlovProdBrand;
    }

    public RichInputListOfValues getItlovProdBrand() {
        return itlovProdBrand;
    }

    public void productBrandChanged(ValueChangeEvent valueChangeEvent) {
        BindingContext bctx = BindingContext.getCurrent();
        DCBindingContainer binding =
            (DCBindingContainer)bctx.getCurrentBindingsEntry();
        String PromoProdukIdSel = "";
        DCIteratorBinding iterProduk =
            binding.findIteratorBinding("PromoProdukView1Iterator");
        ViewObject voPromoProduk = iterProduk.getViewObject();
        Row rowSelected = voPromoProduk.getCurrentRow();
        if (rowSelected.getAttribute("PromoProdukId") != null) {
            PromoProdukIdSel =
                    rowSelected.getAttribute("PromoProdukId").toString();
        }
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            this.getItlovProdExtention().setValue(null);
            this.getItlovProdPackaging().setValue(null);
        }

        clearVariantItem(PromoProdukIdSel);

        DCIteratorBinding iterVariant =
            (DCIteratorBinding)binding.get("ProdukVariantView1Iterator");
        
        if (itlovProdBrand.getValue().toString().equalsIgnoreCase(varAll)) {
            // Update produk extension, packaging to ALL
            Row rowProduk = iterProduk.getCurrentRow();
            rowProduk.setAttribute("ProductExt", varAll);
            rowProduk.setAttribute("ProductPack", varAll);
            
            // Insert variant to ALL
            Row rowVariant = iterVariant.getNavigatableRowIterator().createRow();
            rowVariant.setNewRowState(Row.STATUS_INITIALIZED);
            rowVariant.setAttribute("PromoProdukId", PromoProdukIdSel);
            rowVariant.setAttribute("ProdVariant", varAll);
            rowVariant.setAttribute("VariantDesc", varAll);
            iterVariant.getRowSetIterator().insertRow(rowVariant);

            itlovProdExtention.setValue(varAll);
            itlovProdPackaging.setValue(varAll);
        } else {
            Row rowProduk = iterProduk.getCurrentRow();
            rowProduk.setAttribute("ValidComb", "N");
            itValidComb.setValue("N");
        }
            
        BindingContainer bindings = getBindings();
        OperationBinding operationBinding =
            bindings.getOperationBinding("Commit");
        operationBinding.execute();
        
        iterProduk.executeQuery();
        iterProduk.setCurrentRowWithKey(rowSelected.getKey().toStringFormat(true));

        AdfFacesContext ctx = AdfFacesContext.getCurrentInstance();
        ctx.addPartialTarget(itlovProdExtention);
        ctx.addPartialTarget(itlovProdPackaging);
        ctx.addPartialTarget(itVariant);
        ctx.addPartialTarget(itProductItem);
    }

    public void setItlovProdExtention(RichInputListOfValues itlovProdExtention) {
        this.itlovProdExtention = itlovProdExtention;
    }

    public RichInputListOfValues getItlovProdExtention() {
        return itlovProdExtention;
    }

    public void productExtentionChanged(ValueChangeEvent valueChangeEvent) {
        BindingContext bctx = BindingContext.getCurrent();
        DCBindingContainer binding =
            (DCBindingContainer)bctx.getCurrentBindingsEntry();
        String PromoProdukIdSel = "";
        DCIteratorBinding iterProduk =
            binding.findIteratorBinding("PromoProdukView1Iterator");
        ViewObject voPromoProduk = iterProduk.getViewObject();
        Row rowSelected = voPromoProduk.getCurrentRow();
        if (rowSelected.getAttribute("PromoProdukId") != null) {
            PromoProdukIdSel =
                    rowSelected.getAttribute("PromoProdukId").toString();
        }
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            this.getItlovProdPackaging().setValue(null);
        }

        clearVariantItem(PromoProdukIdSel);

        DCIteratorBinding iterVariant =
            (DCIteratorBinding)binding.get("ProdukVariantView1Iterator");
        
        if (itlovProdExtention.getValue().toString().equalsIgnoreCase(varAll)) {
            // Update produk packaging to ALL
            Row rowProduk = iterProduk.getCurrentRow();
            rowProduk.setAttribute("ProductPack", varAll);
            
            // Insert variant to ALL
            Row rowVariant = iterVariant.getNavigatableRowIterator().createRow();
            rowVariant.setNewRowState(Row.STATUS_INITIALIZED);
            rowVariant.setAttribute("PromoProdukId", PromoProdukIdSel);
            rowVariant.setAttribute("ProdVariant", varAll);
            rowVariant.setAttribute("VariantDesc", varAll);
            iterVariant.getRowSetIterator().insertRow(rowVariant);

            itlovProdPackaging.setValue(varAll);
        } else {
            Row rowProduk = iterProduk.getCurrentRow();
            rowProduk.setAttribute("ValidComb", "N");
            itValidComb.setValue("N");
        }
            
        BindingContainer bindings = getBindings();
        OperationBinding operationBinding =
            bindings.getOperationBinding("Commit");
        operationBinding.execute();
        
        iterProduk.executeQuery();
        iterProduk.setCurrentRowWithKey(rowSelected.getKey().toStringFormat(true));
        
        AdfFacesContext ctx = AdfFacesContext.getCurrentInstance();
        ctx.addPartialTarget(itlovProdPackaging);
        ctx.addPartialTarget(itVariant);
        ctx.addPartialTarget(itProductItem);
    }

    public void productPackagingChanged(ValueChangeEvent valueChangeEvent) {
        BindingContext bctx = BindingContext.getCurrent();
        DCBindingContainer binding =
            (DCBindingContainer)bctx.getCurrentBindingsEntry();
        String PromoProdukIdSel = "";
        DCIteratorBinding iterProduk =
            binding.findIteratorBinding("PromoProdukView1Iterator");
        ViewObject voPromoProduk = iterProduk.getViewObject();
        Row rowSelected = voPromoProduk.getCurrentRow();
        if (rowSelected.getAttribute("PromoProdukId") != null) {
            PromoProdukIdSel =
                    rowSelected.getAttribute("PromoProdukId").toString();
        }

        clearVariantItem(PromoProdukIdSel);
        DCIteratorBinding iterVariant =
            (DCIteratorBinding)binding.get("ProdukVariantView1Iterator");  
        
        if (itlovProdPackaging.getValue().toString().equalsIgnoreCase(varAll)) {            
            // Insert variant to ALL
            Row rowVariant = iterVariant.getNavigatableRowIterator().createRow();
            rowVariant.setNewRowState(Row.STATUS_INITIALIZED);
            rowVariant.setAttribute("PromoProdukId", PromoProdukIdSel);
            rowVariant.setAttribute("ProdVariant", varAll);
            rowVariant.setAttribute("VariantDesc", varAll);
            iterVariant.getRowSetIterator().insertRow(rowVariant);           
        } else {
            Row rowProduk = iterProduk.getCurrentRow();
            rowProduk.setAttribute("ValidComb", "N");
            itValidComb.setValue("N");
        }
        
        BindingContainer bindings = getBindings();
        OperationBinding operationBinding =
            bindings.getOperationBinding("Commit");
        operationBinding.execute();
        
        iterProduk.executeQuery();
        iterProduk.setCurrentRowWithKey(rowSelected.getKey().toStringFormat(true));
        
        AdfFacesContext ctx = AdfFacesContext.getCurrentInstance();
        ctx.addPartialTarget(itVariant);
        ctx.addPartialTarget(itProductItem);

        linkVariant.setDisabled(false);
        linkProduct.setDisabled(false);
        AdfFacesContext.getCurrentInstance().addPartialTarget(linkVariant);
        AdfFacesContext.getCurrentInstance().addPartialTarget(linkProduct);
    }

    public void setItlovProdPackaging(RichInputListOfValues itlovProdPackaging) {
        this.itlovProdPackaging = itlovProdPackaging;
    }

    public RichInputListOfValues getItlovProdPackaging() {
        return itlovProdPackaging;
    }

    public void clearVariantItem() {
        DCIteratorBinding dciterVariant =
            ADFUtils.findIterator("ProdukVariantView1Iterator");
        RowSetIterator rsiVariant = dciterVariant.getRowSetIterator();
        for (Row variantRow : dciterVariant.getAllRowsInRange()) {
            variantRow.remove();
        }
        rsiVariant.closeRowSetIterator();

        DCIteratorBinding dciterItem =
            ADFUtils.findIterator("ProdukItemView1Iterator");
        RowSetIterator rsiItem = dciterItem.getRowSetIterator();
        for (Row itemRow : dciterItem.getAllRowsInRange()) {
            itemRow.remove();
        }
        rsiItem.closeRowSetIterator();
    }

    public void windowVariantReturnListener(ReturnEvent returnEvent) {
        String PromoProdukIdSel = "";
        DCBindingContainer bindingsSelRow =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding iterProduk =
            bindingsSelRow.findIteratorBinding("PromoProdukView1Iterator");
        ViewObject voTableData = iterProduk.getViewObject();
        Row rowSelected = voTableData.getCurrentRow();
        if (rowSelected.getAttribute("PromoProdukId") != null) {
            PromoProdukIdSel =
                    rowSelected.getAttribute("PromoProdukId").toString();
        }

        DCIteratorBinding dciterProdVariant =
            ADFUtils.findIterator("ProdukVariantView1Iterator");
        if (dciterProdVariant.getEstimatedRowCount() == 1) {
            RowSetIterator rsiProdVariant =
                dciterProdVariant.getRowSetIterator();
            Row prodVariantRow = rsiProdVariant.first();
            if (!prodVariantRow.getAttribute("ProdVariant").toString().equalsIgnoreCase(varAll)) {
                DCIteratorBinding dciterProdItem =
                    ADFUtils.findIterator("ProdukItemView1Iterator");
                RowSetIterator rsiProdItem =
                    dciterProdItem.getRowSetIterator();
                for (Row prodItemRow : dciterProdItem.getAllRowsInRange()) {
                    String ppId =
                        prodItemRow.getAttribute("PromoProdukId").toString();
                    if (ppId.equalsIgnoreCase(PromoProdukIdSel)) {
                        prodItemRow.remove();
                        linkProduct.setVisible(false);
                        AdfFacesContext.getCurrentInstance().addPartialTarget(linkProduct);
                    }
                }
                rsiProdItem.closeRowSetIterator();
            } else {
                DCIteratorBinding dciterProdItem =
                    ADFUtils.findIterator("ProdukItemView1Iterator");
                RowSetIterator rsiProdItem =
                    dciterProdItem.getRowSetIterator();
                for (Row prodItemRow : dciterProdItem.getAllRowsInRange()) {
                    String ppId =
                        prodItemRow.getAttribute("PromoProdukId").toString();
                    if (ppId.equalsIgnoreCase(PromoProdukIdSel)) {
                        rsiProdItem.closeRowSetIterator();
                        linkProduct.setVisible(true);
                        AdfFacesContext.getCurrentInstance().addPartialTarget(linkProduct);
                    }
                }
            }
        } else {
            DCIteratorBinding dciterProdItem =
                ADFUtils.findIterator("ProdukItemView1Iterator");
            RowSetIterator rsiProdItem = dciterProdItem.getRowSetIterator();
            for (Row prodItemRow : dciterProdItem.getAllRowsInRange()) {
                prodItemRow.remove();
            }
            rsiProdItem.closeRowSetIterator();
        }

        BindingContainer bindings = this.getBindings();
        OperationBinding operationBinding =
            bindings.getOperationBinding("ExecutePromoProduct");
        operationBinding.execute();

        iterProduk.setCurrentRowWithKey(rowSelected.getKey().toStringFormat(true));
        AdfFacesContext.getCurrentInstance().addPartialTarget(itVariant);
        AdfFacesContext.getCurrentInstance().addPartialTarget(itProductItem);
        AdfFacesContext.getCurrentInstance().addPartialTarget(linkProduct);
    }

    public void windowItemReturnListener(ReturnEvent returnEvent) {
        BindingContainer bindings = this.getBindings();        
        DCBindingContainer bindingsSelRow =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding iterProduk =
            bindingsSelRow.findIteratorBinding("PromoProdukView1Iterator");
        ViewObject voTableData = iterProduk.getViewObject();
        Row rowSelected = voTableData.getCurrentRow();
        OperationBinding operationBinding =
            bindings.getOperationBinding("ExecutePromoProduct");
        operationBinding.execute();
        iterProduk.setCurrentRowWithKey(rowSelected.getKey().toStringFormat(true));
        AdfFacesContext.getCurrentInstance().addPartialTarget(itProductItem);
    }

    public void itlovCustomerPilih(ValueChangeEvent valueChangeEvent) {
        String chgNewVal = (String)valueChangeEvent.getNewValue();
        if (chgNewVal.equalsIgnoreCase(prodRegion)) {
            DCIteratorBinding dciterArea =
                ADFUtils.findIterator("ProdRegionAreaView1Iterator");
            RowSetIterator rsiArea = dciterArea.getRowSetIterator();
            for (Row areaRow : dciterArea.getAllRowsInRange()) {
                areaRow.remove();
            }
            rsiArea.closeRowSetIterator();

            DCIteratorBinding dciterLocation =
                ADFUtils.findIterator("ProdRegionLocView1Iterator");
            RowSetIterator rsiLocation = dciterLocation.getRowSetIterator();
            for (Row locationRow : dciterLocation.getAllRowsInRange()) {
                locationRow.remove();
            }
            rsiLocation.closeRowSetIterator();

            DCIteratorBinding dciterCustType =
                ADFUtils.findIterator("ProdRegionCustTypeView1Iterator");
            RowSetIterator rsiCustType = dciterCustType.getRowSetIterator();
            for (Row custTypeRow : dciterCustType.getAllRowsInRange()) {
                custTypeRow.remove();
            }
            rsiCustType.closeRowSetIterator();

            DCIteratorBinding dciterCustGroup =
                ADFUtils.findIterator("ProdRegionCustGroupView1Iterator");
            RowSetIterator rsiCustGroup = dciterCustGroup.getRowSetIterator();
            for (Row custGroupRow : dciterCustGroup.getAllRowsInRange()) {
                custGroupRow.remove();
            }
            rsiCustGroup.closeRowSetIterator();

            DCIteratorBinding dciterCustomer =
                ADFUtils.findIterator("ProdRegionCustomerView1Iterator");
            RowSetIterator rsiCustomer = dciterCustomer.getRowSetIterator();
            for (Row customerRow : dciterCustomer.getAllRowsInRange()) {
                customerRow.remove();
            }
            rsiCustomer.closeRowSetIterator();

        } else if (chgNewVal.equalsIgnoreCase(prodArea)) {
            DCIteratorBinding dciterRegion =
                ADFUtils.findIterator("ProdRegionView1Iterator");
            RowSetIterator rsiRegion = dciterRegion.getRowSetIterator();
            for (Row regionRow : dciterRegion.getAllRowsInRange()) {
                regionRow.remove();
            }
            rsiRegion.closeRowSetIterator();

            DCIteratorBinding dciterLocation =
                ADFUtils.findIterator("ProdRegionLocView1Iterator");
            RowSetIterator rsiLocation = dciterLocation.getRowSetIterator();
            for (Row locationRow : dciterLocation.getAllRowsInRange()) {
                locationRow.remove();
            }
            rsiLocation.closeRowSetIterator();

            DCIteratorBinding dciterCustType =
                ADFUtils.findIterator("ProdRegionCustTypeView1Iterator");
            RowSetIterator rsiCustType = dciterCustType.getRowSetIterator();
            for (Row custTypeRow : dciterCustType.getAllRowsInRange()) {
                custTypeRow.remove();
            }
            rsiCustType.closeRowSetIterator();

            DCIteratorBinding dciterCustGroup =
                ADFUtils.findIterator("ProdRegionCustGroupView1Iterator");
            RowSetIterator rsiCustGroup = dciterCustGroup.getRowSetIterator();
            for (Row custGroupRow : dciterCustGroup.getAllRowsInRange()) {
                custGroupRow.remove();
            }
            rsiCustGroup.closeRowSetIterator();

            DCIteratorBinding dciterCustomer =
                ADFUtils.findIterator("ProdRegionCustomerView1Iterator");
            RowSetIterator rsiCustomer = dciterCustomer.getRowSetIterator();
            for (Row customerRow : dciterCustomer.getAllRowsInRange()) {
                customerRow.remove();
            }
            rsiCustomer.closeRowSetIterator();

        } else if (chgNewVal.equalsIgnoreCase(prodLocation)) {
            DCIteratorBinding dciterRegion =
                ADFUtils.findIterator("ProdRegionView1Iterator");
            RowSetIterator rsiRegion = dciterRegion.getRowSetIterator();
            for (Row regionRow : dciterRegion.getAllRowsInRange()) {
                regionRow.remove();
            }
            rsiRegion.closeRowSetIterator();

            DCIteratorBinding dciterArea =
                ADFUtils.findIterator("ProdRegionAreaView1Iterator");
            RowSetIterator rsiArea = dciterArea.getRowSetIterator();
            for (Row areaRow : dciterArea.getAllRowsInRange()) {
                areaRow.remove();
            }
            rsiArea.closeRowSetIterator();

            DCIteratorBinding dciterCustType =
                ADFUtils.findIterator("ProdRegionCustTypeView1Iterator");
            RowSetIterator rsiCustType = dciterCustType.getRowSetIterator();
            for (Row custTypeRow : dciterCustType.getAllRowsInRange()) {
                custTypeRow.remove();
            }
            rsiCustType.closeRowSetIterator();

            DCIteratorBinding dciterCustGroup =
                ADFUtils.findIterator("ProdRegionCustGroupView1Iterator");
            RowSetIterator rsiCustGroup = dciterCustGroup.getRowSetIterator();
            for (Row custGroupRow : dciterCustGroup.getAllRowsInRange()) {
                custGroupRow.remove();
            }
            rsiCustGroup.closeRowSetIterator();

            DCIteratorBinding dciterCustomer =
                ADFUtils.findIterator("ProdRegionCustomerView1Iterator");
            RowSetIterator rsiCustomer = dciterCustomer.getRowSetIterator();
            for (Row customerRow : dciterCustomer.getAllRowsInRange()) {
                customerRow.remove();
            }
            rsiCustomer.closeRowSetIterator();

        } else if (chgNewVal.equalsIgnoreCase(prodCustType)) {
            DCIteratorBinding dciterRegion =
                ADFUtils.findIterator("ProdRegionView1Iterator");
            RowSetIterator rsiRegion = dciterRegion.getRowSetIterator();
            for (Row regionRow : dciterRegion.getAllRowsInRange()) {
                regionRow.remove();
            }
            rsiRegion.closeRowSetIterator();

            DCIteratorBinding dciterArea =
                ADFUtils.findIterator("ProdRegionAreaView1Iterator");
            RowSetIterator rsiArea = dciterArea.getRowSetIterator();
            for (Row areaRow : dciterArea.getAllRowsInRange()) {
                areaRow.remove();
            }
            rsiArea.closeRowSetIterator();

            DCIteratorBinding dciterLocation =
                ADFUtils.findIterator("ProdRegionLocView1Iterator");
            RowSetIterator rsiLocation = dciterLocation.getRowSetIterator();
            for (Row locationRow : dciterLocation.getAllRowsInRange()) {
                locationRow.remove();
            }
            rsiLocation.closeRowSetIterator();

            DCIteratorBinding dciterCustGroup =
                ADFUtils.findIterator("ProdRegionCustGroupView1Iterator");
            RowSetIterator rsiCustGroup = dciterCustGroup.getRowSetIterator();
            for (Row custGroupRow : dciterCustGroup.getAllRowsInRange()) {
                custGroupRow.remove();
            }
            rsiCustGroup.closeRowSetIterator();

            DCIteratorBinding dciterCustomer =
                ADFUtils.findIterator("ProdRegionCustomerView1Iterator");
            RowSetIterator rsiCustomer = dciterCustomer.getRowSetIterator();
            for (Row customerRow : dciterCustomer.getAllRowsInRange()) {
                customerRow.remove();
            }
            rsiCustomer.closeRowSetIterator();

        } else if (chgNewVal.equalsIgnoreCase(prodCustGroup)) {
            DCIteratorBinding dciterRegion =
                ADFUtils.findIterator("ProdRegionView1Iterator");
            RowSetIterator rsiRegion = dciterRegion.getRowSetIterator();
            for (Row regionRow : dciterRegion.getAllRowsInRange()) {
                regionRow.remove();
            }
            rsiRegion.closeRowSetIterator();

            DCIteratorBinding dciterArea =
                ADFUtils.findIterator("ProdRegionAreaView1Iterator");
            RowSetIterator rsiArea = dciterArea.getRowSetIterator();
            for (Row areaRow : dciterArea.getAllRowsInRange()) {
                areaRow.remove();
            }
            rsiArea.closeRowSetIterator();

            DCIteratorBinding dciterLocation =
                ADFUtils.findIterator("ProdRegionLocView1Iterator");
            RowSetIterator rsiLocation = dciterLocation.getRowSetIterator();
            for (Row locationRow : dciterLocation.getAllRowsInRange()) {
                locationRow.remove();
            }
            rsiLocation.closeRowSetIterator();

            DCIteratorBinding dciterCustType =
                ADFUtils.findIterator("ProdRegionCustTypeView1Iterator");
            RowSetIterator rsiCustType = dciterCustType.getRowSetIterator();
            for (Row custTypeRow : dciterCustType.getAllRowsInRange()) {
                custTypeRow.remove();
            }
            rsiCustType.closeRowSetIterator();

            DCIteratorBinding dciterCustomer =
                ADFUtils.findIterator("ProdRegionCustomerView1Iterator");
            RowSetIterator rsiCustomer = dciterCustomer.getRowSetIterator();
            for (Row customerRow : dciterCustomer.getAllRowsInRange()) {
                customerRow.remove();
            }
            rsiCustomer.closeRowSetIterator();

        } else if (chgNewVal.equalsIgnoreCase(prodCustomer)) {
            DCIteratorBinding dciterRegion =
                ADFUtils.findIterator("ProdRegionView1Iterator");
            RowSetIterator rsiRegion = dciterRegion.getRowSetIterator();
            for (Row regionRow : dciterRegion.getAllRowsInRange()) {
                regionRow.remove();
            }
            rsiRegion.closeRowSetIterator();

            DCIteratorBinding dciterArea =
                ADFUtils.findIterator("ProdRegionAreaView1Iterator");
            RowSetIterator rsiArea = dciterArea.getRowSetIterator();
            for (Row areaRow : dciterArea.getAllRowsInRange()) {
                areaRow.remove();
            }
            rsiArea.closeRowSetIterator();

            DCIteratorBinding dciterLocation =
                ADFUtils.findIterator("ProdRegionLocView1Iterator");
            RowSetIterator rsiLocation = dciterLocation.getRowSetIterator();
            for (Row locationRow : dciterLocation.getAllRowsInRange()) {
                locationRow.remove();
            }
            rsiLocation.closeRowSetIterator();

            DCIteratorBinding dciterCustType =
                ADFUtils.findIterator("ProdRegionCustTypeView1Iterator");
            RowSetIterator rsiCustType = dciterCustType.getRowSetIterator();
            for (Row custTypeRow : dciterCustType.getAllRowsInRange()) {
                custTypeRow.remove();
            }
            rsiCustType.closeRowSetIterator();

            DCIteratorBinding dciterCustGroup =
                ADFUtils.findIterator("ProdRegionCustGroupView1Iterator");
            RowSetIterator rsiCustGroup = dciterCustGroup.getRowSetIterator();
            for (Row custGroupRow : dciterCustGroup.getAllRowsInRange()) {
                custGroupRow.remove();
            }
            rsiCustGroup.closeRowSetIterator();
        } else {
            JSFUtils.addFacesErrorMessage("Error",
                                          "\"Tipe Customer\" tidak dikenali.");
        }
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchRegCust);
    }

    public void setSwitchRegCust(UIXSwitcher switchRegCust) {
        this.switchRegCust = switchRegCust;
    }

    public UIXSwitcher getSwitchRegCust() {
        return switchRegCust;
    }

    public void saveAll(ActionEvent actionEvent) {
        BindingContainer bindings = getBindings();

        ApprovalAMImpl approvalAM =
            (ApprovalAMImpl)ADFUtils.getApplicationModuleForDataControl("ApprovalAMDataControl");
         
        AttributeBinding userTypeAttr =
            (AttributeBinding)bindings.getControlBinding("UserTypeCreator");
        String userTypeCreator = (String)userTypeAttr.getInputValue();

        AdfFacesContext context = AdfFacesContext.getCurrentInstance();
        Map vScope = context.getViewScope();
        vScope.put("Action", "save");
        Key keyRow = (Key)vScope.get("ProdRowKey");

        DCIteratorBinding dciterPromoProduk =
            ADFUtils.findIterator("PromoProdukView1Iterator");
        
        Key promoProdKey = null;
        Integer rowStatus = null;
        String rowProdVariant = "";
        if (dciterPromoProduk.getEstimatedRowCount() > 0) {
            rowStatus = (Integer)dciterPromoProduk.getCurrentRow().getAttribute("CheckRowStatus");
            rowProdVariant = dciterPromoProduk.getCurrentRow().getAttribute("ProductVariant") == null ? "" : dciterPromoProduk.getCurrentRow().getAttribute("ProductVariant").toString();
            
            if (keyRow != null) {
                promoProdKey = keyRow;
            } else {
                promoProdKey = dciterPromoProduk.getCurrentRow().getKey();
            }
        }
        
        AttributeBinding propIdAttr =
            (AttributeBinding)bindings.getControlBinding("ProposalId");
        DBSequence proposalId = (DBSequence)propIdAttr.getInputValue();

        AttributeBinding discTypeAttr =
            (AttributeBinding)bindings.getControlBinding("DiscountType1");
        String discType = (String)discTypeAttr.getInputValue();
        
        AttributeBinding custTypeAreaAttr =
            (AttributeBinding)bindings.getControlBinding("CustRegFlag");
        String custTypeArea = (String)custTypeAreaAttr.getInputValue();
        
        // Validate product category combination
        HashMap<Integer, String> prodIdComb =
            new HashMap<Integer, String>();
        int v = 0;
        for (Row currRow : dciterPromoProduk.getAllRowsInRange()) {
            String prodCategory =
                (String)currRow.getAttribute("ProductCategory");
            String prodClass =
                (String)currRow.getAttribute("ProductClass");
            String prodBrand =
                (String)currRow.getAttribute("ProductBrand");
            String prodExt =
                (String)currRow.getAttribute("ProductExt");
            String prodPack =
                (String)currRow.getAttribute("ProductPack");
            String prodCombination =
                prodCategory + "." + prodClass + "." + prodBrand +
                "." + prodExt + "." + prodPack;

            String promoProdId =
                String.valueOf(((DBSequence)currRow.getAttribute("PromoProdukId")).getValue());
            String prodIdCombVal = promoProdId + ";" + prodCombination;
            prodIdComb.put(v, prodIdCombVal);
            v = v + 1;
        }

        for (int j = 0; j < prodIdComb.size(); j++) {
            String prodIdCombMap = prodIdComb.get(j);
            String[] output = prodIdCombMap.split(";");
            String promoProdId = output[0].trim();
            String prodCombination = output[1].trim();

            ProdVariantValidationViewImpl prodVariant =
                approvalAM.getProdVariantValidationView1();
            prodVariant.setNamedWhereClauseParam("promoProdukId",
                                                 promoProdId.trim());
            prodVariant.executeQuery();

            if (prodVariant.getEstimatedRowCount() > 0) {
                Row currVariantRow = null;
                String rowIsValid = "T";
                while (prodVariant.hasNext()) {
                    currVariantRow = prodVariant.next();
                    String currProdVariant =
                        (String)currVariantRow.getAttribute("ProdVariant");
                    String fullComb =
                        prodCombination + "." + currProdVariant;
                    if (fullComb != null) {
                        FcsViewCategCombinationViewImpl catCombView =
                            approvalAM.getFcsViewCategCombinationView1();
                        catCombView.setNamedWhereClauseParam("combVal",
                                                             fullComb);
                        catCombView.executeQuery();

                        if (catCombView.getEstimatedRowCount() == 1 &&
                            rowIsValid.equalsIgnoreCase("T")) {
                            updateValidComb(promoProdId.trim(), "Y");
                        } else {
                            updateValidComb(promoProdId.trim(), "N");
                            rowIsValid = "F";
                        }
                    }
                }
            }
        }
        
        if (userTypeCreator.equalsIgnoreCase(userArea)) {
            //***USER AREA ***
            DCIteratorBinding dciterCustArea =
                ADFUtils.findIterator("PromoCustomerAreaView1Iterator");

            Integer cekCustArea = (int)dciterCustArea.getEstimatedRowCount();
            
            Boolean isSavedValid = true;
            String sSavedMsg = "";
            
            if (!(cekCustArea > 0)) {
                sSavedMsg += "\"Customer\" masih kosong, harus diisi.";
                isSavedValid = false;
            }
            
            PromoProdukViewImpl promoProdukVo =
                (PromoProdukViewImpl)approvalAM.getPromoProdukView1();
            promoProdukVo.setWhereClause("PromoProduk.PROPOSAL_ID = " +
                                         proposalId.getValue());
            promoProdukVo.executeQuery();
            
            ViewObject produkVo = dciterPromoProduk.getViewObject();
            RowSetIterator rsiProduk = produkVo.createRowSetIterator(null);
            int i = 1;
            while (rsiProduk.hasNext()) {
                Row produkRow = rsiProduk.next();
                String validComb = (String)produkRow.getAttribute("ValidComb");
                //Validasi produk combination
                if (validComb.equalsIgnoreCase("N")) {
                    if (!isSavedValid) {
                        sSavedMsg += "<nr> ";
                    }
                    sSavedMsg +=
                            "Kombinasi produk pada baris (" + i + ") belum valid.";
                    isSavedValid = false;
                }

                // Validasi Tab "Biaya" / "Promo Barang" / "Potongan"
                if (discType.equalsIgnoreCase(discTypeBiaya)) {
                    if (promoProdukVo.getEstimatedRowCount() < 1) {
                        if (!isSavedValid) {
                            sSavedMsg += "<nr> ";
                        }
                        sSavedMsg += "List produk pada proposal belum diisi.";
                        isSavedValid = false;
                    } else {
                        Number biayaNonYearly =
                            (Number)produkRow.getAttribute("BiaOntop") ==
                            null ? zeroNum :
                            (Number)produkRow.getAttribute("BiaOntop");
                        Number biayaYearly =
                            (Number)produkRow.getAttribute("BiaMf") == null ?
                            zeroNum :
                            (Number)produkRow.getAttribute("BiaMf");
                        if ((biayaNonYearly == null && biayaYearly == null) ||
                            (biayaNonYearly.compareTo(zeroNum) == 0 &&
                             biayaYearly == null) ||
                            (biayaNonYearly.compareTo(zeroNum) == 0 &&
                             biayaYearly.compareTo(zeroNum) == 0) ||
                            (biayaNonYearly == null &&
                             biayaYearly.compareTo(zeroNum) == 0)) {
                            if (!isSavedValid) {
                                sSavedMsg += "<nr> ";
                            }
                            sSavedMsg +=
                                    "Value Target atau Biaya \"Tdk Potong Budget / On Top\" , \"Potong Budget / MF\" produk baris (" +
                                    i + ") belum diisi.";
                            isSavedValid = false;
                        }
                    }
                } else if (discType.equalsIgnoreCase(discTypePromoBarang)) {
                    if (promoProdukVo.getEstimatedRowCount() < 1) {
                        if (!isSavedValid) {
                            sSavedMsg += "<nr> ";
                        }
                        sSavedMsg += "List produk pada proposal belum diisi.";
                        isSavedValid = false;
                    } else {
                        Number promoNonYearly =
                            (Number)produkRow.getAttribute("BrgBonusOnTop") ==
                            null ? zeroNum :
                            (Number)produkRow.getAttribute("BrgBonusOnTop");
                        Number promoYearly =
                            (Number)produkRow.getAttribute("BrgBonusMf") ==
                            null ? zeroNum :
                            (Number)produkRow.getAttribute("BrgBonusMf");
                        if ((promoNonYearly == null && promoYearly == null) ||
                            (promoNonYearly.compareTo(zeroNum) == 0 &&
                             promoYearly == null) ||
                            (promoNonYearly.compareTo(zeroNum) == 0 &&
                             promoYearly.compareTo(zeroNum) == 0) ||
                            (promoNonYearly == null &&
                             promoYearly.compareTo(zeroNum) == 0)) {
                            if (!isSavedValid) {
                                sSavedMsg += "<nr> ";
                            }
                            sSavedMsg +=
                                    "Value Target atau Promo Bonus \"Tdk Potong Budget / On Top\" , \"Potong Budget / MF\" produk baris (" +
                                    i + ") belum diisi.";
                            isSavedValid = false;
                        }
                    }
                } else if (discType.equalsIgnoreCase(discTypePotongan)) {
                    if (promoProdukVo.getEstimatedRowCount() < 1) {
                        if (!isSavedValid) {
                            sSavedMsg += "<nr> ";
                        }
                        sSavedMsg += "List produk pada proposal belum diisi.";
                        isSavedValid = false;
                    } else {
                        Number discNonYearly =
                            (Number)produkRow.getAttribute("DiscOnTop") ==
                            null ? zeroNum :
                            (Number)produkRow.getAttribute("DiscOnTop");
                        Number discYearly =
                            (Number)produkRow.getAttribute("DiscMf") == null ?
                            zeroNum :
                            (Number)produkRow.getAttribute("DiscMf");
                        if ((discNonYearly == null && discYearly == null) ||
                            (discNonYearly.compareTo(zeroNum) == 0 &&
                             discYearly == null) ||
                            (discNonYearly.compareTo(zeroNum) == 0 &&
                             discYearly.compareTo(zeroNum) == 0) ||
                            (discNonYearly == null &&
                             discYearly.compareTo(zeroNum) == 0)) {
                            if (!isSavedValid) {
                                sSavedMsg += "<nr> ";
                            }
                            sSavedMsg +=
                                    "Value Target atau Potongan \"Tdk Potong Budget / On Top\" , \"Potong Budget / MF\" produk baris (" +
                                    i + ") belum diisi.";
                            isSavedValid = false;
                        }
                    }
                }
                i = i + 1;
            }
            
            if (isSavedValid) {
                savePaAll();
                if (dciterPromoProduk.getEstimatedRowCount() == 0) {
                    sSavedMsg = "Data sudah disimpan, dengan catatan sebagai berikut:<nr><nr>List produk belum terisi.";
                    showPopup(sSavedMsg, potmessage);
                }
            } else {
                savePaAll();
                sSavedMsg = "Data sudah disimpan, dengan catatan sebagai berikut:<nr><nr>" + sSavedMsg;
                if (promoProdukVo.getEstimatedRowCount() > 0) {
                    if (rowStatus.equals(0) && rowProdVariant.equalsIgnoreCase("")) {
                        sSavedMsg = sSavedMsg + "<nr>Kombinasi produk pada baris (" + i +") belum valid.";
                    } 
                } else {
                    sSavedMsg = sSavedMsg + "<nr>List produk belum terisi.";
                }
                showPopup(sSavedMsg, potmessage);
            }

            promoProdukVo.setWhereClause(null);
            promoProdukVo.executeQuery();
        } else {
            //*** USER HO ***            
            Boolean isSavedValid = true;
            String sSavedMsg = "";
            
            PromoProdukViewImpl promoProdukVo =
                (PromoProdukViewImpl)approvalAM.getPromoProdukView1();
            promoProdukVo.setWhereClause("PromoProduk.PROPOSAL_ID = " +
                                         proposalId.getValue());
            promoProdukVo.executeQuery();
            
            ViewObject produkVo = dciterPromoProduk.getViewObject();
            RowSetIterator rsiProduk = produkVo.createRowSetIterator(null);
            int i = 1;
            while (rsiProduk.hasNext()) {
                Row produkRow = rsiProduk.next();
                DBSequence promoProdukId =
                    (DBSequence)produkRow.getAttribute("PromoProdukId");
                String hoCustType =
                    (String)produkRow.getAttribute("RegCustFlag");
                String validComb = (String)produkRow.getAttribute("ValidComb");

                //Validasi produk combination
                if (validComb.equalsIgnoreCase("N")) {
                    if (!isSavedValid) {
                        sSavedMsg += "<nr> ";
                    }
                    sSavedMsg +=
                            "Kombinasi produk pada baris (" + i + ") belum valid.";
                    isSavedValid = false;
                }

                // Validasi Tab "Biaya" / "Promo Barang" / "Potongan"
                if (discType.equalsIgnoreCase(discTypeBiaya)) {
                    if (promoProdukVo.getEstimatedRowCount() < 1) {
                        if (!isSavedValid) {
                            sSavedMsg += "<nr> ";
                        }
                        sSavedMsg += "List produk pada proposal belum diisi.";
                        isSavedValid = false;
                    } else {
                        Number biayaNonYearly =
                            (Number)produkRow.getAttribute("BiaOntop") ==
                            null ? zeroNum :
                            (Number)produkRow.getAttribute("BiaOntop");
                        Number biayaYearly =
                            (Number)produkRow.getAttribute("BiaMf") == null ?
                            zeroNum :
                            (Number)produkRow.getAttribute("BiaMf");
                        if ((biayaNonYearly == null && biayaYearly == null) ||
                            (biayaNonYearly.compareTo(zeroNum) == 0 &&
                             biayaYearly == null) ||
                            (biayaNonYearly.compareTo(zeroNum) == 0 &&
                             biayaYearly.compareTo(zeroNum) == 0) ||
                            (biayaNonYearly == null &&
                             biayaYearly.compareTo(zeroNum) == 0)) {
                            if (!isSavedValid) {
                                sSavedMsg += "<nr> ";
                            }
                            sSavedMsg +=
                                    "Value Target atau Biaya \"Tdk Potong Budget / On Top\" , \"Potong Budget / MF\" produk baris (" +
                                    i + ") belum diisi.";
                            isSavedValid = false;
                        }
                    }
                } else if (discType.equalsIgnoreCase(discTypePromoBarang)) {
                    if (promoProdukVo.getEstimatedRowCount() < 1) {
                        if (!isSavedValid) {
                            sSavedMsg += "<nr> ";
                        }
                        sSavedMsg += "List produk pada proposal belum diisi.";
                        isSavedValid = false;
                    } else {
                        Number promoNonYearly =
                            (Number)produkRow.getAttribute("BrgBonusOnTop") ==
                            null ? zeroNum :
                            (Number)produkRow.getAttribute("BrgBonusOnTop");
                        Number promoYearly =
                            (Number)produkRow.getAttribute("BrgBonusMf") ==
                            null ? zeroNum :
                            (Number)produkRow.getAttribute("BrgBonusMf");
                        if ((promoNonYearly == null && promoYearly == null) ||
                            (promoNonYearly.compareTo(zeroNum) == 0 &&
                             promoYearly == null) ||
                            (promoNonYearly.compareTo(zeroNum) == 0 &&
                             promoYearly.compareTo(zeroNum) == 0) ||
                            (promoNonYearly == null &&
                             promoYearly.compareTo(zeroNum) == 0)) {
                            if (!isSavedValid) {
                                sSavedMsg += "<nr> ";
                            }
                            sSavedMsg +=
                                    "Value Target atau Promo Bonus \"Tdk Potong Budget / On Top\" , \"Potong Budget / MF\" produk baris (" +
                                    i + ") belum diisi.";
                            isSavedValid = false;
                        }
                    }
                } else if (discType.equalsIgnoreCase(discTypePotongan)) {
                    if (promoProdukVo.getEstimatedRowCount() < 1) {
                        if (!isSavedValid) {
                            sSavedMsg += "<nr> ";
                        }
                        sSavedMsg += "List produk pada proposal belum diisi.";
                        isSavedValid = false;
                    } else {
                        Number discNonYearly =
                            (Number)produkRow.getAttribute("DiscOnTop") ==
                            null ? zeroNum :
                            (Number)produkRow.getAttribute("DiscOnTop");
                        Number discYearly =
                            (Number)produkRow.getAttribute("DiscMf") == null ?
                            zeroNum :
                            (Number)produkRow.getAttribute("DiscMf");
                        if ((discNonYearly == null && discYearly == null) ||
                            (discNonYearly.compareTo(zeroNum) == 0 &&
                             discYearly == null) ||
                            (discNonYearly.compareTo(zeroNum) == 0 &&
                             discYearly.compareTo(zeroNum) == 0) ||
                            (discNonYearly == null &&
                             discYearly.compareTo(zeroNum) == 0)) {
                            if (!isSavedValid) {
                                sSavedMsg += "<nr> ";
                            }
                            sSavedMsg +=
                                    "Value Target atau Potongan \"Tdk Potong Budget / On Top\" , \"Potong Budget / MF\" produk baris (" +
                                    i + ") belum diisi.";
                            isSavedValid = false;
                        }
                    }
                }
                i = i + 1;
            }
            //rsiProduk.closeRowSetIterator();
            
            if (isSavedValid) {
                savePaAll();
                if (dciterPromoProduk.getEstimatedRowCount() == 0) {
                    sSavedMsg = "Data sudah disimpan, dengan catatan sebagai berikut:<nr><nr>List produk belum terisi.";
                    showPopup(sSavedMsg, potmessage);
                }
            } else {
                savePaAll();
                sSavedMsg = "Data sudah disimpan, dengan catatan sebagai berikut:<nr><nr>" + sSavedMsg;
                if (promoProdukVo.getEstimatedRowCount() > 0) {
                    if (rowStatus.equals(0) && rowProdVariant.equalsIgnoreCase("")) {
                        sSavedMsg = sSavedMsg + "<nr>Kombinasi produk pada baris (" + i +") belum valid.";
                    } 
                } else {
                    sSavedMsg = sSavedMsg + "<nr>List produk belum terisi.";
                }
                showPopup(sSavedMsg, potmessage);
            }

            promoProdukVo.setWhereClause(null);
            promoProdukVo.executeQuery();
        }

        if (promoProdKey != null) {
            dciterPromoProduk.setCurrentRowWithKey(promoProdKey.toStringFormat(true));
        }

        // Destroy view scope
        if (keyRow != null) {
            AdfFacesContext.getCurrentInstance().getViewScope().put("ProdRowKey",
                                                                    null);
        }
    }

    private void savePaAll() {
        BindingContainer bindings = getBindings();
        try {
            OperationBinding operationCommitLast =
                bindings.getOperationBinding("Commit");
            operationCommitLast.execute();
        } catch (Exception e) {
            JSFUtils.addFacesErrorMessage(e.getLocalizedMessage());
        }
    }

    public void setRbApprovalAction(RichSelectOneRadio rbApprovalAction) {
        this.rbApprovalAction = rbApprovalAction;
    }

    public RichSelectOneRadio getRbApprovalAction() {
        return rbApprovalAction;
    }

    public void setCheckCanApprove(String checkCanApprove) {
        this.checkCanApprove = checkCanApprove;
    }

    public String getCheckCanApprove() {
        checkCanApprove = null;

        BindingContainer bindings = getBindings();

        AttributeBinding mekPenagihanAttr =
            (AttributeBinding)bindings.getControlBinding("MekanismePenagihan1");
        String mekPenagihan = (String)mekPenagihanAttr.getInputValue();

        AttributeBinding periodeFromAttr =
            (AttributeBinding)bindings.getControlBinding("PeriodeProgFrom");
        String startDateString = (String)periodeFromAttr.getInputValue();

        DateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        Date startDate = null;
        try {
            startDate = df.parse(startDateString + " 00:00:01");
        } catch (ParseException e) {
            JSFUtils.addFacesErrorMessage(e.getMessage());
        }

        Date todayDate = new Date();
        if ((todayDate.after(startDate) &&
             mekPenagihan.equalsIgnoreCase(onInvoice)) ||
            (todayDate.equals(startDate) &&
             mekPenagihan.equalsIgnoreCase(onInvoice))) {
            checkCanApprove = "N";
            itReasonApproval.setValue("Approval tidak dapat diteruskan, sudah memasuki periode promo.");
            rbApprovalAction.setValue(1);
        } else {
            checkCanApprove = "Y";
            itReasonApproval.setValue("");
            rbApprovalAction.setValue(0);
        }

        return checkCanApprove;
    }

    public void setItReasonApproval(RichInputText itReasonApproval) {
        this.itReasonApproval = itReasonApproval;
    }

    public RichInputText getItReasonApproval() {
        return itReasonApproval;
    }

    public void cancelDetailProduct(ActionEvent actionEvent) {
        pdetailProduct.hide();
        tabTargerCustomer.setDisclosed(true);
        AdfFacesContext.getCurrentInstance().addPartialTarget(tabTargerCustomer);
    }

    public void showPopup(String pesan, RichPopup p) {
        pesan = pesan.replaceAll("<nr>", "<br>");
        otpesan.setValue(pesan);
        AdfFacesContext adc = AdfFacesContext.getCurrentInstance();
        adc.addPartialTarget(otpesan);
        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        p.show(hints);
    }

    public void setPdetailProduct(RichPopup pdetailProduct) {
        this.pdetailProduct = pdetailProduct;
    }

    public RichPopup getPdetailProduct() {
        return pdetailProduct;
    }

    public void setTabTargerCustomer(RichShowDetailItem tabTargerCustomer) {
        this.tabTargerCustomer = tabTargerCustomer;
    }

    public RichShowDetailItem getTabTargerCustomer() {
        return tabTargerCustomer;
    }

    public void tabTargetEvent(DisclosureEvent disclosureEvent) {
        if (disclosureEvent.isExpanded()) {
            BindingContainer bindings = getBindings();
            AttributeBinding userTypeAttr =
                (AttributeBinding)bindings.getControlBinding("UserTypeCreator");
            String userTypeCreator = (String)userTypeAttr.getInputValue();

            if (userTypeCreator.equalsIgnoreCase(userHo)) {
                DCIteratorBinding dciterCustHo =
                    ADFUtils.findIterator("PromoCustomerHoView1Iterator");
                
                Integer cekCustHo = (int)dciterCustHo.getEstimatedRowCount();
                
                if (cekCustHo < 1) {
                    showPopup("Tab customer harus diisi", potmessage);
                    tabTargerCustomer.setDisclosed(true);
                }
            }
        }
    }

    public void tabBiayaEvent(DisclosureEvent disclosureEvent) {
        if (disclosureEvent.isExpanded()) {
            BindingContainer bindings = getBindings();
            AttributeBinding userTypeAttr =
                (AttributeBinding)bindings.getControlBinding("UserTypeCreator");
            String userTypeCreator = (String)userTypeAttr.getInputValue();

            if (userTypeCreator.equalsIgnoreCase(userHo)) {
                DCIteratorBinding dciterCustHo =
                    ADFUtils.findIterator("PromoCustomerHoView1Iterator");
                
                Integer cekCustHo = (int)dciterCustHo.getEstimatedRowCount();
                
                if (cekCustHo < 1) {
                    showPopup("Tab customer harus diisi", potmessage);
                    tabTargerCustomer.setDisclosed(true);
                }
            }
        }
    }

    public void tabPromoBarang(DisclosureEvent disclosureEvent) {
        if (disclosureEvent.isExpanded()) {
            BindingContainer bindings = getBindings();
            AttributeBinding userTypeAttr =
                (AttributeBinding)bindings.getControlBinding("UserTypeCreator");
            String userTypeCreator = (String)userTypeAttr.getInputValue();

            if (userTypeCreator.equalsIgnoreCase(userHo)) {
                DCIteratorBinding dciterCustHo =
                    ADFUtils.findIterator("PromoCustomerHoView1Iterator");
                
                Integer cekCustHo = (int)dciterCustHo.getEstimatedRowCount();
                
                if (cekCustHo < 1) {
                    showPopup("Tab customer harus diisi", potmessage);
                    tabTargerCustomer.setDisclosed(true);
                }
            }
            
            DCIteratorBinding dciterUOM =
                ADFUtils.findIterator("PromoBonusView1Iterator");
            DCIteratorBinding dciterUOMTarget =
                ADFUtils.findIterator("TargetView1Iterator");
            Row rTarget = dciterUOMTarget.getCurrentRow();
            String uomTar = (String)rTarget.getAttribute("Uom");
            if (dciterUOM.getEstimatedRowCount() > 0) {
                for (Row r : dciterUOM.getAllRowsInRange()) {
                    r.setAttribute("Uom", uomTar);
                }
                OperationBinding operationBinding =
                    bindings.getOperationBinding("Commit");
                operationBinding.execute();
                dciterUOM.executeQuery();
            }

        }
    }

    public void tabPotonganEvent(DisclosureEvent disclosureEvent) {
        if (disclosureEvent.isExpanded()) {
            BindingContainer bindings = getBindings();
            AttributeBinding userTypeAttr =
                (AttributeBinding)bindings.getControlBinding("UserTypeCreator");
            String userTypeCreator = (String)userTypeAttr.getInputValue();

            if (userTypeCreator.equalsIgnoreCase(userHo)) {
                DCIteratorBinding dciterCustHo =
                    ADFUtils.findIterator("PromoCustomerHoView1Iterator");
                
                Integer cekCustHo = (int)dciterCustHo.getEstimatedRowCount();
                
                if (cekCustHo < 1) {
                    showPopup("Tab customer harus diisi", potmessage);
                    tabTargerCustomer.setDisclosed(true);
                }
            }
            
            DCIteratorBinding dciterUOM =
                ADFUtils.findIterator("DiscountView1Iterator");
            DCIteratorBinding dciterUOMTarget =
                ADFUtils.findIterator("TargetView1Iterator");
            Row rTarget = dciterUOMTarget.getCurrentRow();
            String uomTar = (String)rTarget.getAttribute("Uom");
            if (dciterUOM.getEstimatedRowCount() > 0) {
                for (Row r : dciterUOM.getAllRowsInRange()) {
                    r.setAttribute("Uom", uomTar);
                }
                OperationBinding operationBinding =
                    bindings.getOperationBinding("Commit");
                operationBinding.execute();
                dciterUOM.executeQuery();
            }
        }
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

    public void setTabBiaya(RichShowDetailItem tabBiaya) {
        this.tabBiaya = tabBiaya;
    }

    public RichShowDetailItem getTabBiaya() {
        return tabBiaya;
    }

    public void setTabPromoBarang(RichShowDetailItem tabPromoBarang) {
        this.tabPromoBarang = tabPromoBarang;
    }

    public RichShowDetailItem getTabPromoBarang() {
        return tabPromoBarang;
    }

    public void setTabPotongan(RichShowDetailItem tabPotongan) {
        this.tabPotongan = tabPotongan;
    }

    public RichShowDetailItem getTabPotongan() {
        return tabPotongan;
    }

    public void setTabTargetAndBudget(RichShowDetailItem tabTargetAndBudget) {
        this.tabTargetAndBudget = tabTargetAndBudget;
    }

    public RichShowDetailItem getTabTargetAndBudget() {
        return tabTargetAndBudget;
    }

    public void btnOkDetailProduct(ActionEvent actionEvent) {
        BindingContainer bindings = getBindings();
        AttributeBinding discTypeAttr =
            (AttributeBinding)bindings.getControlBinding("DiscountType1");
        String discType = (String)discTypeAttr.getInputValue();
        Boolean isIterValid = true;
        String sIterMsg = "";
        AttributeBinding userTypeAttr =
            (AttributeBinding)bindings.getControlBinding("UserTypeCreator");
        String userTypeCreator = (String)userTypeAttr.getInputValue();
        if (discType.equalsIgnoreCase(discTypeBiaya)) {
            DCIteratorBinding dciterBiaya =
                ADFUtils.findIterator("BiayaView1Iterator");
            if (dciterBiaya.getEstimatedRowCount() < 1) {
                if (!isIterValid) {
                    sIterMsg += "<nr> ";
                }
                sIterMsg += "Tab Biaya masih kosong, harus diisi.";
                isIterValid = false;
            }
        } else if (discType.equalsIgnoreCase(discTypePromoBarang)) {
            DCIteratorBinding dciterPromoBonus =
                ADFUtils.findIterator("PromoBonusView1Iterator");
            if (dciterPromoBonus.getEstimatedRowCount() < 1) {
                if (!isIterValid) {
                    sIterMsg += "<nr> ";
                }
                sIterMsg += "Tab Barang Bonus masih kosong, harus diisi.";
                isIterValid = false;
            }
        } else if (discType.equalsIgnoreCase(discTypePotongan)) {
            DCIteratorBinding dciterDiscount =
                ADFUtils.findIterator("DiscountView1Iterator");
            if (dciterDiscount.getEstimatedRowCount() < 1) {
                if (!isIterValid) {
                    sIterMsg += "<nr> ";
                }
                sIterMsg += "Tab Potongan masih kosong, harus diisi.";
                isIterValid = false;
            }
        }
        if (userTypeCreator.equalsIgnoreCase(userHo)) {
            DCIteratorBinding dciterUserHo =
                ADFUtils.findIterator("PromoCustomerHoView1Iterator");
            Integer cekUserHo = (int)dciterUserHo.getEstimatedRowCount();
            if (!(cekUserHo > 0)) {
                if (!isIterValid) {
                    sIterMsg += "<nr> ";
                }
                sIterMsg += "Tab Customer masih kosong, harus diisi.";
                isIterValid = false;
            }
        }
        DCIteratorBinding dciterTarget =
            ADFUtils.findIterator("TargetView1Iterator");
        for (Row r : dciterTarget.getAllRowsInRange()) {
            BigDecimal price =
                new BigDecimal(r.getAttribute("Price") == null ? "0" :
                               (df.format(((Number)r.getAttribute("Price")).getValue())).toString());
            BigDecimal Qty =
                new BigDecimal(r.getAttribute("Qty") == null ? "0" :
                               r.getAttribute("Qty").toString());
            if (price.compareTo(BigDecimal.ZERO) <= 0 ||
                Qty.compareTo(BigDecimal.ZERO) <= 0) {
                if (!isIterValid) {
                    sIterMsg += "<nr> ";
                }
                sIterMsg += "\"Qty. Target\" atau \"Harga\" masih kosong, harus diisi.";
                isIterValid = false;
            }
        }
        if (isIterValid) {
            if (discType.equalsIgnoreCase(discTypeBiaya)) {
                Boolean isInputValid = true;
                String sErrMsg = "";
                DCIteratorBinding dciterBiaya =
                    ADFUtils.findIterator("BiayaView1Iterator");
                for (Row r : dciterBiaya.getAllRowsInRange()) {
                    BigDecimal ontop = BigDecimal.ZERO;
                    BigDecimal mf = BigDecimal.ZERO;
                    BigDecimal totAmt = BigDecimal.ZERO;
                    BigDecimal totalmfOntop = BigDecimal.ZERO;
                    String BNY =
                        r.getAttribute("BiayaNonYearly") == null ? "0" :
                        r.getAttribute("BiayaNonYearly") == "" ? "0" :
                        r.getAttribute("BiayaNonYearly").toString();
                    String BN =
                        r.getAttribute("BiayaYearly") == null ? "0" : 
                        r.getAttribute("BiayaYearly") == "" ? "0" :
                        r.getAttribute("BiayaYearly").toString();
                    String value =
                        r.getAttribute("BiayaTotAmt") == null ? "0" :
                        r.getAttribute("BiayaTotAmt") == "" ? "0" :
                        r.getAttribute("BiayaTotAmt").toString();
                                    
                    ontop = new BigDecimal(BNY);
                    mf = new BigDecimal(BN);
                    totAmt = new BigDecimal(value);
                    totalmfOntop = ontop.add(mf);
                    
                    if (BNY.length() == 0 && BN.length() == 0 ||
                        totAmt.compareTo(totalmfOntop) != 0) {
                        if (!isInputValid) {
                            sErrMsg += "<nr> ";
                        }
                        sErrMsg +=
                            "Tab Biaya : Nilai pada kolom \"Tdk Potong Budget / On Top\" atau \"Potong Budget / MF\" harus diisi sama dengan total nilai \"Value\".";
                        isInputValid = false;
                    } else {
                        // DO NOTHING, DATA IS VALID   
                    }
                }
                
                if (isInputValid) {
                    eventOk();
                } else {
                    showPopup(sErrMsg, potmessage);
                    tabBiaya.setDisclosed(true);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(tabBiaya);
                }

            } else if (discType.equalsIgnoreCase(discTypePromoBarang)) {
                Boolean isInputValid = true;
                String sErrMsg = "";
                DCIteratorBinding dciterPromoBonus =
                    ADFUtils.findIterator("PromoBonusView1Iterator");
                for (Row r : dciterPromoBonus.getAllRowsInRange()) {
                    BigDecimal ontop = BigDecimal.ZERO;
                    BigDecimal mf = BigDecimal.ZERO;
                    BigDecimal QtyMulPrice = BigDecimal.ZERO;
                    BigDecimal totalmfOntop = BigDecimal.ZERO;
                    String BNY =
                        r.getAttribute("DiscNonYearly") == null ? "0" :
                        r.getAttribute("DiscNonYearly") == "" ? "0" :
                        r.getAttribute("DiscNonYearly").toString();
                    String BN =
                        r.getAttribute("DiscYearly") == null ? "0" : 
                        r.getAttribute("DiscYearly") == "" ? "0" :
                        r.getAttribute("DiscYearly").toString();
                    String value =
                        r.getAttribute("QtyMulPrice") == null ? "0" :
                        r.getAttribute("QtyMulPrice") == "" ? "0" :
                        r.getAttribute("QtyMulPrice").toString();

                    ontop = new BigDecimal(BNY);
                    mf = new BigDecimal(BN);
                    QtyMulPrice = new BigDecimal(value);
                    totalmfOntop = ontop.add(mf);

                    if (BNY.length() == 0 && BN.length() == 0 ||
                        QtyMulPrice.compareTo(totalmfOntop) != 0) {
                        if (!isInputValid) {
                            sErrMsg += "<nr> ";
                        }
                        sErrMsg +=
                                "Tab Barang Bonus : Nilai pada kolom \"Tdk Potong Budget / On Top\" atau \"Potong Budget / MF\" harus diisi sama dengan total nilai \"Value\".";
                        isInputValid = false;
                    } else {
                        // DO NOTHING, DATA IS VALID
                    }
                }
                
                if (isInputValid) {
                    eventOk();
                } else {
                    showPopup(sErrMsg, potmessage);
                    tabPromoBarang.setDisclosed(true);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(tabPromoBarang);
                }
            } else if (discType.equalsIgnoreCase(discTypePotongan)) {
                Boolean isInputValid = true;
                String sErrMsg = "";
                DCIteratorBinding dciterDiscount1 =
                    ADFUtils.findIterator("DiscountView1Iterator");
                for (Row r : dciterDiscount1.getAllRowsInRange()) {
                    BigDecimal ontop = BigDecimal.ZERO;
                    BigDecimal mf = BigDecimal.ZERO;
                    BigDecimal totalmfOntop = BigDecimal.ZERO;
                    
                    String BNY =
                        r.getAttribute("DiscNonYearly") == null ? "0" :
                        r.getAttribute("DiscNonYearly") == "" ? "0" :
                        r.getAttribute("DiscNonYearly").toString();
                    String BN =
                        r.getAttribute("DiscYearly") == null ? "0" : 
                        r.getAttribute("DiscYearly") == "" ? "0" : 
                        r.getAttribute("DiscYearly").toString();
                    
                    ontop = new BigDecimal(BNY);
                    mf = new BigDecimal(BN);
                    totalmfOntop = ontop.add(mf);
                    if (BNY.length() == 0 && BN.length() == 0) {
                        if (!isInputValid) {
                            sErrMsg += "<nr> ";
                        }
                        sErrMsg +=
                                "Tab Potongan : Kolom \"Tdk Potong Budget / On Top\" atau Discount \"Potong Budget / MF\" harus diisi.";
                        isInputValid = false;
                    } else {
                        // DO NOTHING, DATA IS VALID
                    }
                }
                
                if (isInputValid) {
                    eventOk();
                } else {
                    showPopup(sErrMsg, potmessage);
                    tabPotongan.setDisclosed(true);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(tabPotongan);
                }
            }

            tabTargerCustomer.setDisclosed(true);
            AdfFacesContext.getCurrentInstance().addPartialTarget(tabTargerCustomer);
        } else {
            showPopup(sIterMsg , potmessage);
        }
    }

    private void eventOk() {
        BindingContainer bindings = getBindings();
        OperationBinding operationBindingCommit =
            bindings.getOperationBinding("Commit");
        operationBindingCommit.execute();

        pdetailProduct.hide();
    }

    public void updateValidComb(String passProdKey, String validValue) {
        DCIteratorBinding dciter =
            ADFUtils.findIterator("PromoProdukView1Iterator");
        for (Row promoProdRow : dciter.getAllRowsInRange()) {
            String promoProdukId =
                ((DBSequence)promoProdRow.getAttribute("PromoProdukId")).toString();
            if (passProdKey.equalsIgnoreCase(promoProdukId.trim())) {
                promoProdRow.setAttribute("ValidComb", validValue);
            }
        }
    }

    public void setPapprove(RichPopup papprove) {
        this.papprove = papprove;
    }

    public RichPopup getPapprove() {
        return papprove;
    }

    public void approveEventBtn(ActionEvent actionEvent) {
        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        papprove.show(hints);
    }

    public void setItVariant(RichInputText itVariant) {
        this.itVariant = itVariant;
    }

    public void custTypeReturnPopupListener(ReturnPopupEvent returnPopupEvent) {
        BindingContainer bindings =
            BindingContext.getCurrent().getCurrentBindingsEntry();

        AttributeBinding exclCustByAttr =
            (AttributeBinding)bindings.getControlBinding("ExclCustBy");
        exclCustByAttr.setInputValue(null);
    }

    public RichInputText getItVariant() {
        return itVariant;
    }

    public void setItBonusVariant(RichInputText itBonusVariant) {
        this.itBonusVariant = itBonusVariant;
    }

    public RichInputText getItBonusVariant() {
        return itBonusVariant;
    }

    public void setSwitchExclCust(UIXSwitcher switchExclCust) {
        this.switchExclCust = switchExclCust;
    }

    public UIXSwitcher getSwitchExclCust() {
        return switchExclCust;
    }

    public void itlovExcludePilih(ValueChangeEvent valueChangeEvent) {
        BindingContainer bindings = this.getBindings();
        AttributeBinding usrTypeCreatorAttr =
            (AttributeBinding)bindings.getControlBinding("UserTypeCreator");
        String usrTypeCreator = (String)usrTypeCreatorAttr.getInputValue();

        String chgNewVal = (String)valueChangeEvent.getNewValue();
        if (chgNewVal.equalsIgnoreCase(prodRegion) &&
            usrTypeCreator.equalsIgnoreCase(userHo)) {
            DCIteratorBinding dciterArea =
                ADFUtils.findIterator("ExclCustAreaView1Iterator");
            RowSetIterator rsiArea = dciterArea.getRowSetIterator();
            for (Row areaRow : dciterArea.getAllRowsInRange()) {
                areaRow.remove();
            }
            rsiArea.closeRowSetIterator();

            DCIteratorBinding dciterLocation =
                ADFUtils.findIterator("ExclCustLocView1Iterator");
            RowSetIterator rsiLocation = dciterLocation.getRowSetIterator();
            for (Row locationRow : dciterLocation.getAllRowsInRange()) {
                locationRow.remove();
            }
            rsiLocation.closeRowSetIterator();

            DCIteratorBinding dciterCustType =
                ADFUtils.findIterator("ExclCustTypeView1Iterator");
            RowSetIterator rsiCustType = dciterCustType.getRowSetIterator();
            for (Row custTypeRow : dciterCustType.getAllRowsInRange()) {
                custTypeRow.remove();
            }
            rsiCustType.closeRowSetIterator();

            DCIteratorBinding dciterCustGroup =
                ADFUtils.findIterator("ExclCustGroupView1Iterator");
            RowSetIterator rsiCustGroup = dciterCustGroup.getRowSetIterator();
            for (Row custGroupRow : dciterCustGroup.getAllRowsInRange()) {
                custGroupRow.remove();
            }
            rsiCustGroup.closeRowSetIterator();

            DCIteratorBinding dciterCustomer =
                ADFUtils.findIterator("ExclCustCustView1Iterator");
            RowSetIterator rsiCustomer = dciterCustomer.getRowSetIterator();
            for (Row customerRow : dciterCustomer.getAllRowsInRange()) {
                customerRow.remove();
            }
            rsiCustomer.closeRowSetIterator();


        } else if (chgNewVal.equalsIgnoreCase(prodArea) &&
                   usrTypeCreator.equalsIgnoreCase(userHo)) {
            DCIteratorBinding dciterRegion =
                ADFUtils.findIterator("ExclCustRegionView1Iterator");
            RowSetIterator rsiRegion = dciterRegion.getRowSetIterator();
            for (Row regionRow : dciterRegion.getAllRowsInRange()) {
                regionRow.remove();
            }
            rsiRegion.closeRowSetIterator();

            DCIteratorBinding dciterLocation =
                ADFUtils.findIterator("ExclCustLocView1Iterator");
            RowSetIterator rsiLocation = dciterLocation.getRowSetIterator();
            for (Row locationRow : dciterLocation.getAllRowsInRange()) {
                locationRow.remove();
            }
            rsiLocation.closeRowSetIterator();

            DCIteratorBinding dciterCustType =
                ADFUtils.findIterator("ExclCustTypeView1Iterator");
            RowSetIterator rsiCustType = dciterCustType.getRowSetIterator();
            for (Row custTypeRow : dciterCustType.getAllRowsInRange()) {
                custTypeRow.remove();
            }
            rsiCustType.closeRowSetIterator();

            DCIteratorBinding dciterCustGroup =
                ADFUtils.findIterator("ExclCustGroupView1Iterator");
            RowSetIterator rsiCustGroup = dciterCustGroup.getRowSetIterator();
            for (Row custGroupRow : dciterCustGroup.getAllRowsInRange()) {
                custGroupRow.remove();
            }
            rsiCustGroup.closeRowSetIterator();

            DCIteratorBinding dciterCustomer =
                ADFUtils.findIterator("ExclCustCustView1Iterator");
            RowSetIterator rsiCustomer = dciterCustomer.getRowSetIterator();
            for (Row customerRow : dciterCustomer.getAllRowsInRange()) {
                customerRow.remove();
            }
            rsiCustomer.closeRowSetIterator();

        } else if (chgNewVal.equalsIgnoreCase(prodLocation) &&
                   usrTypeCreator.equalsIgnoreCase(userHo)) {
            DCIteratorBinding dciterRegion =
                ADFUtils.findIterator("ExclCustRegionView1Iterator");
            RowSetIterator rsiRegion = dciterRegion.getRowSetIterator();
            for (Row regionRow : dciterRegion.getAllRowsInRange()) {
                regionRow.remove();
            }
            rsiRegion.closeRowSetIterator();

            DCIteratorBinding dciterArea =
                ADFUtils.findIterator("ExclCustAreaView1Iterator");
            RowSetIterator rsiArea = dciterArea.getRowSetIterator();
            for (Row areaRow : dciterArea.getAllRowsInRange()) {
                areaRow.remove();
            }
            rsiArea.closeRowSetIterator();

            DCIteratorBinding dciterCustType =
                ADFUtils.findIterator("ExclCustTypeView1Iterator");
            RowSetIterator rsiCustType = dciterCustType.getRowSetIterator();
            for (Row custTypeRow : dciterCustType.getAllRowsInRange()) {
                custTypeRow.remove();
            }
            rsiCustType.closeRowSetIterator();

            DCIteratorBinding dciterCustGroup =
                ADFUtils.findIterator("ExclCustGroupView1Iterator");
            RowSetIterator rsiCustGroup = dciterCustGroup.getRowSetIterator();
            for (Row custGroupRow : dciterCustGroup.getAllRowsInRange()) {
                custGroupRow.remove();
            }
            rsiCustGroup.closeRowSetIterator();

            DCIteratorBinding dciterCustomer =
                ADFUtils.findIterator("ExclCustCustView1Iterator");
            RowSetIterator rsiCustomer = dciterCustomer.getRowSetIterator();
            for (Row customerRow : dciterCustomer.getAllRowsInRange()) {
                customerRow.remove();
            }
            rsiCustomer.closeRowSetIterator();

        } else if (chgNewVal.equalsIgnoreCase(prodCustType) &&
                   usrTypeCreator.equalsIgnoreCase(userHo)) {
            DCIteratorBinding dciterRegion =
                ADFUtils.findIterator("ExclCustRegionView1Iterator");
            RowSetIterator rsiRegion = dciterRegion.getRowSetIterator();
            for (Row regionRow : dciterRegion.getAllRowsInRange()) {
                regionRow.remove();
            }
            rsiRegion.closeRowSetIterator();

            DCIteratorBinding dciterArea =
                ADFUtils.findIterator("ExclCustAreaView1Iterator");
            RowSetIterator rsiArea = dciterArea.getRowSetIterator();
            for (Row areaRow : dciterArea.getAllRowsInRange()) {
                areaRow.remove();
            }
            rsiArea.closeRowSetIterator();

            DCIteratorBinding dciterLocation =
                ADFUtils.findIterator("ExclCustLocView1Iterator");
            RowSetIterator rsiLocation = dciterLocation.getRowSetIterator();
            for (Row locationRow : dciterLocation.getAllRowsInRange()) {
                locationRow.remove();
            }
            rsiLocation.closeRowSetIterator();

            DCIteratorBinding dciterCustGroup =
                ADFUtils.findIterator("ExclCustGroupView1Iterator");
            RowSetIterator rsiCustGroup = dciterCustGroup.getRowSetIterator();
            for (Row custGroupRow : dciterCustGroup.getAllRowsInRange()) {
                custGroupRow.remove();
            }
            rsiCustGroup.closeRowSetIterator();

            DCIteratorBinding dciterCustomer =
                ADFUtils.findIterator("ExclCustCustView1Iterator");
            RowSetIterator rsiCustomer = dciterCustomer.getRowSetIterator();
            for (Row customerRow : dciterCustomer.getAllRowsInRange()) {
                customerRow.remove();
            }
            rsiCustomer.closeRowSetIterator();

        } else if (chgNewVal.equalsIgnoreCase(prodCustGroup) &&
                   usrTypeCreator.equalsIgnoreCase(userHo)) {
            DCIteratorBinding dciterRegion =
                ADFUtils.findIterator("ExclCustRegionView1Iterator");
            RowSetIterator rsiRegion = dciterRegion.getRowSetIterator();
            for (Row regionRow : dciterRegion.getAllRowsInRange()) {
                regionRow.remove();
            }
            rsiRegion.closeRowSetIterator();

            DCIteratorBinding dciterArea =
                ADFUtils.findIterator("ExclCustAreaView1Iterator");
            RowSetIterator rsiArea = dciterArea.getRowSetIterator();
            for (Row areaRow : dciterArea.getAllRowsInRange()) {
                areaRow.remove();
            }
            rsiArea.closeRowSetIterator();

            DCIteratorBinding dciterLocation =
                ADFUtils.findIterator("ExclCustLocView1Iterator");
            RowSetIterator rsiLocation = dciterLocation.getRowSetIterator();
            for (Row locationRow : dciterLocation.getAllRowsInRange()) {
                locationRow.remove();
            }
            rsiLocation.closeRowSetIterator();

            DCIteratorBinding dciterCustType =
                ADFUtils.findIterator("ExclCustTypeView1Iterator");
            RowSetIterator rsiCustType = dciterCustType.getRowSetIterator();
            for (Row custTypeRow : dciterCustType.getAllRowsInRange()) {
                custTypeRow.remove();
            }
            rsiCustType.closeRowSetIterator();

            DCIteratorBinding dciterCustomer =
                ADFUtils.findIterator("ExclCustCustView1Iterator");
            RowSetIterator rsiCustomer = dciterCustomer.getRowSetIterator();
            for (Row customerRow : dciterCustomer.getAllRowsInRange()) {
                customerRow.remove();
            }
            rsiCustomer.closeRowSetIterator();

        } else if (chgNewVal.equalsIgnoreCase(prodCustomer) &&
                   usrTypeCreator.equalsIgnoreCase(userHo)) {
            DCIteratorBinding dciterRegion =
                ADFUtils.findIterator("ExclCustRegionView1Iterator");
            RowSetIterator rsiRegion = dciterRegion.getRowSetIterator();
            for (Row regionRow : dciterRegion.getAllRowsInRange()) {
                regionRow.remove();
            }
            rsiRegion.closeRowSetIterator();

            DCIteratorBinding dciterArea =
                ADFUtils.findIterator("ExclCustAreaView1Iterator");
            RowSetIterator rsiArea = dciterArea.getRowSetIterator();
            for (Row areaRow : dciterArea.getAllRowsInRange()) {
                areaRow.remove();
            }
            rsiArea.closeRowSetIterator();

            DCIteratorBinding dciterLocation =
                ADFUtils.findIterator("ExclCustLocView1Iterator");
            RowSetIterator rsiLocation = dciterLocation.getRowSetIterator();
            for (Row locationRow : dciterLocation.getAllRowsInRange()) {
                locationRow.remove();
            }
            rsiLocation.closeRowSetIterator();

            DCIteratorBinding dciterCustType =
                ADFUtils.findIterator("ExclCustTypeView1Iterator");
            RowSetIterator rsiCustType = dciterCustType.getRowSetIterator();
            for (Row custTypeRow : dciterCustType.getAllRowsInRange()) {
                custTypeRow.remove();
            }
            rsiCustType.closeRowSetIterator();

            DCIteratorBinding dciterCustGroup =
                ADFUtils.findIterator("ExclCustGroupView1Iterator");
            RowSetIterator rsiCustGroup = dciterCustGroup.getRowSetIterator();
            for (Row custGroupRow : dciterCustGroup.getAllRowsInRange()) {
                custGroupRow.remove();
            }
            rsiCustGroup.closeRowSetIterator();

        } else if (chgNewVal.equalsIgnoreCase(propRegion) &&
                   usrTypeCreator.equalsIgnoreCase(userArea)) {
            DCIteratorBinding dciterArea =
                ADFUtils.findIterator("ExclPropCustAreaView1Iterator");
            RowSetIterator rsiArea = dciterArea.getRowSetIterator();
            for (Row areaRow : dciterArea.getAllRowsInRange()) {
                areaRow.remove();
            }
            rsiArea.closeRowSetIterator();

            DCIteratorBinding dciterLocation =
                ADFUtils.findIterator("ExclPropCustLocView1Iterator");
            RowSetIterator rsiLocation = dciterLocation.getRowSetIterator();
            for (Row locationRow : dciterLocation.getAllRowsInRange()) {
                locationRow.remove();
            }
            rsiLocation.closeRowSetIterator();

            DCIteratorBinding dciterCustType =
                ADFUtils.findIterator("ExclPropCustTypeView1Iterator");
            RowSetIterator rsiCustType = dciterCustType.getRowSetIterator();
            for (Row custTypeRow : dciterCustType.getAllRowsInRange()) {
                custTypeRow.remove();
            }
            rsiCustType.closeRowSetIterator();

            DCIteratorBinding dciterCustGroup =
                ADFUtils.findIterator("ExclPropCustGroupView1Iterator");
            RowSetIterator rsiCustGroup = dciterCustGroup.getRowSetIterator();
            for (Row custGroupRow : rsiCustGroup.getAllRowsInRange()) {
                custGroupRow.remove();
            }
            rsiCustGroup.closeRowSetIterator();

            DCIteratorBinding dciterCustomer =
                ADFUtils.findIterator("ExclPropCustCustView1Iterator");
            RowSetIterator rsiCustomer = dciterCustomer.getRowSetIterator();
            for (Row customerRow : dciterCustomer.getAllRowsInRange()) {
                customerRow.remove();
            }
            rsiCustomer.closeRowSetIterator();

        } else if (chgNewVal.equalsIgnoreCase(propArea) &&
                   usrTypeCreator.equalsIgnoreCase(userArea)) {
            DCIteratorBinding dciterRegion =
                ADFUtils.findIterator("ExclPropCustRegionView1Iterator");
            RowSetIterator rsiRegion = dciterRegion.getRowSetIterator();
            for (Row regionRow : dciterRegion.getAllRowsInRange()) {
                regionRow.remove();
            }
            rsiRegion.closeRowSetIterator();

            DCIteratorBinding dciterLocation =
                ADFUtils.findIterator("ExclPropCustLocView1Iterator");
            RowSetIterator rsiLocation = dciterLocation.getRowSetIterator();
            for (Row locationRow : dciterLocation.getAllRowsInRange()) {
                locationRow.remove();
            }
            rsiLocation.closeRowSetIterator();

            DCIteratorBinding dciterCustType =
                ADFUtils.findIterator("ExclPropCustTypeView1Iterator");
            RowSetIterator rsiCustType = dciterCustType.getRowSetIterator();
            for (Row custTypeRow : dciterCustType.getAllRowsInRange()) {
                custTypeRow.remove();
            }
            rsiCustType.closeRowSetIterator();

            DCIteratorBinding dciterCustGroup =
                ADFUtils.findIterator("ExclPropCustGroupView1Iterator");
            RowSetIterator rsiCustGroup = dciterCustGroup.getRowSetIterator();
            for (Row custGroupRow : rsiCustGroup.getAllRowsInRange()) {
                custGroupRow.remove();
            }
            rsiCustGroup.closeRowSetIterator();

            DCIteratorBinding dciterCustomer =
                ADFUtils.findIterator("ExclPropCustCustView1Iterator");
            RowSetIterator rsiCustomer = dciterCustomer.getRowSetIterator();
            for (Row customerRow : dciterCustomer.getAllRowsInRange()) {
                customerRow.remove();
            }
            rsiCustomer.closeRowSetIterator();

        } else if (chgNewVal.equalsIgnoreCase(propLocation) &&
                   usrTypeCreator.equalsIgnoreCase(userArea)) {
            DCIteratorBinding dciterRegion =
                ADFUtils.findIterator("ExclPropCustRegionView1Iterator");
            RowSetIterator rsiRegion = dciterRegion.getRowSetIterator();
            for (Row regionRow : dciterRegion.getAllRowsInRange()) {
                regionRow.remove();
            }
            rsiRegion.closeRowSetIterator();

            DCIteratorBinding dciterArea =
                ADFUtils.findIterator("ExclPropCustAreaView1Iterator");
            RowSetIterator rsiArea = dciterArea.getRowSetIterator();
            for (Row areaRow : dciterArea.getAllRowsInRange()) {
                areaRow.remove();
            }
            rsiArea.closeRowSetIterator();

            DCIteratorBinding dciterCustType =
                ADFUtils.findIterator("ExclPropCustTypeView1Iterator");
            RowSetIterator rsiCustType = dciterCustType.getRowSetIterator();
            for (Row custTypeRow : dciterCustType.getAllRowsInRange()) {
                custTypeRow.remove();
            }
            rsiCustType.closeRowSetIterator();

            DCIteratorBinding dciterCustGroup =
                ADFUtils.findIterator("ExclPropCustGroupView1Iterator");
            RowSetIterator rsiCustGroup = dciterCustGroup.getRowSetIterator();
            for (Row custGroupRow : rsiCustGroup.getAllRowsInRange()) {
                custGroupRow.remove();
            }
            rsiCustGroup.closeRowSetIterator();

            DCIteratorBinding dciterCustomer =
                ADFUtils.findIterator("ExclPropCustCustView1Iterator");
            RowSetIterator rsiCustomer = dciterCustomer.getRowSetIterator();
            for (Row customerRow : dciterCustomer.getAllRowsInRange()) {
                customerRow.remove();
            }
            rsiCustomer.closeRowSetIterator();

        } else if (chgNewVal.equalsIgnoreCase(propCustType) &&
                   usrTypeCreator.equalsIgnoreCase(userArea)) {
            DCIteratorBinding dciterRegion =
                ADFUtils.findIterator("ExclPropCustRegionView1Iterator");
            RowSetIterator rsiRegion = dciterRegion.getRowSetIterator();
            for (Row regionRow : dciterRegion.getAllRowsInRange()) {
                regionRow.remove();
            }
            rsiRegion.closeRowSetIterator();

            DCIteratorBinding dciterArea =
                ADFUtils.findIterator("ExclPropCustAreaView1Iterator");
            RowSetIterator rsiArea = dciterArea.getRowSetIterator();
            for (Row areaRow : dciterArea.getAllRowsInRange()) {
                areaRow.remove();
            }
            rsiArea.closeRowSetIterator();

            DCIteratorBinding dciterLocation =
                ADFUtils.findIterator("ExclPropCustLocView1Iterator");
            RowSetIterator rsiLocation = dciterLocation.getRowSetIterator();
            for (Row locationRow : dciterLocation.getAllRowsInRange()) {
                locationRow.remove();
            }
            rsiLocation.closeRowSetIterator();

            DCIteratorBinding dciterCustGroup =
                ADFUtils.findIterator("ExclPropCustGroupView1Iterator");
            RowSetIterator rsiCustGroup = dciterCustGroup.getRowSetIterator();
            for (Row custGroupRow : rsiCustGroup.getAllRowsInRange()) {
                custGroupRow.remove();
            }
            rsiCustGroup.closeRowSetIterator();

            DCIteratorBinding dciterCustomer =
                ADFUtils.findIterator("ExclPropCustCustView1Iterator");
            RowSetIterator rsiCustomer = dciterCustomer.getRowSetIterator();
            for (Row customerRow : dciterCustomer.getAllRowsInRange()) {
                customerRow.remove();
            }
            rsiCustomer.closeRowSetIterator();

        } else if (chgNewVal.equalsIgnoreCase(propCustGroup) &&
                   usrTypeCreator.equalsIgnoreCase(userArea)) {
            DCIteratorBinding dciterRegion =
                ADFUtils.findIterator("ExclPropCustRegionView1Iterator");
            RowSetIterator rsiRegion = dciterRegion.getRowSetIterator();
            for (Row regionRow : dciterRegion.getAllRowsInRange()) {
                regionRow.remove();
            }
            rsiRegion.closeRowSetIterator();

            DCIteratorBinding dciterArea =
                ADFUtils.findIterator("ExclPropCustAreaView1Iterator");
            RowSetIterator rsiArea = dciterArea.getRowSetIterator();
            for (Row areaRow : dciterArea.getAllRowsInRange()) {
                areaRow.remove();
            }
            rsiArea.closeRowSetIterator();

            DCIteratorBinding dciterLocation =
                ADFUtils.findIterator("ExclPropCustLocView1Iterator");
            RowSetIterator rsiLocation = dciterLocation.getRowSetIterator();
            for (Row locationRow : dciterLocation.getAllRowsInRange()) {
                locationRow.remove();
            }
            rsiLocation.closeRowSetIterator();

            DCIteratorBinding dciterCustType =
                ADFUtils.findIterator("ExclPropCustTypeView1Iterator");
            RowSetIterator rsiCustType = dciterCustType.getRowSetIterator();
            for (Row custTypeRow : dciterCustType.getAllRowsInRange()) {
                custTypeRow.remove();
            }
            rsiCustType.closeRowSetIterator();

            DCIteratorBinding dciterCustomer =
                ADFUtils.findIterator("ExclPropCustCustView1Iterator");
            RowSetIterator rsiCustomer = dciterCustomer.getRowSetIterator();
            for (Row customerRow : dciterCustomer.getAllRowsInRange()) {
                customerRow.remove();
            }
            rsiCustomer.closeRowSetIterator();

        } else if (chgNewVal.equalsIgnoreCase(propCustomer) &&
                   usrTypeCreator.equalsIgnoreCase(userArea)) {
            DCIteratorBinding dciterRegion =
                ADFUtils.findIterator("ExclPropCustRegionView1Iterator");
            RowSetIterator rsiRegion = dciterRegion.getRowSetIterator();
            for (Row regionRow : dciterRegion.getAllRowsInRange()) {
                regionRow.remove();
            }
            rsiRegion.closeRowSetIterator();

            DCIteratorBinding dciterArea =
                ADFUtils.findIterator("ExclPropCustAreaView1Iterator");
            RowSetIterator rsiArea = dciterArea.getRowSetIterator();
            for (Row areaRow : dciterArea.getAllRowsInRange()) {
                areaRow.remove();
            }
            rsiArea.closeRowSetIterator();

            DCIteratorBinding dciterLocation =
                ADFUtils.findIterator("ExclPropCustLocView1Iterator");
            RowSetIterator rsiLocation = dciterLocation.getRowSetIterator();
            for (Row locationRow : dciterLocation.getAllRowsInRange()) {
                locationRow.remove();
            }
            rsiLocation.closeRowSetIterator();

            DCIteratorBinding dciterCustType =
                ADFUtils.findIterator("ExclPropCustTypeView1Iterator");
            RowSetIterator rsiCustType = dciterCustType.getRowSetIterator();
            for (Row custTypeRow : dciterCustType.getAllRowsInRange()) {
                custTypeRow.remove();
            }
            rsiCustType.closeRowSetIterator();

            DCIteratorBinding dciterCustGroup =
                ADFUtils.findIterator("ExclPropCustGroupView1Iterator");
            RowSetIterator rsiCustGroup = dciterCustGroup.getRowSetIterator();
            for (Row custGroupRow : rsiCustGroup.getAllRowsInRange()) {
                custGroupRow.remove();
            }
            rsiCustGroup.closeRowSetIterator();

        } else {
            JSFUtils.addFacesErrorMessage("Error",
                                          "\"Tipe Customer\" tidak dikenali.");
        }

        clearAllCustExclude();
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchExclCust);
    }

    public void addPromoBonus(ActionEvent actionEvent) {
        BindingContainer bindings =
            BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dciter =
            (DCIteratorBinding)bindings.get("PromoBonusView1Iterator");
        RowSetIterator rsi = dciter.getRowSetIterator();
        Row lastRow = rsi.last();

        DCIteratorBinding dciterUOMTarget =
            ADFUtils.findIterator("TargetView1Iterator");
        Row rTarget = dciterUOMTarget.getCurrentRow();
        String uomTar = (String)rTarget.getAttribute("Uom");
        int lastRowIndex = rsi.getRangeIndexOf(lastRow);
        Row newRow = rsi.createRow();
        newRow.setNewRowState(Row.STATUS_INITIALIZED);
        newRow.setAttribute("Uom", uomTar);
        //add row to last index + 1 so it becomes last in the range set
        rsi.insertRowAtRangeIndex(lastRowIndex + 1, newRow);
        //make row the current row so it is displayed correctly
        rsi.setCurrentRow(newRow);
    }

    public void addDiscount(ActionEvent actionEvent) {
        BindingContainer bindings =
            BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dciter =
            (DCIteratorBinding)bindings.get("DiscountView1Iterator");
        RowSetIterator rsi = dciter.getRowSetIterator();
        Row firstRow = rsi.first();
        Row lastRow = rsi.last();

        DCIteratorBinding dciterUOMTarget =
            ADFUtils.findIterator("TargetView1Iterator");
        Row rTarget = dciterUOMTarget.getCurrentRow();
        String uomTar = (String)rTarget.getAttribute("Uom");

        if (dciter.getEstimatedRowCount() > 0) {
            int lastRowIndex = rsi.getRangeIndexOf(lastRow);

            //Get first tipe potongan
            String tipePotongan =
                firstRow.getAttribute("TipePotongan") == null ? "" :
                firstRow.getAttribute("TipePotongan").toString();

            //Get last inserted row qty to
            Number lastRowQtyTo =
                lastRow.getAttribute("QtyTo") == null ? maxNumber :
                (Number)lastRow.getAttribute("QtyTo");
            Number lastRowKelipatan =
                lastRow.getAttribute("Kelipatan") == null ? new Number(0) :
                (Number)lastRow.getAttribute("Kelipatan");

            String TipePerhitungan =
                lastRow.getAttribute("TipePerhitungan") == null ? "" :
                lastRow.getAttribute("TipePerhitungan").toString();
            Integer getCheckRowStatus =
                Integer.parseInt(lastRow.getAttribute("CheckRowStatus").toString());
            String rowKelipatan = lastRowKelipatan.toString();
            //                        System.out.println("lastRowKelipatan "+lastRowKelipatan);
            //                        System.out.println("TipePerhitungan "+TipePerhitungan);
            if (rowKelipatan.equalsIgnoreCase("0") &&
                TipePerhitungan.equalsIgnoreCase(tipeHitungTdkKelipatan)) {
                if (lastRowQtyTo.compareTo(maxNumber) == 0) {
                    JSFUtils.addFacesWarningMessage("Nilai \"Qty To\" harus diisi dan kurang dari " +
                                                    maxNumber);
                } else {
                    Row newRow = rsi.createRow();
                    newRow.setNewRowState(Row.STATUS_INITIALIZED);
                    newRow.setAttribute("Uom", uomTar);
                    newRow.setAttribute("QtyFrom", lastRowQtyTo.add(1));
                    newRow.setAttribute("QtyTo", maxNumber);
                    newRow.setAttribute("TipePotongan", tipePotongan);

                    //add row to last index + 1 so it becomes last in the range set
                    rsi.insertRowAtRangeIndex(lastRowIndex + 1, newRow);
                    //make row the current row so it is displayed correctly
                    rsi.setCurrentRow(newRow);
                    newRow.validate();
        //                    lovTipePerhitungan.setDisabled(true);
                    itqtyFromDisc.setReadOnly(true);
        //                    AdfFacesContext.getCurrentInstance().addPartialTarget(lovTipePerhitungan);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(itqtyFromDisc);

                }
            } else {
                if (lastRowQtyTo.compareTo(maxNumber) == 0) {
                    JSFUtils.addFacesWarningMessage("Nilai \"Qty To\" harus diisi dan kurang dari " +
                                                    maxNumber);
                } else {
                    Row newRow = rsi.createRow();
                    newRow.setNewRowState(Row.STATUS_INITIALIZED);
                    newRow.setAttribute("Uom", uomTar);
                    newRow.setAttribute("QtyFrom", lastRowQtyTo.add(1));
                    newRow.setAttribute("QtyTo", maxNumber);
                    newRow.setAttribute("Kelipatan", lastRowKelipatan);
                    newRow.setAttribute("TipePerhitungan", tipeHitungKelipatan);
                    newRow.setAttribute("TipePotongan", tipePotongan);
                    //add row to last index + 1 so it becomes last in the range set
                    rsi.insertRowAtRangeIndex(lastRowIndex + 1, newRow);
                    //make row the current row so it is displayed correctly
                    rsi.setCurrentRow(newRow);
                    newRow.validate();
        //                    lovTipePerhitungan.setDisabled(true);
                    itqtyFromDisc.setReadOnly(true);
        //                    AdfFacesContext.getCurrentInstance().addPartialTarget(lovTipePerhitungan);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(itqtyFromDisc);
                }
            }
        } else {
            Row newRow = rsi.createRow();
            newRow.setNewRowState(Row.STATUS_INITIALIZED);
            newRow.setAttribute("Uom", uomTar);
            newRow.setAttribute("QtyFrom", new Number(1));
            newRow.setAttribute("QtyTo", maxNumber);
            newRow.validate();
        }
    }

    public void refreshOntop(ValueChangeEvent valueChangeEvent) {
        String socType = socTypePotongan.getValue().toString();
        String socTipeHitung = lovTipePerhitungan.getValue().toString();
        
        DCIteratorBinding dciterTarget =
            ADFUtils.findIterator("TargetView1Iterator");
        Row row = dciterTarget.getCurrentRow();
        BigDecimal ontop = BigDecimal.ZERO;
        BigDecimal rOntop = BigDecimal.ZERO;
        BigDecimal maxOntop = BigDecimal.ZERO;
        BigDecimal RasioOntop = BigDecimal.ZERO;
        BigDecimal rasioTotal = BigDecimal.ZERO;
        BigDecimal rasioT = BigDecimal.ZERO;
        Number qty =
            (Number)row.getAttribute("Qty") == null ? new Number(0) : (Number)row.getAttribute("Qty");
        Number value =
            (Number)row.getAttribute("Value") == null ? new Number(0) :
            (Number)row.getAttribute("Value");
        Number price =
            (Number)row.getAttribute("Price") == null ? new Number(0) :
            (Number)row.getAttribute("Price");

        if (value.compareTo(zeroNum) < 0 ||
            value.compareTo(zeroNum) > 0) {

            if (socType.equalsIgnoreCase(tipePotonganPercent)) {
                if (socTipeHitung.equalsIgnoreCase(tipeHitungTdkKelipatan)) {
                    rOntop =
                            new BigDecimal(rowOntop.getValue().toString().replaceAll(",",
                                                                                     ""));
                    String rasio =
                        otRasioMf.getValue() == "" ? "0" : otRasioMf.getValue() ==
                                                           null ? "0" :
                                                           otRasioMf.getValue().toString();
                    rasioT =
                            new BigDecimal(rasio.toString().replaceAll(" ", "").replaceAll("%",
                                                                                           ""));
                    ontop =(value.multiply(rOntop)).getBigDecimalValue().divide(bdHundred).setScale(2,RoundingMode.HALF_UP);
                    otOnTop.setSubmittedValue(ontop);
                    
                    RasioOntop =
                            ontop.divide(value.getBigDecimalValue(), MathContext.DECIMAL128).multiply(bdHundred).setScale(2,
                                                                                                                          RoundingMode.HALF_UP);
                    //                        ontop.divide(value.getBigDecimalValue()).multiply(bdHundred).setScale(2,RoundingMode.HALF_UP);
                    String rasOntop = RasioOntop.toString();
                    //            System.out.println("rasOntop = "+rasOntop);
                    otRasioOntop.setSubmittedValue(rasOntop);
                    rasioTotal = RasioOntop.add(rasioT);
                    String total = rasioTotal.toString();
                    otRasioTotal.setSubmittedValue(total);
                    DCIteratorBinding dciterPromoProduk =
                        ADFUtils.findIterator("PromoProdukView1Iterator");
                    Row r = dciterPromoProduk.getCurrentRow();
                    r.setAttribute("DiscOnTop", ontop);
                    r.setAttribute("DiscRasioOnTop", rasOntop);
                    r.setAttribute("DiscRasioTotal1", total);
                    // dciterPromoProduk.getDataControl().commitTransaction();
                } else if (socTipeHitung.equalsIgnoreCase(tipeHitungKelipatan)) {
                    // BY PERCENT ON TOP - KELIPATAN
                    BigDecimal newChangedOnTopValue =
                        new BigDecimal(valueChangeEvent.getNewValue() ==
                                       "" ? "0" :
                                       valueChangeEvent.getNewValue() ==
                                       null ? "0" :
                                       valueChangeEvent.getNewValue().toString());
                    
                    Number kelipatanVal = itKelipatanDisc.getValue() == 
                                          "" ? new Number(0) : 
                                          itKelipatanDisc.getValue() == 
                                          null ? new Number(0) : 
                                          (Number)itKelipatanDisc.getValue();
                    /*DEBUG
                    System.out.println("=====================================");
                    System.out.println("QTY            : " + qty.getBigDecimalValue());
                    System.out.println("KELIPATAN      : " + kelipatanVal.getBigDecimalValue());
                    System.out.println("PERCENT OT DISC: " + newChangedOnTopValue);
                    System.out.println("PRICE          : " + price.getBigDecimalValue());
                    System.out.println("-------------------------------------");
                    */
                    BigDecimal roundTgtDivKelipatan = qty.getBigDecimalValue().divide(kelipatanVal.getBigDecimalValue(),0,RoundingMode.DOWN);
                    BigDecimal priceMulPercentDisc = price.getBigDecimalValue().multiply(newChangedOnTopValue.divide(new BigDecimal(100)));
                    ontop = roundTgtDivKelipatan.multiply(kelipatanVal.getBigDecimalValue()).multiply(priceMulPercentDisc);
                    /*DEBUG
                    System.out.println("QTY X KEL ROUND : " + roundTgtDivKelipatan);                                                             
                    System.out.println("PRICE PERCENT   : " + priceMulPercentDisc);
                    System.out.println("-------------------------------------");
                    System.out.println("VAL ONTOP AMOUNT: " + ontop);
                    System.out.println("=====================================");
                    */
                    otOnTop.setSubmittedValue(ontop);
                    RasioOntop =
                            ontop.divide(value.getBigDecimalValue(), MathContext.DECIMAL128).multiply(bdHundred).setScale(2,
                                                                                                                          RoundingMode.HALF_UP);
                    String rasOntop = RasioOntop.toString();
                    otRasioOntop.setSubmittedValue(rasOntop);
                    String rasio =
                        otRasioMf.getValue() == "" ? "0" : otRasioMf.getValue() ==
                                                           null ? "0" :
                                                           otRasioMf.getValue().toString();
                    rasioT =
                            new BigDecimal(rasio.toString().replaceAll(" ", "").replaceAll("%",
                                                                                           ""));
                    rasioTotal = RasioOntop.add(rasioT);
                    String total = rasioTotal.toString();
                    otRasioTotal.setSubmittedValue(total);
                    DCIteratorBinding dciterPromoProduk =
                        ADFUtils.findIterator("PromoProdukView1Iterator");
                    Row r = dciterPromoProduk.getCurrentRow();
                    r.setAttribute("DiscOnTop", ontop);
                    r.setAttribute("DiscRasioOnTop", rasOntop);
                    r.setAttribute("DiscRasioTotal1", total);
                    // dciterPromoProduk.getDataControl().commitTransaction();
                }

                AdfFacesContext.getCurrentInstance().addPartialTarget(otOnTop);
                AdfFacesContext.getCurrentInstance().addPartialTarget(otRasioOntop);
                AdfFacesContext.getCurrentInstance().addPartialTarget(otRasioTotal);
            } else {
                if (socTipeHitung.equalsIgnoreCase(tipeHitungTdkKelipatan)) {
                    String discId = "";
                    DCBindingContainer bindings =
                        (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
                    DCIteratorBinding dcItteratorBindings =
                        bindings.findIteratorBinding("DiscountView1Iterator");
                    ViewObject voTableData = dcItteratorBindings.getViewObject();
                    Row rowSelected = voTableData.getCurrentRow();
                    if (rowSelected.getAttribute("DiscountId") != null) {
                        discId = rowSelected.getAttribute("DiscountId").toString();
                    }
                    DCIteratorBinding dciterDiscount1 =
                        ADFUtils.findIterator("DiscountView1Iterator");
                    for (Row r : dciterDiscount1.getAllRowsInRange()) {
                        String discIdComp =
                            r.getAttribute("DiscountId").toString();
                        if (!discId.equalsIgnoreCase(discIdComp)) {
                            String valueTop =
                                r.getAttribute("DiscNonYearly") == null ? "0" :
                                r.getAttribute("DiscNonYearly") == "" ? "0" :
                                r.getAttribute("DiscNonYearly").toString().replaceAll(",",
                                                                                      "");
                            BigDecimal newChangedOnTopValue =
                                new BigDecimal(valueChangeEvent.getNewValue() ==
                                               "" ? "0" :
                                               valueChangeEvent.getNewValue() ==
                                               null ? "0" :
                                               valueChangeEvent.getNewValue().toString());
                            BigDecimal ontopValue =
                                new BigDecimal(valueTop) == null ?
                                new BigDecimal(0) : new BigDecimal(valueTop);
    
                            if (maxOntop.compareTo(ontopValue) < 0) {
                                maxOntop = ontopValue;
                            }
    
                            if (maxOntop.compareTo(newChangedOnTopValue) < 0) {
                                maxOntop = newChangedOnTopValue;
                            }
                        } else {
                            BigDecimal newChangedOnTopValue =
                                new BigDecimal(valueChangeEvent.getNewValue() ==
                                               "" ? "0" :
                                               valueChangeEvent.getNewValue() ==
                                               null ? "0" :
                                               valueChangeEvent.getNewValue().toString());
                            if (maxOntop.compareTo(newChangedOnTopValue) < 0) {
                                maxOntop = newChangedOnTopValue;
                            }
                        }
                    }
                    ontop = qty.getBigDecimalValue().multiply(maxOntop);
                    otOnTop.setSubmittedValue(ontop);
                    RasioOntop =
                            ontop.divide(value.getBigDecimalValue(), MathContext.DECIMAL128).multiply(bdHundred).setScale(2,
                                                                                                                          RoundingMode.HALF_UP);
                    String rasOntop = RasioOntop.toString();
                    otRasioOntop.setSubmittedValue(rasOntop);
                    String rasio =
                        otRasioMf.getValue() == "" ? "0" : otRasioMf.getValue() ==
                                                           null ? "0" :
                                                           otRasioMf.getValue().toString();
                    rasioT =
                            new BigDecimal(rasio.toString().replaceAll(" ", "").replaceAll("%",
                                                                                           ""));
                    rasioTotal = RasioOntop.add(rasioT);
                    String total = rasioTotal.toString();
                    otRasioTotal.setSubmittedValue(total);
                    DCIteratorBinding dciterPromoProduk =
                        ADFUtils.findIterator("PromoProdukView1Iterator");
                    Row r = dciterPromoProduk.getCurrentRow();
                    r.setAttribute("DiscOnTop", ontop);
                    r.setAttribute("DiscRasioOnTop", rasOntop);
                    r.setAttribute("DiscRasioTotal1", total);
                    // dciterPromoProduk.getDataControl().commitTransaction();
                } else if (socTipeHitung.equalsIgnoreCase(tipeHitungKelipatan)) {
                    // BY AMOUNT ON TOP - KELIPATAN
                    BigDecimal newChangedOnTopValue =
                        new BigDecimal(valueChangeEvent.getNewValue() ==
                                       "" ? "0" :
                                       valueChangeEvent.getNewValue() ==
                                       null ? "0" :
                                       valueChangeEvent.getNewValue().toString());
                    
                    Number kelipatanVal = itKelipatanDisc.getValue() == 
                                          "" ? new Number(0) : 
                                          itKelipatanDisc.getValue() == 
                                          null ? new Number(0) : 
                                          (Number)itKelipatanDisc.getValue();
                    /*DEBUG
                    System.out.println("=====================================");
                    System.out.println("QTY           : " + qty.getBigDecimalValue());
                    System.out.println("KELIPATAN     : " + kelipatanVal.getBigDecimalValue());
                    System.out.println("AMOUNT OT DISC: " + newChangedOnTopValue);
                    System.out.println("-------------------------------------");
                    */
                    try {
                        BigDecimal roundTgtDivKelipatan =
                            qty.getBigDecimalValue().divide(kelipatanVal.getBigDecimalValue(),
                                                            0,
                                                            RoundingMode.DOWN);
                        ontop =
                                roundTgtDivKelipatan.multiply(kelipatanVal.getBigDecimalValue()).multiply(newChangedOnTopValue);
                    } catch (java.lang.ArithmeticException ae) {
                        JSFUtils.addFacesWarningMessage("Nilai kelipatan harus diisi.");
                    } catch (Exception e) {
                        JSFUtils.addFacesErrorMessage("Error", e.getLocalizedMessage());
                    }
                    /*DEBUG
                    System.out.println("QTY X KEL ROUND : " + roundTgtDivKelipatan);
                    System.out.println("-------------------------------------");
                    System.out.println("VAL ONTOP AMOUNT: " + ontop);
                    System.out.println("=====================================");
                    */
                    otOnTop.setSubmittedValue(ontop);
                    RasioOntop =
                            ontop.divide(value.getBigDecimalValue(), MathContext.DECIMAL128).multiply(bdHundred).setScale(2,
                                                                                                                          RoundingMode.HALF_UP);
                    String rasOntop = RasioOntop.toString();
                    otRasioOntop.setSubmittedValue(rasOntop);
                    String rasio =
                        otRasioMf.getValue() == "" ? "0" : otRasioMf.getValue() ==
                                                           null ? "0" :
                                                           otRasioMf.getValue().toString();
                    rasioT =
                            new BigDecimal(rasio.toString().replaceAll(" ", "").replaceAll("%",
                                                                                           ""));
                    rasioTotal = RasioOntop.add(rasioT);
                    String total = rasioTotal.toString();
                    otRasioTotal.setSubmittedValue(total);
                    DCIteratorBinding dciterPromoProduk =
                        ADFUtils.findIterator("PromoProdukView1Iterator");
                    Row r = dciterPromoProduk.getCurrentRow();
                    r.setAttribute("DiscOnTop", ontop);
                    r.setAttribute("DiscRasioOnTop", rasOntop);
                    r.setAttribute("DiscRasioTotal1", total);
                    // dciterPromoProduk.getDataControl().commitTransaction();
                }

                AdfFacesContext.getCurrentInstance().addPartialTarget(otOnTop);
                AdfFacesContext.getCurrentInstance().addPartialTarget(otRasioOntop);
                AdfFacesContext.getCurrentInstance().addPartialTarget(otRasioTotal);
            }
            //            AdfFacesContext.getCurrentInstance().addPartialTarget(tblListProductBonus);
        } else {
            JSFUtils.addFacesWarningMessage("Nilai \"Qty. Target\", \"Harga\"  dan nilai \"Tdk Potong Budget / On Top\" tidak boleh 0 (nol) atau kosong.");
        }
    }

    public void refreshMf(ValueChangeEvent valueChangeEvent) {
        String socType = socTypePotongan.getValue().toString();
        String socTipeHitung = lovTipePerhitungan.getValue().toString();
        
        DCIteratorBinding dciterTarget =
            ADFUtils.findIterator("TargetView1Iterator");
        Row row = dciterTarget.getCurrentRow();
        BigDecimal mf = BigDecimal.ZERO;
        BigDecimal rMf = BigDecimal.ZERO;
        BigDecimal maxMf = BigDecimal.ZERO;
        BigDecimal RasioMf = BigDecimal.ZERO;
        BigDecimal rasioTotal = BigDecimal.ZERO;
        BigDecimal rasioT = BigDecimal.ZERO;
        Number qty =
            (Number)row.getAttribute("Qty") == null ? new Number(0) : (Number)row.getAttribute("Qty");
        Number value =
            (Number)row.getAttribute("Value") == null ? new Number(0) :
            (Number)row.getAttribute("Value");
        Number price =
            (Number)row.getAttribute("Price") == null ? new Number(0) :
            (Number)row.getAttribute("Price");

        if (value.compareTo(zeroNum) < 0 ||
            value.compareTo(zeroNum) > 0) {
            
            if (socType.equalsIgnoreCase(tipePotonganPercent)) {
                if (socTipeHitung.equalsIgnoreCase(tipeHitungTdkKelipatan)) {
                    rMf =
        new BigDecimal(rowMf.getValue().toString().replaceAll(",", ""));
                    mf =
         ((value.multiply(rMf)).getBigDecimalValue()).divide(bdHundred).setScale(2,RoundingMode.HALF_UP);
                    otMF.setSubmittedValue(mf);
                    RasioMf =
                            mf.divide(value.getBigDecimalValue(), MathContext.DECIMAL128).multiply(bdHundred).setScale(2,
                                                                                                                       RoundingMode.HALF_UP);
                    String rasMf = RasioMf.toString();
                    otRasioMf.setSubmittedValue(rasMf);
                    rasioT =
                            new BigDecimal(otRasioOntop.getValue().toString().replaceAll(" ",
                                                                                         "").replaceAll("%",
                                                                                                        ""));
                    rasioTotal = RasioMf.add(rasioT);
                    String Total = rasioTotal.toString();
                    otRasioTotal.setSubmittedValue(Total);
        
                    DCIteratorBinding dciterPromoProduk =
                        ADFUtils.findIterator("PromoProdukView1Iterator");
                    Row r = dciterPromoProduk.getCurrentRow();
                    r.setAttribute("DiscMf", mf);
                    r.setAttribute("DiscRasioMf", rasMf);
                    r.setAttribute("DiscRasioTotal1", Total);
                    // dciterPromoProduk.getDataControl().commitTransaction();
                } else if (socTipeHitung.equalsIgnoreCase(tipeHitungKelipatan)) {
                    // BY PERCENT MF - KELIPATAN
                    BigDecimal newChangedMfValue =
                        new BigDecimal(valueChangeEvent.getNewValue() ==
                                       "" ? "0" :
                                       valueChangeEvent.getNewValue() ==
                                       null ? "0" :
                                       valueChangeEvent.getNewValue().toString());
                    
                    Number kelipatanVal = itKelipatanDisc.getValue() == 
                                          "" ? new Number(0) : 
                                          itKelipatanDisc.getValue() == 
                                          null ? new Number(0) : 
                                          (Number)itKelipatanDisc.getValue();
                    /*DEBUG
                    System.out.println("=====================================");
                    System.out.println("QTY            : " + qty.getBigDecimalValue());
                    System.out.println("KELIPATAN      : " + kelipatanVal.getBigDecimalValue());
                    System.out.println("PERCENT MF DISC: " + newChangedMfValue);
                    System.out.println("PRICE          : " + price.getBigDecimalValue());
                    System.out.println("-------------------------------------");
                    */
                    BigDecimal roundTgtDivKelipatan = qty.getBigDecimalValue().divide(kelipatanVal.getBigDecimalValue(),0,RoundingMode.DOWN);
                    BigDecimal priceMulPercentDisc = price.getBigDecimalValue().multiply(newChangedMfValue.divide(new BigDecimal(100)));
                    mf = (roundTgtDivKelipatan.multiply(kelipatanVal.getBigDecimalValue())).multiply(priceMulPercentDisc);
                    /*                                                                                                       
                    System.out.println("QTY X KEL ROUND : " + roundTgtDivKelipatan);                                                             
                    System.out.println("PRICE PERCENT   : " + priceMulPercentDisc);
                    System.out.println("-------------------------------------");
                    System.out.println("VAL MF AMOUNT   : " + mf);
                    System.out.println("=====================================");
                    */
                    otOnTop.setSubmittedValue(mf);
                    otMF.setSubmittedValue(mf);
                    RasioMf =
                            mf.divide(value.getBigDecimalValue(), MathContext.DECIMAL128).multiply(bdHundred).setScale(2,
                                                                                                                       RoundingMode.HALF_UP);
                    String rasMf = RasioMf.toString();
                    otRasioMf.setSubmittedValue(rasMf);
                    rasioT =
                            new BigDecimal(otRasioOntop.getValue().toString().replaceAll(" ",
                                                                                         "").replaceAll("%",
                                                                                                        ""));
                    rasioTotal = RasioMf.add(rasioT);
                    String Total = rasioTotal.toString();
                    otRasioTotal.setSubmittedValue(Total);
                    
                    DCIteratorBinding dciterPromoProduk =
                        ADFUtils.findIterator("PromoProdukView1Iterator");
                    Row r = dciterPromoProduk.getCurrentRow();
                    r.setAttribute("DiscMf", mf);
                    r.setAttribute("DiscRasioMf", rasMf);
                    r.setAttribute("DiscRasioTotal1", Total);
                    // dciterPromoProduk.getDataControl().commitTransaction();
                }
    
                AdfFacesContext.getCurrentInstance().addPartialTarget(otMF);
                AdfFacesContext.getCurrentInstance().addPartialTarget(otRasioMf);
                AdfFacesContext.getCurrentInstance().addPartialTarget(otRasioTotal);
            } else {
                if (socTipeHitung.equalsIgnoreCase(tipeHitungTdkKelipatan)) {
                    String discId = "";
                    DCBindingContainer bindings =
                        (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
                    DCIteratorBinding dcItteratorBindings =
                        bindings.findIteratorBinding("DiscountView1Iterator");
                    ViewObject voTableData = dcItteratorBindings.getViewObject();
        
                    Row rowSelected = voTableData.getCurrentRow();
                    if (rowSelected.getAttribute("DiscountId") != null) {
                        discId = rowSelected.getAttribute("DiscountId").toString();
                    }
                    DCIteratorBinding dciterDiscount1 =
                        ADFUtils.findIterator("DiscountView1Iterator");
                    for (Row r : dciterDiscount1.getAllRowsInRange()) {
                        String discIdComp = r.getAttribute("DiscountId").toString();
                        if (!discId.equalsIgnoreCase(discIdComp)) {
                            String valueMF =
                                r.getAttribute("DiscYearly") == null ? "0" :
                                r.getAttribute("DiscYearly") == "" ? "0" :
                                r.getAttribute("DiscYearly").toString().replaceAll(",",
                                                                                   "");
                            BigDecimal mfValue =
                                new BigDecimal(valueMF) == null ? new BigDecimal(0) :
                                new BigDecimal(valueMF);
                            BigDecimal newChangedMfValue =
                                new BigDecimal(valueChangeEvent.getNewValue() == "" ?
                                               "0" :
                                               valueChangeEvent.getNewValue() == null ?
                                               "0" :
                                               valueChangeEvent.getNewValue().toString());
                            if (maxMf.compareTo(mfValue) < 0) {
                                maxMf = mfValue;
                            }
                            if (maxMf.compareTo(newChangedMfValue) < 0) {
                                maxMf = newChangedMfValue;
                            }
                        } else {
                            BigDecimal newChangedMfValue =
                                new BigDecimal(valueChangeEvent.getNewValue() == "" ?
                                               "0" :
                                               valueChangeEvent.getNewValue() == null ?
                                               "0" :
                                               valueChangeEvent.getNewValue().toString());
                            if (maxMf.compareTo(newChangedMfValue) < 0) {
                                maxMf = newChangedMfValue;
                            }
                        }
                    }
                    mf = qty.getBigDecimalValue().multiply(maxMf);
                    otMF.setSubmittedValue(mf);
                    RasioMf =
                            mf.divide(value.getBigDecimalValue(), MathContext.DECIMAL128).multiply(bdHundred).setScale(2,
                                                                                                                       RoundingMode.HALF_UP);
                    //                    mf.divide(value.getBigDecimalValue(), 3, RoundingMode.HALF_UP).multiply(bdHundred).setScale(2,RoundingMode.HALF_UP);
                    String rasMf = RasioMf.toString();
                    otRasioMf.setSubmittedValue(rasMf);
                    rasioT =
                            new BigDecimal(otRasioOntop.getValue().toString().replaceAll(" ",
                                                                                         "").replaceAll("%",
                                                                                                        ""));
                    rasioTotal = RasioMf.add(rasioT);
                    String Total = rasioTotal.toString();
                    otRasioTotal.setSubmittedValue(Total);
        
                    DCIteratorBinding dciterPromoProduk =
                        ADFUtils.findIterator("PromoProdukView1Iterator");
                    Row r = dciterPromoProduk.getCurrentRow();
                    r.setAttribute("DiscMf", mf);
                    r.setAttribute("DiscRasioMf", rasMf);
                    r.setAttribute("DiscRasioTotal1", Total);
                    // dciterPromoProduk.getDataControl().commitTransaction();
                } else if (socTipeHitung.equalsIgnoreCase(tipeHitungKelipatan)) {
                    // BY AMOUNT MF - KELIPATAN
                    BigDecimal newChangedMfValue =
                        new BigDecimal(valueChangeEvent.getNewValue() == "" ?
                                       "0" :
                                       valueChangeEvent.getNewValue() == null ?
                                       "0" :
                                       valueChangeEvent.getNewValue().toString());
                    
                    Number kelipatanVal = itKelipatanDisc.getValue() == 
                                          "" ? new Number(0) : 
                                          itKelipatanDisc.getValue() == 
                                          null ? new Number(0) : 
                                          (Number)itKelipatanDisc.getValue();
                    /*DEBUG
                    System.out.println("=====================================");
                    System.out.println("QTY           : " + qty.getBigDecimalValue());
                    System.out.println("KELIPATAN     : " + kelipatanVal.getBigDecimalValue());
                    System.out.println("AMOUNT MF DISC: " + newChangedMfValue);
                    System.out.println("PRICE         : " + price.getBigDecimalValue());
                    System.out.println("-------------------------------------");
                    */
                    try {
                        BigDecimal roundTgtDivKelipatan =
                            qty.getBigDecimalValue().divide(kelipatanVal.getBigDecimalValue(),
                                                            0,
                                                            RoundingMode.DOWN);
                        mf =
                        (roundTgtDivKelipatan.multiply(kelipatanVal.getBigDecimalValue())).multiply(newChangedMfValue);
                    } catch (java.lang.ArithmeticException ae) {
                        JSFUtils.addFacesWarningMessage("Nilai kelipatan harus diisi.");
                    } catch (Exception e) {
                        JSFUtils.addFacesErrorMessage("Error", e.getLocalizedMessage());
                    }
                    /*DEBUG
                    System.out.println("QTY X KEL ROUND : " + roundTgtDivKelipatan);                        
                    System.out.println("-------------------------------------");
                    System.out.println("VAL MF AMOUNT: " + mf);
                    System.out.println("=====================================");
                    */
                    otMF.setSubmittedValue(mf);
                    RasioMf =
                            mf.divide(value.getBigDecimalValue(), MathContext.DECIMAL128).multiply(bdHundred).setScale(2,
                                                                                                                       RoundingMode.HALF_UP);
                    //                    mf.divide(value.getBigDecimalValue(), 3, RoundingMode.HALF_UP).multiply(bdHundred).setScale(2,RoundingMode.HALF_UP);
                    String rasMf = RasioMf.toString();
                    otRasioMf.setSubmittedValue(rasMf);
                    rasioT =
                            new BigDecimal(otRasioOntop.getValue().toString().replaceAll(" ",
                                                                                         "").replaceAll("%",
                                                                                                        ""));
                    rasioTotal = RasioMf.add(rasioT);
                    String Total = rasioTotal.toString();
                    otRasioTotal.setSubmittedValue(Total);
                    
                    DCIteratorBinding dciterPromoProduk =
                        ADFUtils.findIterator("PromoProdukView1Iterator");
                    Row r = dciterPromoProduk.getCurrentRow();
                    r.setAttribute("DiscMf", mf);
                    r.setAttribute("DiscRasioMf", rasMf);
                    r.setAttribute("DiscRasioTotal1", Total);
                    // dciterPromoProduk.getDataControl().commitTransaction();
                }
    
                AdfFacesContext.getCurrentInstance().addPartialTarget(otMF);
                AdfFacesContext.getCurrentInstance().addPartialTarget(otRasioMf);
                AdfFacesContext.getCurrentInstance().addPartialTarget(otRasioTotal);
            }
            //        AdfFacesContext.getCurrentInstance().addPartialTarget(tblListProductBonus);
         } else {
            JSFUtils.addFacesWarningMessage("Nilai \"Qty. Target\", \"Harga\"  dan nilai \"Potong Budget / MF\" tidak boleh 0 (nol) atau kosong.");
         } 
    }

    public void setTableListPotongan(RichTable tableListPotongan) {
        this.tableListPotongan = tableListPotongan;
    }

    public RichTable getTableListPotongan() {
        return tableListPotongan;
    }

    public void approvalHistPopupFetchListener(PopupFetchEvent popupFetchEvent) {
        DCIteratorBinding dciter =
            ADFUtils.findIterator("DocApprovalView1Iterator");
        dciter.executeQuery();
    }

    public void setSocTypePotongan(RichInputListOfValues socTypePotongan) {
        this.socTypePotongan = socTypePotongan;
    }

    public RichInputListOfValues getSocTypePotongan() {
        return socTypePotongan;
    }

    public void setOtOnTop(RichInputText otOnTop) {
        this.otOnTop = otOnTop;
    }

    public RichInputText getOtOnTop() {
        return otOnTop;
    }

    public void setOtMF(RichInputText otMF) {
        this.otMF = otMF;
    }

    public RichInputText getOtMF() {
        return otMF;
    }

    public void setRowOntop(RichInputText rowOntop) {
        this.rowOntop = rowOntop;
    }

    public RichInputText getRowOntop() {
        return rowOntop;
    }

    public void setRowMf(RichInputText rowMf) {
        this.rowMf = rowMf;
    }

    public RichInputText getRowMf() {
        return rowMf;
    }

    public void setValueMf(BigDecimal valueMf) {
        this.valueMf = valueMf;
    }

    public BigDecimal getValueMf() {
        return valueMf;
    }

    public void setOtRasioOntop(RichInputText otRasioOntop) {
        this.otRasioOntop = otRasioOntop;
    }

    public RichInputText getOtRasioOntop() {
        return otRasioOntop;
    }

    public void setOtRasioMf(RichInputText otRasioMf) {
        this.otRasioMf = otRasioMf;
    }

    public RichInputText getOtRasioMf() {
        return otRasioMf;
    }

    public void setOtRasioTotal(RichInputText otRasioTotal) {
        this.otRasioTotal = otRasioTotal;
    }

    public RichInputText getOtRasioTotal() {
        return otRasioTotal;
    }

    public void removeRowPotongan(ActionEvent actionEvent) {
        DCBindingContainer bindings =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcItteratorBindings =
            bindings.findIteratorBinding("DiscountView1Iterator");
        if (dcItteratorBindings.getEstimatedRowCount() == 1) {
            ViewObject voTableData = dcItteratorBindings.getViewObject();
            Row rowSelected = voTableData.getCurrentRow();
            if (rowSelected.getAttribute("DiscountId") != null) {
                voTableData.removeCurrentRow();
                OperationBinding operation =
                    (OperationBinding)BindingContext.getCurrent().getCurrentBindingsEntry().get("Commit");
                operation.execute();
                DCIteratorBinding dciterPromoProduk =
                    ADFUtils.findIterator("PromoProdukView1Iterator");
                Row r = dciterPromoProduk.getCurrentRow();
                r.setAttribute("DiscOnTop", 0);
                r.setAttribute("DiscRasioOnTop", 0);
                r.setAttribute("DiscMf", 0);
                r.setAttribute("DiscRasioMf", 0);
                r.setAttribute("DiscRasioTotal1", 0);
                dciterPromoProduk.getDataControl().commitTransaction();
                otOnTop.setSubmittedValue(0);
                otRasioOntop.setSubmittedValue(0);
                otRasioTotal.setSubmittedValue(0);
                otMF.setSubmittedValue(0);
                otRasioMf.setSubmittedValue(0);
                AdfFacesContext.getCurrentInstance().addPartialTarget(otOnTop);
                AdfFacesContext.getCurrentInstance().addPartialTarget(otRasioOntop);
                AdfFacesContext.getCurrentInstance().addPartialTarget(otRasioTotal);
                AdfFacesContext.getCurrentInstance().addPartialTarget(otMF);
                AdfFacesContext.getCurrentInstance().addPartialTarget(otRasioMf);
            }
        } else {
            ViewObject voTableData = dcItteratorBindings.getViewObject();
            Row rowSelected = voTableData.getCurrentRow();
            if (rowSelected.getAttribute("DiscountId") != null) {
                voTableData.removeCurrentRow();
                OperationBinding operation =
                    (OperationBinding)BindingContext.getCurrent().getCurrentBindingsEntry().get("Commit");
                operation.execute();
            }
            DCIteratorBinding dciterPromoProduk =
                ADFUtils.findIterator("PromoProdukView1Iterator");
            Row r = dciterPromoProduk.getCurrentRow();
            DCIteratorBinding dciterDiscount =
                ADFUtils.findIterator("DiscountView1Iterator");
            BigDecimal totalMf = BigDecimal.ZERO;
            BigDecimal totalOntop = BigDecimal.ZERO;
            for (Row er : dciterDiscount.getAllRowsInRange()) {
                String valueMF = null;
                try {
                    valueMF =
                            er.getAttribute("DiscYearly").toString().replaceAll(",",
                                                                                "");
                } catch (Exception e) {
                    valueMF = null;
                }

                String valueTop = null;
                try {
                    valueTop =
                            er.getAttribute("DiscNonYearly").toString().replaceAll(",",
                                                                                   "");
                } catch (Exception e) {
                    valueTop = null;
                }

                BigDecimal ontopValue =
                    valueTop == null ? new BigDecimal(0) : new BigDecimal(valueTop);
                BigDecimal mfValue =
                    valueMF == null ? new BigDecimal(0) : new BigDecimal(valueMF);
                totalMf = totalMf.add(mfValue);
                totalOntop = totalOntop.add(ontopValue);
            }
            DCIteratorBinding dciterTarget =
                ADFUtils.findIterator("TargetView1Iterator");
            Row row = dciterTarget.getCurrentRow();
            BigDecimal mf = BigDecimal.ZERO;
            BigDecimal rMf = BigDecimal.ZERO;
            BigDecimal RasioMf = BigDecimal.ZERO;
            BigDecimal rasioTotal = BigDecimal.ZERO;
            BigDecimal rasioT = BigDecimal.ZERO;
            BigDecimal ontop = BigDecimal.ZERO;
            BigDecimal rOntop = BigDecimal.ZERO;
            BigDecimal RasioOntop = BigDecimal.ZERO;

            Number qty =
                (Number)row.getAttribute("Qty") == null ? new Number(0) :
                (Number)row.getAttribute("Qty");
            Number value =
                (Number)row.getAttribute("Value") == null ? new Number(0) :
                (Number)row.getAttribute("Value");

            DCIteratorBinding dciterDiscountTgetTPot =
                ADFUtils.findIterator("DiscountView1Iterator");
            for (Row ere : dciterDiscountTgetTPot.getAllRowsInRange()) {
                String typePot =
                    ere.getAttribute("TipePotongan").toString().replaceAll(",",
                                                                           "");
                if (typePot.equalsIgnoreCase("PERCENT")) {
                    rMf = totalMf;
                    mf =
 value.multiply(rMf).getBigDecimalValue().divide(bdHundred).setScale(2,RoundingMode.HALF_UP);
                    otMF.setSubmittedValue(mf);
                    RasioMf =
                            mf.divide(value.getBigDecimalValue(),MathContext.DECIMAL128).multiply(bdHundred).setScale(2, RoundingMode.HALF_UP);
                    String rasMf = RasioMf.toString();
                    otRasioMf.setSubmittedValue(rasMf);
                    rasioT =
                            new BigDecimal(otRasioOntop.getValue().toString().replaceAll(" ",
                                                                                         "").replaceAll("%",
                                                                                                        ""));
                    rasioTotal = RasioMf.add(rasioT);
                    String Total = rasioTotal.toString();
                    otRasioTotal.setSubmittedValue(Total);

                    rOntop = totalOntop;
                    String rasio =
                        otRasioMf.getValue() == "" ? "0" : otRasioMf.getValue() ==
                                                           null ? "0" :
                                                           otRasioMf.getValue().toString();
                    rasioT =
                            new BigDecimal(rasio.toString().replaceAll(" ", "").replaceAll("%",
                                                                                           ""));
                    ontop =
                            value.multiply(rOntop).getBigDecimalValue().divide(bdHundred).setScale(2,RoundingMode.HALF_UP);
                    otOnTop.setSubmittedValue(ontop);
                    RasioOntop =
                            ontop.divide(value.getBigDecimalValue(),MathContext.DECIMAL128).multiply(bdHundred).setScale(2, RoundingMode.HALF_UP);
                    String rasOntop = RasioOntop.toString();
                    otRasioOntop.setSubmittedValue(rasOntop);

                    r.setAttribute("DiscOnTop", ontop);
                    r.setAttribute("DiscRasioOnTop", rasOntop);
                    r.setAttribute("DiscRasioTotal1", Total);
                    r.setAttribute("DiscMf", mf);
                    r.setAttribute("DiscRasioMf", rasMf);

                    dciterPromoProduk.getDataControl().commitTransaction();

                    AdfFacesContext.getCurrentInstance().addPartialTarget(otOnTop);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(otRasioOntop);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(otRasioTotal);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(otMF);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(otRasioMf);

                } else {
                    ontop = qty.getBigDecimalValue().multiply(totalOntop);
                    otOnTop.setSubmittedValue(ontop);
                    RasioOntop =
                            ontop.divide(value.getBigDecimalValue(),MathContext.DECIMAL128).multiply(bdHundred).setScale(2, RoundingMode.HALF_UP);
                    String rasOntop = RasioOntop.toString();

                    otRasioOntop.setSubmittedValue(rasOntop);
                    String rasio =
                        otRasioMf.getValue() == "" ? "0" : otRasioMf.getValue() ==
                                                           null ? "0" :
                                                           otRasioMf.getValue().toString();
                    rasioT =
                            new BigDecimal(rasio.toString().replaceAll(" ", "").replaceAll("%",
                                                                                           ""));
                    rasioTotal = RasioOntop.add(rasioT);
                    String total = rasioTotal.toString();
                    otRasioTotal.setSubmittedValue(total);

                    mf = qty.getBigDecimalValue().multiply(totalMf);
                    otMF.setSubmittedValue(mf);
                    RasioMf =
                            mf.divide(value.getBigDecimalValue(),MathContext.DECIMAL128).multiply(bdHundred).setScale(2, RoundingMode.HALF_UP);
                    String rasMf = RasioMf.toString();
                    otRasioMf.setSubmittedValue(rasMf);

                    r.setAttribute("DiscOnTop", ontop);
                    r.setAttribute("DiscRasioOnTop", rasOntop);
                    r.setAttribute("DiscRasioTotal1", total);
                    r.setAttribute("DiscMf", mf);
                    r.setAttribute("DiscRasioMf", rasMf);
                    dciterPromoProduk.getDataControl().commitTransaction();

                    AdfFacesContext.getCurrentInstance().addPartialTarget(otMF);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(otRasioMf);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(otOnTop);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(otRasioOntop);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(otRasioTotal);
                }
            }
        }
    }

    public void tabExcludeEvent(DisclosureEvent disclosureEvent) {
        if (disclosureEvent.isExpanded()) {
            BindingContainer bindings = getBindings();
            AttributeBinding userTypeAttr =
                (AttributeBinding)bindings.getControlBinding("UserTypeCreator");
            String userTypeCreator = (String)userTypeAttr.getInputValue();
            if (userTypeCreator.equalsIgnoreCase(userHo)) {
                DCIteratorBinding dciterCustHo =
                    ADFUtils.findIterator("PromoCustomerHoView1Iterator");
                
                Integer cekCustHo = (int)dciterCustHo.getEstimatedRowCount();
                
                if (cekCustHo < 1) {
                    showPopup("Tab customer harus diisi", potmessage);
                    tabTargerCustomer.setDisclosed(true);
                }
            }
        }
    }

    public void clearAllCustExclude() {

        BindingContainer bindings = this.getBindings();
        AttributeBinding usrTypeCreatorAttr =
            (AttributeBinding)bindings.getControlBinding("UserTypeCreator");
        String usrTypeCreator = (String)usrTypeCreatorAttr.getInputValue();

        if (usrTypeCreator.equalsIgnoreCase(userHo)) {
            // Remove All Exclude Regions
            DCIteratorBinding dciterRegion =
                ADFUtils.findIterator("ExclCustRegionView1Iterator");
            RowSetIterator rsiRegion = dciterRegion.getRowSetIterator();
            for (Row regionRow : dciterRegion.getAllRowsInRange()) {
                regionRow.remove();
            }
            rsiRegion.closeRowSetIterator();
            
            // Remove All Exclude Areas
            DCIteratorBinding dciterArea =
                ADFUtils.findIterator("ExclCustAreaView1Iterator");
            RowSetIterator rsiArea = dciterArea.getRowSetIterator();
            for (Row areaRow : dciterArea.getAllRowsInRange()) {
                areaRow.remove();
            }
            rsiArea.closeRowSetIterator();
            
            // Remove All Exclude Locations
            DCIteratorBinding dciterLocation =
                ADFUtils.findIterator("ExclCustLocView1Iterator");
            RowSetIterator rsiLocation = dciterLocation.getRowSetIterator();
            for (Row locationRow : dciterLocation.getAllRowsInRange()) {
                locationRow.remove();
            }
            rsiLocation.closeRowSetIterator();
            
            // Remove All Exclude Cust Types
            DCIteratorBinding dciterCustType =
                ADFUtils.findIterator("ExclCustTypeView1Iterator");
            RowSetIterator rsiCustType = dciterCustType.getRowSetIterator();
            for (Row custTypeRow : dciterCustType.getAllRowsInRange()) {
                custTypeRow.remove();
            }
            rsiCustType.closeRowSetIterator();
            
            // Remove All Exclude Cust Groups
            DCIteratorBinding dciterCustGroup =
                ADFUtils.findIterator("ExclCustGroupView1Iterator");
            RowSetIterator rsiCustGroup = dciterCustGroup.getRowSetIterator();
            for (Row custGroupRow : dciterCustGroup.getAllRowsInRange()) {
                custGroupRow.remove();
            }
            rsiCustGroup.closeRowSetIterator();
            
            // Remove All Exclude Customers
            DCIteratorBinding dciterCustomer =
                ADFUtils.findIterator("ExclCustCustView1Iterator");
            RowSetIterator rsiCustomer = dciterCustomer.getRowSetIterator();
            for (Row customerRow : dciterCustomer.getAllRowsInRange()) {
                customerRow.remove();
            }
            rsiCustomer.closeRowSetIterator();

        } else if (usrTypeCreator.equalsIgnoreCase(userArea)) {

            // Remove All Exclude Regions
            DCIteratorBinding dciterRegion =
                ADFUtils.findIterator("ExclPropCustRegionView1Iterator");
            RowSetIterator rsiRegion = dciterRegion.getRowSetIterator();
            for (Row regionRow : dciterRegion.getAllRowsInRange()) {
                regionRow.remove();
            }
            rsiRegion.closeRowSetIterator();
            
            // Remove All Exclude Areas
            DCIteratorBinding dciterArea =
                ADFUtils.findIterator("ExclPropCustAreaView1Iterator");
            RowSetIterator rsiArea = dciterArea.getRowSetIterator();
            for (Row areaRow : dciterArea.getAllRowsInRange()) {
                areaRow.remove();
            }
            rsiArea.closeRowSetIterator();
            
            // Remove All Exclude Locations
            DCIteratorBinding dciterLocation =
                ADFUtils.findIterator("ExclPropCustLocView1Iterator");
            RowSetIterator rsiLocation = dciterLocation.getRowSetIterator();
            for (Row locationRow : dciterLocation.getAllRowsInRange()) {
                locationRow.remove();
            }
            rsiLocation.closeRowSetIterator();
            
            // Remove All Exclude Cust Types
            DCIteratorBinding dciterCustType =
                ADFUtils.findIterator("ExclPropCustTypeView1Iterator");
            RowSetIterator rsiCustType = dciterCustType.getRowSetIterator();
            for (Row custTypeRow : dciterCustType.getAllRowsInRange()) {
                custTypeRow.remove();
            }
            rsiCustType.closeRowSetIterator();
            
            // Remove All Exclude Cust Groups
            DCIteratorBinding dciterCustGroup =
                ADFUtils.findIterator("ExclPropCustGroupView1Iterator");
            RowSetIterator rsiCustGroup = dciterCustGroup.getRowSetIterator();
            for (Row custGroupRow : dciterCustGroup.getAllRowsInRange()) {
                custGroupRow.remove();
            }
            rsiCustGroup.closeRowSetIterator();
            
            // Remove All Exclude Customers
            DCIteratorBinding dciterCustomer =
                ADFUtils.findIterator("ExclPropCustCustView1Iterator");
            RowSetIterator rsiCustomer = dciterCustomer.getRowSetIterator();
            for (Row customerRow : dciterCustomer.getAllRowsInRange()) {
                customerRow.remove();
            }
            rsiCustomer.closeRowSetIterator();

        } else {
            JSFUtils.addFacesWarningMessage("\"User Type Creator\" pada proposal tidak dikenali. Event: clearing exclude.");
        }

        // Clear column "ExclCustBy"
        DCIteratorBinding dciterPromoProduk =
            ADFUtils.findIterator("PromoProdukView1Iterator");
        if (dciterPromoProduk.getEstimatedRowCount() > 0) {
            for (Row r : dciterPromoProduk.getAllRowsInRange()) {
                r.setAttribute("ExclCustBy", null);
            }
        }

        AdfFacesContext.getCurrentInstance().addPartialTarget(tblListProduct);
    }

    public void setItPromoBonusOntop(RichInputText itPromoBonusOntop) {
        this.itPromoBonusOntop = itPromoBonusOntop;
    }

    public RichInputText getItPromoBonusOntop() {
        return itPromoBonusOntop;
    }

    public void setItPromoBonusMf(RichInputText itPromoBonusMf) {
        this.itPromoBonusMf = itPromoBonusMf;
    }

    public RichInputText getItPromoBonusMf() {
        return itPromoBonusMf;
    }

    public void setOtBrgOnTop(RichInputText otBrgOnTop) {
        this.otBrgOnTop = otBrgOnTop;
    }

    public RichInputText getOtBrgOnTop() {
        return otBrgOnTop;
    }

    public void setOtBrgMf(RichInputText otBrgMf) {
        this.otBrgMf = otBrgMf;
    }

    public RichInputText getOtBrgMf() {
        return otBrgMf;
    }

    public void setOtBrgRasioOnTop(RichInputText otBrgRasioOnTop) {
        this.otBrgRasioOnTop = otBrgRasioOnTop;
    }

    public RichInputText getOtBrgRasioOnTop() {
        return otBrgRasioOnTop;
    }

    public void setOtBrgRasioMf(RichInputText otBrgRasioMf) {
        this.otBrgRasioMf = otBrgRasioMf;
    }

    public RichInputText getOtBrgRasioMf() {
        return otBrgRasioMf;
    }

    public void setOtBrgRasioTotal(RichInputText otBrgRasioTotal) {
        this.otBrgRasioTotal = otBrgRasioTotal;
    }

    public RichInputText getOtBrgRasioTotal() {
        return otBrgRasioTotal;
    }

    public void refreshOntopProBar(ValueChangeEvent valueChangeEvent) {
        String PromoBonusIdSel = "";
        Number Mf = new Number(0);
        Number PriceVal = new Number(0);
        Number QtyFrom = new Number(0);
        Number QtyMulPrice = new Number(0);

        BigDecimal RasioOntop = BigDecimal.ZERO;
        BigDecimal rasioTotal = BigDecimal.ZERO;
        BigDecimal rasioT = BigDecimal.ZERO;

        String InputPriceBy = "";
        DCBindingContainer bindingsSelRow =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcItteratorBindings =
            bindingsSelRow.findIteratorBinding("PromoBonusView1Iterator");
        Key parentKeyMaster = dcItteratorBindings.getCurrentRow().getKey();
        ViewObject voTableData = dcItteratorBindings.getViewObject();
        Row rowSelected = voTableData.getCurrentRow();
        if (rowSelected.getAttribute("PromoBonusId") != null) {
            PromoBonusIdSel =
                    rowSelected.getAttribute("PromoBonusId").toString();
            PriceVal =
                    (Number)rowSelected.getAttribute("PriceVal") == null ? new Number(0) :
                    (Number)rowSelected.getAttribute("PriceVal");
            Mf =
    (Number)rowSelected.getAttribute("DiscYearly") == null ? new Number(0) :
    (Number)rowSelected.getAttribute("DiscYearly");
            InputPriceBy =
                    rowSelected.getAttribute("InputPriceBy") == null ? "" :
                    rowSelected.getAttribute("InputPriceBy").toString();
            QtyFrom =
                    (Number)rowSelected.getAttribute("QtyFrom") == null ? new Number(0) :
                    (Number)rowSelected.getAttribute("QtyFrom");
            QtyMulPrice =
                    (Number)rowSelected.getAttribute("QtyMulPrice") == null ?
                    new Number(0) :
                    (Number)rowSelected.getAttribute("QtyMulPrice");

            //            System.out.println("value "+QtyMulPrice);
        }
        if (InputPriceBy.equalsIgnoreCase("PRICELIST")) {
            if (PriceVal.compareTo(zeroNum) < 0 ||
                PriceVal.compareTo(zeroNum) > 0) {
                BigDecimal newChangedOnTopValueCek =
                    new BigDecimal(valueChangeEvent.getNewValue() == "" ? "0" :
                                   valueChangeEvent.getNewValue() == null ?
                                   "0" :
                                   valueChangeEvent.getNewValue().toString());
                BigDecimal totalOntopcek =
                    newChangedOnTopValueCek.add(Mf.getBigDecimalValue());
                BigDecimal totalPriceQty = PriceVal.getBigDecimalValue().multiply(QtyFrom.getBigDecimalValue());
                //                System.out.println("totalPriceQty "+totalPriceQty);
                BigDecimal val =
                    totalOntopcek.divide(totalPriceQty,MathContext.DECIMAL128).setScale(2, RoundingMode.HALF_UP);
                if (val.compareTo(BigDecimal.ONE) == 1) {
                    showPopup("Persentase nilai \"Total Qty Bonus\" dikali \"Price\" melebihi 100% dari nilai budget.",
                              potmessage);
                    btnOkpromoDetail.setDisabled(true);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(tblListProductBonus);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(btnOkpromoDetail);
                } else {
                    DCIteratorBinding dciterTarget =
                        ADFUtils.findIterator("TargetView1Iterator");
                    Row row = dciterTarget.getCurrentRow();
                    Number value =
                        (Number)row.getAttribute("Value") == null ? new Number(0) :
                        (Number)row.getAttribute("Value");

                    DCIteratorBinding dcItteratorBrgBonusOT =
                        ADFUtils.findIterator("PromoBonusView1Iterator");
                    Key parentKey =
                        dcItteratorBrgBonusOT.getCurrentRow().getKey();
                    ViewObject voTableData1 =
                        dcItteratorBrgBonusOT.getViewObject();
                    RowSetIterator iter =
                        voTableData1.createRowSetIterator(null);
                    BigDecimal totalOntop = new BigDecimal(0);
                    BigDecimal totalMFSum = new BigDecimal(0);
                    BigDecimal valueOTsel = BigDecimal.ZERO;
                    while (iter.hasNext()) {
                        Row r = iter.next();
                        String PromoBonusIdBrg =
                            r.getAttribute("PromoBonusId").toString();
                        if (PromoBonusIdBrg.equalsIgnoreCase(PromoBonusIdSel)) {
                            BigDecimal newChangedOnTopValue =
                                new BigDecimal(valueChangeEvent.getNewValue() ==
                                               "" ? "0" :
                                               valueChangeEvent.getNewValue() ==
                                               null ? "0" :
                                               valueChangeEvent.getNewValue().toString());
                            valueOTsel = valueOTsel.add(newChangedOnTopValue);
                            valueChangeEvent.getComponent().processUpdates(FacesContext.getCurrentInstance());
                        }
                        Number MFSum =
                            (Number)r.getAttribute("DiscYearly") == null ?
                            new Number(0) :
                            (Number)r.getAttribute("DiscYearly");
                        totalMFSum = totalMFSum.add(MFSum.getBigDecimalValue());
                        Number ontop =
                            (Number)r.getAttribute("DiscNonYearly") == null ?
                            new Number(0) :
                            (Number)r.getAttribute("DiscNonYearly");
                        totalOntop =
                                totalOntop.add(ontop.getBigDecimalValue());
                    }
                    iter.closeRowSetIterator();
                    String rasio =
                        otBrgRasioMf.getValue() == "" ? "0" : otBrgRasioMf.getValue() ==
                                                              null ? "0" :
                                                              otBrgRasioMf.getValue().toString();
                    rasioT =
                            new BigDecimal(rasio.toString().replaceAll(" ", "").replaceAll("%",
                                                                                           ""));
                    otBrgOnTop.setSubmittedValue(totalOntop);
                    otBrgMf.setSubmittedValue(totalMFSum);
                    if (value.getBigDecimalValue().compareTo(BigDecimal.ZERO) ==
                        0) {
                        showPopup("Total Value Tab Target Bernilai 0",
                                  potmessage);
                    } else {
                        RasioOntop =
                                totalOntop.divide(value.getBigDecimalValue(),
                                                  MathContext.DECIMAL128).multiply(bdHundred).setScale(2,
                                                                                                       RoundingMode.HALF_UP);

                    }
                    BigDecimal rasOntop = RasioOntop;
                    dcItteratorBrgBonusOT.setCurrentRowWithKey(parentKey.toStringFormat(true));
                    otBrgRasioOnTop.setSubmittedValue(rasOntop);
                    rasioTotal = RasioOntop.add(rasioT);
                    String total = rasioTotal.toString();
                    otBrgRasioTotal.setSubmittedValue(total);

                    DCIteratorBinding dciterPromoProduk =
                        ADFUtils.findIterator("PromoProdukView1Iterator");
                    Key parentKeyMasterPromoProduk =
                        dciterPromoProduk.getCurrentRow().getKey();
                    Row r = dciterPromoProduk.getCurrentRow();
                    r.setAttribute("BrgBonusOnTop", totalOntop);
                    r.setAttribute("BrgBonusRasioOnTop", rasOntop);
                    r.setAttribute("BrgBonusRasioTotal", total);

                    dciterPromoProduk.getDataControl().commitTransaction();
                    dciterPromoProduk.executeQuery();
                    dciterPromoProduk.setCurrentRowWithKey(parentKeyMasterPromoProduk.toStringFormat(true));
                    AdfFacesContext.getCurrentInstance().addPartialTarget(otBrgOnTop);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(otBrgRasioOnTop);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(otBrgRasioTotal);
                    btnOkpromoDetail.setDisabled(false);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(btnOkpromoDetail);
                }
            } else {
                JSFUtils.addFacesWarningMessage("Kolom \"Price\" tidak boleh kosong atau diisi 0 (Nol)");
            }
        } else {
            if (PriceVal.compareTo(zeroNum) < 0 ||
                PriceVal.compareTo(zeroNum) > 0) {
                BigDecimal newChangedOnTopValueCek =
                    new BigDecimal(valueChangeEvent.getNewValue() == "" ? "0" :
                                   valueChangeEvent.getNewValue() == null ?
                                   "0" :
                                   valueChangeEvent.getNewValue().toString());
                BigDecimal totalOntopcek =
                    newChangedOnTopValueCek.add(Mf.getBigDecimalValue());
                BigDecimal totalPriceQty =
                    PriceVal.getBigDecimalValue().multiply(QtyFrom.getBigDecimalValue());
                BigDecimal val =
                    totalOntopcek.divide(totalPriceQty, MathContext.DECIMAL128).setScale(2,
                                                                                         RoundingMode.HALF_UP);
                if (val.compareTo(BigDecimal.ONE) == 1) {
                    showPopup("Persentase nilai \"Total Qty Bonus\" dikali \"Price\" melebihi 100% dari nilai budget.",
                              potmessage);

                    btnOkpromoDetail.setDisabled(true);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(tblListProductBonus);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(btnOkpromoDetail);
                } else {
                    DCIteratorBinding dciterTarget =
                        ADFUtils.findIterator("TargetView1Iterator");
                    Row row = dciterTarget.getCurrentRow();
                    Number value =
                        (Number)row.getAttribute("Value") == null ? new Number(0) :
                        (Number)row.getAttribute("Value");

                    DCIteratorBinding dcItteratorBrgBonusOT =
                        ADFUtils.findIterator("PromoBonusView1Iterator");
                    Key parentKey =
                        dcItteratorBrgBonusOT.getCurrentRow().getKey();
                    ViewObject voTableData1 =
                        dcItteratorBrgBonusOT.getViewObject();
                    RowSetIterator iter =
                        voTableData1.createRowSetIterator(null);
                    BigDecimal totalOntop = new BigDecimal(0);
                    BigDecimal totalMFSum = new BigDecimal(0);
                    BigDecimal valueOTsel = BigDecimal.ZERO;
                    while (iter.hasNext()) {
                        Row r = iter.next();
                        String PromoBonusIdBrg =
                            r.getAttribute("PromoBonusId").toString();
                        if (PromoBonusIdBrg.equalsIgnoreCase(PromoBonusIdSel)) {
                            BigDecimal newChangedOnTopValue =
                                new BigDecimal(valueChangeEvent.getNewValue() ==
                                               "" ? "0" :
                                               valueChangeEvent.getNewValue() ==
                                               null ? "0" :
                                               valueChangeEvent.getNewValue().toString());
                            valueOTsel = valueOTsel.add(newChangedOnTopValue);
                            valueChangeEvent.getComponent().processUpdates(FacesContext.getCurrentInstance());
                        }
                        Number MFSum =
                            (Number)r.getAttribute("DiscYearly") == null ?
                            new Number(0) :
                            (Number)r.getAttribute("DiscYearly");
                        totalMFSum = totalMFSum.add(MFSum.getBigDecimalValue());
                        Number ontop =
                            (Number)r.getAttribute("DiscNonYearly") == null ?
                            new Number(0) :
                            (Number)r.getAttribute("DiscNonYearly");
                        totalOntop =
                                totalOntop.add(ontop.getBigDecimalValue());
                    }

                    //                    System.out.println("totalOntop "+totalOntop);
                    iter.closeRowSetIterator();
                    String rasio =
                        otBrgRasioMf.getValue() == "" ? "0" : otBrgRasioMf.getValue() ==
                                                              null ? "0" :
                                                              otBrgRasioMf.getValue().toString();
                    rasioT =
                            new BigDecimal(rasio.toString().replaceAll(" ", "").replaceAll("%",
                                                                                           ""));
                    otBrgOnTop.setSubmittedValue(totalOntop);
                    otBrgMf.setSubmittedValue(totalMFSum);
                    if (value.getBigDecimalValue().compareTo(BigDecimal.ZERO) ==
                        0) {
                        showPopup("Total Value Tab Target Bernilai 0",
                                  potmessage);

                    } else {
                        RasioOntop =
                                totalOntop.divide(value.getBigDecimalValue(),
                                                  MathContext.DECIMAL128).multiply(bdHundred).setScale(2,
                                                                                                       RoundingMode.HALF_UP);
                    }
                    BigDecimal rasOntop = RasioOntop;
                    dcItteratorBrgBonusOT.setCurrentRowWithKey(parentKey.toStringFormat(true));
                    otBrgRasioOnTop.setSubmittedValue(rasOntop);
                    rasioTotal = RasioOntop.add(rasioT);
                    String total = rasioTotal.toString();
                    otBrgRasioTotal.setSubmittedValue(total);

                    DCIteratorBinding dciterPromoProduk =
                        ADFUtils.findIterator("PromoProdukView1Iterator");
                    Key parentKeyMasterPromoProduk =
                        dciterPromoProduk.getCurrentRow().getKey();
                    Row r = dciterPromoProduk.getCurrentRow();
                    r.setAttribute("BrgBonusOnTop", totalOntop);
                    r.setAttribute("BrgBonusRasioOnTop", rasOntop);
                    r.setAttribute("BrgBonusRasioTotal", total);

                    dciterPromoProduk.getDataControl().commitTransaction();
                    dciterPromoProduk.executeQuery();
                    dciterPromoProduk.setCurrentRowWithKey(parentKeyMasterPromoProduk.toStringFormat(true));
                    AdfFacesContext.getCurrentInstance().addPartialTarget(otBrgOnTop);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(otBrgMf);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(otBrgRasioOnTop);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(otBrgRasioTotal);
                    btnOkpromoDetail.setDisabled(false);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(btnOkpromoDetail);
                }
            } else {
                JSFUtils.addFacesWarningMessage("Kolom \"Price\" tidak boleh kosong atau diisi 0 (Nol)");
            }
            dcItteratorBindings.setCurrentRowWithKey(parentKeyMaster.toStringFormat(true));
        }
    }

    public void refreshMfProBar(ValueChangeEvent valueChangeEvent) {
        String PromoBonusIdSel = "";
        String InputPriceBy = "";
        Number QtyFrom = new Number(0);
        Number ontop = new Number(0);
        Number PriceVal = new Number(0);
        Number QtyMulPrice = new Number(0);
        BigDecimal RasioMf = BigDecimal.ZERO;
        BigDecimal rasioTotal = BigDecimal.ZERO;
        BigDecimal rasioT = BigDecimal.ZERO;

        DCBindingContainer bindingsSelRow =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcItteratorBindings =
            bindingsSelRow.findIteratorBinding("PromoBonusView1Iterator");
        Key parentKeyMaster = dcItteratorBindings.getCurrentRow().getKey();

        ViewObject voTableData = dcItteratorBindings.getViewObject();
        Row rowSelected = voTableData.getCurrentRow();
        if (rowSelected.getAttribute("PromoBonusId") != null) {
            PromoBonusIdSel =
                    rowSelected.getAttribute("PromoBonusId").toString();
            PriceVal =
                    (Number)rowSelected.getAttribute("PriceVal") == null ? new Number(0) :
                    (Number)rowSelected.getAttribute("PriceVal");
            ontop =
                    (Number)rowSelected.getAttribute("DiscNonYearly") == null ? new Number(0) :
                    (Number)rowSelected.getAttribute("DiscNonYearly");
            InputPriceBy =
                    rowSelected.getAttribute("InputPriceBy") == null ? "" :
                    rowSelected.getAttribute("InputPriceBy").toString();
            QtyFrom =
                    (Number)rowSelected.getAttribute("QtyFrom") == null ? new Number(0) :
                    (Number)rowSelected.getAttribute("QtyFrom");
            QtyMulPrice =
                    (Number)rowSelected.getAttribute("QtyMulPrice") == null ?
                    new Number(0) :
                    (Number)rowSelected.getAttribute("QtyMulPrice");

            //            System.out.println("value mf "+QtyMulPrice);
        }

        if (InputPriceBy.equalsIgnoreCase("PRICELIST")) {
            if (PriceVal.compareTo(zeroNum) < 0 ||
                PriceVal.compareTo(zeroNum) > 0) {
                BigDecimal newChangedMfValueCek =
                    new BigDecimal(valueChangeEvent.getNewValue() == "" ? "0" :
                                   valueChangeEvent.getNewValue() == null ?
                                   "0" :
                                   valueChangeEvent.getNewValue().toString());
                BigDecimal totalOntop =
                    newChangedMfValueCek.add(ontop.getBigDecimalValue());
                BigDecimal totalPriceQty =
                    PriceVal.getBigDecimalValue().multiply(QtyFrom.getBigDecimalValue());
                BigDecimal val =
                    totalOntop.divide(totalPriceQty, MathContext.DECIMAL128).setScale(2,
                                                                                      RoundingMode.HALF_UP);
                if (val.compareTo(BigDecimal.ONE) == 1) {
                    showPopup("Persentase nilai \"Total Qty Bonus\" dikali \"Price\" melebihi 100% dari nilai budget.",
                              potmessage);
                    btnOkpromoDetail.setDisabled(true);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(tblListProductBonus);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(btnOkpromoDetail);
                } else {
                    DCIteratorBinding dciterTarget =
                        ADFUtils.findIterator("TargetView1Iterator");
                    Row row = dciterTarget.getCurrentRow();
                    Number value =
                        (Number)row.getAttribute("Value") == null ? new Number(0) :
                        (Number)row.getAttribute("Value");

                    DCIteratorBinding dcItteratorBrgBonusMF =
                        ADFUtils.findIterator("PromoBonusView1Iterator");
                    Key parentKey =
                        dcItteratorBrgBonusMF.getCurrentRow().getKey();
                    ViewObject voTableDataMF =
                        dcItteratorBrgBonusMF.getViewObject();
                    RowSetIterator iterMF =
                        voTableDataMF.createRowSetIterator(null);
                    BigDecimal valueOTsel = BigDecimal.ZERO;
                    BigDecimal totalOntopSum = new BigDecimal(0);
                    BigDecimal totalMF = new BigDecimal(0);
                    while (iterMF.hasNext()) {
                        Row r = iterMF.next();
                        String PromoBonusIdBrg =
                            r.getAttribute("PromoBonusId").toString();
                        if (PromoBonusIdBrg.equalsIgnoreCase(PromoBonusIdSel)) {
                            BigDecimal newChangedMfValue =
                                new BigDecimal(valueChangeEvent.getNewValue() ==
                                               "" ? "0" :
                                               valueChangeEvent.getNewValue() ==
                                               null ? "0" :
                                               valueChangeEvent.getNewValue().toString());
                            valueOTsel = valueOTsel.add(newChangedMfValue);
                            valueChangeEvent.getComponent().processUpdates(FacesContext.getCurrentInstance());
                        }
                        Number MF =
                            (Number)r.getAttribute("DiscYearly") == null ?
                            new Number(0) :
                            (Number)r.getAttribute("DiscYearly");
                        totalMF = totalMF.add(MF.getBigDecimalValue());
                        Number ontopSum =
                            (Number)r.getAttribute("DiscNonYearly") == null ?
                            new Number(0) :
                            (Number)r.getAttribute("DiscNonYearly");
                        totalOntopSum =
                                totalOntopSum.add(ontopSum.getBigDecimalValue());
                    }

                    dcItteratorBrgBonusMF.setCurrentRowWithKey(parentKey.toStringFormat(true));
                    iterMF.closeRowSetIterator();
                    String rasio =
                        otBrgRasioOnTop.getValue() == "" ? "0" : otBrgRasioOnTop.getValue() ==
                                                                 null ? "0" :
                                                                 otBrgRasioOnTop.getValue().toString();
                    rasioT =
                            new BigDecimal(rasio.toString().replaceAll(" ", "").replaceAll("%",
                                                                                           ""));
                    otBrgMf.setSubmittedValue(totalMF);
                    otBrgOnTop.setSubmittedValue(totalOntopSum);
                    RasioMf =
                            totalMF.divide(value.getBigDecimalValue(), MathContext.DECIMAL128).multiply(bdHundred).setScale(2,
                                                                                                                            RoundingMode.HALF_UP);
                    BigDecimal rasMf = RasioMf;
                    otBrgRasioMf.setSubmittedValue(rasMf);
                    rasioTotal = RasioMf.add(rasioT);
                    String total = rasioTotal.toString();
                    otBrgRasioTotal.setSubmittedValue(total);

                    DCIteratorBinding dciterPromoProduk =
                        ADFUtils.findIterator("PromoProdukView1Iterator");
                    Key parentKeyMasterPromoProduk =
                        dciterPromoProduk.getCurrentRow().getKey();
                    Row r = dciterPromoProduk.getCurrentRow();

                    r.setAttribute("BrgBonusMf", totalMF);
                    r.setAttribute("BrgBonusRasioMf", rasMf);
                    r.setAttribute("BrgBonusRasioTotal", total);

                    dciterPromoProduk.getDataControl().commitTransaction();
                    dciterPromoProduk.executeQuery();
                    dciterPromoProduk.setCurrentRowWithKey(parentKeyMasterPromoProduk.toStringFormat(true));
                    AdfFacesContext.getCurrentInstance().addPartialTarget(otBrgMf);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(otBrgOnTop);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(otBrgRasioMf);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(otBrgRasioTotal);
                    btnOkpromoDetail.setDisabled(false);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(btnOkpromoDetail);
                }
            } else {
                JSFUtils.addFacesWarningMessage("Kolom \"Price\" tidak boleh kosong atau diisi 0 (Nol)");
            }
        } else {
            if (PriceVal.compareTo(zeroNum) < 0 ||
                PriceVal.compareTo(zeroNum) > 0) {
                BigDecimal newChangedMfValueCek =
                    new BigDecimal(valueChangeEvent.getNewValue() == "" ? "0" :
                                   valueChangeEvent.getNewValue() == null ?
                                   "0" :
                                   valueChangeEvent.getNewValue().toString());
                BigDecimal totalOntop =
                    newChangedMfValueCek.add(ontop.getBigDecimalValue());
                BigDecimal totalPriceQty =
                    PriceVal.getBigDecimalValue().multiply(QtyFrom.getBigDecimalValue());
                BigDecimal val =
                    totalOntop.divide(totalPriceQty, MathContext.DECIMAL128).setScale(2,
                                                                                      RoundingMode.HALF_UP);
                if (val.compareTo(BigDecimal.ONE) == 1) {
                    showPopup("Persentase nilai \"Total Qty Bonus\" dikali \"Price\" melebihi 100% dari nilai budget.",
                              potmessage);
                    btnOkpromoDetail.setDisabled(true);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(tblListProductBonus);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(btnOkpromoDetail);
                } else {
                    DCIteratorBinding dciterTarget =
                        ADFUtils.findIterator("TargetView1Iterator");
                    Row row = dciterTarget.getCurrentRow();
                    Number value =
                        (Number)row.getAttribute("Value") == null ? new Number(0) :
                        (Number)row.getAttribute("Value");

                    DCIteratorBinding dcItteratorBrgBonusMF =
                        ADFUtils.findIterator("PromoBonusView1Iterator");
                    Key parentKey =
                        dcItteratorBrgBonusMF.getCurrentRow().getKey();
                    ViewObject voTableDataMF =
                        dcItteratorBrgBonusMF.getViewObject();
                    RowSetIterator iterMF =
                        voTableDataMF.createRowSetIterator(null);
                    BigDecimal valueOTsel = BigDecimal.ZERO;
                    BigDecimal totalMF = new BigDecimal(0);
                    BigDecimal totalOntopSum = new BigDecimal(0);
                    while (iterMF.hasNext()) {
                        Row r = iterMF.next();
                        String PromoBonusIdBrg =
                            r.getAttribute("PromoBonusId").toString();
                        if (PromoBonusIdBrg.equalsIgnoreCase(PromoBonusIdSel)) {
                            BigDecimal newChangedMfValue =
                                new BigDecimal(valueChangeEvent.getNewValue() ==
                                               "" ? "0" :
                                               valueChangeEvent.getNewValue() ==
                                               null ? "0" :
                                               valueChangeEvent.getNewValue().toString());
                            valueOTsel = valueOTsel.add(newChangedMfValue);
                            valueChangeEvent.getComponent().processUpdates(FacesContext.getCurrentInstance());
                        }
                        Number MF =
                            (Number)r.getAttribute("DiscYearly") == null ?
                            new Number(0) :
                            (Number)r.getAttribute("DiscYearly");
                        totalMF = totalMF.add(MF.getBigDecimalValue());
                        Number ontopSum =
                            (Number)r.getAttribute("DiscNonYearly") == null ?
                            new Number(0) :
                            (Number)r.getAttribute("DiscNonYearly");
                        totalOntopSum =
                                totalOntopSum.add(ontopSum.getBigDecimalValue());
                    }
                    dcItteratorBrgBonusMF.setCurrentRowWithKey(parentKey.toStringFormat(true));
                    iterMF.closeRowSetIterator();
                    String rasio =
                        otBrgRasioOnTop.getValue() == "" ? "0" : otBrgRasioOnTop.getValue() ==
                                                                 null ? "0" :
                                                                 otBrgRasioOnTop.getValue().toString();
                    rasioT =
                            new BigDecimal(rasio.toString().replaceAll(" ", "").replaceAll("%",
                                                                                           ""));
                    otBrgMf.setSubmittedValue(totalMF);
                    otBrgOnTop.setSubmittedValue(totalOntopSum);
                    RasioMf =
                            totalMF.divide(value.getBigDecimalValue(), MathContext.DECIMAL128).multiply(bdHundred).setScale(2,
                                                                                                                            RoundingMode.HALF_UP);
                    BigDecimal rasMf = RasioMf;
                    otBrgRasioMf.setSubmittedValue(rasMf);
                    rasioTotal = RasioMf.add(rasioT);
                    String total = rasioTotal.toString();
                    otBrgRasioTotal.setSubmittedValue(total);

                    DCIteratorBinding dciterPromoProduk =
                        ADFUtils.findIterator("PromoProdukView1Iterator");
                    Key parentKeyMasterPromoProduk =
                        dciterPromoProduk.getCurrentRow().getKey();
                    Row r = dciterPromoProduk.getCurrentRow();

                    r.setAttribute("BrgBonusMf", totalMF);
                    r.setAttribute("BrgBonusRasioMf", rasMf);
                    r.setAttribute("BrgBonusRasioTotal", total);

                    dciterPromoProduk.getDataControl().commitTransaction();
                    dciterPromoProduk.executeQuery();
                    dciterPromoProduk.setCurrentRowWithKey(parentKeyMasterPromoProduk.toStringFormat(true));
                    AdfFacesContext.getCurrentInstance().addPartialTarget(otBrgMf);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(otBrgOnTop);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(otBrgRasioMf);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(otBrgRasioTotal);
                    btnOkpromoDetail.setDisabled(false);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(btnOkpromoDetail);
                }
            } else {
                JSFUtils.addFacesWarningMessage("Kolom \"Price\" tidak boleh kosong atau diisi 0 (Nol)");
            }
            dcItteratorBindings.setCurrentRowWithKey(parentKeyMaster.toStringFormat(true));
        }
    }

    public void setItCategory(RichOutputText itCategory) {
        this.itCategory = itCategory;
    }

    public RichOutputText getItCategory() {
        return itCategory;
    }

    public void setItLovProdCategory(RichInputListOfValues itLovProdCategory) {
        this.itLovProdCategory = itLovProdCategory;
    }

    public RichInputListOfValues getItLovProdCategory() {
        return itLovProdCategory;
    }

    public void setItProductItem(RichInputText itProductItem) {
        this.itProductItem = itProductItem;
    }

    public RichInputText getItProductItem() {
        return itProductItem;
    }

    public void setLinkProduct(RichCommandImageLink linkProduct) {
        this.linkProduct = linkProduct;
    }

    public RichCommandImageLink getLinkProduct() {
        return linkProduct;
    }

    public void setLinkVariant(RichCommandImageLink linkVariant) {
        this.linkVariant = linkVariant;
    }

    public RichCommandImageLink getLinkVariant() {
        return linkVariant;
    }

    public void removeProduk(ActionEvent actionEvent) {
        String PromoProdukIdSel = "";
        DCBindingContainer bindingsSelRow =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcItteratorBindings =
            bindingsSelRow.findIteratorBinding("PromoProdukView1Iterator");
        ViewObject voTableDatasel = dcItteratorBindings.getViewObject();
        Row rowSelected = voTableDatasel.getCurrentRow();
        if (rowSelected.getAttribute("PromoProdukId") != null) {
            PromoProdukIdSel =
                    rowSelected.getAttribute("PromoProdukId").toString();

            DCIteratorBinding iterVar =
                ADFUtils.findIterator("ProdukVariantView1Iterator");
            for (Row r : iterVar.getAllRowsInRange()) {
                String idVar = r.getAttribute("PromoProdukId").toString();
                if (idVar.equalsIgnoreCase(PromoProdukIdSel)) {
                    r.remove();
                }
            }
            //iterVar.getDataControl().commitTransaction();

            DCIteratorBinding iterItem =
                ADFUtils.findIterator("ProdukItemView1Iterator");
            for (Row rItem : iterItem.getAllRowsInRange()) {
                String idItem = rItem.getAttribute("PromoProdukId").toString();
                if (idItem.equalsIgnoreCase(PromoProdukIdSel)) {
                    rItem.remove();
                }
            }
            //iterItem.getDataControl().commitTransaction();

            DCIteratorBinding iterPP =
                ADFUtils.findIterator("PromoProdukView1Iterator");
            for (Row rPP : iterPP.getAllRowsInRange()) {
                String idPP = rPP.getAttribute("PromoProdukId").toString();
                if (idPP.equalsIgnoreCase(PromoProdukIdSel)) {
                    rPP.remove();
                }
            }
            //iterPP.getDataControl().commitTransaction();
        }
    }

    public void setOtBiaOntop(RichInputText otBiaOntop) {
        this.otBiaOntop = otBiaOntop;
    }

    public RichInputText getOtBiaOntop() {
        return otBiaOntop;
    }

    public void setOtBiaMf(RichInputText otBiaMf) {
        this.otBiaMf = otBiaMf;
    }

    public RichInputText getOtBiaMf() {
        return otBiaMf;
    }

    public void setOtBiaRasioOntop(RichInputText otBiaRasioOntop) {
        this.otBiaRasioOntop = otBiaRasioOntop;
    }

    public RichInputText getOtBiaRasioOntop() {
        return otBiaRasioOntop;
    }

    public void setOtBiaRasioMf(RichInputText otBiaRasioMf) {
        this.otBiaRasioMf = otBiaRasioMf;
    }

    public RichInputText getOtBiaRasioMf() {
        return otBiaRasioMf;
    }

    public void setOtBiaRasioTotal(RichInputText otBiaRasioTotal) {
        this.otBiaRasioTotal = otBiaRasioTotal;
    }

    public RichInputText getOtBiaRasioTotal() {
        return otBiaRasioTotal;
    }

    public void setRowBiaOntop(RichInputText rowBiaOntop) {
        this.rowBiaOntop = rowBiaOntop;
    }

    public RichInputText getRowBiaOntop() {
        return rowBiaOntop;
    }

    public void setRowBiaMf(RichInputText rowBiaMf) {
        this.rowBiaMf = rowBiaMf;
    }

    public RichInputText getRowBiaMf() {
        return rowBiaMf;
    }

    public void addNewRowBiaya(ActionEvent actionEvent) {
        BindingContainer bindings =
            BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dciter =
            (DCIteratorBinding)bindings.get("BiayaView1Iterator");
        RowSetIterator rsi = dciter.getRowSetIterator();
        Row lastRow = rsi.last();
        int lastRowIndex = rsi.getRangeIndexOf(lastRow);
        Row newRow = rsi.createRow();
        newRow.setNewRowState(Row.STATUS_INITIALIZED);
        //add row to last index + 1 so it becomes last in the range set
        rsi.insertRowAtRangeIndex(lastRowIndex + 1, newRow);
        //make row the current row so it is displayed correctly
        rsi.setCurrentRow(newRow);
    }

    public void removeBiayaRow(ActionEvent actionEvent) {
        DCBindingContainer bindings =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcItteratorBindings =
            bindings.findIteratorBinding("BiayaView1Iterator");
        if (dcItteratorBindings.getEstimatedRowCount() == 1) {
            ViewObject voTableData = dcItteratorBindings.getViewObject();
            Row rowSelected = voTableData.getCurrentRow();
            if (rowSelected.getAttribute("BiayaId") != null) {
                voTableData.removeCurrentRow();
                OperationBinding operation =
                    (OperationBinding)BindingContext.getCurrent().getCurrentBindingsEntry().get("Commit");
                operation.execute();
                DCIteratorBinding dciterPromoProduk =
                    ADFUtils.findIterator("PromoProdukView1Iterator");
                Row r = dciterPromoProduk.getCurrentRow();
                r.setAttribute("BiaOntop", 0);
                r.setAttribute("BiaRasionOntop", 0);
                r.setAttribute("BiaMf", 0);
                r.setAttribute("BiaRasioMf", 0);
                r.setAttribute("BiaRasioTotal", 0);
                dciterPromoProduk.getDataControl().commitTransaction();
                otBiaOntop.setSubmittedValue(0);
                otBiaRasioOntop.setSubmittedValue(0);
                otBiaRasioTotal.setSubmittedValue(0);
                otBiaMf.setSubmittedValue(0);
                otBiaRasioMf.setSubmittedValue(0);
                AdfFacesContext.getCurrentInstance().addPartialTarget(otBiaOntop);
                AdfFacesContext.getCurrentInstance().addPartialTarget(otBiaRasioOntop);
                AdfFacesContext.getCurrentInstance().addPartialTarget(otBiaRasioTotal);
                AdfFacesContext.getCurrentInstance().addPartialTarget(otBiaMf);
                AdfFacesContext.getCurrentInstance().addPartialTarget(otBiaRasioMf);
            }
        } else {
            ViewObject voTableData = dcItteratorBindings.getViewObject();
            Row rowSelected = voTableData.getCurrentRow();
            if (rowSelected.getAttribute("BiayaId") != null) {
                voTableData.removeCurrentRow();
                OperationBinding operation =
                    (OperationBinding)BindingContext.getCurrent().getCurrentBindingsEntry().get("Commit");
                operation.execute();
            }
            DCIteratorBinding dciterPromoProduk =
                ADFUtils.findIterator("PromoProdukView1Iterator");
            Row r = dciterPromoProduk.getCurrentRow();
            DCIteratorBinding dciterDiscount =
                ADFUtils.findIterator("BiayaView1Iterator");
            BigDecimal totalMf = BigDecimal.ZERO;
            BigDecimal totalOntop = BigDecimal.ZERO;
            for (Row er : dciterDiscount.getAllRowsInRange()) {
                String valueMF =
                    er.getAttribute("BiayaYearly")== null ? "0" : er.getAttribute("BiayaYearly")== "" ? "0" : er.getAttribute("BiayaYearly").toString().replaceAll(",",
                                                                         "");
                String valueTop =
                    er.getAttribute("BiayaNonYearly")== null ? "0" : er.getAttribute("BiayaNonYearly")== "" ? "0" : er.getAttribute("BiayaNonYearly").toString().replaceAll(",",
                                                                            "");
                BigDecimal ontopValue =
                    new BigDecimal(valueTop) == null ? new BigDecimal(0) :
                    new BigDecimal(valueTop);
                BigDecimal mfValue =
                    new BigDecimal(valueMF) == null ? new BigDecimal(0) :
                    new BigDecimal(valueMF);
                totalMf = totalMf.add(mfValue);
                totalOntop = totalOntop.add(ontopValue);
            }
            DCIteratorBinding dciterTarget =
                ADFUtils.findIterator("TargetView1Iterator");
            Row row = dciterTarget.getCurrentRow();
            BigDecimal mf = BigDecimal.ZERO;
            BigDecimal rMf = BigDecimal.ZERO;
            BigDecimal RasioMf = BigDecimal.ZERO;
            BigDecimal rasioTotal = BigDecimal.ZERO;
            BigDecimal rasioT = BigDecimal.ZERO;
            BigDecimal ontop = BigDecimal.ZERO;
            BigDecimal rOntop = BigDecimal.ZERO;
            BigDecimal RasioOntop = BigDecimal.ZERO;
            Number value =
                (Number)row.getAttribute("Value") == null ? new Number(0) :
                (Number)row.getAttribute("Value");

            rMf = totalMf;
            mf = (value.multiply(rMf)).getBigDecimalValue().divide(bdHundred).setScale(2,RoundingMode.HALF_UP);
            otBiaMf.setSubmittedValue(rMf);
            RasioMf =
                    rMf.divide(value.getBigDecimalValue(),MathContext.DECIMAL128).multiply(bdHundred).setScale(2, RoundingMode.HALF_UP);
            String rasMf = RasioMf.toString();
            otBiaRasioMf.setSubmittedValue(rasMf);
            rasioT =
                    new BigDecimal(otBiaRasioOntop.getValue().toString().replaceAll(" ",
                                                                                    "").replaceAll("%",
                                                                                                   ""));
            rasioTotal = RasioMf.add(rasioT);
            String Total = rasioTotal.toString();
            otBiaRasioTotal.setSubmittedValue(Total);

            rOntop = totalOntop;
            String rasio =
                otBiaRasioMf.getValue() == "" ? "0" : otBiaRasioMf.getValue() ==
                                                      null ? "0" :
                                                      otBiaRasioMf.getValue().toString();
            rasioT =
                    new BigDecimal(rasio.toString().replaceAll(" ", "").replaceAll("%",
                                                                                   ""));
            ontop =
                    rOntop.divide(value.getBigDecimalValue(),MathContext.DECIMAL128).multiply(bdHundred).setScale(2, RoundingMode.HALF_UP);

            otBiaOntop.setSubmittedValue(totalOntop);
            RasioOntop =
                    rOntop.divide(value.getBigDecimalValue(),MathContext.DECIMAL128).multiply(bdHundred).setScale(2, RoundingMode.HALF_UP);
            String rasOntop = RasioOntop.toString();
            otBiaRasioOntop.setSubmittedValue(rasOntop);

            r.setAttribute("BiaOntop", totalOntop);
            r.setAttribute("BiaRasionOntop", ontop);
            r.setAttribute("BiaRasioTotal", Total);
            r.setAttribute("BiaMf", rMf);
            r.setAttribute("BiaRasioMf", rasMf);

            dciterPromoProduk.getDataControl().commitTransaction();

            AdfFacesContext.getCurrentInstance().addPartialTarget(otBiaOntop);
            AdfFacesContext.getCurrentInstance().addPartialTarget(otBiaRasioOntop);
            AdfFacesContext.getCurrentInstance().addPartialTarget(otBiaRasioTotal);
            AdfFacesContext.getCurrentInstance().addPartialTarget(otBiaMf);
            AdfFacesContext.getCurrentInstance().addPartialTarget(otBiaRasioMf);
        }
    }

    public void refreshBiayaOntop(ValueChangeEvent valueChangeEvent) {
        String BiayaIdSel = "";
        BigDecimal NewBiayaOntop = zeroNum.getBigDecimalValue();
        BigDecimal CurBiayaMf = zeroNum.getBigDecimalValue();
        BigDecimal TotOtMf = zeroNum.getBigDecimalValue();
        Number TotOtMfNum = zeroNum;
        DCBindingContainer bindingsSelRow =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcItteratorBindings =
            bindingsSelRow.findIteratorBinding("BiayaView1Iterator");
        ViewObject voTableDatasel = dcItteratorBindings.getViewObject();
        Row rowSelected = voTableDatasel.getCurrentRow();
        if (rowSelected.getAttribute("BiayaId") != null) {
            BiayaIdSel = rowSelected.getAttribute("BiayaId").toString();
            NewBiayaOntop = new BigDecimal((String)valueChangeEvent.getNewValue() == null ? "0" : (String)valueChangeEvent.getNewValue());
            CurBiayaMf = ((Number)rowSelected.getAttribute("BiayaYearly") == null ? zeroNum : (Number)rowSelected.getAttribute("BiayaYearly")).getBigDecimalValue();
            TotOtMf = CurBiayaMf.add(NewBiayaOntop);

            try {
                TotOtMfNum = new Number(TotOtMf);
            } catch (SQLException e) {
                TotOtMfNum = zeroNum;
            }
            
            rowSelected.setAttribute("BiayaPrice", TotOtMfNum);
            rowSelected.setAttribute("BiayaTotAmt", TotOtMfNum);
            itTotAmt.setSubmittedValue(TotOtMfNum);
            itBiaPrice.setSubmittedValue(TotOtMfNum);
        }

        DCIteratorBinding dciterTarget =
            ADFUtils.findIterator("TargetView1Iterator");
        Row row = dciterTarget.getCurrentRow();
        BigDecimal RasioOntop = BigDecimal.ZERO;
        BigDecimal rasioTotal = BigDecimal.ZERO;
        BigDecimal rasioT = BigDecimal.ZERO;
        Number value =
            (Number)row.getAttribute("Value") == null ? new Number(0) :
            (Number)row.getAttribute("Value");
        if (value.compareTo(zeroNum) > 0 ||
            value.compareTo(zeroNum) < 0) {
            DCIteratorBinding BiaIterator =
                ADFUtils.findIterator("BiayaView1Iterator");
            ViewObject voTableDataOT = BiaIterator.getViewObject();
            RowSetIterator BiaIter = voTableDataOT.createRowSetIterator(null);
            BigDecimal valueOTsel = BigDecimal.ZERO;
            BigDecimal totalOT = BigDecimal.ZERO;
            while (BiaIter.hasNext()) {
                Row r = BiaIter.next();
                String id = r.getAttribute("BiayaId").toString();
                if (id.equalsIgnoreCase(BiayaIdSel)) {
                    BigDecimal newChangedMfValue =
                        new BigDecimal(valueChangeEvent.getNewValue() == "" ?
                                       "0" :
                                       valueChangeEvent.getNewValue() == null ?
                                       "0" :
                                       valueChangeEvent.getNewValue().toString());
                    valueOTsel = valueOTsel.add(newChangedMfValue);
                    valueChangeEvent.getComponent().processUpdates(FacesContext.getCurrentInstance());
                }
                Number MF =
                    (Number)r.getAttribute("BiayaNonYearly") == null ? new Number(0) :
                    (Number)r.getAttribute("BiayaNonYearly");
                totalOT = totalOT.add(MF.getBigDecimalValue());
            }
            BiaIter.closeRowSetIterator();
            String rasio =
                otBiaRasioMf.getValue() == "" ? "0" : otBiaRasioMf.getValue() ==
                                                      null ? "0" :
                                                      otBiaRasioMf.getValue().toString();
            rasioT =
                    new BigDecimal(rasio.toString().replaceAll(" ", "").replaceAll("%",
                                                                                   ""));
            otBiaOntop.setSubmittedValue(totalOT);
            RasioOntop =
                    totalOT.divide(value.getBigDecimalValue(),MathContext.DECIMAL128).multiply(bdHundred).setScale(2, RoundingMode.HALF_UP);
            BigDecimal rasOntop = RasioOntop;

            otBiaRasioOntop.setSubmittedValue(rasOntop);
            rasioTotal = RasioOntop.add(rasioT);
            String total = rasioTotal.toString();
            otBiaRasioTotal.setSubmittedValue(total);
            DCIteratorBinding dciterPromoProduk =
                ADFUtils.findIterator("PromoProdukView1Iterator");
            Row r = dciterPromoProduk.getCurrentRow();
            r.setAttribute("BiaOntop", totalOT);
            r.setAttribute("BiaRasionOntop", rasOntop);
            r.setAttribute("BiaRasioTotal", total);
            //dciterPromoProduk.getDataControl().commitTransaction();

            AdfFacesContext.getCurrentInstance().addPartialTarget(otBiaOntop);
            AdfFacesContext.getCurrentInstance().addPartialTarget(otBiaRasioOntop);
            AdfFacesContext.getCurrentInstance().addPartialTarget(otBiaRasioTotal);
        }
    }

    public void refreshBiayaMf(ValueChangeEvent valueChangeEvent) {
        String BiayaIdSel = "";
        BigDecimal NewBiayaMf = zeroNum.getBigDecimalValue();
        BigDecimal CurBiayaOntop = zeroNum.getBigDecimalValue();
        BigDecimal TotOtMf = zeroNum.getBigDecimalValue();
        Number TotOtMfNum = zeroNum;
        DCBindingContainer bindingsSelRow =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcItteratorBindings =
            bindingsSelRow.findIteratorBinding("BiayaView1Iterator");
        ViewObject voTableDatasel = dcItteratorBindings.getViewObject();
        Row rowSelected = voTableDatasel.getCurrentRow();
        if (rowSelected.getAttribute("BiayaId") != null) {
            BiayaIdSel = rowSelected.getAttribute("BiayaId").toString();
            NewBiayaMf = new BigDecimal((String)valueChangeEvent.getNewValue() == null ? "0" : (String)valueChangeEvent.getNewValue());
            CurBiayaOntop = ((Number)rowSelected.getAttribute("BiayaNonYearly") == null ? zeroNum : (Number)rowSelected.getAttribute("BiayaNonYearly")).getBigDecimalValue();
            TotOtMf = CurBiayaOntop.add(NewBiayaMf);

            try {
                TotOtMfNum = new Number(TotOtMf);
            } catch (SQLException e) {
                TotOtMfNum = zeroNum;
            }
            
            rowSelected.setAttribute("BiayaPrice", TotOtMfNum);
            rowSelected.setAttribute("BiayaTotAmt", TotOtMfNum);
            itTotAmt.setSubmittedValue(TotOtMfNum);
            itBiaPrice.setSubmittedValue(TotOtMfNum);
        }
        DCIteratorBinding dciterTarget =
            ADFUtils.findIterator("TargetView1Iterator");
        Row row = dciterTarget.getCurrentRow();
        BigDecimal RasioOntop = BigDecimal.ZERO;
        BigDecimal rasioTotal = BigDecimal.ZERO;
        BigDecimal rasioT = BigDecimal.ZERO;
        Number value =
            (Number)row.getAttribute("Value") == null ? new Number(0) :
            (Number)row.getAttribute("Value");
        if (value.compareTo(zeroNum) > 0 ||
            value.compareTo(zeroNum) < 0) {
            DCIteratorBinding BiaIterator =
                ADFUtils.findIterator("BiayaView1Iterator");
            ViewObject voTableDataOT = BiaIterator.getViewObject();
            RowSetIterator BiaIter = voTableDataOT.createRowSetIterator(null);
            BigDecimal valueOTsel = BigDecimal.ZERO;
            BigDecimal totalOT = BigDecimal.ZERO;
            while (BiaIter.hasNext()) {
                Row r = BiaIter.next();
                String id = r.getAttribute("BiayaId").toString();
                if (id.equalsIgnoreCase(BiayaIdSel)) {
                    BigDecimal newChangedMfValue =
                        new BigDecimal(valueChangeEvent.getNewValue() == "" ?
                                       "0" :
                                       valueChangeEvent.getNewValue() == null ?
                                       "0" :
                                       valueChangeEvent.getNewValue().toString());
                    valueOTsel = valueOTsel.add(newChangedMfValue);
                    valueChangeEvent.getComponent().processUpdates(FacesContext.getCurrentInstance());
                }
                Number MF =
                    (Number)r.getAttribute("BiayaYearly") == null ? new Number(0) :
                    (Number)r.getAttribute("BiayaYearly");
                totalOT = totalOT.add(MF.getBigDecimalValue());
            }
            BiaIter.closeRowSetIterator();
            String rasio =
                otBiaRasioOntop.getValue() == "" ? "0" : otBiaRasioOntop.getValue() ==
                                                         null ? "0" :
                                                         otBiaRasioOntop.getValue().toString();
            rasioT =
                    new BigDecimal(rasio.toString().replaceAll(" ", "").replaceAll("%",
                                                                                   ""));
            otBiaMf.setSubmittedValue(totalOT);
            RasioOntop =
                    totalOT.divide(value.getBigDecimalValue(),MathContext.DECIMAL128).multiply(bdHundred).setScale(2, RoundingMode.HALF_UP);
            BigDecimal rasOntop = RasioOntop;

            otBiaRasioMf.setSubmittedValue(rasOntop);
            rasioTotal = RasioOntop.add(rasioT);
            String total = rasioTotal.toString();
            otBiaRasioTotal.setSubmittedValue(total);
            DCIteratorBinding dciterPromoProduk =
                ADFUtils.findIterator("PromoProdukView1Iterator");
            Row r = dciterPromoProduk.getCurrentRow();
            r.setAttribute("BiaMf", totalOT);
            r.setAttribute("BiaRasioMf", rasOntop);
            r.setAttribute("BiaRasioTotal", total);
            //dciterPromoProduk.getDataControl().commitTransaction();
            AdfFacesContext.getCurrentInstance().addPartialTarget(otBiaMf);
            AdfFacesContext.getCurrentInstance().addPartialTarget(otBiaRasioMf);
            AdfFacesContext.getCurrentInstance().addPartialTarget(otBiaRasioTotal);
        }
    }

    public void resetProposal(ActionEvent actionEvent) {
        BindingContainer bindings = getBindings();
        DCIteratorBinding parentIter =
            (DCIteratorBinding)bindings.get("ProposalApprovalView1Iterator");
        //Get current row key
        Key parentKey = parentIter.getCurrentRow().getKey();

        OperationBinding operationBindingRollback =
            bindings.getOperationBinding("Rollback");
        operationBindingRollback.execute();

        //Set again row key as current row
        parentIter.setCurrentRowWithKey(parentKey.toStringFormat(true));
    }

    public static String getLastDay(String year, String month) {
        // get a calendar object
        GregorianCalendar calendar = new GregorianCalendar();
        // convert the year and month to integers
        int yearInt = Integer.parseInt(year);
        int monthInt = Integer.parseInt(month);
        // adjust the month for a zero based index
        monthInt = monthInt - 1;
        // set the date of the calendar to the date provided
        calendar.set(yearInt, monthInt, 1);
        int dayInt = calendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        return Integer.toString(dayInt);
    }

    public void vcePeriodePromo(ValueChangeEvent valueChangeEvent) throws ParseException {
        BindingContainer bindings =
            BindingContext.getCurrent().getCurrentBindingsEntry();

        UserData userData =
            (UserData)JSFUtils.resolveExpression("#{UserData}");
        String usrType =
            userData.getUserType() == null ? "" : userData.getUserType();

        boolean backDateOk = false;
        java.util.Date date2 =
            new java.util.Date(valueChangeEvent.getNewValue().toString());
        java.util.Date today = new java.util.Date();

        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.DATE, -1);
        Date yesterday = c.getTime();

        AttributeBinding custTypeAttr =
            (AttributeBinding)bindings.getControlBinding("CustRegFlag");
        String custType = (String)custTypeAttr.getInputValue();

        ArrayList<String> custRegCodeList = new ArrayList<String>();

        // Calculate history date
        DateFormat dfMM = new SimpleDateFormat("MM");
        String currMon = dfMM.format(date2);
        Integer last3Mon = Integer.valueOf(currMon) - 3;
        Integer last1Mon = Integer.valueOf(currMon) - 1;
        DateFormat dfYY = new SimpleDateFormat("yyyy");
        String currYear = dfYY.format(date2);
        Integer thisYear = Integer.valueOf(currYear);

        DateConversion.Measure last3MonCal =
            new DateConversion.Measure().month(last3Mon).year(thisYear);
        String DateTo =
            thisYear.toString() + String.format("%02d", last1Mon) + getLastDay(thisYear.toString(),
                                                                               last1Mon.toString());
        String d1HisF = last3MonCal.min().toString();
        SimpleDateFormat formatterx = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat formatterui = new SimpleDateFormat("dd-MMM-yyyy");
        java.util.Date fr = formatterx.parse(d1HisF);
        java.util.Date to = formatterx.parse(DateTo);
        idHistFrom.setSubmittedValue(formatterui.format(fr).toString());
        idHistTo.setSubmittedValue(formatterui.format(to).toString());
        otHistFrom.setValue(formatterui.format(fr).toString());
        otHistTo.setValue(formatterui.format(to).toString());
        AdfFacesContext.getCurrentInstance().addPartialTarget(idHistFrom);
        AdfFacesContext.getCurrentInstance().addPartialTarget(idHistTo);
        AdfFacesContext.getCurrentInstance().addPartialTarget(otHistFrom);
        AdfFacesContext.getCurrentInstance().addPartialTarget(otHistTo);

        if (date2.before(yesterday)) {
            DCIteratorBinding dciterCustArea =
                ADFUtils.findIterator("PromoCustomerAreaView1Iterator");
            for (Row r : dciterCustArea.getAllRowsInRange()) {
                String regCode = (String)r.getAttribute("RegionCode");
                if (!custRegCodeList.contains(regCode)) {
                    custRegCodeList.add(regCode);
                }
            }

            if (custRegCodeList.contains(backDateBlockRegion) &&
                custRegCodeList.size() == 1) {
                backDateOk = true;
            } else {
                backDateOk = false;
            }

        } else {
            backDateOk = true;
        }

        if (backDateOk || usrType.equalsIgnoreCase(userHo)) {
            if (idPeriodeTo.getValue() != null) {
                java.util.Date date1 =
                    new Date(idPeriodeTo.getValue().toString());
                java.util.Date date21 =
                    new Date(valueChangeEvent.getNewValue().toString());
                long days = ADFUtils.daysBetween(date1, date21);
                Number days1 = new Number(days + 1);
                itCountDays.setValue(days1);
                AdfFacesContext.getCurrentInstance().addPartialTarget(itCountDays);
            }
        } else {
            idPeriodProgFrom.setSubmittedValue(null);
            AdfFacesContext.getCurrentInstance().addPartialTarget(idPeriodProgFrom);
            JSFUtils.addFacesWarningMessage("Pengajuan proposal ini tidak diperkenankan untuk \"Back Date\"");
        }
    }

    public void idPeriodProgToCount(ValueChangeEvent valueChangeEvent) {
        if (idPeriodProgFrom.getValue() != null) {
            java.util.Date date1 =
                new Date(idPeriodProgFrom.getValue().toString());
            java.util.Date date2 =
                new Date(valueChangeEvent.getNewValue().toString());
            long days = ADFUtils.daysBetween(date1, date2);
            Number days1 = new Number(days + 1);
            itCountDays.setValue(days1);
            AdfFacesContext.getCurrentInstance().addPartialTarget(itCountDays);
        }
    }

    public void setIdPeriodeTo(RichInputDate idPeriodeTo) {
        this.idPeriodeTo = idPeriodeTo;
    }

    public RichInputDate getIdPeriodeTo() {
        return idPeriodeTo;
    }

    public void setIdPeriodProgFrom(RichInputDate idPeriodProgFrom) {
        this.idPeriodProgFrom = idPeriodProgFrom;
    }

    public RichInputDate getIdPeriodProgFrom() {
        return idPeriodProgFrom;
    }

    public void setOtCountDays(RichOutputText otCountDays) {
        this.otCountDays = otCountDays;
    }

    public RichOutputText getOtCountDays() {
        return otCountDays;
    }

    public void setIdHistFrom(RichInputDate idHistFrom) {
        this.idHistFrom = idHistFrom;
    }

    public RichInputDate getIdHistFrom() {
        return idHistFrom;
    }

    public void setIdHistTo(RichInputDate idHistTo) {
        this.idHistTo = idHistTo;
    }

    public RichInputDate getIdHistTo() {
        return idHistTo;
    }


    public void setItCountDays(RichInputText itCountDays) {
        this.itCountDays = itCountDays;
    }

    public RichInputText getItCountDays() {
        return itCountDays;
    }

    public void setSwitchMain(UIXSwitcher switchMain) {
        this.switchMain = switchMain;
    }

    public UIXSwitcher getSwitchMain() {
        return switchMain;
    }

    public void setSocForwardTo(RichSelectOneChoice socForwardTo) {
        this.socForwardTo = socForwardTo;
    }

    public RichSelectOneChoice getSocForwardTo() {
        return socForwardTo;
    }

    public void actionValueChangeListener(ValueChangeEvent valueChangeEvent) {
        socForwardTo.setSubmittedValue(null);
        itUserForward.setSubmittedValue(null);
        AdfFacesContext.getCurrentInstance().addPartialTarget(itReasonApproval);
    }

    public void setItUserForward(RichInputText itUserForward) {
        this.itUserForward = itUserForward;
    }

    public RichInputText getItUserForward() {
        return itUserForward;
    }

    public void forwardValueChangeListener(ValueChangeEvent valueChangeEvent) {
        AdfFacesContext.getCurrentInstance().addPartialTarget(itUserForward);
    }

    public void setOtHistFrom(RichOutputText otHistFrom) {
        this.otHistFrom = otHistFrom;
    }

    public RichOutputText getOtHistFrom() {
        return otHistFrom;
    }

    public void setOtHistTo(RichOutputText otHistTo) {
        this.otHistTo = otHistTo;
    }

    public RichOutputText getOtHistTo() {
        return otHistTo;
    }

    public void tableFilterProcessQuery(QueryEvent queryEvent) {
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
            /*
            if (attrDescriptor.getType().equals(String.class)) {
                if (value != null) {
                    ((AttributeCriterion) criterion).setValue("%" + value + "%");
                }
            }
            */
        }

        //Execute query
        ADFUtils.invokeEL("#{bindings.ProposalApprovalView1Query.processQuery}",
                          new Class[] { QueryEvent.class },
                          new Object[] { queryEvent });

        switchMain.setFacetName("nodata");
        switchButtonMain.setFacetName("nodata");
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchButtonMain);
    }

    public void setPropTypeVal(RichSelectOneChoice propTypeVal) {
        this.propTypeVal = propTypeVal;
    }

    public RichSelectOneChoice getPropTypeVal() {
        return propTypeVal;
    }

    protected void refreshPage() {
        FacesContext fc = FacesContext.getCurrentInstance();
        String refreshpage = fc.getViewRoot().getViewId();
        ViewHandler ViewH = fc.getApplication().getViewHandler();
        UIViewRoot UIV = ViewH.createView(fc, refreshpage);
        UIV.setViewId(refreshpage);
        fc.setViewRoot(UIV);
    }

    public void setBtnOkpromoDetail(RichCommandButton btnOkpromoDetail) {
        this.btnOkpromoDetail = btnOkpromoDetail;
    }

    public RichCommandButton getBtnOkpromoDetail() {
        return btnOkpromoDetail;
    }

    public void setLovTipePerhitungan(RichInputListOfValues lovTipePerhitungan) {
        this.lovTipePerhitungan = lovTipePerhitungan;
    }

    public RichInputListOfValues getLovTipePerhitungan() {
        return lovTipePerhitungan;
    }

    public void setItqtyFromDisc(RichInputText itqtyFromDisc) {
        this.itqtyFromDisc = itqtyFromDisc;
    }

    public RichInputText getItqtyFromDisc() {
        return itqtyFromDisc;
    }

    public void setItKelipatanDisc(RichInputText itKelipatanDisc) {
        this.itKelipatanDisc = itKelipatanDisc;
    }

    public RichInputText getItKelipatanDisc() {
        return itKelipatanDisc;
    }

    public void launchPopupListenerTipePotDisc(LaunchPopupEvent launchPopupEvent) {
        String PromoBonusIdSel = "";
        String lovDiscTipePerhitungan = "";
        DCBindingContainer bindingsSelRow =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcItteratorBindings =
            bindingsSelRow.findIteratorBinding("DiscountView1Iterator");
        ViewObject voTableData = dcItteratorBindings.getViewObject();
        Row rowSelected = voTableData.getCurrentRow();
        if (rowSelected.getAttribute("DiscountId") != null) {
            PromoBonusIdSel =
                    rowSelected.getAttribute("DiscountId").toString();
            lovDiscTipePerhitungan = socTypePotongan.getValue()==null ? "" : socTypePotongan.getValue().toString();
            if(lovDiscTipePerhitungan.equalsIgnoreCase("")){
            ADFContext adfCtx = ADFContext.getCurrent();
            Map pageFlowScope = adfCtx.getPageFlowScope();
        //                Object ppId = pageFlowScope.get("DiscountId");
                Object lovPricebyscope = pageFlowScope.get("lovTipeNull");
                lovDiscTipePerhitungan =lovPricebyscope.toString();
            Object DiscId = pageFlowScope.put("DiscountId", PromoBonusIdSel);
            Object lovTipePerhitunganscope =
                pageFlowScope.put("socTypePotongan", lovDiscTipePerhitungan);
                socTypePotongan.setSubmittedValue(lovDiscTipePerhitungan);
                AdfFacesContext.getCurrentInstance().addPartialTarget(socTypePotongan);
            }else{
                ADFContext adfCtx = ADFContext.getCurrent();
                Map pageFlowScope = adfCtx.getPageFlowScope();
                Object DiscId = pageFlowScope.put("DiscountId", PromoBonusIdSel);
                Object lovTipePerhitunganscope =
                    pageFlowScope.put("lovTipePerhitungan", lovDiscTipePerhitungan);
            }
        }
    }

    public void returnPopupListenerTipePotDisc(ReturnPopupEvent returnPopupEvent) {
        String PromoProdIdSel = "";
        String promoProdId = "";
        DCBindingContainer bindingsSelRow =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();

        DCIteratorBinding iterPromoProduk =
            bindingsSelRow.findIteratorBinding("PromoProdukView1Iterator");
        ViewObject voPromoProduk = iterPromoProduk.getViewObject();
        Row rowSelectedPromoProduk = voPromoProduk.getCurrentRow();
        
        DCIteratorBinding dcItteratorBindings =
            bindingsSelRow.findIteratorBinding("DiscountView1Iterator");
        ViewObject voTableData = dcItteratorBindings.getViewObject();
        Row rowSelected = voTableData.getCurrentRow();
        if (rowSelected.getAttribute("DiscountId") != null) {
            String lovPriceBy = "";
            ADFContext adfCtx = ADFContext.getCurrent();
            Map pageFlowScope = adfCtx.getPageFlowScope();
            Object ppId = pageFlowScope.get("DiscountId");
            Object lovPricebyscope = pageFlowScope.get("lovTipePerhitungan");
            promoProdId = ppId.toString();
            PromoProdIdSel =
                    rowSelected.getAttribute("DiscountId").toString();
            lovPriceBy = socTypePotongan.getValue()== null ? "": socTypePotongan.getValue().toString();
            if (PromoProdIdSel.equalsIgnoreCase(promoProdId)) {
                if (lovPriceBy.equalsIgnoreCase("")) {
                    if (lovPricebyscope.toString().equalsIgnoreCase(tipePotonganPercent)) {
                        socTypePotongan.setSubmittedValue(tipePotonganPercent);
                        rowSelected.setAttribute("TipePotongan", tipePotonganPercent);
                        AdfFacesContext.getCurrentInstance().addPartialTarget(socTypePotongan);
                    } else {
                        socTypePotongan.setSubmittedValue(tipePotonganAmount);
                        rowSelected.setAttribute("TipePotongan", tipePotonganAmount);
                        AdfFacesContext.getCurrentInstance().addPartialTarget(socTypePotongan);
                    }
                }
            }
            
            if (PromoProdIdSel.equalsIgnoreCase(promoProdId)) {
                rowOntop.setSubmittedValue(null);
                rowMf.setSubmittedValue(null);
                rowSelected.setAttribute("DiscNonYearly", null);
                rowSelected.setAttribute("DiscYearly", null);
                AdfFacesContext.getCurrentInstance().addPartialTarget(rowOntop);            
                AdfFacesContext.getCurrentInstance().addPartialTarget(rowMf);

                otOnTop.setSubmittedValue(0);
                otMF.setSubmittedValue(0);
                otRasioOntop.setSubmittedValue(0);
                otRasioMf.setSubmittedValue(0.00);
                otRasioTotal.setSubmittedValue(0.00);
                rowSelectedPromoProduk.setAttribute("DiscOnTop", 0);                
                rowSelectedPromoProduk.setAttribute("DiscMf", 0);                
                rowSelectedPromoProduk.setAttribute("DiscRasioOnTop", 0.00);                
                rowSelectedPromoProduk.setAttribute("DiscRasioMf", 0.00);                
                rowSelectedPromoProduk.setAttribute("DiscRasioTotal1", 0.00);
                AdfFacesContext.getCurrentInstance().addPartialTarget(otOnTop);            
                AdfFacesContext.getCurrentInstance().addPartialTarget(otMF);
                AdfFacesContext.getCurrentInstance().addPartialTarget(otRasioOntop); 
                AdfFacesContext.getCurrentInstance().addPartialTarget(otRasioMf); 
                AdfFacesContext.getCurrentInstance().addPartialTarget(otRasioTotal); 
            }
        }
    }

    public void tipePotonganEvent(ValueChangeEvent valueChangeEvent) {
        DCBindingContainer bindingsSelRow =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        String TipePotongan = valueChangeEvent.getNewValue() == null ? "" :  valueChangeEvent.getNewValue().toString();
        if(TipePotongan.equalsIgnoreCase("PERCENT")){
            DCIteratorBinding dcItteratorBindingsSetTipePot =
                bindingsSelRow.findIteratorBinding("DiscountView1Iterator");
            if(dcItteratorBindingsSetTipePot.getEstimatedRowCount() > 0){
                for(Row r :dcItteratorBindingsSetTipePot.getAllRowsInRange()){
                    socTypePotongan.setSubmittedValue("PERCENT");
                    r.setAttribute("TipePotongan", "PERCENT"); 
                    r.setAttribute("DiscNonYearly", 0);
                    r.setAttribute("DiscYearly", 0);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(socTypePotongan);
                }
            }
            otOnTop.setSubmittedValue(0);
            otMF.setSubmittedValue(0);
            otRasioOntop.setSubmittedValue(0);
            otRasioMf.setSubmittedValue(0);
            otRasioTotal.setSubmittedValue(0);
            AdfFacesContext.getCurrentInstance().addPartialTarget(otOnTop);
            AdfFacesContext.getCurrentInstance().addPartialTarget(otMF);
            AdfFacesContext.getCurrentInstance().addPartialTarget(otRasioOntop);
            AdfFacesContext.getCurrentInstance().addPartialTarget(otRasioMf);
            AdfFacesContext.getCurrentInstance().addPartialTarget(otRasioTotal);
            AdfFacesContext.getCurrentInstance().addPartialTarget(socTypePotongan);
            AdfFacesContext.getCurrentInstance().addPartialTarget(tableListPotongan);
        }else {
            DCIteratorBinding dcItteratorBindingsSetTipePot =
                bindingsSelRow.findIteratorBinding("DiscountView1Iterator");
            if(dcItteratorBindingsSetTipePot.getEstimatedRowCount() > 0){
                for(Row r :dcItteratorBindingsSetTipePot.getAllRowsInRange()){
                    socTypePotongan.setSubmittedValue("AMOUNT");
                    r.setAttribute("TipePotongan", "AMOUNT");
                    r.setAttribute("DiscNonYearly", 0);
                    r.setAttribute("DiscYearly", 0);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(socTypePotongan);
                }
            }
            otOnTop.setSubmittedValue(0);
            otMF.setSubmittedValue(0);
            otRasioOntop.setSubmittedValue(0);
            otRasioMf.setSubmittedValue(0);
            otRasioTotal.setSubmittedValue(0);
            AdfFacesContext.getCurrentInstance().addPartialTarget(otOnTop);
            AdfFacesContext.getCurrentInstance().addPartialTarget(otMF);
            AdfFacesContext.getCurrentInstance().addPartialTarget(otRasioOntop);
            AdfFacesContext.getCurrentInstance().addPartialTarget(otRasioMf);
            AdfFacesContext.getCurrentInstance().addPartialTarget(otRasioTotal);
             AdfFacesContext.getCurrentInstance().addPartialTarget(socTypePotongan);
            AdfFacesContext.getCurrentInstance().addPartialTarget(tableListPotongan);
        }
    }

    public void attachmentPopupReturnListener(ReturnEvent returnEvent) {
        AdfFacesContext.getCurrentInstance().addPartialTarget(btnAttachment);
    }

    public void setBtnAttachment(RichCommandToolbarButton btnAttachment) {
        this.btnAttachment = btnAttachment;
    }

    public RichCommandToolbarButton getBtnAttachment() {
        return btnAttachment;
    }

    public void changeProductApproval(ActionEvent actionEvent) {
        DCBindingContainer bindingsSelRow =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcItteratorBindingsPromoProdukView1 =
            bindingsSelRow.findIteratorBinding("PromoProdukView1Iterator");
//        System.out.println("dcItteratorBindingsPromoProdukView1.getCurrentRow().getAttribute(\"ProductApproval\"): " 
//                           + dcItteratorBindingsPromoProdukView1.getCurrentRow().getAttribute("ProductApproval"));
        String productApproval = 
            dcItteratorBindingsPromoProdukView1.getCurrentRow().getAttribute("ProductApproval").toString();
        String productApprovalNew = "Y";
        if(productApproval.equals("Y")){
            productApprovalNew = "N";
        }
        dcItteratorBindingsPromoProdukView1.getCurrentRow().setAttribute("ProductApproval", productApprovalNew);
//        System.out.println("[AFTER] dcItteratorBindingsPromoProdukView1.getCurrentRow().getAttribute(\"ProductApproval\"): " 
//                           + dcItteratorBindingsPromoProdukView1.getCurrentRow().getAttribute("ProductApproval"));
    }

    public void cancelPpDialogListener(DialogEvent dialogEvent) {
        BindingContainer bindings = getBindings();

        if (dialogEvent.getOutcome().name().equals("ok")) {

            AttributeBinding statusPropAttr =
                (AttributeBinding)bindings.getControlBinding("Status1");
            statusPropAttr.setInputValue("CANCELED");
            
            OperationBinding operationCancelPp =
                bindings.getOperationBinding("cancelDocPp");
            operationCancelPp.execute();
            
            OperationBinding operationBinding =
                bindings.getOperationBinding("Commit");
            operationBinding.execute();

            OperationBinding operationRefresh =
                bindings.getOperationBinding("Execute");
            operationRefresh.execute();

            OperationBinding operationRefreshDoc =
                bindings.getOperationBinding("Execute1");
            operationRefreshDoc.execute();

            switchMain.setFacetName("nodata");
            switchButtonMain.setFacetName("nodata");
            AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
            AdfFacesContext.getCurrentInstance().addPartialTarget(switchButtonMain);
            AdfFacesContext.getCurrentInstance().addPartialTarget(tblApproval);
        }
    }

    public void setItReasonCanceled(RichInputText itReasonCanceled) {
        this.itReasonCanceled = itReasonCanceled;
    }

    public RichInputText getItReasonCanceled() {
        return itReasonCanceled;
    }

    public void filterByUnapprove(ActionEvent actionEvent) {
        ADFContext adfCtx = ADFContext.getCurrent();
        Map pageFlowScope = adfCtx.getPageFlowScope();
        pageFlowScope.put("approvalMode", "unapprove");
        pageFlowScope.put("initAprvl", "filtered");
        
        UserData userData =
            (UserData)JSFUtils.resolveExpression("#{UserData}");
        String usrRole =
            userData.getUserRole() == null ? "" : userData.getUserRole();
        String usrName =
            userData.getUserNameLogin() == null ? "" : userData.getUserNameLogin();
        String compId =
            userData.getCompanyId() == null ? "" : userData.getCompanyId();
        
        DCIteratorBinding iter =
            (DCIteratorBinding)getBindings().get("ProposalApprovalView1Iterator");
        ProposalApprovalViewImpl vo = (ProposalApprovalViewImpl)iter.getViewObject();
        vo.setWhereClause("Proposal.PROPOSAL_ID = DocApproval.PROPOSAL_ID " +
        "AND Proposal.COMPANY_ID = '" + compId + "' " + 
        "AND DocApproval.ROLE_NAME = '" + usrRole + "' " + 
        "AND DocApproval.ACTION_TO = '" + usrName + "' " + 
        "AND Proposal.STATUS = 'ACTIVE' " + 
        "AND Proposal.CONFIRM_NO IS NULL " + 
        "AND DocApproval.ACTION = 'FINISHED' ");
        vo.executeQuery();    

        switchMain.setFacetName("nodata");
        switchButtonMain.setFacetName("nodata");
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchButtonMain);
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblApproval);
    }

    public void filterByApproval(ActionEvent actionEvent) {
        ADFContext adfCtx = ADFContext.getCurrent();
        Map pageFlowScope = adfCtx.getPageFlowScope();
        pageFlowScope.put("approvalMode", "approve");
        pageFlowScope.put("initAprvl", "filtered");
            
        UserData userData =
            (UserData)JSFUtils.resolveExpression("#{UserData}");
        String usrRole =
            userData.getUserRole() == null ? "" : userData.getUserRole();
        String usrName =
            userData.getUserNameLogin() == null ? "" : userData.getUserNameLogin();
        String compId =
            userData.getCompanyId() == null ? "" : userData.getCompanyId();
        
        DCIteratorBinding iter =
            (DCIteratorBinding)getBindings().get("ProposalApprovalView1Iterator");
        ProposalApprovalViewImpl vo = (ProposalApprovalViewImpl)iter.getViewObject();
        vo.setWhereClause("Proposal.PROPOSAL_ID = DocApproval.PROPOSAL_ID " +
        "AND Proposal.COMPANY_ID = '" + compId + "' " + 
        "AND DocApproval.ROLE_NAME = '" + usrRole + "' " + 
        "AND DocApproval.ACTION_TO = '" + usrName + "' " + 
        "AND Proposal.STATUS IN ('INPROCESS', 'REJECTED') " + 
        "AND Proposal.CONFIRM_NO IS NULL " + 
        "AND DocApproval.ACTION IS NULL " + 
        "AND DocApproval.CREATION_DATE = (SELECT MAX(DA.CREATION_DATE) FROM DOC_APPROVAL DA WHERE DA.PROPOSAL_ID = Proposal.PROPOSAL_ID AND DA.ACTION IS NULL AND DA.ROLE_NAME = DocApproval.ROLE_NAME AND DA.ACTION_TO = DocApproval.ACTION_TO)");
        vo.executeQuery();  
        
        switchMain.setFacetName("nodata");
        switchButtonMain.setFacetName("nodata");
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchButtonMain);        
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblApproval);
    }

    public void setInitApprovalList(String initApprovalList) {
        this.initApprovalList = initApprovalList;
    }

    public String getInitApprovalList() {

        ADFContext adfCtx = ADFContext.getCurrent();
        Map pageFlowScope = adfCtx.getPageFlowScope();
        
        String initAprvl = pageFlowScope.get("initAprvl") == null ? "" : pageFlowScope.get("initAprvl").toString();        
        String aprvlMode = pageFlowScope.get("approvalMode") == null ? "" : pageFlowScope.get("approvalMode").toString();
            
        if (initAprvl.equalsIgnoreCase("") && aprvlMode.equalsIgnoreCase("")) {
            // Flag agar where clause tidak ter-execute berkali-kali ketika filtering
            pageFlowScope.put("initAprvl", "initialized");
            pageFlowScope.put("approvalMode", "approve");
            
            UserData userData =
                (UserData)JSFUtils.resolveExpression("#{UserData}");
            String usrRole =
                userData.getUserRole() == null ? "" : userData.getUserRole();
            String usrName =
                userData.getUserNameLogin() == null ? "" : userData.getUserNameLogin();
            String compId =
                userData.getCompanyId() == null ? "" : userData.getCompanyId();
            
            DCIteratorBinding iter =
                (DCIteratorBinding)getBindings().get("ProposalApprovalView1Iterator");
            ProposalApprovalViewImpl vo = (ProposalApprovalViewImpl)iter.getViewObject();
            vo.setWhereClause("Proposal.PROPOSAL_ID = DocApproval.PROPOSAL_ID " +
            "AND Proposal.COMPANY_ID = '" + compId + "' " + 
            "AND DocApproval.ROLE_NAME = '" + usrRole + "' " + 
            "AND DocApproval.ACTION_TO = '" + usrName + "' " + 
            "AND Proposal.STATUS IN ('INPROCESS', 'REJECTED') " + 
            "AND Proposal.CONFIRM_NO IS NULL " + 
            "AND DocApproval.ACTION IS NULL " + 
            "AND DocApproval.CREATION_DATE = (SELECT MAX(DA.CREATION_DATE) FROM DOC_APPROVAL DA WHERE DA.PROPOSAL_ID = Proposal.PROPOSAL_ID AND DA.ACTION IS NULL AND DA.ROLE_NAME = DocApproval.ROLE_NAME AND DA.ACTION_TO = DocApproval.ACTION_TO)");
            vo.executeQuery();  
            
            if (vo.getEstimatedRowCount() > 0) {
                initApprovalList="Y";
            } else {
                initApprovalList="N";
            }
            
            switchMain.setFacetName("nodata");
            switchButtonMain.setFacetName("nodata");
            AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
            AdfFacesContext.getCurrentInstance().addPartialTarget(switchButtonMain);            
            AdfFacesContext.getCurrentInstance().addPartialTarget(tblApproval);
        }
        
        return initApprovalList;
    }
    
    public void unApprovePpDialogListener(DialogEvent dialogEvent) {
        BindingContainer bindings = getBindings();

        if (dialogEvent.getOutcome().name().equals("ok")) {
            AttributeBinding statusPropAttr =
                (AttributeBinding)bindings.getControlBinding("Status1");
            statusPropAttr.setInputValue("INPROCESS");
            
            OperationBinding operationCancelPp =
                bindings.getOperationBinding("unApproveDocPp");
            operationCancelPp.execute();
            
            OperationBinding operationBinding =
                bindings.getOperationBinding("Commit");
            operationBinding.execute();

            OperationBinding operationRefresh =
                bindings.getOperationBinding("Execute");
            operationRefresh.execute();

            OperationBinding operationRefreshDoc =
                bindings.getOperationBinding("Execute1");
            operationRefreshDoc.execute();

            switchMain.setFacetName("nodata");
            switchButtonMain.setFacetName("nodata");
            AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
            AdfFacesContext.getCurrentInstance().addPartialTarget(switchButtonMain);
            AdfFacesContext.getCurrentInstance().addPartialTarget(tblApproval);
        }
    }

    public void launchpopupDiskonListener(LaunchPopupEvent launchPopupEvent) {
        String PromoBonusIdSel = "";
            String lovDiscTipePerhitungan = "";
            DCBindingContainer bindingsSelRow =
                (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
            DCIteratorBinding dcItteratorBindings =
                bindingsSelRow.findIteratorBinding("DiscountView1Iterator");
            ViewObject voTableData = dcItteratorBindings.getViewObject();
            Row rowSelected = voTableData.getCurrentRow();
            if (rowSelected.getAttribute("DiscountId") != null) {
                PromoBonusIdSel =
                        rowSelected.getAttribute("DiscountId").toString();
                lovDiscTipePerhitungan = lovTipePerhitungan.getValue()==null ? "" : lovTipePerhitungan.getValue().toString();
                if(lovDiscTipePerhitungan.equalsIgnoreCase("")){
                ADFContext adfCtx = ADFContext.getCurrent();
                Map pageFlowScope = adfCtx.getPageFlowScope();
        //                Object ppId = pageFlowScope.get("DiscountId");
                    Object lovPricebyscope = pageFlowScope.get("lovTipeNull");
                    lovDiscTipePerhitungan =lovPricebyscope.toString();
                Object DiscId = pageFlowScope.put("DiscountId", PromoBonusIdSel);
                Object lovTipePerhitunganscope =
                    pageFlowScope.put("lovTipePerhitungan", lovDiscTipePerhitungan);
                    rowSelected.setAttribute("TipePerhitungan", lovDiscTipePerhitungan);
                    lovTipePerhitungan.setSubmittedValue(lovDiscTipePerhitungan);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(lovTipePerhitungan);
                }else{
                    ADFContext adfCtx = ADFContext.getCurrent();
                    Map pageFlowScope = adfCtx.getPageFlowScope();
                    Object DiscId = pageFlowScope.put("DiscountId", PromoBonusIdSel);
                    Object lovTipePerhitunganscope =
                        pageFlowScope.put("lovTipePerhitungan", lovDiscTipePerhitungan);
                    rowSelected.setAttribute("TipePerhitungan", lovDiscTipePerhitungan);
                    lovTipePerhitungan.setSubmittedValue(lovDiscTipePerhitungan);
                    AdfFacesContext.getCurrentInstance().addPartialTarget(lovTipePerhitungan);
                }
            }
    }

    public void returnPopupDiskonListener(ReturnPopupEvent returnPopupEvent) {
        String PromoBonusIdSel = "";
        String promoProdId = "";
        DCBindingContainer bindingsSelRow =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcItteratorBindings =
            bindingsSelRow.findIteratorBinding("DiscountView1Iterator");
        ViewObject voTableData = dcItteratorBindings.getViewObject();
        Row rowSelected = voTableData.getCurrentRow();
        if (rowSelected.getAttribute("DiscountId") != null) {
            String lovPriceBy = "";
            ADFContext adfCtx = ADFContext.getCurrent();
            Map pageFlowScope = adfCtx.getPageFlowScope();
            Object ppId = pageFlowScope.get("DiscountId");
            Object lovPricebyscope = pageFlowScope.get("lovTipePerhitungan");
            promoProdId = ppId.toString();
            PromoBonusIdSel =
                    rowSelected.getAttribute("DiscountId").toString();
            lovPriceBy = lovTipePerhitungan.getValue()== null ? "": lovTipePerhitungan.getValue().toString();
            if (PromoBonusIdSel.equalsIgnoreCase(promoProdId)) {
                if (lovPriceBy.equalsIgnoreCase("")) {
                    if (lovPricebyscope.toString().equalsIgnoreCase(tipeHitungTdkKelipatan)) {
                        lovTipePerhitungan.setSubmittedValue(tipeHitungTdkKelipatan);
                        Object DiscId =
                            pageFlowScope.put("DiscountId", PromoBonusIdSel);
                        Object lovTipePerhitunganscope =
                            pageFlowScope.put("lovTipeNull", tipeHitungTdkKelipatan);
                        rowSelected.setAttribute("TipePerhitungan",  tipeHitungTdkKelipatan);
                        AdfFacesContext.getCurrentInstance().addPartialTarget(lovTipePerhitungan);
                    } else {
                        lovTipePerhitungan.setSubmittedValue(tipeHitungKelipatan);
                         Object DiscId = pageFlowScope.put("DiscountId", PromoBonusIdSel);
                         Object lovTipePerhitunganscope =
                         pageFlowScope.put("lovTipeNull", tipeHitungKelipatan);
                        rowSelected.setAttribute("TipePerhitungan",  tipeHitungKelipatan);
                         AdfFacesContext.getCurrentInstance().addPartialTarget(lovTipePerhitungan);
                    }
                }
            }
        }
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
            binding.findIteratorBinding("ProposalApprovalView1Iterator");
        ViewObject voProposal = iterProposal.getViewObject();
        Row rowSelected = voProposal .getCurrentRow();

        UserData userData =
            (UserData)JSFUtils.resolveExpression("#{UserData}");
        String userNameLogin =
            userData.getUserNameLogin() == null ? "" : userData.getUserNameLogin();
        String printCount =
            userData.getPrintCount() == null ? "N" : userData.getPrintCount();
        
        if (printCount.equalsIgnoreCase(printCountYes)) {        
            ApprovalAMImpl approvalAM =
                (ApprovalAMImpl)ADFUtils.getApplicationModuleForDataControl("ApprovalAMDataControl");
            CallableStatement cst = null;
            try {
                cst =
            approvalAM.getDBTransaction().createCallableStatement("BEGIN FCS_PRINT_PREV_LOG(" + propId.getSequenceNumber().getBigDecimalValue() + ", '" + userNameLogin + "'); END;", 0);
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

    public void prodClassReturnPopupListener(ReturnPopupEvent returnPopupEvent) {        
        // Get produk class description
        RichInputListOfValues lovField = (RichInputListOfValues)returnPopupEvent.getSource(); 
        ListOfValuesModel lovModel =  lovField.getModel(); 
        CollectionModel collectionModel = lovModel.getTableModel().getCollectionModel();   
        JUCtrlHierBinding treeBinding = (JUCtrlHierBinding) collectionModel.getWrappedData();
        RowKeySet rks = (RowKeySet) returnPopupEvent.getReturnValue();
        List tableRowKey = (List) rks.iterator().next(); 
        DCIteratorBinding dciter = treeBinding.getDCIteratorBinding(); 
        Key key = (Key) tableRowKey.get(0); 
        Row rw =  dciter.findRowByKeyString(key.toStringFormat(true));
       String rwDesc = (String)rw.getAttribute("SetClassDesc");
        
        // Set produk description 
        BindingContext bctx = BindingContext.getCurrent();
        DCBindingContainer binding =
            (DCBindingContainer)bctx.getCurrentBindingsEntry();
        DCIteratorBinding iterProduk =
            binding.findIteratorBinding("PromoProdukView1Iterator");
        ViewObject voPromoProduk = iterProduk.getViewObject();
        Row rowSelected = voPromoProduk.getCurrentRow();
        String prodClass = (String)rowSelected.getAttribute("ProductClass");
        
        if (prodClass == null) {
            rowSelected.setAttribute("ProductClassDesc", null);
            rowSelected.setAttribute("ProductBrandDesc", null);
            rowSelected.setAttribute("ProductExtDesc", null);
            rowSelected.setAttribute("ProductPackDesc", null);
        } else {
            rowSelected.setAttribute("ProductClassDesc", rwDesc);
            if (rwDesc.equalsIgnoreCase(varAll)) {
                rowSelected.setAttribute("ProductBrandDesc", varAll);
                rowSelected.setAttribute("ProductExtDesc", varAll);
                rowSelected.setAttribute("ProductPackDesc", varAll);
            } else {
                rowSelected.setAttribute("ProductBrandDesc", null);
                rowSelected.setAttribute("ProductExtDesc", null);
                rowSelected.setAttribute("ProductPackDesc", null);
            }
        }
        
        BindingContainer bindings = getBindings();
        OperationBinding operationBinding =
            bindings.getOperationBinding("Commit");
        operationBinding.execute();
        
        iterProduk.executeQuery();
        iterProduk.setCurrentRowWithKey(rowSelected.getKey().toStringFormat(true));
    }

    public void prodBrandReturnPopupListener(ReturnPopupEvent returnPopupEvent) {       
        // Get produk brand description
        RichInputListOfValues lovField = (RichInputListOfValues)returnPopupEvent.getSource(); 
        ListOfValuesModel lovModel =  lovField.getModel(); 
        CollectionModel collectionModel = lovModel.getTableModel().getCollectionModel();   
        JUCtrlHierBinding treeBinding = (JUCtrlHierBinding) collectionModel.getWrappedData();
        RowKeySet rks = (RowKeySet) returnPopupEvent.getReturnValue();
        List tableRowKey = (List) rks.iterator().next(); 
        DCIteratorBinding dciter = treeBinding.getDCIteratorBinding(); 
        Key key = (Key) tableRowKey.get(0); 
        Row rw =  dciter.findRowByKeyString(key.toStringFormat(true));
       String rwDesc = (String)rw.getAttribute("SetBrandDesc");
        
        // Set produk description 
        BindingContext bctx = BindingContext.getCurrent();
        DCBindingContainer binding =
            (DCBindingContainer)bctx.getCurrentBindingsEntry();
        DCIteratorBinding iterProduk =
            binding.findIteratorBinding("PromoProdukView1Iterator");
        ViewObject voPromoProduk = iterProduk.getViewObject();
        Row rowSelected = voPromoProduk.getCurrentRow();
        String prodBrand = (String)rowSelected.getAttribute("ProductBrand");
        if (prodBrand == null) {
            rowSelected.setAttribute("ProductBrandDesc", null);
            rowSelected.setAttribute("ProductExtDesc", null);
            rowSelected.setAttribute("ProductPackDesc", null);
        } else {
            rowSelected.setAttribute("ProductBrandDesc", rwDesc);
            if (rwDesc.equalsIgnoreCase(varAll)) {
                rowSelected.setAttribute("ProductExtDesc", varAll);
                rowSelected.setAttribute("ProductPackDesc", varAll);
            } else {
                rowSelected.setAttribute("ProductExtDesc", null);
                rowSelected.setAttribute("ProductPackDesc", null);
            }
        }
        
        BindingContainer bindings = getBindings();
        OperationBinding operationBinding =
            bindings.getOperationBinding("Commit");
        operationBinding.execute();
        
        iterProduk.executeQuery();
        iterProduk.setCurrentRowWithKey(rowSelected.getKey().toStringFormat(true));
    }

    public void prodExtReturnPopupListener(ReturnPopupEvent returnPopupEvent) {      
        // Get produk extention description
        RichInputListOfValues lovField = (RichInputListOfValues)returnPopupEvent.getSource(); 
        ListOfValuesModel lovModel =  lovField.getModel(); 
        CollectionModel collectionModel = lovModel.getTableModel().getCollectionModel();   
        JUCtrlHierBinding treeBinding = (JUCtrlHierBinding) collectionModel.getWrappedData();
        RowKeySet rks = (RowKeySet) returnPopupEvent.getReturnValue();
        List tableRowKey = (List) rks.iterator().next(); 
        DCIteratorBinding dciter = treeBinding.getDCIteratorBinding(); 
        Key key = (Key) tableRowKey.get(0); 
        Row rw =  dciter.findRowByKeyString(key.toStringFormat(true));
       String rwDesc = (String)rw.getAttribute("SetExtDesc");
        
        // Set produk description 
        BindingContext bctx = BindingContext.getCurrent();
        DCBindingContainer binding =
            (DCBindingContainer)bctx.getCurrentBindingsEntry();
        DCIteratorBinding iterProduk =
            binding.findIteratorBinding("PromoProdukView1Iterator");
        ViewObject voPromoProduk = iterProduk.getViewObject();
        Row rowSelected = voPromoProduk.getCurrentRow();
        String prodExt = (String)rowSelected.getAttribute("ProductExt");
        if (prodExt == null) {
            rowSelected.setAttribute("ProductExtDesc", null);
            rowSelected.setAttribute("ProductPackDesc", null);
        } else {
            rowSelected.setAttribute("ProductExtDesc", rwDesc);
            if (rwDesc.equalsIgnoreCase(varAll)) {
                rowSelected.setAttribute("ProductPackDesc", varAll);
            } else {
                rowSelected.setAttribute("ProductPackDesc", null);
            }
        }
        
        BindingContainer bindings = getBindings();
        OperationBinding operationBinding =
            bindings.getOperationBinding("Commit");
        operationBinding.execute();
        
        iterProduk.executeQuery();
        iterProduk.setCurrentRowWithKey(rowSelected.getKey().toStringFormat(true));
    }

    public void prodPackReturnPopupListener(ReturnPopupEvent returnPopupEvent) {  
        // Get produk packaging description
        RichInputListOfValues lovField = (RichInputListOfValues)returnPopupEvent.getSource(); 
        ListOfValuesModel lovModel =  lovField.getModel(); 
        CollectionModel collectionModel = lovModel.getTableModel().getCollectionModel();   
        JUCtrlHierBinding treeBinding = (JUCtrlHierBinding) collectionModel.getWrappedData();
        RowKeySet rks = (RowKeySet) returnPopupEvent.getReturnValue();
        List tableRowKey = (List) rks.iterator().next(); 
        DCIteratorBinding dciter = treeBinding.getDCIteratorBinding(); 
        Key key = (Key) tableRowKey.get(0); 
        Row rw =  dciter.findRowByKeyString(key.toStringFormat(true));
       String rwDesc = (String)rw.getAttribute("SetPackagingDesc");
        
        // Set produk description 
        BindingContext bctx = BindingContext.getCurrent();
        DCBindingContainer binding =
            (DCBindingContainer)bctx.getCurrentBindingsEntry();
        DCIteratorBinding iterProduk =
            binding.findIteratorBinding("PromoProdukView1Iterator");
        ViewObject voPromoProduk = iterProduk.getViewObject();
        Row rowSelected = voPromoProduk.getCurrentRow();
        String prodPack = (String)rowSelected.getAttribute("ProductPack");
        if (prodPack == null) {
            rowSelected.setAttribute("ProductPackDesc", null);
        } else {
            rowSelected.setAttribute("ProductPackDesc", rwDesc);
        }
        
        BindingContainer bindings = getBindings();
        OperationBinding operationBinding =
            bindings.getOperationBinding("Commit");
        operationBinding.execute();
        
        iterProduk.executeQuery();
        iterProduk.setCurrentRowWithKey(rowSelected.getKey().toStringFormat(true));
    }

    public void paketFlagReturnPopupListener(ReturnPopupEvent returnPopupEvent) {
        BindingContext bctx = BindingContext.getCurrent();
        DCBindingContainer binding =
            (DCBindingContainer)bctx.getCurrentBindingsEntry();
        DCIteratorBinding iterProduk =
            binding.findIteratorBinding("PromoProdukView1Iterator");
        ViewObject voPromoProduk = iterProduk.getViewObject();
        Row rowSelected = voPromoProduk.getCurrentRow();
        
        BindingContainer bindings = getBindings();
        OperationBinding operationBinding =
            bindings.getOperationBinding("Commit");
        operationBinding.execute();
        
        iterProduk.executeQuery();
        iterProduk.setCurrentRowWithKey(rowSelected.getKey().toStringFormat(true));
    }

    public void clearVariantItem(String PromoProdukIdSel) {        
        BindingContainer binding = getBindings();
        //Remove whole produk variant
        DCIteratorBinding iterVariant =
            (DCIteratorBinding)binding.get("ProdukVariantView1Iterator");              
        ProdukVariantViewImpl prodVariantVo = (ProdukVariantViewImpl)iterVariant.getViewObject();
        prodVariantVo.setNamedWhereClauseParam("ppid", PromoProdukIdSel);
        prodVariantVo.executeQuery();
        if (prodVariantVo.getEstimatedRowCount() > 0) {
            while (prodVariantVo.hasNext()) {
                Row r = prodVariantVo.next();                        
                //System.out.println("REM**PROMO PROD ID: " + r.getAttribute("PromoProdukId") + " ; VARIANT : " + r.getAttribute("ProdVariant"));
                r.remove();
            }
        }
        
        //Remove whole produk item
        DCIteratorBinding iterProdItem =
            (DCIteratorBinding)binding.get("ProdukItemView1Iterator");              
        ProdukItemViewImpl prodItemVo = (ProdukItemViewImpl)iterProdItem.getViewObject();
        prodItemVo.setNamedWhereClauseParam("ppid", PromoProdukIdSel);
        prodItemVo.executeQuery();
        //System.out.println("**RSI PROD ITEM: " + prodItemVo.getEstimatedRowCount()); 
        if (prodItemVo.getEstimatedRowCount() > 0) {
            while (prodItemVo.hasNext()) {
                Row r = prodItemVo.next();                        
                //System.out.println("REM**PROMO PROD ID: " + r.getAttribute("PromoProdukId") + " ; ITEM : " + r.getAttribute("ProdItem"));
                r.remove();
            }
        }
    }

    public void priceBasedValueBarangBonusChangeListener(ValueChangeEvent valueChangeEvent) {
        String priceBased =
            valueChangeEvent.getNewValue() == null ? "" : valueChangeEvent.getNewValue().toString();
        if (priceBased.equalsIgnoreCase("PRICELIST")) {
            itPricePromoBonus.setSubmittedValue(new Number(0));
            AdfFacesContext.getCurrentInstance().addPartialTarget(itPricePromoBonus);
        } else {
            itTargetHarga.setSubmittedValue(new Number(0));
            itQtyMulPrice.setSubmittedValue(new Number(0));
            itPricePromoBonus.setSubmittedValue(new Number(0));
            AdfFacesContext.getCurrentInstance().addPartialTarget(itTargetHarga);
            AdfFacesContext.getCurrentInstance().addPartialTarget(itQtyMulPrice);
            AdfFacesContext.getCurrentInstance().addPartialTarget(itPricePromoBonus);
        }
    }

    public void inputpriceByLaunchPopupListener(LaunchPopupEvent launchPopupEvent) {
        String PromoBonusIdSel = "";
        String lovPriceBy = "";
        DCBindingContainer bindingsSelRow =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcItteratorBindings =
            bindingsSelRow.findIteratorBinding("PromoBonusView1Iterator");
        ViewObject voTableData = dcItteratorBindings.getViewObject();
        Row rowSelected = voTableData.getCurrentRow();
        if (rowSelected.getAttribute("PromoBonusId") != null) {
            PromoBonusIdSel =
                    rowSelected.getAttribute("PromoBonusId").toString();
            lovPriceBy = itlovPriceBasedPromoBarang.getValue().toString();
            ADFContext adfCtx = ADFContext.getCurrent();
            Map pageFlowScope = adfCtx.getPageFlowScope();
            Object ppId = pageFlowScope.put("PromoBonusId", PromoBonusIdSel);
            Object lovPricebyscope =
                pageFlowScope.put("lovPriceBy", lovPriceBy);
        }
    }

    public void inputPriceByReturnListerner(ReturnPopupEvent returnPopupEvent) {
        String PromoBonusIdSel = "";
        String promoProdId = "";
        DCBindingContainer bindingsSelRow =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcItteratorBindings =
            bindingsSelRow.findIteratorBinding("PromoBonusView1Iterator");
        ViewObject voTableData = dcItteratorBindings.getViewObject();
        Row rowSelected = voTableData.getCurrentRow();
        if (rowSelected.getAttribute("PromoBonusId") != null) {
            String lovPriceBy = "";
            ADFContext adfCtx = ADFContext.getCurrent();
            Map pageFlowScope = adfCtx.getPageFlowScope();
            Object ppId = pageFlowScope.get("PromoBonusId");
            Object lovPricebyscope = pageFlowScope.get("lovPriceBy");
            promoProdId = ppId.toString();
            PromoBonusIdSel =
                    rowSelected.getAttribute("PromoBonusId").toString();
            lovPriceBy = itlovPriceBasedPromoBarang.getValue().toString();
            if (PromoBonusIdSel.equalsIgnoreCase(promoProdId)) {
                if (lovPriceBy.equalsIgnoreCase("")) {
                    if (lovPricebyscope.toString().equalsIgnoreCase("MANUAL")) {
                        itlovPriceBasedPromoBarang.setSubmittedValue("MANUAL");
                        rowSelected.setAttribute("InputPriceBy", "MANUAL");
                        dcItteratorBindings.getDataControl().commitTransaction();
                        AdfFacesContext.getCurrentInstance().addPartialTarget(itlovPriceBasedPromoBarang);
                        AdfFacesContext.getCurrentInstance().addPartialTarget(btnBonusPriceList);

                    } else {
                        itlovPriceBasedPromoBarang.setSubmittedValue("PRICELIST");
                        rowSelected.setAttribute("InputPriceBy", "PRICELIST");
                        dcItteratorBindings.getDataControl().commitTransaction();
                        AdfFacesContext.getCurrentInstance().addPartialTarget(itlovPriceBasedPromoBarang);
                        AdfFacesContext.getCurrentInstance().addPartialTarget(itPricePromoBonus);
                        AdfFacesContext.getCurrentInstance().addPartialTarget(btnBonusPriceList);
                    }
                }
            }
        }
    }

    public void setItPricePromoBonus(RichInputText itPricePromoBonus) {
        this.itPricePromoBonus = itPricePromoBonus;
    }

    public RichInputText getItPricePromoBonus() {
        return itPricePromoBonus;
    }

    public void setItQtyMulPrice(RichInputText itQtyMulPrice) {
        this.itQtyMulPrice = itQtyMulPrice;
    }

    public RichInputText getItQtyMulPrice() {
        return itQtyMulPrice;
    }

    public void setItlovPriceBasedPromoBarang(RichInputListOfValues itlovPriceBasedPromoBarang) {
        this.itlovPriceBasedPromoBarang = itlovPriceBasedPromoBarang;
    }

    public RichInputListOfValues getItlovPriceBasedPromoBarang() {
        return itlovPriceBasedPromoBarang;
    }

    public void setBtnBonusPriceList(RichCommandButton btnBonusPriceList) {
        this.btnBonusPriceList = btnBonusPriceList;
    }

    public RichCommandButton getBtnBonusPriceList() {
        return btnBonusPriceList;
    }

    public void priceValValueChangeListener(ValueChangeEvent valueChangeEvent) {
        BigDecimal newPriceValValue =
            new BigDecimal(valueChangeEvent.getNewValue() == "" ? "0" :
                           valueChangeEvent.getNewValue() == null ? "0" :
                           valueChangeEvent.getNewValue().toString());
        Number qtyFrom = new Number(0);
        String PromoBonusIdSel = "";

        DCBindingContainer bindingsSelRow =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcItteratorBindings =
            bindingsSelRow.findIteratorBinding("PromoBonusView1Iterator");
        Key parentKey = dcItteratorBindings.getCurrentRow().getKey();
        ViewObject voTableData = dcItteratorBindings.getViewObject();
        Row rowSelected = voTableData.getCurrentRow();
        if (rowSelected.getAttribute("PromoBonusId") != null) {
            PromoBonusIdSel =
                    rowSelected.getAttribute("PromoBonusId").toString();
            qtyFrom =
                    (Number)rowSelected.getAttribute("QtyFrom") == null ? new Number(0) :
                    (Number)rowSelected.getAttribute("QtyFrom");

            BigDecimal totQtyBonus = new BigDecimal(qtyFrom.getValue());

            BigDecimal totalValue = newPriceValValue.multiply(totQtyBonus);
            oracle.jbo.domain.Number number = null;
            try {
                number =
                        new oracle.jbo.domain.Number(df2dgt.format(totalValue).toString());
            } catch (SQLException e) {
                JSFUtils.addFacesErrorMessage("Error",
                                              e.getLocalizedMessage());
            }
            rowSelected.setAttribute("QtyMulPrice", number);
            dcItteratorBindings.getDataControl().commitTransaction();
            dcItteratorBindings.setCurrentRowWithKey(parentKey.toStringFormat(true));
            itQtyMulPrice.setSubmittedValue(number);
            AdfFacesContext.getCurrentInstance().addPartialTarget(itQtyMulPrice);
        }
    }

    public void showPriceListInPromoBarangPopup(ActionEvent actionEvent) {
        RichQuery queryComp = this.qryPriceList;
        QueryModel queryModel = queryComp.getModel();
        QueryDescriptor queryDescriptor = queryComp.getValue();
        queryModel.reset(queryDescriptor);
        queryComp.refresh(FacesContext.getCurrentInstance());
        AdfFacesContext.getCurrentInstance().addPartialTarget(qryPriceList);
        RichPopup.PopupHints hints = new RichPopup.PopupHints();
        ppriceListpromoBarang.show(hints);
    }

    public void priceListPromoBonusPopupFetchListener(PopupFetchEvent popupFetchEvent) {
        BindingContainer bindings =
            BindingContext.getCurrent().getCurrentBindingsEntry();
        PromoProposalAMImpl promoProposalAM =
            (PromoProposalAMImpl)ADFUtils.getApplicationModuleForDataControl("PromoProposalAMDataControl");
        UserData userData =
            (UserData)JSFUtils.resolveExpression("#{UserData}");
        AttributeBinding promoStartDtAttr =
            (AttributeBinding)bindings.getControlBinding("PeriodeProgFrom");
        String promoStartDt = (String)promoStartDtAttr.getInputValue();
        AttributeBinding promoProdukIdAttr =
            (AttributeBinding)bindings.getControlBinding("PromoProdukId");
        DBSequence promoProdukId =
            (DBSequence)promoProdukIdAttr.getInputValue();

        String ProdPromCombFirst = "";
        String ProdPromCombEnd = "";
        String PromoBonusIdSel = "";
        String Uom = "";
        DCBindingContainer bindingsSelRow =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcItteratorBindings =
            bindingsSelRow.findIteratorBinding("PromoBonusView1Iterator");
        ViewObject voTableData = dcItteratorBindings.getViewObject();
        Row rowSelected = voTableData.getCurrentRow();
        if (rowSelected.getAttribute("PromoBonusId") != null) {
            if (rowSelected.getAttribute("ProductCategory") != null ||
                rowSelected.getAttribute("ProductClass") != null ||
                rowSelected.getAttribute("ProductBrand") != null ||
                rowSelected.getAttribute("ProductExt") != null ||
                rowSelected.getAttribute("ProductPack") != null) {
                PromoBonusIdSel =
                        rowSelected.getAttribute("PromoBonusId").toString();
                ProdPromCombFirst =
                        rowSelected.getAttribute("ProductCategory").toString() +
                        "." +
                        rowSelected.getAttribute("ProductClass").toString() +
                        "." +
                        rowSelected.getAttribute("ProductBrand").toString() +
                        "." +
                        rowSelected.getAttribute("ProductExt").toString() +
                        "." +
                        rowSelected.getAttribute("ProductPack").toString();
                Uom = rowSelected.getAttribute("Uom").toString();
            }
        }
        DCIteratorBinding dciterPromoVariant =
            ADFUtils.findIterator("PromoBonusVariantView1Iterator");
        long rowCountVar = dciterPromoVariant.getEstimatedRowCount();
        if (rowCountVar > 0) {
            String variantCode = "";
            StringBuilder sb = new StringBuilder();
            for (Row r : dciterPromoVariant.getAllRowsInRange()) {
                String promoBonusIdVar =
                    r.getAttribute("PromoBonusId").toString();
                if (PromoBonusIdSel.equalsIgnoreCase(promoBonusIdVar)) {
                    String varCode = r.getAttribute("ProdVariant").toString();
                    sb.append(varCode + ";");

                }
            }

            variantCode = sb.substring(0, sb.length() - 1);
            String variantEnd = variantCode.substring(0, 3);

            if (variantEnd.equalsIgnoreCase(varAll)) {
                DCIteratorBinding dciterPromBonusItem =
                    ADFUtils.findIterator("PromoBonusProdItemView1Iterator");

                long rowCountItem = dciterPromBonusItem.getEstimatedRowCount();
                if (rowCountItem > 0) {

                    StringBuilder sbItem = new StringBuilder();
                    for (Row rIt : dciterPromBonusItem.getAllRowsInRange()) {
                        String promoBonusIdIt =
                            rIt.getAttribute("PromoBonusId").toString();
                        if (PromoBonusIdSel.equalsIgnoreCase(promoBonusIdIt)) {
                            String itemCode =
                                rIt.getAttribute("ProdItem").toString();
                            sbItem.append(itemCode + ";");
                        }
                    }
                    ProdPromCombEnd = sbItem.substring(0, sbItem.length() - 1);

                } else {
                    /*
                    //                    ProdPromCombEnd=ProdPromCombFirst;
                    showPopup("\"Product Name\" pada kombinasi produk harus diisi apabila variant \"ALL\".", potmessage);
                    */
                    ProdPromCombEnd = ProdPromCombFirst + "." + variantEnd;
                }

            } else {
                ProdPromCombEnd = ProdPromCombFirst + "." + variantCode;
            }
        }

        CallableStatement cst = null;
        String loggedUserName = userData.getUserNameLogin();    
        /*
        System.out.println("\n================  PROMO BARANG  ==================\n" +
                "PROD COMB     : " + ProdPromCombEnd + "\n" +
                "PPID          : " + promoProdukId.getSequenceNumber() + "\n" +
                "USER NAME     : " + loggedUserName + "\n" +
                "UOM           : " + Uom + "\n" +
                "PROMO START DT: " + promoStartDt.toUpperCase() + "\n" +
                "=================================================");
        */
        try {
            cst =
promoProposalAM.getDBTransaction().createCallableStatement("BEGIN APPS.FCS_PPPC_GET_PRICE_LIST.INSERT_TABLE_PRICE_LIST('" +
                                                           ProdPromCombEnd +
                                                           "', '" +
                                                           promoProdukId.getSequenceNumber() +
                                                           "', '" +
                                                           loggedUserName +
                                                           "', '" + Uom +
                                                           "', '" +
                                                           promoStartDt.toUpperCase() +
                                                           "'); END;", 0);
            cst.executeUpdate();
        } catch (SQLException e) {
            JSFUtils.addFacesErrorMessage(e.getMessage());
        } finally {
            if (cst != null) {
                try {
                    cst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        OperationBinding operationBinding =
            bindings.getOperationBinding("ExecutePriceList");
        operationBinding.execute();
    }

    public void setPpriceListpromoBarang(RichPopup ppriceListpromoBarang) {
        this.ppriceListpromoBarang = ppriceListpromoBarang;
    }

    public RichPopup getPpriceListpromoBarang() {
        return ppriceListpromoBarang;
    }

    public void priceListpromoBonusDialogListener(DialogEvent dialogEvent) {
        if (dialogEvent.getOutcome().name().equals("ok")) {
            String PromoBonusIdSel = "";
            Number Qty = new Number(0);
            DCBindingContainer bindingsSelRow =
                (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
            DCIteratorBinding dcItteratorBindings =
                bindingsSelRow.findIteratorBinding("PromoBonusView1Iterator");
            ViewObject voTableData = dcItteratorBindings.getViewObject();
            Row rowSelected = voTableData.getCurrentRow();
            if (rowSelected.getAttribute("PromoBonusId") != null) {
                PromoBonusIdSel =
                        rowSelected.getAttribute("PromoBonusId").toString();
                Qty =
(Number)rowSelected.getAttribute("QtyFrom") == null ? new Number(0) :
(Number)rowSelected.getAttribute("QtyFrom");
            }
            DCIteratorBinding dciterPriceList =
                ADFUtils.findIterator("FcsPppcPriceListView1Iterator");
            Number priceVal = new Number(0);
            if (dciterPriceList.getEstimatedRowCount() > 0) {
                priceVal =
                        (Number)dciterPriceList.getCurrentRow().getAttribute("Price") ==
                        null ? new Number(0) :
                        (Number)dciterPriceList.getCurrentRow().getAttribute("Price");
            }
            BigDecimal endQty = new BigDecimal(Qty.getValue());
            BigDecimal tgtHarga = new BigDecimal(priceVal.getValue());

            BigDecimal totalValue = endQty.multiply(tgtHarga);
            oracle.jbo.domain.Number number = null;
            try {
                number =
                        new oracle.jbo.domain.Number(df2dgt.format(tgtHarga).toString());
            } catch (SQLException e) {
                JSFUtils.addFacesErrorMessage("Error",
                                              e.getLocalizedMessage());
            }

            oracle.jbo.domain.Number qtyMulPriceNum = null;
            try {
                qtyMulPriceNum =
                        new oracle.jbo.domain.Number(df2dgt.format(totalValue).toString());
            } catch (SQLException e) {
                JSFUtils.addFacesErrorMessage("Error",
                                              e.getLocalizedMessage());
            }

            String PromoBonusIdSeT = "";
            DCIteratorBinding dciterPromoBonusSet =
                ADFUtils.findIterator("PromoBonusView1Iterator");
            Row r = dciterPromoBonusSet.getCurrentRow();
            PromoBonusIdSeT = r.getAttribute("PromoBonusId").toString();
            if (PromoBonusIdSeT.equalsIgnoreCase(PromoBonusIdSel)) {
                r.setAttribute("PriceVal", number);
                r.setAttribute("QtyMulPrice", qtyMulPriceNum);
                itPricePromoBonus.setSubmittedValue(number);
                itQtyMulPrice.setSubmittedValue(qtyMulPriceNum);
                AdfFacesContext.getCurrentInstance().addPartialTarget(itPricePromoBonus);
                AdfFacesContext.getCurrentInstance().addPartialTarget(itQtyMulPrice);
                dciterPromoBonusSet.getDataControl().commitTransaction();
                dciterPromoBonusSet.executeQuery();
            }
        }
    }

    public void setQryPriceList(RichQuery qryPriceList) {
        this.qryPriceList = qryPriceList;
    }

    public RichQuery getQryPriceList() {
        return qryPriceList;
    }

    public void qtyToDiscEvent(ValueChangeEvent valueChangeEvent) {
        FacesContext fctx = FacesContext.getCurrentInstance();        
        DCBindingContainer bindingsSelRow =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcIterDisc =
            bindingsSelRow.findIteratorBinding("DiscountView1Iterator");
        Key discKey = dcIterDisc.getCurrentRow().getKey();
        ViewObject voDisc = dcIterDisc.getViewObject();
        Row rowSelected = voDisc.getCurrentRow();
        
        if (rowSelected.getAttribute("DiscountId") != null) {        
            Number qtyFrom = new Number(0);
            Number oldValue = (Number)valueChangeEvent.getOldValue();
            Number newValue = (Number)valueChangeEvent.getNewValue();
            qtyFrom =
                    (Number)rowSelected.getAttribute("QtyFrom") == null ? new Number(0) :
                    (Number)rowSelected.getAttribute("QtyFrom");
            
            if (qtyFrom.compareTo(newValue) > 0) {
                itqtyToDisc.setSubmittedValue(oldValue);
                rowSelected.setAttribute("QtyTo", oldValue);
                dcIterDisc.setCurrentRowWithKey(discKey.toStringFormat(true));
                AdfFacesContext.getCurrentInstance().addPartialTarget(itqtyToDisc);
                FacesMessage msg =
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Nilai \"Qty To\" harus lebih besar dari \"Qty From\".",
                                     "Nilai \"Qty To\" harus lebih besar dari \"Qty From\".");
                fctx.addMessage(null, msg);
            } 
        }
    }

    public void setItqtyToDisc(RichInputText itqtyToDisc) {
        this.itqtyToDisc = itqtyToDisc;
    }

    public RichInputText getItqtyToDisc() {
        return itqtyToDisc;
    }

    public void totQtyBonusValueChangeListener(ValueChangeEvent valueChangeEvent) {
        BigDecimal newTotQtyBonusValue =
            new BigDecimal(valueChangeEvent.getNewValue() == "" ? "0" :
                           valueChangeEvent.getNewValue() == null ? "0" :
                           valueChangeEvent.getNewValue().toString());
        Number priceVal = new Number(0);
        String PromoBonusIdSel = "";

        DCBindingContainer bindingsSelRow =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcItteratorBindings =
            bindingsSelRow.findIteratorBinding("PromoBonusView1Iterator");
        Key parentKey = dcItteratorBindings.getCurrentRow().getKey();
        ViewObject voTableData = dcItteratorBindings.getViewObject();
        Row rowSelected = voTableData.getCurrentRow();
        if (rowSelected.getAttribute("PromoBonusId") != null) {
            PromoBonusIdSel =
                    rowSelected.getAttribute("PromoBonusId").toString();
            priceVal =
                    (Number)rowSelected.getAttribute("PriceVal") == null ? new Number(0) :
                    (Number)rowSelected.getAttribute("PriceVal");

            BigDecimal tgtHarga = new BigDecimal(priceVal.getValue());

            BigDecimal totalValue = newTotQtyBonusValue.multiply(tgtHarga);
            oracle.jbo.domain.Number number = null;
            try {
                number =
                        new oracle.jbo.domain.Number(df2dgt.format(totalValue).toString());
            } catch (SQLException e) {
                JSFUtils.addFacesErrorMessage("Error",
                                              e.getLocalizedMessage());
            }
            rowSelected.setAttribute("QtyMulPrice", number);
            dcItteratorBindings.getDataControl().commitTransaction();
            dcItteratorBindings.setCurrentRowWithKey(parentKey.toStringFormat(true));
            itQtyMulPrice.setSubmittedValue(number);
            AdfFacesContext.getCurrentInstance().addPartialTarget(itQtyMulPrice);
        }
    }

    public void setItValidComb(RichInputText itValidComb) {
        this.itValidComb = itValidComb;
    }

    public RichInputText getItValidComb() {
        return itValidComb;
    }

    public void changeKelipatan(ValueChangeEvent valueKelipatanEvent) {
        String socType = socTypePotongan.getValue().toString();
        String socTipeHitung = lovTipePerhitungan.getValue().toString();        
        String discOntopStr = rowOntop.getValue() == null ? "0" :
                           rowOntop.getValue() == "" ? "0" :
                           rowOntop.getValue().toString().replaceAll(",","");
        BigDecimal discOntop =
            new BigDecimal(discOntopStr) == null ? new BigDecimal(0) :
            new BigDecimal(discOntopStr);
        
        String discMfStr = rowMf.getValue() == null ? "0" :
                           rowMf.getValue() == "" ? "0" :
                           rowMf.getValue().toString().replaceAll(",","");
        BigDecimal discMf =
            new BigDecimal(discMfStr) == null ? new BigDecimal(0) :
            new BigDecimal(discMfStr);
        
        DCIteratorBinding dciterTarget =
            ADFUtils.findIterator("TargetView1Iterator");
        Row row = dciterTarget.getCurrentRow();
        BigDecimal ontop = BigDecimal.ZERO;
        BigDecimal mf = BigDecimal.ZERO;
        BigDecimal RasioOntop = BigDecimal.ZERO;
        BigDecimal RasioMf = BigDecimal.ZERO;
        BigDecimal rasioTotal = BigDecimal.ZERO;
        BigDecimal rasioT = BigDecimal.ZERO;
        Number qty =
            (Number)row.getAttribute("Qty") == null ? new Number(0) : (Number)row.getAttribute("Qty");
        Number value =
            (Number)row.getAttribute("Value") == null ? new Number(0) :
            (Number)row.getAttribute("Value");
        Number price =
            (Number)row.getAttribute("Price") == null ? new Number(0) :
            (Number)row.getAttribute("Price");

        if (value.compareTo(zeroNum) < 0 ||
            value.compareTo(zeroNum) > 0) {

            if (socType.equalsIgnoreCase(tipePotonganPercent)) {
                if (socTipeHitung.equalsIgnoreCase(tipeHitungKelipatan)) {
                    // BY PERCENT - KELIPATAN
                    BigDecimal kelipatanVal =
                        new BigDecimal(valueKelipatanEvent.getNewValue() ==
                                       "" ? "0" :
                                       valueKelipatanEvent.getNewValue() ==
                                       null ? "0" :
                                       valueKelipatanEvent.getNewValue().toString());
                    
                    //!!! CALCULATE ON TOP BY PERCENT
                    /*DEBUG
                    System.out.println("=====================================");
                    System.out.println("QTY            : " + qty.getBigDecimalValue());
                    System.out.println("KELIPATAN      : " + kelipatanVal);
                    System.out.println("PERCENT OT DISC: " + discOntop);
                    System.out.println("PRICE          : " + price.getBigDecimalValue());
                    System.out.println("-------------------------------------");
                    */
                    BigDecimal roundTgtDivKelipatanOT = qty.getBigDecimalValue().divide(kelipatanVal,0,RoundingMode.DOWN);
                    BigDecimal priceMulPercentDiscOT = price.getBigDecimalValue().multiply(discOntop.divide(new BigDecimal(100)));
                    ontop = (roundTgtDivKelipatanOT.multiply(kelipatanVal)).multiply(priceMulPercentDiscOT);
                    /*DEBUG
                    System.out.println("QTY X KEL ROUND : " + roundTgtDivKelipatanOT);                                                             
                    System.out.println("PRICE PERCENT   : " + priceMulPercentDiscOT);
                    System.out.println("-------------------------------------");
                    System.out.println("VAL ONTOP AMOUNT: " + ontop);
                    System.out.println("=====================================");
                    */
                    otOnTop.setSubmittedValue(ontop);
                    RasioOntop =
                            ontop.divide(value.getBigDecimalValue(), MathContext.DECIMAL128).multiply(bdHundred).setScale(2,
                                                                                                                          RoundingMode.HALF_UP);
                    String rasOntop = RasioOntop.toString();
                    otRasioOntop.setSubmittedValue(rasOntop);
                    String rasio =
                        otRasioMf.getValue() == "" ? "0" : otRasioMf.getValue() ==
                                                           null ? "0" :
                                                           otRasioMf.getValue().toString();
                    rasioT =
                            new BigDecimal(rasio.toString().replaceAll(" ", "").replaceAll("%",
                                                                                           ""));
                    rasioTotal = RasioOntop.add(rasioT);
                    String total = rasioTotal.toString();
                    otRasioTotal.setSubmittedValue(total);
                    DCIteratorBinding dciterPromoProduk =
                        ADFUtils.findIterator("PromoProdukView1Iterator");
                    Row r = dciterPromoProduk.getCurrentRow();
                    r.setAttribute("DiscOnTop", ontop);
                    r.setAttribute("DiscRasioOnTop", rasOntop);
                    r.setAttribute("DiscRasioTotal1", total);
                    
                    //!!! CALCULATE MF BY PERCENT   
                    /*DEBUG
                    System.out.println("=====================================");
                    System.out.println("QTY            : " + qty.getBigDecimalValue());
                    System.out.println("KELIPATAN      : " + kelipatanVal);
                    System.out.println("PERCENT MF DISC: " + discMf);
                    System.out.println("PRICE          : " + price.getBigDecimalValue());
                    System.out.println("-------------------------------------");
                    */
                    BigDecimal roundTgtDivKelipatanMF = qty.getBigDecimalValue().divide(kelipatanVal,0,RoundingMode.DOWN);
                    BigDecimal priceMulPercentDiscMF = price.getBigDecimalValue().multiply(discMf.divide(new BigDecimal(100)));
                    mf = (roundTgtDivKelipatanMF.multiply(kelipatanVal)).multiply(priceMulPercentDiscMF);
                    /*DEBUG
                    System.out.println("QTY X KEL ROUND : " + roundTgtDivKelipatanMF);                                                             
                    System.out.println("PRICE PERCENT   : " + priceMulPercentDiscMF);
                    System.out.println("-------------------------------------");
                    System.out.println("VAL MF AMOUNT: " + mf);
                    System.out.println("=====================================");
                    */
                    otOnTop.setSubmittedValue(mf);
                    otMF.setSubmittedValue(mf);
                    RasioMf =
                            mf.divide(value.getBigDecimalValue(), MathContext.DECIMAL128).multiply(bdHundred).setScale(2,
                                                                                                                       RoundingMode.HALF_UP);
                    String rasMf = RasioMf.toString();
                    otRasioMf.setSubmittedValue(rasMf);
                    rasioT =
                            new BigDecimal(otRasioOntop.getValue().toString().replaceAll(" ",
                                                                                         "").replaceAll("%",
                                                                                                        ""));
                    rasioTotal = RasioMf.add(rasioT);
                    String Total = rasioTotal.toString();
                    otRasioTotal.setSubmittedValue(Total);
                    
                    r.setAttribute("DiscMf", mf);
                    r.setAttribute("DiscRasioMf", rasMf);
                    r.setAttribute("DiscRasioTotal1", Total);
                    
                    // dciterPromoProduk.getDataControl().commitTransaction();
                }

                AdfFacesContext.getCurrentInstance().addPartialTarget(otOnTop);
                AdfFacesContext.getCurrentInstance().addPartialTarget(otRasioOntop);
                AdfFacesContext.getCurrentInstance().addPartialTarget(otRasioTotal);                    
                AdfFacesContext.getCurrentInstance().addPartialTarget(otMF);
                AdfFacesContext.getCurrentInstance().addPartialTarget(otRasioMf);
                AdfFacesContext.getCurrentInstance().addPartialTarget(otRasioTotal);
            } else {
                if (socTipeHitung.equalsIgnoreCase(tipeHitungKelipatan)) {
                    // BY AMOUNT - KELIPATAN
                    BigDecimal kelipatanVal =
                        new BigDecimal(valueKelipatanEvent.getNewValue() ==
                                       "" ? "0" :
                                       valueKelipatanEvent.getNewValue() ==
                                       null ? "0" :
                                       valueKelipatanEvent.getNewValue().toString());
                    
                    //!!! CALCULATE ON TOP BY AMOUNT
                    /*DEBUG
                    System.out.println("=====================================");
                    System.out.println("QTY            : " + qty.getBigDecimalValue());
                    System.out.println("KELIPATAN      : " + kelipatanVal);
                    System.out.println("AMOUNT OT DISC : " + discOntop);
                    System.out.println("-------------------------------------");
                    */
                    try {
                        BigDecimal roundTgtDivKelipatanOT =
                            qty.getBigDecimalValue().divide(kelipatanVal, 0,
                                                            RoundingMode.DOWN);
                        ontop =
                                (roundTgtDivKelipatanOT.multiply(kelipatanVal)).multiply(discOntop);
                    } catch (java.lang.ArithmeticException ae) {
                        JSFUtils.addFacesWarningMessage("Nilai kelipatan harus diisi.");
                    } catch (Exception e) {
                        JSFUtils.addFacesErrorMessage("Error", e.getLocalizedMessage());
                    }
                    /*DEBUG
                    System.out.println("QTY X KEL ROUND : " + roundTgtDivKelipatanOT);
                    System.out.println("-------------------------------------");
                    System.out.println("VAL ONTOP AMOUNT: " + ontop);
                    System.out.println("=====================================");
                    */
                    otOnTop.setSubmittedValue(ontop);
                    RasioOntop =
                            ontop.divide(value.getBigDecimalValue(), MathContext.DECIMAL128).multiply(bdHundred).setScale(2,
                                                                                                                          RoundingMode.HALF_UP);
                    String rasOntop = RasioOntop.toString();
                    otRasioOntop.setSubmittedValue(rasOntop);
                    String rasio =
                        otRasioMf.getValue() == "" ? "0" : otRasioMf.getValue() ==
                                                           null ? "0" :
                                                           otRasioMf.getValue().toString();
                    rasioT =
                            new BigDecimal(rasio.toString().replaceAll(" ", "").replaceAll("%",
                                                                                           ""));
                    rasioTotal = RasioOntop.add(rasioT);
                    String total = rasioTotal.toString();
                    otRasioTotal.setSubmittedValue(total);
                    
                    //!!! CALCULATE MF BY AMOUNT
                    /*DEBUG
                    System.out.println("=====================================");
                    System.out.println("QTY            : " + qty.getBigDecimalValue());
                    System.out.println("KELIPATAN      : " + kelipatanVal);
                    System.out.println("AMOUNT MF DISC : " + discMf);
                    System.out.println("PRICE          : " + price.getBigDecimalValue());
                    System.out.println("-------------------------------------");
                    */
                    BigDecimal roundTgtDivKelipatan = qty.getBigDecimalValue().divide(kelipatanVal,0,RoundingMode.DOWN);
                    mf = (roundTgtDivKelipatan.multiply(kelipatanVal)).multiply(discMf);
                    /*DEBUG
                    System.out.println("QTY X KEL ROUND : " + roundTgtDivKelipatan); 
                    System.out.println("-------------------------------------");
                    System.out.println("VAL MF AMOUNT   : " + mf);
                    System.out.println("=====================================");
                    */
                    otMF.setSubmittedValue(mf);
                    RasioMf =
                            mf.divide(value.getBigDecimalValue(), MathContext.DECIMAL128).multiply(bdHundred).setScale(2,
                                                                                                                       RoundingMode.HALF_UP);
                    //                    mf.divide(value.getBigDecimalValue(), 3, RoundingMode.HALF_UP).multiply(bdHundred).setScale(2,RoundingMode.HALF_UP);
                    String rasMf = RasioMf.toString();
                    otRasioMf.setSubmittedValue(rasMf);
                    rasioT =
                            new BigDecimal(otRasioOntop.getValue().toString().replaceAll(" ",
                                                                                         "").replaceAll("%",
                                                                                                        ""));
                    rasioTotal = RasioMf.add(rasioT);
                    String Total = rasioTotal.toString();
                    otRasioTotal.setSubmittedValue(Total);

                    DCIteratorBinding dciterPromoProduk =
                        ADFUtils.findIterator("PromoProdukView1Iterator");
                    Row r = dciterPromoProduk.getCurrentRow();
                    r.setAttribute("DiscOnTop", ontop);
                    r.setAttribute("DiscRasioOnTop", rasOntop);
                    r.setAttribute("DiscRasioTotal1", total);
                    r.setAttribute("DiscMf", mf);
                    r.setAttribute("DiscRasioMf", rasMf);
                    r.setAttribute("DiscRasioTotal1", Total);
                    // dciterPromoProduk.getDataControl().commitTransaction();
                }

                AdfFacesContext.getCurrentInstance().addPartialTarget(otOnTop);
                AdfFacesContext.getCurrentInstance().addPartialTarget(otRasioOntop);                  
                AdfFacesContext.getCurrentInstance().addPartialTarget(otMF);
                AdfFacesContext.getCurrentInstance().addPartialTarget(otRasioMf);
                AdfFacesContext.getCurrentInstance().addPartialTarget(otRasioTotal);
            }
        } else {
            JSFUtils.addFacesWarningMessage("Nilai \"Qty. Target\", \"Harga\"  dan nilai \"Tdk Potong Budget / On Top\" tidak boleh 0 (nol) atau kosong.");
        }
    }

    public void bonusCategoryReturnPopupListener(ReturnPopupEvent returnPopupEvent) {
        // Get produk category description
        RichInputListOfValues lovField = (RichInputListOfValues)returnPopupEvent.getSource(); 
        ListOfValuesModel lovModel =  lovField.getModel(); 
        CollectionModel collectionModel = lovModel.getTableModel().getCollectionModel();   
        JUCtrlHierBinding treeBinding = (JUCtrlHierBinding) collectionModel.getWrappedData();
        RowKeySet rks = (RowKeySet) returnPopupEvent.getReturnValue();
        List tableRowKey = (List) rks.iterator().next(); 
        DCIteratorBinding dciter = treeBinding.getDCIteratorBinding(); 
        Key key = (Key) tableRowKey.get(0); 
        Row rw =  dciter.findRowByKeyString(key.toStringFormat(true));
        String rwDesc = (String)rw.getAttribute("SetCategoryDesc");
        
        // Set produk description 
        BindingContext bctx = BindingContext.getCurrent();
        DCBindingContainer binding =
            (DCBindingContainer)bctx.getCurrentBindingsEntry();
        DCIteratorBinding iterBonus =
            binding.findIteratorBinding("PromoBonusView1Iterator");
        ViewObject voPromoBonus = iterBonus.getViewObject();
        Row rowSelected = voPromoBonus.getCurrentRow();
        String prodCategory = (String)rowSelected.getAttribute("ProductCategory");
        
        if (prodCategory == null) {
            rowSelected.setAttribute("ProductCategoryDesc", null);
            rowSelected.setAttribute("ProductClassDesc", null);
            rowSelected.setAttribute("ProductBrandDesc", null);
            rowSelected.setAttribute("ProductExtDesc", null);
            rowSelected.setAttribute("ProductPackDesc", null);
        } else {
            rowSelected.setAttribute("ProductCategoryDesc", rwDesc);
            if (rwDesc.equalsIgnoreCase(varAll)) {
                rowSelected.setAttribute("ProductClassDesc", varAll);
                rowSelected.setAttribute("ProductBrandDesc", varAll);
                rowSelected.setAttribute("ProductExtDesc", varAll);
                rowSelected.setAttribute("ProductPackDesc", varAll);
            } else {
                rowSelected.setAttribute("ProductClassDesc", null);
                rowSelected.setAttribute("ProductBrandDesc", null);
                rowSelected.setAttribute("ProductExtDesc", null);
                rowSelected.setAttribute("ProductPackDesc", null);
            }
        }
        
        BindingContainer bindings = getBindings();
        OperationBinding operationBinding =
            bindings.getOperationBinding("Commit");
        operationBinding.execute();
        
        iterBonus.executeQuery();
        iterBonus.setCurrentRowWithKey(rowSelected.getKey().toStringFormat(true));
    }

    public void bonusClassReturnPopupListener(ReturnPopupEvent returnPopupEvent) {
        // Get produk class description
        RichInputListOfValues lovField = (RichInputListOfValues)returnPopupEvent.getSource(); 
        ListOfValuesModel lovModel =  lovField.getModel(); 
        CollectionModel collectionModel = lovModel.getTableModel().getCollectionModel();   
        JUCtrlHierBinding treeBinding = (JUCtrlHierBinding) collectionModel.getWrappedData();
        RowKeySet rks = (RowKeySet) returnPopupEvent.getReturnValue();
        List tableRowKey = (List) rks.iterator().next(); 
        DCIteratorBinding dciter = treeBinding.getDCIteratorBinding(); 
        Key key = (Key) tableRowKey.get(0); 
        Row rw =  dciter.findRowByKeyString(key.toStringFormat(true));
        String rwDesc = (String)rw.getAttribute("SetClassDesc");
        
        // Set produk description 
        BindingContext bctx = BindingContext.getCurrent();
        DCBindingContainer binding =
            (DCBindingContainer)bctx.getCurrentBindingsEntry();
        DCIteratorBinding iterBonus =
            binding.findIteratorBinding("PromoBonusView1Iterator");
        ViewObject voPromoBonus = iterBonus.getViewObject();
        Row rowSelected = voPromoBonus.getCurrentRow();
        String prodClass = (String)rowSelected.getAttribute("ProductClass");
        
        if (prodClass == null) {
            rowSelected.setAttribute("ProductClassDesc", null);
            rowSelected.setAttribute("ProductBrandDesc", null);
            rowSelected.setAttribute("ProductExtDesc", null);
            rowSelected.setAttribute("ProductPackDesc", null);
        } else {
            rowSelected.setAttribute("ProductClassDesc", rwDesc);
            if (rwDesc.equalsIgnoreCase(varAll)) {
                rowSelected.setAttribute("ProductBrandDesc", varAll);
                rowSelected.setAttribute("ProductExtDesc", varAll);
                rowSelected.setAttribute("ProductPackDesc", varAll);
            } else {
                rowSelected.setAttribute("ProductBrandDesc", null);
                rowSelected.setAttribute("ProductExtDesc", null);
                rowSelected.setAttribute("ProductPackDesc", null);
            }
        }
        
        BindingContainer bindings = getBindings();
        OperationBinding operationBinding =
            bindings.getOperationBinding("Commit");
        operationBinding.execute();
        
        iterBonus.executeQuery();
        iterBonus.setCurrentRowWithKey(rowSelected.getKey().toStringFormat(true));
    }

    public void bonusBrandReturnPopupListener(ReturnPopupEvent returnPopupEvent) {
        // Get produk brand description
        RichInputListOfValues lovField = (RichInputListOfValues)returnPopupEvent.getSource(); 
        ListOfValuesModel lovModel =  lovField.getModel(); 
        CollectionModel collectionModel = lovModel.getTableModel().getCollectionModel();   
        JUCtrlHierBinding treeBinding = (JUCtrlHierBinding) collectionModel.getWrappedData();
        RowKeySet rks = (RowKeySet) returnPopupEvent.getReturnValue();
        List tableRowKey = (List) rks.iterator().next(); 
        DCIteratorBinding dciter = treeBinding.getDCIteratorBinding(); 
        Key key = (Key) tableRowKey.get(0); 
        Row rw =  dciter.findRowByKeyString(key.toStringFormat(true));
        String rwDesc = (String)rw.getAttribute("SetBrandDesc");
        
        // Set produk description 
        BindingContext bctx = BindingContext.getCurrent();
        DCBindingContainer binding =
            (DCBindingContainer)bctx.getCurrentBindingsEntry();
        DCIteratorBinding iterBonus =
            binding.findIteratorBinding("PromoBonusView1Iterator");
        ViewObject voPromoBonus = iterBonus.getViewObject();
        Row rowSelected = voPromoBonus.getCurrentRow();
        String prodBrand = (String)rowSelected.getAttribute("ProductBrand");
        if (prodBrand == null) {
            rowSelected.setAttribute("ProductBrandDesc", null);
            rowSelected.setAttribute("ProductExtDesc", null);
            rowSelected.setAttribute("ProductPackDesc", null);
        } else {
            rowSelected.setAttribute("ProductBrandDesc", rwDesc);
            if (rwDesc.equalsIgnoreCase(varAll)) {
                rowSelected.setAttribute("ProductExtDesc", varAll);
                rowSelected.setAttribute("ProductPackDesc", varAll);
            } else {
                rowSelected.setAttribute("ProductExtDesc", null);
                rowSelected.setAttribute("ProductPackDesc", null);
            }
        }
        
        BindingContainer bindings = getBindings();
        OperationBinding operationBinding =
            bindings.getOperationBinding("Commit");
        operationBinding.execute();
        
        iterBonus.executeQuery();
        iterBonus.setCurrentRowWithKey(rowSelected.getKey().toStringFormat(true));
    }

    public void bonusExtReturnPopupListener(ReturnPopupEvent returnPopupEvent) {    
        // Get produk extention description
        RichInputListOfValues lovField = (RichInputListOfValues)returnPopupEvent.getSource(); 
        ListOfValuesModel lovModel =  lovField.getModel(); 
        CollectionModel collectionModel = lovModel.getTableModel().getCollectionModel();   
        JUCtrlHierBinding treeBinding = (JUCtrlHierBinding) collectionModel.getWrappedData();
        RowKeySet rks = (RowKeySet) returnPopupEvent.getReturnValue();
        List tableRowKey = (List) rks.iterator().next(); 
        DCIteratorBinding dciter = treeBinding.getDCIteratorBinding(); 
        Key key = (Key) tableRowKey.get(0); 
        Row rw =  dciter.findRowByKeyString(key.toStringFormat(true));
       String rwDesc = (String)rw.getAttribute("SetExtDesc");
        
        // Set produk description 
        BindingContext bctx = BindingContext.getCurrent();
        DCBindingContainer binding =
            (DCBindingContainer)bctx.getCurrentBindingsEntry();
        DCIteratorBinding iterBonus =
            binding.findIteratorBinding("PromoBonusView1Iterator");
        ViewObject voPromoBonus = iterBonus.getViewObject();
        Row rowSelected = voPromoBonus.getCurrentRow();
        String prodExt = (String)rowSelected.getAttribute("ProductExt");
        if (prodExt == null) {
            rowSelected.setAttribute("ProductExtDesc", null);
            rowSelected.setAttribute("ProductPackDesc", null);
        } else {
            rowSelected.setAttribute("ProductExtDesc", rwDesc);
            if (rwDesc.equalsIgnoreCase(varAll)) {
                rowSelected.setAttribute("ProductPackDesc", varAll);
            } else {
                rowSelected.setAttribute("ProductPackDesc", null);
            }
        }
        
        BindingContainer bindings = getBindings();
        OperationBinding operationBinding =
            bindings.getOperationBinding("Commit");
        operationBinding.execute();
        
        iterBonus.executeQuery();
        iterBonus.setCurrentRowWithKey(rowSelected.getKey().toStringFormat(true));
    }

    public void bonusPackReturnPopupListener(ReturnPopupEvent returnPopupEvent) {   
        // Get produk extention description
        RichInputListOfValues lovField = (RichInputListOfValues)returnPopupEvent.getSource(); 
        ListOfValuesModel lovModel =  lovField.getModel(); 
        CollectionModel collectionModel = lovModel.getTableModel().getCollectionModel();   
        JUCtrlHierBinding treeBinding = (JUCtrlHierBinding) collectionModel.getWrappedData();
        RowKeySet rks = (RowKeySet) returnPopupEvent.getReturnValue();
        List tableRowKey = (List) rks.iterator().next(); 
        DCIteratorBinding dciter = treeBinding.getDCIteratorBinding(); 
        Key key = (Key) tableRowKey.get(0); 
        Row rw =  dciter.findRowByKeyString(key.toStringFormat(true));
       String rwDesc = (String)rw.getAttribute("SetPackagingDesc");
        
        // Set produk description 
        BindingContext bctx = BindingContext.getCurrent();
        DCBindingContainer binding =
            (DCBindingContainer)bctx.getCurrentBindingsEntry();
        DCIteratorBinding iterBonus =
            binding.findIteratorBinding("PromoBonusView1Iterator");
        ViewObject voPromoBonus = iterBonus.getViewObject();
        Row rowSelected = voPromoBonus.getCurrentRow();
        String prodPack = (String)rowSelected.getAttribute("ProductPack");
        if (prodPack == null) {
            rowSelected.setAttribute("ProductPackDesc", null);
        } else {
            rowSelected.setAttribute("ProductPackDesc", rwDesc);
        }
        
        BindingContainer bindings = getBindings();
        OperationBinding operationBinding =
            bindings.getOperationBinding("Commit");
        operationBinding.execute();
        
        iterBonus.executeQuery();
        iterBonus.setCurrentRowWithKey(rowSelected.getKey().toStringFormat(true));
    }

    public void bonusVariantReturnPopupListener(ReturnPopupEvent returnPopupEvent) {
        BindingContext bctx = BindingContext.getCurrent();
        DCBindingContainer binding =
            (DCBindingContainer)bctx.getCurrentBindingsEntry();
        DCIteratorBinding iterBonus =
            binding.findIteratorBinding("PromoBonusView1Iterator");
        ViewObject voPromoBonus = iterBonus.getViewObject();
        Row rowSelected = voPromoBonus.getCurrentRow();
        
        BindingContainer bindings = getBindings();
        OperationBinding operationBinding =
            bindings.getOperationBinding("Commit");
        operationBinding.execute();
        
        iterBonus.executeQuery();
        iterBonus.setCurrentRowWithKey(rowSelected.getKey().toStringFormat(true));
    }

    public void bonusItemReturnPopupListener(ReturnPopupEvent returnPopupEvent) {
        BindingContext bctx = BindingContext.getCurrent();
        DCBindingContainer binding =
            (DCBindingContainer)bctx.getCurrentBindingsEntry();
        DCIteratorBinding iterBonus =
            binding.findIteratorBinding("PromoBonusView1Iterator");
        ViewObject voPromoBonus = iterBonus.getViewObject();
        Row rowSelected = voPromoBonus.getCurrentRow();
        
        BindingContainer bindings = getBindings();
        OperationBinding operationBinding =
            bindings.getOperationBinding("Commit");
        operationBinding.execute();
        
        iterBonus.executeQuery();
        iterBonus.setCurrentRowWithKey(rowSelected.getKey().toStringFormat(true));
    }


    public void biayaQtyValueChangeListener(ValueChangeEvent valueChangeEvent) {
        BigDecimal newBiayaQtyBonusValue =
            new BigDecimal(valueChangeEvent.getNewValue() == "" ? "0" :
                           valueChangeEvent.getNewValue() == null ? "0" :
                           valueChangeEvent.getNewValue().toString());
        Number priceVal = new Number(0);
        String BiayaIdSel = "";

        DCBindingContainer bindingsSelRow =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcItteratorBindings =
            bindingsSelRow.findIteratorBinding("BiayaView1Iterator");
        Key parentKey = dcItteratorBindings.getCurrentRow().getKey();
        ViewObject voTableData = dcItteratorBindings.getViewObject();
        Row rowSelected = voTableData.getCurrentRow();
        if (rowSelected.getAttribute("BiayaId") != null) {
            BiayaIdSel =
                    rowSelected.getAttribute("BiayaId").toString();
            priceVal =
                    (Number)rowSelected.getAttribute("BiayaPrice") == null ? new Number(0) :
                    (Number)rowSelected.getAttribute("BiayaPrice");

            BigDecimal tgtHarga = new BigDecimal(priceVal.getValue());

            BigDecimal totalValue = newBiayaQtyBonusValue.multiply(tgtHarga);
            oracle.jbo.domain.Number number = null;
            try {
                number =
                        new oracle.jbo.domain.Number(df2dgt.format(totalValue).toString());
            } catch (SQLException e) {
                JSFUtils.addFacesErrorMessage("Error",
                                              e.getLocalizedMessage());
            }
            rowSelected.setAttribute("BiayaTotAmt", number);
            //dcItteratorBindings.getDataControl().commitTransaction();
            dcItteratorBindings.setCurrentRowWithKey(parentKey.toStringFormat(true));
            itTotAmt.setSubmittedValue(number);
            AdfFacesContext.getCurrentInstance().addPartialTarget(itTotAmt);
        }
    }

    public void setItTotAmt(RichInputText itTotAmt) {
        this.itTotAmt = itTotAmt;
    }

    public RichInputText getItTotAmt() {
        return itTotAmt;
    }

    public void biayaPriceValueChangeListener(ValueChangeEvent valueChangeEvent) {
        BigDecimal newPriceValValue =
            new BigDecimal(valueChangeEvent.getNewValue() == "" ? "0" :
                           valueChangeEvent.getNewValue() == null ? "0" :
                           valueChangeEvent.getNewValue().toString());
        Number qtyBiaya = new Number(0);
        String BiayaIdSel = "";

        DCBindingContainer bindingsSelRow =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcItteratorBindings =
            bindingsSelRow.findIteratorBinding("BiayaView1Iterator");
        Key parentKey = dcItteratorBindings.getCurrentRow().getKey();
        ViewObject voTableData = dcItteratorBindings.getViewObject();
        Row rowSelected = voTableData.getCurrentRow();
        if (rowSelected.getAttribute("BiayaId") != null) {
            BiayaIdSel =
                    rowSelected.getAttribute("BiayaId").toString();
            qtyBiaya =
                    (Number)rowSelected.getAttribute("Qty") == null ? new Number(0) :
                    (Number)rowSelected.getAttribute("Qty");

            BigDecimal totQtyBiaya = new BigDecimal(qtyBiaya.getValue());

            BigDecimal totalValue = newPriceValValue.multiply(totQtyBiaya);
            oracle.jbo.domain.Number number = null;
            try {
                number =
                        new oracle.jbo.domain.Number(df2dgt.format(totalValue).toString());
            } catch (SQLException e) {
                JSFUtils.addFacesErrorMessage("Error",
                                              e.getLocalizedMessage());
            }
            rowSelected.setAttribute("TotAmt", number);
            //dcItteratorBindings.getDataControl().commitTransaction();
            dcItteratorBindings.setCurrentRowWithKey(parentKey.toStringFormat(true));
            itTotAmt.setSubmittedValue(number);
            AdfFacesContext.getCurrentInstance().addPartialTarget(itTotAmt);
        }
    }

    public void setItBiaPrice(RichInputText itBiaPrice) {
        this.itBiaPrice = itBiaPrice;
    }

    public RichInputText getItBiaPrice() {
        return itBiaPrice;
    }

    public void auditTreeRowDisclosureListener(RowDisclosureEvent rowDisclosureEvent) {
        RichTreeTable tree = (RichTreeTable)rowDisclosureEvent.getSource();
        RowKeySet rks = rowDisclosureEvent.getAddedSet();
        if (rks != null && rks.size() > 0) {
            Iterator iter = rks.iterator();

            while (iter.hasNext()) {
                Object rowKey = iter.next();
                tree.setRowKey(rowKey);
                JUCtrlHierNodeBinding rowData = (JUCtrlHierNodeBinding)tree.getRowData();
                if (rowData != null && rowData.getChildren() != null) { 
                    // Iterate through the children of the expanded node and check if they have children
                    for (Object child : rowData.getChildren()) {
                        JUCtrlHierNodeBinding childNode = (JUCtrlHierNodeBinding)child;
                        if (childNode.getChildren() == null || childNode.getChildren().size() == 0) { 
                            // Child node is a leaf.  Add it to the disclosed rows, to that the ADF tree will not display a disclosure icon
                            tree.getDisclosedRowKeys().add(childNode.getKeyPath());
                        }
                    }
                }
            }

        }
    }

    public void setSwitchButtonMain(UIXSwitcher switchButtonMain) {
        this.switchButtonMain = switchButtonMain;
    }

    public UIXSwitcher getSwitchButtonMain() {
        return switchButtonMain;
    }

    public void tableDoubleClick(ClientEvent clientEvent) {
        switchMain.setFacetName("dataavailable");
        switchButtonMain.setFacetName("dataavailable");
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
        AdfFacesContext.getCurrentInstance().addPartialTarget(switchButtonMain);
        /*
        ControllerContext controllerContext = ControllerContext.getInstance();
        ViewPortContext currentRootViewPort = controllerContext.getCurrentRootViewPort();
        boolean isDataDirty = currentRootViewPort.isDataDirty();
        if (true == isDataDirty) {
            RichPopup.PopupHints hints = new RichPopup.PopupHints();
            popupSaveChanges.show(hints);  
        }
        */
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

    public void onTableSelect(SelectionEvent selectionEvent) {        RowKeySet oldKeySet = selectionEvent.getRemovedSet();
        RichTable table = (RichTable)selectionEvent.getSource();
        CollectionModel tableModel = (CollectionModel)table.getValue();
        JUCtrlHierBinding adfTableBinding =
            (JUCtrlHierBinding)tableModel.getWrappedData();
        ControllerContext cctx = ControllerContext.getInstance();
            DCBindingContainer dcBindingContainer=(DCBindingContainer)
        BindingContext.getCurrent().getCurrentBindingsEntry();
//         if (cctx.getCurrentRootViewPort().isDataDirty()) {
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
                System.out.println("ERR APRVL GET ROW: " + e.getLocalizedMessage());
            }
        }
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
            if (data.equals(varAll)) {
                format.setStyle("min-width:172px;max-width:172px;");
            } else {
                format.setStyle("min-width:172px;max-width:172px;");
            }
        }
        
        return format;
    }

    public void actionDialogApprovalOk(ActionEvent actionEvent) {
        BindingContainer bindings = getBindings();
        boolean mustRefreshPage = true;
        boolean dialogHide = true;
        UserData userData =
            (UserData)JSFUtils.resolveExpression("#{UserData}");
        String usrName =
            userData.getUserNameLogin() == null ? "" : userData.getUserNameLogin();
        AttributeBinding aprvlActionAttr =
            (AttributeBinding)bindings.getControlBinding("Action");
        Integer aprvlAction = (Integer)aprvlActionAttr.getInputValue();
        AttributeBinding aprvlCodeAttr =
            (AttributeBinding)bindings.getControlBinding("AprvlCode");
        String aprvlCode = (String)aprvlCodeAttr.getInputValue();

        DCIteratorBinding dciterApproval =
            ADFUtils.findIterator("ProposalApprovalView1Iterator");
        Key approvalKey = dciterApproval.getCurrentRow().getKey();

        try {
            if (aprvlAction.equals(APPROVED)) {
               savePaAll();
                // Validasi produk harus ada
                DCIteratorBinding dciterProduk =
                    ADFUtils.findIterator("PromoProdukView1Iterator");
                Integer cekProduk = (int)dciterProduk.getEstimatedRowCount();
                if (cekProduk > 0) {
                    AttributeBinding discTypeAttr =
                        (AttributeBinding)bindings.getControlBinding("DiscountType1");
                    String discType = (String)discTypeAttr.getInputValue();
                    AttributeBinding userTypeAttr =
                        (AttributeBinding)bindings.getControlBinding("UserTypeCreator");
                    String userTypeCreator = (String)userTypeAttr.getInputValue();
                    DCIteratorBinding dciterCustHo =
                        ADFUtils.findIterator("PromoCustomerHoView1Iterator");
            
                    Integer cekCustHo = (int)dciterCustHo.getEstimatedRowCount();
                    
                    if (userTypeCreator.equalsIgnoreCase(userHo)) {
                        Boolean isSavedValid = true;
                        String sSavedMsg = "";
                        if (!(cekCustHo > 0)) {
                            if (!isSavedValid) {
                                sSavedMsg += ", ";
                            }
                            sSavedMsg +=
                                    "Tab \"Customer\" pada detail produk masih kosong, harus diisi.";
                            isSavedValid = false;
                        }
                        if (isSavedValid) {
                            // DO APPROVE PROCESS FOR HO PROPOSAL
                            DCIteratorBinding dciter =
                                ADFUtils.findIterator("ApprovalReceiverApproveProposalView1Iterator");
                            ApprovalReceiverApproveProposalViewImpl apvrlReceiverVO =
                                (ApprovalReceiverApproveProposalViewImpl)dciter.getViewObject();
                            apvrlReceiverVO.setNamedWhereClauseParam("aprvlCode",
                                                                     aprvlCode);
                            apvrlReceiverVO.setNamedWhereClauseParam("usrName",
                                                                     usrName);
                            apvrlReceiverVO.executeQuery();
        
                            if (apvrlReceiverVO.getEstimatedRowCount() > 0) {
                                AttributeBinding propStatus =
                                    (AttributeBinding)bindings.getControlBinding("Status1");
                                propStatus.setInputValue(docStatusInprocess);
                            } else {
                                AttributeBinding propStatus =
                                    (AttributeBinding)bindings.getControlBinding("Status1");
                                propStatus.setInputValue(docStatusActive);
                            }
        
                            OperationBinding operationApproveApproval =
                                bindings.getOperationBinding("approveDocApproval");
                            operationApproveApproval.execute();

                            switchMain.setFacetName("nodata");
                            switchButtonMain.setFacetName("nodata");
                            AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
                            AdfFacesContext.getCurrentInstance().addPartialTarget(switchButtonMain);  
                            mustRefreshPage = true;
                        } else {
                            showPopup(sSavedMsg, potmessage);
                            mustRefreshPage = false;
                        }
                    } else {
                        Boolean isIterValid = true;
                        String sIterMsg = "";
                        if (discType.equalsIgnoreCase("BIAYA")) {
                            DCIteratorBinding dciterBiaya =
                                ADFUtils.findIterator("BiayaView1Iterator");
                            if (dciterBiaya.getEstimatedRowCount() < 1) {
                                if (!isIterValid) {
                                    sIterMsg += ", ";
                                }
                                sIterMsg += "Biaya Pada Produk Detail";
                                isIterValid = false;
                            }
                        } else if (discType.equalsIgnoreCase("PROMOBARANG")) {
                            DCIteratorBinding dciterPromoBonus =
                                ADFUtils.findIterator("PromoBonusView1Iterator");
                            if (dciterPromoBonus.getEstimatedRowCount() < 1) {
                                if (!isIterValid) {
                                    sIterMsg += ", ";
                                }
                                sIterMsg += "Promo Pada Produk Detail";
                                isIterValid = false;
                            }
                        } else if (discType.equalsIgnoreCase("POTONGAN")) {
                            DCIteratorBinding dciterDiscount =
                                ADFUtils.findIterator("DiscountView1Iterator");
                            if (dciterDiscount.getEstimatedRowCount() < 1) {
                                if (!isIterValid) {
                                    sIterMsg += ", ";
                                }
                                sIterMsg += "Potongan Pada Produk Detail";
                                isIterValid = false;
                            }
                        }
                        if (userTypeCreator.equalsIgnoreCase(userArea)) {
                            DCIteratorBinding dciterCustArea =
                                ADFUtils.findIterator("PromoCustomerAreaView1Iterator");
            
                            Integer cekCustArea = (int)dciterCustArea.getEstimatedRowCount();
                            if (!(cekCustArea > 0)) {
                                if (!isIterValid) {
                                    sIterMsg += ", ";
                                }
                                sIterMsg += "Customer";
                                isIterValid = false;
                            }
                        }
                        if (isIterValid) {
                            // DO APPROVE PROCESS FOR AREA PROPOSAL
                            DCIteratorBinding dciter =
                                ADFUtils.findIterator("ApprovalReceiverApproveProposalView1Iterator");
                            ApprovalReceiverApproveProposalViewImpl apvrlReceiverVO =
                                (ApprovalReceiverApproveProposalViewImpl)dciter.getViewObject();
                            apvrlReceiverVO.setNamedWhereClauseParam("aprvlCode",
                                                                     aprvlCode);
                            apvrlReceiverVO.setNamedWhereClauseParam("usrName",
                                                                     usrName);
                            apvrlReceiverVO.executeQuery();
        
                            if (apvrlReceiverVO.getEstimatedRowCount() > 0) {
                                AttributeBinding propStatus =
                                    (AttributeBinding)bindings.getControlBinding("Status1");
                                propStatus.setInputValue(docStatusInprocess);
                            } else {
                                AttributeBinding propStatus =
                                    (AttributeBinding)bindings.getControlBinding("Status1");
                                propStatus.setInputValue(docStatusActive);
                            }
        
                            OperationBinding operationApproveApproval =
                                bindings.getOperationBinding("approveDocApproval");
                            operationApproveApproval.execute();

                            switchMain.setFacetName("nodata");
                            switchButtonMain.setFacetName("nodata");
                            AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
                            AdfFacesContext.getCurrentInstance().addPartialTarget(switchButtonMain);
                            mustRefreshPage = true;
                        } else {
                            showPopup("\"" + sIterMsg + "\" masih kosong, harus diisi.", potmessage);
                            mustRefreshPage = false;
                        }
                    }
                } else {
                    String messageText = "Proposal tidak memiliki list produk, mohon dilengkapi atau di \"Reject\".";
                    //showPopup(messageText, potmessage);
                    JSFUtils.addFacesErrorMessage(messageText);
                    mustRefreshPage = false;
                    dialogHide = false;
                }
            } else if (aprvlAction.equals(REJECTED)) {
                AttributeBinding rejectReason =
                    (AttributeBinding)bindings.getControlBinding("Reason");
				String reasonStr =itReasonApproval.getValue()==null ? "":itReasonApproval.getValue().toString();
                if (reasonStr.length() >= 10) {
                    savePaAll();
                    AttributeBinding forwardTo =
                        (AttributeBinding)bindings.getControlBinding("ForwardTo1");
                    String isForward =
                        forwardTo.getInputValue() == null ? "N" : "Y";
                    if (isForward.equalsIgnoreCase("Y")) {
                        AttributeBinding propStatus =
                            (AttributeBinding)bindings.getControlBinding("Status1");
                        propStatus.setInputValue(docStatusReject);

                        OperationBinding operationApproveApproval =
                            bindings.getOperationBinding("rejectFwdDocApproval");
                        operationApproveApproval.execute();
                    } else {
                        AttributeBinding propStatus =
                            (AttributeBinding)bindings.getControlBinding("Status1");
                        propStatus.setInputValue(docStatusReject);

                        OperationBinding operationApproveApproval =
                            bindings.getOperationBinding("rejectDocApproval");
                        operationApproveApproval.execute();
                    }
                    mustRefreshPage = true;
                } else {
                    String messageText = "\"Reason\" harus diisi apabila melakukan reject approval. Minimal 10 karakter.";
                    //showPopup(messageText, potmessage);
                    JSFUtils.addFacesWarningMessage(messageText);
                    mustRefreshPage = false;
                    dialogHide = false;
                }
            } else if (aprvlAction.equals(FORWARD)) {
                savePaAll();
                // Validasi produk harus ada
                DCIteratorBinding dciterProduk =
                    ADFUtils.findIterator("PromoProdukView1Iterator");
                Integer cekProduk = (int)dciterProduk.getEstimatedRowCount();
                if (cekProduk > 0) {
                    AttributeBinding discTypeAttr =
                        (AttributeBinding)bindings.getControlBinding("DiscountType1");
                    String discType = (String)discTypeAttr.getInputValue();
                    AttributeBinding userTypeAttr =
                        (AttributeBinding)bindings.getControlBinding("UserTypeCreator");
                    String userTypeCreator = (String)userTypeAttr.getInputValue();
                    DCIteratorBinding dciterCustHo =
                        ADFUtils.findIterator("PromoCustomerHoView1Iterator");
            
                    Integer cekUserHo = (int)dciterCustHo.getEstimatedRowCount();
                    
                    if (userTypeCreator.equalsIgnoreCase(userHo)) {
                        Boolean isSavedValid = true;
                        String sSavedMsg = "";
                        if (!(cekUserHo > 0)) {
                            if (!isSavedValid) {
                                sSavedMsg += ", ";
                            }
                            sSavedMsg +=
                                    "Tab \"Customer\" pada detail produk masih kosong, harus diisi.";
                            isSavedValid = false;
                        }
                        if (isSavedValid) {
                            // DO FORWARD PROCESS FOR HO PROPOSAL                            
                            AttributeBinding propStatus =
                                (AttributeBinding)bindings.getControlBinding("Status1");
                            propStatus.setInputValue(docStatusInprocess);
        
                            OperationBinding operationApproveApproval =
                                bindings.getOperationBinding("forwardDocApproval");
                            operationApproveApproval.execute();

                            switchMain.setFacetName("nodata");
                            switchButtonMain.setFacetName("nodata");
                            AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
                            AdfFacesContext.getCurrentInstance().addPartialTarget(switchButtonMain);  
                            mustRefreshPage = true;
                        } else {
                            //showPopup(sSavedMsg, potmessage);
                            JSFUtils.addFacesErrorMessage(sSavedMsg);
                            mustRefreshPage = false;
                            dialogHide = false;
                        }
                    } else {
                        Boolean isIterValid = true;
                        String sIterMsg = "";
                        if (discType.equalsIgnoreCase("BIAYA")) {
                            DCIteratorBinding dciterBiaya =
                                ADFUtils.findIterator("BiayaView1Iterator");
                            if (dciterBiaya.getEstimatedRowCount() < 1) {
                                if (!isIterValid) {
                                    sIterMsg += ", ";
                                }
                                sIterMsg += "Biaya Pada Produk Detail";
                                isIterValid = false;
                            }
                        } else if (discType.equalsIgnoreCase("PROMOBARANG")) {
                            DCIteratorBinding dciterPromoBonus =
                                ADFUtils.findIterator("PromoBonusView1Iterator");
                            if (dciterPromoBonus.getEstimatedRowCount() < 1) {
                                if (!isIterValid) {
                                    sIterMsg += ", ";
                                }
                                sIterMsg += "Promo Pada Produk Detail";
                                isIterValid = false;
                            }
                        } else if (discType.equalsIgnoreCase("POTONGAN")) {
                            DCIteratorBinding dciterDiscount =
                                ADFUtils.findIterator("DiscountView1Iterator");
                            if (dciterDiscount.getEstimatedRowCount() < 1) {
                                if (!isIterValid) {
                                    sIterMsg += ", ";
                                }
                                sIterMsg += "Potongan Pada Produk Detail";
                                isIterValid = false;
                            }
                        }
                        if (userTypeCreator.equalsIgnoreCase(userArea)) {
                            DCIteratorBinding dciterCustArea =
                                ADFUtils.findIterator("PromoCustomerAreaView1Iterator");
            
                            Integer cekUserArea = (int)dciterCustArea.getEstimatedRowCount();
                            if (!(cekUserArea > 0)) {
                                if (!isIterValid) {
                                    sIterMsg += ", ";
                                }
                                sIterMsg += "Customer";
                                isIterValid = false;
                            }
                        }
                        if (isIterValid) {
                            // DO FORWARD PROCESS FOR AREA PROPOSAL                            
                            AttributeBinding propStatus =
                                (AttributeBinding)bindings.getControlBinding("Status1");
                            propStatus.setInputValue(docStatusInprocess);
        
                            OperationBinding operationApproveApproval =
                                bindings.getOperationBinding("forwardDocApproval");
                            operationApproveApproval.execute();

                            switchMain.setFacetName("nodata");
                            switchButtonMain.setFacetName("nodata");
                            AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
                            AdfFacesContext.getCurrentInstance().addPartialTarget(switchButtonMain);
                            mustRefreshPage = true;
                        } else {
                            //showPopup("\"" + sIterMsg + "\" masih kosong, harus diisi.", potmessage);
                            JSFUtils.addFacesErrorMessage("\"" + sIterMsg + "\" masih kosong, harus diisi.");
                            mustRefreshPage = false;
                            dialogHide = false;
                        }
                    }
                } else {
                    //showPopup("Proposal tidak memiliki list produk, mohon dilengkapi atau di \"Reject\".", potmessage);
                    JSFUtils.addFacesErrorMessage("Proposal tidak memiliki list produk, mohon dilengkapi atau di \"Reject\".");
                    mustRefreshPage = false;
                    dialogHide = false;
                }
            } else {
                JSFUtils.addFacesErrorMessage("Error",
                                              "Action approval tidak dikenal.");
                mustRefreshPage = false;
                dialogHide = false;
            }

            OperationBinding operationRefresh =
                bindings.getOperationBinding("Execute");
            operationRefresh.execute();

            OperationBinding operationRefreshDoc =
                bindings.getOperationBinding("Execute1");
            operationRefreshDoc.execute();

            switchMain.setFacetName("nodata");
            switchButtonMain.setFacetName("nodata");
            AdfFacesContext.getCurrentInstance().addPartialTarget(switchMain);
            AdfFacesContext.getCurrentInstance().addPartialTarget(switchButtonMain);
        } catch (JboException e) {
            JSFUtils.addFacesErrorMessage("Error", e.getBaseMessage());
            mustRefreshPage = false;
            dialogHide = false;
        }

        AdfFacesContext.getCurrentInstance().addPartialTarget(tblApproval);
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblDocHistory);
        
        if (mustRefreshPage) {
            refreshPage();
        } 
        
        if (dialogHide) {
            papprove.hide();
        }
    }

    public void actionDialogApprovalCancel(ActionEvent actionEvent) {
        BindingContainer bindings = getBindings();

        DCIteratorBinding dciterApproval =
            ADFUtils.findIterator("ProposalApprovalView1Iterator");
        Key approvalKey = dciterApproval.getCurrentRow().getKey();
        
        OperationBinding operationBinding =
            bindings.getOperationBinding("Rollback");
        operationBinding.execute();
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblApproval);
        AdfFacesContext.getCurrentInstance().addPartialTarget(tblDocHistory);
        
        if (approvalKey != null) {
            dciterApproval.setCurrentRowWithKey(approvalKey.toStringFormat(true));
        }
        refreshPage();
        papprove.hide();
    }

    public void setTabCustRegion(RichShowDetailItem tabCustRegion) {
        this.tabCustRegion = tabCustRegion;
    }

    public RichShowDetailItem getTabCustRegion() {
        return tabCustRegion;
    }

    public RichShowDetailItem getTabCustArea() {
        return tabCustArea;
    }

    public void setTabCustArea(RichShowDetailItem tabCustArea) {
        this.tabCustArea = tabCustArea;
    }

    public void setTabCustLocation(RichShowDetailItem tabCustLocation) {
        this.tabCustLocation = tabCustLocation;
    }

    public RichShowDetailItem getTabCustLocation() {
        return tabCustLocation;
    }

    public void setTabCustType(RichShowDetailItem tabCustType) {
        this.tabCustType = tabCustType;
    }

    public RichShowDetailItem getTabCustType() {
        return tabCustType;
    }

    public void setTabCustGroup(RichShowDetailItem tabCustGroup) {
        this.tabCustGroup = tabCustGroup;
    }

    public RichShowDetailItem getTabCustGroup() {
        return tabCustGroup;
    }

    public void setTabCustName(RichShowDetailItem tabCustName) {
        this.tabCustName = tabCustName;
    }

    public RichShowDetailItem getTabCustName() {
        return tabCustName;
    }

    public void removeRowBrgBonus(ActionEvent actionEvent) {
        DCBindingContainer bindings =
            (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcItteratorBindings =
            bindings.findIteratorBinding("PromoBonusView1Iterator");
        if (dcItteratorBindings.getEstimatedRowCount() == 1) {
            ViewObject voTableData = dcItteratorBindings.getViewObject();
            Row rowSelected = voTableData.getCurrentRow();
            if (rowSelected.getAttribute("PromoBonusId") != null) {
                voTableData.removeCurrentRow();
                OperationBinding operation =
                    (OperationBinding)BindingContext.getCurrent().getCurrentBindingsEntry().get("Commit");
                operation.execute();
                DCIteratorBinding dciterPromoProduk =
                    ADFUtils.findIterator("PromoProdukView1Iterator");
                Row r = dciterPromoProduk.getCurrentRow();
                r.setAttribute("BrgBonusOnTop", 0);
                r.setAttribute("BrgBonusRasioOnTop", 0);
                r.setAttribute("BrgBonusMf", 0);
                r.setAttribute("BrgBonusRasioMf", 0);
                r.setAttribute("BrgBonusRasioTotal", 0);
                dciterPromoProduk.getDataControl().commitTransaction();
                otBrgOnTop.setSubmittedValue(0);
                otBrgRasioOnTop.setSubmittedValue(0);
                otBrgRasioTotal.setSubmittedValue(0);
                otBrgMf.setSubmittedValue(0);
                otBrgRasioMf.setSubmittedValue(0);
                AdfFacesContext.getCurrentInstance().addPartialTarget(otBrgOnTop);
                AdfFacesContext.getCurrentInstance().addPartialTarget(otBrgRasioOnTop);
                AdfFacesContext.getCurrentInstance().addPartialTarget(otBrgRasioTotal);
                AdfFacesContext.getCurrentInstance().addPartialTarget(otBrgMf);
                AdfFacesContext.getCurrentInstance().addPartialTarget(otBrgRasioMf);
            }
        } else {
            ViewObject voTableData = dcItteratorBindings.getViewObject();
            Row rowSelected = voTableData.getCurrentRow();
            if (rowSelected.getAttribute("PromoBonusId") != null) {
                voTableData.removeCurrentRow();
                OperationBinding operation =
                    (OperationBinding)BindingContext.getCurrent().getCurrentBindingsEntry().get("Commit");
                operation.execute();
            }
            DCIteratorBinding dciterPromoProduk =
                ADFUtils.findIterator("PromoProdukView1Iterator");
            Row r = dciterPromoProduk.getCurrentRow();
            DCIteratorBinding dciterPromoBrg =
                ADFUtils.findIterator("PromoBonusView1Iterator");
            BigDecimal totalMf = BigDecimal.ZERO;
            BigDecimal totalOntop = BigDecimal.ZERO;
            for (Row er : dciterPromoBrg.getAllRowsInRange()) {
                String valueMF =
                    er.getAttribute("DiscYearly") == null ? "0" : er.getAttribute("DiscYearly") ==
                                                                   "" ? "0" :
                                                                   er.getAttribute("DiscYearly").toString().replaceAll(",",
                                                                                                                        "");
                String valueTop =
                    er.getAttribute("DiscNonYearly") == null ? "0" :
                    er.getAttribute("DiscNonYearly") == "" ? "0" :
                    er.getAttribute("DiscNonYearly").toString().replaceAll(",",
                                                                            "");
                BigDecimal ontopValue =
                    new BigDecimal(valueTop) == null ? new BigDecimal(0) :
                    new BigDecimal(valueTop);
                BigDecimal mfValue =
                    new BigDecimal(valueMF) == null ? new BigDecimal(0) :
                    new BigDecimal(valueMF);
                totalMf = totalMf.add(mfValue);
                totalOntop = totalOntop.add(ontopValue);
            }
            DCIteratorBinding dciterTarget =
                ADFUtils.findIterator("TargetView1Iterator");
            Row row = dciterTarget.getCurrentRow();
            BigDecimal mf = BigDecimal.ZERO;
            BigDecimal rMf = BigDecimal.ZERO;
            BigDecimal RasioMf = BigDecimal.ZERO;
            BigDecimal rasioTotal = BigDecimal.ZERO;
            BigDecimal rasioT = BigDecimal.ZERO;
            BigDecimal ontop = BigDecimal.ZERO;
            BigDecimal rOntop = BigDecimal.ZERO;
            BigDecimal RasioOntop = BigDecimal.ZERO;
            Number qty =
                (Number)row.getAttribute("Qty") == null ? new Number(0) :
                (Number)row.getAttribute("Qty");
            Number value =
                (Number)row.getAttribute("Value") == null ? new Number(0) :
                (Number)row.getAttribute("Value");
            rMf = totalMf;
            mf = (value.multiply(rMf)).getBigDecimalValue().divide(bdHundred).setScale(2,RoundingMode.HALF_UP);
            otBrgMf.setSubmittedValue(rMf);
            RasioMf =
                    rMf.divide(value.getBigDecimalValue(), MathContext.DECIMAL128).multiply(bdHundred).setScale(2,
                                                                                                                RoundingMode.HALF_UP);
            String rasMf = RasioMf.toString();
            otBrgRasioMf.setSubmittedValue(rasMf);
            rasioT =
                    new BigDecimal(otBrgRasioOnTop.getValue().toString().replaceAll(" ",
                                                                                    "").replaceAll("%",
                                                                                                   ""));
            rasioTotal = RasioMf.add(rasioT);
            String Total = rasioTotal.toString();
            otBrgRasioTotal.setSubmittedValue(Total);

            rOntop = totalOntop;
            String rasio =
                otBrgRasioMf.getValue() == "" ? "0" : otBrgRasioMf.getValue() ==
                                                      null ? "0" :
                                                      otBrgRasioMf.getValue().toString();
            rasioT =
                    new BigDecimal(rasio.toString().replaceAll(" ", "").replaceAll("%",
                                                                                   ""));
            ontop =
                    rOntop.divide(value.getBigDecimalValue(), MathContext.DECIMAL128).multiply(bdHundred).setScale(2,
                                                                                                                   RoundingMode.HALF_UP);

            otBrgOnTop.setSubmittedValue(totalOntop);
            RasioOntop =
                    rOntop.divide(value.getBigDecimalValue(), MathContext.DECIMAL128).multiply(bdHundred).setScale(2,
                                                                                                                   RoundingMode.HALF_UP);
            String rasOntop = RasioOntop.toString();
            otBrgRasioOnTop.setSubmittedValue(rasOntop);
            
            r.setAttribute("BrgBonusOnTop", totalOntop);
            r.setAttribute("BrgBonusRasioOnTop", ontop);
            r.setAttribute("BrgBonusMf", rMf);
            r.setAttribute("BrgBonusRasioMf", rasMf);
            r.setAttribute("BrgBonusRasioTotal", Total);

            OperationBinding operationBinding =
                bindings.getOperationBinding("Commit");
            operationBinding.execute();

            AdfFacesContext.getCurrentInstance().addPartialTarget(otBrgOnTop);
            AdfFacesContext.getCurrentInstance().addPartialTarget(otBrgRasioOnTop);
            AdfFacesContext.getCurrentInstance().addPartialTarget(otBrgRasioTotal);
            AdfFacesContext.getCurrentInstance().addPartialTarget(otBrgMf);
            AdfFacesContext.getCurrentInstance().addPartialTarget(otBrgRasioMf);
        }
    }
}
