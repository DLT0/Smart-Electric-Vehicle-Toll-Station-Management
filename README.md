# ⚡ Hệ Thống Quản Lý Trạm Sạc Xe Điện VN

## 📖 Tổng quan
**EVCore** (Smart-EV-Toll-Station) là gói giải pháp backend xử lý nghiệp vụ quản lý trạm sạc xe điện. Được thiết kế theo chuẩn hướng đối tượng (OOP) và phát triển trên nền tảng **Java 21**, dự án này không chỉ là một ứng dụng Console mà còn là một **Core Engine** có khả năng tích hợp linh hoạt vào các hệ thống Web (Spring Boot) hoặc Desktop (JavaFX).

---

## ✅ Chức năng hiện có


| # | Chức năng | Mô tả |
|---|---|---|
| 1 | **Thêm 1 trạm sạc** | Nhập ID, chọn khu vực (số thứ tự), nhập công suất & số cổng. Có vòng lặp kiểm tra ID trùng, dữ liệu sai kiểu sẽ nhắc nhập lại. |
| 2 | **Thêm danh sách trạm** | Nhập số lượng rồi gọi lặp chức năng 1. |
| 3 | **Xem danh sách** | Hiển thị bảng đầy đủ: Loại, ID, Tên, Công suất, Số cổng, Vận hành (h), Trạng thái, Chi phí. |
| 4 | **Cập nhật trạm** | Cập nhật trạng thái sạc hoặc thời gian hoạt động của trạm theo ID. |
| 5 | **Xóa trạm** | Tìm và xóa trạm theo ID, có xác nhận trước khi thực hiện. |
| 6 | **Thống kê** | Thống kê danh sách trạm cần bảo trì. |
| 7 | **Xuất File** | Xuất danh sách trạm ra file Exel. |

---

## 🛠️ Tính năng Kỹ thuật & Nguyên lý Thiết kế

| Tính năng | Mô tả | Chi tiết triển khai |
|---|---|---|
| **Phân loại tự động** | Tự động chọn Class con phù hợp | Chậm (<=11kW), Nhanh (<=120kW), Siêu nhanh (>120kW) |
| **Định danh thông minh**| Quy ước ID duy nhất | Format: `[PREFIX]-[AREA]-[SERIAL]` (VD: `SN-DAL-001`) |
| **Ràng buộc dữ liệu** | Data Validation cực kỳ nghiêm ngặt | Chống trùng ID, giới hạn công suất [7-300kW], type-safety |
| **Quản lý vận hành** | Theo dõi trạng thái & thời gian | Tích hợp logic cảnh báo bảo trì dựa trên số giờ chạy (>400h) |
| **Sắp xếp nâng cao**| Custom Comparator | Ưu tiên hiển thị trạm đang bận, sau đó sắp xếp theo vị trí |

### 🛡️ Cơ chế Validation (Mẫu)
`EVCore` đảm bảo dữ liệu luôn sạch từ tầng Model:
```java
public void setCongSuat(double value) {
    if (value <= 0) value = 7; // Mặc định tối thiểu 7kW
    else if (value > 300) value = 300; // Giới hạn hạ tầng tối đa
    this._congSuat = value;
}
```

---

## 📁 Cấu trúc Thư mục
```text
src/main/java/com/evstation/
├── Main.java          # Entry point (Console Application)
├── Menu.java          # Giao diện điều hướng CLI
└── Module.java        # Core Logic (The real Package)
    ├── HuyenLamDong   # Enum-based Area Management
    ├── TramSac        # Model Layer (Polymorphism & Abstract)
    └── Module         # Service Layer (Business Logic & Repository)
```

---

## 🚀 Hướng dẫn cài đặt & chạy chương trình

### Yêu cầu hệ thống

- **Java**: Microsoft OpenJDK 21+ (hoặc bất kỳ JDK 21 nào)
- **Maven**: 3.9+
- **OS**: Windows 10/11 (hoặc Linux/macOS)

### 1. Clone dự án
```bash
git clone <repository-url>
cd Smart-Electric-Vehicle-Toll-Station-Management-main
```

### 2. Build dự án

> ⚠️ **Lưu ý Windows:** Nếu `JAVA_HOME` hệ thống chưa trỏ đến JDK 21, hãy thiết lập trước khi build:

```powershell
# Thiết lập JAVA_HOME tạm thời cho phiên terminal hiện tại
$env:JAVA_HOME = "C:\Program Files\Microsoft\jdk-21.0.10.7-hotspot"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

# Sau đó build
mvn clean compile
```

### 3. Chạy ứng dụng

Mở file `Main.java` và chọn **Run** trong IDE, hoặc:

```powershell
mvn exec:java -Dexec.mainClass="com.evstation.Main"
```

---
