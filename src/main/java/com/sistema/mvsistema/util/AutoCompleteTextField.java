package com.sistema.mvsistema.util;

import atlantafx.base.controls.CustomTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Bounds;
import javafx.scene.control.ListView;
import javafx.stage.Popup;

import java.util.function.Function;

public class AutoCompleteTextField<T> {

    private final CustomTextField textField;
    private final ListView<T> listView;
    private final Popup popup;
    private final FilteredList<T> filteredItems;
    private final ObservableList<T> originalItems;
    private Function<T, String> stringConverter;
    private boolean blockAutoOpen = false;

    public AutoCompleteTextField() {
        this(FXCollections.observableArrayList());
    }

    public AutoCompleteTextField(ObservableList<T> items) {
        this.originalItems = FXCollections.observableArrayList(items);
        this.filteredItems = new FilteredList<>(originalItems);
        this.stringConverter = Object::toString;

        this.textField = new CustomTextField();
        this.listView = new ListView<>(filteredItems);
        this.popup = new Popup();

        initializeComponents();
        setupEventHandlers();
    }

    public AutoCompleteTextField(ObservableList<T> items, Function<T, String> stringConverter) {
        this(items);
        this.stringConverter = stringConverter;
    }

    public void initializeComponents() {
        listView.setPrefWidth(300);
        listView.setPrefHeight(200);

        popup.getContent().add(listView);
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);

        listView.setCellFactory(lv -> new javafx.scene.control.ListCell<T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(stringConverter.apply(item));
                }
            }
        });
    }

    public void setupEventHandlers() {
        // Apenas filtra conforme digita
        textField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (blockAutoOpen) return;
            if (newValue == null) return;

            String filter = newValue.toLowerCase().trim();

            filteredItems.setPredicate(item -> {
                if (filter.isEmpty()) return true;
                if (item == null) return false;
                return stringConverter.apply(item).toLowerCase().contains(filter);
            });

            // Mostra ou esconde o popup baseado no filtro
            if (filter.isEmpty() || filteredItems.isEmpty()) {
                hidePopup();
            } else {
                showPopup();
            }
        });

        // Apenas ENTER para selecionar
        textField.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER:
                    if (popup.isShowing() && !listView.getSelectionModel().isEmpty()) {
                        T selected = listView.getSelectionModel().getSelectedItem();
                        if (selected != null) {
                            textField.setText(stringConverter.apply(selected));
                            hidePopup();
                        }
                    }
                    break;
                case ESCAPE:
                    hidePopup();
                    break;
            }
        });

        // Seleção por clique na lista
        listView.setOnMouseClicked(event -> {
            T selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                textField.setText(stringConverter.apply(selected));
                hidePopup();
            }
        });

        // Foco - mostra todos os itens quando clica
        textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal && textField.getText().isEmpty()) {
                filteredItems.setPredicate(item -> true);
                if (!filteredItems.isEmpty()) {
                    showPopup();
                }
            }
        });
    }

    public void showPopup() {
        if (popup.isShowing()) return;

        Bounds bounds = textField.localToScreen(textField.getBoundsInLocal());
        popup.show(textField, bounds.getMinX(), bounds.getMaxY());
        listView.setPrefWidth(textField.getWidth());
    }

    public void hidePopup() {
        if (popup.isShowing()) {
            popup.hide();
        }
    }

    public void setTextSilently(String text) {
        blockAutoOpen = true;
        textField.setText(text);
        blockAutoOpen = false;
    }

    // Ou sobrecarregue o setSelectedItem existente
    public void setSelectedItemSilently(T item) {
        blockAutoOpen = true;
        setSelectedItem(item);
        blockAutoOpen = false;
    }

    public CustomTextField getTextField() {
        return textField;
    }

    public void setOriginalItems(ObservableList<T> items) {
        originalItems.setAll(items);
    }

    public T getSelectedItem() {
        String currentText = textField.getText();
        if (currentText == null || currentText.trim().isEmpty()) {
            return null;
        }
        return originalItems.stream()
                .filter(item -> stringConverter.apply(item).equalsIgnoreCase(currentText.trim()))
                .findFirst()
                .orElse(null);
    }

    public void setSelectedItem(T item) {
        if (item != null) {
            textField.setText(stringConverter.apply(item));
        } else {
            textField.clear();
        }
    }

    public void setStringConverter(Function<T, String> stringConverter) {
        this.stringConverter = stringConverter;
    }

    public void setPopupWidth(double width) {
        listView.setPrefWidth(width);
    }

    /**
     * Define a altura do popup (ListView)
     */
    public void setPopupHeight(double height) {
        listView.setPrefHeight(height);
    }

    /**
     * Define a largura do TextField
     */
    public void setTextFieldWidth(double width) {
        textField.setPrefWidth(width);
    }

    /**
     * Define todos os tamanhos de uma vez
     */
    public void setSizes(double textFieldWidth, double popupWidth, double popupHeight) {
        setTextFieldWidth(textFieldWidth);
        setPopupWidth(popupWidth);
        setPopupHeight(popupHeight);
    }
}