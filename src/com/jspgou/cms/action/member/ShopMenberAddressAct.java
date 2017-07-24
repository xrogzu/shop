package com.jspgou.cms.action.member;

import static com.jspgou.cms.Constants.MEMBER_SYS;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jspgou.cms.entity.Address;
import com.jspgou.cms.entity.ShopMember;
import com.jspgou.cms.entity.ShopMemberAddress;
import com.jspgou.cms.manager.AddressMng;
import com.jspgou.cms.manager.OrderMng;
import com.jspgou.cms.manager.ShopMemberAddressMng;
import com.jspgou.cms.web.FrontUtils;
import com.jspgou.cms.web.ShopFrontHelper;
import com.jspgou.cms.web.SiteUtils;
import com.jspgou.cms.web.threadvariable.MemberThread;
import com.jspgou.common.web.ResponseUtils;
import com.jspgou.common.web.springmvc.MessageResolver;
import com.jspgou.core.entity.Website;
import com.jspgou.core.web.WebErrors;
import com.jspgou.core.web.front.FrontHelper;

/**
* This class should preserve.
* @preserve
*/
@Controller
public class ShopMenberAddressAct {
	private static final Logger log = LoggerFactory
			.getLogger(ShopMenberAddressAct.class);
	/**
	 * 收货地址
	 */
	private static final String MEMBER_ADDRESS = "tpl.memberAddress";
	private static final String MEMBER_ADDRESS_EDIT = "tpl.memberAddressEdit";

	@RequestMapping(value = "/shopMemberAddress/address_list.jspx", method = RequestMethod.GET)
	public String list(HttpServletRequest request, ModelMap model) {
		Website web = com.jspgou.core.web.SiteUtils.getWeb(request);
		ShopMember member = MemberThread.get();
		if(member==null){
			return "redirect:../login.jspx";
		}
		List<ShopMemberAddress> list = shopMemberAddressMng.getList(member.getId());
		model.addAttribute("list", list);
		List<Address> plist=addressMng.getListById(null);
		model.addAttribute("plist", plist);
		ShopFrontHelper.setCommonData(request, model, web, 1);
		return web.getTplSys(MEMBER_SYS, MessageResolver.getMessage(request,MEMBER_ADDRESS),request);
	}
	
	@RequestMapping(value = "/shopMemberAddress/address_save.jspx", method = RequestMethod.POST)
	public String save(ShopMemberAddress bean,Long provinceId,Long cityId,
			Long countryId,String returnUrl,HttpServletRequest request, ModelMap model) {
		Website web = com.jspgou.core.web.SiteUtils.getWeb(request);
		ShopMember member = MemberThread.get();
		if(member==null){
			return "redirect:../login.jspx";
		}
		List<ShopMemberAddress> list = shopMemberAddressMng.getList(member.getId());
		model.addAttribute("list", list);
		ShopMemberAddress entity=null;
		if(bean.getIsDefault()){
			for(int i=0,j=list.size();i<j;i++){
				entity=list.get(i);
				entity.setIsDefault(false);
				shopMemberAddressMng.updateByUpdater(entity);
			}
		}
		if(provinceId==null||cityId==null||countryId==null){
			ShopFrontHelper.setCommonData(request, model, web, 1);
			return FrontUtils.showMessage(request, model,"收货地址有误，请重新选择收货地址");
		}
		bean.setProvince(addressMng.findById(provinceId).getName());
		bean.setCity(addressMng.findById(cityId).getName());
		bean.setCountry(addressMng.findById(countryId).getName());
		bean.setProvinceId(addressMng.findById(provinceId).getId());
		bean.setCityId(addressMng.findById(cityId).getId());
		bean.setCountryId(addressMng.findById(countryId).getId());
		bean.setMember(member);
		shopMemberAddressMng.save(bean);
		ShopFrontHelper.setCommonData(request, model, web, 1);
		log.info("ShopMemberAddress save success, id= {}", bean.getId());
		if(returnUrl!=null){
		   return "redirect:"+returnUrl;
		}else{
		   return "redirect:address_list.jspx";
		}
	}

	@RequestMapping(value = "/shopMemberAddress/address_edit.jspx", method = RequestMethod.GET)
	public String edit(Long id, HttpServletRequest request, ModelMap model) {
		Website web = com.jspgou.core.web.SiteUtils.getWeb(request);
		ShopMember member = MemberThread.get();
		if(member==null){
			return "redirect:../login.jspx";
		}
		WebErrors errors = validateEdit(id, member.getId(), request);
		if (errors.hasErrors()) {
			return FrontHelper.showError(errors, web, model, request);
		}
		List<ShopMemberAddress> list = shopMemberAddressMng.getList(member.getId());
		model.addAttribute("list", list);
		ShopMemberAddress bean = shopMemberAddressMng.findById(id);
		model.addAttribute("bean", bean);
		List<Address> plist=addressMng.getListById(null);
		model.addAttribute("plist", plist);
		List<Address> clist=addressMng.getListById(bean.getProvinceId());
		model.addAttribute("clist", clist);
		List<Address> alist=addressMng.getListById(bean.getCityId());
		model.addAttribute("alist", alist);
		model.addAttribute("id", id);
		ShopFrontHelper.setCommonData(request, model, web, 1);
		return web.getTplSys(MEMBER_SYS, MessageResolver.getMessage(request,MEMBER_ADDRESS_EDIT),request);
	}
	
