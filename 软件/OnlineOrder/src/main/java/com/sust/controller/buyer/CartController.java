package com.sust.controller.buyer;

import com.sust.constants.CookieConstant;
import com.sust.constants.UserConstant;
import com.sust.dto.ItemDetailDto;
import com.sust.model.*;
import com.sust.service.EnterInfoForDispatchService;
import com.sust.service.EnterpriseInfoService;
import com.sust.service.ItemService;
import com.sust.service.UserService;
import com.sust.utils.CookieUtils;
import com.sust.utils.JsonUtils;
import com.sust.utils.Result;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping("/user")
@Controller
public class CartController {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CartController.class);

    @Resource
    private ItemService itemService;
    @Resource
    private EnterpriseInfoService enterpriseInfoService;
    @Resource
    private UserService userService;
    @Resource
    private EnterInfoForDispatchService enterInfoForDispatchService;

    @RequestMapping(value = "/additemtocart", method = RequestMethod.POST)
    @ResponseBody
    public String tocartPage(@RequestParam("Number") Integer Number,
                             @RequestParam("itemId") String itemId,
                             HttpServletRequest request,
                             HttpServletResponse response) {
        //TODO:此处应判断是否为登录用户！

        logger.info("number:{}|itemId:{}", Number, itemId);

        addItemToCart(itemId, Number, request, response);

        return JsonUtils.objectToJson(Result.build(0, "添加成功"));
    }

    /**
     * 删除在购物车中的item
     * @param itemId
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/delitemfromcart",method = RequestMethod.POST)
    @ResponseBody
    public String delitemfromcart(@RequestParam("itemId") String itemId,
                                  HttpServletRequest request,
                                  HttpServletResponse response){
        removeItemFromCart(itemId,request,response);
        return JsonUtils.objectToJson(Result.build(0, "删除成功"));
    }

    @RequestMapping("/tocart")
    public String tocart(HttpServletRequest request,
                         HttpServletResponse response,
                         Model model) {
        String cookieVal = CookieUtils.getCookieValue(request, CookieConstant.SHOPPING_CART_NAME);
        List<ItemDetailDto> itemDetailDtos = null;
        if (cookieVal != null) {
            itemDetailDtos = getItemFromCart(cookieVal);
        }
        model.addAttribute("itemDetailDtos", itemDetailDtos);
        return "front/cart";
    }

    @RequestMapping(value = "/toconfirm_order", method = RequestMethod.GET)
    public String toconfirm_order() {

      /*  String cookieValue = CookieUtils.getCookieValue(request, CookieConstant.SHOPPING_CART_NAME);
        List<ItemDetailDto> itemDetailDtos = new ArrayList<>();
        if(cookieValue != null) {
            getItemFromCart(cookieValue,itemDetailDtos);
        }
        model.addAttribute("total", total);
        model.addAttribute("itemDetailDtos", itemDetailDtos);*/

        return "redirect:/user/confirm_order";
    }

    /**
     * 生成订单信息，
     * 包括获取地址信息，生成商品信息
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "/confirm_order")
    public String confirm_order(HttpServletRequest request,
                                HttpServletResponse response,
                                Model model) {

        String cookieValue = CookieUtils.getCookieValue(request, CookieConstant.SHOPPING_CART_NAME);
        //获取商品信息
        List<ItemDetailDto> itemDetailDtos = null;
        if (cookieValue != null) {
            itemDetailDtos = getItemFromCart(cookieValue);
        }
        BigDecimal total = null;
        double val = 0.0;
        //计算总价格
        for (ItemDetailDto itemDetailDto : itemDetailDtos) {
            val += itemDetailDto.getItemPrice().doubleValue() * itemDetailDto.getCnt();
        }
        total = new BigDecimal(val);
        //获取地址信息
        String userId = CookieUtils.getCookieValue(request, UserConstant.USER_ID);
        List<TUserAddress> userAddressList = userService.queryAddressByUserId(userId);
        //获取配送价格信息
        TEnterInfoForDispatch infoForDispatch = enterInfoForDispatchService.queryEnterInfoForDispatchByenterpriseId(itemDetailDtos.get(0).getEnterpriseId());
        Short dispatchPrice = infoForDispatch.getDispatchPrice();


        model.addAttribute("userAddressList", userAddressList);
        model.addAttribute("total", total);
        model.addAttribute("itemDetailDtos", itemDetailDtos);
        model.addAttribute("dispatchPrice", dispatchPrice);

        return "front/confirm_order";
    }

    private void addItemToCart(String itemId,
                               Integer cnt,
                               HttpServletRequest request,
                               HttpServletResponse response) {

        String CartCk = CookieUtils.getCookieValue(request, CookieConstant.SHOPPING_CART_NAME);
        //cookie 不存在
        if (CartCk == null || "".equals(CartCk)) {
            Map<String, Integer> shoppingCart = new HashMap<>();
            this.addGoods(shoppingCart, itemId, cnt);
            String s = JsonUtils.objectToJson(shoppingCart);
            logger.info("cookie:{}", s);
            CookieUtils.setCookie(request, response, CookieConstant.SHOPPING_CART_NAME, s);

        } else { //cookie 存在
            String cookieVal = CookieUtils.getCookieValue(request, CookieConstant.SHOPPING_CART_NAME);
            Map<String, Integer> shoppingCart = JsonUtils.jsonToPojo(cookieVal, HashMap.class);
            this.addGoods(shoppingCart, itemId, cnt);
            CookieUtils.setCookie(request, response, CookieConstant.SHOPPING_CART_NAME, JsonUtils.objectToJson(shoppingCart));

        }
    }

    private void removeItemFromCart(String itemId,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {

        String cookieVal = CookieUtils.getCookieValue(request, CookieConstant.SHOPPING_CART_NAME);
        ShoppingCart shoppingCart = JsonUtils.jsonToPojo(cookieVal, ShoppingCart.class);
        shoppingCart.removeGoods(itemId);
        CookieUtils.setCookie(request, response, CookieConstant.SHOPPING_CART_NAME, JsonUtils.objectToJson(shoppingCart));
    }


    private void addGoods(Map<String, Integer> cart, String itemId, Integer cnt) {
        if (cart.containsKey(itemId)) {
            cart.put(itemId, cnt);
            return;
        }
        cart.put(itemId, cnt);
    }

    /**
     * 从cookie中得到商品数据
     *
     * @param cookieVal
     */
    public List<ItemDetailDto> getItemFromCart(String cookieVal) {
        List<ItemDetailDto> itemDetailDtos = new ArrayList<>();
        Map<String, Integer> cart = JsonUtils.jsonToPojo(cookieVal, HashMap.class);
        if (cart != null) {
            List<String> itemIdList = cart.keySet().stream().collect(Collectors.toList());
            List<TItem> itemList = itemService.BatchQueryByitemId(itemIdList);

            for (TItem item : itemList) {
                // 属性设置
                ItemDetailDto itemDetailDto = new ItemDetailDto();
                TEnterpriseInfo tEnterpriseInfo = enterpriseInfoService.queryById(item.getEnterpriseId());
                itemDetailDto.setItemDetailDto(item, tEnterpriseInfo);
                // 数量设置
                itemDetailDto.setCnt(cart.get(item.getItemId()));

                itemDetailDtos.add(itemDetailDto);
            }
        }

        return itemDetailDtos;
    }

    @RequestMapping(value = "changeCookie", method = RequestMethod.POST)
    public void changeCookie(@RequestParam("itemId") String itemId,
                             @RequestParam("cnt") String cnt,
                             HttpServletRequest request,
                             HttpServletResponse response) {
        String cookieVal = CookieUtils.getCookieValue(request, CookieConstant.SHOPPING_CART_NAME);
        Map<String, Integer> shoppingCart = JsonUtils.jsonToPojo(cookieVal, HashMap.class);
        this.addGoods(shoppingCart, itemId, Integer.valueOf(cnt));
        CookieUtils.setCookie(request, response, CookieConstant.SHOPPING_CART_NAME, JsonUtils.objectToJson(shoppingCart));
    }
}
