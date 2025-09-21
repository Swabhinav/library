package factory;

import model.Book;
import model.BookCopy;
import model.Patron;

import java.util.UUID;

/**
 * Simple factory to create Book, BookCopy and Patron instances.
 */
public final class EntityFactory {

    private EntityFactory() {}

    public static Book createBook(String isbn, String title, String author, int year) {
        return new Book(isbn, title, author, year);
    }

    public static BookCopy createBookCopy(Book book) {
        return new BookCopy(book);
    }

    public static Patron createPatron(String name, String email) {
        String id = UUID.randomUUID().toString();
        return new Patron(id, name, email);
    }
}
