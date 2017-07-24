package com.jspgou.cms.action.front;

import static com.jspgou.cms.Constants.COMMON;
import static com.jspgou.cms.Constants.SHOP_SYS;
import static com.jspgou.cms.Constants.TPLDIR_INDEX;
import static com.jspgou.common.page.SimplePage.cpn;
import static com.jspgou.core.web.front.URLHelper.INDEX;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jspgou.cms.entity.Brand;
import com.jspgou.cms.entity.Category;
import com.jspgou.cms.entity.Exended;
import com.jspgou.cms.entity.Product;
import com.jspgou.cms.entity.ProductStandard;
import com.jspgou.cms.entity.Relatedgoods;
import com.jspgou.cms.entity.ShopArticle;
import com.jspgou.cms.entity.ShopChannel;
import com.jspgou.cms.entity.StandardType;
import com.jspgou.cms.manager.BrandMng;
import com.jspgou.cms.manager.CategoryMng;
import com.jspgou.cms.manager.ExendedMng;
import com.jspgou.cms.manager.ProductMng;
import com.jspgou.cms.manager.ProductStandardMng;
import com.jspgou.cms.manager.RelatedgoodsMng;
import com.jspgou.cms.manager.ShopArticleMng;
import com.jspgou.cms.manager.ShopChannelMng;
import com.jspgou.cms.manager.StandardTypeMng;
import com.jspgou.cms.web.ShopFrontHelper;
import com.jspgou.cms.web.SiteUtils;
import com.jspgou.common.page.Pagination;
import com.jspgou.common.web.RequestUtils;
import com.jspgou.common.web.ResponseUtils;
import com.jspgou.common.web.springmvc.MessageResolver;
import com.jspgou.core.entity.Global;
import com.jspgou.core.entity.Website;
import com.jspgou.core.web.WebErrors;
import com.jspgou.core.web.front.FrontHelper;
import com.jspgou.core.web.front.URLHelper;
import com.jspgou.core.web.front.URLHelper.PageInfo;


/**
 * JspGou动态系统Action
 * @author liufang
 * This class should preserve.
 * @preserve
 */
@Controller
public class DynamicPageAct {
	/**
	 * 首页
	 */
	public static final String TPL_INDEX = "tpl.index";
	
	/**
	 * 品牌模板
	 */
	private static final String BRAND = "tpl.brand";
	/**
	 * 品牌详情模板
	 */
	private static final String BRAND_DETAIL = "tpl.brandDetail";
	
	/**
	 * 商品类别
	 */
	public static final String CATEGORY = "category";
	
	/**
	 * 全部商品分类模板
	 */
	private static final String ALL_PRODUCT_CATEGORY = "tpl.allProductCategory";
	
	/**
	 * WEBLOGIC的默认路径
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/index.jhtml", method = RequestMethod.GET)
	public String indexForWeblogic(HttpServletRequest request, ModelMap model) {
		return index(request, model);
	}

	/**
	 * TOMCAT的默认路径
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(HttpServletRequest request, ModelMap model) {
		Website web = com.jspgou.core.web.SiteUtils.getWeb(request);
		ShopFrontHelper.setCommonData(request, model, web, 1);
		return web.getTemplate(TPLDIR_INDEX, MessageResolver.getMessage(request,TPL_INDEX), request);
	}

	/**
	 * 动态页入口
	 */
	@RequestMapping(value = "/**/*.*", method = RequestMethod.GET)
	public String excute(HttpServletRequest request,HttpServletResponse response, ModelMap model) {
		String url = request.getRequestURL().toString();
		//Global global=com.jspgou.core.web.SiteUtils.getWeb(request).getGlobal();
		//model.addAttribute("global", global);
		PageInfo info = URLHelper.getPageInfo(request);
		int pageNo = URLHelper.getPageNo(request);
		String queryString = request.getQueryString();
		Website web = com.jspgou.core.web.SiteUtils.getWeb(request);
		ShopFrontHelper.setDynamicPageData(request, model, web, url, info.getHrefFormer(), info.getHrefLatter(), pageNo);
		String[] paths = URLHelper.getPaths(request);
		String[] params = URLHelper.getParams(request);
		int len = paths.length;
        if (len == 1) {
			// 单页
			return channel(paths[0], params, pageNo, queryString, url, web,request, response, model);
		} else if (len == 2) {
			if (paths[1].equals(INDEX)) {
				// 栏目页
				return channel(paths[0], params, pageNo, queryString, url, web,request, response, model);
			}
			try {
				Long id = Long.parseLong(paths[1]);
				// 内容页
				return content(paths[0], id, params, pageNo, queryString, url,web, request, response, model);
			} catch (NumberFormatException e) {
			}
		}
		return FrontHelper.pageNotFound(web, model, request);
	}
	
