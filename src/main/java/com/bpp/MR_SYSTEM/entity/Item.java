package com.bpp.MR_SYSTEM.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "item_pengajuan")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pengajuan_id", nullable = false)
    @JsonBackReference
    private Pengajuan pengajuan;

    @Column(name = "nama_barang", nullable = false)
    private String namaBarang;

    @Column(name = "jumlah", nullable = false)
    private Integer jumlah;

    @Column(name = "satuan")
    private String satuan;

    @Column(name = "harga_estimasi", precision = 15, scale = 2)
    private BigDecimal hargaEstimasi;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Pengajuan getPengajuan() { return pengajuan; }
    public void setPengajuan(Pengajuan pengajuan) { this.pengajuan = pengajuan; }

    public String getNamaBarang() { return namaBarang; }
    public void setNamaBarang(String namaBarang) { this.namaBarang = namaBarang; }

    public Integer getJumlah() { return jumlah; }
    public void setJumlah(Integer jumlah) { this.jumlah = jumlah; }

    public String getSatuan() { return satuan; }
    public void setSatuan(String satuan) { this.satuan = satuan; }

    public BigDecimal getHargaEstimasi() { return hargaEstimasi; }
    public void setHargaEstimasi(BigDecimal hargaEstimasi) { this.hargaEstimasi = hargaEstimasi; }
}