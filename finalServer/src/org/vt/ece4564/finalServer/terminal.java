package org.vt.ece4564.finalServer;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.json.*;

public class terminal extends HttpServlet{
	
	JSONArray jArray = new JSONArray();

	public static void main(String[] args) throws Exception{
		Server server = new Server(8080);

		WebAppContext context = new WebAppContext();
		context.setWar("war");
		context.setContextPath("/terminal");
		server.setHandler(context);

		server.start();
		server.join();
	}
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().write(jArray.toString());
	}
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		
		String cmd = req.getParameter("cmd");

		JSONObject jObj = new JSONObject();
		try {
			jObj.put("cmd", cmd);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resp.getWriter().write(e.getMessage());
		}
		
		jArray.put(jObj);	
		resp.getWriter().write("Accepted Command");
	}
}
