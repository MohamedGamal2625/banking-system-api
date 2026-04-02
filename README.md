# bank-management-system
built using Spring Boot to simulate real-world banking operations such as managing customers, accounts, and financial transactions.
The system follows clean architecture principles and demonstrates best backend practices including layered design, DTO usage, transaction handling, and exception management.
 ## Tech Stack
- Java 17
- Spring Boot
- Spring Data JPA
- MySQL
- Hibernate 
- Lombok 
- Exception handling 
- Maven 
- Jakarta Validation
 ## Architecture & Design
 The project follows a <b> layered architecture </b> that separates concerns and ensures maintainable, clean code.<br>
 ### Flow Diagram:
```mermaid
flowchart TD
    A[Client] --> B[Controller]
    B --> C[DTO Layer]
    C --> D[Service Layer]
    D --> E[Repository Layer]
    E --> F[Database]
### Layers Explained:
  - Controller Layer – Handles HTTP requests and responses.<br>
  - DTO Layer – Transfers data between client and server. Maps request DTOs to entities and entities to response DTOs.<br>
  - Service Layer – Contains business logic such as transactions, deposits, withdrawals, and transfers. Ensures business rules are applied.<br>
  - Repository Layer – Interacts with the database using Spring Data JPA.<br>
  - Entity Layer – Represents database tables and relationships.<br>
   ## Features
### Customer Management
  - Create customer
  - Retrieve customer by ID<br>
### Account Management
  - Create account
  - Retrieve account by ID
  - Get all accounts
  - Update account status (ACTIVE / SUSPENDED)<br>
### Transaction Management
  - Deposit money
  - Withdraw money
  - Transfer money between accounts<br>
### Exceptions Handled
  - Prevent insufficient balance withdrawals
  - Prevent transfers to the same account
  - Validate transaction amounts<br>
    ## API Endpoints
### Customers
  - POST /api/customers – Create customer
  - GET /api/customers/{id} – Get customer by ID<br>
### Accounts
  - POST /api/accounts – Create account
  - GET /api/accounts/{id} – Get account by ID
  - GET /api/accounts – Get all accounts
  - PATCH /api/accounts/{id}/status – Update account status<br>
### Transactions
  - POST /api/transactions/deposit – Deposit money
  - POST /api/transactions/withdraw – Withdraw money
  - POST /api/transactions/transfer – Transfer money<br>
### Key Concepts
  - Layered Architecture (Controller → DTO → Service → Repository → Database)
  - DTO Pattern (Request & Response separation)
  - RESTful API Design
  - JPA Relationships (OneToMany / ManyToOne)
  - Transaction Handling (@Transactional)
  - Enum Usage for business states
  - Exception Handling
  - Clean Code Principles<br>
    ## Database Design
### Tables
  - customers
  - accounts
  - transactions<br>
    ## Relationships:
  - One Customer → Many Accounts
  - One Account → Many Transactions (as sender & receiver)<br>
### Transaction Table Columns:
  - source_account_id
  - target_account_id
  - amount
  - transaction_type
  - time_stamp
