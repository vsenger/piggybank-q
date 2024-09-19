package academy.silicon.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.datepicker.DatePicker;
import academy.silicon.model.Entry;
import academy.silicon.rest.EntryResource;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;

@Route("")
public class MainView extends VerticalLayout {

    private final EntryResource entryResource;
    private final Grid<Entry> grid = new Grid<>(Entry.class);
    private final TextField filterAccountId = new TextField("Filter by Account ID");
    private final TextField filterCategory = new TextField("Filter by Category");
    private final DatePicker filterStartDate = new DatePicker("Start Date");
    private final DatePicker filterEndDate = new DatePicker("End Date");
    private final EntryForm form;

    @Inject
    public MainView(EntryResource entryResource) {
        this.entryResource = entryResource;
        this.form = new EntryForm(this);

        addClassName("main-view");
        setSizeFull();

        configureGrid();
        configureForm();

        Tabs tabs = new Tabs(new Tab("CRUD"), new Tab("List/Filter"));
        tabs.addSelectedChangeListener(event -> {
            if (event.getSelectedTab().getLabel().equals("CRUD")) {
                setContent(getCrudLayout());
            } else {
                setContent(getListFilterLayout());
            }
        });

        add(tabs);
        setContent(getCrudLayout());
        updateList();
    }

    private void setContent(Component content) {
        removeAll();
        add(content);
    }

    private Component getCrudLayout() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }

    private Component getListFilterLayout() {
        HorizontalLayout filtering = new HorizontalLayout(filterAccountId, filterCategory, filterStartDate, filterEndDate);
        return new VerticalLayout(filtering, grid);
    }

    private void configureGrid() {
        grid.addClassName("entry-grid");
        grid.setSizeFull();
        grid.setColumns("id", "accountId", "category", "amount", "date", "description");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editEntry(event.getValue()));
    }

    private void configureForm() {
        form.setWidth("25em");
        form.addListener(EntryForm.SaveEvent.class, this::saveEntry);
        form.addListener(EntryForm.DeleteEvent.class, this::deleteEntry);
        form.addListener(EntryForm.CloseEvent.class, e -> closeEditor());
    }

    private void editEntry(Entry entry) {
        if (entry == null) {
            closeEditor();
        } else {
            form.setEntry(entry);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setEntry(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void saveEntry(EntryForm.SaveEvent event) {
        entryResource.createOrUpdateEntry(event.getEntry());
        updateList();
        closeEditor();
    }

    private void deleteEntry(EntryForm.DeleteEvent event) {
        entryResource.deleteEntry(event.getEntry().getId());
        updateList();
        closeEditor();
    }

    private void updateList() {
        grid.setItems(entryResource.getAllEntries());
    }

    private void configureFilter() {
        filterAccountId.setPlaceholder("Type to filter...");
        filterAccountId.setClearButtonVisible(true);
        filterAccountId.setValueChangeMode(ValueChangeMode.LAZY);
        filterAccountId.addValueChangeListener(e -> updateList());

        filterCategory.setPlaceholder("Type to filter...");
        filterCategory.setClearButtonVisible(true);
        filterCategory.setValueChangeMode(ValueChangeMode.LAZY);
        filterCategory.addValueChangeListener(e -> updateList());

        filterStartDate.addValueChangeListener(e -> updateList());
        filterEndDate.addValueChangeListener(e -> updateList());
    }
}
