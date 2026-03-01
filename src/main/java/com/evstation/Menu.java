package com.evstation;

import java.util.*;

public class Menu {

    public enum MenuCT {
        Thoat,
        Them1TruSac,
        ThemDSTruSac,
        TuDongNhapDSTruSac,
        XuatDanhSach,
        CapNhatTrangThai,
        XoaTruSac,
        TimKiem,
        ThongKeTruSacCanBaoTri,
        XuatFile
    }

    private Scanner scanner;
    private Module module;

    public Menu(Module module) {
        this.scanner = new Scanner(System.in);
        this.module = module;
    }

    public static void XuatMenu() {
        System.out.println("=================================================");
        System.out.println("|       SMART EV TOLL STATION MANAGEMENT        |");
        System.out.println("=================================================");
        System.out.println("| Phim | Chuc nang                              |");
        System.out.println("-------------------------------------------------");
        System.out.printf("|   %d  | %-38s |%n", MenuCT.Thoat.ordinal(), "Thoat Chuong Trinh.");
        System.out.printf("|   %d  | %-38s |%n", MenuCT.Them1TruSac.ordinal(), "Them 1 tru sac moi.");
        System.out.printf("|   %d  | %-38s |%n", MenuCT.ThemDSTruSac.ordinal(), "Them danh sach tru sac.");
        System.out.printf("|   %d  | %-38s |%n", MenuCT.TuDongNhapDSTruSac.ordinal(),
                "Tu dong nhap danh sach tru sac.");
        System.out.printf("|   %d  | %-38s |%n", MenuCT.XuatDanhSach.ordinal(), "Xem danh sach tru sac.");
        System.out.printf("|   %d  | %-38s |%n", MenuCT.CapNhatTrangThai.ordinal(), "Cap nhat trang thai.");
        System.out.printf("|   %d  | %-38s |%n", MenuCT.XoaTruSac.ordinal(), "Xoa tru sac.");
        System.out.printf("|   %d  | %-38s |%n", MenuCT.TimKiem.ordinal(), "Tim kiem theo ID.");
        System.out.printf("|   %d  | %-38s |%n", MenuCT.ThongKeTruSacCanBaoTri.ordinal(),
                "Thong ke danh sach tru sac can bao tri");
        System.out.printf("|   %d  | %-38s |%n", MenuCT.XuatFile.ordinal(), "Xuat danh sach tru sac ra file Exel.");
        System.out.println("=================================================");
    }

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

    public void XuLyMenu(MenuCT chon) {
        switch (chon) {
            case Thoat:
                System.out.println("Thoat chuong trinh!");
                break;
            case Them1TruSac:
                module.them1TruSac(scanner);
                break;
            case ThemDSTruSac:
                // module.themDSTruSac(scanner);
                break;
            case TuDongNhapDSTruSac:
                module.khoiTaoDuLieuMau();
                System.out.println("Da khoi tao du lieu mau!");
                break;
            case XuatDanhSach:
                module.xuatDanhSach();
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
