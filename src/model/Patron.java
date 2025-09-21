package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Patron {
    private final String patronId;
    private String name;
    private String email;

    // Borrowing history (immutable view outside)
    private final List<Loan> borrowingHistory = new ArrayList<>();

    public Patron(String patronId, String name, String email) {
        if (patronId == null || patronId.isBlank()) throw new IllegalArgumentException("patronId required");
        this.patronId = patronId;
        this.name = name;
        this.email = email;
    }

    public String getPatronId() { return patronId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }

    public void addLoanRecord(Loan loan) { borrowingHistory.add(loan); }
    public List<Loan> getBorrowingHistory() { return Collections.unmodifiableList(borrowingHistory); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patron patron = (Patron) o;
        return patronId.equals(patron.patronId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patronId);
    }

    @Override
    public String toString() {
        return "Patron{" + "patronId='" + patronId + '\'' + ", name='" + name + '\'' + ", email='" + email + '\'' + '}';
    }
}
