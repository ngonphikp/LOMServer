package Util;

public class CmdDefine {
    public static final String CMD_ID = "cmdid";
    public static final String ERROR_CODE = "ec";

    public static class CMD{
        public static final int REGISTER = 1001;
        public static final int LOGIN = 1002;
        public static final int GETINFO = 1003;
        public static final int SELECTION = 1004;
        public static final int ARRANGE = 1005;
        public static final int TAVERN = 1006;
        public static final int UPLEVEL = 1007;
        public static final int END_GAME = 1008;
        public static final int GET_GUILDS = 1009;
        public static final int CREATE_GUILD = 1010;
        public static final int GET_GUILD = 1011;
        public static final int OUT_GUILD = 1012;
        public static final int PLEASE_GUILD = 1013;
        public static final int FIX_MASTER_GUILD = 1014;
        public static final int GET_NOTI_GUILD = 1015;
        public static final int GET_EVENT_GUILD = 1016;
        public static final int GET_MEMBER_GUID = 1017;
        public static final int FIX_NOTI_GUILD = 1018;
    }

    public static class ErrorCode{
        public static final short SUCCESS = 0;
        public static final short WRONG_USERNAME_OR_PASSWORD = 1;
        public static final short EXIT_ACCOUNT = 2;
    }

    public static class Module{
        public static final String MODULE_ACCOUNT = "account";
        public static final String MODULE_CHARACTER = "character";
        public static final String MODULE_TICK_MILESTONE = "tick_milestone";
        public static final String MODULE_GUILD = "guild";
        public static final String MODULE_EVENT_GUILD = "event_guild";
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

        public static final String ID_GUILD = "id_guild";

        public static final String ACCOUNT = "account";
        public static final String LOGIN_OUT_DATA = "loginoutdata";

        public static final String CHARACTERS = "characters";

        public static final String TICK_MILESTONES = "tick_milestones";

        public static final String TYPE_TAVERN = "type_tavern";
        public static final String CHARACTER = "character";
    }

    public static class ModuleCharacter{
        public static final String ID = "id_char";
        public static final String ID_CFG = "id_cfg_char";
        public static final String LV = "lv_char";
        public static final String IDX = "idx_char";
    }

    public static class ModuleTickMilestone{
        public static final String ID = "id_tms";
        public static final String STAR = "star_tms";
        public static final String ID_AC = "id_ac";
        public static final String ID_ML = "id_ml";

        public static final String IS_SAVE = "is_save";
    }

    public static class ModuleGuild
    {
        public static final String ID = "id_guild";
        public static final String NAME = "name_guild";
        public static final String LV = "lv_guild";
        public static final String NOTI = "noti_guild";

        public static final String ACCOUNTS = "accounts";

        public static final String MASTER = "master";

        public static final String GUILD = "guild";
        public static final String GUILDS = "guilds";

        public static final String EVENTS = "events";
    }

    public static class ModuleEventGuild
    {
        public static final String ID = "id_evt_guild";
        public static final String CONTENT = "content_evt_guild";
        public static final String TIME = "time_evt_guild";

        public static final String COUNT = "count_evt_guild";
    }
}
