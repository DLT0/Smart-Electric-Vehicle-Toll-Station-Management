package com.evstation;

public class TestDisplay {
    public static void main(String[] args) {
        // Tạo module với dữ liệu mẫu
        Module module = new Module();
        
        // Thêm một vài trạm sạc để test
        module.themVaoDanhSach(HuyenLamDong.DA_LAT, 10);  // Cham 
        module.themVaoDanhSach(HuyenLamDong.DA_LAT, 50);  // Nhanh
        module.themVaoDanhSach(HuyenLamDong.BAO_LOC, 200); // Siêu nhanh
        
        // Test hiển thị danh sách 
        System.out.println("=== TEST HIEN THI BANG ===");
        module.xuatDanhSach();
    }
}