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

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


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

//    @GetMapping("/room")
//    public String startRoom(HttpServletRequest request, HttpServletResponse response) {
//
//        //Faruq  vv
//        String auctionId = (String) request.getSession().getAttribute("itemId");
//        String userId = (String) request.getSession().getAttribute("username");
//        System.out.println("AuctionId: " + auctionId + "Username:" +userId);
//
//        //Aidan  vv
//        RestTemplate restTemplate = new RestTemplate();
//        String url = "http://localhost:8090/auction/items/"+auctionId;
//        String r = restTemplate.getForObject(url, String.class);
//        System.out.println("Response: " + r);
//
//
//        return "timer";
//    }

    @GetMapping("/room/{user}/{id}")
    public String getRoom(@PathVariable("id") Long auctionId, @PathVariable("user") Long userId, Model model) {

        restTemplate = new RestTemplate();

        //GET from AuctionDB
        String url = "http://localhost:8090/auction/items/"+auctionId;
        AuctionItem auctionItem = restTemplate.getForObject(url, AuctionItem.class);
        System.out.println("Response: " + auctionItem);

        //GET from UserDB
//        url = "http://localhost:8080/userNameTransfer/"+auctionItem.getCurrent_bidder_id();
//        String highest_bidder_name = restTemplate.getForObject(url, String.class);
//        System.out.println("Response: " + highest_bidder_name);

        //model
        model.addAttribute("auctionItem", auctionItem);
        model.addAttribute("userId", userId);
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

    @PostMapping("/room/{user}/{id}")
    public String forwardSubmit(@PathVariable("user") Long userId, @PathVariable("id") Long auctionId,
                                @ModelAttribute Bid bid, @ModelAttribute AuctionItem auctionItem, Model model) {

        //GET from AuctionDB //TODO replace second GET call with model
        restTemplate = new RestTemplate();
        String url2 = "http://localhost:8090/auction/items/"+auctionId;
        AuctionItem auctionItemReplace = restTemplate.getForObject(url2, AuctionItem.class);
        System.out.println("Response: " + auctionItemReplace);

        System.out.println("Auction Id:"+auctionId);
        System.out.println("User Id:"+userId);
        System.out.println("Bid:"+bid.getBidPrice());
        System.out.println("Auction Item:"+auctionItemReplace);

        //Temp set user
        bid.setUserId(userId);
        bid.setAuctionId(auctionId);

        //Check Bid
        System.out.println(bid);

        //MAKE BID FOR AUCTION ITEM
        createBid(bid);

        //Checks to ensure higher Bid then current highest bid
        if(auctionItemReplace.getCurrent_price() < bid.getBidPrice()){


            auctionItemReplace.setCurrent_price(bid.getBidPrice());
            auctionItemReplace.setCurrent_bidder_id(bid.getUserId());
            System.out.println(auctionItemReplace);

            restTemplate = new RestTemplate();

            String url = "http://localhost:8090/auction/items/"+auctionId;

            // create a request object with the data to be updated
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<AuctionItem> requestEntity = new HttpEntity<>(auctionItemReplace, headers);

            // send the PUT request
            restTemplate.put(url, requestEntity);
            System.out.println("Put:" + auctionItemReplace);
        }
        else {System.out.println("Bid not high enough");}

        return getRoom(auctionId, userId, model);
    }

//    @GetMapping("/room/{username}/{itemId}")
//    public String startRoom(HttpServletRequest request, HttpServletResponse response, @PathVariable String username, @PathVariable long itemId) {
//        System.out.println("StartRoom");
//        //Faruq  vv
////        String auctionId = (String) request.getSession().getAttribute("itemId");
////        String userId = (String) request.getSession().getAttribute("username");
//        System.out.println("AuctionId: " + itemId + " Username: " + username);
//
//        request.getSession().setAttribute("username", username);
//        request.getSession().setAttribute("itemId", itemId);
//
//        //Aidan  vv
//        RestTemplate restTemplate = new RestTemplate();
//        String url = "http://localhost:8090/auction/items/"+itemId;
//        String r = restTemplate.getForObject(url, String.class);
//        System.out.println("Response: " + r);
//
//        return "timer";
//    }

    @GetMapping("/auctionended")
    public String goAuctionEnded() {
        return "auctionended";
    }

}