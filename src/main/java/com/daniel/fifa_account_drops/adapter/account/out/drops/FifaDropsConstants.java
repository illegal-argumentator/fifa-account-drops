package com.daniel.fifa_account_drops.adapter.account.out.drops;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class FifaDropsConstants {

    static final String BASE_URL = "https://tickets.la28.org/";

    static final String LOG_IN_BUTTON = "[data-qa=\"register-login\"]";
    static final String QUEUE_BUTTON = "button.botdetect-button.btn";
    static final String LOGIN_BUTTON = "input[value=\"Log In\"]";
    static final String SAVE_BUTTON = "button[data-qa=\"save-data-button\"]";

    static final String EMAIL_INPUT = "input[placeholder=\"Email *\"]";
    static final String PASSWORD_INPUT = "input[placeholder=\"Password *\"]";

    static final String DROPS_CHECKBOX = "label:has-text(\"Visa Presale Draw\")";
    static final String DROPS_CHECKED = "input[data-qa=\"option\"]#lottery-55";

    static final String SUCCESS_TITLE = "h2[data-qa=\"page-headline\"]";
}
