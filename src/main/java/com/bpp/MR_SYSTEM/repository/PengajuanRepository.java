package com.bpp.MR_SYSTEM.repository;

import com.bpp.MR_SYSTEM.entity.Pengajuan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PengajuanRepository extends JpaRepository<Pengajuan, Integer> {
    List<Pengajuan> findByNoMrContainingIgnoreCase(String keyword);
}