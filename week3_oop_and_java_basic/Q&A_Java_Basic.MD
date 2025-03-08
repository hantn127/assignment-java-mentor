
# Week 3: Java basic

1. Sự khác biệt giữa dữ liệu primitive và non-primitive trong Java là gì?

- Answer:
    - Primitive types: 
      - Là các kiểu dữ liệu nguyên thủy được Java cung cấp sẵn, như byte, short, int, long, float, double, boolean, char. 
      - Giá trị được lưu trực tiếp tại vị trí bộ nhớ mà biến đó đc cấp phát.
      - Biến cục bộ (khai báo trong 1 phương thức, khối lệnh): lưu trong Stack Memory.
      - Biến ko tĩnh (khai báo bên trong 1 class, ko phải static): lưu trong Heap.
      - Biến tĩnh (từ khóa static): lưu trong Method Area.
    - Non-primitive types: 
      - Là các kiểu dữ liệu tham chiếu như String, Array, Class, Object, Interface, Enum.
      - Stack Memory: lưu tham chiếu đến các đối tượng trong Heap.
      - Heap: lưu các đối tượng và các biến instance thuộc đối tượng đó.

2. tại sao String là non-primitive type trong khi có vẻ nó chỉ chứa kí tự?

- Answer:
    - String là một lớp thuộc java.lang.String, có các phương thức và trạng thái riêng, vì vậy nó được quản lý như một kiểu dữ liệu tham chiếu (non-primitive).
    - Tuy nhiên, Java cho phép khởi tạo String giống như kiểu dữ liệu nguyên thủy.

3. Sự khác biệt giữa int và Integer là gì? khi nào nào dùng cái nào?

- Answer:
    - int là kiểu dữ liệu nguyên thủy (primitive type), lưu trữ trực tiếp giá trị số nguyên trong bộ nhớ. 
    - Integer là một wrapper class, đại diện cho int dưới dạng object. Dùng khi cần lưu trữ số nguyên trong cấu trúc dữ liệu như ArrayList<Integer> (vì ArrayList không hỗ trợ int). 

4. Auto-boxing và unboxing là gì? Khi nào chúng xảy ra?

- Answer:
    - Auto-boxing: Tự động chuyển đổi từ kiểu primitive sang wrapper class.
    - Unboxing: Tự động chuyển đổi từ wrapper class về primitive type.
    - Chúng xảy ra khi có sự tương tác giữa các kiểu dữ liệu primitive khác với wrapper class của chúng.

5. Sự khác biệt giữa mảng tĩnh (Array) và danh sách động (ArrayList) trong Java?- Answer:
- Answer:
    - Array:
      - Kích thước cố định khi khai báo.
      - Có thể chứa cả kiểu primitive và object.
    - ArrayList:
      - Kích thước có thể thay đổi linh hoạt. 
      - Chỉ chứa object (vd: ArrayList<Integer>).

6. Sự khác biệt giữa String, StringBuffer, và StringBuilder?
- Answer:
  - String: immutable, mỗi khi thay đổi giá trị sẽ tạo một object mới chứ ko thay đổi trên object đã tạo.
  - StringBuffer: mutable, có thread-safe, thích hợp khi làm với nhiều luồng.
  - StringBuilder: mutable, ko có thread-safe vì thế sẽ nhanh hơn StringBuffer.

7. Giải thích về String Pool trong Java? Tại sao Java sử dụng String Pool?
- Answer:
  - String Pool là một vùng nhớ đặc biệt trong heap, nơi Java lưu trữ các chuỗi String literal để tối ưu hóa bộ nhớ.
  - vd: 
    String s1 = "Hello"; 
    String s2 = "Hello"; // Không tạo object mới, dùng lại từ String Pool
  - Java sử dụng String Pool để tránh tạo nhiều object trùng lặp, tiết kiệm bộ nhớ.


8. String là immutable, điều này có nghĩa gì? Tại sao String được thiết kế theo cách này?
- Answer:
  - Immutable nghĩa là khi một String được tạo, giá trị của nó không thể thay đổi. Nếu sửa đổi, Java sẽ tạo object mới thay vì chỉnh sửa object cũ. 
  - Lợi ích:
    - Hiệu suất: Giúp Java tối ưu hóa bộ nhớ bằng String Pool. 
    - Thread-safe: Không cần đồng bộ hóa khi làm việc với nhiều luồng.

9. intern() method trong String làm gì?
- Answer:
  - intern() kiểm tra xem chuỗi đã tồn tại trong String Pool chưa. Nếu có, nó trả về tham chiếu đến chuỗi đó thay vì tạo mới. 
  - vd:
    String s1 = new String("Hello");
    String s2 = s1.intern(); // s2 trỏ đến chuỗi trong String Pool

10. Chuyện gì xảy ra nếu thay đổi một giá trị của String được tạo bằng String literal?
- Answer:
  - Khi thay đổi 1 giá trị của String được tạo bằng String Literal thì Java sẽ kiểm tra trong String Pool xem có chuỗi đó tồn tại hay chưa, nếu tồn tại thì ko cập nhật, lấy luôn địa chỉ của chuỗi đó. Còn nếu như ko có thì sẽ thêm đối tượng mới vào String Pool. 

11. Toán tử == và .equals() khác nhau thế nào khi so sánh Object?
- Answer: 
  - == so sánh địa chỉ tham chiếu của hai object.
  - .equals() vẫn là dạng ==, là một method được các class khác override lại dùng để so sánh nội dung của hai object.

12. Tại sao 
double a = 0.1
double b = 0.2
boolean check = (a + b) == 0.3
tại sao check lại bằng false?
Nếu double có nhược điểm như vậy tại sao nó vẫn tồn tại
- Answer:
  - Máy tính lưu trữ số thực theo dạng nhị phân nhưng không phải mọi số thập phân đều có thể biểu diễn chính xác trong hệ nhị phân. Các số như 0.1 và 0.2 có dạng nhị phân lặp vô hạn, nên khi lưu trữ trong kiểu double (giới hạn 64-bit), giá trị của chúng chỉ là xấp xỉ chứ không hoàn toàn chính xác. Khi cộng hai số xấp xỉ, kết quả cũng sẽ bị xấp xỉ, và nó sẽ ra kết quả 0.1 + 0.2 không chính xác 100% là 0.3
  - Tại sao double vẫn tồn tại?
    - double chỉ chiếm 64-bit (1 bit cho sign, 11 bit cho exponent, 52 bit cho phần mantisa). Cách lưu trữ này giúp tối ưu hóa tài nguyên so với việc dùng một chuỗi bit vô hạn để lưu số thực chính xác.
    - Khi cần độ chính xác cao như tiền tệ, nên dùng BigDecimal.
