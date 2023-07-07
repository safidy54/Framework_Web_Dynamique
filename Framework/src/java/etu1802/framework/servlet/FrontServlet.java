/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etu1802.framework.servlet;

import etu1802.framework.Mapping;
import etu1802.framework.annotation.url;
import etu1802.framework.util.Utils;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author safidy
 */
public class FrontServlet extends HttpServlet {

    public HashMap<String, Mapping> mappingUrls;
    
    public void addMappingUrls(Class c) {
        Method[] methods = c.getDeclaredMethods();
        for (Method method : methods) {
            url[] a = method.getAnnotationsByType(url.class);
            if (a.length > 0) {
                getMappingUrls().put(a[0].value(), new Mapping(c.getSimpleName(), method.getName()));
            }
        }
    }

    public HashMap<String, Mapping> getMappingUrls() {
        return mappingUrls;
    }

    public void setMappingUrls(HashMap<String, Mapping> mappingUrls) {
        this.mappingUrls = mappingUrls;
    }
    
    public void setMappingUrls(String path) {
        try {
            List<Class> lc = Utils.getClassFrom(path);
            setMappingUrls(new HashMap<String, Mapping>());
            for (Class c : lc) {
                for (Method m : c.getDeclaredMethods()) {
                    url u = m.getAnnotation(url.class);
                    if (u  != null) {
                       getMappingUrls().put(u.value() , new Mapping(c.getSimpleName(), m.getName()));
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    @Override
    public void init() throws ServletException {
        super.init(); 
        setMappingUrls("etu1802.model");
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
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
        if (query!=null)   {
            result = result.concat("?" + query);
        }
        return result;
    }
    
    public Method getMethodsUrl(String url) throws Exception {
        
        List<Class> lc = Utils.getClassFrom("etu1802.model");
        for (Class c : lc) {
            if (c.getSimpleName()==getMappingUrls().get(url).getClassName()) {
                for (Method m : c.getDeclaredMethods()) {
                    if (m.getName()==getMappingUrls().get(url).getMethod()){
                        return m;
                    }
                }
            }
        }
        throw new Exception("Method not found");
    }
}
