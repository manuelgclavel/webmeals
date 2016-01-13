package com.example.mobile.presenter;


import com.example.mobile.MobileUI;
import com.example.mobile.viewer.CloseSessionView;
import com.vaadin.addon.touchkit.ui.NavigationButton.NavigationButtonClickEvent;
import com.vaadin.addon.touchkit.ui.NavigationButton.NavigationButtonClickListener;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings({ "serial", "unused" })
public class ExitBehavior implements ClickListener {


	@Override
	public void buttonClick(ClickEvent event) {
		// TODO Auto-generated method stub
		// ((MobileUI) UI.getCurrent()).setContent(new CloseSessionView());
		//UI.getCurrent().close();
		UI.getCurrent().getPage().setLocation(VaadinServlet.getCurrent().getServletConfig().getServletContext().getContextPath());
		UI.getCurrent().close();
	}
}
