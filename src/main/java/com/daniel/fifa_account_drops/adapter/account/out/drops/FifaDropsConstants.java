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
    static final String DROPS_CHECKED = "input[data-qa='option'].ng-touched.ng-dirty";

    static final String SUCCESS_TITLE = "h2[data-qa=\"page-headline\"]";

    static final String RESTRICTED_H1 = "h1:has-text('We are sorry, your access has been restricted')";
    static final String ACCESS_DENIED_H1 = "h1:has-text('Access Denied')";

    static final String INVALID_CREDS_DIV = "div:has-text('Invalid email or password.')";

    static final String DEFAULT_CAPTCHA_ID = "#recaptcha-anchor-label";
    static final String DEFAULT_CAPTCHA_SOLVER_DIV = "div[class=\\\"capsolver-solver-info\\\"";
    static final String DEFAULT_CAPTCHA_SOLVING_TEXT = "Solving...";
    static final String NUMBERS_CAPTCHA_IMG = "img.captcha-code";

}
