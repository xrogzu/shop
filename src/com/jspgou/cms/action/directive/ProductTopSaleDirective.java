package com.jspgou.cms.action.directive;

import static com.jspgou.cms.Constants.SHOP_SYS;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;

import com.jspgou.common.web.freemarker.DirectiveUtils;
import com.jspgou.core.entity.Website;
import com.jspgou.cms.entity.OrderItem;
import com.jspgou.cms.entity.Product;
import com.jspgou.cms.entity.base.BaseOrderItem;
import com.jspgou.cms.manager.OrderItemMng;

import freemarker.core.Environment;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
* This class should preserve.
* @preserve
*/
public class ProductTopSaleDirective extends ProductAbstractDirective {
	/**
	 * 模板名称
	 */
	public static final String TPL_NAME = "ProductList";

	@Override
	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Calendar c=Calendar.getInstance();
		c.setTime(new Date());
		int i=c.get(Calendar.DAY_OF_WEEK);
		c.add(Calendar.DATE, -i);
		Date d=c.getTime();
		//Date endTime=sdf.format(d);
		c.add(Calendar.DATE, -7);
		Date dd=c.getTime();
		//Date beginTime=sdf.format(dd);
		Website web = getWeb(env, params, websiteMng);
		Integer count=this.getCount(params);
		List<Object[]> oiList=orderItemMng.getOrderItem(d,dd);
		List<Product> productList=new ArrayList<Product>();
		for(int i1=0;i1<oiList.size();i1++){
			Object[] o=oiList.get(i1);
			productList.add(((OrderItem)o[0]).getProduct());
			if(i1==count-1){
				break;
			}
		}
		Map<String, TemplateModel> paramWrap = new HashMap<String, TemplateModel>(
				params);
		paramWrap.put(OUT_LIST, ObjectWrapper.DEFAULT_WRAPPER.wrap(productList));
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
	private OrderItemMng orderItemMng;
}
