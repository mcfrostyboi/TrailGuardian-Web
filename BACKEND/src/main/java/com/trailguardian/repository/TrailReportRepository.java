package main.java.com.trailguardian.repository;

import com.trailguardian.entity.TrailReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TrailReportRepository extends JpaRepository<TrailReport, Long> {
    List<TrailReport> findAllByOrderByCreatedAtDesc();
}