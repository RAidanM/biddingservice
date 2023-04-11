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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



import java.io.IOException;
import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.method.HandlerMethod;

import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;


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

    @GetMapping("/success")
    public String goSuccess(Model model) {
        // population empty attributes
        model.addAttribute("auctionItem", new AuctionItem());
        model.addAttribute("userId", 1L);
        return "success";
    }

    @GetMapping("/room/{user}/{id}")
    public String getRoom(@PathVariable("id") Long auctionId, @PathVariable("user") Long userId, Model model) {

        restTemplate = new RestTemplate();

        //GET from AuctionDB
        String url = "http://localhost:8090/auction/items/"+auctionId;
        AuctionItem auctionItem = restTemplate.getForObject(url, AuctionItem.class);
        System.out.println("Response: " + auctionItem);

        //GET from UserDB //TODO Uncomment this and make work
//        url = "http://localhost:8080/userNameTransfer/"+auctionItem.getCurrent_bidder_id();
//        String highest_bidder_name = restTemplate.getForObject(url, String.class);
//        System.out.println("Response: " + highest_bidder_name);

        //SETTING TIME
        Date startDate = auctionItem.getCreate_date();
        int length = auctionItem.getAuction_length();
        long endTime = length * 3600000L;
        Date endDate = new Date(startDate.getTime()+endTime);
        Date currentTime = new Date();
        long timeLeft = endDate.getTime()-currentTime.getTime();
        long timeLeftSeconds = timeLeft/1000L;

        //model
        model.addAttribute("auctionItem", auctionItem);
        model.addAttribute("userId", userId);
//        model.addAttribute("highestBidderName", highest_bidder_name);
        model.addAttribute("bid", new Bid());
        model.addAttribute("timeleft", timeLeftSeconds);


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

    @PostMapping("/room/forward/{user}/{id}")
    public String forwardSubmit(@PathVariable("user") Long userId, @PathVariable("id") Long auctionId,
                                @ModelAttribute Bid bid, @ModelAttribute AuctionItem auctionItem, Model model) {

        //GET from AuctionDB //TODO replace second GET call with model (still works just not efficent)
        restTemplate = new RestTemplate();
        String url2 = "http://localhost:8090/auction/items/"+auctionId;
        AuctionItem auctionItemReplace = restTemplate.getForObject(url2, AuctionItem.class);
        System.out.println("Response: " + auctionItemReplace);

        System.out.println("Auction Id:"+auctionId);
        System.out.println("User Id:"+userId);
        System.out.println("Bid:"+bid.getBidPrice());
        System.out.println("Auction Item:"+auctionItemReplace);

        //Set Bid
        bid.setUserId(userId);
        bid.setAuctionId(auctionId);

        //Check Bid
        System.out.println(bid);

        //MAKE BID FOR AUCTION ITEM
        createBid(bid);

        //SETTING TIME
        Date startDate = auctionItemReplace.getCreate_date();
        int length = auctionItemReplace.getAuction_length();
        long endTime = length * 3600000;
        Date endDate = new Date(startDate.getTime()+endTime);
        Date currentTime = new Date();

        //Checks to ensure higher Bid then current highest bid
        if(auctionItemReplace.getCurrent_price() < bid.getBidPrice() && endDate.compareTo(currentTime) >= 0){

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
        else {System.out.println("Bid not high enough or past auction time");}

        return getRoom(auctionId, userId, model);
    }

    @PostMapping("/room/dutch/{user}/{id}")
    public String dutchSubmit(@PathVariable("user") Long userId, @PathVariable("id") Long auctionId,
                                @ModelAttribute Bid bid, @ModelAttribute AuctionItem auctionItem, Model model) {

        //GET from AuctionDB //TODO replace second GET call with model (still works just not efficent)
        restTemplate = new RestTemplate();
        String url2 = "http://localhost:8090/auction/items/"+auctionId;
        AuctionItem auctionItemReplace = restTemplate.getForObject(url2, AuctionItem.class);
        System.out.println("Response: " + auctionItemReplace);

        System.out.println("Auction Id:"+auctionId);
        System.out.println("User Id:"+userId);
        System.out.println("Bid:"+bid.getBidPrice());
        System.out.println("Auction Item:"+auctionItemReplace);

        //Set Bid
        bid.setUserId(userId);
        bid.setAuctionId(auctionId);
        bid.setBidPrice(auctionItem.getCurrent_price());

        //Check Bid
        System.out.println(bid);

        //MAKE BID FOR AUCTION ITEM
        createBid(bid);

        auctionItemReplace.setCurrent_bidder_id(bid.getUserId());

        String url = "http://localhost:8090/auction/items/"+auctionId;

        // create a request object with the data to be updated
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AuctionItem> requestEntity = new HttpEntity<>(auctionItemReplace, headers);

        // send the PUT request
        restTemplate.put(url, requestEntity);
        System.out.println("Put:" + auctionItemReplace);

        return goAuctionEnded(userId, auctionId, model);
    }
    @GetMapping("/auctionended/{user}/{id}")
    public String goAuctionEnded(@PathVariable("user") Long userId, @PathVariable("id") Long auctionId, Model model) {

        //GET from AuctionDB //TODO replace second GET call with model (still works just not efficent)
        restTemplate = new RestTemplate();
        String url2 = "http://localhost:8090/auction/items/"+auctionId;
        AuctionItem auctionItemReplace = restTemplate.getForObject(url2, AuctionItem.class);
        System.out.println("Response: " + auctionItemReplace);

        // Added for model on success.html page
        model.addAttribute("userId", userId);
        model.addAttribute("auctionItem", auctionItemReplace);

        System.out.println(auctionItemReplace.getCurrent_bidder_id()+"=="+userId);

        if(auctionItemReplace.getCurrent_bidder_id()==userId){
            System.out.println("Winner");
            return "success";
        }
        else {
            System.out.println("Loser");
            return "auctionended";
        }
    }

    // used to go to the payment microservice
    @GetMapping("/toPayment/{uID}/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public void toPayment(HttpServletResponse response,
                          @PathVariable String uID, @PathVariable long itemId,
                          @RequestParam(name = "expeditedShipping", required = false) Boolean expeditedShipping,
                          Model model)  throws IOException, IOException {

        String target = "http://localhost:8081/checkout";// sends to checkout page //"+uID+"/"+itemId;

        model.addAttribute("expeditedShipping", expeditedShipping != null && expeditedShipping);
        model.addAttribute("userId", uID);
        model.addAttribute("auctionItemId", itemId);

        target = target + "/" + model.getAttribute("userId")
                        + "/" + model.getAttribute("auctionItemId")
                        + "/" + model.getAttribute("expeditedShipping");

        System.out.println(target);
        response.sendRedirect(target);
        System.out.println("Sent to Payment Page");
        //return new ResponseEntity<>("Redirected to Payment Successfully", HttpStatus.OK);
    }

}