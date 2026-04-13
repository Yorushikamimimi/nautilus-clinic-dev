package com.clinic.controller;

import com.clinic.dto.ApiResponse;
import com.clinic.entity.Prescription;
import com.clinic.repository.DrugRepository;
import com.clinic.repository.PrescriptionRepository;
import com.clinic.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionRepository prescriptionRepository;
    private final VisitRepository visitRepository;
    private final DrugRepository drugRepository;

    @GetMapping("/visit/{visitId}")
    public ApiResponse<List<Prescription>> byVisit(@PathVariable Long visitId) {
        return ApiResponse.ok(prescriptionRepository.findByVisitId(visitId));
    }

    @PostMapping
    public ApiResponse<Prescription> create(@RequestBody Map<String, Object> body) {
        Long visitId = Long.valueOf(body.get("visitId").toString());
        Long drugId = Long.valueOf(body.get("drugId").toString());
        String dosage = body.get("dosage").toString();
        String notes = body.getOrDefault("notes", "").toString();

        var visit = visitRepository.findById(visitId).orElse(null);
        var drug = drugRepository.findById(drugId).orElse(null);
        if (visit == null || drug == null) {
            return ApiResponse.fail("就诊记录或药品不存在");
        }

        Prescription p = new Prescription();
        p.setVisit(visit);
        p.setDrug(drug);
        p.setDosage(dosage);
        p.setNotes(notes);
        return ApiResponse.ok(prescriptionRepository.save(p));
    }
}
