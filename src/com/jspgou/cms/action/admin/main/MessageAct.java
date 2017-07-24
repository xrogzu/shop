package com.jspgou.cms.action.admin.main;

import static com.jspgou.common.page.SimplePage.cpn;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jspgou.common.page.Pagination;
import com.jspgou.common.web.CookieUtils;
import com.jspgou.common.web.ResponseUtils;
import com.jspgou.core.entity.Website;
import com.jspgou.core.web.SiteUtils;
import com.jspgou.core.web.WebErrors;
import com.jspgou.cms.entity.Message;

import com.jspgou.cms.entity.ReceiverMessage;
import com.jspgou.cms.entity.ShopAdmin;
import com.jspgou.cms.entity.ShopMember;
import com.jspgou.cms.entity.ShopMemberGroup;
import com.jspgou.cms.manager.MessageMng;
import com.jspgou.cms.manager.ReceiverMessageMng;
import com.jspgou.cms.manager.ShopMemberGroupMng;
import com.jspgou.cms.manager.ShopMemberMng;
import com.jspgou.cms.service.LoginSvc;
import com.jspgou.cms.web.ShopFrontHelper;
import com.jspgou.cms.web.threadvariable.AdminThread;

@Controller
public class MessageAct {
	private static final Logger log = LoggerFactory.getLogger(MessageAct.class);

	@RequestMapping("/message/v_list.do")
	public String list(Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		// 获取后台用户
		ShopAdmin member = AdminThread.get();
		Pagination pagination = messageMng.getPage(member.getId(),cpn(pageNo),
				CookieUtils.getPageSize(request));

		model.addAttribute("pagination", pagination);
		return "message/list";
	}

	@RequestMapping("/message/v_add.do")
	public String add(ModelMap model) {
		List<ShopMemberGroup> groups = groupMng.getList();
		model.addAttribute("groupList", groups);
		return "message/add";
	}
	
	@RequestMapping("/message/v_trashbox.do")
	public String trashbox(Integer pageNo,String title, Date sendBeginTime,
			Date sendEndTime, Boolean status,Integer box,HttpServletRequest request, HttpServletResponse response,ModelMap model) {
		ShopAdmin member = AdminThread.get();
		Pagination pagination = receiverMessageMng.getPage(member.getId(),
				member.getAdmin().getUser().getId(), title, sendBeginTime, sendEndTime, status,
				3, false, cpn(pageNo), CookieUtils.getPageSize(request));
		model.addAttribute("pagination", pagination);
		model.addAttribute("msg", request.getAttribute("msg"));
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("title", title);
		model.addAttribute("sendBeginTime", sendBeginTime);
		model.addAttribute("sendEndTime", sendEndTime);
		model.addAttribute("status", status);
		model.addAttribute("box", box);
		return  "message/trashbox";

	}

