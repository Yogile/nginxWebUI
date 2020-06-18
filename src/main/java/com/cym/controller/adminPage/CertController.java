package com.cym.controller.adminPage;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cym.config.InitConfig;
import com.cym.model.Cert;
import com.cym.service.SettingService;
import com.cym.utils.BaseController;
import com.cym.utils.JsonResult;
import com.cym.utils.SystemTool;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;

@Controller
@RequestMapping("/adminPage/cert")
public class CertController extends BaseController {
	@Autowired
	SettingService settingService;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	Boolean isInApply = false;

	@RequestMapping("")
	public ModelAndView index(HttpSession httpSession, ModelAndView modelAndView) {
		List<Cert> certs = sqlHelper.findAll(Cert.class);

		modelAndView.addObject("certs", certs);
		modelAndView.setViewName("/adminPage/cert/index");
		return modelAndView;
	}

	@RequestMapping("addOver")
	@ResponseBody
	public JsonResult addOver(Cert cert) {
		sqlHelper.insertOrUpdate(cert);
		return renderSuccess();
	}

	@RequestMapping("setAutoRenew")
	@ResponseBody
	public JsonResult setAutoRenew(Cert cert) {
		sqlHelper.updateById(cert);
		return renderSuccess();
	}

	@RequestMapping("detail")
	@ResponseBody
	public JsonResult detail(String id) {
		return renderSuccess(sqlHelper.findById(id, Cert.class));
	}

	@RequestMapping("del")
	@ResponseBody
	public JsonResult del(String id) {
		Cert cert = sqlHelper.findById(id, Cert.class);
		if (cert.getKey() != null) {
			File file = new File(cert.getKey());
			// 删除域名所在文件夹
			FileUtil.del(file.getParent());
		}

		
		sqlHelper.deleteById(id, Cert.class);
		return renderSuccess();
	}

	@RequestMapping("apply")
	@ResponseBody
	public JsonResult apply(String id) {
		if (SystemTool.getSystem().equals("Windows")) {
			return renderError("证书操作只能在linux下进行");
		}

		Cert cert = sqlHelper.findById(id, Cert.class);
		if (cert.getMakeTime() != null) {
			return renderError("该证书已申请");
		}

		if (cert.getDnsType() == null) {
			return renderError("该证书还未设置DNS服务商信息");
		}

		if (isInApply) {
			return renderError("另一个申请进程正在进行，请稍后再申请");
		}
		isInApply = true;

		String rs = "";
		try {
			// 设置环境变量
			setEnv(cert);

			// 申请
			String cmd = InitConfig.acmeSh + " --issue --dns dns_ali -d " + cert.getDomain();
			logger.info(cmd);
			rs = RuntimeUtil.execForStr(cmd);
			logger.info(rs);

		} catch (Exception e) {
			e.printStackTrace();
			rs = e.getMessage();
		}
		if (rs.contains("key ok")) {
			String certDir = "/root/.acme.sh/" + cert.getDomain() + "/";

			String dest = InitConfig.home + "cert/" + cert.getDomain() + ".cer";
			FileUtil.copy(new File(certDir + cert.getDomain() + ".cer"), new File(dest), true);
			cert.setPem(dest);

			dest = InitConfig.home + "cert/" + cert.getDomain() + ".key";
			FileUtil.copy(new File(certDir + cert.getDomain() + ".key"), new File(dest), true);
			cert.setKey(dest);

			cert.setMakeTime(System.currentTimeMillis());
			sqlHelper.updateById(cert);

			isInApply = false;
			return renderSuccess();
		} else {

			isInApply = false;
			return renderError(rs.replace("\n", "<br>"));
		}

	}

	@RequestMapping("renew")
	@ResponseBody
	public JsonResult renew(String id) {
		if (SystemTool.isWindows()) {
			return renderError("证书操作只能在linux下进行");
		}

		Cert cert = sqlHelper.findById(id, Cert.class);
		if (cert.getMakeTime() == null) {
			return renderError("该证书还未申请");
		}
		if (cert.getDnsType() == null) {
			return renderError("该证书还未设置DNS服务商信息");
		}

		if (isInApply) {
			return renderError("另一个申请进程正在进行，请稍后再申请");
		}
		isInApply = true;

		String rs = "";
		try {
			// 设置环境变量
			setEnv(cert);

			// 续签
			String cmd = InitConfig.acmeSh + " --renew --force -d " + cert.getDomain();
			logger.info(cmd);
			rs = RuntimeUtil.execForStr(cmd);
			logger.info(rs);
		} catch (Exception e) {
			e.printStackTrace();
			rs = e.getMessage();
		}

		if (rs.contains("key ok")) {
			String certDir = "/root/.acme.sh/" + cert.getDomain() + "/";

			String dest = InitConfig.home + "cert/" + cert.getDomain() + ".cer";
			FileUtil.copy(new File(certDir + cert.getDomain() + ".cer"), new File(dest), true);
			cert.setPem(dest);

			dest = InitConfig.home + "cert/" + cert.getDomain() + ".key";
			FileUtil.copy(new File(certDir + cert.getDomain() + ".key"), new File(dest), true);
			cert.setKey(dest);

			cert.setMakeTime(System.currentTimeMillis());
			sqlHelper.updateById(cert);

			isInApply = false;
			return renderSuccess();
		} else {

			isInApply = false;
			return renderError(rs.replace("\n", "<br>"));
		}

	}


	private void setEnv(Cert cert) {
		if(cert.getDnsType().equals("ali")) {
			RuntimeUtil.execForStr(new String[] { "source", "export Ali_Key=\"" + cert.getAliKey() + "\"" });
			RuntimeUtil.execForStr(new String[] { "source", "export Ali_Secret=\"" + cert.getAliSecret() + "\"" });
		}
		if(cert.getDnsType().equals("dp")) {
			RuntimeUtil.execForStr(new String[] { "source", "export DP_Id=\"" + cert.getDpId() + "\"" });
			RuntimeUtil.execForStr(new String[] { "source", "export DP_Key=\"" + cert.getDpKey() + "\"" });
		}
	}
}
