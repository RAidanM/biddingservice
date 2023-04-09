package easyBid.biddingservice.controller;

import easyBid.biddingservice.entity.AuctionItem;
import easyBid.biddingservice.entity.Bid;
import easyBid.biddingservice.service.BidService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BidController {

    private BidService bidService;

    RestTemplate restTemplate;

    @Autowired
    public BidController(BidService bidService){
        this.bidService=bidService;
    }

    @GetMapping("api/bids/{id}")
    public ResponseEntity<Bid> getBidById(@PathVariable("id") Long bidId){
        Bid bid = bidService.getBidById(bidId);
        return new ResponseEntity<>(bid, HttpStatus.OK);
    }

    // build create Bid REST API
    @PostMapping("api/bids")
    public ResponseEntity<Bid> createBid(@RequestBody Bid bid){
        Bid savedBid = bidService.createBid(bid);
        return new ResponseEntity<>(savedBid, HttpStatus.CREATED);
    }

    // Build Get All Bids REST API
    // http://localhost:8080/api/Bids
    @GetMapping("api/bids")
    public ResponseEntity<List<Bid>> getAllBids(){
        List<Bid> bids = bidService.getAllBids();
        return new ResponseEntity<>(bids, HttpStatus.OK);
    }

    // Build Update Bid REST API
    @PutMapping("api/bids/{id}")
    // http://localhost:8080/api/Bids/1
    public ResponseEntity<Bid> updateBid(@PathVariable("id") Long bidId,
                                           @RequestBody Bid bid){
        bid.setId(bidId);
        Bid updatedBid = bidService.updateBid(bid);
        return new ResponseEntity<>(updatedBid, HttpStatus.OK);
    }

    // Build Delete Bid REST API
    @DeleteMapping("api/bids/{id}")
    public ResponseEntity<String> deleteBid(@PathVariable("id") Long bidId){
        bidService.deleteBid(bidId);
        return new ResponseEntity<>("Bid successfully deleted!", HttpStatus.OK);
    }

    @GetMapping("/timer")
    public String goTimer() {
        return "timer";
    }

    @GetMapping("/room")
    public String startRoom(HttpServletRequest request, HttpServletResponse response) {

        //Faruq  vv
        String auctionId = (String) request.getSession().getAttribute("itemId");
        String userId = (String) request.getSession().getAttribute("username");
        System.out.println("AuctionId: " + auctionId + "Username:" +userId);

        //Aidan  vv
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8090/auction/items/"+auctionId;
        String r = restTemplate.getForObject(url, String.class);
        System.out.println("Response: " + r);


        return "timer";
    }

    @GetMapping("/room/{id}")
    public String getRoom(@PathVariable("id") Long auctionId, Model model) {

        RestTemplate restTemplate = new RestTemplate();

        //GET from AuctionDB
        String url = "http://localhost:8090/auction/items/"+auctionId;
        AuctionItem auctionItem = restTemplate.getForObject(url, AuctionItem.class);
        System.out.println("Response: " + auctionItem.toString());

        //GET from UserDB
//        url = "http://localhost:8080/userNameTransfer/"+auctionItem.getCurrent_bidder_id();
//        String highest_bidder_name = restTemplate.getForObject(url, String.class);
//        System.out.println("Response: " + highest_bidder_name);

        //model
        model.addAttribute("auctionItem", auctionItem);
//        model.addAttribute("highestBidderName", highest_bidder_name);

        model.addAttribute("bid", new Bid());


        //choosing dutch or forward page
        if(auctionItem.getAuction_type().equals("DUTCH")){
            return "dutchroom";
        }
        else if(auctionItem.getAuction_type().equals("FORWARD")) {
            return "forwardroom";
        }
        else{
            System.out.println("No auction type specified");
            return "timer";
        }
    }

    //TODO
    @PostMapping("/room/{id}")
    public String forwardSubmit(@PathVariable("id") Long auctionId, @ModelAttribute Bid bid) {
        System.out.println("Auction Id:"+auctionId);
        System.out.println("Bid:"+bid.getBidPrice());

        //MAKE BID FOR AUCTION ITEM
        //SEND PUT TO UPDATE AUCTIONITEM



        return "forwardroom";
    }

    @GetMapping("/auctionended")
    public String goAuctionEnded() {
        return "auctionended";
    }

}