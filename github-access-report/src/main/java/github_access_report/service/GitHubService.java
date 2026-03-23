package github_access_report.service;

import github_access_report.model.Repository;
import github_access_report.model.User;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Service
public class GitHubService {

    @Value("${github.token}")
    private String token;

    @Value("${github.org}")
    private String organization;

    private static final Logger logger = LoggerFactory.getLogger(GitHubService.class);

    private final RestTemplate restTemplate = new RestTemplate();


    public Map<String, List<String>> generateAccessReport() {

        Map<String, List<String>> report = new HashMap<>();

        try {logger.info("Generating GitHub access report...");

            List<String> repoNames = getAllRepositories(organization);

            logger.info("Total repositories fetched: {}", repoNames.size()
            );

            repoNames.parallelStream().forEach(repoName -> {

                logger.info("Processing repository: {}", repoName);

                List<String> collaborators = getCollaborators(repoName);

                synchronized (report) {

                    for (String user : collaborators) {
                            report.computeIfAbsent(user, k -> new ArrayList<>()).add(repoName);                     }
                        }
                    });
            }
            catch (Exception e) {logger.error("Error occurred while generating access report", e);
        }
        return report;
    }
    public List<String> getCollaborators(String repoName) {
        List<String> collaborators = new ArrayList<>();

        try { String collaboratorUrl = "https://api.github.com/repos/" + organization + "/" + repoName + "/collaborators";

            HttpHeaders headers = new HttpHeaders();

            headers.setBearerAuth(token);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<User[]> response =
                    restTemplate.exchange(
                            collaboratorUrl,
                            HttpMethod.GET,
                            entity,
                            User[].class
                    );

            User[] userArray = response.getBody();

            if (userArray == null) {
                return collaborators;
            }

            for (User user : userArray) {

                logger.info(
                        "Collaborator found: {} for repo {}",
                        user.getLogin(),
                        repoName
                );

                collaborators.add(user.getLogin());

            }

        } catch (Exception e) {
            logger.error("Error fetching collaborators for repo: {}", repoName, e);
        }

        return collaborators;
    }

    public List<String> getAllRepositories(String username) {

        List<String> repoNames = new ArrayList<>();

        int page = 1;
        int perPage = 100;

        while (true) {

            logger.info("Fetching repositories page: {}", page);

            String url = "https://api.github.com/users/" + username + "/repos?page=" + page + "&per_page=" + perPage;

            HttpHeaders headers = new HttpHeaders();

            headers.setBearerAuth(token);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Repository[]> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            entity,
                            Repository[].class
                    );

            Repository[] repos = response.getBody();

            if (repos == null || repos.length == 0) {
                logger.info("No more repositories found. Stopping pagination.");
                break;
            }
            for (Repository repo : repos) {
                repoNames.add(repo.getName());

            }
            page++;
        }
        return repoNames;
    }
}