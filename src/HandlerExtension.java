import Base.BaseExtension;
import Base.CouchBase;
import Base.QueueManage;
import Handler.*;

public class HandlerExtension extends BaseExtension {

    @java.lang.Override
    public void init() {
        trace("____________________________ HandleExtension ____________________________");
        super.init();

        CouchBase.init();
        QueueManage.init(this);
    }

    @Override
    public void initModule() {
        new HandlerZone(this);
        new HandlerLogin(this);
        new HandlerAccount(this);
        new HandlerCharacter(this);
        new HandlerCampaign(this);
        new HandlerGuild(this);
        new HandlerCF(this);
        new HandlerPvP(this);
    }
}
