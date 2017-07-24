package com.jspgou.cms;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.SignatureException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.jspgou.common.httpClient.HttpProtocolHandler;
import com.jspgou.common.httpClient.HttpRequest;
import com.jspgou.common.httpClient.HttpResponse;
import com.jspgou.common.httpClient.HttpResultType;
import com.jspgou.common.util.Num62;


/* *
 *类名：AlipaySubmit
 *功能：支付宝各接口请求提交类
 *详细：构造支付宝各接口表单HTML文本，获取远程HTTP数据
 */
public class Alipay {
    
    /**
     * 支付宝提供给商户的服务接入网关URL(新)
     */
    private static final String ALIPAY_GATEWAY_NEW = "https://mapi.alipay.com/gateway.do?";
    
    /**
     * 支付宝消息验证地址
     */
    private static final String HTTPS_VERIFY_URL = "https://mapi.alipay.com/gateway.do?service=notify_verify&";

	
    /**
     * 生成签名结果
     * @param sPara 要签名的数组
     * @return 签名结果字符串
     */
	public static String buildRequestMysign(Map<String, String> sPara,String key) {
    	String prestr = createLinkString(sPara); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String mysign = "";
        mysign = sign(prestr, key, "utf-8");
        return mysign;
    }
	
    /**
     * 生成要请求给支付宝的参数数组
     * @param sParaTemp 请求前的参数数组
     * @return 要请求的参数数组
     */
     private static Map<String, String> buildRequestPara(Map<String, String> sParaTemp,String key) {
        //除去数组中的空值和签名参数
        Map<String, String> sPara = paraFilter(sParaTemp);
        //生成签名结果
        String mysign = buildRequestMysign(sPara,key);
        //签名结果与签名方式加入请求提交参数组中
        sPara.put("sign", mysign);
        sPara.put("sign_type", "MD5");
        return sPara;
    }   

    /**
     * 建立请求，以表单HTML形式构造（默认）
     * @param sParaTemp 请求参数数组
     * @param strMethod 提交方式。两个值可选：post、get
     * @param strButtonName 确认按钮显示文字
     * @return 提交表单HTML文本
     */
    public static String buildRequest(Map<String, String> sParaTemp,String key, String strMethod, String strButtonName) {
    //待请求参数数组
        Map<String, String> sPara = buildRequestPara(sParaTemp,key);
        List<String> keys = new ArrayList<String>(sPara.keySet());
        StringBuffer sbHtml = new StringBuffer();
        sbHtml.append("<form id=\"alipaysubmit\" name=\"alipaysubmit\" action=\"" + ALIPAY_GATEWAY_NEW + "_input_charset=" + "utf-8" + "\" method=\"" + strMethod + "\">");
        for (int i = 0; i < keys.size(); i++) {
            String name = keys.get(i);
            String value = sPara.get(name);
            sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
        }
        //submit按钮控件请不要含有name属性
        sbHtml.append("<input type=\"submit\" value=\"" + strButtonName + "\" style=\"display:none;\"></form>");
        sbHtml.append("<script>document.forms['alipaysubmit'].submit();</script>");
        return sbHtml.toString();
    }
    
