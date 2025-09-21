package lk.kavishka.greenshadowbackend.repository;

import lk.kavishka.greenshadowbackend.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends JpaRepository<Equipment, String> {
}
