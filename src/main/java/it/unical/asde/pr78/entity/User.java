package it.unical.asde.pr78.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(
        name = "users"
)
public final class User extends BaseEntity {

    public static final int STATUS_CREATED = 1;
    public static final int STATUS_ACTIVE = 2;
    public static final int STATUS_INACTIVE = 3;

    @Column(
            name = "full_name",
            nullable = false
    )
    @NotNull
    private String fullName;

    @Column(
            name = "email",
            unique = true,
            nullable = false
    )
    @NotNull
    private String email;

    @Column(
            name = "`password`",
            nullable = false
    )
    @Length(min = 8)
    @JsonIgnore
    private String password;

    @Column(
            name = "`status`",
            nullable = false,
            columnDefinition = "TINYINT UNSIGNED"
    )
    @NotNull
    private int status;

    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    @JoinTable(
            name = "users_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)},
            uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "role_id"})}
    )
    private List<Role> roles;

    @Transient
    private String confirmPassword;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getId().equals(user.getId());
    }

    @Override
    public String toString() {
        return "User{" +
                "fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public boolean isActive() {
        return getStatus() == STATUS_ACTIVE;
    }
}