    /**
     * 建立请求，以模拟远程HTTP的POST请求方式构造并获取支付宝的处理结果
     * 如果接口中没有上传文件参数，那么strParaFileName与strFilePath设置为空值
     * 如：buildRequest("", "",sParaTemp)
     * @param strParaFileName 文件类型的参数名
     * @param strFilePath 文件路径
     * @param sParaTemp 请求参数数组
     * @return 支付宝处理结果
     * @throws Exception
     */
    public static String buildRequest(String strParaFileName, String strFilePath,Map<String, String> sParaTemp,String key) throws Exception {
        //待请求参数数组
        Map<String, String> sPara = buildRequestPara(sParaTemp,key);
        HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler.getInstance();
        HttpRequest request = new HttpRequest(HttpResultType.BYTES);
        //设置编码集
        request.setCharset("utf-8");
        request.setParameters(generatNameValuePair(sPara));
        request.setUrl(ALIPAY_GATEWAY_NEW+"_input_charset="+"utf-8");
        HttpResponse response = httpProtocolHandler.execute(request,strParaFileName,strFilePath);
        if (response == null) {
            return null;
        } 
        String strResult = response.getStringResult();
        return strResult;
    }
    
    
    /**
     * MAP类型数组转换成NameValuePair类型
     * @param properties  MAP类型数组
     * @return NameValuePair类型数组
     */
    private static NameValuePair[] generatNameValuePair(Map<String, String> properties) {
        NameValuePair[] nameValuePair = new NameValuePair[properties.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            nameValuePair[i++] = new NameValuePair(entry.getKey(), entry.getValue());
        }
        return nameValuePair;
    }
    
    
    /**
     * 验证消息是否是支付宝发出的合法消息
     * @param params 通知返回来的参数数组
     * @return 验证结果
     */
    public static boolean verify(Map<String, String> params,String partner,String key) {
        //判断responsetTxt是否为true，isSign是否为true
        //responsetTxt的结果不是true，与服务器设置问题、合作身份者ID、notify_id一分钟失效有关
        //isSign不是true，与安全校验码、请求时的参数格式（如：带自定义参数等）、编码格式有关
    	String responseTxt = "true";
		if(params.get("notify_id") != null) {
			String notify_id = params.get("notify_id");
			responseTxt = verifyResponse(notify_id,partner);
		}
	    String sign = "";
	    if(params.get("sign") != null) {sign = params.get("sign");}
	    boolean isSign = getSignVeryfy(params, sign,key);
        //写日志记录（若要调试，请取消下面两行注释）
        //String sWord = "responseTxt=" + responseTxt + "\n isSign=" + isSign + "\n 返回回来的参数：" + AlipayCore.createLinkString(params);
	    //AlipayCore.logResult(sWord);
        if (isSign && responseTxt.equals("true")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据反馈回来的信息，生成签名结果
     * @param Params 通知返回来的参数数组
     * @param sign 比对的签名结果
     * @return 生成的签名结果
     */
	private static boolean getSignVeryfy(Map<String, String> Params, String sign,String key) {
    	//过滤空值、sign与sign_type参数
    	Map<String, String> sParaNew = paraFilter(Params);
        //获取待签名字符串
        String preSignStr = createLinkString(sParaNew);
        //获得签名验证结果
        boolean isSign = false;
        isSign = verify(preSignStr, sign,key, "utf-8");
        return isSign;
    }

    /**
    * 获取远程服务器ATN结果,验证返回URL
    * @param notify_id 通知校验ID
    * @return 服务器ATN结果
    * 验证结果集：
    * invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 
    * true 返回正确信息
    * false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
    */
    private static String verifyResponse(String notify_id,String partner) {
        //获取远程服务器ATN结果，验证是否是支付宝服务器发来的请求
        String veryfy_url = HTTPS_VERIFY_URL + "partner=" + partner + "&notify_id=" + notify_id;
        return checkUrl(veryfy_url);
    }

    /**
    * 获取远程服务器ATN结果
    * @param urlvalue 指定URL路径地址
    * @return 服务器ATN结果
    * 验证结果集：
    * invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 
    * true 返回正确信息
    * false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
    */
    private static String checkUrl(String urlvalue) {
        String inputLine = "";
        try {
            URL url = new URL(urlvalue);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection
                .getInputStream()));
            inputLine = in.readLine().toString();
        } catch (Exception e) {
            e.printStackTrace();
            inputLine = "";
        }
        return inputLine;
    }

    /** 
     * 除去数组中的空值和签名参数
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> paraFilter(Map<String, String> sArray) {
        Map<String, String> result = new HashMap<String, String>();
        if (sArray == null || sArray.size() <= 0) {
            return result;
        }
        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")
                || key.equalsIgnoreCase("sign_type")) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }

    /** 
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }
    
    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static String sign(String text, String key, String input_charset) {
    	text = text + key;
        return DigestUtils.md5Hex(getContentBytes(text, input_charset));
    }
    
    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param sign 签名结果
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static boolean verify(String text, String sign, String key, String input_charset) {
    	text = text + key;
    	String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));
    	if(mysign.equals(sign)) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws SignatureException
     * @throws UnsupportedEncodingException 
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }
    
///////////////////////////////////////微信扫码支付方法（开始）//////////////////////////////////////////////////
	private static final int BLACK = 0xFF000000;//黑色
	private static final int WHITE = 0xFFFFFFFF;//白色
	public static final String characterEncodingUTF8 = "UTF-8";
	public static final String characterEncodingGBK = "GBK";
	public static final String characterEncodingISO = "ISO-8859-1";
	/**
	* 微信统一下单接口
	*/
	public static final String UNIFORM_SINGLE_INTERFACE="https://api.mch.weixin.qq.com/pay/unifiedorder";
	/**
	* 微信支付页面
	*/
	public static final String WECHATPAYMENT="tpl.weChatPayment";
	
	/**
	* 日期格式化对象，将当前日期格式转化为ddHHmmss格式，用于生成随机数
	*/
	public static final DateFormat nameDf =new SimpleDateFormat("ddHHmmss");
	
	/**
	* 微信支付生成随机数，长度不超36位
	*/
	public static String getWCRandomNumber(Integer num){
		return nameDf.format(new Date())+RandomStringUtils.random(num,Num62.N62_CHARS);
	}
	
