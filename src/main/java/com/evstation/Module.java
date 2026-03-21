package com.evstation;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
        System.out.printf("   | %-3s | %-23s |%n", "STT", "Ten Don Vi");
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
// ENUM: Danh sach cac lua chon thong ke tru sac
// ============================================================
enum LoaiThongKe {
    BAO_TRI("Tru sac can bao tri (haomon > 90%)"),
    GIO_SD_THAP("Tru sac co so gio su dung > X (nhap X tu ban phim)"),
    KHU_VUC_CAO("Thong ke khu vuc co tan xuat su dung cao nhat"),
    QUAY_LAI("Quay lai menu chinh");

    private final String moTa;

    LoaiThongKe(String moTa) {
        this.moTa = moTa;
    }

    public String getMoTa() {
        return moTa;
    }

    public static void hienThiMenu() {
        LoaiThongKe[] options = LoaiThongKe.values();
        System.out.println("\n  +-----+--------------------------------------------------+");
        System.out.printf("  | %-3s | %-48s |%n", "STT", "Loai Thong Ke");
        System.out.println("  +-----+--------------------------------------------------+");
        for (int i = 0; i < options.length; i++) {
            System.out.printf("  | %-3d | %-48s |%n", i + 1, options[i].getMoTa());
        }
        System.out.println("  +-----+--------------------------------------------------+");
    }

    public static LoaiThongKe layTheoSoThuTu(int soThuTu) {
        LoaiThongKe[] options = LoaiThongKe.values();
        if (soThuTu < 1 || soThuTu > options.length)
            return null;
        return options[soThuTu - 1];
    }
}

// ============================================================
// LOP QUAN LY: Module
// ============================================================
public class Module {
    private List<TramSac> danhSach = new ArrayList<>();
    // Dinh dang: [PREFIX]-[MA_KHU_VUC]-[STT]
    //
    // PREFIX xac dinh loai tram:
    // SC = Sac Cham (7kW - 11kW)
    // SN = Sac Nhanh (12kW - 120kW)
    // SS = Sac Sieu Nhanh(121kW- 300kW)
    //
    // MA_KHU_VUC: 3 ky tu dau cua Enum HuyenLamDong (viet hoa, bo dau gach duoi)
    // Vi du: DA_LAT -> DAL, DUC_TRONG -> DUC, BAO_LOC -> BAO
    //
    // STT: So thu tu tram cung LOAI tai cung KHU VUC (3 chu so, zero-padding)
    //
    // Vi du day du:
    // SC-DAL-001 -> Sac Cham, Da Lat, thu 1
    // SN-DUC-001 -> Sac Nhanh, Duc Trong,thu 1
    // SS-DIL-001 -> Sac Sieu Nhanh, Di Linh, thu 1
    // SC-DAL-002 -> Sac Cham, Da Lat, thu 2
    private static final String PREFIX_CHAM = "SC"; // Sac Cham (7kW - 11kW)
    private static final String PREFIX_NHANH = "SN"; // Sac Nhanh (12kW - 120kW)
    private static final String PREFIX_SIEU_NHANH = "SS"; // Sac Sieu Nhanh (121kW - 300kW)
    private static final String MA_TRAM_SEPARATOR = "-"; // Ky tu phan cach
    private static final int MA_KHU_VUC_LENGTH = 3; // Do dai ma khu vuc (lay 3 ky tu dau)
    private static final String MA_TRAM_STT_FORMAT = "%03d"; // Dinh dang STT: 3 chu so, zero-padding
    // #endregion NAMING_CONVENTION

    // Constructor: tu dong nap du lieu mau khi khoi tao
    public Module() {
        // Mock data now initialized from Main
    }

    // =========================================================================
    // CÁC HÀM CHỨC NĂNG CHÍNH (PUBLIC API)
    // =========================================================================

