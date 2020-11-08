package Handler;

import Base.BaseExtension;
import Base.BaseHandler;
import Controls.C_TickMilestone;
import Util.CmdDefine;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class HandlerTickMilestone extends BaseHandler {
    public HandlerTickMilestone(BaseExtension extension) {
        super(extension);
    }

    @Override
    protected void HandleClientRequest(int cmdId, User user, ISFSObject data) {
        switch (cmdId) {
            case CmdDefine.CMD.ENDGAME:
                HandleEndGame(user, data);
                break;
        }
    }

    private void HandleEndGame(User user, ISFSObject data) {
        trace("____________________________ HandleEndGame ____________________________");

        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());
        boolean isSave = data.getBool(CmdDefine.ModuleTickMilestone.IS_SAVE);
        int id_ac = data.getInt(CmdDefine.ModuleTickMilestone.ID_AC);
        int id_ml = data.getInt(CmdDefine.ModuleTickMilestone.ID_ML);
        int star = data.getInt(CmdDefine.ModuleTickMilestone.STAR);

        // === Thao tác database ===
        if(isSave){
            C_TickMilestone.setStar(id_ac, id_ml, star);
        }

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.ENDGAME);
        this.send(CmdDefine.Module.MODULE_TICK_MILESTONE, packet, user);
    }

    @Override
    protected void HandleServerEvent(SFSEventType type, ISFSEvent event) {
        switch (type) {

        }
    }

    @Override
    protected void initHandlerClientRequest() {
        this.extension.addRequestHandler(CmdDefine.Module.MODULE_TICK_MILESTONE, this);
    }

    @Override
    protected void initHandlerServerEvent() {
        this.extension.addServerHandler(CmdDefine.Module.MODULE_TICK_MILESTONE, this);
    }
}
