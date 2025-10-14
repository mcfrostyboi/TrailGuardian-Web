package main.java.com.trailguardian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@SpringBootApplication
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class BackendApplication {
    
    // Simple in-memory storage (for demo - acts like a database)
    private List<Map<String, Object>> reports = new ArrayList<>();
    private long nextId = 1;
    
    // Add some sample data when application starts
    public BackendApplication() {
        // Sample reports for demo
        addSampleReport("Fallen Tree", "Large tree blocking main trail", "DANGER", 40.7128, -74.0060, "hiker123");
        addSampleReport("Clean Water Source", "Fresh spring water available", "WATER", 40.7135, -74.0055, "trail_angel");
        addSampleReport("Bear Sighting", "Black bear spotted near campsite", "ANIMAL", 40.7120, -74.0070, "wildlife_watcher");
    }
    
    private void addSampleReport(String title, String description, String type, double lat, double lng, String user) {
        Map<String, Object> report = new HashMap<>();
        report.put("id", nextId++);
        report.put("title", title);
        report.put("description", description);
        report.put("reportType", type);
        report.put("latitude", lat);
        report.put("longitude", lng);
        report.put("submittedBy", user);
        report.put("createdAt", new Date());
        reports.add(report);
    }
    
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
        System.out.println("🚀 TrailGuardian Backend running on http://localhost:8080");
        System.out.println("📊 Sample data loaded: " + 3 + " reports");
    }
    
    // GET all reports
    @GetMapping("/reports")
    public List<Map<String, Object>> getAllReports() {
        System.out.println("📨 GET /api/reports - Returning " + reports.size() + " reports");
        return reports;
    }
    
    // POST new report
    @PostMapping("/reports")
    public Map<String, Object> createReport(@RequestBody Map<String, Object> report) {
        System.out.println("📨 POST /api/reports - Creating new report: " + report.get("title"));
        
        report.put("id", nextId++);
        report.put("createdAt", new Date());
        reports.add(0, report); // Add to beginning for newest first
        
        System.out.println("✅ Report created with ID: " + report.get("id"));
        return report;
    }
    
    // GET report by ID
    @GetMapping("/reports/{id}")
    public Map<String, Object> getReport(@PathVariable Long id) {
        System.out.println("📨 GET /api/reports/" + id);
        
        return reports.stream()
                .filter(r -> id.equals(r.get("id")))
                .findFirst()
                .orElse(Collections.singletonMap("error", "Report not found"));
    }
    
    // DELETE report
    @DeleteMapping("/reports/{id}")
    public Map<String, String> deleteReport(@PathVariable Long id) {
        System.out.println("📨 DELETE /api/reports/" + id);
        
        boolean removed = reports.removeIf(r -> id.equals(r.get("id")));
        if (removed) {
            return Collections.singletonMap("message", "Report deleted successfully");
        } else {
            return Collections.singletonMap("error", "Report not found");
        }
    }
    
    // Health check endpoint
    @GetMapping("/health")
    public Map<String, String> healthCheck() {
        return Collections.singletonMap("status", "OK - TrailGuardian Backend is running!");
    }
}