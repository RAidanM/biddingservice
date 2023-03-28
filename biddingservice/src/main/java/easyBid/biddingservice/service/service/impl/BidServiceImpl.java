package easyBid.biddingservice.service.service.impl;

import easyBid.biddingservice.entity.Bid;
import easyBid.biddingservice.repository.BidRepository;
import easyBid.biddingservice.service.BidService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class BidServiceImpl implements BidService {

    private BidRepository bidRepository;

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
        existingBid.setItemId(bid.getItemId());
        existingBid.setStartDate(bid.getStartDate());
        existingBid.setCurrentBid(bid.getCurrentBid());
        Bid updatedBid = bidRepository.save(existingBid);
        return updatedBid;
    }

    @Override
    public void deleteBid(Long bidId) {
        bidRepository.deleteById(bidId);
    }
}