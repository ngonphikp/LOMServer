package Util;

public class CmdDefine {
    public static final String CMD_ID = "cmdid";
    public static final String ERROR_CODE = "ec";

    public static class CMD{
        public static final int REGISTER = 1001;
        public static final int LOGIN = 1002;

        public static final int GET_INFO = 2001;
        public static final int SELECTION = 2002;
        public static final int TAVERN = 2003;

        public static final int ARRANGE = 3001;
        public static final int UPLEVEL = 3002;

        public static final int GET_TICKS_CAMPAIGN = 4001;
        public static final int END_GAME_CAMPAIGN = 4002;

        public static final int GET_GUILDS = 5001;
        public static final int CREATE_GUILD = 5002;
        public static final int GET_GUILD = 5003;
        public static final int OUT_GUILD = 5004;
        public static final int PLEASE_GUILD = 5005;
        public static final int CHANGE_MASTER_GUILD = 5006;
        public static final int GET_NOTI_GUILD = 5007;
        public static final int GET_EVENT_GUILD = 5008;
        public static final int GET_MEMBER_GUID = 5009;
        public static final int FIX_NOTI_GUILD = 5010;
        public static final int GET_BOSSES_GUILD = 5011;
        public static final int GET_TICK_BOSS_GUILD = 5012;
        public static final int UNLOCK_BOSS_GUILD = 5013;
        public static final int END_GAME_BOSS_GUILD = 5014;
        public static final int REWARD_BOSS_GUILD = 5015;

        public static final int GET_ACCOUNT_GLOBAL = 6001;
        public static final int SEND_MESSAGE_GLOBAL = 6002;
        public static final int GET_ACCOUNT_GUILD = 6003;
        public static final int SEND_MESSAGE_GUILD = 6004;
        public static final int GET_DETAILS = 6005;
        public static final int MAKE_FRIEND = 6006;
        public static final int REMOVE_FRIEND = 6007;
        public static final int GET_ACCOUNT_FRIEND = 6008;
        public static final int FIND_ACCOUNT_GLOBAL = 6009;
        public static final int SEND_MESSAGE_PRIVATE = 6010;

        public static final int START_PVP = 7001;
        public static final int CANCLE_PVP = 7002;

        public static final int JOIN_ROOM_GAME = 8001;
        public static final int OUT_ROOM_GAME = 8002;
        public static final int UN_ACTIVE_CHAR = 8003;
        public static final int ACTIVE_CHAR = 8004;
        public static final int CHANGE_CHAR = 8005;
        public static final int LOCK_ARRANGE = 8006;
        public static final int START_GAME = 8007;
        public static final int SEND_SCENARIO = 8008;

        public static final int INIT_CHARS = 8009;
    }

    public static class ErrorCode{
        public static final short SUCCESS = 0;
        public static final short WRONG_USERNAME_OR_PASSWORD = 1;
        public static final short EXIT_ACCOUNT = 2;
        public static final short ACCOUNT_LOGON = 3;

        public static final short UNFRIENDED = 4;
    }

    public static class Room{
        public static  final String Global = "Global";
        public static  final String Guild = "Guild";
        public static  final String Game = "Game";
    }

    public static class Module{
        public static final String MODULE_ACCOUNT = "account";
        public static final String MODULE_CHARACTER = "character";
        public static final String MODULE_CAMPAIGN = "campaign";
        public static final String MODULE_TICK_CAMPAIGN = "tick_campaign";
        public static final String MODULE_GUILD = "guild";
        public static final String MODULE_EVENT_GUILD = "event_guild";
        public static final String MODULE_CHAT_AND_FRIEND = "chat_and_friend";
        public static final String MODULE_BOSS_GUILD = "boss_guild";
        public static final String MODULE_TICK_BOSS_GUILD = "tick_boss_guild";
        public static final String MODULE_PVP = "PvP";
        public static final String MODULE_GAME = "Game";
    }

    public static class ModuleAccount{
        public static final String ID = "id_ac";
        public static final String USERNAME = "username_ac";
        public static final String PASSWORD = "password_ac";

        public static final String NAME = "name_ac";

        public static final String LV = "lv_ac";
        public static final String POWER = "power_ac";

        public static final String JOB = "job_ac";
        public static final String DEDITOTAL = "dedication_total_ac";
        public static final String DEDIWEEK = "dedication_week_ac";

        public static final String ACCOUNT = "account";
        public static final String ACCOUNTS = "accounts";

        public static final String LOGIN_OUT_DATA = "loginoutdata";
        public static final String TYPE_TAVERN = "type_tavern";
    }

    public static class ModuleCharacter{
        public static final String ID = "id_char";
        public static final String ID_CFG = "id_cfg_char";
        public static final String LV = "lv_char";
        public static final String IDX = "idx_char";

        public static final String CHARACTER = "character";
        public static final String CHARACTERS = "characters";
    }

    public static class ModuleCampaign{
        public static final String ID = "id_camp";
    }

    public static class ModuleTickCampaign{
        public static final String ID = "id_tc";
        public static final String STAR = "star_tc";

        public static final String TICK_CAMPAIGN = "tick_camp";
        public static final String TICKS_CAMPAIGN = "ticks_camp";
    }

    public static class ModuleGuild
    {
        public static final String ID = "id_guild";
        public static final String NAME = "name_guild";
        public static final String LV = "lv_guild";
        public static final String NOTI = "noti_guild";

        public static final String GUILD = "guild";
        public static final String GUILDS = "guilds";

        public static final String MASTER = "master_guild";
    }

    public static class ModuleEventGuild
    {
        public static final String ID = "id_evtg";
        public static final String CONTENT = "content_evtg";
        public static final String TIME = "time_evtg";

        public static final String COUNT = "count_evtg";

        public static final String EVENT_GUILD = "evt_guild";
        public static final String EVENTS_GUILD = "evts_guild";
    }

    public static class ModuleCF
    {
        public static final String ID_ONLINES = "id_onlines";
        public static final String MESSAGE = "message_cf";
        public static final String IS_FRIEND = "is_friend";

        public static final String CONTENT = "content_cf";
        public static final String IS_CHECK_ID = "is_check_id";
    }

    public static class ModuleBossGuild{
        public static final String ID = "id_bg";
        public static final String ID_BOSS = "id_boss";
        public static final String CUR_HP = "cur_hp_bg";
        public static final String STATUS = "status_bg";

        public static final String BOSS_GUILD = "boss_guild";
        public static final String BOSSES_GUILD = "bosses_guild";
    }

    public static class ModuleTickBossGuild{
        public static final String ID = "id_tbg";

        public static final String CUR_TURN = "cur_turn_tbg";
        public static final String IS_REWARD = "is_reward_tbg";

        public static final String POINT = "point_tbg";

        public static final String TICK_BOSS_GUILD = "tick_bg";
        public static final String TICKS_BOSS_GUILD = "ticks_bg";
    }
}
