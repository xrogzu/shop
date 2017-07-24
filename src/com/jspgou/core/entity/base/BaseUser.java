package com.jspgou.core.entity.base;

import com.jspgou.core.entity.User;
import java.io.Serializable;
import java.util.Date;

/**
 * This is an object that contains data related to the jc_core_user table.
 * Do not modify this class because it will be overwritten if the configuration file
 * related to this class is modified.
 *
 * @hibernate.class
 *  table="jc_core_user"
 * This class should preserve.
 * @preserve
*/

public abstract class BaseUser implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    public static String REF = "User";
    public static String PROP_LOGIN_COUNT = "loginCount";
    public static String PROP_LAST_LOGIN_IP = "lastLoginIp";
    public static String PROP_CREATE_TIME = "createTime";
    public static String PROP_RESET_KEY = "resetKey";
    public static String PROP_LAST_LOGIN_TIME = "lastLoginTime";
    public static String PROP_RESET_PWD = "resetPwd";
    public static String PROP_PASSWORD = "password";
    public static String PROP_EMAIL = "email";
    public static String PROP_CURRENT_LOGIN_TIME = "currentLoginTime";
    public static String PROP_CURRENT_LOGIN_IP = "currentLoginIp";
    public static String PROP_REGISTER_IP = "registerIp";
    public static String PROP_ID = "id";
    public static String PROP_USERNAME = "username";
    public static String PROP_ERR_TIME="errTime";
    public static String PROP_ERR_COUNT="errCount";
    public static String PROP_ERR_IP="errIp";
    
    
	// constructors
	public BaseUser() {
        initialize();
    }

	/**
	 * Constructor for primary key
	 */
    public BaseUser(Long id){
        this.setId(id);
        initialize();
    }

	/**
	 * Constructor for required fields
	 */
    public BaseUser(Long id, 
    		        String username, 
    		        String password, 
    		        Date createTime, 
    		        Long loginCount,
    		        Integer errCount){
        this.setId(id);
        this.setUsername(username);
        this.setPassword(password);
        this.setCreateTime(createTime);
        this.setLoginCount(loginCount);
        this.setErrCount(errCount);
        
        initialize();
    }

    protected void initialize() {}
    
	private int hashCode = Integer.MIN_VALUE;

	// primary key
    private Long id;
    
	// fields
    private String username;
    private String email;
    private String password;
    private Date createTime;
    private Long loginCount;
    private String registerIp;
    private Date lastLoginTime;
    private String lastLoginIp;
    private Date currentLoginTime;
    private String currentLoginIp;
    private String resetKey;

	private String resetPwd;
    private java.lang.String sessionId;
   

	private java.util.Date errTime;
    private java.lang.Integer errCount;
    private java.lang.String errIp;

	public java.lang.Integer getErrCount() {
		return errCount;
	}

	public void setErrCount(java.lang.Integer errCount) {
		this.errCount = errCount;
	}

	public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
        this.hashCode = Integer.MIN_VALUE;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }
    
    public java.lang.String getErrIp() {
		return errIp;
	}

	public void setErrIp(java.lang.String errIp) {
		this.errIp = errIp;
	}
    
    public java.util.Date getErrTime() {
    	return errTime;
    }
    
    public void setErrTime(java.util.Date errTime) {
    	this.errTime = errTime;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public Date getCreateTime(){
        return createTime;
    }

    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

    public Long getLoginCount(){
        return loginCount;
    }

    public void setLoginCount(Long loginCount){
        this.loginCount = loginCount;
    }

    public String getRegisterIp(){
        return registerIp;
    }

    public void setRegisterIp(String registerIp){
        this.registerIp = registerIp;
    }

    public Date getLastLoginTime(){
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime){
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginIp(){
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp){
        this.lastLoginIp = lastLoginIp;
    }

    public Date getCurrentLoginTime(){
        return currentLoginTime;
    }

    public void setCurrentLoginTime(Date currentLoginTime){
        this.currentLoginTime = currentLoginTime;
    }

    public String getCurrentLoginIp(){
        return currentLoginIp;
    }

    public void setCurrentLoginIp(String currentLoginIp){
        this.currentLoginIp = currentLoginIp;
    }

    public String getResetKey(){
        return resetKey;
    }

    public void setResetKey(String resetKey) {
       this.resetKey = resetKey;
    }

    public String getResetPwd(){
        return resetPwd;
    }

    public void setResetPwd(String resetPwd){
        this.resetPwd = resetPwd;
    }
    
    public java.lang.String getSessionId(){
    	return sessionId;
    }
    
    public void setSessionId(java.lang.String sessionId){
    	this.sessionId=sessionId;
    }
    
    @Override
	public boolean equals(Object obj){
        if(null == obj)  return false;
        if(!(obj instanceof User))  return false;
        else{
             User user = (User)obj;
             if(null == this.getId() || null == user.getId())  return false;
             else return (this.getId().equals(user.getId()));
        }
    }

    @Override
	public int hashCode() {
        if(Integer.MIN_VALUE == this.hashCode){
            if(null == this.getId()) return super.hashCode();
            else{
                String hashStr = this.getClass().getName()+":"+this.getId().hashCode();
                this.hashCode = hashStr.hashCode();
            }
        }
        return this.hashCode;
    }

    @Override
	public String toString(){
        return super.toString();
    }

}
