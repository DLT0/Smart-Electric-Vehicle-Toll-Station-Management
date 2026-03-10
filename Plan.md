# 📝 Kế hoạch chi tiết: Hệ thống Quản lý Trạm sạc Xe điện

Tài liệu này hướng dẫn chi tiết cách xây dựng hệ thống từ nền tảng logic Java Console đến giao diện người dùng hiện đại (Web UI), áp dụng các nguyên lý hướng đối tượng (OOP) nâng cao.

---

## 🏗️ Giai đoạn 1: Thiết kế Mô hình Đối tượng (OOP Design)

Dựa trên sơ đồ logic nâng cao, chúng ta sẽ triển khai cấu trúc kế thừa để tối ưu quản lý và thể hiện kỹ năng lập trình.

### 1.1. Lớp Trừu tượng: `TramSac` (Abstract Class)
- **Thuộc tính (Fields)**:
    - `maTram` (String) - **Duy nhất (Unique)**: Khóa chính, không được phép trùng lặp.
    - `tenTram` (String): Tên hiển thị hoặc vị trí của trụ sạc.
    - `trangThai` (boolean): `true` (Trống/Sẵn sàng), `false` (Đang sử dụng).
    - `thoiGianHoatDong` (double): Tổng số giờ tích lũy đã vận hành (để tính bảo trì).
    - `congSuatToiDa` (double): Giới hạn công suất tối đa của trụ sạc (kW).
- **Phương thức (Methods)**:
    - `abstract void hienThiThongTin()`: Xuất dữ liệu chi tiết từng loại trụ.
    - `abstract boolean kiemTraBaoTri()`: Trả về `true` nếu `thoiGianHoatDong > 500`.
    - `batSac()` / `tatSac()`: Thay đổi trạng thái vận hành của trụ.
    - Các hàm `Getter/Setter` để truy cập dữ liệu an toàn.

### 1.2. Lớp con: `TramSieuToc` & `TramChuan` (Subclasses)
- **TramSieuToc**: Thêm các thuộc tính riêng như `loaiTanNhiet` (Chất lỏng/Không khí), `soCongSac`.
- **TramChuan**: Thêm thuộc tính `hangSanXuat`.
- Các lớp này sẽ `@Override` (ghi đè) phương thức hiển thị để in thêm các thông số đặc thù của riêng mình.

---

## ⚙️ Giai đoạn 2: Logic Quản lý và Kiểm tra (Validation)

### 2.1. Kiểm tra dữ liệu đầu vào (Validation)
Đây là phần quan trọng để chương trình vận hành ổn định và chuyên nghiệp:
- **Kiểm tra tính duy nhất (Uniqueness)**: Trước khi thêm mới một trụ, hệ thống phải duyệt qua danh sách hiện tại để đảm bảo `maTram` chưa tồn tại.
- **Ràng buộc dữ liệu**: 
    - Công suất, thời gian hoạt động phải là số dương.
    - Tên trạm và mã trạm không được để trống (null hoặc empty).
- **Xử lý ngoại lệ**: Sử dụng `try-catch` để xử lý khi người dùng nhập chữ vào ô nhập số.

### 2.2. Thuật toán Quản lý (`QuanLyTramSac`)
- **Quản lý danh sách**: Sử dụng `ArrayList<TramSac>` để lưu trữ đa hình (chứa được cả trạm chuẩn và siêu tốc).
- **Thêm/Xóa/Sửa**: Thực hiện các thao tác CRUD cơ bản trên ArrayList.
- **Tính toán năng cao**:
    - **Tính tổng tải**: Duyệt danh sách, chỉ cộng dồn công suất của những trụ đang ở trạng thái "Đang sạc".
    - **Thống kê bảo trì**: Lọc và hiển thị danh sách các trụ cần bảo trì dựa trên hàm `kiemTraBaoTri()`.

---

## 💾 Giai đoạn 3: Lưu trữ Dữ liệu (Dùng Thư viện Gson)

Vì dự án dùng Maven, chúng ta sẽ tận dụng thư viện **Gson** để xử lý file chuyên nghiệp thay vì ghi file text thủ công.

- **Ghi File**: Chuyển đổi toàn bộ `ArrayList` sang định dạng JSON và lưu vào file `stations.json`.
- **Đọc File**: Tự độ nạp dữ liệu từ file khi mở chương trình.
- **Lưu ý**: Cần cấu hình `RuntimeTypeAdapterFactory` để Gson có thể phân biệt và khởi tạo đúng loại lớp con (SieuToc hay Chuan) khi đọc từ file JSON.

---

## 🌐 Giai đoạn 4: Backend API & Web Dashboard

Cầu nối để đưa ứng dụng từ màn hình đen trắng (Console) lên trình duyệt.

### 4.1. Web Server (Backend)
- Sử dụng `HttpServer` (có sẵn trong Java) để mở một Server tại máy cục bộ (localhost).
- Tạo cổng API: `/api/stations`. Cổng này sẽ trả về chuỗi JSON chứa danh sách trạm sạc cho bất kỳ ai gọi đến.

### 4.2. Giao diện người dùng (Web UI/UX)
- **Công nghệ**: HTML5, CSS3, JavaScript.
- **Tính năng Dashboard**:
    - Hiển thị danh sách trạm sạc dưới dạng **Card Layout**.
    - **Màu sắc trực quan**: Xanh (Trống), Vàng (Đang sạc), Đỏ (Cảnh báo bảo trì).
    - **Progress Bar**: Hiển thị thanh mức độ hoạt động dựa trên thời gian thực so với hạn mức bảo trì.

---

## 📅 Các mốc thực hiện (Milestones)

### Bước 1: Xây dựng cấu trúc lớp OOP & Validation (Ngày 1-2)
- [ ] Hoàn thành bộ khung lớp trừu tượng và lớp con.
- [ ] Viết bộ logic kiểm tra mã trùng lặp và nhập liệu.

### Bước 2: Hoàn thiện Logic Quản lý & Lưu trữ (Ngày 3-4)
- [ ] Cài đặt các hàm CRUD trong Module.
- [ ] Tích hợp Gson để lưu/đọc dữ liệu bền vững.

### Bước 3: Backend API & WebServer (Ngày 5)
- [ ] Triển khai HttpServer để cung cấp dữ liệu JSON.

### Bước 4: Thiết kế Front-end Web (Ngày 6-7)
- [ ] Dựng giao diện Dashboard bằng HTML/CSS.
- [ ] Dùng JavaScript Fetch lấy dữ liệu từ Backend Java và hiển thị lên Web.

---
*Lưu ý: Mặc dù tài liệu này viết có dấu, nhưng các thông báo in ra màn hình Console Java vẫn nên để không dấu để đảm bảo tương thích tốt nhất trên Windows.*
