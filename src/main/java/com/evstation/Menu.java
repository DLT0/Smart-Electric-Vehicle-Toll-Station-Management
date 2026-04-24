package com.evstation;

import java.util.Scanner;

public class Menu {

    public enum MenuCT {
        Thoat,
        ThemDSTruSac,
        NhapCoDinh,
        XuatDanhSach,
        CapNhatThongTinTram,
        XoaTruSac,
        TimKiem,
        TimTramThuN,
        ThongKeTruSacCanBaoTri,
        TinhChiPhi1Tram,
        SapXepDS,
        TinhChiPhiDS,

    }

    public enum MenuThongKe {
        Thoat,
        BaoTri,
        GioSDThap,
        KhuVucCao
    }
    //prive int thong ke tru

    private Scanner scanner;
    public QuanLyTramSac module;

    public Menu(QuanLyTramSac module) {
        this.scanner = new Scanner(System.in);
        this.module = module;
    }

    public static void xuatMenu() {
        System.out.println("=".repeat(50));
        System.out.println("|      QUAN LY TRAM SAC XE DIEN THONG MINH      |");
        System.out.println("=".repeat(50));
        System.out.println("| Phim | Chuc nang                              |");
        System.out.println("-".repeat(50));
        System.out.printf("|   %-2d  | %-38s |%n", MenuCT.Thoat.ordinal(), "Thoat Chuong Trinh.");
        System.out.printf("|   %-2d  | %-38s |%n", MenuCT.ThemDSTruSac.ordinal(), "Nhap danh sach tru sac.");
        System.out.printf("|   %-2d  | %-38s |%n", MenuCT.NhapCoDinh.ordinal(), "Nhap co dinh.");
        System.out.printf("|   %-2d  | %-38s |%n", MenuCT.XuatDanhSach.ordinal(), "Xuat danh sach tru sac.");
        System.out.printf("|   %-2d  | %-38s |%n", MenuCT.CapNhatThongTinTram.ordinal(), "Cap nhat thong tin tram.");
        System.out.printf("|   %-2d  | %-38s |%n", MenuCT.XoaTruSac.ordinal(), "Xoa tru sac.");
        System.out.printf("|   %-2d  | %-38s |%n", MenuCT.TimKiem.ordinal(), "Tim kiem theo ID.");
        System.out.printf("|   %-2d  | %-38s |%n", MenuCT.TimTramThuN.ordinal(), "Tim tram co thoi gian sac thu n. (Min heap)");
        System.out.printf("|   %-2d  | %-38s |%n", MenuCT.ThongKeTruSacCanBaoTri.ordinal(), "Thong ke danh sach tru sac can bao tri");
        System.out.printf("|   %-2d  | %-38s |%n", MenuCT.TinhChiPhi1Tram.ordinal(), "Tinh chi phi du kien cho 1 tram.");
        System.out.printf("|   %-2d  | %-38s |%n", MenuCT.SapXepDS.ordinal(), "Sap xep danh sach tru sac");
        System.out.printf("|   %-2d  | %-38s |%n", MenuCT.TinhChiPhiDS.ordinal(), "Goi y chi phi cho tat ca cac tram.");

        System.out.println("=".repeat(50));
    }

    public static void xuatMenuThongKe() {
        System.out.println("=".repeat(50));
        System.out.println("|        MENU PHU - THONG KE TRAM SAC          |");
        System.out.println("=".repeat(50));
        System.out.println("| Phim | Chuc nang thong ke                    |");
        System.out.println("-".repeat(50));
        System.out.printf("|   %-2d  | %-37s |%n", MenuThongKe.Thoat.ordinal(), "Quay lai menu chinh.");
        System.out.printf("|   %-2d  | %-37s |%n", MenuThongKe.BaoTri.ordinal(), "Tram sac can bao tri .");
        System.out.printf("|   %-2d  | %-37s |%n", MenuThongKe.GioSDThap.ordinal(), "Tram sac co gio SD > X.");
        System.out.printf("|   %-2d  | %-37s |%n", MenuThongKe.KhuVucCao.ordinal(), "Khu vuc tan xuat cao nhat.");
        System.out.println("=".repeat(50));
    }

    public MenuCT chonMenu() {
        int min = MenuCT.Thoat.ordinal();
        int max = MenuCT.values()[MenuCT.values().length - 1].ordinal();
        int chon;

        do {
            System.out.printf("Nhap chon (%d <= chon <= %d): ", min, max);
            try {
                chon = Integer.parseInt(scanner.nextLine().trim());
                if (min <= chon && chon <= max) {
                    break;
                }
            } catch (NumberFormatException e) {
                // Bo qua, lap lai
            }
        } while (true);

        return MenuCT.values()[chon];
    }

