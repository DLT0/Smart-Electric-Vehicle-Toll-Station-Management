package com.evstation;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// ============================================================
// DanhSachTramSac: Danh sach tram sac voi logic hien thi tich hop.
//
// - extends ArrayList<TramSac>: co the dung truc tiep nhu mot List.
// - Chua toan bo logic render bang (tach ra khoi Module).
// - toString() xay dung bang day du bang StringBuilder (hieu qua,
//   tuong tu C# override ToString() tren collection).
// ============================================================
public class DanhSachTramSac extends ArrayList<TramSac> {

    private static final int BANG_RONG = 160;

    // ─── STATIC UTILITIES ────────────────────────────────────────────────────
    public static String keNgang(String kt, int n) {
        return kt.repeat(n);
    }

    public static long tinhThoiGianSacPhut(LocalDateTime batDau, LocalDateTime ketThuc) {
        if (batDau == null || ketThuc == null) {
            return 0;
        }
        return Duration.between(batDau, ketThuc).toMinutes();
    }

    public static String dinhDangThoiGian(long phut) {
        if (phut < 60) {
            return phut + " phut";
        }
        long gio = phut / 60;
        long phutDu = phut % 60;
        return phutDu == 0 ? gio + " gio" : gio + " gio " + phutDu + " phut";
    }

    // ─── PRIVATE RENDER HELPERS ──────────────────────────────────────────────
    private static long tinhThoiGianTieuChuanPhut(TramSac t) {
        return (long) ((56.0 / t.getCongSuat()) * 60);
    }

    private static String tinhPhuThoiGianQuaHan(TramSac t, long phutDaSac) {
        if (t instanceof TramSacCham) {
            return "";
        }
        long standardMin = tinhThoiGianTieuChuanPhut(t);
        return phutDaSac > standardMin ? " (+" + (phutDaSac - standardMin) + "p)" : "";
    }

    // Xay dung chuoi header bang
    private static String buildTieuDe() {
        String nl = System.lineSeparator();
        return keNgang("=", BANG_RONG) + nl
                + String.format("| %-16s | %-10s | %-40s | %-9s | %-8s | %-15s | %-16s | %-20s |%n",
                        "Loai", "ID", "Ten Tram", "Cong Suat", "Hao Mon",
                        "Luu y bao tri", "Trang Thai", "Thoi gian sac")
                + keNgang("=", BANG_RONG) + nl;
    }

    // Xay dung chuoi 1 dong tram (thoi gian sac thuc te)
    private static String buildDongTram(TramSac t) {
        long phut = 0;
        String extra = "";
        if (!t.isSanSang() && t.getThoiGianBatDauSac() != null) {
            phut = tinhThoiGianSacPhut(t.getThoiGianBatDauSac(), LocalDateTime.now());
            extra = tinhPhuThoiGianQuaHan(t, phut);
        }
        String thoiGianStr = (phut > 0) ? dinhDangThoiGian(phut) + extra : "-";
        return String.format("| %-16s | %-10s | %-40s | %6.1f kW | %-8s | %-15s | %-16s | %-20s |%n",
                t.getLoaiPrefix(), t.getMaTram(), t.getTenTram(), t.getCongSuat(),
                String.format("%.1f%%", t.tinhMucHaoMon()),
                t.getTrangThaiBaoTri(), t.getTrangThaiHoatDong(), thoiGianStr);
    }

    // Xay dung chuoi 1 dong tram (tong gio su dung – dung cho thong ke gio SD)
    private static String buildDongTramWithTotal(TramSac t) {
        double totalHour = t.getThoiGianHoatDong();
        if (!t.isSanSang() && t.getThoiGianBatDauSac() != null) {
            totalHour += tinhThoiGianSacPhut(t.getThoiGianBatDauSac(), LocalDateTime.now()) / 60.0;
        }
        return String.format("| %-16s | %-10s | %-40s | %6.1f kW | %-8s | %-15s | %-16s | %-20s |%n",
                t.getLoaiPrefix(), t.getMaTram(), t.getTenTram(), t.getCongSuat(),
                String.format("%.1f%%", t.tinhMucHaoMon()),
                t.getTrangThaiBaoTri(), t.getTrangThaiHoatDong(),
                String.format("%.1f gio", totalHour));
    }

    // In bang thong tin cua DUY NHAT 1 tram
    public static void xuatThongTin1Tram(TramSac t) {
        if (t == null) {
            return;
        }
        System.out.print(buildTieuDe());
        System.out.print(buildDongTram(t));
        System.out.println(keNgang("=", BANG_RONG));
    }

    // In bang cho bat ky List<TramSac> nao (thoi gian sac thuc te)
    public static void xuatBang(List<TramSac> list) {
        StringBuilder sb = new StringBuilder();
        sb.append(buildTieuDe());
        for (TramSac t : list) {
            sb.append(buildDongTram(t));
        }
        sb.append(keNgang("=", BANG_RONG));
        System.out.println(sb);
    }

    // In bang voi cot tong gio su dung (dung cho thong ke gio SD)
    public static void xuatBangVoiTongGio(List<TramSac> list) {
        StringBuilder sb = new StringBuilder();
        sb.append(buildTieuDe());
        for (TramSac t : list) {
            sb.append(buildDongTramWithTotal(t));
        }
        sb.append(keNgang("=", BANG_RONG));
        System.out.println(sb);
    }

    // ─── toString() ──────────────────────────────────────────────────────────
    // Tra ve bang hien thi DAY DU TOAN BO danh sach (co sap xep) dang String.
    // Hieu qua: dung StringBuilder, build toan bo truoc khi in ra.
    // System.out.println(danhSach) se goi ham nay tu dong.
    @Override
    public String toString() {
        if (this.isEmpty()) {
            return "!!! Danh sach trong! Chua co tram sac nao duoc them.";
        }

        // Sap xep: Dang sac truoc -> theo vi tri -> tie-break ID
        List<TramSac> sorted = new ArrayList<>(this);
        Collections.sort(sorted, (a, b) -> {
            int res = Boolean.compare(a.isSanSang(), b.isSanSang());
            if (res != 0) {
                return res;
            }
            res = Integer.compare(a.getViTri().ordinal(), b.getViTri().ordinal());
            if (res != 0) {
                return res;
            }
            return a.getMaTram().compareTo(b.getMaTram());
        });

        StringBuilder sb = new StringBuilder();
        sb.append("\n")
                .append("=".repeat(50))
                .append(" DANH SACH TRAM SAC ")
                .append("=".repeat(50))
                .append(System.lineSeparator());
        sb.append(buildTieuDe());
        for (TramSac t : sorted) {
            sb.append(buildDongTram(t));
        }
        sb.append(keNgang("=", BANG_RONG));
        return sb.toString();
    }
}
