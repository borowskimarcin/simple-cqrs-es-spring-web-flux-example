package com.marbor.social.app.e2e;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by marcin on 09.07.17.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses(
        {
                UserApiBasicScenarioE2EIT.class,
                SubscriptionsE2EIT.class,
                AddingTweetsE2EIT.class,
                GettingTweetsE2EIT.class

        })
public class E2ETestsSuite
{
}
