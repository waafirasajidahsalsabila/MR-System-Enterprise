package com.bpp.MR_SYSTEM.controller;

import com.bpp.MR_SYSTEM.entity.Pengajuan;
import com.bpp.MR_SYSTEM.entity.Item;
import com.bpp.MR_SYSTEM.repository.PengajuanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pengajuan")
public class PengajuanController {

    @Autowired
    private PengajuanRepository repo;

    private ResponseEntity<?> buildErrorResponse(BindingResult result) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "The request parameters failed to validate."
        );

        problemDetail.setType(URI.create("https://example.net/validation-error"));
        problemDetail.setTitle("Validation Error");

        List<Map<String, String>> invalidParams = new ArrayList<>();

        for (ObjectError error : result.getAllErrors()) {
            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;
                Map<String, String> paramInfo = new HashMap<>();
                paramInfo.put("name", fieldError.getField());
                paramInfo.put("reason", fieldError.getDefaultMessage());
                invalidParams.add(paramInfo);
            } else {
                Map<String, String> paramInfo = new HashMap<>();
                paramInfo.put("name", "global_error");
                paramInfo.put("reason", error.getDefaultMessage());
                invalidParams.add(paramInfo);
            }
        }

        problemDetail.setProperty("invalidParams", invalidParams);
        return ResponseEntity.of(problemDetail).build();
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<Pengajuan> pengajuanPage = repo.findAll(PageRequest.of(page, size));

        Map<String, Object> response = new HashMap<>();
        response.put("totalItems", pengajuanPage.getTotalElements());
        response.put("pengajuans", pengajuanPage.getContent());
        response.put("totalPages", pengajuanPage.getTotalPages());
        response.put("currentPage", pengajuanPage.getNumber() + 1);

        return ResponseEntity.ok(response);
    }

    // PERBAIKAN UTAMA DI SINI: MANUAL MAPPING AGAR ITEMS TERKIRIM
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return repo.findById(id).map(pengajuan -> {
            Map<String, Object> response = new HashMap<>();

            response.put("id", pengajuan.getId());
            response.put("noMr", pengajuan.getNoMr());
            response.put("pemohon", pengajuan.getPemohon());
            response.put("departemen", pengajuan.getDepartemen());
            response.put("prioritas", pengajuan.getPrioritas());
            response.put("tglRequest", pengajuan.getTglRequest());
            response.put("dateOfPpe", pengajuan.getDateOfPpe());
            response.put("hps", pengajuan.getHps());

            List<Map<String, Object>> itemList = new ArrayList<>();
            if (pengajuan.getItems() != null) {
                for (Item item : pengajuan.getItems()) {
                    Map<String, Object> itemMap = new HashMap<>();
                    itemMap.put("id", item.getId());
                    itemMap.put("namaBarang", item.getNamaBarang());
                    itemMap.put("jumlah", item.getJumlah());
                    itemMap.put("satuan", item.getSatuan());
                    itemMap.put("hargaEstimasi", item.getHargaEstimasi());
                    itemList.add(itemMap);
                }
            }
            response.put("items", itemList);

            return ResponseEntity.ok(response);
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<Pengajuan>> search(@PathVariable String keyword) {
        List<Pengajuan> results = repo.findByNoMrContainingIgnoreCase(keyword);
        if (results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(results);
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Pengajuan p, BindingResult result) {
        if (result.hasErrors()) {
            return buildErrorResponse(result);
        }
        try {
            Pengajuan saved = repo.save(p);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody Pengajuan details, BindingResult result) {
        if (result.hasErrors()) {
            return buildErrorResponse(result);
        }

        return repo.findById(id).map(existing -> {
            existing.setNoMr(details.getNoMr());
            existing.setTglRequest(details.getTglRequest());
            existing.setPemohon(details.getPemohon());
            existing.setDepartemen(details.getDepartemen());
            existing.setPrioritas(details.getPrioritas());

            if (details.getDateOfPpe() != null) existing.setDateOfPpe(details.getDateOfPpe());
            if (details.getHps() != null) existing.setHps(details.getHps());

            Pengajuan updated = repo.save(existing);
            return ResponseEntity.ok(updated);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}