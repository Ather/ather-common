/**
 * 
 */
package media.thehoard.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

import media.thehoard.hoardmediaserver.common.configuration.Configuration;

/**
 * @author Michael Haas
 *
 */
public class Network {
	@Deprecated
	public static String parseCallbackAddress() {
		String test = Configuration.getBindAddress();
		if (test.equals("127.0.0.1") || test.equals("localhost"))
			return test;
		if (test.matches("^.[0-9]{1,3}/..[0-9]{1,3}/..[0-9]{1,3}/..[0-9]{1,3}"))
			return "127.0.0.1";
		
		return test;
	}
	
	@Deprecated
	public static String parseAddress() {
		String test = Configuration.getBindAddress();
		if (test.equals("127.0.0.1") || test.equals("localhost"))
			return getExternalAddress();
		if (test.matches("^.[0-9]{1,3}/..[0-9]{1,3}/..[0-9]{1,3}/..[0-9]{1,3}"))
			return test;
		
		try {
			return InetAddress.getByName(test).getHostAddress();
		} catch (UnknownHostException e) {
			//TODO Create HoardException
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getExternalAddress() {
		try {
			return new BufferedReader(new InputStreamReader(new URL("http://checkip.amazonaws.com").openStream())).readLine();
		} catch (IOException e) {
			return "127.0.0.1";
		}
	}
}
