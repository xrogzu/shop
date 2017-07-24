package com.jspgou.cms.action.directive;

import static com.jspgou.cms.Constants.SHOP_SYS;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.jspgou.common.page.Pagination;
import com.jspgou.common.web.freemarker.DirectiveUtils;
import com.jspgou.core.entity.Website;

import freemarker.core.Environment;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
* This class should preserve.
* @preserve
*/
public class ProductPageChannelDirective extends ProductAbstractDirective {
	/**
	 * 模板名称
	 */
	public static final String TPL_NAME = "ProductPage";

	@Override
	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		Website web = getWeb(env, params, websiteMng);
		Long ctgId = getCategoryId(params);
		/*Long brandId=this.getLong("brandId", params);*/
		//品牌多选
		String brandId=getString("brandId", params);
		Long tagId = getTagId(params);
		String[] names= StringUtils.split(this.getString("names", params), ',');
		String[] values= StringUtils.split(this.getString("values", params), ',');
		Integer orderBy=getInt("orderBy",params);
		Double startPrice=getDouble("startPrice",params);
		Double endPrice=getDouble("endPrice",params);
		Pagination pagination = productMng.getPageForTagChannel(brandId, web.getId(), ctgId,
				tagId, isRecommend(params),names,values, isSpecial(params),orderBy,startPrice,endPrice,getPageNo(env),
				getCount(params));
		Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(
				params);
		paramWrap.put(OUT_PAGINATION, ObjectWrapper.DEFAULT_WRAPPER
				.wrap(pagination));
		Map<String, TemplateModel> origMap = DirectiveUtils
				.addParamsToVariable(env, paramWrap);
		if (isInvokeTpl(params)) {
			includeTpl(SHOP_SYS, TPL_NAME, web, params, env);
		} else {
			renderBody(env, loopVars, body);
		}
		DirectiveUtils.removeParamsFromVariable(env, paramWrap, origMap);
	}

}
