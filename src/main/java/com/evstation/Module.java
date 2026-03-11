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
    // * Khai bao cac truong du lieu (the private) - Rang buoc du lieu
    private String _maTram; // Ma dinh danh duy nhat cho tram sac
    private String _tenTram; // Ten = Loai + ViTri + STT
    private HuyenLamDong _viTri; // Vi tri duoc chon tu Enum (type-safe)
    private boolean _trangThai; // true = San sang | false = Dang su dung
    private double _congSuat; // Cong suat sac (don vi: kW), phai > 0
    private double _thoiGianHoatDong; // Tong so gio tich luy da van hanh, phai >= 0
    protected static final double GIA_MOI_KWH = 3850; // Don gia co dinh (VND/kWh)
    private int _sttHeThong; // STT khi them vao he thong (de sap xep fallback)

    // * Dong goi thuoc tinh (the public) - Getter & Setter

    // #region [TODO] Ham tu dong sinh maTram - se trien khai sau
    // Phuong thuc (con ham) tao lop: phat sinh ma tram tu dong
    // Vi du: "SC" + viTri.name() + "-" + sttHeThong -> "SC-DALAT-001"
    // o day du lieu lon den phan thuong vao an lap
    // #endregion

    // maTram: READ-ONLY - chi co getter, khong cho phep cap nhat tu ben ngoai
    public String getMaTram() {
        return _maTram;
    }

    // setMaTram la private: chi constructor trong noi bo duoc goi
    private void setMaTram(String value) {
        if (value == null || value.trim().isEmpty())
            value = "UNKNOWN"; // Rang buoc: ma tram khong duoc de trong
        _maTram = value.trim();
    }

    public String getTenTram() {
        return _tenTram;
    }

    public void setTenTram(String value) {
        if (value == null || value.trim().isEmpty())
            value = "Chua dat ten";
        _tenTram = value.trim();
    }

    public HuyenLamDong getViTri() {
        return _viTri;
    }

    public void setViTri(HuyenLamDong value) {
        if (value == null)
            return; // Rang buoc: vi tri khong duoc null
        _viTri = value;
    }

    public boolean getTrangThai() {
        return _trangThai;
    }

    public void setTrangThai(boolean value) {
        _trangThai = value; // Boolean khong can rang buoc them
    }

    public double getCongSuat() {
        return _congSuat;
    }

    public void setCongSuat(double value) {
        if (value <= 0)
            value = 0; // Rang buoc: cong suat phai la so duong
        else if (value > 300)
            value = 300; // Rang buoc: gioi han toi da 300kW
        _congSuat = value;
    }

    public double getThoiGianHoatDong() {
        return _thoiGianHoatDong;
    }

    public void setThoiGianHoatDong(double value) {
        if (value < 0)
            value = 0; // Rang buoc: thoi gian khong duoc am
        _thoiGianHoatDong = value;
    }

    public int getSttHeThong() {
        return _sttHeThong;
    }

    public void setSttHeThong(int value) {
        if (value < 1)
            value = 1;
        _sttHeThong = value;
    }

    // Constructor: khoi tao thong qua cac setter de dam bao rang buoc du lieu
    public TramSac(String maTram, HuyenLamDong viTri, double congSuat, int sttHeThong) {
        setMaTram(maTram);
        setViTri(viTri);
        setCongSuat(congSuat);
        setThoiGianHoatDong(0.0); // Tu dong khoi tao la 0
        setTrangThai(true); // Mac dinh: San sang / Trong
        setSttHeThong(sttHeThong);
    }

    public double tinhChiPhi(double soGio) {
        return this._congSuat * GIA_MOI_KWH * soGio;
    }

    public abstract void hienThiChiTiet();
}

// ============================================================
// LOP CON: TramSacCham (7kW - 11kW)
// ============================================================
class TramSacCham extends TramSac {
    public TramSacCham(String maTram, HuyenLamDong viTri, double congSuat, int stt, int sttHeThong) {
        super(maTram, viTri, congSuat, sttHeThong);
        setTenTram("Tram Sac Cham " + viTri.getTen() + " " + stt);
    }

    @Override
    public void hienThiChiTiet() {
        String chiPhiStr = String.format("%,7.0f VND/h", tinhChiPhi(1));
        System.out.printf("| %-18s | %-6s | %-30s | %5.1f kW | %10.1f h | %-14s | %-15s |%n",
                "[Sac Cham]", getMaTram(), getTenTram(), getCongSuat(), getThoiGianHoatDong(),
                getTrangThai() ? "San sang" : "Dang sac", chiPhiStr);
    }
}

