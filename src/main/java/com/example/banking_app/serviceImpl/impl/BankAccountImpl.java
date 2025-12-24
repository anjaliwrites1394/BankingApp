package com.example.banking_app.serviceImpl.impl;

import com.example.banking_app.dto.*;
import com.example.banking_app.entity.*;
import com.example.banking_app.exception.*;
import com.example.banking_app.mapper.UserBankMapper;
import com.example.banking_app.repository.BankAccountRepository;
import com.example.banking_app.repository.TransactionHistoryRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.ToDoubleBiFunction;
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
    private TransactionHistoryRepository transactionHistoryRepository;
    @Autowired
    private UserBankMapper userBankMapper;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private JWTService jwtService;


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

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean userExists = user.getUsername().equals(username);

        if(!userExists)
            throw new UserNotAuthorizedException("Not authorized");

        BankAccount bankAccount = userBankMapper.toBankAccount(account);
        BankAccount savedBankAccount = bankAccountRepository.save(bankAccount);

        UsersBankAccountKey key = new UsersBankAccountKey(
                userId,savedBankAccount.getBankAccountNo());
        UsersBankAccount usersBankAccount = new UsersBankAccount(key, user, savedBankAccount);

        usersBankAccountRepository.save(usersBankAccount);
        user.getUsersBankAccounts().add(usersBankAccount);
        savedBankAccount.getUsersBankAccount().add(usersBankAccount);

        return userBankMapper.toUserBankAccountResponseDto(user, savedBankAccount);
    }

    @Override
    @Transactional
    public UserBankAccountResponseDto linkUserAndAccount(Long userId, Long bankAccountNo) {

        Users user = userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("UserId not found", "userId", userId.toString()));
        BankAccount bankAccount = bankAccountRepository.findById(bankAccountNo)
                .orElseThrow(() -> new ResourceNotFoundException("Bank account is invalid", "bankAccountNo", bankAccountNo.toString()));

