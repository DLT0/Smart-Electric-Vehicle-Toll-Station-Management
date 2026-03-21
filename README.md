# ⚡ Hệ Thống Quản Lý Trạm Sạc Xe Điện VN

## 📖 Tổng quan
**Hệ Thống Quản Lý Trạm Sạc Xe Điện VN** là gói giải pháp backend xử lý nghiệp vụ quản lý trạm sạc xe điện. Được thiết kế theo chuẩn hướng đối tượng (OOP) và phát triển trên nền tảng **Java 21**.

---

## ✅ Chức năng hiện có


| # | Chức năng | Mô tả |
|---|---|---|
| 1 | **Thêm 1 trạm sạc** | Chọn khu vực, nhập công suất (7-300kW), hệ thống tự sinh ID theo quy ước và tự phân loại loại trạm. |
| 2 | **Thêm danh sách trạm** | Nhập số lượng và thêm nhiều trạm liên tiếp trong một phiên nhập liệu. |
| 3 | **Xem danh sách trạm** | Hiển thị đầy đủ bảng thông tin: Loại, ID, Tên trạm, Công suất, Hao mòn, Lưu ý bảo trì, Trạng thái, Thời gian sạc. |
| 4 | **Cập nhật trạng thái trạm** | Cập nhật trạng thái sạc (San sang/Dang sac) hoặc cập nhật thời gian hoạt động tích lũy. |
| 5 | **Xóa trạm sạc** | Xóa theo ID, có bước xác nhận trước khi thực hiện. |
| 6 | **Tìm kiếm theo ID** | Tìm kiếm linh hoạt theo ID (hỗ trợ bỏ dấu gạch và khoảng trắng). |
| 7 | **Thống kê trạm sạc** | Có menu phụ gồm: trạm cần bảo trì, trạm có giờ sử dụng > X, khu vực có tần suất sử dụng cao nhất. |
| 8 | **Xuất danh sách ra Excel** | Đang phát triển (Coming soon). |
| 9 | **Tính chi phí cho 1 trạm** | Dự toán hoặc xuất hóa đơn thực tế theo trạng thái trạm, có xử lý thời gian quá hạn cho trạm nhanh/siêu nhanh. |
| 10 | **Sắp xếp danh sách theo hao mòn** | Sắp xếp và hiển thị theo độ hao mòn để hỗ trợ theo dõi bảo trì. |
| 11 | **Gợi ý chi phí cho tất cả trạm** | So sánh chi phí/thời gian giữa các trạm để hỗ trợ lựa chọn phương án sạc. |

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

### Credit
> From CTK49B With Love
