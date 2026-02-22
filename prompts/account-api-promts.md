## Accounts API CRUD Operations

To create the accounts-api project structure, follow the instructions in [springboot-project-structure.md](springboot-project-structure.md).

All endpoints must use the URI prefix: `accounts/v1`.

Implement the following RESTful endpoints:

- **Create Account**: `POST /accounts/v1` — Create a new account.
- **Read Account**: `GET /accounts/v1/{id}` — Retrieve account details by ID.
- **Update Account**: `PUT /accounts/v1/{id}` — Update account information.
- **Delete Account**: `DELETE /accounts/v1/{id}` — Remove an account by ID.
- **List Accounts**: `GET /accounts/v1` — List all accounts.


---

### Additional Endpoint: Inspect Accounts

- **Inspect Account**: `POST /accounts/v1/inspect-accounts` — Inspect an account for SDNList status.

**Logic:**
1. Check if the provided account is in SDNList.txt.
2. If the account is in SDNList.txt:
	 - Check when it was added to SDNList.txt.
	 - If added more than one week ago:
		 - Check when the last transaction was done on this account.
		 - If the last transaction was after one week from SDNList addition, allow including the account number in the search response.
		 - Else, show only a masked value of the account number (using * characters) in the API response.
3. If the account is not in SDNList, include the account number in the response as normal.

The API must return appropriate HTTP status codes and error messages, and mask account numbers as required.
