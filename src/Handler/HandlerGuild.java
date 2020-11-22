package Handler;

import Base.BaseExtension;
import Base.BaseHandler;
import Controls.C_Account;
import Controls.C_EventGuild;
import Controls.C_Guild;
import Models.M_Account;
import Models.M_EventGuild;
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
            case CmdDefine.CMD.GET_GUILD:
                HandleGetGuild(user, data);
                break;
            case CmdDefine.CMD.GET_GUILDS:
                HandleGetGuilds(user, data);
                break;
            case CmdDefine.CMD.CREATE_GUILD:
                HandleCreateGuild(user, data);
                break;
            case CmdDefine.CMD.OUT_GUILD:
                HandleOutGuild(user, data);
                break;
            case CmdDefine.CMD.PLEASE_GUILD:
                HandlePleaseGuild(user, data);
                break;
            case CmdDefine.CMD.FIX_MASTER_GUILD:
                HandleChangeMaster(user, data);
                break;
            case CmdDefine.CMD.GET_NOTI_GUILD:
                HandleGetNotiGuild(user, data);
                break;
            case CmdDefine.CMD.GET_EVENT_GUILD:
                HandleGetEventGuild(user, data);
                break;
            case CmdDefine.CMD.GET_MEMBER_GUID:
                HandleGetMemberGuild(user, data);
                break;
            case CmdDefine.CMD.FIX_NOTI_GUILD:
                HandleFixNotiGuild(user, data);
                break;
        }
    }

    private void HandleChangeMaster(User user, ISFSObject data) {
        trace("____________________________ HandleChangeMaster ____________________________");

        int id_ac = Integer.parseInt(user.getName());
        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());

        int id_guild = data.getInt(CmdDefine.ModuleGuild.ID);
        int master = data.getInt(CmdDefine.ModuleGuild.MASTER);

        // === Thao tác database ===
        C_Guild.setMaster(id_guild, master);

        C_EventGuild.insert("Hội trưởng: " + C_Account.get(id_ac).name + " nhường chức cho " + C_Account.get(master).name, id_guild);

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.FIX_MASTER_GUILD);
        trace(packet.getDump());
        this.send(CmdDefine.Module.MODULE_GUILD, packet, user);
    }

    private void HandlePleaseGuild(User user, ISFSObject data) {
        trace("____________________________ HandlePleaseGuild ____________________________");

        int id_ac = Integer.parseInt(user.getName());

        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());
        int id_guild = data.getInt(CmdDefine.ModuleGuild.ID);

        // === Thao tác database ===
        C_Guild.insertAccount(id_guild, id_ac);
        C_Account.setJob(id_ac, 0, true);
        M_Guild guild = C_Guild.get(id_guild, true);

        C_EventGuild.insert(C_Account.get(id_ac).name + " Gia nhập hội !", id_guild);

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        packet.putSFSObject(CmdDefine.ModuleGuild.GUILD, guild.parse());
        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.PLEASE_GUILD);
        trace(packet.getDump());
        this.send(CmdDefine.Module.MODULE_GUILD, packet, user);
    }

    private void HandleGetGuild(User user, ISFSObject data) {
        trace("____________________________ HandleGetGuild ____________________________");

        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());
        int id = data.getInt(CmdDefine.ModuleGuild.ID);

        // === Thao tác database ===
        M_Guild guild = C_Guild.get(id, true);

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        packet.putSFSObject(CmdDefine.ModuleGuild.GUILD, guild.parse());
        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.GET_GUILD);
        trace(packet.getDump());
        this.send(CmdDefine.Module.MODULE_GUILD, packet, user);
    }

    private void HandleGetNotiGuild(User user, ISFSObject data) {
        trace("____________________________ HandleGetNotiGuild ____________________________");

        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());
        int id = data.getInt(CmdDefine.ModuleGuild.ID);

        // === Thao tác database ===
        String noti = C_Guild.get(id, false).noti;

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        packet.putUtfString(CmdDefine.ModuleGuild.NOTI, noti);
        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.GET_NOTI_GUILD);
        trace(packet.getDump());
        this.send(CmdDefine.Module.MODULE_GUILD, packet, user);
    }

    private void HandleGetEventGuild(User user, ISFSObject data) {
        trace("____________________________ HandleGetEventGuild ____________________________");

        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());
        int id = data.getInt(CmdDefine.ModuleGuild.ID);
        int count = data.getInt(CmdDefine.ModuleEventGuild.COUNT);

        // === Thao tác database ===
        ArrayList<M_EventGuild> lstEvt = C_EventGuild.getByIdGuild(id, count);

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        ISFSArray events = new SFSArray();
        for(int i = 0; i < lstEvt.size(); i++){
            events.addSFSObject(lstEvt.get(i).parse());
        }
        packet.putSFSArray(CmdDefine.ModuleGuild.EVENTS, events);

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.GET_EVENT_GUILD);
        trace(packet.getDump());
        this.send(CmdDefine.Module.MODULE_GUILD, packet, user);
    }

    private void HandleGetMemberGuild(User user, ISFSObject data) {
        trace("____________________________ HandleGetMemberGuild ____________________________");

        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());
        int id = data.getInt(CmdDefine.ModuleGuild.ID);

        // === Thao tác database ===
        ArrayList<M_Account> lstAccount = C_Account.getByIdGuild(id);

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        ISFSArray accounts = new SFSArray();
        for(int i = 0; i < lstAccount.size(); i++){
            accounts.addSFSObject(lstAccount.get(i).parse());
        }
        packet.putSFSArray(CmdDefine.ModuleGuild.ACCOUNTS, accounts);

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.GET_MEMBER_GUID);
        trace(packet.getDump());
        this.send(CmdDefine.Module.MODULE_GUILD, packet, user);
    }

    private void HandleFixNotiGuild(User user, ISFSObject data) {
        trace("____________________________ HandleFixNotiGuild ____________________________");

        int id_ac = Integer.parseInt(user.getName());

        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());
        int id_guild = data.getInt(CmdDefine.ModuleGuild.ID);
        String noti = data.getUtfString(CmdDefine.ModuleGuild.NOTI);

        // === Thao tác database ===
        C_Guild.setNoti(id_guild, noti);

        C_EventGuild.insert(C_Account.get(id_ac).name + " Sửa thông báo !", id_guild);

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.FIX_NOTI_GUILD);
        trace(packet.getDump());
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
        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.GET_GUILDS);
        trace(packet.getDump());
        this.send(CmdDefine.Module.MODULE_GUILD, packet, user);
    }

    private void HandleCreateGuild(User user, ISFSObject data) {
        trace("____________________________ HandleCreateGuild ____________________________");

        int master = Integer.parseInt(user.getName());

        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());
        String name = data.getUtfString(CmdDefine.ModuleGuild.NAME);

        // === Thao tác database ===
        // Thêm guild
        int id = C_Guild.insert(name, master);
        // Thay đổi chức vụ
        C_Account.setJob(master, 1, true);
        // Lấy lại thông tin guild
        M_Guild guild = C_Guild.get(id, true);

        C_EventGuild.insert(C_Account.get(master).name + " Tạo hội !", id);

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);
        packet.putSFSObject(CmdDefine.ModuleGuild.GUILD, guild.parse());

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.CREATE_GUILD);
        trace(packet.getDump());
        this.send(CmdDefine.Module.MODULE_GUILD, packet, user);
    }

    private void HandleOutGuild(User user, ISFSObject data) {
        trace("____________________________ HandleOutGuild ____________________________");

        int id_ac = Integer.parseInt(user.getName());

        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());
        int id_guild = data.getInt(CmdDefine.ModuleGuild.ID);

        // === Thao tác database ===
        C_Account.setJob(id_ac, -1, true);
        C_Guild.deleteAccount(id_guild, id_ac);

        C_EventGuild.insert(C_Account.get(id_ac).name + " Thoát hội !", id_guild);

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.OUT_GUILD);
        trace(packet.getDump());
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
