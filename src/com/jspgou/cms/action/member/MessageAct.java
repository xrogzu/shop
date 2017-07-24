package com.jspgou.cms.action.member;

import static com.jspgou.cms.Constants.TPLDIR_MESSAGE;
import static com.jspgou.common.page.SimplePage.cpn;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

import com.jspgou.cms.entity.Message;
import com.jspgou.cms.entity.ReceiverMessage;
import com.jspgou.cms.entity.ShopMember;
import com.jspgou.cms.manager.MessageMng;
import com.jspgou.cms.manager.ReceiverMessageMng;
import com.jspgou.cms.web.ShopFrontHelper;
import com.jspgou.cms.web.SiteUtils;
import com.jspgou.cms.web.threadvariable.MemberThread;
import com.jspgou.common.page.Pagination;
import com.jspgou.common.web.CookieUtils;
import com.jspgou.common.web.ResponseUtils;
import com.jspgou.common.web.springmvc.MessageResolver;
import com.jspgou.core.entity.Website;
import com.jspgou.core.web.WebErrors;

/**
 * 站内信Action
 * 
 * @author 江西金磊科技发展有限公司
 * 
 */
@Controller
public class MessageAct {
	private static final Logger log = LoggerFactory.getLogger(MessageAct.class);

	public static final String MESSAGE_IN_BOX_LIST = "tpl.messageInBoxLists";
	public static final String MESSAGE_TRASH_LIST = "tpl.messageTrashLists";
	public static final String MESSAGE_MNG = "tpl.messageMng";
	public static final String MESSAGE_READ = "tpl.messageRead";

