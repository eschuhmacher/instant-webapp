package ch.ood.iwa.ui;

import org.vaadin.appfoundation.authentication.SessionHandler;
import org.vaadin.appfoundation.i18n.Lang;
import org.vaadin.appfoundation.view.View;
import org.vaadin.appfoundation.view.ViewContainer;
import org.vaadin.appfoundation.view.ViewHandler;

import ch.ood.iwa.IwaApplication;
import ch.ood.iwa.IwaException;
import ch.ood.iwa.authorization.ModulePermissionManager;
import ch.ood.iwa.module.Module;
import ch.ood.iwa.module.ModuleRegistry;
import ch.ood.iwa.module.ui.LoginView;
import ch.ood.iwa.module.ui.ModuleView;
import ch.ood.iwa.module.ui.WelcomeView;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;

/**
 * IWA Main Window 
 * 
 * @author Mischa
 *
 */
public class MainWindow extends Window implements ViewContainer, ItemClickListener, Button.ClickListener {

	private static final long serialVersionUID = 1L;	
	// should never be displayed, so no need to translate
	private Label lblCurrentUser = new Label("Logged off..."); 
	private Button btnLogout;
	private Button btnHelp;
	private Panel fullScreenLayout = new Panel();
	private Panel mainLayout = new Panel(new VerticalLayout());	
	private HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();	
	private MainArea mainArea  = new MainArea();
	private Accordion menu  = new Accordion();	
	private View currentView;
	private Tree tree = new Tree(); 

	/**
	 * On selection of a Tab
	 */
	private SelectedTabChangeListener selectedTabChangeListener = new SelectedTabChangeListener() {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void selectedTabChange(SelectedTabChangeEvent event) {			
			// unselect. TODO: find a way to clear the current selection
		}
	}; 
	
	/**
	 * Default Constructor
	 */
	public MainWindow() {
		initLayout();
	}
		
	@Override
	public void activate(View view) {
		
		ModuleView moduleView = (ModuleView)view;
		currentView = view;
		
		if (moduleView.isFullScreen()) {
			setContent(fullScreenLayout);
			fullScreenLayout.setSizeFull();
			fullScreenLayout.removeAllComponents();
			fullScreenLayout.addComponent((Component)view);
			
		} else {
			setContent(mainLayout);
			mainLayout.setSizeFull();
			mainLayout.getContent().setSizeFull();
			mainArea.activate(view);	
		}
	}
	
	private void initListeners() {
		menu.addListener(selectedTabChangeListener);
	}
	
	@Override
	public void deactivate(View view) {
		mainArea.deactivate(view);
	}
	
	@Override
	public void itemClick(ItemClickEvent event) {	
		String source = (String) event.getItemId();		
		try {
			View view = IwaApplication.getInstance().getModuleRegistry().getViewByDisplayName(source);
			ViewHandler.activateView(view.getClass());
		} catch (IwaException e) {
			IwaApplication.getInstance().logError(e);
			showNotification(e.getMessage(), e.getDetails(), Notification.TYPE_ERROR_MESSAGE);
		}			
	}

	/**
	 * Initializes the Navigation Main Menu
	 */
	public void initializeNavigation() {
		// Guard
		if (SessionHandler.get() == null) {
			return;
		}		
		// Clean up first
		menu.removeAllComponents();
		
		// Go through all modules 				
		for (Module module : IwaApplication.getInstance().getModuleRegistry().getAllModules()) {					
			// Only add this module if current user has the according permissions
			if (new ModulePermissionManager().hasPermission(SessionHandler.get().getRole(), module)) {
				Tree tree = module.getViewDisplayNamesAsTree();
				tree.addListener(this);
				menu.addTab(tree, module.getDisplayName(), module.getIcon());
				// register all views of this module
				for (View view : module.getAllViews()) {
					ViewHandler.addView(view.getClass(), this);
				}				
			}			
		}
	}
	
