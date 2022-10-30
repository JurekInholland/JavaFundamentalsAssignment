package baumann.javaassignment.assignment.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Item implements Serializable {
    private int id;
    private String title;
    private String author;

    private Member owner;
    private Member borrower;
    private LocalDate lendDate;

    public Item(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAvailable() {
        return borrower == null;
    }

    public String getAvailable() {
        return borrower == null ? "Yes" : "No";
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getLendDate() {
        return lendDate;
    }

    public void setLendDate(LocalDate lendDate) {
        this.lendDate = lendDate;
    }


    public Member getOwner() {
        return owner;
    }

    public void setOwner(Member owner) {
        this.owner = owner;
    }

    public Member getBorrower() {
        return borrower;
    }

    public void setBorrower(Member borrower) {
        this.borrower = borrower;
        if (borrower != null) {
            this.lendDate = LocalDate.now();
        } else {
            this.lendDate = null;
        }
    }


    public String getBorrowerName() {
        if (borrower == null) {
            return "No borrower";
        }
        return borrower.getFirstName() + " " + borrower.getLastName();
    }


    public int getDaysOverdue() {
        if (lendDate == null) {
            return 0;
        }
        return (int) (LocalDate.now().toEpochDay() - lendDate.plusDays(21).toEpochDay());
    }

}
