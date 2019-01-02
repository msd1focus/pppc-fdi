package app.fpp.bean.reports;

import app.fpp.adfextensions.ADFUtils;
import app.fpp.adfextensions.JSFUtils;
import app.fpp.bean.confirmation.ListAddendumBudget;
import app.fpp.bean.useraccessmenu.UserData;

import app.fpp.model.am.BudgetSettingAMImpl;

import app.fpp.model.am.DashboardAMImpl;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.ParseException;

import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import javax.faces.event.ValueChangeEvent;

import oracle.adf.controller.ControllerContext;
import oracle.adf.controller.TaskFlowId;
import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;
import oracle.adf.view.rich.component.rich.input.RichInputDate;
import oracle.adf.view.rich.component.rich.input.RichInputListOfValues;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;
import oracle.adf.view.rich.component.rich.nav.RichGoButton;
import oracle.adf.view.rich.component.rich.output.RichOutputText;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.event.ReturnPopupEvent;
import oracle.adf.view.rich.model.ListOfValuesModel;

import oracle.binding.AttributeBinding;
import oracle.binding.BindingContainer;

import oracle.jbo.Key;
import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;
import oracle.jbo.ViewObject;
import oracle.jbo.uicli.binding.JUCtrlHierBinding;

import org.apache.myfaces.trinidad.event.AttributeChangeEvent;
import org.apache.myfaces.trinidad.model.CollectionModel;
import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.model.RowKeySetImpl;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;

public class GenerateReportBean {
    //repYearlyBudget.jsff
    private RichInputListOfValues budgetNameLov;
    private RichGoButton genYearlyBudgetBtn;
    private RichSelectBooleanCheckbox budgetNameSBC;
    private RichInputListOfValues budgetYearILOV;
    private RichInputListOfValues budgetTypeILOV;
    private RichInputListOfValues detailOrSummaryILOV;
    //repPromoDouble.jsff
    private RichInputDate valPpFromDt;
    private String txtPpFromDt;
    private RichInputDate valPpToDt;
    private String txtPpToDt;
    private RichInputListOfValues valUserRegionLOV;
    private String txtUserRegionLOV;
    private RichInputListOfValues valUserAreaLOV;
    private String txtUserAreaLOV;
    private RichInputListOfValues valUserLocLOV;
    private String txtUserLocLOV;
    private RichInputListOfValues valUserCustTypeLOV;
    private String txtUserCustTypeLOV;
    private RichInputListOfValues valUserCustGroupLOV;
    private String txtUserCustGroupLOV;
    private RichInputListOfValues valUserCustDetailLOV;
    private String txtUserCustDetailLOV;
    private RichInputListOfValues valProdCategoryLOV;
    private String txtProdCategoryLOV;
    private RichInputListOfValues valProdClassLOV;
    private String txtProdClassLOV;
    private RichInputListOfValues valProdBrandLOV;
    private String txtProdBrandLOV;
    private RichInputListOfValues valProdExtentionLOV;
    private String txtProdExtentionLOV;
    private RichInputListOfValues valProdPackagingLOV;
    private String txtProdPackagingLOV;
    private RichInputListOfValues valProdVariantLOV;
    private String txtProdVariantLOV;
    private RichInputListOfValues valProdDetailLOV;
    private String txtProdDetailLOV;
    private RichGoButton btnGenerateReport;
    private String generatedURL;
    private RichGoButton generateReportBtn;
    private RichInputListOfValues statusIpt;
    private RichInputText regionDescIpt;
    private RichInputText custAreaDescIpt;
    private RichInputText custLocationDescIpt;
    private RichInputText prodCategoryDescInpt;
    private RichInputText prodClassDescInpt;
    private RichInputText prodBrandDescInpt;
    private RichInputText prodExtentionDescInpt;
    private RichInputText prodPackagingDescInpt;
    private RichInputText prodVariantDescInpt;
    private RichInputText prodDetailDescInpt;
    private RichInputText customerDetailTemp;
    private RichInputText custGroupDescIpt;
    private RichInputText custTypeDescIpt;
    private RichOutputText otRegionDesc;
    private RichOutputText otAreaDesc;
    private RichOutputText otLocDesc;
    private RichOutputText otCustTypeDesc;
    private RichOutputText otCustGrpDesc;
    private RichOutputText otCustNameDesc;
    private RichOutputText otCategory;
    private RichOutputText otClass;
    private RichOutputText otBrand;
    private RichOutputText otExt;
    private RichOutputText otPack;
    private RichOutputText otVariant;
    private RichOutputText otItemDesc;

    public GenerateReportBean() {
        txtPpFromDt = "";
        txtPpToDt = "";
        txtUserRegionLOV = "";
        txtUserAreaLOV = "";
        txtUserLocLOV = "";
        txtUserCustTypeLOV = "";
        txtUserCustGroupLOV = "";
        txtUserCustDetailLOV = "";
        txtProdCategoryLOV = "";
        txtProdClassLOV = "";
        txtProdBrandLOV = "";
        txtProdExtentionLOV = "";
        txtProdPackagingLOV = "";
        txtProdVariantLOV = "";
        txtProdDetailLOV = "";
    }
    
    DashboardAMImpl DashboardAM =
        (DashboardAMImpl)ADFUtils.getApplicationModuleForDataControl("DashboardAMDataControl");

    public void setBudgetNameLov(RichInputListOfValues budgetNameLov) {
        this.budgetNameLov = budgetNameLov;
    }

    public RichInputListOfValues getBudgetNameLov() {
        return budgetNameLov;
    }

    public void selectAllEvent(ValueChangeEvent valueChangeEvent) {
        budgetNameLov.setSubmittedValue("");
        AdfFacesContext.getCurrentInstance().addPartialTarget(budgetNameLov);
        UserData userData =
            (UserData)JSFUtils.resolveExpression("#{UserData}");
        String domainName =
            userData.getReportDomain() == null ? "" : userData.getReportDomain();
        String detailOrsummary=detailOrSummaryILOV.getValue().toString();
        String budgetType=budgetTypeILOV.getValue()==null ? "":budgetTypeILOV.getValue().toString();
        String budgetyear=budgetYearILOV.getValue()==null ? "":budgetYearILOV.getValue().toString();
        String sbc=valueChangeEvent.getNewValue().toString();
            String status=statusIpt.getValue()==null ? "":statusIpt.getValue().toString();
            if(status.equalsIgnoreCase("ALL")){
                status="";
            }
            if(sbc.equalsIgnoreCase("true")){
                String budgetName=budgetNameLov.getValue()==null ? "":budgetNameLov.getValue().toString();
                genYearlyBudgetBtn.setDestination(domainName+"/birt/frameset?__report=report_yearly_budget.rptdesign&format=pdf&detail="+detailOrsummary+"&budget_type="+budgetType+"&budget_year="+budgetyear+"&budget_name="+"&status="+status+"");
            }else{
                genYearlyBudgetBtn.setDestination(domainName+"/birt/frameset?__report=report_yearly_budget.rptdesign&format=pdf&detail="+detailOrsummary+"&budget_type="+budgetType+"&budget_year="+budgetyear+"&budget_name="+"&status="+status+"");
            }
       AdfFacesContext.getCurrentInstance().addPartialTarget(genYearlyBudgetBtn);
    }

    public void setGenYearlyBudgetBtn(RichGoButton genYearlyBudgetBtn) {
        this.genYearlyBudgetBtn = genYearlyBudgetBtn;
    }

    public RichGoButton getGenYearlyBudgetBtn() {
        return genYearlyBudgetBtn;
    }
    
    public BindingContainer getBindings() {
        return BindingContext.getCurrent().getCurrentBindingsEntry();
    }
    
    public void BudgetTypeReturnPopupListener(ReturnPopupEvent returnPopupEvent) {
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
        
        UserData userData =
            (UserData)JSFUtils.resolveExpression("#{UserData}");
        String domainName =
            userData.getReportDomain() == null ? "" : userData.getReportDomain();
        String detailOrsummary=detailOrSummaryILOV.getValue().toString();
        String budgetyear=budgetYearILOV.getValue()==null ? "":budgetYearILOV.getValue().toString();
        String budgetType=rw.getAttribute("Value").toString();
        String sbc=budgetNameSBC.getValue().toString();
        String status=statusIpt.getValue()==null ? "":statusIpt.getValue().toString();
        if(status.equalsIgnoreCase("ALL")){
            status="";
        }
        if(sbc.equalsIgnoreCase("false")){
            String budgetName=budgetNameLov.getValue()==null ? "":budgetNameLov.getValue().toString();
            genYearlyBudgetBtn.setDestination(domainName+"/birt/frameset?__report=report_yearly_budget.rptdesign&format=pdf&detail="+detailOrsummary+"&budget_type="+budgetType+"&budget_year="+budgetyear+"&budget_name="+budgetName+"&status="+status+"");
        }else{
            genYearlyBudgetBtn.setDestination(domainName+"/birt/frameset?__report=report_yearly_budget.rptdesign&format=pdf&detail="+detailOrsummary+"&budget_type="+budgetType+"&budget_year="+budgetyear+"&budget_name="+"&status="+status+"");
        }
        AdfFacesContext.getCurrentInstance().addPartialTarget(genYearlyBudgetBtn);
    }

    public void BudgetYearReturnPopupListener(ReturnPopupEvent returnPopupEvent) {
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
        
        UserData userData =
            (UserData)JSFUtils.resolveExpression("#{UserData}");
        String domainName =
            userData.getReportDomain() == null ? "" : userData.getReportDomain();
        String detailOrsummary=detailOrSummaryILOV.getValue().toString();
        String budgetType=budgetTypeILOV.getValue()==null ? "":budgetTypeILOV.getValue().toString();
        String budgetyear=rw.getAttribute("BudgetYear").toString();
        String sbc=budgetNameSBC.getValue().toString();
        String status=statusIpt.getValue()==null ? "":statusIpt.getValue().toString();
        if(status.equalsIgnoreCase("ALL")){
            status="";
        }
        if(sbc.equalsIgnoreCase("false")){
            String budgetName=budgetNameLov.getValue()==null ? "":budgetNameLov.getValue().toString();
            genYearlyBudgetBtn.setDestination(domainName+"/birt/frameset?__report=report_yearly_budget.rptdesign&format=pdf&detail="+detailOrsummary+"&budget_type="+budgetType+"&budget_year="+budgetyear+"&budget_name="+budgetName+"&status="+status+"");
             }else{
            genYearlyBudgetBtn.setDestination(domainName+"/birt/frameset?__report=report_yearly_budget.rptdesign&format=pdf&detail="+detailOrsummary+"&budget_type="+budgetType+"&budget_year="+budgetyear+"&budget_name="+"&status="+status+"");
        }
        AdfFacesContext.getCurrentInstance().addPartialTarget(genYearlyBudgetBtn);
    }

    public void BudgetNameReturnPopupListener(ReturnPopupEvent returnPopupEvent) {
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
        
        UserData userData =
            (UserData)JSFUtils.resolveExpression("#{UserData}");
        String domainName =
            userData.getReportDomain() == null ? "" : userData.getReportDomain();
        String detailOrsummary=detailOrSummaryILOV.getValue().toString();
        String budgetType=budgetTypeILOV.getValue()==null ? "":budgetTypeILOV.getValue().toString();
        String budgetyear = budgetYearILOV.getValue()==null ? "":budgetYearILOV.getValue().toString();
        String status=statusIpt.getValue()==null ? "":statusIpt.getValue().toString();
        if(status.equalsIgnoreCase("ALL")){
            status="";
        }
        String budgetName=rw.getAttribute("Description").toString();
            genYearlyBudgetBtn.setDestination(domainName+"/birt/frameset?__report=report_yearly_budget.rptdesign&format=pdf&detail="+detailOrsummary+"&budget_type="+budgetType+"&budget_year="+budgetyear+"&budget_name="+budgetName+"&status="+status+"");
        AdfFacesContext.getCurrentInstance().addPartialTarget(genYearlyBudgetBtn);
    }

    public void DetailorSummaryReturnPopupListener(ReturnPopupEvent returnPopupEvent) {
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
        
        UserData userData =
            (UserData)JSFUtils.resolveExpression("#{UserData}");
        String domainName =
            userData.getReportDomain() == null ? "" : userData.getReportDomain();
        String detailOrsummary=rw.getAttribute("Code").toString();
        String status=statusIpt.getValue()==null ? "":statusIpt.getValue().toString();
        if(status.equalsIgnoreCase("ALL")){
            status="";
        }
        String budgetType=budgetTypeILOV.getValue()==null ? "":budgetTypeILOV.getValue().toString();
        String budgetyear = budgetYearILOV.getValue()==null ? "":budgetYearILOV.getValue().toString();
        String sbc=budgetNameSBC.getValue().toString();
        if(sbc.equalsIgnoreCase("false")){
            String budgetName=budgetNameLov.getValue()==null ? "":budgetNameLov.getValue().toString();
            genYearlyBudgetBtn.setDestination(domainName+"/birt/frameset?__report=report_yearly_budget.rptdesign&format=pdf&detail="+detailOrsummary+"&budget_type="+budgetType+"&budget_year="+budgetyear+"&budget_name="+budgetName+"&status="+status+"");
        }else{
            genYearlyBudgetBtn.setDestination(domainName+"/birt/frameset?__report=report_yearly_budget.rptdesign&format=pdf&detail="+detailOrsummary+"&budget_type="+budgetType+"&budget_year="+budgetyear+"&budget_name="+status+"");
        }
        AdfFacesContext.getCurrentInstance().addPartialTarget(genYearlyBudgetBtn);
    }

    public void setBudgetNameSBC(RichSelectBooleanCheckbox budgetNameSBC) {
        this.budgetNameSBC = budgetNameSBC;
    }

    public RichSelectBooleanCheckbox getBudgetNameSBC() {
        return budgetNameSBC;
    }

    public void setBudgetYearILOV(RichInputListOfValues budgetYearILOV) {
        this.budgetYearILOV = budgetYearILOV;
    }

    public RichInputListOfValues getBudgetYearILOV() {
        return budgetYearILOV;
    }

    public void setBudgetTypeILOV(RichInputListOfValues budgetTypeILOV) {
        this.budgetTypeILOV = budgetTypeILOV;
    }

    public RichInputListOfValues getBudgetTypeILOV() {
        return budgetTypeILOV;
    }

    public void setDetailOrSummaryILOV(RichInputListOfValues detailOrSummaryILOV) {
        this.detailOrSummaryILOV = detailOrSummaryILOV;
    }

    public RichInputListOfValues getDetailOrSummaryILOV() {
        return detailOrSummaryILOV;
    }
    
    //=======================================generate report promo double===========================================
    
    private String reverseString(String txt) {
        char[] charfromtxt = new char[txt.length()];
        for (int i = 0; i < txt.length(); i++)
            charfromtxt[txt.length() - (i + 1)] = txt.charAt(i);
        return new String(charfromtxt);
    }

    public void setValPpFromDt(RichInputDate valPpFromDt) {
        this.valPpFromDt = valPpFromDt;
    }

    public RichInputDate getValPpFromDt() {
        return valPpFromDt;
    }

  
    public void setValPpToDt(RichInputDate valPpToDt) {
        this.valPpToDt = valPpToDt;
    }

    public RichInputDate getValPpToDt() {
        return valPpToDt;
    }
    
    
    public void valChangePpFromDt(ValueChangeEvent valueChangeEvent) {

//          txtPpFromDt = valPpFromDt.getValue().toString();
           SimpleDateFormat formatterdest = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
            String valF = valPpFromDt.getSubmittedValue()==null ? "":valPpFromDt.getSubmittedValue().toString();
            String valT = valPpToDt.getSubmittedValue()==null ? "":valPpToDt.getSubmittedValue().toString();
   
                  try {
                       if(!valT.equalsIgnoreCase("")){
                           Date date=sm.parse(valT);
                           txtPpToDt=formatterdest.format(date);
                       }else{
                           txtPpToDt="";
                       }
                      Date date=sm.parse(valF);
                      txtPpFromDt=formatterdest.format(date);
                  }catch (Exception e){
                      e.printStackTrace();
                  }
                    
   
            
//        ADFContext adfCtx = ADFContext.getCurrent();
//        Map pageFlowScope = adfCtx.getPageFlowScope();
//        Object val = pageFlowScope.put("ppFromDt", valPpFromDt.getValue().toString());
//        Object val2 = pageFlowScope.get("ppToDt");
        
    //        txtPpFromDt = valPpFromDt.getValue().toString();
    ////        txtPpFromDt = reverseString(valPpFromDt.getValue().toString().replaceAll("-", "/"));
            /*  SimpleDateFormat formatterdest = new SimpleDateFormat("dd/MM/yyyy");
             SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
             String valF = valPpFromDt.getSubmittedValue()==null ? "":valPpFromDt.getSubmittedValue().toString();
             String valT = valPpToDt.getSubmittedValue()==null ? "":valPpToDt.getSubmittedValue().toString();
             System.out.println(valF);
           try {
                if(!valT.equalsIgnoreCase("")){
                    Date date=sm.parse(valT);
                    txtPpToDt=formatterdest.format(date);
                }else{
                    txtPpToDt="";
                }
              Date date=sm.parse(valF);
              txtPpFromDt=formatterdest.format(date);
//              txtPpFromDt=valPpFromDt.getSubmittedValue()==null ?"":valPpFromDt.getSubmittedValue().toString();
//              txtPpToDt=valPpToDt.getSubmittedValue()==null?"" :valPpToDt.getSubmittedValue().toString();
              txtUserRegionLOV=valUserRegionLOV.getSubmittedValue()==null?"":valUserRegionLOV.getSubmittedValue().toString();
              txtUserLocLOV=valUserLocLOV.getSubmittedValue()==null ? "" :valUserLocLOV.getSubmittedValue().toString();
              txtUserAreaLOV=valUserAreaLOV.getSubmittedValue() == null ? "" :valUserAreaLOV.getSubmittedValue().toString();
              txtUserCustTypeLOV=valUserCustTypeLOV.getSubmittedValue() == null ? "" :valUserCustTypeLOV.getSubmittedValue().toString();
              txtUserCustGroupLOV=valUserCustGroupLOV.getSubmittedValue() == null ? "" : valUserCustGroupLOV.getSubmittedValue().toString();
              txtUserCustDetailLOV=valUserCustDetailLOV.getSubmittedValue() == null ? "":valUserCustDetailLOV.getSubmittedValue().toString();
              txtProdCategoryLOV=valProdCategoryLOV.getSubmittedValue() == null ? "":valProdCategoryLOV.getSubmittedValue().toString();
              txtProdClassLOV=valProdClassLOV.getSubmittedValue() == null ? "" : valProdClassLOV.getSubmittedValue().toString();
              txtProdBrandLOV=valProdBrandLOV.getSubmittedValue() == null ? "" : valProdBrandLOV.getSubmittedValue().toString();
              txtProdExtentionLOV=valProdExtentionLOV.getSubmittedValue() == null ? "" :valProdExtentionLOV.getSubmittedValue().toString();
              txtProdPackagingLOV=valProdPackagingLOV.getSubmittedValue() == null ? "" :valProdPackagingLOV.getSubmittedValue().toString();
              txtProdVariantLOV=valProdVariantLOV.getSubmittedValue() == null ? "" : valProdVariantLOV.getSubmittedValue().toString();
              txtProdDetailLOV=valProdDetailLOV.getSubmittedValue() == null ? "" :valProdDetailLOV.getSubmittedValue().toString();
            
          } catch (ParseException e) {
              e.printStackTrace();
          }
        UserData userData =
            (UserData)JSFUtils.resolveExpression("#{UserData}");
        String domainName =
            userData.getReportDomain() == null ? "" : userData.getReportDomain();
        String URL=(domainName +
                        "/birt/frameset?__report=pp_report.rptdesign&company=" +
                        "&start_date=" + txtPpFromDt.toString() + "&end_date=" +
                          txtPpToDt.toString() + "&customerregion=" + txtUserRegionLOV +
                        "&customerlocation=" + txtUserLocLOV +
                        "&customerarea=" + txtUserAreaLOV +
                        "&customertype=" + txtUserCustTypeLOV +
                        "&customergroup=" + txtUserCustGroupLOV +
                        "&customerdetail=" + txtUserCustDetailLOV +
                        "&productcategory=" + txtProdCategoryLOV +
                        "&productclass=" + txtProdClassLOV +
                        "&productbrand=" + txtProdBrandLOV +
                        "&productextension=" + txtProdExtentionLOV +
                        "&productpackaging=" + txtProdPackagingLOV +
                        "&productvariant=" + txtProdVariantLOV +
                        "&productdetail=" + txtProdDetailLOV+"$");
        generateReportBtn.setDestination(URL);
        AdfFacesContext.getCurrentInstance().addPartialTarget(generateReportBtn); */
    }

