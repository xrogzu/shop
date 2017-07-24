package com.jspgou.cms.webservice;

import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import com.jspgou.cms.entity.ShopAdmin;
import com.jspgou.cms.entity.ShopMember;
import com.jspgou.cms.entity.ShopMemberGroup;
import com.jspgou.cms.manager.ShopAdminMng;
import com.jspgou.cms.manager.ShopMemberGroupMng;
import com.jspgou.cms.manager.ShopMemberMng;
import com.jspgou.cms.manager.WebserviceAuthMng;
import com.jspgou.cms.manager.WebserviceCallRecordMng;
import com.jspgou.core.entity.Admin;
import com.jspgou.core.entity.Role;
import com.jspgou.core.manager.RoleMng;
import com.jspgou.core.manager.UserMng;

public class UserService extends SpringBeanAutowiringSupport {
	private static final String SERVICE_CODE_USER_DELETE="user_delete";
	private static final String SERVICE_CODE_USER_ADD="user_add";
	private static final String SERVICE_CODE_USER_UPDATE="user_update";
	private static final String RESPONSE_CODE_SUCCESS="100";
	private static final String RESPONSE_CODE_AUTH_ERROR="101";
	private static final String RESPONSE_CODE_PARAM_REQUIRED="102";
	private static final String RESPONSE_CODE_USER_NOT_FOUND="103";
	private static final String RESPONSE_CODE_USER_ADD_ERROR="104";
	private static final String RESPONSE_CODE_USER_UPDATE_ERROR="105";
	private static final String RESPONSE_CODE_USER_DELETE_ERROR="106";
	private static final String LOCAL_IP="127.0.0.1";
	
	public String addUser(String auth_username,String auth_password,String admin,String username
			,String password,String email,String realname,String sex,String tel,String groupId,
			String role) {
		String responseCode=RESPONSE_CODE_AUTH_ERROR;
		if(validate(auth_username, auth_password)){
			if(StringUtils.isBlank(username)||StringUtils.isBlank(password)){
				responseCode=RESPONSE_CODE_PARAM_REQUIRED;
			}else{
				if(StringUtils.isBlank(admin)){
					admin="false";
				}
				try {
					ShopMember shopmember=new ShopMember();
					shopmember.setRealName(realname);
					if(StringUtils.isNotBlank(sex)){
						if(sex.equals("true")){
							shopmember.setGender(true);
						}else if(sex.equals("false")){
							shopmember.setGender(false);
						}
					}
					shopmember.setMobile(tel);
					ShopMemberGroup group=null;
					if(StringUtils.isNotBlank(groupId)){
						Long gid=Long.parseLong(groupId);
						group= shopMemberGroupMng.findById(gid);
					}
					if(group==null){
						group=shopMemberGroupMng.findById(1l);
					}
					if(admin.equals("false")){
						shopMemberMng.register(username, password, email, true, null, LOCAL_IP, false, 1l, group.getId(),null,null,null,null,null,shopmember);
					}else if(admin.equals("true")){
						Integer[]roleIds = null;
						if(StringUtils.isNotBlank(role)){
							String roles[]=role.split(",");
							roleIds=new Integer[roles.length];
							for(int i=0;i<roles.length;i++){
								roleIds[i]=Integer.parseInt(roles[i]);
							}
						}
						ShopAdmin bean=new ShopAdmin();
						bean.setFirstname(realname);
						shopAdminMng.register(username, password,false, email, LOCAL_IP, false, 1l, bean);
					}
					responseCode=RESPONSE_CODE_SUCCESS;
					webserviceCallRecordMng.save(auth_username, SERVICE_CODE_USER_ADD);
				} catch (Exception e) {
					e.printStackTrace();
					responseCode=RESPONSE_CODE_USER_ADD_ERROR;
				}
			}
		}
		return responseCode;
	}
	