	/**
	 * Synchronizes the navigation menu with the given view 
	 *  
	 * @param viewName
	 */
	public void synchronizeNavigation(String viewName) {
		ModuleRegistry moduleRegistry = IwaApplication.getInstance().getModuleRegistry();		
		for (Module module : moduleRegistry.getAllModules()) {			
			ModuleView view = (ModuleView)module.getViewByName(viewName);			
			if (view != null) {			
				String viewDisplayName = view.getDisplayName();			
				tree = module.getViewDisplayNamesAsTree();								
				menu.setSelectedTab(tree);
				/**
				 * if already selected, do nothing (prevents selection hopping)
				 * TODO: does not quite work....
				 */				
				if (menu.getSelectedTab().equals(tree)) {
					return;
				}
				tree.select(viewDisplayName);
			}					
		}
	}
	
	/**
	 * Refreshes the whole view.<br/>
	 * This is useful for example when an Locale change has taken place
	 * 
	 */
	public void refresh() {
		if (SessionHandler.get() != null) {
			setCurrentUser(SessionHandler.get().getName());
		}
		btnLogout.setCaption("(" + Lang.getMessage("Logout") + ")");
		initializeNavigation();
		ViewHandler.activateView(currentView.getClass());
	}
	
	/**
	 * Listens to the logout button
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getSource().equals(btnLogout)) {
			SessionHandler.logout();
			ViewHandler.activateView(LoginView.class);
			
		} else if (event.getSource().equals(btnHelp)) {
			super.open(new ExternalResource(Lang.getMessage("HelpUrl")), "_blank");						
		}
	}

	/**
	 * Set the current user to display it in the header panel
	 * 
	 * @param currentUser
	 */
	public void setCurrentUser(String currentUser) {
		lblCurrentUser.setValue(Lang.getMessage("LoggedInAs", currentUser));
	}
	
	private void initLayout() {
		splitPanel.setSplitPosition(250, HorizontalSplitPanel.UNITS_PIXELS);
		splitPanel.addComponent(menu);
		splitPanel.addComponent(mainArea);
		
		mainLayout.getContent().addComponent(getHeaderPanel());
		mainLayout.getContent().addComponent(splitPanel);
		
		((VerticalLayout)mainLayout.getContent()).setExpandRatio(splitPanel, 5);
		
		splitPanel.setSizeFull();
		
		// Init some additional listeners
		initListeners();
	}
	
	/**
	 * Produces the header panel
	 * 
	 * @return
	 */
	private Panel getHeaderPanel() {
        Panel panel = new Panel(new HorizontalLayout());
        panel.setHeight("110px"); 
        ((HorizontalLayout)panel.getContent()).setSpacing(true);
        ((HorizontalLayout)panel.getContent()).setMargin(false, true, false, true);
        ((HorizontalLayout)panel.getContent()).setWidth("100%");
        
        // Logo
        Embedded logo = new Embedded(null, new ThemeResource(IwaApplication.LOGO_FILE_PATH));
        
        panel.getContent().addComponent(logo);
        // Add a "Home" - Link to the image
        logo.setDescription("Home");
        logo.addListener(new ClickListener() {
			private static final long serialVersionUID = 1L;
			@Override
			public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
				ViewHandler.activateView(WelcomeView.class);				
			}
		});
        
        // Current User
        lblCurrentUser.addStyleName("label-right-align");
        panel.getContent().addComponent(lblCurrentUser);
        ((HorizontalLayout)panel.getContent()).setComponentAlignment(lblCurrentUser, Alignment.MIDDLE_RIGHT);
        ((HorizontalLayout)panel.getContent()).setExpandRatio(lblCurrentUser, 10.0f);

        // Logout Button
        btnLogout = new Button("(" + Lang.getMessage("Logout") + ")");
        btnLogout.setStyleName(BaseTheme.BUTTON_LINK);
        btnLogout.setDescription(Lang.getMessage("Logout"));
        btnLogout.addListener(this); 
        panel.getContent().addComponent(btnLogout);
        ((HorizontalLayout)panel.getContent()).setComponentAlignment(btnLogout, Alignment.MIDDLE_RIGHT);
        ((HorizontalLayout)panel.getContent()).setExpandRatio(btnLogout, 0.1f);
        
        // Help Button
        btnHelp = new Button(Lang.getMessage("Help"));
        btnHelp.setStyleName(BaseTheme.BUTTON_LINK);        
        btnHelp.addListener(this); 
        panel.getContent().addComponent(btnHelp);
        ((HorizontalLayout)panel.getContent()).setComponentAlignment(btnHelp, Alignment.MIDDLE_RIGHT);
                
        return panel;                
	}
	
}

