package com.evstation;

import java.util.Scanner;

public class Menu {

    // =========================================================
    // Enum: định nghĩa tất cả lựa chọn menu
    // - Thoat PHẢI đứng ĐẦU (ordinal = 0)
    // - Lựa chọn cuối PHẢI đứng CUỐI (dùng để kiểm tra giới hạn)
    // =========================================================
    public enum MenuCT {
        Thoat,
        ThemTruSac,
        XemDanhSach,
        CapNhatTrangThai,
        XoaTruSac,
        TimKiem
    }

    private Scanner scanner;
    private Module module;

    public Menu(Module module) {
        this.scanner = new Scanner(System.in);
        this.module = module;
    }

    // =========================================================
    // XuatMenu: in danh sách các lựa chọn ra màn hình
    // =========================================================
    public static void XuatMenu() {
        System.out.println("=================================================");
        System.out.println("|       SMART EV TOLL STATION MANAGEMENT        |");
        System.out.println("=================================================");
        System.out.println("| Phim | Chuc nang                              |");
        System.out.println("-------------------------------------------------");
        System.out.printf("|   %d  | %-38s |%n", MenuCT.Thoat.ordinal(), "Thoat Chuong Trinh");
        System.out.printf("|   %d  | %-38s |%n", MenuCT.ThemTruSac.ordinal(), "Them tru sac moi");
        System.out.printf("|   %d  | %-38s |%n", MenuCT.XemDanhSach.ordinal(), "Xem danh sach tru sac");
        System.out.printf("|   %d  | %-38s |%n", MenuCT.CapNhatTrangThai.ordinal(), "Cap nhat trang thai");
        System.out.printf("|   %d  | %-38s |%n", MenuCT.XoaTruSac.ordinal(), "Xoa tru sac");
        System.out.printf("|   %d  | %-38s |%n", MenuCT.TimKiem.ordinal(), "Tim kiem theo ID");
        System.out.println("=================================================");
    }

    // =========================================================
    // ChonMenu: doc va kiem tra lua chon hop le, tra ve enum
    // =========================================================
    public MenuCT ChonMenu() {
        int min = MenuCT.Thoat.ordinal();
        int max = MenuCT.values()[MenuCT.values().length - 1].ordinal();
        int chon;

        do {
            System.out.printf("Nhap chon (%d <= chon <= %d): ", min, max);
            try {
                chon = Integer.parseInt(scanner.nextLine().trim());
                if (min <= chon && chon <= max)
                    break;
            } catch (NumberFormatException e) {
                // Bo qua, lap lai
            }
        } while (true);

        return MenuCT.values()[chon];
    }

    // =========================================================
    // XuLyMenu: xu ly tung lua chon bang switch-case tren enum
    // =========================================================
    public void XuLyMenu(MenuCT chon) {
        switch (chon) {
            case Thoat:
                System.out.println("Thoat chuong trinh!");
                break;
            case ThemTruSac:
                module.themTruSac(scanner);
                break;
            case XemDanhSach:
                module.xemDanhSach();
                break;
            case CapNhatTrangThai:
                module.capNhatTrangThai(scanner);
                break;
            case XoaTruSac:
                module.xoaTruSac(scanner);
                break;
            case TimKiem:
                module.timKiem(scanner);
                break;
            default:
                break;
        }
    }

    // =========================================================
    // ChayChuongTrinh: vong lap chinh cua chuong trinh
    // =========================================================
    public void ChayChuongTrinh() {
        MenuCT chon;

        do {
            // Xoa man hinh
            System.out.print("\033[H\033[2J");
            System.out.flush();

            XuatMenu();
            chon = ChonMenu();

            if (chon == MenuCT.Thoat)
                break;

            XuLyMenu(chon);

            // Dung cho nguoi dung xem ket qua
            System.out.print("\nNhan Enter de tiep tuc...");
            scanner.nextLine();

        } while (true);

        scanner.close();
    }
}
