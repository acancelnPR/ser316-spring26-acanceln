# Black Box Testing Report - Assignment 2

**Student Name:** Andres Cancel 
**ASU ID:** 1225127709
**Date:** 1/23/26  

---

## Part 1: Equivalence Partitioning (EP)

Identify equivalence partitions for the `checkoutBook(Book book, Patron patron)` method based on the specification (JavaDoc).

Create **multiple tables**, one per partition category (e.g., book state, patron state, renewal, limits, etc.).

Do **not** put everything into one table.

**Column Explanations:**
- **Partition ID**: Unique identifier (e.g., EP 1.1, EP 2.1)
- **State**: The specific state/value for this partition (e.g., "Unavailable", "Available")
- **Valid/Invalid**: Whether this partition represents valid or invalid input
- **Input Condition**: Precise condition that defines this partition
- **Expected Return**: What return code you expect
- **Expected Behavior**: What should happen

### Example EP Table: Book Availability

| Partition ID | State | Valid/Invalid | Input Condition | Expected Return | Expected Behavior |
|--------------|-------|---------------|----------------|-----------------|------------------|
| EP 1.1 | Unavailable (0 copies) | Invalid | availableCopies == 0 AND other conditions allow checkout | 2.0 | No copies to checkout |
| EP 1.2 | Available (1+ copies) | Valid | availableCopies > 0 AND other conditions allow checkout | Success | Book can be checked out |

**Example test cases:** `testBookAvailable()`, `testUnavailableBook()`

---

EP Table: Book Type

| Partition ID | State | Valid/Invalid | Input Condition | Expected Return | Expected Behavior |
|--------------|-------|---------------|----------------|-----------------|------------------|
| EP 1.1 | Book is null/nonexistent | Invalid | book == null | 2.1 | book does not exist, return error code |
| EP 1.2 | Book is reference-only| Invalid | book.isReferenceOnly() == true AND patron is eligible | 5.0 | Book cannot be checked out. Return error code|
| EP 1.3 | Book is Normal | Valid | Book not null AND is not reference. Patron must be eligible | success OR 2.0 | Book may be checked out |


EP Table: Book Availability

| Partition ID | State | Valid/Invalid | Input Condition | Expected Return | Expected Behavior |
|--------------|-------|---------------|----------------|-----------------|------------------|
| EP 2.1 | Unavailable (0 copies) | Invalid | availableCopies == 0 AND other conditions allow checkout | 2.0 | No copies to checkout |
| EP 2.2 | Available (1+ copies) | Valid | availableCopies > 0 AND other conditions allow checkout | Success | Book can be checked out |


EP Table: Patron Eligibility

| Partition ID | State | Valid/Invalid | Input Condition | Expected Return | Expected Behavior |
|--------------|-------|---------------|----------------|-----------------|------------------|
| EP 3.1 | Patron is null | invalid | validatePatronEligibility() == 3.1 | 3.1 | return error code. Patron is null. |
| EP 3.2 | Patron is suspended | invalid | validatePatronEligibility() == 3.0 | 3.0 | return error code. Patron is suspended |
| EP 3.3 | Patron past overdue limit | invalid | validatePatronEligibility() == 4.0 | 4.0 |return error code. Patron past overdue limit |
| EP 3.4 | Patron over fine limit | invalid | validatePatronEligibility() == 4.1 | 4.1 | return error code. Patron has $10.00 or more in unpaid fines |
| EP 3.5 | Patron is Eligible | Valid | validatePatronEligibility() == 0.0 | success | Patron is allowed to checkout a book |


EP Tables: Renewal, Not-Renewal, checkout limit

| Partition ID | State | Valid/Invalid | Input Condition | Expected Return | Expected Behavior |
|--------------|-------|---------------|----------------|-----------------|------------------|
| EP 4.1 | renewal | valid | patron had the book AND other conditions allow checkout  | 0.1 | patron.getCheckedOutBooks() is updated. book.checkout() is not called; available copies do not change.|
| EP 4.2 | not-renewal | valid | all conditions allow checkout AND does not have book| success | Patron successfully checks out a book|
| EP 4.3 | checkout limit reached | invalid | praton.getCheckoutCount() >= patron.getMaxCheckoutLimit() | 3.2 | return error code. Patron reached checkout limit.|


