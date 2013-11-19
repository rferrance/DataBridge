package org.vt.ece4564.finalServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class cli extends HttpServlet{
	
	String cmd = "/c dir";
	String output = " ";

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		output = " ";

		String update = sendCmd(cmd);
		
		if(output == " "){
			resp.getWriter().write(update);
		}else{
			resp.getWriter().write(output);
		}
	}
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//		resp.setContentType("text/plain");
//		output = " ";
//
//		cmd = req.getParameter("cmd");
//		String update = sendCmd(cmd);
//		
//		if(output == " "){
//			resp.getWriter().write(update);
//		}else{
//			resp.getWriter().write(output);
//		}
	}
	
	protected String sendCmd(String command){
		try{
			String inputCmd;
			
			inputCmd = cmd;
			
			// store information about OS running so know which command to run
	        String OSname = System.getProperty("os.name");        
	        String OSver = System.getProperty("os.version");        
	        String OSarch = System.getProperty("os.arch");
			
			// connect runtime object to a cmd.exe system process
			Runtime run = Runtime.getRuntime();
			Process proc;
			
			if(OSname.contains("Windows")){
				proc = run.exec("cmd " + inputCmd);
			}else if (OSname.contains("Mac")){
				proc = run.exec("/usr/bin/open -a Terminal " + inputCmd);
			}else proc = run.exec("/usr/bin/xterm " + inputCmd);
			
			InputStream in = proc.getInputStream();
			String line = "";
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder sb = new StringBuilder();
			
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			
			output = sb.toString();
			in.close();
			
			// waitFor outside program to finish
			int end = proc.waitFor();
			return ("Exit Error: " + end);
		} catch(Exception e){
			e.printStackTrace();
			return (e.toString());
		}

	}
}
