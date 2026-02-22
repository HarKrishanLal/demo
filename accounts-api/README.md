# accounts-api

Sample Spring Boot application implementing Accounts CRUD and an inspect endpoint.

Endpoints:
- `POST /accounts/v1` - create account
- `GET /accounts/v1/{id}` - get account
- `PUT /accounts/v1/{id}` - update account
- `DELETE /accounts/v1/{id}` - delete account
- `GET /accounts/v1` - list accounts
- `POST /accounts/v1/inspect-accounts` - inspect account against SDNList

SDN list file is expected at `../prompts/SDNList.txt` relative to the project root.

To build:

```bash
mvn -f accounts-api/pom.xml clean package
```

To run:

```bash
java -jar accounts-api/target/accounts-api-0.1.0.jar
```
