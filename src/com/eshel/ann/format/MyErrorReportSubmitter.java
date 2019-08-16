package com.eshel.ann.format;

import com.eshel.core.util.Log;
import com.intellij.openapi.diagnostic.ErrorReportSubmitter;
import com.intellij.openapi.diagnostic.IdeaLoggingEvent;
import com.intellij.openapi.diagnostic.SubmittedReportInfo;
import com.intellij.util.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * Created by EshelGuo on 2019/8/16.
 */
public class MyErrorReportSubmitter extends ErrorReportSubmitter{
    public static final String TAG = MyErrorReportSubmitter.class.getSimpleName();
    @NotNull
    @Override
    public String getReportActionText() {
        Log.e(TAG, "...");
        return "上报这个异常";
    }

    @Override
    public boolean submit(@NotNull IdeaLoggingEvent[] events, @Nullable String additionalInfo, @NotNull Component parentComponent, @NotNull Consumer<SubmittedReportInfo> consumer) {
        for (IdeaLoggingEvent event : events) {
            Log.printError(event.getThrowable());
        }
        Log.i(TAG,"additionalInfo: "+ additionalInfo);
        consumer.consume(new SubmittedReportInfo(SubmittedReportInfo.SubmissionStatus.NEW_ISSUE));
        return true;
//        return super.submit(events, additionalInfo, parentComponent, consumer);
    }

}
