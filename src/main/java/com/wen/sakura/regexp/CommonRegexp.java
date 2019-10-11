package com.wen.sakura.regexp;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author huwenwen
 * @date 2019/10/9
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonRegexp {
    /**
     * 身份证
     */
    private static final String ID_CARD = "[0-9]{15}|[0-9]{18}|[0-9]{14}X|[0-9]{17}X";
    /**
     * 银行卡
     */
    private static final String BANK_CARD = "[0-9]{13,19}";

    /**
     * 手机或者固话
     */
    private static final String PHONE_TEL = "[0-9]{3,4}[-]?[0-9]{7,8}";

    /**
     * email
     */
    private static final String EMAIL_ADDRESS = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

    /**
     * ip
     */
    private static final String IPV4 = "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$";
    private static final String IPV6_STD = "^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$";
    private static final String IPV6_HEX_COMPRESSED = "^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$";

}
