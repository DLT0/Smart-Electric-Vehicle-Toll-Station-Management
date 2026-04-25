package com.evstation;

enum HuyenLamDong {
    DA_LAT("Da Lat"),
    BAO_LOC("Bao Loc"),
    DUC_TRONG("Duc Trong"),
    DI_LINH("Di Linh"),
    LAM_HA("Lam Ha"),
    DON_DUONG("Don Duong"),
    BAO_LAM("Bao Lam"),
    DA_HUOAI("Da Huoai"),
    DAM_RONG("Dam Rong");

    private final String tenTiengViet;

    HuyenLamDong(String tenTiengViet) {
        this.tenTiengViet = tenTiengViet;
    }

    public String getTen() {
        return tenTiengViet;
    }

    public static void hienThiDanhSach() {
        HuyenLamDong[] dsKhuVuc = HuyenLamDong.values();
        String line = "  +" + "-".repeat(5) + "+" + "-".repeat(25) + "+";
        System.out.println(line);
        System.out.printf("  | %-3s | %-23s |%n", "STT", "Ten Don Vi");
        System.out.println(line);
        for (int i = 0; i < dsKhuVuc.length; i++) {
            System.out.printf("  | %-3d | %-23s |%n", i + 1, dsKhuVuc[i].getTen());
        }
        System.out.println(line);
    }

    public static HuyenLamDong layTheoSoThuTu(int soThuTu) {
        HuyenLamDong[] dsKhuVuc = HuyenLamDong.values();
        if (soThuTu < 1 || soThuTu > dsKhuVuc.length) {
            return null;
        }
        return dsKhuVuc[soThuTu - 1];
    }
}
