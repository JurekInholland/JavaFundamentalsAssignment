# Java Fundamentals Assignment

Assignment [pdf](https://github.com/JurekInholland/JavaFundamentalsAssignment/blob/master/Assignment.pdf)

This project was built using JDK version 17.0.5, Maven 3.8.1 and JavaFX 17.0.2. The source code is available on [GitHub](https://github.com/JurekInholland/JavaFundamentalsAssignment).
To run the cloned project, run the maven command ``javafx:run`` or use the supplied run config.

The structure of this project consists of application users, library members and items.
To be able to manage members and items, user credentials have to be provided.
The following users are available:
- username: ``admin`` password: ``root``
- username: ``librarian`` password: ``gutenberg``

Application data is serialized on application exit and is written to ``db.dat``.
On application launch, the data is deserialized and loaded.
If no data file is present, or it is invalid/corrupted, sample values are generated.
