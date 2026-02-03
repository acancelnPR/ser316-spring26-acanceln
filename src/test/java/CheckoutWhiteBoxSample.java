import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Sample White-Box tests for the Checkout system.
 * This class demonstrates how to write white-box tests using:
 * - Control Flow Graph (CFG) analysis
 * - Statement coverage
 * - Branch coverage
 * - Path coverage
 *
 * White-box testing focuses on testing the IMPLEMENTATION by
 * examining the code structure and ensuring all paths are tested.
 */
public class CheckoutWhiteBoxSample {

    private Checkout checkout;

    @BeforeEach
    public void setUp() {
        // Setup: Create book
        Book book1 = null;
        Book book2 = new Book("978-0-443456-78-9", "Test Book2",
                "Test Author", Book.BookType.NONFICTION, 1);
        Book book3 = new Book("978-0-123444-78-9", "Test Book3",
                "Test Author", Book.BookType.FICTION, 1);
        Book book4 = new Book("978-0-128556-78-9", "Test Book4",
                "Test Author", Book.BookType.FICTION, 0);
        Book book5 = new Book("978-0-128556-98-9", "Test Book5",
                "Test Author", Book.BookType.FICTION, 0);

        checkout = new Checkout();

        checkout.getInventory().put("978-0-413456-78-9", book1);

        checkout.addBook(book2);
        checkout.addBook(book3);
        checkout.addBook(book4);
        checkout.addBook(book5);


    }

    @Test
    @DisplayName("WB Test: countBooksByType - null type branch")
    public void testCountBooksByType_NullType() {
        // Branch: type == null → TRUE
        int result = checkout.countBooksByType(null, false);
        assertEquals(0, result, "Should return 0 for null type");
    }

    @Test
    @DisplayName("WB Test: countBooksByType - Only count books that are available and of correct type")
    public void testCountBooksByType_Available() {

    //  Counts all books of correct type when available.
        int result = checkout.countBooksByType(Book.BookType.FICTION, true);
        assertEquals(1, result, "Should return 1");
    }

    @Test
    @DisplayName("WB Test: countBooksByType - Count books regardless of availability and of correct type")
    public void testCountBooksByType_AvailableOrNot() {


        //Counts all books of correct type regardless of availability.
        int result = checkout.countBooksByType(Book.BookType.FICTION, false);
        assertEquals(3, result, "Should return 3");
    }

    @Test
    @DisplayName("WB Test: testCalculateFine - calculates fine for overdue book over 0 days")
    public void testCalculateFine_numDaysZero() {


        //Counts all books of correct type regardless of availability.
        double result = checkout.calculateFine(0, Book.BookType.FICTION);
        assertEquals(0.0, result, 0.1, "Should return 3");
    }

    @Test
    @DisplayName("WB Test: testCalculateFine - calculates fine for overdue book over 7 days")
    public void testCalculateFine_numDaysSeven() {


        //Counts all books of correct type regardless of availability.
        double result = checkout.calculateFine(7, Book.BookType.FICTION);

        double expectedFine = 7 * 0.25;
        assertEquals(expectedFine, result, 0.1, "Should return 3");
    }

    @Test
    @DisplayName("WB Test: testCalculateFine - calculates fine for overdue book over 15 days")
    public void testCalculateFine_numDays15AndReferenceOrTextbook() {


        //Counts all books of correct type regardless of availability.
        int numdays = 15;

        double result = checkout.calculateFine(numdays, Book.BookType.REFERENCE);

        double expectedFine = 7 * 0.25;//first 7 days
        expectedFine += 7 * 0.50;//8-14 days
        expectedFine += 1.0; //15th day
        expectedFine *= 2; //double fine for reference book

        assertEquals(expectedFine, result, 0.1, "Should return 12.5");

        result = checkout.calculateFine(numdays, Book.BookType.TEXTBOOK);
        assertEquals(expectedFine, result, 0.1, "Should return 12.5");
    }

    @Test
    @DisplayName("WB Test: isValidISBN - null type isbn and empty isbn")
    public void testIsValidISBN_NullandIsEmpty() {
        String isbn = null;

        boolean result = checkout.isValidISBN(isbn);
        assertFalse(result, "Should return false for null type");

        isbn = "";
        result = checkout.isValidISBN(isbn);
        assertFalse(result, "Should return false for empty type");
    }

    @Test
    @DisplayName("WB Test: isValidISBN - isbn with 10 or 13 letters with hyphens")
    public void testIsValidISBN_CorrectType() {
        String isbn = "978-0-123444-78-9";

        boolean result = checkout.isValidISBN(isbn);
        assertTrue(result, "Should return true for correct format w/ 13 numbers");

        isbn = "978-0-123444";

        result = checkout.isValidISBN(isbn);
        assertTrue(result, "Should return true for correct format w/ 10 numbers");

    }

    @Test
    @DisplayName("WB Test: isValidISBN - isbn with incorrect format: Not 10or13 numbers; contain letters")
    public void testIsValidISBN_IncorrectFormat() {
        String isbn = "978-0-12344";

        boolean result = checkout.isValidISBN(isbn);
        assertFalse(result, "Should return false for incorrect format w/ 9 numbers");

        isbn = "978-0a123444";

        result = checkout.isValidISBN(isbn);
        assertFalse(result, "Should return false for incorrect format w/ letter");

    }

}
