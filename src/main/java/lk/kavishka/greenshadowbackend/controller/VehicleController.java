package lk.kavishka.greenshadowbackend.controller;

import lk.kavishka.greenshadowbackend.dto.VehicleDto;
import lk.kavishka.greenshadowbackend.entity.Staff;
import lk.kavishka.greenshadowbackend.entity.Vehicle;
import lk.kavishka.greenshadowbackend.exception.ResourceNotFoundException;
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
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleRepository vehicleRepository;
    private final StaffRepository staffRepository;
    private final ModelMapper mapper = new ModelMapper();

    @GetMapping
    public List<VehicleDto> getAll() {
        return vehicleRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public VehicleDto getOne(@PathVariable String id) {
        Vehicle v = vehicleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));
        return toDto(v);
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATIVE')")
    @PostMapping
    public ResponseEntity<VehicleDto> create(@RequestBody VehicleDto dto) {
        Vehicle v = mapper.map(dto, Vehicle.class);

        Set<Staff> staffSet = new HashSet<>();
        if (dto.getAssignedStaffIds() != null) {
            for (String sid : dto.getAssignedStaffIds()) {
                Staff s = staffRepository.findById(sid).orElseThrow(() -> new ResourceNotFoundException("Staff not found: " + sid));
                staffSet.add(s);
            }
        }
        v.setAssignedStaff(staffSet);
        Vehicle saved = vehicleRepository.save(v);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(saved));
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATIVE')")
    @PutMapping("/{id}")
    public VehicleDto update(@PathVariable String id, @RequestBody VehicleDto dto) {
        Vehicle existing = vehicleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));
        existing.setVehicleCode(dto.getVehicleCode());
        existing.setLicensePlate(dto.getLicensePlate());
        existing.setCategory(dto.getCategory());
        existing.setFuelType(dto.getFuelType());
        existing.setStatus(dto.getStatus());
        existing.setRemarks(dto.getRemarks());

        Set<Staff> staffSet = new HashSet<>();
        if (dto.getAssignedStaffIds() != null) {
            for (String sid : dto.getAssignedStaffIds()) {
                Staff s = staffRepository.findById(sid).orElseThrow(() -> new ResourceNotFoundException("Staff not found: " + sid));
                staffSet.add(s);
            }
        }
        existing.setAssignedStaff(staffSet);

        Vehicle saved = vehicleRepository.save(existing);
        return toDto(saved);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        vehicleRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private VehicleDto toDto(Vehicle v) {
        VehicleDto dto = mapper.map(v, VehicleDto.class);
        if (v.getAssignedStaff() != null && !v.getAssignedStaff().isEmpty()) {
            dto.setAssignedStaffIds(v.getAssignedStaff().stream().map(Staff::getId).collect(Collectors.toSet()));
        }
        return dto;
    }
}
