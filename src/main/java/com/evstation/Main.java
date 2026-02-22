package com.evstation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Điểm khởi đầu của hệ thống Quản lý Trạm Sạc Xe Điện Thông Minh.
 *
 * @author DLT0
 * @version 1.0.0
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("=== Smart Electric Vehicle Toll Station Management ===");
        logger.info("Hệ thống quản lý trạm sạc xe điện đang khởi động...");

        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║   ⚡ SMART EV TOLL STATION MANAGEMENT SYSTEM ⚡     ║");
        System.out.println("╠══════════════════════════════════════════════════════╣");
        System.out.println("║  1. Thêm trụ sạc mới                               ║");
        System.out.println("║  2. Cập nhật thông số kỹ thuật                      ║");
        System.out.println("║  3. Xoá trụ sạc                                     ║");
        System.out.println("║  4. Cập nhật trạng thái                             ║");
        System.out.println("║  5. Xem danh sách trụ sạc                           ║");
        System.out.println("║  6. Bộ lọc thông minh                               ║");
        System.out.println("║  7. Tìm kiếm theo ID                                ║");
        System.out.println("║  8. Cảnh báo bảo trì                                ║");
        System.out.println("║  9. Thống kê hiệu suất                              ║");
        System.out.println("║ 10. Lưu / Tải dữ liệu                              ║");
        System.out.println("║  0. Thoát                                           ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");

        logger.info("Hệ thống đã sẵn sàng. Đang chờ dữ liệu đầu vào...");

        // TODO: Implement Scanner menu loop
        // TODO: Implement StationManager service
    }
}
