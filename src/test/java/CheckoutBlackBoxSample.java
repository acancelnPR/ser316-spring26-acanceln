import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Constructor;
import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Sample Black-Box tests for the Checkout system.
 * This class demonstrates how to write black-box tests using:
 * - Equivalence Partitioning (EP)
 * - Boundary Value Analysis (BVA)
 * - Parametrized tests across multiple implementations
 *
 * Black-box testing focuses on testing the SPECIFICATION WITHOUT
 * looking at the implementation.
 *
 * The parameterized structure allows testing all Checkout implementations
 * with the same tests to identify which implementations have bugs.
 */
public class CheckoutBlackBoxSample {

    private Checkout checkout;

    /**
     * Provides the list of Checkout classes to test.
     * Each test will run against ALL implementations.
     */
    @SuppressWarnings("unchecked")
    static Stream<Class<? extends Checkout>> checkoutClassProvider() {
        return (Stream<Class<? extends Checkout>>) Stream.of(
                Checkout0.class,
                Checkout1.class,
                Checkout2.class,
                Checkout3.class
        );
    }

    // Uncomment when you implement the method in assign 3 and comment the above
//    static Stream<Class<? extends Checkout>> checkoutClassProvider() {
//        return Stream.of(Checkout.class);
//    }


    /**
     * Helper method to create Checkout instance from class using reflection.
     */
    private Checkout createCheckout(Class<? extends Checkout> clazz) throws Exception {
        Constructor<? extends Checkout> constructor = clazz.getConstructor();
        return constructor.newInstance();
    }

    /**
     * T2: Tests successful checkout of an available book
     * This tests the valid equivalence partition - all conditions met.
     */
    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T2: Successful checkout - available book, eligible patron")
    public void testBookAvailable(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        // Setup: Create available book and eligible patron
        Book book = new Book("978-0-123456-78-9", "Test Book",
                "Test Author", Book.BookType.FICTION, 1);

        Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                Patron.PatronType.STUDENT);

        checkout.addBook(book); // adding the book to the library
        checkout.registerPatron(patron); // adding a patrol to the system

        // Execute checkout
        double result = checkout.checkoutBook(book, patron);

        // Verify: Should return 0.0 for success
        assertEquals(0.0, result, 0.01,
                "Expected successful checkout (0.0) for " + checkoutClass.getSimpleName());

        // Verify: Book should now be unavailable
        assertFalse(book.isAvailable(),
                "Book should be unavailable after checkout for " + checkoutClass.getSimpleName());

        // Verify: Patron should have the book in their checked-out list
        assertTrue(patron.hasBookCheckedOut(book.getIsbn()),
                "Patron should have book in checked-out list for " + checkoutClass.getSimpleName());

