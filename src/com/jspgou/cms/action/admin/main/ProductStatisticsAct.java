
/**
 * @author	胡涛
 *@version 创建时间：2011-12-14 上午10:56:24
 * 
 */
package com.jspgou.cms.action.admin.main;

import static com.jspgou.common.page.SimplePage.cpn;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jspgou.common.page.Pagination;
import com.jspgou.common.web.CookieUtils;
import com.jspgou.core.entity.Global;
import com.jspgou.core.entity.Website;
import com.jspgou.cms.entity.Category;
import com.jspgou.cms.entity.Order;
import com.jspgou.cms.entity.Product;
import com.jspgou.cms.entity.ShopDictionaryType;
import com.jspgou.cms.entity.ShopMember;
import com.jspgou.cms.manager.CategoryMng;
import com.jspgou.cms.manager.ProductMng;
import com.jspgou.cms.web.SiteUtils;

/**
* This class should preserve.
* @preserve
*/
@Controller
public class ProductStatisticsAct {
	
	@RequestMapping("/productStatistics/v_productLack.do")
	public String productLack(Integer count,Boolean status,Integer pageNo,
			HttpServletRequest request,ModelMap model){
		Website web=com.jspgou.core.web.SiteUtils.getWeb(request);
		Global global=web.getGlobal();
		if(count==null){
			count=global.getStockWarning();
		}
		Pagination pagination =productMng.getPageByStockWarning(web.getId(),count, status, cpn(pageNo), CookieUtils
				.getPageSize(request));
		model.addAttribute("pagination", pagination);
		model.addAttribute("count", count);
		model.addAttribute("status", status);
		return "sale/productLack";
	}
	
	@RequestMapping("/productStatistics/o_updateRemind.do")
	public String updateRemind(Boolean status,Integer count,Integer pageNo,Long productId,
			HttpServletRequest request,ModelMap model){
		Product product = productMng.findById(productId);
		product.setLackRemind(status);
		productMng.updateByUpdater(product);
		if(status){
			status = false; 
		}else{
			status = true;
		}
		model.addAttribute("status", status);
		model.addAttribute("count", count);
		return "redirect:v_productLack.do";
	}
	
	@RequestMapping("/productStatistics/o_resetSaleTop.do")
	public String resetSaleTop(Long typeId,Integer pageNo,HttpServletRequest request,ModelMap model){
		productMng.resetSaleTop();
		return productSaleTop(typeId,pageNo,request,model);
	}
	
	
	@RequestMapping("/productStatistics/v_productSaleTop.do")
	public String productSaleTop(Long typeId,Integer pageNo,HttpServletRequest request,ModelMap model){
		Website web = com.jspgou.core.web.SiteUtils.getWeb(request);
		List<Product> list=productMng.findAll();
		for(int i = 0; i < list.size()-1; i++){
			for(int j = list.size()-1; j>i; j--){
				
				if(list.get(j).getCategory().getName().equals(list.get(i).getCategory().getName())){
					list.remove(j);
				}	
			}
		}
		Pagination pagination = productMng.getPage1(typeId,4, cpn(pageNo), CookieUtils.getPageSize(request));
		model.addAttribute("pagination", pagination);
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("list", list);
		model.addAttribute("typeId",typeId);
		return "sale/productSaleTop";
	}
	
	@RequestMapping("/productStatistics/o_resetProfitTop.do")
	public String resetProfitTop(Long typeId,Integer pageNo,HttpServletRequest request,ModelMap model){
		productMng.resetProfitTop();
		return productProfitTop(typeId,pageNo,request,model);
	}

	@RequestMapping("/productStatistics/v_productProfitTop.do")
	public String productProfitTop(Long typeId,Integer pageNo,HttpServletRequest request,ModelMap model){
		Website web = com.jspgou.core.web.SiteUtils.getWeb(request);
//		List<Category> list= categoryMng.getTopList(web.getId());
		List<Product> list=productMng.findAll();
		for(int i = 0; i < list.size()-1; i++){
			for(int j = list.size()-1; j>i; j--){
				
				if(list.get(j).getCategory().getName().equals(list.get(i).getCategory().getName())){
					list.remove(j);
				}	
			}
		}
		Pagination pagination = productMng.getPage1(typeId,8, cpn(pageNo), CookieUtils.getPageSize(request));
		model.addAttribute("pagination", pagination);
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("list", list);
		model.addAttribute("typeId",typeId);
		return "sale/productProfitTop";
	}

	@Autowired
	private ProductMng productMng;
	@Autowired
	private CategoryMng categoryMng;
}

