
# Week 3: OOP

1. Lập trình hướng đối tượng là gì? So sánh với hướng lập trình thủ tục

- Answer:
    - OOP (Object-Oriented programming) Lập trình hướng đối tượng trong đó mọi thứ xoay quanh object. Mỗi đối tượng đc tạo ra dựa trên khuôn mẫu cụ thể gọi là class. 1 class sẽ chứa các thuộc tính và phương thức để mô tả đối tượng.
    - Lập trình hướng thủ tục (POP) thì dựa trên các hàm, thủ tục, thường được dùng gải quyết các bài toán tính toán theo hướng hàm, cần performance cao.
    - Nhược điểm của POP:
      - Tạo 1 biến toàn cục, bất cứ đâu cũng có thể truy cập được, dễ bị thay đổi.
      - Ví dụ thêm các đối tượng mới thì lại phải khai báo lại những thuộc tính, khó tái sử dụng code.
      - Khó mở rộng cho các bài toán quản lí, giảm đi tính toàn vẹn của thiết kế ban đầu.

2. Giải thích 4 nguyên tắc cơ bản OOP: Encapsulation, abstraction, Inheritance, Polymorphism

- Answer:
  - Abstraction (Tính trừu tượng): Chỉ hiển thị những gì cần thiết, che giấu chi tiết thực hiện, cung cấp cho người dùng 1 giao diện chung cho các đối tượng cùng loại (như một lớp trung gian). Cách để đạt đc abstraction là bao gồm Abstract class và Interface.
  - Encapsulation (Tính đóng gói): Giấu thông tin bên trong class, chỉ cho phép truy cập thông qua các phương thức được cho phép. Cách triển khai encapsulation trong OOP dựa trên 4 mức độ truy cập: public, private, protected, default.
  - Inheritance (Kế thừa): Cho phép một class kế thừa thuộc tính và phương thức của class khác. Các cách inheritance trong java: extends, implement, super.
  - Polymorphism (Đa hình): Một hành động có thể có nhiều cách thực hiện khác nhau (method overriding, method overloading).

3. Interface và abstract Class khác nhau như thế nào?

- Answer:
  - Interface tập trung vào việc định nghĩa chức năng chung mà một class phải thực hiện. Ví dụ: Interface Flyable áp dụng cho mọi thứ có khả năng bay: chim, máy bay --> có thể ko liên quan đến nhau ngoại trừ có đặc điểm là có thể bay. 
  - Abstract class tạo ra thiết kế chung cho các lớp con kế thừa. Ví dụ: Abstract class Animal đại diện cho logic cơ bản của động vật (như move, eat), vì chó và mèo đều là động vật và có thể dùng chung đặc điểm này.

4. Một class có thể kế thừa nhiều class cùng lúc không? nếu không thì tại sao?

- Answer:
  - Một class ko thể kế thừa nhiều class, vì để tránh vấn đề khi hai lớp cha có cùng một phương thức, lớp con không biết nên kế thừa từ lớp nào. Tuy nhiên, Java hỗ trợ đa kế thừa thông qua interface.

5. Làm thế nào để đảm bảo một class tuân theo nguyên tắc Encapsulation
- Answer:
  - Cách triển khai encapsulation trong OOP dựa trên 4 mức độ truy cập: public, private, protected, default.
  - Khai báo các thuộc tính ở phạm vi private. 
  - Cung cấp các phương thức getter và setter để truy cập và thay đổi dữ liệu. 
  - Hạn chế quyền truy cập trực tiếp vào dữ liệu của class.

6. Tại sao nên sử dụng getter và setter thay vì truy cập trực tiếp biến instance?
- Answer:
  - Kiểm soát quyền truy cập dữ liệu. 
  - Dễ dàng thay đổi logic xử lý mà không ảnh hưởng đến mã nguồn cũ. Ví dụ như khi cần cập nhật một thuộc tính thì chỉ cần chỉnh sửa trong setter chứ ko cần chỉnh sửa ở mọi nơi đã dùng đến thuộc tính này. 
  - Giúp đảm bảo Encapsulation bằng cách ẩn dữ liệu bên trong class.

7. Một class có thể vừa là abstract class và là interface được không? Tại sao Java không hỗ trợ đa kế thừa với class, nhưng lại hỗ trợ đa kế thừa với interface?
- Answer:
  - Không, vì abstract class đã là một class, còn interface không phải là class. 
  - Java không hỗ trợ đa kế thừa với class để tránh xung đột về phương thức.
  - Interface là 1 trường hợp đặc biệt của abstract, nhưng khác với abstract class thì interface trừu tượng hoàn toàn. Vì abstract class thì có thể khai báo method với implementation, còn interface chỉ có thể khai báo hằng và abstract method.

8. Khi nào sử dụng abstract class thay vì interface?
- Answer:
  - Sử dụng abstract class khi cần cung cấp các thuộc tính chung cần chia sẻ giữa các lớp con.
  - Sử dụng Interface khi cần cung cấp các hành vi bổ sung cho class và những hành vì này không bắt buộc đối với class đó.

9. Sự khác nhau giữa method overloading và method overriding
- Answer:
  - Method overloading: các phương thức cùng tên nhưng khác nhau về tham số --> cùng 1 HĐ nhưng thực hiện theo cách khác nhau.
  - Method overriding: phương thức trong lớp con ghi đè phương thức trong lớp cha --> cùng 1 HĐ nhưng khác vai trò của đối tượng.

10. Có thể override một phương thức static không? về mặt ý nghĩa thì tại sao?
- Answer:
  - Không thể override phương thức static, vì phương thức static thuộc về class, không thuộc về instance. 
  - Nếu một phương thức static được định nghĩa lại trong lớp con, đó chỉ là hiding chứ không phải overriding.

11. Garbage Collection hoạt động như thế nào trong Java? Làm sao để buộc hệ thống thực hiện thu gom rác?
- Answer: 
  - Garbage Collection (GC) trong Java tự động thu hồi bộ nhớ của các đối tượng không còn được tham chiếu.
  - Các thuật toán GC:
    - Mark and Sweep: Đánh dấu các đối tượng còn sử dụng và dọn dẹp những đối tượng không còn tham chiếu. 
    - Generational GC: Chia bộ nhớ heap thành các generations (Young Generation, Old Generation) để tối ưu quá trình dọn dẹp.
  - Cách buộc hệ thống thực hiện GC:
    - Gọi System.gc(), nhưng chỉ là gợi ý, JVM có thể bỏ qua. 
    - Dọn dẹp reference bằng cách đặt biến thành null hoặc dùng WeakReference.
