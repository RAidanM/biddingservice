//package easyBid.biddingservice.controller;
//
//import easyBid.biddingservice.entity.AuctionItem;
//import easyBid.biddingservice.entity.Bid;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.client.RestTemplate;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//
//
//@Controller
////@RequestMapping("/") i think you can just ignore this -syed
//public class FrontController {
//
//    RestTemplate restTemplate;
//
//    @GetMapping("/timer")
//    public String goTimer() {
//        return "timer";
//    }
//
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
//
//    @GetMapping("/room/{id}")
//    public String getRoom(@PathVariable("id") Long auctionId, Model model) {
//
//        RestTemplate restTemplate = new RestTemplate();
//
//        //GET from AuctionDB
//        String url = "http://localhost:8090/auction/items/"+auctionId;
//        AuctionItem auctionItem = restTemplate.getForObject(url, AuctionItem.class);
//        System.out.println("Response: " + auctionItem.toString());
//
//        //GET from UserDB
//        url = "http://localhost:8080/userNameTransfer/"+auctionItem.getCurrent_bidder_id();
//        String highest_bidder_name = restTemplate.getForObject(url, String.class);
//        System.out.println("Response: " + highest_bidder_name);
//
//        //model
//        model.addAttribute("auctionItem", auctionItem);
//        model.addAttribute("highestBidderName", highest_bidder_name);
//
//        model.addAttribute("bid", new Bid());
//
//
//        //choosing dutch or forward page
//        if(auctionItem.getAuction_type().equals("DUTCH")){
//            return "dutchroom";
//        }
//        else if(auctionItem.getAuction_type().equals("FORWARD")) {
//            return "forwardroom";
//        }
//        else{
//            System.out.println("No auction type specified");
//            return "timer";
//        }
//    }
//
//    @PostMapping("/room/{id}")
//    public String forwardSubmit(@ModelAttribute Bid bid, Model model) {
//        model.addAttribute("bid", bid);
//        return "result";
//    }
//
////    @PostMapping("/forwardroom")
////    public String forwardSubmit(@ModelAttribute Bid bid, Model model) {
////        model.addAttribute("bid", bid);
////        return "result";
////    }
//
//    @GetMapping("/auctionended")
//    public String goAuctionEnded() {
//        return "auctionended";
//    }
//}