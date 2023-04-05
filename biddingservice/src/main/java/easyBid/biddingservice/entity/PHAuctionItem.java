//package easyBid.biddingservice.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import org.hibernate.annotations.CreationTimestamp;
//
//import java.sql.Date;
//import java.sql.Time;
//
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//@Entity
//@Table(name = "items")
//public class PHAuctionItem {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long id;
//
//    //item parameters
//    @Column(name = "name")
//    private String name;
//
//    @Column(name = "description")
//    private String description;
//
//    @Column(name = "start_price")
//    private double startPrice;
//
//    @Column(name = "current_price")
//    private double currentPrice;
//
//    @Column(name = "auction_type")
//    private String auctionType;
//
//    @Column(name = "create_date")
//    //@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//    @CreationTimestamp
//    private Date createDate;
//
//    @Column(name = "create_time")
//    //@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//    @CreationTimestamp
//    private Time createTime;
//
//    @Column(name = "auction_length")
//    private int auctionLength;
//}