package it.unibo;

import com.google.gwt.core.client.EntryPoint;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class App implements EntryPoint {
	

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		MainLayoutGui layout = new MainLayoutGui();
		com.google.gwt.user.client.ui.RootPanel.get().add(layout);
	}
}
