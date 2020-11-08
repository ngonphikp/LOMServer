package Handler;

import Base.BaseExtension;
import Base.BaseHandler;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;

public class HandlerZone extends BaseHandler {

    public HandlerZone(BaseExtension extension) {
        super(extension);
    }

    @Override
    protected void HandleClientRequest(int cmdId, User user, ISFSObject data) {
        switch (cmdId){

        }
    }

    @Override
    protected void HandleServerEvent(SFSEventType type, ISFSEvent event) {
        switch (type) {
            case USER_JOIN_ZONE:
                OnUserJoinZone(event);
                break;
            case USER_DISCONNECT:
                OnUserDisconnect(event);
                break;
        }
    }

    public void OnUserJoinZone(ISFSEvent event){
        trace("____________________________ OnUserJoinZone ____________________________");
    }

    public void OnUserDisconnect(ISFSEvent event){
        trace("____________________________ OnUserDisconnect ____________________________");
    }

    @Override
    protected void initHandlerClientRequest() {

    }

    @Override
    protected void initHandlerServerEvent() {
        this.extension.addEventHandler(SFSEventType.USER_JOIN_ZONE, this);
        this.extension.addEventHandler(SFSEventType.USER_DISCONNECT, this);
    }
}
