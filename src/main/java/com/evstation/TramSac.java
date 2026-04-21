package com.evstation;

import java.time.LocalDateTime;

// ============================================================
// LỚP TRỪU TƯỢNG: TramSac
// Mô tả: Định nghĩa cấu trúc dữ liệu và hành vi chung của
//         một trạm sạc xe điện. Đây là lớp cha (base class)
//         cho TramSacCham, TramSacNhanh, TramSacSieuNhanh.
//
//         Áp dụng Đóng gói (Encapsulation):
//         - Tất cả thuộc tính là private → truy cập qua getter/setter
//         - Setter có ràng buộc kiểm tra tính hợp lệ của dữ liệu
//
//         Áp dụng Trừu tượng (Abstraction):
//         - getLoaiPrefix() là abstract → mỗi lớp con tự định nghĩa
// ============================================================
public abstract class TramSac {

    // ─── CÁC THUỘC TÍNH (PRIVATE FIELDS) ────────────────────────────────────
    private String maTram;             // Mã định danh duy nhất (ví dụ: "SC-DAL-001")
    private String tenTram;            // Tên hiển thị: Loại + Vị trí + STT
    private HuyenLamDong viTri;        // Vị trí địa lý (Enum, type-safe, không thể sai kiểu)
    private boolean sanSang;           // true = Sẵn sàng / Trống | false = Đang sạc
    private double congSuat;           // Công suất sạc (kW), phải nằm trong [7, 300]
    private double thoiGianSuDung;     // Số giờ của phiên sạc hiện tại / gần nhất
    private double thoiGianHoatDong;   // Tổng giờ vận hành tích lũy (dùng để tính hao mòn)
    private LocalDateTime thoiGianBatDauSac; // Thời điểm bắt đầu sạc; null nếu đang rảnh
    private double hanBaoTri;           // Ngưỡng bảo trì riêng của trạm (giờ), mặc định = HAN_BAO_TRI_MAC_DINH

    // ─── HẰNG SỐ DÙNG CHUNG CHO MỌI LOẠI TRẠM ──────────────────────────────
    protected static final double GIA_MOI_KWH = 3850;           // Đơn giá điện (VND/kWh)
    protected static final double HAN_BAO_TRI_MAC_DINH = 500.0; // Ngưỡng bảo trì mặc định (giờ)

    // ─── GETTER / SETTER ─────────────────────────────────────────────────────
    public LocalDateTime getThoiGianBatDauSac() {
        return this.thoiGianBatDauSac;
    }

    public void setThoiGianBatDauSac(LocalDateTime t) {
        this.thoiGianBatDauSac = t;
    }

    // maTram: CHỈ ĐỌC từ bên ngoài.
    // Được Module.sinhMaTram() sinh ra 1 lần lúc khởi tạo trạm.
    // Định dạng: "[PREFIX]-[MA_KHU_VUC]-[STT]" (ví dụ: "SC-DAL-001", "SN-DUC-003")
    // setMaTram() là private → không cho phép ghi đè sau khi khởi tạo.
    public String getMaTram() {
        return this.maTram;
    }

    private void setMaTram(String value) {
        if (value == null || value.trim().isEmpty()) {
            value = "UNKNOWN";
        }
        this.maTram = value.trim();
    }

    public String getTenTram() {
        return this.tenTram;
    }

    public void setTenTram(String value) {
        if (value == null || value.trim().isEmpty()) {
            value = "Chua dat ten";
        }
        this.tenTram = value.trim();
    }

    public HuyenLamDong getViTri() {
        return this.viTri;
    }

    public void setViTri(HuyenLamDong value) {
        if (value == null) {
            return; // Ràng buộc: vị trí không được null (bảo vệ tính toàn vẹn)

        }
        this.viTri = value;
    }

    public boolean isSanSang() {
        return this.sanSang;
    }

    public void setSanSang(boolean value) {
        this.sanSang = value;
    }

    public double getCongSuat() {
        return this.congSuat;
    }

    public void setCongSuat(double value) {
        if (value < 7) {
            value = 7; // Rang buoc: cong suat phai la so duong
        } else if (value > 300) {
            value = 300; // Rang buoc: gioi han toi da 300kW

        }
        this.congSuat = value;
    }

    public double getThoiGianSuDung() {
        return this.thoiGianSuDung;
    }

    public void setThoiGianSuDung(double value) {
        if (value < 0) {
            value = 0;
        }
        this.thoiGianSuDung = value;
    }

    public double getThoiGianHoatDong() {
        return this.thoiGianHoatDong;
    }

    public void setThoiGianHoatDong(double value) {
        if (value < 0) {
            value = 0; // Rang buoc: thoi gian khong duoc am

        }
        this.thoiGianHoatDong = value;
    }

    public double getHanBaoTri() {
        return this.hanBaoTri;
    }

    public void setHanBaoTri(double value) {
        if (value <= 0) {
            value = HAN_BAO_TRI_MAC_DINH;
        }
        this.hanBaoTri = value;
    }

    // Constructor: nhan maTram da duoc Module sinh san, cac truong con lai tu dong
    public TramSac(String maTram, HuyenLamDong viTri, double congSuat) {
        setMaTram(maTram); // maTram duoc Module.sinhMaTram() tao ra
        setViTri(viTri);
        setCongSuat(congSuat);
        setThoiGianSuDung(0.0);
        setThoiGianHoatDong(0.0);
        setSanSang(true);
        setThoiGianBatDauSac(null);
        setHanBaoTri(HAN_BAO_TRI_MAC_DINH);
    }

    public double tinhMucHaoMon() {
        if (this.hanBaoTri <= 0) {
            return 0.0;
        }
        double mucHaoMon = (this.thoiGianHoatDong / this.hanBaoTri) * 100;
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