    public MenuThongKe chonMenuThongKe() {
        int min = MenuThongKe.Thoat.ordinal();
        int max = MenuThongKe.values()[MenuThongKe.values().length - 1].ordinal();
        int chon;

        do {
            System.out.printf("Nhap chon (%d <= chon <= %d): ", min, max);
            try {
                chon = Integer.parseInt(scanner.nextLine().trim());
                if (min <= chon && chon <= max) {
                    break;
                }
            } catch (NumberFormatException e) {
                // Bo qua, lap lai
            }
        } while (true);

        return MenuThongKe.values()[chon];
    }

    public void xuLyMenu(MenuCT chon) {
        switch (chon) {
            case SapXepDS:
                System.out.println("\n--- SAP XEP DANH SACH TRAM SAC THEO DO HAO MON ---");
                module.sapXepDS();
                break;
            case Thoat:
                System.out.println("Thoat chuong trinh!");
                break;
            case ThemDSTruSac:
                System.out.println("\n--- THEM DANH SACH TRAM SAC ---");
                module.themDSTruSac(scanner);
                break;
            case NhapCoDinh:
                System.out.println("\n--- NHAP CO DINH (DU LIEU MAU CO SAN) ---");
                module.nhapCoDinh(scanner);
                break;
            case XuatDanhSach:
                module.xuatDanhSach();
                break;
            case CapNhatThongTinTram:
                System.out.println("\n--- CAP NHAT THONG TIN TRAM ---");
                System.out.println("\nDanh sach tru sac hien tai:");
                module.xuatDanhSach();
                module.capNhatThongTinTram(scanner);
                break;
            case XoaTruSac:
                System.out.println("\n--- XOA TRAM SAC ---");
                System.out.println("\nDanh sach tru sac hien tai:");
                module.xuatDanhSach();
                module.xoaTruSac(scanner);

                System.out.println("\nDanh sach tru sac sau khi xoa:");
                module.xuatDanhSach();
                break;
            case TimKiem:
                System.out.println("\n--- TIM KIEM THONG TIN TRAM THEO ID TRAM SAC ---");
                module.xuatDanhSach();
                module.timKiem(scanner);
                break;
            case ThongKeTruSacCanBaoTri:
                xuLyThongKe();
                break;
            case TinhChiPhi1Tram:
                System.out.println("\n--- TINH CHI PHI DU KIEN CHO 1 TRAM ---");
                System.out.println("\nDanh sach tru sac hien tai:");
                module.xuatDanhSach();
                module.tinhChiPhi1Tram(scanner);
                break;
            case TinhChiPhiDS:
                System.out.println("\n--- GOI Y CHI PHI CHO TAT CA CAC TRAM ---");
                module.tinhChiPhiDS(scanner);
                break;
            case TimTramThuN:
                System.out.println("\n--- TIM TRAM CO THOI GIAN SAC DU TINH DUNG THU N ---");
                module.timTramTheoThoiGianSacThuN(scanner);
                break;
            default:
                break;
        }
    }

    public void xuLyThongKe() {
        MenuThongKe chon;

        do {
            xuatMenuThongKe();
            chon = chonMenuThongKe();

            if (chon == MenuThongKe.Thoat) {
                System.out.println("=> Quay lai menu chinh.");
                break;
            }

            xuLyMenuThongKe(chon);

            // Dung cho nguoi dung xem ket qua
            System.out.print("\nNhan Enter de tiep tuc...");
            scanner.nextLine();

        } while (true);
    }

    public void xuLyMenuThongKe(MenuThongKe chon) {
        switch (chon) {
            case Thoat:
                System.out.println("=> Quay lai menu chinh.");
                break;
            case BaoTri:
                System.out.println("\n--- THONG KE TRAM SAC CAN BAO TRI  ---");
                module.thongKeBaoTri();
                break;
            case GioSDThap:
                System.out.println("\n--- THONG KE TRAM SAC CO GIO SU DUNG > X ---");
                module.thongKeGioSDThap(scanner);
                break;
            case KhuVucCao:
                System.out.println("\n--- THONG KE KHU VUC CO TAN XUAT SU DUNG CAO NHAT ---");
                module.thongKeKhuVucCaoNhat();
                break;
            default:
                break;
        }
    }

    public void chayChuongTrinh() {
        MenuCT chon;

        do {
            // Xoa man hinh
            System.out.print("\033[H\033[2J");
            System.out.flush();

            xuatMenu();
            chon = chonMenu();

            if (chon == MenuCT.Thoat) {
                break;
            }

            xuLyMenu(chon);

            // Dung cho nguoi dung xem ket qua
            System.out.print("\nNhan Enter de tiep tuc...");
            scanner.nextLine();

        } while (true);

        scanner.close();
    }
}
