package com.ipbsoft.tictactoe;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.ipbsoft.tictactoe.config.AppConfig;

/**
 * 
 * Main Class
 * 
 * This initializer extends from Spring's AbstractAnnotationConfigDispatcherServletInitializer 
 * and provides required methods for initialize a web service based on Spring config annotations
 * in a quick way
 * 
 * @author insalada
 *
 */
public class TicTacToe extends AbstractAnnotationConfigDispatcherServletInitializer  {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return null;
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] { AppConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}	
}
