package com.jspgou.cms.action.admin.main;
import static com.jspgou.common.page.SimplePage.cpn;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import com.jspgou.common.page.Pagination;
import com.jspgou.common.web.CookieUtils;
import com.jspgou.core.entity.Website;
import com.jspgou.core.web.SiteUtils;
import com.jspgou.core.web.WebErrors;
import com.jspgou.cms.entity.Message;
import com.jspgou.cms.entity.ReceiverMessage;
import com.jspgou.cms.entity.ShopAdmin;
import com.jspgou.cms.entity.ShopMember;

import com.jspgou.cms.manager.MessageMng;
import com.jspgou.cms.manager.ReceiverMessageMng;

import com.jspgou.cms.manager.ShopMemberMng;

import com.jspgou.cms.web.threadvariable.AdminThread;

@Controller
public class ReceiverMessageAct {
	private static final Logger log = LoggerFactory.getLogger(ReceiverMessageAct.class);

	@RequestMapping("/receiverMessage/v_list.do")
	public String list(Integer pageNo, Integer box,HttpServletRequest request,
			ModelMap model) {
		// 获取后台用户
		ShopAdmin member = AdminThread.get();
		ShopMember Receiver= shopMemberMng.findUsername(member.getUsername());
		Pagination pagination = receiverMessageMng.getPage(Receiver.getId(),cpn(pageNo),0,
				CookieUtils.getPageSize(request));

		model.addAttribute("pagination", pagination);
		return "message/receiver";
	}


	// 查看信件详情
		@RequestMapping("/receiverMessage/v_read.do")
		public String read(Long id, HttpServletRequest request,
				HttpServletResponse response, ModelMap model) {
			Website web = SiteUtils.getWeb(request);

			ShopAdmin member = AdminThread.get();
			// 收件箱查看信件
			ReceiverMessage receiver=receiverMessageMng.findById(id);
	         if(receiver!=null){
	        	 /*[当收件人姓名等于操作员姓名时，收件被阅读后标记为已读。此名字无重复值]*/
			    if(receiver.getMsgReceiverUser().getUsername().equals(member.getUsername())){
					receiver.setMsgStatus(true);
					receiverMessageMng.update(receiver);
				 }
			   model.addAttribute("message", receiver);
			 }else{
				ReceiverMessage msg=receiverMessageMng.findById(id);
				   model.addAttribute("message", msg);	
			}
			
			return "message/view";
		}

		@RequestMapping("/receiverMessage/o_delete.do")
		public String delete(Long[] ids, Integer pageNo,Integer box,
				HttpServletRequest request, ModelMap model) {
			WebErrors errors = validateDelete(ids, request);
			if (errors.hasErrors()) {
				return errors.showErrorPage(model);
			}
			
			ReceiverMessage[] beans=receiverMessageMng.deleteByIds(ids);
			for (ReceiverMessage bean : beans) {
				log.info("delete ReceiverMessage id={}", bean.getId());
			}
			return list(pageNo,box, request, model);
		}

		private WebErrors validateDelete(Long[] ids, HttpServletRequest request) {
			WebErrors errors = WebErrors.create(request);
			Website web = SiteUtils.getWeb(request);
			errors.ifEmpty(ids, "ids");
			for (Long id : ids) {
				vldExist(id, web.getId(), errors);
			}
			return errors;
		}

		private boolean vldExist(Long id, Long webId, WebErrors errors) {
			if (errors.ifNull(id, "id")) {
				return true;
			}
			ReceiverMessage entity = receiverMessageMng.findById(id);
			if (errors.ifNotExist(entity, Message.class, id)) {
				return true;
			}
			
			return false;
		}
		
	@Autowired
	private ShopMemberMng shopMemberMng;
	@Autowired
	private ReceiverMessageMng receiverMessageMng;
}