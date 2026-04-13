package com.clinic.controller;

import com.clinic.dto.ApiResponse;
import com.clinic.entity.Patient;
import com.clinic.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    public ApiResponse<List<Patient>> list(@RequestParam(required = false) String name) {
        return ApiResponse.ok(patientService.search(name));
    }

    @GetMapping("/{id}")
    public ApiResponse<Patient> get(@PathVariable Long id) {
        return patientService.findById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.fail("患者不存在"));
    }

    @PostMapping
    public ApiResponse<Patient> create(@RequestBody Patient patient) {
        return ApiResponse.ok(patientService.create(patient));
    }

    @PutMapping("/{id}")
    public ApiResponse<Patient> update(@PathVariable Long id, @RequestBody Patient body) {
        return patientService.update(id, body)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.fail("患者不存在"));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        patientService.delete(id);
        return ApiResponse.ok(null);
    }
}
