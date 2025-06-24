/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
import java.sql.SQLException;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 *
 * @author eddis
 */
@WebServlet("/r/*")
public class RedirectServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String shortCode= request.getPathInfo()!=null ? request.getPathInfo().substring(1): "";
        System.out.println("PathInfo: " + request.getPathInfo() + ", shortCode: " + shortCode);
        if(shortCode.isEmpty()||"/index.jsp".equals(shortCode)){
            return;
        }
        if (!shortCode.matches("^[a-zA-Z0-9]{1,12}$")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid short code format");
            return;
        }
        response.setContentType("text/html");
        try(PrintWriter out = response.getWriter()){
            String longUrl= UrlService.getLongUrl(getServletContext(), shortCode);
            if(longUrl!=null){
                response.sendRedirect(longUrl);
            }
            else{
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.println("<h1>Short code not found</h1>");
                out.println("<a href='/UrlShortener/'><button>Return to Main Page</button></a>");
            }
        }catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Database error: " + e.getMessage());
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Server error"+ e.getMessage());
        }
    }

}
