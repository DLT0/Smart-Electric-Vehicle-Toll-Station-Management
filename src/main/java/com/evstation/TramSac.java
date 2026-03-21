package com.evstation;

import java.time.LocalDateTime;

// ============================================================
// LOP TRUU TUONG: TramSac
// ============================================================
abstract class TramSac {
    // * Khai bao cac truong du lieu (the private) - Rang buoc du lieu
    private String maTram; // Ma dinh danh duy nhat cho tram sac
    private String tenTram; // Ten = Loai + ViTri + STT
    private HuyenLamDong viTri; // Vi tri duoc chon tu Enum (type-safe)
    private boolean sanSang; // true = San sang | false = Dang su dung
    private double congSuat; // Cong suat sac (don vi: kW), phai > 0
    private double thoiGianSuDung; // So gio su dung cua phien sac hien tai/gan nhat
    private double thoiGianHoatDong; // Tong so gio tich luy da van hanh, phai >= 0
    protected static final double GIA_MOI_KWH = 3850; // Don gia co dinh (VND/kWh)
    protected static final double HAN_BAO_TRI_MAC_DINH = 500.0; // Han bao tri mac dinh (h)
    private int sttHeThong; // STT khi them vao he thong (de sap xep fallback)
    private LocalDateTime thoiGianBatDauSac; // Thoi gian bat dau sac (neu dang sac)

    // * Dong goi thuoc tinh (the public) - Getter & Setter
    public LocalDateTime getThoiGianBatDauSac() {
        return this.thoiGianBatDauSac;
    }

    public void setThoiGianBatDauSac(LocalDateTime t) {
        this.thoiGianBatDauSac = t;
    }

    // * ID duoc sinh tu dong boi Module.sinhMaTram()
    // Vi du dinh dang: "SC-DAL-001", "SC-BAO-003", ...
    // setMaTram() chi duoc goi 1 lan tu constructor, sau do READ-ONLY

    // maTram: READ-ONLY - chi co getter, khong cho phep cap nhat tu ben ngoai
    public String getMaTram() {
        return this.maTram;
    }

    private void setMaTram(String value) {
        if (value == null || value.trim().isEmpty())
            value = "UNKNOWN";
        this.maTram = value.trim();
    }

    public String getTenTram() {
        return this.tenTram;
    }

    public void setTenTram(String value) {
        if (value == null || value.trim().isEmpty())
            value = "Chua dat ten";
        this.tenTram = value.trim();
    }

    public HuyenLamDong getViTri() {
        return this.viTri;
    }

    public void setViTri(HuyenLamDong value) {
        if (value == null)
            return; // Rang buoc: vi tri khong duoc null
        this.viTri = value;
    }

    public boolean isSanSang() {
        return this.sanSang;
    }

    public void setSanSang(boolean value) {
        this.sanSang = value; // Boolean khong can rang buoc them
    }

    public double getCongSuat() {
        return this.congSuat;
    }

    public void setCongSuat(double value) {
        if (value <= 0)
            value = 7; // Rang buoc: cong suat phai la so duong
        else if (value > 300)
            value = 300; // Rang buoc: gioi han toi da 300kW
        this.congSuat = value;
    }

    public double getThoiGianSuDung() {
        return this.thoiGianSuDung;
    }

    public void setThoiGianSuDung(double value) {
        if (value < 0)
            value = 0;
        this.thoiGianSuDung = value;
    }

    public double getThoiGianHoatDong() {
        return this.thoiGianHoatDong;
    }

    public void setThoiGianHoatDong(double value) {
        if (value < 0)
            value = 0; // Rang buoc: thoi gian khong duoc am
        this.thoiGianHoatDong = value;
    }

    public int getSttHeThong() {
        return this.sttHeThong;
    }

    public void setSttHeThong(int value) {
        if (value < 1)
            value = 1;
        this.sttHeThong = value;
    }

    // Constructor: nhan maTram da duoc Module sinh san, cac truong con lai tu dong
    public TramSac(String maTram, HuyenLamDong viTri, double congSuat, int sttHeThong) {
        setMaTram(maTram); // maTram duoc Module.sinhMaTram() tao ra
        setViTri(viTri);
        setCongSuat(congSuat);
        setThoiGianSuDung(0.0); // Tu dong khoi tao la 0
        setThoiGianHoatDong(0.0); // Tu dong khoi tao la 0
        setSanSang(true); // Mac dinh: San sang / Trong
        setSttHeThong(sttHeThong);
        setThoiGianBatDauSac(null);
    }

    public double tinhMucHaoMon() {
        double mucHaoMon = (this.thoiGianHoatDong / HAN_BAO_TRI_MAC_DINH) * 100;
        return Math.min(mucHaoMon, 100.0);
    }

    public String getTrangThaiBaoTri() {
        double mucHaoMon = tinhMucHaoMon();
        if (mucHaoMon >= 100.0) {
            return "Bao tri";
        }
        if (mucHaoMon > 90.0) {
            return "Can bao tri";
        }
        return "On dinh";
    }

    public String getTrangThaiHoatDong() {
        if (tinhMucHaoMon() >= 100.0) {
            return "Ngung hoat dong";
        }
        return this.sanSang ? "San sang" : "Dang sac";
    }

    protected abstract String getLoaiPrefix();
}

// ============================================================
// LOP CON: TramSacCham (7kW - 11kW)
// ============================================================
class TramSacCham extends TramSac {
    public TramSacCham(String maTram, HuyenLamDong viTri, double congSuat, int stt, int sttHeThong) {
        super(maTram, viTri, congSuat, sttHeThong);
        setTenTram("Sac Cham " + viTri.getTen() + " " + stt);
    }

    @Override
    protected String getLoaiPrefix() {
        return "[Sac Cham]";
    }
}

// ============================================================
// LOP CON: TramSacNhanh (12kW - 120kW)
// ============================================================
class TramSacNhanh extends TramSac {
    public TramSacNhanh(String maTram, HuyenLamDong viTri, double congSuat, int stt, int sttHeThong) {
        super(maTram, viTri, congSuat, sttHeThong);
        setTenTram("Sac Nhanh " + viTri.getTen() + " " + stt);
    }

    @Override
    protected String getLoaiPrefix() {
        return "[Sac Nhanh]";
    }
}

// ============================================================
// LOP CON: TramSacSieuNhanh (121kW - 300kW)
// ============================================================
class TramSacSieuNhanh extends TramSac {
    public TramSacSieuNhanh(String maTram, HuyenLamDong viTri, double congSuat, int stt, int sttHeThong) {
        super(maTram, viTri, congSuat, sttHeThong);
        setTenTram("Sac Sieu Nhanh " + viTri.getTen() + " " + stt);
    }

    @Override
    protected String getLoaiPrefix() {
        return "[Sac Sieu Nhanh]";
    }
}