package com.sust.controller.buyer;

import com.sust.model.TEnterInfoForDispatch;
import com.sust.model.TItem;
import com.sust.service.EnterInfoForDispatchService;
import com.sust.service.ItemService;
import com.sust.service.UserService;
import com.sust.utils.CookieUtils;
import com.sust.utils.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 2018/4/26
 */
@Controller
public class HomePageController {

    @Resource
    private UserService userService;
    @Resource
    private ItemService itemService;
    @Resource
    private EnterInfoForDispatchService enterInfoForDispatchService;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String toIndex(@RequestParam(value = "curpage", required = false) Integer curpage, @RequestParam(value = "type", required = false) String type, Model model) {
        //分页数据
        Page page = new Page();
        page.vaildCurrentPageNum(curpage);

        Page page2 = new Page();
        page2.vaildCurrentPageNum(curpage);
        page2.setPageNumber(3);

        //查询商品信息
        List<TItem> items = itemService.queryAllByPage(page.getCurrentPage(), page.getPageNumber());
        //查询店铺信息
        List<TEnterInfoForDispatch> enterInfoForDispatchList = enterInfoForDispatchService.queryEnterInfoForDispatchByPage(page2.getCurrentPage(), page2.getPageNumber());

        model.addAttribute("items", items);
        page.updatePageInfo(items);
        model.addAttribute("page", page);

        model.addAttribute("enterInfoForDispatchList", enterInfoForDispatchList);
        page2.updatePageInfo(enterInfoForDispatchList);
        model.addAttribute("page2", page2);
        return "front/index";
    }

    @RequestMapping(value = "/list_item", method = RequestMethod.GET)
    public String listItem(@RequestParam(value = "curpage", required = false) Integer curpage, Model model) {
        //分页数据
        listItemAction(curpage, model);
        return "front/index";
    }


    private void listItemAction(Integer curpage, Model model) {
        Page page = new Page();
        page.vaildCurrentPageNum(curpage);

        //查询商品信息
        List<TItem> items = itemService.queryAllByPage(page.getCurrentPage(), page.getPageNumber());


        page.updatePageInfo(items);

        model.addAttribute("items", items);
        model.addAttribute("page", page);
    }

    @RequestMapping(value = "/register.html", method = RequestMethod.GET)
    public String toRegister() {
        return "front/register";
    }

    //跳转登录页面
    @RequestMapping(value = "/login.html", method = RequestMethod.GET)
    public String toLogin() {
        return "front/login";
    }

    //跳转用户中心
    @RequestMapping(value = "/user/user_center", method = RequestMethod.GET)
    public String toUserCenter(HttpServletRequest request) {
        //判断用户是否存在,存在转到user_center，否则转到登录页
        if (this.isLogin(request)) {
           /* String userId = CookieUtils.getCookieValue(request, "userId");
            TUser user = userService.queryUserInfoByUserId(userId);*/
            return "front/user_center";
        }
        return "front/login";
    }

    @RequestMapping(value = "/user/user_orderlist", method = RequestMethod.GET)
    public String toUserOrderlist(HttpServletRequest request) {
        if (this.isLogin(request)) {
            return "front/user_orderlist";
        }
        return "front/login";
    }

    @RequestMapping(value = "/user/cart", method = RequestMethod.GET)
    public String toCart(HttpServletRequest request) {
        if (this.isLogin(request)) {
            return "front/user_orderlist";
        }
        return "front/login";
    }

    @RequestMapping("/list")
    public String toList() {
        return "front/list";
    }


    @RequestMapping("/article_read.html")
    public String ToarticleRead() {
        return "front/article_read";
    }

    private boolean isLogin(HttpServletRequest request) {
        String userName = CookieUtils.getCookieValue(request, "userId");
        if (userName != null && userName.length() > 0) {
            return true;
        }
        return false;
    }

    @RequestMapping("/abcd")
    public String toabcd(){
        return "front/abcd";
    }
}
