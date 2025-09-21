package lk.kavishka.greenshadowbackend.repository;

import lk.kavishka.greenshadowbackend.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, String> {
}
