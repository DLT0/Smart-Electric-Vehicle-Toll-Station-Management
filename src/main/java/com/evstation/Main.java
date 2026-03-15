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
        // Lay mot vai tram de gia lap "Dang sac"
        TramSac t1 = module.timTramTheoId("SS-DAL-003"); // Da Lat - Sieu nhanh
        if (t1 != null) {
            t1.setSanSang(false);
            t1.setThoiGianBatDauSac(LocalDateTime.now().minusMinutes(135)); // Sac duoc 2h15p
        }

        TramSac t2 = module.timTramTheoId("SN-DUC-001"); // Duc Trong - Nhanh
        if (t2 != null) {
            t2.setSanSang(false);
            t2.setThoiGianBatDauSac(LocalDateTime.now().minusMinutes(45)); // Sac duoc 45p
        }

        TramSac t3 = module.timTramTheoId("SC-DAL-001"); // Da Lat - Cham
        if (t3 != null) {
            t3.setSanSang(false);
            t3.setThoiGianBatDauSac(LocalDateTime.now().minusMinutes(300));
        }

        // Gia lap thoi gian van hanh de canh bao bao tri
        TramSac b1 = module.timTramTheoId("SN-BAO-001");
        if (b1 != null) b1.setThoiGianHoatDong(455.5); 

        TramSac b2 = module.timTramTheoId("SS-DIL-001");
        if (b2 != null) b2.setThoiGianHoatDong(510.0); 

        TramSac b3 = module.timTramTheoId("SC-DAL-002");
        if (b3 != null) b3.setThoiGianHoatDong(12.0);
        
        System.out.println(">>> [He thong] Da nap " + 34 + " tru sac mockup thanh cong!");
    }
}