	/**
	* 微信支付签名sign
	* @param characterEncoding
	* @param param
	* @param key
	* @return
	*/
	@SuppressWarnings("unchecked")
	public static String createSign(Map<String, String> param,String key){
		//签名步骤一：按字典排序参数
		List list=new ArrayList(param.keySet());
		Object[] ary =list.toArray();
		Arrays.sort(ary);
		list=Arrays.asList(ary);
		String str="";
		for(int i=0;i<list.size();i++){
			str+=list.get(i)+"="+param.get(list.get(i)+"")+"&";
		}
		//签名步骤二：加上key
		str+="key="+key;
		//步骤三：加密并大写
		str=MD5Encode(str,characterEncodingUTF8).toUpperCase();
		return str;
	}
		
	public static String MD5Encode(String origin,String charsetName){
		String resultString=null;
		try{
			resultString=new String(origin);
			MessageDigest md=MessageDigest.getInstance("MD5");
			if(charsetName==null||"".equals(charsetName)){
				resultString=byteArrayToHexString(md.digest(resultString.getBytes()));
			}else{
				resultString=byteArrayToHexString(md.digest(resultString.getBytes(charsetName)));
			}
		}catch(Exception e){
		
		}
		return resultString;
	}
	
	private static String byteArrayToHexString(byte b[]){
		StringBuffer resultSb=new StringBuffer();
		for(int i=0;i<b.length;i++){
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}
	
	private static String byteToHexString(byte b){
		int n=b;
		if(n<0){
			n+=256;
		}
		int d1=n/16;
		int d2=n%16;
		return hexDigits[d1]+hexDigits[d2];
	}
	
	private static final String hexDigits[]={ "0", "1", "2", "3", "4", "5",  
		"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	
	/**
	* 利用微信“统一下单”接口获取返回的数据 
	* @param urlStr
	* @param xmlInfo
	* @return
	*/
	public static String testPost(String urlStr,String xmlInfo) {
	String line1 = "";  
	try {  
		URL url = new URL(urlStr);  
		URLConnection con = url.openConnection();  
		con.setDoOutput(true);  
//		con.setRequestProperty("Pragma:", "no-cache");  
		con.setRequestProperty("Cache-Control", "no-cache");  
		con.setRequestProperty("Content-Type", "text/xml");  
	
		OutputStreamWriter out = new OutputStreamWriter(con  
		.getOutputStream());           
		out.write(new String(xmlInfo.getBytes(characterEncodingUTF8)));  
		out.flush();  
		out.close();  
		BufferedReader br = new BufferedReader(new InputStreamReader(con  
		.getInputStream()));  
		String line = "";  
		for (line = br.readLine(); line != null; line = br.readLine()) {  
			line1+=line;  
		} 
		//return line1;
		return new String(line1.getBytes(),characterEncodingUTF8);

		} catch (MalformedURLException e) {  
			e.printStackTrace();  
		} catch (IOException e) {  
			e.printStackTrace();  
		}
		return null;  
	}  
	
	/**
	* 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
	* @param strxml
	* @return
	* @throws JDOMException
	* @throws IOException
	*/
	public static Map doXMLParse(String strxml) throws JDOMException, IOException {
		strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\""+characterEncodingUTF8+"\"");   	 
		if(null == strxml || "".equals(strxml)) {
			return null;
		}
		Map m = new HashMap();
		InputStream in = new ByteArrayInputStream(strxml.getBytes(characterEncodingUTF8));
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(in);
		Element root = doc.getRootElement();
		List list = root.getChildren();
		Iterator it = list.iterator();
		while(it.hasNext()) {
			Element e = (Element) it.next();
			String k = e.getName();
			String v = "";
			List children = e.getChildren();
			if(children.isEmpty()) {
				v = e.getTextNormalize();
			} else {
				v =getChildrenText(children);
			}
			m.put(k, v);
		}
		//关闭流
		in.close();
		return m;
	}
	
	/**
	* 获取子结点的xml
	* @param children
	* @return String
	*/
	public static String getChildrenText(List children) {
		StringBuffer sb = new StringBuffer();
		if(!children.isEmpty()) {
			Iterator it = children.iterator();
			while(it.hasNext()) {
				Element e = (Element) it.next();
				String name = e.getName();
				String value = e.getTextNormalize();
				List list = e.getChildren();
				sb.append("<" + name + ">");
				if(!list.isEmpty()) {
					sb.append(getChildrenText(list));
				}
				sb.append(value);
				sb.append("</" + name + ">");
			}
		}
		return sb.toString();
	}
	
	/**
	* 将需要传递给微信的参数转成xml格式
	* @param parameters
	* @return
	*/
	public static String getRequestXml(Map<String,String> parameters){
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			String k=(String)entry.getKey();
			String v=(String) entry.getValue();
			if ("attach".equalsIgnoreCase(k)||"body".equalsIgnoreCase(k)||"sign".equalsIgnoreCase(k)) {
				sb.append("<"+k+">"+"<![CDATA["+v+"]]></"+k+">");
			}else {
				sb.append("<"+k+">"+v+"</"+k+">");
			}
		}
		sb.append("</xml>");
		return sb.toString();
	}

