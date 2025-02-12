
# Week 1: Git

1. Git thực sự lưu trữ dữ liệu như thế nào? Các đối tượng (objects) như blob, tree, commit, và tag có quan hệ gì với nhau trong hệ thống lưu trữ phân tán của Git?

- Answer: Git lưu trữ lưu trữ ảnh chụp (snapshot) của toàn bộ dự án tại mỗi commit, không lưu các file dưới dạng từng phiên bản riêng biệt. Dữ liệu này được lưu trong thư mục .git và bao gồm 4 đối tượng chính:
    + Blob (Binary Large Object): 1 blob sẽ lưu trữ nội dung 1 file dưới dạng SHA-1.
    + Tree: biểu diễn cấu trúc thư mục, lưu trữ danh sách các blob và các tree con.
    + Commit: 1 commit trỏ đến 1 tree và tham chiếu đến commit cha.
    + Tag: gắn nhãn, đánh dấu phiên bản cho 1 commit.

2. Khi ta di chuyển giữa các commit, Git làm gì dưới lớp vỏ bọc của nó? HEAD thực chất là gì và nó tác động thế nào đến trạng thái hiện tại của repository?

- Answer: 
   + Khi di chuyển giữa các commit, Git sẽ thực hiện thay đổi trạng thái của HEAD.
   + HEAD là 1 pointer trỏ đến commit hiện tại đang làm việc, thường là trỏ đến nhánh hiện tại.
   + Ví dụ: git checkout <branch> thì từ nhánh hiện tại HEAD sẽ trỏ đến nhánh chỉ định.

3. Git có thể tạo hàng nghìn nhánh (branches) gần như tức thì mà không tốn tài nguyên. Điều này hoạt động ra sao về mặt kỹ thuật, và tại sao việc hợp nhất (merge) đôi khi lại dẫn đến xung đột?

- Answer:
    + Khi tạo nhánh mới, mỗi nhánh chỉ là một con trỏ (reference) trỏ đến 1 commit thay vì sao chép toàn bộ nội dung.
    + Tại sao việc hợp nhất (merge) đôi khi lại dẫn đến xung đột?
  
  --> Nếu 2 nhánh thay đổi các file khác nhau, Git có thể tự dộng hợp nhất --> Ko xảy ra conflict.
  
  --> Nếu 2 nhánh thay đổi cùng một dòng code trong cùng một file, Git ko thể quyết định giữ cái nào và bỏ phần nào --> xảy ra conflict.

4. Khi ta thêm file vào staging area, thực chất Git làm gì? Điều gì thực sự thay đổi khi ta chạy lệnh git commit?

- Answer:
    - Khi git add <file>, Git sẽ tạo 1 blob object trong staging area.
    - Khi git commit, Git sẽ làm gì:
      + Lưu file thành các blob.
      + Tạo các tree tham chiếu đến các blob đó.
      + tạo ra commit gắn với tree đó và tham chiếu đến commit cha.

5. Git phân biệt working directory, staging area và repository như thế nào? Tại sao có những thay đổi được theo dõi nhưng chưa được commit?

- Answer:
    + Working directory: chứa các file đang làm việc.
    + Staging area: vùng tạm chứa các thay đổi đã đc add nhưng chưa commit.
    + Repository: kho chứa toàn bộ lịch sử commit.
  - Tại sao có những thay đổi được theo dõi nhưng chưa được commit?
  
  --> Untracked (chưa đc thay đổi): khi thêm file mới vào thư mục dự án mà chưa git add --> Git ko theo dõi file này.
  
  --> Modified (đã đc thay đổi nhưng thay đổi chưa đc staged): khi chỉnh sửa 1 tệp đã đc commit trước đó mà chưa add --> Git nhận ra thay đổi nhưng chưa đưa vào staging area.
  
  --> Stagged (đã thêm vào stagging area nhưng chưa commit): sau khi git add --> lưu vào stagging area nhưng chưa thực sự lưu vào lịch uuwr commit.
  
  --> Git có nhiều trạng thái để theo dõi thay đổi nhưng ko commit ngay để giúp chọn lọc những gì sẽ được commit.

6. Git lưu trữ thay đổi của ta ở đâu? Làm thế nào Git có thể khôi phục những thay đổi đã bị "stash" một cách chính xác?

