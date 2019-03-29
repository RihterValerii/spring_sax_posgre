package jobs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    private int id;
    @Column(name = "type")
    private String type;
    @Column(name = "user_")
    private String user;
    @Column(name = "device")
    private String device;
    @Column(name = "amount")
    private int amount;
    @Column(name = "date")
    @JsonIgnore
    private long longDate;
    @Transient
    private String date;



    public Job() {
    }

    public Job(String type, String user, String device, int amount) {
        this.type = type;
        this.user = user;
        this.device = device;
        this.amount = amount;
    }

    public Job(String type, String user, String device, int amount, int id, long date) {
        this.type = type;
        this.user = user;
        this.device = device;
        this.amount = amount;
        this.id = id;
        longDate = date;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public long getLongDate() {
        return longDate;
    }

    public void setLongDate(long longDate) {
        this.longDate = longDate;
    }

    public String getType() {
        return type;
    }

    public String getUser() {
        return user;
    }

    public String getDevice() {
        return device;
    }

    public int getAmount() {
        return amount;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void convertDate() {
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        date = format.format(new Date(longDate));
    }

    @Override
    public String toString() {
        return "Job{" +
                "type='" + type + '\'' +
                ", user='" + user + '\'' +
                ", device='" + device + '\'' +
                ", amount=" + amount +
                ", id=" + id +
                ", data: " + date +
                '}';
    }
}
