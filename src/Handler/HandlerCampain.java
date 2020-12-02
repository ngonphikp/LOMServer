package Handler;

import Base.BaseExtension;
import Base.BaseHandler;
import Controls.C_TickCampain;
import Models.M_TickCampain;
import Util.CmdDefine;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class HandlerCampain extends BaseHandler {
    public HandlerCampain(BaseExtension extension) {
        super(extension, CmdDefine.Module.MODULE_CAMPAIN);
    }

    @Override
    protected void HandleClientRequest(int cmdId, User user, ISFSObject data) {
        switch (cmdId) {
            case CmdDefine.CMD.END_GAME_CAMPAIN:
                HandleEndGame(user, data);
                break;
        }
    }

    private void HandleEndGame(User user, ISFSObject data) {
        trace("____________________________ HandleEndGame ____________________________");

        int id_ac = Integer.parseInt(user.getName());

        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());
        boolean isSave = data.getBool(CmdDefine.ModuleTickCampain.IS_SAVE);
        int id_ml = data.getInt(CmdDefine.ModuleTickCampain.ID_ML);
        int star = data.getInt(CmdDefine.ModuleTickCampain.STAR);

        // === Thao tác database ===
        if(isSave){
            M_TickCampain tick = C_TickCampain.get(id_ac, id_ml);
            if(tick == null){
                int id = C_TickCampain.insert(id_ac, id_ml, star);
                tick = C_TickCampain.get(id);
            }
            else {
                tick.star = star;
                C_TickCampain.set(tick);
            }
        }

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.END_GAME_CAMPAIN);
        trace(packet.getDump());
        this.send(this.module, packet, user);
    }

    @Override
    protected void HandleServerEvent(SFSEventType type, ISFSEvent event) {
        switch (type) {

        }
    }

    @Override
    protected void initHandlerClientRequest() {
        this.extension.addRequestHandler(CmdDefine.Module.MODULE_CAMPAIN, this);
    }

    @Override
    protected void initHandlerServerEvent() {
        this.extension.addServerHandler(CmdDefine.Module.MODULE_CAMPAIN, this);
    }
}