- Answer:
  - Git lưu trữ thay đổi ở đâu?
    + Các commit và objects được lưu trong .git/objects. 
    + Tham chiếu đến nhánh và HEAD được lưu trong .git/refs
    + Thông tin về stash được lưu trong .git/logs/refs/stash
  - Làm thế nào Git có thể khôi phục những thay đổi đã bị "stash" một cách chính xác?
    + Khi git stash, Git tạo 1 commit ẩn (dangling commit), ko liên kết với nhánh nào.
    + Dùng git stash apply hoặc git stash pop để lấy lại các thay đổi từ các commit ẩn đó và đưa vào working directory.
    + git stash apply: Không xóa stash, có thể áp dụng lại nếu cần.
    + git stash pop: Xóa stash sau khi áp dụng thành công.
      

7. Khi xảy ra xung đột hợp nhất (merge conflict), tại sao Git không thể tự động giải quyết? Git sử dụng cơ chế nào để xác định sự khác biệt và yêu cầu sự can thiệp của lập trình viên?

- Answer:
    + Git ko thể tự động merge nếu 2 nhánh thay đổi cùng một dòng code trong cùng một file, Git ko thể quyết định giữ cái nào và bỏ phần nào.
    + Để xác định sự khác biệt, Git sử dụng thuật toán diff và merge để tìm sự khác biệt giữa các commit. Một số phương pháp phổ biến gồm:
      1.  Two-way merge:
          + Chỉ so sánh HEAD và nhánh cần merge.
          --> Ko có thông tin về commit chung, commit gốc --> ít đc sử dụng
      2. Three-way merge:
         + Commit chung gần nhất của 2 nhánh.
         + Commit của nhánh hiện tại (HEAD).
         + Commit của nhánh cần merge.
          --> Nếu không có xung đột, Git tự động merge. Nếu có xung đột, Git yêu cầu lập trình viên sửa thủ công conflict đó.

8.  Git duy trì lịch sử commit như thế nào? Cấu trúc cây (tree structure) của Git giúp tối ưu hóa việc theo dõi và truy xuất lịch sử ra sao?

- Answer:
  - Git duy trì lịch sử commit như thế nào?
    + Liên kết các commit lại với nhau thông qua cấu trúc DAG (Directed Acyclic Graph).
    + DAG là đồ thị có hướng không chu trình, nghĩa là:
      + Mỗi commit trỏ đến commit cha của nó. 
      + 1 commit không thể trỏ ngược về chính nó. 
      --> Điều này giúp Git tổ chức lịch sử commit không thể bị lặp vô hạn.
    + Mỗi commit gắn với một SHA-1 hash duy nhất trỏ đến commit cha --> tra cứu lịch sử.
    + git reflog: Giúp phục hồi commit đã bị mất do reset hoặc rebase. 
    + git gc: Gom rác, loại bỏ commit không còn tham chiếu.
    
  - Cấu trúc cây (tree structure) của Git giúp tối ưu hóa việc theo dõi và truy xuất lịch sử ra sao?
    + Cây tree tổ chức file theo thư mục, cho phép Git theo dõi từng file riêng biệt mà không cần lưu trữ toàn bộ dự án nhiều lần.
    + Khi dùng git diff, Git không so sánh từng file, mà chỉ cần đối chiếu các hash tree --> truy xuất nhanh hơn.
  
9. Những lệnh này khác nhau như thế nào về bản chất? Tại sao git reset --hard có thể gây mất dữ liệu nhưng git revert thì không?

- Answer:
  - git reset --hard <commit>: 
    + git reset đặt lại vị trí của HEAD và nhánh hiện tại về một commit trước đó. 
    + --hard sẽ xóa toàn bộ thay đổi trong staging area (index) và working directory.
    
  --> Có thể gây mất dữ liệu vì sau khi chạy git reset --hard <commit>, tất cả commit sau <commit> sẽ bị xóa khỏi nhánh.

  - git revert <commit>: 
    + Tạo một commit mới để đảo ngược thay đổi, không làm mất lịch sử.
    
  --> git revert không gây mất dữ liệu vì nó không xóa commit, mà chỉ tạo thêm commit mới để hủy bỏ các thay đổi.


10. Khi dùng git commit --amend, Git thực sự làm gì? Nó có chỉnh sửa commit cũ không, hay tạo ra một commit hoàn toàn mới?

- Answer:
    + Git không sửa commit cũ, mà tạo một commit mới có cùng nội dung nhưng với thông tin được cập nhật.
    + Khi git commit --amend, thông tin có thể cập nhật:
      + Message của commit. 
      + Thay đổi trong staging area (nếu có file mới được thêm vào trước khi amend).
    + Commit cũ sẽ trở thành một dangling commit nếu không còn tham chiếu.