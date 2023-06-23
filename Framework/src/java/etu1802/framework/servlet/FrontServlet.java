/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etu1802.framework.servlet;

import etu1802.framework.Mapping;
import etu1802.framework.ModelView;
import etu1802.framework.annotation.Url;
import etu1802.framework.utils.Util;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Safidy
 */
@WebServlet(name = "FrontServlet", urlPatterns = {"/FrontServlet"})
public class FrontServlet extends HttpServlet {
    HashMap<String, Mapping> mappingUrls;
    private ArrayList<Class> classList;

    public HashMap<String, Mapping> getMappingUrls() {
        return mappingUrls;
    }

    public void setMappingUrls(HashMap<String, Mapping> mappingUrls) {
        this.mappingUrls = mappingUrls;
    }
    
    public void setMappingUrls(String path) {
        try {
            List<Class> lc = Util.getClassFrom(path);
            setMappingUrls(new HashMap<String, Mapping>());
            for (Class c : lc) {
                for (Method m : c.getDeclaredMethods()) {
                    Url u = m.getAnnotation(Url.class);
                    if (u  != null) {
                       getMappingUrls().put(u.value() , new Mapping(c.getSimpleName(), m.getName()));
                    }
                }
                getClassList().add(c);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public ArrayList<Class> getClassList() {
        return classList;
    }

    public void setClassList(ArrayList<Class> classList) {
        this.classList = classList;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init();
        String packageModel = config.getInitParameter("model-package");
        setClassList(new ArrayList<Class>());
        setMappingUrls(packageModel);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try  {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet FrontServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1> <u>Servlet FrontServlet</u> at " + request.getContextPath() + "</h1>");
            out.println("<h1><u>RequestURI</u> at " + request.getRequestURI()+ "</h1>");
            out.println("<h1><u> Url </u>at " + getUrl(request) + "</h1>");
            out.println("</body>");
            out.println("</html>");
            Method m = getMethodFromUrl(getUrl(request));
            Class c = getClassFromUrl(getUrl(request));
            Object o = m.invoke(c.newInstance(), null);
            out.println(o);
            if (o instanceof ModelView) {
                ModelView mv = (ModelView)o;
                RequestDispatcher dispatcher = request.getRequestDispatcher(mv.getView());
                for (Map.Entry<String, Object> entry : mv.getData().entrySet()) {
                    String key = String.valueOf(entry.getKey());
                    Object val = entry.getValue();
                    request.setAttribute(key, val);
                }
                dispatcher.forward(request, response);
            }
            out.println(c.getName() + "<br>");

            Object temp = set(request, c);
            Method me = getMethodFromUrl(getUrl(request));

            Object obj = me.invoke(temp, (Object) null);    
            prepareDispatch(request, response, obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    public String getUrl(HttpServletRequest request) {
        String result;
        String contextPath = request.getContextPath();
        String url = request.getRequestURI();
        result = url.split(contextPath)[1];
        String query = request.getQueryString();
        return result;
    }
    
    public Method getMethodFromUrl(String url) throws Exception {
        
        List<Class> lc = getClassList();
        for (Class c : lc) {
            if (c.getSimpleName().equals(getMappingUrls().get(url).getClassName())) {
                for (Method m : c.getDeclaredMethods()) {
                    if (m.getName().equals(getMappingUrls().get(url).getMethod())){
                        return m;
                    }
                }
            }
        }
        throw new Exception("Method not found");
    }
    public Class getClassFromUrl(String url) throws Exception {
        
        List<Class> lc = getClassList();
        for (Class c : lc) {
            if (c.getSimpleName().equals(getMappingUrls().get(url).getClassName())) {
                for (Method m : c.getDeclaredMethods()) {
                    if (m.getName().equals(getMappingUrls().get(url).getMethod())){
                        return c;
                    }
                }
            }
        }
        throw new Exception("Class not found");
    }

    public Object set(HttpServletRequest request, Class c) throws Exception {

            HashMap<String, Method> setter = Util.getSetters(c);
            Map<String, String[]> param = request.getParameterMap();

            Object temp = c.newInstance();
//            
            for (Map.Entry<String, String[]> entry : param.entrySet()) {
                String key = entry.getKey();
                String[] parameter = entry.getValue();
                if (!setter.containsKey(key)) {
                    continue;
                }

                Method setTemp = setter.get(key);
                Class<?>[] setParam = setTemp.getParameterTypes();
                setTemp.invoke(temp, (Object) Util.CastTo(parameter[0],  setParam[0]));
            }
            return temp;
    }
    
    public void prepareDispatch(HttpServletRequest request, HttpServletResponse response, Object o) throws ServletException, IOException {
         if (o instanceof ModelView) {
                ModelView mv = (ModelView)o;
                mv.listAll();
                RequestDispatcher dispatcher = request.getRequestDispatcher(mv.getView());
                for (Map.Entry<String, Object> entry : mv.getData().entrySet()) {
                    String key = String.valueOf(entry.getKey());
                    Object val = entry.getValue();
                    request.setAttribute(key, val);
                }
                dispatcher.forward(request, response);
            }
    }
}
