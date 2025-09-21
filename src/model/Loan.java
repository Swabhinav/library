package model;

import java.time.LocalDate;
import java.util.Objects;

public class Loan {
    private final String copyId;
    private final String isbn;
    private final String patronId;
    private final LocalDate checkoutDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    public Loan(String copyId, String isbn, String patronId, LocalDate checkoutDate, LocalDate dueDate) {
        this.copyId = copyId;
        this.isbn = isbn;
        this.patronId = patronId;
        this.checkoutDate = checkoutDate;
        this.dueDate = dueDate;
    }

    public String getCopyId() { return copyId; }
    public String getIsbn() { return isbn; }
    public String getPatronId() { return patronId; }
    public LocalDate getCheckoutDate() { return checkoutDate; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    @Override
    public String toString() {
        return "Loan{" + "copyId='" + copyId + '\'' + ", isbn='" + isbn + '\'' + ", patronId='" + patronId + '\''
                + ", checkoutDate=" + checkoutDate + ", dueDate=" + dueDate + ", returnDate=" + returnDate + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Loan)) return false;
        Loan loan = (Loan) o;
        return Objects.equals(copyId, loan.copyId) && Objects.equals(patronId, loan.patronId) &&
                Objects.equals(checkoutDate, loan.checkoutDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(copyId, patronId, checkoutDate);
    }
}
