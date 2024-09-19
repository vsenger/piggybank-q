package academy.silicon.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import academy.silicon.model.Entry;

public class EntryForm extends FormLayout {
    TextField accountId = new TextField("Account ID");
    TextField category = new TextField("Category");
    TextField amount = new TextField("Amount");
    DatePicker date = new DatePicker("Date");
    TextField description = new TextField("Description");

    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

    Binder<Entry> binder = new Binder<>(Entry.class);

    public EntryForm() {
        addClassName("entry-form");
        binder.bindInstanceFields(this);

        add(
            accountId,
            category,
            amount,
            date,
            description,
            createButtonsLayout()
        );
    }

    public void setEntry(Entry entry) {
        binder.setBean(entry);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    // Events
    public static abstract class EntryFormEvent extends ComponentEvent<EntryForm> {
        private Entry entry;

        protected EntryFormEvent(EntryForm source, Entry entry) {
            super(source, false);
            this.entry = entry;
        }

        public Entry getEntry() {
            return entry;
        }
    }

    public static class SaveEvent extends EntryFormEvent {
        SaveEvent(EntryForm source, Entry entry) {
            super(source, entry);
        }
    }

    public static class DeleteEvent extends EntryFormEvent {
        DeleteEvent(EntryForm source, Entry entry) {
            super(source, entry);
        }
    }

    public static class CloseEvent extends EntryFormEvent {
        CloseEvent(EntryForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