// ============================================================
// LOP CON: TramSacNhanh (12kW - 120kW)
// ============================================================
class TramSacNhanh extends TramSac {
    public TramSacNhanh(String maTram, HuyenLamDong viTri, double congSuat, int stt, int sttHeThong) {
        super(maTram, viTri, congSuat, sttHeThong);
        setTenTram("Tram Sac Nhanh " + viTri.getTen() + " " + stt);
    }

    @Override
    public void hienThiChiTiet() {
        String chiPhiStr = String.format("%,7.0f VND/h", tinhChiPhi(1));
        System.out.printf("| %-18s | %-6s | %-30s | %5.1f kW | %10.1f h | %-14s | %-15s |%n",
                "[Sac Nhanh]", getMaTram(), getTenTram(), getCongSuat(), getThoiGianHoatDong(),
                getTrangThai() ? "San sang" : "Dang sac", chiPhiStr);
    }
}

// ============================================================
// LOP CON: TramSacSieuNhanh (121kW - 300kW)
// ============================================================
class TramSacSieuNhanh extends TramSac {
    public TramSacSieuNhanh(String maTram, HuyenLamDong viTri, double congSuat, int stt, int sttHeThong) {
        super(maTram, viTri, congSuat, sttHeThong);
        setTenTram("Tram Sac Sieu Nhanh " + viTri.getTen() + " " + stt);
    }

