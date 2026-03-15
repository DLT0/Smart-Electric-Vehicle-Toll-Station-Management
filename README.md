# ⚡ Hệ Thống Quản Lý Trạm Sạc Xe Điện VN

## 📖 Tổng quan
**Hệ Thống Quản Lý Trạm Sạc Xe Điện VN** là gói giải pháp backend xử lý nghiệp vụ quản lý trạm sạc xe điện. Được thiết kế theo chuẩn hướng đối tượng (OOP) và phát triển trên nền tảng **Java 21**, dự án này không chỉ là một ứng dụng Console mà còn là một **Core Engine** có khả năng tích hợp linh hoạt vào các hệ thống Web (Spring Boot) hoặc Desktop (JavaFX).

---

## ✅ Chức năng hiện có


| # | Chức năng | Mô tả |
|---|---|---|
| 1 | **Thêm 1 trạm sạc** | Tự động sinh ID theo khu vực, nhập công suất (7-300kW). Có validator kiểm tra dữ liệu đầu vào. |
| 2 | **Thêm danh sách trạm** | Nhập số lượng cần thêm và thực hiện nhập liệu hàng loạt. |
| 3 | **Xem danh sách** | Hiển thị bảng chi tiết: Loại, ID, Tên, Công suất, Thời gian vận hành, Trạng thái. Tự động sắp xếp thông minh. |
| 4 | **Cập nhật trạm** | Cập nhật trạng thái (Sẵn sàng/Đang sạc) hoặc điều chỉnh số giờ vận hành tích lũy. |
| 5 | **Xóa trạm** | Gỡ bỏ trạm khỏi hệ thống dựa trên ID, có bước xác nhận an toàn trước khi xóa. |
| 6 | **Tìm kiếm** | Tìm và hiển thị thông tin chi tiết của một trạm cụ thể thông qua mã ID duy nhất. |
| 7 | **Thống kê bảo trì** | Lọc danh sách các trạm có mức độ hao mòn cao (>90% hạn bảo trì 500h). |
| 8 | **Tính chi phí (1 trạm)** | Dự toán số tiền và thời gian sạc dựa trên % pin cần sạc (Dung lượng mặc định 70kWh). |
| 9 | **Gợi ý trạm (DS)** | So sánh thời gian sạc và biểu phí quá hạn giữa tất cả các trạm trong hệ thống. |

---

## 🛠️ Tính năng Kỹ thuật & Nguyên lý Thiết kế

| Tính năng | Mô tả | Chi tiết triển khai |
|---|---|---|
| **Mô phỏng Thời gian thực** | Quản lý phiên sạc chính xác | Sử dụng `LocalDateTime` để tính toán khoảng thời gian sạc (Duration) và tự động cộng dồn vào quỹ giờ vận hành. |
| **Kiến trúc Refactored** | Tối ưu tính tái sử dụng | Phân tách rõ ràng **Public Helpers** (Dùng chung) và **Private Helpers** (Nội bộ). Các hàm tìm kiếm trả về Object thay vì void. |
| **Phân loại tự động** | Tự động chọn Class con phù hợp | Chậm (<=11kW), Nhanh (<=120kW), Siêu nhanh (>120kW) dựa trên tính Đa hình (Polymorphism). |
| **Định danh thông minh**| Quy ước ID duy nhất | Format: `[PREFIX]-[AREA]-[SERIAL]` (VD: `SN-DAL-001`). Tự động nhảy số thứ tự theo khu vực. |
| **Dữ liệu Mẫu (Mocking)**| Sẵn sàng để Demo | Tích hợp sẵn bộ dữ liệu mẫu đa dạng trạm, kèm theo giả lập thời gian bắt đầu sạc cho các trạm đang bận. |
| **Sắp xếp nâng cao**| Custom Comparator | Ưu tiên hiển thị trạm đang sạc lên đầu, sau đó sắp xếp theo vị trí khu vực và thứ tự hệ thống. |

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
    ├── Module         # Service Layer (Business Logic & Repository)
```

> [!NOTE]
> Để biết chi tiết về các thay đổi trong đợt tái cấu trúc (Refactoring) gần nhất, vui lòng xem file [Refactored_Architecture.md](./Refactored_Architecture.md).

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
