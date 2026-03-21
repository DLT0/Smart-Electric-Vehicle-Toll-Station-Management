package com.evstation;

import java.util.*;
import java.time.LocalDateTime;
import java.time.Duration;

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
// LOP TRUU TUONG: TramSac
// ============================================================
abstract class TramSac {
    // * Khai bao cac truong du lieu (the private) - Rang buoc du lieu
    private String maTram; // Ma dinh danh duy nhat cho tram sac
    private String tenTram; // Ten = Loai + ViTri + STT
    private HuyenLamDong viTri; // Vi tri duoc chon tu Enum (type-safe)
    private boolean sanSang; // true = San sang | false = Dang su dung
    private double congSuat; // Cong suat sac (don vi: kW), phai > 0
    private double thoiGianSuDung; // Thoi gian su dung hien tai (gio), duoc cong vao tich luy khi ket thuc
    private double thoiGianHoatDong; // Tong so gio tich luy da van hanh (dung tinh bao tri), phai >= 0
    protected static final double GIA_MOI_KWH = 3850; // Don gia co dinh (VND/kWh)
    protected static final double HAN_BAO_TRI_MAC_DINH = 500.0; // Han bao tri mac dinh (h)
    private int sttHeThong; // STT khi them vao he thong (de sap xep fallback)
    private LocalDateTime thoiGianBatDauSac; // Thoi gian bat dau sac (neu dang sac)

    // * Dong goi thuoc tinh (the public) - Getter & Setter
    public LocalDateTime getThoiGianBatDauSac() {
        return this.thoiGianBatDauSac;
    }

    public void setThoiGianBatDauSac(LocalDateTime t) {
        this.thoiGianBatDauSac = t;
    }

    // * ID duoc sinh tu dong boi Module.sinhMaTram()
    // Vi du dinh dang: "SC-DAL-001", "SC-BAO-003", ...
    // setMaTram() chi duoc goi 1 lan tu constructor, sau do READ-ONLY

    // maTram: READ-ONLY - chi co getter, khong cho phep cap nhat tu ben ngoai
    public String getMaTram() {
        return this.maTram;
    }

    private void setMaTram(String value) {
        if (value == null || value.trim().isEmpty())
            value = "UNKNOWN";
        this.maTram = value.trim();
    }

    public String getTenTram() {
        return this.tenTram;
    }

    public void setTenTram(String value) {
        if (value == null || value.trim().isEmpty())
            value = "Chua dat ten";
        this.tenTram = value.trim();
    }

    public HuyenLamDong getViTri() {
        return this.viTri;
    }

    public void setViTri(HuyenLamDong value) {
        if (value == null)
            return; // Rang buoc: vi tri khong duoc null
        this.viTri = value;
    }

    public boolean isSanSang() {
        return this.sanSang;
    }

    public void setSanSang(boolean value) {
        this.sanSang = value; // Boolean khong can rang buoc them
    }

    public double getCongSuat() {
        return this.congSuat;
    }

    public void setCongSuat(double value) {
        if (value < 7)
            value = 7; // Rang buoc: cong suat phai la so duong
        else if (value > 300)
            value = 300; // Rang buoc: gioi han toi da 300kW
        this.congSuat = value;
    }

    public double getThoiGianSuDung() {
        return this.thoiGianSuDung;
    }

    public void setThoiGianSuDung(double value) {
        if (value < 0)
            value = 0; // Rang buoc: thoi gian khong duoc am
        this.thoiGianSuDung = value;
    }

    public double getThoiGianHoatDong() {
        return this.thoiGianHoatDong;
    }

    public void setThoiGianHoatDong(double value) {
        if (value < 0)
            value = 0; // Rang buoc: thoi gian khong duoc am
        this.thoiGianHoatDong = value;
    }

    public int getSttHeThong() {
        return this.sttHeThong;
    }

    public void setSttHeThong(int value) {
        if (value < 1)
            value = 1;
        this.sttHeThong = value;
    }

    // Constructor: nhan maTram da duoc Module sinh san, cac truong con lai tu dong
    public TramSac(String maTram, HuyenLamDong viTri, double congSuat, int sttHeThong) {
        setMaTram(maTram); // maTram duoc Module.sinhMaTram() tao ra
        setViTri(viTri);
        setCongSuat(congSuat);
        setThoiGianSuDung(0.0); // Tu dong khoi tao la 0
        setThoiGianHoatDong(0.0); // Tu dong khoi tao la 0
        setSanSang(true); // Mac dinh: San sang / Trong
        setSttHeThong(sttHeThong);
        setThoiGianBatDauSac(null);
    }

