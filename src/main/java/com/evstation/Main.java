package com.evstation;

import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        QuanLyTramSac module = new QuanLyTramSac();
        Menu menu = new Menu(module);
        menu.chayChuongTrinh();
    }

    private static void khoiTaoMockData(Module module) {
        // 1. DA LAT
        QuanLyTramSac ql = new QuanLyTramSac();
        ql.themVaoDanhSach(HuyenLamDong.DA_LAT, 7.2); // SC-DAL-001
        ql.themVaoDanhSach(HuyenLamDong.DA_LAT, 60); // SN-DAL-001
        ql.themVaoDanhSach(HuyenLamDong.DA_LAT, 250); // SS-DAL-001
        ql.themVaoDanhSach(HuyenLamDong.DA_LAT, 300); // SS-DAL-002

        // 2. BAO LOC
        ql.themVaoDanhSach(HuyenLamDong.BAO_LOC, 11); // SC-BAO-001
        ql.themVaoDanhSach(HuyenLamDong.BAO_LOC, 120); // SN-BAO-001
        ql.themVaoDanhSach(HuyenLamDong.BAO_LOC, 300); // SS-BAO-001

        //   3. DUC TRONG
        ql.themVaoDanhSach(HuyenLamDong.DUC_TRONG, 30); // SN-DUC-001
        ql.themVaoDanhSach(HuyenLamDong.DUC_TRONG, 150); // SS-DUC-001

        // 4. DI LINH
        ql.themVaoDanhSach(HuyenLamDong.DI_LINH, 60); // SN-DIL-001
        ql.themVaoDanhSach(HuyenLamDong.DI_LINH, 200); // SS-DIL-001

        // 5. CAC KHU VUC KHAC
        ql.themVaoDanhSach(HuyenLamDong.LAM_HA, 30); // SN-LAM-001
        ql.themVaoDanhSach(HuyenLamDong.DON_DUONG, 150); // SS-DON-001
        ql.themVaoDanhSach(HuyenLamDong.BAO_LAM, 11); // SC-BAL-001
        ql.themVaoDanhSach(HuyenLamDong.DAM_RONG, 60); // SN-DAM-001

        // --- GIA LAP CAC TRUONG HOP TEST (SCENARIOS) ---
        // Cac truong hop test cho chuc nang 10 (tinh chi phi) va 11 (sap xep)

        // TH1: Tram dang sac QUA HAN 2 TIENG (SS-DAL-001) - Chi phi cao
        mockCase(module, "SS-DAL-001", false, 120, 2.0, 480.0);

        // TH2: Tram dang sac QUA HAN 2 PHUT (SN-DAL-001) - Chi phi vua phai
        mockCase(module, "SN-DAL-001", false, 58, 1.0, 120.5);

        // TH3: Tram dang sac BINH THUONG (SN-DUC-001) - Chi phi tieu chuan
        mockCase(module, "SN-DUC-001", false, 15, 0.25, 5.0);

        // TH4: Tram sac CHAM dang sac 6 TIENG (SC-DAL-001) - Khong bi phat
        mockCase(module, "SC-DAL-001", false, 360, 6.0, 250.0);

        // TH5: Tram QUA HAN BAO TRI 100% (SS-BAO-001) - Ngung hoat dong
        mockCase(module, "SS-BAO-001", true, 0, 0.0, 500.0);

        // TH6: Tram SAP QUA HAN (SS-DAL-002) - Can than
        mockCase(module, "SS-DAL-002", false, 12, 0.2, 10.0);

        // TH7: Tram moi tinh, dang de trong (SC-BAO-001) - On dinh
        mockCase(module, "SC-BAO-001", true, 0, 0.0, 0.0);

        // TH8: Tram dang sac sieu nhanh (SS-DIL-001) - Hieu qua cao
        mockCase(module, "SS-DIL-001", false, 5, 0.08, 88.0);

        // TH9: Tram nhanh sac vua dung thoi gian tieu chuan (SN-DIL-001)
        mockCase(module, "SN-DIL-001", false, 56, 0.93, 45.0);

        // TH10: Tram can bao tri 95% (SN-BAO-001)
        mockCase(module, "SN-BAO-001", true, 0, 0.0, 475.0);

        // TH11: Tram Lam Ha dang sac lau (SN-LAM-001)
        mockCase(module, "SN-LAM-001", false, 90, 1.5, 200.0);

        // TH12: Tram sieu nhanh Don Duong gan het han (SS-DON-001)
        mockCase(module, "SS-DON-001", true, 0, 0.0, 490.0);

        // TH13: Tram Dam Rong het han bao tri - Ngung hoat dong (SN-DAM-001)
        mockCase(module, "SN-DAM-001", true, 0, 0.0, 500.0);

        // TH14: Tram Bao Lam sac cham gan het han (SC-BAL-001)
        mockCase(module, "SC-BAL-001", true, 0, 0.0, 485.0);

    }

    private static void mockCase(Module module, String id, boolean ready, int minAgo, double usageHours, double totalHours) {
        QuanLyTramSac ql = new QuanLyTramSac();
        TramSac t = ql.timTramTheoId(id);
        if (t != null) {
            t.setSanSang(ready);
            //t.setThoiGianSuDung(usageHours);
            t.setThoiGianHoatDong(totalHours);
            if (!ready) {
                t.setThoiGianBatDauSac(LocalDateTime.now().minusMinutes(minAgo));
            }
        }
    }
}
