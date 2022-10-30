package baumann.javaassignment.assignment.controller;

import baumann.javaassignment.assignment.data.Database;
import baumann.javaassignment.assignment.model.Item;
import baumann.javaassignment.assignment.model.Member;
import baumann.javaassignment.assignment.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainViewController implements Initializable, IController {
    private final User loggedInUser;
    private final Database database;
    private ObservableList<Member> members;
    private ObservableList<Item> items;

    private Item selectedItem;
    private Member selectedMember;

    @FXML
    public TextField lendItemId;
    @FXML
    public TextField lendUserId;
    @FXML
    public TextField receiveItemId;
    @FXML
    public TextField itemSearch;
    @FXML
    public TextField userSearch;
    @FXML
    public TextField modifyItemTitle;
    @FXML
    public TextField modifyItemAuthor;
    @FXML
    public TextField modifyMemberFirstName;
    @FXML
    public TextField modifyMemberLastName;

    @FXML
    public DatePicker modifyMemberDateOfBirth;

    @FXML
    public Label welcomeText;
    @FXML
    public Label lendFeedback;
    @FXML
    public Label receiveFeedback;
    @FXML
    public Label labelMemberFeedback;
    @FXML
    public Label labelModifyMember;
    @FXML
    public Label labelModifyItem;
    @FXML
    public Label labelItemFeedback;
    @FXML
    public Label labelModifyItemFeedback;
    @FXML
    public Label labelModifyMemberFeedback;

    @FXML
    public VBox memberPanel;
    @FXML
    public VBox modifyMemberPanel;
    @FXML
    public VBox itemPanel;
    @FXML
    public VBox modifyItemPanel;

    @FXML
    public TableView<Item> itemsTable;
    @FXML
    public TableView<Member> membersTable;


    public MainViewController(User loggedInUser, Database database) {
        this.loggedInUser = loggedInUser;
        this.database = database;
    }

    public void onLendItemClick() {
        int borrowerId;
        int itemId;

        lendFeedback.setTextFill(Color.RED);

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
        lendFeedback.setTextFill(Color.BLACK);
        lendFeedback.setText("%s was lent to %s.".formatted(item.getTitle(), borrower.getFullName()));
        receiveFeedback.setText("");
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
        lendFeedback.setText("");
        receiveItemId.setText("");
        item.setBorrower(null);
        items.set(items.indexOf(item), item);
    }

    public void onAddItemClick() {
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

    public void onAddMember() {
        selectedMember = null;
        labelModifyMember.setText("Add Member");
        labelModifyMemberFeedback.setText("");
        labelMemberFeedback.setText("");
        modifyMemberFirstName.setText("");
        modifyMemberLastName.setText("");
        modifyMemberDateOfBirth.setValue(null);
        toggleModifyMemberPanel(true);
    }

    public void onCancelAddMember() {
        toggleModifyMemberPanel(false);
    }


    private void toggleModifyMemberPanel(boolean show) {
        memberPanel.setVisible(!show);
        modifyMemberPanel.setVisible(show);
    }


    public void onDeleteMembers() {
        ObservableList<Member> selectedMembers = membersTable.getSelectionModel().getSelectedItems();
        if (selectedMembers.isEmpty()) {
            labelMemberFeedback.setText("Please select one or more members to delete.");
            return;
        }
        labelMemberFeedback.setText("");
        members.removeAll(selectedMembers);
        members.forEach(member -> member.setId(members.indexOf(member) + 1));
    }


    private void toggleModifyItemPanel(boolean show) {
        modifyItemPanel.setVisible(show);
        itemPanel.setVisible(!show);
    }


    public void onCancelModifyItem() {
        labelModifyItemFeedback.setText("");
        toggleModifyItemPanel(false);
    }

    public void onSubmitModifyMember() {
        if (modifyMemberFirstName.getText().isEmpty()) {
            labelModifyMemberFeedback.setText("Please enter a first name.");
            return;
        }
        if (modifyMemberLastName.getText().isEmpty()) {
            labelModifyMemberFeedback.setText("Please enter a last name.");
            return;
        }
        if (modifyMemberDateOfBirth.getValue() == null) {
            labelModifyMemberFeedback.setText("Please enter a date of birth.");
            return;
        }
        labelModifyMemberFeedback.setText("");

        if (selectedMember != null) {
            selectedMember.setFirstName(modifyMemberFirstName.getText());
            selectedMember.setLastName(modifyMemberLastName.getText());
            selectedMember.setDateOfBirth(modifyMemberDateOfBirth.getValue());
            members.set(members.indexOf(selectedMember), selectedMember);
        } else {
            Member newMember = new Member(members.size() + 1,
                    modifyMemberFirstName.getText(),
                    modifyMemberLastName.getText(),
                    modifyMemberDateOfBirth.getValue());
            members.add(newMember);
        }
        toggleModifyMemberPanel(false);
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


    public void onEditMember() {
        ObservableList<Member> selectedMembers = membersTable.getSelectionModel().getSelectedItems();
        if (selectedMembers.size() == 1) {
            labelMemberFeedback.setText("");
            selectedMember = selectedMembers.get(0);
            labelModifyMember.setText("Edit Member");
            modifyMemberFirstName.setText(selectedMember.getFirstName());
            modifyMemberLastName.setText(selectedMember.getLastName());
            modifyMemberDateOfBirth.setValue(selectedMember.getDateOfBirth());
            toggleModifyMemberPanel(true);

        } else {
            labelMemberFeedback.setText("Please select one member to edit.");
        }
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

    public void onUserSearchInput() {
        String query = userSearch.getText();
        if (query.isEmpty()) {
            membersTable.setItems(members);
        } else {
            membersTable.setItems(members.stream().filter(u -> u.getFirstName().toLowerCase().contains(query.toLowerCase()) || u.getLastName().toLowerCase().contains(
                    query.toLowerCase())).collect(Collectors.toCollection(FXCollections::observableArrayList)));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        welcomeText.setText("Welcome %s".formatted(loggedInUser.getUserName()));

        members = FXCollections.observableArrayList(database.getMembers());
        items = FXCollections.observableArrayList(database.getItems());
        membersTable.setItems(members);
        membersTable.selectionModelProperty().get().setSelectionMode(SelectionMode.MULTIPLE);
        itemsTable.setItems(items);
        itemsTable.selectionModelProperty().get().setSelectionMode(SelectionMode.MULTIPLE);
        modifyMemberPanel.setVisible(false);
        modifyItemPanel.setVisible(false);

        modifyMemberDateOfBirth.setConverter(new StringConverter<>() {
            static final String PATTERN = "dd-MM-yyyy";
            final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(PATTERN);

            @Override
            public String toString(LocalDate localDate) {
                if (localDate == null) {
                    return "";
                }
                return dateFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String s) {
                if (s == null || s.trim().isEmpty()) {
                    return null;
                }
                return LocalDate.parse(s, dateFormatter);
            }
        });
    }

    @Override
    public void shutdown() {
        database.setMembers(members);
        database.setItems(items);
        database.serialize();
    }
}