    // HÀM HỖ TRỢ DÙNG CHUNG: XÁC NHẬN YES/NO 
    // confirm(scanner, prompt): Chuẩn hóa toàn bộ câu hỏi Yes/No trong hệ thống.
    // - In ra: "<prompt> (y/n): "
    // - Chấp nhận: y, yes, n, no (không phân biệt hoa thường, bỏ khoảng trắng thừa)
    // - Nếu nhập sai, yêu cầu nhập lại cho đến khi hợp lệ.
    public static boolean confirm(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String ans = scanner.nextLine().trim().toLowerCase();

            if (ans.equals("y") || ans.equals("yes")) {
                return true;
            }
            if (ans.equals("n") || ans.equals("no")) {
                return false;
            }

            System.out.println("!!! Vui long chi nhap y/n hoac yes/no.");
        }
    }

    /**
     * Tinh khoang thoi gian (phut) giua 2 thoi diem batDau va ketThuc.
     * Dung cho chuc nang 10 (tinh chi phi) va giup tinh toan thoi gian da sac thuc
     * te.
     */
    public long tinhThoiGianSacPhut(LocalDateTime batDau, LocalDateTime ketThuc) {
        if (batDau == null || ketThuc == null)
            return 0;
        return Duration.between(batDau, ketThuc).toMinutes();
    }

    /**
     * Dinh dang thoi gian theo yeu cau: neu < 60 thi hien phut, >= 60 thi hien gio
     * va phut.
     */
    public String dinhDangThoiGian(long phut) {
        if (phut < 60) {
            return phut + " phut";
        } else {
            long gio = phut / 60;
            long phutDu = phut % 60;
            if (phutDu == 0)
                return gio + " gio";
            return gio + " gio " + phutDu + " phut";
        }
    }

    /**
     * Tra ve doi tuong TramSac dua vao ID. Giup giam trung lap logic tim kiem.
     */
    public TramSac timTramTheoId(String id) {
        if (id == null || id.trim().isEmpty())
            return null;

        // Loai bo ky tu phan cach (dau gach ngang hoac khoang trang) de nguoi dung tim
        // kiem linh hoat
        String cleanId = id.replaceAll("[- ]", "").toLowerCase();

        for (TramSac t : danhSach) {
            String cleanTramId = t.getMaTram().replaceAll("[- ]", "").toLowerCase();
            if (cleanTramId.equals(cleanId)) {
                return t;
            }
        }
        return null;
    }

    // =========================================================================
    // CÁC HÀM BỔ TRỢ NỘI BỘ (PRIVATE HELPERS)
    // =========================================================================

    // ----------------------------------------------------------
    // Ham phu tro: Tu dong sinh maTram dua theo quy uoc NAMING_CONVENTION
    // prefix duoc chon tu dong dua vao congSuat de phan biet loai tram
    // Tham so countAtLocation la so luong tram hien co tai khu vuc do
    // (duoc tinh mot lan o ham themVaoDanhSach de tranh lap lai duyet danh sach).
    // ----------------------------------------------------------
    private String sinhMaTram(HuyenLamDong khuVuc, double congSuat, int countAtLocation) {
        String prefix = (congSuat <= 11) ? PREFIX_CHAM
                : (congSuat <= 120) ? PREFIX_NHANH
                        : PREFIX_SIEU_NHANH;
        String tenEnum = khuVuc.name().replace("_", ""); // "DA_LAT" -> "DALAT"
        String maKhuVuc = tenEnum.substring(0, Math.min(MA_KHU_VUC_LENGTH, tenEnum.length())).toUpperCase();
        int sttTaiKhuVuc = countAtLocation + 1;
        String sttStr = String.format(MA_TRAM_STT_FORMAT, sttTaiKhuVuc);
        return prefix + MA_TRAM_SEPARATOR + maKhuVuc + MA_TRAM_SEPARATOR + sttStr;
    }

    // ----------------------------------------------------------
    // Ham phu tro: Hien bang khu vuc va cho nguoi dung chon
    // Sau khi chon: in xac nhan va separator de "dismiss" bang
    // ----------------------------------------------------------
    private HuyenLamDong chonKhuVuc(Scanner scanner) {
        HuyenLamDong.hienThiDanhSach();
        HuyenLamDong khuVuc = null;
        while (khuVuc == null) {
            System.out.print("Chon khu vuc (1-" + HuyenLamDong.values().length + "): ");
            try {
                int soChon = Integer.parseInt(scanner.nextLine().trim());
                khuVuc = HuyenLamDong.layTheoSoThuTu(soChon);
                if (khuVuc == null)
                    System.out.println("!!! So thu tu khong hop le!");
            } catch (NumberFormatException e) {
                System.out.println("!!! Phai nhap mot so nguyen!");
            }
        }
        // In xac nhan va xoa bang bang separator
        System.out.println("=> Khu vuc: " + khuVuc.getTen());

        return khuVuc;
    }

    // ----------------------------------------------------------
    // Chuc nang 1: Them 1 tru sac moi
    // ----------------------------------------------------------
    public void them1TruSac(Scanner scanner) {
        // B1: Chon khu vuc (bang hien thi -> chon -> tu dong dismiss)
        System.out.println("Chon khu vuc trong tinh Lam Dong:");
        HuyenLamDong khuVuc = chonKhuVuc(scanner);

        // B2: Nhap cong suat
        double cs = nhapCongSuat(scanner);

        // B3: Sinh ID tu dong va them vao danh sach
        themVaoDanhSach(khuVuc, cs);
    }

    // Ham phu tro: Nhap va kiem tra cong suat hop le
    private double nhapCongSuat(Scanner scanner) {
        double cs = 0;
        System.out.print("Nhap cong suat kW (7 <= cs <= 300): ");
        try {
            cs = Double.parseDouble(scanner.nextLine().trim());
            if (cs < 7) {
                System.out.println("!!! Cong suat toi thieu la 7kW!");
            } else if (cs > 300) {
                System.out.println("!!! Cong suat toi da la 300kW!");
            }
        } catch (NumberFormatException e) {
            System.out.println("!!! Cong suat phai la mot so!");
        }
    return cs;
    }

    // Ham phu tro (Public de tai su dung): Sinh ID, phan loai va them tram vao danh sach.
    // Toi uu: chi tinh so tram hien co tai khu vuc 1 lan, su dung chung cho ma tram va STT.
    public TramSac themVaoDanhSach(HuyenLamDong khuVuc, double cs) {
        int countAtLocation = countStationsAtLocation(khuVuc);
        String id = sinhMaTram(khuVuc, cs, countAtLocation);
        int stt = countAtLocation + 1;
        TramSac moi = null;
        if (cs <= 11) {
            moi = new TramSacCham(id, khuVuc, cs, stt);
            danhSach.add(moi);
            System.out.println("=> [" + id + "] Sac Cham (7-11kW) - " + khuVuc.getTen() + " da duoc them!");
        } else if (cs <= 120) {
            moi = new TramSacNhanh(id, khuVuc, cs, stt);
            danhSach.add(moi);
            System.out.println("=> [" + id + "] Sac Nhanh (12-120kW) - " + khuVuc.getTen() + " da duoc them!");
        } else {
            moi = new TramSacSieuNhanh(id, khuVuc, cs, stt);
            danhSach.add(moi);
            System.out.println("=> [" + id + "] Sac Sieu Nhanh (121-300kW) - " + khuVuc.getTen() + " da duoc them!");
        }
        return moi;
    }

    // Ham phu tro: Dem so tram cung khu vuc
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
        int n = 0;
        while (n <= 0) {
            System.out.print("Nhap so luong tram sac can them (> 0): ");
            try {
                n = Integer.parseInt(scanner.nextLine().trim());
                if (n <= 0)
                    System.out.println("!!! So luong phai la so nguyen duong!");
            } catch (NumberFormatException e) {
                System.out.println("!!! Phai nhap mot so nguyen!");
            }
        }

        final int tongSo = n;
        for (int i = 1; i <= tongSo; i++) {
            System.out.println("\n  [ Tram " + i + " / " + tongSo + " ]");
            System.out.println("  Chon khu vuc:");
            HuyenLamDong khuVuc = chonKhuVuc(scanner);
            double cs = nhapCongSuat(scanner);
            themVaoDanhSach(khuVuc, cs);
        }
        System.out.println("\n=> Da them xong " + tongSo + " tram sac!");
    }

    // ----------------------------------------------------------
    // Chuc nang 3: Nhap co dinh (Du lieu mau co san)
    // ----------------------------------------------------------
    public void nhapCoDinh(Scanner scanner) {
        if (!danhSach.isEmpty()) {
                boolean overwrite = confirm(scanner,
                    "He thong dang co " + danhSach.size() + " tram. Ghi de bang du lieu co dinh?");
            if (!overwrite) {
                System.out.println("=> Huy thao tac.");
                return;
            }
        }
        danhSach.clear();
        // 1. DA LAT
        themVaoDanhSach(HuyenLamDong.DA_LAT, 7.2);    // SC-DAL-001
        themVaoDanhSach(HuyenLamDong.DA_LAT, 60);      // SN-DAL-001
        themVaoDanhSach(HuyenLamDong.DA_LAT, 250);     // SS-DAL-001
        themVaoDanhSach(HuyenLamDong.DA_LAT, 300);     // SS-DAL-002

        // 2. BAO LOC
        themVaoDanhSach(HuyenLamDong.BAO_LOC, 11);     // SC-BAO-001
        themVaoDanhSach(HuyenLamDong.BAO_LOC, 120);    // SN-BAO-001
        themVaoDanhSach(HuyenLamDong.BAO_LOC, 300);    // SS-BAO-001

        // 3. DUC TRONG
        themVaoDanhSach(HuyenLamDong.DUC_TRONG, 30);   // SN-DUC-001
        themVaoDanhSach(HuyenLamDong.DUC_TRONG, 150);  // SS-DUC-001

        // 4. DI LINH
        themVaoDanhSach(HuyenLamDong.DI_LINH, 60);     // SN-DIL-001
        themVaoDanhSach(HuyenLamDong.DI_LINH, 200);    // SS-DIL-001

        // 5. CAC KHU VUC KHAC
        themVaoDanhSach(HuyenLamDong.LAM_HA, 30);      // SN-LAM-001
        themVaoDanhSach(HuyenLamDong.DON_DUONG, 150);  // SS-DON-001
        themVaoDanhSach(HuyenLamDong.BAO_LAM, 11);     // SC-BAL-001
        themVaoDanhSach(HuyenLamDong.DAM_RONG, 60);    // SN-DAM-001

        // --- AP DUNG TRANG THAI TEST ---
        apDungTrangThaiTest("SS-DAL-001", false, 120, 2.0, 480.0);
        apDungTrangThaiTest("SN-DAL-001", false, 58, 1.0, 120.5);
        apDungTrangThaiTest("SN-DUC-001", false, 15, 0.25, 5.0);
        apDungTrangThaiTest("SC-DAL-001", false, 360, 6.0, 250.0);
        apDungTrangThaiTest("SS-BAO-001", true, 0, 0.0, 500.0);
        apDungTrangThaiTest("SS-DAL-002", false, 12, 0.2, 10.0);
        apDungTrangThaiTest("SC-BAO-001", true, 0, 0.0, 0.0);
        apDungTrangThaiTest("SS-DIL-001", false, 5, 0.08, 88.0);
        apDungTrangThaiTest("SN-DIL-001", false, 56, 0.93, 45.0);
        apDungTrangThaiTest("SN-BAO-001", true, 0, 0.0, 475.0);
        apDungTrangThaiTest("SN-LAM-001", false, 90, 1.5, 200.0);
        apDungTrangThaiTest("SS-DON-001", true, 0, 0.0, 490.0);
        apDungTrangThaiTest("SN-DAM-001", true, 0, 0.0, 500.0);
        apDungTrangThaiTest("SC-BAL-001", true, 0, 0.0, 485.0);

        System.out.println("\n=> Da nap xong du lieu co dinh: " + danhSach.size() + " tram sac.");
    }

    // Ham phu tro: Ap dung trang thai test cho tram theo ID
    private void apDungTrangThaiTest(String id, boolean sanSang, int phutTruoc, double gioSuDung, double tongGio) {
        TramSac t = timTramTheoId(id);
        if (t != null) {
            t.setSanSang(sanSang);
            t.setThoiGianSuDung(gioSuDung);
            t.setThoiGianHoatDong(tongGio);
            if (!sanSang) {
                t.setThoiGianBatDauSac(LocalDateTime.now().minusMinutes(phutTruoc));
            }
        }
    }

    // ----------------------------------------------------------
    // Chuc nang 4: Xem danh sach tru sac
    // ----------------------------------------------------------

    // Ham bo tro 1: In duong gach ngang cho bang
    public static void inKeNgang(String kt, int n) {
        for (int i = 0; i < n; i++) {
            System.out.print(kt);
        }
        System.out.println();
    }

    // Ham bo tro 2: In tieu de cac cot cua bang
    private void inTieuDeBang() {
        inKeNgang("=", 160);
        System.out.printf("| %-16s | %-10s | %-40s | %-9s | %-8s | %-15s | %-16s | %-20s |%n",
            "Loai", "ID", "Ten Tram", "Cong Suat", "Hao Mon", "Luu y bao tri", "Trang Thai", "Thoi gian sac");
        inKeNgang("=", 160);
    }

    // Ham bo tro: Tinh thoi gian tieu chuan de sac 80% pin 70kWh (56kWh)
    private long tinhThoiGianTieuChuanPhut(TramSac t) {
        return (long) ((56.0 / t.getCongSuat()) * 60);
    }

    // Ham bo tro: Tao nhan phu neu thoi gian sac vuot nguong tieu chuan
    private String tinhPhuThoiGianQuaHan(TramSac t, long phutDaSac) {
        if (t instanceof TramSacCham) {
            return "";
        }

        long standardMin = tinhThoiGianTieuChuanPhut(t);
        if (phutDaSac > standardMin) {
            return " (+" + (phutDaSac - standardMin) + "p)";
        }
        return "";
    }

    // Ham bo tro 3: Xuat DS
    private void inDSTram(TramSac t) {
        long phut = 0;
        String extra = "";
        if (!t.isSanSang() && t.getThoiGianBatDauSac() != null) {
            phut = tinhThoiGianSacPhut(t.getThoiGianBatDauSac(), LocalDateTime.now());
            extra = tinhPhuThoiGianQuaHan(t, phut);
        }
        String thoiGianStr = (phut > 0) ? dinhDangThoiGian(phut) + extra : "-";
        
        // Tinh muc hao mon va trang thai bao tri
        double mucHaoMon = t.tinhMucHaoMon();
        String haoMonStr = String.format("%.1f%%", mucHaoMon);
        String luuYBaoTri = t.getTrangThaiBaoTri();

        System.out.printf("| %-16s | %-10s | %-40s | %6.1f kW | %-8s | %-15s | %-16s | %-20s |%n",
                t.getLoaiPrefix(), t.getMaTram(), t.getTenTram(), t.getCongSuat(),
                haoMonStr, luuYBaoTri, t.getTrangThaiHoatDong(), thoiGianStr);
    }

    // Ham bo tro 3b: Xuat DS cung tong gio su dung (cho thon ke > x)
    private void inDSTramWithTotal(TramSac t) {
        double totalHour = t.getThoiGianHoatDong();
        if (!t.isSanSang() && t.getThoiGianBatDauSac() != null) {
            totalHour += tinhThoiGianSacPhut(t.getThoiGianBatDauSac(), LocalDateTime.now()) / 60.0;
        }

        String totalStr = String.format("%.1f gio", totalHour);
        
        // Tinh muc hao mon va trang thai bao tri
        double mucHaoMon = t.tinhMucHaoMon();
        String haoMonStr = String.format("%.1f%%", mucHaoMon);
        String luuYBaoTri = t.getTrangThaiBaoTri();
        
        System.out.printf("| %-16s | %-10s | %-40s | %6.1f kW | %-8s | %-15s | %-16s | %-20s |%n",
                t.getLoaiPrefix(), t.getMaTram(), t.getTenTram(), t.getCongSuat(),
                haoMonStr, luuYBaoTri, t.getTrangThaiHoatDong(), totalStr);
    }

    // Kieu 1: Xuat thong tin chi tiet cua DUY NHAT 1 tram
    public void xuatThongTin1Tram(TramSac t) {
        if (t == null)
            return;
        inTieuDeBang();
        inDSTram(t);
        inKeNgang("=", 160);
    }

    // Kieu 2: Xuat toan bao danh sach (co kem logic sap xep)
    public void xuatDanhSach() {
        if (danhSach.isEmpty()) {
            System.out.println("!!! Danh sach trong! Chua co tram sac nao duoc them.");
            return;
        }

        // Tao ban sao de sap xep ma khong anh huong den danh sach goc
        List<TramSac> sortedList = new ArrayList<>(danhSach);

        // Sap xep theo yeu cau:
        // 1. trangThai: Dang hoat dong (false) truoc, San sang (true) sau
        // 2. viTri: Theo thu tu Enum
        // 3. maTram: tie-break de dam bao thu tu on dinh, de doc
        Collections.sort(sortedList, (a, b) -> {
            int res = Boolean.compare(a.isSanSang(), b.isSanSang());
            if (res != 0)
                return res;

            res = Integer.compare(a.getViTri().ordinal(), b.getViTri().ordinal());
            if (res != 0)
                return res;

            return a.getMaTram().compareTo(b.getMaTram());
        });

        System.out.println("\n" + "=".repeat(50) + " DANH SACH TRAM SAC " + "=".repeat(50));
        inTieuDeBang();
        for (TramSac t : sortedList) {
            inDSTram(t);
        }
        System.out.println("=".repeat(160));
    }

    // ----------------------------------------------------------
    // Chuc nang 5: Cap nhat trang thai
    // ----------------------------------------------------------
    public void capNhatTrangThai(Scanner scanner) {

        // 1. Nhap ma tram
        System.out.print("Nhap ID tram can cap nhat: ");
        String id = scanner.nextLine().trim();

        TramSac found = timTramTheoId(id);

        if (found == null) {
            System.out.println("!!! Khong tim thay tram voi ID '" + id + "'");
            return;
        }

        // Hien thi trang thai hien tai
        System.out.println("Thong tin hien tai cua tram:");
        xuatThongTin1Tram(found);

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
                if (confirm(scanner, "Xac nhan thay doi trang thai?")) {
                    // Tinh toan thoi gian sac lien tuc va cong don
                    if (newStatus && !found.isSanSang() && found.getThoiGianBatDauSac() != null) {
                        // Tu Dang sac (false) -> San sang (true)
                        long phutDaSac = tinhThoiGianSacPhut(found.getThoiGianBatDauSac(), LocalDateTime.now());
                        found.setThoiGianHoatDong(found.getThoiGianHoatDong() + (phutDaSac / 60.0));
                        found.setThoiGianBatDauSac(null);
                    } else if (!newStatus && found.isSanSang()) {
                        // Tu San sang (true) -> Dang sac (false)
                        found.setThoiGianBatDauSac(LocalDateTime.now());
                    }

                    found.setSanSang(newStatus);
                    System.out.println("==> Cap nhat trang thai thanh cong!");
                }
            } catch (NumberFormatException e) {
                System.out.println("!!! Phai nhap so 1 hoac 2!");
                return;
            }
        } else if (loai == 2) {
            System.out.print("\nNhap thoi gian hoat dong moi (h >= 0): ");
            try {
                double moi = Double.parseDouble(scanner.nextLine().trim());
                // Khong can kiem tra thu cong - setThoiGianHoatDong() tu xu ly gia tri am
                if (confirm(scanner, "Xac nhan thay doi thoi gian hoat dong?")) {
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

        TramSac found = timTramTheoId(id);

        if (found == null) {
            System.out.println("!!! Khong tim thay tram co ID: " + id);
            return;
        }

        System.out.println("Thong tin tram tim thay:");
        xuatThongTin1Tram(found);

        if (confirm(scanner, "Xac nhan xoa tram nay ?")) {
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

        TramSac found = timTramTheoId(id);
        if (found != null) {
            double mucHaoMon = found.tinhMucHaoMon();
            System.out.println("\nThong tin tram tim thay:");
            System.out.println("- ID: " + found.getMaTram());
            System.out.println("- Ten Tram: " + found.getTenTram());
            System.out.println("- Vi Tri: " + found.getViTri().getTen());
            System.out.printf("- Cong Suat: %.1f kW%n", found.getCongSuat());
            System.out.println("- Trang Thai: " + found.getTrangThaiHoatDong());
            System.out.printf("- Hao Mon: %.1f%%%n", mucHaoMon);
            System.out.println("- Luu y bao tri: " + found.getTrangThaiBaoTri());
        } else {
            System.out.println("!!! Khong tim thay tram co ID: " + id);
        }
    }

    // ----------------------------------------------------------
    // Chức năng 8: Thống kê hệ thống trụ sạc
    // - Hiển thị menu các loại thống kê (bảo trì, giờ sử dụng, khu vực nhiều trạm).
    // - Lặp cho đến khi người dùng chọn quay lại hoặc trả lời "n" khi được hỏi tiếp tục.
    // ----------------------------------------------------------
    public void thongKeTruSac(Scanner scanner) {
        if (danhSach.isEmpty()) {
            System.out.println("!!! Danh sach trong. Khong co tram sac nao de thong ke.");
            return;
        }

        while (true) {
            LoaiThongKe.hienThiMenu();
            System.out.print("Chon loai thong ke (1-4): ");

            LoaiThongKe loaiThongKe = null;
            try {
                int soChon = Integer.parseInt(scanner.nextLine().trim());
                loaiThongKe = LoaiThongKe.layTheoSoThuTu(soChon);
                if (loaiThongKe == null) {
                    System.out.println("!!! So thu tu khong hop le!");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("!!! Phai nhap mot so nguyen!");
                continue;
            }

            switch (loaiThongKe) {
                case BAO_TRI:
                    thongKeBaoTri();
                    break;
                case GIO_SD_THAP:
                    thongKeGioSDThap(scanner);
                    break;
                case KHU_VUC_CAO:
                    thongKeKhuVucCaoNhat();
                    break;
                case QUAY_LAI:
                    System.out.println("=> Quay lai menu chinh.");
                    return;
            }

            System.out.println();
            if (!confirm(scanner, "Ban co muon tiep tuc thong ke?")) {
                break;
            }
        }
    }

    // Hàm hỗ trợ: Thống kê các trạm cần bảo trì (mức hao mòn > 90% ngưỡng 500h)
    // Thuật toán:
    // 1. Duyệt toàn bộ danh sách trạm, tính mức hao mòn bằng TramSac.tinhMucHaoMon().
    // 2. Nếu > 90% thì đưa vào danh sách cảnh báo.
    // 3. In bảng chi tiết các trạm này, kèm thêm dòng giải thích mức hao mòn / 500h.
    public void thongKeBaoTri() {
        List<TramSac> tramBaoTri = new ArrayList<>();
        
        for (TramSac t : danhSach) {
            double mucHaoMon = t.tinhMucHaoMon();
            if (mucHaoMon > 90) {
                tramBaoTri.add(t);
            }
        }

        System.out.println("\n" + "=".repeat(36) + " THONG KE TRAM CAN BAO TRI " + "=".repeat(36));
        System.out.println("(Tram co muc haomon > 90% cua han bao tri 500h)");
        
        if (tramBaoTri.isEmpty()) {
            System.out.println("\n✓ Tat ca cac tram deu co tinh trang tot. Khong co tram nao can bao tri.");
        } else {
            System.out.println("\n[CANH BAO] Co " + tramBaoTri.size() + " tram can bao tri:");
            System.out.println("-".repeat(21));
            inTieuDeBang();
            for (TramSac t : tramBaoTri) {
                inDSTram(t);
            }
            inKeNgang("=", 160);
            System.out.println("\nChi tiet hao mon:");
            for (TramSac t : tramBaoTri) {
                double mucHaoMon = t.tinhMucHaoMon();
                System.out.println("  [" + t.getMaTram() + "] " + t.getTenTram() 
                    + " - Hao mon: " + String.format("%.1f", mucHaoMon) + "% (Da su dung: " 
                    + String.format("%.1f", t.getThoiGianHoatDong()) + "h / 500h)");
            }
        }
    }

    // Hàm hỗ trợ: Thống kê các trạm có tổng số giờ sử dụng > ngưỡng nhập từ bàn phím.
    //
    // Tham số:
    // - scanner: nguồn đọc dữ liệu từ bàn phím.
    //
    // Ý nghĩa biến trong hàm:
    // - gioMin: ngưỡng số giờ tối thiểu mà người dùng muốn lọc (không liên quan tới ràng buộc nội bộ của TramSac),
    //           chỉ dùng làm tiêu chí lọc danh sách hiện tại.
    // - tramCao: danh sách các trạm có tổng số giờ sử dụng vượt qua gioMin.
    // - tongGioSuDung: tổng giờ sử dụng thực tế của từng trạm (bao gồm cả thời gian đang sạc nếu có).
    public void thongKeGioSDThap(Scanner scanner) {
        System.out.print("\nNhap so gio su dung toi thieu (h): ");

        // Ngưỡng lọc do người dùng chọn, không bị ràng buộc bởi TramSac
        double gioMin = 0;
        try {
            gioMin = Double.parseDouble(scanner.nextLine().trim());
            if (gioMin < 0) {
                System.out.println("!!! So gio khong duoc am!");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("!!! Phai nhap mot so!");
            return;
        }

        // Danh sách các trạm có tổng số giờ sử dụng (tích lũy + đang sạc) vượt ngưỡng gioMin
        List<TramSac> tramCao = new ArrayList<>();
        for (TramSac t : danhSach) {
            // tongGioSuDung = thoiGianHoatDong (tích lũy) + số giờ đang sạc (nếu có)
            double tongGioSuDung = t.getThoiGianHoatDong();
            if (!t.isSanSang() && t.getThoiGianBatDauSac() != null) {
                tongGioSuDung += tinhThoiGianSacPhut(t.getThoiGianBatDauSac(), LocalDateTime.now()) / 60.0;
            }
            if (tongGioSuDung > gioMin) {
                tramCao.add(t);
            }
        }

        System.out.println("\n" + "=".repeat(36) + " THONG KE TRAM CO SO GIO SU DUNG > X " + String.format("%.1f", gioMin) + "h" + " " + "=".repeat(36))    ;
        
        if (tramCao.isEmpty()) {
            System.out.println("\nKhong co tram nao co so gio su dung lon hon " + gioMin + "h.");
        } else {
            // Sap xep theo thoi gian hoat dong giam dan
            Collections.sort(tramCao, (a, b) -> Double.compare(
                    b.getThoiGianHoatDong() + (b.isSanSang() ? 0 : tinhThoiGianSacPhut(b.getThoiGianBatDauSac(), LocalDateTime.now()) / 60.0),
                    a.getThoiGianHoatDong() + (a.isSanSang() ? 0 : tinhThoiGianSacPhut(a.getThoiGianBatDauSac(), LocalDateTime.now()) / 60.0)));
            
            System.out.println("\nCo " + tramCao.size() + " tram co so gio su dung > " + gioMin + "h:");
            
            inTieuDeBang();
            for (TramSac t : tramCao) {
                inDSTramWithTotal(t);
            }
            System.out.println("=".repeat(160));
        }
    }

    // Hàm hỗ trợ: Thống kê khu vực có tần suất sử dụng cao nhất
    // Thuật toán:
    // 1. Khởi tạo 2 Map:
    //    - demKhuVuc: đếm số trạm ở mỗi huyện.
    //    - tongGioKhuVuc: tổng số giờ hoạt động của các trạm trong huyện đó.
    // 2. Duyệt danh sách trạm, cộng dồn vào 2 Map trên.
    // 3. Tìm khu vực có nhiều trạm nhất (demKhuVuc lớn nhất).
    // 4. Sắp xếp danh sách các huyện theo số trạm giảm dần, in bảng xếp hạng kèm tỉ lệ % so với toàn hệ thống.
    public void thongKeKhuVucCaoNhat() {
        // Dem so lan su dung (so tram) tai moi khu vuc va tong gio su dung
        Map<HuyenLamDong, Integer> demKhuVuc = new LinkedHashMap<>();
        Map<HuyenLamDong, Double> tongGioKhuVuc = new LinkedHashMap<>();

        for (HuyenLamDong kv : HuyenLamDong.values()) {
            demKhuVuc.put(kv, 0);
            tongGioKhuVuc.put(kv, 0.0);
        }

        for (TramSac t : danhSach) {
            HuyenLamDong kv = t.getViTri();
            demKhuVuc.put(kv, demKhuVuc.get(kv) + 1);
            tongGioKhuVuc.put(kv, tongGioKhuVuc.get(kv) + t.getThoiGianHoatDong());
        }

        // Tim khu vuc co tan xuat cao nhat (so tram nhieu nhat)
        HuyenLamDong kvCaoNhat = null;
        int soTramCaoNhat = 0;
        for (Map.Entry<HuyenLamDong, Integer> entry : demKhuVuc.entrySet()) {
            if (entry.getValue() > soTramCaoNhat) {
                soTramCaoNhat = entry.getValue();
                kvCaoNhat = entry.getKey();
            }
        }

        System.out.println("\n" + "=".repeat(26) + " THONG KE KHU VUC CO TAN SUAT SU DUNG CAO NHAT " + "=".repeat(26));
          
        System.out.printf("| %-40s | So Tram | Tong Gio Su Dung (h)| Ty Le %%    |%n", "Ten Khu Vuc");
        System.out.println("-".repeat(101));

        // Sap xep theo so tram giam dan
        List<Map.Entry<HuyenLamDong, Integer>> sortedList = new ArrayList<>(demKhuVuc.entrySet());
        Collections.sort(sortedList, (a, b) -> Integer.compare(b.getValue(), a.getValue()));

        int rank = 1;
        for (Map.Entry<HuyenLamDong, Integer> entry : sortedList) {
            HuyenLamDong kv = entry.getKey();
            int soTram = entry.getValue();
            double tongGio = tongGioKhuVuc.get(kv);
            double tyLe = (soTram * 100.0) / danhSach.size();
            
            String marker = (rank <= 3) ? " [HANG " + rank + "]" : "";
            System.out.printf("| %-40s | %-7d | %-19.1f | %-9.1f%% |%s%n", 
                kv.getTen(), soTram, tongGio, tyLe, marker);
            rank++;
        }
        System.out.println("-".repeat(101));

        if (kvCaoNhat != null) {
            System.out.println("\n[KET LUAN] Khu vuc co tan xuat su dung cao nhat:");
            System.out.println("  - Khu vuc: " + kvCaoNhat.getTen());
            System.out.println("  - So tram: " + soTramCaoNhat);
            System.out.println("  - Tong gio su dung: " + String.format("%.1f", tongGioKhuVuc.get(kvCaoNhat)) + " gio");
        }
        System.out.println("=".repeat(101));
    }

    // ----------------------------------------------------------
    // Chuc nang 9: Xuat danh sach ra file Excel (chua trien khai) @Loc
    // ----------------------------------------------------------
    // public void xuatFile() { ... }

    // ----------------------------------------------------------
    // Chức năng 10: Tính số tiền dự kiến / hóa đơn thực tế cho 1 trạm
    // - Trường hợp trạm đang trống: tính dự toán chi phí cho mức % pin mong muốn.
    // - Trường hợp trạm đang sạc: tính hóa đơn thực tế dựa trên thời gian đã sạc.
    // Thuật toán (tổng quát):
    // 1. Xác định dung lượng năng lượng cần sạc (kWh) từ dung lượng pin và % cần sạc.
    // 2. Tính chi phí điện = điện năng cần sạc * đơn giá/kWh.
    // 3. Tính thời gian sạc dự kiến theo công suất trụ.
    // 4. Nếu là hóa đơn thực tế, so sánh thời gian đã sạc với thời gian dự kiến để tính số phút quá hạn
    //    và nhân với đơn giá phụ phí (nếu là trạm nhanh/siêu nhanh).
    // 5. In ra bảng chi tiết chi phí điện, phụ phí (nếu có) và tổng tiền.

    public void tinhChiPhi1Tram(Scanner scanner) {
        System.out.print("Nhap ID tram can xem chi phi: ");
        String id = scanner.nextLine().trim();

        TramSac found = timTramTheoId(id);

        if (found == null) {
            System.out.println("!!! Khong tim thay tram co ID: " + id);
            return;
        }

        double dungLuongPin = 70.0;   // Dung lượng pin (kWh) giả định của xe
        double phanTramPin = 80;      // Mặc định sạc lên 80% nếu người dùng không nhập
        boolean isThucTe = !found.isSanSang();

        if (!isThucTe) {
            // TRUONG HOP 1: TRAM DANG TRONG -> DU TOAN
            System.out.print("Nhap % pin can sac (0-100) [Mac dinh: 80]: ");
            String pinInput = scanner.nextLine().trim();
            if (!pinInput.isEmpty()) {
                try {
                    phanTramPin = Double.parseDouble(pinInput);
                    if (phanTramPin <= 0 || phanTramPin > 100) {
                        System.out.println("=> % pin khong hop le, su dung mac dinh 80%.");
                        phanTramPin = 80;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("=> % pin khong hop le, su dung mac dinh 80%.");
                    phanTramPin = 80;
                }
            }
        } else {
            // TRUONG HOP 2: TRAM DANG SAC -> XUAT HOA DON
            System.out.println("(!) Tram nay dang trong phien sac. He thong dang tinh toan hoa don thuc te...");
        }

        double dienNangCanSac = dungLuongPin * (phanTramPin / 100.0);
        double chiPhiDien = dienNangCanSac * TramSac.GIA_MOI_KWH;
        double thoiGianSacGio = dienNangCanSac / found.getCongSuat();
        long thoiGianDuKienPhut = (long) (thoiGianSacGio * 60);

        int phutQuaHan = 0;
        long thoiGianDaSacPhut = 0;
        double donGiaPhuPhi = 0;

        // Xác định đơn giá phụ phí theo loại trạm
        if (found instanceof TramSacNhanh)
            donGiaPhuPhi = 1000;
        else if (found instanceof TramSacSieuNhanh)
            donGiaPhuPhi = 3000;

        if (isThucTe) {
            // Tính toán thời gian thực tế cho trạm đang sạc
            thoiGianDaSacPhut = tinhThoiGianSacPhut(found.getThoiGianBatDauSac(), LocalDateTime.now());
            long quaHan = thoiGianDaSacPhut - thoiGianDuKienPhut;
            if (quaHan > 0 && donGiaPhuPhi > 0) {
                phutQuaHan = (int) quaHan;
            }
        }

        double chiPhiQuaHan = phutQuaHan * donGiaPhuPhi;
        double tongChiPhi = chiPhiDien + chiPhiQuaHan;

        System.out.println("\n" + "=".repeat(45));
        if (isThucTe) {
            System.out.println("      HOA DON THANH TOAN THUC TE (" + found.getMaTram() + ")");
        } else {
            System.out.println("      DU TOAN CHI PHI SAC XE (" + found.getMaTram() + ")");
        }
        System.out.println("=".repeat(45));
        System.out.println("- Ten tram: " + found.getTenTram());
        System.out.println("- Muc pin: " + phanTramPin + "%");

        if (isThucTe) {
            System.out.println("- Thoi gian da sac: " + dinhDangThoiGian(thoiGianDaSacPhut));
            System.out.println("- Thoi gian tieu chuan: " + dinhDangThoiGian(thoiGianDuKienPhut));
        } else {
              System.out.println("- Thoi gian sac du kien: " + dinhDangThoiGian(thoiGianDuKienPhut));
        }

        System.out.printf("- Chi phi dien: %,.0f VND%n", chiPhiDien);

        if (isThucTe && phutQuaHan > 0) {
            System.out.printf("- Chi phi qua han (%d phut): %,.0f VND%n", phutQuaHan, chiPhiQuaHan);
        } else if (!isThucTe && donGiaPhuPhi > 0) {
            // Thong bao ve phu phi neu co the xay ra (cho tram nhanh/sieu nhanh)
            System.out.printf("(!) Luu y: Phu phi qua han tai tram nay la %,.0f VND/phut.%n", donGiaPhuPhi);
        }

        System.out.println("-".repeat(45));

        if (isThucTe) {
            System.out.printf("=> TONG THANH TOAN: %,.0f VND%n", tongChiPhi);
        } else {
            System.out.printf("=> TONG CHI PHI DU KIEN: %,.0f VND%n", tongChiPhi);
        }
        System.out.println("=".repeat(45));
    }

    // ----------------------------------------------------------
    // Chức năng 10.2: Tính chi phí và gợi ý lựa chọn trạm cho cả danh sách
    // - Không phụ thuộc trạng thái hiện tại của trạm (chỉ dùng công suất).
    // - Mục tiêu là so sánh thời gian sạc dự kiến và phụ phí quá hạn / phút giữa các loại trạm.
    // Thuật toán:
    // 1. Xác định điện năng cần sạc (dựa trên % pin người dùng nhập).
    // 2. Với mỗi trạm, tính thời gian sạc dự kiến.
    // 3. Gán đơn giá phụ phí / phút nếu là trạm nhanh/siêu nhanh.
    // 4. In bảng tóm tắt để người dùng dễ so sánh lựa chọn.
    public void tinhChiPhiDS(Scanner scanner) {
        System.out.print("Nhap % pin can sac (0-100) [Mac dinh: 80]: ");
        String pinInput = scanner.nextLine().trim();
        double phanTramPin = 80;
        if (!pinInput.isEmpty()) {
            try {
                phanTramPin = Double.parseDouble(pinInput);
                if (phanTramPin <= 0 || phanTramPin > 100) {
                    System.out.println("=> % pin khong hop le, su dung mac dinh 80%.");
                    phanTramPin = 80;
                }
            } catch (NumberFormatException e) {
                System.out.println("=> % pin khong hop le, su dung mac dinh 80%.");
                phanTramPin = 80;
            }
        }

        double dungLuongPin = 70.0;                        // Dung lượng pin giả định (kWh)
        double dienNangCanSac = dungLuongPin * (phanTramPin / 100.0); // Số kWh cần sạc
        double chiPhiDien = dienNangCanSac * TramSac.GIA_MOI_KWH;     // Chi phí điện thuần tuý

        System.out.println("\n" + "=".repeat(33) + " GOI Y TRAM SAC " + "=".repeat(33));
        System.out.println("- Muc pin can sac: " + phanTramPin + "%");
        System.out.println("- Chi phi du kien thuan tuy: " + String.format("%,.0f", chiPhiDien) + " VND");
        System.out.println("+------------------+------------+-----------+-----------------+-----------------+");
        System.out.printf("| %-16s | %-10s | %-9s | %-15s | %-15s |%n",
                "Loai", "ID", "Cong Suat", "Thoi gian sac", "Tien qua han/p");
        System.out.println("+------------------+------------+-----------+-----------------+-----------------+");

        for (TramSac t : danhSach) {
            double thoiGianGio = dienNangCanSac / t.getCongSuat();
            long thoiGianPhut = (long) (thoiGianGio * 60);
            String thoiGianStr = dinhDangThoiGian(thoiGianPhut);

            double phiQuaHan = 0;
            if (t instanceof TramSacNhanh)
                phiQuaHan = 1000;
            else if (t instanceof TramSacSieuNhanh)
                phiQuaHan = 3000;

            String phiQuaHanStr = (phiQuaHan > 0 && !(t instanceof TramSacCham)) ? String.format("%,.0f", phiQuaHan)
                    : "0";

            System.out.printf("| %-16s | %-10s | %6.1f kW | %-15s | %15s |%n",
                    t.getLoaiPrefix(), t.getMaTram(), t.getCongSuat(), thoiGianStr, phiQuaHanStr);
        }
        System.out.println("+------------------+------------+-----------+-----------------+-----------------+");
        System.out.println("* Lưu ý: Chi phí cuối cùng = Chi phí điện + (Tiền quá hạn/phút * Số phút quá hạn)");
    }

    // ----------------------------------------------------------
    // Chức năng 11: Sắp xếp danh sách trụ sạc theo mức độ ưu tiên sử dụng
    // ----------------------------------------------------------
    // Tiêu chí sắp xếp:
    // 1. Nhóm ưu tiên theo trạng thái + hao mòn:
    //    - Ưu tiên 0: Trạm sẵn sàng và mức hao mòn < 100%.
    //    - Ưu tiên 1: Trạm đang sạc (nhưng chưa vượt 100% hao mòn).
    //    - Ưu tiên 2: Trạm cần bảo trì / ngừng hoạt động (hao mòn >= 100%).
    // 2. Trong cùng một nhóm ưu tiên: sắp xếp theo tổng giờ hoạt động tăng dần
    //    (trạm "ít làm việc" hơn đứng trước để ưu tiên phân bổ tải).
    // 3. Nếu vẫn bằng nhau: sắp xếp theo mã trạm (maTram) để danh sách ổn định, dễ theo dõi.
    public void sapXepDS() {
        if (danhSach.isEmpty()) {
            System.out.println("!!! Danh sach trong. Khong co tram nao de sap xep.");
            return;
        }

        List<TramSac> sorted = new ArrayList<>(danhSach);
        sorted.sort((a, b) -> {
            int pA = a.tinhMucHaoMon() >= 100.0 ? 2 : (a.isSanSang() ? 0 : 1);
            int pB = b.tinhMucHaoMon() >= 100.0 ? 2 : (b.isSanSang() ? 0 : 1);

            if (pA != pB)
                return Integer.compare(pA, pB);

            int byHours = Double.compare(a.getThoiGianHoatDong(), b.getThoiGianHoatDong());
            if (byHours != 0)
                return byHours;

            return a.getMaTram().compareTo(b.getMaTram());
        });

        System.out.println("\n" + "=".repeat(40) + " DANH SACH (DA SAP XEP) " + "=".repeat(40));
        inTieuDeBang();
        for (TramSac t : sorted) {
            inDSTram(t);
        }
        inKeNgang("=", 160);
    }
}