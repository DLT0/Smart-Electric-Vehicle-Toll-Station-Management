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
        // --- 1. DA LAT (10 tram - Du cac loai) ---
        module.themVaoDanhSach(HuyenLamDong.DA_LAT, 7.2);
        module.themVaoDanhSach(HuyenLamDong.DA_LAT, 11);
        module.themVaoDanhSach(HuyenLamDong.DA_LAT, 22.5);
        module.themVaoDanhSach(HuyenLamDong.DA_LAT, 30);
        module.themVaoDanhSach(HuyenLamDong.DA_LAT, 60);
        module.themVaoDanhSach(HuyenLamDong.DA_LAT, 120);
        module.themVaoDanhSach(HuyenLamDong.DA_LAT, 150);
        module.themVaoDanhSach(HuyenLamDong.DA_LAT, 180);
        module.themVaoDanhSach(HuyenLamDong.DA_LAT, 250);
        module.themVaoDanhSach(HuyenLamDong.DA_LAT, 300);

        // --- 2. BAO LOC (6 tram) ---
        module.themVaoDanhSach(HuyenLamDong.BAO_LOC, 11);
        module.themVaoDanhSach(HuyenLamDong.BAO_LOC, 30);
        module.themVaoDanhSach(HuyenLamDong.BAO_LOC, 60);
        module.themVaoDanhSach(HuyenLamDong.BAO_LOC, 120);
        module.themVaoDanhSach(HuyenLamDong.BAO_LOC, 200);
        module.themVaoDanhSach(HuyenLamDong.BAO_LOC, 300);

        // --- 3. DUC TRONG (6 tram) ---
        module.themVaoDanhSach(HuyenLamDong.DUC_TRONG, 7.2);
        module.themVaoDanhSach(HuyenLamDong.DUC_TRONG, 30);
        module.themVaoDanhSach(HuyenLamDong.DUC_TRONG, 60);
        module.themVaoDanhSach(HuyenLamDong.DUC_TRONG, 120);
        module.themVaoDanhSach(HuyenLamDong.DUC_TRONG, 150);
        module.themVaoDanhSach(HuyenLamDong.DUC_TRONG, 250);

        // --- 4. DI LINH (4 tram) ---
        module.themVaoDanhSach(HuyenLamDong.DI_LINH, 11);
        module.themVaoDanhSach(HuyenLamDong.DI_LINH, 60);
        module.themVaoDanhSach(HuyenLamDong.DI_LINH, 150);
        module.themVaoDanhSach(HuyenLamDong.DI_LINH, 200);

        // --- 5. LAM HA (3 tram) ---
        module.themVaoDanhSach(HuyenLamDong.LAM_HA, 11);
        module.themVaoDanhSach(HuyenLamDong.LAM_HA, 120);
        module.themVaoDanhSach(HuyenLamDong.LAM_HA, 250);

        // --- 6. DON DUONG (3 tram) ---
        module.themVaoDanhSach(HuyenLamDong.DON_DUONG, 7.2);
        module.themVaoDanhSach(HuyenLamDong.DON_DUONG, 60);
        module.themVaoDanhSach(HuyenLamDong.DON_DUONG, 120);

        // --- 7. BAO LAM, DA HUOAI, DAM RONG (Moi huyen 1-2 tram) ---
        module.themVaoDanhSach(HuyenLamDong.BAO_LAM, 150);
        module.themVaoDanhSach(HuyenLamDong.DA_HUOAI, 60);
        module.themVaoDanhSach(HuyenLamDong.DAM_RONG, 30);

        // --- GIA LAP TRANG THAI VA THOI GIAN ---
        // Lay nhieu tram de gia lap "Dang sac" voi cac tinh huong khac nhau:
        
        // 1. Dalat - Sieu nhanh (250kW) - Da sac 135p - Qua han nang
        mockCharging(module, "SS-DAL-003", 135); 
        
        // 2. Dalat - Nhanh (60kW) - Da sac 100p - Qua han
        mockCharging(module, "SN-DAL-003", 100);

        // 3. Duc Trong - Nhanh (30kW) - Da sac 45p - Dang trong thoi gian cho phep
        mockCharging(module, "SN-DUC-001", 45);

        // 4. Dalat - Cham (7.2kW) - Da sac 400p - Sac cham ko tinh qua han
        mockCharging(module, "SC-DAL-001", 400);

        // 5. Bao Loc - Sieu nhanh (300kW) - Da sac 15p - Moi bat dau
        mockCharging(module, "SS-BAO-002", 15);

        // 6. Di Linh - Sieu nhanh (200kW) - Da sac 80p - Sap hoac da qua han tuy muc pin
        mockCharging(module, "SS-DIL-002", 80);

        // 7. Lam Ha - Nhanh (120kW) - Da sac 60p - Dang sac
        mockCharging(module, "SN-LAM-001", 60);

        // 8. Don Duong - Cham (7.2kW) - Da sac 200p
        mockCharging(module, "SC-DON-001", 200);

        // Gia lap thoi gian van hanh de canh bao bao tri
        TramSac b1 = module.timTramTheoId("SN-BAO-001");
        if (b1 != null)
            b1.setThoiGianHoatDong(455.5);

        TramSac b2 = module.timTramTheoId("SS-DIL-001");
        if (b2 != null)
            b2.setThoiGianHoatDong(510.0);

        TramSac b3 = module.timTramTheoId("SC-DAL-002");
        if (b3 != null)
            b3.setThoiGianHoatDong(12.0);
    }

    /**
     * Helper de gia lap mot tram dang sac
     */
    private static void mockCharging(Module module, String id, int minutesAgo) {
        TramSac t = module.timTramTheoId(id);
        if (t != null) {
            t.setSanSang(false);
            t.setThoiGianBatDauSac(LocalDateTime.now().minusMinutes(minutesAgo));
        }
    }
}