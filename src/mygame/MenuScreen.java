package mygame;

// Various imports
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.screen.DefaultScreenController;
import de.lessvoid.nifty.controls.textfield.builder.TextFieldBuilder;

/*
* @author ryan
* Builds start menu screen and game hud screen
* Uses MyStartScreen to control screens
 */
public class MenuScreen extends BaseAppState {

	// Instance variables
	private NiftyJmeDisplay nifty;

	MyStartScreen myStartScreen;
	private Application app;
	String ipText;

	/**
	 *
	 * @param nifty - NiftyJmeDisplay
	 * @param app - the application
	 */
	public MenuScreen(NiftyJmeDisplay nifty, Application app) {
		this.nifty = nifty;
		this.app = app;

		myStartScreen = new MyStartScreen(this.app, this.nifty);
	}

	@Override
	protected void initialize(Application app) {
		//It is technically safe to do all initialization and cleanup in the
		//onEnable()/onDisable() methods. Choosing to use initialize() and
		//cleanup() for this is a matter of performance specifics for the
		//implementor.
		//TODO: initialize your AppState, e.g. attach spatials to rootNode
	}

	@Override
	protected void cleanup(Application app) {
		//TODO: clean up what you initialized in the initialize method,
		//e.g. remove all spatials from rootNode
	}

	//onEnable()/onDisable() can be used for managing things that should
	//only exist while the state is enabled. Prime examples would be scene
	//graph attachment or input listener attachment.
	@Override
	protected void onEnable() {
		// Extracts nifty from niftyJmeDisplay and stores it in nifty2
		Nifty nifty2 = nifty.getNifty();

		// Loads two xml files to control nifty gui
		nifty2.loadStyleFile("nifty-default-styles.xml");
		nifty2.loadControlFile("nifty-default-controls.xml");

		// Adds a new screen, start, and builds it using the ScreenBuilder class
		nifty2.addScreen("start", new ScreenBuilder("start") {
			{
				// Uses MyStartScreen class as screen controller
				// Passes app and nifty as parameters
				controller(myStartScreen);

				// Adds layer named background
				layer(new LayerBuilder("background") {
					{
						childLayoutCenter();
						backgroundColor("450f");

						// add image
						image(new ImageBuilder() {
							{
								filename("Interface/BoxWarsBackground.png");

							}
						});
					}
				});

				layer(new LayerBuilder("foreground") {
					{
						childLayoutVertical();

						// panel added
						panel(new PanelBuilder("panel_top") {
							{
								childLayoutCenter();
								alignCenter();
								height("10%");
								width("75%");

								text(new TextBuilder() {
									{
										text("Box Wars");
										font("Interface/Fonts/Arial.fnt");
										height("100%");
										width("100%");
									}
								});
							}
						});
						panel(new PanelBuilder("panel_mid") {
							{
								childLayoutCenter();
								alignCenter();
								height("10%");
								width("75%");

								// add text
								text(new TextBuilder() {
									{
										text("Created by Shane Kim, Jarod Bainbridge, and Ryan Wang.");
										font("Interface/Fonts/Arial.fnt");
										height("100%");
										width("100%");
									}
								});
							}
						});

						panel(new PanelBuilder("Bottom_Up4") {
							{
								childLayoutVertical();
								alignLeft();
								height("15%");
								width("90%");
								// add control
								//control(new TextFieldBuilder("Network", "N")  {

							}
						});

						panel(new PanelBuilder("Bottom_Up3") {
							{
								childLayoutVertical();
								alignLeft();
								height("15%");
								width("90%");

								control(new TextFieldBuilder("address", "IP Address") {
									{
										alignRight();
										valignCenter();
										height("50%");
										width("20%");
									}
								});

								// add control
								control(new ButtonBuilder("JoinButton", "Join") {
									{
										alignRight();
										valignCenter();
										height("50%");
										width("20%");

										interactOnClick("startGame(hud)");
									}

								});
							}
						});

						panel(new PanelBuilder("Bottom_Up2") {
							{
								childLayoutVertical();
								alignLeft();
								height("15%");
								width("90%");

								// add control
								control(new ButtonBuilder("HostButton", "Host") {
									{
										alignRight();
										valignCenter();
										height("50%");
										width("20%");

										interactOnClick("StartServer()");
									}
								});
							}
						});

						panel(new PanelBuilder("Bottom_Up1") {
							{
								childLayoutVertical();
								alignLeft();
								height("15%");
								width("90%");

								// add control
								control(new ButtonBuilder("QuitButton", "Quit") {
									{
										alignRight();
										valignCenter();
										height("50%");
										width("20%");
										visibleToMouse(true);
										interactOnClick("quitGame()");
									}

								});
							}
						});
					}
				});

			}
		}.build(nifty2));

		nifty2.addScreen("hud", new ScreenBuilder("hud") {
			{
				controller(new DefaultScreenController());

				layer(new LayerBuilder("background") {
					{
						childLayoutCenter();
					}
				});

				layer(new LayerBuilder("foreground") {
					{
						childLayoutHorizontal();

						// panel added
						panel(new PanelBuilder("panel_left") {
							{
								childLayoutVertical();
								//backgroundColor("0f08");
								height("100%");
								width("80%");
								// <!-- spacer -->
							}
						});

						panel(new PanelBuilder("panel_right") {
							{
								childLayoutVertical();
//                    backgroundColor("#00f8");
								height("100%");
								width("20%");

								panel(new PanelBuilder("panel_top_right1") {
									{
										childLayoutCenter();
//                        backgroundColor("#00f8");
										height("15%");
										width("100%");

										control(new LabelBuilder() {
											{
												//color("#000");
												width("100%");
												height("100%");
											}
										});
									}
								});

								panel(new PanelBuilder("panel_top_right2") {
									{
										childLayoutCenter();
//                        backgroundColor("#44f8");
										height("15%");
										width("100%");
									}
								});

								panel(new PanelBuilder("panel_bot_right") {
									{
										childLayoutCenter();
										valignCenter();
//                        backgroundColor("#88f8");
										height("70%");
										width("100%");
									}
								});
							}
						}); // panel added
					}
				});
			}
		}.build(nifty2));

		nifty2.gotoScreen("start"); // start the screen

	}

	@Override
	protected void onDisable() {
		//Called when the state was previously enabled but is now disabled
		//either because setEnabled(false) was called or the state is being
		//cleaned up.
	}

	@Override
	public void update(float tpf) {
		//TODO: implement behavior during runtime
		//ipText = nifty.getNifty().getCurrentScreen().findNiftyControl("address", TextField.class).getRealText();
	}
}
