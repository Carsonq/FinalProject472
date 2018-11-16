package csc472.depaul.edu.finalproject.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(indices = {@Index(value = {"account_type", "account_subtype", "institution_id", "institution_name"},
        unique = true)})
public class Account {
    @PrimaryKey(autoGenerate = true)
    private int accountId;
    @ColumnInfo(name = "account_type")
    private String accountType;
    @ColumnInfo(name = "account_subtype")
    private String accountSubtype;
    @ColumnInfo(name = "institution_id")
    private String institutionID;
    @ColumnInfo(name = "institution_name")
    private String institutionName;
    @ColumnInfo(name = "access_token")
    private String accessToken;
    @ColumnInfo(name = "item_id")
    private String itemId;

    public Account() {
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountSubtype() {
        return accountSubtype;
    }

    public void setAccountSubtype(String accountSubtype) {
        this.accountSubtype = accountSubtype;
    }

    public String getInstitutionID() {
        return institutionID;
    }

    public void setInstitutionID(String institutionID) {
        this.institutionID = institutionID;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}