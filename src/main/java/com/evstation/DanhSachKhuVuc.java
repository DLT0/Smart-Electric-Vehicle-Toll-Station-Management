package com.evstation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;

// ============================================================
// ENUM: Danh sach cac don vi hanh chinh tinh Lam Dong 2026
// ============================================================
@Getter
@AllArgsConstructor
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

    // Getter de lay ten hien thi
    public String getTen() {
        return tenTiengViet;
    }

    /**
     * Hien thi danh sach khu vuc dang bang so thu tu. Dung values() de lap qua
     * tat ca phan tu cua Enum tu dong.
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
     * Lay Enum tuong ung theo so thu tu nguoi dung chon (bat dau tu 1). Tra ve
     * null neu so thu tu khong hop le.
     */
    public static HuyenLamDong layTheoSoThuTu(int soThuTu) {
        HuyenLamDong[] dsKhuVuc = HuyenLamDong.values();
        // soThuTu hop le la tu 1 den tong so phan tu
        if (soThuTu < 1 || soThuTu > dsKhuVuc.length) {
            return null;
        }
        // Mang bat dau tu 0, nen can tru 1
        return dsKhuVuc[soThuTu - 1];
    }
}

// ============================================================
// LOP TIEN ICH: DanhSachKhuVuc
//
// Cung cap cac thuat toan lam viec voi danh sach khu vuc:
//   1. Tim kiem khu vuc theo tu khoa (Stream + Predicate)
//   2. Loc tram sac theo khu vuc (Stream + Lambda)
//   3. Dem so tram tai moi khu vuc
//   4. Tinh tong gio hoat dong tai moi khu vuc
//   5. Tim khu vuc co tan xuat su dung cao nhat (Optional)
//   6. Sap xep khu vuc theo so tram giam dan
//   7. Xuat bang thong ke khu vuc
// ============================================================
public class DanhSachKhuVuc {