EP Tables: Success Warnings

| Partition ID | State | Valid/Invalid | Input Condition | Expected Return | Expected Behavior |
|--------------|-------|---------------|----------------|-----------------|------------------|
| EP 5.1 | Success w/ near limit warning | valid | all conditions allow checkout AND praton.getCheckoutCount() is within 2 books patron.getMaxCheckoutLimit() | 1.1 | patron checkout book. patron within 2 of max checkout limit after this checkout|
| EP 5.2 | Success w/ overdue warning | valid | all conditions allow checkout AND patron.getOverdueCount() <= 2 | 1.0 | patron checkout book. patron has 1-2 overdue books|
| EP 5.3 | Success | valid | all conditions allow checkout AND patron is NOT near checkout limit AND does not have overdue books | 0.0 | book checked out normally |

---

## Part 2: Boundary Value Analysis (BVA)

---

### Your BVA Tables (add more as needed)



BVA Table: Overdue Count

| Test ID | Boundary | Input Value | Expected Return | Rationale |
|---------|----------|-------------|-----------------|-----------|
| BVA 1.1 | Below | overdueCount = 0 | Success (depends on other setup) | Below warning threshold |
| BVA 1.2 | Warning High | overdueCount = 2 | 1.0 | Just below reject threshold |
| BVA 1.3 | At | overdueCount = 3 | 4.0 | At rejection boundary |
| BVA 1.4 | Above | overdueCount = 4 | 4.0 | Above rejection boundary |
| BVA 1.5 | Warning Low | overdueCount = 1 | 1.0 | Start of Warning boundry|


BVA Table: Book Availability

| Test ID | Boundary | Input Value | Expected Return | Rationale |
|---------|----------|-------------|-----------------|-----------|
| BVA 2.1 | Below Range | availableCopies = 0 | 2.0 | Below range of available book |
| BVA 2.2 | At | availableCopies = 1 | Success (depends on other setup) | there is 1 book available to rent|

BVA Table: Fines Limits

| Test ID | Boundary | Input Value | Expected Return | Rationale |
|---------|----------|-------------|-----------------|-----------|
| BVA 3.1 | At | fines = $10 | 4.1 | at rejection boundry |
| BVA 3.2 | Just above | fines = $10.01  | 4.1 | above and near rejection boundry |
| BVA 3.3 | Just Below | fines = $9.99  | Success (depends on other setup) | Below rejection boundry|

BVA Table: Checkout Limit

| Test ID | Boundary | Input Value | Expected Return | Rationale |
|---------|----------|-------------|-----------------|-----------|
| BVA 4.1 | At | getCheckoutCount() = MAX | 3.2 | Patron at maximum checkout limit (FACULTY=20, STAFF=15, STUDENT=10, PUBLIC=5, CHILD=3) |
| BVA 4.2 | Warning High | getCheckoutCount() = Max - 1 OR getCheckoutCount() = Max - 2 | 1.1 | Just below reject threshold |
| BVA 4.3 | Above | getCheckoutCount() = MAX + 1 | 3.2 | Above rejection boundary. Should not happen. |
| BVA 4.4 | Below | getCheckoutCount() = MAX - 3 | Success (depends on other setup) | Below warning threshold  |

---

## Part 3: Test Cases Designed

List at least **20** test cases you designed based on your EP/BVA analysis.

Each test case should include:
- EP/BVA coverage
- specific inputs/setup
- expected return code
- expected **observable state changes** (if any)

> Do not test console output.

### Test Case Table
At least some of your tests should verify observable state changes, not just return values.

**Checkout0-3 Columns:** Mark each implementation as Pass (✓) or Fail (✗) for this test case. This helps you track which implementations have bugs and will be useful for Part 4 analysis.