        // Verify: Checkout count increased
        assertEquals(1, patron.getCheckoutCount(),
                "Patron checkout count should be 1 for " + checkoutClass.getSimpleName());


    }

    /**
     * T1: Tests checkout with unavailable book
     * This tests an invalid equivalence partition.
     */
    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T1: Unavailable book returns error code 2.0")
    public void testUnavailableBook(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        // Setup: Create unavailable book
        Book book = new Book("978-0-123456-78-9", "Test Book",
                "Test Author", Book.BookType.FICTION, 5);
        book.setAvailableCopies(-1);  // We are pretending it has been checked out by others and is not available anymore

        Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                Patron.PatronType.STUDENT);

        checkout.addBook(book);
        checkout.registerPatron(patron);

        // Execute checkout
        double result = checkout.checkoutBook(book, patron);

        // Verify: Should return 2.0 for unavailable book
        assertEquals(2.0, result, 0.01,
                "Expected error code 2.0 for unavailable book for " + checkoutClass.getSimpleName());


        // Verify: Patron should NOT have the book
        assertFalse(patron.hasBookCheckedOut(book.getIsbn()),
                "Patron should NOT have book in list for " + checkoutClass.getSimpleName());
    }

    /**
     * T3: tests checkout with patron overdue above
     * This tests an invalid equivalence partition.
     */
    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T3: Overdue Above (4+) returns 4.0; no state change")
    public void testOverdueAbove(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        // Setup: Create unavailable book
        Book book = new Book("978-0-123456-78-9", "Test Book",
                "Test Author", Book.BookType.FICTION, 5);
        book.setAvailableCopies(0);  // We are pretending it has been checked out by others and is not available anymore

        Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                Patron.PatronType.STUDENT);

        checkout.addBook(book);
        patron.setOverdueCount(4);
        checkout.registerPatron(patron);

        // Execute checkout
        double result = checkout.checkoutBook(book, patron);

        // Verify: Should return 2.0 for unavailable book
        assertEquals(4.0, result, 0.01,
                "Expected error code 4.0 for patron Overdue for " + checkoutClass.getSimpleName());

        // Verify: Patron should NOT have the book
        assertFalse(patron.hasBookCheckedOut(book.getIsbn()),
                "Patron should NOT have book in list for " + checkoutClass.getSimpleName());
    }

    /**
     * T4: tests checkout with patron overdue At boundry
     * This tests an invalid equivalence partition.
     */
    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T4: Overdue At (3) returns 4.0; no state change")
    public void testOverdueAt(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        // Setup: Create unavailable book
        Book book = new Book("978-0-123456-78-9", "Test Book",
                "Test Author", Book.BookType.FICTION, 5);
        book.setAvailableCopies(1);

        Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                Patron.PatronType.STUDENT);

        checkout.addBook(book);
        patron.setOverdueCount(3);
        checkout.registerPatron(patron);

        // Execute checkout
        double result = checkout.checkoutBook(book, patron);


        assertEquals(4.0, result, 0.01,
                "Expected error code 4.0 for patron Overdue for " + checkoutClass.getSimpleName());


        assertFalse(patron.hasBookCheckedOut(book.getIsbn()),
                "Patron should NOT have book in list for " + checkoutClass.getSimpleName());

        assertEquals(1, book.getAvailableCopies(),"Book availability should be 1 for " + checkoutClass.getSimpleName() );
    }

    /**
     * T5: tests checkout with patron overdue below boundry
     * This tests a valid equivalence partition.
     */
    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T5: Checkout occurs: availableCopies decrease by 1; Overdue warning displayed ")
    public void testOverdueWarningHigh(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        // Setup: Create book
        Book book = new Book("978-0-123456-78-9", "Test Book",
                "Test Author", Book.BookType.FICTION, 5);
        book.setAvailableCopies(1);  // We are pretending it has been checked out by others and is not available anymore

        Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                Patron.PatronType.STUDENT);

        checkout.addBook(book);
        patron.setOverdueCount(2);
        checkout.registerPatron(patron);

        // Execute checkout
        double result = checkout.checkoutBook(book, patron);

        // Verify: Should return 2.0 for unavailable book
        assertEquals(1.0, result, 0.01,
                "Expected error code 1.0 for patron near Overdue limit for " + checkoutClass.getSimpleName());

        // Verify: Patron should HAVE the book
        assertTrue(patron.hasBookCheckedOut(book.getIsbn()),
                "Patron should have book in list for " + checkoutClass.getSimpleName());

        assertEquals(0, book.getAvailableCopies(),"Book availability should be 0 for " + checkoutClass.getSimpleName() );
    }

    /**
     * T6: tests checkout with patron overdue start of boundry warning
     * This tests an valid equivalence partition.
     */
    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T6: Overdue Warning Low (1) returns 1.0; checkout occurs")
    public void testOverdueWarningLow(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        // Setup: Create book
        Book book = new Book("978-0-123456-78-9", "Test Book",
                "Test Author", Book.BookType.FICTION, 5);
        book.setAvailableCopies(4);  // We are pretending it has been checked out by others and is not available anymore

        Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                Patron.PatronType.STUDENT);

        int beforeCheckCount = patron.getCheckoutCount();

        checkout.addBook(book);
        patron.setOverdueCount(1);
        checkout.registerPatron(patron);

        double result = checkout.checkoutBook(book, patron);

        assertEquals(1.0, result, 0.01,
                "Expected error code 1.0 for patron at Overdue warning for " + checkoutClass.getSimpleName());

        assertTrue(book.isAvailable(),
                "Book should be available after checkout for " + checkoutClass.getSimpleName());

        assertTrue(patron.hasBookCheckedOut(book.getIsbn()),
                "Patron should have book in list for " + checkoutClass.getSimpleName());

        assertEquals(book.getTotalCopies() - 1, book.getAvailableCopies(),
                "Book availability should be -1 for " + checkoutClass.getSimpleName() );

        assertEquals(beforeCheckCount +1, patron.getCheckoutCount(),
                "Patron checkout count should be +1 for " + checkoutClass.getSimpleName());
    }

    /**
     * T7: tests checkout with patron overdue start of boundry warning
     * This tests an valid equivalence partition.
     */
    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T7: overdueCount = 0; normal book, eligible patron otherwise     ")
    public void testOverdueBelow(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        // Setup: Create book
        Book book = new Book("978-0-123456-78-9", "Test Book",
                "Test Author", Book.BookType.FICTION, 5);
        book.setAvailableCopies(5);

        Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                Patron.PatronType.STUDENT);

        int beforeCheckCount = patron.getCheckoutCount();

        checkout.addBook(book);
        patron.setOverdueCount(-1);
        checkout.registerPatron(patron);

        double result = checkout.checkoutBook(book, patron);

        assertEquals(0.0, result, 0.01,
                "Expected error code 0.0 for patron below overdue warning for " + checkoutClass.getSimpleName());

        assertTrue(book.isAvailable(),
                "Book should be available after checkout for " + checkoutClass.getSimpleName());

        assertTrue(patron.hasBookCheckedOut(book.getIsbn()),
                "Patron should have book in list for " + checkoutClass.getSimpleName());

        assertEquals(book.getTotalCopies() - 1, book.getAvailableCopies(),
                "Book availability should be -1 for " + checkoutClass.getSimpleName() );

        assertEquals(beforeCheckCount +1, patron.getCheckoutCount(),
                "Patron checkout count should be +1 for " + checkoutClass.getSimpleName());
    }

    /**
     * T8: tests checkout with patron over checkout limit. limit is 3 for CHILD
     * This tests an invalid equivalence partition.
     */
    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T8: getCheckoutCount() = MAX + 1")
    public void testCheckoutLimitAbove(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        // Setup: Create book
        Book book = new Book("978-0-123456-78-9", "Test Book",
                "Test Author", Book.BookType.FICTION, 5);
        Book book2 = new Book("978-0-443456-78-9", "Test Book2",
                "Test Author", Book.BookType.FICTION, 5);
        Book book3 = new Book("978-0-123444-78-9", "Test Book3",
                "Test Author", Book.BookType.FICTION, 5);
        Book book4 = new Book("978-0-128556-78-9", "Test Book4",
                "Test Author", Book.BookType.FICTION, 5);
        Book checkBook = new Book("978-0-128556-98-9", "Test Book4",
                "Test Author", Book.BookType.FICTION, 5);

        Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                Patron.PatronType.CHILD);

        patron.addCheckedOutBook(book.getIsbn(), LocalDate.of(2026, 5, 25) );
        patron.addCheckedOutBook(book2.getIsbn(), LocalDate.of(2026, 5, 25));
        patron.addCheckedOutBook(book3.getIsbn(), LocalDate.of(2026, 5, 25));
        patron.addCheckedOutBook(book4.getIsbn(), LocalDate.of(2026, 5, 25));