    // ----------------------------------------------------------
    // Thuat toan 1: Tim kiem khu vuc theo tu khoa (partial match, ignore case)
    // Su dung Stream + Predicate de loc.
    // ----------------------------------------------------------
    public static List<HuyenLamDong> timKiem(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return Arrays.asList(HuyenLamDong.values());
        }
        String kw = keyword.trim().toLowerCase();
        Predicate<HuyenLamDong> khopTen = kv -> kv.getTen().toLowerCase().contains(kw);
        Predicate<HuyenLamDong> khopEnum = kv -> kv.name().toLowerCase().replace("_", " ").contains(kw);
        return Arrays.stream(HuyenLamDong.values())
                .filter(khopTen.or(khopEnum))
                .collect(Collectors.toList());
    }

    // ----------------------------------------------------------
    // Thuat toan 2: Loc danh sach tram theo khu vuc chi dinh
    // Su dung Stream + Lambda.
    // ----------------------------------------------------------
    public static List<TramSac> locTramTheoKhuVuc(List<TramSac> danhSach, HuyenLamDong khuVuc) {
        return danhSach.stream()
                .filter(t -> t.getViTri() == khuVuc)
                .collect(Collectors.toList());
    }

    // ----------------------------------------------------------
    // Thuat toan 3: Dem so tram tai moi khu vuc
    // Tra ve Map<HuyenLamDong, Integer> (moi khu vuc co it nhat gia tri 0).
    // ----------------------------------------------------------
    public static Map<HuyenLamDong, Integer> demTramTheoKhuVuc(List<TramSac> danhSach) {
        Map<HuyenLamDong, Integer> demKhuVuc = new LinkedHashMap<>();
        for (HuyenLamDong kv : HuyenLamDong.values()) {
            demKhuVuc.put(kv, 0);
        }
        for (TramSac t : danhSach) {
            demKhuVuc.merge(t.getViTri(), 1, Integer::sum);
        }
        return demKhuVuc;
    }

    // ----------------------------------------------------------
    // Thuat toan 4: Tinh tong gio hoat dong cua tram tai moi khu vuc
    // Tra ve Map<HuyenLamDong, Double>.
    // ----------------------------------------------------------
    public static Map<HuyenLamDong, Double> tongGioTheoKhuVuc(List<TramSac> danhSach) {
        Map<HuyenLamDong, Double> tongGio = new LinkedHashMap<>();
        for (HuyenLamDong kv : HuyenLamDong.values()) {
            tongGio.put(kv, 0.0);
        }
        for (TramSac t : danhSach) {
            tongGio.merge(t.getViTri(), t.getThoiGianHoatDong(), Double::sum);
        }
        return tongGio;
    }

    // ----------------------------------------------------------
    // Thuat toan 5: Tim khu vuc co so tram nhieu nhat
    // Su dung Stream reduce / max voi Comparator lambda.
    // Tra ve Optional<HuyenLamDong> (trong neu danh sach rong).
    // ----------------------------------------------------------
    public static Optional<HuyenLamDong> timKhuVucCaoNhat(List<TramSac> danhSach) {
        Map<HuyenLamDong, Integer> demKhuVuc = demTramTheoKhuVuc(danhSach);
        return demKhuVuc.entrySet().stream()
                .filter(e -> e.getValue() > 0)
                .max((a, b) -> Integer.compare(a.getValue(), b.getValue()))
                .map(Map.Entry::getKey);
    }

    // ----------------------------------------------------------
    // Thuat toan 6: Sap xep khu vuc theo so tram giam dan
    // Su dung Collections.sort voi Lambda Comparator.
    // ----------------------------------------------------------
    public static List<Map.Entry<HuyenLamDong, Integer>> sapXepTheoSoTram(
            Map<HuyenLamDong, Integer> demKhuVuc) {
        List<Map.Entry<HuyenLamDong, Integer>> sorted = new ArrayList<>(demKhuVuc.entrySet());
        Collections.sort(sorted, (a, b) -> Integer.compare(b.getValue(), a.getValue()));
        return sorted;
    }

    // ----------------------------------------------------------
    // Thuat toan 7: Xuat bang thong ke day du (goi boi Module.thongKeKhuVucCaoNhat)
    // ----------------------------------------------------------
    public static void xuatBangThongKe(List<TramSac> danhSach) {
        if (danhSach.isEmpty()) {
            System.out.println("!!! Danh sach trong. Khong co du lieu de thong ke.");
            return;
        }

        Map<HuyenLamDong, Integer> demKhuVuc = demTramTheoKhuVuc(danhSach);
        Map<HuyenLamDong, Double> tongGioKhuVuc = tongGioTheoKhuVuc(danhSach);
        Optional<HuyenLamDong> kvCaoNhat = timKhuVucCaoNhat(danhSach);

        System.out.println("\n" + "=".repeat(26) + " THONG KE KHU VUC CO TAN SUAT SU DUNG CAO NHAT " + "=".repeat(26));
        System.out.printf("| %-40s | So Tram | Tong Gio Su Dung (h)| Ty Le %%    |%n", "Ten Khu Vuc");
        System.out.println("-".repeat(101));

        List<Map.Entry<HuyenLamDong, Integer>> sorted = sapXepTheoSoTram(demKhuVuc);

        int rank = 1;
        for (Map.Entry<HuyenLamDong, Integer> entry : sorted) {
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

        kvCaoNhat.ifPresent(kv -> {
            System.out.println("\n[KET LUAN] Khu vuc co tan xuat su dung cao nhat:");
            System.out.println("  - Khu vuc: " + kv.getTen());
            System.out.println("  - So tram: " + demKhuVuc.get(kv));
            System.out.println("  - Tong gio su dung: " + String.format("%.1f", tongGioKhuVuc.get(kv)) + " gio");
        });

        System.out.println("=".repeat(101));
    }
}