    @Override
    public void hienThiChiTiet() {
        String chiPhiStr = String.format("%,7.0f VND/h", tinhChiPhi(1));
        System.out.printf("| %-18s | %-6s | %-30s | %5.1f kW | %10.1f h | %-14s | %-15s |%n",
                "[Sac Sieu Nhanh]", getMaTram(), getTenTram(), getCongSuat(), getThoiGianHoatDong(),
                getTrangThai() ? "San sang" : "Dang sac", chiPhiStr);
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
        String id;
        while (true) {
            System.out.print("Nhap ID tram: ");
            id = scanner.nextLine().trim();
            if (id.isEmpty()) {
                System.out.println("!!! ID khong duoc de trong!");
                continue;
            }
            boolean exists = false;
            for (TramSac t : danhSach) {
                if (t.getMaTram().equalsIgnoreCase(id)) {
                    exists = true;
                    break;
                }
            }
            if (exists) {
                System.out.println("!!! ID '" + id + "' da ton tai trong he thong! Vui long nhap ID khac.");
                continue;
            }
            break;
        }

        // B2: Chon khu vuc tu Enum
        System.out.println("\nChon khu vuc trong tinh Lam Dong:");
        HuyenLamDong.hienThiDanhSach();
        HuyenLamDong khuVuc = null;
        while (khuVuc == null) {
            System.out.print("Nhap so thu tu (1 - " + HuyenLamDong.values().length + "): ");
            try {
                int soChon = Integer.parseInt(scanner.nextLine().trim());
                khuVuc = HuyenLamDong.layTheoSoThuTu(soChon);
                if (khuVuc == null) {
                    System.out.println("!!! So thu tu khong hop le!");
                }
            } catch (NumberFormatException e) {
                System.out.println("!!! Phai nhap mot so nguyen!");
            }
        }
        System.out.println("=> Da chon: " + khuVuc.getTen());

        // B3: Nhap cong suat
        double cs = 0;
        while (cs <= 0) {
            System.out.print("\nNhap cong suat kW (cs > 0): ");
            try {
                cs = Double.parseDouble(scanner.nextLine().trim());
                if (cs <= 0) {
                    System.out.println("!!! Cong suat phai la so duong!");
                }
            } catch (NumberFormatException e) {
                System.out.println("!!! Cong suat phai la mot so!");
            }
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

        System.out.println("=> Them thanh cong: " + danhSach.get(danhSach.size() - 1).getTenTram());
    }

    // Ham phu tro (dung cho Chuc nang 1): dem so tram cung khu vuc
    private int countStationsAtLocation(HuyenLamDong khuVuc) {
        int count = 0;
        for (TramSac t : danhSach) {
            // So sanh Enum bang == la chinh xac va nhanh hon .equals() voi String
            if (t.getViTri() == khuVuc)
                count++;
        }
        return count;
    }

    // ----------------------------------------------------------
    // Chuc nang 2: Them danh sach tru sac
    // ----------------------------------------------------------
    public void themDSTruSac(Scanner scanner) {
        System.out.println("\n--- THEM DANH SACH TRAM SAC ---");
        System.out.print("Nhap so luong tram sac can them: ");
        int n = Integer.parseInt(scanner.nextLine().trim());
        for (int i = 0; i < n; i++) {
            them1TruSac(scanner);
        }
    }

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

        // Gia lap thoi gian hoat dong cho mot so tram
        danhSach.get(0).setThoiGianHoatDong(120.5);
        danhSach.get(3).setThoiGianHoatDong(390.0); // Canh bao bao tri
        danhSach.get(4).setThoiGianHoatDong(450.0); // Qua han bao tri

        // Gia lap trang thai mot so tram de test sap xep
        danhSach.get(2).setTrangThai(false); // T003 - Dang sac
        danhSach.get(5).setTrangThai(false); // T006 - Dang sac
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
            int res = Boolean.compare(a.getTrangThai(), b.getTrangThai());
            if (res != 0)
                return res;

            res = Integer.compare(a.getViTri().ordinal(), b.getViTri().ordinal());
            if (res != 0)
                return res;

            return Integer.compare(a.getSttHeThong(), b.getSttHeThong());
        });

        System.out.println("\n" + "=".repeat(65) + " DANH SACH TRAM SAC " + "=".repeat(65));
        System.out.println(
                "+--------------------+--------+--------------------------------+-----------+------------+----------------+-----------------+");
        System.out.printf("| %-18s | %-6s | %-30s | %-9s | %-10s | %-14s | %-15s |%n",
                "Loai", "ID", "Ten Tram", "Cong Suat", "Van hanh", "Trang Thai", "Gia niem yet");
        System.out.println(
                "+--------------------+--------+--------------------------------+-----------+------------+----------------+-----------------+");
        for (TramSac t : sortedList) {
            t.hienThiChiTiet();
        }
        System.out.println(
                "+--------------------+--------+--------------------------------+-----------+------------+----------------+-----------------+");
    }

    // ----------------------------------------------------------
    // Chuc nang 5: Cap nhat trang thai
    // ----------------------------------------------------------
    public void capNhatTrangThai(Scanner scanner) {
        System.out.println("\n--- CAP NHAT TRANG THAI TRAM SAC ---");

        // 1. Nhap ma tram
        System.out.print("Nhap ID tram can cap nhat: ");
        String id = scanner.nextLine().trim();

        TramSac found = null;
        for (TramSac t : danhSach) {
            if (t.getMaTram().equalsIgnoreCase(id)) {
                found = t;
                break;
            }
        }

        if (found == null) {
            System.out.println("!!! Khong tim thay tram voi ID '" + id + "'");
            return;
        }

        // Hien thi trang thai hien tai
        System.out.println("Thong tin hien tai cua tram:");
        found.hienThiChiTiet();

        System.out.println("\nChon thong tin can cap nhat:");
        System.out.println("1. Trang thai (San sang/Dang sac)");
        System.out.println("2. Thoi gian hoat dong (h)");
        System.out.print("Nhap lua chon (1-2): ");

        int loai;
        try {
            loai = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("!!! Phai nhap so 1 hoac 2!");
            return;
        }

        if (loai == 1) {
            System.out.println("\nChon trang thai moi:");
            System.out.println("1. San sang (Trong)");
            System.out.println("2. Dang sac (Hoat dong)");
            System.out.print("Nhap lua chon (1-2): ");
            int chon;
            try {
                chon = Integer.parseInt(scanner.nextLine().trim());
                boolean newStatus = (chon == 1);
                if (chon != 1 && chon != 2) {
                    System.out.println("!!! Lua chon khong hop le!");
                    return;
                }
                System.out.print("Xac nhan thay doi trang thai? (y/n): ");
                if (scanner.nextLine().trim().toLowerCase().equals("y")) {
                    found.setTrangThai(newStatus);
                    System.out.println("==> Cap nhat trang thai thanh cong!");
                }
            } catch (NumberFormatException e) {
                System.out.println("!!! Phai nhap so 1 hoac 2!");
                return;
            }
        } else if (loai == 2) {
            System.out.print("\nNhap thoi gian hoat dong moi (h): ");
            try {
                double moi = Double.parseDouble(scanner.nextLine().trim());
                if (moi < 0) {
                    System.out.println("!!! Thoi gian hoat dong khong duoc am!");
                    return;
                }
                System.out.print("Xac nhan thay doi thoi gian hoat dong? (y/n): ");
                if (scanner.nextLine().trim().toLowerCase().equals("y")) {
                    found.setThoiGianHoatDong(moi);
                    System.out.println("==> Cap nhat thoi gian thanh cong!");
                }
            } catch (NumberFormatException e) {
                System.out.println("!!! Thoi gian phai la mot so!");
                return;
            }
        } else {
            System.out.println("!!! Lua chon khong hop le!");
        }

        xuatDanhSach();
    }

    // ----------------------------------------------------------
    // Chuc nang 6: Xoa tru sac
    // ----------------------------------------------------------
    public void xoaTruSac(Scanner scanner) {
        System.out.println("\n--- XOA TRAM SAC ---");

        if (danhSach.isEmpty()) {
            System.out.println("!!! Danh sach trong. Khong co tram nao de xoa.");
            return;
        }

        System.out.print("Nhap ID tram can xoa: ");
        String id = scanner.nextLine().trim();
        if (id.isEmpty()) {
            System.out.println("!!! ID khong duoc de trong.");
            return;
        }

        TramSac found = null;
        for (TramSac t : danhSach) {
            if (t.getMaTram().equalsIgnoreCase(id)) {
                found = t;
                break;
            }
        }

        if (found == null) {
            System.out.println("!!! Khong tim thay tram co ID: " + id);
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

    // ----------------------------------------------------------
    // Chuc nang 7: Tim kiem theo ID
    // ----------------------------------------------------------
    public void timKiem(Scanner scanner) {
        System.out.println("\n--- TIM KIEM TRAM THEO ID TRAM SAC ---");

        if (danhSach.isEmpty()) {
            System.out.println("!!! Danh sach trong. Khong co tram nao.");
            return;
        }

        System.out.print("Nhap ID tram can tim: ");
        String id = scanner.nextLine().trim();
        if (id.isEmpty()) {
            System.out.println("!!! ID khong duoc de trong.");
            return;
        }

        boolean foundAny = false;
        for (TramSac t : danhSach) {
            if (t.getMaTram().equalsIgnoreCase(id)) {
                t.hienThiChiTiet();
                foundAny = true;
            }
        }

        if (!foundAny) {
            System.out.println("!!! Khong tim thay tram co ID: " + id);
        }
    }

    // ----------------------------------------------------------
    // Chuc nang 8: Thong ke he thong tru sac
    // ----------------------------------------------------------
    /*
     * MỤC TIÊU: Cung cấp báo cáo tổng quan và chi tiết về hiệu suất vận hành.
     * 
     * 1. Quy trình thực hiện:
     * - Hiển thị Menu phụ: Cho phép chọn loại thống kê (Theo khu vực, Trạng thái,
     * hoặc Độ ưu tiên bảo trì).
     * - Xử lý dữ liệu: Lọc danh sách và sắp xếp (sort) theo tiêu chí người dùng đã
     * chọn.
     * - Hiển thị kết quả: Xuất bảng dữ liệu chi tiết kèm các chỉ số quan trọng.
     * 
     * 2. Các yêu cầu kỹ thuật:
     * - Quản lý vận hành: (Cần thêm) Thuộc tính gio_da_dung và han_bao_tri (mặc
     * định 400h) trong class TramSac.
     * - Logic bảo trì: Hàm kiểm tra tỷ lệ độ hao mòn (gio_da_dung / han_bao_tri).
     * Nếu > 90% thì báo động bảo trì.
     * - Báo cáo tổng hợp (Summary): Cuối bảng phải có phần kết luận:
     * + Tổng số trụ sạc, số trụ đang bận (Busy) vs. Rảnh (Ready).
     * + Số lượng trụ cần bảo trì gấp hoặc đang bị hỏng.
     * + Khu vực có tần suất sử dụng cao nhất.
     * 3. Nâng cao:
     * - Tìm hiểu thêm chức năng xóa các trạm sạc có số giờ sử dụng > han_bao_tri
     * - Tìm hiểu các viết hàm private, cụ thể là hàm tự động cập nhật giờ sử dụng
     * sau khi kết thúc quá trình sử dụng.
     */
    public void thongKeTruSac(Scanner scanner) {
        System.out.println("-> [Chuc nang 8] Thong ke.");
    }

    // ----------------------------------------------------------
    // Chuc nang 9: Xuat danh sach ra file Excel (chua trien khai)
    // ----------------------------------------------------------
    // public void xuatFile() { ... }
}
