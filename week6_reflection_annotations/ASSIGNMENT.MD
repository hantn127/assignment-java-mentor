
# Week 6: Java Reflection and Annotations

1. Reflection có thể thay đổi các thành phần final trong một lớp được không? Code ví dụ minh hoạ cách thay đổi một biến static final.

- Answer:
  - Reflection có thể thay đổi giá trị của biến static final.

            public class FinalFieldExample {
                public static final String CONSTANT = "Original";

                public static void main(String[] args) throws Exception {
                    Field field = FinalFieldExample.class.getDeclaredField("CONSTANT");
                    field.setAccessible(true);

                    Field modifiersField = Field.class.getDeclaredField("modifiers");
                    modifiersField.setAccessible(true);
                    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

                    field.set(null, "Modified");

                    System.out.println(CONSTANT); 
                    System.out.println(FinalFieldExample.CONSTANT);
                }
            }

2. Reflection có ảnh hưởng như thế nào đến hiệu suất của một ứng dụng Java?

- Answer:
  - Reflection chạy chậm hơn so với code thông thường vì:
    - Tránh tối ưu JIT (Just-In-Time Compiler). 
    - Kiểm tra kiểu tại runtime thay vì compile-time. 
    - Truy cập các thành phần bị ẩn như private field/method. 

3. Rủi ro về bảo mật khi sử dụng reflection? Làm thế nào để giảm thiểu rủi ro này?

- Answer:
  - Rủi ro:
    - Truy cập và thay đổi field/method private, có thể vi phạm nguyên tắc OOP, gây mất an toàn về vấn đề bảo mật. 
    - Gọi các hàm hoặc tải class từ nguồn không tin cậy. 
    - Có thể bị khai thác để thực hiện hành vi độc hại.
  
  - Giảm thiểu:
    - Không gọi setAccessible(true) trừ khi cần thiết.
    - Kiểm tra input trước khi dùng Reflection để tạo hoặc gọi class. 

4. Làm thế nào để truy cập thông tin về nested classes và anonymous classes bằng Reflection? 

   - Answer:
     - Trong Java, các nested classes và anonymous classes có thể được truy cập thông qua Reflection bằng cách sử dụng các phương thức của lớp Class. 
       - Class.getDeclaredClasses() – Trả về tất cả các lớp được khai báo bên trong một lớp cụ thể (kể cả private). 
       - Class.getClasses() – Trả về các lớp public được định nghĩa bên trong lớp đó và các lớp cha. 
       - Có thể truy xuất thông tin lớp ẩn danh thông qua đối tượng của lớp đó bằng Object.getClass() và lấy tên lớp bằng Class.getName(). 
       - Class.isAnonymousClass() – Kiểm tra một lớp có phải là lớp ẩn danh không. 
       - Class.isMemberClass() – Kiểm tra lớp có phải là lớp thành viên không. 
       - Class.getEnclosingClass() – Trả về lớp bao quanh nếu lớp hiện tại là lớp lồng trong.
       

5. Cơ chế hoạt động của setAccessible(true)?

- Answer: 
  - setAccessible(true) bỏ qua kiểm tra Java access control (như private, protected) cho field, method, hoặc constructor. Khi gọi, JVM sẽ cho phép thao tác trên member bị hạn chế.

6. Giải thích cơ chế hoạt động của Annotation Processor? Làm thế nào chúng được tích hợp vào quá trình biên dịch?

- Answer:
  - Annotation Processor là thành phần xử lý các annotation ở compile-time, không phải runtime. 
  - Được tích hợp vào trình biên dịch (javac) qua javax.annotation.processing.Processor. 
  - Tự động chạy khi biên dịch nếu phát hiện annotation phù hợp.

7. Annotation processors có ảnh hưởng như thế nào đến quá trình biên dịch?

- Answer:
  - Tăng thời gian biên dịch nếu xử lý annotation phức tạp hoặc sinh nhiều mã. 
  - Tự động sinh class/code, giúp giảm code lặp nhưng cũng tăng độ phức tạp trong debug. 
  - Không ảnh hưởng đến runtime.

8.  Làm thế nào để tạo một annotation có thể được kế thừa?

- Answer: Có thể sử dụng @Inherited

9. Phân biệt các loại RetentionPolicy của @Retention?

- Answer:
  - SOURCE: Annotation bị loại bỏ sau khi compile. Không có trong bytecode.
  - CLASS: Có trong bytecode, nhưng không có tại runtime. Dùng bởi compiler. 
  - RUNTIME: Có trong bytecode và có thể truy xuất bằng Reflection lúc runtime.

10. Sự khác biệt giữa reflection và annotation processors trong việc xử lý annotation?

- Answer:
  - Reflection:
    - Thời điểm hoạt động: Runtime
    - Mục đích chính: Đọc thông tin metadata lúc chương trình chạy
    - Truy cập annotation: Qua API Reflection (Class, Method, Field)
    - Không thể sinh ra source code mới
  - Annotation processors:
    - Thời điểm hoạt động: Compile-time 
    - Mục đích chính: Phân tích và xử lý annotation khi biên dịch 
    - Truy cập annotation: Qua processingEnv trong AbstractProcessor 
    - Có thể sinh code Java mới