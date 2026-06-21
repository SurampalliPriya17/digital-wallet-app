# Digital Wallet Application



The Modular E-Wallet & Payment Gateway System is a Spring Boot-based application that enables secure digital payments, including wallet creation, fund transfers, and payment processing. It uses Kafka for event-driven communication and MySQL for data storage, ensuring scalability and reliability.



The system follows a modular microservices-style architecture where each service handles a specific domain. It improves scalability and maintainability through service separation, while leveraging REST APIs and Kafka for communication, with controlled inter-service dependencies.



---



\## Architecture \& Services



\### User Service

\*\*Responsibilities:\*\*

\* Register users

\* Manage user profiles

\* Retrieve user information

\* Delete user accounts

\* Publish user creation events to Kafka



\### Wallet Service

\*\*Responsibilities:\*\*

\* Create wallets automatically

\* Maintain wallet balances

\* Add money to wallets

\* Validate balances before transfers

\* Update wallet information



\### Transaction Service

\*\*Responsibilities:\*\*

\* Process money transfers

\* Generate transaction records

\* Track transaction status

\* Maintain transaction history

\* Coordinate fund transfers between wallets



\### Payment Gateway Service

\*\*Responsibilities:\*\*

\* Process wallet recharge requests

\* Manage merchant interactions

\* Generate payment transactions

\* Track payment status

\* Support wallet funding operations



\---



\## Application Screenshots \& Flow



!\[Welcome Screen](images/welcome\_screen.png)



\### 1. User Registration \& Wallet Creation

When a user registers, the User Service saves the user data and publishes an event to Kafka. The Wallet Service consumes the event and automatically creates a wallet for the user, ensuring seamless wallet creation without manual steps.



!\[Create Wallet Account](images/create\_wallet.png)



!\[User Dashboard](images/dashboard\_initial.png)



!\[User Profile](images/user\_profile.png)



\### 2. User Details Updation

The User Service allows users to update their profile details such as name, email, and phone number. It validates the request, updates the information in the database, and returns the updated profile to ensure accurate user records.



!\[Update Wallet Account](images/update\_account.png)



\### 3. Add Money to Wallet

The Add Money feature allows users to fund their wallets through the Payment Gateway Service. After a successful payment, the Wallet Service updates the wallet balance and records the transaction. If the payment fails, the transaction is marked as failed and the wallet balance remains unchanged.



!\[Add Money Form](images/add\_money\_form.png)



!\[Payment Gateway Page](images/payment\_gateway.png)



!\[Wallet Updated Successfully](images/wallet\_updated\_success.png)



!\[Updated Dashboard Balance](images/dashboard\_after\_load.png)



\### 4. Wallet-to-Wallet Money Transfer

The Wallet-to-Wallet Transfer feature enables users to send money securely to other users. The system verifies the sender’s balance, transfers the amount between wallets, and records the transaction for future reference.



!\[Send Money Form](images/send\_money\_form.png)



!\[Transaction Successful](images/transaction\_success.png)



!\[Final Dashboard Balance](images/dashboard\_final.png)



\---



\## Technologies Used



\* \*\*Language:\*\* Java

\* \*\*Framework:\*\* Spring Boot

\* \*\*Message Broker:\*\* Apache Kafka

\* \*\*Database:\*\* MySQL

\* \*\*Build Tool:\*\* Maven

\* \*\*Utilities:\*\* Lombok

\* \*\*Communication:\*\* REST APIs

\* \*\*Frontend/Templating:\*\* Thymeleaf, HTML, CSS

\* \*\*Version Control:\*\* Git, GitHub



