package lk.kavishka.greenshadowbackend.controller;

import lk.kavishka.greenshadowbackend.dto.FieldDto;
import lk.kavishka.greenshadowbackend.entity.FieldEntity;
import lk.kavishka.greenshadowbackend.exception.ResourceNotFoundException;
import lk.kavishka.greenshadowbackend.repository.FieldRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/fields")
@RequiredArgsConstructor
public class FieldController {

    private final FieldRepository fieldRepository;
    private final ModelMapper mapper = new ModelMapper();

    @GetMapping
    public List<FieldDto> getAll() {
        return fieldRepository.findAll().stream().map(f -> mapper.map(f, FieldDto.class)).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public FieldDto getOne(@PathVariable String id) {
        FieldEntity f = fieldRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Field not found"));
        return mapper.map(f, FieldDto.class);
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATIVE')")
    @PostMapping
    public ResponseEntity<FieldDto> create(@RequestBody FieldDto dto) {
        FieldEntity e = mapper.map(dto, FieldEntity.class);
        FieldEntity saved = fieldRepository.save(e);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.map(saved, FieldDto.class));
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATIVE')")
    @PutMapping("/{id}")
    public FieldDto update(@PathVariable String id, @RequestBody FieldDto dto) {
        FieldEntity existing = fieldRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Field not found"));
        existing.setFieldCode(dto.getFieldCode());
        existing.setName(dto.getName());
        existing.setLocation(dto.getLocation());
        existing.setExtentSqm(dto.getExtentSqm());
        existing.setImage1(dto.getImage1());
        existing.setImage2(dto.getImage2());
        FieldEntity saved = fieldRepository.save(existing);
        return mapper.map(saved, FieldDto.class);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        fieldRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