| Test ID Name | EP/BVA | Input Description | Expected Return | Expected State Changes | Checkout0 | Checkout1 | Checkout2 | Checkout3 |
|--------------|--------|-------------------|-----------------|------------------------|-----------|-----------|-----------|-----------|
| T1 testUnavailableBook | EP 1.1/BVA 2.1 | Book unavailable (0 copies), eligible patron | 2.0 | No state change | ✓ | ✓ | ✗ | ✓ |
| T2 testBookAvailable | EP 1.2/BVA 2.2 | Book available (1+ copies), eligible patron, no warnings normal checkout | 0.0 | Patron map updated; copies of book change | ✓ |  ✗  | ✓ | ✓ |
| T3 testOverdueAbove | EP 3.3 / BVA 1.4 | overdueCount = 4; normal book, eligible patron otherwise | 4.0 | No state change |✓ |✓ | ✓  |✓ |
| T4 testOverdueAt | EP 3.3 / BVA 1.3 | overdueCount = 3; normal book, eligible patron otherwise | 4.0 | No state change |✓ |✓ | ✓  |✓ |
| T5 testOverdueWarningHigh | EP 4.2 / BVA 1.2 | overdueCount = 2, normal book, eligible patron otherwise  | 1.0 | Checkout occurs: availableCopies decrease by 1; Overdue warning displayed |✗|✗|✓|✗|
| T6 testOverdueWarningLow | EP 5.2 / BVA 1.5 | overdueCount = 1; normal book, eligible patron otherwise  | 1.0 | Checkout occurs: availableCopies decrease by 1; Overdue warning displayed  |✓|✗|✗|✗|
| T7 testOverdueBelow | EP 5.3 / BVA 1.1 | overdueCount = 0; normal book, eligible patron otherwise   | 0.0 | Checkout occurs: availableCopies decrease by 1 |✗|✗|✓|✓|
| T8 testCheckoutLimitAbove | EP 4.3 / BVA 4.3 | getCheckoutCount() = MAX + 1  | 3.2 | No state change|✓ |✓ | ✓  |✓ |
| T9 testCheckoutLimitWarningHigh | EP 5.1 / BVA 4.2 | getCheckoutCount() = Max - 1; | 1.1 | Checkout occurs: availableCopies decrease by 1; Checkout warning displayed|✓ |X | ✓  |✓ |
| T10 testCheckoutLimitAt | EP 4.3 / BVA 4.1 | getCheckoutCount() = MAX  | 3.2 | No state change |✓ |X | ✓  |✓ |
| T11 testFineThresholdNear | EP 5.3 / BVA 3.3 | fines = 9.99 | 0.0 | Checkout occurs: availableCopies decrease by 1; patron.getCheckedOutBooks() is updated  |✓ |X | ✓  |✓ |
| T12 testFineThresholdCalculates | EP 5.3 / BVA 3.3 | addfine(10.01); payfine(0.02) | 0.0 | Checkout occurs: availableCopies decrease by 1; patron.getCheckedOutBooks() is updated  |✓ |✓ | ✓  |✓ |
| T13 testFineThresholdAt | EP 3.4 / BVA 3.1 | fines = 10 | 4.1 | No state change |✓ |✓ | ✓  |✓ |
| T14 testFineThresholdAbove | EP 3.4 / BVA 3.2 | fines = 10.01 | 4.1 | No state change |✓ |✓ | ✓  |✓ |
| T15 testCheckoutRenewal | EP 4.1 | overdue=0, fines < 10, availableCopies > 0,  | 0.1 | Checkout occurs; patron.getCheckedOutBooks() is updated |✓|✓|✗|✗|
| T16 testNormalSuccessAndReturn| EP 5.3 | overdue=0, fines < 10, availableCopies > 0, patron.hadcheckedoutbook()=true| 0.0 | Checkout occurs: availableCopies stay the same after return; patron.getCheckedOutBooks() is updated 
|✗|✗|✓|✓|
| T17 testReferenceBook | EP 1.2 | book.isReferenceOnly() == true AND patron is eligible  | 5.0 | No state change |X |✓ | ✓  |✓ |
| T18 testPatronSuspension |EP 3.2 | patronType = suspended | 3.0 | No state change |✓ |✓ | ✓  |✓ |
| T19 testPatronNull |EP 3.1| patron = null | 3.1 | No state change |✓ |✓ | ✓  |✓ |
| T20 testBookNull | EP 1.1 | book = null, patron eligible | 2.1 | No state change |✓ |✓ | ✓  |✓ |


---

## Part 4: Bug Analysis

