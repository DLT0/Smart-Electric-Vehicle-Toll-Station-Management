package com.evstation;

import java.util.*;

// ============================================================
// ENUM: Danh sach cac don vi hanh chinh tinh Lam Dong 2026
// ============================================================
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

    // Constructor cua Enum: chay 1 lan khi JVM load class nay
    HuyenLamDong(String ten) {
        this.tenTiengViet = ten;
    }

    // Getter de lay ten hien thi
    public String getTen() {
        return tenTiengViet;
    }

    /**
     * Hien thi danh sach khu vuc dang bang so thu tu.
     * Dung values() de lap qua tat ca phan tu cua Enum tu dong.
     */
    public static void hienThiDanhSach() {
        HuyenLamDong[] dsKhuVuc = HuyenLamDong.values();
        System.out.println("  +-----+-------------------------+");
        System.out.printf("  | %-3s | %-23s |%n", "STT", "Ten Don Vi");
        System.out.println("  +-----+-------------------------+");
        for (int i = 0; i < dsKhuVuc.length; i++) {
            System.out.printf("  | %-3d | %-23s |%n", i + 1, dsKhuVuc[i].getTen());
        }
        System.out.println("  +-----+-------------------------+");
    }

    /**
     * Lay Enum tuong ung theo so thu tu nguoi dung chon (bat dau tu 1).
     * Tra ve null neu so thu tu khong hop le.
     */
    public static HuyenLamDong layTheoSoThuTu(int soThuTu) {
        HuyenLamDong[] dsKhuVuc = HuyenLamDong.values();
        // soThuTu hop le la tu 1 den tong so phan tu
        if (soThuTu < 1 || soThuTu > dsKhuVuc.length)
            return null;
        // Mang bat dau tu 0, nen can tru 1
        return dsKhuVuc[soThuTu - 1];
    }
}

// ============================================================
// LOP TRUU TUONG: TramSac
// ============================================================
abstract class TramSac {
    protected String maTram; // Ma dinh danh duy nhat cho tram sac
    protected String tenTram; // Ten = Loai + ViTri + STT (VD: Tram Sac Nhanh Thanh pho Da Lat 1)
    protected HuyenLamDong viTri; // Vi tri duoc chon tu Enum (type-safe, khong the nhap sai)
    protected boolean trangThai; // true = San sang | false = Dang su dung
    protected double congSuat; // Cong suat sac (don vi: kW)
    protected static final double GIA_MOI_KWH = 3850; // Don gia co dinh (VND/kWh)
    protected int sttHeThong; // STT khi them vao he thong (de sap xep fallback)

    public TramSac(String maTram, HuyenLamDong viTri, double congSuat, int sttHeThong) {
        this.maTram = maTram;
        this.viTri = viTri; // Luu Enum, khong phai String
        this.congSuat = congSuat;
        this.trangThai = true; // Mac dinh: San sang / Trong
        this.sttHeThong = sttHeThong;
    }

    public double tinhChiPhi(double soGio) {
        return this.congSuat * GIA_MOI_KWH * soGio;
    }

    public abstract void hienThiChiTiet();
}

// ============================================================
// LOP CON: TramSacCham (7kW - 11kW)
// ============================================================
class TramSacCham extends TramSac {
    public TramSacCham(String maTram, HuyenLamDong viTri, double congSuat, int stt, int sttHeThong) {
        super(maTram, viTri, congSuat, sttHeThong);
        // Lay ten hien thi tu Enum qua getTen()
        this.tenTram = "Tram Sac Cham " + viTri.getTen() + " " + stt;
    }

    @Override
    public void hienThiChiTiet() {
        String chiPhiStr = String.format("Chi phi: %,9.0f VND/h", tinhChiPhi(1));
        System.out.printf("| %-18s | %-6s | %-40s | %5.1f kW   | %-14s | %-25s |%n",
                "[Sac Cham]", maTram, tenTram, congSuat,
                trangThai ? "San sang" : "Dang hoat dong",
                chiPhiStr);
    }
}

