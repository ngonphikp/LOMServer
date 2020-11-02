import com.smartfoxserver.v2.core.SFSEventType;

public class HandleExtension extends BaseExtension {
    @java.lang.Override
    public void init() {
        trace("____________________________ HandleExtension ____________________________");
        super.init();
        this.addEventHandler(SFSEventType.SERVER_READY, OnServerReadyHandler.class);
    }

    @Override
    public void initModule() {
        new HandlerZone(this);
        new HandlerLogin(this);
        new HandlerUser(this);
    }

    private class OnServerReadyHandler {
    }
}
