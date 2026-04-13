package com.clinic.service;

import com.clinic.entity.Patient;
import com.clinic.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    public List<Patient> search(String name) {
        if (name == null || name.isBlank()) return findAll();
        return patientRepository.findByNameContainingIgnoreCase(name);
    }

    public Optional<Patient> findById(Long id) {
        return patientRepository.findById(id);
    }

    @Transactional
    public Patient create(Patient patient) {
        return patientRepository.save(patient);
    }

    @Transactional
    public Optional<Patient> update(Long id, Patient body) {
        return patientRepository.findById(id).map(p -> {
            p.setName(body.getName());
            p.setGender(body.getGender());
            p.setBirthDate(body.getBirthDate());
            p.setPhone(body.getPhone());
            return patientRepository.save(p);
        });
    }

    @Transactional
    public void delete(Long id) {
        patientRepository.deleteById(id);
    }
}