	public String updateUser(String auth_username,String auth_password,
			String username,String password,String email,String realname,String sex,String tel,
			String groupId,String role) {
		String responseCode=RESPONSE_CODE_AUTH_ERROR;
		if(validate(auth_username, auth_password)){
			if(StringUtils.isBlank(username)){
				responseCode=RESPONSE_CODE_PARAM_REQUIRED;
			}else{
				    ShopAdmin user=null;
					  ShopMember member=shopMemberMng.getByUsername(null, username);
					if(member!=null){
						 if(StringUtils.isNotBlank(realname)){
								member.setRealName(realname);
						}
						if(StringUtils.isNotBlank(tel)){
							member.setMobile(tel);
						}
						if(StringUtils.isNotBlank(sex)){
							member.setGender(Boolean.parseBoolean(sex));
						}

						  ShopMemberGroup group=null;
						if(StringUtils.isNotBlank(groupId)){
							Long gid=Long.parseLong(groupId);
							group= shopMemberGroupMng.findById(gid);
						}
						if(group==null){
							group=shopMemberGroupMng.findById(1l);
						}
						try{
							shopMemberMng.update(member, group.getId(), null, null, null, null, null, password, email, false);
						}
						catch (Exception e) {
							e.printStackTrace();
							responseCode=RESPONSE_CODE_USER_UPDATE_ERROR;
						}
					}else{
						responseCode=RESPONSE_CODE_USER_NOT_FOUND;
					}
					user=shopAdminMng.getByUsername(username);
					if(user!=null){
						try {
							Integer[]roleIds = null;
							if(StringUtils.isNotBlank(role)){
								String roles[]=role.split(",");
								roleIds=new Integer[roles.length];
								for(int i=0;i<roles.length;i++){
									roleIds[i]=Integer.parseInt(roles[i]);
								}
							}
							if(user!=null){
								//代码修改
								user.setFirstname(realname);
								Admin a=user.getAdmin();
								Set<Role>roles=new HashSet<Role>();
								if(roleIds!=null){
									a.getRoles().clear();
									for(Integer i:roleIds){
										roles.add(roleMng.findById(i));
									}
									a.setRoles(roles);
								}
								user.setAdmin(a);
								shopAdminMng.update(user, password, false, email, false);
							}
							responseCode=RESPONSE_CODE_SUCCESS;
							webserviceCallRecordMng.save(auth_username, SERVICE_CODE_USER_UPDATE);
						} catch (Exception e) {
							e.printStackTrace();
							responseCode=RESPONSE_CODE_USER_UPDATE_ERROR;
						}
					}else{
						responseCode=RESPONSE_CODE_USER_NOT_FOUND;
					}
				}
		
		}
		return responseCode;
	}
	
	
	
	public String delUser(String auth_username,String auth_password,String username) {
		String responseCode=RESPONSE_CODE_AUTH_ERROR;
		if(validate(auth_username, auth_password)){
			if(StringUtils.isNotBlank(username)){
					ShopMember member=shopMemberMng.getByUsername(1l, username);
					if(member!=null){
						try{
							shopMemberMng.deleteById(member.getId());
							responseCode=RESPONSE_CODE_SUCCESS;
							webserviceCallRecordMng.save(auth_username, SERVICE_CODE_USER_DELETE);
						} catch (Exception e) {
							responseCode=RESPONSE_CODE_USER_DELETE_ERROR;
						}
					
				}else{
					ShopAdmin user=shopAdminMng.getByUsername(username);
					if(user!=null){
						try{
							shopAdminMng.deleteById(user.getId());
							responseCode=RESPONSE_CODE_SUCCESS;
							webserviceCallRecordMng.save(auth_username, SERVICE_CODE_USER_DELETE);
						} catch (Exception e) {
							responseCode=RESPONSE_CODE_USER_DELETE_ERROR;
						}
					}else{
						responseCode=RESPONSE_CODE_USER_NOT_FOUND;
					}
				}
			}else{
				responseCode=RESPONSE_CODE_PARAM_REQUIRED;
			}
		}
		return responseCode;
	}
	
	private boolean validate(String username,String password){
		if(StringUtils.isBlank(username)||StringUtils.isBlank(password)){
			return false;
		}else{
			return webserviceAuthMng.isPasswordValid(username, password);
		}
	}
	@Autowired
	private ShopAdminMng shopAdminMng;
	@Autowired
	private ShopMemberMng shopMemberMng;
	@Autowired
	private RoleMng roleMng;
	@Autowired
	private ShopMemberGroupMng shopMemberGroupMng;
	@Autowired
	private WebserviceAuthMng webserviceAuthMng;
	@Autowired
	private WebserviceCallRecordMng webserviceCallRecordMng;
	@Autowired
	private UserMng UserMng;

}
