/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package etu1802.framework.servlet;

import etu1802.framework.FileUpload;
import etu1802.framework.Mapping;
import etu1802.framework.ModelView;
import etu1802.framework.annotation.auth;
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
import jakarta.servlet.http.HttpSession;
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
    private HashMap<String, Object> singleton;
    private String isConnected;
    private String profile;
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config); 
        String package_model = config.getInitParameter("model-package");
        setListClass(new ArrayList<>());
        setSingleton(new HashMap<>());
        setMappingUrls(package_model);
        setIsConnected(config.getInitParameter("session-name-isconnected"));
        setProfile(config.getInitParameter("session-name-profile"));
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
            Class class_controller = findController(url);
            Object controller = treatSingleton(class_controller);
            Object model_view = executeController(request, url, controller);
            dispatch(request, response, model_view);
        } catch (Exception ex) {
            Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private boolean treatAuth(HttpServletRequest request, Method controller) {
        auth a = controller.getAnnotation(auth.class);
        if( a != null) {
            HttpSession session = request.getSession();
            String profile = (String) session.getAttribute(getProfile());
            boolean isConnected = (boolean) session.getAttribute(getIsConnected());
            System.out.println(a.value() + "   <" + profile + ">  <" + getProfile() + ">  <" + getIsConnected());
            if (a.value().equals("")){
                return isConnected;
            } else {
                return isConnected && a.value().equals(profile);
            }
        }
        return true;
    }
    
    private void treatSession(HttpServletRequest request, ModelView mv) {
        for (Map.Entry<String, Object> entry : mv.getSession().entrySet()) {
            String key = String.valueOf(entry.getKey());
            Object val = entry.getValue();
            HttpSession session = request.getSession();
            session.setAttribute(key, val);
        }
    }
    
    private Object treatSingleton(Class<?> c) throws InstantiationException, IllegalAccessException {
        if (Utils.isSingleton(c)) {
            if (!getSingleton().containsKey(c.getName())) {
                getSingleton().put(c.getName(), c.newInstance());
            }
            return getSingleton().get(c.getName());
        }
        return c.newInstance();
    }

    
    private void setAll(HttpServletRequest request, Object o) throws NoSuchMethodException, Exception {
        //set_init(o);
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
                }
            } else {
                setter_parameter_object = Utils.cast(parameter, setter_parameter[0]);
            }
            setter.invoke(o, setter_parameter_object);
        }
    }
    
    private void setInit(Object o) throws NoSuchMethodException, Exception {
        HashMap<String, Method> setters = Utils.getAllSetters(o.getClass());
        for (Map.Entry<String, Method> entry : setters.entrySet()) {
            Method setter = (Method)entry.getValue();
            String[] parameter = null;
            Class<?>[] setter_parameter = setter.getParameterTypes();
            Object setter_parameter_object = null;
            if (setter_parameter[0] == FileUpload.class) {
                try {
                    FileUpload fu = new FileUpload();
                    setter_parameter_object = fu;
                } catch (Exception e) {
                }
            } else {
                setter_parameter_object = Utils.cast(parameter, setter_parameter[0]);
            }
            setter.invoke(o, setter_parameter_object);
        }
    }
    
    private void set(HttpServletRequest request, Object o) throws NoSuchMethodException, Exception {
        setInit(o);
        HashMap<String, Method> setters = Utils.getAllSetters(o.getClass());
        Map<String, String[]> parameters = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
            try {
                String key = String.valueOf(entry.getKey());
                Method setter = (Method)setters.get(key);
                String[] parameter = (String[]) entry.getValue();
                Class<?>[] setter_parameter = setter.getParameterTypes();
                Object setter_parameter_object = null;
                if (setter_parameter[0] == FileUpload.class) {
                        FileUpload fu = new FileUpload();
                        Part filePart = getPart(request, key);
                        fu.setName(FileUpload.getFileName(filePart));
                        fu.setBytes(FileUpload.getBytesFromPart(filePart));
                        setter_parameter_object = fu;
                } else {
                    setter_parameter_object = Utils.cast(parameter, setter_parameter[0]);
                }
                setter.invoke(o, setter_parameter_object);
            } catch (Exception e) {
            }
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
        if (model_view instanceof ModelView modelView) {
            try {
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
        treatSession(request, model_view);
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
    
    private Object executeController(HttpServletRequest request, String url, Object controller) throws Exception {
        Object model_view = null;
        Map<String, String[]> parameters = request.getParameterMap();
        Class<?> controller_class = findController(url);
        Method controller_method = findMethodController(controller_class, url);
        if (!treatAuth(request, controller_method)) {
            throw new Exception("Method impossible d'acces");
        }
        Parameter[] controller_method_parameters = controller_method.getParameters();
        Object[] controller_parameters = new Object[controller_method_parameters.length];
        for (int i=0; i<controller_method_parameters.length; i++) {
            Object controller_parameter = Utils.cast(parameters.get(controller_method_parameters[i].getName()), controller_method_parameters[i].getType());
            controller_parameters[i] = controller_parameter;
        }
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
    public HashMap<String, Object> getSingleton() {
        return singleton;
    }
    public void setSingleton(HashMap<String, Object> singleton) {
        this.singleton = singleton;
    }
    public String getIsConnected() {
        return isConnected;
    }
    public void setIsConnected(String isConnected) {
        this.isConnected = isConnected;
    }
    public String getProfile() {
        return profile;
    }
    public void setProfile(String profile) {
        this.profile = profile;
    }
    
    
}