//        System.out.println("HERERERERERER" + patron.getCheckoutCount());
        checkout.addBook(book);
        checkout.addBook(book2);
        checkout.addBook(book3);
        checkout.addBook(book4);
        checkout.addBook(checkBook);
        checkout.registerPatron(patron);

        double result = checkout.checkoutBook(checkBook, patron);

        assertEquals(3.2, result, 0.01,
                "Expected error code 3.2 for patron over checkout limit for " + checkoutClass.getSimpleName());

        assertEquals(3, patron.getMaxCheckoutLimit(), "Patron check count should Checkout limit should be 3 for CHILD " + checkoutClass.getSimpleName());
        assertFalse(patron.hasBookCheckedOut(checkBook.getIsbn()),
                "Patron should NOT have book in list for " + checkoutClass.getSimpleName());
        assertTrue(patron.hasBookCheckedOut(book2.getIsbn()),
                "Patron should still have other previous books in list for " + checkoutClass.getSimpleName());

        assertEquals(4, patron.getCheckoutCount(), "Patron check count should not change " + checkoutClass.getSimpleName());

    }

    /**
     * T9: tests checkout with patron within checkout limit
     * expected state: Checkout occurs: availableCopies decrease by 1; Checkout warning displayed;
     * This tests an valid equivalence partition.
     */
    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T9: tests checkout with patron within checkout limit")
    public void testCheckoutLimitWarningHigh(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        // Setup: Create book
        Book book = new Book("978-0-123456-78-9", "Test Book",
                "Test Author", Book.BookType.FICTION, 5);
        Book book2 = new Book("978-0-443456-78-9", "Test Book2",
                "Test Author", Book.BookType.FICTION, 5);
        Book book3 = new Book("978-0-123444-78-9", "Test Book3",
                "Test Author", Book.BookType.FICTION, 5);
        Book book4 = new Book("978-0-128556-78-9", "Test Book4",
                "Test Author", Book.BookType.FICTION, 5);
        Book checkBook = new Book("978-0-128556-98-9", "Test Book4",
                "Test Author", Book.BookType.FICTION, 5);

        Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                Patron.PatronType.PUBLIC);

        patron.addCheckedOutBook(book.getIsbn(), LocalDate.of(2026, 5, 25) );
        patron.addCheckedOutBook(book2.getIsbn(), LocalDate.of(2026, 5, 25));
        patron.addCheckedOutBook(book3.getIsbn(), LocalDate.of(2026, 5, 25));
        patron.addCheckedOutBook(book4.getIsbn(), LocalDate.of(2026, 5, 25));

        checkout.addBook(book);
        checkout.addBook(book2);
        checkout.addBook(book3);
        checkout.addBook(book4);
        checkout.addBook(checkBook);
        checkout.registerPatron(patron);

        int beforeCheckCount = patron.getCheckoutCount();
        double result = checkout.checkoutBook(checkBook, patron);



        assertEquals(1.1, result, 0.01,
                "Expected code 1.1 for patron over checkout limit for " + checkoutClass.getSimpleName());

        assertTrue(patron.hasBookCheckedOut(checkBook.getIsbn()),
                "Patron should have book in list for " + checkoutClass.getSimpleName());
        assertTrue(patron.hasBookCheckedOut(book2.getIsbn()),
                "Patron should still have other previous books in list for " + checkoutClass.getSimpleName());

        assertTrue(checkBook.isAvailable(), "Book still has copies available " + checkoutClass.getSimpleName());

        assertEquals(beforeCheckCount +1, patron.getCheckoutCount(),
                "Patron checkout count should be +1 for " + checkoutClass.getSimpleName());

        assertEquals(5, patron.getMaxCheckoutLimit(), "Patron check count should Checkout limit should be 5 for PUBLIC " + checkoutClass.getSimpleName());
    }

    /**
     * T10: tests checkout with patron at checkout limit
     * expected state: no state change
     * This tests an valid equivalence partition.
     */
    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T10: tests checkout with patron at checkout limit")
    public void testCheckoutLimitAt(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        // Setup: Create book
        Book book = new Book("978-0-123456-78-9", "Test Book",
                "Test Author", Book.BookType.FICTION, 5);
        Book book2 = new Book("978-0-443456-78-9", "Test Book2",
                "Test Author", Book.BookType.FICTION, 5);
        Book book3 = new Book("978-0-123444-78-9", "Test Book3",
                "Test Author", Book.BookType.FICTION, 5);
        Book book4 = new Book("978-0-128556-78-9", "Test Book4",
                "Test Author", Book.BookType.FICTION, 5);
        Book checkBook = new Book("978-0-128556-98-9", "Test Book4",
                "Test Author", Book.BookType.FICTION, 1);

        Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                Patron.PatronType.CHILD);

        patron.addCheckedOutBook(book.getIsbn(), LocalDate.of(2026, 5, 25) );
        patron.addCheckedOutBook(book2.getIsbn(), LocalDate.of(2026, 5, 25));
        patron.addCheckedOutBook(book3.getIsbn(), LocalDate.of(2026, 5, 25));
