package com.marbor.social.app.e2e;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by marcin on 09.07.17.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses(
        {
                UserApiBasicScenarioE2ETest.class,
                SubscriptionsE2ETest.class,
                AddingTweetsE2ETest.class,
                GettingTweetsE2ETest.class

        })
public class E2ETestsSuite
{
}