// ============================================================
// LOP CON: TramSacNhanh (12kW - 120kW)
// ============================================================
class TramSacNhanh extends TramSac {
    public TramSacNhanh(String maTram, HuyenLamDong viTri, double congSuat, int stt, int sttHeThong) {
        super(maTram, viTri, congSuat, sttHeThong);
        this.tenTram = "Tram Sac Nhanh " + viTri.getTen() + " " + stt;
    }

    @Override
    public void hienThiChiTiet() {
        String chiPhiStr = String.format("Chi phi: %,9.0f VND/h", tinhChiPhi(1));
        System.out.printf("| %-18s | %-6s | %-40s | %5.1f kW   | %-14s | %-25s |%n",
                "[Sac Nhanh]", maTram, tenTram, congSuat,
                trangThai ? "San sang" : "Dang hoat dong",
                chiPhiStr);
    }
}

// ============================================================
// LOP CON: TramSacSieuNhanh (121kW - 300kW)
// ============================================================
class TramSacSieuNhanh extends TramSac {
    public TramSacSieuNhanh(String maTram, HuyenLamDong viTri, double congSuat, int stt, int sttHeThong) {
        super(maTram, viTri, congSuat, sttHeThong);
        this.tenTram = "Tram Sac Sieu Nhanh " + viTri.getTen() + " " + stt;
    }

    @Override
    public void hienThiChiTiet() {
        String chiPhiStr = String.format("Chi phi: %,9.0f VND/h", tinhChiPhi(1));
        System.out.printf("| %-18s | %-6s | %-40s | %5.1f kW   | %-14s | %-25s |%n",
                "[Sac Sieu Nhanh]", maTram, tenTram, congSuat,
                trangThai ? "San sang" : "Dang hoat dong",
                chiPhiStr);
    }
}

// ============================================================
// LOP QUAN LY: Module
// ============================================================
public class Module {
    private List<TramSac> danhSach = new ArrayList<>();

    // Constructor: tu dong nap du lieu mau khi khoi tao
    public Module() {
        khoiTaoDuLieuMau();
    }

    // ----------------------------------------------------------
    // Chuc nang 1: Them 1 tru sac moi
    // ----------------------------------------------------------
    public void them1TruSac(Scanner scanner) {
        System.out.println("\n--- THEM TRU SAC MOI (LAM DONG) ---");

        // B1: Nhap ma tram
        System.out.print("Nhap ID tram: ");
        String id = scanner.nextLine().trim();
        if (id.isEmpty()) {
            System.out.println("=> ID khong duoc de trong!");
            return;
        }
        // Kiem tra ID trung lap trong danh sach
        /*
         * for (TramSac t : danhSach) {
         * if (t.maTram.equalsIgnoreCase(id)) {
         * System.out.println("=> ID '" + id + "' da ton tai trong he thong!");
         * return;
         * }
         * }
         */
        // B2: Chon khu vuc tu Enum (khong nhap tay, chi chon so)
        System.out.println("\nChon khu vuc trong tinh Lam Dong:");
        HuyenLamDong.hienThiDanhSach();
        System.out.print("Nhap so thu tu (1 - " + HuyenLamDong.values().length + "): ");
        int soChon;
        try {
            soChon = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("=> Loi: Phai nhap mot so nguyen!");
            return;
        }
        // layTheoSoThuTu tra ve null neu so khong hop le
        HuyenLamDong khuVuc = HuyenLamDong.layTheoSoThuTu(soChon);
        if (khuVuc == null) {
            System.out.println("=> Loi: So thu tu khong hop le!");
            return;
        }
        System.out.println("=> Da chon: " + khuVuc.getTen());

        // B3: Nhap cong suat
        System.out.print("\nNhap cong suat kW (7 <= cs <= 300): ");
        double cs;
        try {
            cs = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("=> Loi: Cong suat phai la mot so!");
            return;
        }
        if (cs < 7 || cs > 300) {
            System.out.println("=> Loi: Cong suat phai tu 7 den 300 kW!");
            return;
        }

        // B4: Tinh STT tai khu vuc do va them vao danh sach
        int stt = countStationsAtLocation(khuVuc) + 1;
        int sttHeThong = danhSach.size() + 1;

        if (cs <= 11) {
            danhSach.add(new TramSacCham(id, khuVuc, cs, stt, sttHeThong));
            System.out.println("=> Phan loai: [Sac Cham] (7kW - 11kW)");
        } else if (cs <= 120) {
            danhSach.add(new TramSacNhanh(id, khuVuc, cs, stt, sttHeThong));
            System.out.println("=> Phan loai: [Sac Nhanh] (12kW - 120kW)");
        } else {
            danhSach.add(new TramSacSieuNhanh(id, khuVuc, cs, stt, sttHeThong));
            System.out.println("=> Phan loai: [Sac Sieu Nhanh] (121kW - 300kW)");
        }

        System.out.println("=> Them thanh cong: " + danhSach.get(danhSach.size() - 1).tenTram);
    }