//        patron.addCheckedOutBook(book4.getIsbn(), LocalDate.of(2026, 5, 25));


        checkout.addBook(book);
        checkout.addBook(book2);
        checkout.addBook(book3);
//        checkout.addBook(book4);
        checkout.addBook(checkBook);
        checkout.registerPatron(patron);

        int beforeCheckCount = patron.getCheckoutCount();
        double result = checkout.checkoutBook(checkBook, patron);



        assertEquals(3.2, result, 0.01,
                "Expected code 3.2 for patron AT checkout limit for " + checkoutClass.getSimpleName());

        //test state changes
        assertFalse(patron.hasBookCheckedOut(checkBook.getIsbn()),
                "Patron should NOT have book in list for " + checkoutClass.getSimpleName());
        assertTrue(patron.hasBookCheckedOut(book2.getIsbn()),
                "Patron should still have other previous books in list for " + checkoutClass.getSimpleName());
        assertEquals(beforeCheckCount, patron.getCheckoutCount(),
                "Patron checkout count should be +1 for " + checkoutClass.getSimpleName());
        assertEquals(3, patron.getMaxCheckoutLimit(),
                "Patron check count should Checkout limit should be 5 for PUBLIC " + checkoutClass.getSimpleName());
    }

    /**
     * T11: tests checkout with patron near fine limit. limit = 10.00
     * state changes: Checkout occurs: availableCopies decrease by 1; patron.getCheckedOutBooks() is updated
     * This tests an valid equivalence partition.
     */
    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T11: Test patron with 9.99 fines. near limit")
    public void testFineThresholdNear(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        // Setup: Create book
        Book book = new Book("978-0-123456-78-9", "Test Book",
                "Test Author", Book.BookType.FICTION, 5);
        Book book2 = new Book("978-0-443456-78-9", "Test Book2",
                "Test Author", Book.BookType.FICTION, 5);
        Book book3 = new Book("978-0-123444-78-9", "Test Book3",
                "Test Author", Book.BookType.FICTION, 5);
        Book book4 = new Book("978-0-128556-78-9", "Test Book4",
                "Test Author", Book.BookType.FICTION, 5);
        Book checkBook = new Book("978-0-128556-98-9", "Test Book4",
                "Test Author", Book.BookType.FICTION, 1);

        Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                Patron.PatronType.STUDENT);