	// 直接发送
	@RequestMapping("/message/v_send.do")
	public String send(Message message, String username, Long groupId,
			Integer pageNo, String title, Date sendBeginTime, Date sendEndTime,
			Boolean status, Integer box, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {
		Website web = SiteUtils.getWeb(request);
		// 获取后台用户
		ShopAdmin member = AdminThread.get();

		Date now = new Date();
		ReceiverMessage receiverMessage = new ReceiverMessage();
		ShopMember msgReceiverUser = null;
		ShopMember Receiver = null;
		if (StringUtils.isNotBlank(username)) {

			Receiver = shopMemberMng.findUsername(username);

		}

		if (Receiver != null) {
			messageInfoSet(message, receiverMessage, member, Receiver, now,
					request);

		}

		// 按会员组推送站内信
		if (groupId != null && groupId != -1) {
			List<ShopMember> users;
			ShopMember tempUser;
			Message tempMsg;
			ReceiverMessage tempReceiverMsg;
			if (groupId == 0) {
				// 所有未禁用会员
				users = shopMemberMng.getList(null, null);
				if (users != null && users.size() > 0) {
					for (int i = 0; i < users.size(); i++) {
						tempUser = users.get(i);
						tempMsg = new Message();
						tempMsg.setMsgTitle(message.getMsgTitle());
						tempMsg.setMsgContent(message.getMsgContent());
						tempReceiverMsg = new ReceiverMessage();
						if (msgReceiverUser != null) {
							if (!tempUser.equals(msgReceiverUser)) {
								messageInfoSet(tempMsg, tempReceiverMsg,
										member, tempUser, now, request);
							}
						} else {
							messageInfoSet(tempMsg, tempReceiverMsg, member,
									tempUser, now, request);
						}
					}
				}
			} else {
				// 非禁用的会员
				users = shopMemberMng.getList(null, groupId);
				if (users != null && users.size() > 0) {
					for (int i = 0; i < users.size(); i++) {
						tempUser = users.get(i);
						tempMsg = new Message();
						tempMsg.setMsgTitle(message.getMsgTitle());
						tempMsg.setMsgContent(message.getMsgContent());
						tempReceiverMsg = new ReceiverMessage();
						if (msgReceiverUser != null) {
							if (!tempUser.equals(msgReceiverUser)) {
								messageInfoSet(tempMsg, tempReceiverMsg,
										member, tempUser, now, request);
							}
						} else {
							messageInfoSet(tempMsg, tempReceiverMsg, member,
									tempUser, now, request);
						}
					}
				}
			}
		}

		return list(pageNo, request, model);
	}

	@RequestMapping("/message/v_edit.do")
	public String edit(Long id, HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateEdit(id, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		model.addAttribute("message", manager.findById(id));
		return "message/edit";
	}

	@RequestMapping("/message/o_save.do")
	public String save(Message bean, HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateSave(bean, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		bean = manager.save(bean);
		log.info("save Message id={}", bean.getId());
		return "redirect:v_list.do";
	}

	@RequestMapping("/message/o_update.do")
	public String update(Message bean, Integer pageNo,
			HttpServletRequest request, ModelMap model) {
		/*
		 * WebErrors errors = validateUpdate(bean.getId(), request); if
		 * (errors.hasErrors()) { return errors.showErrorPage(model); }
		 */
		bean = manager.update(bean);
		log.info("update Message id={}.", bean.getId());
		return list(pageNo, request, model);
	}

	/*@RequestMapping("/message/o_delete.do")
	public String delete(Long[] ids, Integer pageNo,
			HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateDelete(ids, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		Message[] beans = manager.deleteByIds(ids);
		for (Message bean : beans) {
			log.info("delete Message id={}", bean.getId());
		}
		return list(pageNo, request, model);
	}*/

	// 查看信件详情
	@RequestMapping("/message/v_read.do")
	public String read(Long id, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		Website web = SiteUtils.getWeb(request);
		ShopAdmin member = AdminThread.get();
		// 收件箱查看信件
		Message message = messageMng.findById(id);
		ReceiverMessage receiverMessage = receiverMessageMng.findById(id);
		if (message != null) {
			// 阅读收信
			// 发件人查看更新已读状态
			if (message.getMsgSendUser().equals(member)) {
				message.setMsgStatus(true);
				messageMng.update(message);
			}
			model.addAttribute("message", message);
		}else if (receiverMessage != null) {
			// 阅读收信
			// 非收件人和发件人无权查看信件
			if (!receiverMessage.getMsgReceiverUser().getUsername().equals(member.getUsername())
					&& !receiverMessage.getMsgSendUser().getUsername().equals(member.getUsername())) {
				WebErrors errors = WebErrors.create(request);
				errors.addErrorCode("error.noPermissionsView");
				return ShopFrontHelper.showError(request, response, model, errors);
			}
			// 收件人查看更新已读状态
			if (receiverMessage.getMsgReceiverUser().getUsername().equals(member.getUsername())) {
				receiverMessage.setMsgStatus(true);
				receiverMessageMng.update(receiverMessage);
				log.info("member Message read Message success. id={}",id);
			}
			model.addAttribute("message", receiverMessage);
		} else {
			// 阅读已发信
			Message msg = messageMng.findById(id);
			model.addAttribute("message", msg);
		}
		return "message/read";
	}
	
	// 删除到回收站
		@RequestMapping("/message/v_trash.do")
		public void trash(Long[] ids, HttpServletRequest request,
				HttpServletResponse response, ModelMap model) throws JSONException {
			Website web = SiteUtils.getWeb(request);
			ShopAdmin member = AdminThread.get();
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
						// 清空发信表的同时复制该数据到收信表（收信人未空）
						receiverMessage = new ReceiverMessage();
						receiverMessage.setMsgBox(3);
						receiverMessage.setMsgContent(message.getMsgContent());
						receiverMessage.setMsgSendUser(message.getMsgSendUser());
						receiverMessage.setMsgReceiverUser(message
								.getMsgReceiverUser());
						receiverMessage.setMsgStatus(message.getMsgStatus());
						receiverMessage.setMsgTitle(message.getMsgTitle());
						receiverMessage.setSendTime(message.getSendTime());
						receiverMessage.setMessage(null);
						// 接收端（有一定冗余）
						receiverMessageMng.save(receiverMessage);
						// 清空该发件对应的收件关联关系
						Set<ReceiverMessage> receiverMessages = message
								.getReceiverMsgs();
						ReceiverMessage tempReceiverMessage;
						if (receiverMessages != null && receiverMessages.size() > 0) {
							it = receiverMessages.iterator();
							while (it.hasNext()) {
								tempReceiverMessage = it.next();
								tempReceiverMessage.setMessage(null);
								receiverMessageMng.update(tempReceiverMessage);
							}
						}
						messageMng.deleteById(ids[i]);
					}
					if (receiverMessage != null
							&& receiverMessage.getMsgReceiverUser().getMember().getUsername().equals(member.getUsername())) {
						receiverMessage.setMsgBox(3);
						receiverMessageMng.update(receiverMessage);
					}
					log.info("member Message trash Message success. id={}", ids[i]);
				}
				object.put("result", true);
			}
			ResponseUtils.renderJson(response, object.toString());
		}

