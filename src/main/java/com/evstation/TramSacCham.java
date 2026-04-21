package com.evstation;

// ============================================================
// LOP CON: TramSacCham (7kW - 11kW)
// Mo ta: Tram sac cong suat thap, thuong dung tai nha hoac
//         noi dau xe qua dem. Ke thua tu lop TramSac.
// ============================================================
public class TramSacCham extends TramSac {

    // ─── HANG SO RANG BUOC CONG SUAT ─────────────────────────────────────────
    public static final double CONG_SUAT_MIN = 7.0;   // kW
    public static final double CONG_SUAT_MAX = 11.0;  // kW

    // ─── CONSTRUCTOR ─────────────────────────────────────────────────────────
    public TramSacCham(String maTram, HuyenLamDong viTri, double congSuat, int stt) {
        super(maTram, viTri, congSuat);
        setTenTram("Sac Cham " + viTri.getTen() + " " + stt);
    }

    // ─── PHUONG THUC TRUU TUONG BAT BUOC KE THUA ─────────────────────────────
    @Override
    protected String getLoaiPrefix() {
        return "[Sac Cham]";
    }
}
