package webproject.easydent.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import webproject.easydent.entities.Product;
import webproject.easydent.service.ProductService;
import webproject.easydent.vos.CustomOAuth2User;

import java.util.List;

@Controller
@RequestMapping("/shop")
@RequiredArgsConstructor
@Slf4j
public class ShopController {

    public final ProductService productService;

    @GetMapping
    public String showShop(Model model, @AuthenticationPrincipal CustomOAuth2User customOAuth2User) throws JsonProcessingException {
        List<Product> products = productService.getAllProducts();
        ObjectMapper mapper = new ObjectMapper();
        String productsJson = mapper.writeValueAsString(products);
        model.addAttribute("products", productsJson);
        log.info(productsJson);
        return "shop";
    }

    @GetMapping("/detail")
    public String shopDetail(Model model){
        return "shop_detail";
    }
}