	/**
	 * 栏目页
	 */
	@SuppressWarnings("unchecked")
	public String channel(String path, String[] params, int pageNo,String queryString, String url, Website web,
			HttpServletRequest request, HttpServletResponse response,ModelMap model) {
		Category category = categoryMng.getByPath(web.getId(), path);
		if (category != null) {
			List<Exended> exendeds = exendedMng.getList(category.getType().getId());
			Map map= new HashMap(); 
			Map map1= new HashMap(); 
			int num = exendeds.size();
			for(int i=0;i<num;i++){
				map.put(exendeds.get(i).getId().toString(), exendeds.get(i).getItems());
				map1.put(exendeds.get(i).getId().toString(), exendeds.get(i));
			}
			model.addAttribute("brandId",getBrandId(request));
			model.addAttribute("map", map);
			model.addAttribute("map1", map1);
			model.addAttribute("fields", getNames(request));
			model.addAttribute("zhis", getValues(request));
			model.addAttribute("category", category);
			model.addAttribute("pageSize", getpageSize(request));
			model.addAttribute("names", getName(request));
			model.addAttribute("values", getValue(request));
			model.addAttribute("isShow", getIsShow(request));
			model.addAttribute("orderBy",getOrderBy(request));
			model.addAttribute("startPrice", getStartPrice(request));
			model.addAttribute("endPrice", getEndPrice(request));
			return category.getTplChannelRel(request);
		} else {
			ShopChannel channel = shopChannelMng.getByPath(web.getId(),path);
			if (channel != null) {
				model.addAttribute("channel", channel);
				return channel.getTplChannelRel(request);
			}
		}
		return FrontHelper.pageNotFound(web, model, request);
	}

	/**
	 * 内容页
	 */
	public String content(String chnlName, Long id, String[] params,
			int pageNo, String queryString, String url, Website web,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		Category category = categoryMng.getByPath(web.getId(), chnlName);
		if (category != null) {//商品内容	
			Product product = productMng.findById(id);
			if (product != null) {	
				if(product.getProductFashion()!=null){
					String[] arr = null;
					arr=product.getProductFashion().getNature().split("_");
					model.addAttribute("arr", arr);
				}
                //获取相关商品数据
				List<Relatedgoods> relatedgoods=relatedgoodsMng.findByIdProductId(id);
				List<Product> productList = new ArrayList<Product>();
				if(relatedgoods!=null){
					for(int i=0;i<relatedgoods.size();i++){	
						if(productMng.findById(relatedgoods.get(i).getProductIds())!=null){
							Product product1=productMng.findById(relatedgoods.get(i).getProductIds());
							productList.add(product1);
						}
					}
					model.addAttribute("productList", productList);
				}
				List<ProductStandard> psList = productStandardMng.findByProductId(id);
				List<StandardType> standardTypeList = standardTypeMng.getList(category.getId());
				productMng.updateViewCount(product);
				model.addAttribute("standardTypeList",standardTypeList);
				model.addAttribute("psList",psList);
				model.addAttribute("category", category);
				model.addAttribute("product", product);
				//model.addAttribute("historyProductIds", getHistoryProductIds(request));
				return category.getTplContentRel(request);
			} else {				
				return FrontHelper.pageNotFound(web, model, request);
			}
		} else {//文章内容
			ShopArticle article = shopArticleMng.findById(id);
			if (article != null) {				
				ShopChannel channel = article.getChannel();
				model.addAttribute("article", article);
				model.addAttribute("channel", channel);
				return channel.getTplContentRel(request);
			} else {
				return FrontHelper.pageNotFound(web, model, request);
			}
		}
	}
	
	
	
	public String getHistoryProductIds(HttpServletRequest request){
		String str = null ;
		Cookie[] cookie = request.getCookies();
		int num = cookie.length;
		for (int i = 0; i < num; i++) {
			if (cookie[i].getName().equals("shop_record")) {
				str = cookie[i].getValue();
				break;
			}
		}
		return str;
	}
	/**
	 * 信息提示页面
	 * 
	 * @param request
	 * @param model
	 * @param message
	 *            进行国际化处理
	 * @return
	 */
	@RequestMapping(value = "/showMessage.jspx")
	public String showMessage(HttpServletRequest request,
			ModelMap model, String message) {
		Website web = com.jspgou.core.web.SiteUtils.getWeb(request);
		ShopFrontHelper.setCommonData(request, model, web, 1);
		model.put("message", message);
		return web.getTplSys(COMMON, MessageResolver.getMessage(request,"tpl.messagePage"), request);	
	}
	
