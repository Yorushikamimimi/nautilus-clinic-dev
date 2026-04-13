package com.clinic.controller;

import com.clinic.dto.ApiResponse;
import com.clinic.entity.Visit;
import com.clinic.enums.VisitStatus;
import com.clinic.repository.PatientRepository;
import com.clinic.repository.UserRepository;
import com.clinic.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/visits")
@RequiredArgsConstructor
public class VisitController {

    private final VisitRepository visitRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    @GetMapping
    public ApiResponse<List<Visit>> list() {
        return ApiResponse.ok(visitRepository.findAll());
    }

    @GetMapping("/patient/{patientId}")
    public ApiResponse<List<Visit>> byPatient(@PathVariable Long patientId) {
        return ApiResponse.ok(visitRepository.findByPatientIdOrderByVisitDateDesc(patientId));
    }

    @PostMapping
    public ApiResponse<Visit> create(@RequestBody Map<String, Object> body) {
        Long patientId = Long.valueOf(body.get("patientId").toString());
        Long doctorId = Long.valueOf(body.get("doctorId").toString());
        String complaint = body.getOrDefault("complaint", "").toString();

        var patient = patientRepository.findById(patientId).orElse(null);
        var doctor = userRepository.findById(doctorId).orElse(null);
        if (patient == null || doctor == null) {
            return ApiResponse.fail("患者或医生不存在");
        }

        Visit visit = new Visit();
        visit.setPatient(patient);
        visit.setDoctor(doctor);
        visit.setComplaint(complaint);
        return ApiResponse.ok(visitRepository.save(visit));
    }

    @PutMapping("/{id}/status")
    public ApiResponse<Visit> updateStatus(@PathVariable Long id,
                                           @RequestBody Map<String, String> body) {
        return visitRepository.findById(id).map(v -> {
            v.setStatus(VisitStatus.valueOf(body.get("status")));
            return ApiResponse.ok(visitRepository.save(v));
        }).orElse(ApiResponse.fail("就诊记录不存在"));
    }
}
