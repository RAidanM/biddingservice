package easyBid.biddingservice.controller;

import easyBid.biddingservice.entity.Bid;
import easyBid.biddingservice.entity.BiddingRecord;
import easyBid.biddingservice.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("api/bids")
public class BidController {

    private BidService bidService;

    @Autowired
    public BidController(BidService bidService){
        this.bidService=bidService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Bid> getBidById(@PathVariable("id") Long bidId){
        Bid bid = bidService.getBidById(bidId);
        return new ResponseEntity<>(bid, HttpStatus.OK);
    }

    // build create Bid REST API
    @PostMapping
    public ResponseEntity<Bid> createBid(@RequestBody Bid bid){
        Bid savedBid = bidService.createBid(bid);
        return new ResponseEntity<>(savedBid, HttpStatus.CREATED);
    }

    // Build Get All Bids REST API
    // http://localhost:8080/api/Bids
    @GetMapping
    public ResponseEntity<List<Bid>> getAllBids(){
        List<Bid> bids = bidService.getAllBids();
        return new ResponseEntity<>(bids, HttpStatus.OK);
    }

    // Build Update Bid REST API
    @PutMapping("{id}")
    // http://localhost:8080/api/Bids/1
    public ResponseEntity<Bid> updateBid(@PathVariable("id") Long bidId,
                                           @RequestBody Bid bid){
        bid.setId(bidId);
        Bid updatedBid = bidService.updateBid(bid);
        return new ResponseEntity<>(updatedBid, HttpStatus.OK);
    }

    // Build Delete Bid REST API
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteBid(@PathVariable("id") Long bidId){
        bidService.deleteBid(bidId);
        return new ResponseEntity<>("Bid successfully deleted!", HttpStatus.OK);
    }

}