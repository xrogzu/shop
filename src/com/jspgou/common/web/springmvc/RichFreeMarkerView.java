package com.jspgou.common.web.springmvc;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import com.jspgou.cms.web.threadvariable.AdminThread;
import com.jspgou.core.entity.Website;
import com.jspgou.core.web.SiteUtils;


/**
 * 扩展spring的FreemarkerView，加上base属性。
 * 
 * 支持jsp标签，Application、Session、Request、RequestParameters属性
 * 
 * @author liufang
 * This class should preserve.
 * @preserve
*/
public class RichFreeMarkerView extends FreeMarkerView{
	/**
	 * 部署路径属性名称
	 */
	public static final String CONTEXT_PATH = "base";
	
	/**
	 * 是否开启单点认证
	 */
	public static final String SSO_ENABLE = "ssoEnable";
	/**
	 * 用户
	 */
	public static final String USER = "user";
	
	/**
	 * 在model中增加部署路径base，方便处理部署路径问题。
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected void exposeHelpers(Map model, HttpServletRequest request)
			throws Exception {
        super.exposeHelpers(model, request);
        model.put(CONTEXT_PATH, request.getContextPath());
        Website web = SiteUtils.getWeb(request);
        model.put(SSO_ENABLE,web.getSsoEnable());
        model.put(USER,AdminThread.get());
    }
}
