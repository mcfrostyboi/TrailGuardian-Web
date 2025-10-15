package main.java.com.trailguardian;

import com.trailguardian.entity.TrailReport;
import com.trailguardian.repository.TrailReportRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class BackendApplication {

    private final TrailReportRepository reportRepository;

    public BackendApplication(TrailReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
        System.out.println("🚀 TrailGuardian Backend with DATABASE running on http://localhost:8080");
    }

    // Initialize sample data
    @Bean
    public CommandLineRunner initData(TrailReportRepository reportRepository) {
        return args -> {
            // Clear existing data and add samples
            reportRepository.deleteAll();
            
            reportRepository.save(new TrailReport(
                "Fallen Tree", 
                "Large tree blocking main trail", 
                TrailReport.ReportType.DANGER, 
                40.7128, -74.0060, 
                "hiker123"
            ));
            
            reportRepository.save(new TrailReport(
                "Clean Water Source", 
                "Fresh spring water available", 
                TrailReport.ReportType.WATER, 
                40.7135, -74.0055, 
                "trail_angel"
            ));
            
            reportRepository.save(new TrailReport(
                "Bear Sighting", 
                "Black bear spotted near campsite", 
                TrailReport.ReportType.ANIMAL, 
                40.7120, -74.0070, 
                "wildlife_watcher"
            ));
            
            System.out.println("📊 Sample database data loaded: " + reportRepository.count() + " reports");
        };
    }

    // GET all reports
    @GetMapping("/reports")
    public List<TrailReport> getAllReports() {
        System.out.println("📨 GET /api/reports - Returning " + reportRepository.count() + " reports from DATABASE");
        return reportRepository.findAllByOrderByCreatedAtDesc();
    }

    // POST new report
    @PostMapping("/reports")
    public TrailReport createReport(@RequestBody TrailReport report) {
        System.out.println("📨 POST /api/reports - Creating new report in DATABASE: " + report.getTitle());
        TrailReport savedReport = reportRepository.save(report);
        System.out.println("✅ Report created with ID: " + savedReport.getId());
        return savedReport;
    }

    // GET report by ID
    @GetMapping("/reports/{id}")
    public TrailReport getReport(@PathVariable Long id) {
        System.out.println("📨 GET /api/reports/" + id + " from DATABASE");
        return reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found with id: " + id));
    }

    // DELETE report
    @DeleteMapping("/reports/{id}")
    public String deleteReport(@PathVariable Long id) {
        System.out.println("📨 DELETE /api/reports/" + id + " from DATABASE");
        if (reportRepository.existsById(id)) {
            reportRepository.deleteById(id);
            return "Report deleted successfully";
        } else {
            throw new RuntimeException("Report not found with id: " + id);
        }
    }

    // Health check endpoint
    @GetMapping("/health")
    public String healthCheck() {
        return "OK - TrailGuardian Backend with DATABASE is running! Total reports: " + reportRepository.count();
    }
}