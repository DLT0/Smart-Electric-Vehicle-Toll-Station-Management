# ⚡ Hệ Thống Quản Lý Trạm Sạc Xe Điện VN

## 📖 Tổng quan
**Hệ Thống Quản Lý Trạm Sạc Xe Điện VN** là gói giải pháp backend xử lý nghiệp vụ quản lý trạm sạc xe điện. Được thiết kế theo chuẩn hướng đối tượng (OOP) và phát triển trên nền tảng **Java 17/21**.

---

## ✅ Chức năng hiện có

| # | Chức năng | Mô tả |
|---|---|---|
| 1 | **Thêm 1 trạm sạc** | Chọn khu vực, nhập công suất (7-300kW), hệ thống tự sinh ID theo quy ước và tự phân loại loại trạm. |
| 2 | **Thêm danh sách trạm** | Nhập số lượng và thêm nhiều trạm liên tiếp trong một phiên nhập liệu. |
| 3 | **Xem danh sách trạm** | Hiển thị đầy đủ bảng thông tin: Loại, ID, Tên trạm, Công suất, Hao mòn, Lưu ý bảo trì, Trạng thái, Thời gian sạc. |
| 4 | **Cập nhật thông tin trạm** | Cập nhật tên, vị trí, công suất, trạng thái sạc (Sẵn sàng/Đang sạc), thời gian hoạt động và hạn bảo trì. |
| 5 | **Xóa trạm sạc** | Xóa theo ID, có bước xác nhận trước khi thực hiện. |
| 6 | **Tìm kiếm đa trường** | Tìm kiếm linh hoạt theo ID, tên, loại hoặc khu vực (hỗ trợ partial match, bloom search). |
| 7 | **Tìm trạm thứ N** | Tìm trạm có thời gian sạc dự tính đứng thứ N (sử dụng Min Heap). |
| 8 | **Thống kê trạm sạc** | Có menu phụ gồm: trạm cần bảo trì, trạm có giờ sử dụng > X, khu vực có tần suất sử dụng cao nhất. |
| 9 | **Tính chi phí cho 1 trạm** | Dự toán hoặc xuất hóa đơn thực tế theo trạng thái trạm, có xử lý thời gian quá hạn cho trạm nhanh/siêu nhanh. |
| 10| **Sắp xếp danh sách** | Sắp xếp và hiển thị theo độ hao mòn để hỗ trợ theo dõi bảo trì. |
| 11| **Gợi ý chi phí cho tất cả trạm** | So sánh chi phí/thời gian giữa các trạm để hỗ trợ lựa chọn phương án sạc. |

---

## 🧭 Bảng Hàm Chính Và Hàm Phụ Trợ

### 1) Mapping hàm chính trong `QuanLyTramSac`

| # | Hàm chính | Mục tiêu nghiệp vụ | Hàm phụ trợ liên quan trực tiếp |
|---|---|---|---|
| 1 | `themDSTruSac(Scanner)` | Thêm nhiều trạm theo số lượng nhập | `them1TruSac`, `chonKhuVuc`, `nhapCongSuat`, `themVaoDanhSach` |
| 2 | `nhapCoDinh(Scanner)` | Nạp bộ dữ liệu mẫu | `confirm`, `themVaoDanhSach`, `apDungTrangThaiTest` |
| 3 | `xuatDanhSach()` | Xuất bảng toàn bộ trạm sạc | `DanhSachTramSac.toString()` |
| 4 | `capNhatThongTinTram(Scanner)`| Cập nhật đầy đủ thông tin một trạm | `timTramTheoId`, `DanhSachTramSac.xuatThongTin1Tram`, `promptForDouble`, `confirm` |
| 5 | `xoaTruSac(Scanner)` | Xóa trạm theo ID có xác nhận | `timTramTheoId` (Predicate), `DanhSachTramSac.xuatThongTin1Tram`, `confirm` |
| 6 | `timKiem(Scanner)` | Tìm kiếm đa trường bloom search | `timKiemDanhSach` |
| 7 | `thongKeTruSac(Scanner)` | Điều phối menu thống kê | `LoaiThongKe.hienThiMenu`, `thongKeBaoTri`, `thongKeGioSDThap`, `thongKeKhuVucCaoNhat`, `confirm` |
| 8 | `thongKeBaoTri()` | Thống kê trạm cần bảo trì | `DanhSachTramSac.inTieuDeBang`, `DanhSachTramSac.inDSTram` |
| 9 | `thongKeGioSDThap(Scanner)` | Lọc trạm có tổng giờ sử dụng > X | `tinhThoiGianSacPhut`, `DanhSachTramSac.inTieuDeBang`, `DanhSachTramSac.inDSTramWithTotal` |
| 10| `thongKeKhuVucCaoNhat()` | Thống kê khu vực dùng nhiều nhất | `DanhSachKhuVuc.xuatBangThongKe` |
| 11| `tinhChiPhi1Tram(Scanner)` | Tính dự toán/hóa đơn cho 1 trạm | `timTramTheoId`, `tinhThoiGianSacPhut`, `dinhDangThoiGian` |
| 12| `tinhChiPhiDS(Scanner)` | Gợi ý chi phí cho toàn bộ trạm | `dinhDangThoiGian` |
| 13| `sapXepDS()` | Sắp xếp danh sách theo hao mòn | `DanhSachTramSac.sapXepTheoHaoMon` |

