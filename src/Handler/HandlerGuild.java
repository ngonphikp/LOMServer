package Handler;

import Base.BaseExtension;
import Base.BaseHandler;
import Base.RoomManage;
import Controls.*;
import Models.*;
import Util.C_Util;
import Util.CmdDefine;
import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;

import java.util.ArrayList;


public class HandlerGuild extends BaseHandler {
    public HandlerGuild(BaseExtension extension) {
        super(extension, CmdDefine.Module.MODULE_GUILD);
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
            case CmdDefine.CMD.GET_BOSSES_GUILD:
                HandleGetBosses(user, data);
                break;
            case CmdDefine.CMD.GET_TICK_BOSS_GUILD:
                HandleGetTickBoss(user, data);
                break;
            case CmdDefine.CMD.UNLOCK_BOSS_GUILD:
                HandleUnLockBoss(user, data);
                break;
            case CmdDefine.CMD.END_GAME_BOSS_GUILD:
                HandleEndGameBoss(user, data);
                break;
            case CmdDefine.CMD.REWARD_BOSS_GUILD:
                HandleRewardBoss(user, data);
                break;
        }
    }

    private void HandleChangeMaster(User user, ISFSObject data) {
        trace("____________________________ HandleChangeMaster ____________________________");

        int id_ac = Integer.parseInt(user.getName());
        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());

        // Lấy id guild
        int id_guild = C_Util.KeyToId(CmdDefine.Module.MODULE_GUILD, C_Guild.getKey(id_ac));
        int master = data.getInt(CmdDefine.ModuleGuild.MASTER);

        // === Thao tác database ===
        M_Guild guild = C_Guild.get(id_guild, false);
        guild.master = master;
        C_Guild.set(guild);

        C_EventGuild.insert("Hội trưởng: " + C_Account.get(id_ac).name + " nhường chức cho " + C_Account.get(master).name, id_guild);

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.FIX_MASTER_GUILD);
        trace(packet.getDump());
        this.send(this.module, packet, user);
    }

    private void HandlePleaseGuild(User user, ISFSObject data) {
        trace("____________________________ HandlePleaseGuild ____________________________");

        int id_ac = Integer.parseInt(user.getName());

        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());
        int id_guild = data.getInt(CmdDefine.ModuleGuild.ID);

        // === Thao tác database ===
        C_Guild.insertAccount(id_guild, id_ac);
        M_Account account = C_Account.get(id_ac);
        account.job = 0;
        account.dediTotal = 0;
        account.dediWeek = 0;
        C_Account.set(account);
        M_Guild guild = C_Guild.get(id_guild, true);

        C_EventGuild.insert(C_Account.get(id_ac).name + " Gia nhập hội !", id_guild);

        // === Thao tác với room Guild
        RoomManage manage = new RoomManage(this.getParentExtension());
        Room room = manage.getRoom("GD" + guild.id);
        if(room == null){
            manage.initRoom("HandlerExtension", "GD" + guild.id, CmdDefine.Room.Guild, 50);
            room = manage.getRoom("GD" + guild.id);
        }
        manage.userJoinRoom(user, room);

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        packet.putSFSObject(CmdDefine.ModuleGuild.GUILD, guild.parse());
        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.PLEASE_GUILD);
        trace(packet.getDump());
        this.send(this.module, packet, user);
    }

    private void HandleGetGuild(User user, ISFSObject data) {
        trace("____________________________ HandleGetGuild ____________________________");

        int id = Integer.parseInt(user.getName());
        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());
        // Lấy id guild
        int id_guild = C_Util.KeyToId(CmdDefine.Module.MODULE_GUILD, C_Guild.getKey(id));

        // === Thao tác database ===
        M_Guild guild = C_Guild.get(id_guild, true);

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        packet.putSFSObject(CmdDefine.ModuleGuild.GUILD, guild.parse());
        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.GET_GUILD);
        trace(packet.getDump());
        this.send(this.module, packet, user);
    }

    private void HandleGetNotiGuild(User user, ISFSObject data) {
        trace("____________________________ HandleGetNotiGuild ____________________________");

        int id = Integer.parseInt(user.getName());
        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());
        // Lấy id guild
        int id_guild = C_Util.KeyToId(CmdDefine.Module.MODULE_GUILD, C_Guild.getKey(id));

        // === Thao tác database ===
        String noti = C_Guild.get(id_guild, false).noti;

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        packet.putUtfString(CmdDefine.ModuleGuild.NOTI, noti);
        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.GET_NOTI_GUILD);
        trace(packet.getDump());
        this.send(this.module, packet, user);
    }

    private void HandleGetEventGuild(User user, ISFSObject data) {
        trace("____________________________ HandleGetEventGuild ____________________________");

        int id = Integer.parseInt(user.getName());
        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());
        // Lấy id guild
        int id_guild = C_Util.KeyToId(CmdDefine.Module.MODULE_GUILD, C_Guild.getKey(id));
        int count = data.getInt(CmdDefine.ModuleEventGuild.COUNT);

        // === Thao tác database ===
        ArrayList<M_EventGuild> lstEvt = C_EventGuild.getByIdGuild(id_guild, count);

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
        this.send(this.module, packet, user);
    }

    private void HandleGetMemberGuild(User user, ISFSObject data) {
        trace("____________________________ HandleGetMemberGuild ____________________________");

        int id = Integer.parseInt(user.getName());
        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());
        // Lấy id guild
        int id_guild = C_Util.KeyToId(CmdDefine.Module.MODULE_GUILD, C_Guild.getKey(id));

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
        this.send(this.module, packet, user);
    }

    private void HandleFixNotiGuild(User user, ISFSObject data) {
        trace("____________________________ HandleFixNotiGuild ____________________________");

        int id_ac = Integer.parseInt(user.getName());

        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());
        // Lấy id guild
        int id_guild = C_Util.KeyToId(CmdDefine.Module.MODULE_GUILD, C_Guild.getKey(id_ac));
        String noti = data.getUtfString(CmdDefine.ModuleGuild.NOTI);

        // === Thao tác database ===
        M_Guild guild = C_Guild.get(id_guild, false);
        guild.noti = noti;
        C_Guild.set(guild);

        C_EventGuild.insert(C_Account.get(id_ac).name + " Sửa thông báo !", id_guild);

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.FIX_NOTI_GUILD);
        trace(packet.getDump());
        this.send(this.module, packet, user);
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
        this.send(this.module, packet, user);
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
        M_Account account = C_Account.get(master);
        account.job = 1;
        account.dediTotal = 0;
        account.dediWeek = 0;
        C_Account.set(account);
        // Lấy lại thông tin guild
        M_Guild guild = C_Guild.get(id, true);

        C_EventGuild.insert(C_Account.get(master).name + " Tạo hội !", id);

        // === Thao tác room guild
        // Tạo room guild + id
        RoomManage manage = new RoomManage(this.getParentExtension());
        manage.initRoom("HandlerExtension", "GD" + guild.id, CmdDefine.Room.Guild, 50);
        // User join room
        Room room = manage.getRoom("GD" + guild.id);
        manage.userJoinRoom(user, room);

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);
        packet.putSFSObject(CmdDefine.ModuleGuild.GUILD, guild.parse());

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.CREATE_GUILD);
        trace(packet.getDump());
        this.send(this.module, packet, user);
    }

    private void HandleOutGuild(User user, ISFSObject data) {
        trace("____________________________ HandleOutGuild ____________________________");

        int id_ac = Integer.parseInt(user.getName());

        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());
        // Lấy id guild
        int id_guild = C_Util.KeyToId(CmdDefine.Module.MODULE_GUILD, C_Guild.getKey(id_ac));

        // === Thao tác database ===
        M_Account account = C_Account.get(id_ac);
        account.job = -1;
        account.dediWeek = 0;
        account.dediTotal = 0;
        C_Account.set(account);
        C_Guild.deleteAccount(id_guild, id_ac);

        C_EventGuild.insert(C_Account.get(id_ac).name + " Thoát hội !", id_guild);

        // === Thao tác room guild
        // User out room
        RoomManage manage = new RoomManage(this.getParentExtension());
        Room room = manage.getRoom("GD" + id_guild);
        manage.userOutRoom(user, room);

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.OUT_GUILD);
        trace(packet.getDump());
        this.send(this.module, packet, user);
    }

    private void HandleGetBosses(User user, ISFSObject data) {
        trace("____________________________ HandleGetBosses ____________________________");

        int id_ac = Integer.parseInt(user.getName());

        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());
        // Lấy id guild
        int id_guild = C_Util.KeyToId(CmdDefine.Module.MODULE_GUILD, C_Guild.getKey(id_ac));

        // === Thao tác database ===
        ArrayList<M_BossGuild> bossGuilds = C_BossGuild.gets(id_guild);

        if(bossGuilds.size() == 0){
            for(int i = 0; i < 4; i++){
                C_BossGuild.insert(id_guild, i + 1, 0, 1000000);
            }
            bossGuilds = C_BossGuild.gets(id_guild);
        }

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        ISFSArray bgs = new SFSArray();
        for(int i = 0; i < bossGuilds.size(); i++){
            bgs.addSFSObject(bossGuilds.get(i).parse());
        }
        packet.putSFSArray(CmdDefine.ModuleGuild.BOSSES, bgs);

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.GET_BOSSES_GUILD);
        trace(packet.getDump());
        this.send(this.module, packet, user);
    }

    private void HandleGetTickBoss(User user, ISFSObject data) {
        trace("____________________________ HandleGetTickBoss ____________________________");

        int id_ac = Integer.parseInt(user.getName());

        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());
        int id_bg = data.getInt(CmdDefine.ModuleBossGuild.ID);

        // === Thao tác database ===
        M_BossGuild bossGuild = C_BossGuild.get(id_bg);
        M_TickBossGuild tickBossGuild = C_TickBossGuild.get(id_ac, id_bg);
        if(tickBossGuild == null){
            int cur_turn = 0;
            boolean is_reward = false;
            switch (bossGuild.status){
                case 0: // Lock
                    break;
                case 1:
                    cur_turn = 2;
                    break;
                case 2:
                    break;
            }

            int id_tbg = C_TickBossGuild.insert(id_ac, id_bg, cur_turn, is_reward);

            tickBossGuild = C_TickBossGuild.get(id_tbg);
        }

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        packet.putSFSObject(CmdDefine.ModuleGuild.BOSS, bossGuild.parse());
        packet.putSFSObject(CmdDefine.ModuleGuild.TICK_BOSS, tickBossGuild.parse());

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.GET_TICK_BOSS_GUILD);
        trace(packet.getDump());
        this.send(this.module, packet, user);
    }

    private void HandleUnLockBoss(User user, ISFSObject data) {
        trace("____________________________ HandleUnLockBoss ____________________________");
        int id_ac = Integer.parseInt(user.getName());

        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());
        // Lấy id guild
        int id_guild = C_Util.KeyToId(CmdDefine.Module.MODULE_GUILD, C_Guild.getKey(id_ac));
        int id_bg = data.getInt(CmdDefine.ModuleBossGuild.ID);

        // === Thao tác database ===
        M_BossGuild bossGuild = C_BossGuild.get(id_bg);
        bossGuild.status = 1;
        bossGuild.cur_hp = 1000000;
        C_BossGuild.set(bossGuild);
        C_EventGuild.insert(C_Account.get(id_ac).name + " Mở chiến boss !", id_guild);

        ArrayList<M_TickBossGuild> tickBossGuilds = C_TickBossGuild.gets(id_bg);
        for (int i = 0; i < tickBossGuilds.size(); i++) {
            tickBossGuilds.get(i).cur_turn = 2;
            tickBossGuilds.get(i).is_reward = false;
            C_TickBossGuild.set(tickBossGuilds.get(i));
        }

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.UNLOCK_BOSS_GUILD);
        trace(packet.getDump());
        this.send(this.module, packet, user);
    }

    private void HandleEndGameBoss(User user, ISFSObject data) {
        trace("____________________________ HandleEndGameBoss ____________________________");

        int id_ac = Integer.parseInt(user.getName());

        // === Đọc dữ liệu gửi lên ===
        // Lấy id guild
        int id_guild = C_Util.KeyToId(CmdDefine.Module.MODULE_GUILD, C_Guild.getKey(id_ac));
        trace(data.getDump());
        int id_bg = data.getInt(CmdDefine.ModuleBossGuild.ID);
        int point = data.getInt(CmdDefine.ModuleTickBossGuild.POINT);

        // === Thao tác database ===
        M_BossGuild bossGuild = C_BossGuild.get(id_bg);
        M_TickBossGuild tickBossGuild = C_TickBossGuild.get(id_ac, id_bg);

        // Trừ lượt đánh bos
        tickBossGuild.cur_turn--;
        C_TickBossGuild.set(tickBossGuild);

        if(bossGuild.status == 1){
            // Trừ lượng máu boss
            bossGuild.cur_hp -= point;

            // Nếu boss chết
            if(bossGuild.cur_hp <= 0){
                // Mở trạng thái nhận quà
                bossGuild.status = 2;
                bossGuild.cur_hp = 0;
                C_BossGuild.set(bossGuild);

                // Mở nhận quả với các thành viên đã tham gia đánh boss
                ArrayList<M_TickBossGuild> tickBossGuilds = C_TickBossGuild.gets(id_bg);
                for (int i = 0; i < tickBossGuilds.size(); i++) {
                    if(tickBossGuilds.get(i).cur_turn < 2){
                        tickBossGuilds.get(i).is_reward = true;
                        C_TickBossGuild.set(tickBossGuilds.get(i));
                    }
                }

                C_EventGuild.insert(C_Account.get(id_ac).name + " hạ gục boss !", id_guild);
            }
            else{
                C_BossGuild.set(bossGuild);
            }
        }

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.END_GAME_BOSS_GUILD);
        trace(packet.getDump());
        this.send(this.module, packet, user);
    }

    private void HandleRewardBoss(User user, ISFSObject data) {
        trace("____________________________ HandleRewardBoss ____________________________");

        // === Đọc dữ liệu gửi lên ===
        trace(data.getDump());
        int id_tbg = data.getInt(CmdDefine.ModuleTickBossGuild.ID);

        // === Thao tác database ===
        M_TickBossGuild tickBossGuild = C_TickBossGuild.get(id_tbg);
        tickBossGuild.is_reward = false;
        C_TickBossGuild.set(tickBossGuild);

        // === Gửi dữ liệu xuống ===
        ISFSObject packet = new SFSObject();
        packet.putShort(CmdDefine.ERROR_CODE, CmdDefine.ErrorCode.SUCCESS);

        packet.putInt(CmdDefine.CMD_ID, CmdDefine.CMD.REWARD_BOSS_GUILD);
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
        this.extension.addRequestHandler(CmdDefine.Module.MODULE_GUILD, this);
    }

    @Override
    protected void initHandlerServerEvent() {
        this.extension.addServerHandler(CmdDefine.Module.MODULE_GUILD, this);
    }
}
