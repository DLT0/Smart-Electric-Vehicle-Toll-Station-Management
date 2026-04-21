package com.evstation;

// ============================================================
// LOP CON: TramSacSieuNhanh (121kW - 300kW)
// Mo ta: Tram sac cong suat cao, duoc lap tren cao toc,
//         tram xang lon. Ke thua tu lop TramSac.
// ============================================================
public class TramSacSieuNhanh extends TramSac {

    public static final double CONG_SUAT_MIN = 121.0; // kW
    public static final double CONG_SUAT_MAX = 300.0; // kW

    // ─── CONSTRUCTOR ─────────────────────────────────────────────────────────
    // Constructor mac dinh: tao tram sac sieu nhanh voi trang thai rong, cong suat mac dinh la CONG_SUAT_MIN.
    public TramSacSieuNhanh() {
        super();
        setCongSuat(CONG_SUAT_MIN);
        setTenTram("Sac Sieu Nhanh (mac dinh)");
    }

    public TramSacSieuNhanh(String maTram, HuyenLamDong viTri, double congSuat, int stt) {
        super(maTram, viTri, congSuat);
        setTenTram("Sac Sieu Nhanh " + viTri.getTen() + " " + stt);
    }

    @Override
    protected String getLoaiPrefix() {
        return "[Sac Sieu Nhanh]";
    }
}
