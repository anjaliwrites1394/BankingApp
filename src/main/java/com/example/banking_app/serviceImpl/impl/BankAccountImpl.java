package com.example.banking_app.serviceImpl.impl;

import com.example.banking_app.dto.*;
import com.example.banking_app.entity.BankAccount;
import com.example.banking_app.entity.Users;
import com.example.banking_app.entity.UsersBankAccount;
import com.example.banking_app.entity.UsersBankAccountKey;
import com.example.banking_app.exception.LowBalanceException;
import com.example.banking_app.exception.ResourceNotFoundException;
import com.example.banking_app.exception.UserAlreadyExistsException;
import com.example.banking_app.exception.UserNotAuthorizedException;
import com.example.banking_app.mapper.UserBankMapper;
import com.example.banking_app.repository.BankAccountRepository;
import com.example.banking_app.repository.UserRepository;
import com.example.banking_app.repository.UsersBankAccountRepository;
import com.example.banking_app.serviceImpl.BankAccountService;
import com.example.banking_app.serviceImpl.JWTService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
@Service
public class BankAccountImpl implements BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UsersBankAccountRepository usersBankAccountRepository;
    @Autowired
    private UserBankMapper userBankMapper;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private JWTService jwtService;


//    @Override
//    @Transactional
//    public UserBankAccountResponseDto addAccount(UserBankAccountRequestDto account) {
//
//        Users user =  userBankMapper.toUser(account);
//        Users existingUser = userRepository.findByUsername(user.getUsername());
//        if(existingUser != null)
//            throw new UserAlreadyExistsException("User Already exists", "username", user.getUsername());
//
//        user.setPassword(encoder.encode(user.getPassword()));
//        Users savedUser = userRepository.save(user);
//        BankAccount bankAccount = userBankMapper.toBankAccount(account);
//        BankAccount savedBankAccount = bankAccountRepository.save(bankAccount);
//
//        UsersBankAccountKey key = new UsersBankAccountKey(
//                savedUser.getUserId(),savedBankAccount.getBankAccountNo());
//        UsersBankAccount usersBankAccount = new UsersBankAccount(key, savedUser, savedBankAccount);
//
//        savedUser.getUsersBankAccounts().add(usersBankAccount);
//        savedBankAccount.getUsersBankAccount().add(usersBankAccount);
//
//        return userBankMapper.toUserBankAccountResponseDto(savedUser, savedBankAccount);
//    }

    @Override
    @Transactional
    public UserResponseDto registerUser(UserRequestDto userRequestDto) {
        Users user =  userBankMapper.toUser(userRequestDto);

        Users existingUser = userRepository.findByUsername(user.getUsername());
        if(existingUser != null)
            throw new UserAlreadyExistsException("User Already exists", "username", user.getUsername());

        user.setPassword(encoder.encode(user.getPassword()));
        Users savedUser = userRepository.save(user);
        return userBankMapper.toUserResponseDto(savedUser);
    }

    @Override
    public UserBankAccountResponseDto createAccount(Long userId, AccountRequestDto account) {

        Users user = userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("UserId not found", "userId", userId.toString()));
        BankAccount bankAccount = userBankMapper.toBankAccount(account);
        BankAccount savedBankAccount = bankAccountRepository.save(bankAccount);

        UsersBankAccountKey key = new UsersBankAccountKey(
                userId,savedBankAccount.getBankAccountNo());
        UsersBankAccount usersBankAccount = new UsersBankAccount(key, user, savedBankAccount);

        user.getUsersBankAccounts().add(usersBankAccount);
        savedBankAccount.getUsersBankAccount().add(usersBankAccount);

        return userBankMapper.toUserBankAccountResponseDto(user, savedBankAccount);
    }


    @Override
    public UserBankAccountResponseDto getAccountDetails(Long bankAccountNo) {

        Optional<BankAccount> optionalBankAccount = bankAccountRepository.findById(bankAccountNo);
        if(optionalBankAccount.isEmpty())
            throw new ResourceNotFoundException(
                    "Bank Account Number does not exist", "bankAccountNo", bankAccountNo.toString());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean userExists = optionalBankAccount.get().getUsersBankAccount()
                .stream()
                .map(UsersBankAccount::getUser)
                .anyMatch(users -> users.getUsername().equals(username));

        if(!userExists)
            throw new UserNotAuthorizedException("Not authorized");

        return userBankMapper.toUserBankAccountResponseDto(
                optionalBankAccount.get().getUsersBankAccount().get(0).getUser(), optionalBankAccount.get());
    }

    @Override
    @Transactional
    public UserBankAccountResponseDto depositAmount(Long bankAccountNo, double depositAmount) {

        Optional<BankAccount> optionalBankAccount = bankAccountRepository.findById(bankAccountNo);
        if(optionalBankAccount.isEmpty())
            throw new ResourceNotFoundException("Bank Account does not exist", "bankAccountNo", bankAccountNo.toString());

        BankAccount bankAccount = optionalBankAccount.get();
        bankAccount.setCurrentBal(bankAccount.getCurrentBal()+depositAmount);
        BankAccount savedBankAccount = bankAccountRepository.save(bankAccount);

        return userBankMapper.toUserBankAccountResponseDto(
                savedBankAccount.getUsersBankAccount().get(0).getUser(),
                savedBankAccount);
    }

    @Override
    @Transactional
    public UserBankAccountResponseDto withdrawAmount(Long bankAccountNo, double withdrawAmount) {

        Optional<BankAccount> optionalBankAccount = bankAccountRepository.findById(bankAccountNo);
        if (optionalBankAccount.isEmpty())
            throw new ResourceNotFoundException("Bank Account does not exist", "bankAccountNo", bankAccountNo.toString());

        BankAccount bankAccount = optionalBankAccount.get();
        if (withdrawAmount > bankAccount.getCurrentBal())
            throw new LowBalanceException("Low Balance");

        bankAccount.setCurrentBal(bankAccount.getCurrentBal() - withdrawAmount);
        BankAccount savedBankAccount = bankAccountRepository.save(bankAccount);

        return userBankMapper.toUserBankAccountResponseDto(
                savedBankAccount.getUsersBankAccount().get(0).getUser(),
                savedBankAccount);
    }

    @Override
    @Transactional
    public String deleteAccount(Long bankAccountNo) {

        //Verify user exists
        Optional<BankAccount> optionalBankAccount = bankAccountRepository.findById(bankAccountNo);
        if (optionalBankAccount.isEmpty())
            throw new ResourceNotFoundException("Bank Account does not exist", "bankAccountNo", bankAccountNo.toString());

        //Verifies if user is authorized to delete the bank account
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean userExists = optionalBankAccount.get().getUsersBankAccount()
                .get(0)
                .getUser()
                .getUsername()
                .equals(username);
        if(!userExists)
            throw new UserNotAuthorizedException("Not authorized");

        //Bank Account to be deleted
        BankAccount bankAccount = optionalBankAccount.get();
        //List of UsersBankAccount associated to the BankAccount that needs to be deleted
        List<UsersBankAccount> usersBankAccountList = bankAccount.getUsersBankAccount();
        //List of users that are associated with the bank account
        Set<Users> potentiallyOrphanedUsers = usersBankAccountList
                .stream()
                .map(UsersBankAccount::getUser)
                .collect(Collectors.toSet());
        //Remove all rows with bankaccount no in userBankAccount
        usersBankAccountRepository.deleteAll(usersBankAccountList);
        //Remove bank account
        bankAccountRepository.delete(bankAccount);

        //Remove all users who are associated ONLY with this bank account
        for (Users users: potentiallyOrphanedUsers){
            List<UsersBankAccount> remaining = usersBankAccountRepository.findByUser(users);
            if(remaining.isEmpty())
                userRepository.delete(users);
        }
        return "SUCCESS";
    }

    @Override
    public String verify(Users user) {
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        Authentication authentication =  authManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        if(authentication.isAuthenticated())
            return jwtService.generateToken(user.getUsername());
        throw new UserNotAuthorizedException("Not authorized");
    }




}
