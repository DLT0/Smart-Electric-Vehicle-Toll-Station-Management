package com.evstation;

import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        Module module = new Module();
        khoiTaoMockData(module);

        Menu menu = new Menu(module);
        menu.chayChuongTrinh();
    }

    private static void khoiTaoMockData(Module module) {
        // 1. DA LAT
        module.themVaoDanhSach(HuyenLamDong.DA_LAT, 7.2); // SC-DAL-001
        module.themVaoDanhSach(HuyenLamDong.DA_LAT, 60); // SN-DAL-001
        module.themVaoDanhSach(HuyenLamDong.DA_LAT, 250); // SS-DAL-001
        module.themVaoDanhSach(HuyenLamDong.DA_LAT, 300); // SS-DAL-002

        // 2. BAO LOC
        module.themVaoDanhSach(HuyenLamDong.BAO_LOC, 11); // SC-BAO-001
        module.themVaoDanhSach(HuyenLamDong.BAO_LOC, 120); // SN-BAO-001
        module.themVaoDanhSach(HuyenLamDong.BAO_LOC, 300); // SS-BAO-001

        // 3. DUC TRONG
        module.themVaoDanhSach(HuyenLamDong.DUC_TRONG, 30); // SN-DUC-001
        module.themVaoDanhSach(HuyenLamDong.DUC_TRONG, 150); // SS-DUC-001

        // 4. DI LINH
        module.themVaoDanhSach(HuyenLamDong.DI_LINH, 60); // SN-DIL-001
        module.themVaoDanhSach(HuyenLamDong.DI_LINH, 200); // SS-DIL-001

        // 5. CAC KHU VUC KHAC
        module.themVaoDanhSach(HuyenLamDong.LAM_HA, 30); // SN-LAM-001
        module.themVaoDanhSach(HuyenLamDong.DON_DUONG, 150); // SS-DON-001
        module.themVaoDanhSach(HuyenLamDong.BAO_LAM, 11); // SC-BAL-001
        module.themVaoDanhSach(HuyenLamDong.DAM_RONG, 60); // SN-DAM-001

        // --- GIA LAP CAC TRUONG HOP TEST (SCENARIOS) ---
        // Du lieu Van hanh (tich luy) tam thoi an di nhung van duoc nap de test bao tri
        // sau nay.

        // TH1: Tram dang sac QUA HAN 2 TIENG (SS-DAL-001)
        mockCase(module, "SS-DAL-001", false, 120, 480.0);

        // TH2: Tram dang sac QUA HAN 2 PHUT (SN-DAL-001)
        mockCase(module, "SN-DAL-001", false, 58, 120.5);

        // TH3: Tram dang sac BINH THUONG (SN-DUC-001) - 15 phut
        mockCase(module, "SN-DUC-001", false, 15, 5.0);

        // TH4: Tram sac CHAM dang sac 6 TIENG (SC-DAL-001) - Ko bao gio lo bi phat
        mockCase(module, "SC-DAL-001", false, 360, 250.0);

        // TH5: Tram QUA HAN BAO TRI (SS-BAO-001) - Dang ranh
        mockCase(module, "SS-BAO-001", true, 0, 515.0);

        // TH6: Tram SAP QUA HAN (SS-DAL-002) - Con 1 phut nua la bi tinh tien phat
        mockCase(module, "SS-DAL-002", false, 12, 10.0); // 300kW sac 80% (56kWh) mat ~11.2p -> 12p la vua du

        // TH7: Tram moi tinh, dang de trong (SC-BAO-001)
        mockCase(module, "SC-BAO-001", true, 0, 0.0);

        // TH8: Tram dang sac sieu nhanh (SS-DIL-001) - Moi cam sac 5 phut
        mockCase(module, "SS-DIL-001", false, 5, 88.0);

        // TH9: Tram nhanh sac vua dung thoi gian tieu chuan (SN-DIL-001) - 56 phut
        mockCase(module, "SN-DIL-001", false, 56, 45.0);

    }

    private static void mockCase(Module module, String id, boolean ready, int minAgo, double opHours) {
        TramSac t = module.timTramTheoId(id);
        if (t != null) {
            t.setSanSang(ready);
            t.setThoiGianHoatDong(opHours);
            if (!ready) {
                t.setThoiGianBatDauSac(LocalDateTime.now().minusMinutes(minAgo));
            }
        }
    }
}