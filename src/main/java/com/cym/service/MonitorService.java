package com.cym.service;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.cym.model.MonitorInfo;
import com.sun.management.OperatingSystemMXBean;

import cn.hutool.core.util.NumberUtil;

/** */
/**
 * 获取系统信息的业务逻辑实现类.
 * 
 * @author amg * @version 1.0 Creation date: 2008-3-11 - 上午10:06:06
 */
@Service
public class MonitorService {

	OperatingSystemMXBean osmxb;

	@PostConstruct
	private void init() {
		osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
	}

	/** */
	/**
	 * 获得当前的监控对象.
	 * 
	 * @return 返回构造好的监控对象
	 * @throws Exception
	 * @author amg * Creation date: 2008-4-25 - 上午10:45:08
	 */
	public MonitorInfo getMonitorInfo() {
		Double gb = Double.valueOf(1024 * 1024 * 1024);

		// 总的物理内存
		Double totalMemorySize = osmxb.getTotalPhysicalMemorySize() / gb;
		// 剩余的物理内存
		Double freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize() / gb;
		// 已使用的物理内存
		Double usedMemory = (osmxb.getTotalPhysicalMemorySize() - osmxb.getFreePhysicalMemorySize()) / gb;

		double cpu = osmxb.getSystemCpuLoad();
		double mem = usedMemory / totalMemorySize;
		// 构造返回对象
		MonitorInfo infoBean = new MonitorInfo();
		infoBean.setFreePhysicalMemorySize(NumberUtil.decimalFormat("#.00GB", freePhysicalMemorySize));
		infoBean.setTotalMemorySize(NumberUtil.decimalFormat("#.00GB", totalMemorySize));
		infoBean.setUsedMemory(NumberUtil.decimalFormat("#.00GB", usedMemory));
		infoBean.setCpuRatio(NumberUtil.decimalFormat("#.##%", cpu));
		infoBean.setMemRatio(NumberUtil.decimalFormat("#.##%", mem));

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		
		return infoBean;
	}

	/** */
	/**
	 * 测试方法.
	 * 
	 * @param args
	 * @throws Exception
	 * @author amg * Creation date: 2008-4-30 - 下午04:47:29
	 */
	public static void main(String[] args) throws Exception {

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		File[] roots = File.listRoots();// 获取磁盘分区列表
		for (File file : roots) {
			Map<String, String> map = new HashMap<String, String>();

			long freeSpace = file.getFreeSpace();
			long totalSpace = file.getTotalSpace();
			long usableSpace = totalSpace - freeSpace;

			map.put("path", file.getPath());
			map.put("freeSpace", freeSpace / 1024 / 1024 / 1024 + "G");// 空闲空间
			map.put("usableSpace", usableSpace / 1024 / 1024 / 1024 + "G");// 可用空间
			map.put("totalSpace", totalSpace / 1024 / 1024 / 1024 + "G");// 总空间
			map.put("percent", NumberUtil.decimalFormat("#.##%", (double) usableSpace / (double) totalSpace));// 总空间

			list.add(map);
		}
		System.out.println(list);

	}
}
