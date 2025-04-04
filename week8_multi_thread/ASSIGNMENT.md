## Bài tập thực tế: Hệ thống xử lý dữ liệu log phân tán

Xây dựng một hệ thống xử lý các file chứa log header HTTP lấy từ https://hackertarget.com/500k-http-headers/headers/. Mỗi file có thể chứa hàng ngàn response headers.

Hãy download file header về và để trong thư mục dự án

### Mục tiêu

1.	Đọc và xử lý song song các file header bằng Thread Pool (ExecutorService).
2.	Tách từng HTTP response header (mỗi header là 1 block giữa 2 dòng trống).
3.	Với mỗi header, phân tích:
   - Status Code (200, 301, 404, …)
   - Có hoặc không có trường Server 
   - Có hoặc không có trường X-Powered-By
4.	Định kỳ (mỗi 5s) tổng hợp số lượng status theo nhóm 2xx, 3xx, 4xx, 5xx (ScheduledExecutorService).
5.	Với các file quá lớn → xử lý bằng ForkJoinPool để chia nhỏ (từng phần response).
6.	Multi-process nén các file đã xử lý (giả lập: Compressing...).

#### Cấu trúc dự án gợi ý

```
week8_multi_thread/
├── headers/
│   ├── 200/
│   │   ├── file1.txt
│   │   ├── ...
│   ├── 400/
│   └── 500/
├── src/
│   ├── Main.java
│   ├── HeaderProcessorTask.java
│   ├── HeaderAggregator.java
│   ├── ForkJoinHeaderTask.java
│   └── HeaderCompressorProcess.java
└── output/
    └── processed/
```

### Yêu cầu chi tiết

1. ExecutorService — xử lý từng file
   - Duyệt tất cả các file trong headers/200/, headers/400/, headers/500/
   - Với mỗi file, tạo Callable<HeaderStats> và submit vào fixed thread pool.
   - Trong mỗi task:
     - Đọc nội dung file 
     - Tách từng block HTTP header theo khoảng trắng (giữa các response)
     - Với mỗi block:
     - Lấy status code (dòng đầu tiên HTTP/1.1 XXX)
     - Kiểm tra có Server, X-Powered-By hay không
2. ScheduledExecutorService — tổng hợp định kỳ 
Mỗi 5 giây, in ra:
```
=== Aggregated Stats ===
2xx: 1234
3xx: 432
4xx: 88
5xx: 67
Server Header Present: 87%
X-Powered-By Present: 45%
========================
```

3. ForkJoinPool — xử lý file lớn
   - Nếu file có quá nhiều response (>1000 block), dùng ForkJoinPool chia thành các đoạn nhỏ (danh sách response block)
   - Mỗi RecursiveTask<HeaderStats> sẽ xử lý 1 phần → tổng hợp kết quả lại.

4. Multi-Process: Nén file đầu ra
   - Sau khi xử lý từng file → ghi kết quả vào output/processed/file1.stats
   - Gọi tiến trình khác (dùng ProcessBuilder) để giả lập nén:

### Gợi ý các lớp

- HeaderProcessorTask implements Callable<HeaderStats>: Xử lý 1 file headers
- HeaderAggregator implements Runnable: Tổng hợp định kỳ
- ForkJoinHeaderTask extends RecursiveTask<HeaderStats>: Xử lý 1 tập con block response
- HeaderCompressorProcess: Gọi process giải lập nén file
- HeaderStats:Class chứa thông tin tổng hợp: map status, code, % server, powered-by

### Output mẫu
```aiignore
Processing headers/200/file1.txt with Thread-1
Processing headers/400/file2.txt with Thread-2
[Scheduled Aggregator]
  2xx: 2400
  3xx: 600
  4xx: 150
  5xx: 22
  Server Header Present: 92.3%
  X-Powered-By Present: 48.7%

[Compressor] Compressing: output/processed/file1.stats
```
