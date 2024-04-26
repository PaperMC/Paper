package org.bukkit.support.condition;

import com.mojang.authlib.yggdrasil.YggdrasilEnvironment;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

public class EnableIfMojangServerAvailableCondition implements ExecutionCondition {

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext extensionContext) {
        HttpURLConnection url = null;
        try {
            url = (HttpURLConnection) new URI(YggdrasilEnvironment.PROD.getEnvironment().servicesHost()).toURL().openConnection();
            url.setConnectTimeout((int) TimeUnit.SECONDS.toMillis(1));
            url.connect();

            return ConditionEvaluationResult.enabled("Mojang server available");
        } catch (Exception e) {
            return ConditionEvaluationResult.disabled(e.getMessage());
        } finally {
            if (url != null) {
                url.disconnect();
            }
        }
    }
}