//        patron.addCheckedOutBook(book.getIsbn(), LocalDate.of(2026, 5, 25) );
//        patron.addCheckedOutBook(book2.getIsbn(), LocalDate.of(2026, 5, 25));
//        patron.addCheckedOutBook(book3.getIsbn(), LocalDate.of(2026, 5, 25));
//        patron.addCheckedOutBook(book4.getIsbn(), LocalDate.of(2026, 5, 25));

        patron.addFine(9.99);

        checkout.addBook(book);
        checkout.addBook(book2);
        checkout.addBook(book3);
        checkout.addBook(book4);
        checkout.addBook(checkBook);
        checkout.registerPatron(patron);

        double result = checkout.checkoutBook(checkBook, patron);

        assertEquals(0.0, result, 0.01,
                "Expected  code 0.0 for patron near fine limit for " + checkoutClass.getSimpleName());

        assertEquals(9.99, patron.getFineBalance(), "Patron fine balance should be 9.99 " + checkoutClass.getSimpleName());
        assertTrue(patron.hasBookCheckedOut(checkBook.getIsbn()),
                "Patron should have book in list for " + checkoutClass.getSimpleName());
//        assertTrue(patron.hasBookCheckedOut(book2.getIsbn()),
//                "Patron should still have other previous books in list for " + checkoutClass.getSimpleName());

    }

    /**
     * T11: tests checkout when patron pay fine. limit = 10.00
     * state changes: Checkout occurs: availableCopies decrease by 1; patron.getCheckedOutBooks() is updated
     * This tests an valid equivalence partition.
     */
    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T12: Test patron with 10.01 fines pays 0.02. near limit")
    public void testFineThresholdCalculate(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        // Setup: Create book
        Book book = new Book("978-0-123456-78-9", "Test Book",
                "Test Author", Book.BookType.FICTION, 5);
        Book book2 = new Book("978-0-443456-78-9", "Test Book2",
                "Test Author", Book.BookType.FICTION, 5);
        Book book3 = new Book("978-0-123444-78-9", "Test Book3",
                "Test Author", Book.BookType.FICTION, 5);
        Book book4 = new Book("978-0-128556-78-9", "Test Book4",
                "Test Author", Book.BookType.FICTION, 5);
        Book checkBook = new Book("978-0-128556-98-9", "Test Book4",
                "Test Author", Book.BookType.FICTION, 1);

        Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                Patron.PatronType.STUDENT);

//        patron.addCheckedOutBook(book.getIsbn(), LocalDate.of(2026, 5, 25) );
//        patron.addCheckedOutBook(book2.getIsbn(), LocalDate.of(2026, 5, 25));
//        patron.addCheckedOutBook(book3.getIsbn(), LocalDate.of(2026, 5, 25));
//        patron.addCheckedOutBook(book4.getIsbn(), LocalDate.of(2026, 5, 25));

        patron.addFine(10.01);
        patron.payFine(0.02);

        checkout.addBook(book);
        checkout.addBook(book2);
        checkout.addBook(book3);
        checkout.addBook(book4);
        checkout.addBook(checkBook);
        checkout.registerPatron(patron);

        double result = checkout.checkoutBook(checkBook, patron);

        assertEquals(0.0, result, 0.01,
                "Expected  code 0.0 for patron near fine limit for " + checkoutClass.getSimpleName());

        assertEquals(9.99, patron.getFineBalance(), "Patron fine balance should be 9.99 " + checkoutClass.getSimpleName());
