# ⚡ Hệ thống Quản lý Trạm sạc Xe điện – Tỉnh Lâm Đồng

<div align="center">

![Java](https://img.shields.io/badge/Java_21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![OOP](https://img.shields.io/badge/OOP-Design-4CAF50?style=for-the-badge)

</div>

---

## 📖 Giới thiệu

**Smart EV Station – Lâm Đồng** là ứng dụng console được xây dựng chuyên biệt để quản lý toàn bộ mạng lưới trạm sạc xe điện trong tỉnh Lâm Đồng. Hệ thống cung cấp:

- 📍 **Vị trí địa lý type-safe**: Chọn khu vực từ danh sách 9 đơn vị hành chính Lâm Đồng (Enum-based), tránh lỗi nhập tay.
- 🔋 **Phân loại tự động**: Hệ thống tự xếp loại Sạc Chậm / Sạc Nhanh / Sạc Siêu Nhanh dựa trên công suất.
- 🏷️ **Đặt tên thông minh**: Tên trạm được tạo theo công thức `[Loại] + [Khu vực] + [STT]` (VD: *Tram Sac Nhanh Da Lat 2*).
- 🛡️ **Ràng buộc dữ liệu đầy đủ**: Công suất, số cổng sạc phải là số dương. ID không được để trống hoặc trùng lặp. Nhập sai kiểu số được xử lý bằng `try-catch`.
- ⏱️ **Theo dõi vận hành**: Mỗi trạm lưu `thoiGianHoatDong` (giờ tích lũy) để phục vụ tính năng thống kê và bảo trì sau này.

---

## 🛠️ Công nghệ sử dụng

| Thành phần | Chi tiết |
|---|---|
| Ngôn ngữ | **Java 21** (Microsoft OpenJDK 21) |
| Build Tool | Apache Maven 3.9+ |
| Paradigm | Lập trình hướng đối tượng (OOP) – Abstract Class, Inheritance, Enum |
| Thư viện | `java.util.*` (Scanner, ArrayList, List, Collections) |

---

## 📁 Cấu trúc dự án

```
Smart-Electric-Vehicle-Toll-Station-Management/
├── pom.xml                                    # Cấu hình Maven (Java 21)
├── Plan.md                                    # Kế hoạch phát triển
├── UI_Plan.md                                 # Kế hoạch giao diện tương lai
└── src/main/java/com/evstation/
    ├── Main.java                              # Điểm khởi đầu – khởi tạo Menu và chạy chương trình
    ├── Menu.java                              # Hiển thị menu & điều hướng chức năng
    └── Module.java                            # Toàn bộ logic nghiệp vụ:
        │                                      #   - Enum HuyenLamDong (9 khu vực)
        │                                      #   - Abstract class TramSac
        │                                      #   - TramSacCham / TramSacNhanh / TramSacSieuNhanh
        └───────────────────────────────────── #   - Class Module (quản lý danh sách)
```

---

## 🗃️ Mô hình dữ liệu

### Lớp `TramSac` (Abstract)

| Trường | Kiểu | Mô tả |
|---|---|---|
| `maTram` | `String` | ID định danh duy nhất |
| `tenTram` | `String` | Tên tự động theo công thức |
| `viTri` | `HuyenLamDong` | Vị trí (Enum – type-safe) |
| `trangThai` | `boolean` | `true` = Sẵn sàng, `false` = Đang sạc |
| `congSuat` | `double` | Công suất (kW) – bắt buộc > 0 |
| `soCongSac` | `int` | Số cổng sạc – bắt buộc > 0 |
| `thoiGianHoatDong` | `double` | Tổng giờ vận hành tích lũy (mặc định = 0) |

### Phân loại công suất

| Loại | Phạm vi | Lớp con |
|---|---|---|
| Sạc Chậm | ≤ 11 kW | `TramSacCham` |
| Sạc Nhanh | 12 – 120 kW | `TramSacNhanh` |
| Sạc Siêu Nhanh | > 120 kW | `TramSacSieuNhanh` |

---

## ✅ Chức năng hiện có

| # | Chức năng | Mô tả |
|---|---|---|
| 1 | **Thêm 1 trạm sạc** | Nhập ID, chọn khu vực (số thứ tự), nhập công suất & số cổng. Có vòng lặp kiểm tra ID trùng, dữ liệu sai kiểu sẽ nhắc nhập lại. |
| 2 | **Thêm danh sách trạm** | Nhập số lượng rồi gọi lặp chức năng 1. |
| 3 | **Nạp dữ liệu mẫu** | Tự động nạp 6 trạm mẫu vào hệ thống (có sẵn giờ vận hành giả lập để test). |
| 4 | **Xem danh sách** | Hiển thị bảng đầy đủ: Loại, ID, Tên, Công suất, Số cổng, Vận hành (h), Trạng thái, Chi phí. |
| 5 | **Cập nhật trạm** | Cập nhật trạng thái sạc hoặc thời gian hoạt động của trạm theo ID. |
| 6 | **Xóa trạm** | Tìm và xóa trạm theo ID, có xác nhận trước khi thực hiện. |
| 7 | **Tìm kiếm theo ID** | Tìm và hiển thị chi tiết trạm theo mã ID. |
| 8 | **Thống kê** | *(Đang thiết kế)* Lọc và thống kê theo vị trí, trạng thái, thời gian vận hành. |

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

## 📌 Lưu ý phát triển

- **Encoding**: Toàn bộ text hiển thị dùng ASCII không dấu để tương thích tối đa với các terminal Windows (cmd, PowerShell).
- **JAVA_HOME cố định**: Để không phải thiết lập mỗi lần, hãy cập nhật `JAVA_HOME` trong *System Properties > Environment Variables* của Windows, trỏ đến `C:\Program Files\Microsoft\jdk-21.0.10.7-hotspot`.
- **Thông báo lỗi**: Hệ thống sử dụng prefix `!!!` cho mọi thông báo lỗi/cảnh báo, và `==>` cho thông báo thành công.

---