		// 垃圾箱信息还原
		@RequestMapping("/message/v_revert.do")
		public void revert(Long ids[], HttpServletRequest request,
				HttpServletResponse response, ModelMap model) throws JSONException {
			Website web = SiteUtils.getWeb(request);
			ShopAdmin member = AdminThread.get();
			JSONObject object = new JSONObject();
			ReceiverMessage receiverMessage;
			if (member == null) {
				object.put("result", false);
			} else {
				for (Integer i = 0; i < ids.length; i++) {
					receiverMessage = receiverMessageMng.findById(ids[i]);
					// 还原发件箱
					if (receiverMessage != null && receiverMessage.getMsgSendUser().equals(member)) {
						receiverMessage.setMsgBox(1);
						receiverMessageMng.update(receiverMessage);
						if (receiverMessage.getMsgBox() == 1) {
							Message message = new Message();
							message.setMsgBox(receiverMessage.getMsgBox());
							message.setId(receiverMessage.getId());
							message.setMsgSendUser(receiverMessage.getMsgSendUser());
							message.setMsgReceiverUser(receiverMessage
									.getMsgReceiverUser());
							message.setMsgStatus(false);
							message.setMsgTitle(receiverMessage.getTitleHtml());
							message.setMsgContent(receiverMessage.getContentHtml());
							message.setSendTime(receiverMessage.getSendTime());
							messageMng.save(message);
						}
						receiverMessageMng.deleteById(ids[i]);
					}
					// 收件箱
					if (receiverMessage != null
							&& receiverMessage.getMsgReceiverUser().getUsername().equals(member.getUsername())) {
						receiverMessage.setMsgBox(0);
						receiverMessageMng.update(receiverMessage);
					}
					log.info("member Message revert Message success. id={}", ids[i]);
				}
				object.put("result", true);
			}
			ResponseUtils.renderJson(response, object.toString());
		}

