package com.bpp.MR_SYSTEM.controller;

import com.bpp.MR_SYSTEM.entity.Item;
import com.bpp.MR_SYSTEM.repository.ItemRepository;
import com.bpp.MR_SYSTEM.repository.PengajuanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/item")
public class ItemController {

    @Autowired
    private ItemRepository itemRepo;

    @Autowired
    private PengajuanRepository pengajuanRepo;

    @GetMapping("/{itemId}")
    public ResponseEntity<Item> getItemById(@PathVariable Integer itemId) {
        return itemRepo.findById(itemId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/pengajuan/{pengajuanId}")
    public ResponseEntity<List<Item>> getItemsByPengajuan(@PathVariable Integer pengajuanId) {
        List<Item> items = itemRepo.findByPengajuan_Id(pengajuanId);

        if (items.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(items);
    }

    @PostMapping("/tambah/{pengajuanId}")
    public ResponseEntity<Item> addItemToPengajuan(@PathVariable Integer pengajuanId, @RequestBody Item itemDetails) {
        return pengajuanRepo.findById(pengajuanId).map(pengajuan -> {
            itemDetails.setPengajuan(pengajuan);
            Item savedItem = itemRepo.save(itemDetails);
            return ResponseEntity.status(201).body(savedItem);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<Item> updateItem(@PathVariable Integer itemId, @RequestBody Item details) {
        return itemRepo.findById(itemId).map(existingItem -> {
            existingItem.setNamaBarang(details.getNamaBarang());
            existingItem.setJumlah(details.getJumlah());
            existingItem.setSatuan(details.getSatuan());
            existingItem.setHargaEstimasi(details.getHargaEstimasi());

            Item updated = itemRepo.save(existingItem);
            return ResponseEntity.ok(updated);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Integer itemId) {
        if (!itemRepo.existsById(itemId)) {
            return ResponseEntity.notFound().build();
        }
        itemRepo.deleteById(itemId);
        return ResponseEntity.noContent().build();
    }
}