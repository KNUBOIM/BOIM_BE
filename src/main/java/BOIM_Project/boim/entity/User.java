package BOIM_Project.boim.entity;

import BOIM_Project.boim.status.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(name = "USER_TB")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
    @ManyToOne
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;
    */

    @Column(name = "user_account", nullable = false)
    private String userAccount;

    @Column(name = "user_password", nullable = false)
    private String userPassword;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private Role userRole;

    protected User(){
    }

    @Builder
    public User(String userAccount, String userPassword, String userName, Role userRole){
        this.userAccount = userAccount;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userRole = userRole;
    }

    public static User create(String userAccount, String userPassword, String userName, Role userRole) {

        return User.builder()
            .userAccount(userAccount)
            .userPassword(userPassword)
            .userName(userName)
            .userRole(userRole)
            .build();
    }
}
