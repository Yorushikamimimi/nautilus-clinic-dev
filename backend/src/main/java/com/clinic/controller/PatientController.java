package com.clinic.controller;

import com.clinic.dto.ApiResponse;
import com.clinic.entity.Patient;
import com.clinic.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientRepository patientRepository;

    @GetMapping
    public ApiResponse<List<Patient>> list(@RequestParam(required = false) String name) {
        List<Patient> result = (name != null && !name.isBlank())
                ? patientRepository.findByNameContainingIgnoreCase(name)
                : patientRepository.findAll();
        return ApiResponse.ok(result);
    }

    @GetMapping("/{id}")
    public ApiResponse<Patient> get(@PathVariable Long id) {
        return patientRepository.findById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.fail("患者不存在"));
    }

    @PostMapping
    public ApiResponse<Patient> create(@RequestBody Patient patient) {
        return ApiResponse.ok(patientRepository.save(patient));
    }

    @PutMapping("/{id}")
    public ApiResponse<Patient> update(@PathVariable Long id, @RequestBody Patient body) {
        return patientRepository.findById(id).map(p -> {
            p.setName(body.getName());
            p.setGender(body.getGender());
            p.setBirthDate(body.getBirthDate());
            p.setPhone(body.getPhone());
            return ApiResponse.ok(patientRepository.save(p));
        }).orElse(ApiResponse.fail("患者不存在"));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        patientRepository.deleteById(id);
        return ApiResponse.ok(null);
    }
}
