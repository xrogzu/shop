package com.jspgou.cms.action.admin.main;

import static com.jspgou.common.page.SimplePage.cpn;
import static com.jspgou.common.web.Constants.LIST_SPLIT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.hibernate.ObjectNotFoundException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;

import com.jspgou.cms.Constants;
import com.jspgou.cms.entity.Brand;
import com.jspgou.cms.entity.Category;
import com.jspgou.cms.entity.Exended;
import com.jspgou.cms.entity.Product;
import com.jspgou.cms.entity.ProductExended;
import com.jspgou.cms.entity.ProductExt;
import com.jspgou.cms.entity.ProductFashion;
import com.jspgou.cms.entity.ProductStandard;
import com.jspgou.cms.entity.ProductType;
import com.jspgou.cms.entity.ProductTypeProperty;
import com.jspgou.cms.entity.Relatedgoods;
import com.jspgou.cms.entity.Standard;
import com.jspgou.cms.entity.StandardType;
import com.jspgou.cms.lucene.LuceneProductSvc;
import com.jspgou.cms.manager.BrandMng;
import com.jspgou.cms.manager.CartItemMng;
import com.jspgou.cms.manager.CategoryMng;
import com.jspgou.cms.manager.ExendedMng;
import com.jspgou.cms.manager.ProductFashionMng;
import com.jspgou.cms.manager.ProductMng;
import com.jspgou.cms.manager.ProductStandardMng;
import com.jspgou.cms.manager.ProductTagMng;
import com.jspgou.cms.manager.ProductTypeMng;
import com.jspgou.cms.manager.ProductTypePropertyMng;
import com.jspgou.cms.manager.RelatedgoodsMng;
import com.jspgou.cms.manager.StandardMng;
import com.jspgou.cms.manager.StandardTypeMng;
import com.jspgou.common.image.ImageUtils;
import com.jspgou.common.page.Pagination;
import com.jspgou.common.web.CookieUtils;
import com.jspgou.common.web.RequestUtils;
import com.jspgou.common.web.ResponseUtils;
import com.jspgou.common.web.springmvc.MessageResolver;
import com.jspgou.core.entity.Website;
import com.jspgou.core.web.SiteUtils;
import com.jspgou.core.web.WebErrors;

/**
* This class should preserve.
* @preserve
*/
@Controller
public class ProductAct implements ServletContextAware {
	private static final Logger log = LoggerFactory.getLogger(ProductAct.class);
	private Boolean title_id1=true;	//默认  ID  被选中              
	
	private Boolean title_coverImg2=true;// 默认图片封面被选中
	
	private Boolean title_prdtName3=true;//默认  商品名称 被选中
	
