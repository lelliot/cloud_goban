package org.ligi.cloudgoban;

import java.io.IOException;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GameHttpServlet extends HttpServlet {

	private static final long serialVersionUID = -1115978060338288278L;

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		String key = req.getPathInfo().replace("/", "");
		Game game;
		String debug = "";
		try {
			PersistenceManager pm = PMF.get().getPersistenceManager();
			game = pm.getObjectById(Game.class, key);

			pm.close();

		} catch (JDOObjectNotFoundException e) {
			resp.sendError(404, "not found");
			return;
		} catch (Exception e) {
			resp.sendError(500, "DB error" + e);
			return;
		}

		// forward the request (not redirect)
		req.setAttribute("sgf", game.getSgf().getValue().replace("\n", "").replace("\r", "").replace("]", "];"));
		req.setAttribute("debug", debug);
		RequestDispatcher dispatcher = req.getRequestDispatcher("/game.jsp");
		try {
			dispatcher.forward(req, resp);
		} catch (ServletException e) {
		}
	}

}