//        assertTrue(patron.hasBookCheckedOut(checkBook.getIsbn()),
//                "Patron should have book in list for " + checkoutClass.getSimpleName());
//        assertTrue(patron.hasBookCheckedOut(book2.getIsbn()),
//                "Patron should still have other previous books in list for " + checkoutClass.getSimpleName());

    }

    /**
     * T13: tests checkout with patron at fine limit. limit = 10.00
     * state changes: no change
     * This tests an invalid equivalence partition.
     */
    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T13: Test patron with 10.00 fines; at limit")
    public void testFineThresholdAt(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        // Setup: Create book
        Book book = new Book("978-0-123456-78-9", "Test Book",
                "Test Author", Book.BookType.FICTION, 5);
        Book book2 = new Book("978-0-443456-78-9", "Test Book2",
                "Test Author", Book.BookType.FICTION, 5);
        Book book3 = new Book("978-0-123444-78-9", "Test Book3",
                "Test Author", Book.BookType.FICTION, 5);
        Book book4 = new Book("978-0-128556-78-9", "Test Book4",
                "Test Author", Book.BookType.FICTION, 5);
        Book checkBook = new Book("978-0-128556-98-9", "Test Book4",
                "Test Author", Book.BookType.FICTION, 1);

        Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                Patron.PatronType.STUDENT);

//        patron.addCheckedOutBook(book.getIsbn(), LocalDate.of(2026, 5, 25) );
//        patron.addCheckedOutBook(book2.getIsbn(), LocalDate.of(2026, 5, 25));
//        patron.addCheckedOutBook(book3.getIsbn(), LocalDate.of(2026, 5, 25));
//        patron.addCheckedOutBook(book4.getIsbn(), LocalDate.of(2026, 5, 25));

        patron.addFine(10.00);

        checkout.addBook(book);
        checkout.addBook(book2);
        checkout.addBook(book3);
        checkout.addBook(book4);
        checkout.addBook(checkBook);
        checkout.registerPatron(patron);

        double result = checkout.checkoutBook(checkBook, patron);

        assertEquals(4.1, result, 0.01,
                "Expected  code 4.1 for patron at fine limit for " + checkoutClass.getSimpleName());

        assertEquals(10.00, patron.getFineBalance(), "Patron fine balance should be 10.00 " + checkoutClass.getSimpleName());
        assertFalse(patron.hasBookCheckedOut(checkBook.getIsbn()),
                "Patron should NOT have book in list for " + checkoutClass.getSimpleName());

//        assertTrue(patron.hasBookCheckedOut(book2.getIsbn()),
//                "Patron should still have other previous books in list for " + checkoutClass.getSimpleName());

    }

    /**
     * T14: tests checkout with patron above fine limit. limit = 10.00
     * state changes: no change
     * This tests an invalid equivalence partition.
     */
    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T14: Test patron with 10.01 fines; above limit")
    public void testFineThresholdAbove(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        // Setup: Create book
        Book book = new Book("978-0-123456-78-9", "Test Book",
                "Test Author", Book.BookType.FICTION, 5);
        Book book2 = new Book("978-0-443456-78-9", "Test Book2",
                "Test Author", Book.BookType.FICTION, 5);
        Book book3 = new Book("978-0-123444-78-9", "Test Book3",
                "Test Author", Book.BookType.FICTION, 5);
        Book book4 = new Book("978-0-128556-78-9", "Test Book4",
                "Test Author", Book.BookType.FICTION, 5);
        Book checkBook = new Book("978-0-128556-98-9", "Test Book4",
                "Test Author", Book.BookType.FICTION, 1);

        Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                Patron.PatronType.STUDENT);

//        patron.addCheckedOutBook(book.getIsbn(), LocalDate.of(2026, 5, 25) );
//        patron.addCheckedOutBook(book2.getIsbn(), LocalDate.of(2026, 5, 25));
//        patron.addCheckedOutBook(book3.getIsbn(), LocalDate.of(2026, 5, 25));
//        patron.addCheckedOutBook(book4.getIsbn(), LocalDate.of(2026, 5, 25));

        patron.addFine(10.01);

        checkout.addBook(book);
        checkout.addBook(book2);
        checkout.addBook(book3);
        checkout.addBook(book4);
        checkout.addBook(checkBook);
        checkout.registerPatron(patron);

        double result = checkout.checkoutBook(checkBook, patron);

        assertEquals(4.1, result, 0.01,
                "Expected  code 4.1 for patron at fine limit for " + checkoutClass.getSimpleName());

        assertEquals(10.01, patron.getFineBalance(), "Patron fine balance should be 10.00 " + checkoutClass.getSimpleName());
        assertFalse(patron.hasBookCheckedOut(checkBook.getIsbn()),
                "Patron should NOT have book in list for " + checkoutClass.getSimpleName());
