package com.cym;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.List;

public class KillProcess {

	public static void main(String[] args) {
		String os = System.getProperty("os.name");
		if (os.toLowerCase().startsWith("win")) {
			System.out.println("不可在Windows下运行");
			return;
		}

		if (args == null || args.length == 0) {
			System.out.println("没有指定进程名");
		} else {
			String myPid = getProcessID();
			List<String> list = runShell("ps -ef");

			List<String> pids = new ArrayList<String>();
			for (String line : list) {
				if (line.contains(args[0])) {

					String[] strs = line.split("\\s+");
					if (!strs[1].equals(myPid)) {
						System.out.println(line);
						pids.add(strs[1]);
					}
				}
			}

			for (String pid : pids) {
				System.err.println("杀掉进程:" + pid);
				runShell("kill -9 " + pid);
			}

		}
	}

	public static final String getProcessID() {
		RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
		return runtimeMXBean.getName().split("@")[0];
	}

	/**
	 * 运行shell并获得结果，注意：如果sh中含有awk,一定要按new String[]{"/bin/sh","-c",shStr}写,才可以获得流
	 * 
	 * @param shStr 需要执行的shell
	 * @return
	 */
	public static List<String> runShell(String shStr) {
		List<String> strList = new ArrayList<String>();
		try {
			Process process = Runtime.getRuntime().exec(new String[] { "/bin/sh", "-c", shStr }, null, null);
			InputStreamReader ir = new InputStreamReader(process.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			String line;
			process.waitFor();
			while ((line = input.readLine()) != null) {
				strList.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strList;
	}
}