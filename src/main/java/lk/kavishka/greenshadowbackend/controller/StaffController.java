package lk.kavishka.greenshadowbackend.controller;

import lk.kavishka.greenshadowbackend.dto.StaffDto;
import lk.kavishka.greenshadowbackend.entity.FieldEntity;
import lk.kavishka.greenshadowbackend.entity.Staff;
import lk.kavishka.greenshadowbackend.entity.Vehicle;
import lk.kavishka.greenshadowbackend.exception.ResourceNotFoundException;
import lk.kavishka.greenshadowbackend.repository.FieldRepository;
import lk.kavishka.greenshadowbackend.repository.StaffRepository;
import lk.kavishka.greenshadowbackend.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/staff")
@RequiredArgsConstructor
public class StaffController {

    private final StaffRepository staffRepository;
    private final FieldRepository fieldRepository;
    private final VehicleRepository vehicleRepository;
    private final ModelMapper mapper = new ModelMapper();

    @GetMapping
    public List<StaffDto> getAll() {
        return staffRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public StaffDto getOne(@PathVariable String id) {
        Staff s = staffRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
        return toDto(s);
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATIVE')")
    @PostMapping
    public ResponseEntity<StaffDto> create(@RequestBody StaffDto dto) {
        Staff s = mapper.map(dto, Staff.class);

        // fields
        Set<FieldEntity> fields = new HashSet<>();
        if (dto.getFieldIds() != null) {
            for (String fid : dto.getFieldIds()) {
                FieldEntity f = fieldRepository.findById(fid).orElseThrow(() -> new ResourceNotFoundException("Field not found: " + fid));
                fields.add(f);
            }
        }
        s.setFields(fields);

        // vehicles
        Set<Vehicle> vehicles = new HashSet<>();
        if (dto.getVehicleIds() != null) {
            for (String vid : dto.getVehicleIds()) {
                Vehicle v = vehicleRepository.findById(vid).orElseThrow(() -> new ResourceNotFoundException("Vehicle not found: " + vid));
                vehicles.add(v);
            }
        }
        s.setVehicles(vehicles);

        Staff saved = staffRepository.save(s);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(saved));
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATIVE')")
    @PutMapping("/{id}")
    public StaffDto update(@PathVariable String id, @RequestBody StaffDto dto) {
        Staff existing = staffRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
        existing.setStaffCode(dto.getStaffCode());
        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setDesignation(dto.getDesignation());
        existing.setGender(dto.getGender());
        existing.setJoinedDate(dto.getJoinedDate());
        existing.setDob(dto.getDob());
        existing.setAddressLine1(dto.getAddressLine1());
        existing.setAddressLine2(dto.getAddressLine2());
        existing.setAddressLine3(dto.getAddressLine3());
        existing.setAddressLine4(dto.getAddressLine4());
        existing.setAddressLine5(dto.getAddressLine5());
        existing.setContactNo(dto.getContactNo());
        existing.setEmail(dto.getEmail());
        existing.setRole(dto.getRole());

        // fields
        Set<FieldEntity> fields = new HashSet<>();
        if (dto.getFieldIds() != null) {
            for (String fid : dto.getFieldIds()) {
                FieldEntity f = fieldRepository.findById(fid).orElseThrow(() -> new ResourceNotFoundException("Field not found: " + fid));
                fields.add(f);
            }
        }
        existing.setFields(fields);

        // vehicles
        Set<Vehicle> vehicles = new HashSet<>();
        if (dto.getVehicleIds() != null) {
            for (String vid : dto.getVehicleIds()) {
                Vehicle v = vehicleRepository.findById(vid).orElseThrow(() -> new ResourceNotFoundException("Vehicle not found: " + vid));
                vehicles.add(v);
            }
        }
        existing.setVehicles(vehicles);

        Staff saved = staffRepository.save(existing);
        return toDto(saved);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        staffRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private StaffDto toDto(Staff s) {
        StaffDto dto = mapper.map(s, StaffDto.class);
        if (s.getFields() != null && !s.getFields().isEmpty()) {
            dto.setFieldIds(s.getFields().stream().map(FieldEntity::getId).collect(Collectors.toSet()));
        }
        if (s.getVehicles() != null && !s.getVehicles().isEmpty()) {
            dto.setVehicleIds(s.getVehicles().stream().map(Vehicle::getId).collect(Collectors.toSet()));
        }
        return dto;
    }
}
