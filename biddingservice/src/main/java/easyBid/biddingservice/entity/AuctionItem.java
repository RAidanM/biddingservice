package easyBid.biddingservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Date;
import java.sql.Time;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

@Entity
//@Table(name = "items")
public class AuctionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //item parameters
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private double start_price;

    @Column(nullable = false)
    private double current_price;

    @Column(nullable = false)
    private String auction_type;

    @Column(nullable = false)
    //@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @CreationTimestamp
    private Date create_date;

    @Column(nullable = false)
    //@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @CreationTimestamp
    private Time create_time;

    @Column(nullable = false)
    private int auction_length;

    private int dutch_snap_down;

    private long current_bidder_id;

    @Column(nullable = false)
    private double ship_price_ex;

    @Column(nullable = false)
    private boolean item_sold;

    public void updateItem(AuctionItem update){
        setName(update.getName());
        setDescription(update.getDescription());
        setStart_price(update.getStart_price());
        setCurrent_price(update.getCurrent_price());
        setAuction_type(update.getAuction_type());
        setCreate_date(update.getCreate_date());
        setCreate_time(update.getCreate_time());
        setAuction_length(update.getAuction_length());
        setDutch_snap_down(update.getDutch_snap_down());
        setCurrent_bidder_id(update.getCurrent_bidder_id());
        setShip_price_ex(update.getShip_price_ex());
        setItem_sold(update.isItem_sold());
    }

}
