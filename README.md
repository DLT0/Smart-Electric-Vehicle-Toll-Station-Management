# ⚡ Hệ Thống Quản Lý Trạm Sạc Xe Điện Thông Minh

## 📖 Tổng quan
**Hệ Thống Quản Lý Trạm Sạc Xe Điện Thông Minh** là một ứng dụng Console trên nền tảng **Java 17**, giúp quản lý các trạm sạc xe điện với nhiều mức công suất (Chậm, Nhanh, Siêu Nhanh).
Dự án được thiết kế chặt chẽ theo các nguyên lý Lập trình hướng đối tượng (OOP) và sử dụng Maven để quản lý.

---

## ✅ Các Chức Năng Chính

Chương trình cung cấp một Menu tương tác qua Terminal/Console với các tính năng sau:

| # | Chức năng | Mô tả |
|---|---|---|
| 1 | **Thêm danh sách trụ sạc** | Cho phép nhập số lượng và khởi tạo hàng loạt trụ sạc mới. Tự động sinh ID theo quy ước, phân loại trụ sạc dựa trên công suất (7-300kW). |
| 2 | **Nhập cố định (Dữ liệu mẫu)** | Tự động nạp bộ dữ liệu mock đa dạng với nhiều trạm ở nhiều trạng thái khác nhau để tiện cho việc kiểm thử và demo ứng dụng. |
| 3 | **Xuất danh sách trụ sạc** | In ra bảng danh sách toàn bộ các trạm sạc hiện có cùng thông tin chi tiết. |
| 4 | **Cập nhật thông tin trạm** | Cho phép chỉnh sửa linh hoạt Tên, Vị trí, Công suất, Trạng thái (Sẵn sàng/Đang sạc), Thời gian hoạt động, Hạn bảo trì. Có cơ chế xác nhận trước khi lưu thay đổi. |
| 5 | **Xóa trụ sạc** | Xóa một trạm sạc dựa theo ID nhập vào. |
| 6 | **Tìm kiếm đa trường (Multi-field)** | Tìm kiếm linh hoạt theo ID, Tên trạm, Loại trạm hoặc Khu vực (hỗ trợ tìm kiếm một phần - partial match và không phân biệt hoa thường). |
| 7 | **Tìm trạm sạc theo thứ hạng thời gian** | Tìm trạm có thời gian sạc nhanh thứ N. Ứng dụng cấu trúc dữ liệu **Min Heap** (`PriorityQueue`) để tối ưu việc tìm kiếm thứ hạng. |
| 8 | **Thống kê trạm sạc** | Gồm các menu phụ: <br/>- Các trạm cần bảo trì (hao mòn > 90%).<br/>- Trạm có số giờ sử dụng > mức X chỉ định.<br/>- Khu vực có tần suất sử dụng trạm sạc cao nhất. |
| 9 | **Tính chi phí cho 1 trạm** | Tính chi phí dự kiến cho một phiên sạc dựa trên loại trạm và thời gian sạc thực tế. |
| 10| **Sắp xếp danh sách trạm sạc** | Sắp xếp toàn bộ trạm sạc theo mức độ hao mòn (thời gian hoạt động) để ưu tiên lên kế hoạch bảo trì. |
| 11| **Gợi ý chi phí cho toàn hệ thống** | Đưa ra dự toán hoặc gợi ý chi phí sạc đối chiếu trên tất cả các trạm để người dùng có lựa chọn tối ưu nhất. |

---

## 🛠️ Điểm Nhấn Kỹ Thuật (Technical Highlights)

- **Lập trình Hướng Đối Tượng (OOP):**
  - Sử dụng *Tính Kế Thừa (Inheritance)* và *Tính Trừu Tượng (Abstraction)* thông qua class abstract `TramSac` và các class con (`TramSacCham`, `TramSacNhanh`, `TramSacSieuNhanh`).
  - Áp dụng *Tính Đa Hình (Polymorphism)* để tự động phân loại trạm khi thêm mới và in thông tin tùy chỉnh theo từng loại trạm.
- **Cấu trúc Dữ Liệu & Thuật Toán:**
  - Ứng dụng `PriorityQueue` (Min Heap) để lấy ra phần tử lớn thứ N nhanh chóng.
  - Sử dụng cơ chế tìm kiếm đa trường (Multi-field bloom search) giúp tìm từ khóa trên nhiều thuộc tính (ID, Tên, Khu vực).
