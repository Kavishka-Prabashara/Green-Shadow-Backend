package lk.kavishka.greenshadowbackend.controller;

import lk.kavishka.greenshadowbackend.dto.CropDto;
import lk.kavishka.greenshadowbackend.entity.Crop;
import lk.kavishka.greenshadowbackend.entity.FieldEntity;
import lk.kavishka.greenshadowbackend.exception.ResourceNotFoundException;
import lk.kavishka.greenshadowbackend.repository.CropRepository;
import lk.kavishka.greenshadowbackend.repository.FieldRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/crops")
@RequiredArgsConstructor
public class CropController {

    private final CropRepository cropRepository;
    private final FieldRepository fieldRepository;
    private final ModelMapper mapper = new ModelMapper();

    @GetMapping
    public List<CropDto> getAll() {
        return cropRepository.findAll().stream()
                .map(c -> {
                    CropDto dto = mapper.map(c, CropDto.class);
                    if (c.getField() != null) dto.setFieldId(c.getField().getId());
                    return dto;
                }).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CropDto getOne(@PathVariable String id) {
        Crop c = cropRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Crop not found"));
        CropDto dto = mapper.map(c, CropDto.class);
        if (c.getField() != null) dto.setFieldId(c.getField().getId());
        return dto;
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATIVE')")
    @PostMapping
    public ResponseEntity<CropDto> create(@RequestBody CropDto dto) {
        Crop c = mapper.map(dto, Crop.class);
        if (dto.getFieldId() != null) {
            FieldEntity f = fieldRepository.findById(dto.getFieldId())
                    .orElseThrow(() -> new ResourceNotFoundException("Field not found"));
            c.setField(f);
        }
        Crop saved = cropRepository.save(c);
        CropDto res = mapper.map(saved, CropDto.class);
        if (saved.getField() != null) res.setFieldId(saved.getField().getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATIVE')")
    @PutMapping("/{id}")
    public CropDto update(@PathVariable String id, @RequestBody CropDto dto) {
        Crop existing = cropRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Crop not found"));
        existing.setCropCode(dto.getCropCode());
        existing.setCommonName(dto.getCommonName());
        existing.setScientificName(dto.getScientificName());
        existing.setCategory(dto.getCategory());
        existing.setSeason(dto.getSeason());
        existing.setImage(dto.getImage());
        if (dto.getFieldId() != null) {
            FieldEntity f = fieldRepository.findById(dto.getFieldId())
                    .orElseThrow(() -> new ResourceNotFoundException("Field not found"));
            existing.setField(f);
        } else {
            existing.setField(null);
        }
        Crop saved = cropRepository.save(existing);
        CropDto res = mapper.map(saved, CropDto.class);
        if (saved.getField() != null) res.setFieldId(saved.getField().getId());
        return res;
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        cropRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
