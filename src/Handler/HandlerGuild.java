package Handler;

import Base.BaseExtension;
import Base.BaseHandler;
import Controls.C_Guild;
import Models.M_Guild;
import Util.CmdDefine;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;

import java.util.ArrayList;


public class HandlerGuild extends BaseHandler {
    public HandlerGuild(BaseExtension extension) {
        super(extension);
    }

    @Override
    protected void HandleClientRequest(int cmdId, User user, ISFSObject data) {
        switch (cmdId) {
            case CmdDefine.CMD.GETGUILD:
                HandleGetGuild(user, data);
                break;
            case CmdDefine.CMD.GETGUILDS:
                HandleGetGuilds(user, data);
                break;
            case CmdDefine.CMD.CREATEGUILD:
                HandleCreateGuild(user, data);
                break;
            case CmdDefine.CMD.OUTGUILD:
                HandleOutGuild(user, data);
                break;
        }
    }

    private void HandleGetGuild(User user, ISFSObject data) {
        trace("____________________________ HandleGetGuild ____________________________");

        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());
        int id = data.getInt(CmdDefine.ModuleGuild.ID);

        // === Thao tác database ===

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.GETGUILD);
        this.send(CmdDefine.Module.MODULE_GUILD, packet, user);
    }

    private void HandleGetGuilds(User user, ISFSObject data) {
        trace("____________________________ HandleGetGuilds ____________________________");

        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());

        // === Thao tác database ===
        ArrayList<M_Guild> guilds = C_Guild.getAll();

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        ISFSArray arr = new SFSArray();
        for(int i = 0; i < guilds.size(); i++){
            arr.addSFSObject(guilds.get(i).parse());
        }

        packet.putSFSArray(CmdDefine.ModuleGuild.GUILDS, arr);
        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.GETGUILDS);
        this.send(CmdDefine.Module.MODULE_GUILD, packet, user);
    }

    private void HandleCreateGuild(User user, ISFSObject data) {
        trace("____________________________ HandleCreateGuild ____________________________");

        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());
        String name = data.getUtfString(CmdDefine.ModuleGuild.NAME);
        int master = data.getInt(CmdDefine.ModuleGuild.MASTER);

        // === Thao tác database ===
        // Thêm guild
        int id = C_Guild.insert(name, master);
        // Lấy lại thông tin guild
        M_Guild guild = C_Guild.get(id);

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);
        packet.putSFSObject(CmdDefine.ModuleGuild.GUILD, guild.parse());

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.CREATEGUILD);
        this.send(CmdDefine.Module.MODULE_GUILD, packet, user);
    }

    private void HandleOutGuild(User user, ISFSObject data) {
        trace("____________________________ HandleOutGuild ____________________________");

        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());
        int id = data.getInt(CmdDefine.ModuleAccount.ID);

        // === Thao tác database ===

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.OUTGUILD);
        this.send(CmdDefine.Module.MODULE_GUILD, packet, user);
    }

    @Override
    protected void HandleServerEvent(SFSEventType type, ISFSEvent event) {
        switch (type) {

        }
    }

    @Override
    protected void initHandlerClientRequest() {
        this.extension.addRequestHandler(CmdDefine.Module.MODULE_GUILD, this);
    }

    @Override
    protected void initHandlerServerEvent() {
        this.extension.addServerHandler(CmdDefine.Module.MODULE_GUILD, this);
    }
}