    // Ham phu tro (dung cho Chuc nang 1): dem so tram cung khu vuc
    private int countStationsAtLocation(HuyenLamDong khuVuc) {
        int count = 0;
        for (TramSac t : danhSach) {
            // So sanh Enum bang == la chinh xac va nhanh hon .equals() voi String
            if (t.viTri == khuVuc)
                count++;
        }
        return count;
    }

    // ----------------------------------------------------------
    // Chuc nang 2: Them danh sach tru sac (chua trien khai)
    // ----------------------------------------------------------
    // public void themDSTruSac(Scanner scanner) { ... }

    // ----------------------------------------------------------
    // Chuc nang 3: Tu dong nap du lieu mau vao he thong
    // ----------------------------------------------------------
    public void khoiTaoDuLieuMau() {
        // Xoa danh sach cu truoc khi nap lai de tranh trung lap
        danhSach.clear();

        // Them cac tram sac mau
        danhSach.add(new TramSacCham("T001", HuyenLamDong.DA_LAT, 7.2, 1, 1));
        danhSach.add(new TramSacCham("T002", HuyenLamDong.DA_LAT, 11, 2, 2));
        danhSach.add(new TramSacNhanh("T003", HuyenLamDong.DUC_TRONG, 30, 1, 3));
        danhSach.add(new TramSacNhanh("T004", HuyenLamDong.BAO_LOC, 60, 1, 4));
        danhSach.add(new TramSacSieuNhanh("T005", HuyenLamDong.DI_LINH, 150, 1, 5));
        danhSach.add(new TramSacSieuNhanh("T006", HuyenLamDong.DA_LAT, 250, 3, 6));

        // Gia lap trang thai mot so tram de test sap xep
        danhSach.get(2).trangThai = false; // T003 - Dang sac
        danhSach.get(5).trangThai = false; // T006 - Dang sac
    }

    // ----------------------------------------------------------
    // Chuc nang 4: Xem danh sach tru sac
    // ----------------------------------------------------------
    public void xuatDanhSach() {
        if (danhSach.isEmpty()) {
            System.out.println("Danh sach trong! Chua co tram sac nao duoc them.");
            return;
        }

        // Tao ban sao de sap xep ma khong anh huong den danh sach goc
        List<TramSac> sortedList = new ArrayList<>(danhSach);

        // Sap xep theo yeu cau:
        // 1. trangThai: Dang hoat dong (false) truoc, San sang (true) sau
        // 2. viTri: Theo thu tu Enum
        // 3. sttHeThong: Theo thu tu them vao
        Collections.sort(sortedList, (a, b) -> {
            // true (San sang) > false (Dang hoat dong)
            // Boolean.compare(false, true) tra ve -1, nghia la false dung truoc
            int res = Boolean.compare(a.trangThai, b.trangThai);
            if (res != 0)
                return res;

            res = Integer.compare(a.viTri.ordinal(), b.viTri.ordinal());
            if (res != 0)
                return res;

            return Integer.compare(a.sttHeThong, b.sttHeThong);
        });

        System.out.println("\n" + "=".repeat(56) + " DANH SACH TRAM SAC " + "=".repeat(56));
        System.out.println(
                "+--------------------+--------+------------------------------------------+------------+----------------+---------------------------+");
        System.out.printf("| %-18s | %-6s | %-40s | %-10s | %-14s | %-25s |%n",
                "Loai", "ID", "Ten Tram", "Cong Suat", "Trang Thai", "Gia niem yet");
        System.out.println(
                "+--------------------+--------+------------------------------------------+------------+----------------+---------------------------+");
        for (TramSac t : sortedList) {
            t.hienThiChiTiet();
        }
        System.out.println(
                "+--------------------+--------+------------------------------------------+------------+----------------+---------------------------+");
    }