    public double tinhMucHaoMon() {
        double phanTram = (this.thoiGianHoatDong / HAN_BAO_TRI_MAC_DINH) * 100;
        return Math.min(phanTram, 100.0); // Gioi han toi da 100%
    }

    public String getTrangThaiBaoTri() {
        double mucHaoMon = tinhMucHaoMon();
        if (mucHaoMon >= 100.0) {
            return "Bảo trì";
        } else if (mucHaoMon > 90.0) {
            return "Cần bảo trì";
        } else {
            return "Ổn định";
        }
    }

    public String getTrangThaiHoatDong() {
        double mucHaoMon = tinhMucHaoMon();
        if (mucHaoMon >= 100.0) {
            return "Ngừng hoạt động";
        } else if (!sanSang) {
            return "Đang sạc";
        } else {
            return "Sẵn sàng";
        }
    }

    protected abstract String getLoaiPrefix();
}

// ============================================================
// LOP CON: TramSacCham (7kW - 11kW)
// ============================================================
class TramSacCham extends TramSac {
    public TramSacCham(String maTram, HuyenLamDong viTri, double congSuat, int stt, int sttHeThong) {
        super(maTram, viTri, congSuat, sttHeThong);
        setTenTram("Sac Cham " + viTri.getTen() + " " + stt);
    }

    @Override
    protected String getLoaiPrefix() {
        return "[Sac Cham]";
    }
}

// ============================================================
// LOP CON: TramSacNhanh (12kW - 120kW)
// ============================================================
class TramSacNhanh extends TramSac {
    public TramSacNhanh(String maTram, HuyenLamDong viTri, double congSuat, int stt, int sttHeThong) {
        super(maTram, viTri, congSuat, sttHeThong);
        setTenTram("Sac Nhanh " + viTri.getTen() + " " + stt);
    }

    @Override
    protected String getLoaiPrefix() {
        return "[Sac Nhanh]";
    }
}

// ============================================================
// LOP CON: TramSacSieuNhanh (121kW - 300kW)
// ============================================================
class TramSacSieuNhanh extends TramSac {
    public TramSacSieuNhanh(String maTram, HuyenLamDong viTri, double congSuat, int stt, int sttHeThong) {
        super(maTram, viTri, congSuat, sttHeThong);
        setTenTram("Sac Sieu Nhanh " + viTri.getTen() + " " + stt);
    }

    @Override
    protected String getLoaiPrefix() {
        return "[Sac Sieu Nhanh]";
    }
}

// ============================================================
// ENUM: Danh sach cac lua chon thong ke tru sac
// ============================================================
enum LoaiThongKe {
    BAO_TRI("Tru sac can bao tri (haomon > 90%)"),
    GIO_SD_THAP("Tru sac co so gio su dung < X (nhap X tu ban phim)"),
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
    // true = dang trong che do du lieu mock; false = nguoi dung da nhap du lieu
    // that
    // Khi nguoi dung them tram dau tien, flag nay tu dong dat lai va mock data bi
    // xoa
    private boolean isMockMode = true;

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
    // CÁC HÀM BỔ TRỢ CHUNG (PUBLIC HELPERS) - Tái sử dụng ở nhiều nơi
    // =========================================================================

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
    // ----------------------------------------------------------
    private String sinhMaTram(HuyenLamDong khuVuc, double congSuat) {
        String prefix = (congSuat <= 11) ? PREFIX_CHAM
                : (congSuat <= 120) ? PREFIX_NHANH
                        : PREFIX_SIEU_NHANH;
        String tenEnum = khuVuc.name().replace("_", ""); // "DA_LAT" -> "DALAT"
        String maKhuVuc = tenEnum.substring(0, Math.min(MA_KHU_VUC_LENGTH, tenEnum.length())).toUpperCase();
        int sttTaiKhuVuc = countStationsAtLocation(khuVuc) + 1;
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
        System.out.println("  " + "-".repeat(28));
        return khuVuc;
    }

