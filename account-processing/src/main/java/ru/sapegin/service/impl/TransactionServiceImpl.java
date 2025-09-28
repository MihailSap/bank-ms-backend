package ru.sapegin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sapegin.dto.TransactionDTO;
import ru.sapegin.enums.TransactionStatusEnum;
import ru.sapegin.model.Account;
import ru.sapegin.model.Card;
import ru.sapegin.model.Transaction;
import ru.sapegin.repository.TransactionRepository;
import ru.sapegin.service.TransactionServiceI;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionServiceI {

    private final TransactionRepository transactionRepository;
    private final CardServiceImpl cardService;
    private final AccountServiceImpl accountService;

    @Value("${transaction.max-count}")
    private int N;

    @Value("${transaction.period.start-days-offset}")
    private int sT;

    @Value("${transaction.period.end-days-offset}")
    private int eT;

    @Transactional
    @Override
    public TransactionDTO proccessGetTransaction(TransactionDTO transactionDTO){
        var account = accountService.proccessAccount(transactionDTO);
        var card = cardService.getCardById(transactionDTO.getCardId());
        var transaction = create(transactionDTO, card, account);
        proccessCard(card, account, transaction);

        if(transactionDTO.getType().equals("ACCRUAL") && account.isRecalc()){
            var transactionDate = transaction.getTimestamp();
            var creditPaymentDate = LocalDateTime.now(); //TODO: Поменять заглушку
            if(transactionDate.isAfter(creditPaymentDate) || transactionDate.isEqual(creditPaymentDate)){
                if(account.getBalance().compareTo(transaction.getAmount()) < 0){
                    account.setBalance(account.getBalance().subtract(transaction.getAmount()));
                }
            } else {
                log.info("EXPIRED!!!");
            }
        }

        return mapToDTO(transaction);
    }

    // Card
    public void proccessCard(Card card, Account account, Transaction transaction){
        var transactionsByCardId = transactionRepository.findByCardId(card.getId());
        var cnt = 0;
        if(!transactionsByCardId.isEmpty()){
            for(var transactionAA : transactionsByCardId){
                var timestamp = transactionAA.getTimestamp();
                var startT = LocalDateTime.now().minusDays(sT);
                var endT = LocalDateTime.now().plusDays(eT);
                if(timestamp.isAfter(startT) && timestamp.isBefore(endT)){
                    cnt++;
                }
            }
        }
        if(cnt > N){
            account.setStatus("BLOCKED");
            transaction.setType("BLOCKED");
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
                "ALLOWED",
                transaction.getTimestamp()
        );
    }
}
