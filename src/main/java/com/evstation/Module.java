package com.evstation;

import java.util.*;

// Gom các class thực thể vào đây
abstract class TramSac {
    protected String maSo;
    protected String viTri;
    protected double congSuat; // kW
    protected static final double GIA_MOI_KWH = 3850; // Đơn giá trung bình dựa trên tìm hiểu và tính toán

    public TramSac(String maSo, String viTri, double congSuat) {
        this.maSo = maSo;
        this.viTri = viTri;
        this.congSuat = congSuat;
    }

    public double tinhChiPhi(double soGio) {
        return this.congSuat * GIA_MOI_KWH * soGio;
    }

    public abstract void hienThiChiTiet();
}

class TramSacCham extends TramSac {
    public TramSacCham(String maSo, String viTri, double congSuat) {
        super(maSo, viTri, congSuat);
    }

    @Override
    public void hienThiChiTiet() {
        System.out.printf("[Sạc Chậm]      ID: %-5s | Vị trí: %-15s | CS: %5.1f kW | Chi phí: %,10.0f VNĐ/giờ%n",
                maSo, viTri, congSuat, tinhChiPhi(1));
    }
}

class TramSacNhanh extends TramSac {
    public TramSacNhanh(String maSo, String viTri, double congSuat) {
        super(maSo, viTri, congSuat);
    }

    @Override
    public void hienThiChiTiet() {
        System.out.printf("[Sạc Nhanh]     ID: %-5s | Vị trí: %-15s | CS: %5.1f kW | Chi phí: %,10.0f VNĐ/giờ%n",
                maSo, viTri, congSuat, tinhChiPhi(1));
    }
}

class TramSacSieuNhanh extends TramSac {
    public TramSacSieuNhanh(String maSo, String viTri, double congSuat) {
        super(maSo, viTri, congSuat);
    }

    @Override
    public void hienThiChiTiet() {
        System.out.printf("[Sạc Siêu Nhanh] ID: %-5s | Vị trí: %-15s | CS: %5.1f kW | Chi phí: %,10.0f VNĐ/giờ%n",
                maSo, viTri, congSuat, tinhChiPhi(1));
    }
}

public class Module {
    private List<TramSac> danhSach = new ArrayList<>();

    // Chuc nang 1: Them tru sac moi
    public void themTruSac(Scanner scanner) {
        System.out.println("--- THÊM TRỤ SẠC MỚI ---");
        System.out.print("Nhập ID: ");
        String id = scanner.nextLine();
        System.out.print("Nhập vị trí: ");
        String vt = scanner.nextLine();

        System.out.print("Nhập công suất kW (7 <= cs <= 300): ");
        double cs;
        try {
            cs = Double.parseDouble(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Công suất phải là một số!");
            return;
        }
        if (cs < 7 || cs > 300) {
            System.out.println("Công suất phải lớn hơn hoặc bằng 7 và nhỏ hơn hoặc bằng 300!");
            return;
        } else if (cs <= 11) {
            danhSach.add(new TramSacCham(id, vt, cs));
            System.out.println("=> Hệ thống tự động phân loại: [Sạc Chậm] (7kW - 11kW)");
        } else if (cs <= 120) {
            danhSach.add(new TramSacNhanh(id, vt, cs));
            System.out.println("=> Hệ thống tự động phân loại: [Sạc Nhanh] (30kW - 60kW)");
        } else {
            danhSach.add(new TramSacSieuNhanh(id, vt, cs));
            System.out.println("=> Hệ thống tự động phân loại: [Sạc Siêu Nhanh] (150kW - 300kW)");
        }

        System.out.println("=> Thêm thành công!");
    }

    // Chuc nang 2: Xem danh sach tru sac
    public void xuatDanhSach() {
        if (danhSach.isEmpty()) {
            System.out.println("Danh sách trống!");
            return;
        }
        System.out.println("------------------------- DANH SÁCH TRẠM SẠC --------------------------");
        for (TramSac t : danhSach) {
            t.hienThiChiTiet();
        }
        System.out.println("-----------------------------------------------------------------------");
    }

    // Chuc nang 3: Cap nhat trang thai
    public void capNhatTrangThai(Scanner scanner) {
        System.out.println("-> [Chuc nang 3] Cap nhat trang thai.");
    }

    // Chuc nang 4: Xoa tru sac
    public void xoaTruSac(Scanner scanner) {
        System.out.println("-> [Chuc nang 4] Xoa tru sac.");
    }

    // Chuc nang 5: Tim kiem theo ID
    public void timKiem(Scanner scanner) {
        System.out.println("-> [Chuc nang 5] Tim kiem.");
    }
}
