# White Box Testing Report - Assignment 3

**Student Name:** Andres J cancel Nadal
**ASU ID:** 1225127709
**Date:** 2/3/2026

---

## Part 1: Control Flow Graph for countBooksByType()

### Graph Description

Draw or describe your control flow graph here. Include:
- Node numbers and what they represent
	Nodes represent statements. node1 = the first statement in the method. Displayed as Circles.
- Edges showing control flow
	The flow between the nodes; so the path the code is running. Displayed as arrows
- Conditions at decision points
	Conditional statements are represented by circles with multiple edges, demonstrating different paths.
	

**You can hand-draw and insert an image, or describe it in text format.**

### Node Coverage Sequences

List the sequences needed for complete node coverage:

**Sequence 1:**
- **Path:**
N1 > N2 > 20
- **Purpose:**
type is null; early return
- **Test case:**
testCountBooksByType_NullType


**Sequence 2:**
- **Path:**
N1 > N3 > N4 > N5 > N6 > N7 > N5 > N6 > N8 > N9 > N17 > N18 > 
N5 > N6 > N8 > N9 > N10 > N11 > N12 > N13 > N17 > N18 > N19 > N20
- **Purpose:** 
Only counts books that are available.
- **Test case:**
testCountBooksByType_Available

**Sequence 3:**
- **Path:**
N1 > N3 > N4 > N5 > N6 > N7 > N5 > N6 > N8 > N9 > N10 > N14 > N15 > N16 > N17 > N18 > N19 > N20
- **Purpose:** 
Counts books of all types regardless OF AVAILABILITY.
- **Test case:**
testCountBooksByType_AvailableOrNot



### Edge Coverage Sequences

List the sequences needed for complete edge coverage:

**Sequence 1: Early return ; type is null**
- **Edges covered:**
 N1-F > N2 > N20
- **Test case:**
testCountBooksByType_NullType


**Sequence 2:** edges inside loop
- **Edges covered:**
N6-T > N5
N9-F > N5
N10-F > N14
N10-T > N11
N11-F > N5
N11-T > N12

- **Test case:**
testCountBooksByType_AvailableOrNot
testCountBooksByType_Available


---

## Part 2: Code Coverage with JaCoCo

### Initial Coverage for Checkout.java

**Before adding tests:**
- **Line Coverage:** 44%
- **Branch Coverage:** 40%

### Coverage for countBooksByType()

**Before additional tests:**
- **Branch Coverage:** 47%

**After reaching 80% branch coverage:**
- **Branch Coverage:** 64%
- **Tests added:**
testCalculateFine_numDays15AndReferenceOrTextbook
testCalculateFine_numDaysSeven
testCalculateFine_numDaysZero
testIsValidISBN_IncorrectFormat
testIsValidISBN_CorrectType
testIsValidISBN_NullandIsEmpty


### Final Overall Coverage

- **Line Coverage:** 64%
- **Branch Coverage:** 57%

---

## Part 3: checkoutBook() Implementation

### Test-Driven Development Process

**Number of tests from BlackBox assignment:** 20

**Implementation challenges:**
1. It was relatively easy. I don't think I had any memorable challenges
2.

**All tests passing:** [Yes]

---

## Part 4: Reflection

**How did white-box testing differ from black-box testing?**
In whitebox testing, I had to think about which paths the code would take, and I can to methodically design the "setup" so the code travels through the desired paths

**Which approach do you find more effective? Why?**
I think whitebox is more effective because it tests the internals and you can ensure all paths are tested and behave correctly.

**Would you prefer TDD or implementation first test later? Why?**
I think I would prefer TDD. Because I can detect problems early.