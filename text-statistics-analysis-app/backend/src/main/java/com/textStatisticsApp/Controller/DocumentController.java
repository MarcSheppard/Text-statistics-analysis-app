package com.textStatisticsApp.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.textStatisticsApp.Dao.DocumentDao;
import com.textStatisticsApp.Service.DocumentService;
import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class DocumentController {
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/getDocuments")
    public List<DocumentResponse> getDocuments() {
        List<DocumentResponse> responses = new ArrayList<>();
        for(DocumentDao.Document document : documentService.getDocuments()) {
            responses.add(new DocumentResponse(Long.toString(document.id()), document.name()));
        }
        return responses;
    }

    @GetMapping("/getDocumentById")
    public DocumentDao.Document getDocumentById(@RequestParam long documentId) {
        return documentService.getDocumentById(documentId);
    }

    @PostMapping("/uploadDocument")
    public ResponseEntity<String> uploadDocument(@RequestParam("file") MultipartFile file) throws IOException {
        String text = new String(file.getBytes());
        documentService.addDocument(file.getOriginalFilename(), text);
        return ResponseEntity.ok("File uploaded.");
    }

    @DeleteMapping("/deleteDocumentById")
    public int deleteDocumentById(@RequestParam long documentId) {
        documentService.deleteDocumentById(documentId);
        return Response.SC_OK;
    }

    @DeleteMapping("/deleteAllDocuments")
    public int deleteAllDocuments() {
        documentService.deleteAllDocuments();
        return Response.SC_OK;
    }

    private record DocumentResponse(String id, String name) {}
}
