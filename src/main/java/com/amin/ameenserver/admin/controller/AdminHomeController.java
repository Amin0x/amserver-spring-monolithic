package com.amin.ameenserver.admin.controller;

import com.amin.ameenserver.AmeenApplication;
import com.amin.ameenserver.settings.SettingsRepository;
import com.amin.ameenserver.settings.VersionInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/admin")
public class AdminHomeController {
    @Autowired
    private AdminOrderService orderService;
    private AdminUserService userService;

    @GetMapping(value = "/")
    public String index(Model model){
        
        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDate today = LocalDate.now(ZoneId.of("Europe/Berlin"));
        LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);
        LocalDateTime tomorrowMidnight = todayMidnight.plusDays(1);

        LocalDate localDate = LocalDate.now(ZoneId.systemDefault());
        String firstDayOfYear = localDate.with(TemporalAdjusters.firstDayOfYear()).toString();
        String lastDayOfYear = localDate.with(TemporalAdjusters.lastDayOfYear()).toString();
        
        long userCount = userService.getUsersCount(0);
        long totalDrivers = ueerService.getUsersCount(1);
        long ordersCount = orderService.getOrdersCount();
        long totalRiders = userService.getRidersCount();
        long newUsersTodayCount = userService.getNewUsersCount(todayMidnight, tomorrowMidnight);
        long ordersToday = orderService.getOrderCount(firstDayOfYear, lastDayOfYear);
        long ordersWeek = orderService.getOrderCount(firstDayOfYear, lastDayOfYear);
        long ordersMonth = orderService.getOrderCount(firstDayOfYear, lastDayOfYear);
        long ordersYear = orderService.getOrderCount(firstDayOfYear, lastDayOfYear);
        
        model.addAttribute("totalUsers", userCount);
        model.addAttribute("totalDrivers", totalDrivers);
        model.addAttribute("totalRiders", totalRiders);
        model.addAttribute("newUsersTodayCount", newUsersTodayCount);
        model.addAttribute("totalOrders", ordersCount);
        model.addAttribute("ordersToday", ordersToday);
        model.addAttribute("ordersMonth", ordersMonth);
        model.addAttribute("ordersWeek", ordersWeek);
        model.addAttribute("title", "Amin Admin Panel- Home");
        model.addAttribute("orders", "");
        
        return "admin/index";
    }
}
