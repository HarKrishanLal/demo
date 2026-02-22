# Copilot Instructions for Java Spring Boot Applications

## General Guidelines

1. Use clear, descriptive class and method names that follow Java naming conventions.
2. Annotate classes with appropriate Spring Boot annotations (e.g., `@RestController`, `@Service`, `@Repository`).
3. Structure your project using standard Spring Boot folder hierarchy:
   - `src/main/java` for code
   - `src/main/resources` for configuration
4. Write REST endpoints using `@RequestMapping`, `@GetMapping`, `@PostMapping`, etc.
5. Inject dependencies using `@Autowired` or constructor injection.
6. Handle exceptions with `@ControllerAdvice` and `@ExceptionHandler`.
7. Use `application.properties` or `application.yml` for configuration.
8. Write unit tests with JUnit and integration tests with Spring Boot Test.
9. Document APIs with Swagger/OpenAPI annotations.
10. Keep code modular and reusable; avoid hardcoding values.

## OWASP Coding Standards

11. Validate all user input and sanitize data to prevent injection attacks.
12. Use parameterized queries for database access to avoid SQL injection.
13. Implement proper authentication and authorization checks.
14. Avoid exposing sensitive information in logs or error messages.
15. Use HTTPS for secure communication.
16. Apply least privilege principle for user roles and permissions.
17. Keep dependencies up to date and monitor for vulnerabilities.
18. Handle sensitive data securely (e.g., passwords, tokens) and use encryption.
19. Protect against CSRF and XSS attacks.
20. Review code for security issues and follow secure coding guidelines.

### Accounts API Development

The development of the accounts-api must strictly follow all instructions, guidelines, and standards defined in this file. This includes:

- Requirements and prompts from [prompts/account-api-promts.md](prompts/account-api-promts.md)
- General Guidelines
- OWASP Coding Standards

Ensure every aspect of the accounts-api implementation adheres to these instructions for consistency, security, and best practices.
