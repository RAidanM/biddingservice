package easyBid.biddingservice.controller;

import easyBid.biddingservice.entity.Bid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@Controller
//@RequestMapping("/") i think you can just ignore this -syed
public class TestController {

    RestTemplate restTemplate;

    @GetMapping("/timer")
    public String goTimer() {
        return "timer";
    }

    @GetMapping("/room/{username}/{itemId}")
    public String startRoom(HttpServletRequest request, HttpServletResponse response, @PathVariable String username, @PathVariable long itemId) {
        System.out.println("StartRoom");
        //Faruq  vv
//        String auctionId = (String) request.getSession().getAttribute("itemId");
//        String userId = (String) request.getSession().getAttribute("username");
        System.out.println("AuctionId: " + itemId + " Username: " + username);

        request.getSession().setAttribute("username", username);
        request.getSession().setAttribute("itemId", itemId);

        //Aidan  vv
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8090/auction/items/"+itemId;
        String r = restTemplate.getForObject(url, String.class);
        System.out.println("Response: " + r);

        return "timer";
    }

//    @GetMapping("/room/{id}")
//    public String getRoom(@PathVariable("id") Long auctionId) {
//
//        RestTemplate restTemplate = new RestTemplate();
//        String url = "http://localhost:8090/auction/items/"+auctionId;
//        String response = restTemplate.getForObject(url, String.class);
//        System.out.println("Response: " + response);
//
//
//        return "timer";
//    }


    @GetMapping("/forwardroom")
    public String forwardForm(Model model) {
        model.addAttribute("bid", new Bid());
        return "forwardroom";
    }

    @PostMapping("/forwardroom")
    public String forwardSubmit(@ModelAttribute Bid bid, Model model) {
        model.addAttribute("bid", bid);
        return "result";
    }

    @GetMapping("/auctionended")
    public String goAuctionEnded() {
        return "auctionended";
    }
}