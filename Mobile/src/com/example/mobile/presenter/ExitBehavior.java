package com.example.mobile.presenter;

import com.vaadin.addon.touchkit.ui.NavigationButton.NavigationButtonClickEvent;
import com.vaadin.addon.touchkit.ui.NavigationButton.NavigationButtonClickListener;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class ExitBehavior implements NavigationButtonClickListener {

	@Override
	public void buttonClick(NavigationButtonClickEvent event) {
		// TODO Auto-generated method stub
		UI.getCurrent().close();
		UI.getCurrent().getPage().setLocation(VaadinServlet.getCurrent().getServletConfig().getServletContext().getContextPath());
	}
}
