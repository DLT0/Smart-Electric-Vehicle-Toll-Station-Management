# ⚡ Smart Electric Vehicle Toll Station Management

<div align="center">

![Java](https://img.shields.io/badge/Java_17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-In%20Development-blue?style=for-the-badge)

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
| JSON | Gson 2.11.0 |
| Logging | SLF4J + Logback |
| Testing | JUnit 5 |
| Lưu trữ | File `.txt` / `.json` |

---

## ✨ Tính năng chính

### 🔌 Quản lý trụ sạc
| # | Tính năng | Mô tả |
|---|---|---|
| 1 | **Thêm trụ sạc** | Thêm trụ mới vào hệ thống với hai loại: **Trạm Chuẩn** hoặc **Trạm Siêu Tốc** (yêu cầu thêm thông tin hệ thống làm mát) |
| 2 | **Cập nhật thông số** | Chỉnh sửa thông số kỹ thuật (vị trí, công suất tối đa, loại đầu cắm) khi có thay đổi phần cứng |
| 3 | **Xoá trụ sạc** | Gỡ bỏ các trụ sạc cũ hoặc hỏng hóc khỏi danh sách quản lý |

### 📊 Giám sát & Vận hành
| # | Tính năng | Mô tả |
|---|---|---|
| 4 | **Cập nhật trạng thái** | Chuyển đổi trạng thái: `Available` → `Charging` → `Maintenance`. Có logic bảo vệ: không cho phép chuyển sang "Trống" thủ công khi đang trong tiến trình sạc |
| 5 | **Ghi nhận giờ hoạt động** | Cập nhật số giờ vận hành sau mỗi phiên sạc để theo dõi độ hao mòn thiết bị |
| 6 | **Kiểm soát tải (Load Control)** | Ngăn kích hoạt sạc mới nếu tổng công suất các trụ đang chạy vượt định mức cho phép của toàn trạm |

### 🔍 Tìm kiếm & Lọc
| # | Tính năng | Mô tả |
|---|---|---|
| 7 | **Xem danh sách** | Hiển thị toàn bộ trụ sạc, mỗi loại tự hiển thị thông số đặc trưng riêng (Trạm Siêu Tốc hiển thị thêm nhiệt độ vận hành) |
| 8 | **Bộ lọc thông minh** | Lọc nhanh trụ đang **Lỗi/Hỏng** (để điều phối kỹ thuật viên) hoặc trụ đang **Trống** (để điều hướng xe vào sạc) |
| 9 | **Cảnh báo bảo trì** | Liệt kê các trụ đã hoạt động quá số giờ quy định (ví dụ: > 500 giờ) cần được kiểm tra định kỳ |
| 10 | **Tìm kiếm theo ID** | Truy xuất nhanh thông tin chi tiết của một trụ cụ thể theo mã số khi có sự cố |

### 📈 Thống kê & Lưu trữ
| # | Tính năng | Mô tả |
|---|---|---|
| 11 | **Thống kê hiệu suất** | Tính tổng điện năng (kWh) đã phục vụ trên toàn hệ thống, hỗ trợ đánh giá hiệu suất từng trạm |
| 12 | **Lưu trữ dữ liệu** | Đọc/ghi danh sách trụ sạc ra file `.txt` hoặc `.json`, đảm bảo dữ liệu không bị mất khi tắt chương trình |

---

## 🚀 Hướng dẫn cài đặt & chạy

### Yêu cầu hệ thống
- **Java JDK** phiên bản 17 trở lên
- **Maven** phiên bản 3.8+ (hoặc cài qua [Scoop](https://scoop.sh): `scoop install maven`)
- **Git**

### Các bước thực hiện

**1. Clone repository**
```bash
git clone https://github.com/DLT0/Smart-Electric-Vehicle-Toll-Station-Management.git
cd Smart-Electric-Vehicle-Toll-Station-Management
```

**2. Build dự án**
```bash
mvn clean compile
```

**3. Chạy tests**
```bash
mvn test
```

**4. Đóng gói & chạy ứng dụng**
```bash
mvn clean package -DskipTests
java -jar target/smart-ev-toll-station-1.0.0-SNAPSHOT.jar
```

---

## 📁 Cấu trúc dự án

```
Smart-Electric-Vehicle-Toll-Station-Management/
├── pom.xml                                    # Cấu hình Maven (dependencies, plugins)
├── src/
│   ├── main/
│   │   ├── java/com/evstation/
│   │   │   └── Main.java                      # Điểm khởi đầu chương trình
│   │   └── resources/
│   │       └── logback.xml                    # Cấu hình logging (SLF4J + Logback)
│   └── test/
│       └── java/com/evstation/
│           └── MainTest.java                  # Unit tests (JUnit 5)
├── .gitignore                                 # Danh sách file/thư mục Git bỏ qua
├── LICENSE                                    # Giấy phép MIT
└── README.md                                  # Tài liệu hướng dẫn
```

---

## 📜 Giấy phép

Dự án được phân phối theo giấy phép **MIT**. Xem file [LICENSE](./LICENSE) để biết thêm chi tiết.

---

<div align="center">

Made with ❤️ by **DLT0**

</div>
