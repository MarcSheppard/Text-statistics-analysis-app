package com.textStatisticsApp;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class StatisticsController
{
    private SearchService searchService = new SearchService();

    @GetMapping("/test")
    public String getTestString() {
        return "SUCCESS, communicated with backend.";
    }

    @GetMapping("/getResults")
    public SearchService.AnalysisResults getResults(@RequestParam String query) {
        return searchService.getResults(query);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String text = new String(file.getBytes());
        searchService.addDocument(file.getName(), text);
        return ResponseEntity.ok("File uploaded.");
    }

    @GetMapping("/clear")
    public ResponseEntity<String> clearTextData() {
        //searchService.clearTextData();
        return ResponseEntity.ok("Text data cleared");
    }
}
