# Ke hoach hoan thien chuong trinh Quan ly Tram sac Xe dien

Tai lieu nay phac thao cac buoc de hoan thien code cho sinh vien nam nhat, tap trung vao tinh don gian va hoc ve OOP + File I/O.

## Giai doan 1: Tao lop Mo hinh (Model)
- [ ] Tao file `Station.java` trong package `com.evstation`.
- [ ] Dinh nghia cac thuoc tinh co ban: `id`, `ten`, `trangThai`, `congSuat`.
- [ ] Tao Constructor, Getter/Setter va phuong thuc `toString()` de hien thi thong tin.

## Giai doan 2: Quan ly du lieu trong Bo nho
- [ ] Trong `Module.java`, khai bao mot danh sach `ArrayList<Station>` de luu tru tam thoi cac tru sac.
- [ ] Cai dat logic cho cac ham:
    - `themTruSac`: Nhap thong tin tu ban phim va add vao list.
    - `xemDanhSach`: Duyet list va in ra man hinh.
    - `xoaTruSac`: Tim theo ID va xoa khoi list.
    - `timKiem`: Loc danh sach theo ID hoac ten.

## Giai doan 3: Luu tru va Doc du lieu tu File (File I/O)
- [ ] **Luu file (Write):**
    - [ ] Tao ham `luuDuLieu()` trong `Module.java`.
    - [ ] Dung `FileWriter` va `BufferedWriter` de ghi tung dong du lieu vao file `data.txt`.
    - [ ] Moi tru sac ghi tren 1 dong, phan cach cac thuoc tinh bang dau phay `,`.
- [ ] **Doc file (Read):**
    - [ ] Tao ham `docDuLieu()` trong `Module.java`.
    - [ ] Dung `FileReader` va `BufferedReader` de doc file luc bat dau chuong trinh.
    - [ ] Su dung `split(",")` de tach dong va tao lai doi tuong `Station`.

## Giai doan 4: Hoan thien va Kiem thu
- [ ] Cap nhat `Menu.java` de goi cac ham luu/doc file.
- [ ] Kiem tra loi khi file khong ton tai hoac du lieu bi sai dinh dang.
- [ ] To uu giao dien bang de hien thi danh sach tru sac dep hon.

---
*Luu y: Tat ca code se su dung tieng Viet khong dau de tranh loi hien thi tren Console Windows.*
