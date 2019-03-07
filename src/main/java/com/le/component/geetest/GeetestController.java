package com.le.component.geetest;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 验证码（含图片、短信）
 * 
 * @author URON
 * 
 */
@Controller
@RequestMapping("/geetest")
public class GeetestController {

	@RequestMapping(value = "/register")
	@ResponseBody
	public Map<String, Object> register(HttpServletRequest request) {
		GeetestLib gtSdk = new GeetestLib(GeetestConfig.getGeetest_id(),
				GeetestConfig.getGeetest_key());
		String resStr = "{}";
		// 自定义userid
		String userid = "test";
		// 进行验证预处理
		int gtServerStatus = gtSdk.preProcess(userid);
		// 将服务器状态设置到session中
		request.getSession().setAttribute(gtSdk.gtServerStatusSessionKey,
				gtServerStatus);
		// 将userid设置到session中
		request.getSession().setAttribute("userid", userid);
		resStr = gtSdk.getResponseStr();
		JSONObject myObj = JSONObject.parseObject(resStr);
		Map<String, Object> respMsg = new HashMap<String, Object>();
		respMsg.put("success", myObj.get("success"));
		respMsg.put("gt", myObj.get("gt"));
		respMsg.put("challenge", myObj.get("challenge"));
		return respMsg;
	}
}
