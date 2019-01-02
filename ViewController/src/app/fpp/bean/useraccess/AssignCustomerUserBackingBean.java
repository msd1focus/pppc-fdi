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

public class AssignCustomerUserBackingBean implements Serializable {

    // Shuttle operations
    @SuppressWarnings("compatibility:4429120787595570625")
    private static final long serialVersionUID = 1L;
    List selectedCustomers;
    List allCustomers;
    Boolean refreshSelectedList = false;
    private final String SELECTED_CUSTOMER_ITERATOR =
        "AppUserCustView1Iterator";
    private final String ALL_CUSTOMER_ITERATOR = "AllUserMgmtCustomerShuttleView1Iterator";
    
    public AssignCustomerUserBackingBean() {
        super();
    }


    public BindingContainer getBindings() {
        return BindingContext.getCurrent().getCurrentBindingsEntry();
    }

    public void setSelectedCustomers(List selectedCustomers) {
        this.selectedCustomers = selectedCustomers;
    }

    public List getSelectedCustomers() {
        if (selectedCustomers == null || refreshSelectedList) {
            selectedCustomers =
                    attributeListForIterator(SELECTED_CUSTOMER_ITERATOR, "CustomerId");
        }
        return selectedCustomers;
    }

    public void setAllCustomers(List allCustomers) {
        this.allCustomers = allCustomers;
    }

    public List getAllCustomers() {
        if (allCustomers == null) {
            allCustomers = selectItemsForIterator(ALL_CUSTOMER_ITERATOR, "CustomerId", "CustomerFullName");
        }
        return allCustomers;
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
            (DCIteratorBinding)binding.get(SELECTED_CUSTOMER_ITERATOR);

        //Removing all rows
        for (Row r : iter.getAllRowsInRange()) {
            r.remove();
        }
        
        if (this.getSelectedCustomers().size() > 0) {
            for (int i = 0; i < getSelectedCustomers().size(); i++) {
                List parentRegional = retrieveParentRegionalCustomer((oracle.jbo.domain.Number)getSelectedCustomers().get(i));
                Row row = iter.getRowSetIterator().createRow();
                row.setNewRowState(Row.STATUS_INITIALIZED);
                DCIteratorBinding iterAllCustomer =
                    (DCIteratorBinding)binding.get(ALL_CUSTOMER_ITERATOR);
                for (Row r : iterAllCustomer.getAllRowsInRange()) {
                    if (getSelectedCustomers().get(i).equals(r.getAttribute("CustomerId"))) {
                        row.setAttribute("CustomerDesc",
                                         r.getAttribute("CustomerFullName"));
                    }
                }
                row.setAttribute("CustomerId", getSelectedCustomers().get(i));
                row.setAttribute("UserName", getUserName());
                row.setAttribute("RegionCode", parentRegional.get(0));
                row.setAttribute("AreaCode", parentRegional.get(1));
                row.setAttribute("LocationCode", parentRegional.get(2));
                
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
    
    public List<String> retrieveParentRegionalCustomer(oracle.jbo.domain.Number keyValue) {
        BindingContext bctx = BindingContext.getCurrent();
        DCBindingContainer binding =
            (DCBindingContainer)bctx.getCurrentBindingsEntry();
        DCIteratorBinding iterParentReg =
            (DCIteratorBinding)binding.get(ALL_CUSTOMER_ITERATOR);
        
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
