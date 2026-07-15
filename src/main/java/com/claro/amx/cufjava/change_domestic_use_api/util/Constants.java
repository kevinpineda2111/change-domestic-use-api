package com.claro.amx.cufjava.change_domestic_use_api.util;

public final class Constants {

    private Constants() {}

    // HTTP response codes
    public static final String CODE_SUCCESS = "200";
    public static final String CODE_BAD_REQUEST = "400";
    public static final String CODE_NOT_FOUND = "404";
    public static final String CODE_SERVER_ERROR = "500";

    // HTTP response messages
    public static final String MSG_SUCCESS = "Operación exitosa";
    public static final String MSG_BAD_REQUEST = "Solicitud incorrecta";
    public static final String MSG_NOT_FOUND = "Recurso no encontrado";
    public static final String MSG_SERVER_ERROR = "Error interno del servidor";

    // Business type codes
    public static final String BUSINESS_TYPE_IF = "IF";
    public static final String BUSINESS_TYPE_TF = "TF";
    public static final String BUSINESS_TYPE_IPTV = "IPTV";

    // Domestic use codes
    public static final String DOMESTIC_USE_DDI = "1";
    public static final String DOMESTIC_USE_DDN = "2";
    public static final String DOMESTIC_USE_SOLO_ENTRANTE = "5";

    // Line status codes
    public static final String LINE_STATUS_INACTIVE = "I";
    public static final String LINE_STATUS_CANCELLED = "C";

    // Stored procedure return codes
    public static final int SP_RESULT_OK = 0;

    // Oracle parameter names for F_VALIDA_TRAMITE
    public static final String SP_PARAM_CELLULAR_NUMBER = "P_CELLULAR_NUMBER";
    public static final String SP_PARAM_PENDIENTE = "P_PENDIENTE";
    public static final String SP_PARAM_ERR_NUM = "P_ERR_NUM";
    public static final String SP_PARAM_ERR_MSJ = "P_ERR_MSJ";

    // Oracle parameter names for PRESUSPE
    public static final String SP_PARAM_CELULAR = "CELULAR";
    public static final String SP_PARAM_TCK = "TCK";
    public static final String SP_PARAM_RAZON = "RAZON";
    public static final String SP_PARAM_USER_CONECT = "P_USER_CONECT";
    public static final String SP_PARAM_POUT_MSG_ERR = "POUT_MSG_ERR";

    // Oracle parameter names for LEVANTA_PRESUSP
    public static final String SP_PARAM_P_CELLULAR_NUMBER = "P_CELLULAR_NUMBER";
    public static final String SP_PARAM_P_ACT_ID = "P_ACT_ID";
    public static final String SP_PARAM_P_RSN_ID = "P_RSN_ID";
    public static final String SP_PARAM_P_TCK = "P_TCK";
    public static final String SP_PARAM_P_DESCRIPCION = "P_DESCRIPCION";

    // Oracle parameter names for PA_TIF_PARAMETERS.F_GET_CHAR
    public static final String SP_PARAM_PIN_PARAMETER = "PIN_PARAMETER";
    public static final String SP_PARAM_POUT_CHAR_VALUE = "POUT_CHAR_VALUE";
    public static final String SP_PARAM_POUT_ERR_MSG = "POUT_ERR_MSG";

    // Oracle stored procedure names
    public static final String SP_F_VALIDA_TRAMITE = "F_VALIDA_TRAMITE";
    public static final String SP_PRESUSPE = "PRESUSPE";
    public static final String SP_LEVANTA_PRESUSP = "LEVANTA_PRESUSP";
    public static final String SP_PACKAGE_PA_TIF_PARAMETERS = "PA_TIF_PARAMETERS";
    public static final String SP_F_GET_CHAR = "F_GET_CHAR";

    // Oracle result key
    public static final String SP_RESULT_KEY = "RESULT";

    // Parameter codes
    public static final String PARAM_IPTV23 = "IPTV23";
    public static final String PARAM_IPTV24 = "IPTV24";
    public static final String PARAM_PNTLF = "PNTLF";

    // Forbidden reasons for IF/TF/IPTV
    public static final String REASON_PEDBAJ = "PEDBAJ";
    public static final String REASON_PBCOR = "PBCOR";

    // Default ACT_ID for LEVANTA_PRESUSP
    public static final String DEFAULT_ACT_ID_CMBUD = "CMBUD";
    public static final String LEVANTA_PRESUSP_DESCRIPCION = "Se levanta la presuspension por CALLR";

    // Pending flag values from F_VALIDA_TRAMITE
    public static final String PENDIENTE_YES = "Y";
    public static final String PENDIENTE_NO = "N";

    // Header names
    public static final String HEADER_CORRELATION_ID = "X-Correlation-ID";
    public static final String HEADER_USER = "user";
    public static final String HEADER_COUNTRY = "country";
    public static final String HEADER_CHANNEL = "channel";

    // Module names for exceptions
    public static final String MODULE_CONTROLLER = "Controller";
    public static final String MODULE_SERVICE = "Service";
    public static final String MODULE_REPOSITORY = "Repository";
    public static final String MODULE_VALIDATE_DOMESTIC_USE = "ValidateDomesticUseService";
    public static final String MODULE_GET_FIXED_LINES = "GetFixedLinesInAddressService";
    public static final String MODULE_VALIDATE_TRAMITES = "ValidatePendingTramitesService";
    public static final String MODULE_VALIDATE_REASONS = "ValidateDomesticUseReasonsService";
    public static final String MODULE_SAVE_DOMESTIC_USE = "SaveDomesticUseChangeService";
    public static final String MODULE_PARAMETER_REPO = "ParameterRepository";
    public static final String MODULE_VALIDA_TRAMITE_REPO = "ValidateTramiteRepository";
    public static final String MODULE_PRESUSPE_REPO = "PresuspeRepository";
    public static final String MODULE_LEVANTA_PRESUSP_REPO = "LevantaPresuspRepository";

    public static final String RESULT_NOT_OK = "FAILED";
    public static final String RESULT_SUCCESS = "SUCCESS";
}
