package org.example.profilecase5.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;


import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "User")
public class User  {

    // Getters and Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(name = "username")
    @NotEmpty(message = "Tên người dùng không được để trống")
    private String username;

    @Column(name = "email", unique = true)
    @NotEmpty(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    public void setRole(Role role) {
        this.role = role;
    }

    @Column(name = "password", nullable = false)
    @NotEmpty(message = "Password không được để trống")
    private String password;
    @Column(name = "confirm_password", nullable = false)
    @NotBlank(message = "Xác nhận mật khẩu không được để trống")
    private String confirmPassword;

    @NotEmpty(message = "Số điện thoại không được để trống")
    private String phone;
    @Column(name = "fullname")
    @NotEmpty(message = "Tên không được để trống")
    private String fullname;

    @NotEmpty(message = "Địa chỉ không được để trống")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "ENUM('Active', 'Locked') DEFAULT 'Active'")
    private Status status = Status.Active;

    @Column(columnDefinition = "LONGTEXT")
    private String avatar;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role; // Thay đổi thành đối tượng Role duy nhất


    @Column(name = "created_at", updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp updatedAt;

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public enum Status {
        ACTIVE,   // Make sure the enum constant matches the value being passed.
        Active, LOCKED, Locked
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }



//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "user_roles",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_id")
//    )
//    private Set<Role> roles = new HashSet<>();
//
//    public Set<Role> getRoles() {
//        return roles;
//    }
//
//    public void setRoles(Set<Role> roles) {
//        this.roles = roles;
//    }


    // Getter and Setter for rentalHistories
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RentalHistory> rentalHistories = new HashSet<>();

    public void setRentalHistories(Set<RentalHistory> rentalHistories) {
        this.rentalHistories = rentalHistories;
    }
}