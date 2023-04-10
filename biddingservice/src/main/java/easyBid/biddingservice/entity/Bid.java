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
@Table(name = "bids")
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private long auctionId;

    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    private double bidPrice;

    @Column
    //@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @CreationTimestamp
    private Date bidDate;

    @Column
    //@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @CreationTimestamp
    private Time bidTime;

}