package app.fpp.bean.promoproposal;

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

import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

import oracle.jbo.Row;
import oracle.jbo.domain.DBSequence;

public class PpProdAddVariantBackingBean implements Serializable {

    // Shuttle operations
    @SuppressWarnings("compatibility:4429120787595570625")
    private static final long serialVersionUID = 1L;
    List selectedVariants;
    List allVariants;
    Boolean refreshSelectedList = false;
    private final String SELECTED_VARIANT_ITERATOR =
        "ProdukVariantView1Iterator";
    private final String ALL_VARIANT_ITERATOR =
        "AllProdukVariantShuttleView1Iterator";

    public PpProdAddVariantBackingBean() {
        super();
    }

    public BindingContainer getBindings() {
        return BindingContext.getCurrent().getCurrentBindingsEntry();
    }

    public void setSelectedVariants(List selectedVariants) {
        this.selectedVariants = selectedVariants;
    }

    public List getSelectedVariants() {

        if (selectedVariants == null || refreshSelectedList) {
            selectedVariants =
                    attributeListForIterator(SELECTED_VARIANT_ITERATOR, "ProdVariant");
        }
        return selectedVariants;
    }

    public void setAllVariants(List allVariants) {
        this.allVariants = allVariants;
    }

    public List getAllVariants() {
        if (allVariants == null) {
            allVariants =
                    selectItemsForIterator(ALL_VARIANT_ITERATOR, "SetVariant", "SetVariantDesc");
        }
        return allVariants;
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
            (DCIteratorBinding)binding.get(SELECTED_VARIANT_ITERATOR);

        //Removing all rows
        for (Row r : iter.getAllRowsInRange()) {
            r.remove();
        }

        if (this.getSelectedVariants().size() > 0) {
            for (int i = 0; i < getSelectedVariants().size(); i++) {
                Row row = iter.getRowSetIterator().createRow();
                row.setNewRowState(Row.STATUS_INITIALIZED);
                DCIteratorBinding iterAllVariant =
                    (DCIteratorBinding)binding.get(ALL_VARIANT_ITERATOR);
                for (Row r : iterAllVariant.getAllRowsInRange()) {
                    if (getSelectedVariants().get(i).equals(r.getAttribute("SetVariant"))) {
                        row.setAttribute("VariantDesc",
                                         r.getAttribute("SetVariantDesc"));
                    }
                }
                row.setAttribute("PromoProdukId", getCurrentPromoProdukId().getValue());
                row.setAttribute("ProdVariant", getSelectedVariants().get(i));
                if (getSelectedVariants().get(i).equals("ALL")) {
                    row.setAttribute("VariantDesc", "ALL");
                }
                iter.getRowSetIterator().insertRow(row);
                iter.setCurrentRowWithKey(row.getKey().toStringFormat(true));
            }
        }

        String ok = doCommit();
        return closeAfter;
    }

    public String processCancel() {
        FacesContext fctx = FacesContext.getCurrentInstance();
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

    public DBSequence getCurrentPromoProdukId() {
        ADFContext adfCtx = ADFContext.getCurrent();
        Map pageFlowScope = adfCtx.getPageFlowScope();
        DBSequence promoProdukId =
            (DBSequence)pageFlowScope.get("promoProdukId");
        return promoProdukId;
    }
}