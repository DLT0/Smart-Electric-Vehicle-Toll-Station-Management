# ⚡ Hệ thống Quản lý Trạm sạc Xe điện – Tỉnh Lâm Đồng

<div align="center">

![Java](https://img.shields.io/badge/Java_17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Province](https://img.shields.io/badge/Lâm_Đồng-EV_Hub-4CAF50?style=for-the-badge)

**Giải pháp quản lý hạ tầng sạc xe điện thông minh cho tỉnh Lâm Đồng**

</div>

---

## 📖 Giới thiệu

Khi làn sóng xe điện bắt đầu thâm nhập vào các tỉnh thành Việt Nam, **Lâm Đồng** nổi lên như một điểm đến chiến lược với lưu lượng du lịch và giao thông lớn — từ đô thị sầm uất **Đà Lạt**, trung tâm kinh tế **Bảo Lộc**, đến cửa ngõ sân bay **Liên Khương** (Đức Trọng) và các huyện vùng sâu như **Đam Rông**, **Đạ Huoai**.

**Smart EV Station – Lâm Đồng** là ứng dụng được xây dựng chuyên biệt để quản lý toàn bộ mạng lưới trạm sạc trong tỉnh (theo đơn vị hành chính sau sáp nhập 2026). Hệ thống cung cấp:

- 📍 **Vị trí địa lý có sẵn**: Người dùng chọn khu vực từ danh sách 9 đơn vị hành chính Lâm Đồng thay vì nhập tay.
- 🔋 **Phân loại tự động**: Hệ thống tự xếp loại Sạc Chậm / Sạc Nhanh / Sạc Siêu Nhanh dựa trên công suất nhập vào.
- 🏷️ **Đặt tên thông minh**: Tên trạm được tạo theo công thức `[Loại] + [Khu vực] + [STT]` (VD: *Tram Sac Nhanh Da Lat 2*).
- 📊 **Quản lý toàn diện**: Thêm, xem danh sách, cập nhật trạng thái và tìm kiếm theo ID.

---

## 🛠️ Công nghệ sử dụng

| Thành phần | Chi tiết |
|---|---|
| Ngôn ngữ | Java 17 |
| Build Tool | Maven |
| Paradigm | Lập trình hướng đối tượng (OOP) |

---

## � Cấu trúc dự án

Dự án được triển khai theo mô hình 3 file chính để tối giản quy trình học tập:

```
Smart-Electric-Vehicle-Toll-Station-Management/
├── pom.xml                                    # Cấu hình Maven
└── src/main/java/com/evstation/
    ├── Main.java                              # Điểm khởi đầu (gọi Menu)
    ├── Menu.java                              # Quản lý giao diện & lựa chọn (Enum-based)
    └── Module.java                            # Chứa logic xử lý các chức năng
```

---

## 🚀 Hướng dẫn chạy chương trình

### 1. Build dự án
```bash
mvn clean compile
```

### 2. Chạy ứng dụng
Mở file `Main.java` và chọn **Run** trong IDE, hoặc dùng lệnh:
```bash
mvn exec:java -Dexec.mainClass="com.evstation.Main"
```
