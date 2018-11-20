package csc472.depaul.edu.finalproject.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import csc472.depaul.edu.finalproject.R;
import csc472.depaul.edu.finalproject.db.Account;
import csc472.depaul.edu.finalproject.db.AccountViewModel;
import csc472.depaul.edu.finalproject.models.AccountsAdapter;
import csc472.depaul.edu.finalproject.models.RecyclerItemTouchHelper;
import csc472.depaul.edu.finalproject.models.RecyclerItemTouchHelperListener;

public class AccountManagementActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    private AccountViewModel accountViewModel;
    private RecyclerView recyclerView;
    private List<Account> accountsList;
    private AccountsAdapter accountsAdapter;
    private CoordinatorLayout accountRootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_management);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.title_activity_account_management);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_accounts);
        accountRootLayout = (CoordinatorLayout) findViewById(R.id.account_root_layout);
        accountsAdapter = new AccountsAdapter(this);

        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        accountViewModel.getAllAccounts().observe(this, accounts -> accountsAdapter.setData(accounts));
        accountsAdapter.setAccountDeleteCommand(accountViewModel);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(accountsAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallBack = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(recyclerView);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof AccountsAdapter.ViewHolder) {
            accountsList = accountsAdapter.getAccountList();
            String bankName = accountsList.get(viewHolder.getAdapterPosition()).getInstitutionName();

            final Account deletedItem = accountsList.get(viewHolder.getAdapterPosition());
            final int deleteIndex = viewHolder.getAdapterPosition();

            accountsAdapter.removeAccount(deleteIndex);

            Snackbar snackbar = Snackbar.make(accountRootLayout, bankName + " removed", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    accountsAdapter.restoreAccount(deletedItem, deleteIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem item = menu.getItem(0);
        if (item != null) {
            item.setOnMenuItemClickListener(onClickAddAccount);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getAccountManagementActivity(), MainActivity.class);
        getAccountManagementActivity().startActivity(intent);
    }

    private MenuItem.OnMenuItemClickListener onClickAddAccount = new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            Intent intent = new Intent(getAccountManagementActivity(), NewAuthActivity.class);
            getAccountManagementActivity().startActivity(intent);
            return true;
        }
    };

    private final AccountManagementActivity getAccountManagementActivity() {
        return this;
    }
}
