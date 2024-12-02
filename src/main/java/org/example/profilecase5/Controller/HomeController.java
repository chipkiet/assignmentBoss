package org.example.profilecase5.Controller;

import org.example.profilecase5.Model.House;
import org.example.profilecase5.Model.HouseImage;
import org.example.profilecase5.Model.RentalHistory;
import org.example.profilecase5.Model.User;
import org.example.profilecase5.Service.HouseService;
import org.example.profilecase5.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private UserService userService;
    @Autowired
    private HouseService houseService;

    @GetMapping("")
    public String getAccountPage(Model model, Authentication authentication) {
        String username = authentication.getName();  // Lấy username của người dùng hiện tại
        User user = userService.getUserByUsername(username);  // Tìm người dùng từ username

        if (user != null) {
            model.addAttribute("user", user);
        } else {
            model.addAttribute("error", "User not found");
            return "error";  // Nếu không tìm thấy người dùng
        }

        List<HouseImage> mainImages = houseService.getMainImages();
        model.addAttribute("mainImages", mainImages);

        return "home/home";
    }
    @GetMapping("/detail/{id}")
    public String showDetail(@PathVariable("id") Integer id, Model model) {
        House house = houseService.getHouseById(id);
        List<HouseImage> images = houseService.getImagesByHouseId(id);

        model.addAttribute("house", house);
        model.addAttribute("images", images);

        return "detail/detail";
    }

    @GetMapping("/detail")
    public String userDetails(Model model) {
        // Lấy thông tin người dùng từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName(); // Lấy tên đăng nhập của người dùng

        // Tìm người dùng trong cơ sở dữ liệu dựa trên tên đăng nhập
        User user = userService.getUserByUsername(currentUsername);
        if (user == null) {
            throw new RuntimeException("User not found with username: " + currentUsername);
        }

        // Lấy danh sách lịch sử thuê nhà của người dùng
        Set<RentalHistory> rentalHistories = user.getRentalHistories();

        // Thêm thông tin vào model
        model.addAttribute("user", user);
        model.addAttribute("rentalHistories", rentalHistories);

        // Tính tổng số tiền đã chi tiêu
        double totalSpent = rentalHistories.stream().mapToDouble(RentalHistory::getTotalCost).sum();
        model.addAttribute("totalSpent", totalSpent);

        return "home/history";  // Tên file Thymeleaf
    }

}
