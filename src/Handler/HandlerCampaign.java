package Handler;

import Base.BaseExtension;
import Base.BaseHandler;
import Controls.C_TickCampaign;
import Models.M_TickCampaign;
import Util.CmdDefine;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;

import java.util.ArrayList;

public class HandlerCampaign extends BaseHandler {
    public HandlerCampaign(BaseExtension extension) {
        super(extension, CmdDefine.Module.MODULE_CAMPAIGN);
    }

    @Override
    protected void HandleClientRequest(int cmdId, User user, ISFSObject data) {
        switch (cmdId) {
            case CmdDefine.CMD.GET_TICKS_CAMPAIGN:
                HandleGetTicks(user, data);
                break;
            case CmdDefine.CMD.END_GAME_CAMPAIGN:
                HandleEndGame(user, data);
                break;
        }
    }

    private void HandleGetTicks(User user, ISFSObject data) {
        trace("____________________________ HandleGetTicks ____________________________");

        int id_ac = Integer.parseInt(user.getName());

        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());

        // === Thao tác database ===
        ArrayList<M_TickCampaign> ticks = C_TickCampaign.gets(id_ac);
        if(ticks.size() == 0 || ticks.get(ticks.size() - 1).star != 0){
            C_TickCampaign.insert(id_ac, ticks.size() + 1, 0);
            ticks = C_TickCampaign.gets(id_ac);
        }

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        ISFSArray arr = new SFSArray();
        for(int i = 0; i < ticks.size(); i++){
            arr.addSFSObject(ticks.get(i).parse());
        }
        packet.putSFSArray(CmdDefine.ModuleTickCampaign.TICKS, arr);

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.GET_TICKS_CAMPAIGN);
        trace(packet.getDump());
        this.send(this.module, packet, user);
    }

    private void HandleEndGame(User user, ISFSObject data) {
        trace("____________________________ HandleEndGame ____________________________");

        int id_ac = Integer.parseInt(user.getName());

        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());
        int id_campaign = data.getInt(CmdDefine.ModuleTickCampaign.ID_CAMPAIGN);
        int star = data.getInt(CmdDefine.ModuleTickCampaign.STAR);

        // === Thao tác database ===
        M_TickCampaign tick = C_TickCampaign.get(id_ac, id_campaign);
        if(tick.star < star){
            tick.star = star;
            C_TickCampaign.set(tick);
        }

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.END_GAME_CAMPAIGN);
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
        this.extension.addRequestHandler(CmdDefine.Module.MODULE_CAMPAIGN, this);
    }

    @Override
    protected void initHandlerServerEvent() {
        this.extension.addServerHandler(CmdDefine.Module.MODULE_CAMPAIGN, this);
    }
}
