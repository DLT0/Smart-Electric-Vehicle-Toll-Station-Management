package com.evstation;

import java.util.*;

// ============================================================
// ENUM: Danh sach cac don vi hanh chinh tinh Lam Dong (2026)
// Su dung Enum giup code an toan hon (type-safe) va de mo rong.
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
        System.out.println("  +-----+------------------------------+");
        System.out.printf("  | %-3s | %-28s |%n", "STT", "Ten Don Vi");
        System.out.println("  +-----+------------------------------+");
        for (int i = 0; i < dsKhuVuc.length; i++) {
            // i+1 de hien thi STT bat dau tu 1 thay vi 0
            System.out.printf("  | %-3d | %-28s |%n", i + 1, dsKhuVuc[i].getTen());
        }
        System.out.println("  +-----+------------------------------+");
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

    /**
     * Ham khoi tao (Constructor) cua lop TramSac.
     *
     * - public: Cho phep cac lop con (TramSacCham, ...) goi qua 'super(...)'.
     * - trangThai = true: Mac dinh moi tram khi tao ra deu o trang thai "San sang".
     * - viTri kieu HuyenLamDong (Enum): dam bao nguoi dung chi co the chon
     * nhung gia tri hop le da duoc dinh nghia san.
     */
    public TramSac(String maTram, HuyenLamDong viTri, double congSuat) {
        this.maTram = maTram;
        this.viTri = viTri; // Luu Enum, khong phai String
        this.congSuat = congSuat;
        this.trangThai = true; // Mac dinh: San sang / Trong
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
    public TramSacCham(String maTram, HuyenLamDong viTri, double congSuat, int stt) {
        super(maTram, viTri, congSuat);
        // Lay ten hien thi tu Enum qua getTen()
        this.tenTram = "Tram Sac Cham " + viTri.getTen() + " " + stt;
    }

    @Override
    public void hienThiChiTiet() {
        System.out.printf("[Sac Cham]       ID: %-6s | Ten: %-38s | CS: %5.1f kW | %-12s | Chi phi: %,9.0f VND/gio%n",
                maTram, tenTram, congSuat,
                trangThai ? "San sang" : "Dang sac",
                tinhChiPhi(1));
    }
}

// ============================================================
// LOP CON: TramSacNhanh (12kW - 120kW)
// ============================================================
class TramSacNhanh extends TramSac {
    public TramSacNhanh(String maTram, HuyenLamDong viTri, double congSuat, int stt) {
        super(maTram, viTri, congSuat);
        this.tenTram = "Tram Sac Nhanh " + viTri.getTen() + " " + stt;
    }

    @Override
    public void hienThiChiTiet() {
        System.out.printf("[Sac Nhanh]      ID: %-6s | Ten: %-38s | CS: %5.1f kW | %-12s | Chi phi: %,9.0f VND/gio%n",
                maTram, tenTram, congSuat,
                trangThai ? "San sang" : "Dang sac",
                tinhChiPhi(1));
    }
}

// ============================================================
// LOP CON: TramSacSieuNhanh (121kW - 300kW)
// ============================================================
class TramSacSieuNhanh extends TramSac {
    public TramSacSieuNhanh(String maTram, HuyenLamDong viTri, double congSuat, int stt) {
        super(maTram, viTri, congSuat);
        this.tenTram = "Tram Sac Sieu Nhanh " + viTri.getTen() + " " + stt;
    }

    @Override
    public void hienThiChiTiet() {
        System.out.printf("[Sac Sieu Nhanh] ID: %-6s | Ten: %-38s | CS: %5.1f kW | %-12s | Chi phi: %,9.0f VND/gio%n",
                maTram, tenTram, congSuat,
                trangThai ? "San sang" : "Dang sac",
                tinhChiPhi(1));
    }
}

// ============================================================
// LOP QUAN LY: Module
// ============================================================
public class Module {
    private List<TramSac> danhSach = new ArrayList<>();