    // ----------------------------------------------------------
    // Chuc nang 1: Them 1 tru sac moi
    // ----------------------------------------------------------
    public void them1TruSac(Scanner scanner) {
        // Lan dau tien nguoi dung them tram that -> xoa toan bo mock data
        if (isMockMode) {
            danhSach.clear();
            isMockMode = false;
            System.out.println("[He thong] Du lieu mau da duoc xoa. Bat dau nhap du lieu that.");
        }

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
        while (cs < 7) {
            System.out.print("Nhap cong suat kW (7 <= cs <= 300): ");
            try {
                cs = Double.parseDouble(scanner.nextLine().trim());
                if (cs < 7) {
                    System.out.println("!!! Cong suat toi thieu la 7kW!");
                } else if (cs > 300) {
                    System.out.println("!!! Cong suat toi da la 300kW!");
                    cs = 300;
                }
            } catch (NumberFormatException e) {
                System.out.println("!!! Cong suat phai la mot so!");
            }
        }
        return cs;
    }

    // Ham phu tro (Public de tai su dung): Sinh ID, phan loai va them tram vao danh
    // sach
    public TramSac themVaoDanhSach(HuyenLamDong khuVuc, double cs) {
        String id = sinhMaTram(khuVuc, cs);
        int stt = countStationsAtLocation(khuVuc) + 1;
        int sttHeThong = danhSach.size() + 1;
        TramSac moi = null;
        if (cs <= 11) {
            moi = new TramSacCham(id, khuVuc, cs, stt, sttHeThong);
            danhSach.add(moi);
            System.out.println("=> [" + id + "] Sac Cham (7-11kW) - " + khuVuc.getTen() + " da duoc them!");
        } else if (cs <= 120) {
            moi = new TramSacNhanh(id, khuVuc, cs, stt, sttHeThong);
            danhSach.add(moi);
            System.out.println("=> [" + id + "] Sac Nhanh (12-120kW) - " + khuVuc.getTen() + " da duoc them!");
        } else {
            moi = new TramSacSieuNhanh(id, khuVuc, cs, stt, sttHeThong);
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
        // Xoa mock data mot lan duy nhat truoc khi bat dau vong lap
        if (isMockMode) {
            danhSach.clear();
            isMockMode = false;
        }

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
        System.out.printf("| %-16s | %-10s | %-40s | %-9s | %-8s | %-12s | %-10s | %-20s |%n",
                "Loai", "ID", "Ten Tram", "Cong Suat", "Hao Mon", "Bao Tri", "Trang Thai", "Thoi gian SD");
        inKeNgang("=", 160);
    }

    // Ham bo tro 3: Xuat DS
    private void inDSTram(TramSac t) {
        long phut = 0;
        String extra = "";
        if (!t.isSanSang() && t.getThoiGianBatDauSac() != null) {
            phut = tinhThoiGianSacPhut(t.getThoiGianBatDauSac(), LocalDateTime.now());

            // Tinh thoi gian tieu chuan (Gia su sạc 80% cho xe 70kWh = 56kWh)
            if (!(t instanceof TramSacCham)) {
                long standardMin = (long) ((56.0 / t.getCongSuat()) * 60);
                if (phut > standardMin) {
                    extra = " (+" + (phut - standardMin) + "p)";
                }
            }
        }
        String thoiGianStr = (phut > 0) ? dinhDangThoiGian(phut) + extra : "-";

        double mucHaoMon = t.tinhMucHaoMon();
        String haoMonStr = String.format("%.1f%%", mucHaoMon);
        String baoTriStr = t.getTrangThaiBaoTri();
        String trangThaiStr = t.getTrangThaiHoatDong();

        System.out.printf("| %-16s | %-10s | %-40s | %6.1f kW | %-8s | %-12s | %-10s | %-20s |%n",
                t.getLoaiPrefix(), t.getMaTram(), t.getTenTram(), t.getCongSuat(),
                haoMonStr, baoTriStr, trangThaiStr, thoiGianStr);
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
        // 3. sttHeThong: Theo thu tu them vao
        Collections.sort(sortedList, (a, b) -> {
            int res = Boolean.compare(a.isSanSang(), b.isSanSang());
            if (res != 0)
                return res;

            res = Integer.compare(a.getViTri().ordinal(), b.getViTri().ordinal());
            if (res != 0)
                return res;

            return Integer.compare(a.getSttHeThong(), b.getSttHeThong());
        });

        System.out.println("\n" + "=".repeat(50) + " DANH SACH TRAM SAC " + "=".repeat(50));
        inTieuDeBang();
        for (TramSac t : sortedList) {
            inDSTram(t);
        }
        inKeNgang("=", 160);
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
                System.out.print("Xac nhan thay doi trang thai? (y/n): ");
                if (scanner.nextLine().trim().toLowerCase().equals("y")) {
                    // Tinh toan thoi gian sac lien tuc va cong don
                    if (newStatus && !found.isSanSang() && found.getThoiGianBatDauSac() != null) {
                        // Tu Dang sac (false) -> San sang (true)
                        long phutDaSac = tinhThoiGianSacPhut(found.getThoiGianBatDauSac(), LocalDateTime.now());
                        double gioSuDung = phutDaSac / 60.0;
                        
                        // Cap nhat thoi gian su dung hien tai
                        found.setThoiGianSuDung(gioSuDung);
                        // Cong thoi gian su dung vao thoi gian tich luy
                        found.setThoiGianHoatDong(found.getThoiGianHoatDong() + gioSuDung);
                        found.setThoiGianBatDauSac(null);
                        
                        System.out.printf("=> Thoi gian sac vua ket thuc: %.2f gio%n", gioSuDung);
                        System.out.printf("=> Tong thoi gian tich luy: %.2f gio%n", found.getThoiGianHoatDong());
                    } else if (!newStatus && found.isSanSang()) {
                        // Tu San sang (true) -> Dang sac (false)
                        found.setThoiGianBatDauSac(LocalDateTime.now());
                        found.setThoiGianSuDung(0.0); // Reset thoi gian su dung cho phien moi
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
            xuatThongTin1Tram(found);
        } else {
            System.out.println("!!! Khong tim thay tram co ID: " + id);
        }
    }

    // ----------------------------------------------------------
    // Chuc nang 8: Thong ke he thong tru sac
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

            System.out.print("\nBan co muon tiep tuc thong ke? (y/n): ");
            if (!scanner.nextLine().trim().toLowerCase().startsWith("y")) {
                break;
            }
        }
    }

