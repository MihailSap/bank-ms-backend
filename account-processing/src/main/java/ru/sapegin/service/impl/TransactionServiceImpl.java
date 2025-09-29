package ru.sapegin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sapegin.dto.TransactionDTO;
import ru.sapegin.enums.TransactionStatusEnum;
import ru.sapegin.enums.TransactionTypeEnum;
import ru.sapegin.model.Account;
import ru.sapegin.model.Card;
import ru.sapegin.model.Transaction;
import ru.sapegin.repository.PaymentRepository;
import ru.sapegin.repository.TransactionRepository;
import ru.sapegin.service.TransactionServiceI;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionServiceI {

    private final CardServiceImpl cardService;
    private final AccountServiceImpl accountService;
    private final PaymentServiceImpl paymentService;
    private final TransactionRepository transactionRepository;
    private final PaymentRepository paymentRepository;

    @Value("${transaction.max-count}")
    private int N;

    @Value("${transaction.period.start-days-offset}")
    private int sT;

    @Value("${transaction.period.end-days-offset}")
    private int eT;

    @Transactional
    @Override
    public TransactionDTO proccessGetTransaction(TransactionDTO transactionDTO){
        var account = accountService.updateAccountByTransaction(transactionDTO);
        var card = cardService.getCardById(transactionDTO.getCardId());
        var transaction = create(transactionDTO, card, account);
        checkTransactionsCount(card, account, transaction);
        if(transactionDTO.getType().equals(TransactionTypeEnum.ACCRUAL) && account.isRecalc()){
            if (paymentService.isPaymentExist(account)) {
                var creditMoths = 24;
                paymentService.createCreditPayments(account, creditMoths, transactionDTO.getAmount());
            }
            var paymentOpt = paymentService.getNextCreditPaymentIfExists(account);
            if(paymentOpt.isPresent()){
                var payment = paymentOpt.get();
                if(account.getBalance().compareTo(payment.getAmount()) >= 0){
                    accountService.debitMoney(account, payment.getAmount());
                    payment.setPayedAt(LocalDate.now());
                } else {
                    payment.setExpired(true);
                }
                paymentRepository.save(payment);
            }
        }
        return mapToDTO(transaction);
    }

    @Transactional
    @Override
    public void checkTransactionsCount(Card card, Account account, Transaction transaction){
        var transactionsByCardId = transactionRepository.findByCardId(card.getId());
        var cnt = cardService.getTransactionsCountByTime(transactionsByCardId, sT, eT);
        if(cnt > N){
            accountService.blockAccount(account);
            blockTransaction(transaction);
        }
    }

    @Transactional
    @Override
    public Transaction create(TransactionDTO transactionDTO, Card card, Account account) {
        var transaction = new Transaction();
        transaction.setCard(card);
        transaction.setAccount(account);
        transaction.setType(transactionDTO.getType());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setStatus(TransactionStatusEnum.ALLOWED);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
        log.info("СОЗДАНА Transaction: {}", transaction);
        return transaction;
    }

    @Override
    public TransactionDTO mapToDTO(Transaction transaction){
        return new TransactionDTO(
                transaction.getCard().getId(),
                transaction.getAccount().getId(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getStatus(),
                transaction.getTimestamp()
        );
    }

    @Transactional
    @Override
    public void blockTransaction(Transaction transaction){
        transaction.setStatus(TransactionStatusEnum.BLOCKED);
        transactionRepository.save(transaction);
    }
}
