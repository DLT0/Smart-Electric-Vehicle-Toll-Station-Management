package com.evstation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.stream.Collectors;

public class QuanLyTramSac {

    private final DanhSachTramSac danhSach = new DanhSachTramSac();
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

    private static boolean confirm(Scanner scanner, String prompt) {
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

    private boolean isTenTramTrung(String tenMoi, TramSac ignoreTram) {
        String normalized = tenMoi.trim().toLowerCase();
        for (TramSac t : danhSach) {
            if (t == ignoreTram) {
                continue;
            }
            if (t.getTenTram().trim().toLowerCase().equals(normalized)) {
                return true;
            }
        }
        return false;
    }

    private double promptForDouble(Scanner sc, String prompt, double min, double max, double currentVal) {
        System.out.printf("%s [hien tai: %.1f]: ", prompt, currentVal);
        String input = sc.nextLine().trim();
        if (input.isEmpty()) {
            return currentVal;
        }
        try {
            double val = Double.parseDouble(input);
            if (val >= min && val <= max) {
                return val;
            }
            System.out.printf("!!! Gia tri phai tu %.1f den %.1f. Giu nguyen.%n", min, (max == Double.MAX_VALUE ? 9999.0 : max));
        } catch (NumberFormatException e) {
            System.out.println("!!! Nhap sai dinh dang so. Giu nguyen.");
        }
        return currentVal;
    }

    public TramSac timTramTheoId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        String cleanId = id.replaceAll("[- ]", "").toLowerCase();
        return danhSach.stream()
                .filter(t -> t.getMaTram().replaceAll("[- ]", "").toLowerCase().equals(cleanId))
                .findFirst()
                .orElse(null);
    }

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