    // Ham bo tro: Thong ke tram can bao tri (haomon > 90%)
    public void thongKeBaoTri() {
        List<TramSac> tramBaoTri = new ArrayList<>();
        
        for (TramSac t : danhSach) {
            double mucHaoMon = t.tinhMucHaoMon();
            if (mucHaoMon > 90) {
                tramBaoTri.add(t);
            }
        }

        System.out.println("\n" + "=".repeat(160) + " THONG KE TRAM CAN BAO TRI " + "=".repeat(32));
        System.out.println("(Tram co muc haomon > 90% cua han bao tri 500h)");
        
        if (tramBaoTri.isEmpty()) {
            System.out.println("\n✓ Tat ca cac tram deu co tinh trang tot. Khong co tram nao can bao tri.");
        } else {
            System.out.println("\n[CANH BAO] Co " + tramBaoTri.size() + " tram can bao tri:");
            System.out.println("-".repeat(160));

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

    // Ham bo tro: Thong ke tram co so gio su dung < x (nhap tu ban phim)
    public void thongKeGioSDThap(Scanner scanner) {
        System.out.print("\nNhap so gio su dung toi da (h): ");
        double gioDtMax = 0;
        try {
            gioDtMax = Double.parseDouble(scanner.nextLine().trim());
            if (gioDtMax < 0) {
                System.out.println("!!! So gio khong duoc am!");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("!!! Phai nhap mot so!");
            return;
        }

        List<TramSac> tramThap = new ArrayList<>();
        for (TramSac t : danhSach) {
            if (t.getThoiGianHoatDong() < gioDtMax) {
                tramThap.add(t);
            }
        }

        System.out.println("\n" + "=".repeat(80) + " THONG KE TRAM CO SO GIO SU DUNG < " + String.format("%.1f", gioDtMax) + "h ");
        
        if (tramThap.isEmpty()) {
            System.out.println("\nKhong co tram nao co so gio su dung nho hon " + gioDtMax + "h.");
        } else {
            // Sap xep theo thoi gian hoat dong tang dan
            Collections.sort(tramThap, (a, b) -> Double.compare(a.getThoiGianHoatDong(), b.getThoiGianHoatDong()));
            
            System.out.println("\nCo " + tramThap.size() + " tram co so gio su dung < " + gioDtMax + "h:");
            System.out.println("-".repeat(160));
            inTieuDeBang();
            for (TramSac t : tramThap) {
                inDSTram(t);
            }
            inKeNgang("=", 160);
        }
    }

    // Ham bo tro: Thong ke khu vuc co tan xuat su dung cao nhat (tong so gio su dung)
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

        System.out.println("\n" + "=".repeat(26) + " THONG KE KHU VUC CO TAN XUAT SU DUNG CAO NHAT " + "=".repeat(26));
          
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
        System.out.println("-".repeat(100));

        if (kvCaoNhat != null) {
            System.out.println("\n[KET LUAN] Khu vuc co tan xuat su dung cao nhat:");
            System.out.println("  - Khu vuc: " + kvCaoNhat.getTen());
            System.out.println("  - So tram: " + soTramCaoNhat);
            System.out.println("  - Tong gio su dung: " + String.format("%.1f", tongGioKhuVuc.get(kvCaoNhat)) + " gio");
        }
        System.out.println("=".repeat(101));
        System.out.println("=".repeat(100));
    }

    // ----------------------------------------------------------
    // Chuc nang 9: Xuat danh sach ra file Excel (chua trien khai) @Loc
    // ----------------------------------------------------------
    // public void xuatFile() { ... }

    // ----------------------------------------------------------
    // Chuc nang 10: Tinh so tien du kien
    // ----------------------------------------------------------

    public void tinhChiPhi1Tram(Scanner scanner) {
        System.out.print("Nhap ID tram can xem chi phi: ");
        String id = scanner.nextLine().trim();

        TramSac found = timTramTheoId(id);

        if (found == null) {
            System.out.println("!!! Khong tim thay tram co ID: " + id);
            return;
        }

        double dungLuongPin = 70.0;
        double phanTramPin = 80; // Gia tri mac dinh
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
        System.out.println("+------------------+------------+-----------+-----------------+-----------------+");

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

        // Xac dinh don gia phu phi theo loai tram
        if (found instanceof TramSacNhanh)
            donGiaPhuPhi = 1000;
        else if (found instanceof TramSacSieuNhanh)
            donGiaPhuPhi = 3000;

        if (isThucTe) {
            // Tinh toan thoi gian thuc te cho tram dang sac
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
            // Thong bao ve phu phi neu co the xay ra (cho tram nhanh/sieu nh
        System.out.println("+------------------+------------+-----------+-----------------+-----------------+");
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
    // Chuc nang 10.2: Tinh chi phi danh sach
    // ----------------------------------------------------------
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

        double dungLuongPin = 70.0;
        double dienNangCanSac = dungLuongPin * (phanTramPin / 100.0);
        double chiPhiDien = dienNangCanSac * TramSac.GIA_MOI_KWH;

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
        System.out.println("* Luu y: Chi phi cuoi cung = Chi phi dien + (Tien qua han/p * So phut qua han)");
    }

    // ----------------------------------------------------------
    // Chuc nang 11: Sap xep danh sach
    // ----------------------------------------------------------
    public void sapXepDS() {
        if (danhSach.isEmpty()) {
            System.out.println("!!! Danh sach trong. Khong co tram nao de sap xep.");
            return;
        }

        // Dinh nghia thu tu uu tien cho trang thai
        Map<String, Integer> statusOrder = Map.of(
                "Đang sạc", 0,
                "Sẵn sàng", 1,
                "Cần bảo trì", 2,
                "Bảo trì", 2,
                "Ngừng hoạt động", 3);

        // Ham lay chuoi trang thai tu doi tuong TramSac
        java.util.function.Function<TramSac, String> extractStatus = t -> {
            return t.getTrangThaiHoatDong();
        };

        Comparator<TramSac> comparator = Comparator
            .comparingInt((TramSac t) -> statusOrder.getOrDefault(extractStatus.apply(t), Integer.MAX_VALUE))
            .thenComparing((TramSac t) -> t.getViTri().getTen(), String.CASE_INSENSITIVE_ORDER)
            .thenComparingInt(TramSac::getSttHeThong);

        List<TramSac> sorted = new ArrayList<>(danhSach);
        sorted.sort(comparator);

        System.out.println("\n" + "=".repeat(40) + " DANH SACH (DA SAP XEP) " + "=".repeat(40));
        inTieuDeBang();
        for (TramSac t : sorted) {
            inDSTram(t);
        }
        inKeNgang("=", 160);
    }
}
// viet menu phu su dung enum xu