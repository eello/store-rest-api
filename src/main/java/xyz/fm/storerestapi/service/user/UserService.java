package xyz.fm.storerestapi.service.user;

import xyz.fm.storerestapi.dto.user.LoginRequest;
import xyz.fm.storerestapi.dto.user.PasswordChangeRequest;
import xyz.fm.storerestapi.dto.user.UserJoinRequest;
import xyz.fm.storerestapi.dto.user.WithdrawalRequest;
import xyz.fm.storerestapi.entity.user.BaseUserEntity;

public interface UserService <T extends BaseUserEntity> {

    Boolean isExistEmail(String email);
    Boolean isExistPhoneNumber(String phoneNumber);
    T getByEmail(String email);
    T join(UserJoinRequest request);
    T login(LoginRequest request);
    void changePassword(String email, PasswordChangeRequest request);
    Boolean withdrawal(String email, WithdrawalRequest request);
}
