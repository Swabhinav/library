package model;

import java.util.UUID;

public class BookCopy {
    private final String copyId;
    private final Book book;
    private boolean checkedOut;

    public BookCopy(Book book) {
        this.copyId = UUID.randomUUID().toString();
        this.book = book;
        this.checkedOut = false;
    }

    public String getCopyId() { return copyId; }
    public Book getBook() { return book; }
    public boolean isCheckedOut() { return checkedOut; }
    public void setCheckedOut(boolean checkedOut) { this.checkedOut = checkedOut; }

    @Override
    public String toString() {
        return "BookCopy{" + "copyId='" + copyId + '\'' + ", book=" + book + ", checkedOut=" + checkedOut + '}';
    }
}

