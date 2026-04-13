package com.clinic.service;

import com.clinic.entity.Visit;
import com.clinic.enums.VisitStatus;
import com.clinic.repository.PatientRepository;
import com.clinic.repository.UserRepository;
import com.clinic.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VisitService {

    private final VisitRepository visitRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public List<Visit> findAll() {
        return visitRepository.findAll();
    }

    public List<Visit> findByPatient(Long patientId) {
        return visitRepository.findByPatientIdOrderByVisitDateDesc(patientId);
    }

    @Transactional
    public Optional<Visit> create(Map<String, Object> body) {
        Long patientId = Long.valueOf(body.get("patientId").toString());
        Long doctorId  = Long.valueOf(body.get("doctorId").toString());
        String complaint = body.getOrDefault("complaint", "").toString();

        var patient = patientRepository.findById(patientId).orElse(null);
        var doctor  = userRepository.findById(doctorId).orElse(null);
        if (patient == null || doctor == null) return Optional.empty();

        Visit visit = new Visit();
        visit.setPatient(patient);
        visit.setDoctor(doctor);
        visit.setComplaint(complaint);
        return Optional.of(visitRepository.save(visit));
    }

    @Transactional
    public Optional<Visit> updateStatus(Long id, String status) {
        return visitRepository.findById(id).map(v -> {
            v.setStatus(VisitStatus.valueOf(status));
            return visitRepository.save(v);
        });
    }
}