    // ----------------------------------------------------------
    // Chuc nang 5: Cap nhat trang thai
    // ----------------------------------------------------------
    public void capNhatTrangThai(Scanner scanner) {
        // Đã Phân Công Việc Cho Thành
        /*
         * System.out.println("\n--- CAP NHAT TRANG THAI TRAM SAC ---");
         * 
         * // 1. Nhap ma tram
         * System.out.print("Nhap ID tram can cap nhat: ");
         * String id = scanner.nextLine().trim();
         * 
         * TramSac found = null;
         * for (TramSac t : danhSach) {
         * if (t.maTram.equalsIgnoreCase(id)) {
         * found = t;
         * break;
         * }
         * }
         * 
         * if (found == null) {
         * System.out.println("=> Khong tim thay tram voi ID '" + id + "'");
         * return;
         * }
         * 
         * // Hien thi trang thai hien tai
         * System.out.print("Trang thai hien tai: ");
         * found.hienThiChiTiet();
         * 
         * // 2. Chon trang thai moi
         * System.out.println("\nChon trang thai moi:");
         * System.out.println("1. San sang (Trong)");
         * System.out.println("2. Dang sac (Hoat dong)");
         * System.out.print("Nhap lua chon (1-2): ");
         * 
         * int chon;
         * try {
         * chon = Integer.parseInt(scanner.nextLine().trim());
         * } catch (NumberFormatException e) {
         * System.out.println("=> Phai nhap so 1 hoac 2!");
         * return;
         * }
         * 
         * boolean newStatus;
         * if (chon == 1) {
         * newStatus = true;
         * } else if (chon == 2) {
         * newStatus = false;
         * } else {
         * System.out.println("=> Lua chon khong hop le!");
         * return;
         * }
         * 
         * // 3. Xac nhan
         * System.out.print("Xac nhan thay doi? (y/n): ");
         * String confirm = scanner.nextLine().trim().toLowerCase();
         * 
         * if (confirm.equals("y")) {
         * found.trangThai = newStatus;
         * // 4. Xuat thong bao
         * System.out.println("=> Cap nhat trang thai " + newStatus + " thanh cong!");
         * // 5. Xuat danh sach moi
         * xuatDanhSach();
         * } else {
         * System.out.println("=> Da huy thao tac.");
         * }
         */
    }

    // ----------------------------------------------------------
    // Chuc nang 6: Xoa tru sac (chua trien khai)
    // ----------------------------------------------------------
    public void xoaTruSac(Scanner scanner) {
        System.out.println("-> [Chuc nang 6] Xoa tru sac.");
        // Da Phan Cong Viec Cho Minh
    }

    // ----------------------------------------------------------
    // Chuc nang 7: Tim kiem theo ID (chua trien khai)
    // ----------------------------------------------------------
    public void timKiem(Scanner scanner) {
        System.out.println("-> [Chuc nang 7] Tim kiem.");
        // Da Phan Cong Viec Cho Minh
    }

    // ----------------------------------------------------------
    // Chuc nang 8: Thong ke tru sac can bao tri (chua trien khai)
    // ----------------------------------------------------------
    // public void thongKeTruSacCanBaoTri() { ... }

    // ----------------------------------------------------------
    // Chuc nang 9: Xuat danh sach ra file Excel (chua trien khai)
    // ----------------------------------------------------------
    // public void xuatFile() { ... }
}