//        assertTrue(patron.hasBookCheckedOut(book2.getIsbn()),
//                "Patron should still have other previous books in list for " + checkoutClass.getSimpleName());
    }

    /**
     * T15: Tests successful renewal book
     * This tests the valid equivalence partition - all conditions met.
     */
    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T15\15: Successful checkout - available book, eligible patron")
    public void testCheckoutRenewal(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        // Setup: Create available book and eligible patron
        Book book = new Book("978-0-123456-78-9", "Test Book",
                "Test Author", Book.BookType.FICTION, 2);

        Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                Patron.PatronType.STUDENT);

        patron.addCheckedOutBook(book.getIsbn(), LocalDate.of(2026, 5, 25));

        checkout.addBook(book); // adding the book to the library
        checkout.registerPatron(patron); // adding a patrol to the system

        // Verify: Patron should have the book in their checked-out list
        assertTrue(patron.hasBookCheckedOut(book.getIsbn()),
                "Patron should have book in checked-out before renewal for " + checkoutClass.getSimpleName());

        // Execute checkout
        int availableCopiesBefore = book.getAvailableCopies();
        double result = checkout.checkoutBook(book, patron);

        // Verify: Should return 0.1 for success
        assertEquals(0.1, result, 0.01,
                "Expected successful checkout (0.1) for " + checkoutClass.getSimpleName());

        assertEquals(availableCopiesBefore, book.getAvailableCopies(),"Book availability should be the same after renewal for" + checkoutClass.getSimpleName());

        // Verify: Book should now be unavailable
        assertTrue(book.isAvailable(),
                "Book should be available after checkout for " + checkoutClass.getSimpleName());

        // Verify: Patron should have the book in their checked-out list
        assertTrue(patron.hasBookCheckedOut(book.getIsbn()),
                "Patron should still have book in checked-out list after renewal for " + checkoutClass.getSimpleName());

        // Verify: Checkout count stayed the same
        assertEquals(1, patron.getCheckoutCount(),
                "Patron checkout count should be 1 for " + checkoutClass.getSimpleName());
    }

    /**
     * T16: Tests successful book checkout and return
     * This tests the valid equivalence partition - all conditions met.
     */
    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T16\15: Successful checkout - available book, eligible patron")
    public void testNormalSuccessAndReturn(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        // Setup: Create available book and eligible patron
        Book book = new Book("978-0-123456-78-9", "Test Book",
                "Test Author", Book.BookType.FICTION, 1);

        Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                Patron.PatronType.STUDENT);
        patron.setOverdueCount(0);
        patron.resetFines();
        patron.setAccountSuspended(false);


        checkout.addBook(book); // adding the book to the library
        checkout.registerPatron(patron); // adding a patrol to the system

        // Execute checkout
        int availableCopiesBefore = book.getAvailableCopies();
        double result = checkout.checkoutBook(book, patron);

        // Verify: Should return 0.0 for success
        assertEquals(0.0, result, 0.01,
                "Expected successful checkout (0.0) for " + checkoutClass.getSimpleName());
