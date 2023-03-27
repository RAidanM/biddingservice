package easyBid.biddingservice.service.service.impl;

import easyBid.biddingservice.entity.Bid;
import easyBid.biddingservice.repository.BidRepository;
import easyBid.biddingservice.service.BidService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BidServiceImpl implements BidService {

    private BidRepository bidRepository;

    public BidServiceImpl(BidRepository bidRepository) {
        this.bidRepository = bidRepository;
    }

    @Override
    public Bid createBid(Bid bid) {
        return bidRepository.save(bid);
    }

    @Override
    public Bid getBidById(Long bidId) {
        Optional<Bid> optionalBid = bidRepository.findById(bidId);
        return optionalBid.get();
    }

    @Override
    public List<Bid> getAllBids() {
        return bidRepository.findAll();
    }

    @Override
    public Bid updateBid(Bid bid) {
        Bid existingBid = bidRepository.findById(bid.getId()).get();
        existingBid.setBiddingUser(bid.getBiddingUser());
        existingBid.setBidPrice(bid.getBidPrice());
        existingBid.setBidDate(bid.getBidDate());
        Bid updatedBid = bidRepository.save(existingBid);
        return updatedBid;
    }

    @Override
    public void deleteBid(Long bidId) {
        bidRepository.deleteById(bidId);
    }
}