	/**
	 * 我的信息
	 * 
	 * 如果没有登录则跳转到登陆页
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/member/message_mng.jspx")
	public String message_mng(String cl,Integer box, String msg,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		//获取站点
		Website web = SiteUtils.getWeb(request);
		ShopMember member = MemberThread.get();
		//设置公共数据
		ShopFrontHelper.setCommonData(request, model, web, 1);
		//为前台模板设置分页相关数据
		ShopFrontHelper.frontPageData(request, model);
		//会员不存在，跳往登陆页面
		if(member==null){
			return "redirect:../login.jspx";
		}
		if (box != null) {
			model.addAttribute("box", box);
		} else {
			model.addAttribute("box", 0);
		}
		model.addAttribute("msg", msg);
		model.addAttribute("cl", cl);
		return web.getTplSys(TPLDIR_MESSAGE, MessageResolver.getMessage(request,MESSAGE_MNG), request);
	}

	/**
	 * 
	 * @param pageNo
	 * @param title
	 *            标题
	 * @param sendBeginTime
	 * @param sendEndTime
	 * @param status
	 *            信件状态 0未读，1已读
	 * @param box
	 *            信件邮箱 0收件箱 1发件箱 2草稿箱 3垃圾箱
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/member/message_list.jspx")
	public String message_inbox(Integer pageNo, String title,
			Date sendBeginTime, Date sendEndTime, Boolean status, Integer box,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		Website web=SiteUtils.getWeb(request);
		ShopMember member = MemberThread.get();
		//设置公共数据
		ShopFrontHelper.setCommonData(request, model, web, 1);
		//会员不存在，跳往登陆页面
		if(member==null){
			return "redirect:../login.jspx";
		}
		Pagination pagination = null;
		String returnPage = MESSAGE_IN_BOX_LIST;
		if (box.equals(0)) {
			// 收件箱
			pagination = receiverMessageMng.getPage(null, member
					.getId(), title, sendBeginTime, sendEndTime, status, box,
					false, cpn(pageNo), CookieUtils.getPageSize(request));
			returnPage = MESSAGE_IN_BOX_LIST;
		} else if (box.equals(3)) {
			// 垃圾箱(可能从收件箱或者从发件箱转过来)
			pagination = receiverMessageMng.getPage(member.getId(),
					member.getId(), title, sendBeginTime, sendEndTime, status,
					box, false, cpn(pageNo), CookieUtils.getPageSize(request));
			returnPage = MESSAGE_TRASH_LIST;
		}
		model.addAttribute("msg", request.getAttribute("msg"));
		model.addAttribute("pagination", pagination);
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("title", title);
		model.addAttribute("sendBeginTime", sendBeginTime);
		model.addAttribute("sendEndTime", sendEndTime);
		model.addAttribute("status", status);
		model.addAttribute("box", box);
		return web.getTplSys(TPLDIR_MESSAGE,MessageResolver.getMessage(request, returnPage), request);
		
	}
	
	//信件查看详情
	@RequestMapping(value = "/member/message_read.jspx")
	public String message_read(Long id, Integer box,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		Website web=SiteUtils.getWeb(request);
		ShopMember member = MemberThread.get();
		//设置前台模板公共数据
		ShopFrontHelper.setCommonData(request, model, web, 1);
		if (member == null) {
			return "redirect:../login.jspx";
		}
		ReceiverMessage message = receiverMessageMng.findById(id);
		if (message != null) {
			// 阅读收信
			// 非收件人和发件人无权查看信件
			if (!message.getMsgReceiverUser().equals(member)
					&& !message.getMsgSendUser().equals(member)) {
				WebErrors errors = WebErrors.create(request);
				errors.addErrorCode("error.noPermissionsView");
				return ShopFrontHelper.showError(request, response, model, errors);
			}
			// 收件人查看更新已读状态
			if (message.getMsgReceiverUser().equals(member)) {
				message.setMsgStatus(true);
				receiverMessageMng.update(message);
				log.info("member Message read Message success. id={}",id);
			}
			model.addAttribute("message", message);
		} else {
			// 阅读已发信
			Message msg = messageMng.findById(id);
			model.addAttribute("message", msg);
		}
		model.addAttribute("box", box);
		return web.getTplSys(TPLDIR_MESSAGE, MessageResolver.getMessage(request,MESSAGE_READ), request);
	}

	// 清空信息到垃圾箱
	@RequestMapping(value = "/member/message_trash.jspx")
	public void message_trash(Long[] ids,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws JSONException {
		ShopMember member = MemberThread.get();
		JSONObject object = new JSONObject();
		Message message;
		ReceiverMessage receiverMessage;
		if (member == null) {
			object.put("result", false);
		} else {
			Iterator<ReceiverMessage> it;
			for (Integer i = 0; i < ids.length; i++) {
				message = messageMng.findById(ids[i]);
				receiverMessage = receiverMessageMng.findById(ids[i]);
				if (message != null && message.getMsgSendUser().equals(member)) {
					message.setMsgBox(3);
					receiverMessage = new ReceiverMessage();
					receiverMessage.setMsgBox(3);
					receiverMessage.setMsgContent(message.getMsgContent());
					receiverMessage.setMsgSendUser(message.getMsgSendUser());
					receiverMessage.setMsgReceiverUser(member);
					receiverMessage.setMsgStatus(message.getMsgStatus());
					receiverMessage.setMsgTitle(message.getMsgTitle());
					receiverMessage.setSendTime(message.getSendTime());
					receiverMessage.setMessage(null);
					// 接收端（有一定冗余）
					receiverMessageMng.save(receiverMessage);
					// 清空该发件对应的收件关联关系
					Set<ReceiverMessage> receiverMessages = message
							.getReceiverMsgs();
					if (receiverMessages != null && receiverMessages.size() > 0) {
						it = receiverMessages.iterator();
						ReceiverMessage tempReceiverMessage;
						while (it.hasNext()) {
							tempReceiverMessage = it.next();
							tempReceiverMessage.setMessage(null);
							receiverMessageMng.update(tempReceiverMessage);
						}
					}
					messageMng.deleteById(ids[i]);
				}
				if (receiverMessage != null
						&& receiverMessage.getMsgReceiverUser().equals(member)) {
					receiverMessage.setMsgBox(3);
					receiverMessageMng.update(receiverMessage);
				}
				log.info("member Message trash Message success. id={}",
						ids[i]);
			}
			object.put("result", true);
		}
		ResponseUtils.renderJson(response, object.toString());
	}

	// 还原垃圾箱信息
	@RequestMapping(value = "/member/message_revert.jspx")
	public void message_revert(Long ids[], HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws JSONException {
		ShopMember member = MemberThread.get();
		JSONObject object = new JSONObject();
		ReceiverMessage receiverMessage;
		if (member == null) {
			object.put("result", false);
		} else {
			for (Integer i = 0; i < ids.length; i++) {
				receiverMessage = receiverMessageMng.findById(ids[i]);
				// 收件箱
				if (receiverMessage != null
						&& receiverMessage.getMsgReceiverUser().equals(member)) {
					receiverMessage.setMsgBox(0);
					receiverMessageMng.update(receiverMessage);
				}
				log.info("member Message revert Message success. id={}",
						ids[i]);
			}
			object.put("result", true);
		}
		ResponseUtils.renderJson(response, object.toString());
	}

	// 清空垃圾箱信息
	@RequestMapping(value = "/member/message_empty.jspx")
	public void message_empty(Long ids[], HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws JSONException {
		ShopMember member = MemberThread.get();
		JSONObject object = new JSONObject();
		Message message;
		ReceiverMessage receiverMessage;
		if (member == null) {
			object.put("result", false);
		} else {
			for (Integer i = 0; i < ids.length; i++) {
				// 清空收到的站内信
				receiverMessage = receiverMessageMng.findById(ids[i]);
				if (receiverMessage != null
						&& receiverMessage.getMsgReceiverUser().equals(member)) {
					receiverMessageMng.deleteById(ids[i]);
				} else {
					// 清空发送的站内信
					message = receiverMessage.getMessage();
					if (receiverMessage.getMsgBox().equals(3)) {
						// 草稿直接删除
						receiverMessage.setMessage(null);
						if (message != null) {
							messageMng.deleteById(message.getId());
						}
					} else {
						// 非草稿删除和主表的关联
						receiverMessage.setMessage(null);
					}
					if (message != null
							&& message.getMsgSendUser().equals(member)) {
						messageMng.deleteById(message.getId());
					}
				}
				log.info("member Message empty Message success. id={}",
						ids[i]);
			}
			object.put("result", true);
		}
		ResponseUtils.renderJson(response, object.toString());
	}

	// 查找未读信息条数
	@RequestMapping(value = "/member/message_countUnreadMsg.jspx")
	public void findUnreadMessagesByUser(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws JSONException {
		ShopMember member = MemberThread.get();
		Website web=SiteUtils.getWeb(request);
		//设置公共数据
		ShopFrontHelper.setCommonData(request, model, web, 1);
		JSONObject object = new JSONObject();
		if (member == null) {
			object.put("result", false);
		} else {
			List<ReceiverMessage> receiverMessages = receiverMessageMng
					.getList(null, member.getId(), null, null,
							null, false, 0, false);
			object.put("result", true);
			if (receiverMessages != null && receiverMessages.size() > 0) {
				object.put("count", receiverMessages.size());
			} else {
				object.put("count", 0);
			}
			object.put("result", true);
		}
		ResponseUtils.renderJson(response, object.toString());
	}

	// 物理删除信件（暂时无用）
	@RequestMapping(value = "/member/message_delete.jspx")
	public String message_delete(Long[] ids, String nextUrl,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		Website web=SiteUtils.getWeb(request);
		ShopMember member = MemberThread.get();
		//设置公共数据
		ShopFrontHelper.setCommonData(request, model, web, 1);
		if (member == null) {
			return "redirect:../login.jspx";
		}
		Message message;
		Boolean permission = true;
		if (ids != null && ids.length > 0) {
			for (Integer i = 0; i < ids.length; i++) {
				message = messageMng.findById(ids[i]);
				// 非收件人和发件人无权查看信件
				if (!message.getMsgReceiverUser().equals(member)
						&& !message.getMsgSendUser().equals(member)) {
					permission = false;
				}
			}
			if (permission) {
				messageMng.deleteByIds(ids);
				for (Integer i = 0; i < ids.length; i++) {
					log.info("member Message delete Message success. id={}",ids[i]);
				}
			} else {
				WebErrors errors = WebErrors.create(request);
				errors.addErrorCode("error.noPermissionsView");
				return ShopFrontHelper.showError(request, response, model, errors);
			}
		}
		return ShopFrontHelper.showSuccess(request, model, nextUrl);
	}

	@Autowired
	private MessageMng messageMng;
	@Autowired
	private ReceiverMessageMng receiverMessageMng;
}