- **Thiết Kế Mã Nguồn (Clean Code):**
  - Chia tách UI (`Menu.java`), Data Models (`TramSac*.java`, `DanhSachTramSac.java`, `DanhSachKhuVuc.java`) và Business Logic (`QuanLyTramSac.java`).
  - Tự động sinh ID duy nhất, thông minh (VD: `SC-DAL-001` cho trạm Sạc Chậm ở Đà Lạt số thứ tự 001).
- **Kiểm tra dữ liệu đầu vào (Validation):**
  - Xử lý các Exception khi người dùng nhập sai kiểu dữ liệu (chữ thay vì số).
  - Có các hàm helper (`confirm`, `promptForDouble`) tái sử dụng ở nhiều nơi để chuẩn hóa đầu vào (yes/no) hoặc số thực.

---

## 📁 Cấu trúc Thư Mục Chính

```text
src/main/java/com/evstation/
├── Main.java                # Điểm khởi chạy chương trình (Entry Point)
├── Menu.java                # Giao diện điều hướng dòng lệnh (CLI Navigation)
├── QuanLyTramSac.java       # Xử lý nghiệp vụ chính (Business Logic)
├── DanhSachTramSac.java     # Cấu trúc lưu trữ và thao tác danh sách
├── DanhSachKhuVuc.java      # Quản lý Enum / Danh sách các khu vực
└── TramSac*.java            # Các lớp mô hình dữ liệu (Abstract & Implementations)
```

---

## 🚀 Hướng Dẫn Cài Đặt & Chạy Chương Trình

### Yêu cầu hệ thống:
- **Java:** JDK 17 hoặc mới hơn.
- **Công cụ build:** Maven 3.x.
- **Hệ điều hành:** Đa nền tảng (Windows / Linux / macOS).

### Các bước chạy:

1. **Clone dự án (nếu chưa có):**
   ```bash
   git clone <repository-url>
   cd Smart-Electric-Vehicle-Toll-Station-Management-main
   ```

2. **Biên dịch dự án:**
   Chạy lệnh Maven sau trong thư mục gốc (nơi chứa file `pom.xml`):
   ```bash
   mvn clean compile
   ```

3. **Chạy ứng dụng:**
   Cách 1: Chạy thông qua Maven plugin
   ```bash
   mvn exec:java -Dexec.mainClass="com.evstation.Main"
   ```
   Cách 2: Mở project bằng các IDE như IntelliJ IDEA, Eclipse, VS Code và Run file `Main.java`.

---

> **Lưu ý:** Dự án này hiện tại là phiên bản Console App (Theo giai đoạn 1 và 2 trong `Plan.md`). Các tính năng như lưu/đọc file JSON với Gson và Web Dashboard đang nằm trong lộ trình phát triển tiếp theo.

---

## Ví dụ Constructor Không Tham Số

Hệ thống hỗ trợ tạo đối tượng trạm sạc con mà chưa cần dữ liệu đầu vào ngay lúc khởi tạo. Trường hợp này đối tượng sẽ ở trạng thái rỗng hoặc mặc định an toàn, không phát sinh lỗi.

```java
QuanLyTramSac quanLy = new QuanLyTramSac();
TramSacCham tramMacDinh = new TramSacCham();

System.out.println("Danh sach rong sau khi tao QuanLyTramSac: " + quanLy.timKiemDanhSach("SC").size());
System.out.println("Ma tram: '" + tramMacDinh.getMaTram() + "'");
System.out.println("Ten tram: " + tramMacDinh.getTenTram());
System.out.println("Cong suat: " + tramMacDinh.getCongSuat());
System.out.println("Trang thai: " + tramMacDinh.getTrangThaiHoatDong());
```

Kết quả mong đợi khi chưa có dữ liệu đầu vào:

```text
Danh sach rong sau khi tao QuanLyTramSac: 0
Ma tram: ''
Ten tram: Sac Cham (mac dinh)
Cong suat: 7.0
Trang thai: San sang
```

Ý nghĩa:

- `QuanLyTramSac()` luôn khởi tạo được một danh sách rỗng hợp lệ.
- `TramSacCham()` tạo đối tượng an toàn để dùng cho test, demo, hoặc gán dữ liệu sau.
- Nếu chưa có dữ liệu đầu vào, mã trạm để rỗng; các trường còn lại dùng giá trị mặc định hợp lệ của loại trạm.
