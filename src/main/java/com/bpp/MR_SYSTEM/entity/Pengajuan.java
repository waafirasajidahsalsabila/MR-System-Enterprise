package com.bpp.MR_SYSTEM.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "pengajuan")
public class Pengajuan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Nomor MR tidak boleh kosong!")
    @Column(name = "no_mr", unique = true, nullable = false)
    private String noMr;

    @NotNull(message = "Tanggal Request wajib diisi!")
    @Column(name = "tgl_request", nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate tglRequest;

    @NotBlank(message = "Nama Pemohon tidak boleh kosong!")
    @Column(name = "pemohon", nullable = false)
    private String pemohon;

    @NotBlank(message = "Departemen wajib diisi!")
    @Column(name = "departemen", nullable = false)
    private String departemen;

    @NotBlank(message = "Prioritas wajib diisi!")
    @Pattern(regexp = "^(Low|Medium|High|URGENT)$", message = "Prioritas hanya boleh Low, Medium, High, atau URGENT!")
    @Column(name = "prioritas", nullable = false)
    private String prioritas;

    @Column(name = "date_of_ppe")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOfPpe;

    @Column(name = "hps", precision = 15, scale = 2)
    private BigDecimal hps;

    @OneToMany(mappedBy = "pengajuan", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Item> items = new ArrayList<>();

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNoMr() { return noMr; }
    public void setNoMr(String noMr) { this.noMr = noMr; }

    public LocalDate getTglRequest() { return tglRequest; }
    public void setTglRequest(LocalDate tglRequest) { this.tglRequest = tglRequest; }

    public String getPemohon() { return pemohon; }
    public void setPemohon(String pemohon) { this.pemohon = pemohon; }

    public String getDepartemen() { return departemen; }
    public void setDepartemen(String departemen) { this.departemen = departemen; }

    public String getPrioritas() { return prioritas; }
    public void setPrioritas(String prioritas) { this.prioritas = prioritas; }

    public LocalDate getDateOfPpe() { return dateOfPpe; }
    public void setDateOfPpe(LocalDate dateOfPpe) { this.dateOfPpe = dateOfPpe; }

    public BigDecimal getHps() { return hps; }
    public void setHps(BigDecimal hps) { this.hps = hps; }

    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }
}