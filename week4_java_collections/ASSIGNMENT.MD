# WEEK4: BÀI TẬP JAVA COLLECTIONS: HỆ THỐNG THANH TOÁN ĐA PHƯƠNG THỨC
- Sử dụng Java Collections để hoàn thành những yêu cầu sau 

### 1. Xác minh danh tính & bảo mật giao dịch
- Nếu nhập sai OTP **3 lần liên tiếp**, tài khoản bị khóa
  - Key: ID người dùng.
  - Value: Số lần nhập sai mật khẩu.

### 2. Quản lý phương thức thanh toán & hạn mức
- Sử dụng HashMap để quản lý phương thức thanh toán và hạn mức của người dùng.
- Sử dụng ArrayList để lưu lịch sử giao dịch.

### 3. Hoàn tiền (Refund)
- Sử dụng PriorityQueue để sắp xếp giao dịch theo giới hạn 7 ngày, sắp xếp theo mức độ ưu tiên sau:
  - Giao dịch lớn hơn 10,000$
  - Giao dịch nội địa 
  - Các giao dịch còn lại 

### 4. Báo cáo tài chính & tổng kết giao dịch
- Cung cấp báo cáo tài chính theo ngày, nhóm theo thứ tự thời gian 

### 5. Blacklist 
- Nếu người dùng thực hiện quá 5 giao dịch với trị giá lớn hơn 10,000$ trong 24 giờ, tài khoản bị khoá 
- Cung cấp danh sách người dùng bị khoá tài khoản 

### 6. Chia nhỏ giao dịch 
- Khi số dư không đủ, hệ thống sẽ chia nhỏ giao dịch (max = số dư của phương thức hiện tại) và cho các khoản giao dịch chưa thanh toán vào một danh sách ghi nợ  