    private HuyenLamDong chonKhuVuc(Scanner scanner) {
        HuyenLamDong.hienThiDanhSach();
        HuyenLamDong khuVuc = null;
        while (khuVuc == null) {
            System.out.print("Chon khu vuc (1-" + HuyenLamDong.values().length + "): ");
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
        // In xac nhan va xoa bang bang separator
        System.out.println("=> Khu vuc: " + khuVuc.getTen());

        return khuVuc;
    }

    private void them1TruSac(Scanner scanner) {
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
        while (true) {
            System.out.print("Nhap cong suat kW (7 <= cs <= 300): ");
            try {
                double cs = Double.parseDouble(scanner.nextLine().trim());
                if (cs >= 7 && cs <= 300) {
                    return cs;
                }
                System.out.println("!!! Cong suat phai tu 7kW den 300kW!");
            } catch (NumberFormatException e) {
                System.out.println("!!! Cong suat phai la mot so!");
            }
        }
    }

    private TramSac themVaoDanhSach(HuyenLamDong khuVuc, double cs) {
        int countAtLocation = countStationsAtLocation(khuVuc);
        String id = sinhMaTram(khuVuc, cs, countAtLocation);
        int stt = countAtLocation + 1;
        TramSac moi;
        if (cs <= 11) {
            moi = new TramSacCham(id, khuVuc, cs, stt);
            System.out.println("=> [" + id + "] Sac Cham (7-11kW) - " + khuVuc.getTen() + " da duoc them!");
        } else if (cs <= 120) {
            moi = new TramSacNhanh(id, khuVuc, cs, stt);
            System.out.println("=> [" + id + "] Sac Nhanh (12-120kW) - " + khuVuc.getTen() + " da duoc them!");
        } else {
            moi = new TramSacSieuNhanh(id, khuVuc, cs, stt);
            System.out.println("=> [" + id + "] Sac Sieu Nhanh (121-300kW) - " + khuVuc.getTen() + " da duoc them!");
        }
        danhSach.add(moi);
        return moi;
    }

    private int countStationsAtLocation(HuyenLamDong khuVuc) {
        return (int) danhSach.stream()
                .filter(t -> t.getViTri() == khuVuc)
                .count();
    }

    public void themDSTruSac(Scanner scanner) {
        int tongSo;
        while (true) {
            System.out.print("Nhap so luong tram sac can them (>= 1): ");
            try {
                tongSo = Integer.parseInt(scanner.nextLine().trim());
                if (tongSo > 0) {
                    break;
                }
                System.out.println("!!! So luong phai la so nguyen duong!");
            } catch (NumberFormatException e) {
                System.out.println("!!! Phai nhap mot so nguyen!");
            }
        }
        for (int i = 1; i <= tongSo; i++) {
            System.out.println("\n  [ Tram " + i + " / " + tongSo + " ]");
            them1TruSac(scanner);
        }
        if (tongSo > 1) {
            System.out.println("\n=> Da them xong " + tongSo + " tram sac!");
        }
    }

    public void nhapCoDinh() {
        danhSach.clear();
        // 1. DA LAT
        themVaoDanhSach(HuyenLamDong.DA_LAT, 7.2); // SC-DAL-001
        themVaoDanhSach(HuyenLamDong.DA_LAT, 60); // SN-DAL-001
        themVaoDanhSach(HuyenLamDong.DA_LAT, 250); // SS-DAL-001
        themVaoDanhSach(HuyenLamDong.DA_LAT, 300); // SS-DAL-002

        // 2. BAO LOC
        themVaoDanhSach(HuyenLamDong.BAO_LOC, 11); // SC-BAO-001
        themVaoDanhSach(HuyenLamDong.BAO_LOC, 120); // SN-BAO-001
        themVaoDanhSach(HuyenLamDong.BAO_LOC, 20);

        // 3. DUC TRONG
        themVaoDanhSach(HuyenLamDong.DUC_TRONG, 30); // SN-DUC-001
        themVaoDanhSach(HuyenLamDong.DUC_TRONG, 150); // SS-DUC-001

        // 4. DI LINH
        themVaoDanhSach(HuyenLamDong.DI_LINH, 60); // SN-DIL-001
        themVaoDanhSach(HuyenLamDong.DI_LINH, 200); // SS-DIL-001

        // 5. CAC KHU VUC KHAC
        themVaoDanhSach(HuyenLamDong.LAM_HA, 30); // SN-LAM-001
        themVaoDanhSach(HuyenLamDong.DON_DUONG, 150); // SS-DON-001
        themVaoDanhSach(HuyenLamDong.BAO_LAM, 11); // SC-BAL-001
        themVaoDanhSach(HuyenLamDong.DAM_RONG, 60); // SN-DAM-001

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

    public void xuatDanhSach() {
        System.out.println(danhSach);
    }

    public void capNhatThongTinTram(Scanner scanner) {

        // 1. Tim tram theo ID
        System.out.print("Nhap ID tram can cap nhat: ");
        String id = scanner.nextLine().trim();
        TramSac found = timTramTheoId(id);
        if (found == null) {
            System.out.println("!!! Khong tim thay tram voi ID '" + id + "'");
            return;
        }

        // 2. Hien thi thong tin hien tai
        System.out.println("\nThong tin hien tai:");
        DanhSachTramSac.xuatThongTin1Tram(found);

        // 3. Shadow values (chua ap dung vao doi tuong)
        String newTen = found.getTenTram();
        HuyenLamDong newViTri = found.getViTri();
        double newCongSuat = found.getCongSuat();
        Boolean newSanSang = null; // null = nguoi dung chua chon doi truong nay
        double newThoiGianHD = found.getThoiGianHoatDong();
        double newHanBaoTri = found.getHanBaoTri();

        // 4. Vong lap chinh sua theo tung truong
        while (true) {
            System.out.println("\n--- CHINH SUA TRAM [" + found.getMaTram() + "] (* = da thay doi) ---");
            System.out.println("  1. Ten tram    : " + newTen + (newTen.equals(found.getTenTram()) ? "" : " (*)"));
            System.out
                    .println("  2. Vi tri      : " + newViTri.getTen() + (newViTri == found.getViTri() ? "" : " (*)"));
            System.out.printf("  3. Cong suat   : %.1f kW%s%n", newCongSuat,
                    newCongSuat == found.getCongSuat() ? "" : " (*)");
            String ttHienThi = newSanSang == null
                    ? found.getTrangThaiHoatDong()
                    : (newSanSang ? "San sang" : "Dang sac") + " (*)";
            System.out.println("  4. Trang thai  : " + ttHienThi);
            System.out.printf("  5. Thoi gian HD: %.1f gio%s%n", newThoiGianHD,
                    newThoiGianHD == found.getThoiGianHoatDong() ? "" : " (*)");
            System.out.printf("  6. Han bao tri : %.1f gio%s%n", newHanBaoTri,
                    newHanBaoTri == found.getHanBaoTri() ? "" : " (*)");
            System.out.println("  0. Luu thay doi");
            System.out.println("  9. Huy bo (khong luu)");
            System.out.print("Chon: ");

            int chon;
            try {
                chon = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("!!! Phai nhap so.");
                continue;
            }

            if (chon == 9) {
                System.out.println("=> Huy. Khong co thay doi nao duoc luu.");
                return;
            }
            if (chon == 0) {
                break;
            }

            switch (chon) {
                case 1 -> { // Ten tram
                    System.out.print("Ten tram moi (Enter = giu nguyen): ");
                    String tenInp = scanner.nextLine().trim();
                    if (!tenInp.isEmpty()) {
                        if (isTenTramTrung(tenInp, found)) {
                            System.out.println("!!! Ten nay da ton tai trong he thong! Chon ten khac.");
                        } else {
                            newTen = tenInp;
                            System.out.println("=> Ghi nhan: ten = \"" + newTen + "\"");
                        }
                    }
                }

                case 2 -> { // Vi tri
                    HuyenLamDong.hienThiDanhSach();
                    System.out.print("Chon vi tri (1-" + HuyenLamDong.values().length + ", Enter = giu nguyen): ");
                    String vtInp = scanner.nextLine().trim();
                    if (!vtInp.isEmpty()) {
                        try {
                            HuyenLamDong vt = HuyenLamDong.layTheoSoThuTu(Integer.parseInt(vtInp));
                            if (vt == null) {
                                System.out.println("!!! So thu tu khong hop le!");
                            } else {
                                newViTri = vt;
                                System.out.println("[!] Luu y: maTram KHONG tu dong doi khi thay vi tri.");
                                System.out.println("=> Ghi nhan: vi tri = " + newViTri.getTen());
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("!!! Phai nhap so nguyen.");
                        }
                    }
                }

                case 3 -> { // Cong suat
                    double csMoi = promptForDouble(scanner, "Cong suat moi kW (7-300)", 7, 300, newCongSuat);
                    if (csMoi != newCongSuat) {
                        newCongSuat = csMoi;
                        System.out.println("[!] Luu y: maTram/loai tram KHONG tu dong doi khi thay cong suat.");
                        System.out.printf("=> Ghi nhan: cong suat = %.1f kW%n", newCongSuat);
                    }
                }

                case 4 -> { // Trang thai
                    System.out.println("  1. San sang (Trong)    2. Dang sac (Hoat dong)");
                    System.out.print("Chon (1/2, Enter = giu nguyen): ");
                    String ttInp = scanner.nextLine().trim();
                    if (!ttInp.isEmpty()) {
                        switch (ttInp) {
                            case "1" -> {
                                newSanSang = true;
                                System.out.println("=> Ghi nhan: San sang");
                            }
                            case "2" -> {
                                newSanSang = false;
                                System.out.println("=> Ghi nhan: Dang sac");
                            }
                            default ->
                                System.out.println("!!! Nhap 1 hoac 2.");
                        }
                    }
                }

                case 5 -> { // Thoi gian hoat dong
                    double tgMoi = promptForDouble(scanner, "Thoi gian hoat dong (h >= 0)", 0, Double.MAX_VALUE,
                            newThoiGianHD);
                    if (tgMoi != newThoiGianHD) {
                        newThoiGianHD = tgMoi;
                        System.out.printf("=> Ghi nhan: thoi gian HD = %.1f gio%n", newThoiGianHD);
                    }
                }

                case 6 -> { // Han bao tri
                    double hanMoi = promptForDouble(scanner, "Han bao tri (h, 1-10000)", 1, 10000, newHanBaoTri);
                    if (hanMoi != newHanBaoTri) {
                        newHanBaoTri = hanMoi;
                        System.out.printf("=> Ghi nhan: han bao tri = %.1f gio%n", newHanBaoTri);
                    }
                }

                default ->
                    System.out.println("!!! Lua chon khong hop le. Nhap 0-6 hoac 9.");
            }
        }

        // 5. Kiem tra co thay doi khong
        boolean hasChanges = !newTen.equals(found.getTenTram())
                || newViTri != found.getViTri()
                || newCongSuat != found.getCongSuat()
                || newSanSang != null
                || newThoiGianHD != found.getThoiGianHoatDong()
                || newHanBaoTri != found.getHanBaoTri();

        if (!hasChanges) {
            System.out.println("=> Khong co thay doi nao duoc thuc hien.");
            return;
        }

        // 6. Hien thi tom tat thay doi
        System.out.println("\n--- TOM TAT THAY DOI ---");
        if (!newTen.equals(found.getTenTram())) {
            System.out.println("  Ten tram    : \"" + found.getTenTram() + "\" -> \"" + newTen + "\"");
        }
        if (newViTri != found.getViTri()) {
            System.out.println("  Vi tri      : " + found.getViTri().getTen() + " -> " + newViTri.getTen());
        }
        if (newCongSuat != found.getCongSuat()) {
            System.out.printf("  Cong suat   : %.1f -> %.1f kW%n", found.getCongSuat(), newCongSuat);
        }
        if (newSanSang != null) {
            System.out.println("  Trang thai  : " + found.getTrangThaiHoatDong() + " -> "
                    + (newSanSang ? "San sang" : "Dang sac"));
        }
        if (newThoiGianHD != found.getThoiGianHoatDong()) {
            System.out.printf("  Thoi gian HD: %.1f -> %.1f gio%n", found.getThoiGianHoatDong(), newThoiGianHD);
        }
        if (newHanBaoTri != found.getHanBaoTri()) {
            System.out.printf("  Han bao tri : %.1f -> %.1f gio%n", found.getHanBaoTri(), newHanBaoTri);
        }

        if (!confirm(scanner, "Xac nhan luu toan bo thay doi?")) {
            System.out.println("=> Huy. Khong co thay doi nao duoc luu.");
            return;
        }

        // 7. Ap dung thay doi (trang thai truoc, sau do ghi de thoiGianHD theo y nguoi
        // dung)
        found.setTenTram(newTen);
        found.setViTri(newViTri);
        found.setCongSuat(newCongSuat);
        found.setHanBaoTri(newHanBaoTri);

        if (newSanSang != null) {
            if (newSanSang && !found.isSanSang() && found.getThoiGianBatDauSac() != null) {
                // Sac -> San sang: cong don gio da sac
                long phutDaSac = DanhSachTramSac.tinhThoiGianSacPhut(found.getThoiGianBatDauSac(), LocalDateTime.now());
                found.setThoiGianHoatDong(found.getThoiGianHoatDong() + (phutDaSac / 60.0));
                found.setThoiGianBatDauSac(null);
            } else if (!newSanSang && found.isSanSang()) {
                // San sang -> Sac: ghi nhan thoi diem bat dau
                found.setThoiGianBatDauSac(LocalDateTime.now());
            }
            found.setSanSang(newSanSang);
        }

        // Neu nguoi dung chinh sua truong ThoiGianHD, gia tri do ghi de moi tinh toan
        // tu dong
        if (newThoiGianHD != found.getThoiGianHoatDong()) {
            found.setThoiGianHoatDong(newThoiGianHD);
        }

        System.out.println("==> Cap nhat thong tin tram thanh cong!");
        DanhSachTramSac.xuatThongTin1Tram(found);
    }

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
        DanhSachTramSac.xuatThongTin1Tram(found);

        if (confirm(scanner, "Xac nhan xoa tram nay ?")) {
            danhSach.remove(found);
            System.out.println("=> Da xoa thanh cong tram ID: " + id);
        } else {
            System.out.println("=> Huy thao tac xoa.");
        }
    }

    private List<TramSac> timKiemDanhSach(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return Collections.emptyList();
        }

        TramSac exactMatch = timTramTheoId(keyword);
        if (exactMatch != null) {
            return Collections.singletonList(exactMatch);
        }

        String kw = keyword.trim().toLowerCase();
        return danhSach.stream()
                .filter(t -> t.getMaTram().toLowerCase().contains(kw)
                || t.getTenTram().toLowerCase().contains(kw)
                || t.getLoaiPrefix().toLowerCase().contains(kw)
                || t.getViTri().getTen().toLowerCase().contains(kw)
                || t.getViTri().name().toLowerCase().replace("_", " ").contains(kw))
                .collect(Collectors.toList());
    }

    public void timKiem(Scanner scanner) {

        if (danhSach.isEmpty()) {
            System.out.println("!!! Danh sach trong. Khong co tram nao.");
            return;
        }

        System.out.print("Nhap tu khoa tim kiem (ID / ten / khu vuc / loai): ");
        String keyword = scanner.nextLine().trim();
        if (keyword.isEmpty()) {
            System.out.println("!!! Tu khoa khong duoc de trong.");
            return;
        }

        List<TramSac> ketQua = timKiemDanhSach(keyword);

        if (ketQua.isEmpty()) {
            System.out.println("!!! Khong tim thay tram nao khop voi: \"" + keyword + "\"");
            return;
        }

        System.out.println("\n==> Tim thay " + ketQua.size() + " ket qua cho \"" + keyword + "\":");
        DanhSachTramSac.xuatBang(ketQua);
        System.out.println();
    }

    public void thongKeBaoTri() {
        List<TramSac> tramBaoTri = danhSach.stream()
                .filter(t -> t.tinhMucHaoMon() > 90)
                .collect(Collectors.toList());

        System.out.println("\n" + "=".repeat(36) + " THONG KE TRAM CAN BAO TRI " + "=".repeat(36));
        System.out.println("(Tram co muc haomon > 90% cua han bao tri 500h)");

        if (tramBaoTri.isEmpty()) {
            System.out.println("\n✓ Tat ca cac tram deu co tinh trang tot. Khong co tram nao can bao tri.");
        } else {
            System.out.println("\n[CANH BAO] Co " + tramBaoTri.size() + " tram can bao tri:");
            System.out.println("-".repeat(21));
            DanhSachTramSac.xuatBang(tramBaoTri);
            System.out.println("\nChi tiet hao mon:");
            for (TramSac t : tramBaoTri) {
                double mucHaoMon = t.tinhMucHaoMon();
                System.out.println("  [" + t.getMaTram() + "] " + t.getTenTram()
                        + " - Hao mon: " + String.format("%.1f", mucHaoMon) + "% (Da su dung: "
                        + String.format("%.1f", t.getThoiGianHoatDong()) + "h / 500h)");
            }
        }
    }

    public void thongKeGioSDThap(Scanner scanner) {
        System.out.print("\nNhap so gio su dung toi thieu (h): ");

        final double gioMin;
        try {
            double parsedGioMin = Double.parseDouble(scanner.nextLine().trim());
            if (parsedGioMin < 0) {
                System.out.println("!!! So gio khong duoc am!");
                return;
            }
            gioMin = parsedGioMin;
        } catch (NumberFormatException e) {
            System.out.println("!!! Phai nhap mot so!");
            return;
        }

        List<TramSac> tramCao = danhSach.stream()
                .filter(t -> {
                    double tong = t.getThoiGianHoatDong();
                    if (!t.isSanSang() && t.getThoiGianBatDauSac() != null) {
                        tong += DanhSachTramSac.tinhThoiGianSacPhut(t.getThoiGianBatDauSac(), LocalDateTime.now()) / 60.0;
                    }
                    return tong > gioMin;
                })
                .sorted((a, b) -> {
                    double tgA = a.getThoiGianHoatDong() + (a.isSanSang() ? 0 : DanhSachTramSac.tinhThoiGianSacPhut(a.getThoiGianBatDauSac(), LocalDateTime.now()) / 60.0);
                    double tgB = b.getThoiGianHoatDong() + (b.isSanSang() ? 0 : DanhSachTramSac.tinhThoiGianSacPhut(b.getThoiGianBatDauSac(), LocalDateTime.now()) / 60.0);
                    return Double.compare(tgB, tgA);
                })
                .collect(Collectors.toList());

        System.out.println("\nCo " + tramCao.size() + " tram co so gio su dung > " + gioMin + "h:");

        DanhSachTramSac.xuatBangVoiTongGio(tramCao);
    }

    public void thongKeKhuVucCaoNhat() {
        if (danhSach.isEmpty()) {
            System.out.println("!!! Danh sach trong. Khong co du lieu de thong ke.");
            return;
        }

        Map<HuyenLamDong, Integer> demKhuVuc = demTramTheoKhuVuc();
        Map<HuyenLamDong, Double> tongGioKhuVuc = tongGioTheoKhuVuc();
        Optional<HuyenLamDong> kvCaoNhat = timKhuVucCaoNhat(demKhuVuc);

        System.out.println("\n" + "=".repeat(26) + " THONG KE KHU VUC CO TAN SUAT SU DUNG CAO NHAT " + "=".repeat(26));
        System.out.printf("| %-40s | So Tram | Tong Gio Su Dung (h)| Ty Le %%    |%n", "Ten Khu Vuc");
        System.out.println("-".repeat(101));

        for (Map.Entry<HuyenLamDong, Integer> entry : sapXepTheoSoTram(demKhuVuc)) {
            HuyenLamDong kv = entry.getKey();
            int soTram = entry.getValue();
            double tongGio = tongGioKhuVuc.get(kv);
            double tyLe = (soTram * 100.0) / danhSach.size();
            System.out.printf("| %-40s | %-7d | %-19.1f | %-9.1f%% |%n",
                    kv.getTen(), soTram, tongGio, tyLe);
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

    private Map<HuyenLamDong, Integer> demTramTheoKhuVuc() {
        Map<HuyenLamDong, Integer> dem = new LinkedHashMap<>();
        for (HuyenLamDong kv : HuyenLamDong.values()) {
            dem.put(kv, 0);
        }
        for (TramSac t : danhSach) {
            dem.merge(t.getViTri(), 1, Integer::sum);
        }
        return dem;
    }

    private Map<HuyenLamDong, Double> tongGioTheoKhuVuc() {
        Map<HuyenLamDong, Double> tongGio = new LinkedHashMap<>();
        for (HuyenLamDong kv : HuyenLamDong.values()) {
            tongGio.put(kv, 0.0);
        }
        for (TramSac t : danhSach) {
            tongGio.merge(t.getViTri(), t.getThoiGianHoatDong(), Double::sum);
        }
        return tongGio;
    }

    private Optional<HuyenLamDong> timKhuVucCaoNhat(Map<HuyenLamDong, Integer> dem) {
        return dem.entrySet().stream()
                .filter(e -> e.getValue() > 0)
                .max((a, b) -> Integer.compare(a.getValue(), b.getValue()))
                .map(Map.Entry::getKey);
    }

    private List<Map.Entry<HuyenLamDong, Integer>> sapXepTheoSoTram(Map<HuyenLamDong, Integer> demKhuVuc) {
        List<Map.Entry<HuyenLamDong, Integer>> sorted = new ArrayList<>(demKhuVuc.entrySet());
        Collections.sort(sorted, (a, b) -> Integer.compare(b.getValue(), a.getValue()));
        return sorted;
    }

    public void tinhChiPhi1Tram(Scanner scanner) {
        System.out.print("Nhap ID tram can xem chi phi: ");
        String id = scanner.nextLine().trim();

        TramSac found = timTramTheoId(id);

        if (found == null) {
            System.out.println("!!! Khong tim thay tram co ID: " + id);
            return;
        }

        double dungLuongPin = 70.0;
        double phanTramPin = 80;
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
        if (found instanceof TramSacNhanh) {
            donGiaPhuPhi = 1000;
        } else if (found instanceof TramSacSieuNhanh) {
            donGiaPhuPhi = 3000;
        }

        if (isThucTe) {
            // Tính toán thời gian thực tế cho trạm đang sạc
            thoiGianDaSacPhut = DanhSachTramSac.tinhThoiGianSacPhut(found.getThoiGianBatDauSac(), LocalDateTime.now());
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
            System.out.println("- Thoi gian da sac: " + DanhSachTramSac.dinhDangThoiGian(thoiGianDaSacPhut));
            System.out.println("- Thoi gian tieu chuan: " + DanhSachTramSac.dinhDangThoiGian(thoiGianDuKienPhut));
        } else {
            System.out.println("- Thoi gian sac du kien: " + DanhSachTramSac.dinhDangThoiGian(thoiGianDuKienPhut));
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
        String line = "+" + "-".repeat(18) + "+" + "-".repeat(12) + "+" + "-".repeat(11) + "+" + "-".repeat(17) + "+" + "-".repeat(17) + "+";
        System.out.println(line);
        System.out.printf("| %-16s | %-10s | %-9s | %-15s | %-15s |%n",
                "Loai", "ID", "Cong Suat", "Thoi gian sac", "Tien qua han/p");
        System.out.println(line);

        for (TramSac t : danhSach) {
            double thoiGianGio = dienNangCanSac / t.getCongSuat();
            long thoiGianPhut = (long) (thoiGianGio * 60);
            String thoiGianStr = DanhSachTramSac.dinhDangThoiGian(thoiGianPhut);

            double phiQuaHan = 0;
            if (t instanceof TramSacNhanh) {
                phiQuaHan = 1000;
            } else if (t instanceof TramSacSieuNhanh) {
                phiQuaHan = 3000;
            }

            String phiQuaHanStr = (phiQuaHan > 0 && !(t instanceof TramSacCham)) ? String.format("%,.0f", phiQuaHan)
                    : "0";

            System.out.printf("| %-16s | %-10s | %6.1f kW | %-15s | %15s |%n",
                    t.getLoaiPrefix(), t.getMaTram(), t.getCongSuat(), thoiGianStr, phiQuaHanStr);
        }
        System.out.println(line);
        System.out.println("* Lưu ý: Chi phí cuối cùng = Chi phí điện + (Tiền quá hạn/phút * Số phút quá hạn)");
    }

    public void sapXepDS() {
        if (danhSach.isEmpty()) {
            System.out.println("!!! Danh sach trong. Khong co tram nao de sap xep.");
            return;
        }

        List<TramSac> sorted = new ArrayList<>(danhSach);
        sorted.sort((a, b) -> {
            int pA = a.tinhMucHaoMon() >= 100.0 ? 2 : (a.isSanSang() ? 0 : 1);
            int pB = b.tinhMucHaoMon() >= 100.0 ? 2 : (b.isSanSang() ? 0 : 1);

            if (pA != pB) {
                return Integer.compare(pA, pB);
            }

            int byHours = Double.compare(a.getThoiGianHoatDong(), b.getThoiGianHoatDong());
            if (byHours != 0) {
                return byHours;
            }

            return a.getMaTram().compareTo(b.getMaTram());
        });

        System.out.println("\n" + "=".repeat(40) + " DANH SACH (DA SAP XEP) " + "=".repeat(40));
        DanhSachTramSac.xuatBang(sorted);
    }

    public void timTramTheoThoiGianSacThuN(Scanner scanner) {
        if (danhSach.isEmpty()) {
            System.out.println("!!! Danh sach trong. Khong co tram nao.");
            return;
        }

        // Nhap n tu ban phim
        int n = 0;
        while (n < 1 || n > danhSach.size()) {
            System.out.printf("Nhap n (1 <= n <= %d): ", danhSach.size());
            try {
                n = Integer.parseInt(scanner.nextLine().trim());
                if (n < 1 || n > danhSach.size()) {
                    System.out.println("!!! n phai nam trong khoang 1 den " + danhSach.size() + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("!!! Phai nhap mot so nguyen!");
            }
        }

        final double DUNG_LUONG_PIN = 70.0;
        final double PHAN_TRAM_PIN = 80.0;
        final double DIEN_NANG_CAN_SAC = DUNG_LUONG_PIN * (PHAN_TRAM_PIN / 100.0);

        PriorityQueue<TramSac> minHeap = new PriorityQueue<>((a, b) -> {
            long tgA = (long) (DIEN_NANG_CAN_SAC / a.getCongSuat() * 60);
            long tgB = (long) (DIEN_NANG_CAN_SAC / b.getCongSuat() * 60);
            if (tgA != tgB) {
                return Long.compare(tgA, tgB);
            }
            return a.getMaTram().compareTo(b.getMaTram());
        });

        minHeap.addAll(danhSach);

        TramSac ketQua = null;
        for (int i = 0; i < n; i++) {
            ketQua = minHeap.poll();
        }
        if (ketQua == null) {
            System.out.println("!!! Khong tim duoc ket qua phu hop.");
            return;
        }

        long thoiGianPhut = (long) (DIEN_NANG_CAN_SAC / ketQua.getCongSuat() * 60);

        System.out.println("\n" + "=".repeat(35) + " KET QUA TIM KIEM " + "=".repeat(35));
        System.out.printf("=> Tram co thoi gian sac du tinh dung thu %d (giam dan): [%s]%n", n, ketQua.getMaTram());
        System.out.printf("   Gia dinh xe 70 kWh, sac %.0f%%: %.1f kWh | Cong suat: %.1f kW%n",
                PHAN_TRAM_PIN, DIEN_NANG_CAN_SAC, ketQua.getCongSuat());
        System.out.printf("   Thoi gian sac du tinh     : %s%n", DanhSachTramSac.dinhDangThoiGian(thoiGianPhut));
        System.out.println("-".repeat(88));
        DanhSachTramSac.xuatThongTin1Tram(ketQua);
        System.out.println("=".repeat(88));
    }
}
