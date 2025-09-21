package lk.kavishka.greenshadowbackend.repository;

import lk.kavishka.greenshadowbackend.entity.Crop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CropRepository extends JpaRepository<Crop, String> {
}
