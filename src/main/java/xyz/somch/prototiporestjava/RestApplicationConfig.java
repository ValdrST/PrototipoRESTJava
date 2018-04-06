package xyz.somch.prototiporestjava;

import org.glassfish.jersey.server.ResourceConfig;
import xyz.somch.filtro.FiltroAutorizacion;



/**
 *  set the filter applications manually and not via web.xml
 */
public class RestApplicationConfig extends ResourceConfig {
	
	public RestApplicationConfig() {
        packages("xyz.somch.filtro");
		register( FiltroAutorizacion.class );
	}
}