---

## 🛠️ Tính năng Kỹ thuật & Nguyên lý Thiết kế

| Tính năng | Mô tả | Chi tiết triển khai |
|---|---|---|
| **Kiến trúc Refactored** | Tối ưu tính tái sử dụng và chia tách | Phân tách rõ ràng UI (`Menu`), Logic (`QuanLyTramSac`), Utilities (`DanhSachKhuVuc`) và Models (`TramSac`). |
| **Mô phỏng Thời gian thực** | Quản lý phiên sạc chính xác | Sử dụng `LocalDateTime` để tính toán khoảng thời gian sạc (Duration) và tự động cộng dồn vào quỹ giờ vận hành. |
| **Tìm kiếm Đa trường** | UX tối ưu cho CLI | Hỗ trợ tìm kiếm partial-match trên tất cả các trường (ID, Tên, Khu Vực, Loại trạm) mà không cần chọn tiêu chí cố định. |
| **Phân loại tự động** | Đa hình (Polymorphism) | Chậm (<=11kW), Nhanh (<=120kW), Siêu nhanh (>120kW) tự động khởi tạo dựa vào công suất đầu vào. |
| **Định danh thông minh**| Quy ước ID duy nhất | Format: `[PREFIX]-[AREA]-[SERIAL]` (VD: `SN-DAL-001`). Tự động nhảy số thứ tự theo khu vực một cách chính xác. |
| **Tối ưu mã nguồn** | Stream & Lambda | Ứng dụng mạnh mẽ Java 8 Streams (`filter`, `map`, `reduce`, `collect`) trong các hàm tìm kiếm và thống kê. |

---

## 📁 Cấu trúc Thư mục
```text
src/main/java/com/evstation/
├── Main.java             # Entry point (Khởi tạo App & Menu)
├── Menu.java             # Lớp giao diện điều hướng (CLI Menu)
├── QuanLyTramSac.java    # Service Layer (Xử lý Business Logic chính)
├── DanhSachTramSac.java  # Wrapper Collection quản lý danh sách và in ấn 
├── DanhSachKhuVuc.java   # Utilities xử lý Thống kê khu vực & Enum HuyenLamDong
├── TramSac.java          # Class Cha (Abstract base class)
├── TramSacCham.java      # Class con: Trạm sạc chậm (7-11kW)
├── TramSacNhanh.java     # Class con: Trạm sạc nhanh (12-120kW)
└── TramSacSieuNhanh.java # Class con: Trạm sạc siêu nhanh (121-300kW)
```

---

## 🚀 Hướng dẫn cài đặt & chạy chương trình

### Yêu cầu hệ thống

- **Java**: JDK 17+ (Tương thích tốt trên OpenJDK 17/21)
- **Maven**: 3.9+
- **OS**: Windows 10/11 (hoặc Linux/macOS)

### 1. Clone dự án
```bash
git clone <https://github.com/DLT0/Smart-Electric-Vehicle-Toll-Station-Management>
cd Smart-Electric-Vehicle-Toll-Station-Management
```

### 2. Build dự án

```bash
mvn clean compile
```

> **Lưu ý trên Windows:** Nếu `JAVA_HOME` hệ thống chưa trỏ đến JDK 17/21, hãy thiết lập tạm thời cho terminal:
```powershell
$env:JAVA_HOME = "C:\Program Files\Microsoft\jdk-21.0.10.7-hotspot"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
mvn clean compile
```

### 3. Chạy ứng dụng

Mở file `Main.java` và chọn **Run** trong IDE, hoặc thực thi bằng Maven qua CLI:

```bash
mvn exec:java -Dexec.mainClass="com.evstation.Main"
```

---

### Credit
> From CTK49B With Love
