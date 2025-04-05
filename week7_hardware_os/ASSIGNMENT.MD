# Week 7: Hardware / OS

1. What are the two main components of a CPU?

- Answer:
 - ALU (Arithmetic Logic Unit): Thực hiện các phép toán số học và logic. 
 - CU (Control Unit): Điều phối hoạt động của CPU, hướng dẫn luồng dữ liệu và điều khiển việc thực thi lệnh. 

2. What is the purpose of the ALU?

- Answer:
  - ALU chịu trách nhiệm thực hiện các phép toán số học (như cộng, trừ) và logic (như AND, OR, NOT) trong CPU. 

3. What is the purpose of the memory hierarchy?

- Answer:
  - Mục đích của memory hierarchy là cân bằng giữa tốc độ và chi phí, đảm bảo hệ thống chạy nhanh mà vẫn tiết kiệm tài nguyên. Gồm các cấp độ: registers > cache > RAM > disk, trong đó cấp trên nhanh hơn nhưng đắt hơn và nhỏ hơn. 

4. What is the difference between a hard drive and a solid state drive?

- Answer:
  - Hard Drive (HDD): Dùng đĩa từ để lưu trữ, có phần cơ học quay → chậm hơn, rẻ hơn, dung lượng cao. 
  - State Drive (SSD): Dùng chip flash không có bộ phận chuyển động → nhanh hơn, đắt hơn, tuổi thọ cao hơn.

5. What is the purpose of the operating system?

- Answer:
  - Hệ điều hành (OS) là phần mềm trung gian giữa người dùng và phần cứng, chịu trách nhiệm quản lý tài nguyên hệ thống, quản lý tiến trình, bộ nhớ, thiết bị, và hệ thống tệp.

6. What is the difference between a process and a thread?

- Answer:
  - Process: Là một chương trình đang chạy, có không gian bộ nhớ riêng.
  - Thread: Là luồng nhẹ hơn trong process, chia sẻ bộ nhớ với các thread khác trong cùng process. 
  - Một process có thể có nhiều thread.

7. How does the operating system handle thread synchronization?

- Answer:
  - Hệ điều hành cung cấp các cơ chế như mutexes, semaphores, monitors để đảm bảo các thread không xung đột khi truy cập tài nguyên chung, từ đó tránh deadlock và race condition.

8. What is the purpose of the scheduler?

- Answer:
  - Scheduler là thành phần của OS quyết định thread nào hoặc tiến trình process nào sẽ được CPU thực hiện tiếp theo, nhằm tối ưu hiệu suất, công bằng, và đảm bảo phản hồi nhanh trong hệ thống đa nhiệm.

9. What is the purpose of the file system?

- Answer:
  - File system cung cấp cách lưu trữ, tổ chức, truy cập và bảo vệ dữ liệu trên thiết bị lưu trữ, cho phép người dùng tạo, đọc, ghi và xóa các file một cách hiệu quả.

10. What is the purpose of the file system?

- Answer:
  - File system cung cấp cách lưu trữ, tổ chức, truy cập và bảo vệ dữ liệu trên thiết bị lưu trữ, cho phép người dùng tạo, đọc, ghi và xóa các file một cách hiệu quả.

11. How does the file system handle file access control?

- Answer:
  - File system quản lý quyền truy cập bằng cách áp dụng các quyền như read, write, execute với mỗi file/thư mục, dựa trên người sở hữu, nhóm, và người dùng khác. 
  - Một số hệ điều hành còn hỗ trợ ACL (Access Control List) để kiểm soát chi tiết hơn.
