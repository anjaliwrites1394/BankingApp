package com.example.banking_app.unitTests;

import com.example.banking_app.dto.*;
import com.example.banking_app.entity.*;
import com.example.banking_app.exception.*;
import com.example.banking_app.mapper.UserBankMapper;
import com.example.banking_app.repository.BankAccountRepository;
import com.example.banking_app.repository.TransactionHistoryRepository;
import com.example.banking_app.repository.UserRepository;
import com.example.banking_app.repository.UsersBankAccountRepository;
import com.example.banking_app.serviceImpl.impl.BankAccountImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BankAccountUnitTest {

    @Mock
    UserRepository userRepository;

    @Mock
    BankAccountRepository bankAccountRepository;

    @Mock
    BCryptPasswordEncoder encoder;

    @Mock
    UserBankMapper mapperRepository;

    @Mock
    TransactionHistoryRepository transactionHistoryRepository;

    @Mock
    UsersBankAccountRepository usersBankAccountRepository;

    @Mock
    SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    BankAccountImpl bankAccountService;


    Long min = 1L;
    Long max = 200L;
    double minDouble = 10000.0;
    double maxDouble = 100000.0;
//@Test
//public void addAccountSuccessTest(){
//    String firstName = UUID.randomUUID().toString().substring(0, 7);
//    String lastName = UUID.randomUUID().toString().substring(0, 7);
//    String email = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6)
//            + "@gmail.com";
//    String username = UUID.randomUUID().toString().substring(0, 7);
//    String password = UUID.randomUUID().toString().substring(0, 14);
//
//    double min = 10000.0;
//    double max = 100000.0;
//    double currentBal = ThreadLocalRandom.current().nextDouble(min, max);
//    String accountType = UUID.randomUUID().toString().substring(0, 7);
//
//    UserBankAccountRequestDto userBankAccountRequestDto = new UserBankAccountRequestDto(
//            firstName, lastName, email, username, password, currentBal, accountType);
//
//    //    Users existingUser = new Users(id, firstName, lastName, email, username, password, new ArrayList<>());
//    Long id = new Random().nextLong(900);
//    Mockito.when(mapperRepository.toUser(userBankAccountRequestDto)).thenReturn(
//            new Users(id, firstName, lastName, email, username, password, new ArrayList<>()));
//    Mockito.when(userRepository.findByUsername(username)).thenReturn(null);
//    Mockito.when(encoder.encode(password)).thenReturn(password);
//
//    Users savedUser = new Users(id, firstName, lastName, email, username, password, new ArrayList<>());
//    when(userRepository.save(Mockito.any(Users.class))).thenReturn(savedUser);
//
//    Long bankId = new Random().nextLong(900);
//    BankAccount savedBankAccount = new BankAccount(
//            bankId, new ArrayList<>(), currentBal, LocalDateTime.now(), LocalDateTime.now(), accountType);
//    when(mapperRepository.toBankAccount(userBankAccountRequestDto)).thenReturn(savedBankAccount);
//    when(bankAccountRepository.save(Mockito.any(BankAccount.class))).thenReturn(savedBankAccount);
//
//    // Expected response
//    UserBankAccountResponseDto expectedResponse = new UserBankAccountResponseDto(
//            firstName, lastName, username, email, currentBal, accountType, List.of(bankId));
//    when(mapperRepository.toUserBankAccountResponseDto(Mockito.any(Users.class), Mockito.any(BankAccount.class)))
//            .thenReturn(expectedResponse);
//
//    UserBankAccountResponseDto response = bankAccountService.addAccount(userBankAccountRequestDto);
//
//    //Verify and assert
//    Mockito.verify(userRepository, times(1)).save(Mockito.any(Users.class));
//    Mockito.verify(bankAccountRepository, times(1)).save(Mockito.any(BankAccount.class));
//   // Mockito.verify(usersBankAccountRepository, times(1)).save(Mockito.any(UsersBankAccount.class));
//    Mockito.verify(mapperRepository, times(1)).toUserBankAccountResponseDto
//            (Mockito.any(Users.class), Mockito.any(BankAccount.class));
//
//    assertNotNull(response);
//    assertEquals(expectedResponse.getUsername(), response.getUsername());
//    assertEquals(expectedResponse.getEmail(), response.getEmail());
//    assertEquals(expectedResponse.getCurrentBal(), response.getCurrentBal());
//    assertEquals(expectedResponse.getAccountType(), response.getAccountType());
//
//}
//
//    @Test
//    public void addAccountUserAlreadyExistsException(){
//        Long id = new Random().nextLong(900);
//        String firstName = UUID.randomUUID().toString().substring(0, 7);
//        String lastName = UUID.randomUUID().toString().substring(0, 7);
//        String email = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6)
//                + "@gmail.com";
//        String username = UUID.randomUUID().toString().substring(0, 7);
//        String password = UUID.randomUUID().toString().substring(0, 14);
//
//        double currentBal = ThreadLocalRandom.current().nextDouble(min, max);
//        String accountType = UUID.randomUUID().toString().substring(0, 7);
//
//        UserBankAccountRequestDto userBankAccountRequestDto = new UserBankAccountRequestDto(
//                firstName, lastName, email, username, password, currentBal, accountType);
//
//        Long bankId = new Random().nextLong(900);
//        BankAccount savedBankAccount = new BankAccount(
//                bankId, new ArrayList<>(), currentBal, LocalDateTime.now(), LocalDateTime.now(), accountType);
//
//        Users existingUser = new Users(id, firstName, lastName,email, username, password, new ArrayList<>());
//        Mockito.when(mapperRepository.toUser(userBankAccountRequestDto)).thenReturn(existingUser);
//        Mockito.when(userRepository.findByUsername(username)).thenReturn(existingUser);
//
//        //Verify
//        assertThrows(UserAlreadyExistsException.class, ()->bankAccountService.addAccount(userBankAccountRequestDto));
//        verify(userRepository, times(1)).findByUsername(username);
//        verify(mapperRepository, never()).toBankAccount(any());
//        verify(bankAccountRepository, never()).save(any());
//        verify(mapperRepository, never()).toUserBankAccountResponseDto(existingUser, savedBankAccount);
//    }
//
    private BankAccount createBankAccount(Long bankAccountNo, double currentBal, String accountType ){
        return new BankAccount(
                bankAccountNo, new ArrayList<>(), currentBal, LocalDateTime.now(), LocalDateTime.now(), accountType);
    }
//
//    //Validate the @Valid fields for AddAccount later
//    @Test
//    public void getAccountDetailsSuccessTest(){
//
//        Long bankAccountNo = ThreadLocalRandom.current().nextLong(min, max);
//        String firstName = UUID.randomUUID().toString().substring(0, 7);
//        String lastName = UUID.randomUUID().toString().substring(0, 7);
//        String email = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6)
//                + "@gmail.com";
//        String username = UUID.randomUUID().toString().substring(0, 7);
//        String password = UUID.randomUUID().toString().substring(0, 14);
//        double currentBal = ThreadLocalRandom.current().nextDouble(minDouble, maxDouble);
//        String accountType = UUID.randomUUID().toString().substring(0, 7);
//
//        //Creating Bank Account
//        BankAccount bankAccount = createBankAccount(bankAccountNo, currentBal, accountType);
//        Mockito.when(bankAccountRepository.findById(bankAccountNo)).thenReturn(Optional.of(bankAccount));
//
//        // Mock Security Context
//        SecurityContextHolder.setContext(securityContext);
//        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
//        Mockito.when(authentication.getName()).thenReturn(username);
//
//        //Creating User
//        Long userId = ThreadLocalRandom.current().nextLong(200);
//        Users user = new Users(userId, firstName, lastName, email, username, password, new ArrayList<>());
//
//        //Creating UsersBankAccount
//        UsersBankAccountKey key = new UsersBankAccountKey(userId, bankAccountNo);
//        UsersBankAccount usersBankAccount = new UsersBankAccount();
//        usersBankAccount.setId(key);
//        usersBankAccount.setUser(user);
//        usersBankAccount.setBankAccount(bankAccount);
//
//        // Linking both sides
//        bankAccount.getUsersBankAccount().add(usersBankAccount);
//        user.getUsersBankAccounts().add(usersBankAccount);
//
//        Optional<BankAccount> optionalBankAccount = Optional.of(bankAccount);
//
//        boolean userExists = optionalBankAccount.get().getUsersBankAccount()
//                .stream()
//                .map(UsersBankAccount::getUser)
//                .anyMatch(u -> u.getUsername().equals(username));
//
//        assertTrue(userExists);
//
//        UserBankAccountResponseDto expectedResponse = new UserBankAccountResponseDto(
//                firstName, lastName, username, email, currentBal, accountType, List.of(bankAccountNo));
//        when(mapperRepository.toUserBankAccountResponseDto(Mockito.any(Users.class), Mockito.any(BankAccount.class)))
//                .thenReturn(expectedResponse);
//
//        UserBankAccountResponseDto response = bankAccountService.getAccountDetails(bankAccountNo);
//
//        //Verify and assert
//        Mockito.verify(bankAccountRepository, times(1)).findById(bankAccountNo);
//        Mockito.verify(mapperRepository, times(1)).toUserBankAccountResponseDto
//                (Mockito.any(Users.class), Mockito.any(BankAccount.class));
//
//        assertNotNull(response);
//        assertEquals(expectedResponse.getFirstName(), response.getFirstName());
//        assertEquals(expectedResponse.getLastName(), response.getLastName());
//        assertEquals(expectedResponse.getUsername(), response.getUsername());
//        assertEquals(expectedResponse.getEmail(), response.getEmail());
//        assertEquals(expectedResponse.getCurrentBal(), response.getCurrentBal());
//        assertEquals(expectedResponse.getAccountType(), response.getAccountType());
//        assertEquals(expectedResponse.getBankAccountNumbers(), response.getBankAccountNumbers());
//
//    }
//
//    @Test
//    public void getAccountDetailsResourceNotFoundException(){
//        Long bankAccountNo = ThreadLocalRandom.current().nextLong(min, max);
//        Mockito.when(bankAccountRepository.findById(bankAccountNo)).thenReturn(Optional.ofNullable(null));
//
//        //Verify
//        assertThrows(ResourceNotFoundException.class, ()->bankAccountService.getAccountDetails(bankAccountNo));
//    }
//    @Test
//    public void getAccountDetailsUserNotAuthorizedException(){
//
//        Long bankAccountNo = ThreadLocalRandom.current().nextLong(min, max);
//        String firstName = UUID.randomUUID().toString().substring(0, 7);
//        String lastName = UUID.randomUUID().toString().substring(0, 7);
//        String email = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6)
//                + "@gmail.com";
//        String username = UUID.randomUUID().toString().substring(0, 7);
//        String password = UUID.randomUUID().toString().substring(0, 14);
//        double currentBal = ThreadLocalRandom.current().nextDouble(minDouble, maxDouble);
//        String accountType = UUID.randomUUID().toString().substring(0, 7);
//
//        //Creating Bank Account
//        BankAccount bankAccount = createBankAccount(bankAccountNo, currentBal, accountType);
//        Mockito.when(bankAccountRepository.findById(bankAccountNo)).thenReturn(Optional.of(bankAccount));
//
//        // Mock Security Context
//        String invalidUsername = UUID.randomUUID().toString().substring(0, 7);
//        SecurityContextHolder.setContext(securityContext);
//        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
//        Mockito.when(authentication.getName()).thenReturn(invalidUsername);
//
//        assertThrows(UserNotAuthorizedException.class, ()-> bankAccountService.getAccountDetails(bankAccountNo));
//    }
//
//    @Test
//    public void depositAmountSuccessTest(){
//
//    String firstName = UUID.randomUUID().toString().substring(0, 7);
//    String lastName = UUID.randomUUID().toString().substring(0, 7);
//    String email = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6)
//            + "@gmail.com";
//    String password = UUID.randomUUID().toString().substring(0, 14);
//    String username = UUID.randomUUID().toString().substring(0, 7);
//    Long bankAccountNo = ThreadLocalRandom.current().nextLong(min, max);
//    double depositAmount = ThreadLocalRandom.current().nextDouble(minDouble, maxDouble);
//    double currentBal = ThreadLocalRandom.current().nextDouble(minDouble, maxDouble);
//    String accountType = UUID.randomUUID().toString().substring(0, 7);
//
//    BankAccount bankAccount = createBankAccount(bankAccountNo, currentBal, accountType);
//    Mockito.when(bankAccountRepository.findById(bankAccountNo)).thenReturn(Optional.of(bankAccount));
//    when(bankAccountRepository.save(Mockito.any(BankAccount.class))).thenAnswer(invocation-> invocation.getArgument(0));
//
//    //Creating User
//    Long userId = ThreadLocalRandom.current().nextLong(200);
//    Users user = new Users(userId, firstName, lastName, email, username, password, new ArrayList<>());
//
//    //Creating UsersBankAccount
//    UsersBankAccountKey key = new UsersBankAccountKey(userId, bankAccountNo);
//    UsersBankAccount usersBankAccount = new UsersBankAccount();
//    usersBankAccount.setId(key);
//    usersBankAccount.setUser(user);
//    usersBankAccount.setBankAccount(bankAccount);
//
//    // Linking both sides
//    bankAccount.getUsersBankAccount().add(usersBankAccount);
//    user.getUsersBankAccounts().add(usersBankAccount);
//
//    UserBankAccountResponseDto expectedResponse = new UserBankAccountResponseDto(
//            firstName, lastName, username, email, currentBal, accountType, List.of(bankAccountNo));
//    when(mapperRepository.toUserBankAccountResponseDto(Mockito.any(Users.class), Mockito.any(BankAccount.class)))
//        .thenReturn(expectedResponse);
//
//        UserBankAccountResponseDto response = bankAccountService.depositAmount(bankAccountNo, depositAmount);
//
//
//    //Verify and Assert
//        assertEquals(currentBal+depositAmount, response.getCurrentBal());
//        Mockito.verify(bankAccountRepository, times(1)).save(bankAccount);
//    }

    @Test
    public void registerUserSuccessTest(){

        Users user = createDummyUser();
        UserRequestDto userRequestDto = new UserRequestDto(
                user.getFirstName(), user.getLastName(), user.getEmail(), user.getUsername(), user.getPassword());
        UserResponseDto userResponseDto = new UserResponseDto(
                user.getFirstName(), user.getLastName(), user.getEmail());

        Mockito.when(mapperRepository.toUser(userRequestDto)).thenReturn(user);
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(null);
        Mockito.when(encoder.encode(user.getPassword())).thenReturn("encoded password");
        Mockito.when(userRepository.save(Mockito.any(Users.class))).thenReturn(user);
        Mockito.when(mapperRepository.toUserResponseDto(Mockito.any(Users.class))).thenReturn(userResponseDto);

        UserResponseDto result = bankAccountService.registerUser(userRequestDto);

        //Verify and assert
        assertNotNull(result);
        assertEquals(result.getEmail(), user.getEmail());

        Mockito.verify(mapperRepository, times(1)).toUser(userRequestDto);
        Mockito.verify(userRepository, times(1)).findByUsername(user.getUsername());
        Mockito.verify(mapperRepository, times(1)).toUserResponseDto(user);
        Mockito.verify(userRepository, times(1)).save(user);

    }

@Test
public void createAccountSuccessTest(){

    // Mock Security Context
    SecurityContextHolder.setContext(securityContext);

    //Creating user
    String firstName = UUID.randomUUID().toString().substring(0, 7);
    String lastName = UUID.randomUUID().toString().substring(0, 7);
    String email = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6)
            + "@gmail.com";
    String username = UUID.randomUUID().toString().substring(0, 7);
    String password = UUID.randomUUID().toString().substring(0, 14);
    Long userId = new Random().nextLong(900);
    Users user = new Users(userId, firstName, lastName, email, username, password, new ArrayList<>());

    //Creating bank account
    double currentBal = ThreadLocalRandom.current().nextDouble(min, max);
    String accountType = UUID.randomUUID().toString().substring(0, 7);
    AccountRequestDto accountRequestDto = new AccountRequestDto(currentBal, accountType);
    Long bankAccountNo = new Random().nextLong(900);
    BankAccount bankAccount = createBankAccount(bankAccountNo, currentBal, accountType);

    Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
    Mockito.when(authentication.getName()).thenReturn(username);
    Mockito.when(mapperRepository.toBankAccount(accountRequestDto)).thenReturn(bankAccount);
    Mockito.when(bankAccountRepository.save(Mockito.any(BankAccount.class))).thenReturn(bankAccount);
    Mockito.when(usersBankAccountRepository.save(any(UsersBankAccount.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    // Expected response
    UserBankAccountResponseDto expectedResponse = new UserBankAccountResponseDto(
            firstName, lastName, username, email, currentBal, accountType, List.of(bankAccountNo));
    Mockito.when(mapperRepository.toUserBankAccountResponseDto(Mockito.any(Users.class), Mockito.any(BankAccount.class)))
            .thenReturn(expectedResponse);

    UserBankAccountResponseDto response = bankAccountService.createAccount(userId, accountRequestDto);

    //Verify and assert
    Mockito.verify(userRepository, times(1)).findById(userId);
    Mockito.verify(mapperRepository).toBankAccount(accountRequestDto);
    Mockito.verify(bankAccountRepository, times(1)).save(Mockito.any(BankAccount.class));
    Mockito.verify(usersBankAccountRepository, times(1)).save(Mockito.any(UsersBankAccount.class));
    Mockito.verify(mapperRepository, times(1)).toUserBankAccountResponseDto
            (Mockito.any(Users.class), Mockito.any(BankAccount.class));

    assertNotNull(response);
    assertEquals(expectedResponse.getUsername(), response.getUsername());
    assertEquals(expectedResponse.getEmail(), response.getEmail());
    assertEquals(expectedResponse.getCurrentBal(), response.getCurrentBal());
    assertEquals(expectedResponse.getAccountType(), response.getAccountType());
}

    @Test
    public void linkUserAndAccountSuccessTest(){

        //Creating user
        String firstName = UUID.randomUUID().toString().substring(0, 7);
        String lastName = UUID.randomUUID().toString().substring(0, 7);
        String email = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6)
                + "@gmail.com";
        String username = UUID.randomUUID().toString().substring(0, 7);
        String password = UUID.randomUUID().toString().substring(0, 14);
        Long userId = new Random().nextLong(900);
        Users user = new Users(userId, firstName, lastName, email, username, password, new ArrayList<>());

        //Creating bank account
        double currentBal = ThreadLocalRandom.current().nextDouble(min, max);
        String accountType = UUID.randomUUID().toString().substring(0, 7);
        Long bankAccountNo = new Random().nextLong(900);
        BankAccount bankAccount = createBankAccount(bankAccountNo, currentBal, accountType);

        //Creating UsersBankAccount
        UsersBankAccountKey key = new UsersBankAccountKey(userId, bankAccountNo);
        UsersBankAccount usersBankAccount = new UsersBankAccount();
        usersBankAccount.setId(key);
        usersBankAccount.setUser(user);
        usersBankAccount.setBankAccount(bankAccount);

        UserBankAccountResponseDto expectedResponse = new UserBankAccountResponseDto(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getUsername(),
                bankAccount.getCurrentBal(),
                bankAccount.getAccountType(),
                List.of(bankAccountNo));


        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(bankAccountRepository.findById(bankAccountNo)).thenReturn(Optional.of(bankAccount));
        Mockito.when(usersBankAccountRepository.existsById(key)).thenReturn(false);
        Mockito.when(usersBankAccountRepository.save(Mockito.any(UsersBankAccount.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(mapperRepository.toUserBankAccountResponseDto(user, bankAccount)).thenReturn(expectedResponse);

        UserBankAccountResponseDto result =
                bankAccountService.linkUserAndAccount(userId, bankAccountNo);

        //Verify and assert
        assertNotNull(result);
        assertEquals(result.getBankAccountNumbers(), List.of(bankAccountNo));
        Mockito.verify(userRepository, times(1)).findById(userId);
        Mockito.verify(bankAccountRepository, times(1)).findById(bankAccountNo);
        Mockito.verify(usersBankAccountRepository, times(1)).existsById(key);
        Mockito.verify(usersBankAccountRepository, times(1)).save(Mockito.any(UsersBankAccount.class));
        Mockito.verify(mapperRepository, times(1)).toUserBankAccountResponseDto(user, bankAccount);

    }

    @Test
    public void getAccountDetailsSuccessTest(){

        //Creating user
        String firstName = UUID.randomUUID().toString().substring(0, 7);
        String lastName = UUID.randomUUID().toString().substring(0, 7);
        String email = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6)
                + "@gmail.com";
        String username = UUID.randomUUID().toString().substring(0, 7);
        String password = UUID.randomUUID().toString().substring(0, 14);
        Long userId = new Random().nextLong(900);
        Users user = new Users(userId, firstName, lastName, email, username, password, new ArrayList<>());

        //Creating bank account
        double currentBal = ThreadLocalRandom.current().nextDouble(min, max);
        String accountType = UUID.randomUUID().toString().substring(0, 7);
        Long bankAccountNo = new Random().nextLong(900);
        BankAccount bankAccount = createBankAccount(bankAccountNo, currentBal, accountType);

        //Creating UsersBankAccount
        UsersBankAccountKey key = new UsersBankAccountKey(userId, bankAccountNo);
        UsersBankAccount usersBankAccount = new UsersBankAccount();
        usersBankAccount.setId(key);
        usersBankAccount.setUser(user);
        usersBankAccount.setBankAccount(bankAccount);

        // Linking both sides
        bankAccount.getUsersBankAccount().add(usersBankAccount);
        user.getUsersBankAccounts().add(usersBankAccount);

        UserBankAccountResponseDto expectedResponse = new UserBankAccountResponseDto(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getUsername(),
                bankAccount.getCurrentBal(),
                bankAccount.getAccountType(),
                List.of(bankAccountNo));

        Mockito.when(bankAccountRepository.findById(bankAccountNo)).thenReturn(Optional.of(bankAccount));

        //Mock Security Context
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getName()).thenReturn(username);

        Mockito.when(mapperRepository.toUserBankAccountResponseDto(Mockito.any(Users.class),Mockito.any(BankAccount.class)))
                .thenReturn(expectedResponse);

        UserBankAccountResponseDto result = bankAccountService.getAccountDetails(bankAccountNo);

        //Verify and assert
        assertNotNull(result);
        assertEquals(result.getEmail(), email);
        assertEquals(username, result.getUsername());
        assertEquals(currentBal, result.getCurrentBal());
        Mockito.verify(bankAccountRepository, times(1)).findById(bankAccountNo);
        Mockito.verify(mapperRepository, times(1))
                .toUserBankAccountResponseDto(Mockito.any(Users.class), Mockito.any(BankAccount.class));

        SecurityContextHolder.clearContext();
    }

    public void fundTransferSuccessTest(){
        //Creating sender's bank account
        double senderCurrentBal = ThreadLocalRandom.current().nextDouble(min, max);
        String senderAccountType = UUID.randomUUID().toString().substring(0, 7);
        Long senderBankAccountNo = new Random().nextLong(900);
        BankAccount senderBankAccount = createBankAccount(senderBankAccountNo, senderCurrentBal, senderAccountType);

        //Creating receiver's bank account
        double receiverCurrentBal = ThreadLocalRandom.current().nextDouble(min, max);
        String receiverAccountType = UUID.randomUUID().toString().substring(0, 7);
        Long receiverBankAccountNo = new Random().nextLong(900);
        BankAccount receiverBankAccount = createBankAccount(receiverBankAccountNo, receiverCurrentBal, receiverAccountType);

    }

    @Test
    public void depositAccountSuccessTest(){

        Users user = createDummyUser();

        //Creating bank account
        double currentBal = ThreadLocalRandom.current().nextDouble(min, max);
        String accountType = UUID.randomUUID().toString().substring(0, 7);
        Long bankAccountNo = new Random().nextLong(900);
        BankAccount bankAccount = createBankAccount(bankAccountNo, currentBal, accountType);

        //Creating UsersBankAccount
        UsersBankAccountKey key = new UsersBankAccountKey(user.getUserId(), bankAccountNo);
        UsersBankAccount usersBankAccount = new UsersBankAccount();
        usersBankAccount.setId(key);
        usersBankAccount.setUser(user);
        usersBankAccount.setBankAccount(bankAccount);

        // Linking both sides
        bankAccount.getUsersBankAccount().add(usersBankAccount);
        user.getUsersBankAccounts().add(usersBankAccount);

        //Creating transaction
        Long transactionId = new Random().nextLong(900);
        Transaction transaction = new Transaction(
                transactionId,
                bankAccountNo,
                "DEPOSIT",
                800.0,
                LocalDateTime.now()
        );

        Double depositAmount = new Random().nextDouble(100000);

        UserBankAccountResponseDto userBankAccountResponseDto = new UserBankAccountResponseDto(
            user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getUsername(),
                bankAccount.getCurrentBal(),
                bankAccount.getAccountType(),
                List.of(bankAccountNo)
        );

        Mockito.when(bankAccountRepository.findById(bankAccountNo)).thenReturn(Optional.of(bankAccount));
        Mockito.when(bankAccountRepository.save(bankAccount)).thenReturn(bankAccount);
        Mockito.when(transactionHistoryRepository.save(Mockito.any(Transaction.class))).thenReturn(transaction);
        Mockito.when(mapperRepository.toUserBankAccountResponseDto(user, bankAccount)).thenReturn(userBankAccountResponseDto);

        UserBankAccountResponseDto result = bankAccountService.depositAmount(bankAccountNo, depositAmount);

        //Verify and assert
        assertNotNull(result);
        assertEquals(userBankAccountResponseDto.getCurrentBal(), result.getCurrentBal());
        assertEquals(userBankAccountResponseDto.getEmail(), result.getEmail());
        Mockito.verify(bankAccountRepository, times(1)).findById(bankAccountNo);
        Mockito.verify(bankAccountRepository,times(1)).save(bankAccount);
        Mockito.verify(transactionHistoryRepository, times(1)).save(Mockito.any(Transaction.class));
        Mockito.verify(mapperRepository, times(1))
                .toUserBankAccountResponseDto(user, bankAccount);

    }

    @Test
    public void depositAmountAccountFailTest() {

        //Creating bank account
        double currentBal = ThreadLocalRandom.current().nextDouble(min, max);
        String accountType = UUID.randomUUID().toString().substring(0, 7);
        Long bankAccountNo = new Random().nextLong(1, 900);
        BankAccount bankAccount = createBankAccount(bankAccountNo, currentBal, accountType);

        Double depositAmount = new Random().nextDouble(1, bankAccount.getCurrentBal());

        Mockito.when(bankAccountRepository.findById(bankAccountNo)).thenReturn(Optional.ofNullable(null));

        assertThrows(BankAccountDoesNotExistException.class,
                () -> bankAccountService.depositAmount(bankAccountNo, depositAmount));

        //Verify and assert
        Mockito.verify(bankAccountRepository, times(1)).findById(bankAccountNo);

    }


    @Test
    public void withdrawAmountSuccessTest(){
        //Creating user
        Users user = createDummyUser();

        //Creating bank account
        double currentBal = ThreadLocalRandom.current().nextDouble(min, max);
        String accountType = UUID.randomUUID().toString().substring(0, 7);
        Long bankAccountNo = new Random().nextLong(900);
        BankAccount bankAccount = createBankAccount(bankAccountNo, currentBal, accountType);

        //Creating UsersBankAccount
        UsersBankAccountKey key = new UsersBankAccountKey(user.getUserId(), bankAccountNo);
        UsersBankAccount usersBankAccount = new UsersBankAccount();
        usersBankAccount.setId(key);
        usersBankAccount.setUser(user);
        usersBankAccount.setBankAccount(bankAccount);

        // Linking both sides
        bankAccount.getUsersBankAccount().add(usersBankAccount);
        user.getUsersBankAccounts().add(usersBankAccount);

        //Creating transaction
        Long transactionId = new Random().nextLong(900);
        Transaction transaction = new Transaction(
                transactionId,
                bankAccountNo,
                "WITHDRAW",
                800.0,
                LocalDateTime.now()
        );

        Double withdrawAmount = new Random().nextDouble(1,bankAccount.getCurrentBal());

        UserBankAccountResponseDto userBankAccountResponseDto = new UserBankAccountResponseDto(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getUsername(),
                bankAccount.getCurrentBal(),
                bankAccount.getAccountType(),
                List.of(bankAccountNo)
        );

        Mockito.when(bankAccountRepository.findById(bankAccountNo)).thenReturn(Optional.of(bankAccount));
        Mockito.when(bankAccountRepository.save(bankAccount)).thenReturn(bankAccount);
        Mockito.when(transactionHistoryRepository.save(Mockito.any(Transaction.class))).thenReturn(transaction);
        Mockito.when(mapperRepository.toUserBankAccountResponseDto(user, bankAccount))
                .thenReturn(userBankAccountResponseDto);

        UserBankAccountResponseDto result = bankAccountService.withdrawAmount(bankAccountNo, withdrawAmount);

        //Verify and assert
        assertNotNull(result);
        assertEquals(userBankAccountResponseDto.getCurrentBal(), result.getCurrentBal());
        assertEquals(userBankAccountResponseDto.getEmail(), result.getEmail());
        Mockito.verify(bankAccountRepository, times(1)).findById(bankAccountNo);
        Mockito.verify(bankAccountRepository,times(1)).save(bankAccount);
        Mockito.verify(transactionHistoryRepository, times(1)).save(Mockito.any(Transaction.class));
        Mockito.verify(mapperRepository, times(1))
                .toUserBankAccountResponseDto(user, bankAccount);
    }

    @Test
    public void withdrawAmountLowBalanceFailTest() {
        //Creating user
        Users user = createDummyUser();

        //Creating bank account
        double currentBal = ThreadLocalRandom.current().nextDouble(min, max);
        String accountType = UUID.randomUUID().toString().substring(0, 7);
        Long bankAccountNo = new Random().nextLong(1,900);
        BankAccount bankAccount = createBankAccount(bankAccountNo, currentBal, accountType);

        //Creating UsersBankAccount
        UsersBankAccountKey key = new UsersBankAccountKey(user.getUserId(), bankAccountNo);
        UsersBankAccount usersBankAccount = new UsersBankAccount();
        usersBankAccount.setId(key);
        usersBankAccount.setUser(user);
        usersBankAccount.setBankAccount(bankAccount);

        // Linking both sides
        bankAccount.getUsersBankAccount().add(usersBankAccount);
        user.getUsersBankAccounts().add(usersBankAccount);

        //Creating transaction
        Long transactionId = new Random().nextLong(900);
        Transaction transaction = new Transaction(
                transactionId,
                bankAccountNo,
                "WITHDRAW",
                800.0,
                LocalDateTime.now()
        );

        Double withdrawAmount = new Random().nextDouble( bankAccount.getCurrentBal(), Double.MAX_VALUE);

        UserBankAccountResponseDto userBankAccountResponseDto = new UserBankAccountResponseDto(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getUsername(),
                bankAccount.getCurrentBal(),
                bankAccount.getAccountType(),
                List.of(bankAccountNo)
        );

        Mockito.when(bankAccountRepository.findById(bankAccountNo)).thenReturn(Optional.of(bankAccount));

        assertThrows(LowBalanceException.class, ()->bankAccountService.withdrawAmount(bankAccountNo, withdrawAmount));

        //Verify and assert
        Mockito.verify(bankAccountRepository, times(1)).findById(bankAccountNo);

    }

    @Test
    public void withdrawAmountAccountInvalidFailTest() {

        //Creating bank account
        double currentBal = ThreadLocalRandom.current().nextDouble(min, max);
        String accountType = UUID.randomUUID().toString().substring(0, 7);
        Long bankAccountNo = new Random().nextLong(1, 900);
        BankAccount bankAccount = createBankAccount(bankAccountNo, currentBal, accountType);

        Double withdrawAmount = new Random().nextDouble(1, bankAccount.getCurrentBal());

        Mockito.when(bankAccountRepository.findById(bankAccountNo)).thenReturn(Optional.ofNullable(null));

        assertThrows(BankAccountDoesNotExistException.class,
                () -> bankAccountService.withdrawAmount(bankAccountNo, withdrawAmount));

        //Verify and assert
        Mockito.verify(bankAccountRepository, times(1)).findById(bankAccountNo);

    }




    private Users createDummyUser() {
        //Creating user
        String firstName = UUID.randomUUID().toString().substring(0, 7);
        String lastName = UUID.randomUUID().toString().substring(0, 7);
        String email = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6)
                + "@gmail.com";
        String username = UUID.randomUUID().toString().substring(0, 7);
        String password = UUID.randomUUID().toString().substring(0, 14);
        Long userId = new Random().nextLong(900);
       return new Users(userId, firstName, lastName, email, username, password, new ArrayList<>());
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

}
