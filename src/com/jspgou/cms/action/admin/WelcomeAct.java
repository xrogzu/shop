package com.jspgou.cms.action.admin;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jspgou.cms.entity.ShopAdmin;
import com.jspgou.cms.manager.OrderMng;
import com.jspgou.cms.manager.ReceiverMessageMng;
import com.jspgou.cms.manager.ShopMemberMng;
import com.jspgou.cms.web.SiteUtils;
import com.jspgou.cms.web.threadvariable.AdminThread;

import com.jspgou.common.page.Pagination;
import com.jspgou.cms.entity.ShopMember;
import com.jspgou.common.web.CookieUtils;
import static com.jspgou.common.page.SimplePage.cpn;

/**
* This class should preserve.
* @preserve
*/
@Controller
public class WelcomeAct {

	@RequestMapping("/main.do")
	public String main() {
		return "main";
	}

	@RequestMapping("/left.do")
	public String left() {
		return "left";
	}

	@RequestMapping("/right.do")
	public String right(HttpServletRequest request,ModelMap model) {
		List<Object> o=manager.getTotlaOrder();
		ShopAdmin admin = AdminThread.get();
		Long[] c=(Long[])o.get(0);
		Runtime runtime = Runtime.getRuntime();
		long freeMemoery = runtime.freeMemory();
		long totalMemory = runtime.totalMemory();
		long usedMemory = totalMemory - freeMemoery;
		long maxMemory = runtime.maxMemory();
		long useableMemory = maxMemory - totalMemory + freeMemoery;
		model.addAttribute("c", c);
		model.addAttribute("admin", admin);
		model.addAttribute("restart", 4.6);
		model.addAttribute("site", com.jspgou.core.web.SiteUtils.getWeb(request));
		model.addAttribute("freeMemoery", freeMemoery);
		model.addAttribute("totalMemory", totalMemory);
		model.addAttribute("usedMemory", usedMemory);
		model.addAttribute("maxMemory", maxMemory);
		model.addAttribute("useableMemory", useableMemory);
		return "right";
	}
	
	@RequestMapping("/top.do")
	public String top(HttpServletRequest request,ModelMap model) {
		ShopAdmin admin = AdminThread.get();
		model.addAttribute("admin", admin);
		
		/*[获得当前操作员所收到的“未读邮件“的总数----->rcvMsgUnRead]*/
		ShopMember Receiver= shopMemberMng.findUsername(admin.getUsername());
		Pagination pagination = receiverMessageMng.getUnreadPage(Receiver.getId(),cpn(null),
				CookieUtils.getPageSize(request));
		model.addAttribute("rcvMsgUnRead", pagination.getTotalCount());
		return "top";
	}

	@Autowired
	private OrderMng manager;
	@Autowired
	private ShopMemberMng shopMemberMng;
	@Autowired
	private ReceiverMessageMng receiverMessageMng;

}