    public void valChangePpToDt(ValueChangeEvent valueChangeEvent) {
             SimpleDateFormat formatterdest = new SimpleDateFormat("dd/MM/yyyy");
             SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");             
             String valF = valPpFromDt.getSubmittedValue()==null ? "":valPpFromDt.getSubmittedValue().toString();
             String valT = valPpToDt.getSubmittedValue()==null ? "":valPpToDt.getSubmittedValue().toString();
            try {
                if(!valF.equalsIgnoreCase("")){
                    Date datevalPpFromDt=sm.parse(valF);
                    txtPpFromDt=formatterdest.format(datevalPpFromDt);
                }else{
                    txtPpFromDt="";
                }
                   Date date=sm.parse(valT);
                   txtPpToDt=formatterdest.format(date);
//                   txtPpFromDt=valPpFromDt.getSubmittedValue()==null ?"":valPpFromDt.getSubmittedValue().toString();
//                   txtPpToDt=valPpToDt.getSubmittedValue()==null?"" :valPpToDt.getSubmittedValue().toString();
                   txtUserRegionLOV=valUserRegionLOV.getSubmittedValue()==null?"":valUserRegionLOV.getSubmittedValue().toString();
                   txtUserLocLOV=valUserLocLOV.getSubmittedValue()==null ? "" :valUserLocLOV.getSubmittedValue().toString();
                   txtUserAreaLOV=valUserAreaLOV.getSubmittedValue() == null ? "" :valUserAreaLOV.getSubmittedValue().toString();
                   txtUserCustTypeLOV=valUserCustTypeLOV.getSubmittedValue() == null ? "" :valUserCustTypeLOV.getSubmittedValue().toString();
                   txtUserCustGroupLOV=valUserCustGroupLOV.getSubmittedValue() == null ? "" : valUserCustGroupLOV.getSubmittedValue().toString();
                   txtUserCustDetailLOV=valUserCustDetailLOV.getSubmittedValue() == null ? "":valUserCustDetailLOV.getSubmittedValue().toString();
                   txtProdCategoryLOV=valProdCategoryLOV.getSubmittedValue() == null ? "":valProdCategoryLOV.getSubmittedValue().toString();
                   txtProdClassLOV=valProdClassLOV.getSubmittedValue() == null ? "" : valProdClassLOV.getSubmittedValue().toString();
                   txtProdBrandLOV=valProdBrandLOV.getSubmittedValue() == null ? "" : valProdBrandLOV.getSubmittedValue().toString();
                   txtProdExtentionLOV=valProdExtentionLOV.getSubmittedValue() == null ? "" :valProdExtentionLOV.getSubmittedValue().toString();
                   txtProdPackagingLOV=valProdPackagingLOV.getSubmittedValue() == null ? "" :valProdPackagingLOV.getSubmittedValue().toString();
                   txtProdVariantLOV=valProdVariantLOV.getSubmittedValue() == null ? "" : valProdVariantLOV.getSubmittedValue().toString();
                   txtProdDetailLOV=valProdDetailLOV.getSubmittedValue() == null ? "" :valProdDetailLOV.getSubmittedValue().toString();
                   
                   } catch (ParseException e) {
                   e.printStackTrace();
                   }
                   UserData userData =
                   (UserData)JSFUtils.resolveExpression("#{UserData}");
                   String domainName =
                   userData.getReportDomain() == null ? "" : userData.getReportDomain();
                   String URL=(domainName +
                             "/birt/frameset?__report=pp_report.rptdesign&company=" +
                             "&start_date=" + txtPpFromDt.toString() + "&end_date=" +
                               txtPpToDt.toString() + "&customerregion=" + txtUserRegionLOV +
                             "&customerlocation=" + txtUserLocLOV +
                             "&customerarea=" + txtUserAreaLOV +
                             "&customertype=" + txtUserCustTypeLOV +
                             "&customergroup=" + txtUserCustGroupLOV +
                             "&customerdetail=" + txtUserCustDetailLOV +
                             "&productcategory=" + txtProdCategoryLOV +
                             "&productclass=" + txtProdClassLOV +
                             "&productbrand=" + txtProdBrandLOV +
                             "&productextension=" + txtProdExtentionLOV +
                             "&productpackaging=" + txtProdPackagingLOV +
                             "&productvariant=" + txtProdVariantLOV +
                             "&productdetail=" + txtProdDetailLOV+"&");
                   generateReportBtn.setDestination(URL);
                   AdfFacesContext.getCurrentInstance().addPartialTarget(generateReportBtn);
    }

    public void returnUserRegionLOV(ReturnPopupEvent returnPopupEvent) {
        RowKeySet rks = (RowKeySet)returnPopupEvent.getReturnValue();
        List tableRowKey = (List)rks.iterator().next();
        Key key = (Key)tableRowKey.get(0);
        RichInputListOfValues lovField = (RichInputListOfValues)returnPopupEvent.getSource();
        ListOfValuesModel lovModel = lovField.getModel();
        CollectionModel collectionModel = lovModel.getTableModel().getCollectionModel();
        JUCtrlHierBinding treeBinding = (JUCtrlHierBinding)collectionModel.getWrappedData();
        DCIteratorBinding dciter = treeBinding.getDCIteratorBinding();
        Row rw = dciter.findRowByKeyString(key.toStringFormat(true));
        String txtUserRegion = rw.getAttribute("RegionCode").toString();
//        regionDescIpt.setSubmittedValue(txtUserRegion);
        txtUserRegionLOV = txtUserRegion;
        SimpleDateFormat formatterdest = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
        String valF = valPpFromDt.getSubmittedValue()==null ? "":valPpFromDt.getSubmittedValue().toString();
        String valT = valPpToDt.getSubmittedValue()==null ? "":valPpToDt.getSubmittedValue().toString();
        
        try {
                if (!valF.equalsIgnoreCase("")) {
                    Date datevalPpFromDt = sm.parse(valF);
                    txtPpFromDt = formatterdest.format(datevalPpFromDt);
                } else {
                    txtPpFromDt = "";
                }
                if (!valT.equalsIgnoreCase("")) {
                    Date date = sm.parse(valT);
                    txtPpToDt = formatterdest.format(date);
                } else {
                    txtPpToDt = "";
                }
//              txtPpFromDt=valPpFromDt.getSubmittedValue()==null ?"":valPpFromDt.getSubmittedValue().toString();
//              txtPpToDt=valPpToDt.getSubmittedValue()==null?"" :valPpToDt.getSubmittedValue().toString();
//              txtUserRegionLOV=valUserRegionLOV.getSubmittedValue()==null?"":valUserRegionLOV.getSubmittedValue().toString();
              txtUserLocLOV=valUserLocLOV.getSubmittedValue()==null ? "" :valUserLocLOV.getSubmittedValue().toString();
              txtUserAreaLOV=valUserAreaLOV.getSubmittedValue() == null ? "" :valUserAreaLOV.getSubmittedValue().toString();
              txtUserCustTypeLOV=valUserCustTypeLOV.getSubmittedValue() == null ? "" :valUserCustTypeLOV.getSubmittedValue().toString();
              txtUserCustGroupLOV=valUserCustGroupLOV.getSubmittedValue() == null ? "" : valUserCustGroupLOV.getSubmittedValue().toString();
              txtUserCustDetailLOV=valUserCustDetailLOV.getSubmittedValue() == null ? "":valUserCustDetailLOV.getSubmittedValue().toString();
              txtProdCategoryLOV=valProdCategoryLOV.getSubmittedValue() == null ? "":valProdCategoryLOV.getSubmittedValue().toString();
              txtProdClassLOV=valProdClassLOV.getSubmittedValue() == null ? "" : valProdClassLOV.getSubmittedValue().toString();
              txtProdBrandLOV=valProdBrandLOV.getSubmittedValue() == null ? "" : valProdBrandLOV.getSubmittedValue().toString();
              txtProdExtentionLOV=valProdExtentionLOV.getSubmittedValue() == null ? "" :valProdExtentionLOV.getSubmittedValue().toString();
              txtProdPackagingLOV=valProdPackagingLOV.getSubmittedValue() == null ? "" :valProdPackagingLOV.getSubmittedValue().toString();
              txtProdVariantLOV=valProdVariantLOV.getSubmittedValue() == null ? "" : valProdVariantLOV.getSubmittedValue().toString();
              txtProdDetailLOV=valProdDetailLOV.getSubmittedValue() == null ? "" :valProdDetailLOV.getSubmittedValue().toString();
              
              } catch (ParseException e) {
              e.printStackTrace();
              }
              UserData userData =
              (UserData)JSFUtils.resolveExpression("#{UserData}");
              String domainName =
              userData.getReportDomain() == null ? "" : userData.getReportDomain();
              String URL=(domainName +
                        "/birt/frameset?__report=pp_report.rptdesign&company=" +
                        "&start_date=" + txtPpFromDt.toString() + "&end_date=" +
                          txtPpToDt.toString() + "&customerregion=" + txtUserRegionLOV +
                        "&customerlocation=" + txtUserLocLOV +
                        "&customerarea=" + txtUserAreaLOV +
                        "&customertype=" + txtUserCustTypeLOV +
                        "&customergroup=" + txtUserCustGroupLOV +
                        "&customerdetail=" + txtUserCustDetailLOV +
                        "&productcategory=" + txtProdCategoryLOV +
                        "&productclass=" + txtProdClassLOV +
                        "&productbrand=" + txtProdBrandLOV +
                        "&productextension=" + txtProdExtentionLOV +
                        "&productpackaging=" + txtProdPackagingLOV +
                        "&productvariant=" + txtProdVariantLOV +
                        "&productdetail=" + txtProdDetailLOV+"$");
              generateReportBtn.setDestination(URL);
              AdfFacesContext.getCurrentInstance().addPartialTarget(generateReportBtn);
    }

    public void setValUserRegionLOV(RichInputListOfValues valUserRegionLOV) {
        this.valUserRegionLOV = valUserRegionLOV;
    }

    public RichInputListOfValues getValUserRegionLOV() {
        return valUserRegionLOV;
    }

    public void cbUserRegion(ValueChangeEvent valueChangeEvent) {
    }

    public void returnUserAreaLOV(ReturnPopupEvent returnPopupEvent) {
        RowKeySet rks = (RowKeySet)returnPopupEvent.getReturnValue();
        List tableRowKey = (List)rks.iterator().next();
        Key key = (Key)tableRowKey.get(0);
        RichInputListOfValues lovField = (RichInputListOfValues)returnPopupEvent.getSource();
        ListOfValuesModel lovModel = lovField.getModel();
        CollectionModel collectionModel = lovModel.getTableModel().getCollectionModel();
        JUCtrlHierBinding treeBinding = (JUCtrlHierBinding)collectionModel.getWrappedData();
        DCIteratorBinding dciter = treeBinding.getDCIteratorBinding();
        Row rw = dciter.findRowByKeyString(key.toStringFormat(true));
        txtUserAreaLOV = rw.getAttribute("AreaCode").toString();
//        custAreaDescIpt.setSubmittedValue(txtUserAreaLOV);
            SimpleDateFormat formatterdest = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
            String valF = valPpFromDt.getSubmittedValue()==null ? "":valPpFromDt.getSubmittedValue().toString();
            String valT = valPpToDt.getSubmittedValue()==null ? "":valPpToDt.getSubmittedValue().toString();
            try {
                    if (!valF.equalsIgnoreCase("")) {
                        Date datevalPpFromDt = sm.parse(valF);
                        txtPpFromDt = formatterdest.format(datevalPpFromDt);
                    } else {
                        txtPpFromDt = "";
                    }
                    if (!valT.equalsIgnoreCase("")) {
                        Date date = sm.parse(valT);
                        txtPpToDt = formatterdest.format(date);
                    } else {
                        txtPpToDt = "";
                    }
//              txtPpFromDt=valPpFromDt.getSubmittedValue()==null ?"":valPpFromDt.getSubmittedValue().toString();
//              txtPpToDt=valPpToDt.getSubmittedValue()==null?"" :valPpToDt.getSubmittedValue().toString();
              txtUserRegionLOV=valUserRegionLOV.getSubmittedValue()==null?"":valUserRegionLOV.getSubmittedValue().toString();
              txtUserLocLOV=valUserLocLOV.getSubmittedValue()==null ? "" :valUserLocLOV.getSubmittedValue().toString();
//              txtUserAreaLOV=valUserAreaLOV.getSubmittedValue() == null ? "" :valUserAreaLOV.getSubmittedValue().toString();
              txtUserCustTypeLOV=valUserCustTypeLOV.getSubmittedValue() == null ? "" :valUserCustTypeLOV.getSubmittedValue().toString();
              txtUserCustGroupLOV=valUserCustGroupLOV.getSubmittedValue() == null ? "" : valUserCustGroupLOV.getSubmittedValue().toString();
              txtUserCustDetailLOV=valUserCustDetailLOV.getSubmittedValue() == null ? "":valUserCustDetailLOV.getSubmittedValue().toString();
              txtProdCategoryLOV=valProdCategoryLOV.getSubmittedValue() == null ? "":valProdCategoryLOV.getSubmittedValue().toString();
              txtProdClassLOV=valProdClassLOV.getSubmittedValue() == null ? "" : valProdClassLOV.getSubmittedValue().toString();
              txtProdBrandLOV=valProdBrandLOV.getSubmittedValue() == null ? "" : valProdBrandLOV.getSubmittedValue().toString();
              txtProdExtentionLOV=valProdExtentionLOV.getSubmittedValue() == null ? "" :valProdExtentionLOV.getSubmittedValue().toString();
              txtProdPackagingLOV=valProdPackagingLOV.getSubmittedValue() == null ? "" :valProdPackagingLOV.getSubmittedValue().toString();
              txtProdVariantLOV=valProdVariantLOV.getSubmittedValue() == null ? "" : valProdVariantLOV.getSubmittedValue().toString();
              txtProdDetailLOV=valProdDetailLOV.getSubmittedValue() == null ? "" :valProdDetailLOV.getSubmittedValue().toString();
              
              } catch (ParseException e) {
              e.printStackTrace();
              }
              UserData userData =
              (UserData)JSFUtils.resolveExpression("#{UserData}");
              String domainName =
              userData.getReportDomain() == null ? "" : userData.getReportDomain();
              String URL=(domainName +
                        "/birt/frameset?__report=pp_report.rptdesign&company=" +
                        "&start_date=" + txtPpFromDt.toString() + "&end_date=" +
                          txtPpToDt.toString() + "&customerregion=" + txtUserRegionLOV +
                        "&customerlocation=" + txtUserLocLOV +
                        "&customerarea=" + txtUserAreaLOV +
                        "&customertype=" + txtUserCustTypeLOV +
                        "&customergroup=" + txtUserCustGroupLOV +
                        "&customerdetail=" + txtUserCustDetailLOV +
                        "&productcategory=" + txtProdCategoryLOV +
                        "&productclass=" + txtProdClassLOV +
                        "&productbrand=" + txtProdBrandLOV +
                        "&productextension=" + txtProdExtentionLOV +
                        "&productpackaging=" + txtProdPackagingLOV +
                        "&productvariant=" + txtProdVariantLOV +
                        "&productdetail=" + txtProdDetailLOV+"$");
              generateReportBtn.setDestination(URL);
              AdfFacesContext.getCurrentInstance().addPartialTarget(generateReportBtn);
        
    }

    public void setValUserAreaLOV(RichInputListOfValues valUserAreaLOV) {
        this.valUserAreaLOV = valUserAreaLOV;
    }

    public RichInputListOfValues getValUserAreaLOV() {
        return valUserAreaLOV;
    }

    public void cbUserArea(ValueChangeEvent valueChangeEvent) {
    }

    public void returnUserLocLOV(ReturnPopupEvent returnPopupEvent) {
        RowKeySet rks = (RowKeySet)returnPopupEvent.getReturnValue();
        List tableRowKey = (List)rks.iterator().next();
        Key key = (Key)tableRowKey.get(0);
        RichInputListOfValues lovField = (RichInputListOfValues)returnPopupEvent.getSource();
        ListOfValuesModel lovModel = lovField.getModel();
        CollectionModel collectionModel = lovModel.getTableModel().getCollectionModel();
        JUCtrlHierBinding treeBinding = (JUCtrlHierBinding)collectionModel.getWrappedData();
        DCIteratorBinding dciter = treeBinding.getDCIteratorBinding();
        Row rw = dciter.findRowByKeyString(key.toStringFormat(true));
        txtUserLocLOV = rw.getAttribute("LocCode").toString();
//        custLocationDescIpt.setSubmittedValue(txtUserLocLOV);
        SimpleDateFormat formatterdest = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
        String valF = valPpFromDt.getSubmittedValue()==null ? "":valPpFromDt.getSubmittedValue().toString();
        String valT = valPpToDt.getSubmittedValue()==null ? "":valPpToDt.getSubmittedValue().toString();
        try {
                if (!valF.equalsIgnoreCase("")) {
                    Date datevalPpFromDt = sm.parse(valF);
                    txtPpFromDt = formatterdest.format(datevalPpFromDt);
                } else {
                    txtPpFromDt = "";
                }
                if (!valT.equalsIgnoreCase("")) {
                    Date date = sm.parse(valT);
                    txtPpToDt = formatterdest.format(date);
                } else {
                    txtPpToDt = "";
                }
//              txtPpFromDt=valPpFromDt.getSubmittedValue()==null ?"":valPpFromDt.getSubmittedValue().toString();
//              txtPpToDt=valPpToDt.getSubmittedValue()==null?"" :valPpToDt.getSubmittedValue().toString();
              txtUserRegionLOV=valUserRegionLOV.getSubmittedValue()==null?"":valUserRegionLOV.getSubmittedValue().toString();
                System.out.println(txtUserRegionLOV);
//              txtUserLocLOV=valUserLocLOV.getSubmittedValue()==null ? "" :valUserLocLOV.getSubmittedValue().toString();
               txtUserAreaLOV=valUserAreaLOV.getSubmittedValue() == null ? "" :valUserAreaLOV.getSubmittedValue().toString();
              txtUserCustTypeLOV=valUserCustTypeLOV.getSubmittedValue() == null ? "" :valUserCustTypeLOV.getSubmittedValue().toString();
              txtUserCustGroupLOV=valUserCustGroupLOV.getSubmittedValue() == null ? "" : valUserCustGroupLOV.getSubmittedValue().toString();
              txtUserCustDetailLOV=valUserCustDetailLOV.getSubmittedValue() == null ? "":valUserCustDetailLOV.getSubmittedValue().toString();
              txtProdCategoryLOV=valProdCategoryLOV.getSubmittedValue() == null ? "":valProdCategoryLOV.getSubmittedValue().toString();
              txtProdClassLOV=valProdClassLOV.getSubmittedValue() == null ? "" : valProdClassLOV.getSubmittedValue().toString();
              txtProdBrandLOV=valProdBrandLOV.getSubmittedValue() == null ? "" : valProdBrandLOV.getSubmittedValue().toString();
              txtProdExtentionLOV=valProdExtentionLOV.getSubmittedValue() == null ? "" :valProdExtentionLOV.getSubmittedValue().toString();
              txtProdPackagingLOV=valProdPackagingLOV.getSubmittedValue() == null ? "" :valProdPackagingLOV.getSubmittedValue().toString();
              txtProdVariantLOV=valProdVariantLOV.getSubmittedValue() == null ? "" : valProdVariantLOV.getSubmittedValue().toString();
              txtProdDetailLOV=valProdDetailLOV.getSubmittedValue() == null ? "" :valProdDetailLOV.getSubmittedValue().toString();
              
              } catch (ParseException e) {
              e.printStackTrace();
              }
              UserData userData =
              (UserData)JSFUtils.resolveExpression("#{UserData}");
              String domainName =
              userData.getReportDomain() == null ? "" : userData.getReportDomain();
              String URL=(domainName +
                        "/birt/frameset?__report=pp_report.rptdesign&company=" +
                        "&start_date=" + txtPpFromDt.toString() + "&end_date=" +
                          txtPpToDt.toString() + "&customerregion=" + txtUserRegionLOV +
                        "&customerlocation=" + txtUserLocLOV +
                        "&customerarea=" + txtUserAreaLOV +
                        "&customertype=" + txtUserCustTypeLOV +
                        "&customergroup=" + txtUserCustGroupLOV +
                        "&customerdetail=" + txtUserCustDetailLOV +
                        "&productcategory=" + txtProdCategoryLOV +
                        "&productclass=" + txtProdClassLOV +
                        "&productbrand=" + txtProdBrandLOV +
                        "&productextension=" + txtProdExtentionLOV +
                        "&productpackaging=" + txtProdPackagingLOV +
                        "&productvariant=" + txtProdVariantLOV +
                        "&productdetail=" + txtProdDetailLOV+"&");
              generateReportBtn.setDestination(URL);
              System.out.println(URL);
              AdfFacesContext.getCurrentInstance().addPartialTarget(generateReportBtn);
    }

    public void setValUserLocLOV(RichInputListOfValues valUserLocLOV) {
        this.valUserLocLOV = valUserLocLOV;
    }

