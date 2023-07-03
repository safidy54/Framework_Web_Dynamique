/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etu1802.framework.servlet;

import com.google.gson.Gson;
import etu1802.framework.FileUpload;
import etu1802.framework.Mapping;
import etu1802.framework.ModelView;
import etu1802.framework.annotation.auth;
import etu1802.framework.annotation.restAPI;
import etu1802.framework.annotation.session;
import etu1802.framework.annotation.url;
import etu1802.framework.util.Utils;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

/**
 *
 * @author Safidy
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
        PrintWriter out = response.getWriter();
        try {
            String url = getURL(request);
            Class<?> class_controller = findController(url);
            Object controller = treatSingleton(class_controller);
            Class<?> controller_class = findController(url);
            Method controller_method = findMethodController(controller_class, url);
            restAPI rest = controller_method.getAnnotation(restAPI.class);
            Object model_view = executeController(request, url, controller);
            if (rest != null) {
                 Gson gson = new Gson();
                String json = gson.toJson(model_view);
                out.print(json);
            } else {
                if (model_view instanceof ModelView) {
                    ModelView mv = (ModelView) model_view; 
                    HttpSession session = request.getSession();
                    for (String session_name : mv.getRemovingSession()) {
                        session.removeAttribute(session_name);
                    }
                    if (mv.getInvalidateSession() == true) {
                        session.invalidate();
                    }
                    if (mv.isJSON()) {
                        Gson gson = new Gson();
                        String json = gson.toJson(mv.getData());
                        out.print(json);
                    } else {
                        dispatch(request, response, mv);
                    }
                } else {
                    throw new Exception("La variable de retour n'est pas reconnue");
                }
            }
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
            if (a.value().equals("")){
                return isConnected;
            } else {
                return isConnected && a.value().equals(profile);
            }
        }
        return true;
    }
    
    private void getAllSession(HttpServletRequest request, Object controller, Method method_controller) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        HttpSession session = request.getSession();
        session s = method_controller.getAnnotation(session.class);
        if (s != null) {
            Enumeration<String> attributeNames = session.getAttributeNames();
            Method method_session_get = controller.getClass().getDeclaredMethod("getSession", new Class[0]);
            Method method_session_set = controller.getClass().getDeclaredMethod("setSession", HashMap.class);
            HashMap<String, Object> session_controller = new HashMap<String, Object>();
            while (attributeNames.hasMoreElements()) {
                String attributeName = attributeNames.nextElement();
                Object attributeValue = session.getAttribute(attributeName);
                session_controller.put(attributeName, attributeValue);
            }
            method_session_set.invoke(controller, session_controller);
            System.out.println(method_session_get.invoke(controller, new Object[0]));
        }
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
                    fu.setName(FileUpload.getFileName((jakarta.servlet.http.Part) filePart));
                    fu.setBytes(FileUpload.getBytesFromPart((jakarta.servlet.http.Part) filePart));
                    setter_parameter_object = fu;
                } catch (Exception e) {
                    e.getMessage();
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
                    e.getMessage();
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
                    fu.setName(FileUpload.getFileName((jakarta.servlet.http.Part) filePart));
                    fu.setBytes(FileUpload.getBytesFromPart((jakarta.servlet.http.Part) filePart));
                    setter_parameter_object = fu;
                } else {
                    setter_parameter_object = Utils.cast(parameter, setter_parameter[0]);
                }
                setter.invoke(o, setter_parameter_object);
            } catch (Exception e) {
                e.getMessage();
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
    
    private void dispatch(HttpServletRequest request, HttpServletResponse response, ModelView model_view) throws Exception {
        try {
            RequestDispatcher dispatcher = request.getRequestDispatcher(model_view.getView());
            treatSession(request, model_view);
            for (Map.Entry<String, Object> entry : model_view.getData().entrySet()) {
                String key = String.valueOf(entry.getKey());
                Object val = entry.getValue();
                request.setAttribute(key, val);
            }
            dispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            throw e;
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
        getAllSession(request, controller, controller_method);
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
            ex.getMessage();
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

