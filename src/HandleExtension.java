import com.smartfoxserver.v2.core.SFSEventType;

public class HandleExtension extends BaseExtension {
    @java.lang.Override
    public void init() {
        trace("____________________________ HandleExtension ____________________________");
        super.init();
    }

    @Override
    public void initModule() {
        new HandlerZone(this);
        new HandlerLogin(this);
        new HandlerUser(this);
    }
}