    public RichInputListOfValues getValUserLocLOV() {
        return valUserLocLOV;
    }

    public void cbUserLoc(ValueChangeEvent valueChangeEvent) {
    }

    public void returnUserCustTypeLOV(ReturnPopupEvent returnPopupEvent) {
        RowKeySet rks = (RowKeySet)returnPopupEvent.getReturnValue();
        List tableRowKey = (List)rks.iterator().next();
        Key key = (Key)tableRowKey.get(0);
        RichInputListOfValues lovField = (RichInputListOfValues)returnPopupEvent.getSource();
        ListOfValuesModel lovModel = lovField.getModel();
        CollectionModel collectionModel = lovModel.getTableModel().getCollectionModel();
        JUCtrlHierBinding treeBinding = (JUCtrlHierBinding)collectionModel.getWrappedData();
        DCIteratorBinding dciter = treeBinding.getDCIteratorBinding();
        Row rw = dciter.findRowByKeyString(key.toStringFormat(true));
        txtUserCustTypeLOV = rw.getAttribute("CustType").toString();
//        custTypeDescIpt.setSubmittedValue(txtUserCustTypeLOV);
//        AdfFacesContext.getCurrentInstance().addPartialTarget(custTypeDescIpt);
        SimpleDateFormat formatterdest = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
        String valF = valPpFromDt.getValue()==null ? "":valPpFromDt.getValue().toString();
        String valT = valPpToDt.getValue()==null ? "":valPpToDt.getValue().toString();
        try {
                if (!valF.equalsIgnoreCase("")) {
                    Date datevalPpFromDt = sm.parse(valF);
                    txtPpFromDt = formatterdest.format(datevalPpFromDt);
                } else {
                    txtPpFromDt = "";
                }
                if (!valT.equalsIgnoreCase("")) {
                    Date date = sm.parse(valT);
                    txtPpToDt = formatterdest.format(date);
                } else {
                    txtPpToDt = "";
                }
//              txtPpFromDt=valPpFromDt.getSubmittedValue()==null ?"":valPpFromDt.getSubmittedValue().toString();
//              txtPpToDt=valPpToDt.getSubmittedValue()==null?"" :valPpToDt.getSubmittedValue().toString();
              txtUserRegionLOV=valUserRegionLOV.getSubmittedValue()==null?"":valUserRegionLOV.getSubmittedValue().toString();
              txtUserLocLOV=valUserLocLOV.getSubmittedValue()==null ? "" :valUserLocLOV.getSubmittedValue().toString();
               txtUserAreaLOV=valUserAreaLOV.getSubmittedValue() == null ? "" :valUserAreaLOV.getSubmittedValue().toString();
//              txtUserCustTypeLOV=valUserCustTypeLOV.getSubmittedValue() == null ? "" :valUserCustTypeLOV.getSubmittedValue().toString();
              txtUserCustGroupLOV=valUserCustGroupLOV.getSubmittedValue() == null ? "" : valUserCustGroupLOV.getSubmittedValue().toString();
              txtUserCustDetailLOV=valUserCustDetailLOV.getSubmittedValue() == null ? "":valUserCustDetailLOV.getSubmittedValue().toString();
              txtProdCategoryLOV=valProdCategoryLOV.getSubmittedValue() == null ? "":valProdCategoryLOV.getSubmittedValue().toString();
              txtProdClassLOV=valProdClassLOV.getSubmittedValue() == null ? "" : valProdClassLOV.getSubmittedValue().toString();
              txtProdBrandLOV=valProdBrandLOV.getSubmittedValue() == null ? "" : valProdBrandLOV.getSubmittedValue().toString();
              txtProdExtentionLOV=valProdExtentionLOV.getSubmittedValue() == null ? "" :valProdExtentionLOV.getSubmittedValue().toString();
              txtProdPackagingLOV=valProdPackagingLOV.getSubmittedValue() == null ? "" :valProdPackagingLOV.getSubmittedValue().toString();
              txtProdVariantLOV=valProdVariantLOV.getSubmittedValue() == null ? "" : valProdVariantLOV.getSubmittedValue().toString();
              txtProdDetailLOV=valProdDetailLOV.getSubmittedValue() == null ? "" :valProdDetailLOV.getSubmittedValue().toString();
              
              } catch (ParseException e) {
              e.printStackTrace();
              }
              UserData userData =
              (UserData)JSFUtils.resolveExpression("#{UserData}");
              String domainName =
              userData.getReportDomain() == null ? "" : userData.getReportDomain();
              String URL=(domainName +
                        "/birt/frameset?__report=pp_report.rptdesign&company=" +
                        "&start_date=" + txtPpFromDt.toString() + "&end_date=" +
                          txtPpToDt.toString() + "&customerregion=" + txtUserRegionLOV +
                        "&customerlocation=" + txtUserLocLOV +
                        "&customerarea=" + txtUserAreaLOV +
                        "&customertype=" + txtUserCustTypeLOV +
                        "&customergroup=" + txtUserCustGroupLOV +
                        "&customerdetail=" + txtUserCustDetailLOV +
                        "&productcategory=" + txtProdCategoryLOV +
                        "&productclass=" + txtProdClassLOV +
                        "&productbrand=" + txtProdBrandLOV +
                        "&productextension=" + txtProdExtentionLOV +
                        "&productpackaging=" + txtProdPackagingLOV +
                        "&productvariant=" + txtProdVariantLOV +
                        "&productdetail=" + txtProdDetailLOV+"$");
              generateReportBtn.setDestination(URL);
              AdfFacesContext.getCurrentInstance().addPartialTarget(generateReportBtn);
    }

    public void setValUserCustTypeLOV(RichInputListOfValues valUserCustTypeLOV) {
        this.valUserCustTypeLOV = valUserCustTypeLOV;
    }

    public RichInputListOfValues getValUserCustTypeLOV() {
        return valUserCustTypeLOV;
    }

    public void cbUserCustType(ValueChangeEvent valueChangeEvent) {
       
    }

    public void returnUserCustGroupLOV(ReturnPopupEvent returnPopupEvent) {        
        RowKeySet rks = (RowKeySet)returnPopupEvent.getReturnValue();
        List tableRowKey = (List)rks.iterator().next();
        Key key = (Key)tableRowKey.get(0);
        RichInputListOfValues lovField = (RichInputListOfValues)returnPopupEvent.getSource();
        ListOfValuesModel lovModel = lovField.getModel();
        CollectionModel collectionModel = lovModel.getTableModel().getCollectionModel();
        JUCtrlHierBinding treeBinding = (JUCtrlHierBinding)collectionModel.getWrappedData();
        DCIteratorBinding dciter = treeBinding.getDCIteratorBinding();
        Row rw = dciter.findRowByKeyString(key.toStringFormat(true));
        txtUserCustGroupLOV = rw.getAttribute("CustGroup").toString();
//        custGroupDescIpt.setSubmittedValue(txtUserCustGroupLOV);
//        AdfFacesContext.getCurrentInstance().addPartialTarget(custGroupDescIpt);
        SimpleDateFormat formatterdest = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
        String valF = valPpFromDt.getValue()==null ? "":valPpFromDt.getValue().toString();
        String valT = valPpToDt.getValue()==null ? "":valPpToDt.getValue().toString();
        try {
                if (!valF.equalsIgnoreCase("")) {
                    Date datevalPpFromDt = sm.parse(valF);
                    txtPpFromDt = formatterdest.format(datevalPpFromDt);
                } else {
                    txtPpFromDt = "";
                }
                if (!valT.equalsIgnoreCase("")) {
                    Date date = sm.parse(valT);
                    txtPpToDt = formatterdest.format(date);
                } else {
                    txtPpToDt = "";
                }
//              txtPpFromDt=valPpFromDt.getSubmittedValue()==null ?"":valPpFromDt.getSubmittedValue().toString();
//              txtPpToDt=valPpToDt.getSubmittedValue()==null?"" :valPpToDt.getSubmittedValue().toString();
              txtUserRegionLOV=valUserRegionLOV.getSubmittedValue()==null?"":valUserRegionLOV.getSubmittedValue().toString();
              txtUserLocLOV=valUserLocLOV.getSubmittedValue()==null ? "" :valUserLocLOV.getSubmittedValue().toString();
               txtUserAreaLOV=valUserAreaLOV.getSubmittedValue() == null ? "" :valUserAreaLOV.getSubmittedValue().toString();
                txtUserCustTypeLOV=valUserCustTypeLOV.getSubmittedValue() == null ? "" :valUserCustTypeLOV.getSubmittedValue().toString();
//              txtUserCustGroupLOV=valUserCustGroupLOV.getSubmittedValue() == null ? "" : valUserCustGroupLOV.getSubmittedValue().toString();
              txtUserCustDetailLOV=valUserCustDetailLOV.getSubmittedValue() == null ? "":valUserCustDetailLOV.getSubmittedValue().toString();
              txtProdCategoryLOV=valProdCategoryLOV.getSubmittedValue() == null ? "":valProdCategoryLOV.getSubmittedValue().toString();
              txtProdClassLOV=valProdClassLOV.getSubmittedValue() == null ? "" : valProdClassLOV.getSubmittedValue().toString();
              txtProdBrandLOV=valProdBrandLOV.getSubmittedValue() == null ? "" : valProdBrandLOV.getSubmittedValue().toString();
              txtProdExtentionLOV=valProdExtentionLOV.getSubmittedValue() == null ? "" :valProdExtentionLOV.getSubmittedValue().toString();
              txtProdPackagingLOV=valProdPackagingLOV.getSubmittedValue() == null ? "" :valProdPackagingLOV.getSubmittedValue().toString();
              txtProdVariantLOV=valProdVariantLOV.getSubmittedValue() == null ? "" : valProdVariantLOV.getSubmittedValue().toString();
              txtProdDetailLOV=valProdDetailLOV.getSubmittedValue() == null ? "" :valProdDetailLOV.getSubmittedValue().toString();
              
              } catch (ParseException e) {
              e.printStackTrace();
              }
              UserData userData =
              (UserData)JSFUtils.resolveExpression("#{UserData}");
              String domainName =
              userData.getReportDomain() == null ? "" : userData.getReportDomain();
              String URL=(domainName +
                        "/birt/frameset?__report=pp_report.rptdesign&company=" +
                        "&start_date=" + txtPpFromDt.toString() + "&end_date=" +
                          txtPpToDt.toString() + "&customerregion=" + txtUserRegionLOV +
                        "&customerlocation=" + txtUserLocLOV +
                        "&customerarea=" + txtUserAreaLOV +
                        "&customertype=" + txtUserCustTypeLOV +
                        "&customergroup=" + txtUserCustGroupLOV +
                        "&customerdetail=" + txtUserCustDetailLOV +
                        "&productcategory=" + txtProdCategoryLOV +
                        "&productclass=" + txtProdClassLOV +
                        "&productbrand=" + txtProdBrandLOV +
                        "&productextension=" + txtProdExtentionLOV +
                        "&productpackaging=" + txtProdPackagingLOV +
                        "&productvariant=" + txtProdVariantLOV +
                        "&productdetail=" + txtProdDetailLOV+"$");
              generateReportBtn.setDestination(URL);
              AdfFacesContext.getCurrentInstance().addPartialTarget(generateReportBtn);
    }

    public void setValUserCustGroupLOV(RichInputListOfValues valUserCustGroupLOV) {
        this.valUserCustGroupLOV = valUserCustGroupLOV;
    }

    public RichInputListOfValues getValUserCustGroupLOV() {
        return valUserCustGroupLOV;
    }

    public void cbUserCustGroup(ValueChangeEvent valueChangeEvent) {
       
    }

    public void returnUserCustDetailLOV(ReturnPopupEvent returnPopupEvent) {
        RowKeySet rks = (RowKeySet)returnPopupEvent.getReturnValue();
        List tableRowKey = (List)rks.iterator().next();
        Key key = (Key)tableRowKey.get(0);
        RichInputListOfValues lovField = (RichInputListOfValues)returnPopupEvent.getSource();
        ListOfValuesModel lovModel = lovField.getModel();
        CollectionModel collectionModel = lovModel.getTableModel().getCollectionModel();
        JUCtrlHierBinding treeBinding = (JUCtrlHierBinding)collectionModel.getWrappedData();
        DCIteratorBinding dciter = treeBinding.getDCIteratorBinding();
        Row rw = dciter.findRowByKeyString(key.toStringFormat(true));
        txtUserCustDetailLOV = rw.getAttribute("CustomerNumber").toString();
//        customerDetailTemp.setSubmittedValue(rw.getAttribute("CustomerFullName").toString());
//        
//        AdfFacesContext.getCurrentInstance().addPartialTarget(customerDetailTemp);
        
        SimpleDateFormat formatterdest = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
        String valF = valPpFromDt.getValue()==null ? "":valPpFromDt.getValue().toString();
        String valT = valPpToDt.getValue()==null ? "":valPpToDt.getValue().toString();
        try {
                if (!valF.equalsIgnoreCase("")) {
                    Date datevalPpFromDt = sm.parse(valF);
                    txtPpFromDt = formatterdest.format(datevalPpFromDt);
                } else {
                    txtPpFromDt = "";
                }
                if (!valT.equalsIgnoreCase("")) {
                    Date date = sm.parse(valT);
                    txtPpToDt = formatterdest.format(date);
                } else {
                    txtPpToDt = "";
                }
              String txtUserRegion =regionDescIpt.getSubmittedValue()==null ? "%%":regionDescIpt.getSubmittedValue().toString();
              if(!txtUserRegion.equalsIgnoreCase("%%")){
                   txtUserRegionLOV = txtUserRegion.substring(4).trim();
              }else{
                  txtUserRegionLOV=txtUserRegion;
              }
//              txtPpFromDt=valPpFromDt.getSubmittedValue()==null ?"":valPpFromDt.getSubmittedValue().toString();
//              txtPpToDt=valPpToDt.getSubmittedValue()==null?"" :valPpToDt.getSubmittedValue().toString();
              txtUserRegionLOV=valUserRegionLOV.getSubmittedValue()==null?"":valUserRegionLOV.getSubmittedValue().toString();
              txtUserLocLOV=valUserLocLOV.getSubmittedValue()==null ? "" :valUserLocLOV.getSubmittedValue().toString();
              txtUserAreaLOV=valUserAreaLOV.getSubmittedValue() == null ? "" :valUserAreaLOV.getSubmittedValue().toString();
              txtUserCustTypeLOV=valUserCustTypeLOV.getSubmittedValue() == null ? "" :valUserCustTypeLOV.getSubmittedValue().toString();
              txtUserCustGroupLOV=valUserCustGroupLOV.getSubmittedValue() == null ? "" : valUserCustGroupLOV.getSubmittedValue().toString();
//              txtUserCustDetailLOV=valUserCustDetailLOV.getSubmittedValue() == null ? "":valUserCustDetailLOV.getSubmittedValue().toString();
              txtProdCategoryLOV=valProdCategoryLOV.getSubmittedValue() == null ? "":valProdCategoryLOV.getSubmittedValue().toString();
              txtProdClassLOV=valProdClassLOV.getSubmittedValue() == null ? "" : valProdClassLOV.getSubmittedValue().toString();
              txtProdBrandLOV=valProdBrandLOV.getSubmittedValue() == null ? "" : valProdBrandLOV.getSubmittedValue().toString();
              txtProdExtentionLOV=valProdExtentionLOV.getSubmittedValue() == null ? "" :valProdExtentionLOV.getSubmittedValue().toString();
              txtProdPackagingLOV=valProdPackagingLOV.getSubmittedValue() == null ? "" :valProdPackagingLOV.getSubmittedValue().toString();
              txtProdVariantLOV=valProdVariantLOV.getSubmittedValue() == null ? "" : valProdVariantLOV.getSubmittedValue().toString();
              txtProdDetailLOV=valProdDetailLOV.getSubmittedValue() == null ? "" :valProdDetailLOV.getSubmittedValue().toString();
              
              } catch (ParseException e) {
              e.printStackTrace();
              }
              UserData userData =
              (UserData)JSFUtils.resolveExpression("#{UserData}");
              String domainName =
              userData.getReportDomain() == null ? "" : userData.getReportDomain();
              String URL=(domainName +
                        "/birt/frameset?__report=pp_report.rptdesign&company=" +
                        "&start_date=" + txtPpFromDt.toString() + "&end_date=" +
                          txtPpToDt.toString() + "&customerregion=" + txtUserRegionLOV +
                        "&customerlocation=" + txtUserLocLOV +
                        "&customerarea=" + txtUserAreaLOV +
                        "&customertype=" + txtUserCustTypeLOV +
                        "&customergroup=" + txtUserCustGroupLOV +
                        "&customerdetail=" + txtUserCustDetailLOV +
                        "&productcategory=" + txtProdCategoryLOV +
                        "&productclass=" + txtProdClassLOV +
                        "&productbrand=" + txtProdBrandLOV +
                        "&productextension=" + txtProdExtentionLOV +
                        "&productpackaging=" + txtProdPackagingLOV +
                        "&productvariant=" + txtProdVariantLOV +
                        "&productdetail=" + txtProdDetailLOV+"$");
              generateReportBtn.setDestination(URL);
              AdfFacesContext.getCurrentInstance().addPartialTarget(generateReportBtn);
    }

    public void setValUserCustDetailLOV(RichInputListOfValues valUserCustDetailLOV) {
        this.valUserCustDetailLOV = valUserCustDetailLOV;
    }

    public RichInputListOfValues getValUserCustDetailLOV() {
        return valUserCustDetailLOV;
    }

    public void cbUserCustDetail(ValueChangeEvent valueChangeEvent) {
        
    }

    public void returnProdCategoryLOV(ReturnPopupEvent returnPopupEvent) {
        RowKeySet rks = (RowKeySet)returnPopupEvent.getReturnValue();
        List tableRowKey = (List)rks.iterator().next();
        Key key = (Key)tableRowKey.get(0);
        RichInputListOfValues lovField = (RichInputListOfValues)returnPopupEvent.getSource();
        ListOfValuesModel lovModel = lovField.getModel();
        CollectionModel collectionModel = lovModel.getTableModel().getCollectionModel();
        JUCtrlHierBinding treeBinding = (JUCtrlHierBinding)collectionModel.getWrappedData();
        DCIteratorBinding dciter = treeBinding.getDCIteratorBinding();
        Row rw = dciter.findRowByKeyString(key.toStringFormat(true));
        txtProdCategoryLOV = rw.getAttribute("SetCategoryDesc").toString();
        prodCategoryDescInpt.setSubmittedValue(txtProdCategoryLOV);
        
        SimpleDateFormat formatterdest = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
        String valF = valPpFromDt.getValue()==null ? "":valPpFromDt.getValue().toString();
        String valT = valPpToDt.getValue()==null ? "":valPpToDt.getValue().toString();
        try {
                if (!valF.equalsIgnoreCase("")) {
                    Date datevalPpFromDt = sm.parse(valF);
                    txtPpFromDt = formatterdest.format(datevalPpFromDt);
                } else {
                    txtPpFromDt = "";
                }
                if (!valT.equalsIgnoreCase("")) {
                    Date date = sm.parse(valT);
                    txtPpToDt = formatterdest.format(date);
                } else {
                    txtPpToDt = "";
                }
              String txtUserRegion =regionDescIpt.getSubmittedValue()==null ? "%%":regionDescIpt.getSubmittedValue().toString();
              if(!txtUserRegion.equalsIgnoreCase("%%")){
                   txtUserRegionLOV = txtUserRegion.substring(4).trim();
              }else{
                  txtUserRegionLOV=txtUserRegion;
              }
              //txtUserRegionLOV =valUserRegionLOV.getValue()==null ? "%%":valUserRegionLOV.getValue().toString();
              txtUserLocLOV  =custLocationDescIpt.getSubmittedValue()==null ? "%%":custLocationDescIpt.getSubmittedValue().toString();
              txtUserAreaLOV =custAreaDescIpt.getSubmittedValue()==null ? "%%":custAreaDescIpt.getSubmittedValue().toString();
              txtUserCustTypeLOV =custTypeDescIpt.getValue()==null ? "%%":custTypeDescIpt.getValue().toString();
              txtUserCustGroupLOV=custGroupDescIpt.getValue()==null ? "%%":custGroupDescIpt.getValue().toString();
              txtUserCustDetailLOV=customerDetailTemp.getSubmittedValue()==null ? "%%":customerDetailTemp.getSubmittedValue().toString();
              //txtProdCategoryLOV=valProdCategoryLOV.getValue()==null ? "%%":valProdCategoryLOV.getValue().toString();
                  
              txtProdClassLOV=prodClassDescInpt.getSubmittedValue()==null ? "":prodClassDescInpt.getSubmittedValue().toString();
              txtProdClassLOV=txtProdClassLOV.replace("&", "%26");
              txtProdBrandLOV=prodBrandDescInpt.getSubmittedValue()==null ? "":prodBrandDescInpt.getSubmittedValue().toString();
              txtProdExtentionLOV=prodExtentionDescInpt.getSubmittedValue()==null ? "":prodExtentionDescInpt.getSubmittedValue().toString();
              txtProdPackagingLOV=prodPackagingDescInpt.getSubmittedValue()==null ? "":prodPackagingDescInpt.getSubmittedValue().toString();
                  
              txtProdVariantLOV=prodVariantDescInpt.getSubmittedValue()==null ? "%%":prodVariantDescInpt.getSubmittedValue().toString();
              txtProdDetailLOV=prodDetailDescInpt.getSubmittedValue()==null ? "%%":prodDetailDescInpt.getSubmittedValue().toString();
              
          } catch (ParseException e) {
              e.printStackTrace();
          }
        UserData userData =
        (UserData)JSFUtils.resolveExpression("#{UserData}");
        String domainName =
        userData.getReportDomain() == null ? "" : userData.getReportDomain();
        String Url=(domainName +
                   "/birt/frameset?__report=pp_report.rptdesign&company=" +
                   "&PP_date=" + txtPpFromDt.toString() + "&PP_date_end=" +
                     txtPpToDt.toString() + "&customer%20region=" + txtUserRegionLOV +
                   "&customer%20location=" + txtUserLocLOV +
                   "&customer%20area=" + txtUserAreaLOV +
                   "&customer%20type=" + txtUserCustTypeLOV +
                   "&customer%20group=" + txtUserCustGroupLOV +
                   "&customer%20detail=" + txtUserCustDetailLOV +
                   "&product%20category=" + txtProdCategoryLOV +
                   "&product%20class=" + txtProdClassLOV +
                   "&product%20brand=" + txtProdBrandLOV +
                   "&product%20extension=" + txtProdExtentionLOV +
                   "&product%20packaging=" + txtProdPackagingLOV +
                   "&product%20variant=" + txtProdVariantLOV +
                   "&product%20detail=" + txtProdDetailLOV);
        
        generateReportBtn.setDestination(Url+"&");
//        System.out.println("Url txtProdCategoryLOV "+Url);
        AdfFacesContext.getCurrentInstance().addPartialTarget(generateReportBtn);
        AdfFacesContext.getCurrentInstance().addPartialTarget(prodCategoryDescInpt);
    }

