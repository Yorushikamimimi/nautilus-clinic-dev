package com.clinic.controller;

import com.clinic.dto.ApiResponse;
import com.clinic.entity.Drug;
import com.clinic.repository.DrugRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drugs")
@RequiredArgsConstructor
public class DrugController {

    private final DrugRepository drugRepository;

    @GetMapping
    public ApiResponse<List<Drug>> list(@RequestParam(required = false) String name) {
        List<Drug> result = (name != null && !name.isBlank())
                ? drugRepository.findByNameContainingIgnoreCase(name)
                : drugRepository.findAll();
        return ApiResponse.ok(result);
    }

    @PostMapping
    public ApiResponse<Drug> create(@RequestBody Drug drug) {
        return ApiResponse.ok(drugRepository.save(drug));
    }

    @PutMapping("/{id}")
    public ApiResponse<Drug> update(@PathVariable Long id, @RequestBody Drug body) {
        return drugRepository.findById(id).map(d -> {
            d.setName(body.getName());
            d.setStock(body.getStock());
            d.setUnit(body.getUnit());
            return ApiResponse.ok(drugRepository.save(d));
        }).orElse(ApiResponse.fail("药品不存在"));
    }
}
