package service;

import model.BookCopy;
import model.Loan;
import model.Patron;
import repository.LibraryInventory;

import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;

/**
 * Lending service: checkout and returns. Records loan history to Patron.
 */
public class LendingService {
    private static final Logger LOGGER = Logger.getLogger(LendingService.class.getName());
    private final LibraryInventory inventory;
    // active loans: copyId -> Loan
    private final Map<String, Loan> activeLoans = new HashMap<>();

    public LendingService(LibraryInventory inventory) {
        this.inventory = inventory;
    }

    /**
     * Attempts to checkout a book copy for a patron. Returns created Loan if successful.
     */
    public Optional<Loan> checkout(String isbn, Patron patron, int loanDays) {
        Objects.requireNonNull(patron);
        Optional<BookCopy> copyOpt = inventory.checkoutAvailableCopy(isbn);
        if (copyOpt.isEmpty()) {
            LOGGER.info("No available copy for ISBN: " + isbn);
            return Optional.empty();
        }
        BookCopy copy = copyOpt.get();
        LocalDate checkoutDate = LocalDate.now();
        Loan loan = new Loan(copy.getCopyId(), isbn, patron.getPatronId(),
                checkoutDate, checkoutDate.plusDays(loanDays));
        activeLoans.put(copy.getCopyId(), loan);
        patron.addLoanRecord(loan);
        LOGGER.info("Checked out copy " + copy.getCopyId() + " to patron " + patron.getPatronId());
        return Optional.of(loan);
    }

    public boolean returnCopy(String copyId) {
        Loan loan = activeLoans.get(copyId);
        if (loan == null) {
            LOGGER.warning("Attempt to return unknown/returned copy: " + copyId);
            return false;
        }
        loan.setReturnDate(LocalDate.now());
        boolean inventoryResult = inventory.returnCopy(copyId);
        if (!inventoryResult) {
            LOGGER.warning("Inventory failed to mark copy returned: " + copyId);
            return false;
        }
        activeLoans.remove(copyId);
        LOGGER.info("Return processed for copy " + copyId);
        return true;
    }

    public Optional<Loan> getActiveLoanByCopy(String copyId) {
        return Optional.ofNullable(activeLoans.get(copyId));
    }

    public List<Loan> listActiveLoansForPatron(String patronId) {
        List<Loan> results = new ArrayList<>();
        for (Loan l : activeLoans.values()) {
            if (l.getPatronId().equals(patronId)) results.add(l);
        }
        return results;
    }
}
