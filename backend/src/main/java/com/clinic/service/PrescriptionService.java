package com.clinic.service;

import com.clinic.entity.Prescription;
import com.clinic.repository.DrugRepository;
import com.clinic.repository.PrescriptionRepository;
import com.clinic.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final VisitRepository visitRepository;
    private final DrugRepository drugRepository;

    public List<Prescription> findByVisit(Long visitId) {
        return prescriptionRepository.findByVisitId(visitId);
    }

    @Transactional
    public Optional<Prescription> create(Map<String, Object> body) {
        Long visitId = Long.valueOf(body.get("visitId").toString());
        Long drugId  = Long.valueOf(body.get("drugId").toString());
        String dosage = body.get("dosage").toString();
        String notes  = body.getOrDefault("notes", "").toString();

        var visit = visitRepository.findById(visitId).orElse(null);
        var drug  = drugRepository.findById(drugId).orElse(null);
        if (visit == null || drug == null) return Optional.empty();

        Prescription p = new Prescription();
        p.setVisit(visit);
        p.setDrug(drug);
        p.setDosage(dosage);
        p.setNotes(notes);
        return Optional.of(prescriptionRepository.save(p));
    }
}
