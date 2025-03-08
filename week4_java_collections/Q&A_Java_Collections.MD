
# Week 4: Java Collections

1. Sự khác biệt giữa HashSet, LinkedHashSet và TreeSet? Trong trường hợp nào thì nên dùng cái nào?

- Answer:
  - HashSet:
    - Không đảm bảo thứ tự của các phần tử. 
    - Dùng hàm băm (hashing) để lưu trữ phần tử. 
    - Tìm kiếm nhanh nhưng không có thứ tự. 
    - Dùng khi không cần thứ tự của phần tử và chỉ quan tâm đến sự khác biệt trong phần tử.
    
  - LinkedHashSet:
    - Đảm bảo thứ tự thêm vào của các phần tử (đúng theo thứ tự insertion). 
    - Tương tự HashSet nhưng duy trì một danh sách liên kết các phần tử. 
    - Tìm kiếm chậm hơn HashSet một chút, nhưng đảm bảo thứ tự.
    - Dùng khi cần thứ tự của các phần tử nhưng không cần sắp xếp theo giá trị.
    
  - TreeSet:
    - Duy trì phần tử theo thứ tự tăng dần (hoặc giảm dần nếu có Comparator). 
    - Dựa trên Red-Black Tree (cây tìm kiếm nhị phân tự cân bằng). 
    - Dùng khi cần sắp xếp phần tử. 
    - Dùng Khi cần các phần tử được sắp xếp hoặc cần kiểm tra phần tử theo giá trị. 

2. Làm thế nào để HashSet đảm bảo các phần tử không trùng lặp?

- Answer:
  - HashSet sử dụng hàm băm (hashing) để xác định vị trí của mỗi phần tử trong bộ nhớ. Mỗi phần tử có một giá trị băm độc nhất. 
  - Khi thêm phần tử mới vào, HashSet kiểm tra xem đã có phần tử nào có giá trị băm giống với phần tử mới chưa. Nếu có, phần tử không được thêm vào, đảm bảo sự không trùng lặp. 
  - equals() và hashCode() được sử dụng để xác định sự trùng lặp giữa các phần tử.

3. Sự khác biệt giữa HashMap, LinkedHashMap và TreeMap? Trong trường hợp nào thì nên dùng cái nào?

- Answer:
  - HashMap:
    - Không duy trì thứ tự của các phần tử. 
    - Dùng hàm băm (hashing) để lưu trữ cặp key-value. 
    - Tìm kiếm nhanh nhưng không có thứ tự. 
    - Dùng khi không quan tâm đến thứ tự của các phần tử. 
    
  - LinkedHashMap:
    - Duy trì thứ tự theo thêm vào. 
    - Tìm kiếm chậm hơn HashMap nhưng có thể giữ thứ tự của các phần tử. 
    - Dùng khi cần duy trì thứ tự thêm vào và không cần sắp xếp theo key.
    
  - TreeMap:
    - Duy trì thứ tự sắp xếp tự nhiên của các key (hoặc theo Comparator nếu được cung cấp).
    - Dùng Red-Black Tree để tổ chức các cặp key-value.
    - Tìm kiếm chậm hơn HashMap do cần phải duy trì thứ tự sắp xếp.
    - Dùng khi cần các cặp key-value được sắp xếp theo thứ tự của key. 

4. Từ sau Java 8, HashMap xử lý collisions như thế nào?

- Answer:
  - Từ Java 8 trở đi, HashMap sử dụng cấu trúc LinkedList hoặc TreeNode (cây nhị phân) để xử lý các collisions (trùng lặp băm) khi số lượng phần tử trong một bucket vượt quá một ngưỡng nhất định (thường là 8). 
  - Nếu số phần tử trong một bucket vượt quá 8, HashMap sẽ chuyển từ LinkedList sang Red-Black Tree, giúp tăng tốc độ tìm kiếm từ O(n) xuống O(log n). 

5. Tại sao String và Integer thường được dùng làm key trong HashMap?
- Answer:
  - String và Integer là các loại immutable và có hàm hashCode() ổn định. 
  - Đảm bảo tính độc nhất khi so sánh bằng hàm equals() và hiệu quả khi băm.

6. Cách hoạt động của Red-black Tree khi thêm, xoá phần tử?
- Answer:
  - Red-Black Tree là một dạng cây tìm kiếm nhị phân tự cân bằng.
  - Khi thêm một phần tử:
    - Phần tử mới được thêm vào như trong cây nhị phân bình thường.
    - Sau đó, cây sẽ tự cân bằng lại bằng cách thay đổi màu của các nút (đỏ hoặc đen) và thực hiện các phép quay để duy trì tính chất của cây Red-Black.
  - Khi xoá một phần tử:
    - Phải thực hiện một số phép quay và thay đổi màu sắc của các nút để đảm bảo rằng cây vẫn giữ được tính chất Red-Black. 

7. AVL Tree là gì? Có ưu điểm và nhược điểm gì?
- Answer:
  - AVL Tree là một cây nhị phân tìm kiếm tự cân bằng, chênh lệch độ cao giữa cây con trái và phải của mỗi nút không bao giờ vượt quá 1.
  - Ưu điểm:
    - Đảm bảo độ cân bằng rất tốt, cho phép các phép toán tìm kiếm, chèn và xoá có độ phức tạp O(log n).
  - Nhược điểm:
    - Do yêu cầu phải kiểm tra và điều chỉnh cây sau mỗi thao tác, việc thêm và xoá phần tử có thể chậm hơn một chút so với cây nhị phân tìm kiếm bình thường.

8. B-Tree là gì? Được sử dụng như thế nào?
- Answer:
  - B-Tree là một loại cây tìm kiếm tự cân bằng, thường được sử dụng trong các hệ thống lưu trữ dữ liệu ngoài bộ nhớ (như cơ sở dữ liệu).
  - B-Tree được tối ưu hóa cho việc truy cập dữ liệu từ các thiết bị ngoại vi (như đĩa cứng), giúp giảm thiểu số lượng lần truy cập đĩa.
  - Mỗi nút trong B-Tree có thể có nhiều con (không giống như cây nhị phân), và có thể lưu trữ nhiều phần tử trong mỗi nút.
  - Sử dụng trong các hệ quản trị cơ sở dữ liệu (DBMS), hệ thống tệp tin, và các ứng dụng yêu cầu hiệu suất truy vấn cao.