    public void setValProdCategoryLOV(RichInputListOfValues valProdCategoryLOV) {
        this.valProdCategoryLOV = valProdCategoryLOV;
    }

    public RichInputListOfValues getValProdCategoryLOV() {
        return valProdCategoryLOV;
    }

    public void cbProdCategory(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue().toString().equals("true")) {
            valProdCategoryLOV.setDisabled(true);
            valProdCategoryLOV.setValue(null);
            prodCategoryDescInpt.setValue(null);
            txtProdCategoryLOV = "";
            SimpleDateFormat formatterdest = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
            String valF = valPpFromDt.getValue()==null ? "":valPpFromDt.getValue().toString();
            String valT = valPpToDt.getValue()==null ? "":valPpToDt.getValue().toString();
            try {
                    if (!valF.equalsIgnoreCase("")) {
                        Date datevalPpFromDt = sm.parse(valF);
                        txtPpFromDt = formatterdest.format(datevalPpFromDt);
                    } else {
                        txtPpFromDt = "";
                    }
                    if (!valT.equalsIgnoreCase("")) {
                        Date date = sm.parse(valT);
                        txtPpToDt = formatterdest.format(date);
                    } else {
                        txtPpToDt = "";
                    }
                  String txtUserRegion =regionDescIpt.getSubmittedValue()==null ? "%%":regionDescIpt.getSubmittedValue().toString();
                  if(!txtUserRegion.equalsIgnoreCase("%%")){
                       txtUserRegionLOV = txtUserRegion.substring(4).trim();
                  }else{
                      txtUserRegionLOV=txtUserRegion;
                  }
                  //txtUserRegionLOV =valUserRegionLOV.getValue()==null ? "%%":valUserRegionLOV.getValue().toString();
                  txtUserLocLOV  =custLocationDescIpt.getSubmittedValue()==null ? "%%":custLocationDescIpt.getSubmittedValue().toString();
                  txtUserAreaLOV =custAreaDescIpt.getSubmittedValue()==null ? "%%":custAreaDescIpt.getSubmittedValue().toString();
                
                  txtUserCustTypeLOV =custTypeDescIpt.getSubmittedValue()==null ? "%%":custTypeDescIpt.getSubmittedValue().toString();
                  txtUserCustGroupLOV=custGroupDescIpt.getSubmittedValue()==null ? "%%":custGroupDescIpt.getSubmittedValue().toString();
                  txtUserCustDetailLOV=customerDetailTemp.getSubmittedValue()==null ? "%%":customerDetailTemp.getSubmittedValue().toString();
                  //txtProdCategoryLOV=valProdCategoryLOV.getValue()==null ? "%%":valProdCategoryLOV.getValue().toString();
                      
                      
                  txtProdClassLOV=prodClassDescInpt.getSubmittedValue()==null ? "":prodClassDescInpt.getSubmittedValue().toString();
                  txtProdClassLOV=txtProdClassLOV.replace("&", "%26");
                  txtProdBrandLOV=prodBrandDescInpt.getSubmittedValue()==null ? "":prodBrandDescInpt.getSubmittedValue().toString();
                  txtProdExtentionLOV=prodExtentionDescInpt.getSubmittedValue()==null ? "":prodExtentionDescInpt.getSubmittedValue().toString();
                  txtProdPackagingLOV=prodPackagingDescInpt.getSubmittedValue()==null ? "":prodPackagingDescInpt.getSubmittedValue().toString();
                      
                  txtProdVariantLOV=prodVariantDescInpt.getSubmittedValue()==null ? "%%":prodVariantDescInpt.getSubmittedValue().toString();
                  txtProdDetailLOV=prodDetailDescInpt.getSubmittedValue()==null ? "%%":prodDetailDescInpt.getSubmittedValue().toString();
                  
              } catch (ParseException e) {
                  e.printStackTrace();
              }
            UserData userData =
            (UserData)JSFUtils.resolveExpression("#{UserData}");
            String domainName =
            userData.getReportDomain() == null ? "" : userData.getReportDomain();
            String Url=(domainName +
                       "/birt/frameset?__report=pp_report.rptdesign&company=" +
                       "&PP_date=" + txtPpFromDt.toString() + "&PP_date_end=" +
                         txtPpToDt.toString() + "&customer%20region=" + txtUserRegionLOV +
                       "&customer%20location=" + txtUserLocLOV +
                       "&customer%20area=" + txtUserAreaLOV +
                       "&customer%20type=" + txtUserCustTypeLOV +
                       "&customer%20group=" + txtUserCustGroupLOV +
                       "&customer%20detail=" + txtUserCustDetailLOV +
                       "&product%20category=" + txtProdCategoryLOV +
                       "&product%20class=" + txtProdClassLOV +
                       "&product%20brand=" + txtProdBrandLOV +
                       "&product%20extension=" + txtProdExtentionLOV +
                       "&product%20packaging=" + txtProdPackagingLOV +
                       "&product%20variant=" + txtProdVariantLOV +
                       "&product%20detail=" + txtProdDetailLOV);
            
            generateReportBtn.setDestination(Url+"&");
//            System.out.println("Url txtProdCategoryLOV "+Url);
            AdfFacesContext.getCurrentInstance().addPartialTarget(generateReportBtn);
        }else{
            valProdCategoryLOV.setDisabled(false);
            AdfFacesContext.getCurrentInstance().addPartialTarget(valProdCategoryLOV);
        }
    }

    public void returnProdClassLOV(ReturnPopupEvent returnPopupEvent) {
        RowKeySet rks = (RowKeySet)returnPopupEvent.getReturnValue();
        List tableRowKey = (List)rks.iterator().next();
        Key key = (Key)tableRowKey.get(0);
        RichInputListOfValues lovField = (RichInputListOfValues)returnPopupEvent.getSource();
        ListOfValuesModel lovModel = lovField.getModel();
        CollectionModel collectionModel = lovModel.getTableModel().getCollectionModel();
        JUCtrlHierBinding treeBinding = (JUCtrlHierBinding)collectionModel.getWrappedData();
        DCIteratorBinding dciter = treeBinding.getDCIteratorBinding();
        Row rw = dciter.findRowByKeyString(key.toStringFormat(true));
        txtProdClassLOV = rw.getAttribute("SetClassDesc").toString();
        txtProdClassLOV=txtProdClassLOV.replace("&", "%26");
        prodClassDescInpt.setSubmittedValue(txtProdClassLOV);
        SimpleDateFormat formatterdest = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
        String valF = valPpFromDt.getValue()==null ? "":valPpFromDt.getValue().toString();
        String valT = valPpToDt.getValue()==null ? "":valPpToDt.getValue().toString();
        try {
                if (!valF.equalsIgnoreCase("")) {
                    Date datevalPpFromDt = sm.parse(valF);
                    txtPpFromDt = formatterdest.format(datevalPpFromDt);
                } else {
                    txtPpFromDt = "";
                }
                if (!valT.equalsIgnoreCase("")) {
                    Date date = sm.parse(valT);
                    txtPpToDt = formatterdest.format(date);
                } else {
                    txtPpToDt = "";
                }
              String txtUserRegion =regionDescIpt.getSubmittedValue()==null ? "%%":regionDescIpt.getSubmittedValue().toString();
              if(!txtUserRegion.equalsIgnoreCase("%%")){
                   txtUserRegionLOV = txtUserRegion.substring(4).trim();
              }else{
                  txtUserRegionLOV=txtUserRegion;
              }
              //txtUserRegionLOV =valUserRegionLOV.getValue()==null ? "%%":valUserRegionLOV.getValue().toString();
              txtUserLocLOV  =custLocationDescIpt.getSubmittedValue()==null ? "%%":custLocationDescIpt.getSubmittedValue().toString();
              txtUserAreaLOV =custAreaDescIpt.getSubmittedValue()==null ? "%%":custAreaDescIpt.getSubmittedValue().toString();
            
              txtUserCustTypeLOV =custTypeDescIpt.getSubmittedValue()==null ? "%%":custTypeDescIpt.getSubmittedValue().toString();
              txtUserCustGroupLOV=custGroupDescIpt.getSubmittedValue()==null ? "%%":custGroupDescIpt.getSubmittedValue().toString();
              txtUserCustDetailLOV=customerDetailTemp.getSubmittedValue()==null ? "%%":customerDetailTemp.getSubmittedValue().toString();
              txtProdCategoryLOV=prodCategoryDescInpt.getSubmittedValue()==null ? "":prodCategoryDescInpt.getSubmittedValue().toString();
                  
//              txtProdClassLOV=valProdClassLOV.getValue()==null ? "":valProdClassLOV.getValue().toString();
//              txtProdClassLOV=prodClassDescInpt.getSubmittedValue()==null ? "":prodClassDescInpt.getSubmittedValue().toString();
              txtProdBrandLOV=prodBrandDescInpt.getSubmittedValue()==null ? "":prodBrandDescInpt.getSubmittedValue().toString();
              txtProdExtentionLOV=prodExtentionDescInpt.getSubmittedValue()==null ? "":prodExtentionDescInpt.getSubmittedValue().toString();
              txtProdPackagingLOV=prodPackagingDescInpt.getSubmittedValue()==null ? "":prodPackagingDescInpt.getSubmittedValue().toString();
                  
              txtProdVariantLOV=prodVariantDescInpt.getSubmittedValue()==null ? "%%":prodVariantDescInpt.getSubmittedValue().toString();
              txtProdDetailLOV=prodDetailDescInpt.getSubmittedValue()==null ? "%%":prodDetailDescInpt.getSubmittedValue().toString();
              
              
          } catch (ParseException e) {
              e.printStackTrace();
          }
        UserData userData =
        (UserData)JSFUtils.resolveExpression("#{UserData}");
        String domainName =
        userData.getReportDomain() == null ? "" : userData.getReportDomain();
        String Url=(domainName +
                   "/birt/frameset?__report=pp_report.rptdesign&company=" +
                   "&PP_date=" + txtPpFromDt.toString() + "&PP_date_end=" +
                     txtPpToDt.toString() + "&customer%20region=" + txtUserRegionLOV +
                   "&customer%20location=" + txtUserLocLOV +
                   "&customer%20area=" + txtUserAreaLOV +
                   "&customer%20type=" + txtUserCustTypeLOV +
                   "&customer%20group=" + txtUserCustGroupLOV +
                   "&customer%20detail=" + txtUserCustDetailLOV +
                   "&product%20category=" + txtProdCategoryLOV +
                   "&product%20class=" + txtProdClassLOV +
                   "&product%20brand=" + txtProdBrandLOV +
                   "&product%20extension=" + txtProdExtentionLOV +
                   "&product%20packaging=" + txtProdPackagingLOV +
                   "&product%20variant=" + txtProdVariantLOV +
                   "&product%20detail=" + txtProdDetailLOV);
        
        generateReportBtn.setDestination(Url+"&");
//        System.out.println("Url txtProdClassLOV "+Url);
        AdfFacesContext.getCurrentInstance().addPartialTarget(generateReportBtn);
        AdfFacesContext.getCurrentInstance().addPartialTarget(prodClassDescInpt);
    }

    public void setValProdClassLOV(RichInputListOfValues valProdClassLOV) {
        this.valProdClassLOV = valProdClassLOV;
    }

    public RichInputListOfValues getValProdClassLOV() {
        return valProdClassLOV;
    }

    public void cbProdClass(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue().toString().equals("true")) {
            valProdClassLOV.setDisabled(true);
            valProdClassLOV.setValue(null);
            prodClassDescInpt.setValue(null);
            txtProdClassLOV = "";
            
            SimpleDateFormat formatterdest = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
            String valF = valPpFromDt.getValue()==null ? "":valPpFromDt.getValue().toString();
            String valT = valPpToDt.getValue()==null ? "":valPpToDt.getValue().toString();
            try {
                    if (!valF.equalsIgnoreCase("")) {
                        Date datevalPpFromDt = sm.parse(valF);
                        txtPpFromDt = formatterdest.format(datevalPpFromDt);
                    } else {
                        txtPpFromDt = "";
                    }
                    if (!valT.equalsIgnoreCase("")) {
                        Date date = sm.parse(valT);
                        txtPpToDt = formatterdest.format(date);
                    } else {
                        txtPpToDt = "";
                    }
                  String txtUserRegion =regionDescIpt.getSubmittedValue()==null ? "%%":regionDescIpt.getSubmittedValue().toString();
                  if(!txtUserRegion.equalsIgnoreCase("%%")){
                       txtUserRegionLOV = txtUserRegion.substring(4).trim();
                  }else{
                      txtUserRegionLOV=txtUserRegion;
                  }
                  //txtUserRegionLOV =valUserRegionLOV.getValue()==null ? "%%":valUserRegionLOV.getValue().toString();
                  txtUserLocLOV  =custLocationDescIpt.getSubmittedValue()==null ? "%%":custLocationDescIpt.getSubmittedValue().toString();
                  txtUserAreaLOV =custAreaDescIpt.getSubmittedValue()==null ? "%%":custAreaDescIpt.getSubmittedValue().toString();
                  
                  txtUserCustTypeLOV =custTypeDescIpt.getSubmittedValue()==null ? "%%":custTypeDescIpt.getSubmittedValue().toString();
                  txtUserCustGroupLOV=custGroupDescIpt.getSubmittedValue()==null ? "%%":custGroupDescIpt.getSubmittedValue().toString();
                  txtUserCustDetailLOV=customerDetailTemp.getSubmittedValue()==null ? "%%":customerDetailTemp.getSubmittedValue().toString();
                
                  txtProdCategoryLOV=prodCategoryDescInpt.getSubmittedValue()==null ? "":prodCategoryDescInpt.getSubmittedValue().toString();
                  //txtProdClassLOV=valProdClassLOV.getValue()==null ? "":valProdClassLOV.getValue().toString();
                  //              txtProdClassLOV=prodClassDescInpt.getSubmittedValue()==null ? "":prodClassDescInpt.getSubmittedValue().toString();
                  txtProdBrandLOV=prodBrandDescInpt.getSubmittedValue()==null ? "":prodBrandDescInpt.getSubmittedValue().toString();
                  txtProdExtentionLOV=prodExtentionDescInpt.getSubmittedValue()==null ? "":prodExtentionDescInpt.getSubmittedValue().toString();
                  txtProdPackagingLOV=prodPackagingDescInpt.getSubmittedValue()==null ? "":prodPackagingDescInpt.getSubmittedValue().toString();
                      
                  txtProdVariantLOV=prodVariantDescInpt.getSubmittedValue()==null ? "%%":prodVariantDescInpt.getSubmittedValue().toString();
                  txtProdDetailLOV=prodDetailDescInpt.getSubmittedValue()==null ? "%%":prodDetailDescInpt.getSubmittedValue().toString();
                  
              } catch (ParseException e) {
                  e.printStackTrace();
              }
            UserData userData =
            (UserData)JSFUtils.resolveExpression("#{UserData}");
            String domainName =
            userData.getReportDomain() == null ? "" : userData.getReportDomain();
            String Url=(domainName +
                       "/birt/frameset?__report=pp_report.rptdesign&company=" +
                       "&PP_date=" + txtPpFromDt.toString() + "&PP_date_end=" +
                         txtPpToDt.toString() + "&customer%20region=" + txtUserRegionLOV +
                       "&customer%20location=" + txtUserLocLOV +
                       "&customer%20area=" + txtUserAreaLOV +
                       "&customer%20type=" + txtUserCustTypeLOV +
                       "&customer%20group=" + txtUserCustGroupLOV +
                       "&customer%20detail=" + txtUserCustDetailLOV +
                       "&product%20category=" + txtProdCategoryLOV +
                       "&product%20class=" + txtProdClassLOV +
                       "&product%20brand=" + txtProdBrandLOV +
                       "&product%20extension=" + txtProdExtentionLOV +
                       "&product%20packaging=" + txtProdPackagingLOV +
                       "&product%20variant=" + txtProdVariantLOV +
                       "&product%20detail=" + txtProdDetailLOV);
            generateReportBtn.setDestination(Url+"&");
//            System.out.println("Url txtProdClassLOV "+Url);
            AdfFacesContext.getCurrentInstance().addPartialTarget(generateReportBtn);
        }else{
            valProdClassLOV.setDisabled(false);
            AdfFacesContext.getCurrentInstance().addPartialTarget(valProdClassLOV);
        }
    }

    public void returnProdBrandLOV(ReturnPopupEvent returnPopupEvent) {
        RowKeySet rks = (RowKeySet)returnPopupEvent.getReturnValue();
        List tableRowKey = (List)rks.iterator().next();
        Key key = (Key)tableRowKey.get(0);
        RichInputListOfValues lovField = (RichInputListOfValues)returnPopupEvent.getSource();
        ListOfValuesModel lovModel = lovField.getModel();
        CollectionModel collectionModel = lovModel.getTableModel().getCollectionModel();
        JUCtrlHierBinding treeBinding = (JUCtrlHierBinding)collectionModel.getWrappedData();
        DCIteratorBinding dciter = treeBinding.getDCIteratorBinding();
        Row rw = dciter.findRowByKeyString(key.toStringFormat(true));
        txtProdBrandLOV = rw.getAttribute("SetBrandDesc").toString();
        prodBrandDescInpt.setSubmittedValue(txtProdBrandLOV);
        SimpleDateFormat formatterdest = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
        String valF = valPpFromDt.getValue()==null ? "":valPpFromDt.getValue().toString();
        String valT = valPpToDt.getValue()==null ? "":valPpToDt.getValue().toString();
        try {
                if (!valF.equalsIgnoreCase("")) {
                    Date datevalPpFromDt = sm.parse(valF);
                    txtPpFromDt = formatterdest.format(datevalPpFromDt);
                } else {
                    txtPpFromDt = "";
                }
                if (!valT.equalsIgnoreCase("")) {
                    Date date = sm.parse(valT);
                    txtPpToDt = formatterdest.format(date);
                } else {
                    txtPpToDt = "";
                }
              String txtUserRegion =regionDescIpt.getSubmittedValue()==null ? "%%":regionDescIpt.getSubmittedValue().toString();
              if(!txtUserRegion.equalsIgnoreCase("%%")){
                   txtUserRegionLOV = txtUserRegion.substring(4).trim();
              }else{
                  txtUserRegionLOV=txtUserRegion;
              }
              //txtUserRegionLOV =valUserRegionLOV.getValue()==null ? "%%":valUserRegionLOV.getValue().toString();
              txtUserLocLOV  =custLocationDescIpt.getSubmittedValue()==null ? "%%":custLocationDescIpt.getSubmittedValue().toString();
              txtUserAreaLOV =custAreaDescIpt.getSubmittedValue()==null ? "%%":custAreaDescIpt.getSubmittedValue().toString();
              
              txtUserCustTypeLOV =custTypeDescIpt.getSubmittedValue()==null ? "%%":custTypeDescIpt.getSubmittedValue().toString();
              txtUserCustGroupLOV=custGroupDescIpt.getSubmittedValue()==null ? "%%":custGroupDescIpt.getSubmittedValue().toString();
              txtUserCustDetailLOV=customerDetailTemp.getSubmittedValue()==null ? "%%":customerDetailTemp.getSubmittedValue().toString();
              txtProdCategoryLOV=prodCategoryDescInpt.getSubmittedValue()==null ? "":prodCategoryDescInpt.getSubmittedValue().toString();
              //txtProdClassLOV=valProdClassLOV.getValue()==null ? "":valProdClassLOV.getValue().toString();
              txtProdClassLOV=prodClassDescInpt.getSubmittedValue()==null ? "":prodClassDescInpt.getSubmittedValue().toString();
              txtProdClassLOV=txtProdClassLOV.replace("&", "%26");
//              txtProdBrandLOV=prodBrandDescInpt.getSubmittedValue()==null ? "":prodBrandDescInpt.getSubmittedValue().toString();
              txtProdExtentionLOV=prodExtentionDescInpt.getSubmittedValue()==null ? "":prodExtentionDescInpt.getSubmittedValue().toString();
              txtProdPackagingLOV=prodPackagingDescInpt.getSubmittedValue()==null ? "":prodPackagingDescInpt.getSubmittedValue().toString();
              txtProdVariantLOV=prodVariantDescInpt.getSubmittedValue()==null ? "%%":prodVariantDescInpt.getSubmittedValue().toString();
              txtProdDetailLOV=prodDetailDescInpt.getSubmittedValue()==null ? "%%":prodDetailDescInpt.getSubmittedValue().toString();
              
          } catch (ParseException e) {
              e.printStackTrace();
          }
        UserData userData =
        (UserData)JSFUtils.resolveExpression("#{UserData}");
        String domainName =
        userData.getReportDomain() == null ? "" : userData.getReportDomain();
        String Url=(domainName +
                   "/birt/frameset?__report=pp_report.rptdesign&company=" +
                   "&PP_date=" + txtPpFromDt.toString() + "&PP_date_end=" +
                     txtPpToDt.toString() + "&customer%20region=" + txtUserRegionLOV +
                   "&customer%20location=" + txtUserLocLOV +
                   "&customer%20area=" + txtUserAreaLOV +
                   "&customer%20type=" + txtUserCustTypeLOV +
                   "&customer%20group=" + txtUserCustGroupLOV +
                   "&customer%20detail=" + txtUserCustDetailLOV +
                   "&product%20category=" + txtProdCategoryLOV +
                   "&product%20class=" + txtProdClassLOV +
                   "&product%20brand=" + txtProdBrandLOV +
                   "&product%20extension=" + txtProdExtentionLOV +
                   "&product%20packaging=" + txtProdPackagingLOV +
                   "&product%20variant=" + txtProdVariantLOV +
                   "&product%20detail=" + txtProdDetailLOV);
        
        generateReportBtn.setDestination(Url+"&");
//        System.out.println("Url txtProdBrandLOV "+Url);
        AdfFacesContext.getCurrentInstance().addPartialTarget(generateReportBtn);
        AdfFacesContext.getCurrentInstance().addPartialTarget(prodBrandDescInpt);
    }

    public void setValProdBrandLOV(RichInputListOfValues valProdBrandLOV) {
        this.valProdBrandLOV = valProdBrandLOV;
    }

    public RichInputListOfValues getValProdBrandLOV() {
        return valProdBrandLOV;
    }

    public void cbProdBrand(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue().toString().equals("true")) {
            valProdBrandLOV.setDisabled(true);
            valProdBrandLOV.setValue(null);
            prodBrandDescInpt.setValue(null);
            txtProdBrandLOV = "";
            SimpleDateFormat formatterdest = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
            String valF = valPpFromDt.getValue()==null ? "":valPpFromDt.getValue().toString();
            String valT = valPpToDt.getValue()==null ? "":valPpToDt.getValue().toString();
            try {
                    if (!valF.equalsIgnoreCase("")) {
                        Date datevalPpFromDt = sm.parse(valF);
                        txtPpFromDt = formatterdest.format(datevalPpFromDt);
                    } else {
                        txtPpFromDt = "";
                    }
                    if (!valT.equalsIgnoreCase("")) {
                        Date date = sm.parse(valT);
                        txtPpToDt = formatterdest.format(date);
                    } else {
                        txtPpToDt = "";
                    }
                  String txtUserRegion =regionDescIpt.getSubmittedValue()==null ? "%%":regionDescIpt.getSubmittedValue().toString();
                  if(!txtUserRegion.equalsIgnoreCase("%%")){
                       txtUserRegionLOV = txtUserRegion.substring(4).trim();
                  }else{
                      txtUserRegionLOV=txtUserRegion;
                  }
                  //txtUserRegionLOV =valUserRegionLOV.getValue()==null ? "%%":valUserRegionLOV.getValue().toString();
                  txtUserLocLOV  =custLocationDescIpt.getSubmittedValue()==null ? "%%":custLocationDescIpt.getSubmittedValue().toString();
                  txtUserAreaLOV =custAreaDescIpt.getSubmittedValue()==null ? "%%":custAreaDescIpt.getSubmittedValue().toString();
                  
                  txtUserCustTypeLOV =custTypeDescIpt.getSubmittedValue()==null ? "%%":custTypeDescIpt.getSubmittedValue().toString();
                  txtUserCustGroupLOV=custGroupDescIpt.getSubmittedValue()==null ? "%%":custGroupDescIpt.getSubmittedValue().toString();
                  txtUserCustDetailLOV=customerDetailTemp.getSubmittedValue()==null ? "%%":customerDetailTemp.getSubmittedValue().toString();
                
                  txtProdCategoryLOV=prodCategoryDescInpt.getSubmittedValue()==null ? "":prodCategoryDescInpt.getSubmittedValue().toString();
                //txtProdClassLOV=valProdClassLOV.getValue()==null ? "":valProdClassLOV.getValue().toString();
                  txtProdClassLOV=prodClassDescInpt.getSubmittedValue()==null ? "":prodClassDescInpt.getSubmittedValue().toString();
                  txtProdClassLOV=txtProdClassLOV.replace("&", "%26");
  //              txtProdBrandLOV=prodBrandDescInpt.getSubmittedValue()==null ? "":prodBrandDescInpt.getSubmittedValue().toString();
                  txtProdExtentionLOV=prodExtentionDescInpt.getSubmittedValue()==null ? "":prodExtentionDescInpt.getSubmittedValue().toString();
                  txtProdPackagingLOV=prodPackagingDescInpt.getSubmittedValue()==null ? "":prodPackagingDescInpt.getSubmittedValue().toString();
                  txtProdVariantLOV=prodVariantDescInpt.getSubmittedValue()==null ? "%%":prodVariantDescInpt.getSubmittedValue().toString();
                  txtProdDetailLOV=prodDetailDescInpt.getSubmittedValue()==null ? "%%":prodDetailDescInpt.getSubmittedValue().toString();
              } catch (ParseException e) {
                  e.printStackTrace();
              }
            UserData userData =
            (UserData)JSFUtils.resolveExpression("#{UserData}");
            String domainName =
            userData.getReportDomain() == null ? "" : userData.getReportDomain();
            String Url=(domainName +
                       "/birt/frameset?__report=pp_report.rptdesign&company=" +
                       "&PP_date=" + txtPpFromDt.toString() + "&PP_date_end=" +
                         txtPpToDt.toString() + "&customer%20region=" + txtUserRegionLOV +
                       "&customer%20location=" + txtUserLocLOV +
                       "&customer%20area=" + txtUserAreaLOV +
                       "&customer%20type=" + txtUserCustTypeLOV +
                       "&customer%20group=" + txtUserCustGroupLOV +
                       "&customer%20detail=" + txtUserCustDetailLOV +
                       "&product%20category=" + txtProdCategoryLOV +
                       "&product%20class=" + txtProdClassLOV +
                       "&product%20brand=" + txtProdBrandLOV +
                       "&product%20extension=" + txtProdExtentionLOV +
                       "&product%20packaging=" + txtProdPackagingLOV +
                       "&product%20variant=" + txtProdVariantLOV +
                       "&product%20detail=" + txtProdDetailLOV);
            
            generateReportBtn.setDestination(Url+"&");
//            System.out.println("Url txtProdBrandLOV "+Url);
            AdfFacesContext.getCurrentInstance().addPartialTarget(generateReportBtn);
        }else{
            valProdBrandLOV.setDisabled(false);
            AdfFacesContext.getCurrentInstance().addPartialTarget(valProdBrandLOV);
        }
    }

    public void returnProdExtentionLOV(ReturnPopupEvent returnPopupEvent) {
        RowKeySet rks = (RowKeySet)returnPopupEvent.getReturnValue();
        List tableRowKey = (List)rks.iterator().next();
        Key key = (Key)tableRowKey.get(0);
        RichInputListOfValues lovField = (RichInputListOfValues)returnPopupEvent.getSource();
        ListOfValuesModel lovModel = lovField.getModel();
        CollectionModel collectionModel = lovModel.getTableModel().getCollectionModel();
        JUCtrlHierBinding treeBinding = (JUCtrlHierBinding)collectionModel.getWrappedData();
        DCIteratorBinding dciter = treeBinding.getDCIteratorBinding();
        Row rw = dciter.findRowByKeyString(key.toStringFormat(true));
        txtProdExtentionLOV = rw.getAttribute("SetExtDesc").toString();
        prodExtentionDescInpt.setSubmittedValue(txtProdExtentionLOV);
        
        SimpleDateFormat formatterdest = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
        String valF = valPpFromDt.getValue()==null ? "":valPpFromDt.getValue().toString();
        String valT = valPpToDt.getValue()==null ? "":valPpToDt.getValue().toString();
        try {
                if (!valF.equalsIgnoreCase("")) {
                    Date datevalPpFromDt = sm.parse(valF);
                    txtPpFromDt = formatterdest.format(datevalPpFromDt);
                } else {
                    txtPpFromDt = "";
                }
                if (!valT.equalsIgnoreCase("")) {
                    Date date = sm.parse(valT);
                    txtPpToDt = formatterdest.format(date);
                } else {
                    txtPpToDt = "";
                }
              String txtUserRegion =regionDescIpt.getSubmittedValue()==null ? "%%":regionDescIpt.getSubmittedValue().toString();
              if(!txtUserRegion.equalsIgnoreCase("%%")){
                   txtUserRegionLOV = txtUserRegion.substring(4).trim();
              }else{
                  txtUserRegionLOV=txtUserRegion;
              }
              //txtUserRegionLOV =valUserRegionLOV.getValue()==null ? "%%":valUserRegionLOV.getValue().toString();
              txtUserLocLOV  =custLocationDescIpt.getSubmittedValue()==null ? "%%":custLocationDescIpt.getSubmittedValue().toString();
              txtUserAreaLOV =custAreaDescIpt.getSubmittedValue()==null ? "%%":custAreaDescIpt.getSubmittedValue().toString();
              
              txtUserCustTypeLOV =custTypeDescIpt.getSubmittedValue()==null ? "%%":custTypeDescIpt.getSubmittedValue().toString();
              txtUserCustGroupLOV=custGroupDescIpt.getSubmittedValue()==null ? "%%":custGroupDescIpt.getSubmittedValue().toString();
              txtUserCustDetailLOV=customerDetailTemp.getSubmittedValue()==null ? "%%":customerDetailTemp.getSubmittedValue().toString();
            
              txtProdCategoryLOV=prodCategoryDescInpt.getSubmittedValue()==null ? "":prodCategoryDescInpt.getSubmittedValue().toString();
              //txtProdClassLOV=valProdClassLOV.getValue()==null ? "":valProdClassLOV.getValue().toString();
              txtProdClassLOV=prodClassDescInpt.getSubmittedValue()==null ? "":prodClassDescInpt.getSubmittedValue().toString();
              txtProdClassLOV=txtProdClassLOV.replace("&", "%26");
              txtProdBrandLOV=prodBrandDescInpt.getSubmittedValue()==null ? "":prodBrandDescInpt.getSubmittedValue().toString();
//              txtProdExtentionLOV=prodExtentionDescInpt.getSubmittedValue()==null ? "":prodExtentionDescInpt.getSubmittedValue().toString();
              txtProdPackagingLOV=prodPackagingDescInpt.getSubmittedValue()==null ? "":prodPackagingDescInpt.getSubmittedValue().toString();
              txtProdVariantLOV=prodVariantDescInpt.getSubmittedValue()==null ? "%%":prodVariantDescInpt.getSubmittedValue().toString();
              txtProdDetailLOV=prodDetailDescInpt.getSubmittedValue()==null ? "%%":prodDetailDescInpt.getSubmittedValue().toString();
              
          } catch (ParseException e) {
              e.printStackTrace();
          }
        UserData userData =
        (UserData)JSFUtils.resolveExpression("#{UserData}");
        String domainName =
        userData.getReportDomain() == null ? "" : userData.getReportDomain();
        String Url=(domainName +
                   "/birt/frameset?__report=pp_report.rptdesign&company=" +
                   "&PP_date=" + txtPpFromDt.toString() + "&PP_date_end=" +
                     txtPpToDt.toString() + "&customer%20region=" + txtUserRegionLOV +
                   "&customer%20location=" + txtUserLocLOV +
                   "&customer%20area=" + txtUserAreaLOV +
                   "&customer%20type=" + txtUserCustTypeLOV +
                   "&customer%20group=" + txtUserCustGroupLOV +
                   "&customer%20detail=" + txtUserCustDetailLOV +
                   "&product%20category=" + txtProdCategoryLOV +
                   "&product%20class=" + txtProdClassLOV +
                   "&product%20brand=" + txtProdBrandLOV +
                   "&product%20extension=" + txtProdExtentionLOV +
                   "&product%20packaging=" + txtProdPackagingLOV +
                   "&product%20variant=" + txtProdVariantLOV +
                   "&product%20detail=" + txtProdDetailLOV);
        
        generateReportBtn.setDestination(Url+"&");
//        System.out.println("Url txtProdExtentionLOV "+Url);
        AdfFacesContext.getCurrentInstance().addPartialTarget(generateReportBtn);
        AdfFacesContext.getCurrentInstance().addPartialTarget(prodExtentionDescInpt);
    }

    public void setValProdExtentionLOV(RichInputListOfValues valProdExtentionLOV) {
        this.valProdExtentionLOV = valProdExtentionLOV;
    }

    public RichInputListOfValues getValProdExtentionLOV() {
        return valProdExtentionLOV;
    }

    public void cbProdExtention(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue().toString().equals("true")) {
            valProdExtentionLOV.setDisabled(true);
            valProdExtentionLOV.setValue(null);
            prodExtentionDescInpt.setValue(null);
            txtProdExtentionLOV = "";
            
            SimpleDateFormat formatterdest = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
            String valF = valPpFromDt.getValue()==null ? "":valPpFromDt.getValue().toString();
            String valT = valPpToDt.getValue()==null ? "":valPpToDt.getValue().toString();
            try {
                    if (!valF.equalsIgnoreCase("")) {
                        Date datevalPpFromDt = sm.parse(valF);
                        txtPpFromDt = formatterdest.format(datevalPpFromDt);
                    } else {
                        txtPpFromDt = "";
                    }
                    if (!valT.equalsIgnoreCase("")) {
                        Date date = sm.parse(valT);
                        txtPpToDt = formatterdest.format(date);
                    } else {
                        txtPpToDt = "";
                    }
                  String txtUserRegion =regionDescIpt.getSubmittedValue()==null ? "%%":regionDescIpt.getSubmittedValue().toString();
                  if(!txtUserRegion.equalsIgnoreCase("%%")){
                       txtUserRegionLOV = txtUserRegion.substring(4).trim();
                  }else{
                      txtUserRegionLOV=txtUserRegion;
                  }
                  //txtUserRegionLOV =valUserRegionLOV.getValue()==null ? "%%":valUserRegionLOV.getValue().toString();
                  txtUserLocLOV  =custLocationDescIpt.getSubmittedValue()==null ? "%%":custLocationDescIpt.getSubmittedValue().toString();
                  txtUserAreaLOV =custAreaDescIpt.getSubmittedValue()==null ? "%%":custAreaDescIpt.getSubmittedValue().toString();
                  
                  txtUserCustTypeLOV =custTypeDescIpt.getSubmittedValue()==null ? "%%":custTypeDescIpt.getSubmittedValue().toString();
                  txtUserCustGroupLOV=custGroupDescIpt.getSubmittedValue()==null ? "%%":custGroupDescIpt.getSubmittedValue().toString();
                  txtUserCustDetailLOV=customerDetailTemp.getSubmittedValue()==null ? "%%":customerDetailTemp.getSubmittedValue().toString();
                
                  txtProdCategoryLOV=prodCategoryDescInpt.getSubmittedValue()==null ? "":prodCategoryDescInpt.getSubmittedValue().toString();
                  //txtProdClassLOV=valProdClassLOV.getValue()==null ? "":valProdClassLOV.getValue().toString();
                  txtProdClassLOV=prodClassDescInpt.getSubmittedValue()==null ? "":prodClassDescInpt.getSubmittedValue().toString();
                  txtProdClassLOV=txtProdClassLOV.replace("&", "%26");
                  txtProdBrandLOV=prodBrandDescInpt.getSubmittedValue()==null ? "":prodBrandDescInpt.getSubmittedValue().toString();
                  //              txtProdExtentionLOV=prodExtentionDescInpt.getSubmittedValue()==null ? "":prodExtentionDescInpt.getSubmittedValue().toString();
                  txtProdPackagingLOV=prodPackagingDescInpt.getSubmittedValue()==null ? "":prodPackagingDescInpt.getSubmittedValue().toString();
                  txtProdVariantLOV=prodVariantDescInpt.getSubmittedValue()==null ? "%%":prodVariantDescInpt.getSubmittedValue().toString();
                  txtProdDetailLOV=prodDetailDescInpt.getSubmittedValue()==null ? "%%":prodDetailDescInpt.getSubmittedValue().toString();
                  
              } catch (ParseException e) {
                  e.printStackTrace();
              }
            UserData userData =
            (UserData)JSFUtils.resolveExpression("#{UserData}");
            String domainName =
            userData.getReportDomain() == null ? "" : userData.getReportDomain();
            String Url=(domainName +
                       "/birt/frameset?__report=pp_report.rptdesign&company=" +
                       "&PP_date=" + txtPpFromDt.toString() + "&PP_date_end=" +
                         txtPpToDt.toString() + "&customer%20region=" + txtUserRegionLOV +
                       "&customer%20location=" + txtUserLocLOV +
                       "&customer%20area=" + txtUserAreaLOV +
                       "&customer%20type=" + txtUserCustTypeLOV +
                       "&customer%20group=" + txtUserCustGroupLOV +
                       "&customer%20detail=" + txtUserCustDetailLOV +
                       "&product%20category=" + txtProdCategoryLOV +
                       "&product%20class=" + txtProdClassLOV +
                       "&product%20brand=" + txtProdBrandLOV +
                       "&product%20extension=" + txtProdExtentionLOV +
                       "&product%20packaging=" + txtProdPackagingLOV +
                       "&product%20variant=" + txtProdVariantLOV +
                       "&product%20detail=" + txtProdDetailLOV);
            
            generateReportBtn.setDestination(Url+"&");
//            System.out.println("Url txtProdExtentionLOV "+Url);
            AdfFacesContext.getCurrentInstance().addPartialTarget(generateReportBtn);
        }else{
            valProdExtentionLOV.setDisabled(false);
            AdfFacesContext.getCurrentInstance().addPartialTarget(valProdExtentionLOV);
        }
    }

    public void returnProdPackagingLOV(ReturnPopupEvent returnPopupEvent) {
        RowKeySet rks = (RowKeySet)returnPopupEvent.getReturnValue();
        List tableRowKey = (List)rks.iterator().next();
        Key key = (Key)tableRowKey.get(0);
        RichInputListOfValues lovField = (RichInputListOfValues)returnPopupEvent.getSource();
        ListOfValuesModel lovModel = lovField.getModel();
        CollectionModel collectionModel = lovModel.getTableModel().getCollectionModel();
        JUCtrlHierBinding treeBinding = (JUCtrlHierBinding)collectionModel.getWrappedData();
        DCIteratorBinding dciter = treeBinding.getDCIteratorBinding();
        Row rw = dciter.findRowByKeyString(key.toStringFormat(true));
        txtProdPackagingLOV = rw.getAttribute("SetPackagingDesc").toString();
        prodPackagingDescInpt.setSubmittedValue(txtProdPackagingLOV);
        SimpleDateFormat formatterdest = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
        String valF = valPpFromDt.getValue()==null ? "":valPpFromDt.getValue().toString();
        String valT = valPpToDt.getValue()==null ? "":valPpToDt.getValue().toString();
        try {
                if (!valF.equalsIgnoreCase("")) {
                    Date datevalPpFromDt = sm.parse(valF);
                    txtPpFromDt = formatterdest.format(datevalPpFromDt);
                } else {
                    txtPpFromDt = "";
                }
                if (!valT.equalsIgnoreCase("")) {
                    Date date = sm.parse(valT);
                    txtPpToDt = formatterdest.format(date);
                } else {
                    txtPpToDt = "";
                }
              String txtUserRegion =regionDescIpt.getSubmittedValue()==null ? "%%":regionDescIpt.getSubmittedValue().toString();
              if(!txtUserRegion.equalsIgnoreCase("%%")){
                   txtUserRegionLOV = txtUserRegion.substring(4).trim();
              }else{
                  txtUserRegionLOV=txtUserRegion;
              }
              //txtUserRegionLOV =valUserRegionLOV.getValue()==null ? "%%":valUserRegionLOV.getValue().toString();
              txtUserLocLOV  =custLocationDescIpt.getSubmittedValue()==null ? "%%":custLocationDescIpt.getSubmittedValue().toString();
              txtUserAreaLOV =custAreaDescIpt.getSubmittedValue()==null ? "%%":custAreaDescIpt.getSubmittedValue().toString();
              
              txtUserCustTypeLOV =custTypeDescIpt.getSubmittedValue()==null ? "%%":custTypeDescIpt.getSubmittedValue().toString();
              txtUserCustGroupLOV=custGroupDescIpt.getSubmittedValue()==null ? "%%":custGroupDescIpt.getSubmittedValue().toString();
              txtUserCustDetailLOV=customerDetailTemp.getSubmittedValue()==null ? "%%":customerDetailTemp.getSubmittedValue().toString();
              txtProdCategoryLOV=prodCategoryDescInpt.getSubmittedValue()==null ? "":prodCategoryDescInpt.getSubmittedValue().toString();
              //txtProdClassLOV=valProdClassLOV.getValue()==null ? "":valProdClassLOV.getValue().toString();
              txtProdClassLOV=prodClassDescInpt.getSubmittedValue()==null ? "":prodClassDescInpt.getSubmittedValue().toString();
              txtProdClassLOV=txtProdClassLOV.replace("&", "%26");
              txtProdBrandLOV=prodBrandDescInpt.getSubmittedValue()==null ? "":prodBrandDescInpt.getSubmittedValue().toString();
              txtProdExtentionLOV=prodExtentionDescInpt.getSubmittedValue()==null ? "":prodExtentionDescInpt.getSubmittedValue().toString();
              //txtProdPackagingLOV=prodPackagingDescInpt.getSubmittedValue()==null ? "":prodPackagingDescInpt.getSubmittedValue().toString();
              txtProdVariantLOV=prodVariantDescInpt.getSubmittedValue()==null ? "%%":prodVariantDescInpt.getSubmittedValue().toString();
              txtProdDetailLOV=prodDetailDescInpt.getSubmittedValue()==null ? "%%":prodDetailDescInpt.getSubmittedValue().toString();
              
          } catch (ParseException e) {
              e.printStackTrace();
          }
        UserData userData =
        (UserData)JSFUtils.resolveExpression("#{UserData}");
        String domainName =
        userData.getReportDomain() == null ? "" : userData.getReportDomain();
        String Url=(domainName +
                   "/birt/frameset?__report=pp_report.rptdesign&company=" +
                   "&PP_date=" + txtPpFromDt.toString() + "&PP_date_end=" +
                     txtPpToDt.toString() + "&customer%20region=" + txtUserRegionLOV +
                   "&customer%20location=" + txtUserLocLOV +
                   "&customer%20area=" + txtUserAreaLOV +
                   "&customer%20type=" + txtUserCustTypeLOV +
                   "&customer%20group=" + txtUserCustGroupLOV +
                   "&customer%20detail=" + txtUserCustDetailLOV +
                   "&product%20category=" + txtProdCategoryLOV +
                   "&product%20class=" + txtProdClassLOV +
                   "&product%20brand=" + txtProdBrandLOV +
                   "&product%20extension=" + txtProdExtentionLOV +
                   "&product%20packaging=" + txtProdPackagingLOV +
                   "&product%20variant=" + txtProdVariantLOV +
                   "&product%20detail=" + txtProdDetailLOV);
        
        generateReportBtn.setDestination(Url+"&");
//        System.out.println("Url txtProdPackagingLOV "+Url);
        AdfFacesContext.getCurrentInstance().addPartialTarget(generateReportBtn);
        AdfFacesContext.getCurrentInstance().addPartialTarget(prodPackagingDescInpt);
    }

    public void setValProdPackagingLOV(RichInputListOfValues valProdPackagingLOV) {
        this.valProdPackagingLOV = valProdPackagingLOV;
    }

    public RichInputListOfValues getValProdPackagingLOV() {
        return valProdPackagingLOV;
    }

    public void cbProdPackaging(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue().toString().equals("true")) {
            valProdPackagingLOV.setDisabled(true);
            valProdPackagingLOV.setValue(null);
            prodPackagingDescInpt.setValue(null);
            txtProdPackagingLOV = "";
            SimpleDateFormat formatterdest = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
            String valF = valPpFromDt.getValue()==null ? "":valPpFromDt.getValue().toString();
            String valT = valPpToDt.getValue()==null ? "":valPpToDt.getValue().toString();
            try {
                    if (!valF.equalsIgnoreCase("")) {
                        Date datevalPpFromDt = sm.parse(valF);
                        txtPpFromDt = formatterdest.format(datevalPpFromDt);
                    } else {
                        txtPpFromDt = "";
                    }
                    if (!valT.equalsIgnoreCase("")) {
                        Date date = sm.parse(valT);
                        txtPpToDt = formatterdest.format(date);
                    } else {
                        txtPpToDt = "";
                    }
                  String txtUserRegion =regionDescIpt.getSubmittedValue()==null ? "%%":regionDescIpt.getSubmittedValue().toString();
                  if(!txtUserRegion.equalsIgnoreCase("%%")){
                       txtUserRegionLOV = txtUserRegion.substring(4).trim();
                  }else{
                      txtUserRegionLOV=txtUserRegion;
                  }
                  //txtUserRegionLOV =valUserRegionLOV.getValue()==null ? "%%":valUserRegionLOV.getValue().toString();
                  txtUserLocLOV  =custLocationDescIpt.getSubmittedValue()==null ? "%%":custLocationDescIpt.getSubmittedValue().toString();
                  txtUserAreaLOV =custAreaDescIpt.getSubmittedValue()==null ? "%%":custAreaDescIpt.getSubmittedValue().toString();
                  
                  txtUserCustTypeLOV =custTypeDescIpt.getSubmittedValue()==null ? "%%":custTypeDescIpt.getSubmittedValue().toString();
                  txtUserCustGroupLOV=custGroupDescIpt.getSubmittedValue()==null ? "%%":custGroupDescIpt.getSubmittedValue().toString();
                  txtUserCustDetailLOV=customerDetailTemp.getSubmittedValue()==null ? "%%":customerDetailTemp.getSubmittedValue().toString();
                  txtProdCategoryLOV=prodCategoryDescInpt.getSubmittedValue()==null ? "":prodCategoryDescInpt.getSubmittedValue().toString();
                  //txtProdClassLOV=valProdClassLOV.getValue()==null ? "":valProdClassLOV.getValue().toString();
                  txtProdClassLOV=prodClassDescInpt.getSubmittedValue()==null ? "":prodClassDescInpt.getSubmittedValue().toString();
                  txtProdClassLOV=txtProdClassLOV.replace("&", "%26");
                  txtProdBrandLOV=prodBrandDescInpt.getSubmittedValue()==null ? "":prodBrandDescInpt.getSubmittedValue().toString();
                  txtProdExtentionLOV=prodExtentionDescInpt.getSubmittedValue()==null ? "":prodExtentionDescInpt.getSubmittedValue().toString();
                  //txtProdPackagingLOV=prodPackagingDescInpt.getSubmittedValue()==null ? "":prodPackagingDescInpt.getSubmittedValue().toString();
                  txtProdVariantLOV=prodVariantDescInpt.getSubmittedValue()==null ? "%%":prodVariantDescInpt.getSubmittedValue().toString();
                  txtProdDetailLOV=prodDetailDescInpt.getSubmittedValue()==null ? "%%":prodDetailDescInpt.getSubmittedValue().toString();
              } catch (ParseException e) {
                  e.printStackTrace();
              }
            UserData userData =
            (UserData)JSFUtils.resolveExpression("#{UserData}");
            String domainName =
            userData.getReportDomain() == null ? "" : userData.getReportDomain();
            String Url=(domainName +
                       "/birt/frameset?__report=pp_report.rptdesign&company=" +
                       "&PP_date=" + txtPpFromDt.toString() + "&PP_date_end=" +
                         txtPpToDt.toString() + "&customer%20region=" + txtUserRegionLOV +
                       "&customer%20location=" + txtUserLocLOV +
                       "&customer%20area=" + txtUserAreaLOV +
                       "&customer%20type=" + txtUserCustTypeLOV +
                       "&customer%20group=" + txtUserCustGroupLOV +
                       "&customer%20detail=" + txtUserCustDetailLOV +
                       "&product%20category=" + txtProdCategoryLOV +
                       "&product%20class=" + txtProdClassLOV +
                       "&product%20brand=" + txtProdBrandLOV +
                       "&product%20extension=" + txtProdExtentionLOV +
                       "&product%20packaging=" + txtProdPackagingLOV +
                       "&product%20variant=" + txtProdVariantLOV +
                       "&product%20detail=" + txtProdDetailLOV);
            
            generateReportBtn.setDestination(Url+"&");
//            System.out.println("Url txtProdPackagingLOV "+Url);
            AdfFacesContext.getCurrentInstance().addPartialTarget(generateReportBtn);
        }else{
            valProdPackagingLOV.setDisabled(false);
            AdfFacesContext.getCurrentInstance().addPartialTarget(valProdPackagingLOV);
        }
    }

    public void returnProdVariantLOV(ReturnPopupEvent returnPopupEvent) {
        RowKeySet rks = (RowKeySet)returnPopupEvent.getReturnValue();
        List tableRowKey = (List)rks.iterator().next();
        Key key = (Key)tableRowKey.get(0);
        RichInputListOfValues lovField = (RichInputListOfValues)returnPopupEvent.getSource();
        ListOfValuesModel lovModel = lovField.getModel();
        CollectionModel collectionModel = lovModel.getTableModel().getCollectionModel();
        JUCtrlHierBinding treeBinding = (JUCtrlHierBinding)collectionModel.getWrappedData();
        DCIteratorBinding dciter = treeBinding.getDCIteratorBinding();
        Row rw = dciter.findRowByKeyString(key.toStringFormat(true));
        txtProdVariantLOV = rw.getAttribute("SetVariantDesc").toString();
        prodVariantDescInpt.setSubmittedValue(txtProdVariantLOV);
        SimpleDateFormat formatterdest = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
        String valF = valPpFromDt.getValue()==null ? "":valPpFromDt.getValue().toString();
        String valT = valPpToDt.getValue()==null ? "":valPpToDt.getValue().toString();
        try {
                if (!valF.equalsIgnoreCase("")) {
                    Date datevalPpFromDt = sm.parse(valF);
                    txtPpFromDt = formatterdest.format(datevalPpFromDt);
                } else {
                    txtPpFromDt = "";
                }
                if (!valT.equalsIgnoreCase("")) {
                    Date date = sm.parse(valT);
                    txtPpToDt = formatterdest.format(date);
                } else {
                    txtPpToDt = "";
                }
              String txtUserRegion =regionDescIpt.getSubmittedValue()==null ? "%%":regionDescIpt.getSubmittedValue().toString();
              if(!txtUserRegion.equalsIgnoreCase("%%")){
                   txtUserRegionLOV = txtUserRegion.substring(4).trim();
              }else{
                  txtUserRegionLOV=txtUserRegion;
              }
              //txtUserRegionLOV =valUserRegionLOV.getValue()==null ? "%%":valUserRegionLOV.getValue().toString();
              txtUserLocLOV  =custLocationDescIpt.getSubmittedValue()==null ? "%%":custLocationDescIpt.getSubmittedValue().toString();
              txtUserAreaLOV =custAreaDescIpt.getSubmittedValue()==null ? "%%":custAreaDescIpt.getSubmittedValue().toString();
              
              txtUserCustTypeLOV =custTypeDescIpt.getSubmittedValue()==null ? "%%":custTypeDescIpt.getSubmittedValue().toString();
              txtUserCustGroupLOV=custGroupDescIpt.getSubmittedValue()==null ? "%%":custGroupDescIpt.getSubmittedValue().toString();
              txtUserCustDetailLOV=customerDetailTemp.getSubmittedValue()==null ? "%%":customerDetailTemp.getSubmittedValue().toString();
              txtProdCategoryLOV=prodCategoryDescInpt.getSubmittedValue()==null ? "":prodCategoryDescInpt.getSubmittedValue().toString();
              //txtProdClassLOV=valProdClassLOV.getValue()==null ? "":valProdClassLOV.getValue().toString();
              txtProdClassLOV=prodClassDescInpt.getSubmittedValue()==null ? "":prodClassDescInpt.getSubmittedValue().toString();
              txtProdClassLOV=txtProdClassLOV.replace("&", "%26");
              txtProdBrandLOV=prodBrandDescInpt.getSubmittedValue()==null ? "":prodBrandDescInpt.getSubmittedValue().toString();
              txtProdExtentionLOV=prodExtentionDescInpt.getSubmittedValue()==null ? "":prodExtentionDescInpt.getSubmittedValue().toString();
              txtProdPackagingLOV=prodPackagingDescInpt.getSubmittedValue()==null ? "":prodPackagingDescInpt.getSubmittedValue().toString();
              //txtProdVariantLOV=prodVariantDescInpt.getSubmittedValue()==null ? "%%":prodVariantDescInpt.getSubmittedValue().toString();
              txtProdDetailLOV=prodDetailDescInpt.getSubmittedValue()==null ? "%%":prodDetailDescInpt.getSubmittedValue().toString();
              
          } catch (ParseException e) {
              e.printStackTrace();
          }
        UserData userData =
        (UserData)JSFUtils.resolveExpression("#{UserData}");
        String domainName =
        userData.getReportDomain() == null ? "" : userData.getReportDomain();
        String Url=(domainName +
                   "/birt/frameset?__report=pp_report.rptdesign&company=" +
                   "&PP_date=" + txtPpFromDt.toString() + "&PP_date_end=" +
                     txtPpToDt.toString() + "&customer%20region=" + txtUserRegionLOV +
                   "&customer%20location=" + txtUserLocLOV +
                   "&customer%20area=" + txtUserAreaLOV +
                   "&customer%20type=" + txtUserCustTypeLOV +
                   "&customer%20group=" + txtUserCustGroupLOV +
                   "&customer%20detail=" + txtUserCustDetailLOV +
                   "&product%20category=" + txtProdCategoryLOV +
                   "&product%20class=" + txtProdClassLOV +
                   "&product%20brand=" + txtProdBrandLOV +
                   "&product%20extension=" + txtProdExtentionLOV +
                   "&product%20packaging=" + txtProdPackagingLOV +
                   "&product%20variant=" + txtProdVariantLOV +
                   "&product%20detail=" + txtProdDetailLOV);
        
        generateReportBtn.setDestination(Url+"&");
//        System.out.println("Url txtProdVariantLOV "+Url);
        AdfFacesContext.getCurrentInstance().addPartialTarget(generateReportBtn);
        AdfFacesContext.getCurrentInstance().addPartialTarget(prodVariantDescInpt);
    }

    public void setValProdVariantLOV(RichInputListOfValues valProdVariantLOV) {
        this.valProdVariantLOV = valProdVariantLOV;
    }

    public RichInputListOfValues getValProdVariantLOV() {
        return valProdVariantLOV;
    }

    public void cbProdVariant(ValueChangeEvent valueChangeEvent) {
        if (valueChangeEvent.getNewValue().toString().equals("true")) {
            valProdVariantLOV.setDisabled(true);
            valProdVariantLOV.setValue(null);
            prodVariantDescInpt.setValue(null);
            txtProdVariantLOV = "%%";
            SimpleDateFormat formatterdest = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
            String valF = valPpFromDt.getValue()==null ? "":valPpFromDt.getValue().toString();
            String valT = valPpToDt.getValue()==null ? "":valPpToDt.getValue().toString();
            try {
                    if (!valF.equalsIgnoreCase("")) {
                        Date datevalPpFromDt = sm.parse(valF);
                        txtPpFromDt = formatterdest.format(datevalPpFromDt);
                    } else {
                        txtPpFromDt = "";
                    }
                    if (!valT.equalsIgnoreCase("")) {
                        Date date = sm.parse(valT);
                        txtPpToDt = formatterdest.format(date);
                    } else {
                        txtPpToDt = "";
                    }
                  String txtUserRegion =regionDescIpt.getSubmittedValue()==null ? "%%":regionDescIpt.getSubmittedValue().toString();
                  if(!txtUserRegion.equalsIgnoreCase("%%")){
                       txtUserRegionLOV = txtUserRegion.substring(4).trim();
                  }else{
                      txtUserRegionLOV=txtUserRegion;
                  }
                  //txtUserRegionLOV =valUserRegionLOV.getValue()==null ? "%%":valUserRegionLOV.getValue().toString();
                  txtUserLocLOV  =custLocationDescIpt.getSubmittedValue()==null ? "%%":custLocationDescIpt.getSubmittedValue().toString();
                  txtUserAreaLOV =custAreaDescIpt.getSubmittedValue()==null ? "%%":custAreaDescIpt.getSubmittedValue().toString();
                  
                  txtUserCustTypeLOV =custTypeDescIpt.getSubmittedValue()==null ? "%%":custTypeDescIpt.getSubmittedValue().toString();
                  txtUserCustGroupLOV=custGroupDescIpt.getSubmittedValue()==null ? "%%":custGroupDescIpt.getSubmittedValue().toString();
                  txtUserCustDetailLOV=customerDetailTemp.getSubmittedValue()==null ? "%%":customerDetailTemp.getSubmittedValue().toString();
                
                  txtProdCategoryLOV=prodCategoryDescInpt.getSubmittedValue()==null ? "":prodCategoryDescInpt.getSubmittedValue().toString();
                  //txtProdClassLOV=valProdClassLOV.getValue()==null ? "":valProdClassLOV.getValue().toString();
                  txtProdClassLOV=prodClassDescInpt.getSubmittedValue()==null ? "":prodClassDescInpt.getSubmittedValue().toString();
                  txtProdClassLOV=txtProdClassLOV.replace("&", "%26");
                  txtProdBrandLOV=prodBrandDescInpt.getSubmittedValue()==null ? "":prodBrandDescInpt.getSubmittedValue().toString();
                  txtProdExtentionLOV=prodExtentionDescInpt.getSubmittedValue()==null ? "":prodExtentionDescInpt.getSubmittedValue().toString();
                  txtProdPackagingLOV=prodPackagingDescInpt.getSubmittedValue()==null ? "":prodPackagingDescInpt.getSubmittedValue().toString();
                  //txtProdVariantLOV=prodVariantDescInpt.getSubmittedValue()==null ? "%%":prodVariantDescInpt.getSubmittedValue().toString();
                  txtProdDetailLOV=prodDetailDescInpt.getSubmittedValue()==null ? "%%":prodDetailDescInpt.getSubmittedValue().toString();
                  
              } catch (ParseException e) {
                  e.printStackTrace();
              }
            UserData userData =
            (UserData)JSFUtils.resolveExpression("#{UserData}");
            String domainName =
            userData.getReportDomain() == null ? "" : userData.getReportDomain();
            String Url=(domainName +
                       "/birt/frameset?__report=pp_report.rptdesign&company=" +
                       "&PP_date=" + txtPpFromDt.toString() + "&PP_date_end=" +
                         txtPpToDt.toString() + "&customer%20region=" + txtUserRegionLOV +
                       "&customer%20location=" + txtUserLocLOV +
                       "&customer%20area=" + txtUserAreaLOV +
                       "&customer%20type=" + txtUserCustTypeLOV +
                       "&customer%20group=" + txtUserCustGroupLOV +
                       "&customer%20detail=" + txtUserCustDetailLOV +
                       "&product%20category=" + txtProdCategoryLOV +
                       "&product%20class=" + txtProdClassLOV +
                       "&product%20brand=" + txtProdBrandLOV +
                       "&product%20extension=" + txtProdExtentionLOV +
                       "&product%20packaging=" + txtProdPackagingLOV +
                       "&product%20variant=" + txtProdVariantLOV +
                       "&product%20detail=" + txtProdDetailLOV);
            
            generateReportBtn.setDestination(Url+"&");
//            System.out.println("Url txtProdVariantLOV "+Url);
            AdfFacesContext.getCurrentInstance().addPartialTarget(generateReportBtn);
        }else{
            valProdVariantLOV.setDisabled(false);
            AdfFacesContext.getCurrentInstance().addPartialTarget(valProdVariantLOV);
        }
    }

    public void returnProdDetailLOV(ReturnPopupEvent returnPopupEvent) {
        RowKeySet rks = (RowKeySet)returnPopupEvent.getReturnValue();
        List tableRowKey = (List)rks.iterator().next();
        Key key = (Key)tableRowKey.get(0);
        RichInputListOfValues lovField = (RichInputListOfValues)returnPopupEvent.getSource();
        ListOfValuesModel lovModel = lovField.getModel();
        CollectionModel collectionModel = lovModel.getTableModel().getCollectionModel();
        JUCtrlHierBinding treeBinding = (JUCtrlHierBinding)collectionModel.getWrappedData();
        DCIteratorBinding dciter = treeBinding.getDCIteratorBinding();
        Row rw = dciter.findRowByKeyString(key.toStringFormat(true));
        txtProdDetailLOV = rw.getAttribute("ItemDescription").toString();
        prodDetailDescInpt.setSubmittedValue(txtProdDetailLOV);
        
        SimpleDateFormat formatterdest = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
        String valF = valPpFromDt.getValue()==null ? "":valPpFromDt.getValue().toString();
        String valT = valPpToDt.getValue()==null ? "":valPpToDt.getValue().toString();
        try {
                if (!valF.equalsIgnoreCase("")) {
                    Date datevalPpFromDt = sm.parse(valF);
                    txtPpFromDt = formatterdest.format(datevalPpFromDt);
                } else {
                    txtPpFromDt = "";
                }
                if (!valT.equalsIgnoreCase("")) {
                    Date date = sm.parse(valT);
                    txtPpToDt = formatterdest.format(date);
                } else {
                    txtPpToDt = "";
                }
              String txtUserRegion =regionDescIpt.getSubmittedValue()==null ? "%%":regionDescIpt.getSubmittedValue().toString();
              if(!txtUserRegion.equalsIgnoreCase("%%")){
                   txtUserRegionLOV = txtUserRegion.substring(4).trim();
              }else{
                  txtUserRegionLOV=txtUserRegion;
              }
              //txtUserRegionLOV =valUserRegionLOV.getValue()==null ? "%%":valUserRegionLOV.getValue().toString();
              txtUserLocLOV  =custLocationDescIpt.getSubmittedValue()==null ? "%%":custLocationDescIpt.getSubmittedValue().toString();
              txtUserAreaLOV =custAreaDescIpt.getSubmittedValue()==null ? "%%":custAreaDescIpt.getSubmittedValue().toString();
              
              txtUserCustTypeLOV =custTypeDescIpt.getSubmittedValue()==null ? "%%":custTypeDescIpt.getSubmittedValue().toString();
              txtUserCustGroupLOV=custGroupDescIpt.getSubmittedValue()==null ? "%%":custGroupDescIpt.getSubmittedValue().toString();
              txtUserCustDetailLOV=customerDetailTemp.getSubmittedValue()==null ? "%%":customerDetailTemp.getSubmittedValue().toString();
              txtProdCategoryLOV=prodCategoryDescInpt.getSubmittedValue()==null ? "":prodCategoryDescInpt.getSubmittedValue().toString();
              //txtProdClassLOV=valProdClassLOV.getValue()==null ? "":valProdClassLOV.getValue().toString();
              txtProdClassLOV=prodClassDescInpt.getSubmittedValue()==null ? "":prodClassDescInpt.getSubmittedValue().toString();
              txtProdClassLOV=txtProdClassLOV.replace("&", "%26");
              txtProdBrandLOV=prodBrandDescInpt.getSubmittedValue()==null ? "":prodBrandDescInpt.getSubmittedValue().toString();
              txtProdExtentionLOV=prodExtentionDescInpt.getSubmittedValue()==null ? "":prodExtentionDescInpt.getSubmittedValue().toString();
              txtProdPackagingLOV=prodPackagingDescInpt.getSubmittedValue()==null ? "":prodPackagingDescInpt.getSubmittedValue().toString();
              txtProdVariantLOV=prodVariantDescInpt.getSubmittedValue()==null ? "%%":prodVariantDescInpt.getSubmittedValue().toString();
              //txtProdDetailLOV=prodDetailDescInpt.getSubmittedValue()==null ? "%%":prodDetailDescInpt.getSubmittedValue().toString();
              
          } catch (ParseException e) {
              e.printStackTrace();
          }
        UserData userData =
        (UserData)JSFUtils.resolveExpression("#{UserData}");
        String domainName =
        userData.getReportDomain() == null ? "" : userData.getReportDomain();
        String Url=(domainName +
                   "/birt/frameset?__report=pp_report.rptdesign&company=" +
                   "&PP_date=" + txtPpFromDt.toString() + "&PP_date_end=" +
                     txtPpToDt.toString() + "&customer%20region=" + txtUserRegionLOV +
                   "&customer%20location=" + txtUserLocLOV +
                   "&customer%20area=" + txtUserAreaLOV +
                   "&customer%20type=" + txtUserCustTypeLOV +
                   "&customer%20group=" + txtUserCustGroupLOV +
                   "&customer%20detail=" + txtUserCustDetailLOV +
                   "&product%20category=" + txtProdCategoryLOV +
                   "&product%20class=" + txtProdClassLOV +
                   "&product%20brand=" + txtProdBrandLOV +
                   "&product%20extension=" + txtProdExtentionLOV +
                   "&product%20packaging=" + txtProdPackagingLOV +
                   "&product%20variant=" + txtProdVariantLOV +
                   "&product%20detail=" + txtProdDetailLOV);
        
        generateReportBtn.setDestination(Url+"&");
//        System.out.println("Url txtProdDetailLOV "+Url);
        AdfFacesContext.getCurrentInstance().addPartialTarget(generateReportBtn);
        AdfFacesContext.getCurrentInstance().addPartialTarget(prodDetailDescInpt);
    }

    public void setValProdDetailLOV(RichInputListOfValues valProdDetailLOV) {
        this.valProdDetailLOV = valProdDetailLOV;
    }

    public RichInputListOfValues getValProdDetailLOV() {
        return valProdDetailLOV;
    }

    public void cbProdDetail(ValueChangeEvent valueChangeEvent) {
       
    }

    public void setGeneratedURL(String generatedURL) {
        this.generatedURL = generatedURL;
    }
    
    public String getGeneratedURL() {   
//        ADFContext adfCtx = ADFContext.getCurrent();
//        Map pageFlowScope = adfCtx.getPageFlowScope();
//        Object val = pageFlowScope.get("ppFromDt");
//        Object val2 = pageFlowScope.get("ppToDt");
//        System.out.println(val.toString());
//        DCIteratorBinding dciter =
//            ADFUtils.findIterator("PromoDoubleReportForm1Iterator");
////        dciter.executeQuery();
//        
//        Row rows=dciter.getCurrentRow();
//        System.out.println(rows.getAttribute("RegionCode").toString());
//        ini klo diclear baru muncul help
       /*  BindingContainer bindings = BindingContext.getCurrent().getCurrentBindingsEntry();
        AttributeBinding fromdate = (AttributeBinding) bindings.getControlBinding("PeriodeStart");
        AttributeBinding todate = (AttributeBinding) bindings.getControlBinding("PeriodeEnd");
        AttributeBinding regionattr = (AttributeBinding) bindings.getControlBinding("RegionCode");
        String region= (String)regionattr.getInputValue();
        String valF= (String)fromdate.getInputValue()==null?"":(String)fromdate.getInputValue().toString();
        String valT= (String)todate.getInputValue()==null?"":(String)todate.getInputValue().toString();
        txtUserRegionLOV=region;
        SimpleDateFormat formatterdest = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
//        String valF = valPpFromDt.getSubmittedValue()==null ? "":valPpFromDt.getSubmittedValue().toString();
//        String valT = valPpToDt.getSubmittedValue()==null ? "":valPpToDt.getSubmittedValue().toString();
        try {
                if (!valF.equalsIgnoreCase("")) {
                    Date datevalPpFromDt = sm.parse(valF);
                    txtPpFromDt = formatterdest.format(datevalPpFromDt);
                } else {
                    txtPpFromDt = "";
                }
                if (!valT.equalsIgnoreCase("")) {
                    Date date = sm.parse(valT);
                    txtPpToDt = formatterdest.format(date);
                } else {
                    txtPpToDt = "";
                }
            
        }catch (Exception e){
            e.printStackTrace();
        } */
        UserData userData =
            (UserData)JSFUtils.resolveExpression("#{UserData}");
        String domainName =
            userData.getReportDomain() == null ? "" : userData.getReportDomain();
        setGeneratedURL(domainName +
                        "/birt/frameset?__report=pp_report.rptdesign&company=FDI" +
                        "&start_date=" + txtPpFromDt.toString() + "&end_date=" +
                          txtPpToDt.toString() + "&customerregion=" + txtUserRegionLOV +
                        "&customerlocation=" + txtUserLocLOV +
                        "&customerarea=" + txtUserAreaLOV +
                        "&customertype=" + txtUserCustTypeLOV +
                        "&customergroup=" + txtUserCustGroupLOV +
                        "&customerdetail=" + txtUserCustDetailLOV +
                        "&productcategory=" + txtProdCategoryLOV +
                        "&productclass=" + txtProdClassLOV +
                        "&productbrand=" + txtProdBrandLOV +
                        "&productextension=" + txtProdExtentionLOV +
                        "&productpackaging=" + txtProdPackagingLOV +
                        "&productvariant=" + txtProdVariantLOV +
                        "&productdetail=" + txtProdDetailLOV+"&");
        return generatedURL;
    }

    public void setTxtPpFromDt(String txtPpFromDt) {
        this.txtPpFromDt = txtPpFromDt;
    }

    public String getTxtPpFromDt() {
        return txtPpFromDt;
    }

    public void setTxtPpToDt(String txtPpToDt) {
        this.txtPpToDt = txtPpToDt;
    }

    public String getTxtPpToDt() {
        return txtPpToDt;
    }

    public void setGenerateReportBtn(RichGoButton generateReportBtn) {
        this.generateReportBtn = generateReportBtn;
    }

    public RichGoButton getGenerateReportBtn() {
        return generateReportBtn;
    }


    public void statusLov(ReturnPopupEvent returnPopupEvent) {
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
        
        UserData userData =
            (UserData)JSFUtils.resolveExpression("#{UserData}");
        String domainName =
            userData.getReportDomain() == null ? "" : userData.getReportDomain();
        String status=rw.getAttribute("Value").toString();
        if(status.equalsIgnoreCase("ALL")){
            status="";
        }
        String detailOrsummary=detailOrSummaryILOV.getValue()==null ? "":detailOrSummaryILOV.getValue().toString();
        String budgetType=budgetTypeILOV.getValue()==null ? "":budgetTypeILOV.getValue().toString();
        String budgetyear = budgetYearILOV.getValue()==null ? "":budgetYearILOV.getValue().toString();
        String sbc=budgetNameSBC.getValue().toString();
        if(sbc.equalsIgnoreCase("false")){
            String budgetName=budgetNameLov.getValue()==null ? "":budgetNameLov.getValue().toString();
            genYearlyBudgetBtn.setDestination(domainName+"/birt/frameset?__report=report_yearly_budget.rptdesign&format=pdf&detail="+detailOrsummary+"&budget_type="+budgetType+"&budget_year="+budgetyear+"&budget_name="+budgetName+"&status="+status+"");
        }else{
            genYearlyBudgetBtn.setDestination(domainName+"/birt/frameset?__report=report_yearly_budget.rptdesign&format=pdf&detail="+detailOrsummary+"&budget_type="+budgetType+"&budget_year="+budgetyear+"&budget_name="+status+"");
        }
        AdfFacesContext.getCurrentInstance().addPartialTarget(genYearlyBudgetBtn);
    }

    public void setStatusIpt(RichInputListOfValues statusIpt) {
        this.statusIpt = statusIpt;
    }

    public RichInputListOfValues getStatusIpt() {
        return statusIpt;
    }

    public void setRegionDescIpt(RichInputText regionDescIpt) {
        this.regionDescIpt = regionDescIpt;
    }

    public RichInputText getRegionDescIpt() {
        return regionDescIpt;
    }

    public void setCustAreaDescIpt(RichInputText custAreaDescIpt) {
        this.custAreaDescIpt = custAreaDescIpt;
    }

    public RichInputText getCustAreaDescIpt() {
        return custAreaDescIpt;
    }

    public void setCustLocationDescIpt(RichInputText custLocationDescIpt) {
        this.custLocationDescIpt = custLocationDescIpt;
    }

    public RichInputText getCustLocationDescIpt() {
        return custLocationDescIpt;
    }

    public void setProdCategoryDescInpt(RichInputText prodCategoryDescInpt) {
        this.prodCategoryDescInpt = prodCategoryDescInpt;
    }

    public RichInputText getProdCategoryDescInpt() {
        return prodCategoryDescInpt;
    }

    public void setProdClassDescInpt(RichInputText prodClassDescInpt) {
        this.prodClassDescInpt = prodClassDescInpt;
    }

    public RichInputText getProdClassDescInpt() {
        return prodClassDescInpt;
    }

    public void setProdBrandDescInpt(RichInputText prodBrandDescInpt) {
        this.prodBrandDescInpt = prodBrandDescInpt;
    }

    public RichInputText getProdBrandDescInpt() {
        return prodBrandDescInpt;
    }

    public void setProdExtentionDescInpt(RichInputText prodExtentionDescInpt) {
        this.prodExtentionDescInpt = prodExtentionDescInpt;
    }

    public RichInputText getProdExtentionDescInpt() {
        return prodExtentionDescInpt;
    }

    public void setProdPackagingDescInpt(RichInputText prodPackagingDescInpt) {
        this.prodPackagingDescInpt = prodPackagingDescInpt;
    }

    public RichInputText getProdPackagingDescInpt() {
        return prodPackagingDescInpt;
    }

    public void setProdVariantDescInpt(RichInputText prodVariantDescInpt) {
        this.prodVariantDescInpt = prodVariantDescInpt;
    }

    public RichInputText getProdVariantDescInpt() {
        return prodVariantDescInpt;
    }

    public void setProdDetailDescInpt(RichInputText prodDetailDescInpt) {
        this.prodDetailDescInpt = prodDetailDescInpt;
    }

    public RichInputText getProdDetailDescInpt() {
        return prodDetailDescInpt;
    }

    public void customerDetailTemp(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
    }

    public void setCustomerDetailTemp(RichInputText customerDetailTemp) {
        this.customerDetailTemp = customerDetailTemp;
    }

    public RichInputText getCustomerDetailTemp() {
        return customerDetailTemp;
    }

    public void setCustGroupDescIpt(RichInputText custGroupDescIpt) {
        this.custGroupDescIpt = custGroupDescIpt;
    }

    public RichInputText getCustGroupDescIpt() {
        return custGroupDescIpt;
    }

    public void setCustTypeDescIpt(RichInputText custTypeDescIpt) {
        this.custTypeDescIpt = custTypeDescIpt;
    }

    public RichInputText getCustTypeDescIpt() {
        return custTypeDescIpt;
    }

    public void clearParams(ActionEvent actionEvent) {
        DCBindingContainer bc = (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding iter =
                      (DCIteratorBinding)bc.findIteratorBinding("PromoDoubleReportForm1Iterator");
                                    ViewObject voTableData =
                                        iter.getViewObject();
        voTableData.clearCache();
        iter.executeQuery();
        AdfFacesContext xx=AdfFacesContext.getCurrentInstance();
        valUserRegionLOV.setSubmittedValue("");
        otRegionDesc.setValue("");
        valUserAreaLOV.setSubmittedValue("");
        otAreaDesc.setValue("");
        valUserLocLOV.setSubmittedValue("");
        otLocDesc.setValue("");
        valUserCustTypeLOV.setSubmittedValue("");
        otCustTypeDesc.setValue("");
        valUserCustGroupLOV.setSubmittedValue("");
        otCustGrpDesc.setValue("");
        valUserCustDetailLOV.setSubmittedValue("");
        otCustNameDesc.setValue("");
        valPpToDt.setSubmittedValue("");
        valPpFromDt.setSubmittedValue("");
        
        valProdBrandLOV.setSubmittedValue("");
        otBrand.setValue("");
        valProdCategoryLOV.setSubmittedValue("");
        otCategory.setValue("");
        valProdClassLOV.setSubmittedValue("");
        otClass.setValue("");
        valProdExtentionLOV.setSubmittedValue("");
        otExt.setValue("");
        valProdPackagingLOV.setSubmittedValue("");
        otPack.setValue("");
        valProdVariantLOV.setSubmittedValue("");
        otVariant.setValue("");
        valProdDetailLOV.setSubmittedValue("");
        otItemDesc.setValue("");
        
        xx.addPartialTarget(valProdBrandLOV);
        xx.addPartialTarget(valProdCategoryLOV);
        xx.addPartialTarget(valProdClassLOV);
        xx.addPartialTarget(valProdExtentionLOV);
        xx.addPartialTarget(valProdPackagingLOV);
        xx.addPartialTarget(valProdVariantLOV);
        xx.addPartialTarget(valProdDetailLOV);
        
        xx.addPartialTarget(valUserRegionLOV);        
        xx.addPartialTarget(valUserRegionLOV);
        xx.addPartialTarget(valUserAreaLOV);
        xx.addPartialTarget(valUserLocLOV);
        xx.addPartialTarget(valUserCustTypeLOV);
        xx.addPartialTarget(valUserCustGroupLOV);
        xx.addPartialTarget(valUserCustDetailLOV);
        xx.addPartialTarget(valPpToDt);
        xx.addPartialTarget(valPpFromDt);
        
        xx.addPartialTarget(otRegionDesc);         
        xx.addPartialTarget(otAreaDesc);         
        xx.addPartialTarget(otLocDesc);         
        xx.addPartialTarget(otCustTypeDesc);         
        xx.addPartialTarget(otCustGrpDesc);         
        xx.addPartialTarget(otCustNameDesc);      
        
        xx.addPartialTarget(otCategory);         
        xx.addPartialTarget(otClass);         
        xx.addPartialTarget(otBrand);         
        xx.addPartialTarget(otExt);         
        xx.addPartialTarget(otPack);         
        xx.addPartialTarget(otVariant);         
        xx.addPartialTarget(otItemDesc); 
    }

    public void openUrlEvent(ActionEvent actionEvent) {
        SimpleDateFormat formatterdest = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sm = new SimpleDateFormat("dd-MMM-yyyy");
        String valF = valPpFromDt.getValue()==null ? "":valPpFromDt.getValue().toString();
        String valT = valPpToDt.getValue()==null ? "":valPpToDt.getValue().toString();
        try {
                if (!valF.equalsIgnoreCase("")) {
                    Date datevalPpFromDt = sm.parse(valF);
                    txtPpFromDt = formatterdest.format(datevalPpFromDt);
                } else {
                    txtPpFromDt = "";
                }
                if (!valT.equalsIgnoreCase("")) {
                    Date date = sm.parse(valT);
                    txtPpToDt = formatterdest.format(date);
                } else {
                    txtPpToDt = "";
                }
        }catch (Exception e){
            e.printStackTrace();
        }
        
        txtUserRegionLOV=valUserRegionLOV.getValue()==null?"":valUserRegionLOV.getValue().toString();
//        System.out.println("region "+valUserRegionLOV.getValue()==null?"":valUserRegionLOV.getValue().toString());
        txtUserLocLOV=valUserLocLOV.getValue()==null ? "" :valUserLocLOV.getValue().toString();
        txtUserAreaLOV=valUserAreaLOV.getValue() == null ? "" :valUserAreaLOV.getValue().toString();
        txtUserCustTypeLOV=valUserCustTypeLOV.getValue() == null ? "" :valUserCustTypeLOV.getValue().toString();
        txtUserCustGroupLOV=valUserCustGroupLOV.getValue() == null ? "" : valUserCustGroupLOV.getValue().toString();
        txtUserCustDetailLOV=valUserCustDetailLOV.getValue() == null ? "":valUserCustDetailLOV.getValue().toString();
        txtProdCategoryLOV=valProdCategoryLOV.getValue() == null ? "":valProdCategoryLOV.getValue().toString();
        txtProdClassLOV=valProdClassLOV.getValue() == null ? "" : valProdClassLOV.getValue().toString();
        txtProdBrandLOV=valProdBrandLOV.getValue() == null ? "" : valProdBrandLOV.getValue().toString();
        txtProdExtentionLOV=valProdExtentionLOV.getValue() == null ? "" :valProdExtentionLOV.getValue().toString();
        txtProdPackagingLOV=valProdPackagingLOV.getValue() == null ? "" :valProdPackagingLOV.getValue().toString();
        txtProdVariantLOV=valProdVariantLOV.getValue() == null ? "" : valProdVariantLOV.getValue().toString();
        txtProdDetailLOV=valProdDetailLOV.getValue() == null ? "" :valProdDetailLOV.getValue().toString();
        UserData userData =
            (UserData)JSFUtils.resolveExpression("#{UserData}");
        String domainName =
            userData.getReportDomain() == null ? "" : userData.getReportDomain();
        String URL=(domainName +
                        "/birt/frameset?__report=pp_report.rptdesign&company=FDI" +
                        "&start_date=" + txtPpFromDt.toString() + "&end_date=" +
                          txtPpToDt.toString() + "&customerregion=" + txtUserRegionLOV +
                        "&customerlocation=" + txtUserLocLOV +
                        "&customerarea=" + txtUserAreaLOV +
                        "&customertype=" + txtUserCustTypeLOV +
                        "&customergroup=" + txtUserCustGroupLOV +
                        "&customerdetail=" + txtUserCustDetailLOV +
                        "&productcategory=" + txtProdCategoryLOV +
                        "&productclass=" + txtProdClassLOV +
                        "&productbrand=" + txtProdBrandLOV +
                        "&productextension=" + txtProdExtentionLOV +
                        "&productpackaging=" + txtProdPackagingLOV +
                        "&productvariant=" + txtProdVariantLOV +
                        "&productdetail=" + txtProdDetailLOV+"&");
        generateReportBtn.setDestination(URL);
        AdfFacesContext.getCurrentInstance().addPartialTarget(generateReportBtn);
        FacesContext fctx = FacesContext.getCurrentInstance();
         String rowKeyString = "";
         Map<String, Object> params = new HashMap<String, Object>();
         params.put("rowKey",rowKeyString);
         ExtendedRenderKitService erks =
         Service.getRenderKitService(fctx,
         ExtendedRenderKitService.class);
         StringBuilder script = new StringBuilder();
         script.append("window.open(\""+URL+"\");");
         erks.addScript(FacesContext.getCurrentInstance(),
         script.toString()); 
       
    }

    public void regionClear(ActionEvent actionEvent) {
        valUserRegionLOV.setSubmittedValue("");
        otRegionDesc.setValue("");
        DCBindingContainer bc = (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding iter =
                      (DCIteratorBinding)bc.findIteratorBinding("PromoDoubleReportForm1Iterator");
                                    ViewObject voTableData =
                                        iter.getViewObject();
        voTableData.clearCache();
        iter.executeQuery();
        AdfFacesContext.getCurrentInstance().addPartialTarget(valUserRegionLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valUserAreaLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valUserLocLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valUserCustTypeLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valUserCustGroupLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valUserCustDetailLOV);    
    }

    public void clearArea(ActionEvent actionEvent) {
        valUserAreaLOV.setSubmittedValue("");
        otAreaDesc.setValue("");
        DCBindingContainer bc = (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding iter =
                      (DCIteratorBinding)bc.findIteratorBinding("PromoDoubleReportForm1Iterator");
                                    ViewObject voTableData =
                                        iter.getViewObject();
        voTableData.clearCache();
        iter.executeQuery();
        AdfFacesContext.getCurrentInstance().addPartialTarget(valUserRegionLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valUserAreaLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valUserLocLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valUserCustTypeLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valUserCustGroupLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valUserCustDetailLOV);    
    }
    
    public void clearLoc(ActionEvent actionEvent) {
        valUserLocLOV.setSubmittedValue("");
        otLocDesc.setValue("");
        DCBindingContainer bc = (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding iter =
                      (DCIteratorBinding)bc.findIteratorBinding("PromoDoubleReportForm1Iterator");
                                    ViewObject voTableData =
                                        iter.getViewObject();
        voTableData.clearCache();
        iter.executeQuery();
        AdfFacesContext.getCurrentInstance().addPartialTarget(valUserRegionLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valUserAreaLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valUserLocLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valUserCustTypeLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valUserCustGroupLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valUserCustDetailLOV);    
    }
    public void clearCustype(ActionEvent actionEvent) {
        valUserCustTypeLOV.setSubmittedValue("");
        otCustTypeDesc.setValue("");
        AdfFacesContext.getCurrentInstance().addPartialTarget(valUserRegionLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valUserAreaLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valUserLocLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valUserCustTypeLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valUserCustGroupLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valUserCustDetailLOV);    
    }
    
    public void clearCusGroup(ActionEvent actionEvent) {
        valUserCustGroupLOV.setSubmittedValue("");
        otCustGrpDesc.setValue("");
        DCBindingContainer bc = (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding iter =
                      (DCIteratorBinding)bc.findIteratorBinding("PromoDoubleReportForm1Iterator");
                                    ViewObject voTableData =
                                        iter.getViewObject();
        voTableData.clearCache();
        iter.executeQuery();
        AdfFacesContext.getCurrentInstance().addPartialTarget(valUserRegionLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valUserAreaLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valUserLocLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valUserCustTypeLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valUserCustGroupLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valUserCustDetailLOV);    
    }
    public void clearCusName(ActionEvent actionEvent) {
        valUserCustDetailLOV.setSubmittedValue("");
        otCustNameDesc.setValue("");
        DCBindingContainer bc = (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding iter =
                      (DCIteratorBinding)bc.findIteratorBinding("PromoDoubleReportForm1Iterator");
                                    ViewObject voTableData =
                                        iter.getViewObject();
        voTableData.clearCache();
        iter.executeQuery();
        AdfFacesContext.getCurrentInstance().addPartialTarget(valUserRegionLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valUserAreaLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valUserLocLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valUserCustTypeLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valUserCustGroupLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valUserCustDetailLOV);    
    }
    
    public void categoryClear(ActionEvent actionEvent) {
        valProdCategoryLOV.setSubmittedValue("");
        otCategory.setValue("");
        DCBindingContainer bc = (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding iter =
                      (DCIteratorBinding)bc.findIteratorBinding("PromoDoubleReportForm1Iterator");
                                    ViewObject voTableData =
                                        iter.getViewObject();
        voTableData.clearCache();
        iter.executeQuery();
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdCategoryLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdClassLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdBrandLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdExtentionLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdPackagingLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdVariantLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdDetailLOV);
    }

    public void clearClass(ActionEvent actionEvent) {
        valProdClassLOV.setSubmittedValue("");
        otClass.setValue("");
        DCBindingContainer bc = (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding iter =
                      (DCIteratorBinding)bc.findIteratorBinding("PromoDoubleReportForm1Iterator");
                                    ViewObject voTableData =
                                        iter.getViewObject();
        voTableData.clearCache();
        iter.executeQuery();
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdCategoryLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdClassLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdBrandLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdExtentionLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdPackagingLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdVariantLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdDetailLOV);
    }
    
    public void clearBrand(ActionEvent actionEvent) {
        valProdBrandLOV.setSubmittedValue("");
        otBrand.setValue("");
        DCBindingContainer bc = (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding iter =
                      (DCIteratorBinding)bc.findIteratorBinding("PromoDoubleReportForm1Iterator");
                                    ViewObject voTableData =
                                        iter.getViewObject();
        voTableData.clearCache();
        iter.executeQuery();
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdCategoryLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdClassLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdBrandLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdExtentionLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdPackagingLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdVariantLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdDetailLOV);
    }
    public void clearExtention(ActionEvent actionEvent) {
        valProdExtentionLOV.setSubmittedValue("");
        otExt.setValue("");
        DCBindingContainer bc = (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding iter =
                      (DCIteratorBinding)bc.findIteratorBinding("PromoDoubleReportForm1Iterator");
                                    ViewObject voTableData =
                                        iter.getViewObject();
        voTableData.clearCache();
        iter.executeQuery();
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdCategoryLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdClassLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdBrandLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdExtentionLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdPackagingLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdVariantLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdDetailLOV);
    }
    
    public void clearPackaging(ActionEvent actionEvent) {
        valProdPackagingLOV.setSubmittedValue("");
        otPack.setValue("");
        DCBindingContainer bc = (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding iter =
                      (DCIteratorBinding)bc.findIteratorBinding("PromoDoubleReportForm1Iterator");
                                    ViewObject voTableData =
                                        iter.getViewObject();
        voTableData.clearCache();
        iter.executeQuery();
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdCategoryLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdClassLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdBrandLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdExtentionLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdPackagingLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdVariantLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdDetailLOV);
    }
    public void clearVariant(ActionEvent actionEvent) {
        valProdVariantLOV.setSubmittedValue("");
        otVariant.setValue("");
        DCBindingContainer bc = (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding iter =
                      (DCIteratorBinding)bc.findIteratorBinding("PromoDoubleReportForm1Iterator");
                                    ViewObject voTableData =
                                        iter.getViewObject();
        voTableData.clearCache();
        iter.executeQuery();
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdCategoryLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdClassLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdBrandLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdExtentionLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdPackagingLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdVariantLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdDetailLOV);
    }
    public void clearItems(ActionEvent actionEvent) {
        valProdDetailLOV.setSubmittedValue("");
        otItemDesc.setValue("");
        DCBindingContainer bc = (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding iter =
                      (DCIteratorBinding)bc.findIteratorBinding("PromoDoubleReportForm1Iterator");
                                    ViewObject voTableData =
                                        iter.getViewObject();
        voTableData.clearCache();
        iter.executeQuery();
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdCategoryLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdClassLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdBrandLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdExtentionLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdPackagingLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdVariantLOV);
        AdfFacesContext.getCurrentInstance().addPartialTarget(valProdDetailLOV);
    }

    public void setOtRegionDesc(RichOutputText otRegionDesc) {
        this.otRegionDesc = otRegionDesc;
    }

    public RichOutputText getOtRegionDesc() {
        return otRegionDesc;
    }

    public void regionReturnListener(ReturnPopupEvent returnPopupEvent) {
        RichInputListOfValues lovField = (RichInputListOfValues)returnPopupEvent.getSource(); 
        ListOfValuesModel lovModel =  lovField.getModel(); 
        CollectionModel collectionModel = lovModel.getTableModel().getCollectionModel();   
        JUCtrlHierBinding treeBinding = (JUCtrlHierBinding) collectionModel.getWrappedData();
        RowKeySet rks = (RowKeySet) returnPopupEvent.getReturnValue();
        List tableRowKey = (List) rks.iterator().next(); 
        DCIteratorBinding dciter = treeBinding.getDCIteratorBinding(); 
        Key key = (Key) tableRowKey.get(0); 
        Row rw =  dciter.findRowByKeyString(key.toStringFormat(true));
        String rwDesc = (String)rw.getAttribute("RegionLabel");
        otRegionDesc.setValue(rwDesc);
        AdfFacesContext.getCurrentInstance().addPartialTarget(otRegionDesc);
    }

    public void setOtAreaDesc(RichOutputText otAreaDesc) {
        this.otAreaDesc = otAreaDesc;
    }

    public RichOutputText getOtAreaDesc() {
        return otAreaDesc;
    }

    public void setOtLocDesc(RichOutputText otLocDesc) {
        this.otLocDesc = otLocDesc;
    }

    public RichOutputText getOtLocDesc() {
        return otLocDesc;
    }

    public void setOtCustTypeDesc(RichOutputText otCustTypeDesc) {
        this.otCustTypeDesc = otCustTypeDesc;
    }

    public RichOutputText getOtCustTypeDesc() {
        return otCustTypeDesc;
    }

    public void setOtCustGrpDesc(RichOutputText otCustGrpDesc) {
        this.otCustGrpDesc = otCustGrpDesc;
    }

    public RichOutputText getOtCustGrpDesc() {
        return otCustGrpDesc;
    }

    public void setOtCustNameDesc(RichOutputText otCustNameDesc) {
        this.otCustNameDesc = otCustNameDesc;
    }

    public RichOutputText getOtCustNameDesc() {
        return otCustNameDesc;
    }

    public void areaReturnListener(ReturnPopupEvent returnPopupEvent) {
        RichInputListOfValues lovField = (RichInputListOfValues)returnPopupEvent.getSource(); 
        ListOfValuesModel lovModel =  lovField.getModel(); 
        CollectionModel collectionModel = lovModel.getTableModel().getCollectionModel();   
        JUCtrlHierBinding treeBinding = (JUCtrlHierBinding) collectionModel.getWrappedData();
        RowKeySet rks = (RowKeySet) returnPopupEvent.getReturnValue();
        List tableRowKey = (List) rks.iterator().next(); 
        DCIteratorBinding dciter = treeBinding.getDCIteratorBinding(); 
        Key key = (Key) tableRowKey.get(0); 
        Row rw =  dciter.findRowByKeyString(key.toStringFormat(true));
        String rwDesc = (String)rw.getAttribute("AreaLabel");
        otAreaDesc.setValue(rwDesc);
        AdfFacesContext.getCurrentInstance().addPartialTarget(otAreaDesc);
    }

    public void locationReturnListener(ReturnPopupEvent returnPopupEvent) {
        RichInputListOfValues lovField = (RichInputListOfValues)returnPopupEvent.getSource(); 
        ListOfValuesModel lovModel =  lovField.getModel(); 
        CollectionModel collectionModel = lovModel.getTableModel().getCollectionModel();   
        JUCtrlHierBinding treeBinding = (JUCtrlHierBinding) collectionModel.getWrappedData();
        RowKeySet rks = (RowKeySet) returnPopupEvent.getReturnValue();
        List tableRowKey = (List) rks.iterator().next(); 
        DCIteratorBinding dciter = treeBinding.getDCIteratorBinding(); 
        Key key = (Key) tableRowKey.get(0); 
        Row rw =  dciter.findRowByKeyString(key.toStringFormat(true));
        String rwDesc = (String)rw.getAttribute("LocLabel");
        otLocDesc.setValue(rwDesc);
        AdfFacesContext.getCurrentInstance().addPartialTarget(otLocDesc);
    }

    public void custTypeReturnListener(ReturnPopupEvent returnPopupEvent) {
        RichInputListOfValues lovField = (RichInputListOfValues)returnPopupEvent.getSource(); 
        ListOfValuesModel lovModel =  lovField.getModel(); 
        CollectionModel collectionModel = lovModel.getTableModel().getCollectionModel();   
        JUCtrlHierBinding treeBinding = (JUCtrlHierBinding) collectionModel.getWrappedData();
        RowKeySet rks = (RowKeySet) returnPopupEvent.getReturnValue();
        List tableRowKey = (List) rks.iterator().next(); 
        DCIteratorBinding dciter = treeBinding.getDCIteratorBinding(); 
        Key key = (Key) tableRowKey.get(0); 
        Row rw =  dciter.findRowByKeyString(key.toStringFormat(true));
        String rwDesc = (String)rw.getAttribute("Description");
        otCustTypeDesc.setValue(rwDesc);
        AdfFacesContext.getCurrentInstance().addPartialTarget(otCustTypeDesc);
    }

    public void custGroupReturnListener(ReturnPopupEvent returnPopupEvent) {
        RichInputListOfValues lovField = (RichInputListOfValues)returnPopupEvent.getSource(); 
        ListOfValuesModel lovModel =  lovField.getModel(); 
        CollectionModel collectionModel = lovModel.getTableModel().getCollectionModel();   
        JUCtrlHierBinding treeBinding = (JUCtrlHierBinding) collectionModel.getWrappedData();
        RowKeySet rks = (RowKeySet) returnPopupEvent.getReturnValue();
        List tableRowKey = (List) rks.iterator().next(); 
        DCIteratorBinding dciter = treeBinding.getDCIteratorBinding(); 
        Key key = (Key) tableRowKey.get(0); 
        Row rw =  dciter.findRowByKeyString(key.toStringFormat(true));
        String rwDesc = (String)rw.getAttribute("Description");
        otCustGrpDesc.setValue(rwDesc);
        AdfFacesContext.getCurrentInstance().addPartialTarget(otCustGrpDesc);
    }

    public void custNameReturnListener(ReturnPopupEvent returnPopupEvent) {
        RichInputListOfValues lovField = (RichInputListOfValues)returnPopupEvent.getSource(); 
        ListOfValuesModel lovModel =  lovField.getModel(); 
        CollectionModel collectionModel = lovModel.getTableModel().getCollectionModel();   
        JUCtrlHierBinding treeBinding = (JUCtrlHierBinding) collectionModel.getWrappedData();
        RowKeySet rks = (RowKeySet) returnPopupEvent.getReturnValue();
        List tableRowKey = (List) rks.iterator().next(); 
        DCIteratorBinding dciter = treeBinding.getDCIteratorBinding(); 
        Key key = (Key) tableRowKey.get(0); 
        Row rw =  dciter.findRowByKeyString(key.toStringFormat(true));
        String rwDesc = (String)rw.getAttribute("CustomerFullName");
        otCustNameDesc.setValue(rwDesc);
        AdfFacesContext.getCurrentInstance().addPartialTarget(otCustNameDesc);
    }

    public void setOtCategory(RichOutputText otCategory) {
        this.otCategory = otCategory;
    }

    public RichOutputText getOtCategory() {
        return otCategory;
    }

    public void setOtClass(RichOutputText otClass) {
        this.otClass = otClass;
    }

    public RichOutputText getOtClass() {
        return otClass;
    }

    public void setOtBrand(RichOutputText otBrand) {
        this.otBrand = otBrand;
    }

    public RichOutputText getOtBrand() {
        return otBrand;
    }

    public void setOtExt(RichOutputText otExt) {
        this.otExt = otExt;
    }

    public RichOutputText getOtExt() {
        return otExt;
    }

    public void setOtPack(RichOutputText otPack) {
        this.otPack = otPack;
    }

    public RichOutputText getOtPack() {
        return otPack;
    }

    public void setOtVariant(RichOutputText otVariant) {
        this.otVariant = otVariant;
    }

    public RichOutputText getOtVariant() {
        return otVariant;
    }

    public void setOtItemDesc(RichOutputText otItemDesc) {
        this.otItemDesc = otItemDesc;
    }

    public RichOutputText getOtItemDesc() {
        return otItemDesc;
    }

    public void categoryReturnListener(ReturnPopupEvent returnPopupEvent) {
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
        otCategory.setValue(rwDesc);
        AdfFacesContext.getCurrentInstance().addPartialTarget(otCategory);
    }

    public void classReturnListener(ReturnPopupEvent returnPopupEvent) {
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
        otClass.setValue(rwDesc);
        AdfFacesContext.getCurrentInstance().addPartialTarget(otClass);
    }

    public void brandReturnListener(ReturnPopupEvent returnPopupEvent) {
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
        otBrand.setValue(rwDesc);
        AdfFacesContext.getCurrentInstance().addPartialTarget(otBrand);
    }

    public void extReturnListener(ReturnPopupEvent returnPopupEvent) {
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
        otExt.setValue(rwDesc);
        AdfFacesContext.getCurrentInstance().addPartialTarget(otExt);
    }

    public void packReturnListener(ReturnPopupEvent returnPopupEvent) {
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
        otPack.setValue(rwDesc);
        AdfFacesContext.getCurrentInstance().addPartialTarget(otPack);
    }

    public void variantReturnListener(ReturnPopupEvent returnPopupEvent) {
        RichInputListOfValues lovField = (RichInputListOfValues)returnPopupEvent.getSource(); 
        ListOfValuesModel lovModel =  lovField.getModel(); 
        CollectionModel collectionModel = lovModel.getTableModel().getCollectionModel();   
        JUCtrlHierBinding treeBinding = (JUCtrlHierBinding) collectionModel.getWrappedData();
        RowKeySet rks = (RowKeySet) returnPopupEvent.getReturnValue();
        List tableRowKey = (List) rks.iterator().next(); 
        DCIteratorBinding dciter = treeBinding.getDCIteratorBinding(); 
        Key key = (Key) tableRowKey.get(0); 
        Row rw =  dciter.findRowByKeyString(key.toStringFormat(true));
        String rwDesc = (String)rw.getAttribute("SetVariantDesc");
        otVariant.setValue(rwDesc);
        AdfFacesContext.getCurrentInstance().addPartialTarget(otVariant);
    }

    public void itemReturnListener(ReturnPopupEvent returnPopupEvent) {
        RichInputListOfValues lovField = (RichInputListOfValues)returnPopupEvent.getSource(); 
        ListOfValuesModel lovModel =  lovField.getModel(); 
        CollectionModel collectionModel = lovModel.getTableModel().getCollectionModel();   
        JUCtrlHierBinding treeBinding = (JUCtrlHierBinding) collectionModel.getWrappedData();
        RowKeySet rks = (RowKeySet) returnPopupEvent.getReturnValue();
        List tableRowKey = (List) rks.iterator().next(); 
        DCIteratorBinding dciter = treeBinding.getDCIteratorBinding(); 
        Key key = (Key) tableRowKey.get(0); 
        Row rw =  dciter.findRowByKeyString(key.toStringFormat(true));
        String rwDesc = (String)rw.getAttribute("ItemDescription");
        otItemDesc.setValue(rwDesc);
        AdfFacesContext.getCurrentInstance().addPartialTarget(otItemDesc);
    }
}