	//GET提交方式导致enter键提交 出现分页问题
	@RequestMapping(value = "/brand.jspx", method = RequestMethod.GET)
	public String brand(Long id, HttpServletRequest request, ModelMap model) {
		String tpl;
		Website web = com.jspgou.core.web.SiteUtils.getWeb(request);
		WebErrors errors = validateBrand(id, request);
		if (errors.hasErrors()) {
			return FrontHelper.showError(errors, web, model, request);
		}
		if (id != null) {
			model.addAttribute("brand", brandMng.findById(id));
			tpl = MessageResolver.getMessage(request, BRAND_DETAIL);
		} else {
			tpl = MessageResolver.getMessage(request, BRAND);
		}
		ShopFrontHelper.setCommonData(request, model, web, 1);
		return web.getTplSys(SHOP_SYS, tpl, request);
	}
	
	/*public Integer getBrandId(HttpServletRequest request){
		String brandId = RequestUtils.getQueryParam(request, "brandId");
		Integer id = null;
		if (!StringUtils.isBlank(brandId)) {
			id = Integer.parseInt(brandId);
		}
		return id;
	}*/
	
	//以下为多选action
	public String getBrandId(HttpServletRequest request){
		String brandId = RequestUtils.getQueryParam(request, "brandId");
		return brandId;
	}
		
	/**
	 * 全部商品分类
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value ="/allProductCategory.jspx")
	public String allProductCategory(HttpServletRequest request, ModelMap model) {
		Website web = com.jspgou.core.web.SiteUtils.getWeb(request);
		ShopFrontHelper.setCommonData(request, model, web, 1);
		return web.getTemplate(CATEGORY, MessageResolver.getMessage(request,ALL_PRODUCT_CATEGORY), request);
	}
	
	/**
	 * 商品上拉加载
	 * @throws JSONException 
	 */
	@RequestMapping(value="/cateGoryListLoading.jspx")
	public void cateGoryListLoading(String  brandId, Long categoryId,//栏目页商品分页manager(wangzewu)
			Boolean isRecommend, String[] names,String[] values,Boolean isSpecial, int orderBy,Double startPrice,Double endPrice,int pageNo,int count,HttpServletRequest request,HttpServletResponse response,ModelMap model){    
		Website web = com.jspgou.core.web.SiteUtils.getWeb(request);
		Pagination pagination = productMng.getPageForTagChannel(brandId, web.getId(), categoryId,
				null, isRecommend,names,values, isSpecial,orderBy,startPrice,endPrice,cpn(pageNo),
				count); 
		List<Product> list =(List<Product>) pagination.getList();
		JSONArray arr=new JSONArray();
		boolean no;
		no= pageNo<=pagination.getTotalPage();
		  if(list!=null&&list.size()>0&&no){
			 try{
				  for(Product content:list){
					  JSONObject o=new JSONObject();
					  o.put("url",content.getUrl());//JSONObject(对象)
						o.put("coverImg", content.getProductExt().getCoverImg());
						o.put("name", content.getName());
						o.put("salePrice", content.getSalePrice());
						arr.put(o);//JSONArray(数组)
				  }
			  }catch(JSONException e){
					e.printStackTrace();
				}
			  ResponseUtils.renderJson(response, arr.toString());
		  }

	}
	
	
	public Integer getpageSize(HttpServletRequest request){
		String pageSize = RequestUtils.getQueryParam(request, "pageSize");
		Integer pagesize = null;
		if (!StringUtils.isBlank(pageSize)) {
			pagesize = Integer.parseInt(pageSize);
		}
		if(pagesize==null){
			pagesize = 12;
		}
		return pagesize;
	}

	public Integer getIsShow(HttpServletRequest request){
		String isShow = RequestUtils.getQueryParam(request, "isShow");
		Integer isshow = null;
		if (!StringUtils.isBlank(isShow)) {
			isshow = Integer.parseInt(isShow);
		}
		if(isshow==null){
			isshow = 0;
		}
		return isshow;
	}
	
