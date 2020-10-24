package com.cym.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Formatter;
import java.util.Optional;
import java.util.Properties;
import java.util.StringTokenizer;

import com.cym.ext.NetworkInfo;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;

public class NetWorkUtil {
	private static final int SLEEP_TIME = 2 * 1000;

	// 获取网络上行下行速度
	public static NetworkInfo getNetworkDownUp() {
		Properties props = System.getProperties();
		String os = props.getProperty("os.name").toLowerCase();
		os = os.startsWith("win") ? "windows" : "linux";
		Process pro = null;
		Runtime r = Runtime.getRuntime();
		BufferedReader input = null;
		NetworkInfo networkInfo = new NetworkInfo();

		try {
			String command = "windows".equals(os) ? "netstat -e" : "ifconfig";
			pro = r.exec(command);
			input = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			long result1[] = readInLine(input, "windows");
			Thread.sleep(SLEEP_TIME);
			pro.destroy();
			input.close();
			pro = r.exec(command);
			input = new BufferedReader(new InputStreamReader(pro.getInputStream()));
			long result2[] = readInLine(input, os);
			networkInfo.setReceive(formatNumber((result2[0] - result1[0]) / (1024.0 * (SLEEP_TIME / 1000)))); // 上行速率(kB/s)
			networkInfo.setSend(formatNumber((result2[1] - result1[1]) / (1024.0 * (SLEEP_TIME / 1000)))); // 下行速率(kB/s)

			// 去绝对值
			if (networkInfo.getReceive() < 0) {
				networkInfo.setReceive(0 - networkInfo.getReceive());
			}
			if (networkInfo.getSend() < 0) {
				networkInfo.setSend(0 - networkInfo.getSend());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			Optional.ofNullable(pro).ifPresent(p -> p.destroy());
		}
		networkInfo.setTime(DateUtil.format(new Date(), "HH:mm:ss"));
		return networkInfo;

	}

	private static long[] readInLine(BufferedReader input, String osType) {
		long arr[] = new long[2];
		StringTokenizer tokenStat = null;
		try {
			if (osType.equals("linux")) { // 获取linux环境下的网口上下行速率
				long rx = 0, tx = 0;
				String line = null;
				// 旧
				// RX packets:4171603 errors:0 dropped:0 overruns:0 frame:0
				// TX packets:4171603 errors:0 dropped:0 overruns:0 carrier:0
				// 新
				// RX packets 228451110  bytes 153707332334 (153.7 GB)
				// TX packets 169848511  bytes 155937621328 (155.9 GB)
				
				while ((line = input.readLine()) != null) {
					System.err.println(line);
					
					if (line.indexOf("RX packets") >= 0) {
						rx += Long.parseLong(line.split(" ")[2]);
						System.err.println(rx);
					} else if (line.indexOf("TX packets") >= 0) {
						tx += Long.parseLong(line.split(" ")[2]);
						System.err.println(tx);
					}
				}
				
				arr[0] = rx;
				arr[1] = tx;
			} else { // 获取windows环境下的网口上下行速率
				input.readLine();
				input.readLine();
				input.readLine();
				input.readLine();
				tokenStat = new StringTokenizer(input.readLine());
				tokenStat.nextToken();
				arr[0] = Long.parseLong(tokenStat.nextToken());
				arr[1] = Long.parseLong(tokenStat.nextToken());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arr;
	}

	private static Double formatNumber(double f) {
		return Double.parseDouble(new Formatter().format("%.2f", f).toString());
	}

	public static void main(String[] args) {
		NetworkInfo result = getNetworkDownUp();
		System.out.println("recieve:" + result.getReceive() + "kB/s");
		System.out.println("send:" + result.getSend() + "kB/s");

	}
}
