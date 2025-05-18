package com.amin.ameenserver.admin;

import com.amin.ameenserver.order.Bid;
import com.amin.ameenserver.order.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminBidController {

    private final BidService bidService;

    @Autowired
    public AdminBidController(BidService bidService) {
        this.bidService = bidService;
    }

    @PostMapping("/bids")
    @ResponseBody
    public ResponseEntity<Bid> doCreateBid(@RequestBody Bid bid) {
        try {
            Bid savedBid = bidService.save(bid);
            return new ResponseEntity<>(savedBid, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/bids/create")
    public String createBid(Model model) {
        return "admin/createBid";
    }

    @GetMapping("/bids/{id}")
    public String getBid(@PathVariable Long id, Model model) {
        Bid bid = bidService.findById(id).orElse(null);
        if (bid != null) {
            model.addAttribute("bid", bid);
            return "admin/getBid";
        } else {
            return "admin/bidNotFound";
        }
    }

    @GetMapping("/bids")
    public String getOrderBids(@RequestParam("oid") Long orderId, Model model) {
        List<Bid> allBids = bidService.findAll();
        model.addAttribute("bids", allBids);
        return "admin/allBids";
    }

    @GetMapping("/bids/getUserBids")
    public String getUserBids(@RequestParam("u") Long userId, Model model) {
        List<Bid> allBids = bidService.findAll();
        model.addAttribute("bids", allBids);
        return "admin/allBids";
    }

    @PostMapping("/bids/delete/{id}")
    @ResponseBody
    public String deleteBid(@PathVariable Long id, Model model) {
        Bid bid = bidService.findById(id).orElse(null);
        if (bid != null) {
            bidService.delete(bid);
            model.addAttribute("message", "Bid deleted successfully");
            return "admin/deleteSuccess";
        } else {
            return "admin/bidNotFound";
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
