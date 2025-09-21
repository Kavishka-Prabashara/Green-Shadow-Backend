package lk.kavishka.greenshadowbackend.controller;

import lk.kavishka.greenshadowbackend.dto.MonitoringLogDto;
import lk.kavishka.greenshadowbackend.entity.*;
import lk.kavishka.greenshadowbackend.exception.ResourceNotFoundException;
import lk.kavishka.greenshadowbackend.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class MonitoringLogController {

    private final MonitoringLogRepository logRepository;
    private final FieldRepository fieldRepository;
    private final CropRepository cropRepository;
    private final StaffRepository staffRepository;
    private final ModelMapper mapper = new ModelMapper();

    @GetMapping
    public List<MonitoringLogDto> getAll() {
        return logRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public MonitoringLogDto getOne(@PathVariable String id) {
        MonitoringLog log = logRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Log not found"));
        return toDto(log);
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMINISTRATIVE') or hasRole('SCIENTIST')")
    @PostMapping
    public ResponseEntity<MonitoringLogDto> create(@RequestBody MonitoringLogDto dto) {
        MonitoringLog log = mapper.map(dto, MonitoringLog.class);
        if (dto.getFieldIds() != null) {
            Set<FieldEntity> fields = new HashSet<>();
            for (String fid : dto.getFieldIds()) {
                FieldEntity f = fieldRepository.findById(fid).orElseThrow(() -> new ResourceNotFoundException("Field not found: " + fid));
                fields.add(f);
            }
            log.setFields(fields);
        }
        if (dto.getCropIds() != null) {
            Set<Crop> crops = new HashSet<>();
            for (String cid : dto.getCropIds()) {
                Crop c = cropRepository.findById(cid).orElseThrow(() -> new ResourceNotFoundException("Crop not found: " + cid));
                crops.add(c);
            }
            log.setCrops(crops);
        }
        if (dto.getStaffIds() != null) {
            Set<Staff> staffSet = new HashSet<>();
            for (String sid : dto.getStaffIds()) {
                Staff s = staffRepository.findById(sid).orElseThrow(() -> new ResourceNotFoundException("Staff not found: " + sid));
                staffSet.add(s);
            }
            log.setStaff(staffSet);
        }
        MonitoringLog saved = logRepository.save(log);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(saved));
    }

    @PreAuthorize("hasRole('MANAGER') or hasRole('SCIENTIST')")
    @PutMapping("/{id}")
    public MonitoringLogDto update(@PathVariable String id, @RequestBody MonitoringLogDto dto) {
        MonitoringLog existing = logRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Log not found"));
        existing.setLogCode(dto.getLogCode());
        existing.setLogDate(dto.getLogDate());
        existing.setDetails(dto.getDetails());
        existing.setObservedImage(dto.getObservedImage());

        if (dto.getFieldIds() != null) {
            Set<FieldEntity> fields = new HashSet<>();
            for (String fid : dto.getFieldIds()) {
                FieldEntity f = fieldRepository.findById(fid).orElseThrow(() -> new ResourceNotFoundException("Field not found: " + fid));
                fields.add(f);
            }
            existing.setFields(fields);
        } else existing.setFields(Collections.emptySet());

        if (dto.getCropIds() != null) {
            Set<Crop> crops = new HashSet<>();
            for (String cid : dto.getCropIds()) {
                Crop c = cropRepository.findById(cid).orElseThrow(() -> new ResourceNotFoundException("Crop not found: " + cid));
                crops.add(c);
            }
            existing.setCrops(crops);
        } else existing.setCrops(Collections.emptySet());

        if (dto.getStaffIds() != null) {
            Set<Staff> staffSet = new HashSet<>();
            for (String sid : dto.getStaffIds()) {
                Staff s = staffRepository.findById(sid).orElseThrow(() -> new ResourceNotFoundException("Staff not found: " + sid));
                staffSet.add(s);
            }
            existing.setStaff(staffSet);
        } else existing.setStaff(Collections.emptySet());

        MonitoringLog saved = logRepository.save(existing);
        return toDto(saved);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        logRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private MonitoringLogDto toDto(MonitoringLog log) {
        MonitoringLogDto dto = mapper.map(log, MonitoringLogDto.class);
        if (log.getFields() != null && !log.getFields().isEmpty()) dto.setFieldIds(log.getFields().stream().map(FieldEntity::getId).collect(Collectors.toSet()));
        if (log.getCrops() != null && !log.getCrops().isEmpty()) dto.setCropIds(log.getCrops().stream().map(Crop::getId).collect(Collectors.toSet()));
        if (log.getStaff() != null && !log.getStaff().isEmpty()) dto.setStaffIds(log.getStaff().stream().map(Staff::getId).collect(Collectors.toSet()));
        return dto;
    }
}
