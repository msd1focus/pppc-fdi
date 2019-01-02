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

import oracle.jbo.Row;

public class AssignRegionUserBackingBean implements Serializable {

    // Shuttle operations
    @SuppressWarnings("compatibility:4429120787595570625")
    private static final long serialVersionUID = 1L;
    List selectedRegions;
    List allRegions;
    Boolean refreshSelectedList = false;
    private final String SELECTED_REGION_ITERATOR =
        "AppUserRegionView1Iterator";
    private final String ALL_REGION_ITERATOR = "AllUserMgmtRegionShuttleView1Iterator";
    
    public AssignRegionUserBackingBean() {
        super();
    }

    public BindingContainer getBindings() {
        return BindingContext.getCurrent().getCurrentBindingsEntry();
    }

    public void setSelectedRegions(List selectedRegions) {
        this.selectedRegions = selectedRegions;
    }

    public List getSelectedRegions() {

        if (selectedRegions == null || refreshSelectedList) {
            selectedRegions =
                    attributeListForIterator(SELECTED_REGION_ITERATOR, "RegionCode");
        }
        return selectedRegions;
    }

    public void setAllRegions(List allRegions) {
        this.allRegions = allRegions;
    }

    public List getAllRegions() {
        if (allRegions == null) {
            allRegions = selectItemsForIterator(ALL_REGION_ITERATOR, "RegionCode", "RegionLabel");
        }
        return allRegions;
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
            (DCIteratorBinding)binding.get(SELECTED_REGION_ITERATOR);

        //Removing all rows
        for (Row r : iter.getAllRowsInRange()) {
            r.remove();
        }

        if (this.getSelectedRegions().size() > 0) {
            for (int i = 0; i < getSelectedRegions().size(); i++) {
                Row row = iter.getRowSetIterator().createRow();
                row.setNewRowState(Row.STATUS_INITIALIZED);
                DCIteratorBinding iterAllCategory =
                    (DCIteratorBinding)binding.get(ALL_REGION_ITERATOR);
                for (Row r : iterAllCategory.getAllRowsInRange()) {
                    if (getSelectedRegions().get(i).equals(r.getAttribute("RegionCode"))) {
                        row.setAttribute("RegionDesc",
                                         r.getAttribute("RegionLabel"));
                    }
                }
                row.setAttribute("UserName", getUserName());
                row.setAttribute("RegionCode", getSelectedRegions().get(i));

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
}
