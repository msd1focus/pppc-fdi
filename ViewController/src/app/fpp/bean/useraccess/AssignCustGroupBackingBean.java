package app.fpp.bean.useraccess;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.share.ADFContext;

import oracle.adf.view.rich.context.AdfFacesContext;

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

import oracle.jbo.Key;
import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;
import oracle.jbo.domain.DBSequence;

public class AssignCustGroupBackingBean implements Serializable {

    // Shuttle operations
    @SuppressWarnings("compatibility:4429120787595570625")
    private static final long serialVersionUID = 1L;
    List selectedCustGroup;
    List allCustGroup;
    Boolean refreshSelectedList = false;
    private final String SELECTED_CUST_GROUP_ITERATOR =
        "AppUserCustGroupView1Iterator";
    private final String ALL_CUST_GROUP_ITERATOR = "AllUserMgmtCustGroupShuttleView1Iterator";
    
    public AssignCustGroupBackingBean() {
        super();
    }

    public BindingContainer getBindings() {
        return BindingContext.getCurrent().getCurrentBindingsEntry();
    }

    public void setSelectedCustGroup(List selectedCustGroup) {
        this.selectedCustGroup = selectedCustGroup;
    }

    public List getSelectedCustGroup() {

        if (selectedCustGroup == null || refreshSelectedList) {
            selectedCustGroup =
                    attributeListForIterator(SELECTED_CUST_GROUP_ITERATOR, "CustGroup");
        }
        return selectedCustGroup;
    }

    public void setAllCustGroup(List allCustGroup) {
        this.allCustGroup = allCustGroup;
    }

    public List getAllCustGroup() {
        if (allCustGroup == null) {
            allCustGroup = selectItemsForIterator(ALL_CUST_GROUP_ITERATOR, "CustGroupCode", "CustGroupLabel");
        }
        return allCustGroup;
    }

    public void refreshSelectedList(ValueChangeEvent e) {
        refreshSelectedList = true;
    }

    // Shuttle operations
    public static List attributeListForIterator(String iteratorName,
                                                String valueAttrName) {
        BindingContext bc = BindingContext.getCurrent();
        DCBindingContainer binding =
            (DCBindingContainer)bc.getCurrentBindingsEntry();
        DCIteratorBinding iter = binding.findIteratorBinding(iteratorName);
        List attributeList = new ArrayList();
        for (Row r : iter.getAllRowsInRange()) {
            attributeList.add(r.getAttribute(valueAttrName));
        }
        return attributeList;
    }

    public static List<SelectItem> selectItemsForIterator(String iteratorName,
                                                          String valueAttrName,
                                                          String displayAttrName) {
        BindingContext bc = BindingContext.getCurrent();
        DCBindingContainer binding =
            (DCBindingContainer)bc.getCurrentBindingsEntry();
        DCIteratorBinding iter = binding.findIteratorBinding(iteratorName);
        List<SelectItem> selectItems = new ArrayList<SelectItem>();
        for (Row r : iter.getAllRowsInRange()) {
            selectItems.add(new SelectItem(r.getAttribute(valueAttrName),
                                           (String)r.getAttribute(displayAttrName)));
        }
        return selectItems;
    }

    public String processShuttle() {
        FacesContext fctx = FacesContext.getCurrentInstance();
        String closeAfter = "close";
        BindingContext bctx = BindingContext.getCurrent();
        DCBindingContainer binding =
            (DCBindingContainer)bctx.getCurrentBindingsEntry();
        DCIteratorBinding iter =
            (DCIteratorBinding)binding.get(SELECTED_CUST_GROUP_ITERATOR);

        //Removing all rows
        for (Row r : iter.getAllRowsInRange()) {
            r.remove();
        }

        if (this.getSelectedCustGroup().size() > 0) {
            for (int i = 0; i < getSelectedCustGroup().size(); i++) {
                //List parentRegional = retrieveParentRegionalCustomer((String)getSelectedCustGroup().get(i));
                Row row = iter.getRowSetIterator().createRow();
                row.setNewRowState(Row.STATUS_INITIALIZED);
                DCIteratorBinding iterAllCustGroup =
                    (DCIteratorBinding)binding.get(ALL_CUST_GROUP_ITERATOR);
                for (Row r : iterAllCustGroup.getAllRowsInRange()) {
                    if (getSelectedCustGroup().get(i).equals(r.getAttribute("CustGroupCode"))) {
                        row.setAttribute("CustGroupDesc",
                                         r.getAttribute("CustGroupLabel"));
                    }
                }
                row.setAttribute("UserName", getUserName());
                row.setAttribute("CustGroup", getSelectedCustGroup().get(i));
                
                iter.getRowSetIterator().insertRow(row);
                iter.setCurrentRowWithKey(row.getKey().toStringFormat(true));
            }
        }
        
        String ok = doCommit();
        
        AdfFacesContext context = AdfFacesContext.getCurrentInstance();
        Map pfScope = context.getPageFlowScope();
        pfScope.put("actionReturn", "applied");
        
        return closeAfter;
    }

    public String processCancel() {
        AdfFacesContext context = AdfFacesContext.getCurrentInstance();
        Map pfScope = context.getPageFlowScope();
        pfScope.put("actionReturn", "cancel");
        
        String closeAfter = "close";
        //String cancel = doRollback();
        return closeAfter;
    }

    public String doCommit() {
        BindingContainer bindings = getBindings();
        OperationBinding operationBinding =
            bindings.getOperationBinding("Commit");
        operationBinding.execute();
        if (!operationBinding.getErrors().isEmpty()) {
            return null;
        }
        return null;
    }

    public String doRollback() {
        BindingContainer bindings = getBindings();
        OperationBinding operationBinding =
            bindings.getOperationBinding("Rollback");
        operationBinding.execute();
        if (!operationBinding.getErrors().isEmpty()) {
            return null;
        }
        return null;
    }

    public String getUserName() {
        ADFContext adfCtx = ADFContext.getCurrent();
        Map pageFlowScope = adfCtx.getPageFlowScope();
        String userName = (String)pageFlowScope.get("userName");
        return userName;
    }
    
    public List<String> retrieveParentRegionalCustomer(String keyValue) {
        BindingContext bctx = BindingContext.getCurrent();
        DCBindingContainer binding =
            (DCBindingContainer)bctx.getCurrentBindingsEntry();
        DCIteratorBinding iterParentReg =
            (DCIteratorBinding)binding.get(ALL_CUST_GROUP_ITERATOR); 
        //ERR: Duplicate data kalau ditambahin kolom REG, AREA dan LOC di ALL_CUST_GROUP_ITERATOR
        
        Key key = new Key(new Object[] { keyValue });
        RowSetIterator rsi = iterParentReg.getRowSetIterator();
        Row row = rsi.findByKey(key, 1)[0];
        
        List<String> parentRegionalCustomerList = new ArrayList<String>();
        parentRegionalCustomerList.add((String)row.getAttribute("RegionCode"));
        parentRegionalCustomerList.add((String)row.getAttribute("AreaCode"));
        parentRegionalCustomerList.add((String)row.getAttribute("LocationCode"));
        
        return parentRegionalCustomerList;
    }
}
