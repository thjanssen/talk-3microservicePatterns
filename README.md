# 3 Patterns for Scalable Microservices

When you’re implementing scalable and independent microservices, exchanging data between them quickly becomes a challenge. You need to ensure consistency but distributed transactions create tight coupling and are no longer an option. The same is true for synchronous service calls.

You need new approaches that allow you to exchange data asynchronously and ensure its consistency.

Most architectures achieve that using 3 patterns: The Outbox Pattern, the View Database Pattern, and the SAGA Pattern.
With the help of the outbox pattern, events are stored in Kafka and data in the service’s own database. Based on this, other services can either store the data in their own view database or participate in a complex SAGA to secure a distributed write operation.

In this talk, I will show exactly how all this works and what to look out for.

You can find the slides and further materials here: https://thorben-janssen.com/3-patterns-microservices/
