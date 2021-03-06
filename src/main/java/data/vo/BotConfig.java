package data.vo;

import javax.persistence.*;
import java.util.UUID;

/**
 * Created by asus-pc on 04.02.2017.
 */

@Entity
public class BotConfig {

    @Id
    private String id = UUID.randomUUID().toString();

    @Column
    private String accountId;

    @Column
    private String cron;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String emailserver;

    @Column
    private String emailpassword;

    @Column
    private String microsoft_id;

    @Column
    private String microsoft_secret;

    @Column
    private String skype_id;

    @Column
    private Boolean runned;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailpassword() {
        return emailpassword;
    }

    public void setEmailpassword(String emailpassword) {
        this.emailpassword = emailpassword;
    }

    public String getMicrosoft_id() {
        return microsoft_id;
    }

    public void setMicrosoft_id(String microsoft_id) {
        this.microsoft_id = microsoft_id;
    }

    public String getMicrosoft_secret() {
        return microsoft_secret;
    }

    public void setMicrosoft_secret(String microsoft_secret) {
        this.microsoft_secret = microsoft_secret;
    }

    public String getEmailserver() {
        return emailserver;
    }

    public void setEmailserver(String emailserver) {
        this.emailserver = emailserver;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getSkype_id() {
        return skype_id;
    }

    public void setSkype_id(String skype_id) {
        this.skype_id = skype_id;
    }

    public Boolean getRunned() {
        return runned;
    }

    public void setRunned(Boolean runned) {
        this.runned = runned;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}