### Easter Eggs Found
List any easter egg messages you observed:
- [EASTER EGG #10.1]: 'Testing can show the presence of bugs,'
- [EASTER EGG #17]: 'The happy path matters too.'
- [EASTER EGG #19]: 'Availability testing finds the books that aren't there.'
- [EASTER EGG #19]: 'Can't check out what isn't there.'
- [EASTER EGG #19]: 'Good EP testing checks all partitions.'
- [EASTER EGG #19]: 'Testing the sad path matters.'
- [EASTER EGG #17]: 'The happy path matters too.'
- [EASTER EGG #15.2]: ...xvFZjo5PgG0 (test renewal to complete!)
- [EASTER EGG #10.1/3]: 'Testing can show the presence of bugs,'
- [EASTER EGG #10.3]: '- Dijkstra'
- [EASTER EGG #10.2]: 'but never their absence'
- [EASTER EGG #10.2/3]: 'but never their absence'
- [EASTER EGG #15.1]: https://www.youtube.com/watch?v=xvFZjo5PgG0
- [EASTER EGG #13]: 'Limits exist to be thoroughly tested.'
- [EASTER EGG #13]: 'Boundaries are where bugs hide.'
- [EASTER EGG #13]: 'The difference between theory and practice is that in theory, there is no difference.'
- [EASTER EGG #14]: 'A book renewed is a book re-loved.'
- [EASTER EGG #14]: 'Renewing a book is like giving it a second chance.'
- [EASTER EGG #20]: 'Reference books are meant to be consulted, not carried home.'
- [EASTER EGG #18]: 'Null checking: because null pointer exceptions are not fun.'
- [EASTER EGG #18]: 'Remember to test all the edge cases.'
- [EASTER EGG #18]: 'The best code is no code at all... but this isn't it.'


### Implementation Results

| Implementation | Bugs Found (count) |
|----------------|---------------------|
| Checkout0      | 4|
| Checkout1      | 4|
| Checkout2      | 3|
| Checkout3      | 3|

### Bugs Discovered
List distinct bugs you identified for each implementation. Each bug must cite at least one test case that revealed it.

**Checkout0:**
- Bug 1: [Book should be unavailable after checkout] — Revealed by: [T2]
- Bug 2: [Book availability should be -1] — Revealed by: [T5]
- Bug 3: [Book availability should be the same after return] — Revealed by: [T0]
- Bug 4: [Expected code 5.0 for reference book] — Revealed by: [17]

**Checkout1:**
- Bug 1: [Checked out list not updating when available book] — Revealed by: [T2]
- Bug 2: [Patron should have book in list, but doesn't] — Revealed by: [T5]
- Bug 3: [Expected code 3.2 for patron AT checkout limit] — Revealed by: [10]
- Bug 4: [Book should be available after return] — Revealed by: [T16]


**Checkout2:**
- Bug 1: [Availability testing finds the books that aren't there] — Revealed by: [T1]
- Bug 2: [Book availability should be -1] — Revealed by: [T6]
- Bug 3: [Expected successful renewal (0.1)] — Revealed by: [T15]


**Checkout3:**
- Bug 1: [Expected error code 1.0 for patron near Overdue limit, but returns 0.0] — Revealed by: [T5]
- Bug 2: [Book availability should be -1] — Revealed by: [T6]
- Bug 3: [Book availability should be the same after renewal] — Revealed by: [T15]


### Comparative Analysis
Compare the four implementations:
- Which bugs are most critical (cause the worst failures)?
 	When a patron should have a specific book checked out, but doesn't.
 	Also, finding books that aren't actually unavailable.

- Which implementation would you use if you had to choose? 
	I would choose implementation 3
- Why? Justify your choice considering bug severity and frequency.
	The book availability inaccuracy is a bug that affects patrons but does not break the system or cause severe consequences for the business.
	The last 2 bugs I found are similar, and I believe they have the same root cause.

---

## Part 5: Reflection

**Which testing technique was most effective for finding bugs?**
	Testing the state of the objects in question was most effective because the return code does not indicate the other states of the system.

**What was the most challenging aspect of this assignment?**
	Designing tests that change multiple states of the system.

**How did you decide on your EP and BVA?**
I reviewed the validation order and identified common themes. Then broke it down into limits and boundaries that change the program's state.

**Describe one test where checking only the return value would NOT have been sufficient to detect a bug.**
T2-testBookAvailable: Just looking at the return code, you would not have noticed that the patron does not have the book in their inventory.


