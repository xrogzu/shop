package com.jspgou.cms.action.directive;

import static com.jspgou.cms.Constants.SHOP_SYS;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jspgou.common.web.freemarker.DirectiveUtils;
import com.jspgou.core.entity.Website;
import com.jspgou.cms.entity.KeyWord;
import com.jspgou.cms.manager.KeyWordMng;

import freemarker.core.Environment;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
* This class should preserve.
* @preserve
*/
public class KeyWordDirective extends ProductAbstractDirective {
	/**
	 * 模板名称
	 */
	public static final String TPL_NAME = "ProductList";

	@Override
	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		Website web = getWeb(env, params, websiteMng);
		Integer count=this.getCount(params);
		List<KeyWord> beanList=keywordMng.findKeyWord(count);
		Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(
				params);
		paramWrap.put(OUT_LIST, ObjectWrapper.DEFAULT_WRAPPER.wrap(beanList));
		Map<String, TemplateModel> origMap = DirectiveUtils
				.addParamsToVariable(env, paramWrap);
		if (isInvokeTpl(params)) {
			includeTpl(SHOP_SYS, TPL_NAME, web, params, env);
		} else {
			renderBody(env, loopVars, body);
		}
		DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
	}
	
	@Autowired
	private KeyWordMng keywordMng;
}
