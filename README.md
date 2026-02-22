## Smart-Electric-Vehicle-Toll-Station-Management
Chương trình quản lý việc cho thuê các trụ sạc trong một bãi xe. Cho phép thêm mới trụ sạc, theo dõi trạng thái (trống/đang sạc) và tính toán số tiền khách phải trả khi kết thúc lượt sạc dựa trên loại trụ.

# Các chức năng chính

1.	Thêm mới trụ sạc: Cho phép người quản lý thêm một trụ mới vào hệ thống.
  o	Chọn loại: Trạm Chuẩn hoặc Trạm Siêu Tốc.
  o	Nhập thông số riêng: (Ví dụ: Trạm siêu tốc yêu cầu nhập thêm thông tin về Hệ thống làm mát).
2.	Cập nhật thông số kỹ thuật: Chỉnh sửa các thông số cố định của trụ sạc (vị trí, công suất tối đa, loại đầu cắm) khi có sự thay đổi về phần cứng.
3.	Xóa/Gỡ bỏ trụ sạc: Loại bỏ các trụ sạc cũ hoặc đã hỏng hoàn toàn khỏi danh sách quản lý.
4.	Cập nhật trạng thái thời gian thực: * Thay đổi trạng thái giữa: Trống (Available), Đang sạc (Charging), Bảo trì (Maintenance).
  o	Logic bảo vệ: Không cho phép chuyển trạng thái thủ công sang "Trống" nếu trụ đang trong tiến trình sạc (giả lập).
5.	Ghi nhận thời gian hoạt động: Cập nhật số giờ đã chạy của trụ sạc mỗi khi kết thúc một phiên sạc để theo dõi độ bền thiết bị.
6.	Kiểm soát tải (Load Control): Ngăn chặn việc kích hoạt sạc nếu tổng công suất của các trụ đang chạy vượt quá định mức cho phép của toàn trạm.
7.	Xem danh sách tổng thể: Hiển thị toàn bộ các trụ sạc trong hệ thống. Mỗi loại trụ sẽ tự in ra các thông số đặc trưng của nó (Ví dụ: Trạm siêu tốc hiển thị thêm nhiệt độ vận hành).
8.	Bộ lọc thông minh (Smart Filter): * Lọc nhanh các trụ đang bị Lỗi/Hỏng để kỹ thuật viên đi sửa.
  o	Lọc các trụ đang Trống để điều hướng xe vào sạc.
9.	Cảnh báo bảo trì định kỳ: Liệt kê danh sách các trụ đã hoạt động quá số giờ quy định (ví dụ > 500 giờ) và cần được kiểm tra kỹ thuật.
10.	Tìm kiếm theo Mã số (ID): Truy xuất nhanh thông tin chi tiết của một trụ cụ thể khi có sự cố báo về.
11.	Thống kê hiệu suất: Tính toán tổng lượng điện năng (kWh) đã phục vụ trên toàn hệ thống để quản lý biết trạm nào đang hoạt động hiệu quả nhất.
12.	Lưu trữ dữ liệu (Khuyên dùng): Đọc/Ghi danh sách trụ sạc ra file .txt hoặc .json để dữ liệu không bị mất khi tắt chương trình.

