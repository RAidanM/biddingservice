package easyBid.biddingservice.controller;

import easyBid.biddingservice.entity.BiddingRecord;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("biddingroom")
public class TestController {


    @GetMapping("/button")
    public String goButton() {
        return "button";
    }

    @PostMapping("/myFormSubmit")
    public String handleFormSubmit(@RequestParam("inputData") String inputData) {
        // do something with the submitted data
        System.out.println("Submitted data: " + inputData);

        // return the name of the Thymeleaf template to render
        return "button";
    }

    @GetMapping("/timer")
    public String goTimer() {
        return "timer";
    }

    @GetMapping("/greeting")
    public String greetingForm(Model model) {
        model.addAttribute("biddingRecord", new BiddingRecord());
        return "greeting";
    }

    @PostMapping("/greeting")
    public String greetingSubmit(@ModelAttribute BiddingRecord biddingRecord, Model model) {
        model.addAttribute("biddingRecord", biddingRecord);
        return "result";
    }

}