	private Boolean title_prdtCategory4=true;//默认  分类 被选中
	private Boolean title_prdtType5=true;//默认 类型 被选中
	private Boolean title_prdtSalePrice6=true;//默认 销售价 被选中
	private Boolean title_prdtStockCount7=true;//默认 商品库存 被选中
	private Boolean title_prdtBrand8=true;//默认 品牌 被选中
	private Boolean title_prdtOnSale9=true;//默认 上架 被选中
	private Boolean title_Operate10=true;//默认 操作选项  被选中
	
	
	@RequestMapping("/product/v_title_list.do")
	public String get_list_and_title(Long ctgId,Boolean isOnSale,Boolean isRecommend,
			Boolean isSpecial,Boolean isHotsale,Boolean isNewProduct,
			Long typeId,Double startSalePrice,Double endSalePrice,
			Integer startStock,Integer endStock,Integer pageNo,
			HttpServletRequest request, ModelMap model) {
		String productName = RequestUtils.getQueryParam(request, "productName");
		productName = StringUtils.trim(productName);
		String brandName = RequestUtils.getQueryParam(request, "brandName");
		brandName = StringUtils.trim(brandName);
		Website web = com.jspgou.core.web.SiteUtils.getWeb(request);
		if (ctgId != null) {
			model.addAttribute("category", categoryMng.findById(ctgId));
		}
		Pagination pagination = manager.getPage(com.jspgou.core.web.SiteUtils.getWebId(request),
				ctgId, productName,brandName,isOnSale, isRecommend, isSpecial,isHotsale,isNewProduct,typeId,
				startSalePrice,endSalePrice,startStock,endStock,
				cpn(pageNo), CookieUtils.getPageSize(request));
		List<ProductType> typeList = productTypeMng.getList(web.getId());

		
		List<Category> list = categoryMng.getTopList(com.jspgou.core.web.SiteUtils
				.getWebId(request));
		// 如果没有商品类别，则不传递数据到视图层，由视图层给出相应提示。
		if (list.size() > 0) {
			Category treeRoot = new Category();
			treeRoot.setName(MessageResolver.getMessage(request,
					"product.allCategory"));
			treeRoot.setChild(new LinkedHashSet<Category>(list));
			model.addAttribute("treeRoot", treeRoot);
		}

		model.addAttribute("typeList", typeList);
		model.addAttribute("productName", productName);
		model.addAttribute("brandName",brandName);
		model.addAttribute("isOnSale", isOnSale);
		model.addAttribute("isRecommend", isRecommend);
		model.addAttribute("isSpecial",isSpecial);
		model.addAttribute("isHotsale", isHotsale);
		model.addAttribute("isNewProduct", isNewProduct);
		model.addAttribute("typeId",typeId);
		model.addAttribute("startSalePrice", startSalePrice);
		model.addAttribute("endSalePrice", endSalePrice);
		model.addAttribute("startStock",startStock);
		model.addAttribute("endStock", endStock);
		model.addAttribute("pagination",pagination);
		model.addAttribute("ctgId", ctgId);
		model.addAttribute("pageNo", pagination.getPageNo());
		/*列表标题参数处理的步骤：1先取传来的标题状态参数，2再存储参数，3最后取值返回*/
		get_set_title_status(request);//1与2两步骤由次方法完成
		//第3步取值返回；
		model.addAttribute("title_id1", title_id1);
		model.addAttribute("title_coverImg2",title_coverImg2);
		model.addAttribute("title_prdtName3", title_prdtName3);
		model.addAttribute("title_prdtCategory4", title_prdtCategory4);
		model.addAttribute("title_prdtType5",title_prdtType5);
		model.addAttribute("title_prdtSalePrice6", title_prdtSalePrice6);
		model.addAttribute("title_prdtStockCount7", title_prdtStockCount7);
		model.addAttribute("title_prdtBrand8",title_prdtBrand8);
		model.addAttribute("title_prdtOnSale9",title_prdtOnSale9);
		model.addAttribute("title_Operate10",title_Operate10);
		
		return "product/list";
	}
	
	@RequestMapping("/product/v_categoryList.do")
	public String categoryList(HttpServletRequest request, ModelMap model){
		Website web = SiteUtils.getWeb(request);
		//没有判断子目录是否存在
		List<Category> clist = categoryMng.getTopList(web.getId());
		model.addAttribute("clist", clist);
		return "product/categoryList";
	}
	
	@RequestMapping("/product/v_product_categorychild.do")
		public void productCategoryChild(Long parentId, HttpServletRequest request,
				HttpServletResponse response)throws JSONException{
			Website web = SiteUtils.getWeb(request);
			JSONArray arr = new JSONArray();
			if(parentId !=null){
				List<Category> clist = categoryMng.getChildList(web.getId(), parentId);
				if(clist.size()>=0){
					JSONObject o;
					for(Category t:clist){
						o = new JSONObject();
						o.put("success", true);
						o.put("id", t.getId());
						o.put("name", t.getName());
						arr.put(o);
					}
				}
			}
			ResponseUtils.renderJson(response, arr.toString());
	}

