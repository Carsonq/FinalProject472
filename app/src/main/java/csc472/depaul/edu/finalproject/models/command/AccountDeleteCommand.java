package csc472.depaul.edu.finalproject.models.command;

import csc472.depaul.edu.finalproject.db.Account;
import csc472.depaul.edu.finalproject.db.AccountViewModel;

public class AccountDeleteCommand extends UndoableCommand {
    private Account account;
    private Account previousAccount;
    private AccountViewModel accountViewModel;

    public AccountDeleteCommand()
    {
        this.account = null;
        this.previousAccount = null;
        this.accountViewModel = null;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setAccountViewModel(AccountViewModel accountViewModel) {
        this.accountViewModel = accountViewModel;
    }

    @Override
    public void execute() {
        if (account != null && accountViewModel != null) {
            accountViewModel.deleteAccount(account);
            this.previousAccount = account;
        }
    }

    @Override
    public void undo() {
        if (account != null && accountViewModel != null) {
            accountViewModel.saveAccount(this.previousAccount);
            this.previousAccount = null;
        }
    }
}