//        // Verify: Patron should have the book in their checked-out list
//        assertTrue(patron.hasBookCheckedOut(book.getIsbn()),
//                "Patron should have book in checked-out list after checkout for " + checkoutClass.getSimpleName());
//        assertEquals(availableCopiesBefore - 1 , book.getAvailableCopies(),
//                "Book availability should be -1 after checkout for" + checkoutClass.getSimpleName());


        //Return book
        checkout.returnBook(book.getIsbn(), patron);

        assertTrue(book.isAvailable(),
                "Book should be available after return for " + checkoutClass.getSimpleName());

        assertFalse(patron.hasBookCheckedOut(book.getIsbn()),
                "Patron should NOT have book in checked-out list after return for " + checkoutClass.getSimpleName());
        // Verify: Checkout count stayed the same
        assertEquals(0, patron.getCheckoutCount(),
                "Patron checkout count should be 0 after return for " + checkoutClass.getSimpleName());
        assertEquals(availableCopiesBefore, book.getAvailableCopies(),
                "Book availability should be the same after return for" + checkoutClass.getSimpleName());
    }

    /**
     * T17: Tests that patron should not checkout a reference book
     * This tests the invalid equivalence partition
     */
    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T17\15: unable to Checkout reference book")
    public void testReferenceBook(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        // Setup: Create available book and eligible patron
        Book book = new Book("978-0-123456-78-9", "Test Book",
                "Test Author", Book.BookType.REFERENCE, 2);

        Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                Patron.PatronType.STUDENT);

        checkout.addBook(book); // adding the book to the library
        checkout.registerPatron(patron); // adding a patrol to the system

        // Execute checkout
        int availableCopiesBefore = book.getAvailableCopies();
        double result = checkout.checkoutBook(book, patron);

        assertEquals(5.0, result, 0.01,
                "Expected code 5.0 for " + checkoutClass.getSimpleName());

        assertEquals(availableCopiesBefore, book.getAvailableCopies(),"Book availability should be the same after trying to check-out reference book for" + checkoutClass.getSimpleName());


        assertFalse(book.isAvailable(),
                "Book should be unavailable for " + checkoutClass.getSimpleName());

        assertFalse(patron.hasBookCheckedOut(book.getIsbn()),
                "Patron should NOT have book in checked-out list for " + checkoutClass.getSimpleName());

        assertEquals(0, patron.getCheckoutCount(),
                "Patron checkout count should be 0, after trying reference book for " + checkoutClass.getSimpleName());
    }

    /**
     * T18: Tests that suspended patron should not checkout a book
     * This tests the invalid equivalence partition
     */
    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T18\15:Suspended patron unable to Checkout book")
    public void testPatronSuspension(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        // Setup: Create available book and eligible patron
        Book book = new Book("978-0-123456-78-9", "Test Book",
                "Test Author", Book.BookType.NONFICTION, 2);

        Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                Patron.PatronType.STUDENT);

        patron.setAccountSuspended(true);

        checkout.addBook(book); // adding the book to the library
        checkout.registerPatron(patron); // adding a patrol to the system

        // Execute checkout
        int availableCopiesBefore = book.getAvailableCopies();
        double result = checkout.checkoutBook(book, patron);

        assertEquals(3.0, result, 0.01,
                "Expected code 3.0 for patron suspension in" + checkoutClass.getSimpleName());

        assertEquals(availableCopiesBefore, book.getAvailableCopies(),"Book availability should be the same after trying to check-out reference book for" + checkoutClass.getSimpleName());


        assertTrue(book.isAvailable(),
                "Book should be unavailable for " + checkoutClass.getSimpleName());

        assertTrue(patron.isAccountSuspended(),
                "Patron should be suspended for " + checkoutClass.getSimpleName());

        assertFalse(patron.hasBookCheckedOut(book.getIsbn()),
                "Patron should NOT have book in checked-out list for " + checkoutClass.getSimpleName());

        assertEquals(0, patron.getCheckoutCount(),
                "Patron checkout count should be 0, after trying reference book for " + checkoutClass.getSimpleName());
    }

    /**
     * T19: Tests that patron should not exists; null
     * This tests the invalid equivalence partition
     */
    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T19\15:null patron returns error code right away")
    public void testPatronNull(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        // Setup: Create available book and eligible patron
        Book book = new Book("978-0-123456-78-9", "Test Book",
                "Test Author", Book.BookType.NONFICTION, 2);

        Patron patron = null;

        checkout.addBook(book); // adding the book to the library
//        checkout.registerPatron(patron); // adding a patrol to the system

        // Execute checkout
        int availableCopiesBefore = book.getAvailableCopies();
        double result = checkout.checkoutBook(book, patron);

        assertEquals(3.1, result, 0.01,
                "Expected code 3.1 for patron suspension in" + checkoutClass.getSimpleName());

        assertEquals(availableCopiesBefore, book.getAvailableCopies(),"Book availability should be the same after trying to check-out reference book for" + checkoutClass.getSimpleName());

        assertTrue(book.isAvailable(),
                "Book should be unavailable for " + checkoutClass.getSimpleName());

//        assertTrue(patron.isAccountSuspended(),
//                "Patron should be suspended for " + checkoutClass.getSimpleName());
//
//        assertFalse(patron.hasBookCheckedOut(book.getIsbn()),
//                "Patron should NOT have book in checked-out list for " + checkoutClass.getSimpleName());
//
//        assertEquals(0, patron.getCheckoutCount(),
//                "Patron checkout count should be 0, after trying reference book for " + checkoutClass.getSimpleName());
    }

    /**
     * T20: Tests that book is null, and return error code
     * This tests the invalid equivalence partition
     */
    @ParameterizedTest
    @MethodSource("checkoutClassProvider")
    @DisplayName("T20\15:Suspended patron unable to Checkout book")
    public void testBookNull(Class<? extends Checkout> checkoutClass) throws Exception {
        checkout = createCheckout(checkoutClass);

        // Setup: Create available book and eligible patron
        Book book = null;

        Patron patron = new Patron("P001", "Test Patron", "test@example.com",
                Patron.PatronType.STUDENT);

//        patron.setAccountSuspended(true);

//        checkout.addBook(book); // adding the book to the library
        checkout.registerPatron(patron); // adding a patrol to the system

        // Execute checkout
        double result = checkout.checkoutBook(book, patron);

        assertEquals(2.1, result, 0.01,
                "Expected code 2.1 for patron suspension in" + checkoutClass.getSimpleName());

        assertFalse(patron.isAccountSuspended(),
                "Patron should not be suspended for " + checkoutClass.getSimpleName());
    }
}