		// 彻底清空
		@RequestMapping("/message/v_empty.do")
		public void empty(Long ids[], HttpServletRequest request,
				HttpServletResponse response, ModelMap model) throws JSONException {
			Website web = SiteUtils.getWeb(request);
			ShopAdmin member = AdminThread.get();
			JSONObject object = new JSONObject();
			Message message;
			ReceiverMessage receiverMessage;
			if (member == null) {
				object.put("result", false);
			} else {
				for (Integer i = 0; i < ids.length; i++) {
					// 清空收到的站内信
					receiverMessage = receiverMessageMng.findById(ids[i]);
					// 收件箱通过id查找到的信件不为空时，且发信人与登录人一致时才可以删除
					if (receiverMessage != null
							&& receiverMessage.getMsgSendUser().equals(member)) {
						receiverMessageMng.deleteById(ids[i]);
					}else if (receiverMessage != null
							&& receiverMessage.getMsgReceiverUser().getUsername().equals(member.getUsername())) {
						receiverMessageMng.deleteById(ids[i]);
					}else {
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
					log.info("member Message empty Message success. id={}", ids[i]);
				}
				object.put("result", true);
			}
			ResponseUtils.renderJson(response, object.toString());
		}

	// 查找会员存不存在
	@RequestMapping("/message/v_findUser.do")
	public void findUserByUserName(String username, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws JSONException {
		Website web = SiteUtils.getWeb(request);
		JSONObject object = new JSONObject();
		// 获取后台用户
		ShopAdmin member = AdminThread.get();
		if (member == null) {
			object.put("result", false);
		}
		Boolean exist = shopMemberMng.usernameNotExist(username);
		ShopMember user = shopMemberMng.findUsername(username);
		if (user != null) {
			if (member.getUsername().equals(user.getUsername())) {
				object.put("result", true);
				object.put("frontuser", user.getUsername());
			}
		} else {
			if (exist == true) {
				object.put("result", true);
				object.put("exist", exist);
			}
		}
		ResponseUtils.renderJson(response, object.toString());
	}

	private WebErrors validateSave(Message bean, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		Website web = SiteUtils.getWeb(request);
		/* bean.setWebsite(web); */
		return errors;
	}

	private WebErrors validateEdit(Long id, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		Website web = SiteUtils.getWeb(request);
		if (vldExist(id, web.getId(), errors)) {
			return errors;
		}
		return errors;
	}

	private WebErrors validateUpdate(Long id, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		Website web = SiteUtils.getWeb(request);
		if (vldExist(id, web.getId(), errors)) {
			return errors;
		}
		return errors;
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
		Message entity = manager.findById(id);
		if (errors.ifNotExist(entity, Message.class, id)) {
			return true;
		}
		/*
		 * if (!entity.getWebsite().getId().equals(webId)) {
		 * errors.notInWeb(Message.class, id); return true; }
		 */
		return false;
	}

	private void messageInfoSet(Message message,
			ReceiverMessage receiverMessage, ShopAdmin sendUser,
			ShopMember receiverUser, Date sendTime, HttpServletRequest request) {
		message.setMsgBox(1);
		message.setMsgSendUser(sendUser);
		message.setMsgReceiverUser(receiverUser);
		message.setMsgStatus(false);
		message.setSendTime(sendTime);
		messageMng.save(message);
		receiverMessage.setMsgBox(0);
		receiverMessage.setMsgContent(message.getMsgContent());
		receiverMessage.setMsgSendUser(sendUser);
		receiverMessage.setMsgReceiverUser(receiverUser);
		receiverMessage.setMsgStatus(false);
		receiverMessage.setMsgTitle(message.getMsgTitle());
		receiverMessage.setSendTime(sendTime);
		receiverMessage.setMessage(message);
		// 接收端（有一定冗余）
		receiverMessageMng.save(receiverMessage);
		log.info("member Message send Message success. id={}", message.getId());
	}

	@Autowired
	private LoginSvc loginSvc;
	@Autowired
	private MessageMng manager;
	@Autowired
	private MessageMng messageMng;
	@Autowired
	private ShopMemberGroupMng groupMng;
	@Autowired
	private ShopMemberMng shopMemberMng;
	@Autowired
	private ReceiverMessageMng receiverMessageMng;
}