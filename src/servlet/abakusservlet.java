package servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/abakusservlet")
public class abakusservlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public abakusservlet() {

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//request.setAttribute("outputString", "");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		Generation construct = new Generation();
	
		//transaction of params
		construct.setTablesCount(request.getParameter("tablesCount"));
		construct.setexampleCouts(request.getParameter("exampleCouts"));
		construct.setExampleLowerbound(request.getParameter("bounds"));
		construct.setExampleUpperbound(request.getParameter("bounds"));
		construct.setMaxNumber(request.getParameter("maximin"));
		construct.setMinNumber(request.getParameter("maximin"));
		construct.setLeftHand(request.getParameter("leftHand"));
		construct.setRightHand(request.getParameter("rightHand"));
		construct.setMiniabakus(request.getParameter("miniAbakus"));
		construct.setNoTransaction1to10(request.getParameter("noTransaction1to10"));
		construct.setChangeHands(request.getParameter("changeHands"));
		construct.setFormulaCheck(request.getParameter("formulaCheck"));
		construct.setFormula(request.getParameter("formula"));
		
		//formulate answer for web
		response.getWriter().println(construct.answerConstruct());
		//request.setAttribute("outputString", construct.answerConstruct());
		//request.getRequestDispatcher("index.html").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
