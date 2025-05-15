package BOIM_Project.boim.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Table(name = "REFRESH_TOKENS_TB")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;

    protected RefreshToken(){
    }

    @Builder
    public RefreshToken(User user, String token, Instant expiryDate){
        this.user = user;
        this.token = token;
        this.expiryDate = expiryDate;
    }

    public static RefreshToken createEntity(User user, String token, Instant expiryDate){

        return RefreshToken.builder()
            .user(user)
            .token(token)
            .expiryDate(expiryDate)
            .build();
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }
}
