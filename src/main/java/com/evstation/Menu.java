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
        System.out.printf("Nhap %d de %s%n", MenuCT.Thoat.ordinal(), "Thoat Chuong Trinh");
        System.out.printf("Nhap %d de %s%n", MenuCT.ThemTruSac.ordinal(), MenuCT.ThemTruSac.name());
        System.out.printf("Nhap %d de %s%n", MenuCT.XemDanhSach.ordinal(), MenuCT.XemDanhSach.name());
        System.out.printf("Nhap %d de %s%n", MenuCT.CapNhatTrangThai.ordinal(), MenuCT.CapNhatTrangThai.name());
        System.out.printf("Nhap %d de %s%n", MenuCT.XoaTruSac.ordinal(), MenuCT.XoaTruSac.name());
        System.out.printf("Nhap %d de %s%n", MenuCT.TimKiem.ordinal(), MenuCT.TimKiem.name());
    }

    // =========================================================
    // ChonMenu: đọc và kiểm tra lựa chọn hợp lệ, trả về enum
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
                // Bỏ qua, lặp lại
            }
        } while (true);

        return MenuCT.values()[chon];
    }

    // =========================================================
    // XuLyMenu: xử lý từng lựa chọn bằng switch-case trên enum
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
    // ChayChuongTrinh: vòng lặp chính của chương trình
    // =========================================================
    public void ChayChuongTrinh() {
        MenuCT chon;

        do {
            // Xóa màn hình
            System.out.print("\033[H\033[2J");
            System.out.flush();

            XuatMenu();
            chon = ChonMenu();

            if (chon == MenuCT.Thoat)
                break;

            XuLyMenu(chon);

            // Dừng chờ người dùng xem kết quả (tương đương Console.ReadKey())
            System.out.print("\nNhan Enter de tiep tuc...");
            scanner.nextLine();

        } while (true);

        scanner.close();
    }
}
