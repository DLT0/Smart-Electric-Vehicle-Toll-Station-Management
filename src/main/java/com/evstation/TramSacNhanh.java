package com.evstation;

// ============================================================
// LOP CON: TramSacNhanh (12kW - 120kW)
// Mo ta: Tram sac cong suat trung binh, phu hop tai tram
//         dich vu, trung tam thuong mai. Ke thua tu TramSac.
// ============================================================
public class TramSacNhanh extends TramSac {

    // ─── HANG SO RANG BUOC CONG SUAT ─────────────────────────────────────────
    public static final double CONG_SUAT_MIN = 12.0;  // kW
    public static final double CONG_SUAT_MAX = 120.0; // kW

    // ─── CONSTRUCTOR ─────────────────────────────────────────────────────────
    TramSacNhanh(String maTram, HuyenLamDong viTri, double congSuat, int stt) {
        super(maTram, viTri, congSuat);
        setTenTram("Sac Nhanh " + viTri.getTen() + " " + stt);
    }

    // ─── PHUONG THUC TRUU TUONG BAT BUOC KE THUA ─────────────────────────────
    @Override
    protected String getLoaiPrefix() {
        return "[Sac Nhanh]";
    }
}
