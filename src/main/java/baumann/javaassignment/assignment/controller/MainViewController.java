package baumann.javaassignment.assignment.controller;

import baumann.javaassignment.assignment.data.Database;
import baumann.javaassignment.assignment.model.Item;
import baumann.javaassignment.assignment.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
public class MainViewController implements Initializable, IController {
    private final User loggedInUser;
    private final Database database;
    private final Logger log = Logger.getLogger(this.getClass().getName());
    @FXML
    public Label welcomeText;
    @FXML
    public Label lendFeedback;
    @FXML
    public Label receiveFeedback;
    @FXML
    public Label labelModifyItem;
    @FXML
    public Label labelItemFeedback;
    @FXML
    public Label labelModifyItemFeedback;
    @FXML
    @FXML
    public VBox itemPanel;
    @FXML
    public VBox modifyItemPanel;

    @FXML
    public TableView<Item> itemsTable;
    public MainViewController(User loggedInUser, Database database) {
        this.loggedInUser = loggedInUser;
        this.database = database;
    }

    public void onLendItemClick() {
        int borrowerId;
        int itemId;

        try {
            itemId = Integer.parseInt(lendItemId.getText());
        } catch (NumberFormatException e) {
            lendFeedback.setText("Invalid Item ID");
            return;
        }
        try {
            borrowerId = Integer.parseInt(lendUserId.getText());
        } catch (NumberFormatException e) {
            lendFeedback.setText("Invalid Member code.");
            return;
        }
        Item item = items.stream().filter(i -> i.getId() == itemId).findFirst().orElse(null);
        if (item == null) {
            lendFeedback.setText("Item not found.");
            return;
        }
        Member borrower = members.stream().filter(u -> u.getId() == borrowerId).findFirst().orElse(null);
        if (borrower == null) {
            lendFeedback.setText("Member not found.");
            return;
        }
        if (!item.isAvailable()) {
            lendFeedback.setText("Item is already lent out.");
            return;
        }
        lendFeedback.setText("%s lent to %s.".formatted(item.getTitle(), borrower.getFullName()));
        lendUserId.setText("");
        lendItemId.setText("");

        item.setBorrower(borrower);
        items.set(items.indexOf(item), item);
    }

    public void onReceiveItemClick() {
        receiveFeedback.setTextFill(Color.RED);

        int itemId;
        try {
            itemId = Integer.parseInt(receiveItemId.getText());
        } catch (NumberFormatException e) {
            receiveFeedback.setText("Invalid Item code.");
            return;
        }
        Item item = items.stream().filter(i -> i.getId() == itemId).findFirst().orElse(null);
        if (item == null) {
            receiveFeedback.setText("Item not found.");
            return;
        }
        if (item.isAvailable()) {
            receiveFeedback.setText("Item is currently not lent out.");
            return;
        }

        int daysOverdue = item.getDaysOverdue();
        if (daysOverdue > 0) {
            receiveFeedback.setText("%s was returned. The item was overdue by %d days!".formatted(item.getTitle(),
                    daysOverdue));
        } else {
            receiveFeedback.setTextFill(Color.BLACK);
            receiveFeedback.setText("%s was returned on time.".formatted(item.getTitle()));

        }
        receiveItemId.setText("");
        item.setBorrower(null);
        items.set(items.indexOf(item), item);

    public void onAddItemClick() {
        log.info("Add Item");
        labelModifyItem.setText("Add Item");
        selectedItem = null;
        labelItemFeedback.setText("");
        modifyItemTitle.setText("");
        modifyItemAuthor.setText("");
        toggleModifyItemPanel(true);
    }

    public void onEditItemClick() {
        ObservableList<Item> selectedItems = itemsTable.getSelectionModel().getSelectedItems();
        if (selectedItems.size() != 1) {
            labelItemFeedback.setText("Please select exactly one item to edit.");
            return;
        }
        labelItemFeedback.setText("");
        selectedItem = selectedItems.get(0);
        labelModifyItem.setText("Edit Item");
        modifyItemTitle.setText(selectedItem.getTitle());
        modifyItemAuthor.setText(selectedItem.getAuthor());
        toggleModifyItemPanel(true);
    }

    public void onDeleteItemClick() {
        ObservableList<Item> selectedItems = itemsTable.getSelectionModel().getSelectedItems();
        if (selectedItems.isEmpty()) {
            labelItemFeedback.setText("Please select at least one item.");
            return;
        }
        labelItemFeedback.setText("");
        items.removeAll(selectedItems);
        items.forEach(item -> item.setId(items.indexOf(item) + 1));
    }

    private void toggleModifyItemPanel(boolean show) {
        modifyItemPanel.setVisible(show);
        itemPanel.setVisible(!show);
    }


    public void onCancelModifyItem() {
        log.info("cancel Item");
        labelModifyItemFeedback.setText("");
        toggleModifyItemPanel(false);
    }

    public void onSubmitModifyItem() {
        if (modifyItemTitle.getText().isEmpty()) {
            labelModifyItemFeedback.setText("Item title cannot be empty.");
            return;
        }
        if (modifyItemAuthor.getText().isEmpty()) {
            labelModifyItemFeedback.setText("Item author cannot be empty.");
            return;
        }
        labelModifyItemFeedback.setText("");

        if (selectedItem != null) {
            selectedItem.setTitle(modifyItemTitle.getText());
            selectedItem.setAuthor(modifyItemAuthor.getText());
            items.set(items.indexOf(selectedItem), selectedItem);
        } else {
            Item newItem = new Item(items.size() + 1, modifyItemTitle.getText(), modifyItemAuthor.getText());
            items.add(newItem);
        }
        toggleModifyItemPanel(false);
    }


    public void onItemSearchInput() {
        String query = itemSearch.getText();
        if (query.isEmpty()) {
            itemsTable.setItems(items);
        } else {
            itemsTable.setItems(items.stream().filter(i -> i.getTitle().toLowerCase().contains(query.toLowerCase()) || i.getAuthor().toLowerCase().contains(
                    query.toLowerCase())).collect(Collectors.toCollection(FXCollections::observableArrayList)));
        }
    }
    @Override
    public void shutdown() {
        database.setMembers(members);
        database.setItems(items);
        database.serialize();
    }
}
