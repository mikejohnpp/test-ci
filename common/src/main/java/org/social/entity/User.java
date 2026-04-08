package org.social.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "userID", nullable = false)
    private Integer id;

    @Column(name = "fullName", nullable = false)
    private String fullName;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "password")
    private String password;

    @Column(name = "phoneNumber", length = 50)
    private String phoneNumber;

    @ColumnDefault("current_timestamp()")
    @Column(name = "createAt", nullable = false)
    private Instant createAt;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "statusID", nullable = false)
    private StatusUser statusID;

    @Column(name = "activateCode", length = 200)
    private String activateCode;

    @Column(name = "lockUntil")
    private Instant lockUntil;

    @ColumnDefault("'0'")
    @Column(name = "failPassword")
    private String failPassword;

    @ColumnDefault("0")
    @Column(name = "accountType")
    private Integer accountType;

    @ColumnDefault("0")
    @Column(name = "banned")
    private Integer banned;

    @Column(name = "otpCode", length = 10)
    private String otpCode;

    @Column(name = "otpExpireTime")
    private Long otpExpireTime;

    public String getOtpCode() {
        return otpCode;
    }

    public Long getOtpExpireTime() {
        return otpExpireTime;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public void setOtpExpireTime(Long otpExpireTime) {
        this.otpExpireTime = otpExpireTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Instant getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    }

    public StatusUser getStatusID() {
        return statusID;
    }

    public void setStatusID(StatusUser statusID) {
        this.statusID = statusID;
    }

    public String getActivateCode() {
        return activateCode;
    }

    public void setActivateCode(String activateCode) {
        this.activateCode = activateCode;
    }

    public Instant getLockUntil() {
        return lockUntil;
    }

    public void setLockUntil(Instant lockUntil) {
        this.lockUntil = lockUntil;
    }

    public String getFailPassword() {
        return failPassword;
    }

    public void setFailPassword(String failPassword) {
        this.failPassword = failPassword;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public Integer getBanned() {
        return banned;
    }

    public void setBanned(Integer banned) {
        this.banned = banned;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", createAt=" + createAt +
                ", statusID=" + statusID +
                ", activateCode='" + activateCode + '\'' +
                ", lockUntil=" + lockUntil +
                ", failPassword='" + failPassword + '\'' +
                ", accountType=" + accountType +
                ", banned=" + banned +
                ", otpCode='" + otpCode + '\'' +
                ", otpExpireTime=" + otpExpireTime +
                '}';
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}