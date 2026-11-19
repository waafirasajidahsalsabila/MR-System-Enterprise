package com.bpp.MR_SYSTEM.repository;

import com.bpp.MR_SYSTEM.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findByPengajuan_Id(Integer pengajuanId);
}