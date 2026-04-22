package com.evstation;

// ============================================================
// LOP CON: TramSacSieuNhanh (121kW - 300kW)
// Mo ta: Tram sac cong suat cao, duoc lap tren cao toc,
//         tram xang lon. Ke thua tu lop TramSac.
// ============================================================
public class TramSacSieuNhanh extends TramSac {

    public static final double CONG_SUAT_MIN = 121.0; // kW
    public static final double CONG_SUAT_MAX = 300.0; // kW

    public TramSacSieuNhanh(String maTram, HuyenLamDong viTri, double congSuat, int stt) {
        super(maTram, viTri, congSuat);
        setTenTram("Sac Sieu Nhanh " + viTri.getTen() + " " + stt);
    }

    @Override
    protected String getLoaiPrefix() {
        return "[Sac Sieu Nhanh]";
    }
}
