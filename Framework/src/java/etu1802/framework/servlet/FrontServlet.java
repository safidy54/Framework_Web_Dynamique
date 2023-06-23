/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package etu1802.framework.servlet;

import etu1802.framework.FileUpload;
import etu1802.framework.Mapping;
import etu1802.framework.ModelView;
import etu1802.framework.annotation.url;
import etu1802.framework.util.Utils;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author safidy
 */
public class FrontServlet extends HttpServlet {
    private HashMap<String, Mapping> MappingUrls;
    private ArrayList<Class<?>> list_class;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config); 
        String package_model = config.getInitParameter("model-package");
        setListClass(new ArrayList<>());
        setMappingUrls(package_model);
    }
    
     
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            out.println("<h1>Servlet Frontservlet at " + request.getContextPath() + "</h1>");
            out.println("<h1>URL at " + getURL(request) + "</h1>");
            String url = getURL(request);
            Object model_view = executeController(request, url);
            dispatch(request, response, model_view);
        } catch (Exception ex) {
            Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void set(HttpServletRequest request, Object o) throws NoSuchMethodException, Exception {
        HashMap<String, Method> setters = Utils.getAllSetters(o.getClass());
        Map<String, String[]> parameters = request.getParameterMap();
        for (Map.Entry<String, Method> entry : setters.entrySet()) {
            String key = String.valueOf(entry.getKey());
            Method setter = (Method)entry.getValue();
            String[] parameter = parameters.get(key);
            Class<?>[] setter_parameter = setter.getParameterTypes();
            Object setter_parameter_object = null;
            if (setter_parameter[0] == FileUpload.class) {
                try {
                    FileUpload fu = new FileUpload();
                    Part filePart = getPart(request, key);
                    fu.setName(FileUpload.getFileName(filePart));
                    fu.setBytes(FileUpload.getBytesFromPart(filePart));
                    setter_parameter_object = fu;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                setter_parameter_object = Utils.cast(parameter, setter_parameter[0]);
            }
            setter.invoke(o, setter_parameter_object);
        }
    }
    
    private Part getPart(HttpServletRequest request, String key) throws IOException, ServletException, Exception {
        Collection<Part> part_collection = request.getParts();
        for (Part part : part_collection) {
            if (part.getName().equals(key)) {
                return part;
            }
        }
        throw new Exception("¨Part don't exist");
    }
    
    private void dispatch(HttpServletRequest request, HttpServletResponse response, Object model_view) throws Exception {
        if (model_view instanceof ModelView) {
            try {
                Object modelView = null;
                dispatch(request, response, modelView);
                return;
            } catch (ServletException | IOException e) {
                throw e;
            }
        }
        throw new Exception("The controller's method must return a ModelView");
    }
    
    private void dispatch(HttpServletRequest request, HttpServletResponse response, ModelView model_view) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(model_view.getView());
        for (Map.Entry<String, Object> entry : model_view.getData().entrySet()) {
            String key = String.valueOf(entry.getKey());
            Object val = entry.getValue();
            request.setAttribute(key, val);
        }
        dispatcher.forward(request, response);
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
    
    private Object executeController(HttpServletRequest request, String url) throws Exception {
        Object model_view = null;
        Map<String, String[]> parameters = request.getParameterMap();
        Class<?> controller_class = findController(url);
        Method controller_method = findMethodController(controller_class, url);
        Parameter[] controller_method_parameters = controller_method.getParameters();
        Object[] controller_parameters = new Object[controller_method_parameters.length];
        for (int i=0; i<controller_method_parameters.length; i++) {
            Object controller_parameter = Utils.cast(parameters.get(controller_method_parameters[i].getName()), controller_method_parameters[i].getType());
            controller_parameters[i] = controller_parameter;
        }
        Object controller = controller_class.newInstance();
        set(request,controller );
        model_view = controller_method.invoke(controller, controller_parameters);
        return model_view;
    }
    
    private Method findMethodController(Class<?> c, String url) throws Exception {
        for (Method m : c.getDeclaredMethods()) {
            if (m.getName().equals(getMappingUrls().get(url).getMethod())){
                return m;
            }
        }
        throw new Exception("Method not found");
    }
        
    private Object instanceController(String url) throws Exception {
        Class c = findController(url);
        Object o = c.newInstance();
        return o;
    }
    
    private Class findController(String url) throws Exception {
        List<Class<?>> lc = getListClass();
        for (Class<?> c : lc) {
            if (c.getSimpleName().equals(getMappingUrls().get(url).getClassName())) {
                for (Method m : c.getDeclaredMethods()) {
                    if (m.getName().equals(getMappingUrls().get(url).getMethod())){
                        return c;
                    }
                }
            }
        }
        throw new Exception("Controller not found");
    }
    
    private String getURL(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        String requestURI = request.getRequestURI();
        return requestURI.split(contextPath)[1];
    }

    public HashMap<String, Mapping> getMappingUrls() {
        return MappingUrls;
    }

    public void setMappingUrls(HashMap<String, Mapping> MappingUrls) {
        this.MappingUrls = MappingUrls;
    }
    
    public void setMappingUrls(String path) {
        try {
            List<Class> lc = Utils.getClassFrom(path);
            setMappingUrls(new HashMap<>());
            for (Class c : lc) {
                for (Method m : c.getDeclaredMethods()) {
                    url u = m.getAnnotation(url.class);
                    if (u  != null) {
                       getMappingUrls().put(u.value() , new Mapping(c.getSimpleName(), m.getName()));
                    }
                }
                getListClass().add(c);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<Class<?>> getListClass() {
        return this.list_class;
    }
    public void setListClass(ArrayList<Class<?>> list_class) {
        this.list_class = list_class;
    }
    
}
