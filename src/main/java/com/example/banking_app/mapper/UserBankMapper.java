package com.example.banking_app.mapper;

import com.example.banking_app.dto.*;
import com.example.banking_app.entity.BankAccount;
import com.example.banking_app.entity.Users;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.util.List;


@Mapper(componentModel="spring")
public interface UserBankMapper {

    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "email", target = "email")
    public Users toUser(UserRequestDto userRequestDto);

    @Mapping(source = "accountType", target = "accountType")
    @Mapping(source = "currentBal", target = "currentBal")
    public BankAccount toBankAccount(AccountRequestDto accountRequestDto);

    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "bankAccount.accountType", target = "accountType")
    @Mapping(source = "bankAccount.currentBal", target = "currentBal")
    //@Mapping(source = "bankAccount.userList", target = "userList" )
    public UserBankAccountResponseDto toUserBankAccountResponseDto
            (Users user, BankAccount bankAccount);

    @AfterMapping
    default void populateBankAccountNos(@MappingTarget UserBankAccountResponseDto dto, Users user) {
        if (user.getUsersBankAccounts() != null) {
            List<Long> accountNos = user.getUsersBankAccounts().stream()
                    .map(uba -> uba.getBankAccount().getBankAccountNo())
                    .toList();
            dto.setBankAccountNumbers(accountNos);
        }
    }
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "email", target = "email")
    UserResponseDto toUserResponseDto(Users savedUser);
}

