package utils;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import org.apache.log4j.Logger;

public class ClientUtils {
	private static Logger logger = Logger.getLogger(ClientUtils.class);

	private static final String IPV4_PATTERN = "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$";

	public static String DEFAULT_CLIENT_NAME = genClientName();

	public static String genClientName() {
		return getIpLAN() + "#" + getPid();
	}

	public static String getIpLAN() {
		try {
			Enumeration<NetworkInterface> netInterfaces = null;
			netInterfaces = NetworkInterface.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = netInterfaces.nextElement();
				Enumeration<InetAddress> ips = ni.getInetAddresses();
				while (ips.hasMoreElements()) {
					String ip = ips.nextElement().getHostAddress();
					if (ip.matches(IPV4_PATTERN) && !"127.0.0.1".equals(ip))
						return ip;
				}
			}
		} catch (Exception e) {
			logger.error("getIpLAN error!", e);
		}
		return "127.0.0.1";
	}

	public static int getPid() {
		try {
			RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
			String name = runtime.getName();
			return Integer.parseInt(name.substring(0, name.indexOf('@')));
		} catch (Exception e) {
			return -1;
		}
	}

	public static void main(String[] args) {
		System.out.println(ClientUtils.DEFAULT_CLIENT_NAME);
	}
}
