package edu.vt.ece4564.cmdTool;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class main {
	// currently only accepts one line of commands
	
	public static void main(String[] args) {
		try{
			String inputCmd;
			
			if (args.length != 0){
				inputCmd = args[0];
			}else inputCmd = "/c dir";
			
			
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
				proc = run.exec("/usr/bin/open -a Terminal /path/to/the/executable" + inputCmd);
			}else proc = run.exec("/usr/bin/xterm " + inputCmd);
			
			InputStream in = proc.getInputStream();
			String line = "";
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder sb = new StringBuilder();
			
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
				System.out.println(line);
			}
			
			String response = sb.toString();
			in.close();
			
			// waitFor outside program to finish
			int end = proc.waitFor();
			System.out.println("Exit Error: " + end);
		} catch(Exception e){
			e.printStackTrace();
			System.out.println(e.toString());
		}

	}

}
