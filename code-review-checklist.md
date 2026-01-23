# Code Review Checklist

**Reviewer Name:** Andres Cancel
**Date:** 1/23/2026
**Branch:** Blackbox

## Instructions
Review ALL source files (in main not test) in the project and identify defects using the categories below. Log at least 5 defects total:
- At least 1 from CS (Coding Standards)
- At least 1 from CG (Code Quality/General)
- At least 1 from FD (Functional Defects)
- Remaining can be from any category

## Review Categories

- **CS**: Coding Standards (naming conventions, formatting, style violations)
- **CG**: Code Quality/General (design issues, code smells, maintainability)
- **FD**: Functional Defects (logic errors, incorrect behavior, bugs)
- **MD**: Miscellaneous (documentation, comments, other issues)

## Defect Log

| Defect ID | File | Line(s) | Category | Description | Severity |
|-----------|------|---------|----------|-------------|----------|
| 1 | Patron.java | 151 | CG | Unnecessary if-else statement, it does not add any functionality. | Medium |
| 2 | Patron.java | 192 | CG | Unnecessary if-else statement, it does not add any functionality. | Medium |
| 3 | Book.java | 121 | MD | Need documentation on for override method | Low |
| 4 | Book.java | 106 | FD | Magic number 100 should be totalCopies | High |
| 5 | Checkout.java | 15 | CS | Variable bookList misleading - Map not List | Medium |
| 6 | Checkout.java | 216 | FD | The hyphen format is not enforced | Critical |
| 7 | Patron.java | 162 | FD | Does not check for negative values | Critical |
| 8 | Checkout.java | 255 | MD | doc incorrect return values. Include -1.0 for errors. | High |
| 9 | Book.java | 13 | CG | variable available, is a risk, and not utilized properly. There is a getter method that checks availability on demand; this field is unnecessary and causes maintainability issues. | High |
| 10 | Book.java | 117 | CG | There is a getter that checks for availability on demand. Reference defect #9 | High |

**Severity Levels:**
- **Critical**: Causes system failure, data corruption, or security issues
- **High**: Major functional defect or significant quality issue
- **Medium**: Moderate issue affecting maintainability or minor functional problem
- **Low**: Minor style issue or cosmetic problem

## Example Entry

| Defect ID | File          | Line(s) | Category | Description                                | Severity |
|-----------|---------------|---------|----------|--------------------------------------------|----------|
| 1 | Checkout.java | 17      | CS       | Variable bookList misleading - Map not List | Medium |
| 2 | Book.java     | 107     | FD       | Magic number 100 should be totalCopies      | High |

## Notes
- Be specific with line numbers
- Provide clear, actionable descriptions
- Consider: readability, maintainability, correctness, performance, security
- Focus on issues that impact code quality or functionality
