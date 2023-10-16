# quizlet rest api 
![image](https://github.com/hdnguyen02/quizlet-api/assets/83913057/d913a227-e398-4e4a-9426-b27e0d993332)
## cách chạy
Ứng dụng này được đóng kèm theo Tomcat, không cần cài **Tomcat** hoặc **JBoss**. Bạn chạy nó bằng lệnh __java -jar__
## api client
https://github.com/hdnguyen02/quizlet
## về ứng dụng 
- Cung cấp api cho ứng dụng web client hoặc mobie app
- Cung cấp **flash card** áp dụng thuật toán **SuperMemo 2**
- Tổ chức lớp học
- Cung cấp bài kiểm tra nhanh dựa trên flash card
## SuperMemo 2
- Spaced Repetition (SR) là một kỹ năng học tập dựa trên việc tính toán các khoảng thời gian giữa các lần ôn lại bài học tuỳ theo độ khó của bài học và trí nhớ của người học.
- SR thích hợp trong nhiều hoàn cảnh, đặc biệt trong trường hợp người học cần phải ghi nhớ một lượng lớn nội dung, ví dụ như học từ mới **ngoại ngữ**.
- SuperMemo (Super Memory - SM) là một phương pháp học tập và phần mềm được phát triển bởi SuperMemo World và SuperMemo R&D, tác giả là Piotr Woźniak người Phần Lan từ năm 1985 tới nay. Thuật toán này dựa trên nghiên cứu về trí nhớ dài hạn và ứng dụng phương pháp SR được đề xuất bởi một số nhà tâm lý học vào đầu những năm 1930.
- Công thức:
```
I(1):=1
I(2):=6
for n>2 I(n):=I(n-1)*EF
```
Trong đó:
```
I(n) (interval): trả về khoảng thời gian đối tượng sẽ lặp lại (tính bằng ngày) sau n lần thử
EF (E-Factor): hệ số độ dễ, phản ánh độ dễ hay khó của đối tượng trong việc ghi nhớ.
EF: biến thiên từ 1.1 (khó nhất) và 2.5 (dễ nhất), mặc định khi một đối tượng được lưu vào database sẽ có Ef = 2.5. Trong quá trình học, giá trị này sẽ tăng hoặc giảm tuỳ thuộc vào sự ghi nhớ của người học.
```
- Giá trị EF mới được tính toán dựa trên chất lượng câu trả lời của người học, lựa chọn 1 trong 6 tuỳ chọn:

5 - Hoàn hảo
4 - Trả lời chính xác nhưng còn phải đắn đo
3 - Trả lời chính xác nhưng gặp nhiều khó khăn
2 - Trả lời không chính xác, đáp án đúng dễ dàng nhớ ra
1 - Trả lời sai, nhớ được đáp án
0 - Hoàn toàn không nhớ
Công thức:
```
EF’:=EF+(0.1-(5-q)*(0.08+(5-q)*0.02))
```
Trong đó:
```
EF’ - giá trị mới của E-Factor
EF - giá trị cũ của E-Factor
q - giá trị của câu trả lời (0~5)
```
- Khi EF < 1.3, gán EF = 1.3 (Đối tượng có EF < 1.3 sẽ lặp lại thường xuyên gây khó chịu)
- Khi giá trị câu trả lời nhỏ hơn 3, ta tiến hành lập lại đối tượng từ đầu mà không thay đổi EF (VD: I(1), I(2) coi như đối tượng được học mới)
- Sau mỗi lần học của 1 ngày, lặp lại tất cả các đối tượng có giá trị trả lời (q) nhỏ hơn 4. Tiếp tục lặp lại cho đến khi toàn bộ các đối tượng có giá trị trả lời ít nhất là 4.  
## Công nghệ sử dụng  
- Spring boot
- Spring security
- jwt
- jpa
- lombok
- validation
- inversion of control, dependency injection, etc...
## Diagram
## Thực thể 
## endpoints
- update sau ... 
```

```


