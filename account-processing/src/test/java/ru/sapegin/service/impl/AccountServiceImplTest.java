package ru.sapegin.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sapegin.dto.AccountDTO;
import ru.sapegin.dto.ClientProductDTO;
import ru.sapegin.dto.TransactionDTO;
import ru.sapegin.enums.AccountStatusEnum;
import ru.sapegin.enums.TransactionTypeEnum;
import ru.sapegin.model.Account;
import ru.sapegin.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.Optional;

import static junit.framework.Assert.assertEquals;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    @DisplayName("Корректно ли создаётся новый аккаунт")
    void createTest() {
        var dto = new ClientProductDTO(1L,2L,5,"CC");
        var expectedAccount = new Account(
                dto.clientId(),
                dto.productId(),
                BigDecimal.ZERO,
                BigDecimal.valueOf(dto.interestRate()),
                true,
                false,
                AccountStatusEnum.ACTIVE
        );
        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        var actualAccount = accountService.create(dto);
        assertEquals(expectedAccount, actualAccount);
        verify(accountRepository).save(actualAccount);
    }

    @Test
    @DisplayName("Маппинг Account в AccountDTO")
    void mapToDTOTest() {
        Account account = new Account(1L, 1L, 2L, BigDecimal.TEN, BigDecimal.ONE, true, true, AccountStatusEnum.ACTIVE);
        AccountDTO expectedResult = new AccountDTO(1L, 2L, BigDecimal.TEN, BigDecimal.ONE, true, true, AccountStatusEnum.ACTIVE);
        assertThat(accountService.mapToDTO(account)).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("Обновление поля exist у сущности Account")
    void updateCardExistTest() {
        var account = new Account();
        Long accountId = 1L;
        account.setId(accountId);
        account.setCardExist(false);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account resultAccount = accountService.updateCardExist(accountId);
        assertThat(resultAccount.isCardExist()).isTrue();
    }

    @Test
    @DisplayName("Перевод денег на заблокированный аккаунт")
    void updateBlockedAccountTest() {
        var account = new Account();
        account.setId(1L);
        account.setStatus(AccountStatusEnum.BLOCKED);

        var transactionDTO = new TransactionDTO();
        transactionDTO.setAccountId(1L);
        transactionDTO.setAmount(new BigDecimal("100.00"));
        transactionDTO.setType(TransactionTypeEnum.ACCRUAL);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        assertThatThrownBy(() -> accountService.updateAccountByTransaction(transactionDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Аккаунт заблокирован");

        verify(accountRepository, never()).save(any());
    }


    @Test
    @DisplayName("Блокировка аккаунта")
    void blockAccountTest(){
        var account = new Account();
        account.setId(1L);
        account.setStatus(AccountStatusEnum.ACTIVE);
        accountService.blockAccount(account);
        assertThat(account.getStatus()).isEqualTo(AccountStatusEnum.BLOCKED);
        verify(accountRepository).save(account);
    }

    @Test
    @DisplayName("Списание денег")
    void debitMoneyTest() {
        var account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal("100.00"));
        BigDecimal amount = new BigDecimal("30.00");
        accountService.debitMoney(account, amount);
        assertThat(account.getBalance()).isEqualByComparingTo("70.00");
        verify(accountRepository).save(account);
    }


    @Test
    @DisplayName("Начисление денег")
    void accrualMoneyTest(){
        var account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal("100.00"));
        BigDecimal amount = new BigDecimal("30.00");
        accountService.accrualMoney(account, amount);
        assertThat(account.getBalance()).isEqualByComparingTo("130.00");
        verify(accountRepository).save(account);
    }
}
