package com.amin.ameenserver.admin;

import com.amin.ameenserver.order.Order;
import com.amin.ameenserver.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping(value = "/orders/{id}")
    public String getOrder(Model model, @PathVariable long id){
        Order order = orderService.getOrder(id);
       

        model.addObject("order", order);
        return "admin/order";
    }

    @GetMapping(value = "/orders" , produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAllOrders(Model modelView, @RequestParam(defaultValue = "1") int page){

        var orders = orderService.getAllOrders(page);

        modelView.addObject("orders", orders);
        return "admin/orderList";
    }

    @PostMapping(name = "/orders/{id}/delete")
    public ResponseEntity<String> deleteOrder(@PathVariable long id){
        Order order = orderService.getOrder(id);
        if (order == null){
            return ResponseEntity.badRequest().build();
        }
        //orderService.deleteOrder(order);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @PostMapping("/orders")
    public ResponseEntity<Order> createOrUpdateOrder(Order order){
        Order order1 = orderService.createOrUpdateOrder(order);
        return new ResponseEntity<>(order1, HttpStatus.OK);
    }

    @PostMapping("/orders/createTestOrder")
    public ResponseEntity<Order> createTestOrder(Order order){
        Order order1 = orderService.createTestOrder(order);
        return new ResponseEntity<>(order1, HttpStatus.OK);
    }
}
