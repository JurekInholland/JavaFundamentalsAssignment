public class MainViewController implements Initializable, IController {
    private final User loggedInUser;
    private final Database database;
    private final Logger log = Logger.getLogger(this.getClass().getName());
    @FXML
    public Label welcomeText;
    public void onLendItem() {
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

    public void onReceiveItem() {
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

    }


    @Override
    public void shutdown() {
        database.setMembers(members);
        database.setItems(items);
        database.serialize();
    }
}
