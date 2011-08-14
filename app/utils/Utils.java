package utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.Logger;
import play.Play;
import s3client.AbstractClient;
import s3client.Client;


public class Utils {

	public static void uploadImage(String name, byte[] imageBytes) {
		String s3AccessKey = Play.configuration.getProperty("s3AccesKey");
		String s3PrivateKey = Play.configuration.getProperty("privateKey");
		String s3Bucket = Play.configuration.getProperty("bucket");
		String type = "image/jpeg";
		
		AbstractClient client = new Client(s3AccessKey, s3PrivateKey);
		
		try {
			Map<String, List<String>> headers = new HashMap<String, List<String>>();
			headers.put("Cache-Control", Arrays.asList("max-age=24787379"));
			headers.put("Expires", Arrays.asList("never"));
			Logger.info("Uploading s3 content "+s3Bucket+" · "+name);
			client.putObject(s3Bucket , name, imageBytes, type, true, headers);
		} catch (IOException e) {
			e.printStackTrace();
		}
//		Socket s = null;
//		DataOutputStream out = null;
//		DataInputStream in = null;
//		try {
//			s = new Socket("http://nicolasgrenie.com/facelogin/images/"+name, 80);
//			out = new DataOutputStream(s.getOutputStream());
//			in = new DataInputStream(s.getInputStream());
//			out.write(imageBytes);
//			System.out.println("Response " + in.readUTF());
////			do {
////				utf = in.readUTF();
////				
////			} while(true);
//		} catch(IOException e) {
//			e.printStackTrace();
//		} finally {
//			if (in != null) {try {in.close();}catch(IOException e){}}
//			if (out != null) {try{out.close();}catch(IOException e){}}
//			if (s != null) {try{s.close();}catch(IOException e){}}
//		}
	}
}
