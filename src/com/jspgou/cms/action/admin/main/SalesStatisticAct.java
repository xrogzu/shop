package com.jspgou.cms.action.admin.main;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.net.bsd.RExecClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jspgou.cms.entity.Order;

import com.jspgou.cms.manager.OrderMng;





@Controller
public class SalesStatisticAct {
	private static final Logger log = LoggerFactory
			.getLogger(SalesStatisticAct.class);
    /*
     * 订单统计
     */
	@RequestMapping("/salesstatistic/v_list.do")
	public String list(Integer pageNo, HttpServletRequest request,
			ModelMap model) {

		List<Order> orders = orderMng.getCountByStatus(null, null, null);
		List<Order> notOrders = orderMng.getCountByStatus(null, null, 3);
		model.addAttribute("notOrders", notOrders);
		Integer flag = 1;

		model.addAttribute("flag", flag);
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("orders", orders);

		return "salesstatistic/list";
	}

  /*
   * 销售额统计
   */
	@RequestMapping("/salesstatistic/v_sale.do")
	public String sale(Integer pageNo,HttpServletRequest request,ModelMap model){
		
		 List<Order> orders=orderMng.getCountByStatus1(null, null, null);
		 List<Order> notOrders=orderMng.getCountByStatus1(null, null, 3);
		   Integer flag= 1;
		 
		   model.addAttribute("notOrders", notOrders);
		   model.addAttribute("orders", orders);
		   model.addAttribute("flag", flag);
		   model.addAttribute("pageNo", pageNo);
		 
		   return "salesstatistic/sale";
		
	}
	/*
	 * 按时间搜索销售额统计
	 */
	@RequestMapping("/salesStatistic/saleroom.do")
	public String saleroom(Integer flag, Integer pageNo,
			Date startTime, Date endTime, HttpServletRequest request,
			ModelMap model){
		Calendar calendar =Calendar.getInstance();
		if(startTime == null && endTime == null){
			endTime=calendar.getTime();
			calendar.add(Calendar.MONTH,-1);
			startTime=calendar.getTime();
		}
		
		 if(flag==1){
			 List<Order> orders=orderMng.getCountByStatus1(startTime, endTime, null);
			   model.addAttribute("orders", orders);
		 }else if(flag==2){
			 int y;
			 if(request.getParameter("year")!=null&&!request.getParameter("year").equals("")){
			   y=Integer.parseInt(request.getParameter("year"));	 
			 }else {
				 y=calendar.get(Calendar.YEAR);
			 }
			List<Order> orders=orderMng.getStatisticByYear1(y, null);
			   model.addAttribute("year", y);
			   model.addAttribute("orders", orders);
			
		 }
			   model.addAttribute("flag", flag);
			   model.addAttribute("startTime", startTime);
			   model.addAttribute("endTime", endTime);
		
		   return "salesstatistic/sale";
		
	}
	

	/*
	 * 按时间搜索订单统计
	 */
	@RequestMapping("/salesStatistic/refer.do")
	public String refer(Integer flag, Integer pageNo,
			Date startTime, Date endTime, HttpServletRequest request,
			ModelMap model) {
		Calendar calendar = Calendar.getInstance();// 创建 Calendar日期 对象
		// 默认一个月
		if (startTime == null && endTime == null) {
			endTime = calendar.getTime();
			calendar.add(Calendar.MONTH, -1);
			startTime = calendar.getTime();
		}

		if (flag == 1) {
			// 选择日期统计
			List<Order> orders = orderMng.getCountByStatus(startTime, endTime,
					null);
			
			for (int i=0;i<orders.size();i++){
				
			}
		} else if (flag == 2) {
			int y;
			if (request.getParameter("year") != null
					&& !request.getParameter("year").equals("")) {
				y = Integer.parseInt(request.getParameter("year"));

			} else {

				y = calendar.get(Calendar.YEAR);

			}
			List<Order> orders = orderMng.getStatisticByYear(y, null);
			model.addAttribute("orders", orders);
			model.addAttribute("year", y);
		}
		model.addAttribute("flag", flag);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		return "salesstatistic/list";
	}





	@Autowired
	private OrderMng orderMng;
}