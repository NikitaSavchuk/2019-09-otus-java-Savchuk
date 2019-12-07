package ru.otus.department;

import ru.otus.cell.Cell;
import ru.otus.strategies.WithdrawStrategy;
import ru.otus.bankomat.ATM;
import ru.otus.bankomat.Bankomat;
import ru.otus.visitor.Visitor;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

public class Department implements AtmDepartment, Visitor<Integer> {

    private List<ATM> atmList = new ArrayList<>();
    private final int DEFAULT_NUMBER = 5;
    private WithdrawStrategy DEFAULT_STRATEGY = new WithdrawStrategy();

    public Department() {
        for (int i = 1; i < DEFAULT_NUMBER; i ++) {
            Bankomat bankomat = new Bankomat(("ATM №" + i), DEFAULT_STRATEGY);
            atmList.add(bankomat);
        }
    }

    public void addAtm(ATM atm) {
        atmList.add(atm);
    }

    public List<ATM> getAtmList() {
        return atmList;
    }

    public Optional<ATM> getAtm(String id) {
        return atmList.stream().filter(atm -> atm.getId().equalsIgnoreCase(id)).findFirst();
    }

    public int getTotalCashAmount() {
        return atmList.stream().mapToInt(this::visit).sum();
    }

    public void showTotalCashAmount() {
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(',');
        DecimalFormat format = new DecimalFormat("#,##0.##", symbols);

        System.out.println(format("Общий баланс в департаменте: %s%n", format.format(getTotalCashAmount())));
        atmList.forEach(ATM::showCurrentAtmCashBalance);
    }

    public void showAllAtms() {
        atmList.forEach(System.out::println);
    }

    public void restoreAllAtmsToStartState() {
        atmList.forEach(ATM::restoreStartState);
        System.err.println("Все банкоматы восстановлены.");
    }

    @Override
    public int visit(ATM atm) {
        return visit(atm.getCell());
    }

    @Override
    public Integer visit(Cell cell) {
        return cell.getCellBalance();
    }
}
