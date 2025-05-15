package BOIM_Project.boim.dto.request;

import BOIM_Project.boim.entity.User;
import BOIM_Project.boim.status.Role;

public record SignUpRequest(String userAccount, String userPassword, String userName, Role userRole) {

    public User toUserEntity() {
        return User.create(this.userAccount, this.userPassword, this.userName, this.userRole);
    }
}
