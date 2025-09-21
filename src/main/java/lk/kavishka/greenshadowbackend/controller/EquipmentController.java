package lk.kavishka.greenshadowbackend.controller;

import lk.kavishka.greenshadowbackend.dto.EquipmentDto;
import lk.kavishka.greenshadowbackend.entity.Equipment;
import lk.kavishka.greenshadowbackend.entity.FieldEntity;
import lk.kavishka.greenshadowbackend.entity.Staff;
import lk.kavishka.greenshadowbackend.exception.ResourceNotFoundException;
import lk.kavishka.greenshadowbackend.repository.EquipmentRepository;
import lk.kavishka.greenshadowbackend.repository.FieldRepository;
import lk.kavishka.greenshadowbackend.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/equipment")
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipmentRepository equipmentRepository;
    private final StaffRepository staffRepository;
    private final FieldRepository fieldRepository;
    private final ModelMapper mapper = new ModelMapper();

    @GetMapping
    public List<EquipmentDto> getAll() {
        return equipmentRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public EquipmentDto getOne(@PathVariable String id) {
        Equipment e = equipmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Equipment not found"));
        return toDto(e);
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATIVE')")
    @PostMapping
    public ResponseEntity<EquipmentDto> create(@RequestBody EquipmentDto dto) {
        Equipment e = mapper.map(dto, Equipment.class);
        if (dto.getAssignedStaffId() != null) {
            Staff s = staffRepository.findById(dto.getAssignedStaffId()).orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
            e.setAssignedStaff(s);
        }
        if (dto.getAssignedFieldId() != null) {
            FieldEntity f = fieldRepository.findById(dto.getAssignedFieldId()).orElseThrow(() -> new ResourceNotFoundException("Field not found"));
            e.setAssignedField(f);
        }
        Equipment saved = equipmentRepository.save(e);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(saved));
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATIVE')")
    @PutMapping("/{id}")
    public EquipmentDto update(@PathVariable String id, @RequestBody EquipmentDto dto) {
        Equipment existing = equipmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Equipment not found"));
        existing.setEquipmentCode(dto.getEquipmentCode());
        existing.setName(dto.getName());
        existing.setType(dto.getType());
        existing.setStatus(dto.getStatus());
        if (dto.getAssignedStaffId() != null) {
            Staff s = staffRepository.findById(dto.getAssignedStaffId()).orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
            existing.setAssignedStaff(s);
        } else {
            existing.setAssignedStaff(null);
        }
        if (dto.getAssignedFieldId() != null) {
            FieldEntity f = fieldRepository.findById(dto.getAssignedFieldId()).orElseThrow(() -> new ResourceNotFoundException("Field not found"));
            existing.setAssignedField(f);
        } else {
            existing.setAssignedField(null);
        }
        Equipment saved = equipmentRepository.save(existing);
        return toDto(saved);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        equipmentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private EquipmentDto toDto(Equipment e) {
        EquipmentDto dto = mapper.map(e, EquipmentDto.class);
        if (e.getAssignedStaff() != null) dto.setAssignedStaffId(e.getAssignedStaff().getId());
        if (e.getAssignedField() != null) dto.setAssignedFieldId(e.getAssignedField().getId());
        return dto;
    }
}
