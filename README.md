# ⚡ Smart Electric Vehicle Toll Station Management

<div align="center">

![Java](https://img.shields.io/badge/Java_17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)

Hệ thống quản lý trạm sạc xe điện thông minh — theo dõi trạng thái, kiểm soát tải và tính phí tự động.

</div>

---

## 📖 Giới thiệu

**Smart EV Toll Station Management** là ứng dụng quản lý dành cho các bãi xe/trạm sạc xe điện. Hệ thống cho phép:

- Quản lý toàn bộ vòng đời của các trụ sạc (thêm, sửa, xoá)
- Theo dõi trạng thái hoạt động theo thời gian thực
- Kiểm soát tải điện để đảm bảo an toàn cho toàn trạm
- Tính toán chi phí sạc và thống kê hiệu suất

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