    // ----------------------------------------------------------
    // Chuc nang 1: Them tru sac moi
    // ----------------------------------------------------------
    public void themTruSac(Scanner scanner) {
        System.out.println("\n--- THEM TRU SAC MOI (LAM DONG) ---");

        // B1: Nhap ma tram
        System.out.print("Nhap ID tram: ");
        String id = scanner.nextLine().trim();
        if (id.isEmpty()) {
            System.out.println("=> Loi: ID khong duoc de trong!");
            return;
        }
        // Kiem tra ID trung lap trong danh sach
        for (TramSac t : danhSach) {
            if (t.maTram.equalsIgnoreCase(id)) {
                System.out.println("=> Loi: ID '" + id + "' da ton tai trong he thong!");
                return;
            }
        }

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

        if (cs <= 11) {
            danhSach.add(new TramSacCham(id, khuVuc, cs, stt));
            System.out.println("=> Phan loai: [Sac Cham] (7kW - 11kW)");
        } else if (cs <= 120) {
            danhSach.add(new TramSacNhanh(id, khuVuc, cs, stt));
            System.out.println("=> Phan loai: [Sac Nhanh] (12kW - 120kW)");
        } else {
            danhSach.add(new TramSacSieuNhanh(id, khuVuc, cs, stt));
            System.out.println("=> Phan loai: [Sac Sieu Nhanh] (121kW - 300kW)");
        }

        System.out.println("=> Them thanh cong: " + danhSach.get(danhSach.size() - 1).tenTram);
    }

    // Ham phu tro: dem so tram cung khu vuc (dung Enum so sanh truc tiep bang ==)
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
    // Chuc nang 2: Xem danh sach tru sac
    // ----------------------------------------------------------
    public void xuatDanhSach() {
        if (danhSach.isEmpty()) {
            System.out.println("Danh sach trong! Chua co tram sac nao duoc them.");
            return;
        }
        System.out.println("\n====== DANH SACH TRAM SAC TINH LAM DONG (" + danhSach.size() + " tram) ======");
        for (TramSac t : danhSach) {
            t.hienThiChiTiet();
        }
        System.out.println("==================================================================================");
    }

    // ----------------------------------------------------------
    // Chuc nang 3: Cap nhat trang thai
    // ----------------------------------------------------------
    public void capNhatTrangThai(Scanner scanner) {
        System.out.println("-> [Chuc nang 3] Cap nhat trang thai.");
    }

    // ----------------------------------------------------------
    // Chuc nang 4: Xoa tru sac
    // ----------------------------------------------------------
    public void xoaTruSac(Scanner scanner) {
        System.out.println("\n--- XOA TRAm sac ---");

        if (danhSach.isEmpty()) {
            System.out.println("=> Danh sach trong. Khong co tram nao de xoa.");
            return;
        }

        System.out.print("Nhap ID tram can xoa: ");
        String id = scanner.nextLine().trim();
        if (id.isEmpty()) {
            System.out.println("=> Loi: ID khong duoc de trong.");
            return;
        }

        TramSac found = null;
        for (TramSac t : danhSach) {
            if (t.maTram.equalsIgnoreCase(id)) {
                found = t;
                break;
            }
        }

        if (found == null) {
            System.out.println("=> Khong tim thay tram co ID: " + id);
            return;
        }

        System.out.println("Thong tin tram tim thay:");
        found.hienThiChiTiet();

        System.out.print("Xac nhan xoa tram nay ? (Y/N): ");
        String ans = scanner.nextLine().trim();
        if (ans.equalsIgnoreCase("Y") || ans.equalsIgnoreCase("YES")) {
            danhSach.remove(found);
            System.out.println("=> Da xoa thanh cong tram ID: " + id);
        } else {
            System.out.println("=> Huy thao tac xoa.");
        }
    }
     // nhap ID tram sac can xoa 
       
    // ----------------------------------------------------------
    // Chuc nang 5: Tim kiem theo ID
    // ----------------------------------------------------------
    public void timKiem(Scanner scanner) {
        System.out.println("\n--- TIM KIEM TRAM THEO ID Tram SAC ---");

        if (danhSach.isEmpty()) {
            System.out.println("=> Danh sach trong. Khong co tram nao.");
            return;
        }

        System.out.print("Nhap ID tram can tim: ");
        String id = scanner.nextLine().trim();
        if (id.isEmpty()) {
            System.out.println("=> Loi: ID khong duoc de trong.");
            return;
        }

        boolean foundAny = false;
        for (TramSac t : danhSach) {
            if (t.maTram.equalsIgnoreCase(id)) {
                t.hienThiChiTiet();
                foundAny = true;
            }
        }

        if (!foundAny) {
            System.out.println("=> Khong tim thay tram co ID: " + id);
        }
    }
}