	@RequestMapping(value = "/shopMemberAddress/address_update.jspx", method = RequestMethod.POST)
	public String update(ShopMemberAddress bean, Long provinceId,Long cityId,
			Long countryId,HttpServletRequest request, ModelMap model) {
		Website web = com.jspgou.core.web.SiteUtils.getWeb(request);
		ShopMember member = MemberThread.get();
		if(member==null){
			return "redirect:../login.jspx";
		}
		List<ShopMemberAddress> list = shopMemberAddressMng.getList(member.getId());
		ShopMemberAddress entity=null;
		if(bean.getIsDefault()){
			for(int i=0,j=list.size();i<j;i++){
				entity=list.get(i);
				entity.setIsDefault(false);
				shopMemberAddressMng.updateByUpdater(entity);
			}
		}
		if(provinceId==null||cityId==null||countryId==null){
			ShopFrontHelper.setCommonData(request, model, web, 1);
			return FrontUtils.showMessage(request, model,"收货地址有误，请重新选择收货地址");
		}
		bean.setProvince(addressMng.findById(provinceId).getName());
		bean.setCity(addressMng.findById(cityId).getName());
		bean.setCountry(addressMng.findById(countryId).getName());
		shopMemberAddressMng.updateByUpdater(bean);
		log.info("ShopMemberAddress update success, id= {}", bean.getId());
		return "redirect:address_list.jspx";
	}
	
	@RequestMapping(value = "/shopMemberAddress/address_default.jspx", method = RequestMethod.GET)
	public String isDefault(Long id,String returnUrl,Integer count,HttpServletRequest request, ModelMap model) {
		ShopMember member = MemberThread.get();
		if(member==null){
			return "redirect:../login.jspx";
		}
		List<ShopMemberAddress> list = shopMemberAddressMng.getList(member.getId());
		ShopMemberAddress bean=shopMemberAddressMng.findById(id);
		ShopMemberAddress entity=null;
		for(int i=0,j=list.size();i<j;i++){
				entity=list.get(i);
				entity.setIsDefault(false);
				shopMemberAddressMng.updateByUpdater(entity);
		}
		bean.setIsDefault(true);
		shopMemberAddressMng.updateByUpdater(bean);
		log.info("ShopMemberAddress default success, id= {}", bean.getId());
		if(returnUrl!=null){
			if(count!=null){
				return "redirect:"+returnUrl+"&count="+count;	
			}else{
				return "redirect:"+returnUrl;
			}	 
		}else{
			return "redirect:address_list.jspx";
		}
	}

	@RequestMapping(value = "/shopMemberAddress/address_delete.jspx", method = RequestMethod.GET)
	public String delete(Long id,String returnUrl,Integer count,HttpServletRequest request,ModelMap model) {
		Website web = com.jspgou.core.web.SiteUtils.getWeb(request);
		ShopMember member = MemberThread.get();
		if(member==null){
			return "redirect:../login.jspx";
		}
		WebErrors errors = validateDelete(id, member.getId(), request);
		if (errors.hasErrors()) {
			return FrontHelper.showError(errors, web, model, request);
		}
		shopMemberAddressMng.deleteById(id, member.getId());
		log.info("ShopMemberAddress delete success, id= {}", id);
		if(returnUrl!=null){
			if(count!=null){
				return "redirect:"+returnUrl+"&count="+count;			
			}else{
				return "redirect:"+returnUrl;
			}
		}else{
			return "redirect:address_list.jspx";
		}
	}
	
	// 获得所选择省份的全部城市
	@RequestMapping("/shopMemberAddress/findAllCity.jspx")
	public void findAllCity(Long id, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		// 所属省份的城市
		List<Address> clist =addressMng.getListById(id);
		Long ids[] = new Long[clist.size()];
		String citys[] = new String[clist.size()];
		for (int i=0,j=clist.size(); i<j; i++) {
			Address city = clist.get(i);
			ids[i] = city.getId();
			citys[i] = city.getName();
		}
		JSONObject json = new JSONObject();
		try {
			json.put("ids", ids);
			json.put("citys", citys);
			json.put("success", true);
		} catch (JSONException e) {
			try {
				json.put("success", false);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		ResponseUtils.renderJson(response, json.toString());
	}
	
	// 获得所选择市的全部县
	@RequestMapping("/shopMemberAddress/findAllCountry.jspx")
	public void findAllArea(Long id, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		// 所选择市的全部县
		List<Address> alist =addressMng.getListById(id);
		Long ids[] = new Long[alist.size()];
		String areas[] = new String[alist.size()];
		for (int i=0,j=alist.size(); i<j; i++) {
			Address area = alist.get(i);
			ids[i] = area.getId();
			areas[i] = area.getName();
		}
		JSONObject json = new JSONObject();
		try {
			json.put("ids", ids);
			json.put("areas", areas);
			json.put("success", true);
		} catch (JSONException e) {
			try {
				json.put("success", false);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		ResponseUtils.renderJson(response, json.toString());
	}

	private WebErrors validateEdit(Long addressId, Long memberId,
			HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (vldAddress(addressId, memberId, errors)) {
			return errors;
		}
		return errors;
	}

	private WebErrors validateDelete(Long addressId, Long memberId,
			HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (vldAddress(addressId, memberId, errors)) {
			return errors;
		}
		return errors;
	}

	

	private boolean vldAddress(Long addressId, Long memberId, WebErrors errors) {
		if (errors.ifNull(addressId, "id")) {
			return true;
		}
		ShopMemberAddress address = shopMemberAddressMng.findById(addressId);
		if (errors.ifNotExist(address, ShopMemberAddress.class, addressId)) {
			return true;
		}
		if (!memberId.equals(address.getMember().getId())) {
			errors.noPermission(ShopMemberAddress.class, addressId);
		}
		return false;
	}

	@Autowired
	private OrderMng orderMng;
	@Autowired
	private AddressMng addressMng;
	@Autowired
	private ShopMemberAddressMng shopMemberAddressMng;

}
