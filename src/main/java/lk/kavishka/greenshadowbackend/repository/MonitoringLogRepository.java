package lk.kavishka.greenshadowbackend.repository;

import lk.kavishka.greenshadowbackend.entity.MonitoringLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonitoringLogRepository extends JpaRepository<MonitoringLog, String> {
}
