import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.extensions.SFSExtension;

public class MyExtension extends SFSExtension {
    @java.lang.Override
    public void init() {
        initModule();

        this.addRequestHandler("math", MathHandler.class);
        this.addEventHandler(SFSEventType.SERVER_READY, OnServerReadyHandler.class);
    }

    private void initModule() {
        new UserHandler(this);
        new ZoneHandler(this);
    }

    public void Init() {
        trace("Hello, this is my first SFS2X Extension!");
    }
}