	@RequestMapping("/product/v_left.do")
	public String left(HttpServletRequest request, ModelMap model) {
		List<Category> list = categoryMng.getTopList(com.jspgou.core.web.SiteUtils
				.getWebId(request));
		// 如果没有商品类别，则不传递数据到视图层，由视图层给出相应提示。
		if (list.size() > 0) {
			Category treeRoot = new Category();
			treeRoot.setName(MessageResolver.getMessage(request,
					"product.allCategory"));
			treeRoot.setChild(new LinkedHashSet<Category>(list));
			model.addAttribute("treeRoot", treeRoot);
		}
		return "product/left";
	}

	
	@RequestMapping(value = "/product/ajaxcategory.do")
	public void ajaxcategory(Long ctgId,HttpServletRequest request, HttpServletResponse response,ModelMap model) throws JSONException {
		Category category= categoryMng.findById(ctgId);
		
		List<Brand> list2 = new ArrayList<Brand> ();  
		list2.addAll(category.getBrands());  
	
		Long ids[] = new Long[list2.size()];
		String names[] = new String[list2.size()];
		for (int i=0,j=list2.size(); i<j; i++) {
            Brand brand=list2.get(i);
			ids[i] = brand.getId();
			names[i] = brand.getName();
		}
     
		JSONObject json = new JSONObject();
		try {
			json.put("ids",ids);
			json.put("names",names);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		ResponseUtils.renderJson(response, json.toString());
  }





	
	
	
	@RequiresPermissions("product:v_add")
	@RequestMapping("/product/v_add.do")
	public String add(Long ctgId, HttpServletRequest request, ModelMap model) {
		Category category= categoryMng.findById(ctgId);
		
		// 模型项列表
		List<ProductTypeProperty> itemList = productTypePropertyMng.getList(category.getType().getId(), false);
		List<StandardType> standardTypeList =standardTypeMng.getList(category.getId());
        List<ProductType> typeList = productTypeMng.getList(1L);
		List<Brand> brandList = brandMng.getAllList();
		
		List<Brand> brandList1=brandMng.getBrandList(category.getName());
		model.addAttribute("brandList1", brandList1);
		
		model.addAttribute("brandList", brandList);
		model.addAttribute("typeList", typeList);
		model.addAttribute("ctgId", ctgId);
		model.addAttribute("category", category);
		model.addAttribute("categoryList", categoryMng.getListForProduct(com.jspgou.core.web.SiteUtils.getWebId(request), ctgId));
		model.addAttribute("tagList", productTagMng.getList(com.jspgou.core.web.SiteUtils.getWebId(request)));
		model.addAttribute("standardTypeList", standardTypeList);
		model.addAttribute("itemList", itemList);
		List<Exended> exendeds = exendedMng.getList(category.getType().getId());
		Map map= new HashMap(); 
		Map map1= new HashMap(); 
		int num = exendeds.size();
	    for(int i=0;i<num;i++){
			map.put(exendeds.get(i).getId().toString(), exendeds.get(i).getItems());
			map1.put(exendeds.get(i).getId().toString(), exendeds.get(i));
		}
		model.addAttribute("map", map);
		model.addAttribute("map1", map1);
		return "product/add";
	}

	@RequiresPermissions("product:v_edit")
	@RequestMapping("/product/v_edit.do")
	public String edit(Long id, Long ctgId, HttpServletRequest request,
			ModelMap model) {
		WebErrors errors = validateEdit(id, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		Product product = manager.findById(id);
		List<ProductStandard> psList = productStandardMng.findByProductId(id);
		String productKeywords = StringUtils.join(product.getKeywords(),LIST_SPLIT);// 模型项列表
		Category category= product.getCategory(); 	
		List<StandardType> standardTypeList = standardTypeMng.getList(category.getId());
		List<ProductTypeProperty> itemList = productTypePropertyMng.getList(category.getType().getId(), false);
		List<ProductExended> pelist=product.getExendeds();
		List<Exended> exendeds = exendedMng.getList(category.getType().getId());
        List<ProductType> typeList = productTypeMng.getList(1L);
		List<Brand> brandList = brandMng.getAllList();
		model.addAttribute("brandList", brandList);
		model.addAttribute("typeList", typeList);
		//获取相关商品数据
		List<Relatedgoods> relatedgoods=relatedgoodsMng.findByIdProductId(id);
		List<Product> productList = new ArrayList<Product>();
		if(relatedgoods!=null){
			for(int i=0;i<relatedgoods.size();i++){
				if(manager.findById(relatedgoods.get(i).getProductIds())!=null){
					Product product1=manager.findById(relatedgoods.get(i).getProductIds());
					productList.add(product1);
				}
			}
			model.addAttribute("productList", productList);
		}
		Map map= new HashMap(); 
		Map map1= new HashMap(); 
		int num = exendeds.size();
	    for(int i=0;i<num;i++){
			map.put(exendeds.get(i).getId().toString(), exendeds.get(i).getItems());
			map1.put(exendeds.get(i).getId().toString(), exendeds.get(i));
		}
	    Map map2=new HashMap();
      	for(int i=0;i<pelist.size();i++){
		   map2.put(pelist.get(i).getName(), pelist.get(i).getValue());
	    }
	    model.addAttribute("map2", map2);
		model.addAttribute("map", map);
		model.addAttribute("map1", map1);
		model.addAttribute("productKeywords", productKeywords);
		model.addAttribute("tagList", productTagMng.getList(com.jspgou.core.web.SiteUtils.getWebId(request)));
		model.addAttribute("categoryList", categoryMng.getList(com.jspgou.core.web.SiteUtils.getWebId(request)));
		model.addAttribute("standardTypeList",standardTypeList);
		model.addAttribute("category", category);
		model.addAttribute("product", product);
		model.addAttribute("ctgId", ctgId);
		model.addAttribute("isLimit", product.getProductExt().getIslimitTime());
		model.addAttribute("itemList", itemList);
		model.addAttribute("psList",psList);
		return "product/edit";
	}

	@RequiresPermissions("product:o_save")
	@RequestMapping("/product/o_save.do")
	public String save(Product bean, ProductExt ext,String rightlist,Long categoryId, Long brandId,
			Long[] tagIds, String productKeywords,String[] nature,Long[] picture,String[] colorImg,Long[] character, 
			@RequestParam(value = "file", required = false) MultipartFile file,
			String fashionSwitchPic[],String fashionBigPic[],String fashionAmpPic[],
			Boolean[] isDefaults,Long[] colors,Long[] measures,Integer stockCounts[],
			Double[] salePrices,Double[] marketPrices,Double[] costPrices,Long ctgId,
			HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateSave(bean, file, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		productKeywords = StringUtils.replace(productKeywords, MessageResolver.getMessage(request, "product.keywords.split"), LIST_SPLIT);
		String[] keywords = StringUtils.split(productKeywords, LIST_SPLIT);
		Website web = com.jspgou.core.web.SiteUtils.getWeb(request);
		Map<String, String> exended = RequestUtils.getRequestMap(request, "exended_");
	    List li=new ArrayList(exended.keySet());
	    String[] names = new String[li.size()];
	    String[] values =  new String[li.size()];
	    if(stockCounts!=null){
	    	  Integer  stockCount =0;
	    	  for(Integer s:stockCounts){
	    		  stockCount+=s; 
	    	  }
	    	  bean.setStockCount(stockCount);
	    }
	    for(int i=0;i<li.size();i++){
	    	names[i]=(String) li.get(i);
	    	values[i]=exended.get(li.get(i));
	    }
	    bean.setAttr( RequestUtils.getRequestMap(request, "attr_"));
		bean = manager.save(bean, ext, web.getId(), categoryId, brandId, tagIds,
			keywords,names,values,fashionSwitchPic,fashionBigPic,fashionAmpPic,file);
       relatedgoodsMng.addProduct(bean.getId(),getProductIds(rightlist));
	   if(picture!=null){
			for(int i =0;i<picture.length;i++){
				ProductStandard ps=new ProductStandard();
				ps.setImgPath(colorImg[i]);
				ps.setType(standardMng.findById(picture[i]).getType());
				ps.setProduct(bean);
				ps.setStandard(standardMng.findById(picture[i]));
				ps.setDataType(true);
				productStandardMng.save(ps);
			}
		}
	   if(character!=null){
		   for(int i =0;i<character.length;i++){
				ProductStandard ps=new ProductStandard();
				ps.setType(standardMng.findById(character[i]).getType());
				ps.setProduct(bean);
				ps.setStandard(standardMng.findById(character[i]));
				ps.setDataType(false);
				productStandardMng.save(ps);
			}
	    }
	    saveProductFash(bean,nature,isDefaults,stockCounts,salePrices,marketPrices,costPrices);
	    
	  //生成商品时，生成对应的索引
	  		try {
	  			//添加对应的商品索引
	  			luceneProductSvc.createIndex(bean);
	  		} catch (IOException e) {
	  			// TODO Auto-generated catch block
	  			e.printStackTrace();
	  		}

		log.info("save Product. id={}", bean.getId());
		model.addAttribute("message", "global.success");
		model.addAttribute("brandId", brandId);
		return add(ctgId, request, model);
	}

	@RequiresPermissions("product:o_update")
	@RequestMapping("/product/o_update.do")
	public String update(Product bean, ProductExt ext,String rightlist, Long categoryId,Long brandId,
			Long[] tagIds,String productKeywords,String[] nature,Long[] picture,String[] colorImg,Long[] character,
			@RequestParam(value = "file", required = false) MultipartFile file,
			String fashionSwitchPic[],String fashionBigPic[],String fashionAmpPic[],
			Boolean[] isDefaults,Long[] colors,Long[] measures,Integer stockCounts[],
			Double[] salePrices,Double[] marketPrices,Double[] costPrices,Long[] fashId,
			Long ctgId,Integer pageNo,
			HttpServletRequest request, ModelMap model) {
		WebErrors errors = validateUpdate(bean.getId(), file, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		productKeywords = StringUtils.replace(productKeywords, MessageResolver
				.getMessage(request, "product.keywords.split"), LIST_SPLIT);
		String[] keywords = StringUtils.split(productKeywords, LIST_SPLIT);
		Map<String, String> exended = RequestUtils.getRequestMap(request, "exended_");
		List li=new ArrayList(exended.keySet());
		String[] names = new String[li.size()];
		String[] values =  new String[li.size()];
		for(int i=0;i<li.size();i++){
		    names[i]=(String) li.get(i);
		    values[i]=exended.get(li.get(i));
		}
		Map<String, String> attr = RequestUtils.getRequestMap(request, "attr_");
		 if(stockCounts!=null){
	    	  Integer  stockCount =0;
	    	  for(Integer s:stockCounts){
	    		  stockCount+=s; 
	    	  }
	    	  bean.setStockCount(stockCount);
	    }
		bean = manager.update(bean, ext, categoryId, brandId, tagIds, keywords,names,values,attr,
				fashionSwitchPic,fashionBigPic,fashionAmpPic,file);
        //修改相关商品
		if(relatedgoodsMng.findByIdProductId(bean.getId())!=null){			
			relatedgoodsMng.updateProduct(bean.getId(),getProductIds(rightlist));
		}
		List<ProductStandard> pcList=productStandardMng.findByProductId(bean.getId());
		for(int j=0;j<pcList.size();j++){
			productStandardMng.deleteById(pcList.get(j).getId());
		}
		if(picture!=null){
			for(int i =0;i<picture.length;i++){
				ProductStandard ps=new ProductStandard();
				ps.setImgPath(colorImg[i]);
				ps.setType(standardMng.findById(picture[i]).getType());
				ps.setProduct(bean);
				ps.setStandard(standardMng.findById(picture[i]));
				ps.setDataType(true);
				productStandardMng.save(ps);
			}
		}
	   if(character!=null){
		   for(int i =0;i<character.length;i++){
				ProductStandard ps=new ProductStandard();
				ps.setType(standardMng.findById(character[i]).getType());
				ps.setProduct(bean);
				ps.setStandard(standardMng.findById(character[i]));
				ps.setDataType(false);
				productStandardMng.save(ps);
			}
	   }	
	   try{
			if(bean.getCategory().getColorsize()){
				java.util.Set<ProductFashion> pfList = bean.getFashions();
				 
				if(fashId!=null){
					for(ProductFashion ps:pfList){
						if(!Arrays.asList(fashId).contains(ps.getId())){	
							fashMng.deleteById(ps.getId());
							cartItemMng.deleteByProductFactionId(ps.getId());		
						}	
					}					
				}else{
					for(ProductFashion ps:pfList){	
						fashMng.deleteById(ps.getId());
						cartItemMng.deleteByProductFactionId(ps.getId());			
					}			
				}
				updateProductFash(bean,fashId,nature,isDefaults,stockCounts,salePrices,marketPrices,costPrices);		
		   }
	    }catch(ObjectNotFoundException e) {
		}
	   catch(Exception e) {
			errors.addErrorString(manager.getTipFile("This.ChangeIsContainedInTheCaseOfTheDeletionOfTheGoods"));
			return errors.showErrorPage(model);	
		}	
	   try {
	    	//修改对应的商品索引
			luceneProductSvc.updateIndex(bean);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("update Product. id={}.", bean.getId());
		return get_list_and_title(ctgId,null,null,null, null,null,
				null,null,null,null,null,pageNo, request, model);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/product/o_delete.do")
	public String delete(Long[] ids, Long ctgId, Boolean isRecommend,
			Boolean isSpecial, Integer pageNo, HttpServletRequest request,
			ModelMap model) {
		WebErrors errors = validateDelete(ids, request);
		if (errors.hasErrors()) {
			return errors.showErrorPage(model);
		}
		Product[] beans;
		try {
			beans = manager.deleteByIds(ids);
			for (Product bean : beans) {
				
				//删除对应的商品索引
				luceneProductSvc.deleteIndex(bean);
				log.info("delete Product. id={}", bean.getId());
			}
		} catch (Exception e) {
			return "redirect:v_error.do";
		}
		return get_list_and_title(ctgId,null,isRecommend, isSpecial, null,null,null,null,null,null,null,pageNo, request, model);
	}
	
	
	@RequestMapping("/product/v_error.do")
	public String error(HttpServletRequest request, ModelMap model) {
		return "product/error";
	}
	
	@RequestMapping(value = "/product/v_standardTypes_add.do")
	public String standardTypesAdd(Long categoryId,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		    List<StandardType> standardTypeList = standardTypeMng.getList(categoryId);
		    model.addAttribute("standardTypeList", standardTypeList);
		    model.addAttribute("digit", standardTypeList.size());
		    response.setHeader("Cache-Control", "no-cache");
		    response.setContentType("text/json;charset=UTF-8");
		    return "product/standardTypes_add";
	}
	
	@RequestMapping(value = "/product/v_standards_add.do")
	public String standards(Long standardTypeId,HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws JSONException {
		    List<Standard> sList=standardMng.findByTypeId(standardTypeId);
		    model.addAttribute("sList", sList);
		    model.addAttribute("standardType", standardTypeMng.findById(standardTypeId));
		    response.setHeader("Cache-Control", "no-cache");
		    response.setContentType("text/json;charset=UTF-8");
		    return "product/standards_add";
	}

	@RequestMapping("/product/o_create_index.do")
	public String createIndex(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws ParseException {
		String path = servletContext.getRealPath(Constants.LUCENE_JSPGOU);
		boolean success = false;
		try {
			int count = luceneProductSvc.index(path, null, null, null);
			model.addAttribute("count", count);
			success = true;
		} catch (CorruptIndexException e) {
			log.error("", e);
		} catch (LockObtainFailedException e) {
			log.error("", e);
		} catch (IOException e) {
			log.error("", e);
		}
		model.addAttribute("success", success);
		return "product/create_index_result";
	}
	
	
	
	@RequestMapping("/product/o_delFashion.do")//异步删除商品款式(wangzewu)
	public String deleFashion(Long id,Long productId,HttpServletResponse response) throws JSONException{
		Boolean b=productFashionMng.getOneFash(productId);
		JSONObject j=new JSONObject();
		if(b!=null&&b){
			productFashionMng.deleteById(id);
			j.put("message", "删除成功！");
			j.put("boo", true);
				ResponseUtils.renderJson(response, j.toString());
				return null;
		}else{
			j.put("message", "必须留一款式！");
			j.put("boo", false);
			ResponseUtils.renderJson(response, j.toString());
			return null;
		}
	}
    @RequestMapping("/product/v_search.do")
	public void search(Long typeId,Long brandId,String productName,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {
		List<Product> list=manager.getList(typeId, brandId, productName);
		JSONObject json=new JSONObject();
		try{
			for(int i=0,j=list.size();i<j;i++){
				json.append(list.get(i).getId()+"", list.get(i).getName());
			}
		}catch(JSONException e){
			e.printStackTrace();
		}
		ResponseUtils.renderJson(response, json.toString());
	}

	public Long[] getProductIds(String rightlist){
		String[] arr;
		arr=rightlist.split(",");
		Long productIds[] =new Long[arr.length];
		for(int i=0,j=arr.length;i<j;i++){
			if(!arr[i].equals("")){
				productIds[i]=Long.parseLong(arr[i]);
			}
		}
		return productIds;
	}

	
	private void saveProductFash(Product bean,String[] nature,Boolean[] isDefaults,Integer[] stockCounts,
			Double[] salePrices,Double[] marketPrices,Double[] costPrices ){
		if(nature!=null){
			for(int i =0;i<nature.length;i++){
				String[] arr;
				ProductFashion pfash=new ProductFashion();
				pfash.setCreateTime(new Date());
				pfash.setIsDefault(isDefaults[i]);
				pfash.setStockCount(stockCounts[i]);
				pfash.setMarketPrice(marketPrices[i]);
				pfash.setSalePrice(salePrices[i]);
				pfash.setCostPrice(costPrices[i]);
				pfash.setProductId(bean);
				pfash.setNature(nature[i]);
				arr=nature[i].split("_");
				ProductFashion fashion = productFashionMng.save(pfash,arr);
				productFashionMng.addStandard(fashion,arr);
				if(isDefaults[i]){
					bean.setCostPrice(costPrices[i]);
					bean.setMarketPrice(marketPrices[i]);
					bean.setSalePrice(salePrices[i]);
					manager.update(bean);
				}
			}
		}
	}
	
	
	private void updateProductFash(Product bean,Long[] fashId,String[] nature,Boolean[] isDefaults,Integer[] stockCounts,
			Double[] salePrices,Double[] marketPrices,Double[] costPrices ){
		if(nature!=null){
			for(int i =0;i<nature.length;i++){
				String[] arr;
				ProductFashion pfash;
				if(fashId!=null&&i<fashId.length){
					pfash=productFashionMng.findById(fashId[i]);
					pfash.setCreateTime(new Date());
					pfash.setIsDefault(isDefaults[i]);
					pfash.setStockCount(stockCounts[i]);
					pfash.setMarketPrice(marketPrices[i]);
					pfash.setSalePrice(salePrices[i]);
					pfash.setCostPrice(costPrices[i]);
					pfash.setProductId(bean);
					pfash.setNature(nature[i]);
					arr=nature[i].split("_");
						productFashionMng.updateStandard(pfash, arr);
					if(isDefaults[i]){
						bean.setCostPrice(costPrices[i]);
						bean.setMarketPrice(marketPrices[i]);
						bean.setSalePrice(salePrices[i]);
						manager.update(bean);
					}
				}else{
					pfash = new ProductFashion();
					pfash.setCreateTime(new Date());
					pfash.setIsDefault(isDefaults[i]);
					pfash.setStockCount(stockCounts[i]);
					pfash.setMarketPrice(marketPrices[i]);
					pfash.setSalePrice(salePrices[i]);
					pfash.setCostPrice(costPrices[i]);
					pfash.setProductId(bean);
					pfash.setNature(nature[i]);
					arr=nature[i].split("_");
					ProductFashion fashion = productFashionMng.save(pfash,arr);
					productFashionMng.addStandard(fashion,arr);
					if(isDefaults[i]){
						bean.setCostPrice(costPrices[i]);
						bean.setMarketPrice(marketPrices[i]);
						bean.setSalePrice(salePrices[i]);
						manager.update(bean);
					}
				}
				
			}
		}
	}

	private WebErrors validateSave(Product bean, MultipartFile file,
			HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		if (file != null && !file.isEmpty()) {
			String name = file.getOriginalFilename();
			vldImage(name, errors);
		}
		bean.setWebsite(com.jspgou.core.web.SiteUtils.getWeb(request));
		return errors;
	}

	private WebErrors validateEdit(Long id, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		errors.ifNull(id, "id");
		vldExist(id, errors);
		return errors;
	}

	private WebErrors validateUpdate(Long id, MultipartFile file,
			HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		errors.ifNull(id, "id");
		if (file != null && !file.isEmpty()) {
			String name = file.getOriginalFilename();
			vldImage(name, errors);
			if (errors.hasErrors()) {
				return errors;
			}
		}
		vldExist(id, errors);
		return errors;
	}

	private WebErrors validateDelete(Long[] ids, HttpServletRequest request) {
		WebErrors errors = WebErrors.create(request);
		errors.ifEmpty(ids, "ids");
		for (Long id : ids) {
			vldExist(id, errors);
		}
		return errors;
	}

	private void vldExist(Long id, WebErrors errors) {
		if (errors.hasErrors()) {
			return;
		}
		Product entity = manager.findById(id);
		errors.ifNotExist(entity, Product.class, id);
	}

	private void vldImage(String filename, WebErrors errors) {
		if (errors.hasErrors()) {
			return;
		}
		String ext = FilenameUtils.getExtension(filename);
		if (!ImageUtils.isImage(ext)) {
			errors.addErrorString("not support image extension:" + filename);
		}
	}
	
	/**
	 * /*列表标题参数处理的前两个步骤：1先取传来的标题状态参数，2再存储参数；
	 * @param request
	 */
	private void get_set_title_status(HttpServletRequest request){
		/*列表标题参数处理的步骤：1先取传来的标题状态参数，2再存储参数，3最后取值返回*/
		//：1先取传来的标题状态参数
		String checked=request.getParameter("checked");//参数值
		String index=request.getParameter("index");//参数序号
		//2再存储参数
		if(StringUtils.isNotBlank(checked)&&StringUtils.isNotBlank(index)){
			if(index.equals("1")){
				this.title_id1=Boolean.parseBoolean(checked);
			}
			if(index.equals("2")){
				this.title_coverImg2=Boolean.parseBoolean(checked);
			}
			if(index.equals("3")){
				this.title_prdtName3=Boolean.parseBoolean(checked);
			}
			if(index.equals("4")){
				this.title_prdtCategory4=Boolean.parseBoolean(checked);
			}
			if(index.equals("5")){
				this.title_prdtType5=Boolean.parseBoolean(checked);
			}
			if(index.equals("6")){
				this.title_prdtSalePrice6=Boolean.parseBoolean(checked);
			}
			if(index.equals("7")){
				this.title_prdtStockCount7=Boolean.parseBoolean(checked);
			}
			if(index.equals("8")){
				this.title_prdtBrand8=Boolean.parseBoolean(checked);
			}
			if(index.equals("9")){
				this.title_prdtOnSale9=Boolean.parseBoolean(checked);
			}
			if(index.equals("10")){
				this.title_Operate10=Boolean.parseBoolean(checked);
			}
			
		}else{
			//当没有状态参数传来时取默认值
				this.title_id1=true;
				this.title_coverImg2=true;
				this.title_prdtName3=true;
				this.title_prdtCategory4=true;
				this.title_prdtType5=true;
				this.title_prdtSalePrice6=true;
				this.title_prdtStockCount7=true;
				this.title_prdtBrand8=true;
				this.title_prdtOnSale9=true;
				this.title_Operate10=true;
				
		}
	}
	
	@Autowired
	private StandardMng standardMng;
	@Autowired
	private StandardTypeMng standardTypeMng;
	@Autowired
	private ProductFashionMng fashMng;
	@Autowired
	private LuceneProductSvc luceneProductSvc;
	@Autowired
	private ProductTagMng productTagMng;
	@Autowired
    private RelatedgoodsMng relatedgoodsMng;
	@Autowired
	private CategoryMng categoryMng;
	@Autowired
	private ProductMng manager;
	@Autowired
	private ProductTypePropertyMng productTypePropertyMng;
	@Autowired
	private ProductFashionMng productFashionMng;
	@Autowired
	private ProductTypeMng productTypeMng;
	@Autowired
	private ExendedMng exendedMng;
	@Autowired
	private ProductStandardMng productStandardMng;
	@Autowired
	private CartItemMng cartItemMng; 
	@Autowired
	private BrandMng brandMng;
	private ServletContext servletContext;
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
}