//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        boolean userExists = bankAccount.getUsersBankAccount().isEmpty()?
//                user.getUsername()==username :
//                bankAccount.getUsersBankAccount()
//                .stream()
//                .map(UsersBankAccount::getUser)
//                .anyMatch(users -> users.getUsername().equals(username));

        if(bankAccount.getUsersBankAccount().size()==3)
            throw new MaxUsersInAccountReachedException("Account cannot have more than 3 users");

        UsersBankAccountKey usersBankAccountKey = new UsersBankAccountKey(userId, bankAccountNo);
        if(usersBankAccountRepository.existsById(usersBankAccountKey))
            throw new UserAlreadyLinkedToAccountException("User is already linked to this account");

        UsersBankAccount usersBankAccount = usersBankAccountRepository.save(
                new UsersBankAccount(usersBankAccountKey, user, bankAccount));
        user.getUsersBankAccounts().add(usersBankAccount);
        bankAccount.getUsersBankAccount().add(usersBankAccount);

        return userBankMapper.toUserBankAccountResponseDto(user, bankAccount);

    }

    @Override
    @Transactional
    public String transferFund(Long senderAccountNo, Long receiverAccountNo, Double amount) {

        withdrawAmount(senderAccountNo, amount);
        depositAmount(receiverAccountNo, amount);

        return "SUCCESS";
    }

    @Override
    public UserBankAccountResponseDto getAccountDetails(Long bankAccountNo) {

        BankAccount bankAccount = bankAccountRepository.findById(bankAccountNo)
                .orElseThrow(() -> new ResourceNotFoundException
                        ("Bank Account Number does not exist", "bankAccountNo", bankAccountNo.toString()));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean userExists = bankAccount.getUsersBankAccount()
                .stream()
                .map(UsersBankAccount::getUser)
                .anyMatch(users -> users.getUsername().equals(username));

        if(!userExists)
            throw new UserNotAuthorizedException("Not authorized");

        return userBankMapper.toUserBankAccountResponseDto(
                bankAccount.getUsersBankAccount().get(0).getUser(), bankAccount);
    }

    @Override
    @Transactional
    public UserBankAccountResponseDto depositAmount(Long bankAccountNo, double depositAmount) {

        BankAccount bankAccount = bankAccountRepository.findById(bankAccountNo)
                .orElseThrow(() ->
                        new BankAccountDoesNotExistException("Bank Account does not exist", bankAccountNo));

        bankAccount.setCurrentBal(bankAccount.getCurrentBal()+depositAmount);
        BankAccount savedBankAccount = bankAccountRepository.save(bankAccount);

        //Saving the transaction history
        Transaction transaction = new Transaction(
                null, bankAccountNo, "DEPOSIT", depositAmount, null);
        transactionHistoryRepository.save(transaction);

        return userBankMapper.toUserBankAccountResponseDto(
                savedBankAccount.getUsersBankAccount().get(0).getUser(),
                savedBankAccount);
    }

    @Override
    @Transactional
    public UserBankAccountResponseDto withdrawAmount(Long bankAccountNo, double withdrawAmount) {

        BankAccount bankAccount = bankAccountRepository.findById(bankAccountNo)
                .orElseThrow(()->
            new BankAccountDoesNotExistException("Bank Account does not exist", bankAccountNo));

        if (withdrawAmount > bankAccount.getCurrentBal())
            throw new LowBalanceException("Low Balance");

        bankAccount.setCurrentBal(bankAccount.getCurrentBal() - withdrawAmount);
        BankAccount savedBankAccount = bankAccountRepository.save(bankAccount);

        //Saving the transaction history
        Transaction transaction = new Transaction(
                null, bankAccountNo, "WITHDRAW", withdrawAmount, null);
        transactionHistoryRepository.save(transaction);


        return userBankMapper.toUserBankAccountResponseDto(
                savedBankAccount.getUsersBankAccount().get(0).getUser(),
                savedBankAccount);
    }

    @Override
    @Transactional
    public String deleteAccount(Long bankAccountNo) {

        //Verify if account exists
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

        return "SUCCESS";
    }

    @Override
    @Transactional
    public String deleteUser(Long userId) {
        //Verify if account exists
        Optional<Users> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty())
            throw new ResourceNotFoundException("User does not exist", "userId", userId.toString());

        //This Logic has to CHANGE as user can now have 0 associated bank accounts
        //Verifies if user is authorized to delete the user from database
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean userExists = optionalUser.get().getUsersBankAccounts()
                .get(0)
                .getUser()
                .getUsername()
                .equals(username);
        if(!userExists)
            throw new UserNotAuthorizedException("Not authorized");

        //Fetch the user
        Users user = optionalUser.get();

        /*
        Delete all entries in userBankAccountTable which has this user
        Verify and delete all bank accounts that are associated only with this user
        Delete the user
        */

        List<BankAccount> bankAccountToBeDeletedList = new ArrayList<>();
        List<UsersBankAccount> usersBankAccountToBeDeletedList  = new ArrayList<>();
        //Fetch userBankAccountList of the user
        List<UsersBankAccount> usersBankAccountList = user.getUsersBankAccounts();

        //Traverse userBankAccountList
        for (UsersBankAccount usersBankAccount: usersBankAccountList){
            //Fetch the bank account for every entry in userBankAccountList
            BankAccount bankAccount = usersBankAccount.getBankAccount();

            //Delete bank account if associated with just this user
            int associatedUsersWithBankAccount = bankAccount.getUsersBankAccount().size();
            if(associatedUsersWithBankAccount == 1)
            {
                bankAccountToBeDeletedList.add(bankAccount);
                usersBankAccountToBeDeletedList.add(usersBankAccount);
            }
            else
                usersBankAccountToBeDeletedList.add(usersBankAccount);
        }

        //Delete
        usersBankAccountRepository.deleteAll(usersBankAccountToBeDeletedList);
        bankAccountRepository.deleteAll(bankAccountToBeDeletedList);
        userRepository.delete(user);

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
