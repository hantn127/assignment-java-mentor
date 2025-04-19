## 🧪 BÀI TẬP: ỨNG DỤNG THEO DÕI LOG FILE REALTIME VÀ PHÂN TÍCH DỮ LIỆU

### 🎯 **Mục tiêu**:
Xây dựng một ứng dụng giám sát thư mục `logs/`, mỗi khi có **file log mới được tạo hoặc chỉnh sửa**, chương trình sẽ **đọc nội dung log mới**, sau đó:

- Đọc và phân tích nội dung file sử dụng **Java IO BufferedReader** (Synchronous, Blocking)
- Giám sát sự kiện tạo/sửa file sử dụng **Java NIO WatchService**
- Log kết quả ra màn hình, đồng thời ghi vào một file `processed_logs.txt`
- Có thể phân biệt rõ giữa:
    - `Java NIO`: để giám sát thay đổi thư mục
    - `Java IO`: để đọc nội dung file
    - `BufferedReader`, `BufferedWriter`: để đọc/ghi hiệu quả

---

### 📁 Cấu trúc thư mục:
```
📂 log-monitor
├── logs/
│   ├── sample.log (log file sẽ được thêm/sửa)
├── processed_logs.txt (output lưu kết quả phân tích)
└── LogMonitorApp.java
```

---

## 🔨 YÊU CẦU CỤ THỂ

### 🎯 Yêu cầu chức năng:

1. **Giám sát thư mục `logs/`**:
    - Nếu có file mới hoặc file bị chỉnh sửa → xử lý file đó

2. **Đọc nội dung file theo từng dòng** bằng `BufferedReader`:
    - Với mỗi dòng, kiểm tra nếu chứa từ khóa `"ERROR"` thì:
        - Ghi dòng đó vào `processed_logs.txt` (append) bằng `BufferedWriter`

3. **Ghi log ra console** theo format:
   ```
   [INFO] Đã phát hiện file mới/sửa: sample.log
   [PROCESSING] Dòng lỗi ghi vào processed_logs.txt: [ERROR] ...
   ```

---

## 📘 GỢI Ý CÀI ĐẶT:

## 🧠 GIẢI THÍCH MINDSET VÀ TƯ DUY KẾT HỢP

| Thành phần | Kỹ thuật dùng | Giải thích |
|------------|---------------|------------|
| Giám sát thư mục | `WatchService` (NIO) | Cơ chế Event Loop để theo dõi file được tạo/sửa |
| Đọc nội dung file | `BufferedReader` (IO) | Đọc file line-by-line, tiết kiệm bộ nhớ |
| Ghi log | `BufferedWriter` | Ghi hiệu quả, không bị flush liên tục |
| Blocking / Synchronous | `watchService.take()` | Blocking wait để tối ưu CPU, không polling |
| Tính ứng dụng | Monitoring hệ thống log | Có thể dùng trong microservice, DevOps, logging pipelines |

---

## 🧪 MỞ RỘNG THÊM:

- Đưa ra cấu hình bằng file `.properties` (ví dụ: thư mục cần giám sát)
- Cho phép theo dõi nhiều định dạng file (`*.log`, `*.txt`,...)
- Thêm menu console: `exit`, `clear log`, `xem log gần nhất`
- Thêm filter nâng cao: `WARNING`, `INFO`, `FATAL`
- Tạo thread riêng cho việc xử lý log nếu nhiều file cùng lúc

---
