package com.UdeA.Ciclo3.handler;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//esto redirecciona depediendo del rol del que se loguea
@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    //objeto para crear la estrategia de redireccionamiento cuando inicie sesion
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    
    protected void handler(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
        throws IOException{
            String targetUrl= determineTargetUrl(authentication); //llamo al metodo que me da la url a donde debo ir
            if (response.isCommitted()){
                System.out.println("no se puede redireccionar");
                return;

            }
            redirectStrategy.sendRedirect(request, response, targetUrl);

    }

    protected String determineTargetUrl(Authentication authentication){
        String url="";
        Collection<? extends GrantedAuthority> authorities =authentication.getAuthorities(); //traer una coleccion de las personas logueadas y sus permisos

        List<String> roles = new ArrayList<String>();
        for (GrantedAuthority a: authorities){
             roles.add(a.getAuthority()); //lista con los roles guardados

        }

        if(esAdministrativo(roles)){
            url="/VerEmpresas";
        }
        else if (esOperativo(roles)){
            url="/VerMovimientos";
        }
        else{
            url="/Denegado";
        }
        return url;
    }

    //METODO PARA VERIFICAR SI EL ROL ES OPERATIVO
    private boolean esOperativo(List<String> roles){
        if (roles.contains("ROLE_USER")){
            return true;
        }
        return false;

    }

    //METODO PARA VERIFICAR SI EL ROL ES ADMINISTRATIVO
    private boolean esAdministrativo(List<String> roles){
        if (roles.contains("ROLE_ADMIN")){
            return true;
        }
        return false;

    }

}

