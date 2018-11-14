package csc472.depaul.edu.finalproject.models;

import java.util.List;

import csc472.depaul.edu.finalproject.db.Account;
import csc472.depaul.edu.finalproject.db.TransactionCategory;

public interface ITransactionObserver
{
	public void getTransactions(List<Account> acc);
	public void getData();
	public void saveQueryResult(List<TransactionCategory> tra);
}
      