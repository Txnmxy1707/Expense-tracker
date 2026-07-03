#EXPENSE TRACKER/BUDGETTING APP
Overview
This project is a desktop‑based Expense Tracker application built using Java Swing for the user interface and PostgreSQL for data persistence. It allows users to record daily expenses, categorize them, and view totals in a simple tabular format.

Features
User Interface: Built with Java Swing, including input fields, category dropdown, and a date picker (via JCalendar).

Database Integration: PostgreSQL backend connected through JDBC. Expenses are stored in a table with fields for amount, category, date, and note.

Dynamic Table Updates: Uses DefaultTableModel to display existing records and add new rows instantly.

Total Calculation: Automatically updates the total expense amount whenever a new entry is added.

Clear Functionality: Provides a button to clear all entries from the table and reset the total.

Technologies Used
Java (JDK 26)

Swing (GUI toolkit)

PostgreSQL

JDBC (Database connectivity)

JCalendar (Date picker component)