	public Integer getOrderBy(HttpServletRequest request){
		String orderBy = RequestUtils.getQueryParam(request, "orderBy");
		Integer orderby = null;
		if (!StringUtils.isBlank(orderBy)) {
			orderby = Integer.parseInt(orderBy);
		}
		if(orderby==null){
			orderby = 0;
		}
		return orderby;
	}
	
	public String[] getNames(HttpServletRequest request){
		Map<String, String> attr = RequestUtils.getRequestMap(request, "exended_");
		List li=new ArrayList(attr.keySet());
	    String name= "";
	    for(int i=0;i<li.size();i++){
	    	if(i+1==li.size()){
	    		name+=(String) li.get(i);
	    	}else{
	    		name+=(String) li.get(i)+",";
	    	}
	    }
	    String[] names= StringUtils.split(name, ',');
		return names;
	}
	
	public String[] getValues(HttpServletRequest request){
		Map<String, String> attr = RequestUtils.getRequestMap(request, "exended_");
		List li=new ArrayList(attr.keySet());
	    String value= "";
	    for(int i=0;i<li.size();i++){
	    	if(i+1==li.size()){
	    		if(StringUtils.isBlank(attr.get(li.get(i)))){
	    			value+="0";
	    		}else{
	    			value+=attr.get(li.get(i));
	    		}
	    	}else{
	    		if(StringUtils.isBlank(attr.get(li.get(i)))){
	    			value+="0,";
	    		}else{
	    			value+=attr.get(li.get(i))+",";
	    		}
	    	}
	    }
		String[] values= StringUtils.split(value, ',');
		return values;
	}
	
	
	public String getName(HttpServletRequest request){
		Map<String, String> attr = RequestUtils.getRequestMap(request, "exended_");
		List li=new ArrayList(attr.keySet());
	    String name= "";
	    for(int i=0;i<li.size();i++){
	    	if(i+1==li.size()){
	    		name+=(String) li.get(i);
	    	}else{
	    		name+=(String) li.get(i)+",";
	    	}
	    }
	    
		return name;
	}
	
	public String getValue(HttpServletRequest request){
		Map<String, String> attr = RequestUtils.getRequestMap(request, "exended_");
		List li=new ArrayList(attr.keySet());
	    String value= "";
	    for(int i=0;i<li.size();i++){
	    	if(i+1==li.size()){
	    		if(StringUtils.isBlank(attr.get(li.get(i)))){
	    			value+="0";
	    		}else{
	    			value+=attr.get(li.get(i));
	    		}
	    	}else{
	    		if(StringUtils.isBlank(attr.get(li.get(i)))){
	    			value+="0,";
	    		}else{
	    			value+=attr.get(li.get(i))+",";
	    		}
	    	}
	    }
		return value;
	}
	
   private WebErrors validateBrand(Long id, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (id != null) {
			Brand brand = brandMng.findById(id);
			if (errors.ifNotExist(brand, Brand.class, id)) {
				return errors;
			}
		}
		return errors;
	}
   
   
	public Integer getStartPrice(HttpServletRequest request){
		String startPrice=RequestUtils.getQueryParam(request, "startPrice");
		Integer start=null;
		if(!StringUtils.isBlank(startPrice)){
			if(!startPrice.equals("￥")){				
				start=Integer.parseInt(startPrice.replace("￥", ""));
			}else{
				start=0;
			}
		}
		if(start==null){
			start=0;
		}
		return start;
	}
	
	public Integer getEndPrice(HttpServletRequest request){
		String endPrice=RequestUtils.getQueryParam(request, "endPrice");
		Integer end=null;
		if(!StringUtils.isBlank(endPrice)){
			if(!endPrice.equals("￥")){				
				end=Integer.parseInt(endPrice.replace("￥", ""));
			}else{
				end=0;
			}
		}
		if(end==null){
			end=0;
		}
		return end;
	}
	

	@Autowired
	private CategoryMng categoryMng;
	@Autowired
	private ProductMng productMng;
	@Autowired
    private RelatedgoodsMng relatedgoodsMng;
	@Autowired
	private ShopChannelMng shopChannelMng;
	@Autowired
	private ShopArticleMng shopArticleMng;
	@Autowired
	private BrandMng brandMng;
	@Autowired
	private StandardTypeMng standardTypeMng;
	@Autowired
	private ProductStandardMng productStandardMng;
	@Autowired
	private ExendedMng exendedMng;

}
