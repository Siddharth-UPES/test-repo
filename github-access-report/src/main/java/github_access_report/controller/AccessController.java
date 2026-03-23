package github_access_report.controller;

import github_access_report.service.GitHubService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class AccessController {

    private final GitHubService gitHubService;

    public AccessController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GetMapping("/access-report")
    public Map<String, List<String>> getAccessReport() {
        return gitHubService.generateAccessReport();
    }
}