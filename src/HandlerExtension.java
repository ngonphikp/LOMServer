import Base.BaseExtension;
import Base.CouchBase;
import Handler.*;

public class HandlerExtension extends BaseExtension {

    @java.lang.Override
    public void init() {
        trace("____________________________ HandleExtension ____________________________");
        super.init();

        CouchBase.Init();
    }

    @Override
    public void initModule() {
        new HandlerZone(this);
        new HandlerLogin(this);
        new HandlerAccount(this);
        new HandlerCharacter(this);
        new HandlerTickMilestone(this);
        new HandlerGuild(this);
    }
}
