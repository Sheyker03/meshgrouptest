package t.meshgroup.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import t.meshgroup.database.repo.AccountRepository;
import t.meshgroup.database.repo.BalanceStatRepository;
import t.meshgroup.models.Account;
import t.meshgroup.models.BalanceStats;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Profile("!test")
public class BalanceStatService {
    private final BalanceStatRepository balanceStatRepository;
    private final AccountRepository accountRepository;

    private List<BalanceStats> balances = new ArrayList<>();
    private List<Account> accounts = new ArrayList<>();
    private boolean accountAndBalancesProcessed = false;


    public void saveBalancesByAccounts(List<Account> accounts) {
        List<BalanceStats> balances = accounts.stream()
                .map(it -> new BalanceStats(
                        it.getId(),
                        BigDecimal.valueOf(it.getBalance().doubleValue() * 2.07),
                        System.currentTimeMillis()
                ))
                .collect(Collectors.toList());
        balanceStatRepository.saveAll(balances);
    }

    /**
     * In future - add indexes in Redis + add find by list id for Postgresql + optimize save to postgresql.
     * + add transaction layer for two db;
     */
    @Scheduled(fixedDelay = 30000)
    private void handleBalance() {
        if (accountAndBalancesProcessed)
            return;

        if (accounts.isEmpty() || balances.isEmpty()) {
            loadBalancesAndAccounts();
            return;
        }

        /*
         * It is advisable to obtain id without access to user
         * */
        Map<BalanceStats, Account> resultMap = accounts.stream()
                .filter(acc -> {
                    double actualBalance = acc.getBalance().doubleValue();
                    return balances.stream().filter(balance -> Objects.equals(balance.getUserId(), acc.getUser().getId()))
                            .findFirst().get().getMaxBalance().doubleValue() >= actualBalance * 1.1;
                }).collect(
                        Collectors.toMap(e ->
                                        balances
                                                .stream()
                                                .filter(balance -> e.getUser().getId() == balance.getUserId())
                                                .findFirst().get(),
                                e -> e)
                );

        if (resultMap.isEmpty()) {
            accountAndBalancesProcessed = true;
            return;
        }

        Map<Long, Double> resultMoneyForUpdate = new HashMap<>();

        resultMap.forEach((key, value) -> {
            long lastUpdate = key.getLastUpdate();
            int countIterator = (int) ((System.currentTimeMillis() - lastUpdate) / 30000);

            var maxBalance = key.getMaxBalance().doubleValue();

            double oldBalance = value.getBalance().doubleValue();
            double addBalance = 0;
            for (int i = 0; i < countIterator; i++) {
                if ((oldBalance + addBalance) * 1.1 <= maxBalance)
                    addBalance = oldBalance * 0.1;
            }

            value.setBalance(new BigDecimal(oldBalance + addBalance));
            resultMoneyForUpdate.put(value.getId(), addBalance);
        });

        resultMap.keySet().forEach(it -> it.setLastUpdate(System.currentTimeMillis()));

        addMonetToAccount(resultMoneyForUpdate);
        balanceStatRepository.saveAll(resultMap.keySet());

        System.out.println("balances");
    }

    private void addMonetToAccount(Map<Long, Double> accounts) {
        accounts.forEach((key, value) -> accountRepository.addMoney(key, BigDecimal.valueOf(value)));
    }

    private void loadBalancesAndAccounts() {
        accounts.clear();
        balances.clear();

        accounts.addAll(accountRepository.findAll());

        balanceStatRepository.findAll().forEach(balances::add);
    }
}
