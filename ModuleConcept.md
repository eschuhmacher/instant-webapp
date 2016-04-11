

# Introduction #

A module groups different views and their corresponding presenters.


# Overview #

![http://instant-webapp.googlecode.com/svn/wiki/ModuleConcept.png](http://instant-webapp.googlecode.com/svn/wiki/ModuleConcept.png)

The green boxes are the ones implemented by the application (you), the orange ones are provided by IWA


# Modules #

To implement a new module, simply subclass AbstractModule (note that you can either [checkout](http://code.google.com/p/instant-webapp/source/browse/#svn%2FIwaSampleApplicationGAE%2Ftrunk%2Fsrc%2Fch%2Food%2Fiwa%2Fsample) or [download](http://code.google.com/p/instant-webapp/downloads/list) the complete sample application Eclipse projects for Google App Engine or Apache Tomcat):

```
public class IwaSampleModule extends AbstractModule implements Serializable {
		
	private static final long serialVersionUID = 1L;
	
	private static final ThemeResource icon = new ThemeResource("../runo/icons/16/note.png");
	
	public IwaSampleModule()  {
		super("Sample");
		
		// This is the place to add more views	
		super.registerView(new IwaSampleView());		
	}

	@Override
	public ThemeResource getIcon() {
		return icon;
	}	
}
```

A module can have multiple Views that are registered in the Modules Constructor.

The module is then registered in your application class code (look for the method "initializeModulesByApplication" at the end of the class):

```
public class IwaSampleApplication extends IwaApplication {

	private static final long serialVersionUID = 1L;
	private static final String FULL_SCREEN_VIEW_URL = "FullScreenSampleView";
	private static final String LOGIN_VIEW_URL = "LoginView";

	@Override
	protected void handleParametersByApplication(Map<String, String[]> parameters) {
		if (parameters.containsKey(FULL_SCREEN_VIEW_URL)) {
			ViewHandler.activateView(IwaSampleFullScreenView.class);
		} else if (parameters.containsKey(LOGIN_VIEW_URL)) {
			ViewHandler.activateView(LoginView.class);
		}			
	}

	@Override
	protected void initApplication() {}

	@Override
	protected void initializeFullScreenViews() {
		registerFullScreenView(IwaSampleFullScreenView.class, FULL_SCREEN_VIEW_URL);
	}

	@Override
	protected void initializeModulesByApplication() {
		new ModulePermissionManager().addPermission(ROOT_USER_ROLE, new IwaSampleModule());	
		getModuleRegistry().registerModule(new IwaSampleModule());				
	}
}
```

# Presenters and Views #
The design follows the [Model-View-Presenter](http://en.wikipedia.org/wiki/Model-view-presenter) ideas where possible.

## Views ##
Implementing a view is only a matter of a few lines code:
```
public class IwaSampleView 
                 extends AbstractModuleView<VerticalLayout, IwaSamplePresenter, IwaSamplePresenter.UI> 
                 implements IwaSamplePresenter.UI, Serializable {

	private static final long serialVersionUID = 1L;
	private Button btnHello;		
	
	public IwaSampleView() {		
		super("Sample", new VerticalLayout(), new IwaSamplePresenter());
		getPresenter().setUi(this);
		initLayout();
		getPresenter().initListeners(); 		
	}
		
	private void initLayout() {
		btnHello = new Button("Say Hello");
		getContent().addComponent(btnHello);					
	}
	
	@Override
	public boolean isFullScreen() {
		return false;
	}

	@Override
	public Button getHelloButton() {
		return btnHello;
	}
}
```

## Full Screen View ##
If a view should not be visually embedded in the main view with the header and navigation frame, you can simply define
```
@Override
public boolean isFullScreen() {
	return true;
}
```
in your view and register it in your application
```
@Override
protected void initializeFullScreenViews() {
	registerFullScreenView(IwaSampleFullScreenView.class, FULL_SCREEN_VIEW_URL);
}
```

You can then navigate via hash and the URL you have given to the view, for example http://localhost:7777#FullScreenSampleView (of course this link only works if it runs on your local machine).

Please note that this works on some browser rather instable, so you might also want to make the view reachable by http parameter (for example http://localhost:7777/?FullScreenSampleView, note the exclamation mark ? instead of the hash sign #). To achieve this, write in your application something like:
```
@Override
protected void handleParametersByApplication(Map<String, String[]> parameters) {
	if (parameters.containsKey(FULL_SCREEN_VIEW_URL)) {
		ViewHandler.activateView(IwaSampleFullScreenView.class);
	} else if (parameters.containsKey(LOGIN_VIEW_URL)) {
		ViewHandler.activateView(LoginView.class);
	}			
}
```


## Presenters ##
A Presenter implementation:
```
public class IwaSamplePresenter extends AbstractModulePresenter<IwaSamplePresenter.UI> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Interface for communication between Presenter and View
	 */
	public static interface UI extends IwaModuleUI {		
		Button getHelloButton();
	}
	
	/**
	 * Do the event wiring 
	 */
	public void initListeners() {		
		getUi().getHelloButton().addListener(buttonClickListener);
	}
	
	/**
	 * Button Click Listener
	 */
	private ClickListener buttonClickListener = new ClickListener() {
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			if (event.getButton().equals(getUi().getHelloButton())) {
				getUi().showInfo("Hello", "Hello, World!");
			} 		
		}
	};	
}
```