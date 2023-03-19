
# Transaction Management Service
The Transaction Management Service (TMS) is a microservice of the SpendWise project.

TMS provides the functionality to enable users to create, delete, update, and see Transaction Entries (will be called "Entries") to track their budget, consisting of income and expense.


### Creating PostgreSQL instance with Docker
1. Pull Postgres image to local machine
   `runas /user:administrator docker pull postgres`
2. Run container with Postgres image, port mapping and add db user credentials:
   `runas /user:administrator docker run --name transaction_management_db -p 5432:5432 -e POSTGRES_USER=postgres_uname -e POSTGRES_PASSWORD=your_password -d postgres`

(`runas /user:administrator` is similar to `sudo` in Unix)

Running PostgreSQL instance with Docker:  
` runas /user:administrator docker start transaction_management_db`

Stopping PostgreSQL instance with Docker:  
` runas /user:administrator docker stop transaction_management_db`

### Todo
- [ ] Hide db keys from repo using Jasypt: https://www.north-47.com/spring-boot-password-encryption-with-jasypt/