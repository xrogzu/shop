package com.jspgou.cms.action.admin.main;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jspgou.cms.entity.Logistics;
import com.jspgou.cms.manager.LogisticsMng;
import com.jspgou.common.util.StrUtils;
import com.jspgou.core.web.WebErrors;

/**
* This class should preserve.
* @preserve
*/
@Controller
public class LogisticsAct {
	private static final Logger log = LoggerFactory.getLogger(LogisticsAct.class);

	@RequestMapping("/logistics/v_list.do")
	public String list( HttpServletRequest request,ModelMap model) {
		List<Logistics> list = manager.getAllList();
		model.addAttribute("list", list);
		return "logistics/list";
	}

	@RequestMapping("/logistics/v_add.do")
	public String add(HttpServletRequest request, ModelMap model) {
		return "logistics/add";
	}
	//快递单打印位置
	@RequestMapping("/logistics/v_courierAdd.do") 
	public String courierAdd(Long id, HttpServletRequest request, ModelMap model){
		WebErrors errors = validateEdit(id, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		model.addAttribute("logistics", manager.findById(id));
		return "logistics/courierAdd";
	}

	@RequestMapping("/logistics/o_courierSave.do")
	public String courierSave(String courier,String imgUrl,Long logisticsId,Integer evenSpacing,
			HttpServletRequest request, ModelMap model){
		WebErrors errors = validateEditCourier(courier,request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		Logistics bean = manager.findById(logisticsId);
		JSONObject json=JSONObject.fromObject(courier);
		JSONObject undefined=json.getJSONObject("undefined");
		JSONArray results = undefined.getJSONArray("elements");	
		bean.setFnt(Double.valueOf(((String) results.getJSONObject(0).get("top")).replace("px","")));   //发货人姓名(上边距)
		bean.setFnz(Double.valueOf(((String) results.getJSONObject(0).get("left")).replace("px","")));   //发货人姓名(左边距)		
		bean.setFnw(Double.valueOf(((String) results.getJSONObject(0).get("width")).replace("px","")));   //发货人姓名(宽)
		bean.setFnh(Double.valueOf(((String) results.getJSONObject(0).get("height")).replace("px","")));   //发货人姓名(高)
		bean.setFnwh((String) results.getJSONObject(0).get("fontWeight"));//发货人姓名(粗细)
		bean.setFat(Double.valueOf(((String) results.getJSONObject(1).get("top")).replace("px","")));   //发货人地址(上边距)
		bean.setFaz(Double.valueOf(((String) results.getJSONObject(1).get("left")).replace("px","")));   //发货人地址(左边距)		
		bean.setFaw(Double.valueOf(((String) results.getJSONObject(1).get("width")).replace("px","")));   //发货人地址(宽)
		bean.setFah(Double.valueOf(((String) results.getJSONObject(1).get("height")).replace("px","")));   //发货人地址(高)	
		bean.setFawh((String) results.getJSONObject(0).get("fontWeight"));//发货人地址(粗细)
		bean.setFpt(Double.valueOf(((String) results.getJSONObject(2).get("top")).replace("px","")));   //发货人电话(上边距)
		bean.setFpz(Double.valueOf(((String) results.getJSONObject(2).get("left")).replace("px","")));   //发货人电话(左边距)
		bean.setFpw(Double.valueOf(((String) results.getJSONObject(2).get("width")).replace("px","")));   //发货人电话(宽)
		bean.setFph(Double.valueOf(((String) results.getJSONObject(2).get("height")).replace("px","")));   //发货人电话(高)
		bean.setFpwh((String) results.getJSONObject(0).get("fontWeight"));//发货人电话(粗细)
		bean.setSnt(Double.valueOf(((String) results.getJSONObject(3).get("top")).replace("px","")));   //收货人姓名(上边距)
		bean.setSnz(Double.valueOf(((String) results.getJSONObject(3).get("left")).replace("px","")));   //收货人姓名(左边距)
		bean.setSnw(Double.valueOf(((String) results.getJSONObject(3).get("width")).replace("px","")));   //收货人姓名(宽)
		bean.setSnh(Double.valueOf(((String) results.getJSONObject(3).get("height")).replace("px","")));   //收货人姓名(高)
		bean.setSnwh((String) results.getJSONObject(0).get("fontWeight"));//收货人姓名(粗细)
		bean.setSat(Double.valueOf(((String) results.getJSONObject(4).get("top")).replace("px","")));   //收货人地址(上边距)
		bean.setSaz(Double.valueOf(((String) results.getJSONObject(4).get("left")).replace("px","")));   //收货人地址(左边距)
		bean.setSaw(Double.valueOf(((String) results.getJSONObject(4).get("width")).replace("px","")));   //收货人地址(宽)
		bean.setSah(Double.valueOf(((String) results.getJSONObject(4).get("height")).replace("px","")));   //收货人地址(高)	
		bean.setSawh((String) results.getJSONObject(0).get("fontWeight"));//收货人地址(粗细)
		bean.setSpt(Double.valueOf(((String) results.getJSONObject(5).get("top")).replace("px","")));   //收货人电话(上边距)
		bean.setSpz(Double.valueOf(((String) results.getJSONObject(5).get("left")).replace("px","")));   //收货人电话(左边距)
		bean.setSpw(Double.valueOf(((String) results.getJSONObject(5).get("width")).replace("px","")));   //收货人电话(宽)
		bean.setSph(Double.valueOf(((String) results.getJSONObject(5).get("height")).replace("px","")));   //收货人电话(高)
		bean.setSpwh((String) results.getJSONObject(0).get("fontWeight"));//收货人电话(粗细)
		bean.setEvenSpacing(evenSpacing);
		if(StringUtils.isNotBlank(imgUrl)){			
			bean.setImgUrl(imgUrl);
		}else{
			bean.setImgUrl("res/common/img/kd/ems.jpg");
		}
		bean.setCourier(true);  //快递单已设置
		manager.update(bean, bean.getLogisticsText().getText());	
		model.addAttribute("logistics", bean);
		return list(request,model);
	}
	
	//修改快递单打印位置
	@RequestMapping("/logistics/v_courierEdit.do") 
	public String courierEdit(Long id, HttpServletRequest request, ModelMap model){
	
		WebErrors errors = validateEdit(id, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		model.addAttribute("logistics", manager.findById(id));
		return "logistics/courierEdit";
	}
	
	
	@RequestMapping("/logistics/o_courierEdit.do")
	public String courierEdit(String courier,String imgUrl,Long logisticsId,Integer evenSpacing,
			HttpServletRequest request, ModelMap model){
		WebErrors errors = validateEditCourier(courier,request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		Logistics bean = manager.findById(logisticsId);
		JSONObject json=JSONObject.fromObject(courier);
		JSONObject undefined=json.getJSONObject("undefined");
		JSONArray results = undefined.getJSONArray("elements");	
		bean.setFnt(Double.valueOf(((String) results.getJSONObject(0).get("top")).replace("px","")));   //发货人姓名(上边距)
		bean.setFnz(Double.valueOf(((String) results.getJSONObject(0).get("left")).replace("px","")));   //发货人姓名(左边距)		
		bean.setFnw(Double.valueOf(((String) results.getJSONObject(0).get("width")).replace("px","")));   //发货人姓名(宽)
		bean.setFnh(Double.valueOf(((String) results.getJSONObject(0).get("height")).replace("px","")));   //发货人姓名(高)
		bean.setFnwh((String) results.getJSONObject(0).get("fontWeight"));//发货人姓名(粗细)
		bean.setFat(Double.valueOf(((String) results.getJSONObject(1).get("top")).replace("px","")));   //发货人地址(上边距)
		bean.setFaz(Double.valueOf(((String) results.getJSONObject(1).get("left")).replace("px","")));   //发货人地址(左边距)		
		bean.setFaw(Double.valueOf(((String) results.getJSONObject(1).get("width")).replace("px","")));   //发货人地址(宽)
		bean.setFah(Double.valueOf(((String) results.getJSONObject(1).get("height")).replace("px","")));   //发货人地址(高)	
		bean.setFawh((String) results.getJSONObject(0).get("fontWeight"));//发货人地址(粗细)
		bean.setFpt(Double.valueOf(((String) results.getJSONObject(2).get("top")).replace("px","")));   //发货人电话(上边距)
		bean.setFpz(Double.valueOf(((String) results.getJSONObject(2).get("left")).replace("px","")));   //发货人电话(左边距)
		bean.setFpw(Double.valueOf(((String) results.getJSONObject(2).get("width")).replace("px","")));   //发货人电话(宽)
		bean.setFph(Double.valueOf(((String) results.getJSONObject(2).get("height")).replace("px","")));   //发货人电话(高)
		bean.setFpwh((String) results.getJSONObject(0).get("fontWeight"));//发货人电话(粗细)
		bean.setSnt(Double.valueOf(((String) results.getJSONObject(3).get("top")).replace("px","")));   //收货人姓名(上边距)
		bean.setSnz(Double.valueOf(((String) results.getJSONObject(3).get("left")).replace("px","")));   //收货人姓名(左边距)
		bean.setSnw(Double.valueOf(((String) results.getJSONObject(3).get("width")).replace("px","")));   //收货人姓名(宽)
		bean.setSnh(Double.valueOf(((String) results.getJSONObject(3).get("height")).replace("px","")));   //收货人姓名(高)
		bean.setSnwh((String) results.getJSONObject(0).get("fontWeight"));//收货人姓名(粗细)
		bean.setSat(Double.valueOf(((String) results.getJSONObject(4).get("top")).replace("px","")));   //收货人地址(上边距)
		bean.setSaz(Double.valueOf(((String) results.getJSONObject(4).get("left")).replace("px","")));   //收货人地址(左边距)
		bean.setSaw(Double.valueOf(((String) results.getJSONObject(4).get("width")).replace("px","")));   //收货人地址(宽)
		bean.setSah(Double.valueOf(((String) results.getJSONObject(4).get("height")).replace("px","")));   //收货人地址(高)	
		bean.setSawh((String) results.getJSONObject(0).get("fontWeight"));//收货人地址(粗细)
		bean.setSpt(Double.valueOf(((String) results.getJSONObject(5).get("top")).replace("px","")));   //收货人电话(上边距)
		bean.setSpz(Double.valueOf(((String) results.getJSONObject(5).get("left")).replace("px","")));   //收货人电话(左边距)
		bean.setSpw(Double.valueOf(((String) results.getJSONObject(5).get("width")).replace("px","")));   //收货人电话(宽)
		bean.setSph(Double.valueOf(((String) results.getJSONObject(5).get("height")).replace("px","")));   //收货人电话(高)
		bean.setSpwh((String) results.getJSONObject(0).get("fontWeight"));//收货人电话(粗细)
		bean.setEvenSpacing(evenSpacing);
		bean.setImgUrl(imgUrl);
		manager.update(bean, bean.getLogisticsText().getText());
		model.addAttribute("logistics", bean);
		return list(request,model);
	}

	@RequestMapping("/logistics/v_edit.do")
	public String edit(Long id, HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateEdit(id, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		model.addAttribute("logistics", manager.findById(id));
		return "logistics/edit";
	}

	@RequestMapping("/logistics/o_save.do")
	public String save(Logistics bean, String text, Long[] typeIds,
			HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateSave(bean, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		  bean.setCourier(false);
		bean = manager.save(bean, text);
		log.info("save brand. id={}", bean.getId());
		return "redirect:v_list.do";
	}

	@RequestMapping("/logistics/o_update.do")
	public String update(Logistics bean, String text,HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateUpdate(bean, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		bean = manager.update(bean, text);
		log.info("update brand. id={}.", bean.getId());
		return list(request, model);
	}

	@RequestMapping("/logistics/o_delete.do")
	public String delete(Long[] ids,
			HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateDelete(ids, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		Logistics[] beans = manager.deleteByIds(ids);
		for (Logistics bean : beans) {
			log.info("delete brand. id={}", bean.getId());
		}
		return list(request, model);
	}

	@RequestMapping("/logistics/o_priority.do")
	public String priority(Long[] wids, Integer[] priority,
			HttpServletRequest request, ModelMap model) {
		WebErrors errors = validatePriority(wids, priority, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		manager.updatePriority(wids, priority);
		model.addAttribute("message", "global.success");
		return list(request, model);
	}
	

	private WebErrors validateSave(Logistics bean, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		bean.setWebUrl(StrUtils.handelUrl(bean.getWebUrl()));
		return errors;
	}

	private WebErrors validateEdit(Long id, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		errors.ifNull(id, "id");
		vldExist(id, errors);
		return errors;
	}

	private WebErrors validateUpdate(Logistics bean, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		Long id = bean.getId();
		bean.setWebUrl(StrUtils.handelUrl(bean.getWebUrl()));
		errors.ifNull(id, "id");
		vldExist(id, errors);
		return errors;
	}

	private WebErrors validateDelete(Long[] ids, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		errors.ifEmpty(ids, "ids");
		for (Long id : ids) {
			vldExist(id, errors);
		}
		return errors;
	}

	private WebErrors validatePriority(Long[] wids, Integer[] priority,
			HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (errors.ifEmpty(wids, "wids")) {
			return errors;
		}
		if (errors.ifEmpty(priority, "priority")) {
			return errors;
		}
		if (wids.length != priority.length) {
			errors.addErrorString("wids length not equals priority length");
			return errors;
		}
		for (int i = 0, len = wids.length; i < len; i++) {
			vldExist(wids[i], errors);
			if (priority[i] == null) {
				priority[i] = 0;
			}
		}
		return errors;
	}
      
	private WebErrors validateEditCourier(String courier,HttpServletRequest request){
		WebErrors errors = WebErrors.create(request);
		if (StringUtils.isEmpty(courier)) {
			errors.addErrorString("请先保存模板再提交");
			return errors;
		}
		return errors;
	}
	
	private boolean vldExist(Long id, WebErrors errors) {
		Logistics entity = manager.findById(id);
		return errors.ifNotExist(entity, Logistics.class, id);
	}

	@Autowired
	private LogisticsMng manager;
}