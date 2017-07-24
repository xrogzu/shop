package com.jspgou.cms.action.directive;

import static com.jspgou.cms.Constants.SHOP_SYS;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jspgou.cms.action.directive.abs.WebDirective;
import com.jspgou.cms.entity.Discuss;
import com.jspgou.cms.manager.DiscussMng;
import com.jspgou.common.web.freemarker.DirectiveUtils;
import com.jspgou.core.entity.Website;
import com.jspgou.core.manager.WebsiteMng;

import freemarker.core.Environment;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class DiscussCountDirective extends WebDirective{

	 private static final String TPL_NAME = "discussCount";
	@SuppressWarnings("unchecked")
		public void execute(Environment env, Map params, TemplateModel[] loopVars,
				TemplateDirectiveBody body) throws TemplateException, IOException {
	    	Website web = getWeb(env, params, websiteMng);
	    	String discussType=getString("discussType", params);
	    	Long productId=getLong("productId", params);
	    	List<Discuss> list =discussMng.findByType(productId,discussType);
	    	Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(params);
			paramWrap.put("discusses", ObjectWrapper.DEFAULT_WRAPPER.wrap(list));
			Map<String, TemplateModel> origMap = DirectiveUtils.addParamsToVariable(env, paramWrap);
			if (isInvokeTpl(params)) {
				includeTpl(SHOP_SYS, TPL_NAME, web, params, env);
			} else {
				renderBody(env, loopVars, body);
			}
			DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
		}
	
		private void renderBody(Environment env, TemplateModel[] loopVars,
				TemplateDirectiveBody body) throws TemplateException, IOException {
			body.render(env.getOut());
		}
		
	    @Autowired
	    private WebsiteMng websiteMng;
	    @Autowired
	    private DiscussMng discussMng;
}