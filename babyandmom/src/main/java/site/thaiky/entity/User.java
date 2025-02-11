package site.thaiky.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
@Entity
public class User {
    @NotNull(message = "User ID cannot be null!")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long userId;

    @NotNull(message = "Role ID cannot be null!")
    public Integer roleId;

    @NotBlank(message = "Name cannot be blank!")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters!")
    public String name;

    @NotBlank(message = "Email cannot be blank!")
    @Email(message = "Invalid email format!")
    public String email;

    @NotBlank(message = "Password cannot be blank!")
    @Size(min = 8, message = "Password must be at least 8 characters long!")
    public String password;

    @NotNull(message = "Phone number cannot be null!")
    @Pattern(regexp = "^(?:\\\\+84|0)(?:3[2-9]|5[6|8|9]|7[0|6-9]|8[1-5]|9[0-4|6-9])[0-9]{7}$", message = "Phone number must contain 10 to 15 digits!")
    // (?:\\+84|0): Cho phép số điện thoại bắt đầu bằng +84 (mã quốc gia) hoặc 0 (đầu số nội địa)
    // 3[2-9]: Các đầu số 32 đến 39 (Viettel).
    //5[6|8|9]: Các đầu số 56, 58, 59 (Vietnamobile và Gmobile).
    //7[0|6-9]: Các đầu số 70, 76 đến 79 (Mobifone).
    //8[1-5]: Các đầu số 81 đến 85 (Vinaphone).
    //9[0-4|6-9]: Các đầu số 90 đến 94 và 96 đến 99 (Viettel, Mobifone, Vinaphone).
     //[0-9]{7}: 7 chữ số cuối của số điện thoại.
    public Integer phone;

    @NotBlank(message = "Status cannot be blank!")
    public String status;

    @PastOrPresent(message = "Update time cannot be in the future!")
    public LocalDateTime updateAt;

    @PastOrPresent(message = "Creation time cannot be in the future!")
    public LocalDateTime createAt;

    @NotNull(message = "Deletion status cannot be null!")
    public Boolean isDeleted;
}
