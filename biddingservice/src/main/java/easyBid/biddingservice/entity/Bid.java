package easyBid.biddingservice.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "bids")
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String biddingUser;
    @Column(nullable = false)
    private double bidPrice;
    @Column(nullable = false, unique = true)
    private Date bidDate;

    public Bid (){
        super();
    }
    public Bid (String biddingUser, double bidPrice, Date bidDate){
        super();
        this.biddingUser = biddingUser;
        this.bidPrice = bidPrice;
        this.bidDate = bidDate;
    }

    public Bid (Bid bid){
        super();
        this.biddingUser =bid.getBiddingUser();
        this.bidPrice = bid.getBidPrice();
        this.bidDate = bid.getBidDate();
    }

    public Long getId() {
        return id;
    }

    public String getBiddingUser() {
        return biddingUser;
    }

    public double getBidPrice() {
        return bidPrice;
    }

    public Date getBidDate() {
        return bidDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBiddingUser(String firstName) {
        this.biddingUser = firstName;
    }

    public void setBidPrice(double lastName) {
        this.bidPrice = lastName;
    }

    public void setBidDate(Date bidDate) {
        this.bidDate = bidDate;
    }

}