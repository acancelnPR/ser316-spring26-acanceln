# Static Analysis Report - Assignment 4

**Student Name:** Andres Cancel
**ASU ID:** acanceln
**Date:** 2/8/2026

---

## Part 1: GitHub Actions Setup

**Which branches fail on GitHub Actions? Explain why.**

1. **Branch:** main
   - **Status:** Failing
   - **Reason:failed builds because some tests are not passing**

2. **Branch:** Blackbox
   - **Status:**  Failing
   - **Reason:failed builds because some tests are not passing**

3. **Branch:** Review 
   - **Status:** Passing / Failing
   - **Reason:failed builds because some tests are not passing**

4. **Branch:** Whitebox
   - **Status:** Passing
   - **Reason:all tests passed**

5. **Branch:** StaticAnalysis
   - **Status:** Passing
   - **Reason:all tests passed**


---

## Part 2: Checkstyle Analysis

### Initial Results (StaticAnalysis branch - before fixes)

**Main source violations:** 20 ~
**Test source violations:** 174
(I did not count the initial violation, so I am estimating.)
(I was looking at the PDF while doing this part and not this md document)
### After Fixing Issues

**Main source violations:** 8
**Violations fixed:** 15 ~

---

## Part 3: SpotBugs Analysis

### Initial Results (StaticAnalysis branch - before fixes)

**Bugs found in main:** 7

### Bugs Fixed

1. **Bug:** [bad practice - comparing using "==" instead of .equals()]
   - **File:** [Checkout.java]
   - **Location:** Line 347
   - **Fix applied: replaced "==" with .equals()**

2. **Bug:** [Malicious Code - exposing internal representation]
   - **File:** [Checkout.java]
   - **Location:** Line 441 & 445
   - **Fix applied: removed those variables**

3. **Bug:** [Performance - field never read]
   - **File:** [Checkout.java]
   - **Location:** Line 70 & 71
   - **Fix applied: provided a clone of the map**

---

## Part 4: Branch Comparison

### Checkstyle Comparison

| Branch | Main Violations | Test Violations | Total |
|--------|----------------|-----------------|-------|
| Blackbox | 27|4 | 31|
| Review |27|4 | 31|
| StaticAnalysis (initial) |36 |34 | 70|
| StaticAnalysis (after fixes) | 8| 34| 42|

### SpotBugs Comparison

| Branch | Main Bugs | Test Bugs | Total |
|--------|-----------|-----------|-------|
| Blackbox |6 |0 |6 |
| Review |6 |0 |6 |
| StaticAnalysis (initial) |6 |0 |6 |
| StaticAnalysis (after fixes) | 0|0 |0 |

**Did Review branch improve code quality compared to Blackbox?**
Not for checkout.java. The issues fixed were in other classes, such as patron.java and book.java. So checkout.java does not show progress.

---

## Part 5: Merging to Dev Branch

### Merge Strategy

**How did you merge Review and StaticAnalysis into Dev?**


**Merge conflicts encountered:** _____

### Dev Branch Quality After Merge

**Checkstyle violations:** _____
**SpotBugs issues:** _____

**Did quality improve or worsen? Explain:**


**Build successful:** [Yes/No]

---

## Part 6: Reflection

**Do you think your code got better through this process?**


**In what order would you use these quality practices in the future?**


**Most valuable lesson:**