	public static void noticeWeChatSuccess(){
		Map<String,String> parames=new HashMap<String,String>();
		parames.put("return_code", "SUCCESS");
		parames.put("return_msg", "OK");
		//将参数转成xml格式
		String xmlWeChat = getRequestXml(parames);
		try{    		
			testPost(UNIFORM_SINGLE_INTERFACE,xmlWeChat);
			System.out.println("告诉微信不要再回调了");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
    /**   
     * 将元为单位的转换为分 替换小数点，支持以逗号区分的金额  
     *   
     * @param amount  
     * @return  
     */    
    public static String changeY2F(Double amount){    
        String currency =  amount.toString();  
        int index = currency.indexOf(".");    
        int length = currency.length();    
        Long amLong = 0l;    
        if(index == -1){    
            amLong = Long.valueOf(currency+"00");    
        }else if(length - index >= 3){    
            amLong = Long.valueOf((currency.substring(0, index+3)).replace(".", ""));    
        }else if(length - index == 2){    
            amLong = Long.valueOf((currency.substring(0, index+2)).replace(".", "")+0);    
        }else{    
            amLong = Long.valueOf((currency.substring(0, index+1)).replace(".", "")+"00");    
        }    
        return amLong.toString();    
    }    
        
    public static void main(String[] args) {       
        System.out.println(changeY2F(0.01d));    
        try {  
        } catch (Exception e) {  
            e.printStackTrace();  
        }           
    }    
	
	/**
	 * 字符转码，gbk转成utf-8
	 * @param name
	 * @return
	 */
	public static String CharacterTranscodingGbkToUtf8(String name){
		String str = null;
		try {
			str = new String((name).getBytes(characterEncodingGBK),characterEncodingUTF8);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
	
	/**
	 * 字符转码，ISO-8859-1转成utf-8
	 * @param name
	 * @return
	 */
	public static String CharacterTranscodingIsoToUtf8(String name){
		String str = null;
		try {
			str = new String((name).getBytes(characterEncodingISO),characterEncodingUTF8);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
	
	/**
	* 生成二维码图片并直接以流的形式输出到页面
	* @param code_url
	* @param response
	*/
	@RequestMapping(value="qr_code.jspx")
	@ResponseBody
	public static void getQRCode(String code_url,HttpServletResponse response){
		encodeQrcode(code_url, response);
	}
	
	/**
	* 生成二维码图片 不存储 直接以流的形式输出到页面
	* @param content
	* @param response
	*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void encodeQrcode(String content,HttpServletResponse response){
		if(StringUtils.isBlank(content)){        	   
			return;
		}
		MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		Map hints = new HashMap();
		hints.put(EncodeHintType.CHARACTER_SET, characterEncodingUTF8); //设置字符集编码类型
		BitMatrix bitMatrix = null;
		try {
			bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 300, 300,hints);
			BufferedImage image = toBufferedImage(bitMatrix);
			//输出二维码图片流
			try {
				ImageIO.write(image, "png", response.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();	
			} 
		} catch (WriterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}      
	}
	
	public static BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();  
		BufferedImage image = new BufferedImage(width, height,
		BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE); 
			}
		}  
		return image;	  
	}
	
	/**
	* 判断是否来自微信, 5.0 之后的支持微信支付
	*
	* @param request
	* @return
	*/
	public static boolean isWeiXin(HttpServletRequest request) {
		String userAgent = request.getHeader("User-Agent");
		if (StringUtils.isNotBlank(userAgent)) {
			Pattern p = Pattern.compile("MicroMessenger/(\\d+).+");
			Matcher m = p.matcher(userAgent);
			String version = null;
			if (m.find()) {
				version = m.group(1);
			}
			return (null != version && NumberUtils.toInt(version) >= 5);
		}
		return false;
	}
	
	/**
	* 处理xml请求信息
	* @param reequest
	* @return
	*/
	public static String getXmlRequest(HttpServletRequest request){
		BufferedReader bis=null;
		String result="";
		try{
			bis=new BufferedReader(new InputStreamReader(request.getInputStream()));
			String line=null;
			while((line=bis.readLine())!=null){
				result+=line;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(bis!=null){
				try{
					bis.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
		return result;
	}
//////////////////////////////////////结束//////////////////////////////////////////////////////

}
