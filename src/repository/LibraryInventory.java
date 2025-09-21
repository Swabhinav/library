package repository;

import model.Book;
import model.BookCopy;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Manages books and their copies.
 * Provides search, add, remove, and copy-level availability.
 */
public class LibraryInventory {
    private static final Logger LOGGER = Logger.getLogger(LibraryInventory.class.getName());

    // isbn -> Book (single canonical Book per ISBN)
    private final Map<String, Book> booksByIsbn = new HashMap<>();

    // isbn -> list of copies
    private final Map<String, List<BookCopy>> copiesByIsbn = new HashMap<>();

    public void addBook(Book book, int numCopies) {
        booksByIsbn.put(book.getIsbn(), book);
        copiesByIsbn.computeIfAbsent(book.getIsbn(), k -> new ArrayList<>());
        for (int i = 0; i < numCopies; i++) {
            BookCopy copy = new BookCopy(book);
            copiesByIsbn.get(book.getIsbn()).add(copy);
        }
        LOGGER.info("Added book: " + book + " copies: " + numCopies);
    }

    public boolean removeBook(String isbn) {
        if (!booksByIsbn.containsKey(isbn)) return false;
        // Only allow delete if no copies exist or none checked out
        List<BookCopy> copies = copiesByIsbn.getOrDefault(isbn, Collections.emptyList());
        boolean anyCheckedOut = copies.stream().anyMatch(BookCopy::isCheckedOut);
        if (anyCheckedOut) {
            LOGGER.warning("Attempted to remove book while copies are checked out: " + isbn);
            return false;
        }
        copiesByIsbn.remove(isbn);
        booksByIsbn.remove(isbn);
        LOGGER.info("Removed book with ISBN: " + isbn);
        return true;
    }

    public Optional<Book> findBookByIsbn(String isbn) {
        return Optional.ofNullable(booksByIsbn.get(isbn));
    }

    public List<Book> findByTitle(String title) {
        String t = title.toLowerCase();
        return booksByIsbn.values().stream()
                .filter(b -> b.getTitle().toLowerCase().contains(t))
                .collect(Collectors.toList());
    }

    public List<Book> findByAuthor(String author) {
        String a = author.toLowerCase();
        return booksByIsbn.values().stream()
                .filter(b -> b.getAuthor().toLowerCase().contains(a))
                .collect(Collectors.toList());
    }

    public Optional<BookCopy> checkoutAvailableCopy(String isbn) {
        List<BookCopy> copies = copiesByIsbn.get(isbn);
        if (copies == null) return Optional.empty();
        for (BookCopy copy : copies) {
            if (!copy.isCheckedOut()) {
                copy.setCheckedOut(true);
                LOGGER.info("Checked out copy: " + copy.getCopyId() + " of ISBN: " + isbn);
                return Optional.of(copy);
            }
        }
        return Optional.empty();
    }

    public boolean returnCopy(String copyId) {
        for (List<BookCopy> list : copiesByIsbn.values()) {
            for (BookCopy copy : list) {
                if (copy.getCopyId().equals(copyId)) {
                    if (!copy.isCheckedOut()) {
                        LOGGER.warning("Return attempted on a copy that was not checked out: " + copyId);
                        return false;
                    }
                    copy.setCheckedOut(false);
                    LOGGER.info("Returned copy: " + copyId);
                    return true;
                }
            }
        }
        LOGGER.warning("No copy found with ID: " + copyId);
        return false;
    }

    public List<BookCopy> getCopies(String isbn) {
        return Collections.unmodifiableList(copiesByIsbn.getOrDefault(isbn, Collections.emptyList()));
    }

    public Map<String, Integer> getInventorySummary() {
        Map<String, Integer> summary = new HashMap<>();
        for (String isbn : booksByIsbn.keySet()) {
            summary.put(isbn, copiesByIsbn.getOrDefault(isbn, Collections.emptyList()).size());
        }
        return summary;
    }
}
