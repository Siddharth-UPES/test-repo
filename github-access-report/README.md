# GitHub Access Report Service

## Overview

This project is a Spring Boot application that connects to the GitHub API and generates an access report showing which users have access to which repositories within a given organization or user account.

The service authenticates securely using a GitHub Personal Access Token (PAT), retrieves repositories, fetches collaborators for each repository, aggregates the data, and exposes a REST API endpoint that returns the structured report in JSON format. It is designed to scale efficiently for organizations with many repositories and users.

---

## Features

* Secure authentication using GitHub Personal Access Token (PAT)
* Fetch repositories using pagination
* Retrieve collaborators for each repository
* Aggregate mapping: User → List of Repositories
* REST API endpoint to return access report in JSON format
* CSV export functionality for reporting
* Logging using SLF4J
* Parallel processing for performance and scalability
* Error handling for robust execution

---

## Technology Stack

* Java 23
* Spring Boot 3
* REST API
* GitHub REST API
* Maven
* IntelliJ IDEA

---

## Project Structure

```
src
 └── main
     └── java
         └── github_access_report
             ├── controller
             │     └── GitHubController.java
             ├── service
             │     └── GitHubService.java
             ├── model
             │     ├── Repository.java
             │     └── User.java
             └── GithubAccessReportApplication.java
```

---

## How to Run the Project

### Step 1 — Clone the Repository

```
git clone https://github.com/Siddharth-UPES/test-repo/github-access-report.git
```

### Step 2 — Navigate to Project Folder

```
cd github-access-report
```

### Step 3 — Configure Authentication

Open:

```
src/main/resources/application.properties
```

Add the following configuration:

```
github.token=YOUR_GITHUB_PERSONAL_ACCESS_TOKEN
github.org=YOUR_GITHUB_USERNAME_OR_ORGANIZATION
server.port=8081
```

---

## How to Generate GitHub Personal Access Token

1. Go to GitHub Settings
2. Click Developer Settings
3. Select Personal Access Tokens
4. Generate a new token
5. Select permissions:

```
repo
read:org
```

6. Copy the token and paste it into `application.properties`

---

## API Endpoints

###  Generate Access Report (JSON)

Endpoint:

```
GET /report
```

Example:

```
http://localhost:8081/report
```

Sample Response:

```
{
  "user1": [
    "repo1",
    "repo2"
  ],
  "user2": [
    "repo3"
  ]
}
```

---

## Design Decisions

### Pagination

The GitHub API returns a limited number of repositories per request. Pagination is implemented using:

```
?page=
&per_page=100
```

This ensures support for organizations with 100+ repositories.

---

### Parallel Processing

Parallel processing is implemented using:

```
parallelStream()
```

This allows multiple repositories to be processed simultaneously, improving performance for large organizations.

Thread safety is ensured using:

```
synchronized (report)
```

---

### Logging

Logging is implemented using SLF4J to track runtime execution and debugging.

Examples:

```
Generating GitHub access report
Processing repository
Collaborator found
```

---

## Error Handling

The application includes structured exception handling to ensure reliability.

Examples handled:

* API request failures
* Invalid token
* Network errors
* Empty responses

---

## Scalability Considerations

The system is designed to support:

* 100+ repositories
* 1000+ users
* Efficient API usage

Techniques used:

* Pagination
* Parallel processing
* Efficient data aggregation
* Logging for monitoring

---

## Assumptions

* The GitHub token has required permissions
* The organization or user repositories are accessible
* Network connectivity to GitHub API is available
* Rate limits are within acceptable range

---

## Future Improvements

* GitHub rate limit handling
* Role-based access details
* Caching responses
* Docker deployment
* Unit testing
* API documentation (Swagger)

---

## Author

Siddharth Kumar
MCA Student — UPES
Java Full Stack Development Enthusiast

---

## Status

Production-ready backend service with scalable repository access reporting functionality.
