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
        public static final int ENDGAME = 1008;
        public static final int GETGUILDS = 1009;
        public static final int CREATEGUILD = 1010;
        public static final int GETGUILD = 1011;
        public static final int OUTGUILD = 1012;
    }

    public static class Module{
        public static final String MODULE_USER = "user";
        public static final String MODULE_CHARACTER = "character";
        public static final String MODULE_TICKMILESTONE = "tickmilestone";
        public static final String MODULE_GUILD = "guild";
    }

    public static class ModuleUser{
        public static final String ID = "id";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String NAME = "name";

        public static final String TAI_KHOAN = "taikhoan";
        public static final String LOGIN_OUT_DATA = "loginOutData";

        public static final String NHAN_VAT_S = "nhanvats";

        public static final String TICK_MILESTONES = "tick_milestones";

        public static final String TYPE_TAVERN = "type_tavern";
        public static final String NHAN_VAT = "nhanvat";
    }

    public static class ModuleCharacter{
        public static final String ID_NV = "id_nv";
        public static final String ID_CFG = "id_cfg";
        public static final String ID_TK = "id_tk";
        public static final String LV = "lv";
        public static final String IDX = "idx";
    }

    public static class ModuleTickMilestone{
        public static final String ID_TML = "id_tml";
        public static final String ID_TK = "id_tk";
        public static final String ID_ML = "id_ml";
        public static final String STAR = "star";

        public static final String IS_SAVE = "is_save";
    }
}
