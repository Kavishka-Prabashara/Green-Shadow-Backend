package lk.kavishka.greenshadowbackend.repository;

import lk.kavishka.greenshadowbackend.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffRepository extends JpaRepository<Staff, String> {
}
