package easyBid.biddingservice.service;

import easyBid.biddingservice.entity.Bid;

import java.util.List;

public interface BidService {
    Bid createBid(Bid bid);

    Bid getBidById(Long bidId);

    List<Bid> getAllBids();

    Bid updateBid(Bid bid);

    void deleteBid(Long bidId);
}
